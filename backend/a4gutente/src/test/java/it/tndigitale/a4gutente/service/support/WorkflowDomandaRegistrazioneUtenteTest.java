package it.tndigitale.a4gutente.service.support;

import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;
import org.junit.Test;

import javax.validation.ValidationException;

import static it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente.*;
import static it.tndigitale.a4gutente.service.support.WorkflowDomandaRegistrazioneUtente.checkCambioStato;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class WorkflowDomandaRegistrazioneUtenteTest {

    @Test
    public void ifNewStateApprovataAndOldStateDifferentOfInLavorazioneThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> checkCambioStato(new DomandaRegistrazioneUtente().setStato(CHIUSA), APPROVATA));
    }

    @Test
    public void ifNewStateApprovataAndNotExistIstruttoriaThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> checkCambioStato(new DomandaRegistrazioneUtente().setStato(IN_LAVORAZIONE), APPROVATA));
    }

    @Test
    public void ifNewStateApprovataAndExistIstruttoriaThenNotThrowing() {
        checkCambioStato(new DomandaRegistrazioneUtente().setStato(IN_LAVORAZIONE).setIstruttoriaEntita(new IstruttoriaEntita()),
              APPROVATA);
    }

    @Test
    public void ifNewStateRifiutataAndOldStateDifferentOfInLavorazioneThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> checkCambioStato(new DomandaRegistrazioneUtente().setStato(CHIUSA), RIFIUTATA));
    }

    @Test
    public void ifNewStateRifiutataAndOldStadeEqualInLavorazioneThenNotThrowing() {
        checkCambioStato(new DomandaRegistrazioneUtente().setStato(IN_LAVORAZIONE).setIstruttoriaEntita(new IstruttoriaEntita()),
              RIFIUTATA);
    }

    @Test
    public void ifNewStateInLavorazioneAndOldStateDifferentOfProtocollataThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> checkCambioStato(new DomandaRegistrazioneUtente().setStato(APPROVATA), IN_LAVORAZIONE));
    }

    @Test
    public void ifNewStateInLavorazioneAndOldStateEqualOfProtocollataThenNotThrowing() {
        checkCambioStato(new DomandaRegistrazioneUtente().setStato(PROTOCOLLATA), IN_LAVORAZIONE);
    }

    @Test
    public void ifNewStateNotExpectedThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> checkCambioStato(new DomandaRegistrazioneUtente().setStato(APPROVATA), PROTOCOLLATA));
    }

}
