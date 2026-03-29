package com.smart.common.utils;

import com.smart.common.exception.BusinessException;

/**
 * 业务断言工具, 用于判断业务逻辑是否符合预期
 */
public class AssertUtil {
    // 判空
    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new BusinessException(message);
        }
    }

    // 判空字符串（去除空格）
    public static void notBlank(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new BusinessException(message);
        }
    }

    // 判布尔值
    public static void isTrue(Boolean bool, String message) {
        if (!Boolean.TRUE.equals(bool)) {
            throw new BusinessException(message);
        }
    }
}