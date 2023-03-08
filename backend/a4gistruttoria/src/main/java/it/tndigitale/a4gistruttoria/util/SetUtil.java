package it.tndigitale.a4gistruttoria.util;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SetUtil {

    public static<T> Set<T> emptyIfNull(Set<T> set) {
        return Optional.ofNullable(set)
                       .orElse(new HashSet<>());
    }

}
