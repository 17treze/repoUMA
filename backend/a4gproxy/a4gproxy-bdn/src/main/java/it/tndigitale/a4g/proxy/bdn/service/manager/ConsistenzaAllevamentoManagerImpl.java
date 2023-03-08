package it.tndigitale.a4g.proxy.bdn.service.manager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.infotn.bdn.wsbdnagea.wsclient.ConsistenzaAllevamentoService;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiCONSISTENZAALLEVAMENTO;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiCONSISTENZAALLEVAMENTO.CONSISTENZAALLEVAMENTO;
import it.tndigitale.a4g.proxy.bdn.client.AnagraficaCapoClient;
import it.tndigitale.a4g.proxy.bdn.client.RegistroStallaClient;
import it.tndigitale.a4g.proxy.bdn.config.Bdnparameter;
import it.tndigitale.a4g.proxy.bdn.dto.ConsistenzaAllevamentoDO;
import it.tndigitale.a4g.proxy.bdn.dto.ErroreDO;
import it.tndigitale.a4g.proxy.bdn.repository.interfaces.ConsistenzaAllevamentoDAO;
import it.tndigitale.a4g.proxy.bdn.repository.interfaces.StatoSincronizzazioneBdnDAO;
import it.tndigitale.a4g.proxy.bdn.wsbdninterrogazioni.service.InterrogazioniAziendeServiceImpl;
import it.tndigitale.a4g.proxy.ws.bdn.dsequicapg.DsEQUICAPIG;
import it.tndigitale.a4g.proxy.ws.bdn.dsequicapg.DsEQUICAPIG.CAPO;
import it.tndigitale.a4g.proxy.ws.bdn.dsequiregistrig.DsEQUIREGISTRIG;
import it.tndigitale.a4g.proxy.ws.bdn.dsequiregistrig.DsEQUIREGISTRIG.EQUIREGISTRIDISTALLA;
import it.tndigitale.ws.dsaziendeg.DsAZIENDEG;

@Service
public class ConsistenzaAllevamentoManagerImpl implements ConsistenzaAllevamentoManager {

	@Autowired
	protected ConsistenzaAllevamentoDAO consistenzaAllevamentoDao;
	@Autowired
	protected StatoSincronizzazioneBdnDAO statoSyncDao;
	@Autowired
	protected Bdnparameter bdnParameter;

	@Autowired
	private RegistroStallaClient registroStallaClient;
	@Autowired
	private AnagraficaCapoClient anagraficaCapoClient;


	// Logger
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private static final Set<String> CODICI_SPECIE_EQUIDI = Set.of("0126", "0147", "0148", "0149");

	ConsistenzaAllevamentoService consistenzaAllevamentoService = new ConsistenzaAllevamentoService();

	@Autowired
	InterrogazioniAziendeServiceImpl aziendaService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ErroreDO> inserimentoMassivo(Integer annoCampagna) {

		List<ErroreDO> listaErrori = new ArrayList<>();

		List<String> listOfCuaa;
		try {
			listOfCuaa = statoSyncDao.getListaCuaaTabella(annoCampagna);
		} catch (Exception e1) {
			log.error("Errore nell'estrazione dell'elenco CUAA. Operazione non eseguita per anno campagna {}", annoCampagna, e1);
			ErroreDO errore = new ErroreDO("", ErroreDO.consistenzaAllevamento, new GregorianCalendar(), e1);
			listaErrori.add(errore);
			return listaErrori;
		}

		int cont = 0;
		for (String element : listOfCuaa) {

			log.debug("Iterazione #: " + ++cont + " cuaa " + element);

			ErroreDO errore = inserimentoConsistenzaAllevamento(element, annoCampagna);
			if (errore != null) {
				listaErrori.add(errore);
			}

		}
		return listaErrori;
	}

