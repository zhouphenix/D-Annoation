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

    // @ExecuteTimeAspect 修饰的类、接口的Join Point
    @Pointcut("within(@com.phenix.ann.aspect.ExecuteTimeAspect *)")
    public void withinAnnotatedClass() {
    }

    //synthetic 是内部类编译后添加的修饰语， 所以!synthetic 表示非内部类
    //@ExecuteTimeAspect 修饰的类、接口中的方法（不包括内部类中的方法）
    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methdoInsideAnnotatedType() {
    }

    //@ExecuteTimeAspect 修饰的类的构造方法（不包括内部类中的构造方法）
    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {
    }

    //@ExecuteTimeAspect 修饰的方法 或者 @ExecuteTimeAspect 修饰的类、接口中的方法（不包括内部类中的方法）
    @Pointcut("execution(@com.phenix.ann.aspect.ExecuteTimeAspect * *(..)) || methdoInsideAnnotatedType()")//方法切入点
    public void methodAnnotated() {
    }

    //@ExecuteTimeAspect 修饰的构造方法 或者@ExecuteTimeAspect 修饰的类的构造方法（不包括内部类中的构造方法）
    @Pointcut("execution(@com.phenix.ann.aspect.ExecuteTimeAspect *.new(..)) || constructorInsideAnnotatedType()")
//构造器切入点
    public void constructorAnnotated() {
    }

    @Around("methodAnnotated() || constructorAnnotated()")//在连接点进行方法替换
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();//执行原方法
        // 打印时间差

        logger.info("\n" + "☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯☯" + "\n" +
                //com.phenix.test.Main
                signature.getDeclaringTypeName() +
                "." +
                methodName +
                "(" +
                Arrays.toString(joinPoint.getArgs()) +
                ")" +
                "\nat:" +
                joinPoint.getSourceLocation() +
                "\n执行时间 ∆t[" +
                TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) + "ms]" +
                "\n✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄✄");

        return result;
    }


}
