package it.tndigitale.a4gistruttoria.dto.zootecnia;

import it.tndigitale.a4gistruttoria.repository.model.InterventoZootecnico;

import java.io.Serializable;

public class CapiAziendaPerInterventoFilter implements Serializable {
	
	private static final long serialVersionUID = -999087678680656106L;

    private String cuaa;

    private Integer campagna;

    private InterventoZootecnico intervento;

    private String cuaaSubentrante;

    private Integer idAllevamento;
    
    public CapiAziendaPerInterventoFilter(String cuaa, Integer campagna, InterventoZootecnico intervento, Integer idAllevamento, String cuaaSubentrante) {
		super();
		this.cuaa = cuaa;
		this.campagna = campagna;
		this.intervento = intervento;
		this.idAllevamento = idAllevamento;
		this.cuaaSubentrante = cuaaSubentrante;
	}
    
    public CapiAziendaPerInterventoFilter(String cuaa, Integer campagna, InterventoZootecnico intervento) {
		super();
		this.cuaa = cuaa;
		this.campagna = campagna;
		this.intervento = intervento;
	}

    public CapiAziendaPerInterventoFilter() {
		// TODO Auto-generated constructor stub
	}

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

    public InterventoZootecnico getIntervento() {
        return intervento;
    }

    public void setIntervento(InterventoZootecnico intervento) {
        this.intervento = intervento;
    }

    public String getCuaaSubentrante() {
        return cuaaSubentrante;
    }

    public void setCuaaSubentrante(String cuaaSubentrante) {
        this.cuaaSubentrante = cuaaSubentrante;
    }

    public Integer getIdAllevamento() {
        return idAllevamento;
    }

    public void setIdAllevamento(Integer idAllevamento) {
        this.idAllevamento = idAllevamento;
    }
}
