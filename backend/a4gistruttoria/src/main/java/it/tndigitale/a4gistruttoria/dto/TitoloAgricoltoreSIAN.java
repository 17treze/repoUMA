package it.tndigitale.a4gistruttoria.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class TitoloAgricoltoreSIAN {

	private BigDecimal idTitolare;
	private String cuaaTitolare;
	private String numeroTitolare;
	private BigDecimal valoreTitolo;
	private BigDecimal superficieTitolo;
	private Date dataInizioPossesso;
	private Date dataFinePossesso;
	private BigDecimal annoCampagnaInizio;
	private BigDecimal annoCampagnaFine;

	public BigDecimal getIdTitolare() {
		return idTitolare;
	}

	public void setIdTitolare(BigDecimal idTitolare) {
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

	public Date getDataInizioPossesso() {
		return dataInizioPossesso;
	}

	public void setDataInizioPossesso(Date dataInizioPossesso) {
		this.dataInizioPossesso = dataInizioPossesso;
	}

	public Date getDataFinePossesso() {
		return dataFinePossesso;
	}

	public void setDataFinePossesso(Date dataFinePossesso) {
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

	public BigDecimal getValoreNominaleTitolo() {
		return this.valoreTitolo.divide(this.superficieTitolo, 2, RoundingMode.HALF_UP);
	}

}
