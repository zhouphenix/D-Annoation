package com.phenix.test;

import com.phenix.ann.aspect.MemoryCache;

/**
 * @author ：zhouphenix
 * @date ：Created in 2019/8/13 10:50
 * @description：${description}
 * @modified By：TODO
 * @version: $version$
 */
public class TestAspectJ {


    @MemoryCache
    public String sayHello(String s) {
        System.out.println("Hello, AspectJ!" + s);
        return "Hello " + s;
    }

    public void print(String s){
        System.out.println("################ " +  s);
    }


}
