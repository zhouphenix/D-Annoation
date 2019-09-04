package com.phenix.apt.impl;

import com.google.common.base.Strings;
import com.phenix.ann.apt.InstanceAnn;
import com.phenix.ann.apt.InstanceAnns;
import com.phenix.ann.aspect.MemoryCacheAspect;
import com.phenix.apt.Constants;
import com.phenix.apt.interfaces.IProcessor;
import com.squareup.javapoet.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
     * 构建方式： 通过相同参数构建构造方法，实例化Set<ClassName>
     * <构造方法对应参数列表类型, Set<类>>
     */
    private Map<String, Set<ClassName>> mConstructorTypeMap = new HashMap<>();

    private ProcessingEnvironment mProcessingEnvironment;

    public InstanceFactoryProcessorImpl() {

    }

    @Override
    public void start(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnv) {
        mProcessingEnvironment = processingEnvironment;
    }

    /**
     * 构建方法
     *
     * @param s          构造方法参数字符串 ，java.lang.Object-java.lang.String
     * @param classNames Set<ClassName>
     * @return MethodSpec
     */
    private static MethodSpec buildMethod(String s, Set<ClassName> classNames) {
        System.err.println("s: " + s + ", " + classNames);
        String[] params = s.split("-");
        List<ParameterSpec> parameterSpecs = new ArrayList<>();
        // 参数长度
        int length = params.length;
        // 需要考虑泛型参数 ==> 泛型不适合这类注解，除非穷举所有的可支持类型

        List<String> pModel = new ArrayList<>();
        String p;
        System.err.println("length: " + length);
        for (int i = 0; i < length; i++) {
            try {
                if (Strings.isNullOrEmpty(params[i])) {
                    break;
                }
                p = "p" + i;
                pModel.add(p);
                parameterSpecs.add(ParameterSpec.builder(TypeName.get(Class.forName(params[i])), p).build());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


        //构建方法1
        MethodSpec.Builder method = MethodSpec.methodBuilder("create")
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
                //指定类构造参数
                .addParameters(parameterSpecs);


        //构建代码块
        // 开始{
        CodeBlock.Builder mCodeBlockBuilder = CodeBlock.builder();
        mCodeBlockBuilder.beginControlFlow(" switch (mClass.getName()) ");
        for (ClassName cn : classNames) {
            mCodeBlockBuilder.add("case $S: \n", cn.reflectionName())
                    .indent()
                    .addStatement("return  (T)new $T($L)", cn, String.join(",", pModel))
                    .unindent();

        }
        String[] ps = new String[pModel.size()];
        for (int i = 0; i < pModel.size(); i++) {
            ps[i] = "$L.class";
            System.out.println("+++++++" + i);
        }

        System.out.println(Arrays.toString(ps) + "+++++++++++++++" + String.join(",", ps));

        mCodeBlockBuilder
                .add("default: return mClass.getConstructor(" + String.join(",", ps) + ")", (Object[]) params)
                .addStatement(".newInstance($L)", String.join(",", pModel)
                );
        //结束}
        mCodeBlockBuilder.endControlFlow();

        method.addCode(mCodeBlockBuilder.build());
        return method.build();
    }

    @Override
    public void process(TypeElement element) throws Exception {
        ClassName currentClz = ClassName.get(element);

        InstanceAnns anns = element.getAnnotation(InstanceAnns.class);

        Set<String> methodSet = new HashSet<>();

        //参数管理，同一个构造方法参数使用‘-’连接[, java.lang.Object-java.lang.String, java.lang.Object]
        for (InstanceAnn instanceAnn : anns.value()) {
            // instanceAnn.value() ==> String[],每个元素
            Collections.addAll(methodSet, String.join("-", instanceAnn.value()));
        }

        Set<ClassName> classSet;

        for (String method : methodSet) {
            if (!mConstructorTypeMap.containsKey(method)) {
                classSet = new HashSet<>();
                mConstructorTypeMap.put(method, classSet);
            } else {
                classSet = mConstructorTypeMap.get(method);
            }
            classSet.add(currentClz);
        }

        System.out.println("anns :" + mConstructorTypeMap);

        /*for (AnnotationMirror mirror : list) {
            //mirror.getAnnotationType()
            // ==> com.phenix.ann.apt.InstanceAnns , java.lang.Deprecated
            System.out.println("mirror:" + mirror + ", \n>>> annotationValues : " + mirror.getAnnotationType());
            mirror.getElementValues().forEach((BiConsumer<ExecutableElement, AnnotationValue>) (executableElement, annotationValue) -> {
                //executableElement.getReturnType() ==> com.phenix.ann.apt.InstanceAnn[]
                TypeMirror returnType = executableElement.getReturnType();
                Object value = annotationValue.getValue();
                if (value instanceof List) {
                    //class com.sun.tools.javac.util.List
                    List l = (List) value;

                    System.out.println("instance : " + (l.get(1)));
                }
                System.out.println("ann:" + returnType + ", " +
                        ((List<AnnotationValue>) value).get(1).getValue());

            });

        }*/


    }

    @Override
    public void end() throws Exception {

        final String CLASS_NAME = "InstanceFactory";
        //构建类
        TypeSpec.Builder tb = classBuilder(CLASS_NAME).addModifiers(PUBLIC, FINAL).addJavadoc("@ 实例化工厂 此类由apt自动生成\n");

        mConstructorTypeMap.forEach((s, classNames) -> tb.addMethod(buildMethod(s, classNames)));

        // 生成源代码
        JavaFile.builder(Constants.PACKAGE_NAME, tb.build())
                .build()
                .writeTo(mProcessingEnvironment.getFiler());

    }


}
