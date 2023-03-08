package it.tndigitale.a4gutente.service.loader;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4gutente.repository.dao.IUtenteCompletoDao;
import it.tndigitale.a4gutente.repository.model.A4gtUtente;

@Component
public class UtenteLoader extends AbstractLoader  {

    @Autowired
    private IUtenteCompletoDao utenteCompletoDao;
    @Autowired
    private UtenteComponent utenteComponent;


    public UtenteLoader setComponents(IUtenteCompletoDao utenteCompletoDao,
                                      UtenteComponent utenteComponent) {
        this.utenteCompletoDao = utenteCompletoDao;
        this.utenteComponent = utenteComponent;
        return this;
    }

    @Override
    public A4gtUtente load(Long idUtente) {
        checkId(idUtente);
        return utenteCompletoDao.findById(idUtente)
                                .orElseThrow(() -> new ValidationException("Utente con identificativo " + idUtente + " non trovato"));
    }

    public A4gtUtente loadUtenteConnesso() {
        String identificativo = utenteComponent.utenza();
        return utenteCompletoDao.findByIdentificativo(identificativo);
    }
}
