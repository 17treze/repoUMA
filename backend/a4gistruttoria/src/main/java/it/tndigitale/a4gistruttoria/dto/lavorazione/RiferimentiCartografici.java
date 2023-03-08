package it.tndigitale.a4gistruttoria.dto.lavorazione;

import java.io.Serializable;

public class RiferimentiCartografici implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idParcella;
	private Long idIsola;
	private String codIsola;

	public Long getIdParcella() {
		return idParcella;
	}

	public void setIdParcella(Long idParcella) {
		this.idParcella = idParcella;
	}

	public Long getIdIsola() {
		return idIsola;
	}

	public void setIdIsola(Long idIsola) {
		this.idIsola = idIsola;
	}

	public String getCodIsola() {
		return codIsola;
	}

	public void setCodIsola(String codIsola) {
		this.codIsola = codIsola;
	}
}
