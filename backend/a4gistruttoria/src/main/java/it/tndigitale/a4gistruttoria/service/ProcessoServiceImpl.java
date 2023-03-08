package it.tndigitale.a4gistruttoria.service;

import it.tndigitale.a4gistruttoria.dto.Processo;
import it.tndigitale.a4gistruttoria.dto.ProcessoAnnoCampagnaDomandaDto;
import it.tndigitale.a4gistruttoria.dto.ProcessoFilter;
import it.tndigitale.a4gistruttoria.repository.dao.ProcessoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtProcesso;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import it.tndigitale.a4gistruttoria.strategy.processi.ProcessoStrategy;
import it.tndigitale.a4gistruttoria.strategy.processi.ProcessoStrategyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProcessoServiceImpl implements ProcessoService {

	private static final Logger logger = LoggerFactory.getLogger(ProcessoServiceImpl.class);
	@Autowired
	private ProcessoDao processoDao;
	@Autowired
	private ProcessoStrategyFactory processoFactory;


	/**
	 * Ritorna la lista dei processi attivi relativi all'istruttoria passata come parametro
	 * 
	 * @param idIstruttoria:
	 *            identifica il settore per il quale recuperare i processi
	 * @return Lista dei processi attivi
	 */
	@Override
	public ArrayList<Processo> getListaProcessiAttivi() {
		ArrayList<Processo> listaProcessi = new ArrayList<>();
		List<A4gtProcesso> listaProcessiAttivi = processoDao.findProcessiByStato(StatoProcesso.RUN);

		listaProcessiAttivi.forEach(p -> {
			Processo proc = new Processo();
			proc.setIdProcesso(p.getId());
			proc.setTipoProcesso(p.getTipo());
			proc.setStatoProcesso(p.getStato());
			proc.setPercentualeAvanzamento(p.getPercentualeAvanzamento());
			listaProcessi.add(proc);
		});
		return listaProcessi;
	}

	/**
	 * Ritorna il processo identificato dal parametro idProcesso
	 * 
	 * @param idProcesso:
	 *            l'identificativo del processo che si vuole recuperare
	 * @return Processo
	 */
	@Override
	public Processo getProcessoById(Long idProcesso) {
		Optional<A4gtProcesso> opt = processoDao.findById(idProcesso);
		if (opt.isPresent()) {
			A4gtProcesso processoEntity = opt.get();
			Processo processoDto = new Processo();
			processoDto.setIdProcesso(processoEntity.getId());
			processoDto.setStatoProcesso(processoEntity.getStato());
			processoDto.setTipoProcesso(processoEntity.getTipo());
			processoDto.setPercentualeAvanzamento(processoEntity.getPercentualeAvanzamento());
			return processoDto;
		} else {
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Processo> getProcessi(ProcessoFilter processoFilter) {
		ArrayList<Processo> listaProcessi = new ArrayList<>();
		A4gtProcesso processoEntity = new A4gtProcesso();
		processoEntity.setStato(processoFilter.getStatoProcesso());
		processoEntity.setTipo(processoFilter.getTipoProcesso());
		List<A4gtProcesso> listaProcessiAttivi = processoDao.findAll(Example.of(processoEntity));
		listaProcessiAttivi.forEach(p -> {
			Processo proc = new Processo();
			proc.setIdProcesso(p.getId());
			proc.setTipoProcesso(p.getTipo());
			proc.setStatoProcesso(p.getStato());
			proc.setPercentualeAvanzamento(p.getPercentualeAvanzamento());
			listaProcessi.add(proc);
		});
		return listaProcessi;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Processo> getProcessi(ProcessoFilter processoFilter, List<TipoProcesso> tipoProcessi) {
		ArrayList<Processo> listaProcessi = new ArrayList<>();
		A4gtProcesso processoEntity = new A4gtProcesso();
		processoEntity.setStato(processoFilter.getStatoProcesso());
		tipoProcessi.forEach(tipoProcesso -> {
			processoEntity.setTipo(tipoProcesso);
			List<A4gtProcesso> listaProcessiAttivi = processoDao.findAll(Example.of(processoEntity));
			listaProcessiAttivi.stream().map(p -> {
				Processo proc = new Processo();
				proc.setIdProcesso(p.getId());
				proc.setTipoProcesso(p.getTipo());
				proc.setStatoProcesso(p.getStato());
				proc.setPercentualeAvanzamento(p.getPercentualeAvanzamento());
				listaProcessi.add(proc);
				return listaProcessi;
			}).collect(Collectors.toList());
		});
		return listaProcessi;
	}

	@Override
	@Async("threadGestioneProcessi")
	public void avviaProcesso(ProcessoAnnoCampagnaDomandaDto processoDomanda) {
		//check su tipoProcesso e tipoIstruttoria
		ProcessoStrategy processoStrategyInput = processoFactory.getProcesso(processoDomanda.getTipoProcesso().name());
		logger.debug("Avvio la class " + processoStrategyInput.getClass());
		processoStrategyInput.avvia(processoDomanda);
	}


}
