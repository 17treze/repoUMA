package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;

public class RichiestaAllevamDuEsito implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long capoId;
	private String codiceCapo;
	private String esito;
	private String messaggio;

	public Long getCapoId() {
		return capoId;
	}

	public String getCodiceCapo() {
		return codiceCapo;
	}

	public String getEsito() {
		return esito;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setCapoId(Long capoId) {
		this.capoId = capoId;
	}

	public void setCodiceCapo(String codiceCapo) {
		this.codiceCapo = codiceCapo;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
