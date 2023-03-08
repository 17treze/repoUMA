package it.tndigitale.a4gistruttoria.service.businesslogic.configurazioneistruttoria;

import it.tndigitale.a4gistruttoria.dto.InterventoDto;
import it.tndigitale.a4gistruttoria.dto.InterventoDuPremio;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;

public class InterventoDuPremioBuilder {
	
	
	public static InterventoDuPremio from(String codAgea, Sostegno sostegno){
		InterventoDuPremio interventoDuPremio =  new InterventoDuPremio();
		InterventoDto interventoDto = new InterventoDto();
		interventoDto.setCodiceAgea(codAgea);
		interventoDuPremio.setIntervento(interventoDto);
		interventoDuPremio.getIntervento().setIdentificativoSostegno(sostegno);
		return interventoDuPremio;
	}

}
