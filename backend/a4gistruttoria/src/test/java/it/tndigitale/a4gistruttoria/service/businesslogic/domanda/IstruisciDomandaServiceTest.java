package it.tndigitale.a4gistruttoria.service.businesslogic.domanda;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneDomandaException;

@RunWith(SpringRunner.class)
@SpringBootTest
// @AutoConfigureTestDatabase
public class IstruisciDomandaServiceTest {
    private static final int CAMPAGNA = 2019;
    private static final LocalDate TODAY_ANTICIPO = LocalDate.of(CAMPAGNA, 11, 30);
    private static final LocalDate TODAY_SALDO = LocalDate.of(CAMPAGNA, 12, 1);

    @Autowired
    private IstruisciDomandaService istruisciDomandaService;

    @Autowired
    private IstruttoriaDao istruttoriaDao;

    @Autowired
    private DomandaUnicaDao domandaUnicaDao;

    @MockBean
    private Clock clock;


    @Test
    public void getIstruttoriaAnticipo() {
        Mockito.doReturn(TODAY_ANTICIPO).when(clock).today();
        TipoIstruttoria tipoIstruttoria = istruisciDomandaService.getTipoIstruttoria(CAMPAGNA);
        assertThat(tipoIstruttoria).isEqualTo(TipoIstruttoria.ANTICIPO);
    }

    @Test
    public void getIstruttoriaSaldo() {
        Mockito.doReturn(TODAY_SALDO).when(clock).today();
        TipoIstruttoria tipoIstruttoria = istruisciDomandaService.getTipoIstruttoria(CAMPAGNA);
        assertThat(tipoIstruttoria).isEqualTo(TipoIstruttoria.SALDO);
    }

    @Test
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void caricaIdDomandeSenzaIstruttoria() throws ElaborazioneDomandaException {
        Mockito.doReturn(TODAY_ANTICIPO).when(clock).today();
        List<Long> idDaElaborare = istruisciDomandaService.caricaIdDaElaborare(CAMPAGNA);

        assertThat(idDaElaborare).isNotEmpty();
        assertThat(idDaElaborare).contains(9L);
    }

    @Test
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void caricaIdDomandePerAvviareIstruttoriaSaldo() throws ElaborazioneDomandaException {
        Mockito.doReturn(TODAY_SALDO).when(clock).today();
        List<Long> idDaElaborare = istruisciDomandaService.caricaIdDaElaborare(CAMPAGNA);

        assertThat(idDaElaborare).isNotEmpty();
        assertThat(idDaElaborare).contains(7L);
    }

    @Test
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void caricaIdDomande() throws ElaborazioneDomandaException {
        Mockito.doReturn(TODAY_SALDO).when(clock).today();
        List<Long> idDaElaborare = istruisciDomandaService.caricaIdDaElaborare(CAMPAGNA);

        assertThat(idDaElaborare).isNotEmpty();
        assertThat(idDaElaborare).doesNotContain(11L);
    }

