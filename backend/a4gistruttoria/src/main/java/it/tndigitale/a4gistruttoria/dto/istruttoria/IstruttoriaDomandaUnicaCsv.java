package it.tndigitale.a4gistruttoria.dto.istruttoria;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IstruttoriaDomandaUnicaCsv {
	
	@JsonProperty("CUAA")
	private String cuaa;
	
	@JsonProperty("NUMERO_DOMANDA")
	private String numeroDomanda;
	
	@JsonProperty("DESCRIZIONE_IMPRESA")
	private String descrizioneImpresa;

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public String getDescrizioneImpresa() {
		return descrizioneImpresa;
	}

	public void setDescrizioneImpresa(String descrizioneImpresa) {
		this.descrizioneImpresa = descrizioneImpresa;
	}
}
