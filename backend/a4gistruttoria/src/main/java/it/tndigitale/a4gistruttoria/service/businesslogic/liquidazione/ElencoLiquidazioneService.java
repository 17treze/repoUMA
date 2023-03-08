 package it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gistruttoria.A4gistruttoriaConfigurazione;
import it.tndigitale.a4gistruttoria.repository.dao.ElencoLiquidazioneDao;
import it.tndigitale.a4gistruttoria.repository.model.ElencoLiquidazioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElencoLiquidazioneException;
import it.tndigitale.a4gistruttoria.service.businesslogic.verbale.VerbaleLiquidazioneService;
import it.tndigitale.a4gistruttoria.service.businesslogic.verbale.VerbaleLiquidazioneServiceFactory;
import it.tndigitale.a4gistruttoria.util.EmailUtils;
import it.tndigitale.a4gistruttoria.util.StatoTrasmissioneElencoLiquidazione;

@Service
public class ElencoLiquidazioneService {
	
	private static final String CODICE_ORGANISMO_PAGATORE = "801";
	
	private static final Logger logger = LoggerFactory.getLogger(ElencoLiquidazioneService.class);

	@Autowired
	private LiquidazioneIstruttoriaService liquidazioneIstruttoria;
	@Autowired
	private VerbaleLiquidazioneServiceFactory verbaleServiceFactory;
	@Autowired
	private InvioTracciatoSOCComponent invioTracciatoComponent;
	@Autowired
	private EmailUtils emailService;
	@Autowired
	private A4gistruttoriaConfigurazione configurazione;
	@Autowired
	private ElencoLiquidazioneComponent elencoComp;
	
	@Autowired
	private Clock clock;
	

