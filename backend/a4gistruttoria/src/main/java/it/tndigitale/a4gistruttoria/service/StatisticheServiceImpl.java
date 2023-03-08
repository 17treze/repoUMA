package it.tndigitale.a4gistruttoria.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4gistruttoria.dto.StatisticaDu;
import it.tndigitale.a4gistruttoria.repository.dao.StatisticheDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtStatisticaDu;
import it.tndigitale.a4gistruttoria.util.StatisticaDtoToEntityConverter;
import it.tndigitale.a4gistruttoria.util.TipologiaStatistica;

@Service
public class StatisticheServiceImpl implements StatisticheService {
	
	@Autowired
	StatisticheDao statisticheDao;
	
	@Autowired
	StatisticaDtoToEntityConverter converter;
	
	private static final Logger logger = LoggerFactory.getLogger(StatisticheServiceImpl.class);
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void salvaStatistica(StatisticaDu statisticaDu, TipologiaStatistica tipoStatistica) {
		A4gtStatisticaDu entity = converter.convert(statisticaDu);
		entity.setTipoStatistica(tipoStatistica);
		statisticheDao.save(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void cancellaStatisticheEsistenti(TipologiaStatistica tipoStatistica, Integer annoCampagna, List<String> misure) {
		long affectedRows = CollectionUtils.isEmpty(misure) ? 
				statisticheDao.deleteByTipoStatisticaAndC110(tipoStatistica, annoCampagna):
				statisticheDao.deleteByTipoStatisticaAndC110AndC109a(tipoStatistica, annoCampagna, misure)	;
		logger.info("Cancellate {} righe per tipo {} anno {} e misure {}", affectedRows, tipoStatistica, annoCampagna, misure);		
	}
}
