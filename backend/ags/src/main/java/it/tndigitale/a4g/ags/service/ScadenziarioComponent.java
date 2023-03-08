package it.tndigitale.a4g.ags.service;

import it.tndigitale.a4g.ags.dto.ScadenziarioRicevibilitaDto;
import it.tndigitale.a4g.ags.repository.dao.ConfigurazioneRicevibilitaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
class ScadenziarioComponent {
    @Autowired
    ConfigurazioneRicevibilitaDao ricevibilitaDao;

    // SUPPORTS in alternativa
    @Transactional
    ScadenziarioRicevibilitaDto getScadenziarioRicevibilita(Integer campagna) {
        return ricevibilitaDao.getScandenziario(campagna);
    }
}
