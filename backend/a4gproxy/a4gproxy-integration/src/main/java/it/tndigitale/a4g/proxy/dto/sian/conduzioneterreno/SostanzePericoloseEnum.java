package it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum SostanzePericoloseEnum {
    SENZA_SOSTANZE_PERICOLOSE("0", "particella senza sostanze pericolose"),
    CON_SOSTANZE_PERICOLOSE("1", "particella con sostanze pericolose");

    private final String codice;
    private final String descrizione;

    SostanzePericoloseEnum(String codice, String descrizione) {
        this.codice = codice;
        this.descrizione = descrizione;
    }

    public String getCodice() {
        return codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public static SostanzePericoloseEnum fromCodice(String codice) {
        return Arrays.stream(SostanzePericoloseEnum.values())
                .filter(tipo -> tipo.getCodice().equals(codice))
                .findFirst()
                .orElse(null);
    }
}
