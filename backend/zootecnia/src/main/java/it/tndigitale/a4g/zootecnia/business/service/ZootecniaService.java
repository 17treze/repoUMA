package it.tndigitale.a4g.zootecnia.business.service;

import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.FascicoloNonValidabileException;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.zootecnia.business.event.EndValidazioneFascicoloEvent;
import it.tndigitale.a4g.zootecnia.business.event.StartControlloCompletezzaEvent;
import it.tndigitale.a4g.zootecnia.business.persistence.entity.*;
import it.tndigitale.a4g.zootecnia.business.persistence.repository.AllevamentoDao;
import it.tndigitale.a4g.zootecnia.business.persistence.repository.ControlloCompletezzaDao;
import it.tndigitale.a4g.zootecnia.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.zootecnia.business.persistence.repository.StrutturaAllevamentoDao;
import it.tndigitale.a4g.zootecnia.business.persistence.repository.legacy.FascicoloAgsDao;
import it.tndigitale.a4g.zootecnia.business.persistence.repository.legacy.SincronizzazioneAgsDaoImpl;
import it.tndigitale.a4g.zootecnia.business.persistence.repository.legacy.SincronizzazioneAgsException;
import it.tndigitale.a4g.zootecnia.business.service.client.ZootecniaProxyClient;
import it.tndigitale.a4g.zootecnia.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javax.persistence.EntityNotFoundException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ZootecniaService {

	private static final Logger logger = LoggerFactory.getLogger(ZootecniaService.class);

	@Autowired
	private AllevamentoDao allevamentoDao;
	@Autowired
	StrutturaAllevamentoDao strutturaAllevamentoDao;
	@Autowired
	private ZootecniaProxyClient zootecniaProxyClient;
	@Autowired
	AllevamentoDtoModelConverter allevamentoDtoModelConverter;
	@Autowired
	AllevamentoModelDtoConverter allevamentoModelDtoConverter;
	@Autowired
	FascicoloDao fascicoloDao;
	@Autowired
	private SincronizzazioneAgsDaoImpl sincronizzazioneAgsDaoImpl;
	@Autowired
	private FascicoloAgsDao fascicoloAgsDao;
	@Autowired
	private Clock clock;
	@Autowired 
	private EventBus eventBus;
	@Autowired
	private ControlloCompletezzaDao controlloCompletezzaDao;
	@Autowired
	private UtenteComponent utenteComponent;

	private static final Map<String, String> SPECIE_CODICE_MAP = Map.ofEntries(
			Map.entry("0502", "ACQUACOLTURA"),
			Map.entry("0131", "ALTRE SPECIE"),
			Map.entry("0130", "APICOLTURA"),
			Map.entry("0127", "AVICOLI"),
			Map.entry("0121", "BOVINI BUFALINI"),
			Map.entry("0173", "ELICOLTURA"),
			Map.entry("0126", "EQUIDI"),
			Map.entry("0128", "LAGOMORFI"),
			Map.entry("0124", "OVINI"),
			Map.entry("0125", "CAPRINI"),
			Map.entry("0171", "RUMINANTIA"),
			Map.entry("0122", "SUIDI"),
			Map.entry("0172", "TYLOPODA")
			);

	public List<AnagraficaAllevamentoDto> getAllevamenti(final String cuaa, final int idValidazione) {
		List<AllevamentoModel> allevamenti = allevamentoDao.findByFascicolo_CuaaAndFascicolo_IdValidazione(cuaa, idValidazione);
		List<AnagraficaAllevamentoDto> result = new ArrayList<>();
		if (allevamenti == null) {
			return result;
		}
		allevamenti.forEach(model -> result.add(allevamentoDtoModelConverter.convert(model)));
		return result;
	}

	public List<AnagraficaAllevamentoDto> getAllevamentiFascicoloCompleto(final String cuaa, final int idValidazione) throws FascicoloInvalidConditionException {
		List<AnagraficaAllevamentoDto> retVal = null;
		try {
			FascicoloModel fascicoloFromCuaa = getFascicoloFromCuaa(cuaa, idValidazione);
			Map<ControlliFascicoloZootecniaCompletoEnum, EsitoControlloDto> controlloCompletezzaFascicolo = getControlloCompletezzaFascicolo(fascicoloFromCuaa);
			Set<ControlliFascicoloZootecniaCompletoEnum> keySet = controlloCompletezzaFascicolo.keySet();
			for (var k : keySet) {
				if (controlloCompletezzaFascicolo.get(k).getEsito() < 0) {
					throw new FascicoloInvalidConditionException(k);
				}
			}
			retVal = getAllevamenti(cuaa, idValidazione);
		} catch (NoSuchElementException e) {
			retVal = new ArrayList<>(0);
		}
		return retVal;
	}

	@Transactional
	public void aggiornaAllevamenti(final String cuaa, final LocalDate dataRichiesta) {
		List<AllevamentoModel> result = new ArrayList<>();
		List<it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto> allevamentiDaProxy = zootecniaProxyClient.getAnagraficaAllevamenti(cuaa, dataRichiesta);
		if (allevamentiDaProxy != null && !allevamentiDaProxy.isEmpty()) {

			//			filtrare gli allevamenti per TIPOLOGIA = AL
			List<it.tndigitale.a4g.proxy.client.model.AnagraficaAllevamentoDto> allevamenti = allevamentiDaProxy.stream().filter(
					a -> a.getTipoProduzione().equalsIgnoreCase("AL")).collect(Collectors.toList());
			if(allevamenti != null && !allevamenti.isEmpty()) {
				Optional<FascicoloModel> fascicoloModelOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
				FascicoloModel fascicoloModel;

				if (fascicoloModelOpt.isPresent()) {
					fascicoloModel = fascicoloModelOpt.get();

					List<AllevamentoModel> allevamentiLive = allevamentoDao.findByFascicolo_CuaaAndFascicolo_IdValidazione(cuaa,0);
					allevamentoDao.deleteInBatch(allevamentiLive);
					strutturaAllevamentoDao.deleteInBatch(strutturaAllevamentoDao.findByIdValidazioneAndCuaa(0, cuaa));
				} else {
					fascicoloModel = new FascicoloModel();
					fascicoloModel.setCuaa(cuaa);
					fascicoloDao.save(fascicoloModel);
				}

				Set<StrutturaAllevamentoModel> strutture = new HashSet<>();
				//				accorpamento strutture; struttura.identificativo (AZIENDA_CODICE) per il momento indica una ed una sola struttura
				allevamenti.forEach(dto -> strutture.add(StrutturaAllevamentoMapper.from(dto.getStruttura(), cuaa)));

				//				salvataggio strutture dal Set (contiene dati univoci)
				Set<StrutturaAllevamentoModel> struttureSaved = new HashSet<>();
				strutture.forEach(struttura -> struttureSaved.add(strutturaAllevamentoDao.save(struttura)));

				//				associazione allevamenti <--> strutture
				allevamenti.forEach(dto -> {
					AllevamentoModel allevamento = allevamentoModelDtoConverter.convert(dto);
					Optional<StrutturaAllevamentoModel> struttura = strutturaAllevamentoDao.findByIdentificativoAndIdValidazioneAndCuaa(dto.getStruttura().getIdentificativo(), 0, cuaa);
					allevamento.setStrutturaAllevamento(struttura.get());
					result.add(allevamento);
				});

				//				salvataggio allevamenti
				fascicoloModel.setAllevamenti(result);
				fascicoloModel.setDtAggiornamentoFontiEsterne(LocalDateTime.now());
				fascicoloModel.getAllevamenti().forEach(allevamento -> {
					allevamento.setFascicolo(fascicoloModel);
					allevamentoDao.save(allevamento);
				});
			}
		}
	}

	/*@Transactional
	public void startValidazioneFascicoloAsincrona(final String cuaa) throws FascicoloNonValidabileException {
		FascicoloModel fascicoloDaValidare = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0)
				.orElseThrow(EntityNotFoundException::new);
		if (fascicoloDaValidare.getStato().equals(StatoFascicoloEnum.CONTROLLATO)) {
			fascicoloDaValidare.setStato(StatoFascicoloEnum.IN_VALIDAZIONE);
			StartValidazioneFascicoloEvent event = new StartValidazioneFascicoloEvent(cuaa);
			eventBus.publishEvent(event);
		} else {
			throw new FascicoloNonValidabileException(StatoFascicoloEnum.CONTROLLATO);
		}
	}*/

	private void startValidazioneStruttura(final StrutturaAllevamentoModel strutturaLive, final Integer idValidazione) {
		StrutturaAllevamentoModel strutturaValidata = new StrutturaAllevamentoModel();
		BeanUtils.copyProperties(strutturaLive, strutturaValidata, "idValidazione", "version", "allevamenti");
		strutturaValidata.setIdValidazione(idValidazione);
		strutturaAllevamentoDao.save(strutturaValidata);
	}

	private AllevamentoModel startValidazioneAllevamento(
			final AllevamentoModel allevamento,
			final FascicoloModel fascicoloValidatoModel,
			final Integer idValidazione) {
		var tmpAllevamentoValidatoModel = new AllevamentoModel();
		BeanUtils.copyProperties(allevamento, tmpAllevamentoValidatoModel, "version", "strutturaAllevamento");
		tmpAllevamentoValidatoModel.setFascicolo(fascicoloValidatoModel);
		tmpAllevamentoValidatoModel.setIdValidazione(idValidazione);

		Optional<StrutturaAllevamentoModel> strutturaValidataOpt = strutturaAllevamentoDao.findByIdentificativoAndIdValidazioneAndCuaa(allevamento.getStrutturaAllevamento().getIdentificativo(), idValidazione, fascicoloValidatoModel.getCuaa());
		if(strutturaValidataOpt.isPresent()) {

			StrutturaAllevamentoModel strutturaValidata = strutturaValidataOpt.get();
			strutturaValidata.ignoreValidazioneCheck();
			tmpAllevamentoValidatoModel.setStrutturaAllevamento(strutturaValidata);	
			if(strutturaValidata.getAllevamenti() == null) {
				strutturaValidata.setAllevamenti(new ArrayList<>());
			}
			strutturaValidata.getAllevamenti().add(tmpAllevamentoValidatoModel);
			strutturaAllevamentoDao.save(strutturaValidata);
		}


		/*
		tmpAllevamentoValidatoModel.setAutorizzazioneSanitariaLatte(allevamento.getAutorizzazioneSanitariaLatte());
		tmpAllevamentoValidatoModel.setCfDetentore(allevamento.getCfDetentore());
		tmpAllevamentoValidatoModel.setCfProprietario(allevamento.getCfProprietario());
		tmpAllevamentoValidatoModel.setDenominazioneDetentore(allevamento.getDenominazioneDetentore());
		tmpAllevamentoValidatoModel.setDenominazioneProprietario(allevamento.getDenominazioneProprietario());
		tmpAllevamentoValidatoModel.setDtAperturaAllevamento(allevamento.getDtAperturaAllevamento());
		tmpAllevamentoValidatoModel.setDtChiusuraAllevamento(allevamento.getDtChiusuraAllevamento());
		tmpAllevamentoValidatoModel.setDtFineDetenzione(allevamento.getDtFineDetenzione());
		tmpAllevamentoValidatoModel.setDtInizioDetenzione(allevamento.getDtInizioDetenzione());
		tmpAllevamentoValidatoModel.setId(allevamento.getId());
		tmpAllevamentoValidatoModel.setIdentificativo(allevamento.getIdentificativo());
		tmpAllevamentoValidatoModel.setIdentificativoFiscale(allevamento.getIdentificativoFiscale());
		tmpAllevamentoValidatoModel.setOrientamentoProduttivo(allevamento.getOrientamentoProduttivo());
		tmpAllevamentoValidatoModel.setSoccida(allevamento.getSoccida());
		tmpAllevamentoValidatoModel.setSpecie(allevamento.getSpecie());
		tmpAllevamentoValidatoModel.setStrutturaAllevamento(allevamento.getStrutturaAllevamento());
		tmpAllevamentoValidatoModel.setTipologiaAllevamento(allevamento.getTipologiaAllevamento());
		tmpAllevamentoValidatoModel.setTipologiaProduzione(allevamento.getTipologiaProduzione());*/
		return allevamentoDao.save(tmpAllevamentoValidatoModel);
	}

	@Transactional
	public void startValidazioneFascicolo(final String cuaa, final Integer idValidazione) throws FascicoloNonValidabileException {
		Optional<FascicoloModel> fascicoloLiveOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		if (!fascicoloLiveOpt.isPresent()) {
			return;
		}
		FascicoloModel fascicoloLive = fascicoloLiveOpt.get();
		FascicoloModel tmpFascicoloValidato = new FascicoloModel();
		//BeanUtils.copyProperties(fascicoloLive, tmpFascicoloValidato, "allevamenti", "version");
		tmpFascicoloValidato.setId(fascicoloLive.getId());
		tmpFascicoloValidato.setIdValidazione(idValidazione);
		tmpFascicoloValidato.setCuaa(cuaa);

		/*fascicoloLive.setCodiceFiscaleRappresentanteLegaleValidato(getCodiceFiscaleRappresentanteLegale(fascicoloLive));
		fascicoloLive.setNominativoRappresentanteLegaleValidato(getNominativoRappresentanteLegale(fascicoloLive));
		fascicoloLive.setDataValidazione(LocalDate.now());*/

		FascicoloModel fascicoloValidato = fascicoloDao.save(tmpFascicoloValidato);
		fascicoloValidato.ignoreValidazioneCheck();

		List<AllevamentoModel> allevamentiLive = fascicoloLive.getAllevamenti();
		Set<StrutturaAllevamentoModel> struttureLive = new HashSet<>();
		//		accorpamento strutture
		allevamentiLive.forEach(
				allevamento -> struttureLive.add(allevamento.getStrutturaAllevamento()));
		//		validazione strutture
		struttureLive.forEach(strutturaLive -> startValidazioneStruttura(strutturaLive, idValidazione));
		//		validazione allevamenti
		List<AllevamentoModel> allevamentiValidati = allevamentiLive.stream()
				.map(a -> startValidazioneAllevamento(a, fascicoloValidato, idValidazione))
				.collect(Collectors.toList());
		fascicoloValidato.setAllevamenti(allevamentiValidati);
		fascicoloDao.save(fascicoloValidato);
	}

	@Transactional
	public void sincronizzaAgs(final String cuaa, final Integer idValidazione) throws SincronizzazioneAgsException {
		sincronizzazioneAgsDaoImpl.sincronizzaFascicoloAgs(cuaa, idValidazione);
	}

	@Transactional
	public void invioEventoFineValidazione(final String cuaa, final Integer idValidazione){
		var event = new EndValidazioneFascicoloEvent(cuaa, idValidazione);
		eventBus.publishEvent(event);
	}

	private FascicoloModel getFascicoloFromCuaa(final String cuaa, int idValidazione) throws NoSuchElementException {
		Optional<FascicoloModel> fascicoloOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, idValidazione);
		return fascicoloOpt.orElseThrow();
	}

	private FascicoloModel getFascicoloFromCuaa(final String cuaa) throws NoSuchElementException {
		return getFascicoloFromCuaa(cuaa, 0);
	}

	public Map<ControlliFascicoloZootecniaCompletoEnum, EsitoControlloDto> getControlloCompletezzaFascicolo(final String cuaa) {
		try {
			return getControlloCompletezzaFascicolo(getFascicoloFromCuaa(cuaa));
		} catch (NoSuchElementException | EntityNotFoundException e) {
			var retList = new EnumMap<ControlliFascicoloZootecniaCompletoEnum, EsitoControlloDto>(ControlliFascicoloZootecniaCompletoEnum.class);
			EsitoControlloDto esitoControlloDto = new EsitoControlloDto();
			esitoControlloDto.setEsito(0);
			retList.put(ControlliFascicoloZootecniaCompletoEnum.IS_AGGIORNAMENTO_FONTI_ESTERNE_ZOOTECNIA, esitoControlloDto);
			return retList;
		}
	}

	private static boolean contains(String test) {
		if (StringUtils.isBlank(test)) {
			return false;
		}
		for (ControlliFascicoloZootecniaCompletoEnum c : ControlliFascicoloZootecniaCompletoEnum.values()) {
			if (c.name().equals(test)) {
				return true;
			}
		}
		return false;
	}

	public Map<ControlliFascicoloZootecniaCompletoEnum, EsitoControlloDto> queryControlloCompletezzaFascicolo(final String cuaa) {
		var controlliList = controlloCompletezzaDao.findByCuaa(cuaa);
		var resultMap = new HashMap<ControlliFascicoloZootecniaCompletoEnum, EsitoControlloDto>();
		if (controlliList == null || controlliList.isEmpty()) {
				return resultMap;
			}
		var controlliFilteredList = controlliList.stream().filter(c -> contains(c.getTipoControllo())).collect(Collectors.toList());
		for (ControlloCompletezzaModel controlloCompletezzaModel: controlliFilteredList) {
				var esitoDto = new EsitoControlloDto();
				esitoDto.setEsito(controlloCompletezzaModel.getEsito());
				if (controlloCompletezzaModel.getIdControllo() != null) {
					esitoDto.setIdControllo(Long.valueOf(controlloCompletezzaModel.getIdControllo()));
				}
				resultMap.put(ControlliFascicoloZootecniaCompletoEnum.valueOf(controlloCompletezzaModel.getTipoControllo()), esitoDto);
		}
		return resultMap;
	}

	@Transactional
	public void startControlloCompletezzaFascicoloAsincrono(final String cuaa, final Integer idValidazione) {
		try {
	//		controllo se esiste localmente un fascicolo associato al cuaa, altrimenti si lancia eccezione e si esce
			getFascicoloFromCuaa(cuaa);
//			check dell'esistenza di un controllo di completezza esistente. In tal caso il record viene eliminato
			var controlloCompletezzaList = controlloCompletezzaDao.findByCuaa(cuaa);
			if (controlloCompletezzaList.size() > 0) {
				controlloCompletezzaDao.deleteInBatch(controlloCompletezzaList);
			}

	//		salvare nel db i dati del controllo di completezza tranne l'esito che verrà gestito dal listener
			for (ControlliFascicoloZootecniaCompletoEnum controllo: ControlliFascicoloZootecniaCompletoEnum.values()) {
				var controlloCompletezzaModel = new ControlloCompletezzaModel();
				controlloCompletezzaModel.setCuaa(cuaa);
				controlloCompletezzaModel.setTipoControllo(controllo.name());
				controlloCompletezzaModel.setUtente(utenteComponent.username());
				controlloCompletezzaModel.setDataEsecuzione(LocalDateTime.now());
				controlloCompletezzaDao.save(controlloCompletezzaModel);
			}

			var event = new StartControlloCompletezzaEvent(cuaa, idValidazione);
			eventBus.publishEvent(event);
		} catch ( NoSuchElementException | EntityNotFoundException e) {
			logger.warn("cuaa non censito: {}", cuaa);
		}
	}

	public Map<ControlliFascicoloZootecniaCompletoEnum, EsitoControlloDto> getControlloCompletezzaFascicolo(final FascicoloModel fascicoloModel) {
		var retList = new EnumMap<ControlliFascicoloZootecniaCompletoEnum, EsitoControlloDto>(ControlliFascicoloZootecniaCompletoEnum.class);
		retList.put(ControlliFascicoloZootecniaCompletoEnum.IS_AGGIORNAMENTO_FONTI_ESTERNE_ZOOTECNIA,
				isFascicoloCompletoAggiornamentoFontiEsterne(fascicoloModel));
		return retList;
	}

	private EsitoControlloDto isFascicoloCompletoAggiornamentoFontiEsterne(final FascicoloModel fascicoloModel) {
		LocalDateTime currentDateMinus24Hours = clock.now().minusHours(24);
		LocalDateTime dtAggiornamentoFontiEsterne = fascicoloModel.getDtAggiornamentoFontiEsterne();
		EsitoControlloDto esitoControlloDto = new EsitoControlloDto();
		esitoControlloDto.setEsito((dtAggiornamentoFontiEsterne != null && !dtAggiornamentoFontiEsterne.isBefore(currentDateMinus24Hours)) ? 0 : -3);
		return esitoControlloDto;
	}

	public ReportValidazioneDto getReportValidazione(String cuaa) {

		ReportValidazioneDto reportValidazioneDto = new ReportValidazioneDto();
		
		List<AnagraficaAllevamentoDto> allevamenti = getAllevamenti(cuaa, 0);
		if (CollectionUtils.isEmpty(allevamenti)) {
			reportValidazioneDto.setReportValidazioneAllevamenti(new ArrayList<>());
			reportValidazioneDto.setReportValidazioneConsistenzaZootecnica(new ArrayList<>());
			return reportValidazioneDto;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		List<ReportValidazioneAllevamentoDto> reportValidazioneAllevamenti = allevamenti.stream().map(allevamento -> {
			StrutturaAllevamentoDto strutturaAllevamentoDto = allevamento.getStrutturaAllevamentoDto();
			String codSpecie = allevamento.getSpecie();
			String specie;
			try {
				specie = String.format("%s (%s)", codSpecie, SPECIE_CODICE_MAP.get(codSpecie));
			} catch (NullPointerException npe) {
				specie = codSpecie;
			}
			ReportValidazioneAllevamentoDto reportValidazioneAllevamentoDto = new ReportValidazioneAllevamentoDto(
					specie,
					allevamento.getCfDetentore(),
					strutturaAllevamentoDto.getIdentificativo(),
					strutturaAllevamentoDto.getIndirizzo(),
					strutturaAllevamentoDto.getComune(),
					allevamento.getTipologiaAllevamento(),
					allevamento.getDtInizioDetenzione().format(formatter),
					allevamento.getCfProprietario());
			return reportValidazioneAllevamentoDto;
		}).collect(Collectors.toList());
		reportValidazioneDto.setReportValidazioneAllevamenti(reportValidazioneAllevamenti);
		
		if (getFascicoloFromCuaa(cuaa).getDtAggiornamentoFontiEsterne() != null) {
			reportValidazioneDto.setDtAggiornamentoFontiEsterne(getFascicoloFromCuaa(cuaa).getDtAggiornamentoFontiEsterne().format(formatter));
		}
		
		try {
			List<ReportValidazioneConsistenzaZootecnicaDto> consZootList = fascicoloAgsDao.findConsistenzaZootecnica(cuaa);
			reportValidazioneDto.setReportValidazioneConsistenzaZootecnica(consZootList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reportValidazioneDto;
	}

	@Transactional
	public void rimozioneControlliCompletezza(String cuaa) {
		List<ControlloCompletezzaModel> resultList = controlloCompletezzaDao.findByCuaa(cuaa);
		if (resultList != null && resultList.size() > 0) {
			controlloCompletezzaDao.deleteInBatch(resultList);
		}
	}
}
