package it.tndigitale.a4g.proxy.dto.zootecnia;

public enum InterventoOvicaprino implements Intervento {
    AGNELLA(320),
    OVICAPRINO_MACELLATO(321);

    private Integer codiceAgea;

    private InterventoOvicaprino(Integer codiceAgea) {
        this.codiceAgea = codiceAgea;
    }

    @Override
    public Integer getCodiceAgea() {
        return codiceAgea;
    }
}
