package com.smart.user.service;


import com.smart.common.result.Result;
import com.smart.user.domain.dto.LoginUserDTO;
import com.smart.user.domain.dto.RegisterUserDTO;

/**
 * 用户服务接口
 */
public interface UserService {

    // 方案一：RedisTemplate 手动锁
    Result<Void> registerWithTemplate(RegisterUserDTO dto);



    // 方案二：Redisson 自动锁
    Result<Void> registerWithRedisson(RegisterUserDTO dto);



    // 登录 (统一用一种即可，这里演示存 Token)
    Result<String> login(LoginUserDTO dto);
}
