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
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理SQL异常
     * @param ex
     * @return
     */

    @ExceptionHandler
    public  Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //拿到异常的字符串
        String message = ex.getMessage();
        //判断在字符串是否存在Duplicate entry字段
        if (message != null && message.contains("Duplicate entry")){
            //字符串里都是空格隔开，拿到每个单独字符串，放到数组
            String[] split = message.split(" ");
            //拿到用户存在的名字
            String username = split[2];
            //名字与常量（已存在）拼接
            String arg=username+ MessageConstant.ALREADY_EXISTS;
            //最后返回异常打印
            return Result.error(arg);
        }else {
            //返回异常打印未知
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }

}
