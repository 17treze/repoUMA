package it.tndigitale.a4gistruttoria.component.acs;

import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.repository.dao.ColturaInterventoDao;

@Component
public class ControlloCompatibilitaColturaIntervento {
	
	@Autowired
	private ColturaInterventoDao colturaInterventoDao;
	
	public boolean checkColturaIntervento(Integer annoRiferimento, String codiceColtura3, CodiceInterventoAgs chiaveIntervento) {

		return colturaInterventoDao.findByCodiceColtura3AndIdInterventoDu(
				codiceColtura3,
				chiaveIntervento,
				annoRiferimento) != null;
	}
}