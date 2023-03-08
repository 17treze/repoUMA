package it.tndigitale.a4g.proxy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.ControlloAnomalieCoordinamentoDao;

@Service
public class AnomalieCoordinamentoServiceImpl implements AnomalieCoordinamentoService {
	
	@Autowired
	ControlloAnomalieCoordinamentoDao controlloAnomalieCoordinamentoDao;

	@Override
	public long recuperaEsitoControlloAnomalieCoordinamento(long annoCampagna, long idParcella) {
		return controlloAnomalieCoordinamentoDao.findByAnnoCampagnaAndIdParcella(annoCampagna, idParcella);
	}

}
