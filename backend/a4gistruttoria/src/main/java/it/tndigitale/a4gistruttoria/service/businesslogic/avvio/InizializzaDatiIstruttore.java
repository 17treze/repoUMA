package it.tndigitale.a4gistruttoria.service.businesslogic.avvio;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;

public interface InizializzaDatiIstruttore {
	
	public static final String PREFISSO_NOME_QUALIFICATORE = "INIZIALIZZA_ISTRUTTORIA_";

	void inizializzaDati(IstruttoriaModel istruttoria);
	
	public static String getNomeQualificatore(Sostegno sostegno) {
		return PREFISSO_NOME_QUALIFICATORE + sostegno.name();
	}

}