package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.SuoloDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione.DissociaPoligonoDaLavorazioneBusiness.DissociaPoligonoInputData;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("DISSOCIA_POLIGONO_VIGENTE")
public class DissociaPoligonoDaLavorazioneBusiness extends AzioneLavorazioneBase<LavorazioneSuoloModel, DissociaPoligonoInputData> {

	private static final Logger log = LoggerFactory.getLogger(DissociaPoligonoDaLavorazioneBusiness.class);

	@Autowired
	private SuoloDao suoloDao;

	@Override
	protected LavorazioneSuoloModel eseguiAzione(DissociaPoligonoInputData inputData) {
		return removePoligonoDaLavorazione(inputData.getIdLavorazione(), inputData.getVersione(), inputData.getUtente(), inputData.idPoligono);
	}

	public LavorazioneSuoloModel removePoligonoDaLavorazione(Long idLavorazione, Integer versione, String utente, Long idPoligono) {
		log.debug("start - removePoligonoDaLavorazione per lavorazione {} e poligono {}", idLavorazione, idPoligono);
		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrNotFound(idLavorazione, utente);
		checkValidState(lavorazione);

		SuoloModel suoloToUpdate = getSuoloOrThrowNotFound(idPoligono);
		lavorazione.setDataUltimaModifica(getClock().now());
		lavorazione.removeSuoloInCorsoModel(suoloToUpdate);

		getLavorazioneSuoloDao().save(lavorazione);
		log.debug("END - removePoligonoDaLavorazione ");
		return lavorazione;
	}

	protected SuoloModel getSuoloOrThrowNotFound(Long idPoligono) {
		Optional<SuoloModel> poligono = suoloDao.findById(idPoligono);
		if (!poligono.isPresent()) {
			log.error("SuoloModel non trovata per idpoligono  {}", idPoligono);
			throw SuoloException.ExceptionType.NOT_FOUND_EXCEPTION.newSuoloExceptionInstance("Entita non trovata per idPoligono ".concat(String.valueOf(idPoligono)));
		}
		SuoloModel suoloToUpdate = poligono.get();
		return suoloToUpdate;
	}

	protected void checkValidState(LavorazioneSuoloModel lavorazione) {
		if (!StatoLavorazioneSuolo.IN_CREAZIONE.equals(lavorazione.getStato()) && !StatoLavorazioneSuolo.IN_MODIFICA.equals(lavorazione.getStato())) {
			log.error("LavorazioneSuoloModel {} non in stato corretto per passaggio di stato a IN_CORSO {}", lavorazione.getId(), lavorazione.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Lo stato ".concat(lavorazione.getStato().name())
					.concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente l'associazione di suolo vigente "))));

		}
	}

	protected void checkCoerenzaOrThrowInvalidArgument(LavorazioneSuoloModel lavorazione, SuoloModel suolo) {
		if (suolo.getIdLavorazioneInCorso() == null || !suolo.getIdLavorazioneInCorso().equals(lavorazione)) {
			log.error("Suolo {} gia associato ad altra lavorazione {}", suolo.getId(), suolo.getIdLavorazioneInCorso().getId());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Suolo ".concat(String.valueOf(suolo.getId())).concat(" non associato alla lavorazione"));

		}
	}

	public static class DissociaPoligonoInputData extends AzioneLavorazioneBase.BaseInputData {
		private Long idPoligono;

		public DissociaPoligonoInputData(Long idLavorazione, Integer versione, String utente, Long idPoligono) {
			super(idLavorazione, versione, utente);
			this.idPoligono = idPoligono;
		}
	}
}
