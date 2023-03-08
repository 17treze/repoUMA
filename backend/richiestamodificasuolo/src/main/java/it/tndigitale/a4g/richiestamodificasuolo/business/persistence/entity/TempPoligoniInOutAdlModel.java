package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.locationtech.jts.geom.Geometry;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4ST_TEMP_POLIGONI_IN_OUT_ADL")
public class TempPoligoniInOutAdlModel extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7226172395164674693L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAVORAZIONE")
	private LavorazioneSuoloModel lavorazioneSuolo;

	@Column(name = "SHAPE", columnDefinition = "SDO_GEOMETRY")
	private Geometry shape;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_USO_SUOLO", referencedColumnName = "COD_USO_SUOLO")
	private UsoSuoloModel codUsoSuolo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATO_COLT", referencedColumnName = "STATO_COLT")
	private StatoColtModel statoColt;

	@Column(name = "NOTE")
	private String note;

	@Column(name = "AREA")
	private Double area;

	@Column(name = "ISTATP")
	private String istatp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_GRID")
	private GrigliaSuoloModel grid;

	@Column(name = "SORGENTE")
	private String sorgente;

	@Column(name = "ESITO_VALIDAZIONE")
	private String esitoValidazione;

	public LavorazioneSuoloModel getLavorazioneSuolo() {
		return lavorazioneSuolo;
	}

	public void setLavorazioneSuolo(LavorazioneSuoloModel lavorazioneSuolo) {
		this.lavorazioneSuolo = lavorazioneSuolo;
	}

	public Geometry getShape() {
		return shape;
	}

	public void setShape(Geometry shape) {
		this.shape = shape;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public String getIstatp() {
		return istatp;
	}

	public void setIstatp(String istatp) {
		this.istatp = istatp;
	}

	public GrigliaSuoloModel getGrid() {
		return grid;
	}

	public void setGrid(GrigliaSuoloModel grid) {
		this.grid = grid;
	}

	public String getSorgente() {
		return sorgente;
	}

	public void setSorgente(String sorgente) {
		this.sorgente = sorgente;
	}

	public String getEsitoValidazione() {
		return esitoValidazione;
	}

	public void setEsitoValidazione(String esitoValidazione) {
		this.esitoValidazione = esitoValidazione;
	}

}