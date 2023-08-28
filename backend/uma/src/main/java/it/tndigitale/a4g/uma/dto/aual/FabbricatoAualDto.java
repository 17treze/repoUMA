package it.tndigitale.a4g.uma.dto.aual;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FabbricatoAualDto {

	private Long codiFabb;
	private String codiCuaa;
	private String codiCasiPart;
	private String codiComu;
	private String dataFineCond;
	private String dataInizCond;
	private String descDest;
	private String descFogl;
	private String numePost;
	private String descPart;
	private String codiProv;
	private String descSezi;
	private String descSuba;
	private String numeSupeCope;
	private String numeSupeScop;
	// private TipoFabbricato tipoFabbricato;
	// private TipoConduzione tipoConduzione;
	private String numeVolu;
	private Date dataInizVali;
	private Date dataFineVali;
	private Integer codiFontDato;
	private String descUser;
	private String codiLive;
	private String codiOccu;
	private String codiDest;
	private String codiUso;
	private String codiQual;
	private String codiOccuVari;
	// private List<ProprietarioFabbricato> listaProprietarioFabbricato;
	// private List<UtilizzoFabbricato> listaUtilizzoFabbricato;
	  
	public Long getCodiFabb() {
		return codiFabb;
	}
	public void setCodiFabb(Long codiFabb) {
		this.codiFabb = codiFabb;
	}
	public String getCodiCuaa() {
		return codiCuaa;
	}
	public void setCodiCuaa(String codiCuaa) {
		this.codiCuaa = codiCuaa;
	}
	public String getCodiCasiPart() {
		return codiCasiPart;
	}
	public void setCodiCasiPart(String codiCasiPart) {
		this.codiCasiPart = codiCasiPart;
	}
	public String getCodiComu() {
		return codiComu;
	}
	public void setCodiComu(String codiComu) {
		this.codiComu = codiComu;
	}
	public String getDataFineCond() {
		return dataFineCond;
	}
	public void setDataFineCond(String dataFineCond) {
		this.dataFineCond = dataFineCond;
	}
	public String getDataInizCond() {
		return dataInizCond;
	}
	public void setDataInizCond(String dataInizCond) {
		this.dataInizCond = dataInizCond;
	}
	public String getDescDest() {
		return descDest;
	}
	public void setDescDest(String descDest) {
		this.descDest = descDest;
	}
	public String getDescFogl() {
		return descFogl;
	}
	public void setDescFogl(String descFogl) {
		this.descFogl = descFogl;
	}
	public String getNumePost() {
		return numePost;
	}
	public void setNumePost(String numePost) {
		this.numePost = numePost;
	}
	public String getDescPart() {
		return descPart;
	}
	public void setDescPart(String descPart) {
		this.descPart = descPart;
	}
	public String getCodiProv() {
		return codiProv;
	}
	public void setCodiProv(String codiProv) {
		this.codiProv = codiProv;
	}
	public String getDescSezi() {
		return descSezi;
	}
	public void setDescSezi(String descSezi) {
		this.descSezi = descSezi;
	}
	public String getDescSuba() {
		return descSuba;
	}
	public void setDescSuba(String descSuba) {
		this.descSuba = descSuba;
	}
	public String getNumeSupeCope() {
		return numeSupeCope;
	}
	public void setNumeSupeCope(String numeSupeCope) {
		this.numeSupeCope = numeSupeCope;
	}
	public String getNumeSupeScop() {
		return numeSupeScop;
	}
	public void setNumeSupeScop(String numeSupeScop) {
		this.numeSupeScop = numeSupeScop;
	}
	public String getNumeVolu() {
		return numeVolu;
	}
	public void setNumeVolu(String numeVolu) {
		this.numeVolu = numeVolu;
	}
	public Date getDataInizVali() {
		return dataInizVali;
	}
	public void setDataInizVali(Date dataInizVali) {
		this.dataInizVali = dataInizVali;
	}
	public Date getDataFineVali() {
		return dataFineVali;
	}
	public void setDataFineVali(Date dataFineVali) {
		this.dataFineVali = dataFineVali;
	}
	public Integer getCodiFontDato() {
		return codiFontDato;
	}
	public void setCodiFontDato(Integer codiFontDato) {
		this.codiFontDato = codiFontDato;
	}
	public String getDescUser() {
		return descUser;
	}
	public void setDescUser(String descUser) {
		this.descUser = descUser;
	}
	public String getCodiLive() {
		return codiLive;
	}
	public void setCodiLive(String codiLive) {
		this.codiLive = codiLive;
	}
	public String getCodiOccu() {
		return codiOccu;
	}
	public void setCodiOccu(String codiOccu) {
		this.codiOccu = codiOccu;
	}
	public String getCodiDest() {
		return codiDest;
	}
	public void setCodiDest(String codiDest) {
		this.codiDest = codiDest;
	}
	public String getCodiUso() {
		return codiUso;
	}
	public void setCodiUso(String codiUso) {
		this.codiUso = codiUso;
	}
	public String getCodiQual() {
		return codiQual;
	}
	public void setCodiQual(String codiQual) {
		this.codiQual = codiQual;
	}
	public String getCodiOccuVari() {
		return codiOccuVari;
	}
	public void setCodiOccuVari(String codiOccuVari) {
		this.codiOccuVari = codiOccuVari;
	}
}

