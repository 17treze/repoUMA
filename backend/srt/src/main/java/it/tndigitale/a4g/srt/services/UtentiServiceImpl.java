package it.tndigitale.a4g.srt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.srt.dto.Utente;
import it.tndigitale.a4g.srt.dto.UtenteFilter;
import it.tndigitale.a4g.srt.repository.dao.UtentiDao;

@Service
public class UtentiServiceImpl implements UtentiService {
	
	@Autowired
	UtentiDao utentiDao;

	@Override
	public List<Utente> findByFilter(UtenteFilter filter) {
		return utentiDao.findByFilter(filter);
	}

}
