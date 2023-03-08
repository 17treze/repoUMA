package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;
import java.util.Date;

public class DomandaCollegata {

	private Long id;
	private String cuaa;
	private String tipoDomanda;
	private Long idDomanda;
	private Date dtDomanda;
	private Integer campagna;
	private BigDecimal importoRichiesto;
	private Date dtBdnaOp;
	private Date dtBdna;
	private String protocollo;
	private String statoBdna;
	private String misurePsr;
	private Date dtInizioSilenzioAssenso;
	private Date dtInizioEsitoNegativo;
	private Date dtFineSilenzioAssenso;
	private Date dtFineEsitoNegativo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Date getDtDomanda() {
		return dtDomanda;
	}

	public void setDtDomanda(Date dtDomanda) {
		this.dtDomanda = dtDomanda;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public BigDecimal getImportoRichiesto() {
		return importoRichiesto;
	}

	public void setImportoRichiesto(BigDecimal importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
	}

	public Date getDtBdnaOp() {
		return dtBdnaOp;
	}

	public void setDtBdnaOp(Date dtBdnaOp) {
		this.dtBdnaOp = dtBdnaOp;
	}

	public Date getDtBdna() {
		return dtBdna;
	}

	public void setDtBdna(Date dtBdna) {
		this.dtBdna = dtBdna;
	}

	public String getProtocollo() {
		return protocollo;
	}

	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}

	public String getStatoBdna() {
		return statoBdna;
	}

	public void setStatoBdna(String statoBdna) {
		this.statoBdna = statoBdna;
	}
	public String getMisurePsr() {
		return misurePsr;
	}

	public void setMisurePsr(String misurePsr) {
		this.misurePsr = misurePsr;
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
