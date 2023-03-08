package it.tndigitale.a4gistruttoria.service.businesslogic;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.CalcoloSostegnoException;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;

@Service
public class BloccaIstruttoriaService implements ElaborazioneIstruttoria {
	
	private static final Logger logger = LoggerFactory.getLogger(BloccaIstruttoriaService.class);
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;

	private void bloccaIstruttoria(final Long idIstruttoria) throws CalcoloSostegnoException {
		logger.debug("bloccaIstruttoria: blocca l'istruttoria {}", idIstruttoria);
		Optional<IstruttoriaModel> istruttoriaOpt = istruttoriaDao.findById(idIstruttoria);
		IstruttoriaModel istruttoria = istruttoriaOpt.orElseThrow(() -> new CalcoloSostegnoException("Nessuna istruttoria trovata con identificativo " + idIstruttoria));
		istruttoria.setBloccataBool(Boolean.TRUE);
		istruttoriaDao.save(istruttoria);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void elabora(Long idIstruttoria) throws ElaborazioneIstruttoriaException {
		bloccaIstruttoria(idIstruttoria);
	}
}
