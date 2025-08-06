package org.project.lomfx.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface FxValue {

    Modifier modifier() default Modifier.PUBLIC;

    boolean readOnly() default false;

    String name() default "";

}
