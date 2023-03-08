package it.tndigitale.a4gistruttoria.dto.zootecnia;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;

@ApiModel(description = "Rappresenta il modello dei filtri di ricerca degli esiti calcolo capi zootecnia")
public class EsitoCapiFilter implements Serializable {

	private static final long serialVersionUID = -2008977254089508427L;

	@ApiParam(value="Id dell'allevamento impegnato", required = true)
	private Long idAllevamento;
	
	@ApiParam(value="Identificativo del capo", required = false)
	private String codiceCapo;
	
	@ApiParam(value="Check capo richiesto", required = false)
	private Boolean richiesto;
	
	@ApiParam(value="Anno di campagna", required = false)
	private Integer campagna;
	
	private Boolean fetchCapotraking;

	public Long getIdAllevamento() {
		return idAllevamento;
	}

	public void setIdAllevamento(Long idAllevamento) {
		this.idAllevamento = idAllevamento;
	}

	public String getCodiceCapo() {
		return codiceCapo;
	}

	public void setCodiceCapo(String codiceCapo) {
		this.codiceCapo = codiceCapo;
	}
	
	public Boolean getRichiesto() {
		return richiesto;
	}

	public void setRichiesto(Boolean richiesto) {
		this.richiesto = richiesto;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public Boolean getFetchCapotraking() {
		return fetchCapotraking;
	}

	public void setFetchCapotraking(Boolean fetchCapotraking) {
		this.fetchCapotraking = fetchCapotraking;
	}
	
}
