package it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno;

import java.util.Arrays;

public enum RotazioneColtureOrtiveEnum {

    SENZA_ROTAZIONE_COLTURALE("0", "particella senza rotazione colturale"),
    CICLO_ORTIVO("1", "particella con ciclo ortivo"),
    CICLO_SEMINATIVO("2", "particella con ciclo seminativo"),
    NON_DICHIARATO("3", "non dichiarato");

    private final String codice;
    private final String descrizione;

    RotazioneColtureOrtiveEnum(String codice, String descrizione) {
        this.codice = codice;
        this.descrizione = descrizione;
    }

    public String getCodice() {
        return codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public static RotazioneColtureOrtiveEnum fromCodice(String codice) {
        return Arrays.stream(RotazioneColtureOrtiveEnum.values())
                .filter(tipo -> tipo.getCodice().equals(codice))
                .findFirst()
                .orElse(null);
    }
}
