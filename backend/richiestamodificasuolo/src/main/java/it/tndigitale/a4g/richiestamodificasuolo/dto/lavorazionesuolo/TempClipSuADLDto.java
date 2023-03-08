package it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public class TempClipSuADLDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long idLavorazione;
	private GrigliaSuoloDto idGrid;
	private String istatp;
	private UsoSuoloDto codUsoSuolo;
	private StatoColtDto statoColt;
	private String sorgente;
	private String note;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataInizioValidita;
	private String utente;
	private Integer campagna;
	private Double area;
	private String esitoValidazione;
	private String posizionePoligono;
	private Integer poligonoIntero;
	private Long idSuoloOriginale;
	private Double[] extent;
	private String shape;

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

	public GrigliaSuoloDto getIdGrid() {
		return idGrid;
	}

	public void setIdGrid(GrigliaSuoloDto idGrid) {
		this.idGrid = idGrid;
	}

	public String getIstatp() {
		return istatp;
	}

	public void setIstatp(String istatp) {
		this.istatp = istatp;
	}

	public UsoSuoloDto getCodUsoSuolo() {
		return codUsoSuolo;
	}

	public void setCodUsoSuolo(UsoSuoloDto codUsoSuolo) {
		this.codUsoSuolo = codUsoSuolo;
	}

	public StatoColtDto getStatoColt() {
		return statoColt;
	}

	public void setStatoColt(StatoColtDto statoColt) {
		this.statoColt = statoColt;
	}

	public String getSorgente() {
		return sorgente;
	}

	public void setSorgente(String sorgente) {
		this.sorgente = sorgente;
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

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public String getEsitoValidazione() {
		return esitoValidazione;
	}

	public void setEsitoValidazione(String esitoValidazione) {
		this.esitoValidazione = esitoValidazione;
	}

	public String getPosizionePoligono() {
		return posizionePoligono;
	}

	public void setPosizionePoligono(String posizionePoligono) {
		this.posizionePoligono = posizionePoligono;
	}

	public Integer getPoligonoIntero() {
		return poligonoIntero;
	}

	public void setPoligonoIntero(Integer poligonoIntero) {
		this.poligonoIntero = poligonoIntero;
	}

	public Long getIdSuoloOriginale() {
		return idSuoloOriginale;
	}

	public void setIdSuoloOriginale(Long idSuoloOriginale) {
		this.idSuoloOriginale = idSuoloOriginale;
	}

	public Double[] getExtent() {
		return extent;
	}

	public void setExtent(Double[] extent) {
		this.extent = extent;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public LocalDateTime getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(LocalDateTime dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}
}
