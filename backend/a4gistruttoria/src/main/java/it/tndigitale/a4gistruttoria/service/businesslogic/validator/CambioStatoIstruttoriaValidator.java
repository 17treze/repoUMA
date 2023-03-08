package it.tndigitale.a4gistruttoria.service.businesslogic.validator;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;

@FunctionalInterface
public interface CambioStatoIstruttoriaValidator {

    void valida(IstruttoriaModel istruttoria) throws ElaborazioneIstruttoriaException;

}
