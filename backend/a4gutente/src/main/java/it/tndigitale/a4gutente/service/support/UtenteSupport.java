package it.tndigitale.a4gutente.service.support;

import it.tndigitale.a4gutente.dto.Utente;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;
import it.tndigitale.a4gutente.repository.model.PersonaEntita;
import it.tndigitale.a4gutente.service.loader.PersonaLoader;
import it.tndigitale.a4gutente.service.loader.UtenteLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static it.tndigitale.a4gutente.service.builder.UtenteBuilder.convertSenzaProfiliESedi;

@Component
public class UtenteSupport {

    @Autowired
    private UtenteLoader utenteLoader;
    @Autowired
    private PersonaLoader personaLoader;

    public UtenteSupport setUtenteLoader(UtenteLoader utenteLoader,
                                         PersonaLoader personaLoader) {
        this.utenteLoader = utenteLoader;
        this.personaLoader = personaLoader;
        return this;
    }

    public Utente caricaUtenteConAnagrafica(Long idUtente) {
        A4gtUtente utente = utenteLoader.load(idUtente);
        PersonaEntita persona = personaLoader.loadBy(utente.getCodiceFiscale());
        return convertSenzaProfiliESedi(utente, persona);
    }

}