	/**
	 * Senza la transazione ho problemi con hibernate e LazyInizialitation
	 * 
	 * @param annoCampagna
	 * @param sostegno
	 * @param istruttorie
	 * @return
	 * @throws ElencoLiquidazioneException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ElencoLiquidazioneException.class, timeout = 1200 )
	public Long generaElenco(Integer annoCampagna, Sostegno sostegno, List<Long> istruttorie) throws ElencoLiquidazioneException {
		logger.debug("generaElenco: per anno {} e sostegno {}", annoCampagna, sostegno);
		ElencoLiquidazioneModel elenco = inizializzaElenco();
		Long idElenco = elenco.getId();
		logger.debug("generaElenco: creato elenco {} per anno {} e sostegno {}", idElenco, annoCampagna, sostegno);
		
		StringBuilder tracciatoSB = new StringBuilder();
		int counter = 0;
		for (Long idIstruttoria : istruttorie) {
			String rigaTracciato = liquidazioneIstruttoria.generaRigaTracciato(idIstruttoria, annoCampagna, sostegno, elenco, counter);
			if (rigaTracciato != null) {
				tracciatoSB.append(rigaTracciato);
				tracciatoSB.append(System.getProperty("line.separator"));
			}
		}
		
		aggiornaElencoConTracciato(elenco.getId(), tracciatoSB.toString());
		
		logger.debug("generaElenco: creato elenco {} con tracciato per anno {} e sostegno {}", idElenco, annoCampagna, sostegno);
		return idElenco;
	}

	
	protected ElencoLiquidazioneModel inizializzaElenco() {
		return elencoComp.creaElenco(getCodElenco(), clock.now());
	}
	
	protected void aggiornaElencoConTracciato(Long id, String tracciato) {
		elencoComp.aggiornaElencoConTracciato(id, tracciato);
	}
	
	/**
	 * Genera e persiste il verbale di liquidazione 
	 * 
	 * Tolgo la transazione unica perché andrebbe in timeout. Ogni operazione
	 * di persistenza in scrittura avra' il suo commit quindi ma accettiamo
	 * il compromesso.
	 * 
	 * @param annoCampagna
	 * @param sostegno
	 * @param idElenco
	 * @throws ElencoLiquidazioneException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ElencoLiquidazioneException.class, timeout = 1200 )
	public void generaVerbale(Integer annoCampagna, Sostegno sostegno, Long idElenco) throws ElencoLiquidazioneException {
		try {
			logger.debug("generaVerbale: inizio per elenco {} per anno {} e sostegno {}", idElenco, annoCampagna, sostegno);
			String nomeVerbaleService = 
					VerbaleLiquidazioneService.PREFISSO_QUALIFIER +
					sostegno.name();
			
			VerbaleLiquidazioneService verbaleService = verbaleServiceFactory.getVerbaleLiquidazioneService(nomeVerbaleService);
			byte[] stampa = verbaleService.stampa(idElenco);

			elencoComp.aggiornaVerbale(idElenco, stampa);
		} catch (ElencoLiquidazioneException e) {
			throw e;
		} catch (Exception e) {
			logger.error("generaVerbale: errore generando il verbale per elenco {} e anno campagna {}", idElenco, annoCampagna, e);
			throw new ElencoLiquidazioneException("Errore generico generando il verbale dell'elenco " + idElenco, e);
		}		
	}
	
	/**
	 * Invio elenco prodotto al SOC
	 * 
	 * Tolgo la transazione unica perché andrebbe in timeout. Ogni operazione
	 * di persistenza in scrittura avra' il suo commit quindi ma accettiamo
	 * il compromesso.
	 * 
	 * @param annoCampagna
	 * @param sostegno
	 * @param idElenco
	 * @throws ElencoLiquidazioneException
	 */
	public void inviaElenco(Integer annoCampagna, Sostegno sostegno, Long idElenco) throws ElencoLiquidazioneException {
		ElencoLiquidazioneModel elenco = elencoComp.get(idElenco);
		try {
			logger.debug("inviaElenco: inizio per elenco {} per anno {} e sostegno {}", idElenco, annoCampagna, sostegno);
			String nomeFile = getFileName(idElenco, annoCampagna, recuperaTipoIstruttoria(idElenco));
			invioTracciatoComponent.inviaTracciato(elencoComp.getTracciato(idElenco), nomeFile);
			elencoComp.aggiornaStatoElencoInviato(idElenco);
			
		} catch (Exception e) {
			logger.error("inviaElenco: errore inviando l'elenco {} e anno campagna {}", idElenco, annoCampagna, e);
			throw new ElencoLiquidazioneException("Errore generico inviando l'elenco " + idElenco, e);
		}		
		try {
			notificaMailElenco(sostegno, elenco);
		} catch (Exception e) {
			logger.warn("inviaElenco: errore notificanfo l'elenco {} e anno campagna {}", idElenco, annoCampagna, e);
		}
	}

	private TipoPagamento recuperaTipoIstruttoria(Long idElenco) throws ElencoLiquidazioneException {
		//derivo il TipoIstruttoria dalla lista istruttorie relativo l'elenco liquidazione
		TipoIstruttoria tipoIstruttoria = liquidazioneIstruttoria.getTipoIstruttoriaPerElenco(idElenco);
		if (tipoIstruttoria == null) {
			logger.error("Errore recuperando il tipo istruttoria per elenco {}", idElenco);
			throw new ElencoLiquidazioneException("Errore nel recupero della tipologia istruttoria per elenco "+ idElenco);
		}
		return TipoPagamento.getTipoPagamentoByTipoIstruttoria(tipoIstruttoria);
	}
	
	protected void notificaMailElenco(Sostegno sostegno, ElencoLiquidazioneModel elenco) {
		String soggetto = sostegno + " - elenco di liquidazione - " + elenco.getCodElenco();

		// Invia email
		emailService.sendMessageWithAttachment(configurazione.getDestinatarioMailLiquidazione(), soggetto,
				elenco.getCodElenco() + ".pdf", elenco.getVerbaleLiquidazione());
		
	}
	
