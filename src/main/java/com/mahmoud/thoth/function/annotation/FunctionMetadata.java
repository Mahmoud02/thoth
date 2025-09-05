package com.mahmoud.thoth.function.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionMetadata {
    String name();
    String description();
    String[] properties() default {};
    String[] propertyTypes() default {};
    boolean[] propertyRequired() default {};
    String[] propertyDescriptions() default {};
    String[] propertyDefaults() default {};
}
