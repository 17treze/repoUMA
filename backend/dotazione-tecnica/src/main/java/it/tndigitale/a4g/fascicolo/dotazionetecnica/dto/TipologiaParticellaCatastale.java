package it.tndigitale.a4g.fascicolo.dotazionetecnica.dto;

public enum TipologiaParticellaCatastale {
    FONDIARIA, EDIFICIALE;

    public String value() {
        return name();
    }

    public static TipologiaParticellaCatastale fromValue(String v) {
        return valueOf(v);
    }
}
