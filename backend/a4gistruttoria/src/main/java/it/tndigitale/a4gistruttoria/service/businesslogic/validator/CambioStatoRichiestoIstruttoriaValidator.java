package it.tndigitale.a4gistruttoria.service.businesslogic.validator;

import static it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria.PAGAMENTO_AUTORIZZATO;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;

public class CambioStatoRichiestoIstruttoriaValidator implements CambioStatoIstruttoriaValidator {

	@Override
	public void valida(IstruttoriaModel istruttoria) throws ElaborazioneIstruttoriaException {
		StatoIstruttoria statoPresente = istruttoria.getStato();
        if (PAGAMENTO_AUTORIZZATO.equals(statoPresente)) {
            throw new ElaborazioneIstruttoriaException("Cambio stato non consentito." +
                    "Istruttoria con identificativo " + istruttoria.getId() + " si trova in stato PAGAMENTO AUTORIZZATO");
        }
		
	}

}
