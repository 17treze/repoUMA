/**
 * 
 */
package it.tndigitale.a4gutente.dto;

/**
 * @author it417
 *
 */
public class DatiAnagrafici {
	
	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String email;
	private String telefono;

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public DatiAnagrafici setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
		return this;
	}

	public String getNome() {
		return nome;
	}

	public DatiAnagrafici setNome(String nome) {
		this.nome = nome;
		return this;
	}

	public String getCognome() {
		return cognome;
	}

	public DatiAnagrafici setCognome(String cognome) {
		this.cognome = cognome;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public DatiAnagrafici setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getTelefono() {
		return telefono;
	}

	public DatiAnagrafici setTelefono(String telefono) {
		this.telefono = telefono;
		return this;
	}

}
