package it.tndigitale.a4gutente.service.loader;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gutente.repository.dao.IDomandaRegistrazioneUtenteDao;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;

@Component
public class DomandaLoader extends AbstractLoader {

    @Autowired
    private IDomandaRegistrazioneUtenteDao iDomandaRegistrazioneUtenteDao;

    public DomandaRegistrazioneUtente load(Long idDomanda) {
        checkId(idDomanda);
        return iDomandaRegistrazioneUtenteDao.findById(idDomanda)
                                             .orElseThrow(() ->
                                                     new ValidationException("Domanda con identificativo " + idDomanda + " non presente."));
    }

}
