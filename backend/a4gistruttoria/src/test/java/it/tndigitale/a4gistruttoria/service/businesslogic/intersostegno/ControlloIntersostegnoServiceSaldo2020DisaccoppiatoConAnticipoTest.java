package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.MockIoItalia;
import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.dto.DomandaCollegata;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiInput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ControlloIntersostegnoException;
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = {"a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita"})
// @AutoConfigureTestDatabase
public class ControlloIntersostegnoServiceSaldo2020DisaccoppiatoConAnticipoTest extends MockIoItalia {

    @Autowired
    ControlloIntersostegnoService controlloIntersostegnoManager;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private TransizioneIstruttoriaDao daoTransizioneSostegno;

    @Autowired
    private DomandaUnicaDao daoDomanda;

    @Autowired
    IstruttoriaComponent istruttoriaComponent;

    @Autowired
    private IstruttoriaDao istruttoriaDao;

    @PersistenceContext
    private EntityManager entityManager;
  
    
    static Server h2WebServer;

    //enable h2 console
    @BeforeClass
    public static void initTest() throws SQLException {
       h2WebServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
       h2WebServer.start();
    }

    @AfterClass
    public static void stopServer() throws SQLException {
       h2WebServer.stop();
    }



    /***********************************************************************************
     * SOLO DISACCOPPIATO
     ***********************************************************************************/

    // Dati test:
    // - Disaccoppiato:
    // --- Stato: LIQUIDABILE
    // --- Importo: 2566.22 (lordo e netto)
    // - Anticipo disaccoppiato:
    // -- Stato: CALCOLO_OK
    // -- Importo: 1566.22
    // - Acs:
    // --- Stato: NON_RICHIESTO
    // - Acz:
    // --- Stato: NON_RICHIESTO
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 181662; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void soloDisaccoppiatoConEsitoAntimafiaAlloraControlloIntersostegnoOK() throws ControlloIntersostegnoException, Exception {
        IstruttoriaModel istruttoria = eseguiControlli("181662", "");


        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        assertThat(passiTransizione.size()).isEqualTo(3);

        Optional<PassoTransizioneModel> optPassoImportoMinimo = getPassoImportoMinimo(passiTransizione);

        assertThat(optPassoImportoMinimo.isPresent()).isTrue();

        assertThat(optPassoImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_042.getCodiceEsito());

        Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

        assertThat(optPassoLavAntimafia.isPresent()).isFalse();

        //assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_062.getCodiceEsito());

        Optional<PassoTransizioneModel> optPassoDisciplina = getPassoDisciplina(passiTransizione);

        assertThat(optPassoDisciplina.isPresent()).isTrue();

        PassoTransizioneModel passo = optPassoDisciplina.get();
        DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
        DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

        // disciplina già applicata
        Optional<VariabileCalcolo> optDiscGiaApp = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS);
        assertThat(optDiscGiaApp).isPresent();
        VariabileCalcolo var = optDiscGiaApp.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        optDiscGiaApp = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ);
        assertThat(optDiscGiaApp).isPresent();
        var = optDiscGiaApp.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        optDiscGiaApp = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPDIS);
        assertThat(optDiscGiaApp).isPresent();
        var = optDiscGiaApp.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // disciplina da applicare al disaccoppiato
        Optional<VariabileCalcolo> optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDIS);

        assertThat(optDffrpagdis).isPresent();

        var = optDffrpagdis.get();
        assertEquals(BigDecimal.valueOf(2000).setScale(2, RoundingMode.FLOOR), var.getValNumber());


        // check nuove variabili aggiunte
        Optional<VariabileCalcolo> bpsIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.BPSIMPCALCFINLORDO);
        assertThat(bpsIMPCALCFINLORDO).isPresent();
        var = bpsIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(2368.79).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> greIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GREIMPCALCFINLORDO);
        assertThat(greIMPCALCFINLORDO).isPresent();
        var = greIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(197.43).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> gioIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GIOIMPCALCFINLORDO);
        assertThat(gioIMPCALCFINLORDO).isPresent();
        var = gioIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> disIMPCALCLORDO = getVariabileInput(variabiliInput, TipoVariabile.DISIMPCALCLORDO);
        assertThat(disIMPCALCLORDO).isPresent();
        var = disIMPCALCLORDO.get();
        assertEquals(BigDecimal.valueOf(2566.22).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGBPS = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGBPS);
        assertThat(dfIMPLIPAGBPS).isPresent();
        var = dfIMPLIPAGBPS.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGGRE = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGRE);
        assertThat(dfIMPLIPAGGRE).isPresent();
        var = dfIMPLIPAGGRE.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGGIO = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGIO);
        assertThat(dfIMPLIPAGGIO).isPresent();
        var = dfIMPLIPAGGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());


//		output
        Optional<VariabileCalcolo> dfFRPAGLORDIS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDIS);
        assertThat(dfFRPAGLORDIS).isPresent();
        var = dfFRPAGLORDIS.get();
        assertEquals(BigDecimal.valueOf(2000).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISBPS);
        assertThat(dfFRPAGLORDISBPS).isPresent();
        var = dfFRPAGLORDISBPS.get();
        assertEquals(BigDecimal.valueOf(2000).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGRE);
        assertThat(dfFRPAGLORDISGRE).isPresent();
        var = dfFRPAGLORDISGRE.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGIO);
        assertThat(dfFRPAGLORDISGIO).isPresent();
        var = dfFRPAGLORDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIQDISLORDO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPLIQDISLORDO);
        assertThat(dfIMPLIQDISLORDO).isPresent();
        var = dfIMPLIQDISLORDO.get();
        assertEquals(BigDecimal.valueOf(2559.13).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // check variabili modificate
        Optional<VariabileCalcolo> dfIMPDFDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISBPS);
        assertThat(dfIMPDFDISBPS).isPresent();
        var = dfIMPDFDISBPS.get();
        assertEquals(BigDecimal.valueOf(364.17).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPDFDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGRE);
        assertThat(dfIMPDFDISGRE).isPresent();
        var = dfIMPDFDISGRE.get();
        assertEquals(BigDecimal.valueOf(194.96).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPDFDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGIO);
        assertThat(dfIMPDFDISGIO).isPresent();
        var = dfIMPDFDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());


    }

    // Dati test:
    // - Disaccoppiato:
    // --- Stato: LIQUIDABILE
    // --- Importo: 2566.22 (lordo)
    // - Anticipo disaccoppiato:
    // -- Stato: PAGAMENTO_AUTORIZZATO
    // -- Importo: 1566.22
    // - Acs:
    // --- Stato: NON_RICHIESTO
    // - Acz:
    // --- Stato: NON_RICHIESTO
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/istruttoria_Anticipo_Liquidata_d181662.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 181662; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":2368.79}', '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void soloDisaccoppiatoAnticipoPagatoEsitoAntimafiaAlloraControlloIntersostegnoOK() throws ControlloIntersostegnoException, Exception {
        IstruttoriaModel istruttoria = eseguiControlli("181662", "");


        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        assertThat(passiTransizione.size()).isEqualTo(3);

        Optional<PassoTransizioneModel> optPassoImportoMinimo = getPassoImportoMinimo(passiTransizione);

        assertThat(optPassoImportoMinimo.isPresent()).isTrue();

        assertThat(optPassoImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_042.getCodiceEsito());

        Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

        assertThat(optPassoLavAntimafia.isPresent()).isFalse();

        //assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_062.getCodiceEsito());

        Optional<PassoTransizioneModel> optPassoDisciplina = getPassoDisciplina(passiTransizione);

        assertThat(optPassoDisciplina.isPresent()).isTrue();

        PassoTransizioneModel passo = optPassoDisciplina.get();
        DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
        DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

        // disciplina già applicata
        Optional<VariabileCalcolo> optDiscGiaApp = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS);
        assertThat(optDiscGiaApp).isPresent();
        VariabileCalcolo var = optDiscGiaApp.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        optDiscGiaApp = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ);
        assertThat(optDiscGiaApp).isPresent();
        var = optDiscGiaApp.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        optDiscGiaApp = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPDIS);
        assertThat(optDiscGiaApp).isPresent();
        var = optDiscGiaApp.get();
        assertEquals(BigDecimal.valueOf(1566.22).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        
        Optional<VariabileCalcolo> DFFRAPPDISBPS = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPDISBPS);
        assertThat(DFFRAPPDISBPS).isPresent();
        var = DFFRAPPDISBPS.get();
        assertEquals(BigDecimal.valueOf(474.83).setScale(2, RoundingMode.FLOOR), var.getValNumber());
      
        Optional<VariabileCalcolo> DFFRAPPDISGRE = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPDISGRE);
        assertThat(DFFRAPPDISGRE).isPresent();
        var = DFFRAPPDISGRE.get();
        assertEquals(BigDecimal.valueOf(251.18).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPDISGIO = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPDISGIO);
        assertThat(DFFRAPPDISGIO).isPresent();
        var = DFFRAPPDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // disciplina da applicare al disaccoppiato
        Optional<VariabileCalcolo> optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDIS);

        assertThat(optDffrpagdis).isPresent();

        var = optDffrpagdis.get();
        assertEquals(BigDecimal.valueOf(433.78).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDISBPS);

        assertThat(optDffrpagdis).isPresent();

        var = optDffrpagdis.get();
        assertEquals(BigDecimal.valueOf(433.78).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFIMPDFDISBPS);

        assertThat(optDffrpagdis).isPresent();

        var = optDffrpagdis.get();
        assertEquals(BigDecimal.valueOf(1452.42).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // check nuove variabili aggiunte
        Optional<VariabileCalcolo> bpsIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.BPSIMPCALCFINLORDO);
        assertThat(bpsIMPCALCFINLORDO).isPresent();
        var = bpsIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(2368.79).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> greIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GREIMPCALCFINLORDO);
        assertThat(greIMPCALCFINLORDO).isPresent();
        var = greIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(197.43).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> gioIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GIOIMPCALCFINLORDO);
        assertThat(gioIMPCALCFINLORDO).isPresent();
        var = gioIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> disIMPCALCLORDO = getVariabileInput(variabiliInput, TipoVariabile.DISIMPCALCLORDO);
        assertThat(disIMPCALCLORDO).isPresent();
        var = disIMPCALCLORDO.get();
        assertEquals(BigDecimal.valueOf(2566.22).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGBPS = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGBPS);
        assertThat(dfIMPLIPAGBPS).isPresent();
        var = dfIMPLIPAGBPS.get();
        assertEquals(BigDecimal.valueOf(474.83).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGGRE = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGRE);
        assertThat(dfIMPLIPAGGRE).isPresent();
        var = dfIMPLIPAGGRE.get();
        assertEquals(BigDecimal.valueOf(251.18).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGGIO = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGIO);
        assertThat(dfIMPLIPAGGIO).isPresent();
        var = dfIMPLIPAGGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
       
        
      

