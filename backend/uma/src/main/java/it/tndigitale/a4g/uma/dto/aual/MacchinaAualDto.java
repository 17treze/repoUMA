package it.tndigitale.a4g.uma.dto.aual;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MacchinaAualDto {

    private Long codiMacc;
    private String codiCuaa;
    // private Carburante carburante;
    private String dataCess;
    private String dataIscr;
    // private FormaPossesso formaPossesso;
    private String descMarc;
    private String descMode;
    private String descTarg;
    private String descTela;
    // private TipoMacchina tipoMacchina;
    // private TipoTarga tipoTarga;
    // private Trazione trazione;
    private Date dataInizVali;
    private Date dataFineVali;
    private Integer codiFontDato;
    private String descUser;
    // private Nazionalita nazionalita;
    private String descMatrMoto;
    private String descMarcMoto;
    private String descNomeMoto;
    private Double numePoteKwat;
    private Double numePesoRimoTota;
    private Double numePesoRimoTara;
    private Double numeConsOrar;
    private String numeAnnoIscr;
    private String flagNuovUsat;
    private String descDittScar;
    // private MotivoScarico motivoScarico;
    private Integer codiMaccUma;
    
	public Long getCodiMacc() {
		return codiMacc;
	}
	public void setCodiMacc(Long codiMacc) {
		this.codiMacc = codiMacc;
	}
	public String getCodiCuaa() {
		return codiCuaa;
	}
	public void setCodiCuaa(String codiCuaa) {
		this.codiCuaa = codiCuaa;
	}
	public String getDataCess() {
		return dataCess;
	}
	public void setDataCess(String dataCess) {
		this.dataCess = dataCess;
	}
	public String getDataIscr() {
		return dataIscr;
	}
	public void setDataIscr(String dataIscr) {
		this.dataIscr = dataIscr;
	}
	public String getDescMarc() {
		return descMarc;
	}
	public void setDescMarc(String descMarc) {
		this.descMarc = descMarc;
	}
	public String getDescMode() {
		return descMode;
	}
	public void setDescMode(String descMode) {
		this.descMode = descMode;
	}
	public String getDescTarg() {
		return descTarg;
	}
	public void setDescTarg(String descTarg) {
		this.descTarg = descTarg;
	}
	public String getDescTela() {
		return descTela;
	}
	public void setDescTela(String descTela) {
		this.descTela = descTela;
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
	public String getDescMatrMoto() {
		return descMatrMoto;
	}
	public void setDescMatrMoto(String descMatrMoto) {
		this.descMatrMoto = descMatrMoto;
	}
	public String getDescMarcMoto() {
		return descMarcMoto;
	}
	public void setDescMarcMoto(String descMarcMoto) {
		this.descMarcMoto = descMarcMoto;
	}
	public String getDescNomeMoto() {
		return descNomeMoto;
	}
	public void setDescNomeMoto(String descNomeMoto) {
		this.descNomeMoto = descNomeMoto;
	}
	public Double getNumePoteKwat() {
		return numePoteKwat;
	}
	public void setNumePoteKwat(Double numePoteKwat) {
		this.numePoteKwat = numePoteKwat;
	}
	public Double getNumePesoRimoTota() {
		return numePesoRimoTota;
	}
	public void setNumePesoRimoTota(Double numePesoRimoTota) {
		this.numePesoRimoTota = numePesoRimoTota;
	}
	public Double getNumePesoRimoTara() {
		return numePesoRimoTara;
	}
	public void setNumePesoRimoTara(Double numePesoRimoTara) {
		this.numePesoRimoTara = numePesoRimoTara;
	}
	public Double getNumeConsOrar() {
		return numeConsOrar;
	}
	public void setNumeConsOrar(Double numeConsOrar) {
		this.numeConsOrar = numeConsOrar;
	}
	public String getNumeAnnoIscr() {
		return numeAnnoIscr;
	}
	public void setNumeAnnoIscr(String numeAnnoIscr) {
		this.numeAnnoIscr = numeAnnoIscr;
	}
	public String getFlagNuovUsat() {
		return flagNuovUsat;
	}
	public void setFlagNuovUsat(String flagNuovUsat) {
		this.flagNuovUsat = flagNuovUsat;
	}
	public String getDescDittScar() {
		return descDittScar;
	}
	public void setDescDittScar(String descDittScar) {
		this.descDittScar = descDittScar;
	}
	public Integer getCodiMaccUma() {
		return codiMaccUma;
	}
	public void setCodiMaccUma(Integer codiMaccUma) {
		this.codiMaccUma = codiMaccUma;
	}
}
