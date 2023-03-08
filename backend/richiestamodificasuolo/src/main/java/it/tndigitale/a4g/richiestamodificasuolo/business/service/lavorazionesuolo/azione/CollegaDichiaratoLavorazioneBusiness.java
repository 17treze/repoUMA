package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.RichiestaModificaSuoloUtils;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.evento.AvvioLavorazioneEvento;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.SuoloDichiaratoLavorazioneDto;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

@Component("COLLEGA_DICHIARATO")
public class CollegaDichiaratoLavorazioneBusiness extends AzioneLavorazioneBase<LavorazioneSuoloModel, SuoloDichiaratoLavorazioneDto> {

	private static final Logger log = LoggerFactory.getLogger(CollegaDichiaratoLavorazioneBusiness.class);

	@Autowired
	private UtenteComponent utenteComponent;

	@Override
	protected LavorazioneSuoloModel eseguiAzione(SuoloDichiaratoLavorazioneDto suoloDichiaratoLavorazioneDto) {
		return updateSuoloDichiarato(suoloDichiaratoLavorazioneDto);
	}

	public LavorazioneSuoloModel updateSuoloDichiarato(SuoloDichiaratoLavorazioneDto suoloDichiaratoLavorazioneDto) {
		log.debug("START - updateSuoloDichiaratoLavorazione ");

		String utente = utenteComponent.username();
		validateData(suoloDichiaratoLavorazioneDto);
		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoNotFoundOrUnauthorized(suoloDichiaratoLavorazioneDto.getIdLavorazione(), utente);

		checkValidState(lavorazione);

		SuoloDichiaratoModel suolo = getDichiaratoOrNotFound(suoloDichiaratoLavorazioneDto.getId());
		suoloAssociabile(suolo);
		checkValidSuolo(suolo);

		lavorazione.setDataUltimaModifica(getClock().now());
		lavorazione.addSuoloDichiaratoModel(suolo);
		getLavorazioneSuoloDao().save(lavorazione);

		// Notifica evento aggiornamento richiesta modifica suolo associata al suolo dichiarato
		getEventoPublisher().notificaEvento(new AvvioLavorazioneEvento(lavorazione));
		log.debug("END - updateSuoloDichiaratoLavorazione ");
		return lavorazione;
	}

	protected void validateData(SuoloDichiaratoLavorazioneDto suoloDichiaratoLavorazioneDto) {
		if (suoloDichiaratoLavorazioneDto.getIdLavorazione() == null) {
			log.error("suoloDichiaratoLavorazioneDto.getIdLavorazione() == null");
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("suoloDichiaratoLavorazioneDto.getIdLavorazione() == null");
		}
		if (suoloDichiaratoLavorazioneDto.getId() == null) {
			log.error("suoloDichiaratoLavorazioneDto.getId() null");
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("suoloDichiaratoLavorazioneDto.getId() == null");
		}
	}

	protected void checkValidState(LavorazioneSuoloModel lavorazione) {
		if (!StatoLavorazioneSuolo.IN_CREAZIONE.equals(lavorazione.getStato()) && !StatoLavorazioneSuolo.IN_MODIFICA.equals(lavorazione.getStato())) {
			log.error("LavorazioneSuoloModel {} non in stato corretto per passaggio di stato a IN_CORSO {}", lavorazione.getId(), lavorazione.getStato());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Lo stato ".concat(lavorazione.getStato().name())
					.concat(" della lavorazione ".concat(String.valueOf(lavorazione.getId()).concat(" non consente l'associazione di suolo vigente "))));

		}

	}

	protected void checkValidSuolo(SuoloDichiaratoModel suolo) {
		if (suolo.getLavorazioneSuolo() != null) {
			log.error("SuoloDichiaratoModel {} gia associata ad altra lavorazione {}", suolo.getId(), suolo.getLavorazioneSuolo().getId());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
					.newSuoloExceptionInstance("Suolo dichiarato ".concat(String.valueOf(suolo.getId())).concat(" gia associato ad altra lavorazione "));
		}

	}

	protected void suoloAssociabile(SuoloDichiaratoModel suolo) {
		if (!RichiestaModificaSuoloUtils.listaStatiSuoloAssociabile.contains(suolo.getRichiestaModificaSuolo().getStato())) {
			log.error("SuoloDichiaratoModel {} associato a richiesta non lavorabile", suolo.getId(), suolo.getRichiestaModificaSuolo().getId());
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION
					.newSuoloExceptionInstance("Suolo dichiarato ".concat(String.valueOf(suolo.getId())).concat(" associato a richiesta non lavorabile"));

		}
	}
}
