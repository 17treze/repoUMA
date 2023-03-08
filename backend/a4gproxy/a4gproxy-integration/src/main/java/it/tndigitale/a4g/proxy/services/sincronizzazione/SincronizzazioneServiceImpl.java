/**
 * 
 */
package it.tndigitale.a4g.proxy.services.sincronizzazione;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import it.tndigitale.a4g.proxy.action.InserimentoDatiDittaIndividualeConsumer;
import it.tndigitale.a4g.proxy.action.InserimentoDatiPersonaGiuridicaConsumer;
import it.tndigitale.a4g.proxy.dto.DatiPagamentiDto;
import it.tndigitale.a4g.proxy.dto.Dichiarazione;
import it.tndigitale.a4g.proxy.dto.DichiarazioneAntimafiaBuilder;
import it.tndigitale.a4g.proxy.dto.SuperficiAccertateDto;
import it.tndigitale.a4g.proxy.services.EmailService;
import it.tndigitale.a4g.proxy.services.SincronizzazioneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.AppoSupeAccertDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.DichiarazioneAntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.PagamentiDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabaantiTab;

/**
 * @author S.DeLuca
 *
 */
@Service
public class SincronizzazioneServiceImpl implements SincronizzazioneService {

	private static final Logger log = LoggerFactory.getLogger(SincronizzazioneServiceImpl.class);
	private static final String DITTA_INDIVIDUALE = "DI";
	
	@Autowired
	private DichiarazioneAntimafiaDao dichiarazioneAntimafiaDao;
	@Autowired
	private DichiarazioneAntimafiaBuilder dichiarazioneAntimafiaBuilder;
	
	@Autowired
	private InserimentoDatiPersonaGiuridicaConsumer inserimentoDatiPersonaGiuridica;
	@Autowired
	private InserimentoDatiDittaIndividualeConsumer inserimentoDatiDittaIndividuale;
	
	@Autowired
	private AppoSupeAccertDao appoSupeAccertDao;
	@Autowired
	private PagamentiDao pagamentiDao;
	@Autowired
	private SuperficiAccertateConverter superficiAccertateConverter;

	@Autowired
	private DatiPagamentiConverter datiPagamentiConverter;
	
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private EmailService emailService;
	@Value("${a4g.mail.to}")
	private String mailTo;
	@Value("${a4g.mail.oggetto}")
	private String oggetto;
	@Value("${a4g.mail.messaggio}")
	private String messaggio;	
	
	@Async
	@Override
	@Transactional
	public Future<String> dichiarazioneAntimafia(String dichiarazioneAntimafiaString) throws Exception {
		log.debug("Dichiarazione da sincronizzare: ".concat(dichiarazioneAntimafiaString));
		JsonNode jsonDatiDichiarazione = objectMapper.readTree(dichiarazioneAntimafiaString);
		Long idDichiarazione = jsonDatiDichiarazione.path("id").asLong();
		try {
			dichiarazioneAntimafiaDao
				.findById(idDichiarazione)
				.ifPresent( a -> {
					log.error("Dichiarazione già esistente : ".concat(idDichiarazione.toString()));
					throw new EntityExistsException("Dichiarazione già esistente : ".concat(idDichiarazione.toString()));
				});
			AabaantiTab dichiarazioneAntimafia = dichiarazioneAntimafiaBuilder.build(jsonDatiDichiarazione);
			String formaGiuridicaCodice=jsonDatiDichiarazione.path("datiDichiarazione").path("dettaglioImpresa").path("formaGiuridicaCodice").textValue();
			dichiarazioneAntimafia=dichiarazioneAntimafiaDao.saveAndFlush(dichiarazioneAntimafia);
			BiConsumer<JsonNode,AabaantiTab> biConsumer = DITTA_INDIVIDUALE.equalsIgnoreCase(formaGiuridicaCodice) ? 
												inserimentoDatiDittaIndividuale : inserimentoDatiPersonaGiuridica;
			biConsumer.accept(jsonDatiDichiarazione, dichiarazioneAntimafia);
		} catch (Exception ex) {
			log.error("Errore Inserimento dati",ex);
			//invio mail
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw, true);
			ex.printStackTrace(pw);
			emailService.sendSimpleMessage(	mailTo, 
					MessageFormat.format(oggetto, idDichiarazione),		
					MessageFormat.format(messaggio, sw.getBuffer().toString()));
			return  new AsyncResult<String>("KO");
		}
		log.debug("Inserimento dati correttamente finito");
		return  new AsyncResult<String>("OK");
	}

	@Override
	@Transactional(readOnly = true)
	public Dichiarazione getDichiarazione(Long id) {
		log.debug("Richiesta di ricerca dichiarazione per ID : {}", id);
		Optional<AabaantiTab> aabaantiTab = dichiarazioneAntimafiaDao.findById(id);
		if (!aabaantiTab.isPresent()) return null;
		Dichiarazione dichiarazioneOut=new Dichiarazione();
		BeanUtils.copyProperties(aabaantiTab.get(), dichiarazioneOut);
		return dichiarazioneOut;
	}

	@Override
	@Transactional
	public Dichiarazione aggiornaDichiarazione(Dichiarazione dichiarazione) throws Exception {
		try {
			log.debug("Richiesta di ricerca dichiarazione per ID : {}", dichiarazione.getIdAnti());
			AabaantiTab aabaantiTab = dichiarazioneAntimafiaDao.findById(dichiarazione.getIdAnti()).
					orElseThrow(() -> new EntityNotFoundException(String.format("Nessuna dichiarazione trovata per id: %d", dichiarazione.getIdAnti())));
			BeanUtils.copyProperties(dichiarazione, aabaantiTab);
			AabaantiTab aabaantiTabUpdated = dichiarazioneAntimafiaDao.save(aabaantiTab);
			BeanUtils.copyProperties(aabaantiTabUpdated, dichiarazione);
			return dichiarazione;
		} catch (Exception e) {
			log.error("Errore aggiornamento sincronizzazione Antimafia");
			// invio mail 
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw, true);
			e.printStackTrace(pw);
			
			emailService.sendSimpleMessage(	mailTo, 
					MessageFormat.format(oggetto, dichiarazione.getIdAnti()),		
					MessageFormat.format(messaggio, sw.getBuffer().toString()));
			throw new Exception("Errore aggiornamento sincronizzazione Antimafia " , e);
		}
	}

	@Override
	@Transactional
	public void creaSuperficiAccertate(SuperficiAccertateDto superficiAccertate) throws Exception {
		if (!appoSupeAccertDao.verificaPreliminareInserimentoSupAccertate(superficiAccertate.getCuaa(), superficiAccertate.getAnnoCampagna()))
			appoSupeAccertDao.save(superficiAccertateConverter.convert(superficiAccertate));
	}
	
	@Override
	@Transactional
	public void pulisciSuperficiAccertate(Long annoCampagna) {
		appoSupeAccertDao.deleteByNumeCamp(annoCampagna);
	}

	@Override
	@Transactional
	public void creaDatiPagamenti(DatiPagamentiDto datiPagamenti) {
		if (!pagamentiDao.verificaPreliminareInserimentoPagamento(datiPagamenti.getCuaa(), datiPagamenti.getAnnoCampagna()))
			pagamentiDao.save(datiPagamentiConverter.convert(datiPagamenti));
	}

	@Override
	@Transactional
	public void pulisciDatiPagamenti(Long annoCampagna) {
		pagamentiDao.deleteByNumeCamp(annoCampagna);
	}
}