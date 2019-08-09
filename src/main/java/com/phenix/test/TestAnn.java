package com.phenix.test;

import com.phenix.ann.apt.InstanceAnn;
import com.phenix.ann.apt.InstanceAnns;

import java.util.Arrays;

//@InstanceAnn
//@InstanceAnn("java.lang.Object")
//@InstanceAnn({"java.lang.Object", "java.lang.String"})
@Deprecated
@InstanceAnns({
        @InstanceAnn,
        @InstanceAnn("java.lang.Object"),
        @InstanceAnn({"java.lang.Object", "java.lang.String"})
})
public class TestAnn {

    public TestAnn() {
        System.out.println(this);
    }

    public TestAnn(Object o) {
        System.out.println(this + "," + o);
    }

    public TestAnn(Object o, String s) {
        System.out.println(this + "," + o + ", " + s);
    }

    private static Class[] getArrayElementClass(Object[] arr) {
        Class[] rst = new Class[arr.length];
        for (int i = 0; i < arr.length; i++) {
            rst[i] = arr[i].getClass();
            System.out.println(rst[i]);
        }
        return rst;
    }

    public void sayHello(String s) {
        System.out.println("Hello " + s);
        Object[] arr = new Object[3];
        arr[0] = new Object();
        arr[1] = "123";
        arr[2] = 2;
        System.out.println(" ==== " + Arrays.toString(getArrayElementClass(arr)));

    }


}
