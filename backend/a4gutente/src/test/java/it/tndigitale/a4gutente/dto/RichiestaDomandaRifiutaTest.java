package it.tndigitale.a4gutente.dto;

import it.tndigitale.a4gutente.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class RichiestaDomandaRifiutaTest {

    @Test
    public void forIsValidIfTestoMailNullThenThrowing() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                new RichiestaDomandaRifiuta().setTestoMail(null)
                                             .setMotivazioneRifiuto("motivazione rifiuto")
                                             .setIdDomanda(1L)
                                             .isValid());
    }

    @Test
    public void forIsValidIfTestoMailEmptyThenThrowing() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                new RichiestaDomandaRifiuta().setTestoMail("")
                                             .setMotivazioneRifiuto("motivazione rifiuto")
                                             .setIdDomanda(1L)
                                             .isValid());
    }

    @Test
    public void forIsValidIfTestoMailNotEmptyWithOnlySpaceThenThrowing() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                new RichiestaDomandaRifiuta().setTestoMail("  ")
                                             .setMotivazioneRifiuto("motivazione rifiuto")
                                             .setIdDomanda(1L)
                                             .isValid());
    }

    @Test
    public void forIsValidIfIdDomandaNullThenThrowing() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                new RichiestaDomandaRifiuta().setTestoMail("Testo della mail")
                                             .setMotivazioneRifiuto("motivazione rifiuto")
                                             .setIdDomanda(null)
                                             .isValid());
    }


    @Test
    public void forIsValidIfMotivazioneMailNullThenThrowing() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                new RichiestaDomandaRifiuta().setTestoMail("Testo della mail")
                                             .setMotivazioneRifiuto(null)
                                             .setIdDomanda(1L)
                                             .isValid());
    }

    @Test
    public void forIsValidIfMotivazioneEmptyThenThrowing() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                new RichiestaDomandaRifiuta().setTestoMail("Testo della mail")
                                             .setMotivazioneRifiuto("")
                                             .setIdDomanda(1L)
                                             .isValid());
    }

    @Test
    public void forIsValidIfMotivazioneNotEmptyWithOnlySpaceThenThrowing() {
        assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                new RichiestaDomandaRifiuta().setTestoMail("Testo della mail")
                                             .setMotivazioneRifiuto("  ")
                                             .setIdDomanda(1L)
                                             .isValid());
    }

    @Test
    public void forIsValidIfAllParametersOKThenNotThrowing() {
        new RichiestaDomandaRifiuta().setTestoMail("Testo della mail")
                                     .setMotivazioneRifiuto("Motivazione")
                                     .setIdDomanda(1L)
                                     .isValid();
    }

}
