# D-Annoation
AOP {APT,AspectJ,Javassist}

环境：idea + java + gradle

[TOC]

### 一、实践篇--APT
注解代码生成

build.gradle中添加依赖：
```
    implementation project(':ann')
    implementation project(':aop')
    annotationProcessor project(':apt')
```

使用方式：


```
@InstanceAnns({
        @InstanceAnn,
        @InstanceAnn("java.lang.Object"),
        @InstanceAnn({"java.lang.Object", "java.lang.String"})
})
public class TestAnn 

```

其中InstanceAnn value值： 表示构造方法参数类型

使用 `TestAnn testAnn = InstanceFactory.create(TestAnn.class, "123");`

生成代码
```java

/**
 * @ 实例化工厂 此类由apt自动生成
 */
public final class InstanceFactory {
  /**
   * @此方法由apt自动生成
   */
  public static <T> T create(final Class<T> mClass) throws IllegalAccessException,
      InstantiationException, NoSuchMethodException, InvocationTargetException {
     switch (mClass.getName())  {
      case "com.phenix.test.TestAnn": 
        return  (T)new TestAnn();
      default: return mClass.getConstructor().newInstance();
    }
  }

  /**
   * @此方法由apt自动生成
   */
  public static <T> T create(final Class<T> mClass, Object p0, String p1) throws
      IllegalAccessException, InstantiationException, NoSuchMethodException,
      InvocationTargetException {
     switch (mClass.getName())  {
      case "com.phenix.test.SubTestAnn": 
        return  (T)new SubTestAnn(p0,p1);
      case "com.phenix.test.Main": 
        return  (T)new Main(p0,p1);
      case "com.phenix.test.TestAnn": 
        return  (T)new TestAnn(p0,p1);
      default: return mClass.getConstructor(java.lang.Object.class,java.lang.String.class).newInstance(p0,p1);
    }
  }

  /**
   * @此方法由apt自动生成
   */
  public static <T> T create(final Class<T> mClass, Object p0) throws IllegalAccessException,
      InstantiationException, NoSuchMethodException, InvocationTargetException {
     switch (mClass.getName())  {
      case "com.phenix.test.SubTestAnn": 
        return  (T)new SubTestAnn(p0);
      case "com.phenix.test.TestAnn": 
        return  (T)new TestAnn(p0);
      default: return mClass.getConstructor(java.lang.Object.class).newInstance(p0);
    }
  }

  /**
   * @此方法由apt自动生成
   */
  public static <T> T create(final Class<T> mClass, String p0) throws IllegalAccessException,
      InstantiationException, NoSuchMethodException, InvocationTargetException {
     switch (mClass.getName())  {
      case "com.phenix.test.Main": 
        return  (T)new Main(p0);
      default: return mClass.getConstructor(java.lang.String.class).newInstance(p0);
    }
  }
}
```
【注意】
* 1.注解可继承，但子类InstanceAnns需要比父类范围大，否则不会区别差异，除非重新注解
* 2.泛型 明显不适合改注解，除非添加所有支持的构造类型