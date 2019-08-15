package com.phenix.ann.aspect;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存代理注解，通过aop切片的方式在编译期间织入源代码中
 *  * 功能：缓存某方法的返回值，下次执行该方法时，直接从缓存里获取。
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface MemoryCacheAspect {
}
