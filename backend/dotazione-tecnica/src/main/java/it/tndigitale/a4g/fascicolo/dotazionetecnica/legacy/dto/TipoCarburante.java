package it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto;

public enum TipoCarburante {
	GASOLIO,
	BENZINA;
	
	// in A4G viene trattato solo il carburante Gasolio e Benzina.
	// Il reperimento di altri tipi di carburante viene trattato come null.
	public static TipoCarburante customValueOf(String value) {
		if (!"GASOLIO".equals(value) && !"BENZINA".equals(value)) {
			return null;
		}
		return TipoCarburante.valueOf(value.replaceAll(" ", "_").toUpperCase());
	}
}
