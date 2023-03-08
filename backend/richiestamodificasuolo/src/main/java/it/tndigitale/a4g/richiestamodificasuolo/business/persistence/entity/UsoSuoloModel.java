package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4SD_USO_SUOLO")
public class UsoSuoloModel extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "COD_USO_SUOLO")
	private String codUsoSuolo;

	private String descrizione;

	@Column(name = "DATA_INIZIO")
	private LocalDateTime dataInizio;

	@Column(name = "DATA_FINE")
	private LocalDateTime dataFine;

	@Column(name = "VISUALIZZA_BO")
	private Integer visualizzaBo;

	public String getCodUsoSuolo() {
		return codUsoSuolo;
	}

	public void setCodUsoSuolo(String codUsoSuolo) {
		this.codUsoSuolo = codUsoSuolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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

	public Integer getVisualizzaBo() {
		return visualizzaBo;
	}

	public void setVisualizzaBo(Integer visualizzaBo) {
		this.visualizzaBo = visualizzaBo;
	}

}