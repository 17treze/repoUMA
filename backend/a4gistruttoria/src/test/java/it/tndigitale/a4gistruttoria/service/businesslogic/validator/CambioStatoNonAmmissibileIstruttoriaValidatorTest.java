package it.tndigitale.a4gistruttoria.service.businesslogic.validator;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.Test;

import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;

public class CambioStatoNonAmmissibileIstruttoriaValidatorTest {

    private CambioStatoNonAmmissibileIstruttoriaValidator validator = new CambioStatoNonAmmissibileIstruttoriaValidator();

    @Test
    public void forValidaIfStatoPresentePagamentoAutorizzatoThenThrowing() {
        assertThatExceptionOfType(ElaborazioneIstruttoriaException.class)
                .isThrownBy(() -> validator.valida(new IstruttoriaModel().setA4gdStatoLavSostegno(pagamentoAutorizzato())));
    }

    @Test
    public void forValidaIfStatoPresenteIsNotPagamentoAutorizzatoThenNotThrowing() throws ElaborazioneIstruttoriaException {
        validator.valida(new IstruttoriaModel().setA4gdStatoLavSostegno(statoRichiesto()));
    }

    private A4gdStatoLavSostegno pagamentoAutorizzato() {
        A4gdStatoLavSostegno stato = new A4gdStatoLavSostegno();
        stato.setIdentificativo(StatoIstruttoria.PAGAMENTO_AUTORIZZATO.getStatoIstruttoria());
        return stato;
    }

    private A4gdStatoLavSostegno statoRichiesto() {
        A4gdStatoLavSostegno stato = new A4gdStatoLavSostegno();
        stato.setIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
        return stato;
    }
}
