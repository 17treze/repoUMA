package it.tndigitale.a4g.richiestamodificasuolo.config;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ErroriOracle {
	private static final String NO_ERROR = "no error";
	private static final String ERROR_13349 = "polygon boundary crosses itself";
	private static final String ERROR_13351 = "two or more rings of a complex polygon overlap";
	private static final String ERROR_13350 = "two or more rings of a complex polygon touch";
	private static final String ERROR_13366 = "invalid combination of interior exterior rings";
	private static final String ERROR_13356 = "adjacent points in a geometry are redundant";
	private static final String ERROR_13343 = "a polygon geometry has fewer than four coordinates";
	private static final String ERROR_13367 = "wrong orientation for interior/exterior rings";
	private static final String ERROR_13348 = "polygon boundary is not closed";
	private static final String ERROR_13368 = "simple polygon type has more than one exterior ring";
	private static final String ERROR_13347 = "the coordinates defining an arc are not distinct";

	private static final Map<String, String> map = Stream.of(new AbstractMap.SimpleEntry<>("TRUE", NO_ERROR), new AbstractMap.SimpleEntry<>("13349", ERROR_13349),
			new AbstractMap.SimpleEntry<>("13351", ERROR_13351), new AbstractMap.SimpleEntry<>("13350", ERROR_13350), new AbstractMap.SimpleEntry<>("13366", ERROR_13366),
			new AbstractMap.SimpleEntry<>("13356", ERROR_13356), new AbstractMap.SimpleEntry<>("13343", ERROR_13343), new AbstractMap.SimpleEntry<>("13367", ERROR_13367),
			new AbstractMap.SimpleEntry<>("13348", ERROR_13348), new AbstractMap.SimpleEntry<>("13368", ERROR_13368), new AbstractMap.SimpleEntry<>("13347", ERROR_13347))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

	private ErroriOracle() {
	}

	public static Map<String, String> getMap() {
		return map;
	}
}
