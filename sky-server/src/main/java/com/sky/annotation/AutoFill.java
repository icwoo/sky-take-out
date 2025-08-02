package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 自定义一个注解
 * //read 用于自动填充公共字段
 * @Author Lisheng Li
 * @Date 2025-07-21
 */
//read @target指定哪里可以用这个注解，@retention指定生命周期
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    OperationType value();
}
