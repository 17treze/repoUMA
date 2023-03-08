package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.EsitoLavorazioneDichiarato;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.RichiestaModificaSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione.AggiornaEsitoDichiaratoBusiness.AggiornaEsitoDichiaratoInputData;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("AGGIORNA_ESITO_DICHIARATO")
public class AggiornaEsitoDichiaratoBusiness extends AzioneLavorazioneBase<LavorazioneSuoloModel, AggiornaEsitoDichiaratoInputData> {

	private static final Logger log = LoggerFactory.getLogger(AggiornaEsitoDichiaratoBusiness.class);

	@Autowired
	private UtenteComponent utenteComponent;

	@Override
	protected LavorazioneSuoloModel eseguiAzione(AggiornaEsitoDichiaratoInputData input) {
		return aggiornaEsitoDichiarato(input.idSuoloDichiarato, input.esito);
	}

	public LavorazioneSuoloModel aggiornaEsitoDichiarato(Long idSuoloDichiarato, EsitoLavorazioneDichiarato esito) {
		log.debug("START - aggiornaEsitoDichiarato {}, {}", idSuoloDichiarato, esito);

		SuoloDichiaratoModel suolo = getDichiaratoOrNotFound(idSuoloDichiarato);

		if (suolo.getLavorazioneSuolo() == null) {
			log.error("Suolo senza lavorazione associata");
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
					.newSuoloExceptionInstance("Il poligono di dichiarato ".concat(String.valueOf(idSuoloDichiarato).concat(" non è associato a alcuna lavorazione")));
		}

		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrInvalidArgument(suolo.getLavorazioneSuolo().getId(), utenteComponent.username());
		checkValidStateLavorazione(lavorazione);

		checkValidStateRichiesta(suolo.getRichiestaModificaSuolo());

		suolo.setEsito(esito);
		getSuoloDichiaratoDao().save(suolo);

		log.debug("END - aggiornaEsitoDichiarato ");
		return lavorazione;
	}

	protected void checkValidStateLavorazione(LavorazioneSuoloModel lavorazione) {
		if (!StatoLavorazioneSuolo.IN_CORSO.equals(lavorazione.getStato())) {
			log.error("LavorazioneSuoloModel {} non in stato corretto per l'operazione previsto IN_CORSO ottenuto {}", lavorazione.getId(), lavorazione.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance(
					"Lo stato ".concat(lavorazione.getStato().name()).concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente l'aggiornamento dell'esito "))));
		}
	}

	protected void checkValidStateRichiesta(RichiestaModificaSuoloModel richiesta) {
		if (!StatoRichiestaModificaSuolo.IN_LAVORAZIONE.equals(richiesta.getStato())) {
			log.error("RichiestaModificaSuolo {} non in stato corretto per l'operazione previsto IN_LAVORAZIONE ottenuto {}", richiesta.getId(), richiesta.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Lo stato ".concat(richiesta.getStato().name())
					.concat(" della richietsa ".concat(String.valueOf(richiesta.getId()).concat(" associata al poligono non consente l'aggiornamento dell'esito "))));

		}
	}

	public static class AggiornaEsitoDichiaratoInputData {
		private Long idSuoloDichiarato;
		private EsitoLavorazioneDichiarato esito;

		public AggiornaEsitoDichiaratoInputData(Long idSuoloDichiarato, EsitoLavorazioneDichiarato esito) {
			super();
			this.idSuoloDichiarato = idSuoloDichiarato;
			this.esito = esito;
		}

	}

}
