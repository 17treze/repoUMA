package it.tndigitale.a4gistruttoria.service.loader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.Test;

import it.tndigitale.a4gistruttoria.repository.dao.ConfigurazioneIstruttoriaDisaccoppiatoDao;
import it.tndigitale.a4gistruttoria.repository.model.ConfigurazioneIstruttoriaDisaccoppiatoModel;

public class ConfigurazioneIstruttoriaDisaccoppiatoLoaderTest {

    private ConfigurazioneIstruttoriaDisaccoppiatoLoader loader;
    private ConfigurazioneIstruttoriaDisaccoppiatoDao configurazioneIstruttoriaDisaccoppiatoDao;
    private static final Integer CAMPAGNA = Integer.valueOf("1986");

    public ConfigurazioneIstruttoriaDisaccoppiatoLoaderTest() {
        configurazioneIstruttoriaDisaccoppiatoDao = mock(ConfigurazioneIstruttoriaDisaccoppiatoDao.class);

        loader = new ConfigurazioneIstruttoriaDisaccoppiatoLoader()
                .setComponents(configurazioneIstruttoriaDisaccoppiatoDao);
    }

    @Test
    public void forLoadByIfNotExistByCampagnaThenThrowing() {
        when(configurazioneIstruttoriaDisaccoppiatoDao.findByCampagna(CAMPAGNA)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> loader.loadBy(CAMPAGNA));
        verify(configurazioneIstruttoriaDisaccoppiatoDao).findByCampagna(CAMPAGNA);
    }

    @Test
    public void forLoadByIfExistByCampagnaThenReturnConfiguration() {
        when(configurazioneIstruttoriaDisaccoppiatoDao.findByCampagna(CAMPAGNA)).thenReturn(Optional.of(new ConfigurazioneIstruttoriaDisaccoppiatoModel()));

        ConfigurazioneIstruttoriaDisaccoppiatoModel configuration = loader.loadBy(CAMPAGNA);

        assertThat(configuration).isNotNull();
        verify(configurazioneIstruttoriaDisaccoppiatoDao).findByCampagna(CAMPAGNA);
    }
}
