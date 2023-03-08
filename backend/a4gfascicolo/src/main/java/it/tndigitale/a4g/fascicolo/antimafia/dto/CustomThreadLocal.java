package it.tndigitale.a4g.fascicolo.antimafia.dto;

import java.util.HashMap;
import java.util.Map;

//TODO FIXME verificare il perchè si è dovuto fare il controllo del null in tutti i metodi
public class CustomThreadLocal {

	private static final ThreadLocal<Map<String, Object>> threadLocal = new InheritableThreadLocal<>();
	
	
	private CustomThreadLocal() {

	}

	public static ThreadLocal<Map<String, Object>> getThreadLocal() {
		return threadLocal;
	}

	/**
	 * Sovrascrive la mappa del {@link ThreadLocal} con quella passata in input.
	 * 
	 * @param variables
	 */
	public static void setVariables(Map<String, Object> variables) {
		threadLocal.set(variables);
	}

	/**
	 * Aggiunge la mappa passata in input a quella già presente nel {@link ThreadLocal}.
	 * 
	 * @param variables
	 */
	public static void addVariables(Map<String, Object> variables) {
		if (threadLocal.get() == null) {
			threadLocal.set(new HashMap<>());
		}
		threadLocal.get().putAll(variables);
	}

	/**
	 * Aggiunge una variabile alla mappa già presente nel {@link ThreadLocal}.
	 * 
	 * @param key
	 * @param value
	 */
	public static void addVariable(String key, String value) {
		if (threadLocal.get() == null) {
			threadLocal.set(new HashMap<>());
		}		
		threadLocal.get().put(key, value);
	}

	/**
	 * Recupera la mappa di variabili dal {@link ThreadLocal}.
	 * 
	 * @return
	 */
	public static Map<String, Object> getVariables() {
		return threadLocal.get();
	}

	/**
	 * Recupera la variabile (identificata dalla key) dalla mappa di variabili del {@link ThreadLocal}.
	 * 
	 * @return
	 */
	public static Object getVariable(String key) {
		if (threadLocal.get() == null) {
			threadLocal.set(new HashMap<>());
		}
		return threadLocal.get().get(key);
	}

	/**
	 * Pulisce la mappa del {@link ThreadLocal}.
	 */
	public static void clean() {
		threadLocal.remove();
	}
}
