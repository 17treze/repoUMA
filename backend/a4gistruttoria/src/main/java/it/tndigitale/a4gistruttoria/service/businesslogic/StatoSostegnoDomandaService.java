package it.tndigitale.a4gistruttoria.service.businesslogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.StatoLavSostegnoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

@Service
@Deprecated
public class StatoSostegnoDomandaService {

	@Autowired
	private IstruttoriaDao istruttoriaDao;

	@Autowired
	private StatoLavSostegnoDao daoStatoLavSostegno;

	public A4gdStatoLavSostegno caricaStatoLavorazioneSostegno(StatoIstruttoria codiceStato) throws Exception {
		return daoStatoLavSostegno.findByIdentificativo(codiceStato.getStatoIstruttoria());

	}

	public IstruttoriaModel aggiornaLavorazioneSostegnoDellaDomanda(IstruttoriaModel lavorazione) throws Exception {
		return istruttoriaDao.save(lavorazione);
	}
}
