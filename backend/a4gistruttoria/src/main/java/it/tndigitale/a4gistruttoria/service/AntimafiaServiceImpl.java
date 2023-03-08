/**
 * 
 */
package it.tndigitale.a4gistruttoria.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.style.ToStringCreator;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import it.tndigitale.a4g.framework.client.ClientServiceBuilder;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4gistruttoria.action.AvviaControlloAntimafiaConsumer;
import it.tndigitale.a4gistruttoria.action.antimafia.DatiDuHandler;
import it.tndigitale.a4gistruttoria.action.antimafia.ImportaDatiStrutturaliConsumer;
import it.tndigitale.a4gistruttoria.config.DateFormatConfig;
import it.tndigitale.a4gistruttoria.dto.CertificazioneAntimafiaFilter;
import it.tndigitale.a4gistruttoria.dto.CsvFile;
import it.tndigitale.a4gistruttoria.dto.CustomThreadLocal;
import it.tndigitale.a4gistruttoria.dto.DatiElaborazioneProcesso;
import it.tndigitale.a4gistruttoria.dto.DomandaCollegata;
import it.tndigitale.a4gistruttoria.dto.DomandaCollegataFilter;
import it.tndigitale.a4gistruttoria.dto.DomandaUnicheCsvImport;
import it.tndigitale.a4gistruttoria.dto.DomandeCollegateExport;
import it.tndigitale.a4gistruttoria.dto.DomandeCollegateExportBdna;
import it.tndigitale.a4gistruttoria.dto.DomandeCollegateImport;
import it.tndigitale.a4gistruttoria.dto.EsitiBdna;
import it.tndigitale.a4gistruttoria.dto.EsitiBdnaCsvImport;
import it.tndigitale.a4gistruttoria.dto.ImportaDatiStrutturaliHandler;
import it.tndigitale.a4gistruttoria.dto.ImportaDatiStrutturaliHandlerBuilder;
import it.tndigitale.a4gistruttoria.dto.PageResultWrapper;
import it.tndigitale.a4gistruttoria.dto.Pagination;
import it.tndigitale.a4gistruttoria.dto.ProcessoFilter;
import it.tndigitale.a4gistruttoria.dto.SogliaAcquisizioneFilter;
import it.tndigitale.a4gistruttoria.dto.Sort;
import it.tndigitale.a4gistruttoria.dto.TrasmissioneBdnaDto;
import it.tndigitale.a4gistruttoria.dto.TrasmissioneBdnaFilter;
import it.tndigitale.a4gistruttoria.dto.antimafia.DichiarazioneAntimafia;
import it.tndigitale.a4gistruttoria.dto.antimafia.DichiarazioneAntimafiaConEsiti;
import it.tndigitale.a4gistruttoria.dto.antimafia.SogliaAcquisizione;
import it.tndigitale.a4gistruttoria.processo.events.ProcessoEvent;
import it.tndigitale.a4gistruttoria.processo.events.ProcessoPublisher;
import it.tndigitale.a4gistruttoria.processo.exceptions.ProcessoInEsecuzioneException;
import it.tndigitale.a4gistruttoria.repository.dao.DomandeCollegateDao;
import it.tndigitale.a4gistruttoria.repository.dao.ProcessoDao;
import it.tndigitale.a4gistruttoria.repository.dao.SogliaAcquisizioneDao;
import it.tndigitale.a4gistruttoria.repository.dao.TrasmissioneBdnaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDomandeCollegate;
import it.tndigitale.a4gistruttoria.repository.model.A4gtProcesso;
import it.tndigitale.a4gistruttoria.repository.model.A4gtSogliaAcquisizione;
import it.tndigitale.a4gistruttoria.repository.model.A4gtTrasmissioneBdna;
import it.tndigitale.a4gistruttoria.repository.model.DomandeCollegateCsv;
import it.tndigitale.a4gistruttoria.repository.model.DomandeCollegateExportBdnaCsv;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import it.tndigitale.a4gistruttoria.specification.TrasmissioneBdnaSpecification;
import it.tndigitale.a4gistruttoria.strategy.DomandeCollegateFiltrateStrategy;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi;
import it.tndigitale.a4gistruttoria.util.DomandeCollegateExportBdnaCsvConverter;
import it.tndigitale.a4gistruttoria.util.StatoDichiarazioneEnum;
import it.tndigitale.a4gistruttoria.util.StatoDomandaCollegata;
import it.tndigitale.a4gistruttoria.util.TipoDomandaCollegata;
import javassist.NotFoundException;

/**
 * @author B.Conetta
 *
 */
@Service
public class AntimafiaServiceImpl implements AntimafiaService {

	private static final String DECIMAL_FORMAT_CSV = "####.00";
	private static final String DATE_FORMAT_CSV = "yyyyMMdd_HHmmss";
	@Autowired
	private AvviaControlloAntimafiaConsumer avviaControlloAntimafiaConsumer;
	@Autowired
	private ProcessoDao daoProcesso;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DomandeCollegateDao domandeCollegateDao;
	@Autowired
	private ImportaDatiStrutturaliConsumer importaDatiStrutturaliConsumer;
	@Autowired
	private ConsumeExternalRestApi consumeExternalRestApi;
	@Autowired
	private DomandeCollegateFiltrateStrategy domandeCollegateFiltrateStrategy;
	@Autowired
	private ProcessoPublisher processoControlloAntimafiaPublisher;
	@Value("${a4gistruttoria.ags.uri}")
	private String agsUrl;
	@Value("${a4gistruttoria.srt.imprese.importorichiesto.uri}")
	private String urlSrtImpreseImportoRichiesto;
	//	@Value("${a4gistruttoria.proxy.uri}")
	//	private String a4gproxyUrl;
	//	@Autowired
	//	private DateFormatConfig dateFormatConfig;
	@Autowired
	public ClientServiceBuilder clientServiceBuilder;
	@Value("${verificaperiodica.utenzatecnica.username}")
	private String utenzaTecnica;
	@Value("${a4gistruttoria.a4gfascicolo.uri}")
	private String urlA4gfascicolo;
	@Autowired
	private SogliaAcquisizioneDao sogliaAcquisizioneDao;
	@Autowired
	private TrasmissioneBdnaDao trasmissioneBdnaDao;
	@Autowired
	private TrasmissioneBdnaSpecification trasmissioneBdnaSpecification;
	@Autowired
	private UtenteComponent utente;
	@Autowired
	private DatiDuHandler datiDuHandler;
	@Autowired
	private ImportaDatiSuperficieService importaDatiSuperficieService;

	private static Logger logger = LoggerFactory.getLogger(AntimafiaServiceImpl.class);

	private static final int QUERY_LIMIT = 999;

