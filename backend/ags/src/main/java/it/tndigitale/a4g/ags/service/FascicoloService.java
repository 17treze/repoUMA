package it.tndigitale.a4g.ags.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.ags.dto.Fascicolo;
import it.tndigitale.a4g.ags.dto.FascicoloFilter;
import it.tndigitale.a4g.ags.repository.dao.FascicoloDao;

/**
 * 
 * @author S.DeLuca
 *
 */
@Service
public class FascicoloService {

	@Autowired
	private FascicoloDao fascicoloDao;

	@Transactional(readOnly = true)
	public List<Fascicolo> getFascicoli(FascicoloFilter fascicolo) {
		return fascicoloDao.getFascicoli(fascicolo);
	}
	
	@Transactional(readOnly = true)
	public Fascicolo getFascicolo(Long idFascicolo) throws Exception{
		return fascicoloDao.getFascicolo(idFascicolo);
	}
	
	@Transactional(readOnly = true)
	public boolean checkFascicoloValido(String cuaa) {
		return fascicoloDao.checkFascicoloValido(cuaa);
	}
}
