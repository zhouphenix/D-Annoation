package com.phenix.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author ：zhouphenix
 * @date ：Created in 2019/8/15 14:38
 * @description： 执行时间切片Aspect
 * @modified By：zhouphenix
 * @version: V1.0
 */
@Aspect
public class ExecuteTimeAspect {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Pointcut("execution(@com.phenix.ann.aspect.ExecuteTimeAspect * *(..))")//方法切入点
    public void methodAnnotated() {
    }

    @Pointcut("execution(@com.phenix.ann.aspect.ExecuteTimeAspect *.new(..))")//构造器切入点
    public void constructorAnnotated() {
    }

    @Around("methodAnnotated() || constructorAnnotated()")//在连接点进行方法替换
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();//执行原方法
        StringBuilder logSb = new StringBuilder();
        // 打印时间差
        logSb
                //com.phenix.test.Main
                .append(signature.getDeclaringTypeName())
                .append(".")
                .append(methodName)
                .append("(")
                .append(Arrays.toString(joinPoint.getArgs()))
                .append(")")
                .append("\nat:")
                .append(joinPoint.getSourceLocation())
                .append("\n执行时间△t[")
                .append(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)).append("ms]");
        logger.info(logSb.toString());

        return result;
    }


}
