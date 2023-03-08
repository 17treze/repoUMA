package it.tndigitale.a4gistruttoria.service.businesslogic.processo.domanda;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4g.ags.client.api.DomandeRestControllerApi;
import it.tndigitale.a4gistruttoria.dto.DomandaUnica;
import it.tndigitale.a4gistruttoria.dto.InfoGeneraliDomanda;
import it.tndigitale.a4gistruttoria.dto.ProcessoAnnoCampagnaDomandaDto;
import it.tndigitale.a4gistruttoria.dto.Richieste;
import it.tndigitale.a4gistruttoria.dto.SintesiRichieste;
import it.tndigitale.a4gistruttoria.dto.domandaunica.ErroreControlloRicevibilitaDomanda;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.ErroreRicevibilitaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
@Sql(scripts = "/DomandaUnica/ricevibilita/verificaInserimentoDomandaAgsGiaPresente_delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ElaboraRicevibilitaDomandaUnicaTest {
    private static final String URL_AGS_DOMANDE_DU = "http://localhost:8080/ags/api/v1/domandeDU/";
    private static final Integer CAMPAGNA = 2018;

    @MockBean
    private ConsumeExternalRestApi consumeExternalRestApi;

    @Autowired
    private ElaboraRicevibilitaDomandaUnica elaboraRicevibilitaDomandaUnica;

    @Autowired
    private DomandaUnicaDao domandaUnicaDao;

    @Autowired
    private ErroreRicevibilitaDao erroreRicevibilitaDao;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    @Transactional
    @WithMockUser(username = "utente", roles = {"a4gistruttoria.pac.istruttoria.du.visualizza.tutti", "a4gistruttoria.pac.istruttoria.du.visualizza.utente", "a4g.ags.domanda.du.visualizza"})
    public void verificaInserimentoDomandaAgsRicevibile() throws Exception {
        String urlExportDomandaProtocollata = URL_AGS_DOMANDE_DU.concat("exportDomandaProtocollata/").concat("190874");
        DomandeRestControllerApi domandeRestControllerApi = Mockito.mock(DomandeRestControllerApi.class);

        doReturn(domandeRestControllerApi).when(consumeExternalRestApi).restClientDomandaUnica(DomandeRestControllerApi.class);
        doReturn(Arrays.asList(190874L)).when(domandeRestControllerApi).getDomandeProtocollateUsingGET(CAMPAGNA);
        doReturn(getDomandaUnicaAgsRicevibile()).when(restTemplate).getForObject(new URI(urlExportDomandaProtocollata), DomandaUnica.class);

        elaboraRicevibilitaDomandaUnica.avvia(processoDomanda());

        DomandaUnicaModel domandaUnicaModel = domandaUnicaDao.findByNumeroDomanda(new BigDecimal("190874"));
        assertThat(domandaUnicaModel).isNotNull();
        assertThat(domandaUnicaModel.getStato()).isEqualTo(StatoDomanda.RICEVIBILE);
        assertThat(erroreRicevibilitaDao.findByDomandaUnicaModel(domandaUnicaModel)).isNullOrEmpty();
    }

    @Test
    @Transactional
    @WithMockUser(username = "utente", roles = {"a4gistruttoria.pac.istruttoria.du.visualizza.tutti", "a4gistruttoria.pac.istruttoria.du.visualizza.utente", "a4g.ags.domanda.du.visualizza"})
    public void verificaInserimentoDomandaAgsNonRicevibile() throws Exception {
        String urlExportDomandaProtocollata = URL_AGS_DOMANDE_DU.concat("exportDomandaProtocollata/").concat("190875");
        DomandeRestControllerApi domandeRestControllerApi = Mockito.mock(DomandeRestControllerApi.class);

        doReturn(domandeRestControllerApi).when(consumeExternalRestApi).restClientDomandaUnica(DomandeRestControllerApi.class);
        doReturn(Arrays.asList(190875L)).when(domandeRestControllerApi).getDomandeProtocollateUsingGET(CAMPAGNA);
        doReturn(getDomandaUnicaAgsNonRicevibile()).when(restTemplate).getForObject(new URI(urlExportDomandaProtocollata), DomandaUnica.class);

        elaboraRicevibilitaDomandaUnica.avvia(processoDomanda());

        DomandaUnicaModel domandaUnicaModel = domandaUnicaDao.findByNumeroDomanda(new BigDecimal(190875));
        assertThat(domandaUnicaModel).isNotNull();
        assertThat(domandaUnicaModel.getStato()).isEqualTo(StatoDomanda.NON_RICEVIBILE);
        assertThat(erroreRicevibilitaDao.findByDomandaUnicaModel(domandaUnicaModel)).isNotEmpty();
    }

    @Test
    @Transactional
    @WithMockUser(username = "utente", roles = {"a4gistruttoria.pac.istruttoria.du.visualizza.tutti", "a4gistruttoria.pac.istruttoria.du.visualizza.utente", "a4g.ags.domanda.du.visualizza"})
    @Sql(scripts = "/DomandaUnica/ricevibilita/verificaInserimentoDomandaAgsGiaPresente_insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void verificaInserimentoDomandaAgsGiaPresente() throws Exception {
        String urlExportDomandaProtocollata = URL_AGS_DOMANDE_DU.concat("exportDomandaProtocollata/").concat("190876");
        DomandeRestControllerApi domandeRestControllerApi = Mockito.mock(DomandeRestControllerApi.class);

        doReturn(domandeRestControllerApi).when(consumeExternalRestApi).restClientDomandaUnica(DomandeRestControllerApi.class);
        doReturn(Arrays.asList(190876L)).when(domandeRestControllerApi).getDomandeProtocollateUsingGET(CAMPAGNA);
        doReturn(getDomandaUnicaGiaPresente()).when(restTemplate).getForObject(new URI(urlExportDomandaProtocollata), DomandaUnica.class);

        elaboraRicevibilitaDomandaUnica.avvia(processoDomanda());

        DomandaUnicaModel domandaUnicaModel = domandaUnicaDao.findByNumeroDomanda(new BigDecimal("190876"));
        assertThat(domandaUnicaModel).isNotNull();
        assertThat(domandaUnicaModel.getRagioneSociale()).isNotEqualToIgnoringCase(getDomandaUnicaGiaPresente().getInfoGeneraliDomanda().getRagioneSociale());
    }

    private ProcessoAnnoCampagnaDomandaDto processoDomanda() {
        ProcessoAnnoCampagnaDomandaDto processo = new ProcessoAnnoCampagnaDomandaDto();
        processo.setIdProcesso(1L);
        processo.setTipoProcesso(TipoProcesso.RICEVIBILITA_AGS);
        processo.setCampagna(CAMPAGNA);
        return processo;
    }

    private DomandaUnica getDomandaUnicaAgsRicevibile() {
        DomandaUnica domandaDU = getDomandaUnicaAgs();
        domandaDU.getInfoGeneraliDomanda().setStato("RICEVIBILE");
        domandaDU.getInfoGeneraliDomanda().setCuaaIntestatario("CMARTI52E68B006T");
        domandaDU.getInfoGeneraliDomanda().setRagioneSociale("CAUMO RITA");
        domandaDU.getInfoGeneraliDomanda().setNumeroDomanda(new BigDecimal("190874"));
        return domandaDU;
    }

    private DomandaUnica getDomandaUnicaAgsNonRicevibile() {
        DomandaUnica domandaDU = getDomandaUnicaAgs();
        domandaDU.getInfoGeneraliDomanda().setStato("NON_RICEVIBILE");
        List<ErroreControlloRicevibilitaDomanda> erroriControlloRicevibilita = new ArrayList<>();
        erroriControlloRicevibilita.add(ErroreControlloRicevibilitaDomanda.ANOMALIE_COMPILAZIONE_AGS);
        domandaDU.setTipologiaControllo(erroriControlloRicevibilita);
        domandaDU.getInfoGeneraliDomanda().setCuaaIntestatario("PMPBRN63C17L378V");
        domandaDU.getInfoGeneraliDomanda().setRagioneSociale("POMPERMAIER BRUNO");
        domandaDU.getInfoGeneraliDomanda().setNumeroDomanda(new BigDecimal("190875"));
        return domandaDU;
    }

    private DomandaUnica getDomandaUnicaGiaPresente() {
        DomandaUnica domandaDU = getDomandaUnicaAgs();
        domandaDU.getInfoGeneraliDomanda().setStato("RICEVIBILE");
        domandaDU.getInfoGeneraliDomanda().setCuaaIntestatario("BRGNGL64C30C7941");
        domandaDU.getInfoGeneraliDomanda().setRagioneSociale("RAGIONE SOCIALE DIVERSA");
        domandaDU.getInfoGeneraliDomanda().setNumeroDomanda(new BigDecimal("190876"));
        domandaDU.getInfoGeneraliDomanda().setCodEnteCompilatore("19");
        domandaDU.getInfoGeneraliDomanda().setEnteCompilatore("CAA CIA - CLES - 002");
        return domandaDU;
    }

    private DomandaUnica getDomandaUnicaAgs() {
        DomandaUnica domandaDU = new DomandaUnica();
        InfoGeneraliDomanda infoGeneraliDomanda = new InfoGeneraliDomanda();
        infoGeneraliDomanda.setPac("PAC1420");
        infoGeneraliDomanda.setTipoDomanda("DU");
        infoGeneraliDomanda.setCampagna(CAMPAGNA);
        infoGeneraliDomanda.setModulo("PAGAMENTI DIRETTI");
        infoGeneraliDomanda.setCodModulo("BPS_2018");
        try {
            infoGeneraliDomanda.setDataPresentazione(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-06-14 18:55:07"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            infoGeneraliDomanda.setDataProtocollazione(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-06-14 18:57:07"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        infoGeneraliDomanda.setCodEnteCompilatore("778");
        infoGeneraliDomanda.setEnteCompilatore("CAA ACLI - TRENTO - 006");
        domandaDU.setInfoGeneraliDomanda(infoGeneraliDomanda);
        Richieste richieste = new Richieste();
        SintesiRichieste sintesiRichieste = new SintesiRichieste();
        sintesiRichieste.setRichiestaDisaccoppiato(true);
        sintesiRichieste.setRichiestaSuperfici(false);
        sintesiRichieste.setRichiestaZootecnia(true);
        richieste.setSintesiRichieste(sintesiRichieste);
        domandaDU.setRichieste(richieste);
        return domandaDU;
    }

}