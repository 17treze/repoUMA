package it.tndigitale.a4g.proxy.bdn.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class StatoSincronizzazioneDO {

	private Integer annoCampagna;
	private String cuaa;
	private String statoEsecuzione;
	private Date dataEsecuzione;

	public Integer getAnnoCampagna() {
		return annoCampagna;
	}

	public void setAnnoCampagna(Integer annoCampagna) {
		this.annoCampagna = annoCampagna;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getStatoEsecuzione() {
		return statoEsecuzione;
	}

	public void setStatoEsecuzione(String statoEsecuzione) {
		this.statoEsecuzione = statoEsecuzione;
	}

	public Date getDataEsecuzione() {
		return dataEsecuzione;
	}

	public void setDataEsecuzione(Date dataEsecuzione) {
		this.dataEsecuzione = dataEsecuzione;
	}

}
