package com.phenix.apt.interfaces;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

public interface IProcessor<T> {
    void start(ProcessingEnvironment processingEnvironment, RoundEnvironment roundEnv);
    void process(T t) throws Exception;
    void end() throws Exception;
}
