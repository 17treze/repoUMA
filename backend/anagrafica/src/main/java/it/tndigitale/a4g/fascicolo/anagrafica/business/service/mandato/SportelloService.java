package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SportelloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.SportelloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.CaaService;

@Service
public class SportelloService {

    @Autowired
    private SportelloDao sportelloDao;
    @Autowired
    private CaaService caaService;

    
    public SportelloModel verificaAbilitazioneSportello(Long idNuovoSportello) throws Exception {
		//check se sportello abilitato: cambioSportello.idNuovoSportello
		SportelloModel newSportello = sportelloDao.findById(idNuovoSportello).orElseThrow(() -> new EntityNotFoundException(String.format("Nessun sportello trovato per id: %d", idNuovoSportello)));
		if (!caaService.verificaSportelloIsAbilitato(newSportello.getIdentificativo()).booleanValue()) {
			throw new IllegalArgumentException(String.format("Non è possibile effettuare cambio sportello. Sportello non abilitato per l'utente corrente: %d", idNuovoSportello));
		}
		return newSportello;
	}


}
