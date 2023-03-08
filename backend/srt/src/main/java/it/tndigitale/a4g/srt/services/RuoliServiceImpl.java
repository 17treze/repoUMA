package it.tndigitale.a4g.srt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.srt.repository.dao.RuoliDao;

@Service
public class RuoliServiceImpl implements RuoliService {
	
	@Autowired
	RuoliDao ruoliDao;

	@Override
	public List<String> getRuoliPerUtente(String codiceFiscale) {
		
		return ruoliDao.getRuoliPerUtente(codiceFiscale);
	}

}
