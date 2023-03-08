package it.tndigitale.a4g.proxy.dto.zootecnia;

import io.swagger.annotations.ApiModelProperty;

public class CapiMacellazionePerInterventoFilter extends CapiAziendaPerInterventoFilter<InterventoMacellazione> {
    @ApiModelProperty(value = "Intervento in ambito zootecnico per cui si richiede il premio", required = true)
    private InterventoMacellazione intervento;

    @Override
    public InterventoMacellazione getIntervento() {
        return this.intervento;
    }

    @Override
    public void setIntervento(InterventoMacellazione intervento) {
        this.intervento = intervento;
    }

    @Override
    public Integer getCodiceAgeaIntervento() {
        return getIntervento().getCodiceAgea();
    }
}
