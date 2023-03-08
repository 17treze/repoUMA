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
@Table(name = "A4ST_ATTRIBUTI_LAYER")
public class AttributiLayerModel extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LAYER")
	private LayerModel layerModel;

	private String nome;

	@Column(name = "VALORE_DEFAULT")
	private String valoreDefault;

	private Byte editabile;

	public LayerModel getLayerModel() {
		return layerModel;
	}

	public void setLayerModel(LayerModel layerModel) {
		this.layerModel = layerModel;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getValoreDefault() {
		return valoreDefault;
	}

	public void setValoreDefault(String valoreDefault) {
		this.valoreDefault = valoreDefault;
	}

	public Byte getEditabile() {
		return editabile;
	}

	public void setEditabile(Byte editabile) {
		this.editabile = editabile;
	}

}