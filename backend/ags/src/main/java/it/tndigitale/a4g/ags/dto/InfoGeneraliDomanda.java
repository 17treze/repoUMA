package it.tndigitale.a4g.ags.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import it.tndigitale.a4g.ags.model.StatoDomanda;

/**
 * DTO dei dati generali di domanda, inclusi i dati identificanti.
 * 
 * @author F. Marani
 *
 */
public class InfoGeneraliDomanda {
	private String pac;
	private String tipoDomanda;
	private BigDecimal campagna;
	private String modulo;
	private String codModulo;
	private String cuaaIntestatario;
	private String ragioneSociale;
	private Long numeroDomanda;
	private Long numeroDomandaRettificata;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date dataPresentazione;
	private String codEnteCompilatore;
	private String enteCompilatore;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date dataProtocollazione;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date dataPresentazOriginaria;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date dataProtocollazOriginaria;
	private StatoDomanda stato;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date dataPassaggioStato;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date dtProtocollazioneUltimaModifica;
	private BigDecimal numeroDomandaUltimaModifica;

	public StatoDomanda getStato() {
		return stato;
	}

	public InfoGeneraliDomanda setStato(StatoDomanda stato) {
		this.stato = stato;
		return this;
	}

	public Date getDataPassaggioStato() {
		return dataPassaggioStato;
	}

	public void setDataPassaggioStato(Date dataPassaggioStato) {
		this.dataPassaggioStato = dataPassaggioStato;
	}

	public InfoGeneraliDomanda() {
	}

	public String getPac() {
		return pac;
	}

	public void setPac(String pac) {
		this.pac = pac;
	}

	public String getTipoDomanda() {
		return tipoDomanda;
	}

	public void setTipoDomanda(String tipoDomanda) {
		this.tipoDomanda = tipoDomanda;
	}

	public BigDecimal getCampagna() {
		return campagna;
	}

	public void setCampagna(BigDecimal campagna) {
		this.campagna = campagna;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public String getCodModulo() {
		return codModulo;
	}

	public void setCodModulo(String codModulo) {
		this.codModulo = codModulo;
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

	public Long getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(Long numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public Long getNumeroDomandaRettificata() {
		return numeroDomandaRettificata;
	}

	public void setNumeroDomandaRettificata(Long numeroDomandaRettificata) {
		this.numeroDomandaRettificata = numeroDomandaRettificata;
	}

	public Date getDataPresentazione() {
		return dataPresentazione;
	}

	public void setDataPresentazione(Date dataPresentazione) {
		this.dataPresentazione = dataPresentazione;
	}

	public String getCodEnteCompilatore() {
		return codEnteCompilatore;
	}

	public void setCodEnteCompilatore(String codEnteCompilatore) {
		this.codEnteCompilatore = codEnteCompilatore;
	}

	public String getEnteCompilatore() {
		return enteCompilatore;
	}

	public void setEnteCompilatore(String enteCompilatore) {
		this.enteCompilatore = enteCompilatore;
	}

	public Date getDataProtocollazione() {
		return dataProtocollazione;
	}

	public void setDataProtocollazione(Date dataProtocollazione) {
		this.dataProtocollazione = dataProtocollazione;
	}

	public Date getDataPresentazOriginaria() {
		return dataPresentazOriginaria;
	}

	public void setDataPresentazOriginaria(Date dataPresentazOriginaria) {
		this.dataPresentazOriginaria = dataPresentazOriginaria;
	}

	public Date getDataProtocollazOriginaria() {
		return dataProtocollazOriginaria;
	}

	public void setDataProtocollazOriginaria(Date dataProtocollazOriginaria) {
		this.dataProtocollazOriginaria = dataProtocollazOriginaria;
	}

	public Date getDtProtocollazioneUltimaModifica() {
		return dtProtocollazioneUltimaModifica;
	}

	public void setDtProtocollazioneUltimaModifica(Date dtProtocollazioneUltimaModifica) {
		this.dtProtocollazioneUltimaModifica = dtProtocollazioneUltimaModifica;
	}

	public BigDecimal getNumeroDomandaUltimaModifica() {
		return numeroDomandaUltimaModifica;
	}

	public void setNumeroDomandaUltimaModifica(BigDecimal numeroDomandaUltimaModifica) {
		this.numeroDomandaUltimaModifica = numeroDomandaUltimaModifica;
	}

}
