package com.smart.common.enums;

import lombok.Getter;

/**
 * 统一返回结果枚举类
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"操作成功"),
    UNAUTHORIZED(401,"未登录或token失效"),
    FORBIDDEN(403,"权限不足"),
    PARAM_ERROR(400,"参数非法"),
    USER_NOT_EXIST(1001,"用户不存在"),
    PASSWORD_ERROR(1002,"密码错误"),
    USERNAME_EXIST(1003,"用户名已存在"),
    PHONE_EXIST(1004,"手机号已存在"),
    EMAIL_EXIST(1005,"邮箱已存在"),
    FAIL(500,"系统异常");


    private Integer code;
    private String msg;

    ResultCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