//		output
        Optional<VariabileCalcolo> dfFRPAGLORDIS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDIS);
        assertThat(dfFRPAGLORDIS).isPresent();
        var = dfFRPAGLORDIS.get();
        assertEquals(BigDecimal.valueOf(2000).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISBPS);
        assertThat(dfFRPAGLORDISBPS).isPresent();
        var = dfFRPAGLORDISBPS.get();
        assertEquals(BigDecimal.valueOf(1748.82).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGRE);
        assertThat(dfFRPAGLORDISGRE).isPresent();
        var = dfFRPAGLORDISGRE.get();
        assertEquals(BigDecimal.valueOf(197.43).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGIO);
        assertThat(dfFRPAGLORDISGIO).isPresent();
        var = dfFRPAGLORDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIQDISLORDO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPLIQDISLORDO);
        assertThat(dfIMPLIQDISLORDO).isPresent();
        var = dfIMPLIQDISLORDO.get();
        assertEquals(BigDecimal.valueOf(2612.21).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // check variabili modificate
        Optional<VariabileCalcolo> dfIMPDFDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISBPS);
        assertThat(dfIMPDFDISBPS).isPresent();
        var = dfIMPDFDISBPS.get();
        assertEquals(BigDecimal.valueOf(1452.42).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPDFDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGRE);
        assertThat(dfIMPDFDISGRE).isPresent();
        var = dfIMPDFDISGRE.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPDFDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGIO);
        assertThat(dfIMPDFDISGIO).isPresent();
        var = dfIMPDFDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        
        
    }

    // Dati test:
    // - Disaccoppiato:
    // --- Stato: LIQUIDABILE
    // --- Importo: 175.06
    // - Anticipo disaccoppiato:
    // -- Stato: PAGAMENTO_AUTORIZZATO
    // -- Importo: 1566.22
    // - Acs:
    // --- Stato: NON_RICHIESTO
    // - Acz:
    // --- Stato: NON_RICHIESTO
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/istruttoria_Anticipo_Liquidata_d181662.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 181662; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void anticipoPagatoMaCalcoloSaldoImportoNonLiquidabile() throws ControlloIntersostegnoException, Exception {
        IstruttoriaModel istruttoria = eseguiControlli("181662", "");
        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.NON_LIQUIDABILE.getStatoIstruttoria());

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        assertThat(passiTransizione.size()).isEqualTo(2);

        Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

        assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();

        assertEquals("DUT_043", optPassoLavImportoMinimo.get().getCodiceEsito());


    }

    // Dati test:
    // - Disaccoppiato:
    // --- Stato: LIQUIDABILE
    // --- Importo: 25001
    // - Anticipo disaccoppiato:
    // -- Stato: PAGAMENTO_AUTORIZZATO
    // -- Importo: 1566.22
    // - Acs:
    // --- Stato: NON_RICHIESTO
    // - Acz:
    // --- Stato: NON_RICHIESTO
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/istruttoria_Anticipo_Liquidata_d181662.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 181662; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":25001}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":25001}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and  i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void anticipoPagatoMaNoDomandeCollegateAntimafiaConImportoOltreSogliaAlloraPagamentoNonAutorizzato() throws ControlloIntersostegnoException, Exception {
        IstruttoriaModel istruttoria = eseguiControlli("181662", "");
        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());

        assertEquals(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria(), transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo());

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        assertThat(passiTransizione.size()).isEqualTo(3);

        Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

        Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

        assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();
        assertThat(optPassoLavAntimafia.isPresent()).isFalse();

        assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_042.getCodiceEsito());
        //assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_061.getCodiceEsito());
    }

    // Dati test:
    // - Disaccoppiato:
    // --- Stato: LIQUIDABILE
    // --- Importo: 25001
    // - Anticipo disaccoppiato:
    // -- Stato: PAGAMENTO_AUTORIZZATO
    // -- Importo: 1566.22
    // - Acs:
    // --- Stato: NON_RICHIESTO
    // - Acz:
    // --- Stato: NON_RICHIESTO
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/istruttoria_Anticipo_Liquidata_d181662.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 181662; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":25001}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":25001}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and  i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void anticipoPagatoMaNoDichiarazioneAntimafiaConImportoOltreSogliaAlloraPagamentoNonAutorizzato() throws ControlloIntersostegnoException, Exception {
        String numeroDomanda = "181662";

        String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B%22azienda%22%3A%7B%22cuaa%22%3A%22BRTSRG86A24L174X%22%7D%7D";
        String resString = "[]";

        Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

        DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
        assertThat(d).isNotNull();

        IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

        controlloIntersostegnoManager.elabora(istruttoria.getId());

        Optional<TransizioneIstruttoriaModel> transizioneSostegno =
                daoTransizioneSostegno.findTransizioneControlloIntersostegno(
                        istruttoria).stream().findFirst();

        assertThat(transizioneSostegno.isPresent()).isTrue();

        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());

        assertEquals(StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO.getStatoIstruttoria(), transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo());

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        assertThat(passiTransizione.size()).isEqualTo(3);

        Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

        Optional<PassoTransizioneModel> optPassoLavAntimafia =
                passiTransizione.stream().filter(
                        p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA)).findFirst();

        assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();
        assertThat(optPassoLavAntimafia.isPresent()).isTrue();

        assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_042.getCodiceEsito());
        assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_061.getCodiceEsito());
    }
    
    
 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    /***********************************************************************************
     * DISACCOPPIATO - ACS
     ***********************************************************************************/

    // Dati test:
    // - Disaccoppiato:
    // --- Stato: LIQUIDABILE
    // --- Importo: 175.06
    // - Anticipo disaccoppiato:
    // -- Stato: PAGAMENTO_AUTORIZZATO
    // -- Importo: 1566.22
    // - Acs:
    // --- Stato: CONTROLLI_CALCOLO_OK
    // --- Importo: 500
    // - Acz:
    // --- Stato: NON_RICHIESTO
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/istruttoria_Anticipo_Liquidata_d189674.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 189674; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void conAnticipoPagatoAcsCalcoloMaImportoSaldoNoSufficienteAlloraPagamentoNonAutorizzato() throws ControlloIntersostegnoException, Exception {
        IstruttoriaModel istruttoria = eseguiControlli("189674", "");


        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO.getStatoIstruttoria());

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        assertThat(passiTransizione.size()).isEqualTo(2);

        Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

        assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();

        assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_047.getCodiceEsito());
    }

    // Dati test:
    // - Disaccoppiato:
    // --- Stato: LIQUIDABILE
    // --- Importo Lordo: 1192.58
    // --- Giovane (netto): -16.71
    // --- Greening (netto): 23.83
    // --- BPS (netto): 45.9
    // --- Importo netto: 53.02
    // - Anticipo disaccoppiato:
    // -- Stato: PAGAMENTO_AUTORIZZATO
    // -- Importo: ‭1139.56‬
    // - Acs:
    // --- Stato: LIQUIDABILE
    // --- Importo: 500
    // - Acz:
    // --- Stato: NON_RICHIESTO
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/istruttoria_Anticipo_Liquidata_d189674.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 189674; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":1908.61}', '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":45.9}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"GREIMPCALCFIN\",\"valNumber\":1017.93}', '{\"tipoVariabile\":\"GREIMPCALCFIN\",\"valNumber\":23.83},{\"tipoVariabile\":\"GIOIMPCALCFIN\",\"valNumber\":-16.71}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":53.02}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":1192.58}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    //@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void conAnticipoPagatoAcsCalcoloMaConUnPremioNegativoMaControlloIntersostegnoOK() throws ControlloIntersostegnoException, Exception {
        IstruttoriaModel istruttoria = eseguiControlli("189674", "");


        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        assertThat(passiTransizione.size()).isEqualTo(3);

        Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplina(passiTransizione);

        assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

        PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
        DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
        DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

        // disciplina da applicare al disaccoppiato
        Optional<VariabileCalcolo> optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDIS);

        assertThat(optDffrpagdis).isPresent();

        VariabileCalcolo var = optDffrpagdis.get();
        assertEquals(BigDecimal.valueOf(69.73).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // controllo le variabili calcolate sui premi
        // BPS
        Optional<VariabileCalcolo> optPremio = getVariabileInput(variabiliInput, TipoVariabile.BPSIMPCALCFIN);

        assertThat(optPremio).isPresent();

        VariabileCalcolo varPremio = optPremio.get();
        assertEquals(BigDecimal.valueOf(45.9).setScale(2, RoundingMode.FLOOR), varPremio.getValNumber());

        // GIO
        optPremio = getVariabileInput(variabiliInput, TipoVariabile.GIOIMPCALCFIN);
        assertThat(optPremio).isPresent();
        varPremio = optPremio.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), varPremio.getValNumber());

        // GRE
        optPremio = getVariabileInput(variabiliInput, TipoVariabile.GREIMPCALCFIN);
        assertThat(optPremio).isPresent();
        varPremio = optPremio.get();
        assertEquals(BigDecimal.valueOf(23.83).setScale(2, RoundingMode.FLOOR), varPremio.getValNumber());

        // DISIMPCALC
        optPremio = getVariabileInput(variabiliInput, TipoVariabile.DISIMPCALC);
        assertThat(optPremio).isPresent();
        varPremio = optPremio.get();
        assertEquals(BigDecimal.valueOf(69.73).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // check nuove variabili aggiunte
        Optional<VariabileCalcolo> bpsIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.BPSIMPCALCFINLORDO);
        assertThat(bpsIMPCALCFINLORDO).isPresent();
        var = bpsIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(1908.61).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> greIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GREIMPCALCFINLORDO);
        assertThat(greIMPCALCFINLORDO).isPresent();
        var = greIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(1017.93).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> gioIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GIOIMPCALCFINLORDO);
        assertThat(gioIMPCALCFINLORDO).isPresent();
        var = gioIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> disIMPCALCLORDO = getVariabileInput(variabiliInput, TipoVariabile.DISIMPCALCLORDO);
        assertThat(disIMPCALCLORDO).isPresent();
        var = disIMPCALCLORDO.get();
        assertEquals(BigDecimal.valueOf(2926.54).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGBPS = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGBPS);
        assertThat(dfIMPLIPAGBPS).isPresent();
        var = dfIMPLIPAGBPS.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGGRE = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGRE);
        assertThat(dfIMPLIPAGGRE).isPresent();
        var = dfIMPLIPAGGRE.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGGIO = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGIO);
        assertThat(dfIMPLIPAGGIO).isPresent();
        var = dfIMPLIPAGGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());


