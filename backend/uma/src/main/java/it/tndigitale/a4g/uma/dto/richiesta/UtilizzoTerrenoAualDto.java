package it.tndigitale.a4g.uma.dto.richiesta;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UtilizzoTerrenoAualDto {

      private TerritorioAualDto territorio;
      private Integer codiUtil;
      // private Macrouso macrouso;
      private String codiQual;
      private String numeSupeUtil;
      private Date dataInizVali;
      private Date dataFineVali;
      private Integer codiFontDato;
      private String descUser;
      private List<UtilizzoSuoloAualDto> listaUtilizzoSuolo;
      
	public TerritorioAualDto getTerritorio() {
		return territorio;
	}
	public void setTerritorio(TerritorioAualDto territorio) {
		this.territorio = territorio;
	}
	public Integer getCodiUtil() {
		return codiUtil;
	}
	public void setCodiUtil(Integer codiUtil) {
		this.codiUtil = codiUtil;
	}
	public String getCodiQual() {
		return codiQual;
	}
	public void setCodiQual(String codiQual) {
		this.codiQual = codiQual;
	}
	public String getNumeSupeUtil() {
		return numeSupeUtil;
	}
	public void setNumeSupeUtil(String numeSupeUtil) {
		this.numeSupeUtil = numeSupeUtil;
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
	public List<UtilizzoSuoloAualDto> getListaUtilizzoSuolo() {
		return listaUtilizzoSuolo;
	}
	public void setListaUtilizzoSuolo(List<UtilizzoSuoloAualDto> listaUtilizzoSuolo) {
		this.listaUtilizzoSuolo = listaUtilizzoSuolo;
	}
}

