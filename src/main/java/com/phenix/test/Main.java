package com.phenix.test;


import com.phenix.InstanceFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

//@com.phenix.ann.apt.ExampleAnn
public class Main {


    public static void main(String[] args){
        System.out.println("Hello world!");

        try {
            TestAnn testAnn = InstanceFactory.create(TestAnn.class, "8");
            testAnn.sayHello("phenix");

//            ExampleAnn exampleAnn = new ExampleAnn();
//            System.out.println(exampleAnn);
//            ExampleAnn.main(new String[]{"123"});
//            ExampleAnn.method("SM", "X", "招军买马");
            Constructor<?>[] cs = TestAnn.class.getDeclaredConstructors();
            for (int i = 0; i < cs.length; i++) {
                System.out.println(i + ",, " + Arrays.toString(cs[i].getParameterTypes()));
            }

        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }


}
