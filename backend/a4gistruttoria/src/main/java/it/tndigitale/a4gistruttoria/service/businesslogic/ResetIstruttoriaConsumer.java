package it.tndigitale.a4gistruttoria.service.businesslogic;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.CambioStatoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;


@Component
public abstract class ResetIstruttoriaConsumer extends CambioStatoIstruttoria implements Consumer<IstruttoriaModel> {
	
	private static Logger log = LoggerFactory.getLogger(ResetIstruttoriaConsumer.class);
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	@Autowired
	private TransizioneIstruttoriaDao transizioneDao;
	
	@Override
	public void accept(IstruttoriaModel istruttoria) {
		try {
			cambiaStato(istruttoria.getId(), StatoIstruttoria.RICHIESTO);
		} catch (ElaborazioneIstruttoriaException e) {
			throw new RuntimeException("Errore cambiando lo stato dell'istruttoria con id =  " + istruttoria.getId(), e);
		}
		// Elimino tutte le transizioni relative all'istruttoria
		transizioneDao.deleteAll(istruttoria.getTransizioni());
		istruttoria.setDataUltimoCalcolo(LocalDateTime.now());
		istruttoriaDao.save(istruttoria);
		log.debug("Fine reset per istruttoria con id {} ",istruttoria.getId());		
	}

}
