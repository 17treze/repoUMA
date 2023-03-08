package it.tndigitale.a4g.proxy.dto.catasto;

public enum TipologiaParticellaCatastale {
	FONDIARIA, EDIFICIALE;

	public String value() {
		return name();
	}

	public static TipologiaParticellaCatastale fromValue(String v) {
		return valueOf(v);
	}
}