//		output
        Optional<VariabileCalcolo> dfFRPAGLORDIS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDIS);
        assertThat(dfFRPAGLORDIS).isPresent();
        var = dfFRPAGLORDIS.get();
        assertEquals(BigDecimal.valueOf(2000).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISBPS);
        assertThat(dfFRPAGLORDISBPS).isPresent();
        var = dfFRPAGLORDISBPS.get();
        assertEquals(BigDecimal.valueOf(1908.61).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGRE);
        assertThat(dfFRPAGLORDISGRE).isPresent();
        var = dfFRPAGLORDISGRE.get();
        assertEquals(BigDecimal.valueOf(91.39).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGIO);
        assertThat(dfFRPAGLORDISGIO).isPresent();
        var = dfFRPAGLORDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIQDISLORDO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPLIQDISLORDO);
        assertThat(dfIMPLIQDISLORDO).isPresent();
        var = dfIMPLIQDISLORDO.get();
        assertEquals(BigDecimal.valueOf(2914.94).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // check variabili modificate
        Optional<VariabileCalcolo> dfIMPDFDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISBPS);
        assertThat(dfIMPDFDISBPS).isPresent();
        var = dfIMPDFDISBPS.get();
        assertEquals(BigDecimal.valueOf(1862.71).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPDFDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGRE);
        assertThat(dfIMPDFDISGRE).isPresent();
        var = dfIMPDFDISGRE.get();
        assertEquals(BigDecimal.valueOf(982.50).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPDFDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGIO);
        assertThat(dfIMPDFDISGIO).isPresent();
        var = dfIMPDFDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
    }

    // Dati test:
    // - Disaccoppiato:
    // --- Stato: LIQUIDABILE
    // --- Importo: 175.06
    // - Anticipo disaccoppiato:
    // -- Stato: CALCOLO_OK
    // -- Importo: 1566.22
    // - Acs:
    // --- Stato: LIQUIDABILE
    // --- Importo: 50
    // - Acz:
    // --- Stato: NON_RICHIESTO
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 189674; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":50.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":50.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void conAnticipoCalcolatoAcsLiquidabileMaImportoSaldoNoSufficienteAlloraNonLiquidabile() throws ControlloIntersostegnoException, Exception {
        IstruttoriaModel istruttoria = eseguiControlli("189674", "");


        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.NON_LIQUIDABILE.getStatoIstruttoria());

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        assertThat(passiTransizione.size()).isEqualTo(2);

        Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

        assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();

        assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_050.getCodiceEsito());
    }

    // Dati test:
    // - Disaccoppiato:
    // --- Stato: LIQUIDABILE
    // --- Importo: 175.06
    // - Anticipo disaccoppiato:
    // -- Stato: CALCOLO_OK
    // -- Importo: 1566.22
    // - Acs:
    // --- Stato: NON_AMMISSIBILE
    // --- Importo: 500
    // - Acz:
    // --- Stato: NON_RICHIESTO
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 189674; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'NON_AMMISSIBILE') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void conAnticipoCalcolatoAcsNonAmmissibileMaImportoSaldoNoSufficienteAlloraLiquidabile() throws ControlloIntersostegnoException, Exception {
        IstruttoriaModel istruttoria = eseguiControlli("189674", "");


        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.NON_LIQUIDABILE.getStatoIstruttoria());

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        assertThat(passiTransizione.size()).isEqualTo(2);

        Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

        assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();

        assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_050.getCodiceEsito());
    }

    // Dati test:
    // - Disaccoppiato:
    // --- Stato: LIQUIDABILE
    // --- Importo: 175.06
    // - Anticipo disaccoppiato:
    // -- Stato: CALCOLO_OK
    // -- Importo: 1566.22
    // - Acs:
    // --- Stato: NON_LIQUIDABILE
    // --- Importo: 500
    // - Acz:
    // --- Stato: NON_RICHIESTO
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 189674; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'NON_LIQUIDABILE') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void conAnticipoCalcolatoAcsNonLiquidabileMaImportoSaldoNoSufficienteAlloraLiquidabile() throws ControlloIntersostegnoException, Exception {
        IstruttoriaModel istruttoria = eseguiControlli("189674", "");


        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.NON_LIQUIDABILE.getStatoIstruttoria());

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        assertThat(passiTransizione.size()).isEqualTo(2);

        Optional<PassoTransizioneModel> optPassoLavImportoMinimo = getPassoImportoMinimo(passiTransizione);

        assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();

        assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_050.getCodiceEsito());

    }

    // Dati test:
    // - Disaccoppiato:
    // --- Stato: LIQUIDABILE
    // --- Importo: 175.06
    // - Anticipo disaccoppiato:
    // -- Stato: CALCOLO_OK
    // -- Importo: 1566.22
    // - Acs:
    // --- Stato: LIQUIDABILE
    // --- Importo: 500
    // - Acz:
    // --- Stato: NON_RICHIESTO
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 189674; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":1908.61}', '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":150.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"GREIMPCALCFIN\",\"valNumber\":1017.93}', '{\"tipoVariabile\":\"GREIMPCALCFIN\",\"valNumber\":25.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":175.06}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":500.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void conAnticipoCalcolatoAcsLiquidabileMaImportoRaggiuntoAlloraControlliIntersosegnoOK() throws ControlloIntersostegnoException, Exception {
        IstruttoriaModel istruttoria = eseguiControlli("189674", "");
        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());

        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        assertEquals(3, passiTransizione.size());

        Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream().filter(
                p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO)).findFirst();

        Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

        assertThat(optPassoLavImportoMinimo.isPresent()).isTrue();
        assertThat(optPassoLavAntimafia.isPresent()).isFalse();

        assertThat(optPassoLavImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_049.getCodiceEsito());
        //assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_059.getCodiceEsito());

        Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplina(passiTransizione);

        assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

        PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
        DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
        DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

        // disciplina già applicata a ACS
        Optional<VariabileCalcolo> optDffrappacs = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS);
        // disciplina da applicare al disaccoppiato
        Optional<VariabileCalcolo> optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDIS);

        assertThat(optDffrappacs).isPresent();
        assertThat(optDffrpagdis).isPresent();

        VariabileCalcolo var = optDffrappacs.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        var = optDffrpagdis.get();
        assertEquals(BigDecimal.valueOf(175.06).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // check nuove variabili aggiunte
        Optional<VariabileCalcolo> bpsIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.BPSIMPCALCFINLORDO);
        assertThat(bpsIMPCALCFINLORDO).isPresent();
        var = bpsIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(1908.61).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> greIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GREIMPCALCFINLORDO);
        assertThat(greIMPCALCFINLORDO).isPresent();
        var = greIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(1017.93).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> gioIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GIOIMPCALCFINLORDO);
        assertThat(gioIMPCALCFINLORDO).isPresent();
        var = gioIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> disIMPCALCLORDO = getVariabileInput(variabiliInput, TipoVariabile.DISIMPCALCLORDO);
        assertThat(disIMPCALCLORDO).isPresent();
        var = disIMPCALCLORDO.get();
        assertEquals(BigDecimal.valueOf(2926.54).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGBPS = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGBPS);
        assertThat(dfIMPLIPAGBPS).isPresent();
        var = dfIMPLIPAGBPS.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGGRE = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGRE);
        assertThat(dfIMPLIPAGGRE).isPresent();
        var = dfIMPLIPAGGRE.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGGIO = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGIO);
        assertThat(dfIMPLIPAGGIO).isPresent();
        var = dfIMPLIPAGGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());


//		output
        Optional<VariabileCalcolo> dfFRPAGLORDIS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDIS);
        assertThat(dfFRPAGLORDIS).isPresent();
        var = dfFRPAGLORDIS.get();
        assertEquals(BigDecimal.valueOf(2000).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISBPS);
        assertThat(dfFRPAGLORDISBPS).isPresent();
        var = dfFRPAGLORDISBPS.get();
        assertEquals(BigDecimal.valueOf(1908.61).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGRE);
        assertThat(dfFRPAGLORDISGRE).isPresent();
        var = dfFRPAGLORDISGRE.get();
        assertEquals(BigDecimal.valueOf(91.39).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGIO);
        assertThat(dfFRPAGLORDISGIO).isPresent();
        var = dfFRPAGLORDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIQDISLORDO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPLIQDISLORDO);
        assertThat(dfIMPLIQDISLORDO).isPresent();
        var = dfIMPLIQDISLORDO.get();
        assertEquals(BigDecimal.valueOf(2914.94).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // check variabili modificate
        Optional<VariabileCalcolo> dfIMPDFDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISBPS);
        assertThat(dfIMPDFDISBPS).isPresent();
        var = dfIMPDFDISBPS.get();
        assertEquals(BigDecimal.valueOf(1758.61).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPDFDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGRE);
        assertThat(dfIMPDFDISGRE).isPresent();
        var = dfIMPDFDISGRE.get();
        assertEquals(BigDecimal.valueOf(981.27).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPDFDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGIO);
        assertThat(dfIMPDFDISGIO).isPresent();
        var = dfIMPDFDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

    }

    // Dati test:
    // - Disaccoppiato:
    // --- Stato: LIQUIDABILE
    // --- Importo: 1000
    // - Anticipo disaccoppiato:
    // -- Stato: CALCOLO_OK
    // -- Importo: 1566.22
    // - Acs:
    // --- Stato: PAGAMENTO_AUTORIZZATO
    // --- Importo: 1200
    // --- Disciplina già applicata: 1200
    // - Acz:
    // --- Stato: NON_RICHIESTO
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 189674; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/domanda_Disciplina_Finanziaria_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1746.01}', '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE' AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')) and CODICE_PASSO = 'DISCIPLINA_FINANZIARIA'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void conAnticipoCalcolatoACSErogatoControlloIntersostegnoOKDisciplinaResidua() throws Exception {
        IstruttoriaModel istruttoria = eseguiControlli("189674", "");
        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplina(passiTransizione);

        assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

        PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
        DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
        DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

        // disciplina già applicata a ACS
        Optional<VariabileCalcolo> optDffrappacs = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS);
        // disciplina da applicare al disaccoppiato
        Optional<VariabileCalcolo> optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDIS);

        assertThat(optDffrappacs).isPresent();
        assertThat(optDffrpagdis).isPresent();

        VariabileCalcolo var = optDffrappacs.get();
        assertEquals(BigDecimal.valueOf(1200).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        var = optDffrpagdis.get();
        assertEquals(BigDecimal.valueOf(800).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        // check nuove variabili aggiunte
        Optional<VariabileCalcolo> bpsIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.BPSIMPCALCFINLORDO);
        assertThat(bpsIMPCALCFINLORDO).isPresent();
        var = bpsIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(1908.61).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> greIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GREIMPCALCFINLORDO);
        assertThat(greIMPCALCFINLORDO).isPresent();
        var = greIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(1017.93).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> gioIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GIOIMPCALCFINLORDO);
        assertThat(gioIMPCALCFINLORDO).isPresent();
        var = gioIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> disIMPCALCLORDO = getVariabileInput(variabiliInput, TipoVariabile.DISIMPCALCLORDO);
        assertThat(disIMPCALCLORDO).isPresent();
        var = disIMPCALCLORDO.get();
        assertEquals(BigDecimal.valueOf(2926.54).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGBPS = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGBPS);
        assertThat(dfIMPLIPAGBPS).isPresent();
        var = dfIMPLIPAGBPS.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGGRE = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGRE);
        assertThat(dfIMPLIPAGGRE).isPresent();
        var = dfIMPLIPAGGRE.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGGIO = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGIO);
        assertThat(dfIMPLIPAGGIO).isPresent();
        var = dfIMPLIPAGGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());


