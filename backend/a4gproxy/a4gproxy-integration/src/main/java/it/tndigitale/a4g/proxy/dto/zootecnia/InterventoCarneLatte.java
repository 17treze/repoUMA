package it.tndigitale.a4g.proxy.dto.zootecnia;

public enum InterventoCarneLatte implements Intervento {
    VACCA_LATTE(310),
    VACCA_LATTE_MONTANA(311),
    VACCA_NUTRICE(313),
    VACCA_NUTRICE_NON_ISCRITTA(322),

    BUFALA_30MESI(312),
    VACCA_DUPLICE_ATTITUDINE(314);

    private Integer codiceAgea;

    private InterventoCarneLatte(Integer codiceAgea) {
        this.codiceAgea = codiceAgea;
    }

    @Override
    public Integer getCodiceAgea() {
        return codiceAgea;
    }
}
