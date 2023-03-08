package it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno;

import java.util.Arrays;

public enum CasiParticolariEnum {
    RIORDINO_FONDIARIO("1", "RIORDINO FONDIARIO"),
    ZONA_MILITARE_O_CONFINE("2", "ZONA MILITARE O ZONA DI CONFINE SOGGETTA A VINCOLI DI SICUREZZA"),
    USO_CIVICO("3", "USO CIVICO"),
    ZONA_DEMANIALE("4", "ZONA DEMANIALE"),
    FRAZIONAMENTO("5", "PARTICELLE INTERESSATE DA FRAZIONAMENTO IN DATA SUCCESSIVA AL 31.12.1998"),
    STATO_ESTERO("6", "STATO ESTERO"),
    NUOVO_CATASTO("7", "NUOVO CATASTO EDILIZIO URBANO");


    private final String codTipoConduzione;
    private final String codDescrizione;

    CasiParticolariEnum(String codTipoConduzione, String codDescrizione) {
        this.codTipoConduzione = codTipoConduzione;
        this.codDescrizione = codDescrizione;
    }

    public String getCodTipoConduzione() {
        return codTipoConduzione;
    }

    public String getDescrizione() {
        return codDescrizione;
    }

    public static CasiParticolariEnum fromCodice(String codTipoConduzione) {
        return Arrays.stream(CasiParticolariEnum.values())
                .filter(tipo -> tipo.getCodTipoConduzione().equals(codTipoConduzione))
                .findFirst()
                .orElse(null);
    }
}
