package com.phenix.aop;

/**
 * @author     ：zhouphenix
 * @date       ：Created in 2019/8/13 10:49
 * @description：${description}
 * @modified By：TODO
 * @version: $version$
 */
public aspect TestAspect {


    //捕获特定类中所有连接点
    pointcut withinMyClass(): within(TestAspectJ);

    before(): withinMyClass() {
//        System.out.println("> before():withinMyClass");
//        System.out.println(thisJoinPoint.getSignature());
//        System.out.println(thisJoinPoint.getSourceLocation());
    }

    //捕获特定包中所有连接点
    pointcut withinPackage(): within(com.phenix.aop.*);

    //该aspect也在package com.phenix.aop下，因此exclude掉
    before(): withinPackage() && !within(TestAspect){
//        System.out.println("> before():withinPackage");
//        System.out.println(thisJoinPoint.getSignature());
//        System.out.println(thisJoinPoint.getSourceLocation());
    }

    //捕获特定方法内所有连接点
    pointcut withinMethod(): withincode(* TestAspectJ.sayHello(..));

    before():withinMethod() {
        System.out.println("> before():withinMethod");
        System.out.println(thisJoinPoint.getSignature());
        System.out.println(thisJoinPoint.getSourceLocation());
    }

    //=======================================================================


    //在其他module下不执行：String around():call(public String *.sayHello(..))
    String around():execution(public String *.sayHello(..)){
        System.out.println("开始事务execution...");
        String result = proceed();
        System.out.println("事务结束execution..." + result);
        return result;
    }

    //在其他module下不执行：String around():call(public String *.sayHello(..))
    void around():execution(public void *.print(..)){
        System.out.println("开始事务2 execution...");
        proceed();
        System.out.println("事务结束2 execution...");
    }

}
