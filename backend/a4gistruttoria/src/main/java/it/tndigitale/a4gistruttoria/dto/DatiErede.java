/**
 * 
 */
package it.tndigitale.a4gistruttoria.dto;

import java.util.Date;

/**
 * dati inerenti ad un erede riferito a una Domanda
 * @author B.Conetta
 *
 */
public class DatiErede {
	
	private Long id;
	private Boolean certificato;
	private String cognome;
	private String nome;
	private String codiceFiscale;
	private String iban;
	private String indirizzoResidenza;
	private String codiceIstat;
	private String provResidenza;
	private String capResidenza;
	private String sesso;
	private Date dtNascita;
	private String codIstatNascita;
	private String provNascita;
	private Date dtUltimoAggiornamento;
	
	public String getIndirizzoResidenza() {
		return indirizzoResidenza;
	}
	public void setIndirizzoResidenza(String indirizzoResidenza) {
		this.indirizzoResidenza = indirizzoResidenza;
	}
	public String getProvResidenza() {
		return provResidenza;
	}
	public void setProvResidenza(String provResidenza) {
		this.provResidenza = provResidenza;
	}
	public String getCapResidenza() {
		return capResidenza;
	}
	public void setCapResidenza(String capResidenza) {
		this.capResidenza = capResidenza;
	}
	public Date getDtNascita() {
		return dtNascita;
	}
	public void setDtNascita(Date dtNascita) {
		this.dtNascita = dtNascita;
	}
	public String getProvNascita() {
		return provNascita;
	}
	public void setProvNascita(String provNascita) {
		this.provNascita = provNascita;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getCertificato() {
		return certificato;
	}
	public void setCertificato(Boolean certificato) {
		this.certificato = certificato;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getCodiceIstat() {
		return codiceIstat;
	}
	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}

	public String getSesso() {
		return sesso;
	}
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public String getCodIstatNascita() {
		return codIstatNascita;
	}
	public void setCodIstatNascita(String codIstatNascita) {
		this.codIstatNascita = codIstatNascita;
	}

	public Date getDtUltimoAggiornamento() {
		return dtUltimoAggiornamento;
	}
	public void setDtUltimoAggiornamento(Date dtUltimoAggiornamento) {
		this.dtUltimoAggiornamento = dtUltimoAggiornamento;
	}
}
