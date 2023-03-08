package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.Copertura;

@Entity
@Table(name="A4GT_STOCCAGGIO")
public class StoccaggioModel extends FabbricatoModel {
	private static final long serialVersionUID = -7592445655304561409L;

	@Column(name="COPERTURA", length = 50)
	@Enumerated(EnumType.STRING)
	private Copertura copertura;
	
	@Column(name="ALTEZZA", length = 20)
	private Long altezza;

	public Long getAltezza() {
		return altezza;
	}

	public StoccaggioModel setAltezza(Long altezza) {
		this.altezza = altezza;
		return this;
	}

	public Copertura getCopertura() {
		return copertura;
	}

	public StoccaggioModel setCopertura(Copertura copertura) {
		this.copertura = copertura;
		return this;
	}

}
