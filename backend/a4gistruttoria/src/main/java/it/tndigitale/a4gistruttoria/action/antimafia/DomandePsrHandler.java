package it.tndigitale.a4gistruttoria.action.antimafia;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.CustomThreadLocal;
import it.tndigitale.a4gistruttoria.dto.DatiElaborazioneProcesso;
import it.tndigitale.a4gistruttoria.dto.DomandaCollegata;
import it.tndigitale.a4gistruttoria.repository.dao.DomandeCollegateDao;
import it.tndigitale.a4gistruttoria.repository.dao.ProcessoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtDomandeCollegate;
import it.tndigitale.a4gistruttoria.repository.model.A4gtProcesso;
import it.tndigitale.a4gistruttoria.util.AntimafiaDomandeCollegateHelper;
import it.tndigitale.a4gistruttoria.util.StatoDomandaCollegata;
import it.tndigitale.a4gistruttoria.util.TipoDomandaCollegata;

@Component
public class DomandePsrHandler {
	
	private static Logger logger = LoggerFactory.getLogger(DomandePsrHandler.class);
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ProcessoDao processoDao;
	
	@Autowired
	private DomandeCollegateDao domandeCollegateDao;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void elaboraDomanda(String domandePSR,List<DomandaCollegata> domandeCollegate) throws IOException {
		A4gtProcesso a4gtProcesso = processoDao.getOne((Long) CustomThreadLocal.getVariable("idProcesso"));
		if (a4gtProcesso == null || a4gtProcesso.getDatiElaborazione() == null) {
			String errorMessage = String.format("Processo di importazione domande PSR superficie non trovato");
			logger.error(errorMessage);
			throw new RuntimeException(errorMessage);
		}

		DatiElaborazioneProcesso datiProcesso;
		try {
			datiProcesso = objectMapper.readValue(a4gtProcesso.getDatiElaborazione(), DatiElaborazioneProcesso.class);
		} catch (IOException e) {
			String errorMessage = String.format("Errore parsing 'dati Processo': %s", a4gtProcesso.getDatiElaborazione());
			logger.error(errorMessage, e);
			throw new RuntimeException(errorMessage, e);
		}
		
		if (StringUtils.isEmpty(domandePSR)) {
			aggiornaProcesso(a4gtProcesso, datiProcesso);
			return;
		}
		JsonNode jsonDomandePSR = objectMapper.readTree(domandePSR);
		for (JsonNode domandaPSR : jsonDomandePSR) {
			elaboraDomanda(domandaPSR, domandeCollegate);
		}
		aggiornaProcesso(a4gtProcesso, datiProcesso);
	}

	
	public void elaboraDomanda(JsonNode domandaPSR,List<DomandaCollegata> domandeCollegate) {
		logger.debug("Dati Domanda Psr Superficie da inserire: {}", domandaPSR);
		
		A4gtDomandeCollegate filter = new A4gtDomandeCollegate();
		filter.setTipoDomanda(TipoDomandaCollegata.PSR_SUPERFICIE_EU);
		filter.setCuaa(domandaPSR.get("cuaa").textValue());
		filter.setIdDomanda(domandaPSR.get("idDomanda").asLong());
		Optional<A4gtDomandeCollegate> domanda = domandeCollegateDao.findOne(Example.of(filter));
		A4gtDomandeCollegate a4gtDomandeCollegate;
		if (!domanda.isPresent()) {
			a4gtDomandeCollegate = new A4gtDomandeCollegate();
			a4gtDomandeCollegate.setTipoDomanda(TipoDomandaCollegata.PSR_SUPERFICIE_EU);
			a4gtDomandeCollegate.setCuaa(domandaPSR.get("cuaa").textValue());
			a4gtDomandeCollegate.setIdDomanda(domandaPSR.get("idDomanda").asLong());
			DateFormat dt = new SimpleDateFormat(DATE_FORMAT);
			try {
				a4gtDomandeCollegate.setDtDomanda(dt.parse(domandaPSR.get("dataDomanda").textValue()));
			} catch (ParseException e) {
				logger.error("Errore nella conversione della data", e);
				throw new RuntimeException(e);
			}
			a4gtDomandeCollegate.setCampagna(domandaPSR.get("anno").asInt());
			a4gtDomandeCollegate.setImportoRichiesto(BigDecimal.valueOf(domandaPSR.get("importoRichiesto").asDouble()));
			a4gtDomandeCollegate.setStatoBdna(StatoDomandaCollegata.NON_CARICATO);
			domandeCollegateDao.save(a4gtDomandeCollegate);
			DomandaCollegata domandaCollegata = new DomandaCollegata();
			BeanUtils.copyProperties(a4gtDomandeCollegate, domandaCollegata);
			domandaCollegata.setStatoBdna(a4gtDomandeCollegate.getStatoBdna().toString());
			domandaCollegata.setTipoDomanda(a4gtDomandeCollegate.getTipoDomanda().toString());
			domandeCollegate.add(domandaCollegata);
		} else {
			// BRIAMCNT009
			a4gtDomandeCollegate = domanda.get();
			if (a4gtDomandeCollegate.getImportoRichiesto().compareTo(BigDecimal.valueOf(domandaPSR.get("importoRichiesto").asDouble())) != 0) {
				a4gtDomandeCollegate.setImportoRichiesto(BigDecimal.valueOf(domandaPSR.get("importoRichiesto").asDouble()));
				DateFormat dt = new SimpleDateFormat(DATE_FORMAT);
				try {
					a4gtDomandeCollegate.setDtDomanda(dt.parse(domandaPSR.get("dataDomanda").textValue()));
				} catch (ParseException e) {
					logger.error("Errore nella conversione della data", e);
					throw new RuntimeException(e);
				}
				a4gtDomandeCollegate.setCampagna(domandaPSR.get("anno").asInt());
				a4gtDomandeCollegate.setImportoRichiesto(BigDecimal.valueOf(domandaPSR.get("importoRichiesto").asDouble()));
				a4gtDomandeCollegate.setStatoBdna(StatoDomandaCollegata.NON_CARICATO);
				AntimafiaDomandeCollegateHelper.resetDomandaCollegata(a4gtDomandeCollegate);
				domandeCollegateDao.save(a4gtDomandeCollegate);
				DomandaCollegata domandaCollegataDto = new DomandaCollegata();
				BeanUtils.copyProperties(a4gtDomandeCollegate, domandaCollegataDto);
				domandaCollegataDto.setStatoBdna(a4gtDomandeCollegate.getStatoBdna().toString());
				domandaCollegataDto.setTipoDomanda(a4gtDomandeCollegate.getTipoDomanda().toString());
				domandeCollegate.add(domandaCollegataDto);
			}
		}
	}
	
	private void aggiornaProcesso(A4gtProcesso a4gtProcesso, DatiElaborazioneProcesso datiProcesso) {
		try {
			Short totaleDomandeGestite = new Short(datiProcesso.getTotale());
			datiProcesso.setTotale((++totaleDomandeGestite).toString());
			Double percentuale = (Double.valueOf(datiProcesso.getTotale()) * 100) / Double.valueOf(datiProcesso.getDaElaborare());
			a4gtProcesso.setPercentualeAvanzamento(BigDecimal.valueOf(percentuale));
			a4gtProcesso.setDatiElaborazione(objectMapper.writeValueAsString(datiProcesso));
			A4gtProcesso processoSaved = processoDao.save(a4gtProcesso);
			logger.info("Salvataggio processo con id {} avvenuto con successo.", processoSaved.getId());
		} catch (Exception ex) {
			String errorMessage = String.format("Fallito l'aggiornamento %s del processo con ID", CustomThreadLocal.getVariable("idProcesso").toString());
			logger.error(errorMessage, ex);
			throw new RuntimeException(errorMessage, ex);
		}
	}
}
