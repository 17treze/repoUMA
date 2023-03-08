package it.tndigitale.a4g.fascicolo.territorio.dto;

public class CodificaColtura {
	private String codiceSuolo;
	private String codiceVarieta;
	private String codiceDestinazioneUso;
	private String codiceUso;
	private String codiceQualita;
	
	public String getCodiceSuolo() {
		return codiceSuolo;
	}
	public CodificaColtura setCodiceSuolo(String codiceSuolo) {
		this.codiceSuolo = codiceSuolo;
		return this;
	}
	public String getCodiceVarieta() {
		return codiceVarieta;
	}
	public CodificaColtura setCodiceVarieta(String codiceVarieta) {
		this.codiceVarieta = codiceVarieta;
		return this;
	}
	public String getCodiceDestinazioneUso() {
		return codiceDestinazioneUso;
	}
	public CodificaColtura setCodiceDestinazioneUso(String codiceDestinazioneUso) {
		this.codiceDestinazioneUso = codiceDestinazioneUso;
		return this;
	}
	public String getCodiceUso() {
		return codiceUso;
	}
	public CodificaColtura setCodiceUso(String codiceUso) {
		this.codiceUso = codiceUso;
		return this;
	}
	public String getCodiceQualita() {
		return codiceQualita;
	}
	public CodificaColtura setCodiceQualita(String codiceQualita) {
		this.codiceQualita = codiceQualita;
		return this;
	}
}
