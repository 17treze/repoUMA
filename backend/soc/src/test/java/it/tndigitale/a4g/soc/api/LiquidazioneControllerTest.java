package it.tndigitale.a4g.soc.api;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import org.assertj.core.api.Assertions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import it.tndigitale.a4g.soc.business.dto.ImportoLiquidato;
import it.tndigitale.a4g.soc.utility.JsonSupport;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/customsql/liquidazione.sql")
public class LiquidazioneControllerTest {

    private static final String DOMANDA_PSR_VISUALIZZA= "a4g.ags.domanda.psr.visualizza";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    @WithMockUser(username="FRSLBT76H42E625Z", roles = { DOMANDA_PSR_VISUALIZZA })
    public void getImportiLiquidazioneTestIntegration() throws Exception {
    	String urlQuery = ApiUrls.LIQUIDAZIONE_V1 + "?cuaa=BRTMRA56H20H612O&numeroDomanda=1888&anno=2015&tipoDomanda=DOMANDA_UNICA&idElencoLiquidazione=712";
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(urlQuery))
                                           .andDo(MockMvcResultHandlers.print())
                                           .andExpect(MockMvcResultMatchers.status().isOk())
                                           .andReturn();

        String resultString = mvcResult.getResponse().getContentAsString();
        List<ImportoLiquidato> importoLiquidatoList = JsonSupport.toList(resultString, ImportoLiquidato[].class);
        Assertions.assertThat(importoLiquidatoList.size()).isEqualTo(1);
        ImportoLiquidato importoLiquidato = importoLiquidatoList.get(0);
        Assertions.assertThat(importoLiquidato).isNotNull();
        Assertions.assertThat(importoLiquidato.getTipoBilancio()).isEqualTo("FF");
        Assertions.assertThat(importoLiquidato.getProgressivo()).isEqualTo(10665L);
        Assertions.assertThat(importoLiquidato.getIncassatoNetto()).isEqualTo(BigDecimal.valueOf(196.86d));
        Assertions.assertThat(importoLiquidato.getAnno()).isEqualTo(2015);
        int compareTo = importoLiquidato.getTotaleRecuperato().compareTo(BigDecimal.valueOf(35d));
        Assertions.assertThat(compareTo).isZero();
        Assertions.assertThat(importoLiquidato.getDebiti()).hasSize(2);
    }

    @Test
    @Transactional
    @WithMockUser(username="FRSLBT76H42E625Z", roles = { DOMANDA_PSR_VISUALIZZA })
    public void getImportiLiquidazioneTestIntegrationWithNoResults() throws Exception {
    	String urlQuery = ApiUrls.LIQUIDAZIONE_V1 + "?cuaa=BRTMRA56H20H612O&numeroDomanda=1988&anno=2015&tipoDomanda=DOMANDA_UNICA&idElencoLiquidazione=712";
    	final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(urlQuery))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isNoContent())
               .andReturn();
        String resultString = mvcResult.getResponse().getContentAsString();
        List<ImportoLiquidato> importoLiquidatoList = JsonSupport.toList(resultString, ImportoLiquidato[].class);
        Assertions.assertThat(importoLiquidatoList.size()).isEqualTo(0);
    }
    
    @Test
    @Transactional
    @WithMockUser(username="FRSLBT76H42E625Z", roles = { DOMANDA_PSR_VISUALIZZA })
    public void getImportiLiquidazionePsrStrutturaleTestIntegration() throws Exception {
    	String urlQuery = ApiUrls.LIQUIDAZIONE_V1 + "?cuaa=BRTMRA56H20H612O&numeroDomanda=13144&anno=2015&tipoDomanda=DOMANDA_PSR_STRUTTURALE";
    	MockHttpServletRequestBuilder urlRequestBuilder = MockMvcRequestBuilders.get(urlQuery);
        final MvcResult mvcResult = mockMvc.perform(urlRequestBuilder)
                                           .andDo(MockMvcResultHandlers.print())
                                           .andExpect(MockMvcResultMatchers.status().isOk())
                                           .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String resultString = response.getContentAsString();
        List<ImportoLiquidato> importoLiquidatoList = JsonSupport.toList(resultString, ImportoLiquidato[].class);
        Assertions.assertThat(importoLiquidatoList.size()).isEqualTo(1);
        ImportoLiquidato importoLiquidato = importoLiquidatoList.get(0);
        Assertions.assertThat(importoLiquidato).isNotNull();
        Assertions.assertThat(importoLiquidato.getTipoBilancio()).isEqualTo("FF");
        Assertions.assertThat(importoLiquidato.getProgressivo()).isEqualTo(10665L);
        Assertions.assertThat(importoLiquidato.getIncassatoNetto()).isEqualTo(BigDecimal.valueOf(196.86d));
        Assertions.assertThat(importoLiquidato.getAnno()).isEqualTo(2015);
        int compareTo = importoLiquidato.getTotaleRecuperato().compareTo(BigDecimal.valueOf(35d));
        Assertions.assertThat(compareTo).isZero();
        Assertions.assertThat(importoLiquidato.getDebiti()).hasSize(2);
    }
    
}
