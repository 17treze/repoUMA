package it.tndigitale.a4gutente.api;

import static it.tndigitale.a4gutente.TestSupport.nonAutorizzato;
import static it.tndigitale.a4gutente.dto.MotivazioneDisattivazione.FINE_RAPPORTO;
import static it.tndigitale.a4gutente.utility.JsonSupport.toObject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gutente.codici.Ruoli;
import it.tndigitale.a4gutente.dto.Istruttoria;
import it.tndigitale.a4gutente.dto.IstruttoriaPerStorico;
import it.tndigitale.a4gutente.dto.IstruttoriaSenzaDomanda;
import it.tndigitale.a4gutente.dto.StoricoIstruttorie;
import it.tndigitale.a4gutente.repository.dao.IIstruttoriaDao;
import it.tndigitale.a4gutente.repository.model.IstruttoriaEntita;
import it.tndigitale.a4gutente.utility.JsonSupport;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IstruttoriaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IIstruttoriaDao istruttoriaDao;

    @MockBean
    private Clock clock;

    private static final Long ID_DOMANDA_SENZA_SEDI_E_ENTI = 333444987L;
    private static final Long ID_DOMANDA = 333444989L;
    private static final Long ID_DOMANDA_INESISTENTE = 333444700L;
    private static final Long ID_UTENTE = 20000001L;
    private static final Long ID_UTENTE_STORICO = 100000101L;
    private static final String URL_STORICO = ApiUrls.ISTRUTTORIA + "/utente/" + ID_UTENTE_STORICO + "/storico";
    private static final LocalDateTime NOW = LocalDateTime.of(2019,10,10,1,0);

    @Test
    @Transactional
    @WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.VISUALIZZA_ISTRUTTORIA_DOMANDA_COD})
    @Sql("/customsql/istruttoria.sql")
    public void caricaIstruttoriaDomandaSenzaSediEdENTI() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(ApiUrls.ISTRUTTORIA + "/domanda/" + ID_DOMANDA_SENZA_SEDI_E_ENTI))
                                           .andDo(print())
                                           .andExpect(status().isOk())
                                           .andReturn();

        Istruttoria istruttoria = toObject(mvcResult.getResponse().getContentAsString(), Istruttoria.class);

        assertThat(istruttoria).isNotNull();
        assertThat(istruttoria.getIdDomanda()).isEqualTo(ID_DOMANDA_SENZA_SEDI_E_ENTI);
        assertThat(istruttoria.getSedi()).isEmpty();
        assertThat(istruttoria.getProfili()).isEmpty();
    }

    @Test
    @Transactional
    @WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.VISUALIZZA_ISTRUTTORIA_DOMANDA_COD})
    @Sql("/customsql/istruttoria.sql")
    public void caricaIstruttoriaDomanda() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(ApiUrls.ISTRUTTORIA + "/domanda/" + ID_DOMANDA))
                                           .andDo(print())
                                           .andExpect(status().isOk())
                                           .andReturn();

        Istruttoria istruttoria = toObject(mvcResult.getResponse().getContentAsString(), Istruttoria.class);
        assertThat(istruttoria).isNotNull();
        assertThat(istruttoria.getIdDomanda()).isEqualTo(ID_DOMANDA);
        assertThat(istruttoria.getSedi()).hasSize(1);
        assertThat(istruttoria.getSedi()).contains(333444992L);
        assertThat(istruttoria.getProfili()).hasSize(1);
        assertThat(istruttoria.getProfili()).contains(333444991L);
    }

    @Test
    @Transactional
    @WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.VISUALIZZA_DOMANDE_COD})
    @Sql("/customsql/istruttoria.sql")
    public void caricaIstruttoriaDomandaKOPerMancanzaAbilitazioni() throws Exception {
        mockMvc.perform(get(ApiUrls.ISTRUTTORIA + "/domanda/" + ID_DOMANDA))
               .andDo(print())
               .andExpect(nonAutorizzato());
    }

    @Test
    @Transactional
    @WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.VISUALIZZA_ISTRUTTORIA_DOMANDA_COD})
    @Sql("/customsql/istruttoria.sql")
    public void caricaIstruttoriaDomandaInesistente() throws Exception {
        mockMvc.perform(get(ApiUrls.ISTRUTTORIA + "/domanda/" + ID_DOMANDA_INESISTENTE))
               .andDo(print())
               .andExpect(status().isNoContent());
//        TODO
//               .andExpect(status().isNotFound()); //prima di integrazione con framework
    }

    @Test
    @Transactional
    @WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.VISUALIZZA_ISTRUTTORIA_DOMANDA_COD})
    @Sql("/customsql/istruttoria.sql")
    public void cicloDiVitaIstruttoriaDomandaKOPerMancanzaAbilitazioni() throws Exception{
        mockMvc.perform(post(ApiUrls.ISTRUTTORIA + "/domanda")
                            .content(contentIstruttoriaDaCreare())
                            .header("Content-Type", "application/json"))
                .andDo(print())
                .andExpect(nonAutorizzato());

        mockMvc.perform(put(ApiUrls.ISTRUTTORIA + "/domanda")
                                    .content(contentIstruttoriaDaModificare(10234L))
                                    .header("Content-Type", "application/json"))
               .andDo(print())
               .andExpect(nonAutorizzato());
    }


    @Test
    @Transactional
    @WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.EDITA_ISTRUTTORIA_DOMANDA_COD})
    @Sql("/customsql/istruttoria.sql")
    public void cicloDiVitaIstruttoriaDomanda() throws Exception{
        final MvcResult mvcResult = mockMvc.perform(post(ApiUrls.ISTRUTTORIA + "/domanda")
                                                      .content(contentIstruttoriaDaCreare())
                                                      .header("Content-Type", "application/json")
                                                    )
                                           .andDo(print())
                                           .andExpect(status().isOk())
                                           .andReturn();

        Long idIstruttoriaCreate = toObject(mvcResult.getResponse().getContentAsString(), Long.class);

        assertThat(idIstruttoriaCreate).isNotNull();

        final MvcResult mvcResult2 = mockMvc.perform(put(ApiUrls.ISTRUTTORIA + "/domanda")
                                                        .content(contentIstruttoriaDaModificare(idIstruttoriaCreate))
                                                        .header("Content-Type", "application/json")
                                                    )
                                            .andDo(print())
                                            .andExpect(status().isOk())
                                            .andReturn();

        Long idIstruttoriaUpdate = toObject(mvcResult2.getResponse().getContentAsString(), Long.class);

        assertThat(idIstruttoriaUpdate).isNotNull();
        assertThat(idIstruttoriaUpdate).isEqualTo(idIstruttoriaCreate);

    }

    @Test
    @Transactional
    @WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.EDITA_ISTRUTTORIA_DOMANDA_COD})
    @Sql("/customsql/istruttoria.sql")
    public void aggiornamentoIstruttoriaNonInLavorazioneGeneraErrore() throws Exception {
        mockMvc.perform(post(ApiUrls.ISTRUTTORIA + "/domanda").content(contentIstruttoriaDaCreareConErrore())
                                                                         .header("Content-Type", "application/json"))
               .andDo(print())
               .andExpect(status().isInternalServerError());

        mockMvc.perform(put(ApiUrls.ISTRUTTORIA + "/domanda").content(contentIstruttoriaDaModificareConErrore())
                                                                        .header("Content-Type", "application/json"))
               .andDo(print())
               .andExpect(status().isInternalServerError());

    }

    @Test
    @Transactional
    @WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.CREA_UTENTE_COD})
    @Sql("/customsql/ModificaAbilitazioniUtente.sql")
    public void creazioneIstruttoriaSenzaDomandaPerModificaAbilitazioniUtente() throws Exception {
        when(clock.now()).thenReturn(NOW);

        final MvcResult mvcResult = mockMvc.perform(post(urlAggiornamentoIstrSenzaDomanda()).content(contentIstrutturiaSenzaDomanda())
                                                                                            .header("Content-Type", "application/json"))
                                           .andDo(print())
                                           .andExpect(status().isOk())
                                           .andReturn();

        Long idIstruttoriaCreate = toObject(mvcResult.getResponse().getContentAsString(), Long.class);

        assertThat(idIstruttoriaCreate).isNotNull();
        IstruttoriaEntita istruttoriaEntita = istruttoriaDao.findById(idIstruttoriaCreate)
                                                            .orElseThrow(() -> new Exception("Istruttoria non trovata"));
        assertThat(istruttoriaEntita.getDomanda()).isNull();
        assertThat(istruttoriaEntita.getMotivazioneDisattivazione()).isEqualTo(FINE_RAPPORTO);
        assertThat(istruttoriaEntita.getUtente()).isNotNull();
        assertThat(istruttoriaEntita.getUtente().getId()).isEqualTo(ID_UTENTE);
        assertThat(istruttoriaEntita.getUtente().getA4gtEntes()).hasSize(1);
        assertThat(istruttoriaEntita.getUtente().getA4gtEntes()).extracting("id").contains(20000006L);
        assertThat(istruttoriaEntita.getUtente().getProfili()).hasSize(1);
        assertThat(istruttoriaEntita.getUtente().getProfili()).extracting("id").contains(20000004L);
        assertThat(istruttoriaEntita.getUtente().getA4grUtenteAziendas()).hasSize(0);
        assertThat(istruttoriaEntita.getEnti()).hasSize(1);
        assertThat(istruttoriaEntita.getEnti()).extracting("id").contains(20000006L);
        assertThat(istruttoriaEntita.getProfili()).hasSize(1);
        assertThat(istruttoriaEntita.getProfili()).extracting("id").contains(20000004L);
        assertThat(istruttoriaEntita.getProfiliDisabilitati()).extracting("id").contains(20000003L);
        assertThat(istruttoriaEntita.getIstruttore()).isNotNull();
        assertThat(istruttoriaEntita.getIstruttore().getIdentificativo()).isEqualTo("FRSLBT76H42E625Z");
        assertThat(istruttoriaEntita.getDataTermineIstruttoria()).isEqualTo(NOW);
    }

    @Test
    @Transactional
    @WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.CREA_UTENTE_COD})
    @Sql("/customsql/ModificaAbilitazioniUtente.sql")
    public void creazioneIstruttoriaSenzaDomandaPerModificaAbilitazioniUtenteSenzaProfiliEEnti() throws Exception {
        when(clock.now()).thenReturn(NOW);

        final MvcResult mvcResult = mockMvc.perform(post(urlAggiornamentoIstrSenzaDomanda()).content(contentIstruttoriaSenzaDomandaSenzaProfiliESedi())
                                                                                            .header("Content-Type", "application/json"))
                                           .andDo(print())
                                           .andExpect(status().isOk())
                                           .andReturn();

        Long idIstruttoriaCreate = toObject(mvcResult.getResponse().getContentAsString(), Long.class);

        assertThat(idIstruttoriaCreate).isNotNull();
        IstruttoriaEntita istruttoriaEntita = istruttoriaDao.findById(idIstruttoriaCreate)
                                                            .orElseThrow(() -> new Exception("Istruttoria non trovata"));
        assertThat(istruttoriaEntita.getDomanda()).isNull();
        assertThat(istruttoriaEntita.getUtente()).isNotNull();
        assertThat(istruttoriaEntita.getUtente().getId()).isEqualTo(ID_UTENTE);
        assertThat(istruttoriaEntita.getUtente().getA4gtEntes()).hasSize(0);
        assertThat(istruttoriaEntita.getUtente().getProfili()).hasSize(0);
        assertThat(istruttoriaEntita.getUtente().getA4grUtenteAziendas()).hasSize(0);
        assertThat(istruttoriaEntita.getEnti()).hasSize(0);
        assertThat(istruttoriaEntita.getProfili()).hasSize(0);
        assertThat(istruttoriaEntita.getIstruttore()).isNotNull();
        assertThat(istruttoriaEntita.getIstruttore().getIdentificativo()).isEqualTo("FRSLBT76H42E625Z");
        assertThat(istruttoriaEntita.getDataTermineIstruttoria()).isEqualTo(NOW);
    }

    @Test
    @Transactional
    @WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.VISUALIZZA_DOMANDE_COD})
    @Sql("/customsql/ModificaAbilitazioniUtente.sql")
    public void creazioneIstruttoriaSenzaDomandaPerModificaAbilitazioniUtenteKOPerMancataAutorizzazione() throws Exception {
        mockMvc.perform(post(urlAggiornamentoIstrSenzaDomanda()).content(contentIstrutturiaSenzaDomanda())
                                                                .header("Content-Type", "application/json"))
               .andDo(print())
               .andExpect(nonAutorizzato());
    }


    @Test
    @Transactional
    @WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.IMPORTAZIONE_MASSIVA_UTENTI_COD})
    @Sql("/customsql/storicoIstruttorie.sql")
    public void visualizzaStoricoIstruttorieUtenteKOPErMancanzaAutorizzazioni() throws Exception {
        mockMvc.perform(get(URL_STORICO))
               .andDo(print())
               .andExpect(nonAutorizzato());
    }

    @Test
    @Transactional
    @WithMockUser(username="FRSLBT76H42E625Z", roles= {Ruoli.VISUALIZZA_DOMANDE_COD})
    @Sql("/customsql/storicoIstruttorie.sql")
    public void visualizzaStoricoIstruttorieUtente() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(get(URL_STORICO))
                                           .andDo(print())
                                           .andExpect(status().isOk())
                                           .andReturn();

        StoricoIstruttorie storico = toObject(mvcResult.getResponse().getContentAsString(),
                                              StoricoIstruttorie.class);

        assertThat(storico).isNotNull();
        assertThat(storico.getUtente().getId()).isEqualTo(ID_UTENTE_STORICO);
        assertThat(storico.getUtente().getIdentificativo()).isEqualTo("VNBMRC64R31F587O");
        assertThat(storico.getUtente().getCodiceFiscale()).isEqualTo("VNBMRC64R31F587O");
        assertThat(storico.getUtente().getCognome()).isEqualTo("VAN BASTEN");
        assertThat(storico.getIstruttorie()).hasSize(2);

        IstruttoriaPerStorico istruttoria1 = storico.getIstruttorie().get(0);
        assertThat(istruttoria1.getId()).isEqualTo(100001002L);
        assertThat(istruttoria1.getIdDomanda()).isNull();
        assertThat(istruttoria1.getIstruttore().getId()).isEqualTo(100000103L);
        assertThat(istruttoria1.getIstruttore().getCodiceFiscale()).isEqualTo("FRNBRS60E08L339G");
        assertThat(istruttoria1.getIstruttore().getCognome()).isEqualTo("BARESI");
        assertThat(istruttoria1.getProfili()).hasSize(3);
        assertThat(istruttoria1.getProfili().get(0).getDisabled()).isFalse();
        assertThat(istruttoria1.getProfili().get(1).getDisabled()).isFalse();
        assertThat(istruttoria1.getProfili().get(2).getDisabled()).isTrue();
        assertThat(istruttoria1.getSedi()).hasSize(2);

        IstruttoriaPerStorico istruttoria2 = storico.getIstruttorie().get(1);
        assertThat(istruttoria2.getId()).isEqualTo(100001001L);
        assertThat(istruttoria2.getIdDomanda()).isEqualTo(100000105L);
        assertThat(istruttoria2.getIstruttore().getId()).isEqualTo(100000103L);
        assertThat(istruttoria2.getIstruttore().getCodiceFiscale()).isEqualTo("FRNBRS60E08L339G");
        assertThat(istruttoria2.getIstruttore().getCognome()).isEqualTo("BARESI");
        assertThat(istruttoria2.getProfili()).hasSize(3);
        assertThat(istruttoria2.getProfili().get(0).getDisabled()).isFalse();
        assertThat(istruttoria2.getProfili().get(1).getDisabled()).isFalse();
        assertThat(istruttoria2.getProfili().get(2).getDisabled()).isFalse();
        assertThat(istruttoria2.getSedi()).hasSize(1);


    }


    private String urlAggiornamentoIstrSenzaDomanda() {
        return ApiUrls.ISTRUTTORIA + "/utente/" + ID_UTENTE;
    }

    private String contentIstrutturiaSenzaDomanda() {
        return JsonSupport.toJson(new IstruttoriaSenzaDomanda().setIdUtente(ID_UTENTE)
                                                               .setMotivazioneDisattivazione(FINE_RAPPORTO)
                                                               .setProfili(Arrays.asList(20000004L))
                                                               .setProfiliDaDisattivare(Arrays.asList(20000003L))
                                                               .setSedi(Arrays.asList(20000006L)));
    }

    private String contentIstruttoriaSenzaDomandaSenzaProfiliESedi() {
        return JsonSupport.toJson(new IstruttoriaSenzaDomanda().setIdUtente(ID_UTENTE));
    }

    private String contentIstruttoriaDaCreare() {
        return JsonSupport.toJson(new Istruttoria().setIdDomanda(333444993L)
                                                   .setMotivazioneRifiuto("Motivazione rifiuto")
                                                   .setTestoComunicazione("Testo comunicazione")
                                                   .setVariazioneRichiesta("Variazione richiesta")
                                                   .setSedi(Collections.singletonList(333444992L))
                                                   .setProfili(Collections.singletonList(333444991L)));
    }

    private String contentIstruttoriaDaModificare(Long idIstruttoria) {
        return JsonSupport.toJson(new Istruttoria().setId(idIstruttoria)
                                                   .setIdDomanda(333444993L)
                                                   .setMotivazioneRifiuto("Motivazione rifiuto x")
                                                   .setTestoComunicazione("Testo comunicazione x")
                                                   .setVariazioneRichiesta("Variazione richiesta x")
                                                   .setSedi(Collections.singletonList(333444993L))
                                                   .setProfili(Collections.singletonList(333444994L)));
    }

    private String contentIstruttoriaDaCreareConErrore() {
        return JsonSupport.toJson(new Istruttoria().setIdDomanda(333444995L)
                                                   .setMotivazioneRifiuto("Motivazione rifiuto")
                                                   .setTestoComunicazione("Testo comunicazione")
                                                   .setVariazioneRichiesta("Variazione richiesta")
                                                   .setSedi(Collections.singletonList(333444992L))
                                                   .setProfili(Collections.singletonList(333444991L)));
    }

    private String contentIstruttoriaDaModificareConErrore() {
        return JsonSupport.toJson(new Istruttoria().setId(333444997L)
                                                   .setIdDomanda(333444996L)
                                                   .setMotivazioneRifiuto("Motivazione rifiuto x")
                                                   .setTestoComunicazione("Testo comunicazione x")
                                                   .setVariazioneRichiesta("Variazione richiesta x"));
    }

}
