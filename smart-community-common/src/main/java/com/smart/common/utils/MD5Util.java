package com.smart.common.utils;

import cn.hutool.crypto.digest.DigestUtil;

/**
 * MD5加密工具
 */
public class MD5Util {
    // 盐值（自定义，后续可放到配置文件）
    private static final String SALT = "ugc_2026";

    // MD5加密（密码专用）
    public static String encrypt(String str) {
        return DigestUtil.md5Hex(str + SALT);
    }
}