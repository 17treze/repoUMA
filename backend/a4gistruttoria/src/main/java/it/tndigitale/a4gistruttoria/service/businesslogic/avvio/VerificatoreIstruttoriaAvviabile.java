package it.tndigitale.a4gistruttoria.service.businesslogic.avvio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;

@Component
public class VerificatoreIstruttoriaAvviabile implements VerificatoreIstruttoria {
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	
	@Autowired
	private DomandaUnicaDao domandaDao;

	@Override
	public boolean isIstruttoriaAvviabile(Long idDomanda, TipoIstruttoria tipologia, Sostegno sostegno) {
		DomandaUnicaModel domanda = domandaDao.findById(idDomanda).get();
		// controllo che il sostegno sia stato effettivamente richiesto
		boolean sostegnorichiesto = domanda.getSostegni().stream().anyMatch(sostegnoDomanda -> sostegno.equals(sostegnoDomanda.getSostegno()));
		if (!sostegnorichiesto) return false;
		// Cerco se esiste gia una istruttoria di quella tipologia per quel sostegno su quella domanda ... se esiste => false , true altrimenti
		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, sostegno, tipologia);
		return istruttoria == null;
	}
}
