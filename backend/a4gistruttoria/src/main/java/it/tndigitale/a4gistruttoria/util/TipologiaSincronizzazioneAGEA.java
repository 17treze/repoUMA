package it.tndigitale.a4gistruttoria.util;

import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;

public enum TipologiaSincronizzazioneAGEA {
	SINCRONIZZAZIONE_SUPERFICI_ACCERTATE(TipoProcesso.SINCRONIZZAZIONE_SUPERFICI_ACCERTATE),
	SINCRONIZZAZIONE_PAGAMENTI(TipoProcesso.SINCRONIZZAZIONE_PAGAMENTI);

	private TipologiaSincronizzazioneAGEA(TipoProcesso elaborazione) {
		this.elaborazione = elaborazione;
	}

	private TipoProcesso elaborazione;

	public TipoProcesso getElaborazione() {
		return elaborazione;
	}
}
