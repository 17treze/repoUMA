package it.tndigitale.a4gistruttoria.service.businesslogic.avvio;

import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;

@Component(VerificatoreIstruttoriaAnticipoAvviabile.NOME_QUALIFICATORE)
public class VerificatoreIstruttoriaAnticipoAvviabile extends VerificatoreIstruttoriaAvviabile {


	public static final String NOME_QUALIFICATORE = PREFISSO_NOME_QUALIFICATORE + "ANTICIPO";

	@Override
	public boolean isIstruttoriaAvviabile(Long idDomanda, TipoIstruttoria tipologia, Sostegno sostegno) {
		// Solo per il disaccoppiato posso avviare l'istruttoria di anticipo
		return Sostegno.DISACCOPPIATO.equals(sostegno) && super.isIstruttoriaAvviabile(idDomanda, tipologia, sostegno);
	}

}
