package com.phenix.apt;

import com.google.auto.service.AutoService;
import com.phenix.ann.apt.ExampleAnn;
import com.phenix.ann.apt.InstanceAnn;
import com.phenix.apt.impl.ExampleProcessorImpl;
import com.phenix.apt.impl.InstanceFactoryProcessorImpl;
import com.phenix.apt.interfaces.IProcessor;
import com.phenix.apt.util.Utils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@AutoService(Processor.class)
public class FactoryProcessor extends AbstractProcessor {

    private Messager mMessager;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Map<Class<? extends Annotation>, IProcessor<TypeElement>> map = getSupportedAnnotationClass();

        Set<String> set = new HashSet<>();
        for (Class clz : map.keySet()) {
            set.add(clz.getCanonicalName());
        }
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("┌======================================================================┐");
        Map<Class<? extends Annotation>, IProcessor<TypeElement>> map = getSupportedAnnotationClass();
        System.out.println("FactoryProcessor.process: " + map.keySet() + ", " + roundEnv.processingOver());
        IProcessor<TypeElement> processor;
        // 遍历所有支持的注解
        for (Map.Entry<Class<? extends Annotation>, IProcessor<TypeElement>> entry : map.entrySet()) {
            processor = entry.getValue();
            System.out.println("start: " + entry.getKey());
            processor.start(processingEnv, roundEnv);

            // 过滤Type类型elements
            for (TypeElement element : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(entry.getKey()))) {
                try {
                    if (Utils.isValidClass(element)) {
                        System.out.println("正在处理: " + element.toString());
                        mMessager.printMessage(Diagnostic.Kind.NOTE, "正在处理: " + element.toString());
                        processor.process(element);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("异常信息：" + e.getMessage());
                    mMessager.printMessage(Diagnostic.Kind.ERROR, "异常信息：" + e.getMessage());
                }
            }
            try {
                processor.end();
                System.out.println("end: " + entry.getKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("└======================================================================┘");
        return true;
    }

    /**
     * 获取所有支持的注解类列表及处理器
     *
     * @return Map
     */
    private Map<Class<? extends Annotation>, IProcessor<TypeElement>> getSupportedAnnotationClass() {
        Map<Class<? extends Annotation>, IProcessor<TypeElement>> map = new HashMap<>();
        map.put(InstanceAnn.class, new InstanceFactoryProcessorImpl());
        map.put(ExampleAnn.class, new ExampleProcessorImpl());
        return map;
    }

}
