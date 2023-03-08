package it.tndigitale.a4g.richiestamodificasuolo.dto.layer;

import java.io.Serializable;
import java.util.List;

public class LayerSwitcherDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4180732592798075147L;

	private Long id;

	private String codice;

	private String url;

	private String nomeLayer;

	private String workspace;

	private List<ProprietaLayerDto> proprieta;

	private List<AttributiLayerDto> attributi;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<ProprietaLayerDto> getProprieta() {
		return proprieta;
	}

	public void setProprieta(List<ProprietaLayerDto> proprieta) {
		this.proprieta = proprieta;
	}

	public String getNomeLayer() {
		return nomeLayer;
	}

	public void setNomeLayer(String nomeLayer) {
		this.nomeLayer = nomeLayer;
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public List<AttributiLayerDto> getAttributi() {
		return attributi;
	}

	public void setAttributi(List<AttributiLayerDto> attributi) {
		this.attributi = attributi;
	}

}
