package it.tndigitale.a4g.srt.dto;

public class Utente {

	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String ruolo;
	private String ente;
	
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
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	
	public String getRuolo() {
		return ruolo;
	}
	
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	
	public String getEnte() {
		return ente;
	}
	
	public void setEnte(String ente) {
		this.ente = ente;
	}
}
