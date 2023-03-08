package it.tndigitale.a4gistruttoria.dto;

import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;

public class DomandaUnicaRicercaFilter {
	
	private String cuaaIntestatario;
	
	private Long numeroDomanda;
	
	private String ragioneSociale;
	
	private String statoLavorazioneDomanda;
	
	private StatoDomanda statoDomanda;
	
	private Integer annoCampagna;

	public String getCuaaIntestatario() {
		return cuaaIntestatario;
	}
	
	public void setCuaaIntestatario(String cuaaIntestatario) {
		this.cuaaIntestatario = cuaaIntestatario;
	}

	public Long getNumeroDomanda() {
		return numeroDomanda;
	}
	
	public void setNumeroDomanda(Long numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}
	
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	
	public String getStatoLavorazioneDomanda() {
		return statoLavorazioneDomanda;
	}
	
	public void setStatoLavorazioneDomanda(String statoLavorazioneDomanda) {
		this.statoLavorazioneDomanda = statoLavorazioneDomanda;
	}
	
	public StatoDomanda getStatoDomanda() {
		return statoDomanda;
	}
	
	public void setStatoDomanda(final StatoDomanda statoDomanda) {
		this.statoDomanda = statoDomanda;
	}

	public Integer getAnnoCampagna() {
		return annoCampagna;
	}

	public void setAnnoCampagna(Integer annoCampagna) {
		this.annoCampagna = annoCampagna;
	}
}
