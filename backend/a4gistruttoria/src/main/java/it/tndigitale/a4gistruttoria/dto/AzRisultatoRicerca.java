package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AzRisultatoRicerca implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2042950226933625198L;
	
	private Long idDomanda;
	private String cuaaIntestatario;
	private Long numeroDomanda;
	private String ragioneSociale;
	private String idDomandaIntegrativa;
	private LocalDateTime dtUltimoAggiornamentoDi;
	private String codiceElenco;
	
	public Long getIdDomanda() {
		return idDomanda;
	}
	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}
	public String getCuaaIntestatario() {
		return cuaaIntestatario;
	}
	public void setCuaaIntestatario(String cuaaIntestatario) {
		this.cuaaIntestatario = cuaaIntestatario;
	}
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public String getIdDomandaIntegrativa() {
		return idDomandaIntegrativa;
	}
	public void setIdDomandaIntegrativa(String idDomandaIntegrativa) {
		this.idDomandaIntegrativa = idDomandaIntegrativa;
	}
	public LocalDateTime getDtUltimoAggiornamentoDi() {
		return dtUltimoAggiornamentoDi;
	}
	public void setDtUltimoAggiornamentoDi(LocalDateTime dtUltimoAggiornamentoDi) {
		this.dtUltimoAggiornamentoDi = dtUltimoAggiornamentoDi;
	}
	public Long getNumeroDomanda() {
		return numeroDomanda;
	}
	public void setNumeroDomanda(Long numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}
	public String getCodiceElenco() {
		return codiceElenco;
	}
	public void setCodiceElenco(String codiceElenco) {
		this.codiceElenco = codiceElenco;
	}

}
