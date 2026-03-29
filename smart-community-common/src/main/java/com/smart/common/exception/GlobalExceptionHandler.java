package com.smart.common.exception;

import com.smart.common.constants.ResultConstants;
import com.smart.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 处理所有未捕获的异常
    @ExceptionHandler(Exception.class)// 指定处理此异常的函数
    public Result<Void> handleException(Exception e) {
        log.error("全局异常捕获：", e);
        return Result.fail(ResultConstants.FAIL, "服务器内部错误，请联系管理员");
    }

    // 处理自定义业务异常（后续扩展，先留空）
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常：", e);
        return Result.fail(e.getCode(), e.getMessage());
    }
}