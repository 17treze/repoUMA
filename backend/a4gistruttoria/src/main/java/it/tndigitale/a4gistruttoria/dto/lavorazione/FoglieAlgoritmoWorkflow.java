package it.tndigitale.a4gistruttoria.dto.lavorazione;

import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

public interface FoglieAlgoritmoWorkflow {

	String getCodiceEsito();

	boolean isEsitoOK();

	StatoIstruttoria getStatoWF();

}