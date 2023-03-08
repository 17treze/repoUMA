package it.tndigitale.a4gistruttoria.service.businesslogic.processo.istruttoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.service.businesslogic.avvio.AvvioIntegrazioneIstruttoriaService;

@Component("INTEGRAZIONE_ISTRUTTORIE")
public class ElaboraIntegrazioneIstruttorie extends ElaboraIstruttoria {
	@Autowired
	private AvvioIntegrazioneIstruttoriaService serviceAvvio;

	@Override
	protected AvvioIntegrazioneIstruttoriaService getElaborazioneIstruttoriaService() {
		return serviceAvvio;
	}

	/**
	 * Non voglio notificare alcun errore all'istruttoria originaria
	 */
	@Override
	protected void istruttoriaInErrore(Long idIstruttoria, Exception e) {
	}
}
