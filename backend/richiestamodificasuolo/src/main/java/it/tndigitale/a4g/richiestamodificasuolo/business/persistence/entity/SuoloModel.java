package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

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
@Table(name = "A4ST_SUOLO")
@DynamicUpdate
public class SuoloModel extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = -278866270453965187L;

	private String istatp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_GRID")
	private GrigliaSuoloModel idGrid;

	// @org.hibernate.annotations.Type(type = "org.hibernate.spatial.GeometryType")
	@Column(name = "SHAPE", columnDefinition = "SDO_GEOMETRY")
	private Geometry shape;

	@Column(name = "AREA_COLT")
	private Double areaColt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_USO_SUOLO", referencedColumnName = "COD_USO_SUOLO")
	private UsoSuoloModel codUsoSuoloModel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATO_COLT", referencedColumnName = "STATO_COLT")
	private StatoColtModel statoColtSuolo;

	private String sorgente;

	@Column(name = "DATA_INIZIO_VALIDITA")
	private LocalDateTime dataInizioValidita;

	@Column(name = "DATA_FINE_VALIDITA")
	private LocalDateTime dataFineValidita;

	private String note;

	private Integer campagna;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAVORAZIONE_IN_CORSO")
	private LavorazioneSuoloModel idLavorazioneInCorso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAVORAZIONE_INIZIO")
	private LavorazioneSuoloModel idLavorazioneInizio;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAVORAZIONE_FINE")
	private LavorazioneSuoloModel idLavorazioneFine;

	public String getIstatp() {
		return istatp;
	}

	public void setIstatp(String istatp) {
		this.istatp = istatp;
	}

	public GrigliaSuoloModel getIdGrid() {
		return idGrid;
	}

	public void setIdGrid(GrigliaSuoloModel idGrid) {
		this.idGrid = idGrid;
	}

	public Geometry getShape() {
		return shape;
	}

	public void setShape(Geometry shape) {
		this.shape = shape;
	}

	public Double getAreaColt() {
		return areaColt;
	}

	public void setAreaColt(Double areaColt) {
		this.areaColt = areaColt;
	}

	public UsoSuoloModel getCodUsoSuoloModel() {
		return codUsoSuoloModel;
	}

	public void setCodUsoSuoloModel(UsoSuoloModel codUsoSuoloModel) {
		this.codUsoSuoloModel = codUsoSuoloModel;
	}

	public StatoColtModel getStatoColtSuolo() {
		return statoColtSuolo;
	}

	public void setStatoColtSuolo(StatoColtModel statoColtSuolo) {
		this.statoColtSuolo = statoColtSuolo;
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

	public LavorazioneSuoloModel getIdLavorazioneInCorso() {
		return idLavorazioneInCorso;
	}

	public void setIdLavorazioneInCorso(LavorazioneSuoloModel idLavorazioneInCorso) {
		this.idLavorazioneInCorso = idLavorazioneInCorso;
	}

	public LavorazioneSuoloModel getIdLavorazioneInizio() {
		return idLavorazioneInizio;
	}

	public void setIdLavorazioneInizio(LavorazioneSuoloModel idLavorazioneInizio) {
		this.idLavorazioneInizio = idLavorazioneInizio;
	}

	public LavorazioneSuoloModel getIdLavorazioneFine() {
		return idLavorazioneFine;
	}

	public void setIdLavorazioneFine(LavorazioneSuoloModel idLavorazioneFine) {
		this.idLavorazioneFine = idLavorazioneFine;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof SuoloModel))
			return false;
		return id != null && id.equals(((SuoloModel) o).getId());
	}

	@Override
	public int hashCode() {
		return Optional.ofNullable(id).map(i -> i.hashCode()).orElseGet(() -> getClass().hashCode());
	}
}
