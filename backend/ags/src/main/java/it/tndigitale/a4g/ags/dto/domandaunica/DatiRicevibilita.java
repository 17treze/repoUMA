package it.tndigitale.a4g.ags.dto.domandaunica;

import java.util.Set;

import it.tndigitale.a4g.ags.dto.ErroreControlloRicevibilitaDomanda;

public class DatiRicevibilita {

	private DatiDomanda domanda;
	private Set<ErroreControlloRicevibilitaDomanda> erroriRicevibilita;
	
	DatiDomanda getDomanda() {
		return domanda;
	}
	Set<ErroreControlloRicevibilitaDomanda> getErroriRicevibilita() {
		return erroriRicevibilita;
	}
	void setDomanda(DatiDomanda domanda) {
		this.domanda = domanda;
	}
	void setErroriRicevibilita(Set<ErroreControlloRicevibilitaDomanda> erroriRicevibilita) {
		this.erroriRicevibilita = erroriRicevibilita;
	}

}
