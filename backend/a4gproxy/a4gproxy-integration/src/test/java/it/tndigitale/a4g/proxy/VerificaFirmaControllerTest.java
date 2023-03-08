package it.tndigitale.a4g.proxy;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;
import org.springframework.ws.test.client.MockWebServiceServer;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.proxy.dto.InfoVerificaFirma;
import it.tndigitale.a4g.proxy.services.VerificaFirmaServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
public class VerificaFirmaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private VerificaFirmaServiceImpl verificaFirmaService;

	@Autowired
	private ObjectMapper objectMapper;
	
	private MockWebServiceServer mockServer;

	@Before
	public void init() {
		mockServer = MockWebServiceServer.createServer(verificaFirmaService);
	}

	@Test
	public void verificaFirma() throws Exception {
		MockMultipartFile firstFile = new MockMultipartFile("documentoFirmato", Files.readAllBytes(Paths.get("./src/test/resources/verificafirma/verificaFirmaFile.xml.p7m")));
		Source requestPayload = new StreamSource(new FileReader("src/test/resources/verificafirma/verificaFirmaRequest.xml"));
		Source responsePayload = new StreamSource(new FileReader("src/test/resources/verificafirma/verificaFirmaResponse.xml"));
		mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/verificafirma").file(firstFile)).andExpect(status().isOk());
		mockServer.verify();
	}

	@Test(expected = NestedServletException.class)
	public void verificaFirmaNoPKCS() throws Exception {
		MockMultipartFile firstFile = new MockMultipartFile("documentoFirmato", Files.readAllBytes(Paths.get("./src/test/resources/verificafirma/verificaFirmaNoPKCSFile.xml")));
		Source requestPayload = new StreamSource(new FileReader("src/test/resources/verificafirma/verificaFirmaNoPKCSRequest.xml"));
		Source responsePayload = new StreamSource(new FileReader("src/test/resources/verificafirma/verificaFirmaNoPKCSResponse.xml"));
		mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/verificafirma").file(firstFile)).andExpect(status().isBadRequest())
		.andExpect(content().string(containsString("Il file non contiene una busta PKCS#7")));
		mockServer.verify();
	}

	@Test
	public void verificaFirmaSingola() throws Exception {
		MockMultipartFile firstFile = new MockMultipartFile("documentoFirmato", Files.readAllBytes(Paths.get("./src/test/resources/verificafirma/verificaFirmaFile.xml.p7m")));
		Source requestPayload = new StreamSource(new FileReader("src/test/resources/verificafirma/verificaFirmaRequest.xml"));
		Source responsePayload = new StreamSource(new FileReader("src/test/resources/verificafirma/verificaFirmaResponse.xml"));
		mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
		String infoVerificaFirmaString = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/verificafirma/singola/ZNECLD69E10H612N").file(firstFile)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		InfoVerificaFirma actualValue = objectMapper.readValue(infoVerificaFirmaString, InfoVerificaFirma.class);
		assertEquals(LocalDate.of(2017, 11, 30), actualValue.getDataFirma());
		assertEquals("ZNECLD69E10H612N", actualValue.getCfFirmatario());
		mockServer.verify();
	}

	@Test
	public void verificaFirmaSingolaException() throws Exception {
		MockMultipartFile firstFile = new MockMultipartFile("documentoFirmato", Files.readAllBytes(Paths.get("./src/test/resources/verificafirma/verificaFirmaFile.xml.p7m")));
		Source requestPayload = new StreamSource(new FileReader("src/test/resources/verificafirma/verificaFirmaRequest.xml"));
		Source responsePayload = new StreamSource(new FileReader("src/test/resources/verificafirma/verificaFirmaResponse.xml"));
		mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/verificafirma/singola/AAABBB11C22D333E").file(firstFile))
		.andExpect(status().is4xxClientError());
	}

}
