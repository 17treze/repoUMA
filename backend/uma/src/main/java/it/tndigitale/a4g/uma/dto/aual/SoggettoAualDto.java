package it.tndigitale.a4g.uma.dto.aual;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SoggettoAualDto {
	
	private String descTipoAtti;
	private String dataInizAtti;
	private String flagStatAtti;
	private String codiFisc;
	private String codiPiva;
	private String descNatuGiur;
	private String descCogn;
	private String descNome;
	private String codiSex;
	private String dataNasc;
	private String descComuNasc;
	private String descProvNasc;
	private String descDeno;
	private String descStatAtti;
	private String flagAgriAtti;
	private String flagGiovAgri;
	private String flagPersFisi;
	private String dataFineAtti;
	private String dataAggi;
	private String codiFiscLuna;
	private String codiSiglProvNasc;
	private String descInte;
	private String dataInizPartIvaa;
	private String dataFinePartIvaa;
	private String cuaa;
	private List<RecapitoAualDto> recapito;
	private List<ContoCorrenteAualDto> contoCorrente;
	private List<RappresentanteLegaleAualDto> rappresentanteLegale;

	public List<RappresentanteLegaleAualDto> getRappresentanteLegale() {
		return rappresentanteLegale;
	}
	public void setRappresentanteLegale(List<RappresentanteLegaleAualDto> rappresentanteLegale) {
		this.rappresentanteLegale = rappresentanteLegale;
	}
	public String getDescTipoAtti() {
		return descTipoAtti;
	}
	public void setDescTipoAtti(String descTipoAtti) {
		this.descTipoAtti = descTipoAtti;
	}
	public String getDataInizAtti() {
		return dataInizAtti;
	}
	public void setDataInizAtti(String dataInizAtti) {
		this.dataInizAtti = dataInizAtti;
	}
	public String getFlagStatAtti() {
		return flagStatAtti;
	}
	public void setFlagStatAtti(String flagStatAtti) {
		this.flagStatAtti = flagStatAtti;
	}
	public String getCodiFisc() {
		return codiFisc;
	}
	public void setCodiFisc(String codiFisc) {
		this.codiFisc = codiFisc;
	}
	public String getCodiPiva() {
		return codiPiva;
	}
	public void setCodiPiva(String codiPiva) {
		this.codiPiva = codiPiva;
	}
	public String getDescNatuGiur() {
		return descNatuGiur;
	}
	public void setDescNatuGiur(String descNatuGiur) {
		this.descNatuGiur = descNatuGiur;
	}
	public String getDescCogn() {
		return descCogn;
	}
	public void setDescCogn(String descCogn) {
		this.descCogn = descCogn;
	}
	public String getDescNome() {
		return descNome;
	}
	public void setDescNome(String descNome) {
		this.descNome = descNome;
	}
	public String getCodiSex() {
		return codiSex;
	}
	public void setCodiSex(String codiSex) {
		this.codiSex = codiSex;
	}
	public String getDataNasc() {
		return dataNasc;
	}
	public void setDataNasc(String dataNasc) {
		this.dataNasc = dataNasc;
	}
	public String getDescComuNasc() {
		return descComuNasc;
	}
	public void setDescComuNasc(String descComuNasc) {
		this.descComuNasc = descComuNasc;
	}
	public String getDescProvNasc() {
		return descProvNasc;
	}
	public void setDescProvNasc(String descProvNasc) {
		this.descProvNasc = descProvNasc;
	}
	public String getDescDeno() {
		return descDeno;
	}
	public void setDescDeno(String descDeno) {
		this.descDeno = descDeno;
	}
	public String getDescStatAtti() {
		return descStatAtti;
	}
	public void setDescStatAtti(String descStatAtti) {
		this.descStatAtti = descStatAtti;
	}
	public String getFlagAgriAtti() {
		return flagAgriAtti;
	}
	public void setFlagAgriAtti(String flagAgriAtti) {
		this.flagAgriAtti = flagAgriAtti;
	}
	public String getFlagGiovAgri() {
		return flagGiovAgri;
	}
	public void setFlagGiovAgri(String flagGiovAgri) {
		this.flagGiovAgri = flagGiovAgri;
	}
	public String getFlagPersFisi() {
		return flagPersFisi;
	}
	public void setFlagPersFisi(String flagPersFisi) {
		this.flagPersFisi = flagPersFisi;
	}
	public String getDataFineAtti() {
		return dataFineAtti;
	}
	public void setDataFineAtti(String dataFineAtti) {
		this.dataFineAtti = dataFineAtti;
	}
	public String getDataAggi() {
		return dataAggi;
	}
	public void setDataAggi(String dataAggi) {
		this.dataAggi = dataAggi;
	}
	public String getCodiFiscLuna() {
		return codiFiscLuna;
	}
	public void setCodiFiscLuna(String codiFiscLuna) {
		this.codiFiscLuna = codiFiscLuna;
	}
	public String getCodiSiglProvNasc() {
		return codiSiglProvNasc;
	}
	public void setCodiSiglProvNasc(String codiSiglProvNasc) {
		this.codiSiglProvNasc = codiSiglProvNasc;
	}
	public String getDescInte() {
		return descInte;
	}
	public void setDescInte(String descInte) {
		this.descInte = descInte;
	}
	public String getDataInizPartIvaa() {
		return dataInizPartIvaa;
	}
	public void setDataInizPartIvaa(String dataInizPartIvaa) {
		this.dataInizPartIvaa = dataInizPartIvaa;
	}
	public String getDataFinePartIvaa() {
		return dataFinePartIvaa;
	}
	public void setDataFinePartIvaa(String dataFinePartIvaa) {
		this.dataFinePartIvaa = dataFinePartIvaa;
	}
	public String getCuaa() {
		return cuaa;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public List<RecapitoAualDto> getRecapito() {
		return recapito;
	}
	public void setRecapito(List<RecapitoAualDto> recapito) {
		this.recapito = recapito;
	}
	public List<ContoCorrenteAualDto> getContoCorrente() {
		return contoCorrente;
	}
	public void setContoCorrente(List<ContoCorrenteAualDto> contoCorrente) {
		this.contoCorrente = contoCorrente;
	}
}
