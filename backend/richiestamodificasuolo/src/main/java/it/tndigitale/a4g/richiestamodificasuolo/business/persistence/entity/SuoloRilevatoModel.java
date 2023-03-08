package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.locationtech.jts.geom.Geometry;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

/**
 * The persistent class for the A4S_SUOLO_RILEVATO database table.
 * 
 */
@Entity
@Table(name = "A4ST_SUOLO_RILEVATO")
public class SuoloRilevatoModel extends EntitaDominio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ID_ISOLA")
	private Long idIsola;

	@Column(name = "ID_SUOL_GIS_RILEVATO")
	private Long idSuolGisRilevato;

	@Column(name = "SHAPE", columnDefinition = "SDO_GEOMETRY")
	private Geometry shape;

	@Column(name = "NOTE_CARICAMENTO")
	private String note;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RICHIESTA")
	private RichiestaModificaSuoloModel richiestaModificaSuolo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "CODI_RILE_RILEVATO", referencedColumnName = "CODI_RILE"), @JoinColumn(name = "CODI_PROD_RILE_RILEVATO", referencedColumnName = "CODI_PROD_RILE"), })
	private CodiRilePcgModel codiRilePcgRilevato;

	public Long getIdIsola() {
		return idIsola;
	}

	public void setIdIsola(Long idIsola) {
		this.idIsola = idIsola;
	}

	public Long getIdSuolGisRilevato() {
		return idSuolGisRilevato;
	}

	public void setIdSuolGisRilevato(Long idSuolGisRilevato) {
		this.idSuolGisRilevato = idSuolGisRilevato;
	}

	public Geometry getShape() {
		return shape;
	}

	public void setShape(Geometry shape) {
		this.shape = shape;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public RichiestaModificaSuoloModel getRichiestaModificaSuolo() {
		return richiestaModificaSuolo;
	}

	public void setRichiestaModificaSuolo(RichiestaModificaSuoloModel richiestaModificaSuolo) {
		this.richiestaModificaSuolo = richiestaModificaSuolo;
	}

	public CodiRilePcgModel getDescrizioneVarRilevato() {
		return codiRilePcgRilevato;
	}

	public void setDescrizioneVarRilevato(CodiRilePcgModel descrizioneVarRilevato) {
		this.codiRilePcgRilevato = descrizioneVarRilevato;
	}

}
