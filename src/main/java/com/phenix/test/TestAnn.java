package com.phenix.test;

import com.phenix.ann.apt.ExampleAnn;
import com.phenix.ann.apt.InstanceAnn;

//@InstanceAnn
@ExampleAnn
public class TestAnn {

    public void sayHello(String s){
        System.out.println("Hello " +  s);
    }
}
