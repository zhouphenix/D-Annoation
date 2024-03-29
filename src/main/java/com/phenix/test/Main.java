package com.phenix.test;


import com.phenix.ExampleAnn;
import com.phenix.InstanceFactory;
import com.phenix.ann.apt.InstanceAnn;
import com.phenix.ann.apt.InstanceAnns;
import com.phenix.ann.aspect.ExecuteTimeAspect;
import com.phenix.ann.aspect.MemoryCacheAspect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@com.phenix.ann.apt.ExampleAnn
@InstanceAnns({
        @InstanceAnn("java.lang.String"),
        @InstanceAnn({"java.lang.Object", "java.lang.String"})
})
public class Main {

    @ExecuteTimeAspect
    public Main(String s) {
        System.out.println("@ExecuteTimeAspect Main " + s);
    }

    public Main(Object o, String s) {
    }


    @MemoryCacheAspect
    public void print(String s) {
        System.out.println("################ " + s);
    }

    @ExecuteTimeAspect
    public static void main(String[] args) {
        System.out.println("测试开始\n\n" +
                "                  ,;,,;\n" +
                "                 ,;;'(    马\n" +
                "       __      ,;;' ' \\   ┇\n" +
                "    /'  '\\'~~'~' \\ /'\\.)  到 \n" +
                " ,;(      )    /  |.      ┇\n" +
                ",;' \\    /-.,,(   ) \\     成\n" +
                "     ) /       ) / )|     ┇ \n" +
                "     ||        ||  \\)     功\n" +
                "     (_\\       (_\\");

        try {
            /* 测试@Instance*/
            System.err.println("+++++++++++++++++++++++++++++++++++++++++");
            System.err.println("+++++++++++++++测试 @Instance+++++++++++++");
            System.err.println("+++++++++++++++++++++++++++++++++++++++++");
            TestAnn testAnn = InstanceFactory.create(TestAnn.class, (Object) "123");
            testAnn.sayHello("phenix");

            ExampleAnn exampleAnn = new ExampleAnn();
            System.out.println(exampleAnn);
            ExampleAnn.main(new String[]{"123"});
            ExampleAnn.method("SM", "X", "招军买马");
            Constructor<?>[] cs = TestAnn.class.getDeclaredConstructors();
            Object oo = new Object();
            new TestAnn(Object.class.cast(oo));
            for (int i = 0; i < cs.length; i++) {
                System.out.println(i + ", " + Arrays.toString(cs[i].getParameterTypes()));
            }

            SubTestAnn<String> subTestAnn = new SubTestAnn<>("enen");
            subTestAnn.println("测试MemoryCache");

        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        /* 测试@MemoryCacheAspect (AspectJ)*/
        System.err.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.err.println("+++++++++++++++测试@MemoryCacheAspect (AspectJ)+++++++++++++");
        System.err.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
        try {
            TestAnn MemoryCache1 = InstanceFactory.create(TestAnn.class, (Object) "345");
            TestAnn MemoryCache2 = InstanceFactory.create(TestAnn.class, (Object) "345");
            TestAnn MemoryCache3 = InstanceFactory.create(TestAnn.class, (Object) "5678");
            MemoryCache1.sayHello("AAA");
            MemoryCache2.sayHello("BBB");
            MemoryCache3.sayHello("CCC");
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }


        TestAspectJ hello = new TestAspectJ();
        hello.sayHello("12321");
        hello.sayHello("22321");
        hello.sayHello("32321");
        hello.sayHello("12321");

        Main main = new Main("滋它");
        main.print("滋醒它");
        main.print("滋醒它2");
        main.print("滋醒它3");
    }


}
