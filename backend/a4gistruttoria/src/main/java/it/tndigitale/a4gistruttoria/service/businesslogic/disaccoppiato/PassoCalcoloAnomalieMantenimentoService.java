package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4gistruttoria.A4gistruttoriaConfigurazione;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ConsistenzaAllevamentoDto;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ConsistenzaPascolo2015Dto;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiIstruttoriaPascoli;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiPassoLavorazione;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MapVariabili;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MovimentazionePascoloOviniDto;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ParticellaColtura;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.ComuniLimitrofiDao;
import it.tndigitale.a4gistruttoria.repository.dao.EsitoMantenimentoPascoloDao;
import it.tndigitale.a4gistruttoria.repository.dao.PascoloParticellaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtPascoloParticella;
import it.tndigitale.a4gistruttoria.repository.model.EsitoMantenimentoPascolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.DatiIstruttoreService;
import it.tndigitale.a4gistruttoria.service.DomandeService;
import it.tndigitale.a4gistruttoria.service.businesslogic.PassoDatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.support.CalcoloAnomalieMantenimentoSupport;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.CalcoloSostegnoException;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

/**
 * Classe di servizio che gestisce il calcolo delle particelle al pascolo con anomalie mantenimento. 
 * Riferimento cartella https://drive.google.com/open?id=1PqLAzXMxXJU8SY4nx-uYjqlyafpm5mO9 
 * Dettaglio Algoritmo 1.2_ANOMALIE_MANTENIMENTO
 *
 * @author Alessandro Cuel
 * 
 */
@Service
public class PassoCalcoloAnomalieMantenimentoService extends PassoDatiElaborazioneIstruttoria {

	private static final Logger logger = LoggerFactory.getLogger(PassoCalcoloAnomalieMantenimentoService.class);

	@Autowired
	DomandeService domandaService;
	@Autowired
	DomandaUnicaDao daoDomanda;
	@Autowired
	PascoloParticellaDao pascoloParticellaDao;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	A4gistruttoriaConfigurazione configurazione;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private ComuniLimitrofiDao daoComuniLimitrofi;
	@Autowired
	private EsitoMantenimentoPascoloDao esitoMantenimentoPascoloDao;
	@Autowired
	private CalcoloAnomalieMantenimentoSupport calcoloAnomalieMantenimentoSupport;
	@Autowired
	private DatiIstruttoreService datiIstruttoreService;

	private final String PASCOLO_AZIENDALE = "999TN999";
	private final String ESITO_POSITIVO = "POSITIVO";
	private final String ESITO_NEGATIVO = "NEGATIVO";
	private final String DOC_PRESENTE = "PRESENTE";

	List<String> mancanzaDatiIstruttore = Arrays.asList("PASF_07 (IMP.CONT)", "PASF_10 (IMP.CONT)", "PASF_14 (IMP.CONT)");
	List<String> presenzaAnomalie = Arrays.asList("PASF_02 (MAN4)", "PASF_03 (MAN4)", "PASF_05 (MAN4)", "PASF_06 (MAN4)", "PASF_09 (MAN5)", "PASF_11 (MAN 1)", "PASF_13 (MAN 2)",
			"PASF_16 (MAN2 e MAN3)");

	// Rappresenta i coefficienti utilizzati nel calcolo delle uba della man4 a partire dal 2021
	private EnumMap<CodiceSpecie, Map<FasciaEta, Float>> coefficientiMan4PasUbaBovini2021 = new EnumMap<>(
			Map.ofEntries(
					Map.entry(CodiceSpecie.BOVINI, 
							Map.ofEntries(
								Map.entry(FasciaEta.MESI_0_6, 0.4F),
								Map.entry(FasciaEta.MESI_6_24, 0.6F),
								Map.entry(FasciaEta.OLTRE_24, 1F)
							)
					),
					Map.entry(CodiceSpecie.CAVALLI, 
							Map.ofEntries(
								Map.entry(FasciaEta.MESI_6_24, 1F),
								Map.entry(FasciaEta.OLTRE_24, 1F)
							)
					),
					Map.entry(CodiceSpecie.MULI, 
							Map.ofEntries(
								Map.entry(FasciaEta.MESI_6_24, 0.5F),
								Map.entry(FasciaEta.OLTRE_24, 0.5F)
							)
					),
					Map.entry(CodiceSpecie.ASINI, 
							Map.ofEntries(
								Map.entry(FasciaEta.MESI_6_24, 0.5F),
								Map.entry(FasciaEta.OLTRE_24, 0.5F)
							)
					),
					Map.entry(CodiceSpecie.BARDOTTI, 
							Map.ofEntries(
								Map.entry(FasciaEta.MESI_6_24, 0.5F),
								Map.entry(FasciaEta.OLTRE_24, 0.5F)
							)
					)
			));
	
	// Rappresenta i coefficienti utilizzati nel calcolo delle uba della man4 prima del 2021 (<= 2020)
	private EnumMap<CodiceSpecie, Map<FasciaEta, Float>> coefficientiMan4PasUbaBovini2020 = new EnumMap<>(
			Map.ofEntries(
					Map.entry(CodiceSpecie.BOVINI, 
							Map.ofEntries(
								Map.entry(FasciaEta.MESI_0_6, 1F),
								Map.entry(FasciaEta.MESI_6_24, 0.6F),
								Map.entry(FasciaEta.OLTRE_24, 0.4F)
							)
					),
					Map.entry(CodiceSpecie.CAVALLI, 
							Map.ofEntries(
								Map.entry(FasciaEta.MESI_6_24, 1F),
								Map.entry(FasciaEta.OLTRE_24, 1F)
							)
					),
					Map.entry(CodiceSpecie.MULI, 
							Map.ofEntries(
								Map.entry(FasciaEta.MESI_6_24, 1F),
								Map.entry(FasciaEta.OLTRE_24, 1F)
							)
					),
					Map.entry(CodiceSpecie.ASINI, 
							Map.ofEntries(
								Map.entry(FasciaEta.MESI_6_24, 1F),
								Map.entry(FasciaEta.OLTRE_24, 1F)
							)
					),
					Map.entry(CodiceSpecie.BARDOTTI, 
							Map.ofEntries(
								Map.entry(FasciaEta.MESI_6_24, 1F),
								Map.entry(FasciaEta.OLTRE_24, 1F)
							)
					)
			));
	
	@Override
	protected DatiPassoLavorazione elaboraPasso(DatiElaborazioneIstruttoria dati, MapVariabili variabiliCalcolo,
			HashMap<TipoControllo, EsitoControllo> mappaEsiti) {
		DatiPassoLavorazione passo = new DatiPassoLavorazione();
		passo.setIdTransizione(dati.getTransizione().getId());
		passo.setPasso(getPasso());
		DomandaUnicaModel domanda = dati.getIstruttoria().getDomandaUnicaModel();
		try {
			if (domandaService.isPascoliImpegnati(domanda.getId())) {
				List<String> listaEsiti = eseguiAlgoritmoCalcoloMan(dati, variabiliCalcolo, mappaEsiti);

				if (CollectionUtils.containsAny(listaEsiti, mancanzaDatiIstruttore)) {
					passo.setCodiceEsito(CodiceEsito.DUF_018.getCodiceEsito());
					passo.setEsito(false);
					mappaEsiti.put(TipoControllo.BRIDUSDC019_PresenzaMAN,
							new EsitoControllo(TipoControllo.BRIDUSDC019_PresenzaMAN, false));
					mappaEsiti.put(TipoControllo.BRIDUSDC067_PresenzaInfoIstruttoreMAN,
							new EsitoControllo(TipoControllo.BRIDUSDC067_PresenzaInfoIstruttoreMAN, false));
				} else if (CollectionUtils.containsAny(listaEsiti, presenzaAnomalie)) {
					// DUT_004 -> SI MAN
					passo.setCodiceEsito(CodiceEsito.DUT_004.getCodiceEsito());
					passo.setEsito(true);
					mappaEsiti.put(TipoControllo.BRIDUSDC019_PresenzaMAN,
							new EsitoControllo(TipoControllo.BRIDUSDC019_PresenzaMAN, true));
					mappaEsiti.put(TipoControllo.BRIDUSDC067_PresenzaInfoIstruttoreMAN,
							new EsitoControllo(TipoControllo.BRIDUSDC067_PresenzaInfoIstruttoreMAN, true));
				} else {
					// DUT_003 -> NO MAN
					passo.setCodiceEsito(CodiceEsito.DUT_003.getCodiceEsito());
					passo.setEsito(true);
					mappaEsiti.put(TipoControllo.BRIDUSDC019_PresenzaMAN,
							new EsitoControllo(TipoControllo.BRIDUSDC019_PresenzaMAN, false));
					mappaEsiti.put(TipoControllo.BRIDUSDC067_PresenzaInfoIstruttoreMAN,
							new EsitoControllo(TipoControllo.BRIDUSDC067_PresenzaInfoIstruttoreMAN, true));
				}

				// Memorizzo l'output per domanda
				// La variabile PFANOMMAN viene valorizzata dalle esecuzioni sui singoli pascoli
				List<VariabileCalcolo> variabiliPartColt = new ArrayList<>();
				if (variabiliCalcolo.get(TipoVariabile.PFANOMMAN) != null && variabiliCalcolo.get(TipoVariabile.PFANOMMAN).getValList() != null) {
					variabiliPartColt.add(variabiliCalcolo.get(TipoVariabile.PFANOMMAN));
				}
				if (variabiliCalcolo.get(TipoVariabile.PFPASCOLO) != null && variabiliCalcolo.get(TipoVariabile.PFPASCOLO).getValList() != null) {
					variabiliPartColt.add(variabiliCalcolo.get(TipoVariabile.PFPASCOLO));
				}
				passo.getDatiSintesi().setVariabiliCalcolo(variabiliPartColt);
				passo.getDatiSintesi().setVariabiliParticellaColtura(variabiliPartColt);

				// Memorizzo lista esiti controlli effettuati
				mappaEsiti.values().stream().map(e -> passo.getDatiSintesi().getEsitiControlli().add(e));
				
			} else {
				passo.setCodiceEsito(CodiceEsito.DUT_002.getCodiceEsito());
				passo.setEsito(true);
				
			}
		} catch (Exception e) {
			logger.error("Errore nel calcolo delle anomalie di Mantenimento per la domanda: {} ", domanda.getId(), e);
			passo.setCodiceEsito(CodiceEsito.DUE.getCodiceEsito());
			passo.setEsito(false);
		}
		
		// Memorizzo lista esiti controlli effettuati
		mappaEsiti.values().forEach(e -> {
			passo.getDatiSintesi().getEsitiControlli().add(e);
		});

		return passo;
	}

