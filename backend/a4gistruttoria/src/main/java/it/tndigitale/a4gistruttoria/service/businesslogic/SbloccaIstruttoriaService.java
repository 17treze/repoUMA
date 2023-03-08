package it.tndigitale.a4gistruttoria.service.businesslogic;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.CalcoloSostegnoException;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SbloccaIstruttoriaService implements ElaborazioneIstruttoria {
	
	private static final Logger logger = LoggerFactory.getLogger(SbloccaIstruttoriaService.class);
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;

	private void sbloccaIstruttoria(final Long idIstruttoria) throws CalcoloSostegnoException {
		logger.debug("sbloccaIstruttoria: blocca l'istruttoria {}", idIstruttoria);
		Optional<IstruttoriaModel> istruttoriaOpt = istruttoriaDao.findById(idIstruttoria);
		IstruttoriaModel istruttoria = istruttoriaOpt.orElseThrow(() -> new CalcoloSostegnoException("Nessuna istruttoria trovata con identificativo " + idIstruttoria));
		istruttoria.setBloccataBool(Boolean.FALSE);
		istruttoriaDao.save(istruttoria);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void elabora(Long idIstruttoria) throws ElaborazioneIstruttoriaException {
		sbloccaIstruttoria(idIstruttoria);
	}
}
