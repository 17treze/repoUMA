package it.tndigitale.a4gistruttoria.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DettaglioPascoli;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DettaglioPascoliControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private MockMvc mockMvc;
    private static final Long ID_ISTRUTTORIA = 90909091L;

    @Test
    @WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
    @Sql(scripts = "/sql/dettaglioPascoliControllerTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/dettaglioPascoliControllerTestDelete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public  void getInfoDettaglioPascoliWithEsitoMantenimento() throws Exception {
        MvcResult mvcResult = this.mockMvc
                                  .perform(get(ApiUrls.DETTAGLIO_PASCOLI_V1 + "/istruttoria/" + ID_ISTRUTTORIA +"/conesitomantenimento"))
//                                  .andExpect(status().isOk())
                                  .andReturn();

        List<DettaglioPascoli> results = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                                                                new TypeReference<List<DettaglioPascoli>>() {});
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getEsitoMan()).isNotNull();
        assertThat(results.get(0).getDatiInput()).isNotNull();
        assertThat(results.get(0).getDatiOutput()).isNotNull();
        assertThat(results.get(1).getEsitoMan()).isNotNull();
        assertThat(results.get(1).getDatiInput()).isNotNull();
        assertThat(results.get(1).getDatiOutput()).isNotNull();
    }

    @Test
    @WithMockUser(username = "istruttore")
    @Sql(scripts = "/sql/dettaglioPascoliControllerTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/dettaglioPascoliControllerTestDelete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public  void getInfoDettaglioPascoliWithEsitoMantenimentoKOPerMancanzaAbilitazioni() throws Exception {
        this.mockMvc
                .perform(get(ApiUrls.DETTAGLIO_PASCOLI_V1 + "/istruttoria/" + ID_ISTRUTTORIA +"/conesitomantenimento"))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "istruttore", roles = {
            "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
            "a4gistruttoria.pac.istruttoria.du.visualizza.utente"})
    @Sql(scripts = "/sql/dettaglioPascoliControllerTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/dettaglioPascoliControllerTestDelete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getInfoDettaglioPascoliWithDatiIstruttoria() throws Exception {
        MvcResult mvcResult = this.mockMvc
                                  .perform(get(ApiUrls.DETTAGLIO_PASCOLI_V1 + "/istruttoria/" + ID_ISTRUTTORIA +"/condatiistruttoria"))
                                  .andExpect(status().isOk())
                                  .andReturn();

        List<DettaglioPascoli> results = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                                                                new TypeReference<List<DettaglioPascoli>>() {});
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getDatiIstruttoriaPascoli()).isNotNull();
        assertThat(results.get(1).getDatiIstruttoriaPascoli()).isNotNull();
    }

    @Test
    @WithMockUser(username = "istruttore")
    @Sql(scripts = "/sql/dettaglioPascoliControllerTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/dettaglioPascoliControllerTestDelete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getInfoDettaglioPascoliWithDatiIstruttoriaKoPerMancanzaAbilitazioni() throws Exception {
        this.mockMvc.perform(get(ApiUrls.DETTAGLIO_PASCOLI_V1 + "/istruttoria/" + ID_ISTRUTTORIA + "/condatiistruttoria"))
                    .andExpect(status().isForbidden())
                    .andReturn();
    }

}
