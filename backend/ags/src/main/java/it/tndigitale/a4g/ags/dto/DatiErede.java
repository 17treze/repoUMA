package it.tndigitale.a4g.ags.dto;

import java.io.Serializable;
import java.util.Date;

public class DatiErede implements Serializable{
	private static final long serialVersionUID = -5702563402809233974L;
	
	private Long idSoggetto;
	private Long idPersona;
	private String cognome;
	private String nome;
	private String codiceFiscale;
	private String indirizzoResidenza;
	private String codBelfResidenza;
	private String provResidenza;
	private String capResidenza;
	private String iban;
	private String sesso;
	private Date dtNascita;
	private String codBelfNascita;
	private String provNascita;
	private Boolean ibanValido;
	private Boolean domandaSospesaAgea;
	private String codiceIstat;
	private String codiceIstatNascita;
	
	/**
	 * @return the cognome
	 */
	public String getCognome() {
		return cognome;
	}
	/**
	 * @param cognome the cognome to set
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	/**
	 * @return the codBelfResidenza
	 */
	public String getCodBelfResidenza() {
		return codBelfResidenza;
	}
	/**
	 * @param codBelfResidenza the codBelfResidenza to set
	 */
	public void setCodBelfResidenza(String codBelfResidenza) {
		this.codBelfResidenza = codBelfResidenza;
	}
	/**
	 * @return the provResidenza
	 */
	public String getProvResidenza() {
		return provResidenza;
	}
	/**
	 * @param provResidenza the provResidenza to set
	 */
	public void setProvResidenza(String provResidenza) {
		this.provResidenza = provResidenza;
	}
	/**
	 * @return the iban
	 */
	public String getIban() {
		return iban;
	}
	/**
	 * @param iban the iban to set
	 */
	public void setIban(String iban) {
		this.iban = iban;
	}
	/**
	 * @return the sesso
	 */
	public String getSesso() {
		return sesso;
	}
	/**
	 * @param sesso the sesso to set
	 */
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}
	/**
	 * @return the codBelfNascita
	 */
	public String getCodBelfNascita() {
		return codBelfNascita;
	}
	/**
	 * @param codBelfNascita the codBelfNascita to set
	 */
	public void setCodBelfNascita(String codBelfNascita) {
		this.codBelfNascita = codBelfNascita;
	}
	/**
	 * @return the provNascita
	 */
	public String getProvNascita() {
		return provNascita;
	}
	/**
	 * @param provNascita the provNascita to set
	 */
	public void setProvNascita(String provNascita) {
		this.provNascita = provNascita;
	}
	public Boolean getIbanValido() {
		return ibanValido;
	}
	public void setIbanValido(Boolean ibanValido) {
		this.ibanValido = ibanValido;
	}
	public Boolean getDomandaSospesaAgea() {
		return domandaSospesaAgea;
	}
	public void setDomandaSospesaAgea(Boolean domandaSospesaAgea) {
		this.domandaSospesaAgea = domandaSospesaAgea;
	}
	public Long getIdSoggetto() {
		return idSoggetto;
	}
	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getIndirizzoResidenza() {
		return indirizzoResidenza;
	}
	public void setIndirizzoResidenza(String indirizzoResidenza) {
		this.indirizzoResidenza = indirizzoResidenza;
	}
	public Long getIdPersona() {
		return idPersona;
	}
	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
	public Date getDtNascita() {
		return dtNascita;
	}
	public void setDtNascita(Date dtNascita) {
		this.dtNascita = dtNascita;
	}
	public String getCapResidenza() {
		return capResidenza;
	}
	public void setCapResidenza(String capResidenza) {
		this.capResidenza = capResidenza;
	}
	public String getCodiceIstat() {
		return codiceIstat;
	}
	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}
	public String getCodiceIstatNascita() {
		return codiceIstatNascita;
	}
	public void setCodiceIstatNascita(String codiceIstatNascita) {
		this.codiceIstatNascita = codiceIstatNascita;
	}
	

}
