package it.tndigitale.a4gistruttoria.strategy;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.ElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.ResetIstruttoriaDisaccoppiatoConsumer;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;
import it.tndigitale.a4gistruttoria.service.businesslogic.superficie.ResetIstruttoriaSuperficieConsumer;
import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.ResetIstruttoriaZootecniaConsumer;

@Component
public class ResetIstruttoriaStrategy implements ElaborazioneIstruttoria {
	
	private static Logger log = LoggerFactory.getLogger(ResetIstruttoriaStrategy.class);
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	@Autowired
	private ResetIstruttoriaZootecniaConsumer resetIstruttoriaZootecniaConsumer;
	@Autowired
	private ResetIstruttoriaDisaccoppiatoConsumer resetIstruttoriaDisaccoppiatoConsumer;
	@Autowired
	private ResetIstruttoriaSuperficieConsumer resetIstruttoriaSuperficieConsumer;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void elabora(Long idIstruttoria) throws ElaborazioneIstruttoriaException {
		log.debug("Inizio reset per istruttoria con id {} ",idIstruttoria);
		IstruttoriaModel istruttoria = 
				istruttoriaDao
					.findById(idIstruttoria)
					.orElseThrow(() ->
						new EntityNotFoundException("Istruttoria[" + idIstruttoria + "] non trovata"));
		
		// Reset solo se l'istruttoria non è BLOCCATA e lo stato è valido
		if (!StatoIstruttoria.isStatoValidoPerResetIstruttoria(istruttoria.getStato())
				&& Boolean.TRUE.equals(istruttoria.getBloccataBool())) {
			log.debug("Reset non possibile: l'istruttoria con id {} non soddisfa i requisiti (bloccata o stato di partenza non valido)",idIstruttoria);
		} else {
			final Map<Sostegno, Consumer<IstruttoriaModel>> calcoloSostegni = new HashMap<>();
			calcoloSostegni.put(Sostegno.ZOOTECNIA, resetIstruttoriaZootecniaConsumer);
			calcoloSostegni.put(Sostegno.DISACCOPPIATO, resetIstruttoriaDisaccoppiatoConsumer);
			calcoloSostegni.put(Sostegno.SUPERFICIE, resetIstruttoriaSuperficieConsumer);
			
			calcoloSostegni.entrySet().stream()
				.filter(it -> it.getKey().equals(istruttoria.getSostegno()))
				.map(Map.Entry::getValue)
				.forEach(cons -> cons.accept(istruttoria));
		}
	}
}
