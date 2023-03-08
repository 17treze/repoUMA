package it.tndigitale.a4gutente.dto;

import it.tndigitale.a4gutente.exception.ValidationException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class RichiestaDomandaRifiutoTest {

    @Test
    public void ifTestoMailNullThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> new RichiestaDomandaRifiuta().setIdDomanda(1L)
                                                               .setMotivazioneRifiuto("xxxxx")
                                                               .isValid());
    }

    @Test
    public void ifTestoMailEmptyThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> new RichiestaDomandaRifiuta().setIdDomanda(1L)
                                                               .setMotivazioneRifiuto("xxxxx")
                                                               .setTestoMail("")
                                                               .isValid());
    }

    @Test
    public void ifIdDomandaNullThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> new RichiestaDomandaRifiuta().setMotivazioneRifiuto("xxxxx")
                                                               .setTestoMail("yyyy yyy yyyy")
                                                               .isValid());
    }

    @Test
    public void ifMotivazioneRifiutoNullThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> new RichiestaDomandaRifiuta().setMotivazioneRifiuto(null)
                                                               .setIdDomanda(1L)
                                                               .setTestoMail("xxxxxxxxxx")
                                                               .isValid());
    }

    @Test
    public void ifMotivazioneRifiutoEmptyThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> new RichiestaDomandaRifiuta().setMotivazioneRifiuto("")
                                                               .setIdDomanda(1L)
                                                               .setTestoMail("xxxxxxxxxx")
                                                               .isValid());
    }

    @Test
    public void ifAllParametersMandatoryAreSpecifiedThenNotThrowing() {
        new RichiestaDomandaRifiuta().setMotivazioneRifiuto("xxxx xx x xxx")
                                     .setIdDomanda(1L)
                                     .setTestoMail("xxxxxxxxxx")
                                     .isValid();
    }

}
