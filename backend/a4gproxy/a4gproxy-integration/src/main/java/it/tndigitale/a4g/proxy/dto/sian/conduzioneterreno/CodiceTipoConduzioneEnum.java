package it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno;

import java.util.Arrays;

public enum CodiceTipoConduzioneEnum {
    PROPRIETA_O_COMPROPRIETA("1", "proprietà o comproprietà"),
    AFFITTO("2", "affitto"),
    MEZZADRIA("3", "mezzadria"),
    ALTRA_FORMA("4", "altra forma"),
    EST_INF_COMUNE_MONTANO("5", "Est.Inf.5.000mq Comune Montano(DL.24/06/2014 N.91)");


    private final String codTipoConduzione;
    private final String codDescrizione;

    CodiceTipoConduzioneEnum(String codTipoConduzione, String codDescrizione) {
        this.codTipoConduzione = codTipoConduzione;
        this.codDescrizione = codDescrizione;
    }

    public String getCodTipoConduzione() {
        return codTipoConduzione;
    }

    public String getCodDescrizione() {
        return codDescrizione;
    }

    public static CodiceTipoConduzioneEnum fromCodTipoConduzione(String codTipoConduzione) {
        return Arrays.stream(CodiceTipoConduzioneEnum.values())
                .filter(tipo -> tipo.getCodTipoConduzione().equals(codTipoConduzione))
                .findFirst()
                .orElse(null);
    }
}
