package com.phenix.apt.util;

import com.squareup.javapoet.ClassName;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import static com.phenix.apt.Constants.ANNOTATION;
import static javax.lang.model.element.Modifier.*;

public class Utils {


    public static boolean isPublic(TypeElement element) {
        return element.getModifiers().contains(PUBLIC);
    }

    public static boolean isAbstract(TypeElement element) {
        return element.getModifiers().contains(ABSTRACT);
    }

    public static boolean isValidClass(TypeElement element) throws RuntimeException {
        if (element.getKind() != ElementKind.CLASS) {
            return false;
        }

        if (!isPublic(element)) {
            String message = String.format("Classes annotated with %s must be public.", ANNOTATION);
            throw new RuntimeException(message);
        }

        if (isAbstract(element)) {
            String message = String.format("Classes annotated with %s must not be abstract.", ANNOTATION);
            throw new RuntimeException(message);
        }

        return true;
    }

    public static String getPackageName(Elements elements, TypeElement typeElement) {
        PackageElement pkg = elements.getPackageOf(typeElement);
        if (pkg.isUnnamed()) {
            return "";
        }
        return pkg.getQualifiedName().toString();
    }


    public static String getClassName(TypeElement typeElement) throws ClassNotFoundException {
        return ClassName.get(typeElement).simpleName();
    }

    public static ClassName getType(String className) {
        return ClassName.get(className.substring(0, className.lastIndexOf(".")),
                className.substring(className.lastIndexOf(".") + 1, className.length()));
    }
}
