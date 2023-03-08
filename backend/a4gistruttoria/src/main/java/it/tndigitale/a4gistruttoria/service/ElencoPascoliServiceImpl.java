package it.tndigitale.a4gistruttoria.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.dto.InformazioniColtivazione;
import it.tndigitale.a4gistruttoria.dto.Particella;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ParticellaColtura;
import it.tndigitale.a4gistruttoria.repository.dao.*;
import it.tndigitale.a4gistruttoria.repository.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Riferimento al file su drive /PROCESSI_ALGORITMI/PASCOLI: creazione lista https://drive.google.com/open?id=13tSYvQ7AJZ9Mv07SlQMdEeQT2Ro1a4vC6jxCh8yE3WA
 * 
 * @author A2AC0147
 *
 */
@Service
@Transactional
public class ElencoPascoliServiceImpl implements ElencoPascoliService {

	private static final Logger logger = LoggerFactory.getLogger(ElencoPascoliServiceImpl.class);

	private final String COD_COLT_PASCOLO_1 = "721-218-019";
	private final String COD_COLT_PASCOLO_2 = "721-218-009";
	private final String COD_COLT_PASCOLO_3 = "720-054-009";
	private final String COD_COLT_PASCOLO_4 = "680-103-009";
	private final String COD_COLT_PASCOLO_5 = "640-064-009";
	private final String COD_COLT_PASCOLO_6 = "600-063-009";
	private final String COD_COLT_PASCOLO_7 = "560-065-009";
	private final String COD_COLT_PASCOLO_8 = "560-460-009";

	private List<String> listaColturaPascolo = Arrays.asList(COD_COLT_PASCOLO_1, COD_COLT_PASCOLO_2, COD_COLT_PASCOLO_3, COD_COLT_PASCOLO_4, COD_COLT_PASCOLO_5, COD_COLT_PASCOLO_6, COD_COLT_PASCOLO_7,
			COD_COLT_PASCOLO_8);

	private final String COD_COLT_PASC_AZI_1 = "560-461-009";
	private final String COD_COLT_PASC_AZI_2 = "680-620-009";
	private final String COD_COLT_PASC_AZI_3 = "720-650-009";
	private List<String> listaColtPascoloAzie = Arrays.asList(COD_COLT_PASC_AZI_1, COD_COLT_PASC_AZI_2, COD_COLT_PASC_AZI_3);

	private final String PASCOLO_ANIMALI_PROPRI = "PASCOLAMENTO CON ANIMALI PROPRI";
	private final String PASCOLO_ANIMALI_TERZI = "PASCOLAMENTO CON ANIMALI DI TERZI";

	private List<String> listaMantenimento = Arrays.asList(PASCOLO_ANIMALI_PROPRI, PASCOLO_ANIMALI_TERZI);

	private final String PASCOLO_AZIENDALE = "999TN999";

	@Autowired
	private RichiestaSuperficieDao daoRichiestaSuperficie;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private DomandaUnicaDao daoDomanda;
	@Autowired
	private PascoloParticellaDao daoPascoloParticella;
	
	@Autowired
	private DatiPascoloDao daoDatiPascolo;
	@Autowired
	private ComuniLimitrofiDao daoComuniLimitrofi;

	@Override
	@Transactional
	public void estraiParticellePascolo(Long idDomanda) throws IOException {
		logger.debug("estraiParticellePascolo: elaboro domanda {}",idDomanda);
		Optional<DomandaUnicaModel> dom = daoDomanda.findById(idDomanda);
		if (dom.isPresent()) {
			DomandaUnicaModel domanda = dom.get();
			List<A4gtDatiPascolo> listDatiPascoloMalga = daoDatiPascolo.findByDomandaUnicaModel(domanda);
			if (!listDatiPascoloMalga.isEmpty()) {
				listDatiPascoloMalga.forEach(pascolo -> {
					if (!pascolo.getCodicePascolo().equals(PASCOLO_AZIENDALE))
						estraiPascoliDiMalga(pascolo, domanda);
				});
			}

//			estraiPascoliAziendali(domanda);
		}
	}

