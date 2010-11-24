package javabot.commands;

import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({FIELD})
public @interface Param {
    String name() default "";

    boolean required() default true;

    boolean primary() default false;

    String defaultValue() default "";
}
