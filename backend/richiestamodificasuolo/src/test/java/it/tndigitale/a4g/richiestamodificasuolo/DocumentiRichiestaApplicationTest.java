package it.tndigitale.a4g.richiestamodificasuolo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.h2.tools.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.SportelloFascicoloDto;
import it.tndigitale.a4g.fascicolo.client.model.Fascicolo;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.richiestamodificasuolo.api.ApiUrls;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ProfiloUtente;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.client.AnagraficaFascicoloClient;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.client.FascicoloClient;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.DocumentazioneRichiestaDto;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DocumentiRichiestaApplicationTest {
    static Server h2WebServer;
    
    @BeforeAll
    public static void initTest() throws SQLException {
        h2WebServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
        h2WebServer.start();
    }

    @AfterAll
    public static void stopServer() throws SQLException {
        h2WebServer.stop();
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AnagraficaFascicoloClient anagrafica;
    
    @MockBean
    private FascicoloClient fascicoloClient;

    public Paginazione paginazione;

    public DocumentiRichiestaApplicationTest() {
        paginazione = new Paginazione();
        paginazione.setNumeroElementiPagina(1);
        paginazione.setPagina(0);

    }

    /*
     * @Before public void initialize() throws Exception { Mockito.when(abilitazioniUtente.getAziendeUtente()).thenReturn(new ArrayList<String>()); }
     */

    @Test
    @WithMockUser(username = "utente", roles = { "NO RUOLO" })
    void getDocumentoUtenteNonLoggato() throws Exception {
        Long idRichiesta = 2501L;

        mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti/").concat("1"))).andExpect(status().isForbidden());
    }

    /*
     * @Test
     * 
     * @WithMockUser(username = "utente", roles = { "TEST" }) void getDocumentoUtenteNonAutorizzatoRuolo() throws Exception { Long idRichiesta = 2501L;
     * 
     * mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti/").concat("1"))).andExpect(status().isUnauthorized()); }
     */

    /*
     * @Test
     * 
     * @WithMockUser(username = "utente", roles = {Ruoli.VISUALIZZA_FASCICOLO_FILTRO_ENTE_COD}) void getDocumentoUtenteNonAutorizzatoMandati() throws Exception { Long idRichiesta = 2501L;
     * 
     * mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti/").concat("1"))) .andExpect(status() .isUnauthorized()); }
     */

    @Test
    @WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD })
    void getDocumentoOk() throws Exception {
        Long idRichiesta = 2501L;
        Long idDocumento = 101L;

        mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti/").concat(String.valueOf(idDocumento)))).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD })
    void getDocumentoInesistente() throws Exception {
        Long idRichiesta = 2501L;
        Long idDocumento = 12345L;

        mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti/").concat(String.valueOf(idDocumento))))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser(username = "utente", roles = { Ruoli.EDIT_DOCUMENTO_RICHIESTA_TUTTI_COD })
    void uploadDocumentiRichiestaModificaSuoloOk() throws Exception {

        Long idRichiesta = 2501L;

        String fileName = "TestPDF.pdf";
        Path filePdfPath = Paths.get("./src/test/resources/" + fileName);
        MockMultipartFile file = new MockMultipartFile("file", fileName, "", Files.readAllBytes(filePdfPath));
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti"));
        builder.with(request -> {
            // request.setParameter("descrizione", "DescrizioneTEST");
            request.setParameter("profiloUtente", ProfiloUtente.CAA.toString());
            request.setParameter("dimensione", "8");
            request.setMethod(HttpMethod.POST.name());
            return request;
        });

        this.mockMvc.perform(builder.file(file)).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
    void uploadDocumentiRichiestaModificaSuoloOkProfiloCAA() throws Exception {

        Long idRichiesta = 2501L;

        String fileName = "TestPDF.pdf";
        Path filePdfPath = Paths.get("./src/test/resources/" + fileName);
        MockMultipartFile file = new MockMultipartFile("file", fileName, "", Files.readAllBytes(filePdfPath));
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti"));
        builder.with(request -> {
            // request.setParameter("descrizione", "DescrizioneTEST");
            request.setParameter("profiloUtente", ProfiloUtente.CAA.toString());
            request.setParameter("dimensione", "8");
            request.setMethod(HttpMethod.POST.name());
            return request;
        });
        utenteConMandato();
        this.mockMvc.perform(builder.file(file)).andExpect(status().isCreated());
    }
    
    @Test
    @WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_ENTE_COD })
    void uploadDocumentiRichiestaModificaSuoloOkProfiloCAASenzaMandato() throws Exception {

        Long idRichiesta = 2501L;

        String fileName = "TestPDF.pdf";
        Path filePdfPath = Paths.get("./src/test/resources/" + fileName);
        MockMultipartFile file = new MockMultipartFile("file", fileName, "", Files.readAllBytes(filePdfPath));
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti"));
        builder.with(request -> {
            // request.setParameter("descrizione", "DescrizioneTEST");
            request.setParameter("profiloUtente", ProfiloUtente.CAA.toString());
            request.setParameter("dimensione", "8");
            request.setMethod(HttpMethod.POST.name());
            return request;
        });
        utenteSenzaMandato();
        this.mockMvc.perform(builder.file(file)).andExpect(status().isForbidden());
    }

    /*
     * @Test
     * 
     * @WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_FASCICOLO_FILTRO_ENTE_COD }) void uploadDocumentiRichiestaModificaSuoloFormatoErrato() throws Exception {
     * 
     * try { Long idRichiesta = 2501L;
     * 
     * String fileName = "TestPDF.pdf"; Path filePdfPath = Paths.get("./src/test/resources/" + fileName); MockMultipartFile file = new MockMultipartFile("file", "TestPDF.exe", "",
     * Files.readAllBytes(filePdfPath)); MockMultipartHttpServletRequestBuilder builder =
     * MockMvcRequestBuilders.multipart(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti")); builder; builder.with(request -> { //
     * request.setParameter("descrizione", "DescrizioneTEST"); request.setParameter("profiloUtente", "DIPENDENTE_CAA"); request.setParameter("dimensione", "8");
     * request.setMethod(HttpMethod.POST.name()); return request; });
     * 
     * this.mockMvc.perform(builder.file(file)).andExpect(status().isUnauthorized()); } catch (Exception ex) { System.out.println(ex.getMessage()); } }
     */

    /*
     * @Test
     * 
     * @WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_FASCICOLO_FILTRO_ENTE_COD }) void uploadDocumentiRichiestaInesistente() throws Exception { try { Long idRichiesta = 123345L;
     * 
     * String fileName = "TestPDF.pdf"; Path filePdfPath = Paths.get("./src/test/resources/" + fileName); MockMultipartFile file = new MockMultipartFile("file", fileName, "",
     * Files.readAllBytes(filePdfPath)); MockMultipartHttpServletRequestBuilder builder =
     * MockMvcRequestBuilders.multipart(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti")); builder; builder.with(request -> { //
     * request.setParameter("descrizione", "DescrizioneTEST"); request.setParameter("profiloUtente", "DIPENDENTE_CAA"); request.setParameter("dimensione", "8");
     * request.setMethod(HttpMethod.POST.name()); return request; });
     * 
     * var a = this.mockMvc.perform(builder.file(file));
     * 
     * assert (false);
     * 
     * } catch (Exception ex) { System.out.println(ex.getMessage());
     * 
     * assert (true); } }
     */

    @Test
    @WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD })
    void ricercaDocumentiRichiestaModificaSuoloOk() throws Exception {

        Long idRichiesta = 2501L;

        MvcResult result = mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti") + "?numeroElementiPagina="
                + paginazione.getNumeroElementiPagina() + "&pagina=" + paginazione.getPagina() + "&valid=true")).andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(
                "{\"count\":4,\"risultati\":[{\"id\":104,\"nomeFile\":\"Allegato2\",\"descrizione\":\"Allegato foto\",\"dimensione\":2145,\"utente\":\"MRNFNC76R23A785E\",\"profiloUtente\":\"CAA\",\"dataInserimento\":\"2021-02-11T00:00:00\",\"docContent\":null,\"idPoligonoDichiarato\":null}]}",
                                content);
        ;

    }

    @Test
    @WithMockUser(username = "utente", roles = { Ruoli.EDIT_LAVORAZIONE_SUOLO_COD })
    void deleteDocumentoOkUtenteNonLoggato() throws Exception {
        Long idRichiesta = 2501L;
        Long idDocumento = 1L;

        String url = ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti/").concat(String.valueOf(idDocumento));

        mockMvc.perform(delete(url)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "utente", roles = { Ruoli.EDIT_DOCUMENTO_RICHIESTA_TUTTI_COD, Ruoli.VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD })
    void deleteDocumentoOk() throws Exception {

        Long idRichiesta = 2501L;
        Long idDocumento = 101L;

        MvcResult result = mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti") + "?valid=true")).andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        RisultatiPaginati<DocumentazioneRichiestaDto> res = objectMapper.readValue(contentAsString, new TypeReference<RisultatiPaginati<DocumentazioneRichiestaDto>>() {
        });
        Optional<DocumentazioneRichiestaDto> docFound = res.getRisultati().stream().filter(x -> x.getId() == idDocumento).findFirst();
        assertEquals(docFound.isPresent(), true);

        /*
         * Mockito.when(abilitazioniComponent.checkAccessResourceRichiestaModificaSuolo(idRichiesta)).thenReturn(true);
         * 
         * String cuaa = "MCHGLN69H10L378R"; List<FascicoloDto> responseAnagrafica = new ArrayList<FascicoloDto>(); FascicoloDto fascicolo = new FascicoloDto(); fascicolo.setCuaa(cuaa);
         * responseAnagrafica.add(fascicolo);
         * 
         * Mockito.when(anagrafica.ricercaFascicoli()).thenReturn(responseAnagrafica);
         */

        String url = ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti/").concat(String.valueOf(idDocumento));

        mockMvc.perform(delete(url)).andExpect(status().isOk());

        result = mockMvc.perform(get(ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti") + "?valid=true")).andExpect(status().isOk()).andReturn();

        contentAsString = result.getResponse().getContentAsString();

        res = objectMapper.readValue(contentAsString, new TypeReference<RisultatiPaginati<DocumentazioneRichiestaDto>>() {
        });
        docFound = res.getRisultati().stream().filter(x -> x.getId() == idDocumento).findFirst();

        assertEquals(docFound.isPresent(), false);

    }

    @Test
    @WithMockUser(username = "utente", roles = { Ruoli.EDIT_DOCUMENTO_RICHIESTA_TUTTI_COD })
    void deleteDocumentoInesistente() throws Exception {
        Long idRichiesta = 2501L;
        Long idDocumento = 5678L;

        String url = ApiUrls.RICHIESTA_MODIFICA_SUOLO.concat("/").concat(String.valueOf(idRichiesta)).concat("/documenti/").concat(String.valueOf(idDocumento));

        mockMvc.perform(delete(url)).andExpect(status().isNotAcceptable());

    }

    private void utenteConMandato() throws Exception {
        String cuaa = "MCHGLN69H10L378R";
        List<SportelloFascicoloDto> responseAnagrafica = new ArrayList<SportelloFascicoloDto>();
        SportelloFascicoloDto sportelloFascicolo = new SportelloFascicoloDto();
        sportelloFascicolo.setIdentificativoSportello(18);
        List<String> listCuaa = new ArrayList<String>();
        listCuaa.add(cuaa);
        sportelloFascicolo.setCuaaList(listCuaa);
        responseAnagrafica.add(sportelloFascicolo);

        Mockito.when(anagrafica.ricercaFascicoli()).thenReturn(responseAnagrafica);
    }

    private void utenteSenzaMandato() throws Exception {
        List<Fascicolo> responseAnagrafica = new ArrayList<Fascicolo>();

        Mockito.when(fascicoloClient.ricercaFascicoli()).thenReturn(responseAnagrafica);
    }
}
