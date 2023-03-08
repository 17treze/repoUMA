package it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno;

import java.util.Arrays;

public enum FlagTerrazzataEnum {

    SENZA_TERRAZZAMENTI_O_LIVELLAMENTI("0", "particella senza terrazzamenti o livellamenti"),
    CON_TERRAZZAMENTI("1", "particella con terrazzamenti"),
    CON_LIVELLAMENTI("2", "particella con livellamenti"),
    CON_TERRAZZAMENTI_E_LIVELLAMENTI("3", "particella con terrazzamenti e livellamenti");

    private final String codice;
    private final String descrizione;

    FlagTerrazzataEnum(String codice, String descrizione) {
        this.codice = codice;
        this.descrizione = descrizione;
    }

    public String getCodice() {
        return codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public static FlagTerrazzataEnum fromCodice(String codice) {
        return Arrays.stream(FlagTerrazzataEnum.values())
                .filter(tipo -> tipo.getCodice().equals(codice))
                .findFirst()
                .orElse(null);
    }
}
