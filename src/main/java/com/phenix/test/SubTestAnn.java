package com.phenix.test;

import com.phenix.ann.apt.InstanceAnn;
import com.phenix.ann.apt.InstanceAnns;
import com.phenix.ann.aspect.MemoryCacheAspect;

/**
 * @author ：zhouphenix
 * @date ：Created in 2019/8/9 17:59
 * @description：${description}
 * @modified By：TODO
 * @version: $version$
 */
@InstanceAnns({
        @InstanceAnn("java.lang.Object"),
        @InstanceAnn({"java.lang.Object", "java.lang.String"})
})
public class SubTestAnn<T> extends TestAnn {

    public SubTestAnn(T o) {
        super(o);
    }

    public SubTestAnn(Object o, String s) {
        super(o, s);
    }

    @MemoryCacheAspect
    public void println(String s) {
        System.out.println("++++++++++" + s);
    }
}
