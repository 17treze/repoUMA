package it.tndigitale.a4g.richiestamodificasuolo.dto.filter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;

@ApiModel(description = "Rappresenta il modello dei filtri di ricerca delle lavorazioni suolo")
public class LavorazioneSuoloFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiParam(value = "id lavorazioni suolo da ricercare", required = true)
	private Long idLavorazione;

	@ApiParam(value = "Utente creatore della lavorazione ", required = true)
	private String utente;

	@ApiParam(value = "StatoLavorazioneSuolo da ricercare", required = true)
	private List<StatoLavorazioneSuolo> listStatiLavorazione;

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

	public Long getIdLavorazione() {
		return idLavorazione;
	}

	public void setIdLavorazione(Long idLavorazione) {
		this.idLavorazione = idLavorazione;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public List<StatoLavorazioneSuolo> getListStatiLavorazione() {
		return listStatiLavorazione;
	}

	public void setListStatiLavorazione(List<StatoLavorazioneSuolo> listStatiLavorazione) {
		this.listStatiLavorazione = listStatiLavorazione;
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

	public LocalDateTime getDataUltimaModifica() {
		return dataUltimaModifica;
	}

	public void setDataUltimaModifica(LocalDateTime dataUltimaModifica) {
		this.dataUltimaModifica = dataUltimaModifica;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	@Override
	public String toString() {
		return String.format(
				"LavorazioneSuoloFilter [idLavorazione=%s, utente=%s, campagna=%s, listStatiLavorazione=%s, dataInizioLavorazione=%s,dataFineLavorazione=%s,note=%s,titolo=%s,sopralluogo=%s,dataUltimaModifica=%s]",
				idLavorazione, utente, campagna, listStatiLavorazione, dataInizioLavorazione, dataFineLavorazione, note, titolo, sopralluogo, dataUltimaModifica);
	}

}
