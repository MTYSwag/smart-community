package com.smart.common.exception;

import com.smart.common.constants.ResultConstants;
import com.smart.common.enums.ResultCodeEnum;
import com.smart.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 处理所有未捕获的异常(全局兜底)
    @ExceptionHandler(Exception.class)// 指定处理此异常的函数
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        log.error("全局异常捕获：", e);
        return Result.fail(ResultConstants.FAIL, "服务器内部错误，请联系管理员");
    }

    // 处理自定义业务异常（后续扩展，先留空）
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常：", e);
        return Result.fail(e.getCode(), e.getMessage());
    }

    // 参数校验异常
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> validException(BindException e){
        String msg = e.getMessage();
        return Result.fail(ResultCodeEnum.PARAM_ERROR.getCode(),msg);
    }

}