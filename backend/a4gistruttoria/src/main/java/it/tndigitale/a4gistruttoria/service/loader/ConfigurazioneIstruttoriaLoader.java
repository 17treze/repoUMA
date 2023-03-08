package it.tndigitale.a4gistruttoria.service.loader;

import it.tndigitale.a4gistruttoria.repository.dao.ConfigurazioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.ConfigurazioneIstruttoriaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Component
public class ConfigurazioneIstruttoriaLoader {

    @Autowired
    private ConfigurazioneIstruttoriaDao configurazioneIstruttoriaDao;

    public ConfigurazioneIstruttoriaLoader setComponents(ConfigurazioneIstruttoriaDao configurazioneIstruttoriaDao) {
        this.configurazioneIstruttoriaDao = configurazioneIstruttoriaDao;
        return this;
    }

    public ConfigurazioneIstruttoriaModel loadBy(Integer annoCampagna) {
        return configurazioneIstruttoriaDao
                .findByCampagna(annoCampagna)
                .orElseThrow(() -> new EntityNotFoundException("Configurazione istruttoria non trovata per l'anno campagna " + annoCampagna));
    }

}
