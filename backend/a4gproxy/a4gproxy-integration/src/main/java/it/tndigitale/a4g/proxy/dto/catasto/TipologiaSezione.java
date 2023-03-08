package it.tndigitale.a4g.proxy.dto.catasto;

import it.taa.regione.librofondiario.benetype.schemas.SezioneType;

public enum TipologiaSezione {

    I,
    II;

    public String value() {
        return name();
    }

    public static TipologiaSezione fromValue(String v) {
        return valueOf(v);
    }

}
