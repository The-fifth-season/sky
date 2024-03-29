package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice           //全局处理异常的注解，用于处理Restful接口抛出的异常，以json格式返回错误信息
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return 错误信息
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }
    @ExceptionHandler
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){            //捕获数据库报错异常
        String message = ex.getMessage();               //获取错误信息
        String msg;
        //错误信息示例：Duplicate entry 'yjl' for key 'employee.idx_username（数据库中yjl用户名（不允许重复）已经存在）
        if (message.contains("Duplicate entry")) {
            String s = message.split(" ")[2];
            String s1 = s.replaceAll("'", "");
            msg = s1 + MessageConstant.HAD_FOUND;
        }else {
            msg = MessageConstant.UNKNOWN_ERROR;
        }
        return Result.error(msg);
    }
}
