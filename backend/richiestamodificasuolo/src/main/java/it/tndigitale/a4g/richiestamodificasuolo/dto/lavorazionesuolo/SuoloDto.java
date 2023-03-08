package it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public class SuoloDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String istatp;
	private String shape;
	private Double areaColt;
	private UsoSuoloDto codUsoSuoloModel;
	private String sorgente;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataInizioValidita;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataFineValidita;
	private String note;
	private Integer campagna;
	private LavorazioneSuoloDto idLavorazioneInCorso;
	private LavorazioneSuoloDto idLavorazioneInizio;
	private LavorazioneSuoloDto idLavorazioneFine;
	private GrigliaSuoloDto idGrid;
	private StatoColtDto statoColtSuolo;
	private Double[] extent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIstatp() {
		return istatp;
	}

	public void setIstatp(String istatp) {
		this.istatp = istatp;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public Double getAreaColt() {
		return areaColt;
	}

	public void setAreaColt(Double areaColt) {
		this.areaColt = areaColt;
	}

	public UsoSuoloDto getCodUsoSuoloModel() {
		return codUsoSuoloModel;
	}

	public void setCodUsoSuoloModel(UsoSuoloDto codUsoSuoloModel) {
		this.codUsoSuoloModel = codUsoSuoloModel;
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

	public LavorazioneSuoloDto getIdLavorazioneInCorso() {
		return idLavorazioneInCorso;
	}

	public void setIdLavorazioneInCorso(LavorazioneSuoloDto idLavorazioneInCorso) {
		this.idLavorazioneInCorso = idLavorazioneInCorso;
	}

	public LavorazioneSuoloDto getIdLavorazioneInizio() {
		return idLavorazioneInizio;
	}

	public void setIdLavorazioneInizio(LavorazioneSuoloDto idLavorazioneInizio) {
		this.idLavorazioneInizio = idLavorazioneInizio;
	}

	public LavorazioneSuoloDto getIdLavorazioneFine() {
		return idLavorazioneFine;
	}

	public void setIdLavorazioneFine(LavorazioneSuoloDto idLavorazioneFine) {
		this.idLavorazioneFine = idLavorazioneFine;
	}

	public GrigliaSuoloDto getIdGrid() {
		return idGrid;
	}

	public void setIdGrid(GrigliaSuoloDto idGrid) {
		this.idGrid = idGrid;
	}

	public StatoColtDto getStatoColtSuolo() {
		return statoColtSuolo;
	}

	public void setStatoColtSuolo(StatoColtDto statoColtSuolo) {
		this.statoColtSuolo = statoColtSuolo;
	}

	public Double[] getExtent() {
		return extent;
	}

	public void setExtent(Double[] extent) {
		this.extent = extent;
	}

}
