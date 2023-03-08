package it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno;

import java.util.Arrays;

public enum FlagIrriguaEnum {

    NON_IRRIGUA("0", "particella non irrigua"),
    IRRIGUA("1", "particella irrigua"),
    NON_DICHIARATO("2", "non dichiarato");

    private final String codice;
    private final String descrizione;

    FlagIrriguaEnum(String codice, String descrizione) {
        this.codice = codice;
        this.descrizione = descrizione;
    }

    public String getCodice() {
        return codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public static FlagIrriguaEnum fromCodice(String codice) {
        return Arrays.stream(FlagIrriguaEnum.values())
                .filter(tipo -> tipo.getCodice().equals(codice))
                .findFirst()
                .orElse(null);
    }
}
