package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.FascicoloValidazioneEnum;

public class MandatoVerificaException extends Exception {

	public MandatoVerificaException(final FascicoloValidazioneEnum aperturaNotification) {
		super(aperturaNotification.name());
	}

	public MandatoVerificaException(final MandatoVerificaEnum aperturaNotification) {
		super(aperturaNotification.name());
	}
}
