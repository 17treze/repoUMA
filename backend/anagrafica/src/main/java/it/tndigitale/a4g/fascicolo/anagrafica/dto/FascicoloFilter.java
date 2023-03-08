package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;

@ApiModel(description = "Rappresenta il modello dei filtri di ricerca dei fascicoli")
public class FascicoloFilter implements Serializable {

	private static final long serialVersionUID = -8972470613918097937L;
	
	@ApiParam(value="CUAA delle istruttorie da ricercare", required = false)
	private String cuaa;
	
	@ApiParam(value="Ragione sociale/denominazione delle istruttorie da ricercare", required = false)
	private String ragioneSociale;
	
	@ApiParam(value="Lista codici utenti connesso. Valorizzato internamente", required = false, hidden=true)
	private List<String> entiUtenteConnesso;
	
	@ApiParam(value="Fascicoli che hanno una Revoca Ordinaria. Data di Revoca Ordinaria", required = false)
	private Boolean revocaOrdinariaMandato;

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public List<String> getEntiUtenteConnesso() {
		return entiUtenteConnesso;
	}

	public void setEntiUtenteConnesso(List<String> entiUtenteConnesso) {
		this.entiUtenteConnesso = entiUtenteConnesso;
	}
	public Boolean getRevocaOrdinariaMandato() {
		return revocaOrdinariaMandato;
	}
	public void setRevocaOrdinariaMandato(Boolean revocaOrdinariaMandato) {
		this.revocaOrdinariaMandato = revocaOrdinariaMandato;
	}
}
