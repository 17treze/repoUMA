package it.tndigitale.a4g.fascicolo.mediator.business.service.utente;

import it.tndigitale.a4g.fascicolo.mediator.business.service.client.AnagraficaPrivateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("abilitazioniComponent")
public class AbilitazioniComponent {

//	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired private AnagraficaPrivateClient anagraficaPrivateClient;

//	verifica se l'utente connesso puo' consultare il fascicolo live
	public boolean checkLetturaFascicolo(final String cuaa) {
		return anagraficaPrivateClient.checkLetturaFascicolo(cuaa);
	}

	public boolean isAventeDirittoFirmaOrCaa(final String cuaa) {
		return anagraficaPrivateClient.isAventeDirittoFirmaOrCaaUsingGET(cuaa);
	}

//	verifica se l'utente connesso può aprire un fascicolo
	public boolean checkAperturaFascicolo(final String cuaa) {
		return anagraficaPrivateClient.checkAperturaFascicolo(cuaa);
	}
}
