package com.smart.common.exception;

import com.smart.common.enums.ResultCodeEnum;
import lombok.Data;

/**
 * 自定义业务异常
 */
@Data
public class BusinessException extends RuntimeException {

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 构造方法 - 只有消息（默认状态码500）
     * @param message 异常消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = ResultCodeEnum.FAIL.getCode(); // 默认服务器错误
    }

    /**
     * 构造方法 - 包含状态码和消息
     * @param code 状态码
     * @param message 异常消息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造方法 - 包含状态码、消息和原因
     * @param code 状态码
     * @param message 异常消息
     * @param cause 原始异常
     */
    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 快速创建业务异常 - 用户名已存在
     * @return BusinessException
     */
    public static BusinessException usernameExists(String username) {
        return new BusinessException(ResultCodeEnum.USERNAME_EXIST.getCode(), "用户名" + username + "已存在");
    }

    /**
     * 快速创建业务异常 - 手机号已存在
     * @param phone
     * @return
     */
    public static BusinessException phoneExists(String phone) {
        return new BusinessException(ResultCodeEnum.PHONE_EXIST.getCode(), "手机号" + phone + "已被注册");
    }

    /**
     * 快速创建业务异常 - 邮箱已存在
     * @param email
     * @return
     */
    public static BusinessException emailExists(String email) {
        return new BusinessException(ResultCodeEnum.EMAIL_EXIST.getCode(), "邮箱" + email + "已被注册");
    }

    /**
     * 快速创建业务异常 - 用户不存在
     * @return BusinessException
     */
    public static BusinessException userNotFound() {
        return new BusinessException(ResultCodeEnum.USER_NOT_EXIST.getCode(), "用户不存在");
    }

    /**
     * 快速创建业务异常 - 密码错误
     * @return BusinessException
     */
    public static BusinessException passwordError() {
        return new BusinessException(ResultCodeEnum.PASSWORD_ERROR.getCode(), "密码错误");
    }

    /**
     * 快速创建业务异常 - 系统繁忙
     * @return BusinessException
     */
    public static BusinessException systemBusy() {
        return new BusinessException(ResultCodeEnum.FAIL.getCode(), "系统繁忙，请稍后重试");
    }


    public static BusinessException userDisabled() {
        return new BusinessException(ResultCodeEnum.FORBIDDEN.getCode(), "账号已被禁用");
    }
}