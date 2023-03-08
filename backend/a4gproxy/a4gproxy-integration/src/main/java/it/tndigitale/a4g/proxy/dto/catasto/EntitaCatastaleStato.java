package it.tndigitale.a4g.proxy.dto.catasto;

public enum EntitaCatastaleStato {
    ESTINTA,
    VALIDATA;

    public String value() {
        return name();
    }

    public static EntitaCatastaleStato fromValue(String v) {
        return valueOf(v);
    }

}
