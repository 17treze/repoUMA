package it.tndigitale.a4g.fascicolo.anagrafica.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ReportValidazioneFascicoloPersonaGiuridicaDto {

	private String codiceFiscale;
	private String partitaIva;
	private String sedeLegale;
	private String ragioneSociale;
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getPartitaIva() {
		return partitaIva;
	}
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}
	public String getSedeLegale() {
		return sedeLegale;
	}
	public void setSedeLegale(String sedeLegale) {
		this.sedeLegale = sedeLegale;
	}
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}	
}
