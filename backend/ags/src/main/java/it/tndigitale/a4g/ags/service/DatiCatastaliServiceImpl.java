package it.tndigitale.a4g.ags.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.ags.dto.RegioneCatastale;
import it.tndigitale.a4g.ags.repository.dao.DatiCatastaliDao;

@Service
public class DatiCatastaliServiceImpl implements DatiCatastaliService {
	
	@Autowired
	DatiCatastaliDao datiCatastaliDao;

	@Override
	public RegioneCatastale getRegioneByCodNazionaleAndDataValidita(String codNazionale, Date dataValidita) {

		if (dataValidita == null)
			dataValidita = new Date();
		
		return datiCatastaliDao.getRegioneByCodNazionaleAndDataValidita(codNazionale, dataValidita);
	}

}
