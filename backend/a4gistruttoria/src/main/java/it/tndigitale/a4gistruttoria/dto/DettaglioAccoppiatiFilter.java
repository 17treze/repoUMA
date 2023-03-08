package it.tndigitale.a4gistruttoria.dto;

import java.io.Serializable;

import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

public class DettaglioAccoppiatiFilter implements Serializable {
	
	private static final long serialVersionUID = -459189769986869093L;
	
	private StatoIstruttoria statoLavorazioneSostegno;

	public StatoIstruttoria getStatoLavorazioneSostegno() {
		return statoLavorazioneSostegno;
	}

	public void setStatoLavorazioneSostegno(StatoIstruttoria statoLavorazioneSostegno) {
		this.statoLavorazioneSostegno = statoLavorazioneSostegno;
	}
}
