package it.tndigitale.a4g.framework.event.store.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReprocessEvent {
    boolean reThrowException() default false;
    // boolean sendMail() default true;
}
