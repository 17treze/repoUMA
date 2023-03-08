package it.tndigitale.a4gistruttoria.service.loader;

import static it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria.NON_AMMISSIBILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.Test;

import it.tndigitale.a4gistruttoria.repository.dao.StatoLavorazioneSostegnoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno;

public class StatoLavSostegnoLoaderTest {

    private StatoLavSostegnoLoader loader;
    private StatoLavorazioneSostegnoDao statoLavorazioneSostegnoDao;

    public StatoLavSostegnoLoaderTest() {
        statoLavorazioneSostegnoDao = mock(StatoLavorazioneSostegnoDao.class);

        loader = new StatoLavSostegnoLoader()
            .setComponents(statoLavorazioneSostegnoDao);
    }

    @Test
    public void forLoadByIdentificativoIfNotExistThenThrowing() {
        when(statoLavorazioneSostegnoDao.findByIdentificativo(NON_AMMISSIBILE.getStatoIstruttoria()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> loader.loadByIdentificativo(NON_AMMISSIBILE.getStatoIstruttoria()));
        verify(statoLavorazioneSostegnoDao).findByIdentificativo(NON_AMMISSIBILE.getStatoIstruttoria());
    }

    @Test
    public void forLoadByIdentificativoIfNotExistThenNotThrowingAndReturnEntity() {
        when(statoLavorazioneSostegnoDao.findByIdentificativo(NON_AMMISSIBILE.getStatoIstruttoria()))
                .thenReturn(Optional.of(new A4gdStatoLavSostegno()));

        A4gdStatoLavSostegno statoLavSostegno = loader.loadByIdentificativo(NON_AMMISSIBILE.getStatoIstruttoria());

        assertThat(statoLavSostegno).isNotNull();
        verify(statoLavorazioneSostegnoDao).findByIdentificativo(NON_AMMISSIBILE.getStatoIstruttoria());
    }
}
