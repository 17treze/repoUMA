package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "A4GD_REG_OLIO_NAZIONALE")
@NamedQuery(name = "RegistroOlioNazionaleModel.findAll", query = "SELECT a FROM RegistroOlioNazionaleModel a")
public class RegistroOlioNazionaleModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {

	@Column(name = "CUAA_INTESTATARIO")
	private String cuaaIntestatario;
	@Column(name = "INIZIO_CAMPAGNA")
	private Integer inizioCampagna;
	@Column(name = "FINE_CAMPAGNA")
	private Integer fineCampagna;


	public String getCuaaIntestatario() {
		return cuaaIntestatario;
	}

	public RegistroOlioNazionaleModel setCuaaIntestatario(String cuaaIntestatario) {
		this.cuaaIntestatario = cuaaIntestatario;
		return this;
	}

	public Integer getInizioCampagna() {
		return inizioCampagna;
	}

	public RegistroOlioNazionaleModel setInizioCampagna(Integer inizioCampagna) {
		this.inizioCampagna = inizioCampagna;
		return this;
	}

	public Integer getFineCampagna() {
		return fineCampagna;
	}

	public RegistroOlioNazionaleModel setFineCampagna(Integer fineCampagna) {
		this.fineCampagna = fineCampagna;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RegistroOlioNazionaleModel that = (RegistroOlioNazionaleModel) o;
		return Objects.equals(cuaaIntestatario, that.cuaaIntestatario) &&
				Objects.equals(inizioCampagna, that.inizioCampagna) &&
				Objects.equals(fineCampagna, that.fineCampagna);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cuaaIntestatario, inizioCampagna, fineCampagna);
	}
}
