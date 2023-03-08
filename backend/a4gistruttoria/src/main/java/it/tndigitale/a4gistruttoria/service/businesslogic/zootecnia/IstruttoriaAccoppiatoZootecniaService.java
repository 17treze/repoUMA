package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia;

import static java.util.Optional.ofNullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4gistruttoria.dto.Alpeggio;
import it.tndigitale.a4gistruttoria.dto.Capi;
import it.tndigitale.a4gistruttoria.dto.Capo;
import it.tndigitale.a4gistruttoria.dto.EtichettaturaCarne;
import it.tndigitale.a4gistruttoria.dto.InformazioniAllevamento;
import it.tndigitale.a4gistruttoria.dto.ProduzioneLatte;
import it.tndigitale.a4gistruttoria.dto.StatisticaZootecnia;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoBdn;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoBdnBuilder;
import it.tndigitale.a4gistruttoria.dto.zootecnia.EsitoCapiFilter;
import it.tndigitale.a4gistruttoria.repository.dao.AnalisiLatteDao;
import it.tndigitale.a4gistruttoria.repository.dao.CapiPerInterventoDao;
import it.tndigitale.a4gistruttoria.repository.dao.EsitoCalcoloCapoDao;
import it.tndigitale.a4gistruttoria.repository.dao.EtichetttaturaCarneDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.ProduzioneLatteDao;
import it.tndigitale.a4gistruttoria.repository.dao.RegAlpeggioDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtEtichettaturaCarne;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.ElencoCapiCsv;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.RegistroAlpeggioModel;
import it.tndigitale.a4gistruttoria.repository.model.RegistroAnalisiLatteModel;
import it.tndigitale.a4gistruttoria.repository.model.RegistroProduzioneLatteModel;
import it.tndigitale.a4gistruttoria.repository.specification.EsitoCapiSpecificationBuilder;
import it.tndigitale.a4gistruttoria.util.MapperWrapper;

@Service
public class IstruttoriaAccoppiatoZootecniaService {

	@Autowired
	private IstruttoriaDao istruttoriaDao;
	@Autowired
	private CapoBdnBuilder capoBdnBuilder;
	@Autowired
	private EsitoCalcoloCapoDao esitoCalcoloCapoDao;
	@Autowired
	private EtichetttaturaCarneDao etichettaturaCarneDao;
	@Autowired
	private RegAlpeggioDao regAlpeggioDao;
	@Autowired
	private AnalisiLatteDao analisiLatteDao;
	@Autowired
	private ProduzioneLatteDao produzioneLatteDao;
	@Autowired
	private MapperWrapper mapperWrapper;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private CapiPerInterventoDao capiPerInterventoDao;

	private static Logger logger = LoggerFactory.getLogger(IstruttoriaAccoppiatoZootecniaService.class);

	@Transactional(readOnly = true)
	public List<Capi> getAllevamentiImpegnati(Long idIstruttoria) {
		return  getAllevamentiImpegnati(idIstruttoria,null);
	}

	public List<Capi> getAllevamentiImpegnati(Long idIstruttoria, Boolean richiesto) {
		Optional<IstruttoriaModel> istruttoriaModelOptional = istruttoriaDao.findById(idIstruttoria);
		DomandaUnicaModel domandaUnicaModel = istruttoriaModelOptional.get().getDomandaUnicaModel();

		return domandaUnicaModel.getAllevamentiImpegnati().stream().map(a4gtRichiestaAllevamDu -> {
			Capi richiestaAllevamDu = new Capi();
			BeanUtils.copyProperties(a4gtRichiestaAllevamDu, richiestaAllevamDu);
			richiestaAllevamDu.setCodiceIntervento(a4gtRichiestaAllevamDu.getIntervento().getCodiceAgea());
			richiestaAllevamDu.setCuaaIntestatario(a4gtRichiestaAllevamDu.getDomandaUnica().getCuaaIntestatario());
			EsitoCapiFilter esitoFilter = new EsitoCapiFilter();
			esitoFilter.setIdAllevamento(a4gtRichiestaAllevamDu.getId());
			ofNullable(richiesto)
			.ifPresent(esitoFilter::setRichiesto);
			richiestaAllevamDu.setCount(esitoCalcoloCapoDao.count(EsitoCapiSpecificationBuilder.getFilter(esitoFilter)));
			return richiestaAllevamDu;
		}
				).collect(Collectors.toList());
	}

