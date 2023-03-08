package it.tndigitale.a4g.richiestamodificasuolo.dto.filter;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public class SuoloFilter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long statoColt;
	private String sorgente;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataInizioValidita;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataFineValidita;
	private String note;
	private Integer campagna;
	private Long idLavorazioneInizio;
	private Long idLavorazioneFine;
	private Long idLavorazioneInCorso;
	private Long idGrid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStatoColt() {
		return statoColt;
	}

	public void setStatoColt(Long statoColt) {
		this.statoColt = statoColt;
	}

	public String getSorgente() {
		return sorgente;
	}

	public void setSorgente(String sorgente) {
		this.sorgente = sorgente;
	}

	public LocalDateTime getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(LocalDateTime dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public LocalDateTime getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(LocalDateTime dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public Long getIdLavorazioneInizio() {
		return idLavorazioneInizio;
	}

	public void setIdLavorazioneInizio(Long idLavorazioneInizio) {
		this.idLavorazioneInizio = idLavorazioneInizio;
	}

	public Long getIdLavorazioneFine() {
		return idLavorazioneFine;
	}

	public void setIdLavorazioneFine(Long idLavorazioneFine) {
		this.idLavorazioneFine = idLavorazioneFine;
	}

	public Long getIdLavorazioneInCorso() {
		return idLavorazioneInCorso;
	}

	public void setIdLavorazioneInCorso(Long idLavorazioneInCorso) {
		this.idLavorazioneInCorso = idLavorazioneInCorso;
	}

	public Long getIdGrid() {
		return idGrid;
	}

	public void setIdGrid(Long idGrid) {
		this.idGrid = idGrid;
	}

	@Override
	public String toString() {
		return String.format(
				"SuoloFilter [id=%s,statoColt=%s,sorgente=%s,dataInizioValidita=%s,dataFineValidita=%s,note=%s,campagna=%s,idLavorazioneInizio=%s,idLavorazioneInCorso=%s,idLavorazioneFine=%s,idGrid=%s]",
				id, statoColt, sorgente, dataInizioValidita, dataFineValidita, note, campagna, idLavorazioneInizio, idLavorazioneInCorso, idLavorazioneFine, idGrid);
	}
}