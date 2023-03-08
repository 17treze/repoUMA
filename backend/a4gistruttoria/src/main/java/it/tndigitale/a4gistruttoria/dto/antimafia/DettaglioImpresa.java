
package it.tndigitale.a4gistruttoria.dto.antimafia;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DettaglioImpresa {

	@JsonProperty("formaGiuridicaCodice")
	private String formaGiuridicaCodice;
	@JsonProperty("formaGiuridicaDescrizione")
	private String formaGiuridicaDescrizione;
	@JsonProperty("denominazione")
	private String denominazione;
	@JsonProperty("sedeLegale")
	private String sedeLegale;
	@JsonProperty("estremiCCIAA")
	private EstremiCCIAA estremiCCIAA;
	@JsonProperty("oggettoSociale")
	private String oggettoSociale;
	@JsonProperty("codiceFiscale")
	private String codiceFiscale;
	@JsonProperty("partitaIva")
	private String partitaIva;
	@JsonProperty("indirizzoPEC")
	private String indirizzoPEC;
	@JsonProperty("dettaglioPersonaGiuridica")
	private DettaglioPersonaGiuridica dettaglioPersonaGiuridica;
	@JsonProperty("soggettiImpresa")
	private List<SoggettoImpresa> soggettiImpresa = null;
	@JsonProperty("aziendeCollegate")
	private List<AziendaCollegata> aziendeCollegate = null;


	@JsonProperty("formaGiuridicaCodice")
	public String getFormaGiuridicaCodice() {
		return formaGiuridicaCodice;
	}

	@JsonProperty("formaGiuridicaCodice")
	public void setFormaGiuridicaCodice(String formaGiuridicaCodice) {
		this.formaGiuridicaCodice = formaGiuridicaCodice;
	}

	@JsonProperty("formaGiuridicaDescrizione")
	public String getFormaGiuridicaDescrizione() {
		return formaGiuridicaDescrizione;
	}

	@JsonProperty("formaGiuridicaDescrizione")
	public void setFormaGiuridicaDescrizione(String formaGiuridicaDescrizione) {
		this.formaGiuridicaDescrizione = formaGiuridicaDescrizione;
	}

	@JsonProperty("denominazione")
	public String getDenominazione() {
		return denominazione;
	}

	@JsonProperty("denominazione")
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	@JsonProperty("sedeLegale")
	public String getSedeLegale() {
		return sedeLegale;
	}

	@JsonProperty("sedeLegale")
	public void setSedeLegale(String sedeLegale) {
		this.sedeLegale = sedeLegale;
	}

	@JsonProperty("estremiCCIAA")
	public EstremiCCIAA getEstremiCCIAA() {
		return estremiCCIAA;
	}

	@JsonProperty("estremiCCIAA")
	public void setEstremiCCIAA(EstremiCCIAA estremiCCIAA) {
		this.estremiCCIAA = estremiCCIAA;
	}

	@JsonProperty("oggettoSociale")
	public String getOggettoSociale() {
		return oggettoSociale;
	}

	@JsonProperty("oggettoSociale")
	public void setOggettoSociale(String oggettoSociale) {
		this.oggettoSociale = oggettoSociale;
	}

	@JsonProperty("codiceFiscale")
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	@JsonProperty("codiceFiscale")
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	@JsonProperty("partitaIva")
	public String getPartitaIva() {
		return partitaIva;
	}

	@JsonProperty("partitaIva")
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	@JsonProperty("indirizzoPEC")
	public String getIndirizzoPEC() {
		return indirizzoPEC;
	}

	@JsonProperty("indirizzoPEC")
	public void setIndirizzoPEC(String indirizzoPEC) {
		this.indirizzoPEC = indirizzoPEC;
	}

	@JsonProperty("dettaglioPersonaGiuridica")
	public DettaglioPersonaGiuridica getDettaglioPersonaGiuridica() {
		return dettaglioPersonaGiuridica;
	}

	@JsonProperty("dettaglioPersonaGiuridica")
	public void setDettaglioPersonaGiuridica(DettaglioPersonaGiuridica infoPG) {
		this.dettaglioPersonaGiuridica = infoPG;
	}

	@JsonProperty("soggettiImpresa")
	public List<SoggettoImpresa> getSoggettiImpresa() {
		return soggettiImpresa;
	}

	@JsonProperty("soggettiImpresa")
	public void setSoggettiImpresa(List<SoggettoImpresa> soggettiImpresa) {
		this.soggettiImpresa = soggettiImpresa;
	}

	@JsonProperty("aziendeCollegate")
	public List<AziendaCollegata> getAziendeCollegate() {
		return aziendeCollegate;
	}

	@JsonProperty("aziendeCollegate")
	public void setAziendeCollegate(List<AziendaCollegata> aziendeCollegate) {
		this.aziendeCollegate = aziendeCollegate;
	}

}
