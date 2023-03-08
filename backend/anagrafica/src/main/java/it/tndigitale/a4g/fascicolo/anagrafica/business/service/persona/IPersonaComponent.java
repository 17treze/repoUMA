package it.tndigitale.a4g.fascicolo.anagrafica.business.service.persona;

import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaDto;

public interface IPersonaComponent<T extends PersonaDto> {
	DatiAperturaFascicoloDto getDatiAnagraficiSintesi(String codiceFiscale, final Integer idValidazione);
	
	T getDatiPersona(String codiceFiscale, final Integer idValidazione);
}