	@Override
	@Async("threadGestioneProcessi")
	public void avviaControllo(List<Long> ids) throws Exception, ProcessoInEsecuzioneException {
		try {
			if (CollectionUtils.isEmpty(ids)) {
				logger.warn("Nessuna domanda antimafia da controllare");
				throw new Exception("Impossibile avviare il controllo massivo, non esistono domande antimafia da controllare");
			}
			DatiElaborazioneProcesso datiElaborazioneProcesso = new DatiElaborazioneProcesso();
			datiElaborazioneProcesso.setTotale("0");
			datiElaborazioneProcesso.setGestite(new ArrayList<>());
			datiElaborazioneProcesso.setConProblemi(new ArrayList<>());
			datiElaborazioneProcesso.setDaElaborare(String.valueOf(ids.size()));

			ProcessoEvent processoEvent = new ProcessoEvent();
			processoEvent.setTipoProcesso(TipoProcesso.CONTROLLO_ANTIMAFIA);
			processoEvent.setStatoProcesso(StatoProcesso.RUN);
			processoEvent.setPercentualeAvanzamento(BigDecimal.ZERO);
			processoEvent.setDtInizio(new Date());
			processoEvent.setDatiElaborazioneProcesso(datiElaborazioneProcesso);
			processoControlloAntimafiaPublisher.handleEvent(processoEvent);

			ids.forEach(avviaControlloAntimafiaConsumer);

			processoEvent = new ProcessoEvent();
			processoEvent.setId((Long) CustomThreadLocal.getVariable("idProcesso"));
			processoEvent.setStatoProcesso(StatoProcesso.OK);
			processoEvent.setDtFine(new Date());
			processoControlloAntimafiaPublisher.handleEvent(processoEvent);
		} catch (Exception ex) {
			if (ex instanceof ProcessoInEsecuzioneException) {
				String warn = "Impossibile procedere poichè è già in corso un movimento di controllo massivo. Riprovare più tardi.";
				logger.warn(warn);
				throw new Exception(warn);
			}
			logger.error("Errore nell'esecuzione del processo di controllo massivo per le Domande Antimafia", ex);
			throw ex;
		}
	}


	@Override
	@Transactional(readOnly = true)
	public DatiElaborazioneProcesso getElaborazioneControlloAntimafia(Long idAntimafia) throws Exception {
		DatiElaborazioneProcesso datiProcesso = null;
		try {
			A4gtProcesso processo = daoProcesso.getProcessoByIdAntimafia(idAntimafia, TipoProcesso.CONTROLLO_ANTIMAFIA, StatoProcesso.OK);
			if (processo == null) {
				String str = String.format("Attenzione! ID_ANTIMAFIA: %d con tipo processo %s e stato processo %s non trovato nella colonna DATI_ELABORAZIONI della tabella A4GT_PROCESSO ",
						idAntimafia, TipoProcesso.CONTROLLO_ANTIMAFIA, StatoProcesso.OK);
				logger.warn(str);
				throw new EntityNotFoundException(
						String.format("Nessun Processo trovato per ID_ANTIMAFIA: %d con tipo processo %s e stato processo %s ", idAntimafia, TipoProcesso.CONTROLLO_ANTIMAFIA, StatoProcesso.OK));
			}
			datiProcesso = objectMapper.readValue(processo.getDatiElaborazione(), DatiElaborazioneProcesso.class);
			// Filtro le domande antimafia per ID perche' potrebbero esserci piu' domande elaborate per lo stesso processo
			if (!Strings.isNullOrEmpty(datiProcesso.getTotale()) && Integer.parseInt(datiProcesso.getTotale()) > 1) {
				List<String> listDomandeGestite = new ArrayList<>();
				Optional<String> domandaFiltrata = datiProcesso.getGestite().stream().findFirst().filter(domanda -> domanda.indexOf(String.valueOf(idAntimafia)) != -1);
				if (domandaFiltrata.isPresent()) {
					listDomandeGestite.add(domandaFiltrata.get());
					datiProcesso.setGestite(listDomandeGestite);
				}
			}
		} catch (Exception ex) {
			logger.error("Errore nel recupero dei dati elaborati per il controllo della domanda antimafia");
			throw ex;
		}
		return datiProcesso;
	}

	@Override
	public List<DomandaCollegata> importaDatiStrutturali(DomandeCollegateImport domandeCollegateImport) throws Exception {

		Assert.notNull(domandeCollegateImport, () -> {
			logger.warn("L'input del servizio importaDatiStrutturali non deve essere null.");
			return "Errore nell' importazione dei dati strutturali";
		});
		List<String> cuaaList = Objects.requireNonNull(domandeCollegateImport.getCuaa(), () -> {
			logger.warn("Nessun cuaa fornito in input per il servizio importaDatiStrutturali.");
			return "Errore nell' importazione dei dati strutturali";
		});
		List<DomandaCollegata> domandeCollegate = new ArrayList<>();
		cuaaList.forEach(cuaa -> {
			ImportaDatiStrutturaliHandler importaDatiStrutturali = new ImportaDatiStrutturaliHandlerBuilder().with(importaDatiStrutturaliHandler -> {
				importaDatiStrutturaliHandler.dataPresentazione = domandeCollegateImport.getDataPresentazione();
				importaDatiStrutturaliHandler.importo = domandeCollegateImport.getImporto();
				importaDatiStrutturaliHandler.cuaa = cuaa;
			}).build();

			importaDatiStrutturaliConsumer.accept(importaDatiStrutturali);
			if (importaDatiStrutturali.getDomandaCollegata() != null) {
				domandeCollegate.add(importaDatiStrutturali.getDomandaCollegata());
			}
		});

		return domandeCollegate;
	}

	@Override
	public List<DomandaCollegata> importaDatiDU(DomandeCollegateImport domandeCollegateImport, MultipartFile csv) throws Exception {
		List<DomandaUnicheCsvImport> domandeUnicheCsvImports = new ArrayList<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(csv.getInputStream()));
		try (Stream<String> stream = br.lines().skip(1)) {
			stream.forEach(linea -> domandeUnicheCsvImports.add(new DomandaUnicheCsvImport(linea)));
		} catch (Exception e) {
			logger.error("Fallita la lettura del file csv", e);
			throw new RuntimeException("Fallita la lettura del file csv");
		}

		String righeErrate = "";
		for (Integer i = 0; i < domandeUnicheCsvImports.size(); i++) {
			if (!domandeUnicheCsvImports.get(i).validate()) {
				righeErrate = righeErrate.concat(Integer.toString(i + 2)).concat(",");
			}
		}
		if (!righeErrate.isEmpty()) {
			throw new RuntimeException("Le seguenti righe non rispettano il tracciato standard: ".concat(righeErrate.substring(0, righeErrate.length() - 1)));
		}
		List<DomandaCollegata> domandeCollegate = new ArrayList<>();
		domandeUnicheCsvImports.forEach(domandaUnicaCsvImport -> {
			datiDuHandler.elaboraDomanda(domandaUnicaCsvImport, domandeCollegate, domandeCollegateImport);
		});

