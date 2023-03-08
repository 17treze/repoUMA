package it.tndigitale.a4g.fascicolo.anagrafica.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.FascicoloAgsDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.persona.PersonaMethodFactory;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.legacy.CaricaAgsDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaFisicaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaGiuridicaDto;

@Service
public class PersonaService {

	@Autowired
	private FascicoloAgsDao fascicoloAgsDao;
	@Autowired
	private PersonaMethodFactory personaMethodFactory;

	public List<CaricaAgsDto> getCariche(String cf ,String cuaa) {
		return fascicoloAgsDao.getCariche(cf,cuaa);
	}
	
	public PersonaFisicaDto getPersonaFisica(final String cf, final Integer idValidazione) {
		return (PersonaFisicaDto) personaMethodFactory.from(cf).getDatiPersona(cf, idValidazione);
	}
	
	public PersonaGiuridicaDto getPersonaGiuridica(final String cf, final Integer idValidazione) {
		return (PersonaGiuridicaDto) personaMethodFactory.from(cf).getDatiPersona(cf, idValidazione);
	}
}
