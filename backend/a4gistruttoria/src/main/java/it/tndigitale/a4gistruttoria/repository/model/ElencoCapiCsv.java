package it.tndigitale.a4gistruttoria.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ElencoCapiCsv {

	@JsonProperty("id_capo_bdn")
	private String idCapoBdn;
	@JsonProperty("codice_marca")
	private String codiceMarca;
	@JsonProperty("codice_intervento")
	private String codiceIntervento;
	@JsonProperty("codice_fiscale_richiedente")
	private String codiceFiscaleRichiedente;
	@JsonProperty("codice_organismo_pagatore")
	private String codiceOrganismoPagatore;
	@JsonProperty("codice_asl")
	private String codiceAsl;
	@JsonProperty("id_alle_bdn")
	private String idAlleBdn;
	@JsonProperty("data")
	private String data;
	@JsonProperty("anno")
	private String anno;

	public String getIdCapoBdn() {
		return idCapoBdn;
	}

	public void setIdCapoBdn(String idCapoBdn) {
		this.idCapoBdn = idCapoBdn;
	}

	public String getCodiceMarca() {
		return codiceMarca;
	}

	public void setCodiceMarca(String codiceMarca) {
		this.codiceMarca = codiceMarca;
	}

	public String getCodiceIntervento() {
		return codiceIntervento;
	}

	public void setCodiceIntervento(String codiceIntervento) {
		this.codiceIntervento = codiceIntervento;
	}

	public String getCodiceFiscaleRichiedente() {
		return codiceFiscaleRichiedente;
	}

	public void setCodiceFiscaleRichiedente(String codiceFiscaleRichiedente) {
		this.codiceFiscaleRichiedente = codiceFiscaleRichiedente;
	}

	public String getCodiceAsl() {
		return codiceAsl;
	}

	public void setCodiceAsl(String codiceAsl) {
		this.codiceAsl = codiceAsl;
	}

	public String getIdAlleBdn() {
		return idAlleBdn;
	}

	public void setIdAlleBdn(String idAlleBdn) {
		this.idAlleBdn = idAlleBdn;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getCodiceOrganismoPagatore() {
		return codiceOrganismoPagatore;
	}

	public void setCodiceOrganismoPagatore(String codiceOrganismoPagatore) {
		this.codiceOrganismoPagatore = codiceOrganismoPagatore;
	}
}
