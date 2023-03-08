package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Entity
@Table(name = "A4ST_LAYER")
public class LayerModel extends EntitaDominio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5033743181338623045L;

	private String codice;

	private String workspace;

	private String url;

	@Column(name = "NOME_LAYER")
	private String nomeLayer;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "layerModel")
	private List<LayerProfiloModel> listLayerProfilo;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "layerProprietaLayerModel")
	private List<ProprietaLayerModel> listProprietaLayer;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "layerModel")
	private List<AttributiLayerModel> listAttributiLayer;

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNomeLayer() {
		return nomeLayer;
	}

	public void setNomeLayer(String nomeLayer) {
		this.nomeLayer = nomeLayer;
	}

	public List<LayerProfiloModel> getListLayerProfilo() {
		return listLayerProfilo;
	}

	public void setListLayerProfilo(List<LayerProfiloModel> listLayerProfilo) {
		this.listLayerProfilo = listLayerProfilo;
	}

	public List<ProprietaLayerModel> getListProprietaLayer() {
		return listProprietaLayer;
	}

	public void setListProprietaLayer(List<ProprietaLayerModel> listProprietaLayer) {
		this.listProprietaLayer = listProprietaLayer;
	}

	public List<AttributiLayerModel> getListAttributiLayer() {
		return listAttributiLayer;
	}

	public void setListAttributiLayer(List<AttributiLayerModel> listAttributiLayer) {
		this.listAttributiLayer = listAttributiLayer;
	}

}
