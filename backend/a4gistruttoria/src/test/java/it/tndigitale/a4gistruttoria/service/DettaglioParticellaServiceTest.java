package it.tndigitale.a4gistruttoria.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.DettaglioParticellaDto;
import it.tndigitale.a4gistruttoria.dto.Pagina;
import it.tndigitale.a4gistruttoria.dto.filter.DettaglioParticellaRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DettaglioParticellaServiceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private DettaglioParticellaService dettaglioParticellaService;
    
    @Autowired DomandeService domandeService;

    @Autowired private MockMvc mockMvc;

    private static final String PROPERTY_ORDINAMENTO = "idParticella";
    private static final Long ID_ISTRUTTORIA = 7777111L;

    @Test
    @Sql(scripts = "/sql/dettaglioParticellaService.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/dettaglioParticellaServiceDelete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDettaglioParticella() throws Exception {
        Pagina<DettaglioParticellaDto> pagina = dettaglioParticellaService.getDettaglioParticellaPaginabile(dettaglioParticellaRequest());

        assertThat(pagina).isNotNull();
        assertThat(pagina.getElementiTotali()).isEqualTo(2);
        assertThat(pagina.getRisultati()).hasSize(2);
        assertThat(pagina.getRisultati().get(0).getInfoCatastali().getIdParticella()).isEqualTo(2619499L);
        assertThat(pagina.getRisultati().get(1).getInfoCatastali().getIdParticella()).isEqualTo(1971119L);
    }


    private DettaglioParticellaRequest dettaglioParticellaRequest() {
        return new DettaglioParticellaRequest().setPaginazione(new Paginazione(20, 0))
                                               .setOrdinamento(new Ordinamento(PROPERTY_ORDINAMENTO, Ordinamento.Ordine.DESC))
                                               .setIdIstruttoria(ID_ISTRUTTORIA);
    }
    
    @Test
    @WithMockUser(username="utente", roles={ "a4gistruttoria.pac.istruttoria.du.visualizza.tutti" })
    public void getDettaglioSuperficiePerCalcolo_OK() throws Exception {
        String idDomandaUnica = "252";
        String idParticella = "3405620";
        String codiceColtura = "160-111-011";
        
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(
        		ApiUrls.DETTAGLIO_PARTICELLA_V1 + "/domanda/" + idDomandaUnica
                + "/particella/" + idParticella + "/codice-coltura/" + codiceColtura));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
			    .andExpect(status().is2xxSuccessful())
			    .andExpect(jsonPath("$[0].idDomanda").exists())
			    .andExpect(jsonPath("$[0].idDomanda").value(idDomandaUnica))
			    .andExpect(jsonPath("$[0].idParcelle[0]").value(266203));
    }
}
