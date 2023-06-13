package com.coke.yangboot.common.log;

import java.lang.annotation.*;

/**
 * 公共日志注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface SysLog {
    /**
     * 功能名称
     * @return
     */
    String functionName() default "";

    /**
     * 方法名
     * @return
     */
    String methodName() default "";
}
