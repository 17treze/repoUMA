package it.tndigitale.a4gutente.dto;

public class Persona extends EntitaDominio {

	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String nrProtocolloPrivacyGenerale;
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNrProtocolloPrivacyGenerale() {
		return nrProtocolloPrivacyGenerale;
	}
	public void setNrProtocolloPrivacyGenerale(String nrProtocolloPrivacyGenerale) {
		this.nrProtocolloPrivacyGenerale = nrProtocolloPrivacyGenerale;
	}
}
