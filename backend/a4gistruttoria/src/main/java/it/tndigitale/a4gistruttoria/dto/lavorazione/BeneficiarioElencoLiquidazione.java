package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.math.BigDecimal;
import java.util.Date;

import it.tndigitale.a4gistruttoria.repository.model.A4gtDatiErede;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;

@Deprecated
public class BeneficiarioElencoLiquidazione {
	
	private String ragioneSociale;
	private String iban;
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
	
	public BeneficiarioElencoLiquidazione(A4gtDatiErede datiErede) {
		this.ragioneSociale = datiErede.getCognome()+" "+datiErede.getNome();
		this.iban = datiErede.getIban();
		this.codiceFiscale = datiErede.getCodiceFiscale();
		this.cognome = datiErede.getCognome();
		this.nome = datiErede.getNome();
		this.sesso = datiErede.getSesso();
		this.dataNascita = datiErede.getDtNascita();
		this.codiceIstatNascita = datiErede.getCodIstatNascita();
		this.siglaProvNacita = datiErede.getProvinciaNascita();
		this.indirizzoRecapito = datiErede.getIndirizzo();
		this.codiceIstatRecapito = new BigDecimal(datiErede.getCodiceIstat());
		this.siglaProvRecapito = datiErede.getProvincia();
		this.cap = new BigDecimal(datiErede.getCap());
	}
	public BeneficiarioElencoLiquidazione(String iban, Cuaa infoCuaa, DomandaUnicaModel domanda) {
		this.ragioneSociale = domanda.getRagioneSociale();
		this.iban = iban;
		this.codiceFiscale = infoCuaa.getCodiceFiscale();
		this.cognome = infoCuaa.getCognome();
		this.nome = infoCuaa.getNome();
		this.sesso = infoCuaa.getSesso();
		this.dataNascita = infoCuaa.getDataNascita();
		this.codiceIstatNascita = infoCuaa.getCodiceIstatNascita();
		this.siglaProvNacita = infoCuaa.getSiglaProvNacita();
		this.indirizzoRecapito = infoCuaa.getIndirizzoRecapito();
		this.codiceIstatRecapito = infoCuaa.getCodiceIstatRecapito();
		this.siglaProvRecapito = infoCuaa.getSiglaProvRecapito();
		this.cap = new BigDecimal(infoCuaa.getCap().toString());
	}
	/**
	 * @return the ragioneSociale
	 */
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	/**
	 * @param ragioneSociale the ragioneSociale to set
	 */
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
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
	 * @return the codiceFiscale
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	/**
	 * @param codiceFiscale the codiceFiscale to set
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
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
	 * @return the dataNascita
	 */
	public Date getDataNascita() {
		return dataNascita;
	}
	/**
	 * @param dataNascita the dataNascita to set
	 */
	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}
	/**
	 * @return the codiceIstatNascita
	 */
	public String getCodiceIstatNascita() {
		return codiceIstatNascita;
	}
	/**
	 * @param codiceIstatNascita the codiceIstatNascita to set
	 */
	public void setCodiceIstatNascita(String codiceIstatNascita) {
		this.codiceIstatNascita = codiceIstatNascita;
	}
	/**
	 * @return the siglaProvNacita
	 */
	public String getSiglaProvNacita() {
		return siglaProvNacita;
	}
	/**
	 * @param siglaProvNacita the siglaProvNacita to set
	 */
	public void setSiglaProvNacita(String siglaProvNacita) {
		this.siglaProvNacita = siglaProvNacita;
	}
	/**
	 * @return the comuneNascita
	 */
	public String getComuneNascita() {
		return comuneNascita;
	}
	/**
	 * @param comuneNascita the comuneNascita to set
	 */
	public void setComuneNascita(String comuneNascita) {
		this.comuneNascita = comuneNascita;
	}
	/**
	 * @return the indirizzoRecapito
	 */
	public String getIndirizzoRecapito() {
		return indirizzoRecapito;
	}
	/**
	 * @param indirizzoRecapito the indirizzoRecapito to set
	 */
	public void setIndirizzoRecapito(String indirizzoRecapito) {
		this.indirizzoRecapito = indirizzoRecapito;
	}
	/**
	 * @return the codiceIstatRecapito
	 */
	public BigDecimal getCodiceIstatRecapito() {
		return codiceIstatRecapito;
	}
	/**
	 * @param codiceIstatRecapito the codiceIstatRecapito to set
	 */
	public void setCodiceIstatRecapito(BigDecimal codiceIstatRecapito) {
		this.codiceIstatRecapito = codiceIstatRecapito;
	}
	/**
	 * @return the siglaProvRecapito
	 */
	public String getSiglaProvRecapito() {
		return siglaProvRecapito;
	}
	/**
	 * @param siglaProvRecapito the siglaProvRecapito to set
	 */
	public void setSiglaProvRecapito(String siglaProvRecapito) {
		this.siglaProvRecapito = siglaProvRecapito;
	}
	/**
	 * @return the comuneRecapito
	 */
	public String getComuneRecapito() {
		return comuneRecapito;
	}
	/**
	 * @param comuneRecapito the comuneRecapito to set
	 */
	public void setComuneRecapito(String comuneRecapito) {
		this.comuneRecapito = comuneRecapito;
	}
	/**
	 * @return the cap
	 */
	public BigDecimal getCap() {
		return cap;
	}
	/**
	 * @param cap the cap to set
	 */
	public void setCap(BigDecimal cap) {
		this.cap = cap;
	}	

}
