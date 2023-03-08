package it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.azione;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.*;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository.JobFmeLavorazioneDao;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.Utils.UtilsFme;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.lavorazionesuolo.AzioneLavorazioneBase;
import it.tndigitale.a4g.richiestamodificasuolo.dto.fme.ResponseBodyFmeDto;
import it.tndigitale.a4g.richiestamodificasuolo.exception.SuoloException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

@Component("REFRESH_STATO_JOB_FME_LAVORAZIONE")
public class RefreshStatoJobFmeLavorazione extends AzioneLavorazioneBase<LavorazioneSuoloModel, AzioneLavorazioneBase.InputDataFme> {

    private static final Logger log = LoggerFactory.getLogger(RefreshStatoJobFmeLavorazione.class);

    @Autowired
    private JobFmeLavorazioneDao jobFmeLavorazioneDao;

    @Autowired
    private UtilsFme utilsFme;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected LavorazioneSuoloModel eseguiAzione(InputDataFme inputData) {
        return refreshStatoLavorazione(inputData.getIdLavorazione(), inputData.getUtente(), inputData.getTipoJobFME());
    }

    public LavorazioneSuoloModel refreshStatoLavorazione(Long idLavorazione, String utente, TipoJobFME tipoJobFME) {

        LavorazioneSuoloModel lavorazione = getUtenteLavorazioneValidoOrNotFound(idLavorazione, utente);

        ResponseEntity<String> responseFmeJob;
        try {
            responseFmeJob = utilsFme.checkJobStatusFme(getIdJobFme(idLavorazione, tipoJobFME));
        } catch (IndexOutOfBoundsException | URISyntaxException e){
            log.error("Impossibile recuperare l'idJob FME per la lavorazione", e);
            throw SuoloException.ExceptionType.GENERIC_EXCEPTION.newSuoloExceptionInstance("Impossibile recuperare l'idJob per la lavorazione");
        }

        if (responseFmeJob.getStatusCodeValue() != 200) {
            log.error("Errore refresh job FME");
            throw SuoloException.ExceptionType.GENERIC_EXCEPTION.newSuoloExceptionInstance("Errore nel recupero dello stato del job FME");
        }

        ResponseBodyFmeDto fmeDto = new ResponseBodyFmeDto();
        
        try{
            fmeDto = objectMapper.readValue(responseFmeJob.getBody(), ResponseBodyFmeDto.class);
        } catch (Exception e){
            log.error("Errore durante il tentativo di refreshStatoLavorazione ", e);
            throw SuoloException.ExceptionType.GENERIC_EXCEPTION.newSuoloExceptionInstance("Errore generico in refreshStatoLavorazione ");
        }
            
        if (fmeDto.getStatus() != null && fmeDto.getStatus().equals(StatoJobFme.JOB_FAILURE.name()) &&
                    lavorazione.getStato().equals(StatoLavorazioneSuolo.CONSOLIDAMENTO_IN_CORSO)){
            lavorazione.setStato(StatoLavorazioneSuolo.ERRORE_CONSOLIDAMENTO);
            getLavorazioneSuoloDao().saveAndFlush(lavorazione);
        }

        if (fmeDto.getStatus() != null && fmeDto.getStatus().equals(StatoJobFme.SUCCESS.name()) &&
                    lavorazione.getStato().equals(StatoLavorazioneSuolo.CONSOLIDAMENTO_IN_CORSO)){
            log.error("Consolidamento ancora in corso ma statoJobFme SUCCESS");
            throw SuoloException.ExceptionType.GENERIC_EXCEPTION.newSuoloExceptionInstance("Anomalia nel consolidamento rilevata. Necessario contattare l'assistenza.");                             
        }

        return lavorazione;
    }

    private Long getIdJobFme (Long idLavorazione, TipoJobFME tipoJobFME) {
        return jobFmeLavorazioneDao.findByIdLavorazioneAndTipoJobFme(idLavorazione, tipoJobFME).get(0).getIdJobFme();
    }

}
