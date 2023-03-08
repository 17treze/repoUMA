package it.tndigitale.a4gistruttoria.dto;

import java.util.Date;

public class ConfigurazioneInstruttoriaZootecnicaFilter {

	private Date dtAperturaDomanda;
	private Date dtChiusuraDomanda;

	public Date getDtAperturaDomanda() {
		return dtAperturaDomanda;
	}

	public Date getDtChiusuraDomanda() {
		return dtChiusuraDomanda;
	}

	public void setDtAperturaDomanda(Date dtAperturaDomanda) {
		this.dtAperturaDomanda = dtAperturaDomanda;
	}

	public void setDtChiusuraDomanda(Date dtChiusuraDomanda) {
		this.dtChiusuraDomanda = dtChiusuraDomanda;
	}
}
