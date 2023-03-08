package it.tndigitale.a4gistruttoria.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.component.StampaComponent;
import it.tndigitale.a4gistruttoria.service.businesslogic.verbale.VerbaleLiquidazioneDisaccoppiatoService;

@RunWith(SpringRunner.class)
@SpringBootTest
// @AutoConfigureTestDatabase
public class StampaVerbaleLiquidazioneDisaccoppiatoTests {
	
	@Autowired
	private VerbaleLiquidazioneDisaccoppiatoService service;
	@MockBean
	private StampaComponent serviceStampa;
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/domanda_verbale_liquidazione_disaccoppiato_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/intersostegno/domanda_verbale_liquidazione_disaccoppiato_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita" })
	public void domandePerVerbaleLiquidazioneDisaccoppiato() throws Exception {
		byte[] expectedResult = Files.readAllBytes(Paths.get("./src/test/resources/DomandaUnica/intersostegno/stampa/output/verbaleLiquidazioneDisaccoppiato.pdf"));
		JsonNode inputData = objectMapper.readTree(new File("src/test/resources/DomandaUnica/intersostegno/stampa/json/verbaleLiquidazioneDisaccoppiato.json"));
		String dati = objectMapper.writeValueAsString(inputData);
		String template = "template/verbaleLiquidazioneDomandaUnica.docx";
		Mockito.when(serviceStampa.stampaPDF_A(Mockito.eq(dati), Mockito.eq(template))).thenReturn(expectedResult);
		
		service.stampa((long) 9999999);
		
		Mockito.verify(serviceStampa, Mockito.times(1)).stampaPDF_A(dati, template);
	}

}