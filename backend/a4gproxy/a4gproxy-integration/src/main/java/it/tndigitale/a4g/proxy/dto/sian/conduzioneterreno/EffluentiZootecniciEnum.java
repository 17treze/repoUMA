package it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno;

import java.util.Arrays;

public enum EffluentiZootecniciEnum {
    SENZA_EFFLUENTI("0", "particella senza effluenti zootecnici"),
    CON_EFFLUENTI("1", "particella con effluenti zootecnici");

    private final String codice;
    private final String descrizione;

    EffluentiZootecniciEnum(String codice, String descrizione) {
        this.codice = codice;
        this.descrizione = descrizione;
    }

    public String getCodice() {
        return codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public static EffluentiZootecniciEnum fromCodice(String codice) {
        return Arrays.stream(EffluentiZootecniciEnum.values())
                .filter(tipo -> tipo.getCodice().equals(codice))
                .findFirst()
                .orElse(null);
    }
}