	protected String getCodElenco() {
		String separatore = "-";
		DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		
		StringBuilder sbCodElenco = new StringBuilder();
		sbCodElenco
		.append(CampoElencoLiquidazione.SETTORE.getCostante())
		.append(separatore)
		.append(CODICE_ORGANISMO_PAGATORE)
		.append(separatore)
		.append(dtFormatter.format(clock.today()));
		return sbCodElenco.toString();
	}
	
	protected String getFileName(Long idElencoLiquidazione, Integer annoCampagna, TipoPagamento tipoPagamento) {
		StringBuilder nomeFileSoc = new StringBuilder();
		nomeFileSoc.append(CampoElencoLiquidazione.SETTORE.getCostante());
		nomeFileSoc.append("-").append(CODICE_ORGANISMO_PAGATORE);
		nomeFileSoc.append("-").append(annoCampagna);
		nomeFileSoc.append(".").append(idElencoLiquidazione);
		nomeFileSoc.append(".").append(idElencoLiquidazione);
		nomeFileSoc.append(".").append(tipoPagamento.getCodice());
		nomeFileSoc.append(".txt");

		return nomeFileSoc.toString();
	}

	public byte[] getVerbale(Long idElencoLiquidazione) {
		return elencoComp.getVerbale(idElencoLiquidazione);
	}
	
	public String getTracciato(Long idElencoLiquidazione) {
		return elencoComp.getTracciato(idElencoLiquidazione);
	}
	
	@Component
	static class ElencoLiquidazioneComponent {
		@Autowired
		private ElencoLiquidazioneDao daoElencoLiquidazione;
		
		protected byte[] getVerbale(Long idElencoLiquidazione) {
			return daoElencoLiquidazione.findById(idElencoLiquidazione).map(elenco -> elenco.getVerbaleLiquidazione()).orElse(null);
		}
		
		protected String getTracciato(Long idElencoLiquidazione) {
			return daoElencoLiquidazione.findById(idElencoLiquidazione).map(elenco -> elenco.getTracciatoSoc()).orElse(null);
		}
		
		protected ElencoLiquidazioneModel creaElenco(String codElenco, LocalDateTime dtCreazione) {
			
			ElencoLiquidazioneModel elencoLiquidazione = new ElencoLiquidazioneModel();
			elencoLiquidazione.setCodElenco(codElenco);
			elencoLiquidazione.setDtCreazione(dtCreazione);
			elencoLiquidazione = daoElencoLiquidazione.save(elencoLiquidazione);
			return elencoLiquidazione;
		}
		
		protected void aggiornaElencoConTracciato(Long id, String tracciato) {
			ElencoLiquidazioneModel elenco = get(id);
			elenco.setTracciatoSoc(tracciato);
			elenco.setStato(StatoTrasmissioneElencoLiquidazione.ELENCO_PRODOTTO);
			elenco.setCodElenco(elenco.getCodElenco().concat("-").concat(elenco.getId().toString()));
			elenco = daoElencoLiquidazione.save(elenco);			
		}
		
		protected ElencoLiquidazioneModel get(Long id) {
			return daoElencoLiquidazione.findById(id).orElseThrow(() -> new EntityNotFoundException("Elenco " + id + " non trovato"));
		}
		
		protected void aggiornaStatoElencoInviato(Long id) {
			ElencoLiquidazioneModel elenco = get(id);
			elenco.setStato(StatoTrasmissioneElencoLiquidazione.ELENCO_INVIATO_SOC);
			
			daoElencoLiquidazione.save(elenco);
			
		}
		protected void aggiornaVerbale(Long id, byte[] verbale) {
			ElencoLiquidazioneModel elenco = daoElencoLiquidazione.findById(id).orElseThrow(() -> new EntityNotFoundException("Elenco " + id + " non trovato"));
			elenco.setVerbaleLiquidazione(verbale);
			
			daoElencoLiquidazione.save(elenco);			
		}
	}
}
