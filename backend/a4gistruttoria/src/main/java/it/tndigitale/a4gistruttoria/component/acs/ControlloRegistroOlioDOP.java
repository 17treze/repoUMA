package it.tndigitale.a4gistruttoria.component.acs;

import java.util.Optional;

import it.tndigitale.a4gistruttoria.repository.model.RegistroOlioDOPModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.repository.dao.RegistroOlioDOPDao;

@Component
public class ControlloRegistroOlioDOP {
	
	@Autowired
	private RegistroOlioDOPDao registroOlioDOPDao;
	
	public Boolean checkRegistroOlioDOP(String cuaaIntestatario, Integer annoCampagna) {
		Optional<RegistroOlioDOPModel> regOlioDOP =
				registroOlioDOPDao
						.findByCuaaIntestatarioAndInizioCampagnaLessThanEqualAndFineCampagnaGreaterThanEqual(
								cuaaIntestatario, annoCampagna, annoCampagna);
		return regOlioDOP.isPresent();
	}
}