		return domandeCollegate;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TrasmissioneBdnaDto> getTrasmissioniBdna(TrasmissioneBdnaFilter trasmissioneBdnaFilter) throws Exception {

		List<A4gtTrasmissioneBdna> trasmissioni = trasmissioneBdnaDao.findAll(trasmissioneBdnaSpecification.getFilter(trasmissioneBdnaFilter));
		List<TrasmissioneBdnaDto> trasmissioniOut = new ArrayList<>();
		trasmissioni.stream().forEach(trasmissione -> {
			TrasmissioneBdnaDto trasmissioneBdnaDto = new TrasmissioneBdnaDto();
			BeanUtils.copyProperties(trasmissione, trasmissioneBdnaDto);
			trasmissioniOut.add(trasmissioneBdnaDto);
		});
		return trasmissioniOut;
	}

	@Override
	@Async("threadGestioneProcessi")
	public void importaDatiSuperficie(DomandeCollegateImport domandeCollegateImport) throws Exception {
		String errore = domandeCollegateImport.validate();
		if (errore != null && !errore.isEmpty()) {
			throw new RuntimeException(errore);
		}

		importaDatiSuperficieService.importaDatiSuperficie(domandeCollegateImport);
	}

	@Override
	@Transactional(readOnly = true)
	public PageResultWrapper<DichiarazioneAntimafiaConEsiti> getDomandeCollegateConEsiti(CertificazioneAntimafiaFilter filter,Pagination pagination,Sort sort) throws Exception {
		try {
			PageResultWrapper<DichiarazioneAntimafia> dichiarazioniAntimafiaPage = consumeExternalRestApi
					.getDichiarazioniAntimafiaPage(filter,pagination,sort);
			List<DichiarazioneAntimafia> dichiarazioniAntimafia=dichiarazioniAntimafiaPage.getResults();
			List<String> listaCuaa = dichiarazioniAntimafia.stream().map(dichiarazione -> dichiarazione.getDatiDichiarazione().getDettaglioImpresa().getCodiceFiscale()).collect(Collectors.toList());

			final List<List<String>> subLists = Lists.partition(listaCuaa, QUERY_LIMIT);
			List<A4gtDomandeCollegate> domandeCollegate = new ArrayList<A4gtDomandeCollegate>();
			subLists.forEach(subList -> domandeCollegate.addAll(domandeCollegateDao.findByCuaaIn(subList)));
			
			List<DichiarazioneAntimafiaConEsiti> esiti=dichiarazioniAntimafia.stream().map(dichiarazione -> {
				List<A4gtDomandeCollegate> a4gtDomandeCollegate = domandeCollegate.stream()
						.filter(domanda -> domanda.getCuaa().equals(dichiarazione.getDatiDichiarazione().getDettaglioImpresa().getCodiceFiscale())).collect(Collectors.toList());
				DichiarazioneAntimafiaConEsiti dac = domandeCollegateFiltrateStrategy.getDomanda(a4gtDomandeCollegate, filter.getStato());
				BeanUtils.copyProperties(dichiarazione, dac);

				return dac;

			}).collect(Collectors.toList());
			PageResultWrapper<DichiarazioneAntimafiaConEsiti> pageResultWrapper=new PageResultWrapper<>();
			pageResultWrapper.setResults(esiti);
			pageResultWrapper.setPagSize(dichiarazioniAntimafiaPage.getPagSize());
			pageResultWrapper.setPagStart(dichiarazioniAntimafiaPage.getPagStart());
			pageResultWrapper.setTotal(dichiarazioniAntimafiaPage.getTotal());
			return pageResultWrapper;
		} catch (Exception e) {
			logger.error(String.format("Errore nella restituzione delle domande collegate filtrate per stato %s", filter.getStato()));
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public DomandaCollegata getDettaglioDomandeCollegate(Long id) throws Exception {
		DomandaCollegata domandaCollegataOut = new DomandaCollegata();
		try {
			A4gtDomandeCollegate domandaCollegataIn = domandeCollegateDao.getOne(id);
			BeanUtils.copyProperties(domandaCollegataIn, domandaCollegataOut);
			domandaCollegataOut.setStatoBdna(domandaCollegataIn.getStatoBdna().toString());
			domandaCollegataOut.setTipoDomanda(domandaCollegataIn.getTipoDomanda().toString());
		} catch (Exception e) {
			logger.error("Errore nel recupero del dettaglio della domanda con ID {}", id);
			throw e;
		}
		return domandaCollegataOut;
	}

	@Override
	@Transactional
	public CsvFile creaCsvFile(DomandeCollegateExport domandeCollegateExport) throws Exception {
		List<StatoDomandaCollegata> statiDomandaCollegataDaEsportare = Arrays.asList(StatoDomandaCollegata.NON_CARICATO , StatoDomandaCollegata.ANOMALIA);
		
		// ORA-01795: maximum number of expressions in a list is 1000
		final List<List<String>> subLists = Lists.partition(domandeCollegateExport.getCuaa(), QUERY_LIMIT);
		List<A4gtDomandeCollegate> domandeCollegate = new ArrayList<A4gtDomandeCollegate>();
		
		if (TipoDomandaCollegata.PSR_SUPERFICIE_EU.equals(domandeCollegateExport.getTipoDomanda())) {
			subLists.forEach(subList -> domandeCollegate.addAll(domandeCollegateDao.findByCuaaInAndStatoBdnaInAndTipoDomandaAndCampagnaInAndA4gtTrasmissioneBdnaIsNull(subList, statiDomandaCollegataDaEsportare,
					domandeCollegateExport.getTipoDomanda(), domandeCollegateExport.getAnniCampagna())));
		} else {
			subLists.forEach(subList -> domandeCollegate.addAll(domandeCollegateDao.findByCuaaInAndStatoBdnaInAndTipoDomandaAndA4gtTrasmissioneBdnaIsNull(subList, statiDomandaCollegataDaEsportare, domandeCollegateExport.getTipoDomanda())));
		}

		CsvFile csvFile = creazioneCsv(domandeCollegateExport.getTipoDomanda(), domandeCollegate, new Date());

		// creazione nuova trasmissione
		A4gtTrasmissioneBdna a4gtTrasmissioneBdna = new A4gtTrasmissioneBdna();
		a4gtTrasmissioneBdna.setCfOperatore(utente.utenza());
		a4gtTrasmissioneBdna.setDtCreazione(new Date());
		a4gtTrasmissioneBdna.setTipoDomanda(domandeCollegateExport.getTipoDomanda());
		A4gtTrasmissioneBdna trasmissioneSaved = trasmissioneBdnaDao.save(a4gtTrasmissioneBdna);

		// aggiornamento dati DB
		domandeCollegate.forEach(domanda -> {
			domanda.setA4gtTrasmissioneBdna(trasmissioneSaved);
		});
		domandeCollegateDao.saveAll(domandeCollegate);
		logger.info("Trasmissione con id {} creata dall'utente {}", trasmissioneSaved.getId() , utente.utenza());
		return csvFile;
	}

	private CsvFile creazioneCsv(TipoDomandaCollegata tipoDomanda, List<A4gtDomandeCollegate> domandeCollegate, Date dataCreazione) throws IOException {
		if (CollectionUtils.isEmpty(domandeCollegate)) {
			throw new IllegalArgumentException("Non esistono domande collegate da esportare");
		}

		DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT_CSV);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(dfs);
		List<DomandeCollegateCsv> domandeCollegateCsv = domandeCollegate.stream().map(domanda -> new DomandeCollegateCsv(domanda.getCuaa(), "IS1", df.format(domanda.getImportoRichiesto())))
				.collect(Collectors.toList());
		// creazione file CSV in memoria
		CsvSchema domandeCollegateSchema = CsvSchema.builder().addColumn("CODI_FISC", CsvSchema.ColumnType.STRING).addColumn("TIPO_RICH", CsvSchema.ColumnType.STRING)
				.addColumn("NUME_IMPO", CsvSchema.ColumnType.STRING).build().withColumnSeparator(';').withoutQuoteChar().withHeader();
		CsvMapper mapper = new CsvMapper();
		ObjectWriter domandeCollegateWriter = mapper.writerFor(DomandeCollegateCsv.class).with(domandeCollegateSchema);
		ByteArrayOutputStream csvByteArray = new ByteArrayOutputStream();
		domandeCollegateWriter.writeValues(csvByteArray).writeAll(domandeCollegateCsv);
		CsvFile csvFile = new CsvFile();
		csvFile.setCsvByteArray(csvByteArray.toByteArray());
		// creazione file name secondo formato BR
		StringBuilder filename = new StringBuilder();
		filename.append(utente.utenza()).append("_Flusso_");
		switch (tipoDomanda) {
		case DOMANDA_UNICA:
			filename.append("DU_");
			break;
		case PSR_STRUTTURALI_EU:
			filename.append("PSR_STR_");
			break;
		case PSR_SUPERFICIE_EU:
			filename.append("PSR_SUP_");
			break;
		}
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT_CSV);
		filename.append(formatter.format(dataCreazione));
		filename.append(".csv");
		csvFile.setCsvFileName(filename.toString());
		return csvFile;
	}

