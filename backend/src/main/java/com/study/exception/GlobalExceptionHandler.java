package com.study.exception;

import com.study.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result handleException(Exception e) {
        log.error("程序出现错误:",e);
        return Result.error("程序出现错误,请联系管理员");
    }
    @ExceptionHandler
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("程序出现错误:", e);
        String message = e.getMessage();
        try {
            if (message != null && message.contains("Duplicate entry")) {
                String[] arr = message.split("'");
                if (arr.length >= 2) {
                    return Result.error(arr[1] + "已存在");
                }
            }
        } catch (Exception ex) {
            log.error("解析重复键异常消息失败:", ex);
        }
        return Result.error("数据重复,请检查后重试");
    }
}