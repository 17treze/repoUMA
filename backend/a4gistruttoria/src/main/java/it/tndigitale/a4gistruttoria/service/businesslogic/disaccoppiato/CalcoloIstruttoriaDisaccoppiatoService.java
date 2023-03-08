package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.component.dis.InitVariabiliInputCalcoloGiovane;
import it.tndigitale.a4gistruttoria.component.dis.InitVariabiliInputControlliFinaliDisaccoppiatoConsumer;
import it.tndigitale.a4gistruttoria.component.dis.InitVariabiliInputSanzioniBPS;
import it.tndigitale.a4gistruttoria.dto.*;
import it.tndigitale.a4gistruttoria.dto.lavorazione.*;
import it.tndigitale.a4gistruttoria.repository.dao.*;
import it.tndigitale.a4gistruttoria.repository.model.*;
import it.tndigitale.a4gistruttoria.service.DatiIstruttoreService;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.PassoDatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.StatoSostegnoDomandaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.TransizioneIstruttoriaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.CalcoloSostegnoException;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;
import it.tndigitale.a4gistruttoria.service.loader.ConfigurazioneIstruttoriaDisaccoppiatoLoader;
import it.tndigitale.a4gistruttoria.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalcoloIstruttoriaDisaccoppiatoService implements ElaborazioneIstruttoria{

	private static final Logger logger = LoggerFactory.getLogger(CalcoloIstruttoriaDisaccoppiatoService.class);

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private PassoCalcoloAmmissibilitaService serviceAmmissibilita;
	@Autowired
	private PassoCalcoloAnomalieMantenimentoService serviceAnomalieMan;
	
	@Autowired
	protected StatoSostegnoDomandaService statoDomandaService;
	
	@Autowired
	private PassoCalcoloRiduzioniService serviceRiduzioni;
	@Autowired
	private PassoCalcoloSanzioniService serviceSanzioni;
	@Autowired
	private PassoCalcoloGreeningService serviceGreening;
	@Autowired
	private PassoRiepilogoSanzioniService serviceRiepilogoSanzioni;
	@Autowired
	private PassoCalcoloGiovaneService serviceGiovaneAgricoltore;
	@Autowired
	private PassoCalcoliControlliFinaliService serviceControlliFinali;

	@Autowired
	private StatoLavSostegnoDao daoStatoLavSostegno;
	@Autowired
	private TransizioneIstruttoriaDao daoTransizioneSostegno;
	@Autowired
	private IstruttoriaDao daoLavorazioneSostegno;
	@Autowired
	private RichiestaSuperficieDao daoRichiestaSuperficie;
	@Autowired
	private ColturaInterventoDao daoColturaIntervento;
	@Autowired
	private CampioneDao daoCampione;
	@Autowired
	private DatiParticellaColturaDao daoPartColt;

	@Autowired
	private EsitoMantenimentoPascoloDao daoMantenimento;

	@Autowired
	AnomDomandaSostegnoDao daoAnomDomandaSostegno;
	@Autowired
	PascoloParticellaDao pascoloParticellaDao;
	@Autowired
	private TransizioneIstruttoriaService transizioneService;

	@Autowired
	private DomandeService serviceDomande;
	
	@Autowired
	private InitVariabiliInputSanzioniBPS initVariabiliInputSanzioniBPS;
	@Autowired
	private InitVariabiliInputCalcoloGiovane initVariabiliInputCalcoloGiovane;
	@Autowired
	private InitVariabiliInputControlliFinaliDisaccoppiatoConsumer initVariabiliInputControlliFinali;
	@Autowired
	private ConfigurazioneIstruttoriaDisaccoppiatoLoader configurazioneIstruttoriaDisaccoppiatoLoader;
	@Autowired
	private DatiIstruttoreService datiIstruttoreService;

	protected void eseguiCalcoloDisaccoppiatoIstruttoria(Long idIstruttoria) throws CalcoloSostegnoException {
		logger.debug("eseguiCalcoloDisaccoppiatoIstruttoria: inizio per istruttoria {}", idIstruttoria);
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		Optional<IstruttoriaModel> istruttoriaOpt = daoLavorazioneSostegno.findById(idIstruttoria);
		
		IstruttoriaModel istruttoria = istruttoriaOpt.orElseThrow(() -> new CalcoloSostegnoException("Nessuna istruttoria trovata con identificativo " + idIstruttoria));
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		
		datiIntermediLavorazione.setIstruttoria(istruttoria);

		logger.debug("Calcolo Disaccoppiato iniziato per la domanda con ID {}", domanda.getId());

		// 0. Rimozione record relativi ad esecuzioni precedenti se presenti.
		deleteEsecuzioniPrecedenti(datiIntermediLavorazione);

		// 1. Recupero dati di Istruttoria da AGS
		datiIntermediLavorazione.setInfoIstruttoria(getInfoIstruttoriaAGS(datiIntermediLavorazione));

		// 2. Recupero Informazioni SIAN
		datiIntermediLavorazione.setInfoSian(getInfoAgricoltoreSIAN(datiIntermediLavorazione));

		// 3. Caricamento della matrice di compatibilita' per filtro sup. eleggibili e sigeco
		List<A4gdColturaIntervento> matriceCompatibilita = 
				daoColturaIntervento.findColtureByIntervento(
						CodiceInterventoAgs.BPS,
						domanda.getCampagna().intValue()
				);
		
		
		if (matriceCompatibilita == null || matriceCompatibilita.isEmpty()) {
			logger.warn("Matrice di compatibilità non configurata per l'anno {}", domanda.getCampagna());
		}
		
		datiIntermediLavorazione.setColtureCompatibiliSostegno(matriceCompatibilita.stream().map(m -> m.getCodiceColtura3()).collect(Collectors.toList()));

		// 3.1 Gestione parcelle < 200mt
		Set<Long> parcelleNonAmmissibili = new HashSet<Long>();
		List<A4gtRichiestaSuperficie> listaParticelleRichieste = 
				daoRichiestaSuperficie.findByDomandaIntervento(domanda.getId(), CodiceInterventoAgs.BPS);
		// raggruppo le particelle sulla base dei riferimenti cartografici
		Map<String, List<A4gtRichiestaSuperficie>> mapParcellaParticelle = listaParticelleRichieste.stream().collect(Collectors.groupingBy(A4gtRichiestaSuperficie::getRiferimentiCartografici));
		mapParcellaParticelle.forEach((c, l) -> {
			try {
				Long supTotaleParcella = l.stream().collect(Collectors.summingLong(p -> p.getSupRichiesta().longValue()));
				if (supTotaleParcella.compareTo(new Long(200)) < 0) {
					RiferimentiCartografici rifCartografico = mapper.readValue(c, RiferimentiCartografici.class);
					parcelleNonAmmissibili.add(rifCartografico.getIdParcella());
				}
			} catch (IOException e) {
				String errorMessage = "Errore nel recupero delle parcelle con superficie inferiore a 200mt con questi riferimenti: ".concat(c);
				logger.error(errorMessage, e);
				throw new RuntimeException(errorMessage);
			}
		});

		datiIntermediLavorazione.setParcelleNonAmmissibili(parcelleNonAmmissibili);
		
		PassoTransizioneModel esitoPasso = new PassoTransizioneModel();

		try {
			// 4. Inizializzazione transizione
			datiIntermediLavorazione.setTransizione(avviaTransizioneCalcolo(datiIntermediLavorazione));

			// 5. Esecuzione flusso
			// 5.1. Ammissibilita'
			esitoPasso = eseguiPassoLavorazione(serviceAmmissibilita, datiIntermediLavorazione, matriceCompatibilita);

			// 5.2. Anomalie Mantenimento
			esitoPasso = eseguiPassoLavorazione(serviceAnomalieMan, datiIntermediLavorazione, matriceCompatibilita);

			// 5.3. Riduzioni BPS
			esitoPasso = eseguiPassoLavorazione(serviceRiduzioni, datiIntermediLavorazione, matriceCompatibilita);

			// 5.4. Sanzioni BPS
			boolean hasSanzBps = false;
			Optional<EsitoControllo> optBR21 = datiIntermediLavorazione.getDatiSintesi().get(TipologiaPassoTransizione.RIDUZIONI_BPS).getEsitiControlli().stream()
					.filter(e -> e.getTipoControllo().equals(TipoControllo.BRIDUSDC021_idDomandaSanzioni)).findFirst();
			if (optBR21.isPresent()) {
				hasSanzBps = !optBR21.get().getValString().equals("false");
			}
			esitoPasso = eseguiPassoLavorazione(serviceSanzioni, datiIntermediLavorazione, matriceCompatibilita);
			if (esitoPasso.getCodiceEsito().startsWith("DUF")) {
				scriviInfoParticellaColtura(datiIntermediLavorazione);
				return;
			}

			// 5.5. Greening
			esitoPasso = eseguiPassoLavorazione(serviceGreening, datiIntermediLavorazione, matriceCompatibilita);

			// 5.6. Giovane Agricoltore
			esitoPasso = eseguiPassoLavorazione(serviceGiovaneAgricoltore, datiIntermediLavorazione, matriceCompatibilita);

			// 5.7. Riepilogo sanzioni
			boolean hasSanzGreening = false;
			boolean hasSanzGiovane = false;
			// Recupero l'eisto del controllo sulle sanzioni Greening e Giovane Agricoltore.
			// Nel caso in cui non siano presenti gli esiti setto la presenza di sanzioni greening e giovane a False
			Optional<EsitoControllo> optBR55 = datiIntermediLavorazione.getDatiSintesi().get(TipologiaPassoTransizione.GREENING).getEsitiControlli().stream()
					.filter(e -> e.getTipoControllo().equals(TipoControllo.BRIDUSDC055_sanzioneGreening)).findFirst();
			Optional<EsitoControllo> optBR60 = datiIntermediLavorazione.getDatiSintesi().get(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE).getEsitiControlli().stream()
					.filter(e -> e.getTipoControllo().equals(TipoControllo.BRIDUSDC060_sanzioniGiovane)).findFirst();
			if (optBR55.isPresent()) {
				datiIntermediLavorazione.getEsitiInputNext().add(optBR55.get());
				hasSanzGreening = optBR55.get().getEsito();
			} else {
				datiIntermediLavorazione.getEsitiInputNext().add(new EsitoControllo(TipoControllo.BRIDUSDC055_sanzioneGreening, false));
			}
			if (optBR60.isPresent()) {
				datiIntermediLavorazione.getEsitiInputNext().add(optBR60.get());
				hasSanzGiovane = optBR60.get().getEsito();
			} else {
				datiIntermediLavorazione.getEsitiInputNext().add(new EsitoControllo(TipoControllo.BRIDUSDC060_sanzioniGiovane, false));
			}
			// l'esecuzione del passo riepilogo sanzioni viene effettuato se e solo se e' presente almeno una sanzione.
			if (hasSanzBps || hasSanzGreening || hasSanzGiovane) {
				esitoPasso = eseguiPassoLavorazione(serviceRiepilogoSanzioni, datiIntermediLavorazione, matriceCompatibilita);
			}

			// 5.8. Controlli finali
			eseguiPassoLavorazione(serviceControlliFinali, datiIntermediLavorazione, matriceCompatibilita);

			logger.info("Calcolo Disaccoppiato terminato per l'istruttoria con ID {}", datiIntermediLavorazione.getIstruttoria().getId());

		} catch (CalcoloKOException cOse) {
		} catch (CalcoloSostegnoException cse) {
			throw cse;
		} catch (Exception e) {
			String errorMessage = "Errore generico nel calcolo dell'istruttoria  con id " + idIstruttoria;
			logger.error(errorMessage, e);
			throw new CalcoloSostegnoException(errorMessage, e);
		}
	}
	
	/**
	 * Metodo generico per l'esecuzione di un singolo passo di lavorazione
	 * 
	 * @param passoLavorazione
	 * @param datiIntermediLavorazione
	 * @param matriceCompatibilita
	 * @return
	 */
	protected PassoTransizioneModel eseguiPassoLavorazione(PassoDatiElaborazioneIstruttoria passoLavorazione, DatiElaborazioneIstruttoria datiIntermediLavorazione, List<A4gdColturaIntervento> matriceCompatibilita)
			throws CalcoloSostegnoException {
		TipologiaPassoTransizione passo = passoLavorazione.getPasso();
		try {
			DomandaUnicaModel domanda = datiIntermediLavorazione.getIstruttoria().getDomandaUnicaModel();
			logger.debug("Eseguo passo {} per domanda {}", passo, domanda.getId());
			switch (passo) {
			case AMMISSIBILITA:
				datiIntermediLavorazione.setVariabiliInputNext(initInputAmmissibilita(datiIntermediLavorazione));
				break;
			case ANOMALIE_MANTENIMENTO:
				datiIntermediLavorazione.setVariabiliInputNext(initInputMan(datiIntermediLavorazione, matriceCompatibilita));
				break;
			case RIDUZIONI_BPS:
				datiIntermediLavorazione.setVariabiliInputNext(initInputRiduzioniBps(datiIntermediLavorazione));
				datiIntermediLavorazione.getVariabiliInputNext().stream()
						.filter(v -> (v.getTipoVariabile().equals(TipoVariabile.PFSUPIMP) || v.getTipoVariabile().equals(TipoVariabile.PFSUPELE)
								|| v.getTipoVariabile().equals(TipoVariabile.PFSUPSIGECO) || v.getTipoVariabile().equals(TipoVariabile.PFANOMCOORD)
								|| v.getTipoVariabile().equals(TipoVariabile.PFSUPANCOORD)))
						.forEach(v -> datiIntermediLavorazione.elaboraVariabileParticellaColtura(v));
				break;
			case SANZIONI_BPS:
				datiIntermediLavorazione.setVariabiliInputNext(initInputSanzioniBps(datiIntermediLavorazione));
				Optional<EsitoControllo> optBR21 = datiIntermediLavorazione.getDatiSintesi().get(TipologiaPassoTransizione.RIDUZIONI_BPS).getEsitiControlli().stream()
						.filter(e -> e.getTipoControllo().equals(TipoControllo.BRIDUSDC021_idDomandaSanzioni)).findFirst();
				if (optBR21.isPresent()) {
					datiIntermediLavorazione.getEsitiInputNext().add(optBR21.get());
				}
				break;
			case GREENING:
				datiIntermediLavorazione.setVariabiliInputNext(initInputGreening(datiIntermediLavorazione));
				break;
			case GIOVANE_AGRICOLTORE:
				datiIntermediLavorazione.setVariabiliInputNext(initInputGiovaneAgricoltore(datiIntermediLavorazione));
				break;
			case RIEPILOGO_SANZIONI:
				datiIntermediLavorazione.setVariabiliInputNext(initInputRiepilogoSanzioniBps(datiIntermediLavorazione));
				break;
			case CONTROLLI_FINALI:
				datiIntermediLavorazione.setVariabiliInputNext(initInputControlliFinaliBps(datiIntermediLavorazione));
				break;
			default:
				break;
			}
			PassoTransizioneModel esitoLavorazione = passoLavorazione.eseguiPasso(datiIntermediLavorazione);

			if (esitoLavorazione.getCodiceEsito().equalsIgnoreCase("DUE")) {

				String errorMessage = "Errore nell'esecuzione del passo di lavorazione " + passo;
				throw new CalcoloSostegnoException(errorMessage);

			} else if (esitoLavorazione.getCodiceEsito().startsWith("DUF")) {
				scriviInfoParticellaColtura(datiIntermediLavorazione);

				/**
				 * Ho ottenuto un esito finale. Se sono nell'ultimo passo allora stato ok, altrimenti
				 * se sono in passo intermedio vuol dire che è ko
				 */

				A4gdStatoLavSostegno statoAggiornato = getStatoOK();
				if (!TipologiaPassoTransizione.CONTROLLI_FINALI.equals(passo)) {
					statoAggiornato = getStatKO();
				}

				TransizioneIstruttoriaModel transizione = datiIntermediLavorazione.getTransizione();
				transizione.setA4gdStatoLavSostegno1(statoAggiornato);
				transizioneService.aggiornaTransizione(transizione);

				IstruttoriaModel istruttoria = datiIntermediLavorazione.getTransizione().getIstruttoria();
				istruttoria.setA4gdStatoLavSostegno(statoAggiornato);
				statoDomandaService.aggiornaLavorazioneSostegnoDellaDomanda(istruttoria);
				
				if (!TipologiaPassoTransizione.CONTROLLI_FINALI.equals(passo)) {
					String message = "Istruttoria di calcolo KO per l'istruttoria " + datiIntermediLavorazione.getIstruttoria().getId() + "; passo " + passo;
					logger.info(message);
					throw new CalcoloKOException(message);
				}
				

			}
			datiIntermediLavorazione.setDatiFromEntity(passo, esitoLavorazione);
			return esitoLavorazione;
		} catch (CalcoloSostegnoException cse) {
			throw cse;
		} catch (Exception e) {
			logger.error("Errore eseguendo il passo {} per l'istruttoria {}", passo, datiIntermediLavorazione.getIstruttoria().getId(), e);
			throw new CalcoloSostegnoException("Errore eseguendo il passo " + passo + " per l'istruttoria " + datiIntermediLavorazione.getIstruttoria().getId(), e);
		}
	}
	
	private A4gdStatoLavSostegno getStatoOK() {
		return daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria());
	}

	private A4gdStatoLavSostegno getStatKO() {
		return daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria());
	}
	
	/**
	 * Metodo per la scrittura delle informazioni di particella/coltura all'interno del DB
	 * 
	 * @param datiElaborazioneIntermedi
	 */
	private void scriviInfoParticellaColtura(DatiElaborazioneIstruttoria datiElaborazioneIntermedi) {
		IstruttoriaModel istruttoria = datiElaborazioneIntermedi.getIstruttoria();
		if (datiElaborazioneIntermedi != null && datiElaborazioneIntermedi.getInfoParticella() != null) {
			datiElaborazioneIntermedi.getInfoParticella().forEach((k, v) -> {
				A4gtDatiParticellaColtura datiPartColt = new A4gtDatiParticellaColtura();
				try {
					datiPartColt.setIstruttoria(istruttoria);
					datiPartColt.setInfoCatastali(mapper.writeValueAsString(v.getInfoParticella()));
					datiPartColt.setCodiceColtura3(v.getCodColtura());
					datiPartColt.setDatiParticella(mapper.writeValueAsString(v.getVariabiliCalcoloParticella()));
					daoPartColt.save(datiPartColt);
				} catch (JsonProcessingException e) {
					logger.error("Errore nella scrittura sul DB delle informazioni di particella/coltura per l'istruttoria {}", istruttoria.getId(), e);
					throw new RuntimeException("Errore nella scrittura delle informazioni di particella/coltura", e);
				}
			});
		}
	}

	/**
	 * Metodo per la rimozione delle informazioni relative ad un calcolo precedente per la domanda in oggetto
	 * 
	 * @param datiElaborazioneIntermedi
	 */
	private void deleteEsecuzioniPrecedenti(DatiElaborazioneIstruttoria datiElaborazioneIntermedi) {
		IstruttoriaModel istruttoria = datiElaborazioneIntermedi.getIstruttoria();
		Optional<TransizioneIstruttoriaModel> transizionePrecedenteOpt = istruttoria.getTransizioni().stream() // gia ordinato
				.filter(t -> Arrays.asList(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria(),
						StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria())
						.contains(t.getA4gdStatoLavSostegno1().getIdentificativo()))
				.findFirst();		
		if (transizionePrecedenteOpt.isPresent()) {
			daoTransizioneSostegno.delete(transizionePrecedenteOpt.get());
		}

		List<A4gtDatiParticellaColtura> datiPartColtPrecedenti = daoPartColt.findByIstruttoria(istruttoria);
		daoPartColt.deleteAll(datiPartColtPrecedenti);

		List<EsitoMantenimentoPascolo> esitiVerificheMantenimentiPrecedenti = daoMantenimento.findByIstruttoria(istruttoria);
		daoMantenimento.deleteAll(esitiVerificheMantenimentiPrecedenti);
	}

	/**
	 * Metodo per il recupero da AGS delle informazioni di Istruttoria necessarie per il calcolo di premio
	 * 
	 * @param datiElaborazioneIntermedi
	 * @return
	 * @throws CalcoloSostegnoException
	 */
	private InfoIstruttoriaDomanda getInfoIstruttoriaAGS(DatiElaborazioneIstruttoria datiElaborazioneIntermedi) throws CalcoloSostegnoException {
		InfoIstruttoriaDomanda infoIstruttoria = new InfoIstruttoriaDomanda();
		try {
			infoIstruttoria = serviceDomande.recuperaInfoIstruttoriaDomanda(datiElaborazioneIntermedi.getIstruttoria().getDomandaUnicaModel());
		} catch (ConnectException | EntityNotFoundException e) {
			logger.error("Errore nel recupero delle informazioni di istruttoria da AGS", e);
			throw new CalcoloSostegnoException("Errore nel recupero delle informazioni di istruttoria da AGS.", e);
		}
		return infoIstruttoria;
	}

	/**
	 * Metodo per il recupero da A4GProxy delle informazioni sincronizzate con SIAN.
	 * 
	 * @param datiElaborazioneIntermedi
	 * @return
	 */
	private AgricoltoreSIAN getInfoAgricoltoreSIAN(DatiElaborazioneIstruttoria datiElaborazioneIntermedi) {
		AgricoltoreSIAN infoSian = new AgricoltoreSIAN();
		try {
			infoSian = serviceDomande.recuperaInfoAgricoltoreSIAN(datiElaborazioneIntermedi.getIstruttoria().getDomandaUnicaModel().getNumeroDomanda());

		} catch (Exception e) {
			logger.error("Errore nel recupero informazioni da SIAN", e);
		}
		return infoSian;
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

	/**
	 * Metodo per l'inizializzazione delle variabili di input per il passo di ammissibilita
	 * 
	 * @return
	 */
	public List<VariabileCalcolo> initInputAmmissibilita(DatiElaborazioneIstruttoria datiElaborazioneIntermedi) {
		ArrayList<VariabileCalcolo> input = new ArrayList<>();
		DomandaUnicaModel domanda = datiElaborazioneIntermedi.getIstruttoria().getDomandaUnicaModel();
		Integer annoCampagna = getAnnoCampagna(domanda);

		ConfigurazioneIstruttoriaDisaccoppiatoModel configurazioneIstruttorie =
				configurazioneIstruttoriaDisaccoppiatoLoader.loadBy(annoCampagna);

		// 1. Dati di istruttoria
		input.add(new VariabileCalcolo(TipoVariabile.GREPERC, configurazioneIstruttorie.getPercentualeIncrementoGreening()));
		input.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, configurazioneIstruttorie.getPercentualeRiduzioneTitoli()));

		// rimozione particelle in parcelle < 200mt
		// BigDecimal supRichiesta = daoRichiestaSuperficie.sumSuperficieRichiestaNettaCompatibile(domanda.getId(), CodiceInterventoAgs.BPS.getCodiceInterventoAgs());
		BigDecimal supRichiesta = BigDecimal.ZERO;
		BigDecimal supRichiestaNonAmmissibile = BigDecimal.ZERO;
		List<A4gtRichiestaSuperficie> supRichiesteDb = daoRichiestaSuperficie.findByDomandaIntervento(domanda.getId(), CodiceInterventoAgs.BPS);
		List<A4gtRichiestaSuperficie> particelleAmmissibili = supRichiesteDb.stream().filter(p -> {
			try {
				return (datiElaborazioneIntermedi.getColtureCompatibiliSostegno().contains(p.getCodiceColtura3())
						&& !datiElaborazioneIntermedi.getParcelleNonAmmissibili().contains(mapper.readValue(p.getRiferimentiCartografici(), RiferimentiCartografici.class).getIdParcella()));
			} catch (IOException e1) {
				String errorMessage = "Errore nel recupero delle parcelle con superficie inferiore a 200mt per questi riferimenti: ".concat(p.getRiferimentiCartografici());
				logger.error(errorMessage);
				return false;
			}
		}).collect(Collectors.toList());

		List<A4gtRichiestaSuperficie> particelleNonAmmissibili = supRichiesteDb.stream().filter(p -> {
			try {
				return (datiElaborazioneIntermedi.getParcelleNonAmmissibili().contains(mapper.readValue(p.getRiferimentiCartografici(), RiferimentiCartografici.class).getIdParcella()));
			} catch (IOException e1) {
				String errorMessage = "Errore nel recupero delle parcelle con superficie inferiore a 200mt per questi riferimenti: ".concat(p.getRiferimentiCartografici());
				logger.error(errorMessage);
				return false;
			}
		}).collect(Collectors.toList());

		for (A4gtRichiestaSuperficie s : particelleNonAmmissibili) {
			supRichiestaNonAmmissibile = supRichiestaNonAmmissibile.add(s.getSupRichiestaNetta());
		}

		for (A4gtRichiestaSuperficie s : particelleAmmissibili) {
			supRichiesta = supRichiesta.add(s.getSupRichiestaNetta());
		}

		// 3. Dati di superficie
		input.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP200, supRichiestaNonAmmissibile.divide(BigDecimal.valueOf(10000))));
		input.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, supRichiesta.divide(BigDecimal.valueOf(10000))));

		// 4. Flag Greening, Giovane Agricoltore
		input.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));

		boolean giovaneRich = false;
		for (DichiarazioneDomandaUnicaModel ddu : domanda.getDichiarazioni()) {
			if (ddu.getCodice().equalsIgnoreCase("DUGA02")) {
				giovaneRich = true;
			}
		}
		input.add(new VariabileCalcolo(TipoVariabile.GIORIC, giovaneRich));

		if (giovaneRich) {
			input.add(new VariabileCalcolo(TipoVariabile.GIOPERC, configurazioneIstruttorie.getPercentualeIncrementoGiovane()));
			input.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, configurazioneIstruttorie.getLimiteGiovane()));
		}

		// 2. Informazioni SIAN

		// 2.1 Titoli
		List<TitoloAgricoltoreSIAN> listaTitoli = datiElaborazioneIntermedi.getInfoSian().getTitoliSIAN();
		// 2.1.1 Ordino la lista dei titoli in ordine decrescente per valore nominale e superficie
		listaTitoli.sort(Comparator.comparing(TitoloAgricoltoreSIAN::getValoreNominaleTitolo).reversed().thenComparing(Comparator.comparing(TitoloAgricoltoreSIAN::getSuperficieTitolo).reversed()));

		// 2.1.2 Calcolo la superficie totale dei titoli e il valore medio
		BigDecimal superficieTotale = new BigDecimal(0);
		BigDecimal supTitoli = BigDecimal.ZERO;
		BigDecimal valoreTotale = BigDecimal.ZERO;
		BigDecimal valoreTitoliGiovane = BigDecimal.ZERO;
		for (TitoloAgricoltoreSIAN t : listaTitoli) {
			if (supTitoli.compareTo(supRichiesta) < 0) {
				if (supRichiesta.subtract(supTitoli).compareTo(t.getSuperficieTitolo()) > 0) {
					supTitoli = supTitoli.add(t.getSuperficieTitolo());
					valoreTotale = valoreTotale.add(t.getValoreTitolo().setScale(4, RoundingMode.FLOOR));
				} else {
					BigDecimal supResidua = supRichiesta.subtract(supTitoli);
					supTitoli = supTitoli.add(supResidua);
					valoreTotale = valoreTotale.add(t.getValoreTitolo().multiply(supResidua).setScale(4, RoundingMode.FLOOR));
				}
			}
			superficieTotale = superficieTotale.add(t.getSuperficieTitolo().setScale(4, RoundingMode.FLOOR));
			valoreTitoliGiovane = valoreTitoliGiovane.add(t.getValoreTitolo().setScale(4, RoundingMode.FLOOR));
		}

		input.add(new VariabileCalcolo(TipoVariabile.TITONUM, new BigDecimal(listaTitoli.size())));
		input.add(new VariabileCalcolo(TipoVariabile.TITSUP, superficieTotale));

		// se non ci sono titoli , TITVAL = 0
		if (!listaTitoli.isEmpty()) {
			input.add(new VariabileCalcolo(TipoVariabile.TITVAL, valoreTotale.divide(supTitoli, 2, RoundingMode.HALF_UP)));
			if (giovaneRich) {
				input.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, valoreTitoliGiovane.divide(superficieTotale, 2, RoundingMode.HALF_UP)));
			}
		} else {
			input.add(new VariabileCalcolo(TipoVariabile.TITVAL, BigDecimal.ZERO));
			if (giovaneRich) {
				input.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, BigDecimal.ZERO));
			}
		}
		// 2.2 Agricoltore Attivo
		if (datiElaborazioneIntermedi.getInfoSian().getInfoAgricoltoreSIAN() != null && datiElaborazioneIntermedi.getInfoSian().getInfoAgricoltoreSIAN().getFlagAgriAtti() != null) {
			input.add(new VariabileCalcolo(TipoVariabile.AGRATT, datiElaborazioneIntermedi.getInfoSian().getInfoAgricoltoreSIAN().getFlagAgriAtti().equals(new BigDecimal(1))));
		} else {
			input.add(new VariabileCalcolo(TipoVariabile.AGRATT, new BigDecimal(-1)));
		}

		return input;
	}

	/**
	 * Metodo per l'inizializzazione delle variabili di input per il passo di calcolo delle MAN
	 * 
	 * @param datiElaborazioneIntermedi
	 * @return
	 */
	public List<VariabileCalcolo> initInputMan(DatiElaborazioneIstruttoria datiElaborazioneIntermedi, List<A4gdColturaIntervento> matriceCompatibilita) {
		ArrayList<VariabileCalcolo> input = new ArrayList<>();
		DomandaUnicaModel domanda = datiElaborazioneIntermedi.getIstruttoria().getDomandaUnicaModel();
		// 1.2 Superfici Eleggibili
		ArrayList<ParticellaColtura> supEleggibiliGis = new ArrayList<>();
		ArrayList<ParticellaColtura> supEleggibiliSigeco = new ArrayList<>();
		ArrayList<ParticellaColtura> supAnomalieCoord = new ArrayList<>();
		List<InfoEleggibilitaParticella> listaInfoEleggibilita = 
				Optional.ofNullable(datiElaborazioneIntermedi.getInfoIstruttoria().getDatiEleggibilita()).orElse(new ArrayList<InfoEleggibilitaParticella>());
		List<A4gtRichiestaSuperficie> supRichiesteDb = daoRichiestaSuperficie.findByDomandaIntervento(domanda.getId(), CodiceInterventoAgs.BPS);
		// filtro le particelle sulla base della matrice di compatibilita' coltura/destinazione ed escludo le particelle afferenti a parcelle con superficie totale < 200mt
		List<A4gtRichiestaSuperficie> listParticelleRichiesteFiltered = supRichiesteDb.stream().filter(p -> {
			try {
				return (datiElaborazioneIntermedi.getColtureCompatibiliSostegno().contains(p.getCodiceColtura3())
						&& !datiElaborazioneIntermedi.getParcelleNonAmmissibili().contains(mapper.readValue(p.getRiferimentiCartografici(), RiferimentiCartografici.class).getIdParcella()));
			} catch (IOException e1) {
				String errorMessage = "Errore nel recupero delle parcelle con superficie inferiore a 200mt per questi riferimenti: ".concat(p.getRiferimentiCartografici());
				logger.error(errorMessage);
				return false;
			}
		}).collect(Collectors.toList());

		listParticelleRichiesteFiltered.stream().forEach(part -> {
			// 1.2.1 Superfici GIS
			ParticellaColtura pcGis = new ParticellaColtura();
			ParticellaColtura pcSigeco = new ParticellaColtura();
			ParticellaColtura pcAnCoord = new ParticellaColtura();
			// definizione particella
			try {
				Particella particella = mapper.readValue(part.getInfoCatastali(), Particella.class);
				pcGis.setParticella(particella);
				pcSigeco.setParticella(particella);
				pcAnCoord.setParticella(particella);
			} catch (IOException e) {
				logger.error("Errore nel recupero delle anomalie di mantenimento e coordinamento per particella/coltura {}", e);
			}
			// definizione coltura
			pcGis.setColtura(part.getCodiceColtura3());
			pcSigeco.setColtura(part.getCodiceColtura3());
			pcAnCoord.setColtura(part.getCodiceColtura3());
			// Memorizzazione della codiceColturaDiversa nel valString e leguminose nel valBool
			List<A4gdColturaIntervento> colture = matriceCompatibilita.stream().filter(x -> x.getCodiceColtura3().equals(part.getCodiceColtura3())).collect(Collectors.toList());

			if (!colture.isEmpty()) {
				pcGis.setValBool(colture.get(0).getLeguminose().equals("1"));
				pcGis.setValString(colture.get(0).getCodColturaDiversa().stripTrailingZeros().toPlainString());
				pcSigeco.setValBool(colture.get(0).getLeguminose().equals("1"));
				pcSigeco.setValString(colture.get(0).getCodColturaDiversa().stripTrailingZeros().toPlainString());
				pcAnCoord.setValBool(colture.get(0).getLeguminose().equals("1"));
				pcAnCoord.setValString(colture.get(0).getCodColturaDiversa().stripTrailingZeros().toPlainString());
			} else {
				pcGis.setValBool(false);
				pcGis.setValString("0");
				pcSigeco.setValBool(false);
				pcSigeco.setValString("0");
				pcAnCoord.setValBool(false);
				pcAnCoord.setValString("0");
			}

			Optional<InfoEleggibilitaParticella> optPartSupEle = listaInfoEleggibilita.stream()
					.filter(iep -> "BPS".equals(iep.getCodIntervento())) // Betty correzione segnalazione 89
					.filter(p -> (p.getParticella().getIdParticella().equals(pcGis.getParticella().getIdParticella()) && p.getCodColtura3().equals(pcGis.getColtura()))).findAny();

			Float supGis = 0F;
			Float supSigeco = 0F;
			Float supAnCoord = 0F;
			if (optPartSupEle.isPresent()) {
				supGis = optPartSupEle.get().getSuperficieGis().floatValue();
				supSigeco = optPartSupEle.get().getSuperficieSigeco().floatValue();
				if(optPartSupEle.get().getSuperficieAnomaliaCoor() != null) {
					supAnCoord = optPartSupEle.get().getSuperficieAnomaliaCoor().floatValue();
				}
			} else {
				logger.warn("Superficie eleggibile non trovata per la particella {} nel calcolo della domanda {}. Superifice eleggibile e sigeco settata a 0.", pcGis.getParticella().getIdParticella(),
						domanda.getNumeroDomanda());
			}
			// Elaboro solo la prima occorrenza della coppia particella/coltura, in quanto i valori di superficie per richieste differenti con stessa particella/coltura vengono accorpati
			if (supAnomalieCoord.stream().noneMatch(x -> (x.getParticella().getIdParticella().equals(pcGis.getParticella().getIdParticella()) && x.getColtura().equals(pcGis.getColtura())))) {
				if (supAnCoord.compareTo(0F) > 0)
					pcAnCoord.setValNum(supAnCoord / 10000);
				else
					pcAnCoord.setValNum(supAnCoord);
				supAnomalieCoord.add(pcAnCoord);
			}
			// Elaboro solo la prima occorrenza della coppia particella/coltura, in quanto i valori di superficie per richieste differenti con stessa particella/coltura vengono accorpati
			if (supEleggibiliGis.stream().noneMatch(x -> (x.getParticella().getIdParticella().equals(pcGis.getParticella().getIdParticella()) && x.getColtura().equals(pcGis.getColtura())))) {
				if (supGis.compareTo(0F) > 0)
					pcGis.setValNum(supGis / 10000);
				else
					pcGis.setValNum(supGis);
				supEleggibiliGis.add(pcGis);
			}
			// Elaboro solo la prima occorrenza della coppia particella/coltura, in quanto i valori di superficie per richieste differenti con stessa particella/coltura vengono accorpati
			if (supEleggibiliSigeco.stream().noneMatch(x -> (x.getParticella().getIdParticella().equals(pcSigeco.getParticella().getIdParticella()) && x.getColtura().equals(pcSigeco.getColtura())))) {
				if (supSigeco.compareTo(0F) > 0)
					pcSigeco.setValNum(supSigeco / 10000);
				else
					pcSigeco.setValNum(supSigeco);
				supEleggibiliSigeco.add(pcSigeco);
			}
		});
		input.add(new VariabileCalcolo(TipoVariabile.PFSUPANCOORD, supAnomalieCoord));
		input.add(new VariabileCalcolo(TipoVariabile.PFSUPELE, supEleggibiliGis));
		input.add(new VariabileCalcolo(TipoVariabile.PFSUPSIGECO, supEleggibiliSigeco));

		HashMap<String, List<ParticellaColtura>> mapPartElePas = new HashMap<>();
		HashMap<String, List<ParticellaColtura>> mapPartSigecoPas = new HashMap<>();

		// Per ogni pascolo richiesto in domanda (Aziendale o di Malga)
		domanda.getA4gtPascoloParticellas().forEach(pascolo -> {

			List<ParticellaColtura> listPascPartGis = new ArrayList<>();
			List<ParticellaColtura> listPascPartSigeco = new ArrayList<>();

			try {
				// Recupero la lista delle particelle associate al pascolo
				List<ParticellaColtura> listParticellePascolo = mapper.readValue(pascolo.getInfoPartPascolo(), new TypeReference<List<ParticellaColtura>>() {
				});

				// per ogni particella associata al pascolo
				listParticellePascolo.stream().forEach(part -> {

					// la considero una sola volta
					if (listPascPartGis.stream().noneMatch(x -> (x.getParticella().getIdParticella().equals(part.getParticella().getIdParticella()) && x.getColtura().equals(part.getColtura())))) {

						// recupero se presente la sup. Eleggibile. N.B. la presenza (della coppia idParticella/coltura) nella lista delle sup. Eleggibili garantisce l'ammissibilità della particella
						// poichè la lista delle sup. Eleggibili è costruita applicando i criteri di:
						// - Compatibilità con la matrice coltura/intervento
						// - Esclusione parcelle < 200mt
						// - Compatibilità con intervento BPS
						Optional<ParticellaColtura> optPartEle = supEleggibiliGis.stream()
								.filter(p -> (p.getParticella().getIdParticella().equals(part.getParticella().getIdParticella()) && p.getColtura().equals(part.getColtura()))).findAny();
						if (optPartEle.isPresent()) {
							ParticellaColtura partEle = optPartEle.get();
							partEle.setDescMantenimento(part.getDescMantenimento());
							partEle.setDescColtura(part.getDescColtura());
							partEle.setLivello(part.getLivello());
							partEle.setValString(part.getValString());
							listPascPartGis.add(partEle);
						}

					}

					// la considero una sola volta
					if (listPascPartSigeco.stream().noneMatch(x -> (x.getParticella().getIdParticella().equals(part.getParticella().getIdParticella()) && x.getColtura().equals(part.getColtura())))) {

						// recupero se presente la sup. Sigeco. N.B. la presenza (della coppia idParticella/coltura) nella lista delle sup. Sigeco garantisce l'ammissibilità della particella
						// poichè la lista delle sup. Sigeco è costruita applicando i criteri di:
						// - Compatibilità con la matrice coltura/intervento
						// - Esclusione parcelle < 200mt
						// - Compatibilità con intervento BPS
						Optional<ParticellaColtura> optPartSigeco = supEleggibiliSigeco.stream()
								.filter(p -> (p.getParticella().getIdParticella().equals(part.getParticella().getIdParticella()) && p.getColtura().equals(part.getColtura()))).findAny();
						if (optPartSigeco.isPresent()) {
							ParticellaColtura partSigeco = optPartSigeco.get();
							partSigeco.setDescMantenimento(part.getDescMantenimento());
							partSigeco.setDescColtura(part.getDescColtura());
							partSigeco.setLivello(part.getLivello());
							partSigeco.setValString(part.getValString());
							listPascPartSigeco.add(partSigeco);
						}

					}

				});

				mapPartElePas.put(pascolo.getDescPascolo(), listPascPartGis);
				mapPartSigecoPas.put(pascolo.getDescPascolo(), listPascPartSigeco);
			} catch (IOException e) {
				logger.error("Errore nel recupero delle particelle associate al pascolo {}", pascolo.getDescPascolo(), e);
				throw new RuntimeException("Errore nel recupero delle particelle associate al pascolo " + pascolo.getDescPascolo(), e);
			}

		});

		input.add(new VariabileCalcolo(TipoVariabile.PASSUPSIGEMAP, mapPartSigecoPas));
		input.add(new VariabileCalcolo(TipoVariabile.PASSUPELEMAP, mapPartElePas));

		CampioneModel campione = daoCampione.findByCuaaAndAmbitoCampioneAndAnnoCampagna(
				domanda.getCuaaIntestatario(),
				AmbitoCampione.SUPERFICIE,
				domanda.getCampagna());
		input.add(new VariabileCalcolo(TipoVariabile.ISCAMP, campione != null));

		return input;
	}

	/**
	 * Metodo per l'inizializzazione delle variabili di input per il passo di Riduzioni
	 * 
	 * @return
	 * @throws CalcoloSostegnoException
	 */
	public List<VariabileCalcolo> initInputRiduzioniBps(DatiElaborazioneIstruttoria datiElaborazioneIntermedi) throws CalcoloSostegnoException {
		ArrayList<VariabileCalcolo> input = new ArrayList<>();
		IstruttoriaModel istruttoriaModel = datiElaborazioneIntermedi.getTransizione().getIstruttoria();
		DomandaUnicaModel domanda = istruttoriaModel.getDomandaUnicaModel();

		// 1. Superfici per particella/coltura

		// 1.1 Superfici impegnate
		List<A4gtRichiestaSuperficie> supRichiesteDb = daoRichiestaSuperficie.findByDomandaIntervento(domanda.getId(), CodiceInterventoAgs.BPS);
		ArrayList<ParticellaColtura> supRichieste = new ArrayList<>();

		// filtro le particelle sulla base della matrice di compatibilita' coltura/destinazione ed escludo le particelle afferenti a parcelle con superficie totale < 200mt
		List<A4gtRichiestaSuperficie> listParticelleRichiesteFiltered = supRichiesteDb.stream().filter(p -> {
			try {
				return (datiElaborazioneIntermedi.getColtureCompatibiliSostegno().contains(p.getCodiceColtura3())
						&& !datiElaborazioneIntermedi.getParcelleNonAmmissibili().contains(mapper.readValue(p.getRiferimentiCartografici(), RiferimentiCartografici.class).getIdParcella()));
			} catch (IOException e1) {
				String errorMessage = "Errore nel recupero delle parcelle con superficie inferiore a 200mt per questi riferimenti: ".concat(p.getRiferimentiCartografici());
				logger.error(errorMessage);
				return false;
			}
		}).collect(Collectors.toList());

		listParticelleRichiesteFiltered.forEach(part -> {
			ParticellaColtura pc = new ParticellaColtura();
			try {
				pc.setParticella(mapper.readValue(part.getInfoCatastali(), Particella.class));
			} catch (IOException e) {
				String errorMessage = "Errore nel recupero delle informazioni catastali nel calcolo Disaccoppiato per la domanda " + domanda.getNumeroDomanda();
				logger.error(errorMessage, e);
			}

			pc.setColtura(part.getCodiceColtura3());

			// Recupero Livello particella
			InformazioniColtivazione infoColt = new InformazioniColtivazione();
			try {
				infoColt = mapper.readValue(part.getInfoColtivazione(), InformazioniColtivazione.class);
			} catch (IOException e) {
				String errorMessage = "Errore nel recupero delle informazioni di coltivazione nel calcolo Disaccoppiato per la domanda " + domanda.getNumeroDomanda();
				logger.error(errorMessage, e);
			}
			pc.setLivello(infoColt.getCodLivello());

			// Elaboro solo la prima occorrenza della coppia particella/coltura, in quanto i valori di superficie per richieste differenti con stessa particella/coltura vengono accorpati
			if (supRichieste.stream().noneMatch(x -> (x.getParticella().getIdParticella().equals(pc.getParticella().getIdParticella()) && x.getColtura().equals(pc.getColtura())))) {

				Float supRichiestaParts = supRichiesteDb.stream()
						.filter(p -> (p.getCodiceColtura3().equals(part.getCodiceColtura3()) && p.getInfoCatastali().equalsIgnoreCase(part.getInfoCatastali())))
						.collect(Collectors.summingLong(p -> p.getSupRichiestaNetta().longValue())).floatValue();

				pc.setValNum(supRichiestaParts / 10000); // conversione in Ettari
				supRichieste.add(pc);
			}
		});

		input.add(new VariabileCalcolo(TipoVariabile.PFSUPIMP, supRichieste));
		ArrayList<ParticellaColtura> supEleggibiliGis = new ArrayList<>();
		ArrayList<ParticellaColtura> supEleggibiliSigeco = new ArrayList<>();

		// recupero da init input MAN
		VariabileCalcolo pfSupEle = datiElaborazioneIntermedi.getVariabileInput(TipologiaPassoTransizione.ANOMALIE_MANTENIMENTO, TipoVariabile.PFSUPELE);
		if (pfSupEle != null && pfSupEle.getValList() != null) {
			input.add(pfSupEle);
			supEleggibiliGis = pfSupEle.getValList();
		}

		VariabileCalcolo pfSupSigeco = datiElaborazioneIntermedi.getVariabileInput(TipologiaPassoTransizione.ANOMALIE_MANTENIMENTO, TipoVariabile.PFSUPSIGECO);
		if (pfSupSigeco != null && pfSupSigeco.getValList() != null) {
			input.add(pfSupSigeco);
			supEleggibiliSigeco = pfSupSigeco.getValList();
		}

		// 2. Anomalie di mantenimento
		ArrayList<ParticellaColtura> partAnomMan = new ArrayList<>();

		BigDecimal bpsSupScostMan = BigDecimal.ZERO;
		VariabileCalcolo varPfAnomMan = datiElaborazioneIntermedi.getVariabileParticellaColtura(TipologiaPassoTransizione.ANOMALIE_MANTENIMENTO, TipoVariabile.PFANOMMAN);
		if (varPfAnomMan != null && varPfAnomMan.getValList() != null && !varPfAnomMan.getValList().isEmpty()) {
			List<ParticellaColtura> listPfAnomMan = varPfAnomMan.getValList();
			for (ParticellaColtura part : listPfAnomMan) {
				ParticellaColtura pc = new ParticellaColtura();
				pc.setParticella(part.getParticella());
				pc.setColtura(part.getColtura());
				pc.setLivello(part.getLivello());
				// Elaboro solo la prima occorrenza della coppia particella/coltura, in quanto i valori di superficie per richieste differenti con stessa particella/coltura vengono accoprpati
				if (partAnomMan.stream().noneMatch(x -> (x.getParticella().getIdParticella().equals(pc.getParticella().getIdParticella()) && x.getColtura().equals(pc.getColtura())))) {
					// recupero il valore booleano che indica la presenza di anomalie di mantenimento
					pc.setValBool(part.getValBool());
					// calcolo e setto il valore numerico che indica la sup. della particella
					Float supScostMan = varPfAnomMan.getValList().stream()
							.filter(p -> (p.getColtura().equals(part.getColtura()) && p.getParticella().getIdParticella().equals(part.getParticella().getIdParticella()) && p.getValBool()))
							.collect(Collectors.summingDouble(p -> p.getValNum())).floatValue();
						bpsSupScostMan = bpsSupScostMan.add(BigDecimal.valueOf(supScostMan));
					partAnomMan.add(pc);
				}
			}
			// aggiungo alla lista delle particelle PFANOMMAN anche le particelle richieste in domanda ma che non afferiscono a nessun pascolo
			for (A4gtRichiestaSuperficie rich : listParticelleRichiesteFiltered) {
				try {
					Particella infoCatastali = mapper.readValue(rich.getInfoCatastali(), Particella.class);

					if (partAnomMan.stream().noneMatch(p -> p.getParticella().getIdParticella().equals(infoCatastali.getIdParticella()) && p.getColtura().equals(rich.getCodiceColtura3()))) {
						ParticellaColtura pc = new ParticellaColtura();
						pc.setParticella(infoCatastali);
						pc.setColtura(rich.getCodiceColtura3());
						// Recupero Livello particella
						InformazioniColtivazione infoColt = new InformazioniColtivazione();
						try {
							infoColt = mapper.readValue(rich.getInfoColtivazione(), InformazioniColtivazione.class);
						} catch (IOException e) {
							String errorMessage = "Errore nel recupero delle informazioni di coltivazione nel calcolo Disaccoppiato per la domanda " + domanda.getNumeroDomanda();
							logger.error(errorMessage, e);
						}
						pc.setLivello(infoColt.getCodLivello());
						// Elaboro solo la prima occorrenza della coppia particella/coltura, in quanto i valori di superficie per richieste differenti con stessa particella/coltura vengono accoprpati
						if (partAnomMan.stream().noneMatch(x -> (x.getParticella().getIdParticella().equals(pc.getParticella().getIdParticella()) && x.getColtura().equals(pc.getColtura())))) {
							pc.setValBool(false);
							partAnomMan.add(pc);
						}
					}
				} catch (IOException e) {
					String errorMessage = "Errore nel recupero delle informazioni catastali nel calcolo Disaccoppiato per la domanda " + domanda.getNumeroDomanda();
					logger.error(errorMessage, e);
					throw new CalcoloSostegnoException(errorMessage);
				}

			}

		} else {
			// Nel caso in cui non esista la variabile PFANOMMAN (ad esempio per domande senza pascoli) la creo per poterla utilizzare nella definizione della sup. determinata.
			listParticelleRichiesteFiltered.forEach(part -> {
				ParticellaColtura pc = new ParticellaColtura();
				try {
					pc.setParticella(mapper.readValue(part.getInfoCatastali(), Particella.class));
				} catch (IOException e) {
					logger.error("Errore nel recupero delle anomalie di mantenimento e coordinamento per particella/coltura", e);
				}

				pc.setColtura(part.getCodiceColtura3());

				// Recupero Livello particella
				InformazioniColtivazione infoColt = new InformazioniColtivazione();
				try {
					infoColt = mapper.readValue(part.getInfoColtivazione(), InformazioniColtivazione.class);
				} catch (IOException e) {
					String errorMessage = "Errore nel recupero delle informazioni di coltivazione nel calcolo Disaccoppiato per la domanda " + domanda.getNumeroDomanda();
					logger.error(errorMessage, e);
				}
				pc.setLivello(infoColt.getCodLivello());

				// Elaboro solo la prima occorrenza della coppia particella/coltura, in quanto i valori di superficie per richieste differenti con stessa particella/coltura vengono accoprpati
				if (partAnomMan.stream().noneMatch(x -> (x.getParticella().getIdParticella().equals(pc.getParticella().getIdParticella()) && x.getColtura().equals(pc.getColtura())))) {
					pc.setValBool(false);
					partAnomMan.add(pc);
				}
			});
		}

		input.add(new VariabileCalcolo(TipoVariabile.PFANOMMAN, partAnomMan));
		input.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, bpsSupScostMan));

		VariabileCalcolo varCampione = datiElaborazioneIntermedi.getVariabileInput(TipologiaPassoTransizione.ANOMALIE_MANTENIMENTO, TipoVariabile.ISCAMP);
		boolean isCampione = varCampione.getValBoolean();

		// 3. Anomalie di coordinamento
