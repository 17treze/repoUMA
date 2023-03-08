package it.tndigitale.a4gistruttoria.util;

import static it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria.NON_AMMISSIBILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.Test;

import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

public class StatoIstruttoriaEnumTest {

    @Test
    public void forValueOfIstruttoriaOrThrowIfStatoOfIstruttoriaNullThenThrowing() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> new IstruttoriaModel().getStato());
    }

    @Test
    public void forValueOfIstruttoriaOrThrowIfStatoIsPresentThenReturn() {
        StatoIstruttoria stato = istruttoriaModel().getStato();

        assertThat(stato).isEqualTo(NON_AMMISSIBILE);
    }


    private IstruttoriaModel istruttoriaModel() {
        A4gdStatoLavSostegno stato = new A4gdStatoLavSostegno();
        stato.setIdentificativo(NON_AMMISSIBILE.getStatoIstruttoria());;
        return new IstruttoriaModel().setA4gdStatoLavSostegno(stato);
    }
}
