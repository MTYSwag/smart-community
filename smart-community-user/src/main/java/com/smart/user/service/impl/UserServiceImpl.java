package com.smart.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.common.result.Result;
import com.smart.common.constants.UserConstants;
import com.smart.common.exception.BusinessException;
import com.smart.common.utils.BCryptUtil;
import com.smart.common.utils.JwtUtil;
import com.smart.user.domain.dto.LoginUserDTO;
import com.smart.user.domain.dto.RegisterUserDTO;
import com.smart.user.entity.SysUser;
import com.smart.user.mapper.SysUserMapper;
import com.smart.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    //因为是在common模块，所以不能用 @Autowired 注入，需要用构造参数注入
//    private final JwtUtil jwtUtil;
    /**
     *  注册用户-方案一：RedisTemplate 手动锁
     *  是为了测试RedisTemplate 手动锁的使用：作用：防止并发注册时，用户名重复注册
     * @param registerUserDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> registerWithTemplate(RegisterUserDTO registerUserDTO) {
        String username = registerUserDTO.getUsername();
        String phone = registerUserDTO.getPhone();
        String email = registerUserDTO.getEmail();
        //生成redis 锁key 键
        String lockKey = UserConstants.USER_REGISTER_LOCK_PREFIX + username;
        // 锁值（线程名:当前时间戳）
        String lockValue = Thread.currentThread().getName() + ":" + System.currentTimeMillis();
        // 1. 尝试加锁 (SETNX + 过期时间)
        //这是redis的SETNX命令，如果key不存在，才设置值，否则返回false
        Boolean isLocked = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 10, TimeUnit.SECONDS);

        if (Boolean.FALSE.equals(isLocked)) {
            throw BusinessException.systemBusy();
        }
        try {
            // 2. 检查用户唯一性
            checkUserUniqueness(username, phone, email);
            // 3. 模拟业务耗时 (可选，用于测试锁过期)
            // Thread.sleep(12000);
            // 4. 如果用户名不存在，才密码进行加密 & 保存
            SysUser user = new SysUser()
                    .setUsername(username)
                    // 密码加密
                    .setPassword((BCryptUtil.encrypt(registerUserDTO.getPassword())))
                    .setNickname(registerUserDTO.getNickname())
                    .setStatus(1)
                    .setFollowCount(0)
                    .setFansCount(0)
                    .setPhone(registerUserDTO.getPhone())
                    .setCreateTime(LocalDateTime.now())
                    .setDeleted(0);

            this.save(user);
            return Result.success("注册成功");
        } finally {
            // 5. 释放锁 (高危：需判断是否是自己的锁，防止误删)
            // 简单实现：直接删除。生产环境需用 Lua 脚本比对 lockValue
            Object currentValue = redisTemplate.opsForValue().get(lockKey);
            if (currentValue != null && currentValue.toString().equals(lockValue)) {
                redisTemplate.delete(lockKey);
            }
        }
    }

    /**
     *  注册用户-方案二：Redisson 自动锁
     * @param dto
     * @return
     */
    @Override
    public Result<Void> registerWithRedisson(RegisterUserDTO dto) {
        String username = dto.getUsername();
        String phone = dto.getPhone();
        String email = dto.getEmail();
        //锁的唯一标识符就是redis的键，这里用用户名作为锁的键
        String lockKey = UserConstants.USER_REGISTER_LOCK_PREFIX + username;
        // 获取锁对象，这是一个分布式锁
        RLock lock = redissonClient.getLock(lockKey);//这是在Redisson中获取锁对象
        //isLocked的是判断获取锁成功与否的标志位
        boolean isLocked = false;
        try {
            // tryLock(等待时间, 租赁时间, 单位)
            // 租赁时间为 -1 时，开启看门狗 (Watchdog)，默认 30s 续期
            //tryLock(等待时间(尝试获取锁的最大等待时间), 租期时间(锁的持有时间，-1表示启用看门狗), 时间单位)
            isLocked = lock.tryLock(5, -1, TimeUnit.SECONDS);// 等待 5s，租期 30s -1 表示无限期
            if (!isLocked) {
                throw BusinessException.systemBusy();
            }
            // 检查用户唯一性
            checkUserUniqueness(username, phone, email);
            // 模拟长耗时业务 (即使超过 30s，锁也不会失效，因为看门狗在续期)
            // Thread.sleep(35000);
            SysUser sysUser = new SysUser()
                    .setUsername(username)
                    .setPassword(BCryptUtil.encrypt(dto.getPassword()))// 密码加密
                    .setNickname(dto.getNickname())
                    .setStatus(1)
                    .setFollowCount(0)
                    .setFansCount(0)
                    .setPhone(dto.getPhone())
                    .setCreateTime(LocalDateTime.now())
                    .setEmail(dto.getEmail())
                    .setDeleted(0);
            // 保存用户
            this.save(sysUser);
            return Result.success("注册成功");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException("系统异常");
        } finally {
            // 安全释放：只有当前线程持有锁才释放；lock.isHeldByCurrentThread() 方法判断是否是当前线程持有锁，防止其他线程释放自己的锁
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 检查用户唯一性
     */
    private void checkUserUniqueness(String username, String phone,String email) {
        SysUser existUser = this.lambdaQuery()
                .eq(SysUser::getUsername, username)
                .or()
                .eq(SysUser::getPhone, phone)
                .or()
                .eq(SysUser::getEmail, email)
                .eq(SysUser::getDeleted, 0)
                .one();

        if (existUser != null) {
            if (username.equals(existUser.getUsername())) {
                throw BusinessException.usernameExists(username);
            }
            if (phone != null && phone.equals(existUser.getPhone())) {
                throw BusinessException.phoneExists(phone);
            }
            if (email != null && email.equals(existUser.getEmail())) {
                throw BusinessException.emailExists(email);
            }
        }
    }
    /**
     *  登录用户
     * @param dto
     * @return
     */
    @Override
    public Result<String> login(LoginUserDTO dto) {
        // 检查用户是否存在
        SysUser user = isLoginUserExist(dto);
        // 生成 Token
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",user.getId());
        claims.put("username",user.getUsername());
        String token = JwtUtil.createToken(claims);
        // 【缓存场景】将 Token 存入 Redis，用于后续校验或单点登录控制
        String redisKey = "user:token:" + user.getId();
        redisTemplate.opsForValue().set(redisKey, token, 2, TimeUnit.HOURS);

        return Result.success(token);

    }

    /**
     * 检查登录用户是否存在
     * @param dto
     * @return
     */
    private SysUser isLoginUserExist(LoginUserDTO dto) {
        SysUser user = this.lambdaQuery()
                .eq(SysUser::getUsername, dto.getUsername())
                .or()
                .eq(SysUser::getPhone, dto.getPhone())
                .eq(SysUser::getDeleted, 0)
                .one();

        if (user == null) {
            throw BusinessException.userNotFound();
        }
        if (user.getStatus() == 0) {
            throw BusinessException.userDisabled();
        }
        if (!BCryptUtil.matches(dto.getPassword(), user.getPassword())) {
            throw BusinessException.passwordError();
        }
        return user;
    }

}
