package it.tndigitale.a4g.proxy.dto.zootecnia;

import io.swagger.annotations.ApiModelProperty;

public class CapiCarneLattePerInterventoFilter extends CapiAziendaPerInterventoFilter<InterventoCarneLatte> {
    @ApiModelProperty(value = "Intervento in ambito zootecnico per cui si richiede il premio", required = true)
    private InterventoCarneLatte intervento;

    @Override
    public InterventoCarneLatte getIntervento() {
        return this.intervento;
    }

    @Override
    public void setIntervento(InterventoCarneLatte intervento) {
        this.intervento = intervento;
    }

    @Override
    public Integer getCodiceAgeaIntervento() {
        return getIntervento().getCodiceAgea();
    }
}
