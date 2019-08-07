package com.phenix.apt.impl;

import com.phenix.apt.Constants;
import com.phenix.apt.interfaces.IProcessor;
import com.squareup.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.text.DateFormat;
import java.util.*;

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
                                "@date       ：Created in $L\n" +
                                "@description：此方法由apt自动生成\n" +
                                "@version    : $L\n", DateFormat.getDateTimeInstance().format(new Date()), "V1.0")

                        // 添加泛型支持
                        .addTypeVariable(TypeVariableName.get("V"))
                        // 添加泛型成员 V mV
                        .addField(FieldSpec.builder(TypeVariableName.get("V"), "mV", Modifier.PRIVATE).build())
                        // 添加泛型成员 List<V> mListV
                        .addField(FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(List.class), TypeVariableName.get("V")),
                                "mListV",
                                Modifier.PRIVATE)
                                .build())

                        // 添加 private int mField
                        .addField(FieldSpec.builder(int.class, "mField", Modifier.PRIVATE)
                                .addJavadoc("添加 private int mField\n")
                                .initializer("2")
                                .build())
                        // 添加数组类型 private int[] mArr
                        .addField(FieldSpec.builder(int[].class, "mArr", Modifier.PRIVATE)
                                .addJavadoc("添加数组类型 private int[] mArr\n")
                                .build())
                        // 添加对象类型 private File mRef
                        .addField(FieldSpec.builder(File.class, "mRef", Modifier.PRIVATE)
                                .addJavadoc("添加对象类型 private File mRef\n")
                                .initializer("new $T($S)", File.class, "D:\\123.png")
                                .build())

                        // static{} 静态块
                        .addStaticBlock(CodeBlock.builder()
                                .addStatement("$T.out.println($S)", System.class, "static 块").build())
                        // {} 普通块
                        .addInitializerBlock(CodeBlock.builder()
                                .addStatement("$T.out.println($S)", System.class, "普通{} 块").build())
                        //  构造方法
                        //  public ExampleAnn() {
                        //  }
                        .addMethod(MethodSpec.constructorBuilder()
                                .addModifiers(Modifier.PUBLIC)
                                .addJavadoc("构造方法\n")
                                .addStatement("$T.out.println($S)", System.class, "构造构造")
                                .build())

                        // 添加
                        //  public static void method(final String s, String... ss) {
                        //    System.out.println(s + ss);
                        //  }
                        .addMethod(MethodSpec.methodBuilder("method")
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .addJavadoc("static + void + (String) method \n")
                                .addParameter(ParameterSpec.builder(String.class, "s", Modifier.FINAL).build())
                                .addParameter(TypeVariableName.get("String..."), "ss", Modifier.FINAL)
                                .returns(void.class)
                                .addStatement("$T.out.println(s + $T.toString(ss))", System.class, Arrays.class)
                                .build())
                        //  添加
                        //  @Override
                        //  public String toString() {
                        //    return super.toString();
                        //  }
                        .addMethod(MethodSpec.methodBuilder("toString")
                                .addJavadoc("override toString()\n")
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .addStatement("return super.toString()")
                                .returns(String.class)
                                .build())
                        // 添加 public static <T extends Comparable<? super T>> void sort(List<T> list) throws Exception
                        .addMethod(MethodSpec
                                .methodBuilder("sort")
                                .addJavadoc("泛型参数排序方法\n")
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .returns(void.class)
                                .addException(Exception.class)
                                .addTypeVariable(TypeVariableName.get("T",
                                        ParameterizedTypeName.get(ClassName.get(Comparable.class), WildcardTypeName.supertypeOf(TypeVariableName.get("T")))))
                                .addParameter(ParameterizedTypeName.get(ClassName.get(List.class), TypeVariableName.get("T")),
                                        "list")
                                .build())

                        // 添加 public static <T extends Comparable<? super T>> void sort(List<T> list, Comparator<? super T> c)
                        .addMethod(MethodSpec
                                .methodBuilder("sort")
                                .addJavadoc("泛型参数排序方法\n")
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .returns(void.class)
                                .addTypeVariable(TypeVariableName.get("T",
                                        ParameterizedTypeName.get(ClassName.get(Comparable.class), WildcardTypeName.supertypeOf(TypeVariableName.get("T")))))
                                .addParameter(ParameterizedTypeName.get(ClassName.get(List.class), TypeVariableName.get("T")),
                                        "list")
                                .addParameter(ParameterizedTypeName.get(ClassName.get(Comparator.class), WildcardTypeName.supertypeOf(TypeVariableName.get("T"))), "c")
                                .addStatement("$T.out.println($S)", System.class, "sort(List<T> list, Comparator<? super T> c)")
                                .addStatement("$T.sort(list,c)", Collections.class)
                                .build())

                        // 添加
                        //  public static void main(String[] args) {
                        //
                        //  }
                        .addMethod(MethodSpec.methodBuilder("main")
                                .addJavadoc("main()\n")
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .returns(void.class)
                                .addParameter(String[].class, "args")
                                // System.out.println("Hello, JavaPoet!");
                                .addComment("Hello, JavaPoet!")
                                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                                .addCode(CodeBlock.builder()
                                        .addStatement("$T f = new $T($S)", File.class, File.class, "D:\\123.png")
                                        .addStatement("$T.out.println($S + $L)", System.class, "Does f exist?", "f.exists()")
                                        .build())
                                //  int total = 0;
                                //  for (int i = 0; i < 10; i++) {
                                //    total += i;
                                //  }
                                .addComment("for循环")
                                .addCode("int total = 0;\n"
                                        + "for (int i = 0; i < 10; i++) {\n"
                                        + "  total += i;\n"
                                        + "}\n")
                                //  method("Hello ExampleAnn!");
                                .addComment("调用method(s)")
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
                                .addComment("if-else if-else语法")
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
                                .addComment("try... catch... finally语法")
                                .beginControlFlow("try")
                                .addStatement("throw new Exception($S)", "Failed")
                                .nextControlFlow("catch ($T e)", Exception.class)
                                .addStatement("e.printStackTrace()")
                                .nextControlFlow("finally")
                                .endControlFlow()

                                // while
                                .addComment("while语法")
                                .addStatement("total = $L", 0)
                                .beginControlFlow("while (total < $L)", 10)
                                .addStatement("total ++")
                                .addStatement("$T.out.println(total)", System.class)
                                .endControlFlow()

                                // do... while
                                .addComment("do... while语法")
                                .addStatement("$T.out.println($S)", System.class, "+++++++分割线+++++++")
                                .beginControlFlow("do")
                                .addStatement("total ++")
                                .addStatement("$T.out.println(total)", System.class)
                                .endControlFlow("while (total < 12)")

                                // 调用sort(List<T> list, Comparator<? super T> c)
                                .addComment("调用sort(List<T> list, Comparator<? super T> c)")
                                .addStatement("int[] arr = new int[]{3,1,0,1,2,3,4,5}")
                                .addStatement("$T<$T> list = new $T()", List.class, String.class, ArrayList.class)
                                .addComment("for...each 循环")
                                .addCode("for(int i : arr){\n"
                                        +"list.add(i+\"\");"
                                        +"\n}\n"
                                )
                                .addStatement("$T.out.println($S + list)", System.class, "排序前：")
                                .addStatement("sort(list, $L)",
                                        TypeSpec.anonymousClassBuilder("")
                                                .superclass(ParameterizedTypeName.get(ClassName.get(Comparator.class), TypeVariableName.get(String.class)))
                                        .addMethod(MethodSpec.methodBuilder("compare")
                                                .addAnnotation(Override.class)
                                                .addModifiers(Modifier.PUBLIC)
                                                .returns(int.class)
                                                .addParameter(String.class , "o1")
                                                .addParameter(String.class , "o2")
                                                .addStatement("return $N.compareTo($N)", "o1", "o2")
                                                .build())
                                        .build()
                                    )
                                .addStatement("$T.out.println($S + list)", System.class, "排序后：")
                                .build())
                        // 内部类
                        .addType(TypeSpec.classBuilder("InnerClass").addJavadoc("内部类\n").build())

                        .build();


        JavaFile jf = JavaFile.builder(Constants.PACKAGE_NAME, clz)
                .build();
        jf.writeTo(mProcessingEnvironment.getFiler());
        System.out.println(jf);
    }


}
