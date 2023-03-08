package it.tndigitale.a4gistruttoria.component.acs;

import java.util.Optional;

import it.tndigitale.a4gistruttoria.repository.model.RegistroOlioNazionaleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.repository.dao.RegistroOlioNazionaleDao;

@Component
public class ControlloRegistroOlioNazionale {
	
	@Autowired
	private RegistroOlioNazionaleDao registroOlioNazionaleDao;
	
	public Boolean checkRegistroOlioNazionale(String cuaaIntestatario, Integer annoCampagna) {
		Optional<RegistroOlioNazionaleModel> regOlioNazionale =
				registroOlioNazionaleDao
						.findByCuaaIntestatarioAndInizioCampagnaLessThanEqualAndFineCampagnaGreaterThanEqual(
								cuaaIntestatario, annoCampagna, annoCampagna);
		return regOlioNazionale.isPresent();
	}
}
