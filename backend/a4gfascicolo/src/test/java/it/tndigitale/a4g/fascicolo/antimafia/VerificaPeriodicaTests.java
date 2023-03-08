package it.tndigitale.a4g.fascicolo.antimafia;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.function.Supplier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.antimafia.dto.AggiornaDichiarazioneEsito;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Dichiarazione;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DichiarazioneFilter;
import it.tndigitale.a4g.fascicolo.antimafia.service.AntimafiaServiceImpl;
import it.tndigitale.a4g.fascicolo.antimafia.service.PassaggioVerificaPeriodicaService;
import it.tndigitale.a4g.framework.client.ClientServiceBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"spring.h2.console.enabled=true"}) //attiva console h2 localmente
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class VerificaPeriodicaTests {
	
	@Value("${a4gistruttoria.proxy.uri}")
	private String a4gproxyUrl;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private RestTemplate restTemplate;
    
    @SpyBean
    PassaggioVerificaPeriodicaService passaggioVerificaPeriodicaService;

    @MockBean
    private ClientServiceBuilder clientServiceBuilder;

    @MockBean
    AntimafiaServiceImpl antimafiaService;
    
    private String getDichiarazioniAntimafia() throws Exception {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/passaggioVerificaPeriodica/dichiarazioniAntimafia.json"));
		return objectMapper.writeValueAsString(response);
	}

    
//    @Captor
//    private ArgumentCaptor<PassaggioVerificaPeriodicaService> passaggioVerificaPeriodicaServiceArgumentCaptor;
    
	@Test(expected = Test.None.class /* no exception expected */)
	@WithMockUser("UTENTEAPPAG")
	public void passaggioVerificaPeriodica() throws Exception {
		Mockito.when(clientServiceBuilder.buildWith(ArgumentMatchers.any(Supplier.class))).thenReturn(restTemplate);
		
		// get sincronizzazione
		JsonNode responseSinc = objectMapper.readTree(new File("src/test/resources/passaggioVerificaPeriodica/sincAntimafia.json"));
		Mockito.doReturn(objectMapper.writeValueAsString(responseSinc)).when(restTemplate).getForObject(Mockito.eq(new URI(a4gproxyUrl.concat("sincronizzazione/antimafia/").concat("18"))),
				Mockito.eq(String.class));

		List<Dichiarazione> dichiarazioni = objectMapper.readValue(getDichiarazioniAntimafia(), new TypeReference<List<Dichiarazione>>() {
		});
		
		Mockito.when(antimafiaService.getDichiarazioni(ArgumentMatchers.any(DichiarazioneFilter.class))).thenReturn(dichiarazioni);
		AggiornaDichiarazioneEsito aggiornaDichiarazioneEsito = new AggiornaDichiarazioneEsito();
		Mockito.when(antimafiaService.aggiornaDichiarazione(ArgumentMatchers.any(Dichiarazione.class))).thenReturn(aggiornaDichiarazioneEsito);
		Mockito.when(restTemplate.exchange(Mockito.contains("a4gproxy"), Mockito.eq(HttpMethod.PUT), Mockito.any(), Mockito.eq(String.class))).thenReturn(new ResponseEntity<String>(HttpStatus.OK));

		passaggioVerificaPeriodicaService.passaggioVerificaPeriodica();
		Mockito.verify(passaggioVerificaPeriodicaService, Mockito.timeout(1000).times(1)).passaggioVerificaPeriodica();
		System.out.println("END");
	}

}
