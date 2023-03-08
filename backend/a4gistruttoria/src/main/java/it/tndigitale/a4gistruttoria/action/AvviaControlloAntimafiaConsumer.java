/**
 * 
 */
package it.tndigitale.a4gistruttoria.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.tndigitale.a4gistruttoria.dto.CustomThreadLocal;
import it.tndigitale.a4gistruttoria.dto.DatiElaborazioneProcesso;
import it.tndigitale.a4gistruttoria.dto.IstruttoriaAntimafiaChainHandler;
import it.tndigitale.a4gistruttoria.dto.IstruttoriaAntimafiaChainHandlerBuilder;
import it.tndigitale.a4gistruttoria.dto.IstruttoriaAntimafiaEsito;
import it.tndigitale.a4gistruttoria.processo.events.ProcessoEvent;
import it.tndigitale.a4gistruttoria.processo.events.ProcessoPublisher;
import it.tndigitale.a4gistruttoria.repository.dao.ProcessoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtProcesso;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi;
import it.tndigitale.a4gistruttoria.util.EmailUtils;
import it.tndigitale.a4gistruttoria.util.IstruttoriaAntimafiaStatoEnum;
import it.tndigitale.a4gistruttoria.util.StatoDichiarazioneEnum;

/**
 * @author S.DeLuca
 *
 */
@Component
public class AvviaControlloAntimafiaConsumer implements Consumer<Long> {

	private static Logger log = LoggerFactory.getLogger(AvviaControlloAntimafiaConsumer.class);

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ParixConsumer parixConsumer;
	@Autowired
	private SiapConsumer siapConsumer;
	@Autowired
	private SoggettiConsumer soggettiConsumer;
	@Autowired
	private AziendeConsumer aziendeConsumer;
	@Autowired
	private StateConsumer stateConsumer;
	@Autowired
	private ConsumeExternalRestApi callRestService;
	@Autowired
	private ProcessoDao processoDao;
	@Autowired
	private EmailUtils emailService;
	@Autowired
	private ProcessoPublisher processoControlloAntimafiaPublisher;

	@Value("${a4gistruttoria.antimafia.mail.to}")
	private String mailTo;
	@Value("${a4gistruttoria.antimafia.mail.oggetto}")
	private String oggetto;
	@Value("${a4gistruttoria.antimafia.mail.messaggio}")
	private String messaggio;
	
	private static String idProgetto = "idProcesso";
	private static String identificativo = "identificativo";
	
	@Override
	@Retryable(value = { Exception.class, RuntimeException.class }, maxAttempts = 3, backoff = @Backoff(5000))
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void accept(Long idDichiarazioneAntimafia) {
		log.info("Inizio esecuzione algoritmo per dichiarazione con ID: {}", idDichiarazioneAntimafia);
		A4gtProcesso a4gtProcesso = processoDao.getOne((Long) CustomThreadLocal.getVariable(idProgetto));
		if (a4gtProcesso == null || a4gtProcesso.getDatiElaborazione() == null) {
			String errorMessage = String.format("Processo istruttoria antimafia con ID '%s' non trovato", String.valueOf(idDichiarazioneAntimafia));
			log.error(errorMessage);
			throw new RuntimeException(errorMessage);
		}

		DatiElaborazioneProcesso datiProcesso;
		try {
			datiProcesso = objectMapper.readValue(a4gtProcesso.getDatiElaborazione(), DatiElaborazioneProcesso.class);
		} catch (IOException e) {
			String errorMessage = String.format("Errore parsing 'dati Processo': %s", a4gtProcesso.getDatiElaborazione());
			log.error(errorMessage, e);
			throw new RuntimeException(errorMessage, e);
		}
		try {
			// recupero domanda antimafia
			ResponseEntity<String> responseGetFascicolo = callRestService.getDichiarazioneAntimafia(String.valueOf(idDichiarazioneAntimafia));
			if (!responseGetFascicolo.getStatusCode().is2xxSuccessful()) {
				String errorMessage = String.format(
						"Fallita la GET della domanda antimafia con id %s del processo con ID %s",
						idDichiarazioneAntimafia, CustomThreadLocal.getVariable(idProgetto).toString());
				throw new RuntimeException(errorMessage);
			}
			String responseDomandaAntimafia = responseGetFascicolo.getBody();
			JsonNode jsonDomandaAntimafia = objectMapper.readTree(responseDomandaAntimafia);
			ObjectNode identificativoStato = (ObjectNode) jsonDomandaAntimafia.path("stato");
			if (StatoDichiarazioneEnum.PROTOCOLLATA.getIdentificativoStato().equals(identificativoStato.path(identificativo).textValue())) {
				// builder per l'antimafia chain handler con i dati della dichiarazione e l'esito (vuoto)
				IstruttoriaAntimafiaChainHandler istruttoriaAntimafiaChain = new IstruttoriaAntimafiaChainHandlerBuilder().with(istruttoriaAntimafiaChainBuilder -> {
					istruttoriaAntimafiaChainBuilder.datiDichiarazione = jsonDomandaAntimafia.path("datiDichiarazione");
					istruttoriaAntimafiaChainBuilder.istruttoriaAntimafiaEsito = new IstruttoriaAntimafiaEsito(idDichiarazioneAntimafia);
				}).build();
				// chain di consumer per la gestione dell'esito dell'istruttoria antimafia
				parixConsumer.andThen(siapConsumer).andThen(soggettiConsumer).andThen(aziendeConsumer).andThen(stateConsumer).accept(istruttoriaAntimafiaChain);
				datiProcesso.getGestite().add(objectMapper.writeValueAsString(istruttoriaAntimafiaChain.getIstruttoriaAntimafiaEsito()));

				if (IstruttoriaAntimafiaStatoEnum.C_01.getStato().equals(istruttoriaAntimafiaChain.getIstruttoriaAntimafiaEsito().getStato())) {
					identificativoStato.put(identificativo, StatoDichiarazioneEnum.CONTROLLATA.getIdentificativoStato());
				} else {
					identificativoStato.put(identificativo, StatoDichiarazioneEnum.CONTROLLO_MANUALE.getIdentificativoStato());
				}
				// aggiornamento stato dichiarazione antimafia
				ResponseEntity<String> responseAggiornamento = callRestService.putDichiarazioneAntimafia(String.valueOf(idDichiarazioneAntimafia), jsonDomandaAntimafia);
				if (!responseAggiornamento.getStatusCode().is2xxSuccessful()) {
					String errorMessage = String.format("Fallito l'aggiornamento dello STATO della domanda con id %s del processo con ID %s",
							String.valueOf(idDichiarazioneAntimafia),
							CustomThreadLocal.getVariable(idProgetto).toString());
					throw new RuntimeException(errorMessage);
				}
			} else {
				log.info("QuadroDichiarazione con id {} skipped perchè non in stato PROTOCOLLATO ma in stato {}",
						idDichiarazioneAntimafia, identificativoStato.path(identificativo).textValue());
			}
			aggiornaProcesso(idDichiarazioneAntimafia, a4gtProcesso, datiProcesso);
		} catch (Exception e) {
			String errorMessage = String.format("Errore generico domanda antimafia con id %s del processo con ID %s",
					String.valueOf(idDichiarazioneAntimafia), CustomThreadLocal.getVariable(idProgetto).toString());
			log.error(errorMessage, e);
			throw new RuntimeException(errorMessage, e);
		}
	}