	@Override
	public TipologiaPassoTransizione getPasso() {
		return TipologiaPassoTransizione.ANOMALIE_MANTENIMENTO;
	}

	
	// Esegue l'algoritmo di dettaglio per il calcolo della anomalie di mantenimento
	// vedere ..AlberiDecisionali/1.2.1_ANOMALIE_MANTENIMENTO_DETTAGLIO.png
	private List<String> eseguiAlgoritmoCalcoloMan(DatiElaborazioneIstruttoria dati,
												   MapVariabili variabiliCalcolo,
												   HashMap<TipoControllo, EsitoControllo> mappaEsiti) throws CalcoloSostegnoException {
		List<String> listaEsitiMan = new ArrayList<String>();
		IstruttoriaModel istruttoria = dati.getIstruttoria(); 
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		DatiCalcoloPascolo datiCalcoloPascolo = null;
		DatiSintesi datiSintesiPascolo = null;
		for (A4gtPascoloParticella pascolo : domanda.getA4gtPascoloParticellas()) {
			datiCalcoloPascolo = new DatiCalcoloPascolo();
			datiSintesiPascolo = new DatiSintesi();

			// resetto le variabili relative ai valori inserite dall'istruttore
			variabiliCalcolo.remove(TipoVariabile.PASSUPDETIST);
			variabiliCalcolo.remove(TipoVariabile.PASSUPELE);
			variabiliCalcolo.remove(TipoVariabile.PASSUPSIGECO);

			String esitoMan = calcolaEsitoMan(dati, pascolo, variabiliCalcolo, datiCalcoloPascolo, mappaEsiti);

			datiSintesiPascolo.setVariabiliCalcolo(datiCalcoloPascolo.getVariabiliOutput());

			 datiSintesiPascolo.getEsitiControlli().addAll(mappaEsiti.values().stream()
				.filter(e -> e.getPascolo().equals(pascolo.getDescPascolo()))
				.collect(Collectors.toList())
			 );

			datiSintesiPascolo.getEsitiControlli().forEach(e -> {
				mappaEsiti.remove(e.getTipoControllo());
			});

			pascoloParticellaDao.save(pascolo);

			EsitoMantenimentoPascolo esitoMantenimentoPascolo =
					calcoloAnomalieMantenimentoSupport.from(esitoMan, istruttoria, datiCalcoloPascolo.getVariabiliInput(), datiSintesiPascolo, pascolo);

			esitoMantenimentoPascoloDao.save(esitoMantenimentoPascolo);

			listaEsitiMan.add(esitoMan);

		}

		return listaEsitiMan;
	}


	private String calcolaEsitoMan(DatiElaborazioneIstruttoria dati,
								   A4gtPascoloParticella pascolo,
								   MapVariabili variabiliCalcolo,
								   DatiCalcoloPascolo datiCalcoloPascolo,
								   HashMap<TipoControllo, EsitoControllo> mappaEsiti) throws CalcoloSostegnoException {
		IstruttoriaModel istruttoria = dati.getTransizione().getIstruttoria();
		DatiIstruttoriaPascoli datiIstruttore = leggiDatiIstruttoria(istruttoria, pascolo.getDescPascolo());
		Boolean isPascoloAziendale = pascolo.getCodPascolo().replaceAll(" ", "").equalsIgnoreCase(PASCOLO_AZIENDALE);
		EsitoControllo esitoTipoPascolo = new EsitoControllo(TipoControllo.BRIDUSDC013_tipoPascolo, !isPascoloAziendale);
		esitoTipoPascolo.setPascolo(pascolo.getDescPascolo());
		mappaEsiti.put(TipoControllo.BRIDUSDC013_tipoPascolo, esitoTipoPascolo);
		return (isPascoloAziendale)? valutaPascoloAziendale(istruttoria, pascolo, datiIstruttore, datiCalcoloPascolo, mappaEsiti, variabiliCalcolo):
									 valutaPascoloDiMalga(istruttoria, pascolo, datiIstruttore, datiCalcoloPascolo, mappaEsiti, variabiliCalcolo);
	}

