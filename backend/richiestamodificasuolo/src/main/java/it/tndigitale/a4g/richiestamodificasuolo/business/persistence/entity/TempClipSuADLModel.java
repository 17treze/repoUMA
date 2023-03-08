package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.locationtech.jts.geom.Geometry;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4ST_TEMP_CLIP_SU_ADL")
@DynamicUpdate
public class TempClipSuADLModel extends EntitaDominio implements Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_GRID")
	private GrigliaSuoloModel idGrid;

	@Column(name = "ISTATP")
	private String istatp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_USO_SUOLO", referencedColumnName = "COD_USO_SUOLO")
	private UsoSuoloModel codUsoSuolo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATO_COLT", referencedColumnName = "STATO_COLT")
	private StatoColtModel statoColt;

	@Column(name = "SORGENTE")
	private String sorgente;

	@Column(name = "NOTE")
	private String note;

	@Column(name = "CAMPAGNA")
	private Integer campagna;

	@Column(name = "UTENTE")
	private String utente;

	@Column(name = "DATA_INIZIO_VALIDITA")
	private LocalDateTime dataInizioValidita;

	@Column(name = "AREA")
	private Double area;

	@Column(name = "SHAPE", columnDefinition = "SDO_GEOMETRY")
	private Geometry shape;

	@Column(name = "ESITO_VALIDAZIONE")
	private String esitoValidazione;

	@Column(name = "POSIZIONE_POLIGONO")
	private String posizionePoligono;

	@Column(name = "POLIGONO_INTERO")
	private Integer poligonoIntero;

	@Column(name = "ID_SUOLO_ORIGINALE")
	private Long idSuoloOriginale;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAVORAZIONE")
	private LavorazioneSuoloModel lavorazioneSuolo;

	public GrigliaSuoloModel getIdGrid() {
		return idGrid;
	}

	public void setIdGrid(GrigliaSuoloModel idGrid) {
		this.idGrid = idGrid;
	}

	public String getIstatp() {
		return istatp;
	}

	public void setIstatp(String istatp) {
		this.istatp = istatp;
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

	public Geometry getShape() {
		return shape;
	}

	public void setShape(Geometry shape) {
		this.shape = shape;
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

	public LavorazioneSuoloModel getLavorazioneSuolo() {
		return lavorazioneSuolo;
	}

	public void setLavorazioneSuolo(LavorazioneSuoloModel lavorazioneSuolo) {
		this.lavorazioneSuolo = lavorazioneSuolo;
	}

	public void tempClipSuADLModel(LavorazioneSuoloModel lavorazioneSuolo) {
		this.lavorazioneSuolo = lavorazioneSuolo;
	}

	public UsoSuoloModel getCodUsoSuolo() {
		return codUsoSuolo;
	}

	public void setCodUsoSuolo(UsoSuoloModel codUsoSuolo) {
		this.codUsoSuolo = codUsoSuolo;
	}

	public StatoColtModel getStatoColt() {
		return statoColt;
	}

	public void setStatoColt(StatoColtModel statoColt) {
		this.statoColt = statoColt;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public LocalDateTime getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(LocalDateTime dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
}
