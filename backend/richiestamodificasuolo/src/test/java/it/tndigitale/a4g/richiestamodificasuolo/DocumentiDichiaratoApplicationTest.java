package it.tndigitale.a4g.richiestamodificasuolo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.h2.tools.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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

import static it.tndigitale.a4g.richiestamodificasuolo.Ruoli.*;
import it.tndigitale.a4g.richiestamodificasuolo.api.ApiUrls;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ProfiloUtente;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.client.AnagraficaFascicoloClient;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.client.FascicoloClient;
import it.tndigitale.a4g.richiestamodificasuolo.dto.richiestaModificaSuolo.DocumentazioneRichiestaDto;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DocumentiDichiaratoApplicationTest {
    
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

    public DocumentiDichiaratoApplicationTest() {
    }

    @Test
    @WithMockUser(username = "utente", roles = "NO RUOLO")
    void getDocumentoUtenteNonLoggato() throws Exception {
        Long idDichiarato = 136454L;

        mockMvc
            .perform(get(ApiUrls.SUOLO_DICHIARATO + "/" + idDichiarato + "/documentiDichiarato"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "utente", roles = VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD)
    void getDocumentoOk() throws Exception {
        Long idDichiarato = 136454L;
        Long idDocumento = 110L;

        mockMvc
            .perform(get(ApiUrls.SUOLO_DICHIARATO + "/" + idDichiarato + "/documentoDichiarato/" + idDocumento))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "utente", roles = VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD)
    void getDocumentoInesistente() throws Exception {
        Long idDichiarato = 136454L;
        Long idDocumento = 999L;

        mockMvc
            .perform(get(ApiUrls.SUOLO_DICHIARATO + "/" + idDichiarato + "/documentoDichiarato/" + idDocumento))
            .andExpect(status().isNotAcceptable());
    }

    @Test
    @WithMockUser(username = "utente", roles = EDIT_MESSAGGIO_RICHIESTA_TUTTI_COD)
    void uploadDocumentiRichiestaModificaSuoloOk() throws Exception {
        Long idDichiarato = 136454L;

        String fileName = "TestPDF.pdf";
        Path filePdfPath = Paths.get("./src/test/resources/" + fileName);
        MockMultipartFile file = new MockMultipartFile("file", fileName, "", Files.readAllBytes(filePdfPath));
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(ApiUrls.SUOLO_DICHIARATO + "/" + idDichiarato + "/documentoDichiarato");
        
        builder.with(request -> {
            // request.setParameter("descrizione", "DescrizioneTEST");
            request.setParameter("profiloUtente", ProfiloUtente.CAA.toString());
            request.setParameter("dimensione", "8");
            request.setMethod(HttpMethod.POST.name());
            
            return request;
        });

        mockMvc
            .perform(builder.file(file))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "utente", roles = VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD)
    void ricercaDocumentiDichiaratoOk() throws Exception {
        Long idDichiarato = 136454L;

        MvcResult result = mockMvc
            .perform(get(ApiUrls.SUOLO_DICHIARATO + "/" + idDichiarato + "/documentiDichiarato"))
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(
            "[{\"id\":110,\"nomeFile\":\"Allegato4\",\"descrizione\":\"descrizione\",\"dimensione\":2145,\"utente\":\"MRNFNC76R23A785E\",\"profiloUtente\":\"CAA\",\"dataInserimento\":\"2021-02-11T00:00:00\",\"docContent\":null,\"idPoligonoDichiarato\":136454}]",
            content
        );

    }

    @Test
    @WithMockUser(username = "utente", roles = "NO RUOLO")
    void deleteDocumentoOkUtenteNonLoggato() throws Exception {
        Long idDichiarato = 136454L;
        Long idDocumento = 110L;

        mockMvc
            .perform(delete(ApiUrls.SUOLO_DICHIARATO + "/" + idDichiarato + "/documentoDichiarato/" + idDocumento))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "utente", roles = {VISUALIZZA_RICHIESTA_MODIFICA_SUOLO_TUTTI_COD, EDIT_MESSAGGIO_RICHIESTA_TUTTI_COD})
    void deleteDocumentoOk() throws Exception {
        Long idDichiarato = 136454L;
        Long idDocumento = 110L;

        MvcResult result = mockMvc
            .perform(get(ApiUrls.SUOLO_DICHIARATO + "/" + idDichiarato + "/documentiDichiarato"))
            .andExpect(status().isOk())
            .andReturn();

        String json = result.getResponse().getContentAsString();

        List<DocumentazioneRichiestaDto> documenti = objectMapper.readValue(json, new TypeReference<List<DocumentazioneRichiestaDto>>() {});
        Optional<DocumentazioneRichiestaDto> documento = documenti
            .stream()
            .filter(x -> x.getId().equals(idDocumento))
            .findFirst();
        
        assertTrue(documento.isPresent());

        mockMvc
            .perform(delete(ApiUrls.SUOLO_DICHIARATO + "/" + idDichiarato + "/documentoDichiarato/" + idDocumento))
            .andExpect(status().isOk());

        result = mockMvc
            .perform(get(ApiUrls.SUOLO_DICHIARATO + "/" + idDichiarato + "/documentiDichiarato"))
            .andExpect(status().isNoContent())
            .andReturn();

        json = result.getResponse().getContentAsString();

        documenti = objectMapper.readValue(json, new TypeReference<List<DocumentazioneRichiestaDto>>() {});
        documento = documenti
            .stream()
            .filter(x -> x.getId().equals(idDocumento))
            .findFirst();

        assertFalse(documento.isPresent());
    }

    @Test
    @WithMockUser(username = "utente", roles = EDIT_MESSAGGIO_RICHIESTA_TUTTI_COD)
    void deleteDocumentoInesistente() throws Exception {
        Long idDichiarato = 136454L;
        Long idDocumento = 999L;

        mockMvc
            .perform(delete(ApiUrls.SUOLO_DICHIARATO + "/" + idDichiarato + "/documentoDichiarato/" + idDocumento))
            .andExpect(status().isNotAcceptable());
    }
}
