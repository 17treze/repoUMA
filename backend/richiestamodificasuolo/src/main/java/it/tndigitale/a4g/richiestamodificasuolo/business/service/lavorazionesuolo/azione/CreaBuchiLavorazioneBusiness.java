package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.*;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione.ValidLavorazioneInCorsoBusiness.TipoValidazione;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione.CreaBuchiLavorazioneBusiness.CreaBuchiInputData;
import it.tndigitale.a4g.richiestamodificasuolo.dto.lavorazionesuolo.*;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("CREA_BUCHI_LAVORAZIONE")
public class CreaBuchiLavorazioneBusiness extends AzioneLavorazioneBase<CreaBuchiLavorazioneInCorsoDto, CreaBuchiInputData> {

	private static final Logger log = LoggerFactory.getLogger(CreaBuchiLavorazioneBusiness.class);

	@Autowired
	private UtenteComponent utenteComponent;

	@Value("${it.tndigit.serverFme.verificaIntersezioneLavorazioneUpas.creazioneBuchiLavorazione}")
	private String creazioneBuchiLavorazione;

	@Autowired
	private UtilsFme utilsFme;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	protected CreaBuchiLavorazioneInCorsoDto eseguiAzione(CreaBuchiInputData input) {
		return creaBuchiLavorazione(input.idLavorazione, input.tipoValidazione);
	}

	public CreaBuchiLavorazioneInCorsoDto creaBuchiLavorazione(Long idLavorazione, TipoValidazione tipoValidazione) {
		log.debug("START - creaBuchiLavorazione {}", idLavorazione);

		CreaBuchiLavorazioneInCorsoDto res = new CreaBuchiLavorazioneInCorsoDto();

		LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrInvalidArgument(idLavorazione, utenteComponent.username());

		if (tipoValidazione.equals(TipoValidazione.TUTTI)) {
			try {
				creazioneBuchiLavorazione(lavorazione);
				res.setEsito("OK");
			} catch (Exception e) {
				log.error("Errore validazione trasformata FME creaBuchiLavorazione", e);
				throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore validazione trasformata FME creaBuchiLavorazione ");
			}
		}

		log.debug("END - creaBuchiLavorazione {}", idLavorazione);
		return res;
	}

	/**
	 * @param lavorazione
	 * @return
	 */
	private HttpStatus creazioneBuchiLavorazione(LavorazioneSuoloModel lavorazione) {

		try {
			ResponseEntity<String> responseCreaBuchiFme = utilsFme.callProcedureFme(lavorazione.getId(), creazioneBuchiLavorazione);
			final ObjectNode responseCreaBuchiFmeNode = objectMapper.readValue(responseCreaBuchiFme.getBody(), ObjectNode.class);

			if (responseCreaBuchiFme.getStatusCodeValue() != 200 && responseCreaBuchiFmeNode.has("statusMessage")
					&& responseCreaBuchiFmeNode.get("status").toString().equalsIgnoreCase("SUCCESS") ) {
				log.error("Errore trasformata FME in creazioneBuchiLavorazione");
				throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore validazione trasformata FME");
			}
			return HttpStatus.OK;
		} catch (Exception e){
			log.error("Errore trasformata FME in creazioneBuchiLavorazione");
			throw SuoloException.ExceptionType.INVALID_ARGUMENT_EXCEPTION.newSuoloExceptionInstance("Errore validazione trasformata FME: " + e);
		}
	}

	public static class CreaBuchiInputData {
		private final Long idLavorazione;
		private final TipoValidazione tipoValidazione;

		public CreaBuchiInputData(Long idLavorazione, TipoValidazione tipoValidazione) {
			super();
			this.idLavorazione = idLavorazione;
			this.tipoValidazione = tipoValidazione;
		}
	}

}
