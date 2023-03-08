package it.tndigitale.a4gutente.dto;

import it.tndigitale.a4gutente.exception.ValidationException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class RichiestaDomandaApprovazioneTest {

    @Test
    public void ifTestoMailNullThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> new RichiestaDomandaApprovazione().setIdDomanda(1L)
                                                                    .isValid());
    }

    @Test
    public void ifTestoMailEmptyThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> new RichiestaDomandaApprovazione().setIdDomanda(1L)
                                                                    .setTestoMail("")
                                                                    .isValid());
    }

    @Test
    public void ifTestoMailNotEmptyBatWithOnlySpaceThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> new RichiestaDomandaApprovazione().setIdDomanda(1L)
                        .setTestoMail("   ")
                        .isValid());
    }

    @Test
    public void ifIdDomandaNullThenThrowing() {
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> new RichiestaDomandaApprovazione().setTestoMail("xxx xxx xxx")
                                                                    .isValid());
    }

    @Test
    public void ifAllParametersMandatoryAreSpecifiedThenNotThrowing() {
        new RichiestaDomandaApprovazione().setIdDomanda(1L)
                                          .setTestoMail("xxx xx xxxx")
                                          .isValid();
    }

}
