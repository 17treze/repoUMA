package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.LavorazioneSuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.evento.CancellazioneLavorazioneEvento;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase.BaseInputData;
import it.tndigitale.a4g.richiestamodificasuolo.dto.mapper.LavorazioneSuoloMapper;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("CANCELLAZIONE")
public class EliminaLavorazioneBusiness extends AzioneLavorazioneBase<LavorazioneSuoloModel, BaseInputData> {

	private static final Logger log = LoggerFactory.getLogger(EliminaLavorazioneBusiness.class);

	private static final List<StatoLavorazioneSuolo> listaStatiConsentiti = Arrays.asList(StatoLavorazioneSuolo.IN_CREAZIONE, StatoLavorazioneSuolo.IN_CORSO, StatoLavorazioneSuolo.SOSPESA,
			StatoLavorazioneSuolo.IN_MODIFICA);

	@Autowired
	private AssociazionePoligoniALavorazioneDaDichiaratoBusiness associazionePoligoniComponent;

	@Autowired
	private LavorazioneSuoloMapper lavorazioneSuoloMapper;

	@Override
	protected LavorazioneSuoloModel eseguiAzione(BaseInputData inputData) {
		deleteLavorazioneSuolo(inputData.getIdLavorazione(), inputData.getUtente());
		return null;
	}

	/**
	 * Questo metodo effettua la cancellazione della lavorazione i suoi side effect sono: 1) Ripristino stato suolo dichiarato 2) Rimozione del suolo dichiarato dalla lavorazione 3) Rimozione del
	 * suolo dalla lavorazione 4) Rollback stato richiesta collegato ai suoli dichiarati 5) Rimozione delle anomalie e poligoni di workspace (effettuata tramite hibernate)
	 * 
	 * @param idLavorazione
	 * @param utente
	 */
	public void deleteLavorazioneSuolo(Long idLavorazione, String utente) {
		log.debug("START - deleteLavorazioneSuolo {}", idLavorazione);
		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrNotFound(idLavorazione, utente);
		LavorazioneSuoloModel cloneLavorazione = lavorazioneSuoloMapper.clona(lavorazione);

		chechValidState(lavorazione);

		rollbackEsitoDichiarato(lavorazione);
		dissociaSuoloDichiaratoDaLavorazione(lavorazione);

		dissociaSuoloDaLavorazione(lavorazione);

		LavorazioneSuoloDao lavorazioneSuoloDao = getLavorazioneSuoloDao();
		// lavorazioneSuoloDao.save(lavorazione);

		getEventoPublisher().notificaEvento(new CancellazioneLavorazioneEvento(cloneLavorazione));

		lavorazioneSuoloDao.delete(lavorazione);

		log.debug("END - deleteLavorazioneSuolo");
	}

	protected void chechValidState(LavorazioneSuoloModel lavorazione) {
		if (!listaStatiConsentiti.contains(lavorazione.getStato())) {
			log.error("LavorazioneSuoloModel {} non in stato corretto per cancellazione {}", lavorazione.getId(), lavorazione.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance(
					"Lo stato ".concat(lavorazione.getStato().name()).concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente la cancellazione"))));

		}

	}
}
