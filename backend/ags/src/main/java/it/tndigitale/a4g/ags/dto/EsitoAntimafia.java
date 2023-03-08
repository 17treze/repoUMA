package it.tndigitale.a4g.ags.dto;

import java.util.Date;

public class EsitoAntimafia {

	private String cuaa;
	private String tipoDomanda;
	private Long idDomanda;
	private Date dtInizioSilenzioAssenso;
	private Date dtInizioEsitoNegativo;
	private Date dtFineSilenzioAssenso;
	private Date dtFineEsitoNegativo;

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getTipoDomanda() {
		return tipoDomanda;
	}

	public void setTipoDomanda(String tipoDomanda) {
		this.tipoDomanda = tipoDomanda;
	}

	public Long getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}

	public Date getDtInizioSilenzioAssenso() {
		return dtInizioSilenzioAssenso;
	}

	public Date getDtInizioEsitoNegativo() {
		return dtInizioEsitoNegativo;
	}

	public Date getDtFineSilenzioAssenso() {
		return dtFineSilenzioAssenso;
	}

	public Date getDtFineEsitoNegativo() {
		return dtFineEsitoNegativo;
	}

	public void setDtInizioSilenzioAssenso(Date dtInizioSilenzioAssenso) {
		this.dtInizioSilenzioAssenso = dtInizioSilenzioAssenso;
	}

	public void setDtInizioEsitoNegativo(Date dtInizioEsitoNegativo) {
		this.dtInizioEsitoNegativo = dtInizioEsitoNegativo;
	}

	public void setDtFineSilenzioAssenso(Date dtFineSilenzioAssenso) {
		this.dtFineSilenzioAssenso = dtFineSilenzioAssenso;
	}

	public void setDtFineEsitoNegativo(Date dtFineEsitoNegativo) {
		this.dtFineEsitoNegativo = dtFineEsitoNegativo;
	}

}
