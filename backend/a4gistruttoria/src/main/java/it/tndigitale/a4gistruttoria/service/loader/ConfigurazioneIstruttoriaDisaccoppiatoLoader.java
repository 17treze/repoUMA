package it.tndigitale.a4gistruttoria.service.loader;

import it.tndigitale.a4gistruttoria.repository.dao.ConfigurazioneIstruttoriaDisaccoppiatoDao;
import it.tndigitale.a4gistruttoria.repository.model.ConfigurazioneIstruttoriaDisaccoppiatoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Component
public class ConfigurazioneIstruttoriaDisaccoppiatoLoader {

    @Autowired
    private ConfigurazioneIstruttoriaDisaccoppiatoDao configurazioneIstruttoriaDisaccoppiatoDao;

    public ConfigurazioneIstruttoriaDisaccoppiatoLoader setComponents(ConfigurazioneIstruttoriaDisaccoppiatoDao configurazioneIstruttoriaDisaccoppiatoDao) {
        this.configurazioneIstruttoriaDisaccoppiatoDao = configurazioneIstruttoriaDisaccoppiatoDao;
        return this;
    }

    public ConfigurazioneIstruttoriaDisaccoppiatoModel loadBy(Integer annoCampagna) {
        return configurazioneIstruttoriaDisaccoppiatoDao
                .findByCampagna(annoCampagna)
                .orElseThrow(() -> new EntityNotFoundException("Configurazione istruttoria non trovata per l'anno campagna " + annoCampagna));
    }

}
