package it.tndigitale.a4g.srt.dto;

import java.util.Objects;

import io.swagger.annotations.ApiParam;

public class UtenteFilter {

	@ApiParam(value="Codice fiscale dell'utente da cercare (inizia con)", required = false)
	private String codiceFiscale;
	
	@ApiParam(value="Nome dell'utente da cercare (contains)", required = false)
	private String nome;
	
	@ApiParam(value="Cognome dell'utente da cercare (contains)", required = false)
	private String cognome;
	
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
	
	@Override
	public int hashCode() {
		return Objects.hash(codiceFiscale, cognome, nome);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UtenteFilter other = (UtenteFilter) obj;
		return Objects.equals(codiceFiscale, other.codiceFiscale) && Objects.equals(cognome, other.cognome)
				&& Objects.equals(nome, other.nome);
	}
	
	@Override
	public String toString() {
		return "UtenteFilter [codiceFiscale=" + codiceFiscale + ", nome=" + nome + ", cognome=" + cognome + "]";
	}
}
