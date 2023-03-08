package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.locationtech.jts.geom.Geometry;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4ST_GRIGLIA_SUOLO")
public class GrigliaSuoloModel extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "INTERSECA_SUOLO")
	private boolean intersecaSuolo;

	@Column(name = "SHAPE", columnDefinition = "SDO_GEOMETRY")
	private Geometry shape;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "idGrid")
	private List<SuoloModel> listaSuoloModel;

	@Column(name = "AREA")
	private Double area;

	public boolean isIntersecaSuolo() {
		return intersecaSuolo;
	}

	public void setIntersecaSuolo(boolean intersecaSuolo) {
		this.intersecaSuolo = intersecaSuolo;
	}

	public Geometry getShape() {
		return shape;
	}

	public void setShape(Geometry shape) {
		this.shape = shape;
	}

	public List<SuoloModel> getListaSuoloModel() {
		return listaSuoloModel;
	}

	public void setListaSuoloModel(List<SuoloModel> listaSuoloModel) {
		this.listaSuoloModel = listaSuoloModel;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

}
