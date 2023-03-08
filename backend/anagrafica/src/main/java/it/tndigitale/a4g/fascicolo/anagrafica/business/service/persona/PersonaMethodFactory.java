package it.tndigitale.a4g.fascicolo.anagrafica.business.service.persona;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.support.PersonaSelector;

@Component
public class PersonaMethodFactory {
	@Autowired
	private PersonaFisicaComponent personaFisicaComponent;

	@Autowired
	private PersonaGiuridicaComponent personaGiuridicaComponent;

	public IPersonaComponent from(final String codiceFiscale) {
		if (PersonaSelector.isPersonaFisica(codiceFiscale)) {
			return personaFisicaComponent;
		} else {
			return personaGiuridicaComponent;
		}
	}
}
