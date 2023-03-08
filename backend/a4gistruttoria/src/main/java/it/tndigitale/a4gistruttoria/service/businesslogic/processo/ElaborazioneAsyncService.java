package it.tndigitale.a4gistruttoria.service.businesslogic.processo;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.DatiElaborazioneProcesso;
import it.tndigitale.a4gistruttoria.dto.Processo;
import it.tndigitale.a4gistruttoria.dto.ProcessoIstruttoriaDto;
import it.tndigitale.a4gistruttoria.repository.dao.ProcessoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtProcesso;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import it.tndigitale.a4gistruttoria.strategy.processi.ProcessoSyncronizeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ElaborazioneAsyncService {

	private static final Logger logger = LoggerFactory.getLogger(ElaborazioneAsyncService.class);

	@Autowired
	private ProcessoSyncronizeStatus sync;

	@Autowired
	private ProcessoDao processoDao;
	
	@Autowired
	private ObjectMapper objectMapper;

	public Long creaProcesso(Processo processo) throws Exception {
		logger.debug("elabora: inizio");
		if (!puoProcedere(processo)) {
			logger.info("Non posso procedere");
			return null;
		}
		// Betty: workaround per far funzionare con vecchia struttura
		Long idProcesso = sync.creaNuovoProcesso(processo);

		return idProcesso;
	}
	
	protected boolean puoProcedere(Processo processo) {
		try {
			List<ProcessoIstruttoriaDto> inEsecuzione = getProcessiInEsecuzione(processo.getTipoProcesso());
			return (inEsecuzione == null) || inEsecuzione.isEmpty();
		} catch (Exception e) {
			logger.debug("puoProcedere: errore recuperando i processi in esecuzione di tipo {}", processo.getTipoProcesso(), e);
			return false;
		}
	}

	@Transactional(readOnly = true)
	public List<ProcessoIstruttoriaDto> getProcessiInEsecuzione(TipoProcesso tipoProcesso) throws Exception {
		ArrayList<ProcessoIstruttoriaDto> listaProcessi = new ArrayList<>();
		A4gtProcesso processoEntity = new A4gtProcesso();
		processoEntity.setStato(StatoProcesso.RUN);
		//filtro per tipo processo se passato in input
		if (tipoProcesso != null) {
			processoEntity.setTipo(tipoProcesso);
		}
		List<A4gtProcesso> listaProcessiAttivi = processoDao.findAll(Example.of(processoEntity));
		//mapping dto
		listaProcessiAttivi.forEach(p -> {
			ProcessoIstruttoriaDto proc = new ProcessoIstruttoriaDto();
			proc.setIdProcesso(p.getId());
			proc.setTipoProcesso(p.getTipo());
			proc.setStatoProcesso(p.getStato());
			proc.setPercentualeAvanzamento(p.getPercentualeAvanzamento());
			proc.setDatiElaborazioneProcesso(recuperaDatiElaborazione(p.getDatiElaborazione()));
			listaProcessi.add(proc);
		});
		return listaProcessi;
	}
	
	private DatiElaborazioneProcesso recuperaDatiElaborazione(String datiElaborazione) {
		DatiElaborazioneProcesso  datiElaborazioneProcessoIstruttoria = new DatiElaborazioneProcesso();
		if (StringUtils.isEmpty(datiElaborazione)) return datiElaborazioneProcessoIstruttoria;
		try {
			datiElaborazioneProcessoIstruttoria = objectMapper.readValue(datiElaborazione, DatiElaborazioneProcesso.class);
		} catch (IOException e) {
			logger.error("Errore nella mapping dei dati elaborazione processo istruttoria: {}",datiElaborazione);
		}
		return datiElaborazioneProcessoIstruttoria;
	}
}
