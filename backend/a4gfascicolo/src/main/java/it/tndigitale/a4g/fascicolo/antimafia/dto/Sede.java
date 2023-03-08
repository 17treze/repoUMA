package it.tndigitale.a4g.fascicolo.antimafia.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"provincia",
	"comune",
	"ccomune",
	"toponimo",
	"via",
	"ncivico",
	"cap",
	"stato",
	"frazione",
	"altreindicazioni",
	"stradario",
	"telefono",
	"fax",
	"indirizzopec"
})
public class Sede {

	@JsonProperty("provincia")
	public String provincia;
	@JsonProperty("comune")
	public String comune;
	@JsonProperty("ccomune")
	public String ccomune;
	@JsonProperty("toponimo")
	public String toponimo;
	@JsonProperty("via")
	public String via;
	@JsonProperty("ncivico")
	public String ncivico;
	@JsonProperty("cap")
	public String cap;
	@JsonProperty("stato")
	public String stato;
	@JsonProperty("frazione")
	public String frazione;
	@JsonProperty("altreindicazioni")
	public String altreindicazioni;
	@JsonProperty("stradario")
	public String stradario;
	@JsonProperty("telefono")
	public String telefono;
	@JsonProperty("fax")
	public String fax;
	
	
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	public String getCcomune() {
		return ccomune;
	}
	public void setCcomune(String ccomune) {
		this.ccomune = ccomune;
	}
	public String getToponimo() {
		return toponimo;
	}
	public void setToponimo(String toponimo) {
		this.toponimo = toponimo;
	}
	public String getVia() {
		return via;
	}
	public void setVia(String via) {
		this.via = via;
	}
	public String getNcivico() {
		return ncivico;
	}
	public void setNcivico(String ncivico) {
		this.ncivico = ncivico;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public String getFrazione() {
		return frazione;
	}
	public void setFrazione(String frazione) {
		this.frazione = frazione;
	}
	public String getAltreindicazioni() {
		return altreindicazioni;
	}
	public void setAltreindicazioni(String altreindicazioni) {
		this.altreindicazioni = altreindicazioni;
	}
	public String getStradario() {
		return stradario;
	}
	public void setStradario(String stradario) {
		this.stradario = stradario;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}

}
