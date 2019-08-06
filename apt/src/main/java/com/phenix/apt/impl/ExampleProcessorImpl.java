package com.phenix.apt.impl;

import com.phenix.apt.Constants;
import com.phenix.apt.interfaces.IProcessor;
import com.squareup.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExampleProcessorImpl implements IProcessor<TypeElement> {

    private ProcessingEnvironment mProcessingEnvironment;

    @Override
    public void start(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnv) {
        mProcessingEnvironment = processingEnvironment;
        System.out.println("ExampleProcessorImpl start ");
    }

    @Override
    public void process(TypeElement typeElement) throws Exception {

    }

    @Override
    public void end() throws Exception {
        System.out.println("ExampleProcessorImpl end ");

        TypeSpec clz =
                // 添加类 ExampleAnn
                TypeSpec.classBuilder("ExampleAnn")
                        .addModifiers(Modifier.PUBLIC)
                        .addJavadoc("@author     ：zhouphenix\n" +
                                "@date       ：Created in $S\n" +
                                "@description：此方法由apt自动生成\n" +
                                "@version:     $S\n", DateFormat.getDateTimeInstance().format(new Date()), "V1.0")

                        .addField(FieldSpec.builder(int.class, "mField", Modifier.PRIVATE)
                                .addJavadoc("添加 private int mField\n")
                                .build())

                        .addMethod(constructor())

                        // 添加
                        //  public void method(final String s) {
                        //    System.out.println(s);
                        //  }
                        .addMethod(MethodSpec.methodBuilder("method")
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .addParameter(ParameterSpec.builder(String.class, "s", Modifier.FINAL).build())
                                .returns(void.class)
                                .addStatement("$T.out.println(s)", System.class)
                                .build())
                        //  添加
                        //  @Override
                        //  public String toString() {
                        //    return super.toString();
                        //  }
                        .addMethod(MethodSpec.methodBuilder("toString")
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .addStatement("return super.toString()")
                                .returns(String.class)
                                .build())

                        // 添加
                        //  public static void main(String[] args) {
                        //
                        //  }
                        .addMethod(MethodSpec.methodBuilder("main")
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .returns(void.class)
                                .addParameter(String[].class, "args")
                                // System.out.println("Hello, JavaPoet!");
                                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                                //  int total = 0;
                                //  for (int i = 0; i < 10; i++) {
                                //    total += i;
                                //  }
                                .addCode("int total = 0;\n"
                                        + "for (int i = 0; i < 10; i++) {\n"
                                        + "  total += i;\n"
                                        + "}\n")
                                //  method("Hello ExampleAnn!");
                                .addStatement("method($S)", "Hello ExampleAnn!")
                                //添加if-else if-else
                                //      long now = System.currentTimeMillis();
                                //    if (System.currentTimeMillis() < now) {
                                //      System.out.println("Time travelling, woo hoo!");
                                //    } else if (System.currentTimeMillis() == now) {
                                //      System.out.println("Time stood still!");
                                //    } else {
                                //      System.out.println("Ok, time still moving forward");
                                //    }
                                .addStatement("long now = $T.currentTimeMillis()", System.class)
                                .beginControlFlow("if ($T.currentTimeMillis() < now)", System.class)
                                .addStatement("$T.out.println($S)", System.class, "Time travelling, woo hoo!")
                                .nextControlFlow("else if ($T.currentTimeMillis() == now)", System.class)
                                .addStatement("$T.out.println($S)", System.class, "Time stood still!")
                                .nextControlFlow("else")
                                .addStatement("$T.out.println($S)", System.class, "Ok, time still moving forward")
                                .endControlFlow()
                                // try-catch
                                //    try {
                                //      throw new Exception("Failed");
                                //    } catch (Exception e) {
                                //      throw new RuntimeException(e);
                                //    }
                                .beginControlFlow("try")
                                .addStatement("throw new Exception($S)", "Failed")
                                .nextControlFlow("catch ($T e)", Exception.class)
                                .addStatement("throw new $T(e)", RuntimeException.class)
                                .endControlFlow()
                                .build())
                        .build();


//        TypeSpec clz = clazz(builtinTypeField(),          // int
//                arrayTypeField(),            // int[]
//                refTypeField(),              // File
//                typeField(),                 // T
//                parameterizedTypeField(),    // List<String>
//                wildcardTypeField(),         // List<? extends String>
//                constructor(),               // 构造函数
//                baz(),              //baz方法
//                method(code()));             // 普通方法


//        TypeSpec clz = TypeSpec.classBuilder("ExampleAnn").build();
        JavaFile jf = JavaFile.builder(Constants.PACKAGE_NAME, clz)
                .build();
        jf.writeTo(mProcessingEnvironment.getFiler());
        System.out.println(jf);
    }


    public static MethodSpec baz() {
        return MethodSpec.methodBuilder("baz")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(Runnable.class, "runnable")
                .build();
    }

    private MethodSpec computeRange(String name, int from, int to, String op) {
        return MethodSpec.methodBuilder(name)
                .returns(int.class)
                .addStatement("int result = 1")
                .beginControlFlow("for (int i = " + from + "; i < " + to + "; i++)")
                .addStatement("result = result " + op + " i")
                .endControlFlow()
                .addStatement("return result")
                .build();
    }


    /**
     * `public abstract class ExampleAnn<T> implements Serializable, Comparable<String>, Map<T, ? extends ExampleAnn> {
     * ...
     * }`
     *
     * @return
     */
    public static TypeSpec clazz(FieldSpec builtinTypeField, FieldSpec arrayTypeField, FieldSpec refTypeField,
                                 FieldSpec typeField, FieldSpec parameterizedTypeField, FieldSpec wildcardTypeField,
                                 MethodSpec constructor, MethodSpec bazMethodSpec, MethodSpec methodSpec) {
        return TypeSpec.classBuilder("ExampleAnn")
                // 限定符
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                // 泛型
                .addTypeVariable(TypeVariableName.get("T"))

                // 继承与接口
                .superclass(Object.class)
                .addSuperinterface(Serializable.class)
                .addSuperinterface(ParameterizedTypeName.get(Comparable.class, String.class))
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(Map.class),
                        TypeVariableName.get(String.class),
                        WildcardTypeName.subtypeOf(ClassName.get(Constants.PACKAGE_NAME, "ExampleAnn"))))

                // 初始化块
                .addStaticBlock(CodeBlock.builder().build())
                .addInitializerBlock(CodeBlock.builder().build())

                // 属性
                .addField(builtinTypeField)
                .addField(arrayTypeField)
                .addField(refTypeField)
                .addField(typeField)
                .addField(parameterizedTypeField)
                .addField(wildcardTypeField)

                // 方法 （构造函数也在此定义）
                .addMethod(constructor)
                .addMethod(bazMethodSpec)
                .addMethod(methodSpec)

                // 内部类
                .addType(TypeSpec.classBuilder("InnerClass").build())

                .build();
    }

    /**
     * 内置类型
     */
    public static FieldSpec builtinTypeField() {
        // private int mInt;
        return FieldSpec.builder(int.class, "mInt", Modifier.PRIVATE).build();
    }

    /**
     * 数组类型
     */
    public static FieldSpec arrayTypeField() {
        // private int[] mArr;
        return FieldSpec.builder(int[].class, "mArr", Modifier.PRIVATE).build();
    }

    /**
     * 需要导入 import 的类型
     */
    public static FieldSpec refTypeField() {
        // private File mRef;
        return FieldSpec.builder(File.class, "mRef", Modifier.PRIVATE).build();
    }

    /**
     * 泛型
     */
    public static FieldSpec typeField() {
        // private File mT;
        return FieldSpec.builder(TypeVariableName.get("T"), "mT", Modifier.PRIVATE).build();
    }

    /**
     * 参数化类型
     */
    public static FieldSpec parameterizedTypeField() {
        // private List<String> mParameterizedField;
        return FieldSpec.builder(ParameterizedTypeName.get(List.class, String.class),
                "mParameterizedField",
                Modifier.PRIVATE)
                .build();
    }

    /**
     * 通配符参数化类型
     *
     * @return
     */
    public static FieldSpec wildcardTypeField() {
        // private List<? extends String> mWildcardField;
        return FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(List.class),
                WildcardTypeName.subtypeOf(String.class)),
                "mWildcardField",
                Modifier.PRIVATE)
                .build();
    }

    /**
     * 构造函数
     */
    public MethodSpec constructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("构造方法\n")
                .build();
    }

    /**
     * `@Override
     * public <T> Integer method(String string, T t, Map<Integer, ? extends T> map) throws IOException, RuntimeException {
     * ...
     * }`
     *
     * @param codeBlock
     * @return
     */
    public static MethodSpec method(CodeBlock codeBlock) {
        return MethodSpec.methodBuilder("method")
//                .addAnnotation(Override.class)
                .addTypeVariable(TypeVariableName.get("T"))
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addParameter(String.class, "string")
                .addParameter(TypeVariableName.get("T"), "t")
                .addParameter(ParameterizedTypeName.get(ClassName.get(Map.class),
                        ClassName.get(Integer.class),
                        WildcardTypeName.subtypeOf(TypeVariableName.get("T"))),
                        "map")
                .addException(IOException.class)
                .addException(RuntimeException.class)
                .addCode(codeBlock)
                .build();
    }

    /**
     * ‘method’ 方法中的具体语句
     */
    public static CodeBlock code() {
        return CodeBlock.builder()
                .addStatement("int foo = 1")
                .addStatement("$T bar = $S", String.class, "a string")

                // Object obj = new HashMap<Integer, ? extends T>(5);
                .addStatement("$T obj = new $T(5)",
                        ParameterizedTypeName.get(ClassName.get(HashMap.class),
                                ClassName.get(Integer.class),
                                WildcardTypeName.subtypeOf(TypeVariableName.get("T"))), HashMap.class)

                // method(new Runnable(String param) {
                //   @Override
                //   void run() {
                //   }
                // });
                .addStatement("baz($L)", TypeSpec.anonymousClassBuilder("")
                        .superclass(Runnable.class)
                        .addMethod(MethodSpec.methodBuilder("run")
                                .addAnnotation(Override.class)
                                .returns(TypeName.VOID)
                                .build())
                        .build())

                // for
                .beginControlFlow("for (int i = 0; i < 5; i++)")
                .endControlFlow()

                // while
                .beginControlFlow("while (false)")
                .endControlFlow()

                // do... while
                .beginControlFlow("do")
                .endControlFlow("while (false)")

                // if... else if... else...
                .beginControlFlow("if (false)")
                .nextControlFlow("else if (false)")
                .nextControlFlow("else")
                .endControlFlow()

                // try... catch... finally
                .beginControlFlow("try")
                .nextControlFlow("catch ($T e)", Exception.class)
                .addStatement("e.printStackTrace()")
                .nextControlFlow("finally")
                .endControlFlow()

                .addStatement("return 0")
                .build();
    }
}
