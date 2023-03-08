package it.tndigitale.a4gistruttoria.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DomandeCollegateCsv {
	

	@JsonProperty("CODI_FISC")
	private String codiceFiscale;
	@JsonProperty("TIPO_RICH")
	private String tipoRichiesta;
	@JsonProperty("NUME_IMPO")
	private String importoRichiesto;
	
	public DomandeCollegateCsv(String codiceFiscale, String tipoRichiesta, String importoRichiesto) {
		super();
		this.codiceFiscale = codiceFiscale;
		this.tipoRichiesta = tipoRichiesta;
		this.importoRichiesto = importoRichiesto;
	}
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getTipoRichiesta() {
		return tipoRichiesta;
	}
	public void setTipoRichiesta(String tipoRichiesta) {
		this.tipoRichiesta = tipoRichiesta;
	}
	public String getImportoRichiesto() {
		return importoRichiesto;
	}
	public void setImportoRichiesto(String importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
	} 

}
