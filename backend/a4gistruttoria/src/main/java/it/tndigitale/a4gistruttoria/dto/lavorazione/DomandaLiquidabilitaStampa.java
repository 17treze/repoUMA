package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.math.BigDecimal;
import java.util.Date;

public class DomandaLiquidabilitaStampa {

	private BigDecimal numeroDomanda;
	private BigDecimal numeroProtocollo;
	private Date dtPresentazione;
	private String ragioneSociale;
	private String cuaa;
	private String domicilio;
	private BigDecimal pagBase;
	private BigDecimal pagGreening;
	private BigDecimal pagGiovane;
	private BigDecimal pagTot;

	public DomandaLiquidabilitaStampa(BigDecimal numeroDomanda, BigDecimal numeroProtocollo, Date dtPresentazione, BigDecimal pagBase, BigDecimal pagGreening, BigDecimal pagGiovane) {
		this.numeroDomanda = numeroDomanda;
		this.numeroProtocollo = numeroProtocollo;
		this.dtPresentazione = dtPresentazione;
		this.pagBase = pagBase;
		this.pagGreening = pagGreening;
		this.pagGiovane = pagGiovane;
		this.pagTot = pagBase.add(pagGreening).add(pagGiovane);
	}

	public DomandaLiquidabilitaStampa() {
		this.pagTot = this.pagBase.add(this.pagGiovane).add(this.pagGreening);

	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public BigDecimal getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(BigDecimal numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public BigDecimal getNumeroProtocollo() {
		return numeroProtocollo;
	}

	public void setNumeroProtocollo(BigDecimal numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	public Date getDtPresentazione() {
		return dtPresentazione;
	}

	public void setDtPresentazione(Date dtPresentazione) {
		this.dtPresentazione = dtPresentazione;
	}

	public BigDecimal getPagBase() {
		return pagBase;
	}

	public void setPagBase(BigDecimal pagBase) {
		this.pagBase = pagBase;
	}

	public BigDecimal getPagGreening() {
		return pagGreening;
	}

	public void setPagGreening(BigDecimal pagGreening) {
		this.pagGreening = pagGreening;
	}

	public BigDecimal getPagGiovane() {
		return pagGiovane;
	}

	public void setPagGiovane(BigDecimal pagGiovane) {
		this.pagGiovane = pagGiovane;
	}

	public BigDecimal getPagTot() {
		return pagTot;
	}

	public void setPagTot(BigDecimal pagTot) {
		this.pagTot = pagTot;
	}

}
