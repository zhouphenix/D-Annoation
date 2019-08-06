package com.phenix.test;


import com.phenix.ExampleAnn;
import com.phenix.InstanceFactory;

public class Main {


    public static void main(String[] args){
        System.out.println("Hello world!");

        try {
            TestAnn testAnn = (TestAnn) InstanceFactory.create(TestAnn.class);
            testAnn.sayHello("phenix");
            ExampleAnn exampleAnn = new ExampleAnn();
            System.out.println(exampleAnn);
            ExampleAnn.main(new String[]{"123"});
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }


    }


}
