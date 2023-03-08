package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;
import java.util.Date;

import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;

public class IstruttoriaEdit {

	private BigDecimal percIncrementoGreening;
	private BigDecimal percIncrementoGiovane;
	private BigDecimal limiteGiovane;
	private BigDecimal percRiduzioneLineare1;
	private BigDecimal percRiduzioneLineare2;
	private BigDecimal percRiduzioneLineare3;
	private BigDecimal percRiduzioneTitoli;
	private BigDecimal percDisciplinaFinanziaria;
	private Date dtRicevibilita;
	private Date dtScadenzaDomande;
	private Date dtScadenzaDomandeRitardo;
	private Date dtScadenzaDomandeRitiroparz;
	private BigDecimal percPagamento;
	private TipoIstruttoria tipoIstruttoria;

	public IstruttoriaEdit() {
		// costruttore vuoto
	}

	public Date getDtRicevibilita() {
		return dtRicevibilita;
	}

	public void setDtRicevibilita(Date dtRicevibilita) {
		this.dtRicevibilita = dtRicevibilita;
	}

	public Date getDtScadenzaDomande() {
		return dtScadenzaDomande;
	}

	public void setDtScadenzaDomande(Date dtScadenzaDomande) {
		this.dtScadenzaDomande = dtScadenzaDomande;
	}

	public Date getDtScadenzaDomandeRitardo() {
		return dtScadenzaDomandeRitardo;
	}

	public void setDtScadenzaDomandeRitardo(Date dtScadenzaDomandeRitardo) {
		this.dtScadenzaDomandeRitardo = dtScadenzaDomandeRitardo;
	}

	public Date getDtScadenzaDomandeRitiroparz() {
		return dtScadenzaDomandeRitiroparz;
	}

	public void setDtScadenzaDomandeRitiroparz(Date dtScadenzaDomandeRitiroparz) {
		this.dtScadenzaDomandeRitiroparz = dtScadenzaDomandeRitiroparz;
	}

	public BigDecimal getPercIncrementoGreening() {
		return percIncrementoGreening;
	}

	public void setPercIncrementoGreening(BigDecimal percIncrementoGreening) {
		this.percIncrementoGreening = percIncrementoGreening;
	}

	public BigDecimal getPercIncrementoGiovane() {
		return percIncrementoGiovane;
	}

	public void setPercIncrementoGiovane(BigDecimal percIncrementoGiovane) {
		this.percIncrementoGiovane = percIncrementoGiovane;
	}

	public BigDecimal getLimiteGiovane() {
		return limiteGiovane;
	}

	public void setLimiteGiovane(BigDecimal limiteGiovane) {
		this.limiteGiovane = limiteGiovane;
	}

	public BigDecimal getPercRiduzioneLineare1() {
		return percRiduzioneLineare1;
	}

	public void setPercRiduzioneLineare1(BigDecimal percRiduzioneLineare1) {
		this.percRiduzioneLineare1 = percRiduzioneLineare1;
	}

	public BigDecimal getPercRiduzioneLineare2() {
		return percRiduzioneLineare2;
	}

	public void setPercRiduzioneLineare2(BigDecimal percRiduzioneLineare2) {
		this.percRiduzioneLineare2 = percRiduzioneLineare2;
	}

	public BigDecimal getPercDisciplinaFinanziaria() {
		return percDisciplinaFinanziaria;
	}

	public void setPercDisciplinaFinanziaria(BigDecimal percDisciplinaFinanziaria) {
		this.percDisciplinaFinanziaria = percDisciplinaFinanziaria;
	}

	public BigDecimal getPercRiduzioneLineare3() {
		return percRiduzioneLineare3;
	}

	public void setPercRiduzioneLineare3(BigDecimal percRiduzioneLineare3) {
		this.percRiduzioneLineare3 = percRiduzioneLineare3;
	}

	public BigDecimal getPercRiduzioneTitoli() {
		return percRiduzioneTitoli;
	}

	public void setPercRiduzioneTitoli(BigDecimal percRiduzioneTitoli) {
		this.percRiduzioneTitoli = percRiduzioneTitoli;
	}

	public BigDecimal getPercPagamento() {
		return percPagamento;
	}

	public void setPercPagamento(BigDecimal percPagamento) {
		this.percPagamento = percPagamento;
	}

	public TipoIstruttoria getTipoIstruttoria() {
		return tipoIstruttoria;
	}

	public void setTipoIstruttoria(TipoIstruttoria tipoIstruttoria) {
		this.tipoIstruttoria = tipoIstruttoria;
	}

}
