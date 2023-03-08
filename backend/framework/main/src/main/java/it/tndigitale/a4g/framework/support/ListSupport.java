package it.tndigitale.a4g.framework.support;

import static java.util.Collections.emptyList;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ListSupport {

    public static<T> List<T> convert(Set<T> set) {
        return Optional.ofNullable(set)
                       .orElse(new HashSet<>())
                       .stream()
                       .collect(Collectors.toList());
    }

    public static<T> Set<T> convertToSet(List<T> list) {
        return emptyIfNull(list).stream()
                                .collect(Collectors.toSet());
    }

    public static<T> T getFirstElementOf(List<T> lista) {
        return Optional.ofNullable(lista)
                       .orElse(new ArrayList<>())
                       .stream()
                       .findFirst()
                       .orElse(null);
    }

    public static <T> List<T> emptyIfNull(List<T> list) {
        return Optional.ofNullable(list)
                       .orElse(new ArrayList<>());
    }

    public static <T> Boolean isNotEmpty(List<T> list) {
        return list !=null && !list.isEmpty();
    }

    public static <T> List<T> intersect(List<T> list1, List<T> list2) {
        if (isEmpty(list1) || isEmpty(list2)) {
            return emptyList();
        }
        return list1.stream()
                    .distinct()
                    .filter(list2::contains)
                    .collect(Collectors.toList());
    }
}