	@Override
	public TrasmissioneBdnaDto aggiornaTrasmissioneBdna(TrasmissioneBdnaDto trasmissioneBdnaDto) throws Exception {

		// query per capire che trasmissioni aggiornare per id? 
		Optional<A4gtTrasmissioneBdna> trasmissioneToUpdateOpt = trasmissioneBdnaDao.findById(trasmissioneBdnaDto.getId());

		if (!trasmissioneToUpdateOpt.isPresent()) {
			throw new NotFoundException("Trasmissione bdna con id ".concat(trasmissioneBdnaDto.getId().toString()).concat(" non trovata"));
		}
		A4gtTrasmissioneBdna trasmissioneToUpdate = trasmissioneToUpdateOpt.get();

		// dtCreazione deve essere immutabile
		BeanUtils.copyProperties(trasmissioneBdnaDto, trasmissioneToUpdate, "dtCreazione");

		//dtConferma corrisponde allo stato CONFERMATA (CARICATO) per ogni domanda collegata alla trasmissione corrente
		if(trasmissioneBdnaDto.getDtConferma() != null) {
			List<A4gtDomandeCollegate> domandeCollegate = trasmissioneToUpdate.getA4gtDomandeCollegates();
			domandeCollegate.forEach(domandaCollegata -> {
				domandaCollegata.setStatoBdna(StatoDomandaCollegata.CARICATO);
			});
			domandeCollegateDao.saveAll(domandeCollegate);
		}



		TrasmissioneBdnaDto trasmissioneBdnaOut = new TrasmissioneBdnaDto();
		try {
			A4gtTrasmissioneBdna trasmissioneSalvata = trasmissioneBdnaDao.save(trasmissioneToUpdate);
			ToStringCreator resume = new ToStringCreator(trasmissioneSalvata)
					.append("dtCreazione", trasmissioneSalvata.getDtCreazione())
					.append("cfOperatore", trasmissioneSalvata.getCfOperatore())
					.append("tipoDomanda", trasmissioneSalvata.getTipoDomanda())
					.append("dtConferma" , trasmissioneSalvata.getDtConferma());
			BeanUtils.copyProperties(trasmissioneSalvata, trasmissioneBdnaOut);
			logger.info("Trasmissione con id {} confermata dall'utente {}", trasmissioneBdnaOut.getId() , trasmissioneBdnaOut.getCfOperatore());
			logger.info("[Conferma Caricamento] - Conferma invio file csv a BDNA : {}", resume);
			return trasmissioneBdnaOut;
		} catch (Exception e) {
			logger.error("Errore durante il salvataggio della trasmissione bdna " , e);
			throw new Exception("Errore durante il salvataggio della trasmissione bdna");
		}
	}

	@Override
	public void cancellaTrasmissioneBdna(Long idTrasmissione) throws Exception {
		A4gtTrasmissioneBdna trasmissione = trasmissioneBdnaDao.findById(idTrasmissione)
				.orElseThrow(() -> new NotFoundException("trasmissione con id ".concat(idTrasmissione.toString()).concat(" non trovata")));
		ToStringCreator resume = new ToStringCreator(trasmissione)
				.append("dtCreazione", trasmissione.getDtCreazione())
				.append("cfOperatore", trasmissione.getCfOperatore())
				.append("tipoDomanda", trasmissione.getTipoDomanda());
		try {
			List<A4gtDomandeCollegate> a4gtDomandeCollegates = trasmissione.getA4gtDomandeCollegates();
			a4gtDomandeCollegates.forEach(domandaCollegata -> {
				domandaCollegata.setA4gtTrasmissioneBdna(null);
				domandeCollegateDao.save(domandaCollegata);
			});
			trasmissioneBdnaDao.deleteById(idTrasmissione);
			logger.info("Trasmissione con id {} cancellata dall'utente {}", idTrasmissione , utente.utenza());
			logger.info("[Annulla Caricamento] - Eliminazione riferimento al file precedentemente scaricato : {}", resume);
		} catch (Exception e) {
			logger.error("Errore durante la delete della trasmissione bdna ".concat(idTrasmissione.toString()) , e);
			throw new Exception("Errore durante la delete della trasmissione bdna".concat(idTrasmissione.toString()));
		}
	}

