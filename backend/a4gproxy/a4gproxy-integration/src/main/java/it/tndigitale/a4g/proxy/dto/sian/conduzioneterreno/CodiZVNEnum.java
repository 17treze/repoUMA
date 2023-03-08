package it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno;

import java.util.Arrays;

public enum CodiZVNEnum {
    NO("0", "zone vulnerabili ai nitrati di origine agricola: NO"),
    SI("1", "zone vulnerabili ai nitrati di origine agricola: SI");

    private final String codice;
    private final String descrizione;

    CodiZVNEnum(String codice, String descrizione) {
        this.codice = codice;
        this.descrizione = descrizione;
    }

    public String getCodice() {
        return codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public static CodiZVNEnum fromCodice(String codice) {
        return Arrays.stream(CodiZVNEnum.values())
                .filter(tipo -> tipo.getCodice().equals(codice))
                .findFirst()
                .orElse(null);
    }
}
