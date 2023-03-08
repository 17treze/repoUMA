package it.tndigitale.a4gistruttoria.service.businesslogic.avvio;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;

public interface VerificatoreIstruttoria {
	
	public static final String PREFISSO_NOME_QUALIFICATORE = "VERIFICATORE_ISTRUTTORIA_";

	boolean isIstruttoriaAvviabile(Long idDomanda, TipoIstruttoria tipologia, Sostegno sostegno);
	
	public static String getNomeQualificatore(TipoIstruttoria tipologia) {
		return PREFISSO_NOME_QUALIFICATORE + tipologia.name();
	}
}