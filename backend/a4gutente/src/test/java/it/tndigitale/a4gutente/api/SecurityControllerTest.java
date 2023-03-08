package it.tndigitale.a4gutente.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext context;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    @WithMockUser("FRSLBT76H42E625Z")
    public void ricercaDomandaProtocollataNonAutorizzato403() throws Exception {
        String cf = "TRRRNZ56R23F837Z";
        String params = convertiCriteriParams(cf, StatoDomandaRegistrazioneUtente.PROTOCOLLATA);
        ResultActions resultActions = mockMvc
                .perform(get(ApiUrls.DOMANDE_V1).param("params", params));
        // verifico il risultato
        resultActions.andDo(print()).andExpect(status().isForbidden());

    }


    protected String convertiCriteriParams(String cf, StatoDomandaRegistrazioneUtente stato) {
        return "{\"stato\":\"" + stato + "\",\"codiceFiscale\":\"" + cf + "\"}";
    }

}
