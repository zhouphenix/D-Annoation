package com.phenix.apt.impl;

import com.phenix.ann.aspect.MemoryCache;
import com.phenix.apt.Constants;
import com.phenix.apt.interfaces.IProcessor;
import com.squareup.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import java.util.HashSet;
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
     * 代码体builder
     */
    private CodeBlock.Builder mCodeBlockBuilder;
    /**
     * 注解的class
     */
    private Set<ClassName> mAddedSet = new HashSet<>();

    private ProcessingEnvironment mProcessingEnvironment;

    public InstanceFactoryProcessorImpl() {
        this.mCodeBlockBuilder = CodeBlock.builder();
    }

    @Override
    public void start(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnv) {
        mProcessingEnvironment = processingEnvironment;
        //构建代码块
        // 开始{
        mCodeBlockBuilder.beginControlFlow(" switch (mClass.getSimpleName()) ");
    }

    @Override
    public void process(TypeElement element) throws Exception {
        ClassName currentClz = ClassName.get(element);
        mAddedSet.add(currentClz);
    }

    @Override
    public void end() throws Exception {
        for (ClassName cn : mAddedSet) {
            mCodeBlockBuilder.addStatement("case $S: return  new $T()", cn.reflectionName(), cn);//初始化Presenter
        }
        mCodeBlockBuilder.addStatement("default: return mClass.newInstance()");
        //结束}
        mCodeBlockBuilder.endControlFlow();


        final String CLASS_NAME = "InstanceFactory";
        //构建类
        TypeSpec.Builder tb = classBuilder(CLASS_NAME).addModifiers(PUBLIC, FINAL).addJavadoc("@ 实例化工厂 此类由apt自动生成");
        //构建方法1
        MethodSpec.Builder mb1 = MethodSpec.methodBuilder("create")
//                .addAnnotation(MemoryCache.class)
                .addJavadoc("@此方法由apt自动生成")
                .returns(Object.class)
                .addModifiers(PUBLIC, STATIC)
                .addException(IllegalAccessException.class)
                .addException(InstantiationException.class)
                .addParameter(Class.class, "mClass");

        mb1.addCode(mCodeBlockBuilder.build());

        tb.addMethod(mb1.build());

        // 生成源代码
        JavaFile.builder(Constants.PACKAGE_NAME, tb.build())
                .build()
                .writeTo(mProcessingEnvironment.getFiler());

    }
}
