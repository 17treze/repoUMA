package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4SD_STATO_COLT")
public class StatoColtModel extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = -1324203621368923822L;

	private String descrizione;

	@Column(name = "DESC_BREVE")
	private String descBreve;

	@Column(name = "DATA_INIZIO")
	private LocalDateTime dataInizio;

	@Column(name = "DATA_FINE")
	private LocalDateTime dataFine;

	@Column(name = "DATA_AGGIORNAMENTO")
	private LocalDateTime dataAggiornamento;

	@Column(name = "STATO_COLT")
	private String statoColt;

	private String note;

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDescBreve() {
		return descBreve;
	}

	public void setDescBreve(String descBreve) {
		this.descBreve = descBreve;
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

	public LocalDateTime getDataAggiornamento() {
		return dataAggiornamento;
	}

	public void setDataAggiornamento(LocalDateTime dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getStatoColt() {
		return statoColt;
	}

	public void setStatoColt(String statoColt) {
		this.statoColt = statoColt;
	}

}
