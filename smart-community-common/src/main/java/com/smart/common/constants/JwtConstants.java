package com.smart.common.constants;

/**
 * JWT常量类
 */
public class JwtConstants {
    /**
     * 密钥（32位以上），不可泄露改变
     */
    public static final String JWT_SECRET = "smartcommunity20260328mtyswagger2026";

    // 过期时间：1小时
    /**
     * 过期时间（毫秒）
     */
    public static final long JWT_EXPIRE = 1000 * 60 * 60 ;

    // 请求头token key
    public static final String TOKEN_HEADER = "token";
}

