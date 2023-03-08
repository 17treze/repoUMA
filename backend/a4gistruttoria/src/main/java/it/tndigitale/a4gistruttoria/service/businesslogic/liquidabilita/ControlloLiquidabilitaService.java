package it.tndigitale.a4gistruttoria.service.businesslogic.liquidabilita;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.repository.dao.AnomDomandaSostegnoDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.StatoLavSostegnoDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.StatoSostegnoDomandaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.TransizioneIstruttoriaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.CalcoloSostegnoException;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;

@Service
public class ControlloLiquidabilitaService implements ElaborazioneIstruttoria {

	private static final Logger logger = LoggerFactory.getLogger(ControlloLiquidabilitaService.class);

	@Autowired
	private StatoLavSostegnoDao daoStatoLavSostegno;
	@Autowired
	private TransizioneIstruttoriaDao daoTransizioneSostegno;
	@Autowired
	private IstruttoriaDao daoLavorazioneSostegno;
	@Autowired
	AnomDomandaSostegnoDao daoAnomDomandaSostegno;
	
	@Autowired
	private PassoControlloLiquidabilitaService passoLiquidabilitaService;
	@Autowired
	private TransizioneIstruttoriaService transizioneService;
	@Autowired
	protected StatoSostegnoDomandaService statoDomandaService;

	@PersistenceContext
	protected EntityManager entityManager;