	/**
	 * Codice pascolo diverso da 999TN999 a cui sono associate delle ppff con i codiciColturali definiti in listaColturaPascolo oppure con coltura con macrouso 080 e criterio di mantenimento in
	 * listaMantenimento.
	 * 
	 * * dichiarate con i seguenti codici colturali (721-218-019; 721-218-009; 720-054-009; 680-103-009; 640-064-009; 600-063-009; 560-065-009; 560-460-009); *dichiarate con codici colturali che hanno
	 * il macrouso 080 (COD_COLTURA like '080%') AND con i seguenti criteri di mantenimento (PASCOLAMENTO CON ANIMALI PROPRI, PASCOLAMENTO CON ANIMALI DI TERZI).
	 * 
	 * @param pascolo
	 * @param domanda
	 * @return
	 */
	private Long estraiPascoliDiMalga(A4gtDatiPascolo pascolo, DomandaUnicaModel domanda) {
		logger.debug("Inizio Valutazione Pascoli malga");
		Long result = 0L;
		A4gtPascoloParticella pascoloParticellaModel = new A4gtPascoloParticella();
		pascoloParticellaModel.setSupNettaPascolo(BigDecimal.ZERO);
		ArrayList<ParticellaColtura> listaParticellaColtura = new ArrayList<ParticellaColtura>();
		try {
			List<Particella> listaParticelle = mapper.reader().forType(new TypeReference<List<Particella>>() {
			}).readValue(pascolo.getParticelleCatastali());
			listaParticelle.forEach(particella -> {
				List<A4gtRichiestaSuperficie> richiestaSuperficie = daoRichiestaSuperficie.findByDomandaIdParticellaAndIntervento(domanda.getId(), particella.getIdParticella(), CodiceInterventoAgs.BPS);
				richiestaSuperficie.forEach(richiesta -> {
					try {
						Particella partRichiesta = mapper.readValue(richiesta.getInfoCatastali(), Particella.class);
						InformazioniColtivazione infoColtRichiesta = mapper.readValue(richiesta.getInfoColtivazione(), InformazioniColtivazione.class);
						if (listaColturaPascolo.contains(infoColtRichiesta.getCodColtura3()) || (infoColtRichiesta.getCodColtura3().startsWith("080")
								&& (infoColtRichiesta.getDescMantenimento() != null && listaMantenimento.contains(infoColtRichiesta.getDescMantenimento())))) {
							ParticellaColtura particellaColtura = valorizzaParticellaColtura(partRichiesta, infoColtRichiesta);
							particellaColtura.setValString(partRichiesta.getCodNazionale());
							particellaColtura.setValNum(richiesta.getSupRichiestaNetta().floatValue());
							listaParticellaColtura.add(particellaColtura);
							pascoloParticellaModel.setSupNettaPascolo(pascoloParticellaModel.getSupNettaPascolo().add(richiesta.getSupRichiestaNetta()));
						}

					} catch (IOException e) {
						logger.error("estraiPascoliDiMalga: errore elaborando superficie " + richiesta.getId() + " della domanda " + richiesta.getDomandaUnicaModel().getId(), e);
						throw new NoResultException("Errore elaborando superficie richiesta " + richiesta.getId() + " " + e.getMessage());
					}

				});

			});

			Map<String, List<ParticellaColtura>> res = listaParticellaColtura.stream().collect(Collectors.groupingBy(ParticellaColtura::getValString));
			res.toString();

			pascoloParticellaModel.setCodPascolo(pascolo.getCodicePascolo());
			pascoloParticellaModel.setDescPascolo(pascolo.getCodicePascolo());
			pascoloParticellaModel.setDomandaUnicaModel(domanda);
			pascoloParticellaModel.setInfoPartPascolo(mapper.writeValueAsString(listaParticellaColtura));
			daoPascoloParticella.save(pascoloParticellaModel);
			result = pascoloParticellaModel.getId();

		} catch (IOException e) {
			logger.error("estraiPascoliDiMalga: errore elaborando pascolo " + pascolo.getId() + " della domanda " + domanda.getId(), e);
			throw new NoResultException("estraiPascoliDiMalga: errore elaborando pascolo " + pascolo.getId() + " della domanda " + domanda.getId() + e.getMessage());
		}
		logger.debug("Fine Valutazione Pascoli malga");
		return result;
	}

