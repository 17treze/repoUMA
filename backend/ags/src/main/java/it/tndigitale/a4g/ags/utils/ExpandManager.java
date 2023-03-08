package it.tndigitale.a4g.ags.utils;

import java.util.List;

public class ExpandManager {

	private static ThreadLocal<List<String>> threadLocal = new ThreadLocal<>();

	private ExpandManager() {

	}

	public static ThreadLocal<List<String>> getThreadLocal() {
		return threadLocal;
	}

	public static void setExpands(List<String> expands) {
		threadLocal.set(expands);
	}

	public static List<String> getExpands() {
		return threadLocal.get();
	}

	public static void clean() {
		threadLocal.remove();
	}
}