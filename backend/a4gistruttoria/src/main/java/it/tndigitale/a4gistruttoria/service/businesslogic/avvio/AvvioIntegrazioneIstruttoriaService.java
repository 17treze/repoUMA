package it.tndigitale.a4gistruttoria.service.businesslogic.avvio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;

@Service
public class AvvioIntegrazioneIstruttoriaService implements ElaborazioneIstruttoria {
	
	@Autowired
	private AvvioIstruttoriaService avvioIstruttoria;

	@Autowired
	private IstruttoriaDao istruttoriaDao;

	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ElaborazioneIstruttoriaException.class)
	public void elabora(Long idIstruttoria) throws ElaborazioneIstruttoriaException {
		IstruttoriaModel istruttoria = istruttoriaDao.findById(idIstruttoria).get();
		avvioIstruttoria.avvioIstruttoria(
				istruttoria.getDomandaUnicaModel().getId(), 
				TipoIstruttoria.INTEGRAZIONE,
				istruttoria.getSostegno()
			);

	}

}
