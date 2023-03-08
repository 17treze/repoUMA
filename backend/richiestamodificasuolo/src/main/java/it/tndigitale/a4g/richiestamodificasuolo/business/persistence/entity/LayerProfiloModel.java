package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4SR_LAYER_PROFILO")
public class LayerProfiloModel extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5786296638921600153L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAYER")
	private LayerModel layerModel;

	@Column(name = "profilo_utente")
	private String profiloUtente;

	public LayerModel getLayerModel() {
		return layerModel;
	}

	public void setLayerModel(LayerModel layerModel) {
		this.layerModel = layerModel;
	}

	public String getProfiloUtente() {
		return profiloUtente;
	}

	public void setProfiloUtente(String profiloUtente) {
		this.profiloUtente = profiloUtente;
	}

}
