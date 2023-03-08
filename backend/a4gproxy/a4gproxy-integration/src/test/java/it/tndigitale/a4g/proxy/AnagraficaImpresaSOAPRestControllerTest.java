package it.tndigitale.a4g.proxy;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ws.test.client.MockWebServiceServer;

import it.tndigitale.a4g.proxy.services.AnagraficaImpresaServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class AnagraficaImpresaSOAPRestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private AnagraficaImpresaServiceImpl anagraficaImpresaServiceImpl;
	
	private MockWebServiceServer mockServer;

	@Before
	public void init() {
		mockServer = MockWebServiceServer.createServer(anagraficaImpresaServiceImpl);
	}
	
	@Test
	public void getAnagraficaImpresa() throws Exception {
		Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getAnagraficaImpresaRequest.xml"));
		Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getAnagraficaImpresaResponse.xml"));
		mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
		this.mockMvc.perform(get("/api/v1/anagraficaimpresa/TRRCST78B08C794X")).andExpect(content().string(containsString("\"esito\":\"OK\"")));
		mockServer.verify();
	}

	@Test
	public void getAnagraficaImpresaNoContent() throws Exception {
		Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getAnagraficaImpresaNoContentRequest.xml"));
		Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getAnagraficaImpresaNoContentResponse.xml"));
		mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
		this.mockMvc.perform(get("/api/v1/anagraficaimpresa/XXXXXXXXXXXCXXXX"))
				.andExpect(content().string(containsString("\"tipo\":\"IMP_occorrenza_0\",\"msgerr\":\"NESSUNA IMPRESA TROVATA\"")));
		mockServer.verify();		
	}

	@Test
	public void getDettaglioCompletoImpresa() throws Exception {
		Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getDettaglioCompletoImpresaRequest.xml"));
		Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getDettaglioCompletoImpresaResponse.xml"));
		mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
		this.mockMvc.perform(get("/api/v1/anagraficaimpresa/dettagliocompleto").param("params", "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"173710\"}"))
				.andExpect(content().string(containsString("\"esito\":\"OK\"")));
		mockServer.verify();
	}

	@Test
	public void getDettaglioCompletoImpresaNoContent() throws Exception {
		Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getDettaglioCompletoImpresaNoContentRequest.xml"));
		Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getDettaglioCompletoImpresaNoContentResponse.xml"));
		mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
		this.mockMvc.perform(get("/api/v1/anagraficaimpresa/dettagliocompleto").param("params", "{\"provinciaSede\":\"TN\",\"numeroREASede\":\"000000\"}"))
				.andExpect(content().string(containsString("\"tipo\":\"IMP_occorrenza_0\",\"msgerr\":\"NESSUNA IMPRESA TROVATA\"")));
		mockServer.verify();
	}
	
	@Test
	public void getPersona() throws Exception {
		Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getPersonaRequest.xml"));
		Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getPersonaResponse.xml"));
		mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
		this.mockMvc.perform(get("/api/v1/anagraficaimpresa/dettagliPersone/TRRCST78B08C794X")).andExpect(content().string(containsString("\"esito\":\"OK\"")));
		mockServer.verify();
	}

	@Test
	public void getPersonaNoContent() throws Exception {
		Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getPersonaNoContentRequest.xml"));
		Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getPersonaNoContentResponse.xml"));
		mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
		this.mockMvc.perform(get("/api/v1/anagraficaimpresa/dettagliPersone/XXXXXXXXXXXXXXXX"))
				.andExpect(content().string(containsString("\"tipo\":\"PER_occorrenza_0\",\"msgerr\":\"NESSUNA PERSONA TROVATA\"")));
		mockServer.verify();
	}
	
	@Test
	public void getPersonaFisica() throws Exception {
		Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getAnagraficaImpresaRequest.xml"));
		Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getAnagraficaImpresaResponse.xml"));
		Source requestDettaglioCompletoPayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getDettaglioCompletoImpresaRequest.xml"));
		Source responseDettaglioCompletoPayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/getDettaglioCompletoImpresaResponse.xml"));
		mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
		mockServer.expect(payload(requestDettaglioCompletoPayload)).andRespond(withPayload(responseDettaglioCompletoPayload));
		
		String jsonResponseContent = new String(Files.readAllBytes(Paths.get("src/test/resources/anagraficaimpresa/expectedGetPersonaFisicaV2Ok.json")));
		this.mockMvc.perform(get("/api/v2/anagraficaimpresa/personafisica/TRRCST78B08C794X"))
			.andExpect(status().isOk())
			.andExpect(content().json(jsonResponseContent));
		mockServer.verify();
	}

	@Test
	public void getPersonaFisicaErrorNessunaImpresa() throws Exception {
		Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/PersonaFisica_getAnagraficaImpresaNoContentRequest.xml"));
		Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/PersonaFisica_getAnagraficaImpresaNoContentResponse.xml"));
		mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
		this.mockMvc.perform(get("/api/v2/anagraficaimpresa/personafisica/XXXXXXXXXXXXXXXX")).andExpect(status().isNoContent());
		mockServer.verify();
	}
	
	@Test
	public void getPersonaGiuridica() throws Exception {
		Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/PersonaGiuridica_getAnagraficaImpresaRequest.xml"));
		Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/PersonaGiuridica_getAnagraficaImpresaResponse.xml"));
		Source requestDettaglioCompletoPayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/PersonaGiuridica_getDettaglioCompletoImpresaRequest.xml"));
		Source responseDettaglioCompletoPayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/PersonaGiuridica_getDettaglioCompletoImpresaResponse.xml"));
		mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
		mockServer.expect(payload(requestDettaglioCompletoPayload)).andRespond(withPayload(responseDettaglioCompletoPayload));
		
		String jsonResponseContent = new String(Files.readAllBytes(Paths.get("src/test/resources/anagraficaimpresa/ExpectedGetPersonaGiuridicaV2Ok-new.json")));
		this.mockMvc.perform(get("/api/v2/anagraficaimpresa/personagiuridica/00123890220"))
			.andExpect(status().isOk())
			.andExpect(content().json(jsonResponseContent));
		mockServer.verify();
	}

	@Test
	public void getPersonaGiuridicaNessunaImpresa() throws Exception {
		Source requestPayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/PersonaGiuridica_getAnagraficaImpresaNoContentRequest.xml"));
		Source responsePayload = new StreamSource(new FileReader("src/test/resources/anagraficaimpresa/PersonaGiuridica_getAnagraficaImpresaNoContentResponse.xml"));
		mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
		
		this.mockMvc.perform(get("/api/v2/anagraficaimpresa/personagiuridica/XXXXXXXXXXX"))
			.andExpect(status().isNoContent());
		mockServer.verify();
	}
}
