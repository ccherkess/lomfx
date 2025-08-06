package org.project.lomfx.handlers;

import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import org.project.lomfx.annotations.FxValue;
import org.project.lomfx.inserters.GetterMethodInserter;
import org.project.lomfx.inserters.MethodInserter;
import org.project.lomfx.inserters.PropertyMethodInserter;
import org.project.lomfx.inserters.SetterMethodInserter;
import org.project.lomfx.utils.SubtypeChecker;

import javax.lang.model.element.Element;
import java.util.List;

public class FxValueHandler {

    private final JavacProcessingEnvironment processingEnv;
    private final List<MethodInserter> methodInserters;

    public FxValueHandler(JavacProcessingEnvironment jcProcEnv) {
        this.processingEnv = jcProcEnv;
        this.methodInserters = List.of(
                new PropertyMethodInserter(jcProcEnv),
                new GetterMethodInserter(jcProcEnv),
                new SetterMethodInserter(jcProcEnv)
        );
    }

    public void handle(Element element, FxValue annotation) {
        methodInserters.forEach(i -> i.insert(
                        element,
                        annotation.name().isEmpty() ? element.getSimpleName().toString() : annotation.name(),
                        annotation.modifier(),
                        annotation.readOnly() || !SubtypeChecker.isWritableValueSubtype(element, processingEnv)
                )
        );
    }

}
