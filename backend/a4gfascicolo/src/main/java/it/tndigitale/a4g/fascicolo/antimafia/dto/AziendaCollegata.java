
package it.tndigitale.a4g.fascicolo.antimafia.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"cciaa", "denominazione", "codiceFiscale", "comuneSede", "indirizzoSede" , "nCivico" , "provinciaSede", "capSede",
		"carica", "selezionato" })
public class AziendaCollegata {

	@JsonProperty("cciaa")
	private String cciaa;
	@JsonProperty("denominazione")
	private String denominazione;
	@JsonProperty("codiceFiscale")
	private String codiceFiscale;
	@JsonProperty("comuneSede")
	private String comuneSede;
	@JsonProperty("indirizzoSede")
	private String indirizzoSede;
	@JsonProperty("nCivico")
	private String nCivico;
	@JsonProperty("provinciaSede")
	private String provinciaSede;
	@JsonProperty("capSede")
	private String capSede;
	@JsonProperty("carica")
	private List<Carica> carica;
	@JsonProperty("selezionato")
	private boolean selezionato;
    @JsonProperty("dettaglioImpresa")
    private DettaglioImpresa dettaglioImpresa;

	@JsonProperty("cciaa")
	public String getCciaa() {
		return cciaa;
	}

	@JsonProperty("cciaa")
	public void setCciaa(String cciaa) {
		this.cciaa = cciaa;
	}
	
	@JsonProperty("denominazione")
	public String getDenominazione() {
		return denominazione;
	}

	@JsonProperty("denominazione")
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	@JsonProperty("codiceFiscale")
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	@JsonProperty("codiceFiscale")
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	@JsonProperty("comuneSede")
	public String getComuneSede() {
		return comuneSede;
	}

	@JsonProperty("comuneSede")
	public void setComuneSede(String comuneSede) {
		this.comuneSede = comuneSede;
	}

	@JsonProperty("indirizzoSede")
	public String getIndirizzoSede() {
		return indirizzoSede;
	}

	@JsonProperty("indirizzoSede")
	public void setIndirizzoSede(String indirizzoSede) {
		this.indirizzoSede = indirizzoSede;
	}
	
	@JsonProperty("nCivico")
	public String getnCivico() {
		return nCivico;
	}
	
	@JsonProperty("nCivico")
	public void setnCivico(String nCivico) {
		this.nCivico = nCivico;
	}

	@JsonProperty("provinciaSede")
	public String getProvinciaSede() {
		return provinciaSede;
	}

	@JsonProperty("provinciaSede")
	public void setProvinciaSede(String provinciaSede) {
		this.provinciaSede = provinciaSede;
	}

	@JsonProperty("capSede")
	public String getCapSede() {
		return capSede;
	}

	@JsonProperty("capSede")
	public void setCapSede(String capSede) {
		this.capSede = capSede;
	}

	@JsonProperty("carica")
	public List<Carica> getCarica() {
		return carica;
	}

	@JsonProperty("carica")
	public void setCarica(List<Carica> carica) {
		this.carica = carica;
	}

	@JsonProperty("selezionato")
	public boolean isSelezionato() {
		return selezionato;
	}

	@JsonProperty("selezionato")
	public void setSelezionato(boolean selezionato) {
		this.selezionato = selezionato;
	}

	public DettaglioImpresa getDettaglioImpresa() {
		return dettaglioImpresa;
	}

	public void setDettaglioImpresa(DettaglioImpresa dettaglioImpresa) {
		this.dettaglioImpresa = dettaglioImpresa;
	}

}
