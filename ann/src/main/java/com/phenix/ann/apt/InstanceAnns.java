package com.phenix.ann.apt;

import java.lang.annotation.*;

/**
 * @author ：zhouphenix
 * @date ：Created in 2019/8/8 10:04
 * @description：InstanceAnn可重复注解容器
 * @version: V1.0
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Inherited
public @interface InstanceAnns {
    InstanceAnn[] value() default {};
}
