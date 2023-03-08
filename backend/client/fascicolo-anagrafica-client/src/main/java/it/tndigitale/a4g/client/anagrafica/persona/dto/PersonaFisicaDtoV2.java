package it.tndigitale.a4g.client.anagrafica.persona.dto;

import io.swagger.annotations.ApiModelProperty;

public class PersonaFisicaDtoV2 extends PersonaDtoV2 {
    private static final long serialVersionUID = 2693904307385187836L;

    @ApiModelProperty(value = "I dati dell'eventuale ditta individuale")
    private AnagraficaDtoV2 anagrafica;

    @ApiModelProperty(value = "Se si tratta di una pesona deceduta", required = true)
    private Boolean deceduta;

    @ApiModelProperty(value = "I dati dell'eventuale ditta individuale")
    private ImpresaIndividualeDto impresaIndividuale;

    public Boolean getDeceduta() {
        return deceduta;
    }

    public void setDeceduta(Boolean deceduta) {
        this.deceduta = deceduta;
    }

    public AnagraficaDtoV2 getAnagrafica() {
        return anagrafica;
    }

    public void setAnagrafica(AnagraficaDtoV2 anagrafica) {
        this.anagrafica = anagrafica;
    }

    public ImpresaIndividualeDto getImpresaIndividuale() {
        return impresaIndividuale;
    }

    public void setImpresaIndividuale(ImpresaIndividualeDto impresaIndividuale) {
        this.impresaIndividuale = impresaIndividuale;
    }
}
