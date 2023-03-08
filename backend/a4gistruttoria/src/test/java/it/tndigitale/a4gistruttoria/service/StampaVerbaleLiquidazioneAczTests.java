package it.tndigitale.a4gistruttoria.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.api.ElencoLiquidazioneController;
import it.tndigitale.a4gistruttoria.component.StampaComponent;
import it.tndigitale.a4gistruttoria.service.businesslogic.verbale.VerbaleLiquidazioneZootecniaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
public class StampaVerbaleLiquidazioneAczTests {
	
	@Autowired
	private VerbaleLiquidazioneZootecniaService service;
	
	@MockBean
	private StampaComponent serviceStampa;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@Transactional
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita" })
	@Sql(scripts = { "/DomandaUnica/intersostegno/domanda_verbale_liquidazione_acz.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/domanda_verbale_liquidazione_acz_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void domandePerVerbaleLiquidazioneAcz() throws Exception {
		byte[] expectedResult = Files.readAllBytes(Paths.get("./src/test/resources/DomandaUnica/intersostegno/stampa/output/verbaleLiquidazioneAcz.pdf"));
		JsonNode inputData = objectMapper.readTree(new File("src/test/resources/DomandaUnica/intersostegno/stampa/json/verbaleLiquidazioneAcz.json"));
		String dati = objectMapper.writeValueAsString(inputData);
		String template = "template/verbaleLiquidazioneDomandaUnica.docx";
        Mockito.when(serviceStampa.stampaPDF_A(Mockito.eq(dati), Mockito.eq(template))).thenReturn(expectedResult);
		
		service.stampa((long) 9999999);

		Mockito.verify(serviceStampa, Mockito.times(1)).stampaPDF_A(dati, template);
	}
	
	@Test
	@Transactional
	@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.istruttoria.du.visualizza.tutti",
			"a4gistruttoria.pac.istruttoria.du.visualizza.utente", "a4gistruttoria.pac.istruttoria.edita" })
	@Sql(scripts = { "/DomandaUnica/intersostegno/domanda_verbale_liquidazione_acz.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/domanda_verbale_liquidazione_acz_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void servizioVerbaleElencoLiquidazioneOk() throws Exception {
		String url = ApiUrls.ELENCO_LIQUIDAZIONE_ISTRUTTORIA_DU_V1 + "/9999999" + ElencoLiquidazioneController.VERBALE_PATH;
		byte[] expectedContent = Files.readAllBytes(Paths.get("./src/test/resources/DomandaUnica/intersostegno/stampa/output/verbaleLiquidazioneAcz.pdf"));
		this.mockMvc.perform(get(url))
			.andExpect(status().isOk())
			.andExpect(content().bytes(expectedContent));
	}

}