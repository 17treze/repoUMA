package it.tndigitale.a4gistruttoria.dto;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;

public class ControlloLiquidabilita {
	private Long idDomanda;
	private Sostegno sostegno;

	public Long getIdDomanda() {
		return idDomanda;
	}

	public Sostegno getSostegno() {
		return sostegno;
	}

	public void setIdDomanda(Long idDomanda) {
		this.idDomanda = idDomanda;
	}

	public void setSostegno(Sostegno sostegno) {
		this.sostegno = sostegno;
	}

}
