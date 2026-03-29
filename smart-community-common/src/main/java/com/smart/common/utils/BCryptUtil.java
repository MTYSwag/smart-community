package com.smart.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptUtil {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    // 加密
    public static String encrypt(String rawPassword){
        return ENCODER.encode(rawPassword);
    }

    // 校验
    public static boolean matches(String rawPassword,String encodedPassword){
        return ENCODER.matches(rawPassword,encodedPassword);
    }
}
