package it.tndigitale.a4g.ags.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.ags.dto.CatastoPianoColture;
import it.tndigitale.a4g.ags.dto.DatiAggiuntiviParticella;
import it.tndigitale.a4g.ags.dto.InfoParticella;
import it.tndigitale.a4g.ags.repository.dao.PianiColtureDao;

@Service
public class PianiColtureServiceImpl implements PianiColtureService {

	@Autowired
	PianiColtureDao pianiColtureDao;

	@Override
	public boolean eseguiCaricaPiani(Date dateFrom) {
		return pianiColtureDao.eseguiCaricaPiani(dateFrom);
	}

	@Override
	public List<InfoParticella> getParticelleFromPianiColture(Integer anno) {
		return pianiColtureDao.getParticelleFromPianiColture(anno);
	}

	@Override
	public List<CatastoPianoColture> getPianiColtureByInfoParticella(InfoParticella info, Integer anno) {
		return pianiColtureDao.getPianiColtureByInfoParticella(info, anno);
	}

	@Override
	public Integer getParticelleDistinctCount(Integer anno) {
		return pianiColtureDao.getParticelleDistinctCount(anno);
	}

	@Override
	public DatiAggiuntiviParticella getDatiAggiuntiviPerParticella(InfoParticella info, Integer anno) {
		return pianiColtureDao.getDatiAggiuntiviParticella(info, anno);
	}

}