//		output
        Optional<VariabileCalcolo> dfFRPAGLORDIS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDIS);
        assertThat(dfFRPAGLORDIS).isPresent();
        var = dfFRPAGLORDIS.get();
        assertEquals(BigDecimal.valueOf(800).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISBPS);
        assertThat(dfFRPAGLORDISBPS).isPresent();
        var = dfFRPAGLORDISBPS.get();
        assertEquals(BigDecimal.valueOf(800).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGRE);
        assertThat(dfFRPAGLORDISGRE).isPresent();
        var = dfFRPAGLORDISGRE.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGIO);
        assertThat(dfFRPAGLORDISGIO).isPresent();
        var = dfFRPAGLORDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIQDISLORDO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPLIQDISLORDO);
        assertThat(dfIMPLIQDISLORDO).isPresent();
        var = dfIMPLIQDISLORDO.get();
        assertEquals(BigDecimal.valueOf(2899.91).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // check variabili modificate
        Optional<VariabileCalcolo> dfIMPDFDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISBPS);
        assertThat(dfIMPDFDISBPS).isPresent();
        var = dfIMPDFDISBPS.get();
        assertEquals(BigDecimal.valueOf(1094.73).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPDFDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGRE);
        assertThat(dfIMPDFDISGRE).isPresent();
        var = dfIMPDFDISGRE.get();
        assertEquals(BigDecimal.valueOf(1005.18).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPDFDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGIO);
        assertThat(dfIMPDFDISGIO).isPresent();
        var = dfIMPDFDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

    }

    // Dati test:
    // - Disaccoppiato:
    // --- Stato: LIQUIDABILE
    // --- Importo: 1000
    // - Anticipo disaccoppiato:
    // -- Stato: CALCOLO_OK
    // -- Importo: 1566.22
    // - Acs:
    // --- Stato: CONTROLLI_INTERSOSTEGNO_OK
    // --- Importo: 1200
    // --- Disciplina calcolata applicata: 1200
    // - Acz:
    // --- Stato: NON_RICHIESTO
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 189674; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":1908.61}', '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":900.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"GREIMPCALCFIN\",\"valNumber\":1017.93}', '{\"tipoVariabile\":\"GREIMPCALCFIN\",\"valNumber\":100.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/domanda_Disciplina_Finanziaria_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1746.01}', '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE' AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')) and CODICE_PASSO = 'DISCIPLINA_FINANZIARIA'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_INTERSOSTEGNO_OK') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void conAnticipoCalcolatoACSControlliIntersostegnoOKAlloraControlloIntersostegnoOKDisciplinaTutta() throws Exception {
        IstruttoriaModel istruttoria = eseguiControlli("189674", "");
        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplina(passiTransizione);

        assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

        PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
        DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
        DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

        // disciplina già applicata a ACS
        Optional<VariabileCalcolo> optDffrappacs = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS);
        // disciplina da applicare al disaccoppiato
        Optional<VariabileCalcolo> optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDIS);

        assertThat(optDffrappacs).isPresent();
        assertThat(optDffrpagdis).isPresent();

        VariabileCalcolo var = optDffrappacs.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        var = optDffrpagdis.get();
        assertEquals(BigDecimal.valueOf(1000).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // check nuove variabili aggiunte
        Optional<VariabileCalcolo> bpsIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.BPSIMPCALCFINLORDO);
        assertThat(bpsIMPCALCFINLORDO).isPresent();
        var = bpsIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(1908.61).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> greIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GREIMPCALCFINLORDO);
        assertThat(greIMPCALCFINLORDO).isPresent();
        var = greIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(1017.93).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> gioIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GIOIMPCALCFINLORDO);
        assertThat(gioIMPCALCFINLORDO).isPresent();
        var = gioIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> disIMPCALCLORDO = getVariabileInput(variabiliInput, TipoVariabile.DISIMPCALCLORDO);
        assertThat(disIMPCALCLORDO).isPresent();
        var = disIMPCALCLORDO.get();
        assertEquals(BigDecimal.valueOf(2926.54).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGBPS = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGBPS);
        assertThat(dfIMPLIPAGBPS).isPresent();
        var = dfIMPLIPAGBPS.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGGRE = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGRE);
        assertThat(dfIMPLIPAGGRE).isPresent();
        var = dfIMPLIPAGGRE.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGGIO = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGIO);
        assertThat(dfIMPLIPAGGIO).isPresent();
        var = dfIMPLIPAGGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());


