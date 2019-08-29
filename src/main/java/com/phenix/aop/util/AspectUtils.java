package com.phenix.aop.util;

/**
 * @author ：zhouphenix
 * @date ：Created in 2019/8/15 17:53
 * @description：${description}
 * @modified By：TODO
 * @version: $version$
 */
public class AspectUtils {

    /**
     * 拼接key
     * @param methodName 方法名
     * @param args 参数列表
     * @return String
     */
    public static String buildKey(String methodName, Object... args) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(methodName);
        for (Object obj : args) {
            keyBuilder.append(":").append(obj);
        }
        return keyBuilder.toString();
    }
}
