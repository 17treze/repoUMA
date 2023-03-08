package it.tndigitale.a4gistruttoria.repository.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the A4GT_SOSTEGNO database table.
 * 
 */
@Entity
@Table(name = "A4GT_SOSTEGNO")
public class SostegnoModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 6719738701269959638L;

	@Enumerated(EnumType.STRING)
    private Sostegno sostegno;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOMANDA")
    private DomandaUnicaModel domandaUnicaModel;

	public Sostegno getSostegno() {
		return sostegno;
	}

	public void setSostegno(Sostegno sostegno) {
		this.sostegno = sostegno;
	}

	public DomandaUnicaModel getDomandaUnicaModel() {
		return domandaUnicaModel;
	}

	public void setDomandaUnicaModel(DomandaUnicaModel domandaUnicaModel) {
		this.domandaUnicaModel = domandaUnicaModel;
	}

}