//		output
        Optional<VariabileCalcolo> dfFRPAGLORDIS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDIS);
        assertThat(dfFRPAGLORDIS).isPresent();
        var = dfFRPAGLORDIS.get();
        assertEquals(BigDecimal.valueOf(2000).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISBPS);
        assertThat(dfFRPAGLORDISBPS).isPresent();
        var = dfFRPAGLORDISBPS.get();
        assertEquals(BigDecimal.valueOf(1908.61).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGRE);
        assertThat(dfFRPAGLORDISGRE).isPresent();
        var = dfFRPAGLORDISGRE.get();
        assertEquals(BigDecimal.valueOf(91.39).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGIO);
        assertThat(dfFRPAGLORDISGIO).isPresent();
        var = dfFRPAGLORDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIQDISLORDO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPLIQDISLORDO);
        assertThat(dfIMPLIQDISLORDO).isPresent();
        var = dfIMPLIQDISLORDO.get();
        assertEquals(BigDecimal.valueOf(2914.94).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // check variabili modificate
        Optional<VariabileCalcolo> dfIMPDFDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISBPS);
        assertThat(dfIMPDFDISBPS).isPresent();
        var = dfIMPDFDISBPS.get();
        assertEquals(BigDecimal.valueOf(1008.61).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPDFDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGRE);
        assertThat(dfIMPDFDISGRE).isPresent();
        var = dfIMPDFDISGRE.get();
        assertEquals(BigDecimal.valueOf(906.33).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPDFDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGIO);
        assertThat(dfIMPDFDISGIO).isPresent();
        var = dfIMPDFDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

    }
    
 // Dati test:
    // - Disaccoppiato:
    // --- Stato: LIQUIDABILE
    // --- Importo: 1000
    // - Anticipo disaccoppiato:
    // -- Stato: CALCOLO_OK
    // -- Importo: 1566.22
    // - Acs:
    // --- Stato: PAGAMENTO_AUTORIZZATO
    // --- Importo: 1200
    // --- Disciplina già applicata: 1200
    // - Acz:
    // --- Stato: NON_RICHIESTO
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_Acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 189674; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":2926.54}', '{\"tipoVariabile\":\"IMPCALCFINLORDO\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":285.09}', '{\"tipoVariabile\":\"ACSIMPCALCLORDOTOT\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE') and CODICE_PASSO = 'CALCOLO_ACS'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_liquidabile_disaccoppiato_acs.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/istruttoria_saldo_acs_pagamento_autorizzato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1746.01}', '{\"tipoVariabile\":\"DFFRPAGACS\",\"valNumber\":1200.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'SGNNLN54T28A372J' and i.SOSTEGNO = 'SUPERFICIE' AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')) and CODICE_PASSO = 'DISCIPLINA_FINANZIARIA'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_Acs_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void conSuperficiePagamentoAutorizzatoControlloIntersostegnoOKDisciplina() throws Exception {
        IstruttoriaModel istruttoria = eseguiControlliSuperficie("189674", "");
        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplina(passiTransizione);

        assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

        PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
        DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
        DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

        // disciplina già applicata a ACS
        Optional<VariabileCalcolo> optDffrappacs = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS);
        
        assertThat(optDffrappacs).isPresent();
        
        VariabileCalcolo var = optDffrappacs.get();
        assertEquals(BigDecimal.valueOf(618.43).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        
        //input
        Optional<VariabileCalcolo> DFFRAPPACS_M8 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS_M8);
        assertThat(DFFRAPPACS_M8).isPresent();
        var = DFFRAPPACS_M8.get();
        assertEquals(BigDecimal.valueOf(25.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPACS_M9 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS_M9);
        assertThat(DFFRAPPACS_M9).isPresent();
        var = DFFRAPPACS_M9.get();
        assertEquals(BigDecimal.valueOf(35.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPACS_M10 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS_M10);
        assertThat(DFFRAPPACS_M10).isPresent();
        var = DFFRAPPACS_M10.get();
        assertEquals(BigDecimal.valueOf(15.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPACS_M11 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS_M11);
        assertThat(DFFRAPPACS_M11).isPresent();
        var = DFFRAPPACS_M11.get();
        assertEquals(BigDecimal.valueOf(20.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPACS_M14 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS_M14);
        assertThat(DFFRAPPACS_M14).isPresent();
        var = DFFRAPPACS_M14.get();
        assertEquals(BigDecimal.valueOf(25.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPACS_M16 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS_M16);
        assertThat(DFFRAPPACS_M16).isPresent();
        var = DFFRAPPACS_M16.get();
        assertEquals(BigDecimal.valueOf(35.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPACS_M17 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS_M17);
        assertThat(DFFRAPPACS_M17).isPresent();
        var = DFFRAPPACS_M17.get();
        assertEquals(BigDecimal.valueOf(658.43).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        
       
        //output 
        Optional<VariabileCalcolo> DFFRPAGACS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGACS);
        assertThat(DFFRPAGACS).isPresent();
        var = DFFRPAGACS.get();
        assertEquals(BigDecimal.valueOf(359.22).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGLORACS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORACS);
        assertThat(DFFRPAGLORACS).isPresent();
        var = DFFRPAGLORACS.get();
        assertEquals(BigDecimal.valueOf(359.22).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGACS_M8 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGACS_M8);
        assertThat(DFFRPAGACS_M8).isPresent();
        var = DFFRPAGACS_M8.get();
        assertEquals(BigDecimal.valueOf(359.22).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGACS_M9 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGACS_M9);
        assertThat(DFFRPAGACS_M9).isPresent();
        var = DFFRPAGACS_M9.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGACS_M10 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGACS_M10);
        assertThat(DFFRPAGACS_M10).isPresent();
        var = DFFRPAGACS_M10.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGACS_M11 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGACS_M11);
        assertThat(DFFRPAGACS_M11).isPresent();
        var = DFFRPAGACS_M11.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGACS_M14 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGACS_M14);
        assertThat(DFFRPAGACS_M14).isPresent();
        var = DFFRPAGACS_M14.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGACS_M15 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGACS_M15);
        assertThat(DFFRPAGACS_M15).isPresent();
        var = DFFRPAGACS_M15.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGACS_M16 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGACS_M16);
        assertThat(DFFRPAGACS_M16).isPresent();
        var = DFFRPAGACS_M16.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGACS_M17 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGACS_M17);
        assertThat(DFFRPAGACS_M17).isPresent();
        var = DFFRPAGACS_M17.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        //dipendono da DFFRAPPACS e ACSIMPCALCLORDO-M8-DFFRPAGLORACS-M8
       
        
        
        //DFIMPDFDFACS-M8
        
        Optional<VariabileCalcolo> DFIMPDFDISACS_M8 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACS_M8);
        assertThat(DFIMPDFDISACS_M8).isPresent();
        var = DFIMPDFDISACS_M8.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPDFDISACS_M9 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACS_M9);
        assertThat(DFIMPDFDISACS_M9).isPresent();
        var = DFIMPDFDISACS_M9.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPDFDISACS_M10 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACS_M10);
        assertThat(DFIMPDFDISACS_M10).isPresent();
        var = DFIMPDFDISACS_M10.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPDFDISACS_M11 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACS_M11);
        assertThat(DFIMPDFDISACS_M11).isPresent();
        var = DFIMPDFDISACS_M11.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPDFDISACS_M14 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACS_M14);
        assertThat(DFIMPDFDISACS_M14).isPresent();
        var = DFIMPDFDISACS_M14.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPDFDISACS_M15 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACS_M15);
        assertThat(DFIMPDFDISACS_M15).isPresent();
        var = DFIMPDFDISACS_M15.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPDFDISACS_M16 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACS_M16);
        assertThat(DFIMPDFDISACS_M16).isPresent();
        var = DFIMPDFDISACS_M16.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPDFDISACS_M17 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACS_M17);
        assertThat(DFIMPDFDISACS_M17).isPresent();
        var = DFIMPDFDISACS_M17.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        //last
        Optional<VariabileCalcolo> DFIMPRIDACS = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPRIDACS);
        assertThat(DFIMPRIDACS).isPresent();
        var = DFIMPRIDACS.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPLIQACS = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPLIQACS);
        assertThat(DFIMPLIQACS).isPresent();
        var = DFIMPLIQACS.get();
        assertEquals(BigDecimal.valueOf(359.22).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPLIQACSLORDO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPLIQACSLORDO);
        assertThat(DFIMPLIQACSLORDO).isPresent();
        var = DFIMPLIQACSLORDO.get();
        assertEquals(BigDecimal.valueOf(1022.65).setScale(2, RoundingMode.FLOOR), var.getValNumber());

    }
    
    
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/225145_domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/225145_domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void testCalcoloDisciplinaFinanziariaDisaccoppiato() throws ControlloIntersostegnoException, Exception {
        String numeroDomanda = "225145";
        
        DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
        assertThat(d).isNotNull();

        IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(
                d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

        controlloIntersostegnoManager.elabora(istruttoria.getId());

        istruttoria = istruttoriaDao.getOne(istruttoria.getId());
        entityManager.refresh(istruttoria);

        
        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplina(passiTransizione);

        assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

        PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
        DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
        DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);



        VariabileCalcolo var = null;
        
       
        Optional<VariabileCalcolo> DFFR = getVariabileInput(variabiliInput, TipoVariabile.DFFR);
        assertThat(DFFR).isPresent();
        assertEquals(BigDecimal.valueOf(2000).setScale(2, RoundingMode.FLOOR), DFFR.get().getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPACZ = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ);
        assertThat(DFFRAPPACZ).isPresent();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), DFFRAPPACZ.get().getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPACS = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS);
        assertThat(DFFRAPPACS).isPresent();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), DFFRAPPACS.get().getValNumber());
        
        Optional<VariabileCalcolo> DISIMPCALCLORDO = getVariabileInput(variabiliInput, TipoVariabile.DISIMPCALCLORDO);
        assertThat(DISIMPCALCLORDO).isPresent();
        assertEquals(BigDecimal.valueOf(3403.48).setScale(2, RoundingMode.FLOOR), DISIMPCALCLORDO.get().getValNumber());
        
        Optional<VariabileCalcolo> BPSIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.BPSIMPCALCFINLORDO);
        assertThat(BPSIMPCALCFINLORDO).isPresent();
        assertEquals(BigDecimal.valueOf(1575.03).setScale(2, RoundingMode.FLOOR), BPSIMPCALCFINLORDO.get().getValNumber());
        
        Optional<VariabileCalcolo> GREIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GREIMPCALCFINLORDO);
        assertThat(GREIMPCALCFINLORDO).isPresent();
        assertEquals(BigDecimal.valueOf(962.34).setScale(2, RoundingMode.FLOOR), GREIMPCALCFINLORDO.get().getValNumber());
        
        
        Optional<VariabileCalcolo> GIOIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GIOIMPCALCFINLORDO);
        assertThat(GIOIMPCALCFINLORDO).isPresent();
        assertEquals(BigDecimal.valueOf(866.11).setScale(2, RoundingMode.FLOOR), GIOIMPCALCFINLORDO.get().getValNumber());
        
        Optional<VariabileCalcolo> DISIMPCALC = getVariabileInput(variabiliInput, TipoVariabile.DISIMPCALC);
        assertThat(DISIMPCALC).isPresent();
        assertEquals(BigDecimal.valueOf(1647.04).setScale(2, RoundingMode.FLOOR), DISIMPCALC.get().getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPDIS = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPDIS);
        assertThat(DFFRAPPDIS).isPresent();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), DFFRAPPDIS.get().getValNumber());
        
        
        Optional<VariabileCalcolo> BPSIMPCALCFIN = getVariabileInput(variabiliInput, TipoVariabile.BPSIMPCALCFIN);
        assertThat(BPSIMPCALCFIN).isPresent();
        assertEquals(BigDecimal.valueOf(417.57).setScale(2, RoundingMode.FLOOR), BPSIMPCALCFIN.get().getValNumber());
        
        Optional<VariabileCalcolo> DFPERC = getVariabileInput(variabiliInput, TipoVariabile.DFPERC);
        assertThat(DFPERC).isPresent();
        assertEquals(BigDecimal.valueOf(0.02906192)/*.setScale(2, RoundingMode.FLOOR)*/, DFPERC.get().getValNumber());
        
        Optional<VariabileCalcolo> GREIMPCALCFIN = getVariabileInput(variabiliInput, TipoVariabile.GREIMPCALCFIN);
        assertThat(GREIMPCALCFIN).isPresent();
        assertEquals(BigDecimal.valueOf(363.36).setScale(2, RoundingMode.FLOOR), GREIMPCALCFIN.get().getValNumber());
        
        Optional<VariabileCalcolo> GIOIMPCALCFIN = getVariabileInput(variabiliInput, TipoVariabile.GIOIMPCALCFIN);
        assertThat(GIOIMPCALCFIN).isPresent();
        assertEquals(BigDecimal.valueOf(866.11).setScale(2, RoundingMode.FLOOR), GIOIMPCALCFIN.get().getValNumber());
        
        
        Optional<VariabileCalcolo> DFIMPLIPAGBPS = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGBPS);
        assertThat(DFIMPLIPAGBPS).isPresent();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), DFIMPLIPAGBPS.get().getValNumber());
        
        Optional<VariabileCalcolo> DFIMPLIPAGGRE = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGRE);
        assertThat(DFIMPLIPAGGRE).isPresent();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), DFIMPLIPAGGRE.get().getValNumber());
        
        
        Optional<VariabileCalcolo> DFIMPLIPAGGIO = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGIO);
        assertThat(DFIMPLIPAGGIO).isPresent();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), DFIMPLIPAGGIO.get().getValNumber());
        
        
        
        //NEW: DFFRPAGLORDIS = MIN((DFFR - (DFFRAPPACZ+DFFRAPPACS));DISIMPCALCLORDO)
        Optional<VariabileCalcolo> DFFRPAGLORDIS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDIS);
        assertThat(DFFRPAGLORDIS).isPresent();
        var = DFFRPAGLORDIS.get();
        assertEquals(BigDecimal.valueOf(2000).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // NEW: DFFRPAGLORDISBPS = min(DFFRPAGLORDIS;BPSIMPCALCFINLORDO)
        Optional<VariabileCalcolo> DFFRPAGLORDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISBPS);
        assertThat(DFFRPAGLORDISBPS).isPresent();
        var = DFFRPAGLORDISBPS.get();
        assertEquals(BigDecimal.valueOf(1575.03).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
       // NEW: DFFRPAGLORDISGRE = min(DFFRPAGLORDIS-DFFRPAGLORDISBPS;GREIMPCALCFINLORDO)  (2000-1575.03,962.34)
        Optional<VariabileCalcolo> DFFRPAGLORDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGRE);
        assertThat(DFFRPAGLORDISGRE).isPresent();
        var = DFFRPAGLORDISGRE.get();
        assertEquals(BigDecimal.valueOf(424.97).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
     // NEW: DFFRPAGLORDISGIO = min(DFFRPAGLORDIS-DFFRPAGLORDISBPS-DFFRPAGLORDISGRE;GIOIMPCALCFINLORDO)= min(2000-1575.03-424.97),866.11)
        Optional<VariabileCalcolo> DFFRPAGLORDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGIO);
        assertThat(DFFRPAGLORDISGIO).isPresent();
        var = DFFRPAGLORDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        
        //franchigia
        //DFFRPAGDIS MIN((DFFR - (DFFRAPPDIS+DFFRAPPACZ+DFFRAPPACS));DISIMPCALC) = min((2000 - (0+0+0)),1647.04) = 1647.04
        Optional<VariabileCalcolo> DFFRPAGDIS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGDIS);
        assertThat(DFFRPAGDIS).isPresent();
        var = DFFRPAGDIS.get();
        assertEquals(BigDecimal.valueOf(1647.04).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
       // DFFRPAGDISBPS = min(DFFRPAGDIS;BPSIMPCALCFIN)  (1647.04,417.57)
        Optional<VariabileCalcolo> DFFRPAGDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGDISBPS);
        assertThat(DFFRPAGDISBPS).isPresent();
        var = DFFRPAGDISBPS.get();
        assertEquals(BigDecimal.valueOf(417.57).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        
       // DFFRPAGDISGRE = min(DFFRPAGDIS-DFFRPAGDISBPS;GREIMPCALCFIN) = min(1647.04-417.57,363.36)
        Optional<VariabileCalcolo> DFFRPAGDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGDISGRE);
        assertThat(DFFRPAGDISGRE).isPresent();
        var = DFFRPAGDISGRE.get();
        assertEquals(BigDecimal.valueOf(363.36).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
     // DFFRPAGDISGIO = min(DFFRPAGDIS-DFFRPAGDISBPS-DFFRPAGDISGRE;GIOIMPCALCFIN) => min((1647.04-417.57-363.36),866.11)   dovrebbe essere 840,94
        Optional<VariabileCalcolo> DFFRPAGDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGDISGIO);
        assertThat(DFFRPAGDISGIO).isPresent();
        var = DFFRPAGDISGIO.get();
        assertEquals(BigDecimal.valueOf(866.11).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        
     // EDIT: DFIMPDFDISBPS = max((DFFRPAGLORDISBPS+(BPSIMPCALCFINLORDO-DFFRPAGLORDISBPS)*(100-DFPERC)-DFIMPLIPAGBPS-DFFRPAGDISBPS);0) = max(1575.03 + (1575.03-1575.03)*(0.97093808) - 0- 417.57) = 
        Optional<VariabileCalcolo> DFIMPDFDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISBPS);
        assertThat(DFIMPDFDISBPS).isPresent();
        var = DFIMPDFDISBPS.get();
        assertEquals(BigDecimal.valueOf(1157.46).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
     // EDIT: DFIMPDFDISGRE = max((DFFRPAGLORDISGRE+(GREIMPCALCFINLORDO-DFFRPAGLORDISGRE)*(100-DFPERC)-DFIMPLIPAGGRE-DFFRPAGDISGRE);0) = max(424.97+ (962.34-424.97)**(0.97093808) - 0 - 363.36) = 424.97 + 521,75 - 363.36 = 583,36
        Optional<VariabileCalcolo> DFIMPDFDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGRE);
        assertThat(DFIMPDFDISGRE).isPresent();
        var = DFIMPDFDISGRE.get();
        assertEquals(BigDecimal.valueOf(583.36).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        // EDIT: DFIMPDFDISGIO = max((DFFRPAGLORDISGIO+(GIOIMPCALCFINLORDO-DFFRPAGLORDISGIO)*(100-DFPERC)-DFIMPLIPAGGIO-DFFRPAGDISGIO;0) = 0 + (866.11-0)*(0.97093808) - 0 - 866.11 = -25,17 -> 0
        Optional<VariabileCalcolo> DFIMPDFDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGIO);
        assertThat(DFIMPDFDISGIO).isPresent();
        var = DFIMPDFDISGIO.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        
     // DFIMPLIQDIS = DFFRPAGDISBPS+DFFRPAGDISGRE+DFFRPAGDISGIO+DFIMPDFDISBPS+DFIMPDFDISGRE+DFIMPDFDISGIO = 417.57 + 363.36 + 866.11 + 1157.46 + 583,36 + 0 = 3.387,86
        Optional<VariabileCalcolo> DFIMPLIQDIS = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPLIQDIS);
        assertThat(DFIMPLIQDIS).isPresent();
        var = DFIMPLIQDIS.get();
        assertEquals(BigDecimal.valueOf(3387.86).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
     // DFIMPRIDDIS = DISIMPCALC-DFIMPLIQDIS  = 1647.04 - 3387.86
        Optional<VariabileCalcolo> DFIMPRIDDIS = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPRIDDIS);
        assertThat(DFIMPRIDDIS).isPresent();
        var = DFIMPRIDDIS.get();
        assertEquals(BigDecimal.valueOf(-1740.82).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        
        // NEW: DFIMPLIQDISLORDO = DFIMPLIQDIS+(DFIMPLIPAGBPS+DFIMPLIPAGGRE+DFIMPLIPAGGIO) = 3387.86+0+0+0
        Optional<VariabileCalcolo> DFIMPLIQDISLORDO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPLIQDISLORDO);
        assertThat(DFIMPLIQDISLORDO).isPresent();
        var = DFIMPLIQDISLORDO.get();
        assertEquals(BigDecimal.valueOf(3387.86).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
    }
    
    
   
     /*
     * Modificare le variabili in input legate al passo della disciplina finanziaria per l'istruttoria di ANTICIPO
     * questi dati saranno utilizzati per il calcolo della disciplina finanaziari alegata all'istruttoria a SALDO della stessa domanda per il calcolo cumulativo della franchigia
     * per il calcolo viene sempre utilizzato il dato dell'ultimo passo di disciplina finanziaria legato alla domanda prendendo in considerazioen sia dati di output che dati di input
     * 
     * aggiunta update per i dati di input della disciplina finanaziaria creata per l'istruttoria di ANTICIPO.
     */
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/istruttoria_Anticipo_Liquidata_d181662.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 181662; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":2566.22}', '{\"tipoVariabile\":\"IMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "update A4GT_PASSO_TRANSIZIONE set DATI_OUTPUT = replace(DATI_OUTPUT, '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":2368.79}', '{\"tipoVariabile\":\"BPSIMPCALCFIN\",\"valNumber\":1000.00}') where ID_TRANSIZ_SOSTEGNO = (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts join a4gt_istruttoria i on i.id = ts.id_istruttoria join A4GT_DOMANDA d on d.id = i.id_domanda where d.cuaa_intestatario = 'BRTSRG86A24L174X' and i.SOSTEGNO = 'DISACCOPPIATO' and i.tipologia = 'SALDO') and CODICE_PASSO = 'CONTROLLI_FINALI'; COMMIT;")
    @Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/istruttoria_anticipo_d181663_add_inpu_disciplina_finanziaria.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)   
    @Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void test_adeguamenti_calcolo_disciplina_finanziaria() throws ControlloIntersostegnoException, Exception {
        IstruttoriaModel istruttoria = eseguiControlli("181662", "");
        
        
        


        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno2().getIdentificativo()).isEqualTo(StatoIstruttoria.LIQUIDABILE.getStatoIstruttoria());
        assertThat(transizioneSostegno.get().getA4gdStatoLavSostegno1().getIdentificativo()).isEqualTo(StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK.getStatoIstruttoria());

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        assertThat(passiTransizione.size()).isEqualTo(3);

        Optional<PassoTransizioneModel> optPassoImportoMinimo = getPassoImportoMinimo(passiTransizione);

        assertThat(optPassoImportoMinimo.isPresent()).isTrue();

        assertThat(optPassoImportoMinimo.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_042.getCodiceEsito());

        Optional<PassoTransizioneModel> optPassoLavAntimafia = getPassoImportoAntimafia(passiTransizione);

        assertThat(optPassoLavAntimafia.isPresent()).isFalse();

        //assertThat(optPassoLavAntimafia.get().getCodiceEsito()).isEqualTo(CodiceEsito.DUT_062.getCodiceEsito());

        Optional<PassoTransizioneModel> optPassoDisciplina = getPassoDisciplina(passiTransizione);

        assertThat(optPassoDisciplina.isPresent()).isTrue();

        PassoTransizioneModel passo = optPassoDisciplina.get();
        DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
        DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

        // disciplina già applicata
        Optional<VariabileCalcolo> optDiscGiaApp = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACS);
        assertThat(optDiscGiaApp).isPresent();
        VariabileCalcolo var = optDiscGiaApp.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        optDiscGiaApp = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ);
        assertThat(optDiscGiaApp).isPresent();
        var = optDiscGiaApp.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        optDiscGiaApp = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPDIS);
        assertThat(optDiscGiaApp).isPresent();
        var = optDiscGiaApp.get();
        assertEquals(BigDecimal.valueOf(1566.22).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        
        //variabili input per accumulo franchigia
        Optional<VariabileCalcolo> DFFRAPPDISBPS = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPDISBPS);
        assertThat(DFFRAPPDISBPS).isPresent();
        var = DFFRAPPDISBPS.get();
        assertEquals(BigDecimal.valueOf(524.83).setScale(2, RoundingMode.FLOOR), var.getValNumber());
      
        Optional<VariabileCalcolo> DFFRAPPDISGRE = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPDISGRE);
        assertThat(DFFRAPPDISGRE).isPresent();
        var = DFFRAPPDISGRE.get();
        assertEquals(BigDecimal.valueOf(271.18).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPDISGIO = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPDISGIO);
        assertThat(DFFRAPPDISGIO).isPresent();
        var = DFFRAPPDISGIO.get();
        assertEquals(BigDecimal.valueOf(1).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        //controlla che l'output non sia valorizzato
        Optional<VariabileCalcolo> _DFFRAPPDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRAPPDISBPS);
        assertThat(_DFFRAPPDISBPS).isNotPresent();
        Optional<VariabileCalcolo> _DFFRAPPDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRAPPDISGRE);
        assertThat(_DFFRAPPDISGRE).isNotPresent();
        Optional<VariabileCalcolo> _DFFRAPPDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRAPPDISGIO);
        assertThat(_DFFRAPPDISGIO).isNotPresent();
        
   
        // disciplina da applicare al disaccoppiato
        Optional<VariabileCalcolo> optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDIS);

        assertThat(optDffrpagdis).isPresent();

        var = optDffrpagdis.get();
        assertEquals(BigDecimal.valueOf(433.78).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFFRPAGDISBPS);

        assertThat(optDffrpagdis).isPresent();

        var = optDffrpagdis.get();
        assertEquals(BigDecimal.valueOf(433.78).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        optDffrpagdis = getVariabileOutputPasso(variabiliOutput, TipoVariabile.DFIMPDFDISBPS);

        assertThat(optDffrpagdis).isPresent();

        var = optDffrpagdis.get();
        assertEquals(BigDecimal.valueOf(1452.15).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // check nuove variabili aggiunte
        Optional<VariabileCalcolo> bpsIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.BPSIMPCALCFINLORDO);
        assertThat(bpsIMPCALCFINLORDO).isPresent();
        var = bpsIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(2368.79).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> greIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GREIMPCALCFINLORDO);
        assertThat(greIMPCALCFINLORDO).isPresent();
        var = greIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(197.43).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> gioIMPCALCFINLORDO = getVariabileInput(variabiliInput, TipoVariabile.GIOIMPCALCFINLORDO);
        assertThat(gioIMPCALCFINLORDO).isPresent();
        var = gioIMPCALCFINLORDO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> disIMPCALCLORDO = getVariabileInput(variabiliInput, TipoVariabile.DISIMPCALCLORDO);
        assertThat(disIMPCALCLORDO).isPresent();
        var = disIMPCALCLORDO.get();
        assertEquals(BigDecimal.valueOf(2566.22).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGBPS = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGBPS);
        assertThat(dfIMPLIPAGBPS).isPresent();
        var = dfIMPLIPAGBPS.get();
        assertEquals(BigDecimal.valueOf(474.83).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGGRE = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGRE);
        assertThat(dfIMPLIPAGGRE).isPresent();
        var = dfIMPLIPAGGRE.get();
        assertEquals(BigDecimal.valueOf(251.18).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIPAGGIO = getVariabileInput(variabiliInput, TipoVariabile.DFIMPLIPAGGIO);
        assertThat(dfIMPLIPAGGIO).isPresent();
        var = dfIMPLIPAGGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
       
//		output
        Optional<VariabileCalcolo> dfFRPAGLORDIS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDIS);
        assertThat(dfFRPAGLORDIS).isPresent();
        var = dfFRPAGLORDIS.get();
        assertEquals(BigDecimal.valueOf(2000).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISBPS);
        assertThat(dfFRPAGLORDISBPS).isPresent();
        var = dfFRPAGLORDISBPS.get();
        assertEquals(BigDecimal.valueOf(1727.82).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGRE);
        assertThat(dfFRPAGLORDISGRE).isPresent();
        var = dfFRPAGLORDISGRE.get();
        assertEquals(BigDecimal.valueOf(197.43).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfFRPAGLORDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORDISGIO);
        assertThat(dfFRPAGLORDISGIO).isPresent();
        var = dfFRPAGLORDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPLIQDISLORDO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPLIQDISLORDO);
        assertThat(dfIMPLIQDISLORDO).isPresent();
        var = dfIMPLIQDISLORDO.get();
        assertEquals(BigDecimal.valueOf(2611.94).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        // check variabili modificate
        Optional<VariabileCalcolo> dfIMPDFDISBPS = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISBPS);
        assertThat(dfIMPDFDISBPS).isPresent();
        var = dfIMPDFDISBPS.get();
        assertEquals(BigDecimal.valueOf(1452.15).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPDFDISGRE = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGRE);
        assertThat(dfIMPDFDISGRE).isPresent();
        var = dfIMPDFDISGRE.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        Optional<VariabileCalcolo> dfIMPDFDISGIO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISGIO);
        assertThat(dfIMPDFDISGIO).isPresent();
        var = dfIMPDFDISGIO.get();
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPLIQDIS = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPLIQDIS);
        assertThat(DFIMPLIQDIS).isPresent();
        var = DFIMPLIQDIS.get();
        assertEquals(BigDecimal.valueOf(1885.93).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPRIDDIS = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPRIDDIS);
        assertThat(DFIMPRIDDIS).isPresent();
        var = DFIMPRIDDIS.get();
        assertEquals(BigDecimal.valueOf(-688.50).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
    }
    
    @Test
    @Transactional
    @Sql(scripts = "/DomandaUnica/istruttoria_anticipo_acz_controlli_ok.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/DomandaUnica/istruttoria_anticipo_acz_controlli_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void conZootecniaPagamentoAutorizzatoControlloIntersostegnoOKDisciplina() throws Exception {
        IstruttoriaModel istruttoria = eseguiControlliZootecnia("1191062", "");
        Optional<TransizioneIstruttoriaModel> transizioneSostegno = caricaTransizioneIntersostegno(istruttoria);

        assertThat(transizioneSostegno.isPresent()).isTrue();

        Set<PassoTransizioneModel> passiTransizione = transizioneSostegno.get().getPassiTransizione();
        Optional<PassoTransizioneModel> optPassoDisciplinaFinanziaria = getPassoDisciplina(passiTransizione);

        assertThat(optPassoDisciplinaFinanziaria.isPresent()).isTrue();

        PassoTransizioneModel passo = optPassoDisciplinaFinanziaria.get();
        DatiInput variabiliInput = objectMapper.readValue(passo.getDatiInput(), DatiInput.class);
        DatiOutput variabiliOutput = objectMapper.readValue(passo.getDatiOutput(), DatiOutput.class);

        // disciplina già applicata a ACZ
        Optional<VariabileCalcolo> optDffrappacz = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ);
        
        assertThat(optDffrappacz).isPresent();
        
        VariabileCalcolo var = optDffrappacz.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());

        
        //input
        Optional<VariabileCalcolo> DFFRAPPACZ_310 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ_310);
        assertThat(DFFRAPPACZ_310).isPresent();
        var = DFFRAPPACZ_310.get();
        assertEquals(BigDecimal.valueOf(60.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPACZ_311 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ_311);
        assertThat(DFFRAPPACZ_311).isPresent();
        var = DFFRAPPACZ_311.get();
        assertEquals(BigDecimal.valueOf(65.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPACZ_313 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ_313);
        assertThat(DFFRAPPACZ_313).isPresent();
        var = DFFRAPPACZ_313.get();
        assertEquals(BigDecimal.valueOf(115.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
       
        Optional<VariabileCalcolo> DFFRAPPACZ_322 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ_322);
        assertThat(DFFRAPPACZ_322).isPresent();
        var = DFFRAPPACZ_322.get();
        assertEquals(BigDecimal.valueOf(45.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPACZ_315 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ_315);
        assertThat(DFFRAPPACZ_315).isPresent();
        var = DFFRAPPACZ_315.get();
        assertEquals(BigDecimal.valueOf(40.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPACZ_316 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ_316);
        assertThat(DFFRAPPACZ_316).isPresent();
        var = DFFRAPPACZ_316.get();
        assertEquals(BigDecimal.valueOf(55.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPACZ_318 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ_318);
        assertThat(DFFRAPPACZ_318).isPresent();
        var = DFFRAPPACZ_318.get();
        assertEquals(BigDecimal.valueOf(30.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPACZ_320 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ_320);
        assertThat(DFFRAPPACZ_320).isPresent();
        var = DFFRAPPACZ_320.get();
        assertEquals(BigDecimal.valueOf(30.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRAPPACZ_321 = getVariabileInput(variabiliInput, TipoVariabile.DFFRAPPACZ_321);
        assertThat(DFFRAPPACZ_321).isPresent();
        var = DFFRAPPACZ_321.get();
        assertEquals(BigDecimal.valueOf(65.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
       
        
        
       
        //output 
        Optional<VariabileCalcolo> DFFRPAGACZ = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGACZ);
        assertThat(DFFRPAGACZ).isPresent();
        var = DFFRPAGACZ.get();
        assertEquals(BigDecimal.valueOf(2000.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGLORACZ = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORACZ);
        assertThat(DFFRPAGLORACZ).isPresent();
        var = DFFRPAGLORACZ.get();
        assertEquals(BigDecimal.valueOf(2000.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGLORACZ_310 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORACZ_310);
        assertThat(DFFRPAGLORACZ_310).isPresent();
        var = DFFRPAGLORACZ_310.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGLORACZ_311 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORACZ_311);
        assertThat(DFFRPAGLORACZ_311).isPresent();
        var = DFFRPAGLORACZ_311.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGLORACZ_313 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORACZ_313);
        assertThat(DFFRPAGLORACZ_313).isPresent();
        var = DFFRPAGLORACZ_313.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGLORACZ_322 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORACZ_322);
        assertThat(DFFRPAGLORACZ_322).isPresent();
        var = DFFRPAGLORACZ_322.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGLORACZ_315 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORACZ_315);
        assertThat(DFFRPAGLORACZ_315).isPresent();
        var = DFFRPAGLORACZ_315.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGLORACZ_316 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORACZ_316);
        assertThat(DFFRPAGLORACZ_316).isPresent();
        var = DFFRPAGLORACZ_316.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGLORACZ_318 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORACZ_318);
        assertThat(DFFRPAGLORACZ_318).isPresent();
        var = DFFRPAGLORACZ_318.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGLORACZ_320 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORACZ_320);
        assertThat(DFFRPAGLORACZ_320).isPresent();
        var = DFFRPAGLORACZ_320.get();
        assertEquals(BigDecimal.valueOf(1935.00).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFFRPAGLORACZ_321 = getVariabileOutput(variabiliOutput, TipoVariabile.DFFRPAGLORACZ_321);
        assertThat(DFFRPAGLORACZ_321).isPresent();
        var = DFFRPAGLORACZ_321.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        //dipendono da DFFRAPPACZ e ACSIMPCALCLORDO-310-DFFRPAGLORACZ-321                
        Optional<VariabileCalcolo> DFIMPDFDISACZ_310 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACZ_310);
        assertThat(DFIMPDFDISACZ_310).isPresent();
        var = DFIMPDFDISACZ_310.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPDFDISACZ_311 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACZ_311);
        assertThat(DFIMPDFDISACZ_311).isPresent();
        var = DFIMPDFDISACZ_311.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPDFDISACZ_313 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACZ_313);
        assertThat(DFIMPDFDISACZ_313).isPresent();
        var = DFIMPDFDISACZ_313.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPDFDISACZ_322 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACZ_322);
        assertThat(DFIMPDFDISACZ_322).isPresent();
        var = DFIMPDFDISACZ_322.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPDFDISACZ_315 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACZ_315);
        assertThat(DFIMPDFDISACZ_315).isPresent();
        var = DFIMPDFDISACZ_315.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPDFDISACZ_316 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACZ_316);
        assertThat(DFIMPDFDISACZ_316).isPresent();
        var = DFIMPDFDISACZ_316.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPDFDISACZ_318 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACZ_318);
        assertThat(DFIMPDFDISACZ_318).isPresent();
        var = DFIMPDFDISACZ_318.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPDFDISACZ_320 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACZ_320);
        assertThat(DFIMPDFDISACZ_320).isPresent();
        var = DFIMPDFDISACZ_320.get();
        assertEquals(BigDecimal.valueOf(921.12).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPDFDISACZ_321 = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPDFDISACZ_321);
        assertThat(DFIMPDFDISACZ_321).isPresent();
        var = DFIMPDFDISACZ_321.get();
        assertEquals(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        
       
        //last
        Optional<VariabileCalcolo> DFIMPRIDACZ = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPRIDACZ);
        assertThat(DFIMPRIDACZ).isPresent();
        var = DFIMPRIDACZ.get();
        assertEquals(BigDecimal.valueOf(22.63).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPLIQACZ = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPLIQACZ);
        assertThat(DFIMPLIQACZ).isPresent();
        var = DFIMPLIQACZ.get();
        assertEquals(BigDecimal.valueOf(2921.12).setScale(2, RoundingMode.FLOOR), var.getValNumber());
        
        Optional<VariabileCalcolo> DFIMPLIQACZLORDO = getVariabileOutput(variabiliOutput, TipoVariabile.DFIMPLIQACZLORDO);
        assertThat(DFIMPLIQACZLORDO).isPresent();
        var = DFIMPLIQACZLORDO.get();
        assertEquals(BigDecimal.valueOf(6113.93).setScale(2, RoundingMode.FLOOR), var.getValNumber());

    }
    
    
    
    


    private IstruttoriaModel eseguiControlli(String numeroDomanda, String response1) throws Exception {

        if (numeroDomanda.equals("181662")) {

            String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B+%22azienda%22%3A%7B%22cuaa%22%3A%22BRTSRG86A24L174X%22%7D%2C+%22stato%22+%3A+%7B%22identificativo%22%3A+%22CONTROLLATA%22%7D%7D";
            JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/antimafia.json"));
            String resString = objectMapper.writeValueAsString(response);
            Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

            String serviceUrl1 = "http://localhost:8080/a4gistruttoria/api/v1/antimafia/domandecollegate?params=%7B+%22cuaa%22%3A%22BRTSRG86A24L174X%22%2C+%22tipoDomanda%22%3A+%22DOMANDA_UNICA%22%2C+%22idDomanda%22%3A%22181662%22+%7D";
            String resString1 = response1;
            if (response1 == null || response1.isEmpty()) {
                resString1 = objectMapper.writeValueAsString(new ArrayList<DomandaCollegata>());
            }
            Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl1)), Mockito.eq(String.class))).thenReturn(resString1);
        } else {

            String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B+%22azienda%22%3A%7B%22cuaa%22%3A%22SGNNLN54T28A372J%22%7D%2C+%22stato%22+%3A+%7B%22identificativo%22%3A+%22CONTROLLATA%22%7D%7D";
            JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/antimafia.json"));
            String resString = objectMapper.writeValueAsString(response);
            Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

            String serviceUrl1 = "http://localhost:8080/a4gistruttoria/api/v1/antimafia/domandecollegate?params=%7B+%22cuaa%22%3A%22SGNNLN54T28A372JX%22%2C+%22tipoDomanda%22%3A+%22DOMANDA_UNICA%22%2C+%22idDomanda%22%3A%22189674%22+%7D";
            String resString1 = response1;
            if (response1 == null || response1.isEmpty()) {
                resString1 = objectMapper.writeValueAsString(new ArrayList<DomandaCollegata>());
            }
            Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl1)), Mockito.eq(String.class))).thenReturn(resString1);
        }

        DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
        assertThat(d).isNotNull();

        IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(
                d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

        controlloIntersostegnoManager.elabora(istruttoria.getId());

        istruttoria = istruttoriaDao.getOne(istruttoria.getId());
        entityManager.refresh(istruttoria);

        // return d;
        return istruttoria;
    }
    
    private IstruttoriaModel eseguiControlliSuperficie(String numeroDomanda, String response1) throws Exception {

        if (numeroDomanda.equals("181662")) {

            String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B+%22azienda%22%3A%7B%22cuaa%22%3A%22BRTSRG86A24L174X%22%7D%2C+%22stato%22+%3A+%7B%22identificativo%22%3A+%22CONTROLLATA%22%7D%7D";
            JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/antimafia.json"));
            String resString = objectMapper.writeValueAsString(response);
            Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

            String serviceUrl1 = "http://localhost:8080/a4gistruttoria/api/v1/antimafia/domandecollegate?params=%7B+%22cuaa%22%3A%22BRTSRG86A24L174X%22%2C+%22tipoDomanda%22%3A+%22DOMANDA_UNICA%22%2C+%22idDomanda%22%3A%22181662%22+%7D";
            String resString1 = response1;
            if (response1 == null || response1.isEmpty()) {
                resString1 = objectMapper.writeValueAsString(new ArrayList<DomandaCollegata>());
            }
            Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl1)), Mockito.eq(String.class))).thenReturn(resString1);
        } else {

            String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B+%22azienda%22%3A%7B%22cuaa%22%3A%22SGNNLN54T28A372J%22%7D%2C+%22stato%22+%3A+%7B%22identificativo%22%3A+%22CONTROLLATA%22%7D%7D";
            JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/antimafia.json"));
            String resString = objectMapper.writeValueAsString(response);
            Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

            String serviceUrl1 = "http://localhost:8080/a4gistruttoria/api/v1/antimafia/domandecollegate?params=%7B+%22cuaa%22%3A%22SGNNLN54T28A372JX%22%2C+%22tipoDomanda%22%3A+%22DOMANDA_UNICA%22%2C+%22idDomanda%22%3A%22189674%22+%7D";
            String resString1 = response1;
            if (response1 == null || response1.isEmpty()) {
                resString1 = objectMapper.writeValueAsString(new ArrayList<DomandaCollegata>());
            }
            Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl1)), Mockito.eq(String.class))).thenReturn(resString1);
        }

        DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
        assertThat(d).isNotNull();

        IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.SUPERFICIE, TipoIstruttoria.SALDO);

        controlloIntersostegnoManager.elabora(istruttoria.getId());

        istruttoria = istruttoriaDao.getOne(istruttoria.getId());
        entityManager.refresh(istruttoria);

        // return d;
        return istruttoria;
    }
    
    private IstruttoriaModel eseguiControlliZootecnia(String numeroDomanda, String response1) throws Exception {

        
        String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B+%22azienda%22%3A%7B%22cuaa%22%3A%22SGNNLN54T28A372J%22%7D%2C+%22stato%22+%3A+%7B%22identificativo%22%3A+%22CONTROLLATA%22%7D%7D";
        JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/antimafia.json"));
        String resString = objectMapper.writeValueAsString(response);
        Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

        String serviceUrl1 = "http://localhost:8080/a4gistruttoria/api/v1/antimafia/domandecollegate?params=%7B+%22cuaa%22%3A%22SGNNLN54T28A372JX%22%2C+%22tipoDomanda%22%3A+%22DOMANDA_UNICA%22%2C+%22idDomanda%22%3A%22189674%22+%7D";
        String resString1 = response1;
        if (response1 == null || response1.isEmpty()) {
            resString1 = objectMapper.writeValueAsString(new ArrayList<DomandaCollegata>());
        }
        Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl1)), Mockito.eq(String.class))).thenReturn(resString1);
   

        DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
        assertThat(d).isNotNull();

        //List<IstruttoriaModel> istruttoriaList = istruttoriaDao.findByDomandaUnicaModelId(d.getId());
        
        //moking InizializzaDatiSostegnoAccoppiatoZootecnia.caricaPremio(istruttria model)
        
        //IstruttoriaModel istruttoria = istruttoriaList.stream().filter(istr -> istr.getId() == 100379).limit(1).collect(Collectors.toList()).get(0);
        
        IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.ZOOTECNIA, TipoIstruttoria.SALDO);

        controlloIntersostegnoManager.elabora(istruttoria.getId());

        istruttoria = istruttoriaDao.getOne(istruttoria.getId());
        entityManager.refresh(istruttoria);

        // return d;
        return istruttoria;
    }

    private Optional<VariabileCalcolo> getVariabileInput(DatiInput variabiliInput,
                                                         TipoVariabile tipovariabile) {
        return variabiliInput.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(tipovariabile)).findFirst();
    }

    private Optional<VariabileCalcolo> getVariabileOutput(DatiOutput variabili,
                                                          TipoVariabile tipovariabile) {
        return variabili.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(tipovariabile)).findFirst();
    }

    private Optional<VariabileCalcolo> getVariabileOutputPasso(DatiOutput variabiliOutput, TipoVariabile variabile) {
        Optional<VariabileCalcolo> optDffrpagdis = variabiliOutput.getVariabiliCalcolo().stream().filter(v -> v.getTipoVariabile().equals(variabile)).findFirst();
        return optDffrpagdis;
    }

    private Optional<PassoTransizioneModel> getPassoImportoAntimafia(Set<PassoTransizioneModel> passiTransizione) {
        Optional<PassoTransizioneModel> optPassoLavAntimafia = passiTransizione.stream().filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_ANTIMAFIA))
                .findFirst();
        return optPassoLavAntimafia;
    }

    private Optional<PassoTransizioneModel> getPassoImportoMinimo(Set<PassoTransizioneModel> passiTransizione) {
        Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream()
                .filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.CONTROLLO_IMPORTO_MINIMO))
                .findFirst();
        return optPassoLavImportoMinimo;
    }

    private Optional<PassoTransizioneModel> getPassoDisciplina(Set<PassoTransizioneModel> passiTransizione) {
        Optional<PassoTransizioneModel> optPassoLavImportoMinimo = passiTransizione.stream()
                .filter(p -> p.getCodicePasso().equals(TipologiaPassoTransizione.DISCIPLINA_FINANZIARIA))
                .findFirst();
        return optPassoLavImportoMinimo;
    }

    private Optional<TransizioneIstruttoriaModel> caricaTransizioneIntersostegno(IstruttoriaModel istruttoria) {
        Optional<TransizioneIstruttoriaModel> transizioneSostegno =
                istruttoria.getTransizioni().stream().filter(t -> Arrays.asList(StatoIstruttoria.NON_LIQUIDABILE,
                        StatoIstruttoria.PAGAMENTO_NON_AUTORIZZATO,
                        StatoIstruttoria.CONTROLLI_INTERSOSTEGNO_OK).contains(StatoIstruttoria.valueOfByIdentificativo(t.getA4gdStatoLavSostegno1().getIdentificativo())))
                        .sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione))
                        .findFirst();
        return transizioneSostegno;
    }
}
