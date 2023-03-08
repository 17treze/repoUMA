package it.tndigitale.a4g.proxy.dto;

import java.math.BigDecimal;

public class TitoloAgricoltoreSIAN {

	private long idTitolare;
	private String cuaaTitolare;
	private String numeroTitolare;
	private BigDecimal valoreTitolo;
	private BigDecimal superficieTitolo;
	private String dataInizioPossesso;
	private String dataFinePossesso;
	private BigDecimal annoCampagnaInizio;
	private BigDecimal annoCampagnaFine;

	public long getIdTitolare() {
		return idTitolare;
	}

	public void setIdTitolare(long idTitolare) {
		this.idTitolare = idTitolare;
	}

	public String getCuaaTitolare() {
		return cuaaTitolare;
	}

	public void setCuaaTitolare(String cuaaTitolare) {
		this.cuaaTitolare = cuaaTitolare;
	}

	public String getNumeroTitolare() {
		return numeroTitolare;
	}

	public void setNumeroTitolare(String numeroTitolare) {
		this.numeroTitolare = numeroTitolare;
	}

	public BigDecimal getSuperficieTitolo() {
		return superficieTitolo;
	}

	public void setSuperficieTitolo(BigDecimal superficieTitolo) {
		this.superficieTitolo = superficieTitolo;
	}

	public String getDataInizioPossesso() {
		return dataInizioPossesso;
	}

	public void setDataInizioPossesso(String dataInizioPossesso) {
		this.dataInizioPossesso = dataInizioPossesso;
	}

	public String getDataFinePossesso() {
		return dataFinePossesso;
	}

	public void setDataFinePossesso(String dataFinePossesso) {
		this.dataFinePossesso = dataFinePossesso;
	}

	public BigDecimal getAnnoCampagnaInizio() {
		return annoCampagnaInizio;
	}

	public void setAnnoCampagnaInizio(BigDecimal annoCampagnaInizio) {
		this.annoCampagnaInizio = annoCampagnaInizio;
	}

	public BigDecimal getAnnoCampagnaFine() {
		return annoCampagnaFine;
	}

	public void setAnnoCampagnaFine(BigDecimal annoCampagnaFine) {
		this.annoCampagnaFine = annoCampagnaFine;
	}

	public BigDecimal getValoreTitolo() {
		return valoreTitolo;
	}

	public void setValoreTitolo(BigDecimal valoreTitolo) {
		this.valoreTitolo = valoreTitolo;
	}

}