	private void aggiornaProcesso(Long idDichiarazioneAntimafia, A4gtProcesso a4gtProcesso, DatiElaborazioneProcesso datiProcesso) {
		try {
			Short totaleDomandeGestite = new Short(datiProcesso.getTotale());
			datiProcesso.setTotale((++totaleDomandeGestite).toString());
			Double percentuale = (Double.valueOf(datiProcesso.getTotale()) * 100) / Double.valueOf(datiProcesso.getDaElaborare());
			a4gtProcesso.setPercentualeAvanzamento(BigDecimal.valueOf(percentuale));
			a4gtProcesso.setDatiElaborazione(objectMapper.writeValueAsString(datiProcesso));
			A4gtProcesso processoSaved = processoDao.save(a4gtProcesso);
			log.info("Salvataggio processo con id {} avvenuto con successo.", processoSaved.getId());
		} catch (Exception ex) {
			String errorMessage = String.format("Fallito l'aggiornamento dell'esito della domanda con id %s del processo con ID %s",
					idDichiarazioneAntimafia.toString(), CustomThreadLocal.getVariable(idProgetto).toString());
			log.error(errorMessage, ex);
			throw new RuntimeException(errorMessage, ex);
		}
	}

	@Recover
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public void recoverAndSendMail(Exception ex, Long idDichiarazioneAntimafia) {
		log.info("Inizio recover algoritmo per dichiarazione con ID: {}", idDichiarazioneAntimafia);
		// invio mail
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		ex.printStackTrace(pw);
		emailService.sendSimpleMessage(mailTo, MessageFormat.format(oggetto, idDichiarazioneAntimafia), MessageFormat.format(messaggio, sw.getBuffer().toString()));

		List<Long> antimafiaIds = new ArrayList<>();
		antimafiaIds.add(idDichiarazioneAntimafia);
		ProcessoEvent processoEvent = new ProcessoEvent();
		processoEvent.setId((Long) CustomThreadLocal.getVariable(idProgetto));
		DatiElaborazioneProcesso datiElaborazioneProcesso = new DatiElaborazioneProcesso();
		List<String> domandeConProblemi = new ArrayList<>();
		domandeConProblemi.add(String.valueOf(idDichiarazioneAntimafia));
		datiElaborazioneProcesso.setConProblemi(domandeConProblemi);
		processoEvent.setDatiElaborazioneProcesso(datiElaborazioneProcesso);
		processoEvent.setDtFine(new Date());

		processoControlloAntimafiaPublisher.handleEvent(processoEvent);
	}
}
