package it.tndigitale.a4gistruttoria.service.businesslogic;

import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;

public interface ElaborazioneIstruttoria {

	public void elabora(Long idIstruttoria) throws ElaborazioneIstruttoriaException;
}
