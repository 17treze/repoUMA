package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione.RimuoviDichiaratoLavorazioneBusiness.RimuoviDichiaratoInputData;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("RIMUOVI_DICHIARATO")
public class RimuoviDichiaratoLavorazioneBusiness extends AzioneLavorazioneBase<LavorazioneSuoloModel, RimuoviDichiaratoInputData> {

	private static final Logger log = LoggerFactory.getLogger(RimuoviDichiaratoLavorazioneBusiness.class);

	@Autowired
	private UtenteComponent utenteComponent;

	@Override
	protected LavorazioneSuoloModel eseguiAzione(RimuoviDichiaratoInputData input) {
		return removeSuoloDichiaratoDaLavorazione(input.idLavorazione, input.idSuoloDichiarato, input.utente);
	}

	public LavorazioneSuoloModel removeSuoloDichiaratoDaLavorazione(Long idLavorazione, Long idSuoloDichiarato, String utente) {
		log.debug("START - removeSuoloDichiaratoDaLavorazione {}, {}", idLavorazione, idSuoloDichiarato);
		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoNotFoundOrUnauthorized(idLavorazione, utenteComponent.username());
		checkValidState(lavorazione);
		SuoloDichiaratoModel suolo = getDichiaratoOrNotFound(idSuoloDichiarato);

		checkCoerenzaOrThrowInvalidArgument(lavorazione, suolo);

		lavorazione.setDataUltimaModifica(getClock().now());
		lavorazione.removeSuoloDichiaratoModel(suolo);
		getLavorazioneSuoloDao().save(lavorazione);

		log.debug("END - removeSuoloDichiaratoDaLavorazione ");
		return lavorazione;
	}

	protected void checkValidState(LavorazioneSuoloModel lavorazione) {
		if (!StatoLavorazioneSuolo.IN_CREAZIONE.equals(lavorazione.getStato()) && !StatoLavorazioneSuolo.IN_MODIFICA.equals(lavorazione.getStato())) {
			log.error("LavorazioneSuoloModel {} non in stato corretto per passaggio di stato a IN_CORSO {}", lavorazione.getId(), lavorazione.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Lo stato ".concat(lavorazione.getStato().name())
					.concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente l'associazione di suolo vigente "))));

		}
	}

	protected void checkCoerenzaOrThrowInvalidArgument(LavorazioneSuoloModel lavorazione, SuoloDichiaratoModel suolo) {
		if (suolo.getLavorazioneSuolo() == null || !suolo.getLavorazioneSuolo().equals(lavorazione)) {
			log.error("Suolo dichiarato {} non risulta associato alla lavorazione corrente", suolo.getId());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
					.newSuoloExceptionInstance("Suolo dichiarato ".concat(String.valueOf(suolo.getId())).concat(" non risulta associato alla lavorazione corrente "));

		}
	}

	public static class RimuoviDichiaratoInputData {
		private Long idLavorazione;
		private Long idSuoloDichiarato;
		private String utente;

		public RimuoviDichiaratoInputData(Long idLavorazione, Long idSuoloDichiarato, String utente) {
			super();
			this.idLavorazione = idLavorazione;
			this.idSuoloDichiarato = idSuoloDichiarato;
			this.utente = utente;
		}

	}
}
