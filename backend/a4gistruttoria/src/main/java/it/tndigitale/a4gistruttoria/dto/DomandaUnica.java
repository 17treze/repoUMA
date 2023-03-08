package it.tndigitale.a4gistruttoria.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.tndigitale.a4gistruttoria.dto.domandaunica.ErroreControlloRicevibilitaDomanda;

import com.fasterxml.jackson.annotation.JsonView;

public class DomandaUnica {

	private Long id;
	private InfoGeneraliDomanda infoGeneraliDomanda;
	private InfoIstruttoriaDomanda infoIstruttoriaDomanda;
	private InfoLiquidabilita infoLiquidabilita;
	private Richieste richieste;
	@JsonInclude(Include.NON_EMPTY)
	private String codiceElenco;
	private String identificativoDI;
	private LocalDateTime dataDI;
	private Boolean erroreCalcolo;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dataUltimoCalcolo;
	private List<CalcoloSostegnoAgs> calcoliSostegno;
	private List<ErroreControlloRicevibilitaDomanda> tipologiaControllo;

	public DomandaUnica(Long id, InfoGeneraliDomanda infoGeneraliDomanda, Richieste richieste) {
		this.id = id;
		this.infoGeneraliDomanda = infoGeneraliDomanda;
		this.richieste = richieste;
	}

	public DomandaUnica() {

	}

	@JsonView(Pagina.class)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonView(Pagina.class)
	public InfoGeneraliDomanda getInfoGeneraliDomanda() {
		return infoGeneraliDomanda;
	}

	public void setInfoGeneraliDomanda(InfoGeneraliDomanda infoGeneraliDomanda) {
		this.infoGeneraliDomanda = infoGeneraliDomanda;
	}

	@JsonView(Pagina.class)
	public Richieste getRichieste() {
		return richieste;
	}

	public void setRichieste(Richieste richieste) {
		this.richieste = richieste;
	}

	@JsonView(Pagina.class)
	public String getCodiceElenco() {
		return codiceElenco;
	}

	public void setCodiceElenco(String codiceElenco) {
		this.codiceElenco = codiceElenco;
	}

	public String getIdentificativoDI() {
		return identificativoDI;
	}

	public void setIdentificativoDI(String identificativoDI) {
		this.identificativoDI = identificativoDI;
	}

	public LocalDateTime getDataDI() {
		return dataDI;
	}

	public void setDataDI(LocalDateTime dataDI) {
		this.dataDI = dataDI;
	}

	public InfoIstruttoriaDomanda getInfoIstruttoriaDomanda() {
		return infoIstruttoriaDomanda;
	}

	public InfoLiquidabilita getInfoLiquidabilita() {
		return infoLiquidabilita;
	}

	public void setInfoIstruttoriaDomanda(InfoIstruttoriaDomanda infoIstruttoriaDomanda) {
		this.infoIstruttoriaDomanda = infoIstruttoriaDomanda;
	}

	public void setInfoLiquidabilita(InfoLiquidabilita infoLiquidabilita) {
		this.infoLiquidabilita = infoLiquidabilita;
	}

	public Boolean getErroreCalcolo() {
		return this.erroreCalcolo;
	}

	public void setErroreCalcolo(Boolean erroreCalcolo) {
		this.erroreCalcolo = erroreCalcolo;
	}

	public LocalDateTime getDataUltimoCalcolo() {
		return dataUltimoCalcolo;
	}

	public void setDataUltimoCalcolo(LocalDateTime dataUltimoCalcolo) {
		this.dataUltimoCalcolo = dataUltimoCalcolo;
	}
	
	public List<CalcoloSostegnoAgs> getCalcoliSostegno() {
		return calcoliSostegno;
	}

	public void setCalcoliSostegno(List<CalcoloSostegnoAgs> calcoliSostegno) {
		this.calcoliSostegno = calcoliSostegno;
	}

	public List<ErroreControlloRicevibilitaDomanda> getTipologiaControllo() {
		return tipologiaControllo;
	}

	public void setTipologiaControllo(List<ErroreControlloRicevibilitaDomanda> tipologiaControllo) {
		this.tipologiaControllo = tipologiaControllo;
	}
}