	private ParticellaColtura valorizzaParticellaColtura(Particella particella, InformazioniColtivazione infoColtivazione) {
		ParticellaColtura particellaColtura = new ParticellaColtura();
		particellaColtura.setParticella(particella);
		particellaColtura.setColtura(infoColtivazione.getCodColtura3());
		particellaColtura.setLivello(infoColtivazione.getCodLivello());
		particellaColtura.setDescMantenimento(infoColtivazione.getDescMantenimento());
		return particellaColtura;
	}

	/**
	 * - dichiarate con i seguenti codici colturali (721-218-019; 721-218-009; 720-054-009; 680-103-009; 640-064-009; 600-063-009; 560-065-009; 560-460-009) AND appartenenti a particelle collegate al
	 * CODICE PASCOLO 999TN999 - dichiarate con i seguenti codici colturali (560-461-009;680-620-009;720-650-009) - dichiarate con codici colturali che hanno il macrouso 080 (like '080%') AND con i
	 * seguenti criteri di mantenimento (PASCOLAMENTO CON ANIMALI PROPRI, PASCOLAMENTO CON ANIMALI DI TERZI) e appartenenti a particelle collegate ad un codice pascolo diverso da 999TN999 O a nessun
	 * codice Pascolo
	 * 
	 * @param domanda
	 * @return
	 */
	public void estraiPascoliAziendali(DomandaUnicaModel domanda) throws IOException {
		logger.debug("Inizio Valutazione Pascoli aziendali");
		A4gtDatiPascolo pascoloGenerico = daoDatiPascolo.findByDomandaAndCodicePascolo(domanda.getId(), PASCOLO_AZIENDALE);
		ArrayList<ParticellaColtura> listaParticellePascolo = new ArrayList<ParticellaColtura>();

		ArrayList<ParticellaColtura> listaPartPascoloGenerico = new ArrayList<ParticellaColtura>();
		ArrayList<ParticellaColtura> listaPartPascoloImpegni = new ArrayList<ParticellaColtura>();
		ArrayList<ParticellaColtura> listaPartNonInPascoli = new ArrayList<ParticellaColtura>();
		// Caso 1 - dichiarate con i seguenti codici colturali (721-218-019; 721-218-009; 720-054-009; 680-103-009; 640-064-009; 600-063-009; 560-065-009; 560-460-009) AND appartenenti a particelle
		// collegate al CODICE PASCOLO 999TN999
		if (pascoloGenerico != null) {
			List<Particella> listaParticelle = mapper.reader().forType(new TypeReference<List<Particella>>() {
			}).readValue(pascoloGenerico.getParticelleCatastali());
			listaParticelle.forEach(particella -> {
				List<A4gtRichiestaSuperficie> richiestaSuperficie1 = daoRichiestaSuperficie.findByDomandaIdParticellaAndIntervento(domanda.getId(), particella.getIdParticella(), CodiceInterventoAgs.BPS);
				richiestaSuperficie1.forEach(richiesta -> {
					try {
						Particella partRichiesta = mapper.readValue(richiesta.getInfoCatastali(), Particella.class);
						InformazioniColtivazione infoColtRichiesta = mapper.readValue(richiesta.getInfoColtivazione(), InformazioniColtivazione.class);
						if (listaColturaPascolo.contains(infoColtRichiesta.getCodColtura3()) || (infoColtRichiesta.getCodColtura3().startsWith("080")
								&& (infoColtRichiesta.getDescMantenimento() != null && listaMantenimento.contains(infoColtRichiesta.getDescMantenimento())))) {
							ParticellaColtura particellaColtura = valorizzaParticellaColtura(partRichiesta, infoColtRichiesta);
							particellaColtura.setValString(partRichiesta.getCodNazionale());
							particellaColtura.setValNum(richiesta.getSupRichiestaNetta().floatValue());
							listaPartPascoloGenerico.add(particellaColtura);
							// pascoloParticellaModel.setSupNettaPascolo(pascoloParticellaModel.getSupNettaPascolo().add(richiesta.getSupRichiestaNetta()));
						}

					} catch (IOException e) {
						logger.error("estraiPascoliAziendali: errore elaborando superficie " + richiesta.getId() + " della domanda " + richiesta.getDomandaUnicaModel().getId(), e);
						throw new NoResultException("Errore elaborando superficie richiesta " + richiesta.getId() + " " + e.getMessage());
					}

				});

			});
		}

		// Caso 2 - dichiarate con i seguenti codici colturali (560-461-009;680-620-009;720-650-009)
		List<A4gtRichiestaSuperficie> richiestaSuperficie2 = daoRichiestaSuperficie.findByDomandaInterventoCodColtura3(domanda.getId(), CodiceInterventoAgs.BPS, listaColtPascoloAzie);
		richiestaSuperficie2.forEach(richiesta -> {
			try {
				Particella partRichiesta = mapper.readValue(richiesta.getInfoCatastali(), Particella.class);
				InformazioniColtivazione infoColtRichiesta = mapper.readValue(richiesta.getInfoColtivazione(), InformazioniColtivazione.class);

				ParticellaColtura particellaColtura = valorizzaParticellaColtura(partRichiesta, infoColtRichiesta);
				particellaColtura.setValString(partRichiesta.getCodNazionale());
				particellaColtura.setValNum(richiesta.getSupRichiestaNetta().floatValue());
				listaPartPascoloImpegni.add(particellaColtura);

			} catch (IOException e) {
				logger.error("estraiPascoliAziendali: errore elaborando superficie " + richiesta.getId() + " della domanda " + richiesta.getDomandaUnicaModel().getId(), e);
				throw new NoResultException("Errore elaborando superficie richiesta " + richiesta.getId() + " " + e.getMessage());
			}

		});

		// Caso 3 - dichiarate con codici colturali che hanno il macrouso 080 (like '080%') AND con i seguenti criteri di mantenimento (PASCOLAMENTO CON ANIMALI PROPRI, PASCOLAMENTO CON ANIMALI DI
		// TERZI) AND NON appartenenti a particelle collegate ad un codice pascolo diverso da 999TN999
		List<A4gtRichiestaSuperficie> richiestaSuperficie3 = daoRichiestaSuperficie.findByDomandaAndNotImpegnatePascolo(domanda.getId());
		richiestaSuperficie3.forEach(richiesta -> {
			try {
				Particella partRichiesta = mapper.readValue(richiesta.getInfoCatastali(), Particella.class);
				InformazioniColtivazione infoColtRichiesta = mapper.readValue(richiesta.getInfoColtivazione(), InformazioniColtivazione.class);

				ParticellaColtura particellaColtura = valorizzaParticellaColtura(partRichiesta, infoColtRichiesta);
				particellaColtura.setValString(partRichiesta.getCodNazionale());
				particellaColtura.setValNum(richiesta.getSupRichiestaNetta().floatValue());
				listaPartNonInPascoli.add(particellaColtura);

			} catch (IOException e) {
				logger.error("estraiPascoliAziendali: errore elaborando superficie " + richiesta.getId() + " della domanda " + richiesta.getDomandaUnicaModel().getId(), e);
				throw new NoResultException("Errore elaborando superficie richiesta " + richiesta.getId() + " " + e.getMessage());
			}

		});

		listaParticellePascolo.addAll(listaPartPascoloGenerico);
		listaParticellePascolo.addAll(listaPartPascoloImpegni);
		listaParticellePascolo.addAll(listaPartNonInPascoli);
		gestisciAggregazioniPascoliComune(listaParticellePascolo, domanda);
		logger.debug("Fine Valutazione Pascoli aziendali");
	}

