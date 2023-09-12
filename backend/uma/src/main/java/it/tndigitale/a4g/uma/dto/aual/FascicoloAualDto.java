package it.tndigitale.a4g.uma.dto.aual;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FascicoloAualDto {
	
	private String codiCuaa;
	private String codiInps;
	private String codiTipoDete;
	private String dataAperFasc;
	private String dataChiuFasc;
	private String dataElab;
	private String dataScheVali;
	private String dataSottMand;
	private String dataValiFasc;
	private String descDeno;
	private String descDete;
	private String numeScheVali;
	private String email;
	private String descPec;
	private String numeIscrRea;
	private String numeIscrRegiImpr;
	
	public String getCodiCuaa() {
		return codiCuaa;
	}
	public void setCodiCuaa(String codiCuaa) {
		this.codiCuaa = codiCuaa;
	}
	public String getCodiInps() {
		return codiInps;
	}
	public void setCodiInps(String codiInps) {
		this.codiInps = codiInps;
	}
	public String getCodiTipoDete() {
		return codiTipoDete;
	}
	public void setCodiTipoDete(String codiTipoDete) {
		this.codiTipoDete = codiTipoDete;
	}
	public String getDataAperFasc() {
		return dataAperFasc;
	}
	public void setDataAperFasc(String dataAperFasc) {
		this.dataAperFasc = dataAperFasc;
	}
	public String getDataChiuFasc() {
		return dataChiuFasc;
	}
	public void setDataChiuFasc(String dataChiuFasc) {
		this.dataChiuFasc = dataChiuFasc;
	}
	public String getDataElab() {
		return dataElab;
	}
	public void setDataElab(String dataElab) {
		this.dataElab = dataElab;
	}
	public String getDataScheVali() {
		return dataScheVali;
	}
	public void setDataScheVali(String dataScheVali) {
		this.dataScheVali = dataScheVali;
	}
	public String getDataSottMand() {
		return dataSottMand;
	}
	public void setDataSottMand(String dataSottMand) {
		this.dataSottMand = dataSottMand;
	}
	public String getDataValiFasc() {
		return dataValiFasc;
	}
	public void setDataValiFasc(String dataValiFasc) {
		this.dataValiFasc = dataValiFasc;
	}
	public String getDescDeno() {
		return descDeno;
	}
	public void setDescDeno(String descDeno) {
		this.descDeno = descDeno;
	}
	public String getDescDete() {
		return descDete;
	}
	public void setDescDete(String descDete) {
		this.descDete = descDete;
	}
	public String getNumeScheVali() {
		return numeScheVali;
	}
	public void setNumeScheVali(String numeScheVali) {
		this.numeScheVali = numeScheVali;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDescPec() {
		return descPec;
	}
	public void setDescPec(String descPec) {
		this.descPec = descPec;
	}
	public String getNumeIscrRea() {
		return numeIscrRea;
	}
	public void setNumeIscrRea(String numeIscrRea) {
		this.numeIscrRea = numeIscrRea;
	}
	public String getNumeIscrRegiImpr() {
		return numeIscrRegiImpr;
	}
	public void setNumeIscrRegiImpr(String numeIscrRegiImpr) {
		this.numeIscrRegiImpr = numeIscrRegiImpr;
	}

//	private String codiTipoAzie;
//	private String numeDocu;
//	private String dataDocu;
//	private String dataScadDocu;
//	private String dataInizInps;
//	private String dataCessInps;
	
/*
        "tipoDocumento": {
            "codiTipoDocu": "1",
            "descTipoDocu": null
        },
        "tipoInps": {
            "codiTipoIscrInps": "ND",
            "descTipoIscrInps": null
        },
        "op": {
            "codiOrgaPaga": "IT01",
            "descOrgaPaga": null
        },
        "flagDurc": "1",
        "regImprese": {
            "numeIscrRi": "N.D.",
            "dataIscrRi": "20210715",
            "dataCessRi": "99991231"
        },
        "rea": {
            "numeIscrRea": 643754,
            "dataIscrRea": "19000101",
            "dataCessRea": "99991231"
        }
 * 
 */

}
