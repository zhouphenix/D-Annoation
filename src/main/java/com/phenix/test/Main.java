package com.phenix.test;


import com.phenix.InstanceFactory;
import com.phenix.ann.apt.InstanceAnn;
import com.phenix.ann.apt.InstanceAnns;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

//@com.phenix.ann.apt.ExampleAnn
@InstanceAnns({
        @InstanceAnn("java.lang.String"),
        @InstanceAnn({"java.lang.Object", "java.lang.String"})
})
public class Main {

    public Main(String s) {

    }

    public Main(Object o, String s) {
    }

    public static void main(String[] args){
        System.out.println("Hello world!");

        try {
            TestAnn testAnn = InstanceFactory.create(TestAnn.class, "123");
            testAnn.sayHello("phenix");

//            ExampleAnn exampleAnn = new ExampleAnn();
//            System.out.println(exampleAnn);
//            ExampleAnn.main(new String[]{"123"});
//            ExampleAnn.method("SM", "X", "招军买马");
            Constructor<?>[] cs = TestAnn.class.getDeclaredConstructors();
            Object oo = new Object();
            new TestAnn(Object.class.cast(oo));
            for (int i = 0; i < cs.length; i++) {
                System.out.println(i + ",, " + Arrays.toString(cs[i].getParameterTypes()));
            }

            SubTestAnn<String> SubTestAnn = new SubTestAnn<>("enen");

        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }


}
