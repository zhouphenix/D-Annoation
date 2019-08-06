package com.phenix.test;


import com.phenix.InstanceFactory;

public class Main {


    public static void main(String[] args){
        System.out.println("Hello world!");

        try {
            TestAnn testAnn = (TestAnn) InstanceFactory.create(TestAnn.class);
            testAnn.sayHello("phenix");
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }



    }


}
