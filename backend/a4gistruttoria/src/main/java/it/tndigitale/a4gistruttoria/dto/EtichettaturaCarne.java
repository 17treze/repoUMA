package it.tndigitale.a4gistruttoria.dto;

import java.util.Date;

public class EtichettaturaCarne {

	private Long id;
	private String cuaa;
	private String codiceAsl;
	private Date dtFineValidita;
	private Date dtInizioValidita;

	public Long getId() {
		return id;
	}

	public String getCuaa() {
		return cuaa;
	}

	public String getCodiceAsl() {
		return codiceAsl;
	}

	public Date getDtFineValidita() {
		return dtFineValidita;
	}

	public Date getDtInizioValidita() {
		return dtInizioValidita;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public void setCodiceAsl(String codiceAsl) {
		this.codiceAsl = codiceAsl;
	}

	public void setDtFineValidita(Date dtFineValidita) {
		this.dtFineValidita = dtFineValidita;
	}

	public void setDtInizioValidita(Date dtInizioValidita) {
		this.dtInizioValidita = dtInizioValidita;
	}

}
