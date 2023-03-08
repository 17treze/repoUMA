package it.tndigitale.a4g.uma.business.service.elenchi;

public class ElencoBaseTemplate {

	private String ente;
	private String cuaa;
	private String denominazione;

	private Long idDomanda;
	private Long campagna;

	public String getEnte() {
		return ente;
	}
	public ElencoBaseTemplate setEnte(String ente) {
		this.ente = ente;
		return this;
	}

	public String getCuaa() {
		return cuaa;
	}
	public ElencoBaseTemplate setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}

	public String getDenominazione() {
		return denominazione;
	}
	public ElencoBaseTemplate setDenominazione(String denominazione) {
		this.denominazione = denominazione;
		return this;
	}

	public Long getIdDomanda() {
		return idDomanda;
	}
	public ElencoBaseTemplate setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
		return this;
	}

	public Long getCampagna() {
		return campagna;
	}
	public ElencoBaseTemplate setCampagna(Long campagna) {
		this.campagna = campagna;
		return this;
	}
}
