package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.InputCopiaLavorazione;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("CAMBIA_CAMPAGNA")
public class CambiaCampagnaLavorazioneBusiness extends AzioneLavorazioneBase<LavorazioneSuoloModel, InputCopiaLavorazione> {

	private static final Logger log = LoggerFactory.getLogger(CambiaCampagnaLavorazioneBusiness.class);

	private static final List<StatoLavorazioneSuolo> listaStatiConsentiti = Arrays.asList(StatoLavorazioneSuolo.IN_CREAZIONE, StatoLavorazioneSuolo.IN_MODIFICA);

	@Override
	protected LavorazioneSuoloModel eseguiAzione(InputCopiaLavorazione inputCopiaLavorazione) {
		return cambiaCampagnaLavorazione(inputCopiaLavorazione.getUtente(), inputCopiaLavorazione.getAnnoCampagna(), inputCopiaLavorazione.getIdLavorazionePadre());
	}

	public LavorazioneSuoloModel cambiaCampagnaLavorazione(String username, Integer annoCampagna, Long idLavorazione) {

		log.debug("START - insertDaCopiaLavorazioneSuolo ");

		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrNotFound(idLavorazione, username);

		checkValidInputData(lavorazione, annoCampagna);

		checkValidState(lavorazione);

		LavorazioneSuoloModel lavorazionTmp = cleanLavorazione(lavorazione, false, false);

		lavorazionTmp.setCampagna(annoCampagna);
		lavorazionTmp.setDataUltimaModifica(getClock().now());

		LavorazioneSuoloModel responseLavorazione = getLavorazioneSuoloDao().save(lavorazionTmp);

		log.debug("END - insertLavorazioneSuolo - Copia e Insert Lavorazione con id # {}", responseLavorazione.getId());
		return responseLavorazione;
	}

	protected void checkValidState(LavorazioneSuoloModel lavorazione) {
		if (!listaStatiConsentiti.contains(lavorazione.getStato())) {
			log.error("LavorazioneSuoloModel {} non in stato corretto per cancellazione {}", lavorazione.getId(), lavorazione.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance(
					"Lo stato ".concat(lavorazione.getStato().name()).concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente il cambio campagna"))));
		}
	}

	protected void checkValidInputData(LavorazioneSuoloModel lavorazione, Integer annoCampagna) {
		if (annoCampagna.equals(lavorazione.getCampagna())) {
			log.error("LavorazioneSuoloModel {} è già assegnato alla campagna {}", lavorazione.getId(), annoCampagna);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("La lavorazione è già assegnato alla campagna " + annoCampagna);
		}
	}
}
