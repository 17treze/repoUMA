package it.tndigitale.a4g.proxy.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Dichiarazione implements Serializable{
	
	private static final long serialVersionUID = -2475402384357612158L;
	//private static final String JSON_DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static final String JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
	
	
	private long idAnti;
	private String cuaa;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JSON_DATE_FORMAT)
	private Date dataAggi;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JSON_DATE_FORMAT)
	private Date dataFineVali;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JSON_DATE_FORMAT)
	private Date dataInizVali;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JSON_DATE_FORMAT)
	private Date dataInse;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JSON_DATE_FORMAT)
	private Date dataInviPref;
	private BigDecimal idAll1;
	private BigDecimal idAll2;
	private BigDecimal idOrpa;
	private BigDecimal tipoPers;
	
	public long getIdAnti() {
		return idAnti;
	}
	public void setIdAnti(long idAnti) {
		this.idAnti = idAnti;
	}
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public Date getDataAggi() {
		return dataAggi;
	}
	public void setDataAggi(Date dataAggi) {
		this.dataAggi = dataAggi;
	}
	public Date getDataFineVali() {
		return dataFineVali;
	}
	public void setDataFineVali(Date dataFineVali) {
		this.dataFineVali = dataFineVali;
	}
	public Date getDataInizVali() {
		return dataInizVali;
	}
	public void setDataInizVali(Date dataInizVali) {
		this.dataInizVali = dataInizVali;
	}
	public Date getDataInse() {
		return dataInse;
	}
	public void setDataInse(Date dataInse) {
		this.dataInse = dataInse;
	}
	public Date getDataInviPref() {
		return dataInviPref;
	}
	public void setDataInviPref(Date dataInviPref) {
		this.dataInviPref = dataInviPref;
	}
	public BigDecimal getIdAll1() {
		return idAll1;
	}
	public void setIdAll1(BigDecimal idAll1) {
		this.idAll1 = idAll1;
	}
	public BigDecimal getIdAll2() {
		return idAll2;
	}
	public void setIdAll2(BigDecimal idAll2) {
		this.idAll2 = idAll2;
	}
	public BigDecimal getIdOrpa() {
		return idOrpa;
	}
	public void setIdOrpa(BigDecimal idOrpa) {
		this.idOrpa = idOrpa;
	}
	public BigDecimal getTipoPers() {
		return tipoPers;
	}
	public void setTipoPers(BigDecimal tipoPers) {
		this.tipoPers = tipoPers;
	}
}
