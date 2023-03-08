package it.tndigitale.a4gistruttoria.service.businesslogic.validator;

import static it.tndigitale.a4gistruttoria.service.businesslogic.validator.CambioDiStatoIstruttoriaValidatorFactory.createValidatorFrom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.Test;

import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;

public class CambioDiStatoIstruttoriaValidatorFactoryTest {

    @Test
    public void forCreateValidatorFromIfNON_AMMISSIBILEThenCreateValidator() {
        CambioStatoIstruttoriaValidator validator = createValidatorFrom(StatoIstruttoria.NON_AMMISSIBILE);

        assertThat(validator).isNotNull();

        assertThat(validator).isInstanceOf(CambioStatoNonAmmissibileIstruttoriaValidator.class);
    }

    // MODIFICATO DA RICHIESTO A CONTROLLI_CALCOLO_KO. VERIFICARE CHE QUESTO TEST ABBIA SENSO
    @Test
    public void forCreateValidatorFromIfNoManageStateThenThrowing() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> createValidatorFrom(StatoIstruttoria.CONTROLLI_CALCOLO_KO));
    }
}
