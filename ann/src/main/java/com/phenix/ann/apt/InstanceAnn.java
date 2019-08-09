package com.phenix.ann.apt;

import java.lang.annotation.*;

/**
 * 实例化注解,会被主动添加到实例化工厂,自动生成new来替换掉反射的newInstance代码
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Repeatable(InstanceAnns.class)
public @interface InstanceAnn {

    String[] value() default {};
}
