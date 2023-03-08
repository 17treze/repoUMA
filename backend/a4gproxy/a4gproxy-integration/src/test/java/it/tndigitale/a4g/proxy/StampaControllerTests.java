package it.tndigitale.a4g.proxy;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.proxy.dto.FormatoStampa;
import it.tndigitale.a4g.proxy.services.StampaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
public class StampaControllerTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private StampaService service;

	@Test
	public void dovrebbeProdurreUnaStampa() throws Exception {
		byte[] expectedResult = FileUtils.readFileToByteArray(ResourceUtils.getFile(this.getClass().getResource("/stampa/output/stampaJSON-20181017-101853.pdf")));
		File template = ResourceUtils.getFile(this.getClass().getResource("/stampa/template/templateJSonRichiestaDomandaNuovoUtente.docx"));
		MockMultipartFile multipartFile = 
				new MockMultipartFile("file", 
						"templateJSonRichiestaDomandaNuovoUtente.docx", 
						"multipart/form-data", 
						new FileInputStream(template));
		JsonNode inputData = objectMapper.readTree(new File("src/test/resources/stampa/json/domandaNuovoUtenteElisabettaFreschi.json"));
		String dati = objectMapper.writeValueAsString(inputData);
		Mockito.when(service.stampaJSON2PDF(Mockito.eq(dati), any(InputStream.class))).thenReturn(expectedResult);
		this.mvc.perform(multipart("/api/v1/stampa")
				.file(multipartFile)
				.param("dati", dati)
				.param("formatoStampa", FormatoStampa.PDF.name()))
		.andExpect(status().isOk())
		.andExpect(content().bytes(expectedResult));
	}
}