	@Override
	@Transactional
	public ErroreDO inserimentoConsistenzaAllevamento(String cuaa, Integer annoCampagna) {

		String dataInizioPeriodo = "14/05/".concat(annoCampagna.toString());
		String dataFinePeriodo = "16/05/".concat(annoCampagna.toString());
		String responsabilita = bdnParameter.getResponsabilita();

		ErroreDO errore = null;

		try {
			ArrayOfRootDatiCONSISTENZAALLEVAMENTO ca = consistenzaAllevamentoService.inviaConsistenzaAllevamento(cuaa, dataInizioPeriodo, dataFinePeriodo, responsabilita);
			if (ca == null || CollectionUtils.isEmpty(ca.getCONSISTENZAALLEVAMENTO())) {
				log.debug("Nessun record attivo sulla BDN per il cuaa {}, chiudo eventuali record esistenti sul DB", cuaa);
				consistenzaAllevamentoDao.closeRecordByCuaa(cuaa, annoCampagna.intValue());
				return null;
			}

			for (CONSISTENZAALLEVAMENTO consistenza : ca.getCONSISTENZAALLEVAMENTO()) {
				ConsistenzaAllevamentoDO consistenzaDo = new ConsistenzaAllevamentoDO(consistenza, cuaa, annoCampagna.intValue());
				String codiceAzienda = consistenzaDo.getCodiAsll();
				log.info("Per consistenza con allevID {} - EsisteAziendaInCache {}", consistenza.getPALLEVID(), codiceAzienda);
				if (!consistenzaAllevamentoDao.esisteAziendaInCache(codiceAzienda)) {
					log.debug("Devo salvare i dati dell'allevamento {}", codiceAzienda);
					sincronizzaAzienda(codiceAzienda);
				}

				// se l'allevamento è di equidi è necessario fare ulteriori chiamate per reperirne i dati corretti
				log.debug("CodiSpec: {}", consistenzaDo.getCodiSpec());
				if (!CODICI_SPECIE_EQUIDI.contains(consistenzaDo.getCodiSpec())) {
					// Cerco eventuale record presente su DB, se esiste non salvo ma proseguo
					ConsistenzaAllevamentoDO onDb = consistenzaAllevamentoDao.getConsistenzaAllevamento(cuaa, consistenzaDo);
						if (onDb != null && onDb.compareTo(consistenzaDo) == 0) {
							log.debug("record su BDN e DB uguali, nessuna operazione sul DB per il record {}", onDb.getIdCaws());
						} else {
							if (onDb != null) {
								log.debug("record sul DB con ID " + onDb.getIdCaws() + " da chiudere prima di inserire il nuovo");
								consistenzaAllevamentoDao.closeRecordById(onDb);
							}
							consistenzaAllevamentoDao.insert(consistenzaDo, cuaa, annoCampagna.intValue());
						}
					} else {
						log.debug("EQUIDE - IdAlleBdn: {}", consistenzaDo.getIdAlleBdn());
						long start = System.currentTimeMillis();
						// storico di tutti gli equidi passati per l'allevamento indicato
						Object mapElencoEquiRegistriStalla = mapElencoEquiRegistriStalla(registroStallaClient.getElencoEquiRegistroStalla(String.valueOf(consistenzaDo.getIdAlleBdn()), "storico").getContent().get(0));
						
						if (mapElencoEquiRegistriStalla != null && mapElencoEquiRegistriStalla instanceof String) {
							log.error("Errore BDN - webservices/Elenco_Equi_RegistriStalla ID Allev BDN: {}, tipo: storico - Error: {}", consistenzaDo.getIdAlleBdn(), mapElencoEquiRegistriStalla );
							throw new IllegalArgumentException("Errore BDN Elenco_Equi_RegistriStalla - Response : " + mapElencoEquiRegistriStalla); 
						} else if (mapElencoEquiRegistriStalla == null) {
							log.warn("BDN - webservices/Elenco_Equi_RegistriStalla ID Allev BDN: {}, tipo: storico non ha prodotto risultati: {}", consistenzaDo.getIdAlleBdn(), mapElencoEquiRegistriStalla );
						} else { // Caso OK
							
							// Filtro i registri dei pascoli
							LocalDateTime dataRiferimento = LocalDateTime.of(annoCampagna, 05, 15, 0, 0);
							List<EQUIREGISTRIDISTALLA> registriFiltered = ((DsEQUIREGISTRIG)mapElencoEquiRegistriStalla).getEQUIREGISTRIDISTALLA().stream()
									.filter(registro -> 
									// la data di riferimento - 15 maggio della campagna di riferimento - è compresa fra dt_ingresso e dt_uscita
									registro.getDTINGRESSO().toGregorianCalendar().toZonedDateTime().toLocalDateTime()
									.isBefore(dataRiferimento)
									&&
									// Se DT_USCITA è null lo consideriamo valido (ovvero non è ancora uscito)
									(registro.getDTUSCITA() == null || registro.getDTUSCITA().toGregorianCalendar().toZonedDateTime().toLocalDateTime()
									.isAfter(dataRiferimento))
									&&
									// hanno più di 6 mesi alla data di riferimento, quindi “data di riferimento” - DT_NASCITA>= 6 mesi
									registro.getDTNASCITA().toGregorianCalendar().toZonedDateTime().toLocalDateTime().isBefore(dataRiferimento.minusMonths(6))
											)
									.collect(Collectors.toList());
							log.debug("Registri filtrati size : {}", registriFiltered.size());
							if (!CollectionUtils.isEmpty(registriFiltered)) {
								List<CAPO> capi = new ArrayList<>();
								
								// Per ciascun elemento filtrato chiamo getCapoEquino e prendo tutti i capi
								for (EQUIREGISTRIDISTALLA registro : registriFiltered) {
									log.debug("Registro cod elettronico: {}", registro.getCODELETTRONICO());
									var getCapoEquinoResponse = new DsEQUICAPIG();
									try {
										log.debug("GetCapoEquino per cuaa [" + cuaa + "]" + " e registro con cod elettronico: " + registro.getCODELETTRONICO() + " passaporto: " + registro.getPASSAPORTO() + " codiceUELN: " + registro.getCODICEUELN());
										getCapoEquinoResponse = mapGetCapoEquino(anagraficaCapoClient.getCapoEquino(registro.getCODELETTRONICO(), registro.getPASSAPORTO(), registro.getCODICEUELN()).getContent().get(0));
										if (getCapoEquinoResponse != null && !CollectionUtils.isEmpty(getCapoEquinoResponse.getCAPO())) {
											capi.addAll(getCapoEquinoResponse.getCAPO());
										} else {
											log.error("Errore BDN - webservices/getCapoEquino - registro con cod elettronico: " + registro.getCODELETTRONICO() + " passaporto: " + registro.getPASSAPORTO() + " codiceUELN: " + registro.getCODICEUELN());
											throw new IllegalArgumentException("Errore BDN getCapoEquino - Response : " + getCapoEquinoResponse); 
										}
									} catch (JAXBException e) {
										log.error("Errore nel caricamento di getCapoEquino per cuaa [" + cuaa + "]: {}", e);
										throw e; 
									}
								}
			
								// Raggruppo tutti i capi per codice specie
								Map<String, List<CAPO>> mappa = capi.stream().collect(Collectors.groupingBy(CAPO::getSPECAPOCODICE));
								for (Iterator<Map.Entry<String, List<CAPO>>> entries = mappa.entrySet().iterator(); entries.hasNext(); ) {
								    Map.Entry<String, List<CAPO>> entry = entries.next();
									log.debug("Mappa speCodice: {} - lista capi size: {}", entry.getKey(), entry.getValue().size());
									// Calcolo i valori da salvare e sommare
									long capiOver6MesiUnder24Mesi = entry.getValue().stream().filter(capo -> capo.getDTNASCITA().toGregorianCalendar().toZonedDateTime().toLocalDateTime().isBefore(dataRiferimento.minusMonths(6)) &&
											capo.getDTNASCITA().toGregorianCalendar().toZonedDateTime().toLocalDateTime().isAfter(dataRiferimento.minusMonths(24))).count();
									long capiOver24Mesi = entry.getValue().stream().filter(capo -> capo.getDTNASCITA().toGregorianCalendar().toZonedDateTime().toLocalDateTime().isBefore(dataRiferimento.minusMonths(24))).count();
									consistenzaDo.setCodiSpec(entry.getKey()); // Codice Specie provieniente da getCapoEquino
									consistenzaDo.setConsCapi06(0);
									consistenzaDo.setConsCapi624(Double.valueOf(Long.toString(capiOver6MesiUnder24Mesi)));
									consistenzaDo.setConsCapiOver24(Double.valueOf(Long.toString(capiOver24Mesi)));
									consistenzaDo.setConsTota(Double.valueOf(Long.toString(capiOver6MesiUnder24Mesi + capiOver24Mesi))); // Somma dei due valori calcolati
									// Cerco eventuale record presente su DB, se esiste non salvo ma proseguo
									try {
										ConsistenzaAllevamentoDO consistenzaDoForCompare;
										consistenzaDoForCompare = new ConsistenzaAllevamentoDO(consistenza, cuaa, annoCampagna.intValue());
										consistenzaDoForCompare.setCodiSpec(entry.getKey());
										ConsistenzaAllevamentoDO onDb = consistenzaAllevamentoDao.getConsistenzaAllevamento(cuaa, consistenzaDoForCompare);
										if (onDb != null && onDb.compareTo(consistenzaDo) == 0) {
											log.debug("record su BDN e DB uguali, nessuna operazione sul DB per il record {}", onDb.getIdCaws());
										} else {
											if (onDb != null) {
												log.debug("record sul DB con ID " + onDb.getIdCaws() + " da chiudere prima di inserire il nuovo");
												consistenzaAllevamentoDao.closeRecordById(onDb);
											}
											consistenzaAllevamentoDao.insert(consistenzaDo, cuaa, annoCampagna.intValue());
										}
									} catch (Exception e) {
										log.error("Errore nella lettura su DB per verificare i risultati se esistono per cuaa [" + cuaa + "]: {}", e);
										throw e; 
									}
								}
							}
						}
						long finish = System.currentTimeMillis();
						long durata = (finish - start) * 1000;
						log.debug("Inserimento consistenza allevamento per equini:\nallevamento\t{}\ncodice specie allevamento\t{}\ndurata(s)\t{}\n", consistenzaDo.getIdAlleBdn(), consistenzaDo.getCodiSpec(), durata);
					}
				}
		} catch (Exception e) {
			log.error("Errore nel caricamento della Consistenza Allevamento per cuaa [" + cuaa + "]: {}", e.getMessage());
			e.printStackTrace(); // TODO: remove
			errore = new ErroreDO(cuaa, ErroreDO.consistenzaAllevamento, new GregorianCalendar(), e);
		}
		return errore;
	}

