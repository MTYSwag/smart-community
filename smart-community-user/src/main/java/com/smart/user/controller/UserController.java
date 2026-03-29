package com.smart.user.controller;

import com.smart.common.result.Result;
import com.smart.user.domain.dto.LoginUserDTO;
import com.smart.user.domain.dto.RegisterUserDTO;
import com.smart.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
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

}
