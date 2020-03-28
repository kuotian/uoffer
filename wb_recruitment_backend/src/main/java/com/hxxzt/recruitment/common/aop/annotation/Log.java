package com.hxxzt.recruitment.common.aop.annotation;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 *
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块
     */
    String module() default "";

    /**
     * 操作内容
     */
    public String operation() default "";
}
