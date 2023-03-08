package it.tndigitale.a4gutente.dto;

import it.tndigitale.a4gutente.exception.ValidationException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static it.tndigitale.a4gutente.dto.MotivazioneDisattivazione.ALTRO;
import static it.tndigitale.a4gutente.dto.MotivazioneDisattivazione.FINE_RAPPORTO;
import static java.util.Collections.emptyList;

public class IstruttoriaSenzaDomandaTest {

    @Test
    public void forValidaIfProfiliNullThenNotThrowing() {
        new IstruttoriaSenzaDomanda().setProfili(null)
                                     .setProfiliDaDisattivare(Arrays.asList(1L))
                                     .setMotivazioneDisattivazione(ALTRO)
                                     .valida();
    }

    @Test
    public void forValidaIfProfiliEmptyThenNotThrowing() {
        new IstruttoriaSenzaDomanda().setProfili(emptyList())
                                     .setProfiliDaDisattivare(Arrays.asList(1L))
                                     .setMotivazioneDisattivazione(FINE_RAPPORTO)
                                     .valida();
    }

    @Test
    public void forValidaIfProfiliDaDisattivareNullThenNotThrowing() {
        new IstruttoriaSenzaDomanda().setProfili(Arrays.asList(1L))
                                     .setProfiliDaDisattivare(null)
                                     .valida();
    }

    @Test
    public void forValidaIfProfiliDaDisattivareEmptyThenNotThrowing() {
        new IstruttoriaSenzaDomanda().setProfili(Arrays.asList(1L))
                                     .setProfiliDaDisattivare(emptyList())
                                     .valida();
    }

    @Test
    public void forValidaIfProfiliAndProfiliDaDisattivareNotEmptyAndNotIntersectThenNotThrowing() {
        new IstruttoriaSenzaDomanda().setProfili(Arrays.asList(1L,3L))
                                     .setProfiliDaDisattivare(Arrays.asList(2L,5L))
                                     .setMotivazioneDisattivazione(FINE_RAPPORTO)
                                     .valida();
    }

    @Test
    public void forValidaIfProfiliAndProfiliDaDisattivareNotEmptyAndIntersectThenThrowing() {
        Assertions.assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
            new IstruttoriaSenzaDomanda().setProfili(Arrays.asList(1L,3L))
                                         .setProfiliDaDisattivare(Arrays.asList(2L,3L))
                                         .setMotivazioneDisattivazione(FINE_RAPPORTO)
                                         .valida()
        );
    }

    @Test
    public void forValidaIfProfiliDaDisattivareNotEmptyAndMotivoDisattivazioneNullThenThrowing() {
        Assertions.assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                new IstruttoriaSenzaDomanda().setProfili(Arrays.asList(1L,3L))
                        .setProfiliDaDisattivare(Arrays.asList(2L,4L))
                        .setMotivazioneDisattivazione(null)
                        .valida()
        );
    }


    @Test
    public void forValidaIfProfiliDaDisattivareNullAndMotivoDisattivazioneNullThenThrowing() {
        Assertions.assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                new IstruttoriaSenzaDomanda().setProfili(Arrays.asList(1L,3L))
                        .setProfiliDaDisattivare(null)
                        .setMotivazioneDisattivazione(FINE_RAPPORTO)
                        .valida()
        );
    }


    @Test
    public void forValidaIfProfiliDaDisattivareEmptyAndMotivoDisattivazioneNullThenThrowing() {
        Assertions.assertThatExceptionOfType(ValidationException.class).isThrownBy(() ->
                new IstruttoriaSenzaDomanda().setProfili(Arrays.asList(1L,3L))
                        .setProfiliDaDisattivare(Collections.emptyList())
                        .setMotivazioneDisattivazione(FINE_RAPPORTO)
                        .valida()
        );
    }
}
