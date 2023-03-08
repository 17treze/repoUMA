package it.tndigitale.a4g.proxy.bdn.service.manager;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.infotn.bdn.wsbdnagea.wsclient.ConsistenzaAlPascoloPAC2015Service;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiCONSISTENZAPASCOLO2015;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiCONSISTENZAPASCOLO2015.CONSISTENZAPASCOLO2015;
import it.tndigitale.a4g.istruttoria.client.model.CuaaDenominazione;
import it.tndigitale.a4g.proxy.bdn.client.A4gIstruttoriaClient;
import it.tndigitale.a4g.proxy.bdn.client.AnagraficaCapoClient;
import it.tndigitale.a4g.proxy.bdn.client.StruttureClient;
import it.tndigitale.a4g.proxy.bdn.dto.ConsistenzaAlPascoloPAC2015DO;
import it.tndigitale.a4g.proxy.bdn.dto.ConsistenzaAlPascoloPAC2015DOBuilder;
import it.tndigitale.a4g.proxy.bdn.dto.ErroreDO;
import it.tndigitale.a4g.proxy.bdn.dto.FasciaEtaConsistenzaPascolo;
import it.tndigitale.a4g.proxy.bdn.repository.interfaces.ConsistenzaAlPascoloPAC2015DAO;
import it.tndigitale.a4g.proxy.bdn.repository.interfaces.StatoSincronizzazioneBdnDAO;
import it.tndigitale.a4g.proxy.ws.bdn.dsequicapg.DsEQUICAPIG;
import it.tndigitale.a4g.proxy.ws.bdn.dsequicapg.DsEQUICAPIG.CAPO;
import it.tndigitale.a4g.proxy.ws.bdn.dsequiregistripascolig.DsEQUIREGISTRIPASCOLIG;
import it.tndigitale.a4g.proxy.ws.bdn.dsoviregistripascolig.DsOVIREGISTRIPASCOLIG;
import it.tndigitale.a4g.proxy.ws.bdn.dsregistripascolig.DsREGISTRIPASCOLIG;
import it.tndigitale.a4g.proxy.ws.bdn.dsregistripascolig.DsREGISTRIPASCOLIG.REGISTRIPASCOLI;

@Service
public class ConsistenzaAlPascoloPAC2015ManagerImpl implements ConsistenzaAlPascoloPAC2015Manager {

	@Autowired
	protected ConsistenzaAlPascoloPAC2015DAO consPascoloDao;
	@Autowired
	protected StatoSincronizzazioneBdnDAO statoSincronizzazioneDao;
	@Autowired
	private StruttureClient struttureClient;
	@Autowired
	private A4gIstruttoriaClient a4gIstruttoriaClient;
	@Autowired
	private AnagraficaCapoClient anagraficaCapoClient;
	// Logger
	private Logger log = LoggerFactory.getLogger(this.getClass());

	ConsistenzaAlPascoloPAC2015Service consistenzaService = new ConsistenzaAlPascoloPAC2015Service();

	private static final Set<String> CODICI_SPECIE_BOVINI = Set.of("0121");
	private static final Set<String> CODICI_SPECIE_OVINI = Set.of("0124", "0125");
	private static final Set<String> CODICI_SPECIE_EQUIDI = Set.of("0126", "0147", "0148", "0149");

