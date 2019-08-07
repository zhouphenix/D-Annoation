package com.phenix.apt.impl;

import com.phenix.apt.Constants;
import com.phenix.apt.interfaces.IProcessor;
import com.squareup.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.*;


/**
 * 代码类也不是很多
 * AnnotationSpec：用来创建注解
 * ArrayTypeName：用来创建数组[]
 * ClassName：用来包装一个类
 * CodeBlock：编写代码块
 * CodeWriter：
 * FieldSpec：代表一个成员变量，一个字段声明。
 * JavaFile：包含一个顶级类的Java文件。
 * MethodSpec：表一个构造函数或方法声明。
 * NameAllocator：名称分配器（通过对象得到名字）
 * ParameterizedTypeName：参数泛型（类List< String> datalist）
 * ParameterSpec： 用来创建参数
 * TypeName：如在添加返回值类型是使用
 * TypeSpec：代表一个类，接口，或者枚举声明
 * TypeVariableName：泛型
 * Util：
 * WildcardTypeName：泛型
 * ---------------------
 * POET 占位符：
 * $L代表的是字面量
 * $S for Strings
 * $N for Names(我们自己生成的方法名或者变量名等等)
 * $T for Types
 * ---------------------
 */
public class InstanceFactoryProcessorImpl implements IProcessor<TypeElement> {


    /**
     * 注解的class
     */
    private Set<ClassName> mAddedSet = new HashSet<>();

    private ProcessingEnvironment mProcessingEnvironment;

    public InstanceFactoryProcessorImpl() {

    }

    @Override
    public void start(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnv) {
        mProcessingEnvironment = processingEnvironment;
    }

    @Override
    public void process(TypeElement element) throws Exception {
        ClassName currentClz = ClassName.get(element);
        mAddedSet.add(currentClz);
    }

    @Override
    public void end() throws Exception {
        final String CLASS_NAME = "InstanceFactory";
        //构建类
        TypeSpec.Builder tb = classBuilder(CLASS_NAME).addModifiers(PUBLIC, FINAL).addJavadoc("@ 实例化工厂 此类由apt自动生成\n");
        //构建方法1
        MethodSpec.Builder mb1 = MethodSpec.methodBuilder("create")
//                .addAnnotation(MemoryCache.class)
                .addJavadoc("@此方法由apt自动生成\n")
                .returns(TypeVariableName.get("T"))
                .addTypeVariable(TypeVariableName.get("T"))
                .addModifiers(PUBLIC, STATIC)
                .addException(IllegalAccessException.class)
                .addException(InstantiationException.class)
                .addException(NoSuchMethodException.class)
                .addException(InvocationTargetException.class)
                //指定类构造
                .addParameter(
                        ParameterSpec
                                .builder(ParameterizedTypeName.get(ClassName.get(Class.class), TypeVariableName.get("T")), "mClass", FINAL)
//                                .addAnnotation(NotNull.class)
                                .build())
                //构造方法需要的参数
                .addParameter(
                        ParameterSpec
                                .builder(TypeVariableName.get("Object..."), "params")
//                                .addAnnotation(Nullable.class)
                                .build());

        //构建代码块
        // 开始{
        CodeBlock.Builder mCodeBlockBuilder = CodeBlock.builder();
        mCodeBlockBuilder.beginControlFlow(" switch (mClass.getName()) ");
        for (ClassName cn : mAddedSet) {
            mCodeBlockBuilder.add("case $S: \n", cn.reflectionName())
                    .indent()
                    .beginControlFlow("if (params == null || params.length == 0)")
                    .addStatement("return  (T)new $T()", cn)
                    .nextControlFlow("else")
                    .addStatement("return  (T)new $T($L)", cn, "params")
                    .endControlFlow()
                    .unindent();

        }
        mCodeBlockBuilder.addStatement("default: return mClass.getConstructor().newInstance()");
        //结束}
        mCodeBlockBuilder.endControlFlow();

        mb1.addCode(mCodeBlockBuilder.build());

        tb.addMethod(mb1.build());

        // 生成源代码
        JavaFile.builder(Constants.PACKAGE_NAME, tb.build())
                .build()
                .writeTo(mProcessingEnvironment.getFiler());

    }


}