	private void gestisciAggregazioniPascoliComune(ArrayList<ParticellaColtura> listaParticellePascolo, DomandaUnicaModel domanda) throws JsonProcessingException {

		ArrayList<A4gtPascoloParticella> listPascoloParticellaModel = new ArrayList<A4gtPascoloParticella>();
		if (!listaParticellePascolo.isEmpty()) {
			// raggruppa le particelle per il codice nazionale che abbiamo salvato all'interno del campo valString
			Map<String, List<ParticellaColtura>> mapComunePascoli = listaParticellePascolo.stream().collect(Collectors.groupingBy(ParticellaColtura::getValString));
			Map<String, List<ParticellaColtura>> mapComuniAmministrativi = new HashMap<>();
			Set<String> comuniElaborati = new HashSet<>();

			mapComunePascoli.entrySet().stream().forEach(c -> {
				if (!comuniElaborati.contains(c.getKey())) {
					List<ParticellaColtura> listParticelleComuneAmm = getListaParticelleComuneAmministrativo(c.getKey(), comuniElaborati, mapComunePascoli);
					StringBuilder mapKey = new StringBuilder();
					listParticelleComuneAmm.stream().collect(Collectors.groupingBy(ParticellaColtura::getValString)).keySet().stream().forEach(k -> mapKey.append("- ").append(k));
					mapComuniAmministrativi.put(mapKey.toString(), listParticelleComuneAmm);
				}
			});

			mapComuniAmministrativi.entrySet().stream().forEach(c -> {
				logger.debug("Valutazione Pascoli " + c.getKey());
				A4gtPascoloParticella pascoloParticellaModel = new A4gtPascoloParticella();
				pascoloParticellaModel.setSupNettaPascolo(BigDecimal.ZERO);
				c.getValue().forEach(pascolo -> {
					pascoloParticellaModel.setSupNettaPascolo(pascoloParticellaModel.getSupNettaPascolo().add(new BigDecimal(pascolo.getValNum())));
				});
				pascoloParticellaModel.setCodPascolo(PASCOLO_AZIENDALE);
				pascoloParticellaModel.setDescPascolo("AZIENDALE " + c.getKey());
				pascoloParticellaModel.setDomandaUnicaModel(domanda);
				try {
					pascoloParticellaModel.setInfoPartPascolo(mapper.writeValueAsString(c.getValue()));
				} catch (JsonProcessingException e) {
					logger.error("Errore nel salvataggio delle particelle afferenti al pascolo relativo comune amministrativo " + c.getKey());
				}
				listPascoloParticellaModel.add(pascoloParticellaModel);
			});

			daoPascoloParticella.saveAll(listPascoloParticellaModel);
		}

	}

