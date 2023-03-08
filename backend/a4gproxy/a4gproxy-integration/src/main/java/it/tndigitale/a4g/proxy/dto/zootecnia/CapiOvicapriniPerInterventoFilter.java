package it.tndigitale.a4g.proxy.dto.zootecnia;

import io.swagger.annotations.ApiModelProperty;

public class CapiOvicapriniPerInterventoFilter extends CapiAziendaPerInterventoFilter<InterventoOvicaprino> {
    @ApiModelProperty(value = "Intervento in ambito zootecnico per cui si richiede il premio", required = true)
    private InterventoOvicaprino intervento;

    @Override
    public InterventoOvicaprino getIntervento() {
        return this.intervento;
    }

    @Override
    public void setIntervento(InterventoOvicaprino intervento) {
        this.intervento = intervento;
    }

    @Override
    public Integer getCodiceAgeaIntervento() {
        return getIntervento().getCodiceAgea();
    }
}
