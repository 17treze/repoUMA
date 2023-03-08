package it.tndigitale.a4gutente.service.loader;

import java.util.Optional;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import it.tndigitale.a4gutente.repository.dao.IPersonaDao;
import it.tndigitale.a4gutente.repository.model.PersonaEntita;

@Component
public class PersonaLoader extends AbstractLoader{

    @Autowired
    private IPersonaDao personaDao;

    public PersonaLoader setPersonaDao(IPersonaDao personaDao) {
        this.personaDao = personaDao;
        return this;
    }

    @Override
    public PersonaEntita load(Long idPersona) {
        checkId(idPersona);
        return personaDao.findById(idPersona)
                         .orElseThrow(() -> new ValidationException("Persona con identificativo " + idPersona + " non trovato"));
    }

    public PersonaEntita loadBy(String codiceFiscale) {
        if (StringUtils.isEmpty(codiceFiscale)) {
            throw new ValidationException("CodiceFiscale dell'oggetto da caricare NULL o Empty");
        }
        PersonaEntita personaEntita = personaDao.findOneByCodiceFiscale(codiceFiscale);
        return Optional.ofNullable(personaEntita)
                       .orElseThrow(() -> new ValidationException("Persona con codice fiscale " + codiceFiscale + " non trovato"));
    }

}
