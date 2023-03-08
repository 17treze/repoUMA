package it.tndigitale.a4gutente.service.loader;

import javax.validation.ValidationException;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

public abstract class AbstractLoader {

    public abstract <T extends EntitaDominio> T load(Long id);

    protected void checkId(Long id) {
        if (id == null) {
            throw new ValidationException("Identificativo dell'oggetto da caricare NULL");
        }
    }
}
