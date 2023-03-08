package it.tndigitale.a4g.fascicolo.anagrafica.business.service.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SportelloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.SportelloService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.CambioSportelloPatch;
import it.tndigitale.a4g.framework.time.Clock;

@Component
public class CambioSportelloValidation {
	
    @Autowired
    private SportelloService sportelloService;
    @Autowired
    private Clock clock;
    
	public SportelloModel validaCambioSportello(CambioSportelloPatch patch, String identificativoAttualeCaa, SportelloModel oldSportello) throws Exception {
		SportelloModel newSportello = sportelloService.verificaAbilitazioneSportello(patch.getIdNuovoSportello());
		//verifica se CAA nuovo sportello è diverso da CAA attuale sportello
		if (!identificativoAttualeCaa.equalsIgnoreCase(newSportello.getCentroAssistenzaAgricola().getCodiceFiscale())) {
			throw new IllegalArgumentException("Non è possibile effettuare cambio sportello. CAA nuovo sportello è diverso da CAA attuale sportello");
		}
		// verifica se sto passando da uno sportello ad un altro. non è possibile fare cambio con precedente
		if (patch.getIdNuovoSportello().equals(oldSportello.getId())) {
			throw new IllegalArgumentException(String.format("Non è possibile effettuare cambio sportello. Mandato già affidato allo sportello %s" , oldSportello.getDenominazione()));
		}
		// verifica che la data immessa sia > sysdate + 1  (dtInizio nuovo sportello deve essere da sysDate + 1dd in poi )
		if (clock.today().compareTo(patch.getDataCambio()) >= 0) {
			throw new IllegalArgumentException("La data della modifica deve essere successiva alla data corrente");
		}
		return newSportello;
	}

}
