package it.tndigitale.a4g.ags.dto;

public class Utente {
	private String utenza;
	private String cf;
	private String descrizione;
	private String nome;
	private String cognome;
	
	public String getUtenza() {
		return utenza;
	}
	public void setUtenza(String utenza) {
		this.utenza = utenza;
	}
	public String getCf() {
		return cf;
	}
	public void setCf(String cf) {
		this.cf = cf;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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
}
