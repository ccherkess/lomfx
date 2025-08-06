package org.project.lomfx.utils;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class SubtypeChecker {

    public static boolean isObservableValueSubtype(Element element, ProcessingEnvironment processingEnv) {
        Types typeUtils = processingEnv.getTypeUtils();
        Elements elementUtils = processingEnv.getElementUtils();

        TypeMirror observableValueType = typeUtils.getDeclaredType(
                elementUtils.getTypeElement(ObservableValue.class.getName()),
                typeUtils.getWildcardType(null, null)
        );

        return processingEnv.getTypeUtils().isSubtype(element.asType(), observableValueType);
    }

    public static boolean isWritableValueSubtype(Element element, ProcessingEnvironment processingEnv) {
        Types typeUtils = processingEnv.getTypeUtils();
        Elements elementUtils = processingEnv.getElementUtils();

        TypeMirror writableValueType = typeUtils.getDeclaredType(
                elementUtils.getTypeElement(WritableValue.class.getName()),
                typeUtils.getWildcardType(null, null)
        );

        return processingEnv.getTypeUtils().isSubtype(element.asType(), writableValueType);
    }

}
