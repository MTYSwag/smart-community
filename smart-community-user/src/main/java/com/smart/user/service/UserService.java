package com.smart.user.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.common.VO.PageResultVO;
import com.smart.common.result.Result;
import com.smart.user.domain.dto.LoginUserDTO;
import com.smart.user.domain.dto.RegisterUserDTO;
import com.smart.user.domain.dto.UpdateUserInfoDTO;
import com.smart.user.domain.dto.UserPageDTO;
import com.smart.user.domain.vo.UserInfoVo;
import com.smart.user.entity.SysUser;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 方案一：RedisTemplate 手动锁
     */
    Result<Void> registerWithTemplate(RegisterUserDTO dto);

    /**
     * 方案二：Redisson 自动锁
     */
    Result<Void> registerWithRedisson(RegisterUserDTO dto);

    /**
     * 登录 (统一用一种即可，这里演示存 Token)
     */
    Result<String> login(LoginUserDTO dto);

    /**
     * 获取用户信息
     */
    Result<UserInfoVo> getUserInfo();

    /**
     * 更新用户信息
     * @param dto
     * @return
     */
    Result<Void> updateUserInfo(UpdateUserInfoDTO dto);

    /**
     * 分页查询用户列表(传统mybatis方式)
     * @param dto
     * @return
     */
    PageResultVO<UserInfoVo> getUserPageFromMybatis(UserPageDTO dto);

    /**
     * 分页查询用户列表(mybatis-plus方式)
     * @param dto
     * @return
     */
    Page<UserInfoVo> getUserPageFromMybatisPlus(UserPageDTO dto);

}