	public RisultatiPaginati<Capo> ricercaEsitiCapi(EsitoCapiFilter filter, Paginazione paginazione, Ordinamento ordinamento) {
		Page<EsitoCalcoloCapoModel> page = esitoCalcoloCapoDao.findAll(EsitoCapiSpecificationBuilder.getFilter(filter),
				PageableBuilder.build().from(paginazione, ordinamento));
		List<Capo> risultati = page.stream()
				.map(i -> from(i))
				.collect(Collectors.toList());
		return RisultatiPaginati.of(risultati, page.getTotalElements());		
	}

	private Capo from(EsitoCalcoloCapoModel esitoCalcoloCapoModel) {
		Capo capo = new Capo();
		BeanUtils.copyProperties(esitoCalcoloCapoModel, capo);
		capo.setEsito(esitoCalcoloCapoModel.getEsito().toString());
		// recupero lista risposta json della BDN
		List<JsonNode> clsCapiVacca = esitoCalcoloCapoModel.getA4gtCapoTrackings().stream()
				.map(capi -> mapperWrapper.readValue(capi.getDati(), JsonNode.class))
				.collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(clsCapiVacca)) {
			// creazione capo secondo il modello bdn
			CapoBdn capoBdn = capoBdnBuilder.build(clsCapiVacca, esitoCalcoloCapoModel.getAllevamentoImpegnato().getIntervento().getCodiceAgea());
			// recupera dati di dettaglio del capo provenienti dalla BDN
			capo.setCapoBdn(capoBdn);
		}
		return capo;
	}

	@Transactional
	public Capo modificaCapo(Capo capo) {
		Optional<EsitoCalcoloCapoModel> optCapo = esitoCalcoloCapoDao.findById(capo.getId());
		if (optCapo.isPresent()) {
			EsitoCalcoloCapoModel corrente = optCapo.get();
			corrente.setDuplicato(capo.getDuplicato());
			corrente.setControlloNonSuperato(capo.getControlloNonSuperato());
			esitoCalcoloCapoDao.save(corrente);
		}
		return capo;
	}

	@Transactional(readOnly = true, timeout = 600)
	public Resource getCapiImpegnatiPerAGEA(Integer annoCampagna) throws Exception {
		EsitoCapiFilter filter = new EsitoCapiFilter();
		filter.setCampagna(annoCampagna);
		filter.setRichiesto(true);
		filter.setFetchCapotraking(true);
		List<EsitoCalcoloCapoModel> esitiCalcoloCapi = esitoCalcoloCapoDao.findAll(EsitoCapiSpecificationBuilder.getFilter(filter));
		logger.debug("get Capi Impegnati Per AGEA Tot: {}",esitiCalcoloCapi.size());
		List<ElencoCapiCsv> elencoCapiCsv=esitiCalcoloCapi.stream()
				.distinct() ////distinc messo nello stream perchè nella specification da errore
				.map(esito -> {
					ElencoCapiCsv elencoCapoCsv=new ElencoCapiCsv();
					InformazioniAllevamento informazioniAllevamento=mapperWrapper.readValue(esito.getAllevamentoImpegnato().getDatiAllevamento(), InformazioniAllevamento.class);
					elencoCapoCsv.setIdAlleBdn(informazioniAllevamento.getIdAllevamento().toString());
					elencoCapoCsv.setCodiceAsl(informazioniAllevamento.getCodiceAllevamento());
					elencoCapoCsv.setAnno(esito.getAllevamentoImpegnato().getDomandaUnica().getCampagna().toString());
					elencoCapoCsv.setCodiceFiscaleRichiedente(esito.getAllevamentoImpegnato().getDomandaUnica().getCuaaIntestatario());
					elencoCapoCsv.setCodiceIntervento(esito.getAllevamentoImpegnato().getIntervento().getCodiceAgea());
					elencoCapoCsv.setCodiceMarca(esito.getCodiceCapo());
					elencoCapoCsv.setCodiceOrganismoPagatore("IT25");
					elencoCapoCsv.setIdCapoBdn(String.valueOf(esito.getCapoId()));
					//recupero lista risposta json della BDN
					List<JsonNode> clsCapiVacca=esito.getA4gtCapoTrackings().stream()
							.map(capo -> {
								try {
									return objectMapper.readTree(capo.getDati());
								} catch (IOException e) {
									return null;
								}
							})
							.collect(Collectors.toList());
					if (!CollectionUtils.isEmpty(clsCapiVacca)) {
						//creazione capo secondo il modello bdn
						//recupera data nascita vitello, o data di inizio detenzione per le agnelle
						elencoCapoCsv.setData(capoBdnBuilder.getDataCsv(clsCapiVacca, elencoCapoCsv.getCodiceIntervento()));
					}else {
						logger.warn("Non è possibile recuperare la data nascita vitello, o data di inizio detenzione per le agnelle per il capo {} dell'allevamento {} ",elencoCapoCsv.getIdCapoBdn(),elencoCapoCsv.getCodiceAsl());
					}
					logger.trace("Intervento {}		cod capo {}",elencoCapoCsv.getCodiceIntervento(),elencoCapoCsv.getCodiceMarca());
					return elencoCapoCsv;})
				.collect(Collectors.toList());
		// creazione file CSV in memoria
		CsvSchema elencoCapiSchema = CsvSchema.builder()
				.addColumn("id_capo_bdn", CsvSchema.ColumnType.STRING)
				.addColumn("codice_marca", CsvSchema.ColumnType.STRING)
				.addColumn("codice_intervento", CsvSchema.ColumnType.STRING)
				.addColumn("codice_fiscale_richiedente", CsvSchema.ColumnType.STRING)
				.addColumn("codice_organismo_pagatore", CsvSchema.ColumnType.STRING)
				.addColumn("codice_asl", CsvSchema.ColumnType.STRING)
				.addColumn("id_alle_bdn", CsvSchema.ColumnType.STRING)
				.addColumn("data", CsvSchema.ColumnType.STRING)
				.addColumn("anno", CsvSchema.ColumnType.STRING)
				.build().withColumnSeparator(';').withoutQuoteChar().withHeader();
		CsvMapper mapper = new CsvMapper();
		ObjectWriter elencoCapiWriter = mapper.writerFor(ElencoCapiCsv.class).with(elencoCapiSchema);
		ByteArrayOutputStream csvByteArray = new ByteArrayOutputStream();
		elencoCapiWriter.writeValues(csvByteArray).writeAll(elencoCapiCsv);
		logger.debug("FINE get Capi Impegnati Per AGEA Tot: {}",esitiCalcoloCapi.size());
		return new ByteArrayResource(csvByteArray.toByteArray());
	}

	@Transactional(readOnly = true)
	public List<EtichettaturaCarne> getEtichettaturaCarne(String annoCampagna, String cuaa) throws Exception {
		try {
			A4gtEtichettaturaCarne a4gtEtichettaturaCarneFilter = new A4gtEtichettaturaCarne();
			a4gtEtichettaturaCarneFilter.setCuaa(cuaa);

			List<EtichettaturaCarne> listEtichettaturaCarne = new ArrayList<>();
			List<A4gtEtichettaturaCarne> a4gtEtichettaturaCarneList = etichettaturaCarneDao.findAll(Example.of(a4gtEtichettaturaCarneFilter));

			a4gtEtichettaturaCarneList = a4gtEtichettaturaCarneList.stream()
					.filter(etichettatura -> {
						Calendar dataInizioValidita = Calendar.getInstance();
						dataInizioValidita.setTime(etichettatura.getDtInizioValidita());
						Calendar dataFineValidita = Calendar.getInstance();
						if (etichettatura.getDtFineValidita() == null) {
							dataFineValidita.set(9999, 0, 0);
						} else {
							dataFineValidita.setTime(etichettatura.getDtFineValidita());
						}
						return (dataInizioValidita.get(Calendar.YEAR) < Integer.parseInt(annoCampagna) && dataFineValidita.get(Calendar.YEAR) > Integer.parseInt(annoCampagna));
					})
					.collect(Collectors.toList());

			if (!CollectionUtils.isEmpty(a4gtEtichettaturaCarneList)) {
				listEtichettaturaCarne = a4gtEtichettaturaCarneList.stream().map(a4gtEtichettaturaCarne -> {
					EtichettaturaCarne etichettaturaCarne = new EtichettaturaCarne();
					BeanUtils.copyProperties(a4gtEtichettaturaCarne, etichettaturaCarne);
					return etichettaturaCarne;
				}).collect(Collectors.toList());
			}
			return listEtichettaturaCarne;
		} catch (Exception ex) {
			logger.error("Errore nel reperire i dettagli dell'etichettatura carne per l'azienda {}", cuaa);
			throw ex;
		}
	}

	@Transactional(readOnly = true)
	public List<Alpeggio> getRegistrazioniAlpeggio(Integer annoCampagna, String cuaa) throws Exception {
		try {
			RegistroAlpeggioModel registroAlpeggiofilterModel = new RegistroAlpeggioModel();
			registroAlpeggiofilterModel.setCampagna(annoCampagna);
			registroAlpeggiofilterModel.setCuaa(cuaa);

			List<Alpeggio> listAlpeggio = new ArrayList<>();
			List<RegistroAlpeggioModel> registroAlpeggioModelList = regAlpeggioDao.findAll(Example.of(registroAlpeggiofilterModel));

			if (!CollectionUtils.isEmpty(registroAlpeggioModelList)) {
				listAlpeggio = registroAlpeggioModelList.stream().map(a4gtRegAlpeggio -> {
					Alpeggio alpeggio = new Alpeggio();
					BeanUtils.copyProperties(a4gtRegAlpeggio, alpeggio);
					return alpeggio;
				}).collect(Collectors.toList());
			}
			return listAlpeggio;
		} catch (Exception ex) {
			logger.error("Errore nel reperire i dettagli dell'alpeggio per l'azienda {}", cuaa);
			throw ex;
		}
	}

	@Transactional(readOnly = true)
	public List<ProduzioneLatte> getProduzioneLatte(Integer annoCampagna, String cuaa) throws Exception {
		try {
			ProduzioneLatte produzioneLatte = new ProduzioneLatte();

			RegistroAnalisiLatteModel registroAnalisiLatteModelFilter = new RegistroAnalisiLatteModel();
			registroAnalisiLatteModelFilter.setCampagna(annoCampagna);
			registroAnalisiLatteModelFilter.setCuaa(cuaa);
			List<RegistroAnalisiLatteModel> listaRegistroAnalisiLatteModel = analisiLatteDao.findAll(Example.of(registroAnalisiLatteModelFilter));

			OptionalDouble contenutoProteineCheck = listaRegistroAnalisiLatteModel.stream().mapToDouble(x -> x.getContenutoProteina().doubleValue()).average();
			if (contenutoProteineCheck.isPresent()) {
				produzioneLatte.setContenutoProteine(BigDecimal.valueOf(contenutoProteineCheck.getAsDouble()));
			}

			OptionalDouble tenoreBatteriCheck = listaRegistroAnalisiLatteModel.stream().map(RegistroAnalisiLatteModel::getCaricaBatterica).mapToDouble(x -> Math.log(x.doubleValue())).average();
			if (tenoreBatteriCheck.isPresent()) {
				produzioneLatte.setTenoreBatteri(BigDecimal.valueOf(Math.exp(tenoreBatteriCheck.getAsDouble())));
			}

			OptionalDouble tenoreCelluleCheck = listaRegistroAnalisiLatteModel.stream().map(RegistroAnalisiLatteModel::getCelluleSomatiche).mapToDouble(x -> Math.log(x.doubleValue())).average();
			if (tenoreCelluleCheck.isPresent()) {
				produzioneLatte.setTenoreCellule(BigDecimal.valueOf(Math.exp(tenoreCelluleCheck.getAsDouble())));
			}

			RegistroProduzioneLatteModel registroProduzioneLatteModelFilter = new RegistroProduzioneLatteModel();

			registroProduzioneLatteModelFilter.setCampagna(annoCampagna);
			registroProduzioneLatteModelFilter.setCuaa(cuaa);
			List<RegistroProduzioneLatteModel> listaRegistroProduzioneLatteModel = produzioneLatteDao.findAll(Example.of(registroProduzioneLatteModelFilter));

			Function<Date, Integer> monthMap = date -> {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				return calendar.get(Calendar.MONTH);
			};

			Function<List<Integer>, Map<String, Boolean>> analisiProduzioneLatte = analisiProduzione -> {

				Map<Boolean, List<Integer>> partitioning = Stream.iterate(0, n -> n + 1).limit(12).collect(Collectors.partitioningBy(analisiProduzione::contains));

				Map<String, Boolean> mapProduzioneLatteTrue = partitioning.get(true).stream().map(m -> new DateFormatSymbols(Locale.ITALY).getMonths()[m.intValue()])
						.collect(Collectors.toMap(x -> x, y -> Boolean.TRUE));

				Map<String, Boolean> mapProduzioneLatteFalse = partitioning.get(false).stream().map(m -> new DateFormatSymbols(Locale.ITALY).getMonths()[m.intValue()])
						.collect(Collectors.toMap(x -> x, y -> Boolean.FALSE));

				return Stream.of(mapProduzioneLatteTrue, mapProduzioneLatteFalse).map(Map::entrySet).flatMap(Collection::stream).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			};

			List<Integer> mesiProduzioneLatte = !CollectionUtils.isEmpty(listaRegistroProduzioneLatteModel)
					? listaRegistroProduzioneLatteModel.stream().map(RegistroProduzioneLatteModel::getDtConsVend).map(monthMap).distinct().sorted().collect(Collectors.toList()) : new ArrayList<>();

			List<Integer> mesiAnalisiLatte = !CollectionUtils.isEmpty(listaRegistroAnalisiLatteModel)
					? listaRegistroAnalisiLatteModel.stream().map(RegistroAnalisiLatteModel::getDtAnalisi).map(monthMap).distinct().sorted().collect(Collectors.toList()) : new ArrayList<>();

			produzioneLatte.setMesiAnalisi(analisiProduzioneLatte.apply(mesiAnalisiLatte));
			produzioneLatte.setMesiProduzione(analisiProduzioneLatte.apply(mesiProduzioneLatte));

			List<ProduzioneLatte> produzioneLatteList = new ArrayList<>();
			produzioneLatteList.add(produzioneLatte);

			return produzioneLatteList;
		} catch (Exception ex) {
			logger.error("Errore nel reperire i dettagli di analisi e produzione latte per l'azienda {}", cuaa);
			throw ex;
		}
	}


	/**
	 * Restituisce le statistiche per zootecnia. Esiti capi raggruppati per intervento 
	 * Solo nello stato PRESENTATA
	 * @param annoCampagna
	 * @return
	 */
	public List<StatisticaZootecnia> getCapiPerIntervento(Integer annoCampagna){
		return capiPerInterventoDao.getCapiPerIntervento(annoCampagna);
	}

}