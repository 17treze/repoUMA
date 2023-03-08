package it.tndigitale.a4g.fascicolo.antimafia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DichiarazioniAntimafiaCsv {
	
	@JsonProperty("CUAA")
	private String cuaa;
	@JsonProperty("STATO")
	private String stato;
	@JsonProperty("DESCRIZIONE_IMPRESA")
	private String descrizioneImpresa;
	
	
	public DichiarazioniAntimafiaCsv(String cuaa, String stato, String descrizioneImpresa) {
		super();
		this.cuaa = cuaa;
		this.stato = stato;
		this.descrizioneImpresa = descrizioneImpresa;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getDescrizioneImpresa() {
		return descrizioneImpresa;
	}

	public void setDescrizioneImpresa(String descrizioneImpresa) {
		this.descrizioneImpresa = descrizioneImpresa;
	}
	 
}
