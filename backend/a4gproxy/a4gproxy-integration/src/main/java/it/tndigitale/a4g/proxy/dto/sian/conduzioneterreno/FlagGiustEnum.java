package it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno;

import java.util.Arrays;

public enum FlagGiustEnum {
    DOCUMENTAZIONE_GIUSTIFICATIVA_ASSENTE("0", "Assenza di documentazione giustificativa"),
    DOCUMENTAZIONE_GIUSTIFICATIVA_PRESENTE("1", "Presenza di documentazione giustificativa della presenza a catasto (es. visura)");

    private final String codice;
    private final String descrizione;

    FlagGiustEnum(String codice, String descrizione) {
        this.codice = codice;
        this.descrizione = descrizione;
    }

    public String getCodice() {
        return codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public static FlagGiustEnum fromCodice(String codice) {
        return Arrays.stream(FlagGiustEnum.values())
                .filter(tipo -> tipo.getCodice().equals(codice))
                .findFirst()
                .orElse(null);
    }
}