	@Override
	@Transactional(propagation = Propagation.REQUIRED, timeout = 600)
	public List<ErroreDO> inserimentoConsistenzaAlPascoloPAC2015PerCUAA(String cuaa, Integer annoCampagna) {
		log.debug("inserimentoConsistenzaAlPascoloPAC2015PerCUAA");

		List<ErroreDO> listaErrori = new ArrayList<>();
		try {
			List<String> elencoPascoli = a4gIstruttoriaClient.getElencoPascoli(annoCampagna, cuaa);
			if (!CollectionUtils.isEmpty(elencoPascoli)) {
				Set<String> elencoPascoliSet = new HashSet<>(elencoPascoli);
				for (String pascolo : elencoPascoliSet) {
					ErroreDO errore = inserimentoConsistenzaAlPascoloPAC2015(pascolo, annoCampagna);
					if (errore != null) {
						listaErrori.add(errore);
					}
				}
				return listaErrori;
			}
		} catch (Exception e1) {
			log.error("Errore nell'estrazione dell'elenco CodiciPascolo. Operazione non eseguita per cuaa {}", cuaa, e1);
			ErroreDO errore = new ErroreDO("", ErroreDO.consistenzaAlPascoloPAC2015, new GregorianCalendar(), e1);
			listaErrori.add(errore);
			return listaErrori;
		}
		return new ArrayList<>();
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ErroreDO> inserimentoMassivo(Integer annoCampagna) {
		List<ErroreDO> listaErrori = new ArrayList<>();
		Set<String> elencoPascoliSenzaDuplicati = null;
		try {
			List<String> elencoPascoli = a4gIstruttoriaClient.getElencoPascoli(annoCampagna);
			if (!CollectionUtils.isEmpty(elencoPascoli)) {
				elencoPascoliSenzaDuplicati = new HashSet<>(elencoPascoli);
			}
		} catch (Exception e1) {
			log.error("Errore nell'estrazione dell'elenco CodiciPascolo. Operazione non eseguita");
			ErroreDO errore = new ErroreDO("", ErroreDO.consistenzaAlPascoloPAC2015, new GregorianCalendar(), e1);
			listaErrori.add(errore);
			return listaErrori;
		}

		if (elencoPascoliSenzaDuplicati != null) {
			int cont = 0;
			for (String element : elencoPascoliSenzaDuplicati) {

				log.debug("Iterazione #: " + ++cont + " codicePascolo " + element);

				ErroreDO errore = inserimentoConsistenzaAlPascoloPAC2015(element, annoCampagna);
				if (errore != null) {
					listaErrori.add(errore);
				}

			}
		}
		return listaErrori;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Long aggiornaListaCuaaDaSincronizzare(Integer annoCampagna) throws RestClientException {
		Long result = 0L;
		log.debug("Inizio aggiornaListaCuaaDaSincronizzare");
		try {

			List<CuaaDenominazione> elencoCuaa = a4gIstruttoriaClient.getElencoCuaa(annoCampagna);
			if (CollectionUtils.isEmpty(elencoCuaa)) {
				log.debug("nessun cuaa da aggiornare per l'anno di campagna {}", annoCampagna);
				return result;
			}

			List<String> listaCuaaSync = statoSincronizzazioneDao.getListaCuaaTabella(annoCampagna);
			if (listaCuaaSync == null) {
				listaCuaaSync = new ArrayList<>();
			}

			for (CuaaDenominazione cuaaDenominazione : elencoCuaa) {
				if (!listaCuaaSync.contains(cuaaDenominazione.getCuaa())) {
					result++;
					log.debug("nuovo cuaa {} da gestire per l'anno di campagna {}", cuaaDenominazione.getCuaa(), annoCampagna);
					statoSincronizzazioneDao.insertCuaa(annoCampagna, cuaaDenominazione.getCuaa());
				}
			}
			log.debug("nuov cuaa da gestire per l'anno di campagna {} = #{}", annoCampagna, result);
		} catch (Exception e1) {
			log.error("Errore aggiornaListaCuaaDaSincronizzare per anno campagna {}", annoCampagna, e1);
			throw new RestClientException(e1.getMessage());
		}

		return result;
	}

	@Transactional
	public ErroreDO inserimentoConsistenzaAlPascoloPAC2015(String codicePascolo, Integer annoCampagna) {
		log.debug("######### InserimentoConsistenzaAlPascoloPAC2015 #########");

		ErroreDO errore = null;

		try {
			consPascoloDao.closeRecordByCodicePascolo(codicePascolo, annoCampagna);

			ArrayOfRootDatiCONSISTENZAPASCOLO2015 uscitePascolo = consistenzaService.getConsistenzaAlPascoloPAC2015(String.valueOf(annoCampagna), codicePascolo);

			if (uscitePascolo == null) {
				log.debug("nessun record attivo sulla BDN");
				return null;
			}

			List<CONSISTENZAPASCOLO2015> pascoliCuaa = uscitePascolo.getCONSISTENZAPASCOLO2015();

			Iterator<CONSISTENZAPASCOLO2015> i = pascoliCuaa.iterator();

			while (i.hasNext()) {
				log.debug("pascoloCuaa, {}" ,i);
				CONSISTENZAPASCOLO2015 consistenzaPascolo2015 = i.next();

				// Date di riferimento
				String dataInizio = "01/01/".concat(annoCampagna.toString());
				String dataFine = "12/31/".concat(annoCampagna.toString());

				// Chiamate ai registri dei pascoli in base alla specie di animali al pascolo
				log.debug("Params: codicePascolo {} dataInizio {} dataFine {}", consistenzaPascolo2015.getPASCOLOCODICE(), dataInizio, dataFine);
				// Bovini
				if (CODICI_SPECIE_BOVINI.contains(consistenzaPascolo2015.getSPECODICE())) {
					DsREGISTRIPASCOLIG elencoRegistriPascoloCodPeriodo = mapElencoRegistriPascoli(struttureClient.getElencoRegistriPascoloCodPeriodo(consistenzaPascolo2015.getPASCOLOCODICE(), dataInizio, dataFine).getContent().get(0));
					// Inserimento delle consistenze con check per verificare se già esistono sul db
					if ( elencoRegistriPascoloCodPeriodo != null && !CollectionUtils.isEmpty(elencoRegistriPascoloCodPeriodo.getREGISTRIPASCOLI()) ) {
						List<ConsistenzaAlPascoloPAC2015DO> boviniLista = prepareConsistenzePascoloBovini(elencoRegistriPascoloCodPeriodo, annoCampagna, consistenzaPascolo2015);
						for (ConsistenzaAlPascoloPAC2015DO bovino : boviniLista) {	
							List<ConsistenzaAlPascoloPAC2015DO> onDbList = consPascoloDao.getConsistenzaAlPascoloPAC2015(annoCampagna, bovino);
							log.debug("Inserimento consistenza bovini: per codice {}, - fasciaEta : {}, - #capi: {}", bovino.getCodiPasc(), bovino.getFascEtaa(), bovino.getNumeCapi() );
							// Check valori duplicati prima di una nuova insert
							if (!CollectionUtils.isEmpty(onDbList)) {
								Boolean flagPascolo=false;
								for(ConsistenzaAlPascoloPAC2015DO onDb : onDbList ) {		
									if( onDb.getCodiPasc().trim().compareTo(bovino.getCodiPasc().trim())==0 && 
										onDb.getNumeCapi().compareTo(bovino.getNumeCapi())==0 && 
										onDb.getNumeCamp().compareTo(bovino.getNumeCamp())==0 && 
										onDb.getFascEtaa().compareTo(bovino.getFascEtaa())==0 &&
										onDb.getNumeCapiMedi().compareTo(bovino.getNumeCapiMedi())==0 && 
										onDb.getGiorPasc().longValue()== bovino.getGiorPasc().longValue() && 
										onDb.getCodiGrupSpec().compareTo(bovino.getCodiGrupSpec())==0 && 
										onDb.getCodiSpec().compareTo(bovino.getCodiSpec())==0 && 
										onDb.getDescSpec().compareTo(bovino.getDescSpec())==0 && 
										onDb.getCodiFiscSogg() != null ? onDb.getCodiFiscSogg().trim().compareTo(bovino.getCodiFiscSogg().trim())==0 : true &&
										onDb.getCodiFiscResp() != null ? onDb.getCodiFiscResp().trim().compareTo(bovino.getCodiFiscResp().trim())==0 : true ) {
										flagPascolo=true;
										log.debug("record su BDN e DB uguali, nessuna operazione sul DB");
										break;
									}
								}
								if(!flagPascolo) {
									log.debug("Inserimento bovino : {}", bovino.getCodiSpec() );
									consPascoloDao.insert(bovino, annoCampagna);
								}
							} else {
								log.debug("Inserimento bovino onDbList is Empty: {}", bovino.getCodiSpec() );
								consPascoloDao.insert(bovino, annoCampagna);
							}
						}
					}
				}

				// Ovi-Caprini
				if (CODICI_SPECIE_OVINI.contains(consistenzaPascolo2015.getSPECODICE())) { 
					DsOVIREGISTRIPASCOLIG elencoOVIRegistriPascoloCodPeriodo = mapElencoOviRegistriPascoli(struttureClient.getElencoOVIRegistriPascoloCodPeriodo(consistenzaPascolo2015.getPASCOLOCODICE(), dataInizio, dataFine).getContent().get(0));
					// Inserimento delle consistenze con check per verificare se già esistono sul db
					if ( elencoOVIRegistriPascoloCodPeriodo != null && !CollectionUtils.isEmpty(elencoOVIRegistriPascoloCodPeriodo.getREGISTRIPASCOLI()) ) {
						List<ConsistenzaAlPascoloPAC2015DO> oviniLista = prepareConsistenzePascoloOvini(elencoOVIRegistriPascoloCodPeriodo, annoCampagna, consistenzaPascolo2015);
						for (ConsistenzaAlPascoloPAC2015DO ovi : oviniLista) {	
							List<ConsistenzaAlPascoloPAC2015DO> onDbList = consPascoloDao.getConsistenzaAlPascoloPAC2015(annoCampagna, ovi);
							log.debug("Inserimento consistenza ovini: per codice {}, - fasciaEta : {}, - #capi: {}", ovi.getCodiPasc(), ovi.getFascEtaa(), ovi.getNumeCapi() );
							// Check valori duplicati prima di una nuova insert
							if (!CollectionUtils.isEmpty(onDbList)) {
								Boolean flagPascolo=false;
								for(ConsistenzaAlPascoloPAC2015DO onDb : onDbList ) {
									if( onDb.getCodiPasc().trim().compareTo(ovi.getCodiPasc().trim())==0 && 
										onDb.getNumeCapi().compareTo(ovi.getNumeCapi())==0 && 
										onDb.getNumeCamp().compareTo(ovi.getNumeCamp())==0 && 
										onDb.getFascEtaa().compareTo(ovi.getFascEtaa())==0 &&
										onDb.getNumeCapiMedi().compareTo(ovi.getNumeCapiMedi())==0 &&
										onDb.getGiorPasc().longValue() == ovi.getGiorPasc().longValue() && 
										onDb.getCodiGrupSpec().compareTo(ovi.getCodiGrupSpec())==0 && 
										onDb.getCodiSpec().compareTo(ovi.getCodiSpec())==0 && 
										onDb.getDescSpec().compareTo(ovi.getDescSpec())==0 && 
										onDb.getCodiFiscSogg() != null ? onDb.getCodiFiscSogg().trim().compareTo(ovi.getCodiFiscSogg().trim())==0 : true && 
										onDb.getCodiFiscResp() != null ? onDb.getCodiFiscResp().trim().compareTo(ovi.getCodiFiscResp().trim())==0 : true) {
										flagPascolo=true;
										log.debug("record su BDN e DB uguali, nessuna operazione sul DB");
										break;
									}
								}
								if(!flagPascolo) {
									log.debug("Inserimento ovini : {}", ovi.getCodiSpec() );
									consPascoloDao.insert(ovi, annoCampagna);
								}
								
							} else {
								log.debug("Inserimento ovini onDbList is Empty : {}", ovi.getCodiSpec() );
								consPascoloDao.insert(ovi, annoCampagna);
							}	
						}
					}
				}

				// Equini
				if (CODICI_SPECIE_EQUIDI.contains(consistenzaPascolo2015.getSPECODICE())) { 
					DsEQUIREGISTRIPASCOLIG elencoEQUIRegistriPascoloCod = mapElencoequiRegistriPascoli(struttureClient.getElencoEQUIRegistriPascoloCod(consistenzaPascolo2015.getPASCOLOCODICE(), dataInizio, dataFine).getContent().get(0));
					// Inserimento delle consistenze con check per verificare se già esistono sul db
					if ( elencoEQUIRegistriPascoloCod != null && !CollectionUtils.isEmpty(elencoEQUIRegistriPascoloCod.getREGISTRIPASCOLI()) ) {
						List<ConsistenzaAlPascoloPAC2015DO> equiniLista = prepareConsistenzePascoloEqui(elencoEQUIRegistriPascoloCod, annoCampagna, consistenzaPascolo2015);
						for (ConsistenzaAlPascoloPAC2015DO equi : equiniLista) {	
							List<ConsistenzaAlPascoloPAC2015DO> onDbList = consPascoloDao.getConsistenzaAlPascoloPAC2015(annoCampagna, equi);
							log.debug("Inserimento consistenza equini: per codice {}, - fasciaEta : {}, - #capi: {}", equi.getCodiPasc(), equi.getFascEtaa(), equi.getNumeCapi() );
							// Check valori duplicati prima di una nuova insert
							if (!CollectionUtils.isEmpty(onDbList)) {
								Boolean flagPascolo=false;
								for(ConsistenzaAlPascoloPAC2015DO onDb : onDbList ) {
									if( onDb.getCodiPasc().trim().compareTo(equi.getCodiPasc().trim())==0 && 
										onDb.getNumeCapi().compareTo(equi.getNumeCapi())==0	&& 
										onDb.getNumeCamp().compareTo(equi.getNumeCamp())==0 && 
										onDb.getFascEtaa().compareTo(equi.getFascEtaa())==0 &&
										onDb.getNumeCapiMedi().compareTo(equi.getNumeCapiMedi())==0 && 
										onDb.getGiorPasc().longValue()== equi.getGiorPasc().longValue() && 
										onDb.getCodiGrupSpec().compareTo(equi.getCodiGrupSpec())==0 && 
										onDb.getCodiSpec().compareTo(equi.getCodiSpec())==0 && 
										onDb.getDescSpec().compareTo(equi.getDescSpec())==0 && 
										onDb.getCodiFiscSogg() != null ? onDb.getCodiFiscSogg().trim().compareTo(equi.getCodiFiscSogg().trim())==0 : true &&
										onDb.getCodiFiscResp() != null ? onDb.getCodiFiscResp().trim().compareTo(equi.getCodiFiscResp().trim())==0 : true) {
										flagPascolo=true;
										log.debug("record su BDN e DB uguali, nessuna operazione sul DB");
										break;
									}
								} 
								if(!flagPascolo) {
									log.debug("Inserimento equini : {}", equi.getCodiSpec() );
									consPascoloDao.insert(equi, annoCampagna);
								}
							} 
							else {
								log.debug("Inserimento equini onDbList is Empty: {}", equi.getCodiSpec() );
								consPascoloDao.insert(equi, annoCampagna);
							}			
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Errore nel caricamento della ConsistenzaAlPascoloPAC2015 per codicePascolo [" + codicePascolo + "]: " + e.getMessage());
			errore = new ErroreDO(codicePascolo, ErroreDO.consistenzaAlPascoloPAC2015, new GregorianCalendar(), e);
		}
		return errore;
	}

	private List<ConsistenzaAlPascoloPAC2015DO> prepareConsistenzePascoloBovini(DsREGISTRIPASCOLIG elencoRegistriPascolo, Integer annoCampagna, CONSISTENZAPASCOLO2015 consistenzaPascolo2015) {
		log.debug("Per il cuaa {}, anno {}, calcolo Bovini START ****", consistenzaPascolo2015.getCODFISCALEDETEN(), annoCampagna);

		LocalDateTime dtInizioPascolo = LocalDateTime.of(annoCampagna,Month.JUNE, 15, 0 ,0);
		LocalDateTime dtFinePascolo = LocalDateTime.of(annoCampagna,Month.SEPTEMBER, 25, 0, 0);

		List<ConsistenzaAlPascoloPAC2015DO> consistenzeAlPascoloPAC2015DO = new ArrayList<>();

		elencoRegistriPascolo.getREGISTRIPASCOLI()
		.stream()
		.peek((x) -> log.debug("Capo id {}" , x.getCAPOID()))
		.peek((x) -> log.debug("Capo ingresso pascolo {}" , x.getDTINGRESSOPASCOLO()))
		.filter(r -> annoCampagna.equals(r.getDTINGRESSOPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime().getYear()))
		.peek((x) -> log.debug("Capo rientro pascolo {}" , x.getDTRIENTROPASCOLO()))
		.filter(r -> r.getDTRIENTROPASCOLO() != null)
		.collect(Collectors.groupingBy(DsREGISTRIPASCOLIG.REGISTRIPASCOLI::getCAPOID)) // mappa <capoId, listaRegitriPascoli> creata per gestire più rientri al pascolo nello stesso anno
		.entrySet()
		.stream()
		.peek((x) -> log.debug("Capo ID: " + x.getKey().toString() + " - Lista registri pascoli size: " + x.getValue().size()))
		// filtro in base ai giorni di pascolamento -> devono rispecchiare la soglia min di 60gg e max 300gg (prima del 2021) oppure min di 60gg (dal 2021 in poi)
		.filter(entry -> {
			Long giorniPascolamentoAnnuale = entry.getValue().stream().map(registro -> {
				LocalDateTime dtUscita = registro.getDTRIENTROPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
				LocalDateTime dtIngresso = registro.getDTINGRESSOPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
				return Duration.between(dtIngresso.toLocalDate().atStartOfDay(), dtUscita.toLocalDate().atStartOfDay()).toDays();
			}).reduce((a,b) -> a + b).orElse(0L);

			log.debug("Giorni Pascolamento Annuale {}", giorniPascolamentoAnnuale);
			// il limite dei 300 giorni dal 2021 è garantito dal filtro sui pascoli con rientri
			if (annoCampagna >= 2021) {
				return giorniPascolamentoAnnuale >= 60; 
			} else { // annoCampagna < 2021
				return giorniPascolamentoAnnuale >= 60 && giorniPascolamentoAnnuale <= 300;
			}
		})
		.forEach(entry -> {
			Long giorniPascolamentoTotali = 0L;
			if (annoCampagna >= 2021) { // finestra temporale = 15 giugno - 25 settembre
				giorniPascolamentoTotali = entry.getValue().stream().map(r -> {
					LocalDateTime dtIngresso = r.getDTINGRESSOPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
					LocalDateTime dtUscita = r.getDTRIENTROPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
					LocalDateTime dtIngressoMax = dtInizioPascolo.compareTo(dtIngresso) >= 0 ? dtInizioPascolo : dtIngresso;
					LocalDateTime dtUscitaMin = dtFinePascolo.compareTo(dtUscita) <= 0 ? dtFinePascolo : dtUscita;
					return Duration.between(dtIngressoMax.toLocalDate().atStartOfDay(), dtUscitaMin.toLocalDate().atStartOfDay()).toDays();
				}).reduce((a,b) -> a + b).orElse(0L);

				log.debug("Giorni Pascolamento Totali {}", giorniPascolamentoTotali);
				// calcolo fasce eta
				Optional<REGISTRIPASCOLI> capo = entry.getValue().stream().findFirst();
				if (capo.isPresent() && giorniPascolamentoTotali >= 60) {
					LocalDate dtNascita = capo.get().getDTNASCITA().toGregorianCalendar().toZonedDateTime().toLocalDate();
					log.debug("Capo id da lista pascoli {}, nato: {}", capo.get().getCAPOID(), dtNascita);
					long months = ChronoUnit.MONTHS.between(dtNascita, dtInizioPascolo);
					ConsistenzaAlPascoloPAC2015DO bdnDataWithGiorniDiPascolamento = new ConsistenzaAlPascoloPAC2015DOBuilder()
							.newDto()
							.with(consistenzaPascolo2015.getCODFISCALEDETEN(), annoCampagna.longValue(), consistenzaPascolo2015.getPASCOLOCODICE())
							.with(consistenzaPascolo2015)
							.addGiorniPascolamento(giorniPascolamentoTotali)
							.addFasciaEta(toFasceEta(months))
							.addCodiceSpecie(capo.get().getCODICESPECIEPROVENIENZA())
							.build();
					consistenzeAlPascoloPAC2015DO.add(bdnDataWithGiorniDiPascolamento);
				}

			} else { // annoCampagna < 2021 - finestra temporale = Anno intero
				giorniPascolamentoTotali = entry.getValue().stream().map(registro -> {
					LocalDateTime dtUscita = registro.getDTRIENTROPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
					LocalDateTime dtIngresso = registro.getDTINGRESSOPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
					return Duration.between(dtIngresso.toLocalDate().atStartOfDay(), dtUscita.toLocalDate().atStartOfDay()).toDays();
				}).reduce((a,b) -> a + b).orElse(0L);

				log.debug("Giorni Pascolamento Totali {}", giorniPascolamentoTotali);
				// calcolo fasce eta
				Optional<REGISTRIPASCOLI> capo = entry.getValue().stream().findFirst();
				if (capo.isPresent()) {
					LocalDate dtNascita = capo.get().getDTNASCITA().toGregorianCalendar().toZonedDateTime().toLocalDate();
					log.debug("Capo id da lista pascoli {}, nato: {}", capo.get().getCAPOID(), dtNascita);
					long months = ChronoUnit.MONTHS.between(dtNascita, dtInizioPascolo);
					ConsistenzaAlPascoloPAC2015DO bdnDataWithGiorniDiPascolamento = new ConsistenzaAlPascoloPAC2015DOBuilder()
							.newDto()
							.with(consistenzaPascolo2015.getCODFISCALEDETEN(), annoCampagna.longValue(), consistenzaPascolo2015.getPASCOLOCODICE())
							.with(consistenzaPascolo2015)
							.addGiorniPascolamento(giorniPascolamentoTotali)
							.addFasciaEta(toFasceEta(months))
							.addCodiceSpecie(capo.get().getCODICESPECIEPROVENIENZA())
							.build();
					consistenzeAlPascoloPAC2015DO.add(bdnDataWithGiorniDiPascolamento);
				}
			}
		});

		// I bovini hanno una sola specie, non è necessario quindi fare questa distinzione
		List<ConsistenzaAlPascoloPAC2015DO> boviniLista = groupByFasceEta(consistenzeAlPascoloPAC2015DO);

		log.debug("Per il cuaa calcolate "+ boviniLista.size() + " consistenze relative ai bovini da salvare");
		return boviniLista;
	}

	private List<ConsistenzaAlPascoloPAC2015DO> prepareConsistenzePascoloOvini(DsOVIREGISTRIPASCOLIG elencoOviRegistriPascolo, Integer annoCampagna, CONSISTENZAPASCOLO2015 consistenzaPascolo2015) {
		log.debug("Per il cuaa {}, anno {}, calcolo Ovini START ****", consistenzaPascolo2015.getCODFISCALEDETEN(), annoCampagna);
		LocalDateTime dtInizioPascolo = LocalDateTime.of(annoCampagna,Month.JUNE, 15, 0, 0);
		LocalDateTime dtFinePascolo = LocalDateTime.of(annoCampagna,Month.SEPTEMBER, 25, 0, 0);

		List<ConsistenzaAlPascoloPAC2015DO> consistenzeAlPascoloPAC2015DO = new ArrayList<>();

		elencoOviRegistriPascolo.getREGISTRIPASCOLI()
		.stream()
		.peek((x) -> log.debug("Capo id {}" , x.getCAPOID()))
		.peek((x) -> log.debug("Capo ingresso pascolo {}" , x.getDTINGRESSOPASCOLO()))
		.filter(r -> annoCampagna.equals(r.getDTINGRESSOPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime().getYear()))
		.peek((x) -> log.debug("Capo rientro pascolo {}" , x.getDTRIENTROPASCOLO()))
		.filter(r -> r.getDTRIENTROPASCOLO() != null)
		.collect(Collectors.groupingBy(DsOVIREGISTRIPASCOLIG.REGISTRIPASCOLI::getCAPOID)) // mappa <capoId, listaRegitriPascoli> creata per gestire più rientri al pascolo nello stesso anno
		.entrySet()
		.stream()
		.peek((x) -> log.debug("Capo ID: " + x.getKey().toString() + "Lista registri pascoli size: " + x.getValue().size()))
		// filtro in base ai giorni di pascolamento -> devono rispecchiare la soglia min di 60gg e max 300gg (prima del 2021) oppure min di 60gg (dal 2021 in poi)
		.filter(entry -> {
			Long giorniPascolamentoAnnuale = entry.getValue().stream().map(registro -> {
				LocalDateTime dtUscita = registro.getDTRIENTROPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
				LocalDateTime dtIngresso = registro.getDTINGRESSOPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
				return Duration.between(dtIngresso.toLocalDate().atStartOfDay(), dtUscita.toLocalDate().atStartOfDay()).toDays();
			}).reduce((a,b) -> a + b).orElse(0L);

			log.debug("Giorni Pascolamento Annuale {}", giorniPascolamentoAnnuale);
			// il limite dei 300 giorni dal 2021 è garantito dal filtro sui pascoli con rientri
			if (annoCampagna >= 2021) {
				return giorniPascolamentoAnnuale >= 60; 
			} else { // annoCampagna < 2021
				return giorniPascolamentoAnnuale >= 30 && giorniPascolamentoAnnuale <= 300;
			}
		})
		.forEach(entry -> {
			Long giorniPascolamentoTotali = 0L;
			if (annoCampagna >= 2021) { // finestra temporale = 15 giugno - 25 settembre
				giorniPascolamentoTotali = entry.getValue().stream().map(r -> {
					LocalDateTime dtIngresso = r.getDTINGRESSOPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
					LocalDateTime dtUscita = r.getDTRIENTROPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
					LocalDateTime dtIngressoMax = dtInizioPascolo.compareTo(dtIngresso) >= 0 ? dtInizioPascolo : dtIngresso;
					LocalDateTime dtUscitaMin = dtFinePascolo.compareTo(dtUscita) <= 0 ? dtFinePascolo : dtUscita;
					return Duration.between(dtIngressoMax.toLocalDate().atStartOfDay(), dtUscitaMin.toLocalDate().atStartOfDay()).toDays();
				}).reduce((a,b) -> a + b).orElse(0L);

				log.debug("Giorni Pascolamento Totali {}", giorniPascolamentoTotali);
				// calcolo fasce eta
				// Creiamo n consistenze per ogni capo con tutti i dati utili
				Optional<DsOVIREGISTRIPASCOLIG.REGISTRIPASCOLI> capo = entry.getValue().stream().findFirst();
				if (capo.isPresent() && giorniPascolamentoTotali >= 60) {
					log.debug("Capo id da lista pascoli {}", capo.get().getCAPOID());
					LocalDate dtNascita = capo.get().getDTNASCITA().toGregorianCalendar().toZonedDateTime().toLocalDate();
					long months = ChronoUnit.MONTHS.between(dtNascita, dtInizioPascolo);
					ConsistenzaAlPascoloPAC2015DO bdnDataWithGiorniDiPascolamento = new ConsistenzaAlPascoloPAC2015DOBuilder()
							.newDto()
							.with(consistenzaPascolo2015.getCODFISCALEDETEN(), annoCampagna.longValue(), consistenzaPascolo2015.getPASCOLOCODICE())
							.with(consistenzaPascolo2015)
							.addGiorniPascolamento(giorniPascolamentoTotali)
							.addFasciaEta(toFasceEta(months))
							.addCodiceSpecie(capo.get().getCODICESPECIEPROVENIENZA())
							.build();
					consistenzeAlPascoloPAC2015DO.add(bdnDataWithGiorniDiPascolamento);
				}

			} else { // annoCampagna < 2021 - finestra temporale = Anno intero
				giorniPascolamentoTotali = entry.getValue().stream().map(registro -> {
					LocalDateTime dtUscita = registro.getDTRIENTROPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
					LocalDateTime dtIngresso = registro.getDTINGRESSOPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
					return Duration.between(dtIngresso.toLocalDate().atStartOfDay(), dtUscita.toLocalDate().atStartOfDay()).toDays();
				}).reduce((a,b) -> a + b).orElse(0L);

				log.debug("Giorni Pascolamento Totali {}", giorniPascolamentoTotali);
				// calcolo fasce eta
				// Creiamo n consistenze per ogni capo con tutti i dati utili
				Optional<DsOVIREGISTRIPASCOLIG.REGISTRIPASCOLI> capo = entry.getValue().stream().findFirst();
				if (capo.isPresent()) {
					log.debug("Capo id da lista pascoli {}", capo.get().getCAPOID());
					LocalDate dtNascita = capo.get().getDTNASCITA().toGregorianCalendar().toZonedDateTime().toLocalDate();
					long months = ChronoUnit.MONTHS.between(dtNascita, dtInizioPascolo);
					ConsistenzaAlPascoloPAC2015DO bdnDataWithGiorniDiPascolamento = new ConsistenzaAlPascoloPAC2015DOBuilder()
							.newDto()
							.with(consistenzaPascolo2015.getCODFISCALEDETEN(), annoCampagna.longValue(), consistenzaPascolo2015.getPASCOLOCODICE())
							.with(consistenzaPascolo2015)
							.addGiorniPascolamento(giorniPascolamentoTotali)
							.addFasciaEta(toFasceEta(months))
							.addCodiceSpecie(capo.get().getCODICESPECIEPROVENIENZA())
							.build();
					consistenzeAlPascoloPAC2015DO.add(bdnDataWithGiorniDiPascolamento);
				}

			}
		});

		// Creo una mappa con le specie e per ogni specie raggruppo/aggrego per fasce di eta
		List<ConsistenzaAlPascoloPAC2015DO> oviLista = new ArrayList<>();
		mapBySpecie(consistenzeAlPascoloPAC2015DO).forEach((specie, listaConsistenze) -> {
			oviLista.addAll(groupByFasceEta(listaConsistenze));
		});

		log.debug("Per il cuaa calcolate "+ oviLista.size() + " consistenze relative agli ovi-caprini da salvare");
		return oviLista;
	}

	private List<ConsistenzaAlPascoloPAC2015DO> prepareConsistenzePascoloEqui(DsEQUIREGISTRIPASCOLIG elencoRegistriPascolo, Integer annoCampagna, CONSISTENZAPASCOLO2015 consistenzaPascolo2015) {
		log.debug("Per il cuaa {}, anno {}, calcolo Equini START ****", consistenzaPascolo2015.getCODFISCALEDETEN(), annoCampagna);
		LocalDateTime dtInizioPascolo = LocalDateTime.of(annoCampagna,Month.JUNE, 15, 0, 0);
		LocalDateTime dtFinePascolo = LocalDateTime.of(annoCampagna,Month.SEPTEMBER, 25, 0, 0);

		List<ConsistenzaAlPascoloPAC2015DO> consistenzeAlPascoloPAC2015DO = new ArrayList<>();
		List<ConsistenzaAlPascoloPAC2015DO> equiLista = new ArrayList<ConsistenzaAlPascoloPAC2015DO>();
		elencoRegistriPascolo.getREGISTRIPASCOLI()
		.stream()
		.peek((x) -> log.debug("Capo id {}" , x.getCAPOID()))
		.peek((x) -> log.debug("Capo ingresso pascolo {}: " , x.getDTINGRESSOPASCOLO()))
		.filter(r -> annoCampagna.equals(r.getDTINGRESSOPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime().getYear()))
		.peek((x) -> log.debug("Capo rientro pascolo {}: " , x.getDTRIENTROPASCOLO()))
		.filter(r -> r.getDTRIENTROPASCOLO() != null)
		.collect(Collectors.groupingBy(DsEQUIREGISTRIPASCOLIG.REGISTRIPASCOLI::getCAPOID)) // mappca <capoId, listaRegitriPascoli> creata per gestire più rientri al pascolo nello stesso anno
		.entrySet()
		.stream()
		.peek((x) -> log.debug("Capo ID: " + x.getKey().toString() + " - Lista registri size: " + x.getValue().size()))
		// filtro in base ai giorni di pascolamento -> devono rispecchiare la soglia min di 60gg e max 300gg (prima del 2021) oppure min di 60gg (dal 2021 in poi)
		.filter(entry -> {
			Long giorniPascolamentoAnnuale = entry.getValue().stream().map(registro -> {
				LocalDateTime dtUscita = registro.getDTRIENTROPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
				LocalDateTime dtIngresso = registro.getDTINGRESSOPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
				return Duration.between(dtIngresso.toLocalDate().atStartOfDay(), dtUscita.toLocalDate().atStartOfDay()).toDays();
			}).reduce((a,b) -> a + b).orElse(0L);

			log.debug("Giorni Pascolamento Annuale {}", giorniPascolamentoAnnuale);
			// il limite dei 300 giorni dal 2021 è garantito dal filtro sui pascoli con rientri
			if (annoCampagna >= 2021) {
				return giorniPascolamentoAnnuale >= 60;
			} else { // annoCampagna < 2021
				return giorniPascolamentoAnnuale >= 60 && giorniPascolamentoAnnuale <= 300;
			}
		})
		// Filtro aggiuntivo solo per gli equini: non vanno considerti i capi che vanno da 0 a 6 mesi di età
		.filter(entry -> {
			LocalDate dtNascita = entry.getValue().stream().findFirst().get().getDTNASCITA().toGregorianCalendar().toZonedDateTime().toLocalDate();
			long months = ChronoUnit.MONTHS.between(dtNascita, dtInizioPascolo);
			// Se il capo è nato dopo il 15 dicembre dell'anno di campagna precedente lo scartiamo
			if (months < 6) {
				return false;
			}
			return true;
		})
		.forEach(entry -> {
			Long giorniPascolamentoTotali = 0L;
			if (annoCampagna >= 2021) { // finestra temporale = 15 giugno - 25 settembre
				giorniPascolamentoTotali = entry.getValue().stream().map(r -> {
					LocalDateTime dtIngresso = r.getDTINGRESSOPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
					LocalDateTime dtUscita = r.getDTRIENTROPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
					LocalDateTime dtIngressoMax = dtInizioPascolo.compareTo(dtIngresso) >= 0 ? dtInizioPascolo : dtIngresso;
					LocalDateTime dtUscitaMin = dtFinePascolo.compareTo(dtUscita) <= 0 ? dtFinePascolo : dtUscita;
					return Duration.between(dtIngressoMax.toLocalDate().atStartOfDay(), dtUscitaMin.toLocalDate().atStartOfDay()).toDays();
				}).reduce((a,b) -> a + b).orElse(0L);

				log.debug("Giorni Pascolamento Totali {}", giorniPascolamentoTotali);
				// calcolo fasce eta
				// Chiamata getCapoEquino
				// Per ciascun elemento filtrato chiamo getCapoEquino e prendo tutti i capi
				var getCapoEquinoResponse = new DsEQUICAPIG();
				DsEQUIREGISTRIPASCOLIG.REGISTRIPASCOLI registro = entry.getValue().stream().findFirst().get();

				if (giorniPascolamentoTotali >= 60) {
					try {
						log.debug("Chiamata Get capo equino per capo ID {} ", registro.getCAPOID());
						getCapoEquinoResponse = mapGetCapoEquino(
								anagraficaCapoClient.getCapoEquino(registro.getCODICEELETTRONICO(), registro.getPASSAPORTO(), registro.getCODICEUELN()).getContent().get(0)
						);
						if (getCapoEquinoResponse != null && !CollectionUtils.isEmpty(getCapoEquinoResponse.getCAPO())) {  
							CAPO capo = getCapoEquinoResponse.getCAPO().get(0);
							log.debug("Capo id da getCapoEquinoResponse {}", capo.getCAPOID());
							LocalDate dtNascita = capo.getDTNASCITA().toGregorianCalendar().toZonedDateTime().toLocalDate();
							long months = ChronoUnit.MONTHS.between(dtNascita, dtInizioPascolo);
							ConsistenzaAlPascoloPAC2015DO bdnDataWithGiorniDiPascolamento = new ConsistenzaAlPascoloPAC2015DOBuilder()
									.newDto()
									.with(consistenzaPascolo2015.getCODFISCALEDETEN(), annoCampagna.longValue(), consistenzaPascolo2015.getPASCOLOCODICE())
									.with(consistenzaPascolo2015)
									.addGiorniPascolamento(giorniPascolamentoTotali)
									.addFasciaEta(toFasceEta(months))
									.addCodiceSpecie(capo.getSPECAPOCODICE())
									.build();
							consistenzeAlPascoloPAC2015DO.add(bdnDataWithGiorniDiPascolamento);
						} else {
							log.error("Errore BDN - webservices/getCapoEquino - registro con cod elettronico: " + registro.getCODICEELETTRONICO() + " passaporto: " + registro.getPASSAPORTO() + " codiceUELN: " + registro.getCODICEUELN());
							throw new IllegalArgumentException("Errore BDN getCapoEquino - Response : " + getCapoEquinoResponse); 
						}
					} catch (JAXBException e) {
						log.error("Errore nel caricamento di getCapoEquino per cuaa [" + consistenzaPascolo2015.getCODFISCALEDETEN() + "]: " + e.getMessage() + " e capo ID {}", registro.getCAPOID() );
						throw new RuntimeException(e);
					}
				}

			} else { // annoCampagna < 2021 - finestra temporale = Anno intero
				giorniPascolamentoTotali = entry.getValue().stream().map(registro -> {
					LocalDateTime dtUscita = registro.getDTRIENTROPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
					LocalDateTime dtIngresso = registro.getDTINGRESSOPASCOLO().toGregorianCalendar().toZonedDateTime().toLocalDateTime();
					return Duration.between(dtIngresso.toLocalDate().atStartOfDay(), dtUscita.toLocalDate().atStartOfDay()).toDays();
				}).reduce((a,b) -> a + b).orElse(0L);

				log.debug("Giorni Pascolamento Totali {}", giorniPascolamentoTotali);
				// calcolo fasce eta
				// Chiamata getCapoEquino
				// Per ciascun elemento filtrato chiamo getCapoEquino e prendo tutti i capi
				var getCapoEquinoResponse = new DsEQUICAPIG();
				DsEQUIREGISTRIPASCOLIG.REGISTRIPASCOLI registro = entry.getValue().stream().findFirst().get();

				try {
					log.debug("Chiamata Get capo equino per capo ID {} ", registro.getCAPOID());
					getCapoEquinoResponse = mapGetCapoEquino(
							anagraficaCapoClient.getCapoEquino(registro.getCODICEELETTRONICO(), registro.getPASSAPORTO(), registro.getCODICEUELN()).getContent().get(0)
					);
					if (getCapoEquinoResponse != null && !CollectionUtils.isEmpty(getCapoEquinoResponse.getCAPO())) {  
						CAPO capo = getCapoEquinoResponse.getCAPO().get(0);
						log.debug("Capo id da getCapoEquinoResponse {}", capo.getCAPOID());
						LocalDate dtNascita = capo.getDTNASCITA().toGregorianCalendar().toZonedDateTime().toLocalDate();
						long months = ChronoUnit.MONTHS.between(dtNascita, dtInizioPascolo);
						ConsistenzaAlPascoloPAC2015DO bdnDataWithGiorniDiPascolamento = new ConsistenzaAlPascoloPAC2015DOBuilder()
								.newDto()
								.with(consistenzaPascolo2015.getCODFISCALEDETEN(), annoCampagna.longValue(), consistenzaPascolo2015.getPASCOLOCODICE())
								.with(consistenzaPascolo2015)
								.addGiorniPascolamento(giorniPascolamentoTotali)
								.addFasciaEta(toFasceEta(months))
								.addCodiceSpecie(capo.getSPECAPOCODICE())
								.build();
						consistenzeAlPascoloPAC2015DO.add(bdnDataWithGiorniDiPascolamento);
					} else {
						log.error("Errore BDN - webservices/getCapoEquino - registro con cod elettronico: " + registro.getCODICEELETTRONICO() + " passaporto: " + registro.getPASSAPORTO() + " codiceUELN: " + registro.getCODICEUELN());
						throw new IllegalArgumentException("Errore BDN getCapoEquino - Response : " + getCapoEquinoResponse); 
					}
				} catch (JAXBException e) {
					log.error("Errore nel caricamento di getCapoEquino per cuaa [" + consistenzaPascolo2015.getCODFISCALEDETEN() + "]: " + e.getMessage() + " e capo ID {}", registro.getCAPOID() );
					throw new RuntimeException(e);
				}
			}
		});

		// Creo una mappa con le specie e per ogni specie raggruppo/aggrego per fasce di eta
		mapBySpecie(consistenzeAlPascoloPAC2015DO).forEach((specie, listaConsistenze) -> {
			equiLista.addAll(groupByFasceEta(listaConsistenze));
		});	
		log.debug("Per il cuaa calcolate " + equiLista.size() + " consistenze relative agli equini da salvare");
		return equiLista;
	}

	private List<ConsistenzaAlPascoloPAC2015DO> groupByFasceEta(List<ConsistenzaAlPascoloPAC2015DO> consistenzeAlPascoloPAC2015DO) {
		List<ConsistenzaAlPascoloPAC2015DO> out = new ArrayList<>();
		Map<String, List<ConsistenzaAlPascoloPAC2015DO>> mappa = consistenzeAlPascoloPAC2015DO.stream().collect(Collectors.groupingBy(ConsistenzaAlPascoloPAC2015DO::getFascEtaa));
		mappa.forEach((fasciaEta, listaConsistenze) -> {
			ConsistenzaAlPascoloPAC2015DO consistenzaAlPascoloPAC2015DO = new ConsistenzaAlPascoloPAC2015DOBuilder()
					.withFull(listaConsistenze.get(0))
					.addNumeroCapi(Long.valueOf(listaConsistenze.size()))
					.addGiorniPascolamento(listaConsistenze.stream().map(cons -> cons.getGiorPasc()).reduce((a,b) -> a+b).orElse(0L))
					.build();
			out.add(consistenzaAlPascoloPAC2015DO);
		});
		return out;
	}

	private Map<String, List<ConsistenzaAlPascoloPAC2015DO>> mapBySpecie(List<ConsistenzaAlPascoloPAC2015DO> consistenzeAlPascoloPAC2015DO) {
		return consistenzeAlPascoloPAC2015DO.stream().collect(Collectors.groupingBy(ConsistenzaAlPascoloPAC2015DO::getCodiSpec));
	}

	private FasciaEtaConsistenzaPascolo toFasceEta(long months) {
		if (months < 6L) {
			log.debug("Fascia eta < 6, mesi: {}" , months);
			return FasciaEtaConsistenzaPascolo.MESI_0_6;
		} else if (months >= 6L && months < 24L) {
			log.debug("Fascia eta >= 6 e < 24, mesi: {}" , months);
			return FasciaEtaConsistenzaPascolo.MESI_6_24;
		} else {
			log.debug("Fascia eta => 24, mesi: {}" , months);
			return FasciaEtaConsistenzaPascolo.OLTRE_24;
		}

	}

	private DsREGISTRIPASCOLIG mapElencoRegistriPascoli(Object obj) throws JAXBException {
		log.debug("mapElencoRegistriPascoli START");
		Node content = (Node) obj; // root
		NodeList childList = content.getChildNodes();  // 0 = error, 1 = dati
		Node dati = childList.item(1);
		log.debug("Node dati 0 {}", childList.item(0).getLocalName());
		log.debug("Node dati 1 {}", childList.item(1).getLocalName());
		Node datiSenzaDsPascoli = removeBy(dati, "dsPASCOLI_G");

		JAXBContext context = JAXBContext.newInstance(DsREGISTRIPASCOLIG.class);
		Unmarshaller um = context.createUnmarshaller();
		DsREGISTRIPASCOLIG response = null;
		if (datiSenzaDsPascoli.getFirstChild() != null)
			response = (DsREGISTRIPASCOLIG)um.unmarshal((Node)datiSenzaDsPascoli.getFirstChild());
		return response;
	}

	private DsOVIREGISTRIPASCOLIG mapElencoOviRegistriPascoli(Object obj) throws JAXBException {
		log.debug("mapElencoRegistriPascoli START");
		Node content = (Node) obj; // root
		NodeList childList = content.getChildNodes();  // 0 = error, 1 = dati
		Node dati = childList.item(1);
		log.debug("Node dati 0 {}", childList.item(0).getLocalName());
		log.debug("Node dati 1 {}", childList.item(1).getLocalName());
		Node datiSenzaDsPascoli = removeBy(dati, "dsPASCOLI_G");

		JAXBContext context = JAXBContext.newInstance(DsOVIREGISTRIPASCOLIG.class);
		Unmarshaller um = context.createUnmarshaller();
		DsOVIREGISTRIPASCOLIG response = null;
		if (datiSenzaDsPascoli.getFirstChild() != null)
			response = (DsOVIREGISTRIPASCOLIG)um.unmarshal((Node)datiSenzaDsPascoli.getFirstChild());
		return response;
	}

	private DsEQUIREGISTRIPASCOLIG mapElencoequiRegistriPascoli(Object obj) throws JAXBException {
		log.debug("mapElencoequiRegistriPascoli START");
		Node content = (Node) obj; // root
		NodeList childList = content.getChildNodes();  // 0 = error, 1 = dati
		Node dati = childList.item(1);
		log.debug("Node dati 0 {}", childList.item(0).getLocalName());
		log.debug("Node dati 1 {}", childList.item(1).getLocalName());
		Node datiSenzaDsPascoli = removeBy(dati, "dsPASCOLI_G");

		JAXBContext context = JAXBContext.newInstance(DsEQUIREGISTRIPASCOLIG.class);
		Unmarshaller um = context.createUnmarshaller();
		DsEQUIREGISTRIPASCOLIG response = null;
		if (datiSenzaDsPascoli.getFirstChild() != null)
			response = (DsEQUIREGISTRIPASCOLIG)um.unmarshal((Node)datiSenzaDsPascoli.getFirstChild());
		return response;
	}

	private DsEQUICAPIG mapGetCapoEquino(Object obj) throws JAXBException {
		Node content = (Node) obj;
		NodeList childList = content.getChildNodes();
		Node dati = childList.item(1);

		JAXBContext context = JAXBContext.newInstance(DsEQUICAPIG.class);
		Unmarshaller um = context.createUnmarshaller();
		DsEQUICAPIG response = null;
		if (dati.getFirstChild() != null)
			response = (DsEQUICAPIG)um.unmarshal((Node)dati.getFirstChild());

		return response;
	}

	// rimuovo tutti i nodi diversi dal tipo 'Element' e il nodo etichettato con toRemove
	private Node removeBy(Node node, String toRemove) {
		int i = 0;
		log.debug("Node child dati size {}", node.getChildNodes().getLength());
		while ( i < node.getChildNodes().getLength()) {
			log.debug("Node child dati posizione {}, nome {} ", i, node.getChildNodes().item(i).getLocalName());
			if (node.getChildNodes().item(i) instanceof Element) {
				Element e = (Element) node.getChildNodes().item(i);
				if (toRemove != null && e.getTagName().equals(toRemove)) {
					// di tipo Element e == toRemove -> lo rimuovo
					log.debug("Rimozione nodo {}",node.getChildNodes().item(i).getLocalName());
					node.removeChild(node.getChildNodes().item(i));
				} else { // di tipo Element e != toRemove -> lo lascio
					log.debug("Ok nodo {}",node.getChildNodes().item(i).getLocalName());
					i++;
				}
			} else { // non di tipo Element -> lo rimuovo
				log.debug("Rimozione nodo {}",node.getChildNodes().item(i).getLocalName());
				node.removeChild(node.getChildNodes().item(i));
			}
		}
		return node;
	}
}