//		recupero da init input MAN
		ArrayList<ParticellaColtura> supAnomalieCoord = new ArrayList<>();
		VariabileCalcolo pfSupAnCoord = datiElaborazioneIntermedi.getVariabileInput(TipologiaPassoTransizione.ANOMALIE_MANTENIMENTO, TipoVariabile.PFSUPANCOORD);
		if (pfSupAnCoord != null && pfSupAnCoord.getValList() != null) {
			input.add(pfSupAnCoord);
			supAnomalieCoord = pfSupAnCoord.getValList();
		}
		BigDecimal bpsSupScostCoord = BigDecimal.ZERO;
		ArrayList<ParticellaColtura> partAnomCoord = new ArrayList<>();

		for (A4gtRichiestaSuperficie part : listParticelleRichiesteFiltered) {
			ParticellaColtura pc = new ParticellaColtura();
			try {
				pc.setParticella(mapper.readValue(part.getInfoCatastali(), Particella.class));
			} catch (IOException e) {
				logger.error("Errore nel recupero delle anomalie di mantenimento e coordinamento per particella/coltura {}", e);
			}

			pc.setColtura(part.getCodiceColtura3());

			// Recupero Livello particella
			InformazioniColtivazione infoColt = new InformazioniColtivazione();
			try {
				infoColt = mapper.readValue(part.getInfoColtivazione(), InformazioniColtivazione.class);
			} catch (IOException e) {
				String errorMessage = "Errore nel recupero delle informazioni di coltivazione nel calcolo Disaccoppiato per la domanda " + domanda.getNumeroDomanda();
				logger.error(errorMessage, e);
			}
			pc.setLivello(infoColt.getCodLivello());

			// Recupero id Parcella a cui afferisce la particella
			RiferimentiCartografici rifCartografici = new RiferimentiCartografici();
			try {
				rifCartografici = mapper.readValue(part.getRiferimentiCartografici(), RiferimentiCartografici.class);
			} catch (IOException e) {
				String errorMessage = "Errore nel recupero delle informazioni di coltivazione nel calcolo Disaccoppiato per la domanda " + domanda.getNumeroDomanda();
				logger.error(errorMessage, e);
			}
			
//			Elaboro solo la prima occorrenza della coppia particella/coltura, in quanto i valori di superficie per richieste differenti con stessa particella/coltura vengono accorpati
			if (partAnomCoord.stream().noneMatch(x -> (x.getParticella().getIdParticella().equals(pc.getParticella().getIdParticella()) && x.getColtura().equals(pc.getColtura())))) {

				Optional<ParticellaColtura> pcSupAnCoord = supAnomalieCoord.stream().filter(
						p -> p.getParticella().getIdParticella().equals(pc.getParticella().getIdParticella()) && p.getColtura().equals(pc.getColtura()))
						.findAny();
				
				Float supAnCoord = Float.valueOf(0F);
				if (pcSupAnCoord.isPresent()) {
					supAnCoord = pcSupAnCoord.get().getValNum();
				} else {
					logger.warn("Superficie anomalie non trovata per la particella {} nella valutazione delle anomalie di coordinamento per la domanda {}", pc.getParticella().getIdParticella(),
							domanda.getNumeroDomanda());
				}
				
				boolean PFANOMCOORD = supAnCoord > 0 ? true : false;
				if (PFANOMCOORD) {
					bpsSupScostCoord = bpsSupScostCoord.add(BigDecimal.valueOf(supAnCoord));
				}
				pc.setValBool(PFANOMCOORD);
				partAnomCoord.add(pc);
			}
		}
		
		input.add(new VariabileCalcolo(TipoVariabile.PFANOMCOORD, partAnomCoord));
		input.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, bpsSupScostCoord.setScale(4, RoundingMode.HALF_UP)));

		// 4. Altre
		input.add(datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.AMMISSIBILITA, TipoVariabile.TITVALRID));
		input.add(datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.AMMISSIBILITA, TipoVariabile.BPSSUPRIC));

		Float supGisTotale = supEleggibiliGis.stream().collect(Collectors.summingDouble(p -> p.getValNum())).floatValue();
		input.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, BigDecimal.valueOf(supGisTotale)));

		Float supSigecoTotale = supEleggibiliSigeco.stream().collect(Collectors.summingDouble(p -> p.getValNum())).floatValue();
		input.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, BigDecimal.valueOf(supSigecoTotale)));

		// 5. Superificie determinata Istruttore
		DatiIstruttoria datiIstruttore = datiIstruttoreService.getDatiIstruttoreDisaccoppiato(istruttoriaModel.getId());
		if (datiIstruttore != null) {
			try {
				if (datiIstruttore != null && datiIstruttore.getbPSSuperficie() != null) {
					input.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, datiIstruttore.getbPSSuperficie()));
				}
			} catch (Exception e) {
				String errorMessage = "Errore nel recupero della superficie determinata inserita dall'istruttore per la domanda " + domanda.getNumeroDomanda();
				logger.error(errorMessage, e);
				throw new CalcoloSostegnoException(errorMessage);
			}
		}
		// 6. Domanda a campione
		input.add(varCampione);

		// 7. Domanda chiusa in SIGECO
		boolean esitoSigeco = serviceDomande.recuperaEsitoSigeco(datiElaborazioneIntermedi.getIstruttoria().getDomandaUnicaModel().getNumeroDomanda());
		input.add(new VariabileCalcolo(TipoVariabile.DOMSIGECOCHIUSA, esitoSigeco));

		return input;

	}

	/**
	 * Metodo per l'inizializzazione delle variabili di input per il passo di Greening
	 * 
	 * @return
	 * @throws CalcoloSostegnoException
	 */
	public List<VariabileCalcolo> initInputGreening(DatiElaborazioneIstruttoria datiElaborazioneIntermedi) throws CalcoloSostegnoException {
		ArrayList<VariabileCalcolo> input = new ArrayList<>();
		IstruttoriaModel istruttoriaModel = datiElaborazioneIntermedi.getTransizione().getIstruttoria();
		input.add(datiElaborazioneIntermedi.getVariabileInput(TipologiaPassoTransizione.AMMISSIBILITA, TipoVariabile.GREPERC));
		input.add(datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.AMMISSIBILITA, TipoVariabile.TITVALRID));
		input.add(datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSSUPDET));
		input.add(datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSSUPAMM));

		boolean bio = false;
		for (DichiarazioneDomandaUnicaModel ddu : istruttoriaModel.getDomandaUnicaModel().getDichiarazioni()) {
			if (ddu.getCodice().equalsIgnoreCase("DUDICH_20")) {
				bio = ddu.getValoreBool().equals(new BigDecimal(1));
			}
		}
		input.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, bio));
		// Greening � Sup non ammissibile mancato Rispetto Inverdimento Istruttore numero quattro decimali ha Superficie inserita da applicativo
		DatiIstruttoria datiIstruttore = datiIstruttoreService.getDatiIstruttoreDisaccoppiato(istruttoriaModel.getId());
		if (datiIstruttore != null ) {
			try {
				if (datiIstruttore != null && datiIstruttore.getGreeningSuperficie() != null) {
					input.add(new VariabileCalcolo(TipoVariabile.GRESUPRIDIST, datiIstruttore.getGreeningSuperficie()));
				}
			} catch (Exception e) {
				String errorMessage = String.format(
						"Errore nel recupero della sup. ammissibile mancato rispetto Inverdimento inserita dall'istruttore %s",
						istruttoriaModel.getDomandaUnicaModel().getNumeroDomanda());
				logger.error(errorMessage, e);
				throw new CalcoloSostegnoException(errorMessage);
			}
		}
		// particella/coltura
		input.add(datiElaborazioneIntermedi.getVariabileParticellaColtura(TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.PFSUPDET));

		// GREPFAZOTO
		input.add(new VariabileCalcolo(TipoVariabile.GREFPAZOTO, BigDecimal.ONE));
		input.add(datiElaborazioneIntermedi.getVariabileInput(TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.PFSUPIMP));

		return input;
	}

	/**
	 * Metodo per l'inizializzazione delle variabili di input per il passo di calcolo Sanzioni BPS
	 * 
	 * @param datiElaborazioneIntermedi
	 * @return
	 * @throws CalcoloSostegnoException
	 */
	private List<VariabileCalcolo> initInputSanzioniBps(DatiElaborazioneIstruttoria datiElaborazioneIntermedi) throws CalcoloSostegnoException {
		return initVariabiliInputSanzioniBPS.initInputSanzioniBps(datiElaborazioneIntermedi);
	}

	/**
	 * Metodo per l'inizializzazione delle variabili di input per il passo di calcolo Giovane Agricoltore BPS
	 * 
	 * @param datiElaborazioneIntermedi
	 * @return
	 * @throws CalcoloSostegnoException
	 */
	private List<VariabileCalcolo> initInputGiovaneAgricoltore(DatiElaborazioneIstruttoria datiElaborazioneIntermedi) throws CalcoloSostegnoException {
		return initVariabiliInputCalcoloGiovane.initInputGiovaneAgricoltore(datiElaborazioneIntermedi);
	}

	/**
	 * Metodo per l'inizializzazione delle variabili di input per il passo di Riepilogo Sanzioni BPS
	 * 
	 * @param datiElaborazioneIntermedi
	 * @return
	 */
	private List<VariabileCalcolo> initInputRiepilogoSanzioniBps(DatiElaborazioneIstruttoria datiElaborazioneIntermedi) {
		ArrayList<VariabileCalcolo> input = new ArrayList<>();

		input.add(datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.RIDUZIONI_BPS, TipoVariabile.BPSIMPAMM));
		VariabileCalcolo importoGreening = datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.GREENING, TipoVariabile.GREIMPAMM);
		if (importoGreening != null) {
			input.add(importoGreening);
		}
		VariabileCalcolo importoGiovane = datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPAMM);
		if (importoGiovane != null) {
			input.add(importoGiovane);
		}
		VariabileCalcolo importoSanzione = datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.SANZIONI_BPS, TipoVariabile.BPSIMPSANZ);
		if (importoSanzione != null) {
			input.add(importoSanzione);
		}
		VariabileCalcolo importoSanzioneRecidiva = datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.SANZIONI_BPS, TipoVariabile.BPSIMPSANZREC);
		if (importoSanzioneRecidiva != null) {
			input.add(importoSanzioneRecidiva);
		}
		VariabileCalcolo giovaneImportoSanzione = datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPSANZ);
		if (giovaneImportoSanzione != null) {
			input.add(giovaneImportoSanzione);
		}
		VariabileCalcolo giovaneImportoSanzioneRecidiva = datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.GIOVANE_AGRICOLTORE, TipoVariabile.GIOIMPSANZREC);
		if (giovaneImportoSanzioneRecidiva != null) {
			input.add(giovaneImportoSanzioneRecidiva);
		}
		VariabileCalcolo greeningImportoSanzione = datiElaborazioneIntermedi.getVariabileOutput(TipologiaPassoTransizione.GREENING, TipoVariabile.GREIMPSANZ);
		if (greeningImportoSanzione != null) {
			input.add(greeningImportoSanzione);
		}

		return input;
	}

	/**
	 * Metodo per l'inizializzazione delle variabili di input per il passo dei Controlli Finali BPS
	 * 
	 * @param datiElaborazioneIntermedi
	 * @return
	 * @throws CalcoloSostegnoException
	 */
	private List<VariabileCalcolo> initInputControlliFinaliBps(DatiElaborazioneIstruttoria datiElaborazioneIntermedi) throws CalcoloSostegnoException {
		return this.initVariabiliInputControlliFinali.initInputControlliFinali(datiElaborazioneIntermedi);
	}
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ElaborazioneIstruttoriaException.class)
	public void elabora(Long idIstruttoria) throws ElaborazioneIstruttoriaException{
		eseguiCalcoloDisaccoppiatoIstruttoria(idIstruttoria);
	}

	private class CalcoloKOException extends CalcoloSostegnoException {

		public CalcoloKOException(String errorMessage) {
			super(errorMessage);
		}

		public CalcoloKOException(String errorMessage, Throwable cause) {
			super(errorMessage, cause);
		}

		private static final long serialVersionUID = 1393856067019375018L;
		
	}

	private Integer getAnnoCampagna(DomandaUnicaModel domanda) {
		return Optional.ofNullable(domanda.getCampagna())
				.orElseThrow(() ->
						new EntityNotFoundException("Impossibile ricavare l'anno della campagna a partire dalla domanda con id " +
								domanda.getId()));
	}
}
