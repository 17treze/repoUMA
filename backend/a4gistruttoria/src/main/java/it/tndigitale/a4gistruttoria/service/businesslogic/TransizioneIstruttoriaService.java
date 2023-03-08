package it.tndigitale.a4gistruttoria.service.businesslogic;

import java.util.Date;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.repository.dao.StatoLavSostegnoDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;

@Service
public class TransizioneIstruttoriaService {

	@Autowired
	private StatoLavSostegnoDao daoStatoLavSostegno;

	@Autowired
	private TransizioneIstruttoriaDao transizioneDao;

	public TransizioneIstruttoriaModel avviaTransizioneCalcolo(IstruttoriaModel istruttoria) throws Exception {
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		transizione.setA4gdStatoLavSostegno2(istruttoria.getA4gdStatoLavSostegno());
		transizione.setIstruttoria(istruttoria);
		transizione.setDataEsecuzione(new Date());
		return transizioneDao.save(transizione);
	}

	public TransizioneIstruttoriaModel aggiornaTransizione(TransizioneIstruttoriaModel transizione) throws Exception {
		return transizioneDao.save(transizione);
	}

	public TransizioneIstruttoriaModel aggiornaTransizioneCalcoloOK(TransizioneIstruttoriaModel transizione) throws Exception {
		A4gdStatoLavSostegno stato = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.CONTROLLI_CALCOLO_OK.getStatoIstruttoria());
		transizione.setA4gdStatoLavSostegno1(stato);
		transizione = transizioneDao.save(transizione);
		return transizione;
	}


	public TransizioneIstruttoriaModel aggiornaTransizioneCalcoloKO(TransizioneIstruttoriaModel transizione) throws Exception {
		A4gdStatoLavSostegno stato = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.CONTROLLI_CALCOLO_KO.getStatoIstruttoria());
		transizione.setA4gdStatoLavSostegno1(stato);
		transizione = transizioneDao.save(transizione);
		return transizione;
	}
	
	public TransizioneIstruttoriaModel caricaUltimaTransizione(IstruttoriaModel istruttoria, StatoIstruttoria stato) throws Exception {
		A4gdStatoLavSostegno statoEntity = daoStatoLavSostegno.findByIdentificativo(stato.getStatoIstruttoria());
		return istruttoria.getTransizioni().stream() // gia ordinato
				.filter(t -> statoEntity.getId().equals( t.getA4gdStatoLavSostegno1().getId()))
				.findFirst()
				.orElseThrow(() -> new EntityNotFoundException(String.format("Nessuna transizione trovata per l'istruttoria id: %d e stato %s", istruttoria.getId(), stato)));
	}
	
}