	private static final List<String> STATI_ISTRUTTORIA = asList(StatoIstruttoria.CONTROLLI_LIQUIDABILE_KO.getStatoIstruttoria(),
																 StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
	
	/**
	 * Metodo per la rimozione delle informazioni relative ad un calcolo precedente per la domanda in oggetto
	 * 
	 * @param datiElaborazioneIntermedi
	 */
	protected void deleteEsecuzioniPrecedenti(DatiElaborazioneIstruttoria datiElaborazioneIntermedi) {
		IstruttoriaModel istruttoria = datiElaborazioneIntermedi.getIstruttoria();
		Optional<TransizioneIstruttoriaModel> transizionePrecedenteOpt =
                Optional.ofNullable(istruttoria.getTransizioni())
                        .orElseGet(() -> new HashSet<>())
                        .stream() // gia ordinato
				        .filter(t -> STATI_ISTRUTTORIA.contains(t.getA4gdStatoLavSostegno1().getIdentificativo()))
				        .findFirst();
		if (transizionePrecedenteOpt.isPresent()) {
			daoTransizioneSostegno.delete(transizionePrecedenteOpt.get());
		}
		entityManager.flush();
		entityManager.refresh(istruttoria);
	}

	/**
	 * metodo per la gestione del flusso di esecuzione del Controllo di liquidabilità di una singola istruttoria
	 * 
	 * @param idIstruttoria
	 */
	protected void eseguiControlloLiquidabilita(Long idIstruttoria) throws ElaborazioneIstruttoriaException {
		try {
			DatiLiquidabilitaIstruttoria datiIntermediLavorazione = new DatiLiquidabilitaIstruttoria();
			Optional<IstruttoriaModel> istruttoriaOpt = daoLavorazioneSostegno.findById(idIstruttoria);
			
			IstruttoriaModel istruttoria = istruttoriaOpt.orElseThrow(() -> new CalcoloSostegnoException("Nessuna istruttoria trovata con identificativo " + idIstruttoria));
			
			datiIntermediLavorazione.setIstruttoria(istruttoria);
			datiIntermediLavorazione.setVariabiliInputNext(new ArrayList<>());

			logger.debug("Calcolo liquidabilita per istruttoria con ID {}", istruttoria.getId());

			// 0. Rimozione record relativi ad esecuzioni precedenti se presenti.
			deleteEsecuzioniPrecedenti(datiIntermediLavorazione);
			
			// 4. Inizializzazione transizione
			datiIntermediLavorazione.setTransizione(avviaTransizioneCalcolo(datiIntermediLavorazione));
			
			PassoTransizioneModel passoLiquidabilita = passoLiquidabilitaService.eseguiPasso(datiIntermediLavorazione);
			
			CheckLiquidabilita checkLiquidabilita = checkLiquidabilita(passoLiquidabilita);
			
			A4gdStatoLavSostegno statoAggiornato = Optional.of(checkLiquidabilita)
					.map(check -> {
						switch (check) {
		                    case LIQUIDABILE: return getStatoOK();
		                    case DEBITI: return getStatoDebitiDaRecuperare();
		                    default: return getStatoKO();
						}
					})
					.orElseGet(() -> getStatoKO());
			
			TransizioneIstruttoriaModel transizione = datiIntermediLavorazione.getTransizione();
			transizione.setA4gdStatoLavSostegno1(statoAggiornato);
			transizioneService.aggiornaTransizione(transizione);
			
			if (CheckLiquidabilita.LIQUIDABILE.equals(checkLiquidabilita)) {
				DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
				domanda.setIban(datiIntermediLavorazione.getIban());
			}

			istruttoria.setA4gdStatoLavSostegno(statoAggiornato);
			statoDomandaService.aggiornaLavorazioneSostegnoDellaDomanda(istruttoria);
		} catch (Exception e) {
			logger.error("Errore elaborando l'istruttoria {} ", idIstruttoria, e);
			throw new ElaborazioneIstruttoriaException("Errore elaborando l'istruttoria " + idIstruttoria, e);
		}
		
	}
	
	protected boolean isLiquidabilitaOK(PassoTransizioneModel passoLiquidabilita) {
		return Arrays.asList(CodiceEsito.DUF_019.getCodiceEsito(), CodiceEsito.DUF_021.getCodiceEsito())
				.contains(passoLiquidabilita.getCodiceEsito());
	}
	
	
	protected CheckLiquidabilita checkLiquidabilita(PassoTransizioneModel passoLiquidabilita) {
		boolean liquidabile = Arrays.asList(CodiceEsito.DUF_019.getCodiceEsito(), CodiceEsito.DUF_021.getCodiceEsito())
				.contains(passoLiquidabilita.getCodiceEsito());
		
		//ricade sempre in stato debiti
		boolean debiti = Arrays.asList(CodiceEsito.DUF_044.getCodiceEsito(),CodiceEsito.DUF_043.getCodiceEsito(),CodiceEsito.DUF_045.getCodiceEsito())
				.contains(passoLiquidabilita.getCodiceEsito());
		
		return debiti ? CheckLiquidabilita.DEBITI : (liquidabile ? CheckLiquidabilita.LIQUIDABILE : CheckLiquidabilita.NON_LIQUIDABILE);
		
	}
	
	
	/**
	 * Metodo per l'inizializzazione della transizione da associare al calcolo disaccoppiato per la domanda in oggetto
	 * 
	 * @param datiElaborazioneIntermedi
	 * @return
	 */
	private TransizioneIstruttoriaModel avviaTransizioneCalcolo(DatiElaborazioneIstruttoria datiElaborazioneIntermedi) throws Exception {
		return transizioneService.avviaTransizioneCalcolo(datiElaborazioneIntermedi.getIstruttoria());
	}

	private A4gdStatoLavSostegno getStatoOK() {
		return daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
	}

	private A4gdStatoLavSostegno getStatoKO() {
		return daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.CONTROLLI_LIQUIDABILE_KO.getStatoIstruttoria());
	}
	
	private A4gdStatoLavSostegno getStatoDebitiDaRecuperare() {
		return daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.DEBITI.getStatoIstruttoria());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ElaborazioneIstruttoriaException.class)
	public void elabora(Long idIstruttoria) throws ElaborazioneIstruttoriaException {
		eseguiControlloLiquidabilita(idIstruttoria);
	}
	
	static class DatiLiquidabilitaIstruttoria extends DatiElaborazioneIstruttoria {
		private String iban;

		String getIban() {
			return iban;
		}

		void setIban(String iban) {
			this.iban = iban;
		}
		
	}
	
	protected enum CheckLiquidabilita {
		LIQUIDABILE,
		NON_LIQUIDABILE,
		DEBITI
	}
	
	

}
