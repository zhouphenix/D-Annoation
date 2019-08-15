package com.phenix.aop;

import com.phenix.aop.util.AspectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


/**
 * 根据MemoryCache注解自动添加缓存代理代码，通过aop切片的方式在编译期间织入源代码中
 * 功能：缓存某方法的返回值，下次执行该方法时，直接从缓存里获取。
 */
@Aspect
public class MemoryCacheAspect {
    @Pointcut("execution(@com.phenix.ann.aspect.MemoryCacheAspect * *(..))")//方法切入点
    public void methodAnnotated() {
    }

    @Around("methodAnnotated()")//在连接点进行方法替换
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        CacheManager mMemoryCacheManager = CacheManager.getInstance();
        String key = AspectUtils.buildKey(methodName, joinPoint.getArgs());
        Object result = mMemoryCacheManager.get(key);//key规则 ： 方法名＋参数1+参数2+...
        //缓存已有，直接返回
        if (result != null) {
            System.out.println("(key：value) = " + key + "-->" + result);
            return result;
        }
        //执行原方法
        result = joinPoint.proceed();
        //对象不为空
        if (result != null) {
            mMemoryCacheManager.add(key, result);//存入缓存
            System.out.println("key：" + key + "--->" + "save");
        }

        return result;
    }
}
