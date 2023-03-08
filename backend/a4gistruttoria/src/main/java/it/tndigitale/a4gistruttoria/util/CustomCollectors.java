package it.tndigitale.a4gistruttoria.util;

import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class CustomCollectors {

	private CustomCollectors() {
	}

	public static <T> Collector<T, ?, T> toSingleton() {
		return Collectors.collectingAndThen(Collectors.toList(), list -> {
			if (list.size() != 1) {
				throw new IllegalStateException("Zero or more than one element found.");
			}
			return list.get(0);
		});
	}
	
	public static <T> Collector<T, ?, Optional<T>> collectOne() {
		return Collectors.collectingAndThen(Collectors.toList(), list -> {
			if (list.size() > 1) {
				throw new IllegalStateException("More than one element found.");
			}
			return Optional.ofNullable(list.isEmpty() ? null : list.get(0));
		});
	}
}
