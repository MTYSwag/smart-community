package com.smart.common.result;

import java.io.Serializable;

import com.smart.common.constants.ResultConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 统一返回结果类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Result<T> implements Serializable {
    // 状态码
    private Integer code;
    // 提示信息
    private String message;
    // 数据
    private T data;

    // 成功返回-无数据
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(ResultConstants.SUCCESS);
        return result;
    }
    /*
     * 带提示信息的成功返回-无数据
     */
    public static <T> Result<T> success(String message) {
        Result<T> result = new Result<>();
        result.setCode(ResultConstants.SUCCESS);
        result.setMessage(message);
        return result;
    }

    // 成功返回-有数据
    public static <T> Result<T> success(String message,T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultConstants.SUCCESS);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    // 失败返回
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    // 通用失败返回
    public static <T> Result<T> fail(String message) {
        return fail(ResultConstants.FAIL, message);
    }

}