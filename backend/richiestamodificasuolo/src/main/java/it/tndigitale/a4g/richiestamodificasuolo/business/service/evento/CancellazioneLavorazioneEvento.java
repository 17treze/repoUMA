package it.tndigitale.a4g.richiestamodificasuolo.business.service.evento;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;

public class CancellazioneLavorazioneEvento extends LavorazioneSuoloEvento {

	public CancellazioneLavorazioneEvento(LavorazioneSuoloModel lavorazione) {
		super();
		setLavorazione(lavorazione);
	}

	public CancellazioneLavorazioneEvento() {
		super();
	}
}
