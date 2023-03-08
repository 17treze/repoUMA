package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;

public class Istruttoria implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private BigDecimal annoRiferimento;
	private String codicePac;
	private String descrizioneDomanda;
	private String descrizionePac;
	private Date dtRicevibilita;
	private Date dtScadenzaDomande;
	private Date dtScadenzaDomandeRitardo;
	private Date dtScadenzaDomandeRitiro;
	private Date dtScadenzaDomandeRitiroparz;
	private String tipoDomanda;
	private BigDecimal percIncrementoGreening;
	private BigDecimal percIncrementoGiovane;
	private BigDecimal limiteGiovane;
	private BigDecimal percRiduzioneLineare1;
	private BigDecimal percRiduzioneLineare2;
	private BigDecimal percDisciplinaFinanziaria;
	private BigDecimal percRiduzioneLineare3;
	private BigDecimal percRiduzioneTitoli;
	private TipoIstruttoria tipoIstruttoria;
	private BigDecimal percPagamento;

	public Istruttoria() {

		// empty costructor
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAnnoRiferimento() {
		return this.annoRiferimento;
	}

	public void setAnnoRiferimento(BigDecimal annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	public String getCodicePac() {
		return this.codicePac;
	}

	public void setCodicePac(String codicePac) {
		this.codicePac = codicePac;
	}

	public String getDescrizioneDomanda() {
		return this.descrizioneDomanda;
	}

	public void setDescrizioneDomanda(String descrizioneDomanda) {
		this.descrizioneDomanda = descrizioneDomanda;
	}

	public String getDescrizionePac() {
		return this.descrizionePac;
	}

	public void setDescrizionePac(String descrizionePac) {
		this.descrizionePac = descrizionePac;
	}

	public Date getDtRicevibilita() {
		return this.dtRicevibilita;
	}

	public void setDtRicevibilita(Date dtRicevibilita) {
		this.dtRicevibilita = dtRicevibilita;
	}

	public Date getDtScadenzaDomande() {
		return this.dtScadenzaDomande;
	}

	public void setDtScadenzaDomande(Date dtScadenzaDomande) {
		this.dtScadenzaDomande = dtScadenzaDomande;
	}

	public Date getDtScadenzaDomandeRitardo() {
		return this.dtScadenzaDomandeRitardo;
	}

	public void setDtScadenzaDomandeRitardo(Date dtScadenzaDomandeRitardo) {
		this.dtScadenzaDomandeRitardo = dtScadenzaDomandeRitardo;
	}

	public Date getDtScadenzaDomandeRitiro() {
		return this.dtScadenzaDomandeRitiro;
	}

	public void setDtScadenzaDomandeRitiro(Date dtScadenzaDomandeRitiro) {
		this.dtScadenzaDomandeRitiro = dtScadenzaDomandeRitiro;
	}

	public Date getDtScadenzaDomandeRitiroparz() {
		return this.dtScadenzaDomandeRitiroparz;
	}

	public void setDtScadenzaDomandeRitiroparz(Date dtScadenzaDomandeRitiroparz) {
		this.dtScadenzaDomandeRitiroparz = dtScadenzaDomandeRitiroparz;
	}

	public String getTipoDomanda() {
		return this.tipoDomanda;
	}

	public void setTipoDomanda(String tipoDomanda) {
		this.tipoDomanda = tipoDomanda;
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

	public TipoIstruttoria getTipoIstruttoria() {
		return tipoIstruttoria;
	}

	public void setTipoIstruttoria(TipoIstruttoria tipoIstruttoria) {
		this.tipoIstruttoria = tipoIstruttoria;
	}

	public BigDecimal getPercPagamento() {
		return percPagamento;
	}

	public void setPercPagamento(BigDecimal percPagamento) {
		this.percPagamento = percPagamento;
	}

}