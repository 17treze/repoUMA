package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;

public class ImportaDatiStrutturaliHandler {
	private String cuaa;
	private Date dataPresentazione;
	private JsonNode importoRichiesto;
	private BigDecimal importo;
	private DomandaCollegata domandaCollegata;

	public ImportaDatiStrutturaliHandler() {
		super();
	}

	public ImportaDatiStrutturaliHandler(String cuaa, Date dataPresentazione, JsonNode importoRichiesto, BigDecimal importo) {
		super();
		this.cuaa = cuaa;
		this.dataPresentazione = dataPresentazione;
		this.importoRichiesto = importoRichiesto;
		this.importo = importo;
	}

	public String getCuaa() {
		return cuaa;
	}

	public Date getDataPresentazione() {
		return dataPresentazione;
	}

	public JsonNode getImportoRichiesto() {
		return importoRichiesto;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public void setDataPresentazione(Date dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	public void setImportoRichiesto(JsonNode importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
	}

	public BigDecimal getImporto() {
		return importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	
	public DomandaCollegata getDomandaCollegata() {
		return domandaCollegata;
	}

	public void setDomandaCollegata(DomandaCollegata domandaCollegata) {
		this.domandaCollegata = domandaCollegata;
	}

}
