package com.smart.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.common.VO.PageResultVO;
import com.smart.common.result.Result;
import com.smart.user.domain.dto.LoginUserDTO;
import com.smart.user.domain.dto.RegisterUserDTO;
import com.smart.user.domain.dto.UpdateUserInfoDTO;
import com.smart.user.domain.dto.UserPageDTO;
import com.smart.user.domain.vo.UserInfoVo;
import com.smart.user.entity.SysUser;
import com.smart.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户服务", description = "用户服务接口")
public class UserController {

    private final UserService userService;

    @PostMapping("/register/template")
    @Operation(summary = "用户注册-方案一：RedisTemplate 手动锁", description = "用户注册-方案一：RedisTemplate 手动锁")
    public Result<Void> registerTemplate(@RequestBody RegisterUserDTO dto) {
        return userService.registerWithTemplate(dto);
    }

    @PostMapping("/register/redisson")
    @Operation(summary = "用户注册-方案二：Redisson 自动锁", description = "用户注册-方案二：Redisson 自动锁")
    public Result<Void> registerRedisson(@RequestBody RegisterUserDTO dto) {
        return userService.registerWithRedisson(dto);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录")
    public Result<String> login(@RequestBody LoginUserDTO dto) {
        return userService.login(dto);
    }

    @GetMapping("/getUserInfo")
    @Operation(summary = "获取当前登录用户信息", description = "获取当前登录用户信息")
    public Result<UserInfoVo> getUserInfo(){
        return userService.getUserInfo();
    }

    @PostMapping("/updateUserInfo")
    @Operation(summary = "更新当前登录用户信息(除密码)", description = "更新当前登录用户信息")
    public Result<Void> updateUserInfo(@RequestBody UpdateUserInfoDTO dto){
        return userService.updateUserInfo(dto);
    }

    @GetMapping("/getUserPageFromMybatis")
    @Operation(summary = "用户分页列表-Mybatis", description = "用户分页列表-Mybatis")
    public Result<PageResultVO<UserInfoVo>> getUserPageFromMybatis(@Valid UserPageDTO dto) {
        PageResultVO<UserInfoVo> pageData = userService.getUserPageFromMybatis(dto);
        return Result.success(pageData);
    }

    @GetMapping("/getUserPageFromMybatisPlus")
    @Operation(summary = "用户分页列表-MybatisPlus", description = "用户分页列表-MybatisPlus")
    public Result<Page<UserInfoVo>> getUserPageFromMybatisPlus(@Valid UserPageDTO dto) {
        Page<UserInfoVo> pageData = userService.getUserPageFromMybatisPlus(dto);
        return Result.success(pageData);
    }
}
