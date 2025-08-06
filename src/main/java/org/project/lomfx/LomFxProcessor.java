package org.project.lomfx;

import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import org.project.lomfx.annotations.FxValue;
import org.project.lomfx.handlers.FxValueHandler;
import org.project.lomfx.utils.ProcessingEnvironmentUtil;
import org.project.lomfx.utils.SubtypeChecker;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

public class LomFxProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_17;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(FxValue.class.getName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        JavacProcessingEnvironment jcProcEnv = ProcessingEnvironmentUtil.getJavacProcessingEnvironment(processingEnv);

        if (jcProcEnv == null) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.WARNING,
                    "The LomFxProcessor only works with javac compiler. But it was not detected. Nothing will happen."
            );

            return false;
        }

        FxValueHandler fxValueHandler = new FxValueHandler(jcProcEnv);

        for (Element element : roundEnv.getElementsAnnotatedWith(FxValue.class)) {
            FxValue annotation = element.getAnnotation(FxValue.class);

            if (element.getKind() == ElementKind.FIELD) {
                if (!SubtypeChecker.isObservableValueSubtype(element, processingEnv)) {
                    processingEnv.getMessager().printMessage(
                            Diagnostic.Kind.ERROR,
                            "The field type of the annotated FxValue is not a subtype of ObservableValue!"
                    );
                }
                fxValueHandler.handle(element, annotation);
            } else if (element.getKind() == ElementKind.CLASS) {
                if (!annotation.name().isEmpty()) {
                    processingEnv.getMessager().printMessage(
                            Diagnostic.Kind.ERROR,
                            "The name should be empty when the annotation is on the class!"
                    );
                }
                element.getEnclosedElements().stream()
                        .filter(e -> e.getKind() == ElementKind.FIELD
                                && SubtypeChecker.isObservableValueSubtype(e, processingEnv))
                        .forEach(e -> fxValueHandler.handle(e, annotation));
            }
        }

        return true;
    }


}