	/**
	 * Metodo per l'accorpamento, in una lista unica restituita in output, delle particelle afferenti a comuni tra loro confinanti.
	 * 
	 * @param codNazionale
	 * @param comuniElaborati
	 * @param mapComunePascoli
	 * @return
	 */
	public List<ParticellaColtura> getListaParticelleComuneAmministrativo(String codNazionale, Set<String> comuniElaborati, Map<String, List<ParticellaColtura>> mapComunePascoli) {

		List<ParticellaColtura> listaParticelle = new ArrayList<>();
		listaParticelle.addAll(mapComunePascoli.get(codNazionale));

		// 1. aggiungo il comune alla lista dei comuni elaborati
		comuniElaborati.add(codNazionale);

		// 2. determino la lista dei comuni limitrofi al comune che sto elaborando
		List<String> listaComuniLimitrofi = new ArrayList<>();
		daoComuniLimitrofi.findByCodNazionaleComune(codNazionale).stream().forEach(cl -> listaComuniLimitrofi.add(cl.getCodNazComuneConfinante()));

		// 3. estraggo la lista delle particelle dai comuni limitrofi nel caso in cui il comune non sia stato già elaborato e sia presente nella lista dei comuni in domanda
		listaComuniLimitrofi.forEach(l -> {
			if (!comuniElaborati.contains(l) && mapComunePascoli.keySet().contains(l)) {
				listaParticelle.addAll(getListaParticelleComuneAmministrativo(l, comuniElaborati, mapComunePascoli));
			}
		});

		return listaParticelle;

	}

}
