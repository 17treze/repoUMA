package it.tndigitale.a4gistruttoria.service.businesslogic.validator;

import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

public class CambioDiStatoIstruttoriaValidatorFactory {

    public static CambioStatoIstruttoriaValidator createValidatorFrom(StatoIstruttoria statoFuturo) {
        CambioStatoIstruttoriaValidator validator = null;
        switch (statoFuturo) {
            case NON_AMMISSIBILE:
                validator = new CambioStatoNonAmmissibileIstruttoriaValidator();
                break;
            case CONTROLLI_INTERSOSTEGNO_OK:
            case PAGAMENTO_NON_AUTORIZZATO:
            case NON_LIQUIDABILE:
            	validator = new CambioStatoIntersostegnoIstruttoriaValidator();
            	break;
            case PAGAMENTO_AUTORIZZATO:
            	validator = new CambioStatoPagamentoAutorizzatoIstruttoriaValidator();
            	break;
            case INTEGRATO:
            	validator = ((IstruttoriaModel istruttoria) ->  {
	            	 new CambioStatoIntegratoIstruttoriaValidator();
	            	});
            case RICHIESTO:
            	validator = ((IstruttoriaModel istruttoria) ->  {
	            	new CambioStatoRichiestoIstruttoriaValidator();	
	            	});
            	break;            	
            default:
                throw new RuntimeException("Stato futuro non richiesto");
        }
        return validator;
    }

}
