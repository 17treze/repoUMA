package it.tndigitale.a4g.proxy.dto.zootecnia;

public enum InterventoMacellazione implements Intervento {
    BOVINO_MACELLATO(315),
    BOVINO_MACELLATO_12MESI(316),
    BOVINO_MACELLATO_ETICHETTATO(318);

    private Integer codiceAgea;

    private InterventoMacellazione(Integer codiceAgea) {
        this.codiceAgea = codiceAgea;
    }

    @Override
    public Integer getCodiceAgea() {
        return codiceAgea;
    }
}
