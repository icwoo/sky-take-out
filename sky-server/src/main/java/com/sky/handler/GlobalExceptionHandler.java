package com.sky.handler;

import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
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
     * sql主键名字相同的异常处理
     * @param ex
     * @return
     */
/*    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //错误信息duplicate entry '哈哈' for key 'employee.idx_username'
        //ex.getMessage()用来获取错误信息
        String message=ex.getMessage();

        //如果包含”duplicate entry“这两单词就执行
        if (message.contains("duplicate entry")){
            String[] split = message.split("");//
            String name = split[2];

            //String msg=name+"已存在";//跟下面那个意思一样,应该是为了降低耦合
            String msg=name+ MessageConstant.Already_Exists;

            return Result.error(msg);
        }else {
            return  Result.error(MessageConstant.UNKNOWN_ERROR);
            //return  Result.error("未知错误");跟上面那句意思一样
        }
    }*/

}
