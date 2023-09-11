package it.tndigitale.a4g.uma.business.persistence.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="TAB_AGRI_UMAL_FABBISOGNO_FABBRICATI")
public class FabbisognoFabbricatoModel extends FabbisognoModel implements Serializable {

	private static final long serialVersionUID = -8182144211702106179L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FABBRICATO")
	private FabbricatoModel fabbricatoModel;

	public FabbricatoModel getFabbricatoModel() {
		return fabbricatoModel;
	}
	public FabbisognoFabbricatoModel setFabbricatoModel(FabbricatoModel fabbricatoModel) {
		this.fabbricatoModel = fabbricatoModel;
		return this;
	}
}