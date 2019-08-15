package com.phenix.ann.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ：zhouphenix
 * @date ：Created in 2019/8/15 14:35
 * @description：执行时间
 * @version: $version$
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface ExecuteTimeAspect {
}
