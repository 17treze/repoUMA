package it.tndigitale.a4g.uma.dto.richiesta;

import java.util.Date;
import java.util.List;

   public class TerritorioAualDto {

      private Integer codiTerr;
      private String codiCuaa;
      // private CasoParticolare casoParticolare;
      // private TipoConduzione tipoConduzione;
      private String codiComu;
      private String dataDocu;
      private String dataFineCond;
      private String dataInizCond;
      // private EffluentiZootecnici effluentiZootecnici;
      private String flagGius;
      // private FlagIrrigazione flagIrrigazione;
      // private FlagTerrazzamenti flagTerrazzamenti;
      private String descFogl;
      private String numeDocu;
      private String descPart;
      private String codiProv;
      // private RotazioneColture rotazioneColture;
      private String descSezi;
      // private SostanzePericolose sostanzePericolose;
      private String descSuba;
      private String numeSupeCata;
      private String numeSupeCond;
      // private ConduzioneParticella conduzioneParticella;
      private Date dataInizVali;
      private Date dataFineVali;
      private Integer codiFontDato;
      private String descUser;
      private String dataScadDocu;
      private String codiZvn;
      private String supeGraf;
      // private List<ProprietarioTerreno> listaProprietarioTerreno;
      // private List<DocumentoTerritorio> listaDocumentoTerritorio;
      private List<UtilizzoTerrenoAualDto> listaUtilizzoTerreno;
	public Integer getCodiTerr() {
		return codiTerr;
	}
	public void setCodiTerr(Integer codiTerr) {
		this.codiTerr = codiTerr;
	}
	public String getCodiCuaa() {
		return codiCuaa;
	}
	public void setCodiCuaa(String codiCuaa) {
		this.codiCuaa = codiCuaa;
	}
	public String getCodiComu() {
		return codiComu;
	}
	public void setCodiComu(String codiComu) {
		this.codiComu = codiComu;
	}
	public String getDataDocu() {
		return dataDocu;
	}
	public void setDataDocu(String dataDocu) {
		this.dataDocu = dataDocu;
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
	public String getFlagGius() {
		return flagGius;
	}
	public void setFlagGius(String flagGius) {
		this.flagGius = flagGius;
	}
	public String getDescFogl() {
		return descFogl;
	}
	public void setDescFogl(String descFogl) {
		this.descFogl = descFogl;
	}
	public String getNumeDocu() {
		return numeDocu;
	}
	public void setNumeDocu(String numeDocu) {
		this.numeDocu = numeDocu;
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
	public String getNumeSupeCata() {
		return numeSupeCata;
	}
	public void setNumeSupeCata(String numeSupeCata) {
		this.numeSupeCata = numeSupeCata;
	}
	public String getNumeSupeCond() {
		return numeSupeCond;
	}
	public void setNumeSupeCond(String numeSupeCond) {
		this.numeSupeCond = numeSupeCond;
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
	public String getDataScadDocu() {
		return dataScadDocu;
	}
	public void setDataScadDocu(String dataScadDocu) {
		this.dataScadDocu = dataScadDocu;
	}
	public String getCodiZvn() {
		return codiZvn;
	}
	public void setCodiZvn(String codiZvn) {
		this.codiZvn = codiZvn;
	}
	public String getSupeGraf() {
		return supeGraf;
	}
	public void setSupeGraf(String supeGraf) {
		this.supeGraf = supeGraf;
	}
	public List<UtilizzoTerrenoAualDto> getListaUtilizzoTerreno() {
		return listaUtilizzoTerreno;
	}
	public void setListaUtilizzoTerreno(List<UtilizzoTerrenoAualDto> listaUtilizzoTerreno) {
		this.listaUtilizzoTerreno = listaUtilizzoTerreno;
	}
}

