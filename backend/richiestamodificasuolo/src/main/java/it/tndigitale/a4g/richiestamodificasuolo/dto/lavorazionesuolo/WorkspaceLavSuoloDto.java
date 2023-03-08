package it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

public class WorkspaceLavSuoloDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long idLavorazione;
	private UsoSuoloDto codUsoSuoloWorkspaceLavSuolo;
	private StatoColtDto statoColtWorkspaceLavSuolo;
	private String note;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataUltimaModifica;
	private Double area;
	private Double[] extent;
	private Set<AnomaliaValidazioneDto> anomaliaValidazioneDto;
	private Long idLavorazioneOrig;
	private String sorgente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdLavorazione() {
		return idLavorazione;
	}

	public void setIdLavorazione(Long idLavorazione) {
		this.idLavorazione = idLavorazione;
	}

	public UsoSuoloDto getCodUsoSuoloWorkspaceLavSuolo() {
		return codUsoSuoloWorkspaceLavSuolo;
	}

	public void setCodUsoSuoloWorkspaceLavSuolo(UsoSuoloDto codUsoSuolo) {
		this.codUsoSuoloWorkspaceLavSuolo = codUsoSuolo;
	}

	public StatoColtDto getStatoColtWorkspaceLavSuolo() {
		return statoColtWorkspaceLavSuolo;
	}

	public void setStatoColtWorkspaceLavSuolo(StatoColtDto statoColtSuolo) {
		this.statoColtWorkspaceLavSuolo = statoColtSuolo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public LocalDateTime getDataUltimaModifica() {
		return dataUltimaModifica;
	}

	public void setDataUltimaModifica(LocalDateTime dataUltimaModifica) {
		this.dataUltimaModifica = dataUltimaModifica;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public Double[] getExtent() {
		return extent;
	}

	public void setExtent(Double[] extent) {
		this.extent = extent;
	}

	public Set<AnomaliaValidazioneDto> getAnomaliaValidazioneDto() {
		return anomaliaValidazioneDto;
	}

	public void setAnomaliaValidazioneDto(Set<AnomaliaValidazioneDto> anomaliaValidazioneDto) {
		this.anomaliaValidazioneDto = anomaliaValidazioneDto;
	}

	public Long getIdLavorazioneOrig() {
		return idLavorazioneOrig;
	}

	public void setIdLavorazioneOrig(Long idLavorazioneOrig) {
		this.idLavorazioneOrig = idLavorazioneOrig;
	}

	public String getSorgente() {
		return sorgente;
	}

	public void setSorgente(String sorgente) {
		this.sorgente = sorgente;
	}
}
