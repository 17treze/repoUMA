package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "A4GT_REGISTRO_DOP")
public class RegistroDopModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	private static final long serialVersionUID = 4867495103915612945L;

	@Column(name = "CUAA", nullable = false)
	private String cuaa;

	@Column(name = "CAMPAGNA_INIZIO", nullable = false)
	private Integer campagnaInizio;

	@Column(name = "CAMPAGNA_FINE", nullable = true)
	private Integer campagnaFine;


	public String getCuaa() {
		return cuaa;
	}

	public RegistroDopModel setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}

	public Integer getCampagnaInizio() {
		return campagnaInizio;
	}

	public RegistroDopModel setCampagnaInizio(Integer campagnaInizio) {
		this.campagnaInizio = campagnaInizio;
		return this;
	}

	public Integer getCampagnaFine() {
		return campagnaFine;
	}

	public RegistroDopModel setCampagnaFine(Integer campagnaFine) {
		this.campagnaFine = campagnaFine;
		return this;
	}
}
