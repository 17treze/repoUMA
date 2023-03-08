package it.tndigitale.a4gistruttoria;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class ReflectionUtil {
    public static Annotation getAnnotationOfTypeFromClass(Class<?> cls, Class<?> type) {
        if (cls == null || type == null) {
            return null;
        }
        return Arrays.asList(cls.getDeclaredAnnotations())
                     .stream()
                     .filter(annotation -> annotation.annotationType().equals(type))
                     .findFirst()
                     .orElse(null);
    }
}