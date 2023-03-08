package it.tndigitale.a4g.richiestamodificasuolo.business.service.evento;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;

public abstract class LavorazioneSuoloEvento {
	private LavorazioneSuoloModel lavorazione;

	public LavorazioneSuoloModel getLavorazione() {
		return lavorazione;
	}

	public void setLavorazione(LavorazioneSuoloModel lavorazione) {
		this.lavorazione = lavorazione;
	}
}
