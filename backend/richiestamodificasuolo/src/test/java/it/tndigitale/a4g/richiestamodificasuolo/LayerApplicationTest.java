package it.tndigitale.a4g.richiestamodificasuolo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;
import java.util.List;

import org.h2.tools.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.richiestamodificasuolo.api.ApiUrls;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.ProfiloUtente;
import it.tndigitale.a4g.richiestamodificasuolo.business.service.client.UtenteControllerClient;
import it.tndigitale.a4g.richiestamodificasuolo.dto.layer.LayerSwitcherDto;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LayerApplicationTest {
    
    static Server h2WebServer;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private UtenteControllerClient utenteControllerClient;

    @BeforeAll
    public static void initTest() throws SQLException {
        h2WebServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
        h2WebServer.start();
    }
    @AfterAll
    public static void stopServer() throws SQLException {
        h2WebServer.stop();
    }

    @Test
    @WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_LAYER_CAA_COD })
    void getLayerAssociatiAlProfiloCAA() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(ApiUrls.LAYER).param("profilo_utente", "CAA"));

        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        List<LayerSwitcherDto> results = objectMapper.readValue(content, new TypeReference<List<LayerSwitcherDto>>() {});

        results.forEach(layer -> {
            assertNotNull(layer.getId());
            assertNotNull(layer.getUrl());
            assertNotNull(layer.getProprieta());
        });
    }

    @Test
    @WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_LAYER_BACKOFFICE_COD })
    void getLayerAssociatiAlProfiloBackOffice() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(ApiUrls.LAYER).param("profilo_utente", ProfiloUtente.BACKOFFICE.toString()));

        final MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        List<LayerSwitcherDto> results = objectMapper.readValue(content, new TypeReference<List<LayerSwitcherDto>>() {});

        results.forEach(layer -> {
            assertNotNull(layer.getId());
            assertNotNull(layer.getUrl());
        });
    }

    @Test
    @WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_LAYER_VITICOLO_COD })
    void getLayerAssociatiAlProfiloBackViticolo() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(ApiUrls.LAYER).param("profilo_utente", ProfiloUtente.VITICOLO.toString()));

        resultActions.andDo(print()).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_LAYER_CAA_COD })
    void getLayerAssociatiAlProfiloCAA403() throws Exception {
        mockMvc.perform(get(ApiUrls.LAYER).param("profilo_utente", ProfiloUtente.BACKOFFICE.toString())).andExpect(status().isForbidden());
        mockMvc.perform(get(ApiUrls.LAYER).param("profilo_utente", ProfiloUtente.VITICOLO.toString())).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_LAYER_BACKOFFICE_COD })
    void getLayerAssociatiAlProfiloBackOffice403() throws Exception {
        mockMvc.perform(get(ApiUrls.LAYER).param("profilo_utente", "CAA")).andExpect(status().isForbidden());
        mockMvc.perform(get(ApiUrls.LAYER).param("profilo_utente", ProfiloUtente.VITICOLO.toString())).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "utente", roles = { Ruoli.VISUALIZZA_LAYER_VITICOLO_COD })
    void getLayerAssociatiAlProfiloViticolo403() throws Exception {
        mockMvc.perform(get(ApiUrls.LAYER).param("profilo_utente", ProfiloUtente.CAA.toString())).andExpect(status().isForbidden());
        mockMvc.perform(get(ApiUrls.LAYER).param("profilo_utente", ProfiloUtente.BACKOFFICE.toString())).andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(username = "utente", roles = {})
    void getLayersUtenteCAA() throws Exception {
        getLayersUtente(
            Arrays.asList(ProfiloUtente.CAA),
            Arrays.asList(
                "pub_stem_s:agea_2017",
                "app_a4s:A4SV_SUOLO_LAYER2",
                "app_a4s:A4SV_SUOLO_DICHIARATO_LAYER2",
                "app_a4s:A4SV_SUOLO_RILEVATO_LAYER2"
            )
        );
    }
    
    @Test
    @WithMockUser(username = "utente", roles = {})
    void getLayersUtenteBackOffice() throws Exception {
        getLayersUtente(
            Arrays.asList(ProfiloUtente.BACKOFFICE),
            Arrays.asList(
                "pub_stem_s:agea_2017",
                "app_a4s:A4SV_SUOLO_LAYER2",
                "app_a4s:A4SV_SUOLO_DICHIARATO_LAYER2",
                "app_a4s:A4SV_SUOLO_RILEVATO_LAYER2",
                "app_a4s:A4SV_WORKSPACE_LAV_SUOLO_LAYER2"
            )
        );
    }
    
    @Test
    @WithMockUser(username = "utente", roles = {})
    void getLayersUtenteViticolo() throws Exception {
        getLayersUtente(
            Arrays.asList(ProfiloUtente.VITICOLO),
            Arrays.asList(
                "pub_stem_s:agea_2017",
                "app_a4s:A4SV_SUOLO_LAYER2",
                "app_a4s:A4SV_SUOLO_DICHIARATO_LAYER2",
                "app_a4s:A4SV_SUOLO_RILEVATO_LAYER2",
                "app_a4s:A4SV_WORKSPACE_LAV_SUOLO_LAYER2"
            )
        );
    }
    
    @Test
    @WithMockUser(username = "utente", roles = {})
    void getLayersUtenteCAAandBackOffice() throws Exception {
        getLayersUtente(
            Arrays.asList(ProfiloUtente.BACKOFFICE, ProfiloUtente.CAA),
            Arrays.asList(
                "pub_stem_s:agea_2017",
                "app_a4s:A4SV_SUOLO_LAYER2",
                "app_a4s:A4SV_SUOLO_DICHIARATO_LAYER2",
                "app_a4s:A4SV_SUOLO_RILEVATO_LAYER2",
                "app_a4s:A4SV_WORKSPACE_LAV_SUOLO_LAYER2"
            )
        );
    }
    
    private void getLayersUtente(List<ProfiloUtente> profili, List<String> layers) throws Exception {
        List<String> list = profili
            .stream()
            .map(ProfiloUtente::toString)
            .collect(Collectors.toList());
        
        Mockito
            .when(utenteControllerClient.getProfili(anyString()))
            .thenReturn(list);
        
        String content = mockMvc
            .perform(get(ApiUrls.LAYER.concat("/abilitati")).param("codiceFiscale", "NSCMSM74A22H612G"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        
        Map<String, String> nomi = objectMapper.readValue(content, new TypeReference<Map<String, String>>() {});
        
        Assertions
            .assertThat(nomi.keySet())
            .hasSameElementsAs(layers);
        
        Assertions
            .assertThat(nomi.values())
            .allMatch(Objects::isNull);
    }
}