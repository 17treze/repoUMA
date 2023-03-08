package it.tndigitale.a4g.richiestamodificasuolo.dto.layer;

import java.io.Serializable;

public class ProprietaLayerDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3719108484071538456L;

	private String nome;

	private String valore;

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
