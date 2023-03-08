package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;

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
@Table(name = "A4ST_AREA_DI_LAVORO")
@DynamicUpdate
public class AreaDiLavoroModel extends EntitaDominio implements Serializable {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAVORAZIONE")
	private LavorazioneSuoloModel lavorazioneSuolo;

	@Column(name = "SHAPE", columnDefinition = "SDO_GEOMETRY")
	private Geometry shape;

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

}
