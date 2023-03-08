package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4ST_PROPRIETA_LAYER")
public class ProprietaLayerModel extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8291558206447033519L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAYER")
	private LayerModel layerProprietaLayerModel;

	private String nome;

	private String valore;

	public LayerModel getLayerProprietaLayerModel() {
		return layerProprietaLayerModel;
	}

	public void setLayerProprietaLayerModel(LayerModel layerProprietaLayerModel) {
		this.layerProprietaLayerModel = layerProprietaLayerModel;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

}
