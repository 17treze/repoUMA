package it.tndigitale.a4gutente.service.support;

import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;

import javax.validation.ValidationException;


public class WorkflowDomandaRegistrazioneUtente {

    public static void checkCambioStato(DomandaRegistrazioneUtente domandaRegistrazioneUtente,
                                        StatoDomandaRegistrazioneUtente newState) throws ValidationException {
        StatoDomandaRegistrazioneUtente oldState = domandaRegistrazioneUtente.getStato();
        switch (newState) {
            case APPROVATA:
                if (!oldState.equals(StatoDomandaRegistrazioneUtente.IN_LAVORAZIONE)) {
                    throw new ValidationException("La domanda deve essere in lavorazione per poter essere approvata");
                }
                esisteIstruttoria(domandaRegistrazioneUtente);
                domandaRegistrazioneUtente.setStato(newState);
                break;
            case RIFIUTATA:
                if (!oldState.equals(StatoDomandaRegistrazioneUtente.IN_LAVORAZIONE)) {
                    throw new ValidationException("La domanda deve essere in lavorazione per poter essere rifiutata");
                }
                domandaRegistrazioneUtente.setStato(newState);
                break;
            case IN_LAVORAZIONE:
                if (!oldState.equals(StatoDomandaRegistrazioneUtente.PROTOCOLLATA)) {
                    throw new ValidationException("La domanda deve essere protocollata per poter passare in lavorazione");
                }
                break;
            default:
                throw new ValidationException("Valore del nuovo stato non gestito");
        }
    }

    private static void esisteIstruttoria(DomandaRegistrazioneUtente domandaRegistrazioneUtente) {
        if (domandaRegistrazioneUtente.getIstruttoriaEntita()==null) {
            throw new ValidationException("Impossibile cambiare lo stato della domanda: Non esiste una istruttoria associata");
        }
    }

}
