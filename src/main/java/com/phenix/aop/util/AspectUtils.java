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
        keyBuilder.append(methodName).append(":");
        for (Object obj : args) {
            if (obj instanceof String) keyBuilder.append((String) obj);
            else if (obj instanceof Class) keyBuilder.append(((Class) obj).getSimpleName());
        }
        return keyBuilder.toString();
    }
}
