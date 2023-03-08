package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "A4GT_ETICHETTURA_CARNE")
@NamedQuery(name = "A4gtEtichettaturaCarne.findAll", query = "SELECT a FROM A4gtEtichettaturaCarne a")
public class A4gtEtichettaturaCarne extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	private static final long serialVersionUID = 1L;
	@Column(name = "CUAA")
	private String cuaa;
	@Column(name = "DT_INIZIO_VALIDITA")
	private Date dtInizioValidita;
	@Column(name = "CODICE_ASL")
	private String codiceAsl;
	@Column(name = "DT_FINE_VALIDITA")
	private Date dtFineValidita;

	public String getCuaa() {
		return cuaa;
	}

	public Date getDtInizioValidita() {
		return dtInizioValidita;
	}

	public String getCodiceAsl() {
		return codiceAsl;
	}

	public Date getDtFineValidita() {
		return dtFineValidita;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public void setDtInizioValidita(Date dtInizioValidita) {
		this.dtInizioValidita = dtInizioValidita;
	}

	public void setCodiceAsl(String codiceAsl) {
		this.codiceAsl = codiceAsl;
	}

	public void setDtFineValidita(Date dtFineValidita) {
		this.dtFineValidita = dtFineValidita;
	}
}
