package it.tndigitale.a4gistruttoria.repository.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The persistent class for the A4GD_INTERVENTO_DU_PREMIO database table.
 * 
 */
@Entity
@Table(name = "A4GT_IMPORTO_UNITARIO")
@NamedQuery(name = "ImportoUnitarioInterventoModel.findAll", query = "SELECT a FROM ImportoUnitarioInterventoModel a")
public class ImportoUnitarioInterventoModel extends it.tndigitale.a4g.framework.repository.model.EntitaDominio implements Serializable {
	private static final long serialVersionUID = 6832898089200980187L;

	@Column(name = "ANNO_CAMPAGNA")
	private Integer campagna;

	@Column(name = "VALORE_UNITARIO", nullable = false, precision = 10, scale = 2)
	private BigDecimal valoreUnitario;
	
	@Column(name = "PRIORITA")
	private Integer priorita;

	// bi-directional many-to-one association to InterventoModel
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_INTERVENTO", nullable = false)
	private InterventoModel intervento;

	public Integer getCampagna() {
		return campagna;
	}

	public void setCampagna(Integer campagna) {
		this.campagna = campagna;
	}

	public BigDecimal getValoreUnitario() {
		return this.valoreUnitario;
	}

	public void setValoreUnitario(BigDecimal valoreUnitario) {
		this.valoreUnitario = valoreUnitario;
	}
	
	public Integer getPriorita() {
		return priorita;
	}

	public void setPriorita(Integer priorita) {
		this.priorita = priorita;
	}

	public InterventoModel getIntervento() {
		return this.intervento;
	}

	public void setIntervento(InterventoModel intervento) {
		this.intervento = intervento;
	}

}