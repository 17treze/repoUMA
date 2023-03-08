package it.tndigitale.a4g.ags.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.ags.dto.Utente;
import it.tndigitale.a4g.ags.dto.UtenteFilter;
import it.tndigitale.a4g.ags.repository.dao.UtenteDao;


@Service
public class UtenteServiceImpl implements UtenteService {

	@Autowired
	private UtenteDao utenteDao;
	
	public List<Utente> getUtenti(String codiceFiscale){
		return utenteDao.getUtenti(codiceFiscale);
	}

	@Override
	public List<Utente> getUtenti(UtenteFilter filtri) {
		return utenteDao.getUtenti(filtri);
	}
}