	private String valutaPascoloDiMalga(IstruttoriaModel istruttoria, A4gtPascoloParticella pascolo,
			DatiIstruttoriaPascoli datiIstruttore, DatiCalcoloPascolo datiCalcoloPascolo,
			HashMap<TipoControllo, EsitoControllo> mappaEsiti, MapVariabili variabiliCalcolo) throws CalcoloSostegnoException {

		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		List<VariabileCalcolo> variabiliOutput = datiCalcoloPascolo.getVariabiliOutput();
		List<VariabileCalcolo> variabiliInput = datiCalcoloPascolo.getVariabiliInput();
		
		List<ConsistenzaPascolo2015Dto> listConsistenzaPascolo = getConsistenzaPascoloDaCacheBdn(domanda.getCampagna(), pascolo.getCodPascolo());

		List<MovimentazionePascoloOviniDto> listMovimentiPascolo = getMovimentazionePascoloOviniDaCacheBdn(domanda.getCampagna(), pascolo.getCodPascolo());

		if (pascolo.getCodPascolo().substring(3, 5).equals("TN")) {

			EsitoControllo esitoVerificaProvinciaPascolo = new EsitoControllo(TipoControllo.BRIDUSDC061_VerificaPascoloProvinciaTN, true);
			esitoVerificaProvinciaPascolo.setPascolo(pascolo.getDescPascolo());
			mappaEsiti.put(TipoControllo.BRIDUSDC061_VerificaPascoloProvinciaTN, esitoVerificaProvinciaPascolo);

			// "Servizio Consistenza_Al_Pascolo_PAC2015 (CODI_FISC_RESP) interrogato con il Codice pascolo e la campagna"
			if (listConsistenzaPascolo != null && !listConsistenzaPascolo.isEmpty()) {

				List<ConsistenzaPascolo2015Dto> listBovini = listConsistenzaPascolo.stream().filter(
						b -> (b.getSpecie().equals("BOVINI") || b.getSpecie().equals("MULI") || b.getSpecie().equals("ASINI") || b.getSpecie().equals("CAVALLI") || b.getSpecie().equals("BARDOTTI")))
						.collect(Collectors.toList());

				List<ConsistenzaPascolo2015Dto> listOvini = listConsistenzaPascolo.stream().filter(b -> (b.getSpecie().equals("OVINI") || b.getSpecie().equals("CAPRINI")))
						.collect(Collectors.toList());

				// Verifica MAN4
				if ((listConsistenzaPascolo.get(0).getCodiceFiscaleResponsabile() != null
						&& listConsistenzaPascolo.get(0).getCodiceFiscaleResponsabile().equalsIgnoreCase(domanda.getCuaaIntestatario()))
						|| (listConsistenzaPascolo.get(0).getCodiceFiscaleResponsabile() == null && datiIstruttore != null && datiIstruttore.getCuaaResponsabile() != null
								&& !datiIstruttore.getCuaaResponsabile().isEmpty() && datiIstruttore.getCuaaResponsabile().equalsIgnoreCase(domanda.getCuaaIntestatario()))) {
					return eseguiAlgoritmoMan4(datiIstruttore, listBovini, listOvini, listMovimentiPascolo, true, pascolo, mappaEsiti, datiCalcoloPascolo, variabiliCalcolo);

				} else {

					EsitoControllo esitoVerificaMan4Titolare = new EsitoControllo(TipoControllo.BRIDUSDC065_VerificaMan4Titolare, false);
					esitoVerificaMan4Titolare.setPascolo(pascolo.getDescPascolo());
					mappaEsiti.put(TipoControllo.BRIDUSDC065_VerificaMan4Titolare, esitoVerificaMan4Titolare);

					return eseguiAlgoritmoMan4Titolare(pascolo, true, datiCalcoloPascolo, variabiliCalcolo);
				}

			} else {

				EsitoControllo esitoPresenzaDato = new EsitoControllo(TipoControllo.BRIDUSDC064_VerificaResponsabilePascolo, false);
				esitoPresenzaDato.setPascolo(pascolo.getDescPascolo());
				mappaEsiti.put(TipoControllo.BRIDUSDC064_VerificaResponsabilePascolo, esitoPresenzaDato);

				if (datiIstruttore == null || datiIstruttore.getCuaaResponsabile() == null || datiIstruttore.getCuaaResponsabile().equals("")) {

					EsitoControllo esitoPresenzaDatoIstruttore = new EsitoControllo(TipoControllo.BRIDUSDC066_PresenzaInfoIstruttoreMan4, false);
					esitoPresenzaDatoIstruttore.setPascolo(pascolo.getDescPascolo());
					mappaEsiti.put(TipoControllo.BRIDUSDC066_PresenzaInfoIstruttoreMan4, esitoPresenzaDatoIstruttore);

					return CodiceEsito.PASF_07.getCodiceEsito();
				} else if (datiIstruttore.getCuaaResponsabile().equalsIgnoreCase(domanda.getCuaaIntestatario()))
					return eseguiAlgoritmoMan4(datiIstruttore, new ArrayList<>(), new ArrayList<>(), listMovimentiPascolo, false, pascolo, mappaEsiti, datiCalcoloPascolo, variabiliCalcolo);
				else {

					EsitoControllo esitoVerificaMan4Titolare = new EsitoControllo(TipoControllo.BRIDUSDC065_VerificaMan4Titolare, false);
					esitoVerificaMan4Titolare.setPascolo(pascolo.getDescPascolo());
					mappaEsiti.put(TipoControllo.BRIDUSDC065_VerificaMan4Titolare, esitoVerificaMan4Titolare);

					return eseguiAlgoritmoMan4Titolare(pascolo, false, datiCalcoloPascolo, variabiliCalcolo);
				}

			}

		} else {

			// Verifica MAN5

			EsitoControllo esitoVerificaProvinciaPascolo = new EsitoControllo(TipoControllo.BRIDUSDC061_VerificaPascoloProvinciaTN, false);
			esitoVerificaProvinciaPascolo.setPascolo(pascolo.getDescPascolo());
			mappaEsiti.put(TipoControllo.BRIDUSDC061_VerificaPascoloProvinciaTN, esitoVerificaProvinciaPascolo);

			// Valorizzazione variabili di calcolo a livello di PASCOLO

			// PASSUPELE
			List<ParticellaColtura> listPartElePascolo = variabiliCalcolo.get(TipoVariabile.PASSUPELEMAP).getValMap().get(pascolo.getDescPascolo());
			Float supGisTotale = listPartElePascolo.stream().collect(Collectors.summingDouble(p -> p.getValNum())).floatValue();
			variabiliCalcolo.setVal(TipoVariabile.PASSUPELE, BigDecimal.valueOf(supGisTotale));

			// PASSUPSIGECO
			List<ParticellaColtura> listPartSigecoPascolo = variabiliCalcolo.get(TipoVariabile.PASSUPSIGEMAP).getValMap().get(pascolo.getDescPascolo());
			Float supSigecoTotale = listPartSigecoPascolo.stream().collect(Collectors.summingDouble(p -> p.getValNum())).floatValue();
			variabiliCalcolo.setVal(TipoVariabile.PASSUPSIGECO, BigDecimal.valueOf(supSigecoTotale));

			// PASSUPDET
			if (datiIstruttore != null && datiIstruttore.getSuperficieDeterminata() != null) {
				variabiliCalcolo.setVal(TipoVariabile.PASSUPDETIST, datiIstruttore.getSuperficieDeterminata()); // dato istruttore
				variabiliCalcolo.setVal(TipoVariabile.PASSUPDET, datiIstruttore.getSuperficieDeterminata()); // NVL(PASSUPDETIST;NVL(PASSUPSIGECO;PASSUPELE))
			} else if (variabiliCalcolo.get(TipoVariabile.ISCAMP).getValBoolean())
				variabiliCalcolo.setVal(TipoVariabile.PASSUPDET, variabiliCalcolo.get(TipoVariabile.PASSUPSIGECO).getValNumber());
			else
				variabiliCalcolo.setVal(TipoVariabile.PASSUPDET, variabiliCalcolo.get(TipoVariabile.PASSUPELE).getValNumber());

			// Impostazione delle variabili di input da persistere
			variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASSUPELE));
			variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASSUPSIGECO));
			if (variabiliCalcolo.get(TipoVariabile.PASSUPDETIST) != null)
				variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASSUPDETIST));

			// Impostazione delle variabile di output da persistere
			variabiliOutput.add(variabiliCalcolo.get(TipoVariabile.PASSUPDET));

			List<ParticellaColtura> listParticellePascolo = new ArrayList<>();
			if (variabiliCalcolo.get(TipoVariabile.ISCAMP).getValBoolean())
				listParticellePascolo = listPartSigecoPascolo;
			else
				listParticellePascolo = listPartElePascolo;

			ArrayList<ParticellaColtura> listPfAnomMan = new ArrayList<>();
			ArrayList<ParticellaColtura> listPfPascolo = new ArrayList<>();
			VariabileCalcolo pfAnomManPascolo;
			VariabileCalcolo pfPascolo;
			VariabileCalcolo pfAnomMan;
			VariabileCalcolo pfPascoloDom;

			// Esito presenza dato istruttore
			if (datiIstruttore == null || datiIstruttore.getEsitoControlloMantenimento() == null || datiIstruttore.getEsitoControlloMantenimento().isEmpty()) {
				EsitoControllo esitoVerificaDatiIstruttoreMan5 = new EsitoControllo(TipoControllo.BRIDUSDC062_PresenzaInfoIstruttoreMan5, false);
				esitoVerificaDatiIstruttoreMan5.setPascolo(pascolo.getDescPascolo());
				mappaEsiti.put(TipoControllo.BRIDUSDC062_PresenzaInfoIstruttoreMan5, esitoVerificaDatiIstruttoreMan5);

				listPfAnomMan = getListPfAnomMan(listParticellePascolo, true);
				listPfPascolo = getPfPascolo(pascolo);

				pfAnomManPascolo = new VariabileCalcolo(TipoVariabile.PFANOMMAN, listPfAnomMan);
				pfPascolo = new VariabileCalcolo(TipoVariabile.PFPASCOLO, listPfPascolo);

				// Setto le variabili ParticellaColtura nell'output del singolo pascolo
				variabiliOutput.add(pfAnomManPascolo);
				variabiliOutput.add(pfPascolo);

				// Aggiungo le particelle alla variabile PFANOMMAN di domanda
				pfAnomMan = variabiliCalcolo.get(TipoVariabile.PFANOMMAN);
				if (pfAnomMan != null) {
					ArrayList<ParticellaColtura> listPc = pfAnomMan.getValList();
					listPc.addAll(listPfAnomMan);
					variabiliCalcolo.setVal(TipoVariabile.PFANOMMAN, listPc);
				} else {
					variabiliCalcolo.setVal(TipoVariabile.PFANOMMAN, listPfAnomMan);
				}

				// Aggiungo le particelle alla variabile PFPASCOLO di domanda
				pfPascoloDom = variabiliCalcolo.get(TipoVariabile.PFPASCOLO);
				if (pfPascoloDom != null) {
					ArrayList<ParticellaColtura> listPcPascolo = pfPascoloDom.getValList();
					listPcPascolo.addAll(listPfPascolo);
					variabiliCalcolo.setVal(TipoVariabile.PFPASCOLO, listPcPascolo);
				} else {
					variabiliCalcolo.setVal(TipoVariabile.PFPASCOLO, listPfPascolo);
				}

				return CodiceEsito.PASF_10.getCodiceEsito();
			}

			switch (datiIstruttore.getEsitoControlloMantenimento()) {

			case ESITO_POSITIVO:

				listPfAnomMan = getListPfAnomMan(listParticellePascolo, false);
				listPfPascolo = getPfPascolo(pascolo);

				pfAnomManPascolo = new VariabileCalcolo(TipoVariabile.PFANOMMAN, listPfAnomMan);
				pfPascolo = new VariabileCalcolo(TipoVariabile.PFPASCOLO, listPfPascolo);

				// Setto le variabili ParticellaColtura nell'output del singolo pascolo
				variabiliOutput.add(pfAnomManPascolo);
				variabiliOutput.add(pfPascolo);

				// Aggiungo le particelle alla variabile PFANOMMAN di domanda
				pfAnomMan = variabiliCalcolo.get(TipoVariabile.PFANOMMAN);
				if (pfAnomMan != null) {
					ArrayList<ParticellaColtura> listPc = pfAnomMan.getValList();
					listPc.addAll(listPfAnomMan);
					variabiliCalcolo.setVal(TipoVariabile.PFANOMMAN, listPc);
				} else {
					variabiliCalcolo.setVal(TipoVariabile.PFANOMMAN, listPfAnomMan);
				}

				// Aggiungo le particelle alla variabile PFPASCOLO di domanda
				pfPascoloDom = variabiliCalcolo.get(TipoVariabile.PFPASCOLO);
				if (pfPascoloDom != null) {
					ArrayList<ParticellaColtura> listPcPascolo = pfPascoloDom.getValList();
					listPcPascolo.addAll(listPfPascolo);
					variabiliCalcolo.setVal(TipoVariabile.PFPASCOLO, listPcPascolo);
				} else {
					variabiliCalcolo.setVal(TipoVariabile.PFPASCOLO, listPfPascolo);
				}

				return CodiceEsito.PASF_08.getCodiceEsito();

			case ESITO_NEGATIVO:

				EsitoControllo esitoMan5 = new EsitoControllo(TipoControllo.BRIDUSDC063_EsitoMAN5, false);
				esitoMan5.setPascolo(pascolo.getDescPascolo());
				mappaEsiti.put(TipoControllo.BRIDUSDC063_EsitoMAN5, esitoMan5);

				listPfAnomMan = getListPfAnomMan(listParticellePascolo, true);
				listPfPascolo = getPfPascolo(pascolo);

				pfAnomManPascolo = new VariabileCalcolo(TipoVariabile.PFANOMMAN, listPfAnomMan);
				pfPascolo = new VariabileCalcolo(TipoVariabile.PFPASCOLO, listPfPascolo);

				// Setto le variabili ParticellaColtura nell'output del singolo pascolo
				variabiliOutput.add(pfAnomManPascolo);
				variabiliOutput.add(pfPascolo);

				// Aggiungo le particelle alla variabile PFANOMMAN di domanda
				pfAnomMan = variabiliCalcolo.get(TipoVariabile.PFANOMMAN);
				if (pfAnomMan != null) {
					ArrayList<ParticellaColtura> listPc = pfAnomMan.getValList();
					listPc.addAll(listPfAnomMan);
					variabiliCalcolo.setVal(TipoVariabile.PFANOMMAN, listPc);
				} else {
					variabiliCalcolo.setVal(TipoVariabile.PFANOMMAN, listPfAnomMan);
				}

				// Aggiungo le particelle alla variabile PFPASCOLO di domanda
				pfPascoloDom = variabiliCalcolo.get(TipoVariabile.PFPASCOLO);
				if (pfPascoloDom != null) {
					ArrayList<ParticellaColtura> listPcPascolo = pfPascoloDom.getValList();
					listPcPascolo.addAll(listPfPascolo);
					variabiliCalcolo.setVal(TipoVariabile.PFPASCOLO, listPcPascolo);
				} else {
					variabiliCalcolo.setVal(TipoVariabile.PFPASCOLO, listPfPascolo);
				}
				return CodiceEsito.PASF_09.getCodiceEsito();
			default:
				return "";

			}

		}
	}

	private String valutaPascoloAziendale(IstruttoriaModel istruttoria, A4gtPascoloParticella pascolo,
			DatiIstruttoriaPascoli datiIstruttore, DatiCalcoloPascolo datiCalcoloPascolo,
			HashMap<TipoControllo, EsitoControllo> mappaEsiti, MapVariabili variabiliCalcolo)
			throws CalcoloSostegnoException {

		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		List<ConsistenzaAllevamentoDto> listConsistenzaAllevamento = getConsistenzaAllevamentoDaCacheBdn(domanda.getCampagna(), domanda.getCuaaIntestatario());
		EsitoControllo esitoMan1 = new EsitoControllo(TipoControllo.BRIDUSDC015_mantenimentoSupMan1, (listConsistenzaAllevamento != null && !listConsistenzaAllevamento.isEmpty()));
		esitoMan1.setPascolo(pascolo.getDescPascolo());
		mappaEsiti.put(TipoControllo.BRIDUSDC015_mantenimentoSupMan1, esitoMan1);
		List<VariabileCalcolo> variabiliOutput = datiCalcoloPascolo.getVariabiliOutput();
		if (listConsistenzaAllevamento == null || listConsistenzaAllevamento.isEmpty()) {
			// Algoritmo MAN1
			return eseguiAlgoritmoMan1(pascolo, datiCalcoloPascolo, variabiliCalcolo);
		} else {

			String codiceEsito;
			boolean esitoMan;

			// Algoritmo MAN2
			eseguiAlgoritmoMan2(listConsistenzaAllevamento, pascolo, datiIstruttore, datiCalcoloPascolo, variabiliCalcolo);

			// BRIDUSDC016 = (IF PASRUHMAN2 >= 0,20 THEN OK ELSE KO)
			EsitoControllo esitoMan2 = new EsitoControllo(TipoControllo.BRIDUSDC016_mantenimentoSupMan2,
					variabiliCalcolo.get(TipoVariabile.PASRUHMAN2).getValNumber().compareTo(BigDecimal.valueOf(0.20)) >= 0);
			esitoMan2.setPascolo(pascolo.getDescPascolo());
			mappaEsiti.put(TipoControllo.BRIDUSDC016_mantenimentoSupMan2, esitoMan2);

			if (mappaEsiti.get(TipoControllo.BRIDUSDC016_mantenimentoSupMan2).getEsito()) {
				codiceEsito = CodiceEsito.PASF_12.getCodiceEsito();
				esitoMan = false;
			} else {
				// BRIDUSDC068
				EsitoControllo esitoAllevamentoFuoriComune = new EsitoControllo(TipoControllo.BRIDUSDC068_presenzaAllevamentiFuoriComune,
						!variabiliCalcolo.get(TipoVariabile.PASALLLON).getValString().isEmpty());
				esitoAllevamentoFuoriComune.setPascolo(pascolo.getDescPascolo());
				mappaEsiti.put(TipoControllo.BRIDUSDC068_presenzaAllevamentiFuoriComune, esitoAllevamentoFuoriComune);

				if (!mappaEsiti.get(TipoControllo.BRIDUSDC068_presenzaAllevamentiFuoriComune).getEsito()) {
					codiceEsito = CodiceEsito.PASF_13.getCodiceEsito();
					esitoMan = true;
				} else {
					// BRIDUSDC069
					EsitoControllo esitoDocTrasporto = new EsitoControllo(TipoControllo.BRIDUSDC069_presenzaDatoDocumentazioneTrasporto,
							datiIstruttore != null && datiIstruttore.getVerificaDocumentazione() != null && !datiIstruttore.getVerificaDocumentazione().equals(""));
					esitoDocTrasporto.setPascolo(pascolo.getDescPascolo());
					mappaEsiti.put(TipoControllo.BRIDUSDC069_presenzaDatoDocumentazioneTrasporto, esitoDocTrasporto);

					if (!mappaEsiti.get(TipoControllo.BRIDUSDC069_presenzaDatoDocumentazioneTrasporto).getEsito()) {
						codiceEsito = CodiceEsito.PASF_14.getCodiceEsito();
						esitoMan = true;
					} else {
						// Algoritmo MAN3
						eseguiAlgoritmoMan3(datiIstruttore, datiCalcoloPascolo, variabiliCalcolo);

						EsitoControllo esitoMan3 = new EsitoControllo(TipoControllo.BRIDUSDC017_mantenimentoSupMan3,
								variabiliCalcolo.get(TipoVariabile.PASRUHMAN3).getValNumber().compareTo(BigDecimal.valueOf(0.20)) >= 0
										&& variabiliCalcolo.get(TipoVariabile.PASTRASPORTO).getValString().equalsIgnoreCase(DOC_PRESENTE));
						esitoMan3.setPascolo(pascolo.getDescPascolo());
						mappaEsiti.put(TipoControllo.BRIDUSDC017_mantenimentoSupMan3, esitoMan3);

						if (mappaEsiti.get(TipoControllo.BRIDUSDC017_mantenimentoSupMan3).getEsito()) {
							codiceEsito = CodiceEsito.PASF_15.getCodiceEsito();
							esitoMan = false;
						} else {
							codiceEsito = CodiceEsito.PASF_16.getCodiceEsito();
							esitoMan = true;
						}
					}
				}

			}

			List<ParticellaColtura> listParticelleMan = new ArrayList<>();
			if (variabiliCalcolo.get(TipoVariabile.ISCAMP).getValBoolean())
				listParticelleMan = variabiliCalcolo.get(TipoVariabile.PASSUPSIGEMAP).getValMap().get(pascolo.getDescPascolo());
			else
				listParticelleMan = variabiliCalcolo.get(TipoVariabile.PASSUPELEMAP).getValMap().get(pascolo.getDescPascolo());

			// PFANOMMAN - IF $PASCOLO PASRUHMAN2 >= 0,20 THEN NO ELSE SI
			ArrayList<ParticellaColtura> listPfAnomMan = getListPfAnomMan(listParticelleMan, esitoMan);
			// PFPASCOLO
			ArrayList<ParticellaColtura> listPfPascolo = getPfPascolo(pascolo);

			VariabileCalcolo pfAnomManPascolo = new VariabileCalcolo(TipoVariabile.PFANOMMAN, listPfAnomMan);
			VariabileCalcolo pfPascolo = new VariabileCalcolo(TipoVariabile.PFPASCOLO, listPfPascolo);

			// Setto le variabili ParticellaColtura nell'output del singolo pascolo
			variabiliOutput.add(pfAnomManPascolo);
			variabiliOutput.add(pfPascolo);

			// Aggiungo le particelle alla variabile PFANOMMAN di domanda
			VariabileCalcolo pfAnomMan = variabiliCalcolo.get(TipoVariabile.PFANOMMAN);
			if (pfAnomMan != null) {
				ArrayList<ParticellaColtura> listPc = pfAnomMan.getValList();
				listPc.addAll(listPfAnomMan);
				variabiliCalcolo.setVal(TipoVariabile.PFANOMMAN, listPc);
			} else {
				variabiliCalcolo.setVal(TipoVariabile.PFANOMMAN, listPfAnomMan);
			}

			// Aggiungo le particelle alla variabile PFPASCOLO di domanda
			VariabileCalcolo pfPascoloDom = variabiliCalcolo.get(TipoVariabile.PFPASCOLO);
			if (pfPascoloDom != null) {
				ArrayList<ParticellaColtura> listPcPascolo = pfPascoloDom.getValList();
				listPcPascolo.addAll(listPfPascolo);
				variabiliCalcolo.setVal(TipoVariabile.PFPASCOLO, listPcPascolo);
			} else {
				variabiliCalcolo.setVal(TipoVariabile.PFPASCOLO, listPfPascolo);
			}

			return codiceEsito;
		}
	}

	/**
	 * Algoritmo MAN1
	 *
	 * @param pascolo
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private String eseguiAlgoritmoMan1(A4gtPascoloParticella pascolo, DatiCalcoloPascolo datiCalcoloPascolo, MapVariabili variabiliCalcolo) throws CalcoloSostegnoException {

		List<VariabileCalcolo> variabiliOutput = datiCalcoloPascolo.getVariabiliOutput();
		List<ParticellaColtura> listParticellePascolo = new ArrayList<>();
		if (variabiliCalcolo.get(TipoVariabile.ISCAMP).getValBoolean())
			listParticellePascolo = variabiliCalcolo.get(TipoVariabile.PASSUPSIGEMAP).getValMap().get(pascolo.getDescPascolo());
		else
			listParticellePascolo = variabiliCalcolo.get(TipoVariabile.PASSUPELEMAP).getValMap().get(pascolo.getDescPascolo());

		// Il boolean HasMan viene valorizzato a True in quanto il metodo viene eseguito sempre e solo se si verificano le condizioni di presenza MAN
		ArrayList<ParticellaColtura> listPfAnomMan = getListPfAnomMan(listParticellePascolo, true);
		ArrayList<ParticellaColtura> listPfPascolo = getPfPascolo(pascolo);

		VariabileCalcolo pfAnomManPascolo = new VariabileCalcolo(TipoVariabile.PFANOMMAN, listPfAnomMan);
		VariabileCalcolo pfPascolo = new VariabileCalcolo(TipoVariabile.PFPASCOLO, listPfPascolo);

		// Setto le variabili ParticellaColtura nell'output del singolo pascolo
		variabiliOutput.add(pfAnomManPascolo);
		variabiliOutput.add(pfPascolo);

		// Aggiungo le particelle alla variabile PFANOMMAN di domanda
		VariabileCalcolo pfAnomMan = variabiliCalcolo.get(TipoVariabile.PFANOMMAN);
		if (pfAnomMan != null) {
			ArrayList<ParticellaColtura> listPc = pfAnomMan.getValList();
			listPc.addAll(listPfAnomMan);
			variabiliCalcolo.setVal(TipoVariabile.PFANOMMAN, listPc);
		} else {
			variabiliCalcolo.setVal(TipoVariabile.PFANOMMAN, listPfAnomMan);
		}

		// Aggiungo le particelle alla variabile PFPASCOLO di domanda
		VariabileCalcolo pfPascoloDom = variabiliCalcolo.get(TipoVariabile.PFPASCOLO);
		if (pfPascoloDom != null) {
			ArrayList<ParticellaColtura> listPcPascolo = pfPascoloDom.getValList();
			listPcPascolo.addAll(listPfPascolo);
			variabiliCalcolo.setVal(TipoVariabile.PFPASCOLO, listPcPascolo);
		} else {
			variabiliCalcolo.setVal(TipoVariabile.PFPASCOLO, listPfPascolo);
		}

		return CodiceEsito.PASF_11.getCodiceEsito();
	}

	/**
	 * Algoritmo MAN2
	 *
	 * @param listConsistenzaAllevamento
	 * @param pascolo
	 * @param datiIstruttoriaPascoli
	 * @return
	 * @throws ParseException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private void eseguiAlgoritmoMan2(List<ConsistenzaAllevamentoDto> listConsistenzaAllevamento,
			A4gtPascoloParticella pascolo, DatiIstruttoriaPascoli datiIstruttoriaPascoli,
			DatiCalcoloPascolo datiCalcoloPascolo, MapVariabili variabiliCalcolo)
			throws CalcoloSostegnoException {

		List<VariabileCalcolo> variabiliOutput = datiCalcoloPascolo.getVariabiliOutput();
		List<VariabileCalcolo> variabiliInput = datiCalcoloPascolo.getVariabiliInput();
		
		List<ParticellaColtura> listParticellePascolo;
		try {
			listParticellePascolo = mapper.readValue(pascolo.getInfoPartPascolo(), new TypeReference<List<ParticellaColtura>>() {
			});
		} catch (IOException e) {
			logger.error("Errore leggendo le informazioni di particella pascolo del pascolo {} - {} ", pascolo.getCodPascolo(), pascolo.getId(), e);
			throw new CalcoloSostegnoException("Errore leggendo le informazioni di particella pascolo del pascolo " + pascolo.getId(), e);
		}

		// Recupero i codici_comune per i Comuni afferenti al pascolo
		Set<BigDecimal> setCodComuni = new HashSet<>();
		listParticellePascolo.stream().collect(Collectors.groupingBy(ParticellaColtura::getValString)).keySet().stream().forEach(c -> {
			setCodComuni.add(daoComuniLimitrofi.getCodComuneByCodNazionale(c));
		});

		// Recupero la lista dei codici_comune dei Comuni limitrofi ai Comuni del pascolo.
		Set<BigDecimal> codComuniLimitrofi = new HashSet<>();
		codComuniLimitrofi.addAll(daoComuniLimitrofi.getListCodComuniLimitrofi(setCodComuni));

		// Aggiungo ai comuni limitrofi i comuni del Pascolo
		codComuniLimitrofi.addAll(setCodComuni);

		Date now = new Date();
		// Filtro la lista considerando solo i record con data fine attiva e afferenti al Comune del Pascolo (o Comuni limitrofi)
		List<ConsistenzaAllevamentoDto> listAllevamentoComuneAmm = listConsistenzaAllevamento.stream()
				.filter(p -> 
					((now.compareTo(p.getDataFine()) < 1) && 
						(codComuniLimitrofi.contains(new BigDecimal(p.getCodiceComune())))))
				.collect(Collectors.toList());

		BigDecimal numeroUbaAllevamento = BigDecimal.ZERO;
		StringBuilder allLim = new StringBuilder("");
		for (ConsistenzaAllevamentoDto a : listAllevamentoComuneAmm) {
			// PSALLLIM
			if (allLim.indexOf(a.getCodiAsll()) == -1)
				allLim.append(a.getCodiAsll()).append(", ");

			// PASUBAALLLIM
			BigDecimal numUba = BigDecimal.ZERO;
			if (a.getNumeCamp() >= 2021L) {
				if (a.getCodiSpec().equals("0121")) {
					logger.debug("PASUBAALLLIM anno >= 2021, getConsCapi06: {}, getConsCapi624: {}, getConsCapiOver24: {}", a.getConsCapi06(), a.getConsCapi624(), a.getConsCapiOver24() );
					BigDecimal numCapi0_6 = BigDecimal.valueOf(a.getConsCapi06()).multiply(BigDecimal.valueOf(0.4));
					BigDecimal numCapi6_24 = BigDecimal.valueOf(a.getConsCapi624()).multiply(BigDecimal.valueOf(0.6));
					BigDecimal numCapiOver24 = BigDecimal.valueOf(a.getConsCapiOver24());
					numUba = numCapi0_6.add(numCapi6_24).add(numCapiOver24);
					logger.debug("PASUBAALLLIM anno >= 2021, 0_6: {}, 6_24: {}, over24: {}", numCapi0_6, numCapi6_24, numCapiOver24 );
					logger.debug("PASUBAALLLIM anno >= 2021, numero Uba: {}", numUba);
				} else if (a.getCodiSpec().equals("0124") || a.getCodiSpec().equals("0125")) {
					numUba = BigDecimal.valueOf(a.getConsTota()).multiply(BigDecimal.valueOf(0.15));
				} else if (a.getCodiSpec().equals("0126")) {
					numUba = BigDecimal.valueOf(a.getConsCapi624()).add(BigDecimal.valueOf(a.getConsCapiOver24()));
				} else if (a.getCodiSpec().equals("0147") || a.getCodiSpec().equals("0148") || a.getCodiSpec().equals("0149")) {
					BigDecimal numCapi6_24 = BigDecimal.valueOf(a.getConsCapi624()).multiply(BigDecimal.valueOf(0.5));
					BigDecimal numCapiOver24 = BigDecimal.valueOf(a.getConsCapiOver24()).multiply(BigDecimal.valueOf(0.5));
					numUba = numCapi6_24.add(numCapiOver24);
				}
			} else { // campagna 2020 e precedenti
				if (a.getCodiSpec().equals("0121")) {
					BigDecimal numCapi0_6 = BigDecimal.valueOf(a.getConsCapi06()).multiply(BigDecimal.valueOf(0.4));
					BigDecimal numCapi6_24 = BigDecimal.valueOf(a.getConsCapi624()).multiply(BigDecimal.valueOf(0.6));
					BigDecimal numCapiOver24 = BigDecimal.valueOf(a.getConsCapiOver24());

					numUba = numCapi0_6.add(numCapi6_24).add(numCapiOver24);
				} else if (a.getCodiSpec().equals("0124") || a.getCodiSpec().equals("0125")) {

					numUba = BigDecimal.valueOf(a.getConsTota()).multiply(BigDecimal.valueOf(0.15));
				} else if (a.getCodiSpec().equals("0126") || a.getCodiSpec().equals("0149")) {
					numUba = BigDecimal.valueOf(a.getConsCapi624()).add(BigDecimal.valueOf(a.getConsCapiOver24()));
				}
			}
			numeroUbaAllevamento = numeroUbaAllevamento.add(numUba);
		}
		String allevamentiLimitrofi = allLim.toString();
		allevamentiLimitrofi = Optional.ofNullable(allevamentiLimitrofi).filter(s -> !s.isEmpty()).map(a -> a.substring(0, a.length() - 2)).orElse("");
		variabiliCalcolo.setVal(TipoVariabile.PASALLLIM, allevamentiLimitrofi);
		variabiliCalcolo.setVal(TipoVariabile.PASUBAALLLIM, numeroUbaAllevamento);
		// Filtro la lista considerando solo i record con data fine attiva e NON afferenti al Comune del Pascolo (o Comuni limitrofi)
		List<ConsistenzaAllevamentoDto> listAllevamentiLon = listConsistenzaAllevamento.stream()
				.filter(p -> (now.compareTo(p.getDataFine()) < 1) && !codComuniLimitrofi.contains(new BigDecimal(p.getCodiceComune())))
				.collect(Collectors.toList());

		BigDecimal numeroUbaAllevamentoLon = BigDecimal.ZERO;
		StringBuilder allLon = new StringBuilder();
		for (ConsistenzaAllevamentoDto a : listAllevamentiLon) {
			// PSALLLON
			if (allLon.indexOf(a.getCodiAsll()) == -1)
				allLon.append(a.getCodiAsll()).append(", ");

			// PSUBAALLLON
			BigDecimal numUba = BigDecimal.ZERO;
			
			// IDU-2021-04 Calcolo delle UBA - Evolutiva - dall'anno di campagna 2021 si tiene conto di un diverso calcolo per le UBA.
			if (a.getNumeCamp() >= 2021L) {
				if (a.getCodiSpec().equals("0121")) {
					BigDecimal numCapi0_6 = BigDecimal.valueOf(a.getConsCapi06()).multiply(BigDecimal.valueOf(0.4));
					BigDecimal numCapi6_24 = BigDecimal.valueOf(a.getConsCapi624()).multiply(BigDecimal.valueOf(0.6));
					BigDecimal numCapiOver24 = BigDecimal.valueOf(a.getConsCapiOver24());
					numUba = numCapi0_6.add(numCapi6_24).add(numCapiOver24);
				} else if (a.getCodiSpec().equals("0124") || a.getCodiSpec().equals("0125")) {
					numUba = BigDecimal.valueOf(a.getConsTota()).multiply(BigDecimal.valueOf(0.15));
				} else if (a.getCodiSpec().equals("0126")) {
					numUba = BigDecimal.valueOf(a.getConsCapi624()).add(BigDecimal.valueOf(a.getConsCapiOver24()));
				} else if (a.getCodiSpec().equals("0147") || a.getCodiSpec().equals("0148") || a.getCodiSpec().equals("0149")) {
					BigDecimal numCapi6_24 = BigDecimal.valueOf(a.getConsCapi624()).multiply(BigDecimal.valueOf(0.5));
					BigDecimal numCapiOver24 = BigDecimal.valueOf(a.getConsCapiOver24()).multiply(BigDecimal.valueOf(0.5));
					numUba = numCapi6_24.add(numCapiOver24);
				}
			} else { // campagna 2020 e precedenti
				if (a.getCodiSpec().equals("0121")) {
					BigDecimal numCapi0_6 = BigDecimal.valueOf(a.getConsCapi06()).multiply(BigDecimal.valueOf(0.4));
					BigDecimal numCapi6_24 = BigDecimal.valueOf(a.getConsCapi624()).multiply(BigDecimal.valueOf(0.6));
					BigDecimal numCapiOver24 = BigDecimal.valueOf(a.getConsCapiOver24());
	
					numUba = numCapi0_6.add(numCapi6_24).add(numCapiOver24);
				} else if (a.getCodiSpec().equals("0124") || a.getCodiSpec().equals("0125")) {
	
					numUba = BigDecimal.valueOf(a.getConsTota()).multiply(BigDecimal.valueOf(0.15));
				} else if (a.getCodiSpec().equals("0126") || a.getCodiSpec().equals("0149")) {
					numUba = BigDecimal.valueOf(a.getConsCapi624()).add(BigDecimal.valueOf(a.getConsCapiOver24()));
				}
			}
			numeroUbaAllevamentoLon = numeroUbaAllevamentoLon.add(numUba);
		}
		String allevamentiNonLimitrofi = allLon.toString();
		allevamentiNonLimitrofi = Optional.ofNullable(allevamentiNonLimitrofi).filter(s -> !s.isEmpty()).map(a -> a.substring(0, a.length() - 2)).orElse("");

		variabiliCalcolo.setVal(TipoVariabile.PASALLLON, allevamentiNonLimitrofi);
		variabiliCalcolo.setVal(TipoVariabile.PASUBAALLLON, numeroUbaAllevamentoLon);

		// PASSUPELE
		List<ParticellaColtura> listPartElePascolo = variabiliCalcolo.get(TipoVariabile.PASSUPELEMAP).getValMap().get(pascolo.getDescPascolo());
		Float supGisTotale = listPartElePascolo.stream().collect(Collectors.summingDouble(p -> p.getValNum())).floatValue();
		variabiliCalcolo.setVal(TipoVariabile.PASSUPELE, BigDecimal.valueOf(supGisTotale));

		// PASSUPSIGECO
		List<ParticellaColtura> listPartSigecoPascolo = variabiliCalcolo.get(TipoVariabile.PASSUPSIGEMAP).getValMap().get(pascolo.getDescPascolo());
		Float supSigecoTotale = listPartSigecoPascolo.stream().collect(Collectors.summingDouble(p -> p.getValNum())).floatValue();
		variabiliCalcolo.setVal(TipoVariabile.PASSUPSIGECO, BigDecimal.valueOf(supSigecoTotale));

		// PASSUPDET
		if (datiIstruttoriaPascoli != null && datiIstruttoriaPascoli.getSuperficieDeterminata() != null) {
			variabiliCalcolo.setVal(TipoVariabile.PASSUPDETIST, datiIstruttoriaPascoli.getSuperficieDeterminata()); // dato istruttore
			variabiliCalcolo.setVal(TipoVariabile.PASSUPDET, datiIstruttoriaPascoli.getSuperficieDeterminata()); // NVL(PASSUPDETIST;NVL(PASSUPSIGECO;PASSUPELE))
		} else if (variabiliCalcolo.get(TipoVariabile.ISCAMP).getValBoolean())
			variabiliCalcolo.setVal(TipoVariabile.PASSUPDET, variabiliCalcolo.get(TipoVariabile.PASSUPSIGECO).getValNumber());
		else
			variabiliCalcolo.setVal(TipoVariabile.PASSUPDET, variabiliCalcolo.get(TipoVariabile.PASSUPELE).getValNumber());

		if (variabiliCalcolo.get(TipoVariabile.PASSUPDET).getValNumber().compareTo(BigDecimal.ZERO) > 0) {
			// PASRUHMAN2 = PASUBAALLLIM/PASSUPDET
			variabiliCalcolo.setVal(TipoVariabile.PASRUHMAN2, variabiliCalcolo.divide(TipoVariabile.PASUBAALLLIM, TipoVariabile.PASSUPDET));

			// PASRUHMAN3 = PASUBAALLLIM+PASUBAALLLON/PASSUPDET
			variabiliCalcolo.setVal(TipoVariabile.PASRUHMAN3, variabiliCalcolo.divide(variabiliCalcolo.add(TipoVariabile.PASUBAALLLIM, TipoVariabile.PASUBAALLLON), TipoVariabile.PASSUPDET));
		} else {
			variabiliCalcolo.setVal(TipoVariabile.PASRUHMAN3, new BigDecimal(99));
			variabiliCalcolo.setVal(TipoVariabile.PASRUHMAN2, new BigDecimal(99));
		}
		// Impostazione delle variabili di input da persistere
		variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASALLLIM));
		variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASALLLON));
		variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASUBAALLLIM));
		variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASUBAALLLON));
		variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASSUPELE));
		variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASSUPSIGECO));
		if (variabiliCalcolo.get(TipoVariabile.PASSUPDETIST) != null)
			variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASSUPDETIST));

		// Impostazione delle variabile di output da persistere
		variabiliOutput.add(variabiliCalcolo.get(TipoVariabile.PASSUPDET));
		variabiliOutput.add(variabiliCalcolo.get(TipoVariabile.PASRUHMAN2));
		variabiliOutput.add(variabiliCalcolo.get(TipoVariabile.PASRUHMAN3));

	}

	/**
	 * Algoritmo MAN3
	 *
	 * @param datiIstruttoria
	 * @return
	 */
	private void eseguiAlgoritmoMan3(DatiIstruttoriaPascoli datiIstruttoria, DatiCalcoloPascolo datiCalcoloPascolo,
			MapVariabili variabiliCalcolo) {

		variabiliCalcolo.add(TipoVariabile.PASTRASPORTO, new VariabileCalcolo(TipoVariabile.PASTRASPORTO, datiIstruttoria.getVerificaDocumentazione()));
		datiCalcoloPascolo.getVariabiliOutput().add(variabiliCalcolo.get(TipoVariabile.PASTRASPORTO));

	}

	/**
	 * Algoritmo MAN4
	 *
	 * @param datiIstruttoriaPascoli
	 * @param listBovini
	 * @param listOvini
	 * @param listMovimentiPascolo
	 * @param isInfoTitolare
	 * @param pascolo
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	private String eseguiAlgoritmoMan4(DatiIstruttoriaPascoli datiIstruttoriaPascoli,
			List<ConsistenzaPascolo2015Dto> listBovini, List<ConsistenzaPascolo2015Dto> listOvini,
			List<MovimentazionePascoloOviniDto> listMovimentiPascolo, Boolean isInfoTitolare,
			A4gtPascoloParticella pascolo, HashMap<TipoControllo, EsitoControllo> mappaEsiti,
			DatiCalcoloPascolo datiCalcoloPascolo, MapVariabili variabiliCalcolo)
			throws CalcoloSostegnoException {

		List<VariabileCalcolo> variabiliOutput = datiCalcoloPascolo.getVariabiliOutput();
		List<VariabileCalcolo> variabiliInput = datiCalcoloPascolo.getVariabiliInput();
		// Si sommano "il numero di UBA" così calcolate per i soli record il cui "numero di giorni di pascolamento per capo" (GIOR_PASC/NUME_CAPI) è uguale o superiore a 60
		// Betty 17.04.2019: escludo i capi dispersi che sono entrati al pascolo anno precedente e usciti mai piu e per dedurli faccio conto una medi adi 300 giorni # correttiva 42
		listBovini.forEach(b -> {
			BigDecimal mediaGiorniPascolo = (b.getGiorniAlPascolo().divide(b.getNumeroCapi(), 2, RoundingMode.HALF_UP));
			if (mediaGiorniPascolo.doubleValue() >= 60 && mediaGiorniPascolo.doubleValue() <= 200) {
				
				Float coefficiente = b.getAnnoCampagna().longValue() >= 2021L ? 
						coefficientiMan4PasUbaBovini2021.get(CodiceSpecie.valueOf(b.getSpecie())).getOrDefault(FasciaEta.valueOf(b.getFasciaEta()), 0F) :
						coefficientiMan4PasUbaBovini2020.get(CodiceSpecie.valueOf(b.getSpecie())).getOrDefault(FasciaEta.valueOf(b.getFasciaEta()), 0F);
				logger.debug("coefficientiMan4PasUbaBovini2021 specie: {} - Fascia eta:{} - coefficiente: {}", b.getSpecie(), b.getFasciaEta(), coefficiente);
				logger.debug("setPasUbaBovini: numeroCapi: {} * coefficiente = {} ", b.getNumeroCapi(), b.getNumeroCapi().multiply(BigDecimal.valueOf(coefficiente)));
				datiCalcoloPascolo.setPasUbaBovini(datiCalcoloPascolo.getPasUbaBovini().add(b.getNumeroCapi().multiply(BigDecimal.valueOf(coefficiente))));
			} else if (mediaGiorniPascolo.doubleValue() > 200) {
				logger.warn("Trovati bovini o equini al pascolo con media giorni {} per il pascolo {}", mediaGiorniPascolo, b.getCodicePascolo());
			}
		});

		// Si sommano "il numero di UBA" (0,15*NUME_CAPI) così calcolate per i soli record il cui "numero di giorni di pascolamento per capo" è uguale o superiore a 30
		// Betty 17.04.2019: escludo i capi dispersi che sono entrati al pascolo anno precedente e usciti mai piu e per dedurli faccio conto una medi adi 300 giorni # correttiva 42
		listOvini.forEach(o -> {
			BigDecimal mediaGiorniPascolo = (o.getGiorniAlPascolo().divide(o.getNumeroCapi(), 2, RoundingMode.HALF_UP));
			if (mediaGiorniPascolo.doubleValue() >= 30 && mediaGiorniPascolo.doubleValue() <= 200) {
				if (Arrays.asList(FasciaEta.MESI_0_6, FasciaEta.MESI_6_24, FasciaEta.OLTRE_24).contains(FasciaEta.valueOf(o.getFasciaEta()))) {
					datiCalcoloPascolo.setPasUbaOvini(datiCalcoloPascolo.getPasUbaOvini().add(o.getNumeroCapi().multiply(BigDecimal.valueOf(0.15))));
				}
//				if (!"OLTRE_24".equals(o.getFasciaEta()) && !"6_24".equals(o.getFasciaEta())) { // Betty 18.06.2019: per il conteggio uso solo la classificazione 0_12 e OLTRE_12
//					datiCalcoloPascolo.setPasUbaOvini(datiCalcoloPascolo.getPasUbaOvini().add(o.getNumeroCapi().multiply(BigDecimal.valueOf(0.15))));
//				}
			} else if (mediaGiorniPascolo.doubleValue() > 200) {
				logger.warn("Trovati ovini al pascolo con media giorni {} per il pascolo {}", mediaGiorniPascolo, o.getCodicePascolo());
			}
		});

		// Si sommano "il numero di UBA per partita" così calcolate (0,15*NUME_OVIN) per i soli record il cui "numero di giorni di pascolamento per partita" è uguale o superiore a 30 (fino al 2020) o 60 (dal 2021).
		if (listMovimentiPascolo != null) {
			listMovimentiPascolo.stream().forEach(movimentiPascolo -> {
				logger.debug("Calcolo PASUBAOVINIPARTITA - Anno Campagna: {}", movimentiPascolo.getAnnoCampagna().longValue());
				if (movimentiPascolo.getAnnoCampagna().longValue() <= 2020L) {
					datiCalcoloPascolo.setPasUbaOviniPartita(datiCalcoloPascolo.getPasUbaOviniPartita()
							.add(BigDecimal.valueOf(listMovimentiPascolo.stream().filter(
									// betty 18.06.2019: aggiunto controllo su giorni pascolamento per segnalazione 67
									x -> (x.getGiorniPascolamento() != null && x.getGiorniPascolamento().intValue() >= 30))
									.mapToDouble(c -> c.getNumeroCapi().doubleValue() * 0.15).sum())));
				} else { //Evolutiva: dal 2021 in poi Si sommano "il numero di UBA per partita" così calcolate per i soli record il cui "numero di giorni di pascolamento per partita" è uguale o superiore a 60.  
						logger.debug("giorni pascolamento = {}; codice pascolo {}, #capi {}", movimentiPascolo.getGiorniPascolamento(), movimentiPascolo.getCodPascolo(), movimentiPascolo.getNumeroCapi());
						BigDecimal ubaParziali = BigDecimal.ZERO;
						if (movimentiPascolo.getGiorniPascolamento() != null && movimentiPascolo.getGiorniPascolamento().intValue() >= 60) {
							ubaParziali = movimentiPascolo.getNumeroCapi().multiply(BigDecimal.valueOf(0.15));
							datiCalcoloPascolo.setPasUbaOviniPartita(datiCalcoloPascolo.getPasUbaOviniPartita().add(ubaParziali));
							logger.debug("PasUbaOviniPartita parziale = {} ", ubaParziali );
						} 
				}
			});
		}

		if (datiIstruttoriaPascoli != null && datiIstruttoriaPascoli.getSuperficieDeterminata() != null) {
			variabiliCalcolo.setVal(TipoVariabile.PASSUPDETIST, datiIstruttoriaPascoli.getSuperficieDeterminata()); // dato istruttore
		}
		variabiliCalcolo.setVal(TipoVariabile.PASUBABOVINI, datiCalcoloPascolo.getPasUbaBovini());
		variabiliCalcolo.setVal(TipoVariabile.PASUBAOVINI, datiCalcoloPascolo.getPasUbaOvini());
		variabiliCalcolo.setVal(TipoVariabile.PASUBAOVINIPARTITA, datiCalcoloPascolo.getPasUbaOviniPartita());

		// PASSUPELE
		List<ParticellaColtura> listPartElePascolo = variabiliCalcolo.get(TipoVariabile.PASSUPELEMAP).getValMap().get(pascolo.getDescPascolo());
		Float supGisTotale = listPartElePascolo.stream().collect(Collectors.summingDouble(p -> p.getValNum())).floatValue();
		variabiliCalcolo.setVal(TipoVariabile.PASSUPELE, BigDecimal.valueOf(supGisTotale));

		// PASSUPSIGECO
		List<ParticellaColtura> listPartSigecoPascolo = variabiliCalcolo.get(TipoVariabile.PASSUPSIGEMAP).getValMap().get(pascolo.getDescPascolo());
		Float supSigecoTotale = listPartSigecoPascolo.stream().collect(Collectors.summingDouble(p -> p.getValNum())).floatValue();
		variabiliCalcolo.setVal(TipoVariabile.PASSUPSIGECO, BigDecimal.valueOf(supSigecoTotale));

		// PASSUPDET
		if (datiIstruttoriaPascoli != null && datiIstruttoriaPascoli.getSuperficieDeterminata() != null) {
			variabiliCalcolo.setVal(TipoVariabile.PASSUPDETIST, datiIstruttoriaPascoli.getSuperficieDeterminata()); // dato istruttore
			variabiliCalcolo.setVal(TipoVariabile.PASSUPDET, datiIstruttoriaPascoli.getSuperficieDeterminata()); // NVL(PASSUPDETIST;NVL(PASSUPSIGECO;PASSUPELE))
		} else if (variabiliCalcolo.get(TipoVariabile.ISCAMP).getValBoolean())
			variabiliCalcolo.setVal(TipoVariabile.PASSUPDET, variabiliCalcolo.get(TipoVariabile.PASSUPSIGECO).getValNumber());
		else
			variabiliCalcolo.setVal(TipoVariabile.PASSUPDET, variabiliCalcolo.get(TipoVariabile.PASSUPELE).getValNumber());

		variabiliCalcolo.setVal(TipoVariabile.PASUBATOT, variabiliCalcolo.add(TipoVariabile.PASUBABOVINI, TipoVariabile.PASUBAOVINI, TipoVariabile.PASUBAOVINIPARTITA));
		if (variabiliCalcolo.get(TipoVariabile.PASSUPDET).getValNumber().compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal rappUbaHa = variabiliCalcolo.get(TipoVariabile.PASUBATOT).getValNumber().divide(variabiliCalcolo.get(TipoVariabile.PASSUPDET).getValNumber(), 2, RoundingMode.HALF_UP);
			variabiliCalcolo.setVal(TipoVariabile.PASRUH, rappUbaHa);
		} else {
			// Nel caso in cui PASSUPDET sia 0 valorizzo PASRUH con un numero alto
			variabiliCalcolo.setVal(TipoVariabile.PASRUH, new BigDecimal(99));
		}

		List<ParticellaColtura> listParticellePascolo = new ArrayList<>();
		if (variabiliCalcolo.get(TipoVariabile.ISCAMP).getValBoolean())
			listParticellePascolo = listPartSigecoPascolo;
		else
			listParticellePascolo = listPartElePascolo;

		// PFANOMMAN - IF $PASCOLO PASRUH >= 0,20 THEN NO ELSE SI
		ArrayList<ParticellaColtura> listPfAnomMan = getListPfAnomMan(listParticellePascolo, variabiliCalcolo.get(TipoVariabile.PASRUH).getValNumber().doubleValue() < 0.20);
		// PFPASCOLO
		ArrayList<ParticellaColtura> listPfPascolo = getPfPascolo(pascolo);

		VariabileCalcolo pfAnomManPascolo = new VariabileCalcolo(TipoVariabile.PFANOMMAN, listPfAnomMan);
		VariabileCalcolo pfPascolo = new VariabileCalcolo(TipoVariabile.PFPASCOLO, listPfPascolo);

		// Setto le variabili ParticellaColtura nell'output del singolo pascolo
		variabiliOutput.add(pfAnomManPascolo);
		variabiliOutput.add(pfPascolo);

		// Aggiungo le particelle alla variabile PFANOMMAN di domanda
		VariabileCalcolo pfAnomMan = variabiliCalcolo.get(TipoVariabile.PFANOMMAN);
		if (pfAnomMan != null) {
			ArrayList<ParticellaColtura> listPc = pfAnomMan.getValList();
			listPc.addAll(listPfAnomMan);
			variabiliCalcolo.setVal(TipoVariabile.PFANOMMAN, listPc);
		} else {
			variabiliCalcolo.setVal(TipoVariabile.PFANOMMAN, listPfAnomMan);
		}

		// Aggiungo le particelle alla variabile PFPASCOLO di domanda
		VariabileCalcolo pfPascoloDom = variabiliCalcolo.get(TipoVariabile.PFPASCOLO);
		if (pfPascoloDom != null) {
			ArrayList<ParticellaColtura> listPcPascolo = pfPascoloDom.getValList();
			listPcPascolo.addAll(listPfPascolo);
			variabiliCalcolo.setVal(TipoVariabile.PFPASCOLO, listPcPascolo);
		} else {
			variabiliCalcolo.setVal(TipoVariabile.PFPASCOLO, listPfPascolo);
		}

		// Impostazione delle variabile di input da persistere
		variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASUBABOVINI));
		variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASUBAOVINI));
		variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASUBAOVINIPARTITA));
		variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASSUPELE));
		variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASSUPSIGECO));
		variabiliInput.add(variabiliCalcolo.get(TipoVariabile.PASSUPDETIST));

		// Impostazione delle variabile di output da persistere
		variabiliOutput.add(variabiliCalcolo.get(TipoVariabile.PASSUPDET));
		variabiliOutput.add(variabiliCalcolo.get(TipoVariabile.PASUBATOT));
		variabiliOutput.add(variabiliCalcolo.get(TipoVariabile.PASRUH));

		if (variabiliCalcolo.get(TipoVariabile.PASRUH).getValNumber().doubleValue() >= 0.20 && isInfoTitolare) {
			// BRIDUSDC014
			EsitoControllo esitoMan4 = new EsitoControllo(TipoControllo.BRIDUSDC014_mantenimentoSupMan4, true);
			esitoMan4.setPascolo(pascolo.getDescPascolo());
			mappaEsiti.put(TipoControllo.BRIDUSDC014_mantenimentoSupMan4, esitoMan4);
			return CodiceEsito.PASF_01.getCodiceEsito();
		} else if (variabiliCalcolo.get(TipoVariabile.PASRUH).getValNumber().doubleValue() < 0.20 && isInfoTitolare) {
			// BRIDUSDC014
			EsitoControllo esitoMan4 = new EsitoControllo(TipoControllo.BRIDUSDC014_mantenimentoSupMan4, false);
			esitoMan4.setPascolo(pascolo.getDescPascolo());
			mappaEsiti.put(TipoControllo.BRIDUSDC014_mantenimentoSupMan4, esitoMan4);
			return CodiceEsito.PASF_02.getCodiceEsito();
		} else if (variabiliCalcolo.get(TipoVariabile.PASRUH).getValNumber().doubleValue() >= 0.20 && !isInfoTitolare) {
			// BRIDUSDC014
			EsitoControllo esitoMan4 = new EsitoControllo(TipoControllo.BRIDUSDC014_mantenimentoSupMan4, true);
			esitoMan4.setPascolo(pascolo.getDescPascolo());
			mappaEsiti.put(TipoControllo.BRIDUSDC014_mantenimentoSupMan4, esitoMan4);
			return CodiceEsito.PASF_04.getCodiceEsito();
		} else if (variabiliCalcolo.get(TipoVariabile.PASRUH).getValNumber().doubleValue() < 0.20 && !isInfoTitolare) {
			// BRIDUSDC014
			EsitoControllo esitoMan4 = new EsitoControllo(TipoControllo.BRIDUSDC014_mantenimentoSupMan4, false);
			esitoMan4.setPascolo(pascolo.getDescPascolo());
			mappaEsiti.put(TipoControllo.BRIDUSDC014_mantenimentoSupMan4, esitoMan4);
			return CodiceEsito.PASF_05.getCodiceEsito();
		}
		return "";
	}

	private String eseguiAlgoritmoMan4Titolare(A4gtPascoloParticella pascolo, boolean hasInfoTitolareService,
			DatiCalcoloPascolo datiCalcoloPascolo, MapVariabili variabiliCalcolo) 
					throws CalcoloSostegnoException {

		List<VariabileCalcolo> variabiliOutput = datiCalcoloPascolo.getVariabiliOutput();

		List<ParticellaColtura> listParticellePascolo = new ArrayList<>();
		if (variabiliCalcolo.get(TipoVariabile.ISCAMP).getValBoolean())
			listParticellePascolo = variabiliCalcolo.get(TipoVariabile.PASSUPSIGEMAP).getValMap().get(pascolo.getDescPascolo());
		else
			listParticellePascolo = variabiliCalcolo.get(TipoVariabile.PASSUPELEMAP).getValMap().get(pascolo.getDescPascolo());

		// Il boolean HasMan viene valorizzato a True in quanto il metodo viene eseguito sempre e solo se si verificano le condizioni di presenza MAN
		ArrayList<ParticellaColtura> listPfAnomMan = getListPfAnomMan(listParticellePascolo, true);
		ArrayList<ParticellaColtura> listPfPascolo = getPfPascolo(pascolo);

		VariabileCalcolo pfAnomManPascolo = new VariabileCalcolo(TipoVariabile.PFANOMMAN, listPfAnomMan);
		VariabileCalcolo pfPascolo = new VariabileCalcolo(TipoVariabile.PFPASCOLO, listPfPascolo);

		// Setto le variabili ParticellaColtura nell'output del singolo pascolo
		variabiliOutput.add(pfAnomManPascolo);
		variabiliOutput.add(pfPascolo);

		// Aggiungo le particelle alla variabile PFANOMMAN di domanda
		VariabileCalcolo pfAnomMan = variabiliCalcolo.get(TipoVariabile.PFANOMMAN);
		if (pfAnomMan != null) {
			ArrayList<ParticellaColtura> listPc = pfAnomMan.getValList();
			listPc.addAll(listPfAnomMan);
			variabiliCalcolo.setVal(TipoVariabile.PFANOMMAN, listPc);
		} else {
			variabiliCalcolo.setVal(TipoVariabile.PFANOMMAN, listPfAnomMan);
		}

		// Aggiungo le particelle alla variabile PFPASCOLO di domanda
		VariabileCalcolo pfPascoloDom = variabiliCalcolo.get(TipoVariabile.PFPASCOLO);
		if (pfPascoloDom != null) {
			ArrayList<ParticellaColtura> listPcPascolo = pfPascoloDom.getValList();
			listPcPascolo.addAll(listPfPascolo);
			variabiliCalcolo.setVal(TipoVariabile.PFPASCOLO, listPcPascolo);
		} else {
			variabiliCalcolo.setVal(TipoVariabile.PFPASCOLO, listPfPascolo);
		}

		if (hasInfoTitolareService)
			return CodiceEsito.PASF_03.getCodiceEsito();
		else
			return CodiceEsito.PASF_06.getCodiceEsito();
	}

	private List<ConsistenzaPascolo2015Dto> getConsistenzaPascoloDaCacheBdn(Integer annoCampagna, String codPascolo)
			throws CalcoloSostegnoException {
		List<ConsistenzaPascolo2015Dto> listConsistenzaPascolo = null;
		try {
			String params = "{ \"annoCampagna\": " + annoCampagna + " , \"codPascolo\": \"" + codPascolo.trim() + "\" }";
			String encoded = URLEncoder.encode(params, "UTF-8");

			String resource = configurazione.getUriCacheBdn().concat("consistenzaPascolo?params=").concat(encoded);
			ResponseEntity<List<ConsistenzaPascolo2015Dto>> response = restTemplate.exchange(new URI(resource), HttpMethod.GET, null,
					new ParameterizedTypeReference<List<ConsistenzaPascolo2015Dto>>() {
					});
			listConsistenzaPascolo = response.getBody();
		} catch (Exception e1) {
			logger.error("Errore nella chiamata al servizio di lettura consistenza pascolo da cache bdn per anno e pascolo {}", annoCampagna, codPascolo, e1);
			throw new CalcoloSostegnoException("Errore leggendo la cache della consistenza al pascolo " + codPascolo, e1);
		}
		return listConsistenzaPascolo;

	}

	protected List<MovimentazionePascoloOviniDto> getMovimentazionePascoloOviniDaCacheBdn(Integer annoCampagna, String codPascolo)
			throws CalcoloSostegnoException {	
		try {
			List<MovimentazionePascoloOviniDto> listMovimentiPascolo = null;
			String params = "{ \"annoCampagna\": " + annoCampagna + " , \"codPascolo\": \"" + codPascolo.trim() + "\"}";
			String encoded = URLEncoder.encode(params, "UTF-8");

			String resource = configurazione.getUriCacheBdn().concat("movimentiPascolo?params=").concat(encoded);
			ResponseEntity<List<MovimentazionePascoloOviniDto>> response = restTemplate.exchange(new URI(resource), HttpMethod.GET, null,
					new ParameterizedTypeReference<List<MovimentazionePascoloOviniDto>>() {
					});
			listMovimentiPascolo = response.getBody();
			
			return calcolaMovimentazione(listMovimentiPascolo);
		} catch (Exception e1) {
			logger.error("Errore nella chiamata al servizio di lettura movimentazione pascolo da cache bdn per anno e pascolo {} {}", annoCampagna, codPascolo, e1);
			throw new CalcoloSostegnoException("Errore leggendo la cache della movimentazione al pascolo " + codPascolo, e1);
		}

	}

	private List<MovimentazionePascoloOviniDto> calcolaMovimentazione(List<MovimentazionePascoloOviniDto> movimentazioniPascolo) {
		List<MovimentazionePascoloOviniDto> result = new ArrayList<>();
		if (movimentazioniPascolo == null || movimentazioniPascolo.isEmpty()) {
			return result;
		}
		Map<BigDecimal, List<MovimentazionePascoloOviniDto>> movimentazioniPerAllevamento = 
				movimentazioniPascolo.stream().collect(Collectors.groupingBy(MovimentazionePascoloOviniDto::getIdAllevamento));
		MovimentazionePascoloOviniDto single = null;
		for (BigDecimal idAllevamento : movimentazioniPerAllevamento.keySet()) {
			single = collassaMovimentazioni(movimentazioniPerAllevamento.get(idAllevamento));
			if (single != null) {
				logger.debug("calcolaMovimentazione #elemento capi: {}, detentore: {}", single.getNumeroCapi(), single.getCodiceFiscaleDetentore());
				result.add(single);
			}
		}
		logger.debug("calcolaMovimentazione #result: {}", result.size());
		return result;
	}
	
	/**
	 * Collassa le movimentazioni ad una sola che ha:
	 * - come numero di capi il minimo del totale di quelli entrati ed il totale di quelli usciti 
	 * - come data di entrata l'ultima delle entrate
	 * - come data di uscita quella della prima uscita
	 * @param movimentiAllevamento lista di movimentazioni da collassare
	 * @return un'unica movimentazione
	 */
	private MovimentazionePascoloOviniDto collassaMovimentazioni(List<MovimentazionePascoloOviniDto> movimentiAllevamento) {
		List<MovimentazionePascoloOviniDto> ingressiAllevamento = movimentiAllevamento.stream().filter(m -> m.getDataIngresso() != null).collect(Collectors.toList());
		logger.debug("collassaMovimentazioni movimentiAllevamento #ingressiAllevamento: {}", ingressiAllevamento.size());
		List<MovimentazionePascoloOviniDto> usciteAllevamento = movimentiAllevamento.stream().filter(m -> m.getDataUscita() != null).collect(Collectors.toList());
		logger.debug("collassaMovimentazioni movimentiAllevamento #usciteAllevamento: {}", usciteAllevamento.size());

		// non esistono ingressi ... ANOMALIA
		if (ingressiAllevamento == null || ingressiAllevamento.isEmpty()) {
			logger.warn("calcolaMovimentazione: per l'allevamento {} NON TROVO NESSUNA ENTRATA PER IL CALCOLO DEL PERIODO AL PASCOLO");
			return null;
		}
		// non esistono uscite ... ANOMALIA
		if (usciteAllevamento == null || usciteAllevamento.isEmpty()) {
			logger.warn("calcolaMovimentazione: per l'allevamento {} NON TROVO NESSUNA USCITA PER IL CALCOLO DEL PERIODO AL PASCOLO");
			return null;
		}
		
		// Variabili necessarie per sfruttare lambda expression su ingressiAllevamento e usciteAllevamento
		List<MovimentazionePascoloOviniDto> ingressi = ingressiAllevamento;
		List<MovimentazionePascoloOviniDto> uscite = usciteAllevamento;
		
		// Caso 1 N o N a 1 con N > 1 : tolgo entrate o uscite inferiori a trenta giorni rispetto alla movimentazione di riferimento
		if (ingressiAllevamento.size() == 1 && usciteAllevamento.size() > 1) {
			uscite = usciteAllevamento.stream().filter(
				m -> calcolaGiorni(ingressiAllevamento.get(0).getDataIngresso(), m.getDataUscita()) >= 30).collect(Collectors.toList());
		}
		if (ingressiAllevamento.size() > 1 && usciteAllevamento.size() == 1) {
			ingressi = ingressiAllevamento.stream().filter(
					m -> calcolaGiorni(m.getDataIngresso(), usciteAllevamento.get(0).getDataUscita()) >= 30).collect(Collectors.toList());
		}
		
		if (ingressi == null || ingressi.isEmpty()) {
			logger.warn("calcolaMovimentazione: per l'allevamento {} NON HO ENTRATE CHE SODDISFANO I REQUISITI MINIMI DI DURATA");
			return null;
		}
		// non esistono uscite ... ANOMALIA
		if (uscite == null || uscite.isEmpty()) {
			logger.warn("calcolaMovimentazione: per l'allevamento {} NON HO USCITE CHE SODDISFANO I REQUISITI MINIMI DI DURATA");
			return null;
		}
		
		// Collasso
		MovimentazionePascoloOviniDto ingresso = calcolaDatiPerIngresso(ingressi);
		MovimentazionePascoloOviniDto uscita = calcolaDatiPerUscita(uscite);
		logger.debug("Collasso ingresso: Giorni Pascolamento: {}", ingresso.getGiorniPascolamento());
		logger.debug("Collasso uscita: Giorni Pascolamento: {}", uscita.getGiorniPascolamento());
		logger.debug("Collasso ingresso: #capi: {}", ingresso.getNumeroCapi());
		logger.debug("Collasso uscita: #capi: {}", uscita.getNumeroCapi());
		MovimentazionePascoloOviniDto creaDatiMovimentazione = creaDatiMovimentazione(ingresso, uscita);

		// Evolutiva: se campagna >= 2021 allora calcolare il "numero di giorni di pascolamento per partita" escludendo i periodi precedenti al 15 giugno della campagna di riferimento e successivi al 25 settembre della campagna di riferimento
		if (creaDatiMovimentazione.getAnnoCampagna().longValue() >= 2021) {
			
			// trovare l'intersezione del range di date (dtIngresso, dtFine) con (inizioRange, fineRange)
			LocalDate inizioRange = LocalDate.of(creaDatiMovimentazione.getAnnoCampagna().intValue(), Month.JUNE, 15);
			LocalDate fineRange = LocalDate.of(creaDatiMovimentazione.getAnnoCampagna().intValue(), Month.SEPTEMBER, 25);
			
			LocalDate dataIngresso = LocalDateConverter.fromDate(creaDatiMovimentazione.getDataIngresso());
			LocalDate dataUscita = LocalDateConverter.fromDate(creaDatiMovimentazione.getDataUscita());
			
		    if (fineRange.isBefore(inizioRange) || dataUscita.isBefore(inizioRange)) {
		        // le date non sono coerenti 
		    	creaDatiMovimentazione.setGiorniPascolamento(0L);
		    }
			
	        if (fineRange.isBefore(dataIngresso) || dataUscita.isBefore(inizioRange)) {
	            // interamente fuori dal range temporale
	        	creaDatiMovimentazione.setGiorniPascolamento(0L);
	        } else {
	            LocalDate laterStart = Collections.max(Arrays.asList(inizioRange, dataIngresso));
	            LocalDate earlierEnd = Collections.min(Arrays.asList(fineRange, dataUscita));
	            
	            creaDatiMovimentazione.setDataIngresso(java.sql.Date.valueOf(laterStart));
	            creaDatiMovimentazione.setDataUscita(java.sql.Date.valueOf(earlierEnd));
	            creaDatiMovimentazione.setGiorniPascolamento(Duration.between(laterStart.atStartOfDay(), earlierEnd.atStartOfDay()).toDays());
	        }
		}
		logger.debug("creaDatiMovimentazione: Numero Capi: {}", creaDatiMovimentazione.getNumeroCapi());
		return creaDatiMovimentazione;
	}
	
	private MovimentazionePascoloOviniDto calcolaDatiPerIngresso(List<MovimentazionePascoloOviniDto> ingressiAllevamento) {
		MovimentazionePascoloOviniDto ultimoIngresso = 
				ingressiAllevamento
				.stream()
				.max(Comparator.comparing(MovimentazionePascoloOviniDto::getDataIngresso))
				.orElseThrow(NoSuchElementException::new);
		Long totCapi = ingressiAllevamento
				.stream()
				.map(MovimentazionePascoloOviniDto::getNumeroCapi)
				.collect(Collectors.summingLong(BigDecimal::longValue));
		ultimoIngresso.setNumeroCapi(new BigDecimal(totCapi.toString()));
		return ultimoIngresso;
	}

	private MovimentazionePascoloOviniDto calcolaDatiPerUscita(List<MovimentazionePascoloOviniDto> usciteAllevamento) {
		MovimentazionePascoloOviniDto primaUscita = 
				usciteAllevamento
				.stream()
				.min(Comparator.comparing(MovimentazionePascoloOviniDto::getDataUscita))
				.orElseThrow(NoSuchElementException::new);
		Long totCapi = usciteAllevamento
				.stream()
				.map(MovimentazionePascoloOviniDto::getNumeroCapi)
				.collect(Collectors.summingLong(BigDecimal::longValue));
		primaUscita.setNumeroCapi(new BigDecimal(totCapi.toString()));
		return primaUscita;
	}

	private MovimentazionePascoloOviniDto creaDatiMovimentazione(MovimentazionePascoloOviniDto ingresso, MovimentazionePascoloOviniDto uscita) {
		MovimentazionePascoloOviniDto movimentazioneAllevamento = new MovimentazionePascoloOviniDto();
		movimentazioneAllevamento.setIdAllevamento(ingresso.getIdAllevamento());
		movimentazioneAllevamento.setCodiceFiscaleDetentore(ingresso.getCodiceFiscaleDetentore());
		movimentazioneAllevamento.setIdPascolo(ingresso.getIdPascolo());
		movimentazioneAllevamento.setAnnoCampagna(ingresso.getAnnoCampagna());
		movimentazioneAllevamento.setCodPascolo(ingresso.getCodPascolo());
		movimentazioneAllevamento.setComunePascolo(ingresso.getComunePascolo());
		movimentazioneAllevamento.setDataIngresso(ingresso.getDataIngresso());
		movimentazioneAllevamento.setDataUscita(uscita.getDataUscita());
		movimentazioneAllevamento.setGiorniPascolamento(calcolaGiorni(ingresso.getDataIngresso(), uscita.getDataUscita()));
		movimentazioneAllevamento.setNumeroCapi(ingresso.getNumeroCapi().min(uscita.getNumeroCapi()));
		return movimentazioneAllevamento;
	}
	
	private long calcolaGiorni(java.sql.Date ingresso, java.sql.Date uscita) {
		return ChronoUnit.DAYS.between(ingresso.toLocalDate().atStartOfDay(), uscita.toLocalDate().atStartOfDay());
	}

	private List<ConsistenzaAllevamentoDto> getConsistenzaAllevamentoDaCacheBdn(Integer annoCampagna, String codiceFiscale) throws CalcoloSostegnoException {
		try {
			String params = "{ \"annoCampagna\": " + annoCampagna + " , \"codiceFiscale\": \"" + codiceFiscale.trim() + "\" }";
			String encoded = URLEncoder.encode(params, "UTF-8");

			String resource = configurazione.getUriCacheBdn().concat("consistenzaAllevamento?params=").concat(encoded);
			ResponseEntity<List<ConsistenzaAllevamentoDto>> response = restTemplate.exchange(new URI(resource), HttpMethod.GET, null,
					new ParameterizedTypeReference<List<ConsistenzaAllevamentoDto>>() {
					});
			return response.getBody();
		} catch (IOException | URISyntaxException e) {
			logger.error("Errore nella lettura della cache della consistenza allevamento per anno {} e codice fiscale {} ", annoCampagna, codiceFiscale, e);
			throw new CalcoloSostegnoException("Errore nella lettura della cache della consistenza allevamento per codice fiscale " + codiceFiscale, e);
		}
	}
	
	private DatiIstruttoriaPascoli leggiDatiIstruttoria(IstruttoriaModel istruttoria, String codicePascolo) throws CalcoloSostegnoException {

		try {
			List<DatiIstruttoriaPascoli> list = datiIstruttoreService.getDatiIstruttoreDisaccoppiatoPascoli(istruttoria.getId());
			DatiIstruttoriaPascoli result = null;
			if (CollectionUtils.isEmpty(list))
				return result;

			Optional<DatiIstruttoriaPascoli> datiIstruttoriaPascolo = list.stream().filter(b -> (b.getDescrizionePascolo().equals(codicePascolo))).findFirst();

			if (datiIstruttoriaPascolo.isPresent())
				result = datiIstruttoriaPascolo.get();
			return result;
		} catch (Exception e) {
			logger.error("Errore nella lettura dei dati istruttore per l'istruttoria {} ", istruttoria.getId(), e);
			throw new CalcoloSostegnoException("Errore nella lettura dei dati istruttore per l'istruttoria {} " + istruttoria.getId(), e);
		}

	}

	/**
	 * Metodo privato per la valorizzazione della variabile PFPASCOLO con una lista di oggetti Particella/coltura aggiornati con il codice del pascolo al quale afferiscono.
	 *
	 * @param pascolo
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private ArrayList<ParticellaColtura> getPfPascolo(A4gtPascoloParticella pascolo) throws CalcoloSostegnoException {

		try {
			List<ParticellaColtura> listParticellePascolo = mapper.readValue(pascolo.getInfoPartPascolo(), new TypeReference<List<ParticellaColtura>>() {
			});

			ArrayList<ParticellaColtura> listPfPascolo = new ArrayList<>();

			listParticellePascolo.stream().forEach(p -> {
				ParticellaColtura pcPascolo = new ParticellaColtura();
				pcPascolo.setColtura(p.getColtura());
				pcPascolo.setParticella(p.getParticella());
				pcPascolo.setValString(pascolo.getDescPascolo());
				listPfPascolo.add(pcPascolo);

			});

			return listPfPascolo;
		} catch (IOException e) {
			logger.error("Erorre estraendo i dati delle particelle colture dal pascolo {} con codice {}", pascolo.getId(), pascolo.getCodPascolo(), e);
			throw new CalcoloSostegnoException("Erorre estraendo i dati delle particelle colture dal pascolo con codice {}" + pascolo.getCodPascolo(), e);
		}
	}

	/**
	 * Metodo privato per la valorizzazione della variabile di domanda PfAnomMan
	 *
	 * @param hasMan
	 * @return
	 */
	private ArrayList<ParticellaColtura> getListPfAnomMan(List<ParticellaColtura> listPartColt, boolean hasMan) {

		ArrayList<ParticellaColtura> listPfAnomMan = new ArrayList<>();

		listPartColt.stream().forEach(p -> {
			ParticellaColtura pcMan = new ParticellaColtura();
			pcMan.setColtura(p.getColtura());
			pcMan.setParticella(p.getParticella());
			pcMan.setLivello(p.getLivello());
			pcMan.setDescColtura(p.getDescColtura());
			pcMan.setDescMantenimento(p.getDescMantenimento());
			pcMan.setValNum(p.getValNum());
			pcMan.setValBool(hasMan);

			listPfAnomMan.add(pcMan);
		});

		return listPfAnomMan;

	}

	private static class DatiCalcoloPascolo {
		private List<VariabileCalcolo> variabiliInput = new ArrayList<>();
		private List<VariabileCalcolo> variabiliOutput = new ArrayList<>();

		private BigDecimal pasUbaBovini = BigDecimal.ZERO;
		private BigDecimal pasUbaOvini = BigDecimal.ZERO;
		private BigDecimal pasUbaOviniPartita = BigDecimal.ZERO;

		public List<VariabileCalcolo> getVariabiliInput() {
			return variabiliInput;
		}
		public List<VariabileCalcolo> getVariabiliOutput() {
			return variabiliOutput;
		}
		public BigDecimal getPasUbaBovini() {
			return pasUbaBovini;
		}
		public BigDecimal getPasUbaOvini() {
			return pasUbaOvini;
		}
		public BigDecimal getPasUbaOviniPartita() {
			return pasUbaOviniPartita;
		}
		public void setPasUbaBovini(BigDecimal pasUbaBovini) {
			this.pasUbaBovini = pasUbaBovini;
		}
		public void setPasUbaOvini(BigDecimal pasUbaOvini) {
			this.pasUbaOvini = pasUbaOvini;
		}
		public void setPasUbaOviniPartita(BigDecimal pasUbaOviniPartita) {
			this.pasUbaOviniPartita = pasUbaOviniPartita;
		}
	}
	
	
	private enum CodiceSpecie {
		BOVINI,
		CAVALLI,
		MULI,
		ASINI,
		BARDOTTI
	}
	
	private enum FasciaEta {
		MESI_0_6,
		MESI_6_24,
		OLTRE_24,
		// esistenti in cache bdn ma non usati:
		OLTRE_12,
		MESI_0_12
	}
}
