package com.phenix.test;

import com.phenix.ann.apt.InstanceAnn;

import java.util.Arrays;

@InstanceAnn
public class TestAnn {

    public TestAnn() {
    }
    public TestAnn(Object o){

    }

    public TestAnn(Object o, String s){

    }

    public void sayHello(String s) {
        System.out.println("Hello " + s);
        Object[] arr = new Object[3];
        arr[0] = new Object();
        arr[1] = "123";
        arr[2] = 2;
        System.out.println(" ==== " + Arrays.toString(getArrayElementClass(arr)));
    }


    private static Class[] getArrayElementClass(Object[] arr){
        Class[] rst = new Class[arr.length];
        for (int i = 0; i < arr.length; i++) {
            rst[i] = arr[i].getClass();
            System.out.println(rst[i]);
        }
        return rst;
    }


}
