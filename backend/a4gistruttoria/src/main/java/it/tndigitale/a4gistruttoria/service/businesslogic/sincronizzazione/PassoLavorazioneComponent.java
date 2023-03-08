package it.tndigitale.a4gistruttoria.service.businesslogic.sincronizzazione;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.TransizioneIstruttoriaService;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
class PassoLavorazioneComponent {
    @Autowired
    private TransizioneIstruttoriaService transizioneService;

    private static final Logger logger = LoggerFactory.getLogger(PassoLavorazioneComponent.class);

    Optional<List<PassoTransizioneModel>> recuperaPassiLavorazioneIstruttoria(IstruttoriaModel istruttoria) {
        // Disaccoppiato
        Optional<List<PassoTransizioneModel>> passi = Optional.empty();
        StatoIstruttoria stato = adjustStatoLavorazione(
                StatoIstruttoria.valueOfByIdentificativo(istruttoria.getA4gdStatoLavSostegno().getIdentificativo()));
        try {
            passi = Optional.of(
                    transizioneService.caricaUltimaTransizione(istruttoria, stato)
                            .getPassiTransizione()
                            .stream().collect(Collectors.toList())
            );
        } catch (Exception e) {
            logger.warn("recuperaPassiLavorazione: errore caricando le transizioni e i passi di lavorazione dell'istruttoria " + istruttoria.getId(), e.getMessage());
        }

        return passi;
    }

    private StatoIstruttoria adjustStatoLavorazione(StatoIstruttoria stato) {
        if (StatoIstruttoria.CONTROLLI_CALCOLO_OK.compareTo(stato) == 0)
            return StatoIstruttoria.CONTROLLI_CALCOLO_OK;
        if (StatoIstruttoria.NON_AMMISSIBILE.compareTo(stato) < 0) {
            return StatoIstruttoria.CONTROLLI_CALCOLO_OK;
        } else {
            return StatoIstruttoria.CONTROLLI_CALCOLO_KO;
        }
    }
}
