package it.tndigitale.a4gistruttoria.dto;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;

public class DatiDettaglioFilter {

	private Sostegno sostegno;
	private String cuaa;

	public Sostegno getSostegno() {
		return sostegno;
	}

	public void setSostegno(Sostegno sostegno) {
		this.sostegno = sostegno;
	}

	public String getCuaa() {
		return cuaa;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
}
