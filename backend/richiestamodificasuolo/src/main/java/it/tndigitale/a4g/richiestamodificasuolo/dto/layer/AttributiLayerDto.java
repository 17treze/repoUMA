package it.tndigitale.a4g.richiestamodificasuolo.dto.layer;

import java.io.Serializable;

public class AttributiLayerDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nome;

	private String valoreDefault;

	private Byte editabile;

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