    @Test
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "utente", roles = {"a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita"})
    public void avviaIstruttoriaSaldoDisaccoppiato() throws ElaborazioneDomandaException {
        final long ID_DOMANDA = 7L;
        Mockito.doReturn(TODAY_SALDO).when(clock).today();

        istruisciDomandaService.elabora(ID_DOMANDA);

        List<IstruttoriaModel> list = istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(ID_DOMANDA, Sostegno.DISACCOPPIATO);
        assertThat(list).isNotNull();
        Optional<IstruttoriaModel> istruttoria = list.stream().filter(i -> i.getTipologia().equals(TipoIstruttoria.SALDO)).findFirst();
        DomandaUnicaModel domanda = domandaUnicaDao.findByNumeroDomanda(new BigDecimal(ID_DOMANDA));

        assertThat(domanda.getStato()).isEqualTo(StatoDomanda.IN_ISTRUTTORIA);
        assertTrue(istruttoria.isPresent());
        assertThat(istruttoria.get().getTipologia()).isEqualTo(TipoIstruttoria.SALDO);
        assertThat(istruttoria.get().getDatiIstruttoreDisModel()).isNotNull();
        assertThat(istruttoria.get().getDatiIstruttoreDisPascoli()).isNotNull();
    }

    @Test
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "utente", roles = {"a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita"})
    public void avviaIstruttoriaSaldoSuperficie() throws ElaborazioneDomandaException {
        final long ID_DOMANDA = 8L;
        Mockito.doReturn(TODAY_SALDO).when(clock).today();

        istruisciDomandaService.elabora(ID_DOMANDA);

        List<IstruttoriaModel> list = istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(ID_DOMANDA, Sostegno.SUPERFICIE);
        assertThat(list).isNotNull();
        Optional<IstruttoriaModel> istruttoria = list.stream().filter(i -> i.getTipologia().equals(TipoIstruttoria.SALDO)).findFirst();
        DomandaUnicaModel domanda = domandaUnicaDao.findByNumeroDomanda(new BigDecimal(ID_DOMANDA));

        assertThat(domanda.getStato()).isEqualTo(StatoDomanda.IN_ISTRUTTORIA);
        assertTrue(istruttoria.isPresent());
        assertThat(istruttoria.get().getTipologia()).isEqualTo(TipoIstruttoria.SALDO);

    }

    @Test
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "utente", roles = {"a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita"})
    public void avviaIstruttoriaAnticipoError() throws ElaborazioneDomandaException {
        final long ID_DOMANDA = 9L;
        Mockito.doReturn(TODAY_ANTICIPO).when(clock).today();

        istruisciDomandaService.elabora(ID_DOMANDA);

        List<IstruttoriaModel> list = istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(ID_DOMANDA, Sostegno.ZOOTECNIA);
        DomandaUnicaModel domanda = domandaUnicaDao.findByNumeroDomanda(new BigDecimal(ID_DOMANDA));

        assertThat(domanda.getStato()).isEqualTo(StatoDomanda.RICEVIBILE);
        assertThat(list).isNullOrEmpty();
    }

    @Test
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "utente", roles = {"a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita"})
    public void avviaIstruttoriaAnticipoDisaccoppiato() throws ElaborazioneDomandaException {
        final long ID_DOMANDA = 10L;
        Mockito.doReturn(TODAY_ANTICIPO).when(clock).today();

        istruisciDomandaService.elabora(ID_DOMANDA);

        List<IstruttoriaModel> list = istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(ID_DOMANDA, Sostegno.DISACCOPPIATO);
        assertThat(list).isNotNull();
        Optional<IstruttoriaModel> istruttoria = list.stream().filter(i -> i.getTipologia().equals(TipoIstruttoria.ANTICIPO)).findFirst();
        DomandaUnicaModel domanda = domandaUnicaDao.findByNumeroDomanda(new BigDecimal(ID_DOMANDA));

        assertThat(domanda.getStato()).isEqualTo(StatoDomanda.IN_ISTRUTTORIA);
        assertTrue(istruttoria.isPresent());
        assertThat(istruttoria.get().getTipologia()).isEqualTo(TipoIstruttoria.ANTICIPO);
    }

    @Test
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "utente", roles = {"a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita"})
    public void avviaIstruttoriaSaldoDisaccoppiatoSenzaIstruttore() throws ElaborazioneDomandaException {
        final long ID_DOMANDA = 13L;
        Mockito.doReturn(TODAY_SALDO).when(clock).today();

        istruisciDomandaService.elabora(ID_DOMANDA);

        List<IstruttoriaModel> list = istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(ID_DOMANDA, Sostegno.DISACCOPPIATO);
        assertThat(list).isNotNull();
        Optional<IstruttoriaModel> istruttoria = list.stream().filter(i -> i.getTipologia().equals(TipoIstruttoria.SALDO)).findFirst();

        DomandaUnicaModel domanda = domandaUnicaDao.findByNumeroDomanda(new BigDecimal(ID_DOMANDA));

        assertThat(domanda.getStato()).isEqualTo(StatoDomanda.IN_ISTRUTTORIA);
        assertTrue(istruttoria.isPresent());
        assertThat(istruttoria.get().getTipologia()).isEqualTo(TipoIstruttoria.SALDO);
        assertThat(istruttoria.get().getDatiIstruttoreDisModel()).isNull();

    }

    @Test
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "utente", roles = {"a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita"})
    public void avviaIstruttoriaSaldoDisaccoppiatoConIstruttoreAndDOM_ANNO_PREC_NON_LIQ_true() throws ElaborazioneDomandaException {
        final long ID_DOMANDA = 14L;
        Mockito.doReturn(TODAY_SALDO).when(clock).today();

        istruisciDomandaService.elabora(ID_DOMANDA);

        List<IstruttoriaModel> list = istruttoriaDao.findByDomandaUnicaModel_idAndSostegno(ID_DOMANDA, Sostegno.DISACCOPPIATO);
        assertThat(list).isNotNull();
        Optional<IstruttoriaModel> istruttoria = list.stream().filter(i -> i.getTipologia().equals(TipoIstruttoria.SALDO)).findFirst();

        DomandaUnicaModel domanda = domandaUnicaDao.findByNumeroDomanda(new BigDecimal(ID_DOMANDA));

        assertThat(domanda.getStato()).isEqualTo(StatoDomanda.IN_ISTRUTTORIA);
        assertTrue(istruttoria.isPresent());
        assertThat(istruttoria.get().getTipologia()).isEqualTo(TipoIstruttoria.SALDO);
        assertThat(istruttoria.get().getDatiIstruttoreDisModel()).isNotNull();
        assertThat(istruttoria.get().getDatiIstruttoreDisModel().getDomAnnoPrecNonLiq()).isTrue();
        assertThat(istruttoria.get().getDatiIstruttoreDisPascoli()).isNotNull();

    }

}