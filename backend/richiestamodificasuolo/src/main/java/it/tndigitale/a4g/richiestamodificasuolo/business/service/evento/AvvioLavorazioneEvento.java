package it.tndigitale.a4g.richiestamodificasuolo.business.service.evento;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;

public class AvvioLavorazioneEvento extends LavorazioneSuoloEvento {

	public AvvioLavorazioneEvento(LavorazioneSuoloModel lavorazione) {
		super();
		setLavorazione(lavorazione);
	}

	public AvvioLavorazioneEvento() {
		super();
	}
}
