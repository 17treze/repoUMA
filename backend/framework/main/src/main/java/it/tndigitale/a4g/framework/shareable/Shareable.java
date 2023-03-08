package it.tndigitale.a4g.framework.shareable;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

@Target(METHOD)
@Retention(RUNTIME)
public @interface Shareable {
    
    @Target(PARAMETER)
    @Retention(RUNTIME)
    public @interface Ignore {}
}