	@Override
	@Transactional
	public DomandaCollegata aggiornaDomandaCollegate(DomandaCollegata domandaCollegata) throws Exception {
		A4gtDomandeCollegate domandaCollegateToUpdate = domandeCollegateDao.getOne(domandaCollegata.getId());
		BeanUtils.copyProperties(domandaCollegata, domandaCollegateToUpdate);
		domandaCollegateToUpdate.setTipoDomanda(TipoDomandaCollegata.valueOf(domandaCollegata.getTipoDomanda()));
		domandaCollegateToUpdate.setStatoBdna(StatoDomandaCollegata.valueOf(domandaCollegata.getStatoBdna()));
		DomandaCollegata domandaCollegataOut = new DomandaCollegata();
		if(domandaCollegateToUpdate.getStatoBdna().equals(StatoDomandaCollegata.ANOMALIA)) {
			domandaCollegateToUpdate.setA4gtTrasmissioneBdna(null);
		}
		try {
			A4gtDomandeCollegate domandaCollegataUpdated = domandeCollegateDao.save(domandaCollegateToUpdate);
			BeanUtils.copyProperties(domandaCollegataUpdated, domandaCollegataOut);
			domandaCollegataOut.setStatoBdna(domandaCollegataUpdated.getStatoBdna().toString());
			domandaCollegataOut.setTipoDomanda(domandaCollegataUpdated.getTipoDomanda().toString());
		} catch (Exception e) {
			logger.error("Errore nel salvataggio del dettaglio della domanda con ID {}", domandaCollegata.getId());
			throw e;
		}
		return domandaCollegataOut;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DomandaCollegata> getDomandeCollegate(DomandaCollegataFilter domandaCollegataFilter) throws Exception {
		A4gtDomandeCollegate domandeCollegateIn = new A4gtDomandeCollegate();
		BeanUtils.copyProperties(domandaCollegataFilter, domandeCollegateIn);

		List<A4gtDomandeCollegate> domandeCollegate = domandeCollegateDao.findAll(Example.of(domandeCollegateIn));
		if (!CollectionUtils.isEmpty(domandeCollegate)) {
			return domandeCollegate.stream().map(a4gtDomandeCollegate -> {
				DomandaCollegata domandaCollegata = new DomandaCollegata();
				BeanUtils.copyProperties(a4gtDomandeCollegate, domandaCollegata);
				domandaCollegata.setStatoBdna(a4gtDomandeCollegate.getStatoBdna().toString());
				domandaCollegata.setTipoDomanda(a4gtDomandeCollegate.getTipoDomanda().toString());
				return domandaCollegata;
			}).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	@Transactional
	public DatiElaborazioneProcesso getStatoAvanzamento(ProcessoFilter processoFilter) throws Exception {
		DatiElaborazioneProcesso datiElaborazioneProcesso = new DatiElaborazioneProcesso();
		A4gtProcesso a4gtProcesso = new A4gtProcesso();

		try {
			a4gtProcesso.setStato(processoFilter.getStatoProcesso());
			a4gtProcesso.setTipo(processoFilter.getTipoProcesso());

			Optional<A4gtProcesso> a4gtProcessoTrovato = daoProcesso.findOne(Example.of(a4gtProcesso));

			if (!a4gtProcessoTrovato.isPresent()) {
				datiElaborazioneProcesso.setEsito("Non ci sono in corso processi di controllo");
			} else {
				datiElaborazioneProcesso = objectMapper.readValue(a4gtProcessoTrovato.get().getDatiElaborazione(), DatiElaborazioneProcesso.class);
			}

			return datiElaborazioneProcesso;

		} catch (Exception e) {
			logger.error(
					String.format("Errore nella ricerca dei dati elaborazione del processo in esecuzione con stato %s e tipo %s", processoFilter.getStatoProcesso(), processoFilter.getTipoProcesso()));
			throw e;
		}
	}

	//	@Scheduled(cron = "${cron.expression.verificaperiodica}")
	//	@Override
	//	@Transactional
	//	public void passaggioVerificaPeriodica() throws Exception {
	//		logger.info("Passaggio Verifica Periodica in data {} ", Calendar.getInstance());
	//		RestTemplate restTemplateUtenzaTecnica = getRestTemplateUtenzaApplicativa();
	//		HttpHeaders headers = new HttpHeaders();
	//		headers.setContentType(MediaType.APPLICATION_JSON);
	//		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
	//		String params = "?params="
	//				.concat(URLEncoder.encode(String.format("{\"statiDichiarazione\": [\"%s\",\"%s\",\"%s\"]}",
	//						StatoDichiarazioneEnum.CONTROLLATA.getIdentificativoStato(), StatoDichiarazioneEnum.CONTROLLO_MANUALE.getIdentificativoStato(),
	//						StatoDichiarazioneEnum.PROTOCOLLATA.getIdentificativoStato()), StandardCharsets.UTF_8.name()));
	//		List<DichiarazioneAntimafia> dichiarazioniAntimafia = restTemplateUtenzaTecnica
	//				.exchange(new URI(urlA4gfascicolo.concat("antimafia").concat("/").concat(params)), HttpMethod.GET, entity, new ParameterizedTypeReference<List<DichiarazioneAntimafia>>() {
	//				}).getBody();
	//		if (CollectionUtils.isEmpty(dichiarazioniAntimafia)) {
	//			logger.debug("Nessuna dichiarazione Antimafia trovata su cui fare il passaggio in Verifica Periodica");
	//			return;
	//		}
	//		dichiarazioniAntimafia.stream()
	//		// escludi passaggio automatico per domande in esito positivo AM-03-07-01REV 
	//		.filter(dichiarazione -> !(dichiarazione.getStato().getIdentificativo().equals(StatoDichiarazioneEnum.POSITIVO.getIdentificativoStato())))
	//		.forEach(dichiarazione -> {
	//			try {
	//				String sincronizzazione = restTemplateUtenzaTecnica.getForObject(new URI(a4gproxyUrl.concat("sincronizzazione/antimafia/").concat(dichiarazione.getId().toString())), String.class);
	//				if (!StringUtils.isEmpty(sincronizzazione)) {
	//					JsonNode jsonSincronizzazione = objectMapper.readTree(sincronizzazione);
	//					Date dataInizVali = dateFormatConfig.convertiJsonIsoDate(jsonSincronizzazione.get("dataInizVali").asText());
	//					Calendar calDataInizVali = Calendar.getInstance();
	//					calDataInizVali.setTime(dataInizVali);
	//					// adding days into Date in Java
	//					calDataInizVali.add(Calendar.DATE, 180);
	//					if (Calendar.getInstance().compareTo(calDataInizVali) > 0) {
	//						logger.info("Passaggio Verifica Periodica dichiarazione con id {} ", dichiarazione.getId());
	//						// passaggio a VERIFICA PERIODICA
	//						dichiarazione.getStato().setId(null);
	//						dichiarazione.getStato().setDescrizione(null);
	//						dichiarazione.getStato().setIdentificativo(StatoDichiarazioneEnum.VERIFICA_PERIODICA.getIdentificativoStato());
	//						URI uriFascicolo = new URI(urlA4gfascicolo.concat("antimafia").concat("/").concat(dichiarazione.getId().toString()));
	//						HttpEntity<String> entityPut = new HttpEntity<>(objectMapper.writeValueAsString(objectMapper.valueToTree(dichiarazione)), headers);
	//						logger.debug("Passaggio Verifica Periodica della dichiarazione antimafia");
	//						restTemplateUtenzaTecnica.exchange(uriFascicolo, HttpMethod.PUT, entityPut, String.class);
	//						// aggiornamento DATA_FINE_VALI a sysdate
	//						((ObjectNode) (jsonSincronizzazione)).put("dataFineVali", dateFormatConfig.convertiDataFull((new Date()).getTime()));
	//						URI uriSincronizzazione = new URI(a4gproxyUrl.concat("sincronizzazione/antimafia/").concat(dichiarazione.getId().toString()));
	//						HttpEntity<String> entitySincronizzazione = new HttpEntity<>(objectMapper.writeValueAsString(jsonSincronizzazione), headers);
	//						logger.debug("Sincronizzazione: Aggiornamento DATA_FINE_VALI a sysdate");
	//						restTemplateUtenzaTecnica.exchange(uriSincronizzazione, HttpMethod.PUT, entitySincronizzazione, String.class);
	//					}
	//				}
	//			} catch (Exception e) {
	//				logger.error("Errore passaggio automatico a verifica periodica dichiarazione con ID {}", dichiarazione.getId(), e);
	//			}
	//		});
	//	}
	//

	@Scheduled(cron = "${cron.expression.sincronizzazioneags}")
	@Override
	@Transactional
	public void sincronizzazioneCertificazioniAntimafiaAgs() throws Exception {
		logger.info("Sincronizzazione Certificazioni Antimafia Ags {} ", new Date());
		CertificazioneAntimafiaFilter filter=new CertificazioneAntimafiaFilter();
		filter.setStato(StatoDichiarazioneEnum.CONTROLLATA);
		PageResultWrapper<DichiarazioneAntimafia> dichiarazioniAntimafiaPage = consumeExternalRestApi
				.getDichiarazioniAntimafiaPage(filter,null,null,getRestTemplateUtenzaApplicativa());
		List<DichiarazioneAntimafia> dichiarazioniAntimafia = dichiarazioniAntimafiaPage.getResults();
		if (CollectionUtils.isEmpty(dichiarazioniAntimafia)) {
			logger.info("Nessuna dichiarazione Antimafia trovata su cui fare Sincronizzazione Certificazioni Antimafia Ags");
			return;
		}
		List<String> listaCuaa = dichiarazioniAntimafia.stream().map(val -> val.getAzienda().getCuaa()).collect(Collectors.toList());
		consumeExternalRestApi.sincronizzaConAgs(listaCuaa, getRestTemplateUtenzaApplicativa());
	}


	public RestTemplate getRestTemplateUtenzaApplicativa() {
		return clientServiceBuilder.buildWith(() -> utenzaTecnica);

	}

	@Override
	@Transactional
	public SogliaAcquisizione getSogliaAcquisizione(SogliaAcquisizioneFilter sogliaAcquisizioneFilter) throws Exception {
		A4gtSogliaAcquisizione a4gtSogliaAcquisizione = new A4gtSogliaAcquisizione();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date sysdate = cal.getTime();
		a4gtSogliaAcquisizione.setSettore(sogliaAcquisizioneFilter.getSettore());
		A4gtSogliaAcquisizione a4gtSogliaAcquisizioneResult = sogliaAcquisizioneDao.findBySettoreAndDate(sogliaAcquisizioneFilter.getSettore(),sysdate); 
		if(a4gtSogliaAcquisizioneResult == null) {
			throw new Exception(String.format("Nessuna tipologia di domanda con tipo: %s", sogliaAcquisizioneFilter.getSettore()));
		}
		else {
			SogliaAcquisizione sogliaAcquisizione = new SogliaAcquisizione();
			BeanUtils.copyProperties(a4gtSogliaAcquisizioneResult, sogliaAcquisizione);
			return sogliaAcquisizione;
		}
	}

	@Override
	@Transactional
	public EsitiBdna importaEsitiBDNA(MultipartFile csv) throws Exception {
		List<EsitiBdnaCsvImport> esitiBdnaCsvImports = new ArrayList<>();
		BufferedReader br = new BufferedReader(new InputStreamReader(csv.getInputStream()));
		try (Stream<String> stream = br.lines().skip(1)) {
			stream.forEach(linea -> esitiBdnaCsvImports.add(new EsitiBdnaCsvImport(linea)));
		} catch (Exception e) {
			logger.error("Fallita la lettura del file csv", e);
			throw new RuntimeException("Fallita la lettura del file csv");
		}
		String righeErrate = "";
		for (Integer i = 0; i < esitiBdnaCsvImports.size(); i++) {
			if (!esitiBdnaCsvImports.get(i).validate()) {
				righeErrate = righeErrate.concat(Integer.toString(i + 1)).concat(",");
			}
		}
		if (!righeErrate.isEmpty()) {
			throw new RuntimeException("Le seguenti righe non rispettano il tracciato standard: ".concat(righeErrate.substring(0, righeErrate.length() - 1)));
		}
		List<String> cuaaErrati = new ArrayList<>();
		List<String> cuaaCorretti = new ArrayList<>();
		List<DomandaCollegata> domandeCollegate = new ArrayList<>();
		RestTemplate restTemplateUtenzaTecnica = getRestTemplateUtenzaApplicativa();
		for (EsitiBdnaCsvImport esitiBdnaCsvImport : esitiBdnaCsvImports) {
			A4gtDomandeCollegate filter = new A4gtDomandeCollegate();
			filter.setCuaa(esitiBdnaCsvImport.getCodiceFiscale());
			filter.setImportoRichiesto(esitiBdnaCsvImport.getValoreAppalto());
			List<A4gtDomandeCollegate> domande = domandeCollegateDao.findAll(Example.of(filter));
			if(domande == null || domande.isEmpty() || domande.size() > 1) {
				cuaaErrati.add(esitiBdnaCsvImport.getCodiceFiscale());
			}
			else {
				A4gtDomandeCollegate a4gtDomandeCollegate;
				a4gtDomandeCollegate = domande.get(0);
				//CTRLIAMCNT012
				if(a4gtDomandeCollegate.getStatoBdna().compareTo(StatoDomandaCollegata.NON_CARICATO) != 0) {
					//IN LAVORAZIONE
					if(esitiBdnaCsvImport.getStatoRichiesta().compareTo(StatoDomandaCollegata.IN_LAVORAZIONE.getStatoDomandaCollegata()) == 0) {
						if(esitiBdnaCsvImport.getNumProtocollo().isEmpty()) {
							cuaaErrati.add(esitiBdnaCsvImport.getCodiceFiscale());
						}
						else {
							Calendar cal = Calendar.getInstance();
							cal.setTime(esitiBdnaCsvImport.getDataInserimento());
							cal.add(Calendar.DATE, 30);
							a4gtDomandeCollegate.setDtInizioSilenzioAssenso(cal.getTime());
							cal.setTime(esitiBdnaCsvImport.getDataInserimento());
							cal.add(Calendar.DATE, 210);
							a4gtDomandeCollegate.setDtFineSilenzioAssenso(cal.getTime());
							a4gtDomandeCollegate.setDtInizioEsitoNegativo(null);
							a4gtDomandeCollegate.setDtFineEsitoNegativo(null);
							a4gtDomandeCollegate.setDtBdna(esitiBdnaCsvImport.getDataInserimento());
							a4gtDomandeCollegate.setProtocollo(esitiBdnaCsvImport.getNumProtocollo());
							a4gtDomandeCollegate.setStatoBdna(StatoDomandaCollegata.IN_LAVORAZIONE);
							domandeCollegateDao.save(a4gtDomandeCollegate);
							DomandaCollegata domandaCollegata = new DomandaCollegata();
							BeanUtils.copyProperties(a4gtDomandeCollegate, domandaCollegata);
							domandaCollegata.setStatoBdna(a4gtDomandeCollegate.getStatoBdna().toString());
							domandaCollegata.setTipoDomanda(a4gtDomandeCollegate.getTipoDomanda().toString());
							domandeCollegate.add(domandaCollegata);
							cuaaCorretti.add(domandaCollegata.getCuaa());
						}
					} 
					//IN_ISTRUTTORIA
					if(esitiBdnaCsvImport.getStatoRichiesta().compareTo(StatoDomandaCollegata.IN_ISTRUTTORIA.getStatoDomandaCollegata()) == 0) {
						if(esitiBdnaCsvImport.getNumProtocollo().isEmpty()) {
							cuaaErrati.add(esitiBdnaCsvImport.getCodiceFiscale());
						}
						else {
							Calendar cal = Calendar.getInstance();
							cal.setTime(esitiBdnaCsvImport.getDataInserimento());
							cal.add(Calendar.DATE, 30);
							a4gtDomandeCollegate.setDtInizioSilenzioAssenso(cal.getTime());
							cal.setTime(esitiBdnaCsvImport.getDataInserimento());
							cal.add(Calendar.DATE, 210);
							a4gtDomandeCollegate.setDtFineSilenzioAssenso(cal.getTime());
							a4gtDomandeCollegate.setDtInizioEsitoNegativo(null);
							a4gtDomandeCollegate.setDtFineEsitoNegativo(null);
							a4gtDomandeCollegate.setDtBdna(esitiBdnaCsvImport.getDataInserimento());
							a4gtDomandeCollegate.setProtocollo(esitiBdnaCsvImport.getNumProtocollo());
							a4gtDomandeCollegate.setStatoBdna(StatoDomandaCollegata.IN_ISTRUTTORIA);
							domandeCollegateDao.save(a4gtDomandeCollegate);
							DomandaCollegata domandaCollegata = new DomandaCollegata();
							BeanUtils.copyProperties(a4gtDomandeCollegate, domandaCollegata);
							domandaCollegata.setStatoBdna(a4gtDomandeCollegate.getStatoBdna().toString());
							domandaCollegata.setTipoDomanda(a4gtDomandeCollegate.getTipoDomanda().toString());
							domandeCollegate.add(domandaCollegata);
							cuaaCorretti.add(domandaCollegata.getCuaa());
						}
					}
					//ATTI
					if(esitiBdnaCsvImport.getStatoRichiesta().compareTo(StatoDomandaCollegata.ATTI.getStatoDomandaCollegata()) == 0) {
						if(esitiBdnaCsvImport.getNumProtocollo().isEmpty()) {
							cuaaErrati.add(esitiBdnaCsvImport.getCodiceFiscale());
						}
						else {
							Calendar cal = Calendar.getInstance();
							cal.setTime(esitiBdnaCsvImport.getDataInserimento());
							cal.add(Calendar.DATE, 30);
							a4gtDomandeCollegate.setDtInizioSilenzioAssenso(cal.getTime());
							cal.setTime(esitiBdnaCsvImport.getDataInserimento());
							cal.add(Calendar.DATE, 210);
							a4gtDomandeCollegate.setDtFineSilenzioAssenso(cal.getTime());
							a4gtDomandeCollegate.setDtInizioEsitoNegativo(null);
							a4gtDomandeCollegate.setDtFineEsitoNegativo(null);
							a4gtDomandeCollegate.setDtBdna(esitiBdnaCsvImport.getDataInserimento());
							a4gtDomandeCollegate.setProtocollo(esitiBdnaCsvImport.getNumProtocollo());
							a4gtDomandeCollegate.setStatoBdna(StatoDomandaCollegata.ATTI);
							domandeCollegateDao.save(a4gtDomandeCollegate);
							DomandaCollegata domandaCollegata = new DomandaCollegata();
							BeanUtils.copyProperties(a4gtDomandeCollegate, domandaCollegata);
							domandaCollegata.setStatoBdna(a4gtDomandeCollegate.getStatoBdna().toString());
							domandaCollegata.setTipoDomanda(a4gtDomandeCollegate.getTipoDomanda().toString());
							domandeCollegate.add(domandaCollegata);
							cuaaCorretti.add(domandaCollegata.getCuaa());
						}
					}
					//ANOMALIA
					if(esitiBdnaCsvImport.getStatoRichiesta().compareTo(StatoDomandaCollegata.ANOMALIA.getStatoDomandaCollegata()) == 0) {
						a4gtDomandeCollegate.setDtInizioEsitoNegativo(null);
						a4gtDomandeCollegate.setDtFineEsitoNegativo(null);
						a4gtDomandeCollegate.setDtBdna(esitiBdnaCsvImport.getDataInserimento());
						a4gtDomandeCollegate.setProtocollo(esitiBdnaCsvImport.getNumProtocollo());
						a4gtDomandeCollegate.setStatoBdna(StatoDomandaCollegata.ANOMALIA);
						a4gtDomandeCollegate.setA4gtTrasmissioneBdna(null);
						domandeCollegateDao.save(a4gtDomandeCollegate);
						DomandaCollegata domandaCollegata = new DomandaCollegata();
						BeanUtils.copyProperties(a4gtDomandeCollegate, domandaCollegata);
						domandaCollegata.setStatoBdna(a4gtDomandeCollegate.getStatoBdna().toString());
						domandaCollegata.setTipoDomanda(a4gtDomandeCollegate.getTipoDomanda().toString());
						domandeCollegate.add(domandaCollegata);
						cuaaCorretti.add(domandaCollegata.getCuaa());
					}
					//CHIUSA CON ESITO NEGATIVO
					if(esitiBdnaCsvImport.getStatoRichiesta().compareTo(StatoDomandaCollegata.CHIUSA_CON_ESITO_NEGATIVO.getStatoDomandaCollegata()) == 0) {
						if(esitiBdnaCsvImport.getNumProtocollo().isEmpty()) {
							cuaaErrati.add(esitiBdnaCsvImport.getCodiceFiscale());
						}
						else {
							Calendar cal = Calendar.getInstance();
							cal.setTime(esitiBdnaCsvImport.getDataInserimento());
							cal.add(Calendar.DATE, 30);
							a4gtDomandeCollegate.setDtInizioSilenzioAssenso(cal.getTime());
							cal.setTime(esitiBdnaCsvImport.getDataInserimento());
							cal.add(Calendar.DATE, 210);
							a4gtDomandeCollegate.setDtFineSilenzioAssenso(cal.getTime());
							a4gtDomandeCollegate.setDtInizioEsitoNegativo(esitiBdnaCsvImport.getDataInserimento());
							cal.setTime(esitiBdnaCsvImport.getDataInserimento());
							cal.add(Calendar.DATE, 365);
							a4gtDomandeCollegate.setDtFineEsitoNegativo(cal.getTime());
							a4gtDomandeCollegate.setProtocollo(esitiBdnaCsvImport.getNumProtocollo());
							a4gtDomandeCollegate.setDtBdna(esitiBdnaCsvImport.getDataInserimento());
							a4gtDomandeCollegate.setStatoBdna(StatoDomandaCollegata.CHIUSA_CON_ESITO_NEGATIVO);
							domandeCollegateDao.save(a4gtDomandeCollegate);
							DomandaCollegata domandaCollegata = new DomandaCollegata();
							BeanUtils.copyProperties(a4gtDomandeCollegate, domandaCollegata);
							domandaCollegata.setStatoBdna(a4gtDomandeCollegate.getStatoBdna().toString());
							domandaCollegata.setTipoDomanda(a4gtDomandeCollegate.getTipoDomanda().toString());
							domandeCollegate.add(domandaCollegata);
							cuaaCorretti.add(domandaCollegata.getCuaa());
						}
					}
					//IN CODA
					if(esitiBdnaCsvImport.getStatoRichiesta().compareTo(StatoDomandaCollegata.IN_CODA.getStatoDomandaCollegata()) == 0 ||
							esitiBdnaCsvImport.getStatoRichiesta().compareTo(StatoDomandaCollegata.IN_INSERIMENTO.getStatoDomandaCollegata()) == 0 ) {
						a4gtDomandeCollegate.setDtBdna(esitiBdnaCsvImport.getDataInserimento());
						domandeCollegateDao.save(a4gtDomandeCollegate);
						DomandaCollegata domandaCollegata = new DomandaCollegata();
						BeanUtils.copyProperties(a4gtDomandeCollegate, domandaCollegata);
						domandaCollegata.setStatoBdna(a4gtDomandeCollegate.getStatoBdna().toString());
						domandaCollegata.setTipoDomanda(a4gtDomandeCollegate.getTipoDomanda().toString());
						domandeCollegate.add(domandaCollegata);
						cuaaCorretti.add(domandaCollegata.getCuaa());
					}
				}
				else {
					cuaaErrati.add(esitiBdnaCsvImport.getCodiceFiscale());
				}
			}
		}
		//AGGIORNO GLI ESITI BDNA NELLA TABELLA TPAGA_ESITI_ANTIMAFIA DI AGS
		if(!domandeCollegate.isEmpty()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entitySincronizzazione = new HttpEntity<>(objectMapper.writeValueAsString(domandeCollegate), headers);
			URI uriAgs = new URI(agsUrl.concat("esiti/antimafia"));
			restTemplateUtenzaTecnica.exchange(uriAgs, HttpMethod.PUT, entitySincronizzazione, String.class);
		}
		//RITORNO L'ELENCO DEI CUAA CORRETTI E IN ERRORE
		EsitiBdna esitiBdna = new EsitiBdna();
		esitiBdna.setDomandeAggiornate(cuaaCorretti);
		esitiBdna.setDomandeConErrori(cuaaErrati);
		return esitiBdna;
	}

	private String getDichiarazioneAntimafiaURL() throws Exception {
		String formatted = StatoDichiarazioneEnum.getStringFormatted();
		return "?params=".concat(URLEncoder.encode(String.format("{\"statiDichiarazione\":%s}", formatted), StandardCharsets.UTF_8.name()));
	}

	@Override
	@Transactional(readOnly = true)
	public CsvFile getDichiarazioniAntimafiaConDomandeCollegate() throws Exception {
		List<DichiarazioneAntimafia> dichiarazioniAntimafia = consumeExternalRestApi.getDichiarazioniAntimafia(getDichiarazioneAntimafiaURL());
		if (CollectionUtils.isEmpty(dichiarazioniAntimafia)) {
			return null;
		}

		List<String> listaCuaa = dichiarazioniAntimafia.stream().map(dichiarazione -> dichiarazione.getDatiDichiarazione().getDettaglioImpresa().getCodiceFiscale()).collect(Collectors.toList());
		// ORA-01795: maximum number of expressions in a list is 1000
		// Partiziono la lista in sottoliste di dimensione massimo 999 (limite massimo di JPA)
		final List<List<String>> subLists = Lists.partition(listaCuaa, QUERY_LIMIT);
		List<A4gtDomandeCollegate> domandeCollegate = new ArrayList<A4gtDomandeCollegate>();
		subLists.forEach(subList -> domandeCollegate.addAll(domandeCollegateDao.findByCuaaIn(subList)));

		List<DomandeCollegateExportBdna> dichAntimafiaEsiti = new ArrayList<>();

		dichiarazioniAntimafia.stream().forEach(item -> {
			DomandeCollegateExportBdna res = new DomandeCollegateExportBdna();
			res.setDichiarazioneAntimafia(item);
			res.setDomandeCollegate(domandeCollegate.stream()
					.filter(p -> p.getCuaa().equals(item.getAzienda().getCuaa()))
					.collect(Collectors.toList()));
			dichAntimafiaEsiti.add(res);
		});

		List<DomandeCollegateExportBdnaCsv> csvItems = new ArrayList<>();
		dichAntimafiaEsiti.forEach(item -> {
			csvItems.addAll(DomandeCollegateExportBdnaCsvConverter.from(item));
		});

		DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT_CSV);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(dfs);
		CsvSchema domandeCollegateExportSchema = DomandeCollegateExportBdnaCsvConverter.getDomandeCollegateExportSchema();
		CsvMapper mapper = new CsvMapper();
		ObjectWriter domandeCollegateWriter = mapper.writerFor(DomandeCollegateExportBdnaCsv.class).with(domandeCollegateExportSchema);
		ByteArrayOutputStream csvByteArray = new ByteArrayOutputStream();
		domandeCollegateWriter.writeValues(csvByteArray).writeAll(csvItems);
		CsvFile csvFile = new CsvFile();
		csvFile.setCsvByteArray(csvByteArray.toByteArray());
		// creazione file name
		StringBuilder filename = new StringBuilder("Export_");
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT_CSV);
		filename.append(formatter.format(new Date()));
		filename.append(".csv");
		csvFile.setCsvFileName(filename.toString());

		return csvFile;
	}



	@Override
	public CsvFile scaricaCsv(Long id) throws Exception {
		Optional<A4gtTrasmissioneBdna> trasmissioneResult = trasmissioneBdnaDao.findById(id);
		if(!trasmissioneResult.isPresent()) {
			throw new Exception(String.format("Nessuna trasmissione trovata con id : %d", id));
		}
		if(trasmissioneResult.get().getA4gtDomandeCollegates() != null && trasmissioneResult.get().getA4gtDomandeCollegates().size() > 0) {
			List<A4gtDomandeCollegate> domandeCollegate = trasmissioneResult.get().getA4gtDomandeCollegates();
			CsvFile csv = creazioneCsv(trasmissioneResult.get().getTipoDomanda(), domandeCollegate, trasmissioneResult.get().getDtCreazione());			
			csv.setCsvFileName(csv.getCsvFileName().replace(utente.utenza(), trasmissioneResult.get().getCfOperatore()));
			return csv;
		}
		else {
			throw new Exception(String.format("Nessuna domanda collegata presente per la trasmissione con id : %d", id)); 
		}
	}

}
