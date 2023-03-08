package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ModalitaADL;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.InputCreazioneLavorazione;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("CREAZIONE")
public class CreazioneLavorazioneBusiness extends AzioneLavorazioneBase<LavorazioneSuoloModel, InputCreazioneLavorazione> {

	private static final Logger log = LoggerFactory.getLogger(CreazioneLavorazioneBusiness.class);

	@Override
	protected LavorazioneSuoloModel eseguiAzione(InputCreazioneLavorazione inputCreazioneLavorazione) {
		return createLavorazioneSuolo(inputCreazioneLavorazione.getUtente(), inputCreazioneLavorazione.getAnnoCampagna());
	}

	public LavorazioneSuoloModel createLavorazioneSuolo(String username, Integer annoCampagna) {

		log.debug("START - insertLavorazioneSuolo ");
		chechPresentEmptyLavorazioni(username);

		LavorazioneSuoloModel newLavorazioneSuoloModel = new LavorazioneSuoloModel();

		newLavorazioneSuoloModel.setUtente(username);
		newLavorazioneSuoloModel.setDataInizioLavorazione(getClock().now());
		newLavorazioneSuoloModel.setDataUltimaModifica(getClock().now());
		newLavorazioneSuoloModel.setStato(StatoLavorazioneSuolo.IN_CREAZIONE);
		newLavorazioneSuoloModel.setCampagna(annoCampagna);
		newLavorazioneSuoloModel.setModalitaAdl(ModalitaADL.POLIGONI_INTERI);
		LavorazioneSuoloModel responseLavorazione = getLavorazioneSuoloDao().save(newLavorazioneSuoloModel);

		log.debug("END - insertLavorazioneSuolo - Nuova Lavorazione con id # {}", responseLavorazione.getId());

		return responseLavorazione;
	}

	protected void chechPresentEmptyLavorazioni(String utente) {
		if (getLavorazioneSuoloDao().countExistLavorazioniEmpty(utente, StatoLavorazioneSuolo.IN_CREAZIONE) > 0) {
			log.error("Esiste già una lavorazione in creazione associata all'utente {} vuota", utente);
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("L'utente ".concat(utente).concat(" ha già associato una lavorazione vuota"));
		}
	}
}
