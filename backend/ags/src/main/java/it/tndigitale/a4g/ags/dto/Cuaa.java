package it.tndigitale.a4g.ags.dto;

import java.math.BigDecimal;
import java.util.Date;

public class Cuaa {

	private String codiceFiscale;

	private String cognome;

	private String nome;

	private String sesso;

	private Date dataNascita;

	private String codiceIstatNascita;

	private String siglaProvNacita;
	
	private String comuneNascita;

	private String indirizzoRecapito;

	private BigDecimal codiceIstatRecapito;

	private String siglaProvRecapito;
	
	private String comuneRecapito;

	private BigDecimal cap;

	public Cuaa() {

	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
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

	public String getSesso() {
		return sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getCodiceIstatNascita() {
		return codiceIstatNascita;
	}

	public void setCodiceIstatNascita(String codiceIstatNascita) {
		this.codiceIstatNascita = codiceIstatNascita;
	}

	public String getSiglaProvNacita() {
		return siglaProvNacita;
	}

	public void setSiglaProvNacita(String siglaProvNacita) {
		this.siglaProvNacita = siglaProvNacita;
	}

	public String getComuneNascita() {
		return comuneNascita;
	}

	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}

	public String getIndirizzoRecapito() {
		return indirizzoRecapito;
	}

	public void setIndirizzoRecapito(String indirizzoRecapito) {
		this.indirizzoRecapito = indirizzoRecapito;
	}

	public BigDecimal getCodiceIstatRecapito() {
		return codiceIstatRecapito;
	}

	public void setCodiceIstatRecapito(BigDecimal codiceIstatRecapito) {
		this.codiceIstatRecapito = codiceIstatRecapito;
	}

	public String getSiglaProvRecapito() {
		return siglaProvRecapito;
	}

	public void setSiglaProvRecapito(String siglaProvRecapito) {
		this.siglaProvRecapito = siglaProvRecapito;
	}

	public String getComuneRecapito() {
		return comuneRecapito;
	}

	public void setComuneRecapito(String comuneRecapito) {
		this.comuneRecapito = comuneRecapito;
	}

	public BigDecimal getCap() {
		return cap;
	}

	public void setCap(BigDecimal cap) {
		this.cap = cap;
	}

}
