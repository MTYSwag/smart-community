package com.smart.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 登录用户工具类
 */
public class LoginUserUtil {
    /**
     * 获取当前登录用户id
     * @return
     */
    public static Long getUserId(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String userId = request.getHeader("userId");
        return Long.valueOf(userId);
    }

}
