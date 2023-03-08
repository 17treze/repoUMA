package it.tndigitale.a4gutente.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

//	TODO webEnvironment viene utilizzato per attivare la console web di h2 (via property non funziona) 
@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"spring.h2.console.enabled=true"})
@SpringBootTest
@AutoConfigureMockMvc
public class ProtocollaDomandaRegistrazioneUtenteControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RestTemplate restTemplate;
	
	@Value("${a4gutente.integrazioni.uri}")
	private String a4gproxyUri;

	@Value("${a4gutente.integrazioni.protocollo.path}")
	private String protocolloUriPath;
	

	@Test
//	@Ignore
	@WithMockUser("TRRCST78B08C794X")
	@Transactional
	public void protocollazioneDomandaFirmataOk() throws Exception {
		ResponseEntity<String> response = new ResponseEntity<>("1235412", HttpStatus.ACCEPTED);
		Mockito.when(restTemplate.exchange(Mockito.eq(a4gproxyUri + protocolloUriPath), Mockito.eq(HttpMethod.POST), Mockito.any(), Mockito.eq(String.class)))
				.thenReturn(response);
		
		ResultActions resultActions = 
				mockMvc.perform(put(ApiUrls.DOMANDE_V1 + "/200/protocolla"));
		
		// verifico il risultato
		resultActions.andDo(print()).andExpect(status().isOk());
	}
}
