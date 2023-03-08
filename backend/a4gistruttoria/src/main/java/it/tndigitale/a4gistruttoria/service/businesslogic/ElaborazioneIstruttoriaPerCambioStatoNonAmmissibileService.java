package it.tndigitale.a4gistruttoria.service.businesslogic;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;

import static it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria.NON_AMMISSIBILE;

@Service
public class ElaborazioneIstruttoriaPerCambioStatoNonAmmissibileService extends CambioStatoIstruttoria implements ElaborazioneIstruttoria  {

    @Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = ElaborazioneIstruttoriaException.class)    
    public void elabora(Long idIstruttoria) throws ElaborazioneIstruttoriaException {
        cambiaStato(idIstruttoria, NON_AMMISSIBILE);
    }

}