	public void sincronizzaAzienda(String codiceAzienda) {
		consistenzaAllevamentoDao.deleteAzienda(codiceAzienda);
		DsAZIENDEG.AZIENDE datiAzienda = aziendaService.getAzienda(codiceAzienda);
		if (datiAzienda != null) {
			consistenzaAllevamentoDao.insertAziendaNuova(codiceAzienda, datiAzienda.getCOMCODICE());
		} else {
			log.warn("Azienda {} non trovata in BDN", codiceAzienda);
		}
	}

	private Object mapElencoEquiRegistriStalla(Object obj) throws JAXBException {
		Node content = (Node) obj;
		NodeList childList = content.getChildNodes();
		Node errorInfo = childList.item(0);
		Node dati = childList.item(1);
		
		JAXBContext context = JAXBContext.newInstance(DsEQUIREGISTRIG.class);
		Unmarshaller um = context.createUnmarshaller();
		Object response = null;
		if (dati.getFirstChild() != null) { 
			response = (DsEQUIREGISTRIG)um.unmarshal((Node)dati.getFirstChild());
		} else { // dati.getFirstChild() = null -> se dati vuoto, verifico il contenuto di error-info->error->id,des
			String descrizioneErrore = errorInfo.getFirstChild().getLastChild().getNodeValue();
			String idErrore = errorInfo.getFirstChild().getFirstChild().getNodeValue();
			if (descrizioneErrore != null && !descrizioneErrore.isEmpty()){
				response = descrizioneErrore;
			} else if (idErrore != null && !idErrore.isEmpty()) {
				response = idErrore;			
			}
		}
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
}
