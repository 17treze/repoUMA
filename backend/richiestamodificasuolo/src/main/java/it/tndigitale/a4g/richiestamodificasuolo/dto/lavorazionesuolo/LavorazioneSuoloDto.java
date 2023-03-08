package it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public class LavorazioneSuoloDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2790423696662818360L;
	private Long id;
	private String utente;
	private String utenteAgs;
	private String stato;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataInizioLavorazione;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataFineLavorazione;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataUltimaModifica;
	private String note;
	private String titolo;
	private String sopralluogo;
	private Integer campagna;
	private String modalitaADL;
	private Double xUltimoZoom;
	private Double yUltimoZoom;
	private Double scalaUltimoZoom;
	private String readOnly;
	private Long idLavorazionePadre;

	public Long getIdLavorazionePadre() {
		return idLavorazionePadre;
	}

	public void setIdLavorazionePadre(Long idLavorazionePadre) {
		this.idLavorazionePadre = idLavorazionePadre;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public String getUtenteAgs() {
		return utenteAgs;
	}

	public void setUtenteAgs(String utenteAgs) {
		this.utenteAgs = utenteAgs;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public LocalDateTime getDataInizioLavorazione() {
		return dataInizioLavorazione;
	}

	public void setDataInizioLavorazione(LocalDateTime dataInizioLavorazione) {
		this.dataInizioLavorazione = dataInizioLavorazione;
	}

	public LocalDateTime getDataFineLavorazione() {
		return dataFineLavorazione;
	}

	public void setDataFineLavorazione(LocalDateTime dataFineLavorazione) {
		this.dataFineLavorazione = dataFineLavorazione;
	}

	public LocalDateTime getDataUltimaModifica() {
		return dataUltimaModifica;
	}

	public void setDataUltimaModifica(LocalDateTime dataUltimaModifica) {
		this.dataUltimaModifica = dataUltimaModifica;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getSopralluogo() {
		return sopralluogo;
	}

	public void setSopralluogo(String sopralluogo) {
		this.sopralluogo = sopralluogo;
	}

	public Double getxUltimoZoom() {
		return xUltimoZoom;
	}

	public void setxUltimoZoom(Double xUltimoZoom) {
		this.xUltimoZoom = xUltimoZoom;
	}

	public Double getyUltimoZoom() {
		return yUltimoZoom;
	}

	public void setyUltimoZoom(Double yUltimoZoom) {
		this.yUltimoZoom = yUltimoZoom;
	}

	public Double getScalaUltimoZoom() {
		return scalaUltimoZoom;
	}

	public void setScalaUltimoZoom(Double scalaUltimoZoom) {
		this.scalaUltimoZoom = scalaUltimoZoom;
	}

	public String getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public String getModalitaADL() {
		return modalitaADL;
	}

	public void setModalitaADL(String modalitaADL) {
		this.modalitaADL = modalitaADL;
	}

}
