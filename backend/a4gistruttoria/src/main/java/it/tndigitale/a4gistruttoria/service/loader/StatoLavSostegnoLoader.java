package it.tndigitale.a4gistruttoria.service.loader;

import it.tndigitale.a4gistruttoria.repository.dao.StatoLavorazioneSostegnoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Component
public class StatoLavSostegnoLoader {

    @Autowired
    private StatoLavorazioneSostegnoDao statoLavorazioneSostegnoDao;

    public StatoLavSostegnoLoader setComponents(StatoLavorazioneSostegnoDao statoLavorazioneSostegnoDao) {
        this.statoLavorazioneSostegnoDao = statoLavorazioneSostegnoDao;
        return this;
    }

    public A4gdStatoLavSostegno loadByIdentificativo(String identificativo) {
        return statoLavorazioneSostegnoDao.findByIdentificativo(identificativo)
                                          .orElseThrow(() ->
                                                  new EntityNotFoundException("Stato lavorazione sostegno con identificativo "
                                                          + identificativo + " non trovato"));
    }

}
