package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4SD_CODI_RILE_PCG")
public class CodiRilePcgModel extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -801865319978455845L;

	@Column(name = "CODI_RILE")
	private String codiRile;

	@Column(name = "DESC_RILE")
	private String descRile;

	@Column(name = "CODI_PROD_RILE")
	private String codiProdRile;

	@Column(name = "DESC_PROD_RILE")
	private String descProdRile;

	@Column(name = "CODI_RILE_PREVALENTE")
	private String codiRilePrevalente;

	@Column(name = "DESC_RILE_PREVALENTE")
	private String descRilePrevalente;

	@Column(name = "DATA_INIZIO")
	private LocalDateTime dataInizio;

	@Column(name = "DATA_FINE")
	private LocalDateTime dataFine;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "codiRilePcgDichiarato")
	private List<SuoloDichiaratoModel> listaSuoloDichiarato;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "codiRilePcgRilevato")
	private List<SuoloRilevatoModel> listaSuoloRilevato;

	public String getCodiRile() {
		return codiRile;
	}

	public void setCodiRile(String codiRile) {
		this.codiRile = codiRile;
	}

	public String getDescRile() {
		return descRile;
	}

	public void setDescRile(String descRile) {
		this.descRile = descRile;
	}

	public String getCodiProdRile() {
		return codiProdRile;
	}

	public void setCodiProdRile(String codiProdRile) {
		this.codiProdRile = codiProdRile;
	}

	public String getDescProdRile() {
		return descProdRile;
	}

	public void setDescProdRile(String descProdRile) {
		this.descProdRile = descProdRile;
	}

	public String getCodiRilePrevalente() {
		return codiRilePrevalente;
	}

	public void setCodiRilePrevalente(String codiRilePrevalente) {
		this.codiRilePrevalente = codiRilePrevalente;
	}

	public String getDescRilePrevalente() {
		return descRilePrevalente;
	}

	public void setDescRilePrevalente(String descRilePrevalente) {
		this.descRilePrevalente = descRilePrevalente;
	}

	public LocalDateTime getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(LocalDateTime dataInizio) {
		this.dataInizio = dataInizio;
	}

	public LocalDateTime getDataFine() {
		return dataFine;
	}

	public void setDataFine(LocalDateTime dataFine) {
		this.dataFine = dataFine;
	}

	public List<SuoloDichiaratoModel> getListaSuoloDichiarato() {
		return listaSuoloDichiarato;
	}

	public void setListaSuoloDichiarato(List<SuoloDichiaratoModel> listaSuoloDichiarato) {
		this.listaSuoloDichiarato = listaSuoloDichiarato;
	}

	public List<SuoloRilevatoModel> getListaSuoloRilevato() {
		return listaSuoloRilevato;
	}

	public void setListaSuoloRilevato(List<SuoloRilevatoModel> listaSuoloRilevato) {
		this.listaSuoloRilevato = listaSuoloRilevato;
	}

}
