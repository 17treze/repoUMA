package it.tndigitale.a4g.fascicolo.territorio.business.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import it.tndigitale.a4g.fascicolo.territorio.dto.ReportValidazioneFascicoloIsolaAziendaleDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.ReportValidazioneFascicoloSchedarioFrutticoloDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.ReportValidazioneFascicoloTitoloConduzioneDto;
import it.tndigitale.a4g.fascicolo.territorio.dto.ReportValidazioneTerreniAgsDto;
import it.tndigitale.a4g.framework.component.dto.EsitoControlloDto;
import it.tndigitale.a4g.framework.event.store.handler.EventBus;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.territorio.business.persistence.entity.ControlloCompletezzaModel;
import it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dao.ControlloCompletezzaDao;
import it.tndigitale.a4g.territorio.business.persistence.repository.legacy.dao.FascicoloAgsDao;
import it.tndigitale.a4g.territorio.event.StartControlloCompletezzaEvent;

@Service
public class FascicoloService {

	private static final Logger logger = LoggerFactory.getLogger(FascicoloService.class);

	@Autowired
	private FascicoloAgsDao fascicoloDao;
	@Autowired
	private ControlloCompletezzaDao controlloCompletezzaDao;
	@Autowired 
	private EventBus eventBus;
	@Autowired
	private UtenteComponent utenteComponent;

	public Map<ControlliFascicoloAgsCompletoEnum, EsitoControlloDto> getControlloCompletezzaFascicolo(String cuaa) throws SQLException, NoSuchElementException {
		try {
			var retList = new EnumMap<ControlliFascicoloAgsCompletoEnum, EsitoControlloDto>(ControlliFascicoloAgsCompletoEnum.class);
			retList.put(ControlliFascicoloAgsCompletoEnum.IS_TERRENI_CONSISTENTI, fascicoloDao.getControlloCompletezzaFascicolo(cuaa));
			retList.put(ControlliFascicoloAgsCompletoEnum.IS_PIANOCOLT_CONSISTENTE, fascicoloDao.getControlloPianoColturaleAlfanumerico(cuaa));
			retList.put(ControlliFascicoloAgsCompletoEnum.IS_PCG_CONSISTENTE, fascicoloDao.getControlloPianoColturaleGrafico(cuaa));
			return retList;
		}
		catch (JsonMappingException e) {
			throw new NoSuchElementException(e.getMessage());
		}
		catch (JsonProcessingException e) {
			throw new NoSuchElementException(e.getMessage());
		}		
	}

	private static boolean contains(String test) {
		if (StringUtils.isBlank(test)) {
			return false;
		}
		for (ControlliFascicoloAgsCompletoEnum c : ControlliFascicoloAgsCompletoEnum.values()) {
			if (c.name().equals(test)) {
				return true;
			}
		}
		return false;
	}

	public Map<ControlliFascicoloAgsCompletoEnum, EsitoControlloDto> queryControlloCompletezzaFascicolo(final String cuaa) {
		var controlliList = controlloCompletezzaDao.findByCuaa(cuaa);
		var resultMap = new HashMap<ControlliFascicoloAgsCompletoEnum, EsitoControlloDto>();
		if (controlliList == null || controlliList.isEmpty()) {
			return resultMap;
		}
		try {
			var controlliFilteredList = controlliList.stream().filter(c -> contains(c.getTipoControllo())).collect(Collectors.toList());
			for (ControlloCompletezzaModel controlloCompletezzaModel: controlliFilteredList) {
				var esitoDto = new EsitoControlloDto();
				esitoDto.setEsito(controlloCompletezzaModel.getEsito());
				if (controlloCompletezzaModel.getIdControllo() != null) {
					esitoDto.setIdControllo(Long.valueOf(controlloCompletezzaModel.getIdControllo()));
					esitoDto.setSegnalazioni(fascicoloDao.getSegnalazioni(Long.valueOf(controlloCompletezzaModel.getIdControllo())));
				}
				resultMap.put(ControlliFascicoloAgsCompletoEnum.valueOf(controlloCompletezzaModel.getTipoControllo()), esitoDto);
			}
			return resultMap;
		}
		catch (SQLException e) {
			throw new NoSuchElementException(e.getMessage());
		}
		catch (JsonMappingException e) {
			throw new NoSuchElementException(e.getMessage());
		}
		catch (JsonProcessingException e) {
			throw new NoSuchElementException(e.getMessage());
		}		
	}

	@Transactional
	public void startControlloCompletezzaFascicoloAsincrono(final String cuaa, final Integer idValidazione) {
		try {
			// controllo se esiste localmente un fascicolo associato al cuaa, altrimenti si lancia eccezione e si esce
			//			attualmente territorio non dispone di una tabella fascicolo
			//			getFascicoloFromCuaa(cuaa);
			// check dell'esistenza di un controllo di completezza esistente. In tal caso il record viene eliminato
			var controlloCompletezzaList = controlloCompletezzaDao.findByCuaa(cuaa);
			if (controlloCompletezzaList.size() > 0) {
				controlloCompletezzaDao.deleteInBatch(controlloCompletezzaList);
			}

			// salvare nel db i dati del controllo di completezza tranne l'esito che verrà gestito dal listener
			for (ControlliFascicoloAgsCompletoEnum controllo: ControlliFascicoloAgsCompletoEnum.values()) {
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
			logger.warn("cuaa non censito: {}", cuaa, e);
		}
	}

	@Transactional
	public void rimozioneControlliCompletezza(String cuaa) {
		List<ControlloCompletezzaModel> resultList = controlloCompletezzaDao.findByCuaa(cuaa);
		if (resultList != null && resultList.size() > 0) {
			controlloCompletezzaDao.deleteInBatch(resultList);
		}
	}

	public ReportValidazioneTerreniAgsDto getReportValidazione(final String cuaa) {

		var report = new ReportValidazioneTerreniAgsDto();
		try {
			// Isole aziendali
			List<ReportValidazioneFascicoloIsolaAziendaleDto> isoleList = fascicoloDao.findIsoleAziendali(cuaa);
			report.setIsoleList(isoleList);
			// Titoli di conduzione
			List<ReportValidazioneFascicoloTitoloConduzioneDto> titoliList = fascicoloDao.findTitoliConduzione(cuaa);
			report.setTitoliConduzioneList(titoliList);
			// Schedario frutticolo
			List<ReportValidazioneFascicoloSchedarioFrutticoloDto> ufList = fascicoloDao.findSchedarioFrutticolo(cuaa);
			report.setSchedarioFrutticoloList(ufList);
		} 
		catch ( JsonProcessingException | SQLException e) {
			logger.warn("cuaa non censito: {}", cuaa, e);
		}
		return report;
	}

}
