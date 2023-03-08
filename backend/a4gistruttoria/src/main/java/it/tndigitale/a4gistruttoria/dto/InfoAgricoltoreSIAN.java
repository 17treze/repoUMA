package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;
import java.util.Date;

public class InfoAgricoltoreSIAN {

	private long idRaia;
	private BigDecimal annoCamp;
	private String codFisc;
	private Date dataFineAsse;
	private Date dataFinePossRequ;
	private Date dataInizAsse;
	private Date dataInizPossRequ;
	private String dtlCapxaction;
	private String dtlCapxtimestamp;
	private BigDecimal flagAgriAtti;
	private BigDecimal idOrpa;
	private BigDecimal idSogg;

	public InfoAgricoltoreSIAN() {

		// empty constructor
	}

	public long getIdRaia() {
		return idRaia;
	}

	public void setIdRaia(long idRaia) {
		this.idRaia = idRaia;
	}

	public BigDecimal getAnnoCamp() {
		return annoCamp;
	}

	public void setAnnoCamp(BigDecimal annoCamp) {
		this.annoCamp = annoCamp;
	}

	public String getCodFisc() {
		return codFisc;
	}

	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}

	public Date getDataFineAsse() {
		return dataFineAsse;
	}

	public void setDataFineAsse(Date dataFineAsse) {
		this.dataFineAsse = dataFineAsse;
	}

	public Date getDataFinePossRequ() {
		return dataFinePossRequ;
	}

	public void setDataFinePossRequ(Date dataFinePossRequ) {
		this.dataFinePossRequ = dataFinePossRequ;
	}

	public Date getDataInizAsse() {
		return dataInizAsse;
	}

	public void setDataInizAsse(Date dataInizAsse) {
		this.dataInizAsse = dataInizAsse;
	}

	public Date getDataInizPossRequ() {
		return dataInizPossRequ;
	}

	public void setDataInizPossRequ(Date dataInizPossRequ) {
		this.dataInizPossRequ = dataInizPossRequ;
	}

	public String getDtlCapxaction() {
		return dtlCapxaction;
	}

	public void setDtlCapxaction(String dtlCapxaction) {
		this.dtlCapxaction = dtlCapxaction;
	}

	public String getDtlCapxtimestamp() {
		return dtlCapxtimestamp;
	}

	public void setDtlCapxtimestamp(String dtlCapxtimestamp) {
		this.dtlCapxtimestamp = dtlCapxtimestamp;
	}

	public BigDecimal getFlagAgriAtti() {
		return flagAgriAtti;
	}

	public void setFlagAgriAtti(BigDecimal flagAgriAtti) {
		this.flagAgriAtti = flagAgriAtti;
	}

	public BigDecimal getIdOrpa() {
		return idOrpa;
	}

	public void setIdOrpa(BigDecimal idOrpa) {
		this.idOrpa = idOrpa;
	}

	public BigDecimal getIdSogg() {
		return idSogg;
	}

	public void setIdSogg(BigDecimal idSogg) {
		this.idSogg = idSogg;
	}

}