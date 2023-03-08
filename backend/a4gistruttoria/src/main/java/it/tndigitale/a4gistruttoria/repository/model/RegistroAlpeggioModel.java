package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the A4GT_REG_ALPEGGIO database table.
 * 
 */
@Entity
@Table(name="A4GT_REG_ALPEGGIO")
public class RegistroAlpeggioModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 8135021591339980445L;

	@Column
	private String cuaa;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_FINE")
	private Date dtFine;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INIZIO")
	private Date dtInizio;

	@Column(name = "ANNO_CAMPAGNA", nullable = false)
	private Integer campagna;

	public RegistroAlpeggioModel() {
	}

	public String getCuaa() {
		return cuaa;
	}

	public RegistroAlpeggioModel setCuaa(String cuaa) {
		this.cuaa = cuaa;
		return this;
	}

	public Date getDtFine() {
		return dtFine;
	}

	public RegistroAlpeggioModel setDtFine(Date dtFine) {
		this.dtFine = dtFine;
		return this;
	}

	public Date getDtInizio() {
		return dtInizio;
	}

	public RegistroAlpeggioModel setDtInizio(Date dtInizio) {
		this.dtInizio = dtInizio;
		return this;
	}

	public Integer getCampagna() {
		return campagna;
	}

	public RegistroAlpeggioModel setCampagna(Integer campagna) {
		this.campagna = campagna;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RegistroAlpeggioModel that = (RegistroAlpeggioModel) o;
		return Objects.equals(cuaa, that.cuaa) &&
				Objects.equals(dtFine, that.dtFine) &&
				Objects.equals(dtInizio, that.dtInizio) &&
				Objects.equals(campagna, that.campagna);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cuaa, dtFine, dtInizio, campagna);
	}
}