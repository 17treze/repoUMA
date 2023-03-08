package it.tndigitale.a4gistruttoria.service.businesslogic.validator;

import static it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;

public class CambioStatoPagamentoAutorizzatoIstruttoriaValidator implements CambioStatoIstruttoriaValidator {

    @Override
    public void valida(IstruttoriaModel istruttoria) throws ElaborazioneIstruttoriaException {
        StatoIstruttoria statoPresente = istruttoria.getStato();
        if (!CONTROLLI_INTERSOSTEGNO_OK.equals(statoPresente)) {
            throw new ElaborazioneIstruttoriaException("Cambio stato non consentito." +
                    "Istruttoria con identificativo " + istruttoria.getId() + " si trova in stato " + statoPresente);
        }
    }

}
