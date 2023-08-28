package it.tndigitale.a4g.uma.dto.aual;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContoCorrenteAualDto {

	private String contCorr;
	private String codiSwift1;
	private String codiSwift2;
	private String codiAbii;
	private String descAbii;
	private String codiCabb;
	private String descCabb;
	private String codiSiglProv;
	private String descProv;
	private String descComu;
	private String dataIniz;
	private String dataFine;
	private String codiCapp;
	private String codiNumeCcor;
	private String codiNazi;
	private String checkDigit;
	private String codiPin;
	public String getContCorr() {
		return contCorr;
	}
	public void setContCorr(String contCorr) {
		this.contCorr = contCorr;
	}
	public String getCodiSwift1() {
		return codiSwift1;
	}
	public void setCodiSwift1(String codiSwift1) {
		this.codiSwift1 = codiSwift1;
	}
	public String getCodiSwift2() {
		return codiSwift2;
	}
	public void setCodiSwift2(String codiSwift2) {
		this.codiSwift2 = codiSwift2;
	}
	public String getCodiAbii() {
		return codiAbii;
	}
	public void setCodiAbii(String codiAbii) {
		this.codiAbii = codiAbii;
	}
	public String getDescAbii() {
		return descAbii;
	}
	public void setDescAbii(String descAbii) {
		this.descAbii = descAbii;
	}
	public String getCodiCabb() {
		return codiCabb;
	}
	public void setCodiCabb(String codiCabb) {
		this.codiCabb = codiCabb;
	}
	public String getDescCabb() {
		return descCabb;
	}
	public void setDescCabb(String descCabb) {
		this.descCabb = descCabb;
	}
	public String getCodiSiglProv() {
		return codiSiglProv;
	}
	public void setCodiSiglProv(String codiSiglProv) {
		this.codiSiglProv = codiSiglProv;
	}
	public String getDescProv() {
		return descProv;
	}
	public void setDescProv(String descProv) {
		this.descProv = descProv;
	}
	public String getDescComu() {
		return descComu;
	}
	public void setDescComu(String descComu) {
		this.descComu = descComu;
	}
	public String getDataIniz() {
		return dataIniz;
	}
	public void setDataIniz(String dataIniz) {
		this.dataIniz = dataIniz;
	}
	public String getDataFine() {
		return dataFine;
	}
	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}
	public String getCodiCapp() {
		return codiCapp;
	}
	public void setCodiCapp(String codiCapp) {
		this.codiCapp = codiCapp;
	}
	public String getCodiNumeCcor() {
		return codiNumeCcor;
	}
	public void setCodiNumeCcor(String codiNumeCcor) {
		this.codiNumeCcor = codiNumeCcor;
	}
	public String getCodiNazi() {
		return codiNazi;
	}
	public void setCodiNazi(String codiNazi) {
		this.codiNazi = codiNazi;
	}
	public String getCheckDigit() {
		return checkDigit;
	}
	public void setCheckDigit(String checkDigit) {
		this.checkDigit = checkDigit;
	}
	public String getCodiPin() {
		return codiPin;
	}
	public void setCodiPin(String codiPin) {
		this.codiPin = codiPin;
	}

}
