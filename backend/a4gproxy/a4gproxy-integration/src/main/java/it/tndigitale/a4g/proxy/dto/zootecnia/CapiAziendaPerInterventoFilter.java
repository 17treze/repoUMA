package it.tndigitale.a4g.proxy.dto.zootecnia;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "Rappresenta i criteri per ricercare i capi detenuti da una certa azienda che ha richiesto " +
        "un intervento per un certo anno di campagna")
public abstract class CapiAziendaPerInterventoFilter<T extends Enum<? extends Intervento>> implements Serializable {
    private static final long serialVersionUID = 891532017854287351L;

    @ApiModelProperty(hidden = true)
    private String cuaa;

    @ApiModelProperty(hidden = true)
    private Integer campagna;

    @ApiModelProperty(value = "Cuaa che e' subentrato al richiedente", required = false)
    private String cuaaSubentrante;

    @ApiModelProperty(value = "Identificativo tecnologico di BDN che identifica univocamente i capi con stesso detentore, proprietario nel medesimo allevamento",
            required = false)
    private Integer idAllevamento;


    public String getCuaa() {
        return cuaa;
    }

    public void setCuaa(String cuaa) {
        this.cuaa = cuaa;
    }

    public Integer getCampagna() {
        return campagna;
    }

    public void setCampagna(Integer campagna) {
        this.campagna = campagna;
    }

    public abstract T getIntervento();

    public abstract void setIntervento(T intervento);

    public String getCuaaSubentrante() {
        return cuaaSubentrante;
    }

    public void setCuaaSubentrante(String cuaaSubentrante) {
        this.cuaaSubentrante = cuaaSubentrante;
    }

    public abstract Integer getCodiceAgeaIntervento();

    public Integer getIdAllevamento() {
        return idAllevamento;
    }

    public void setIdAllevamento(Integer idAllevamento) {
        this.idAllevamento = idAllevamento;
    }
}
