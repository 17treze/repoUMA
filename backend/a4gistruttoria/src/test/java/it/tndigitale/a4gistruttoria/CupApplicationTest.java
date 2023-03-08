package it.tndigitale.a4gistruttoria;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.AnagraficaAzienda;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CupApplicationTest {

	@Autowired
    private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private A4gistruttoriaConfigurazione configurazione;

	@MockBean
	private RestTemplate restTemplate;
	
	@Test
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita" })
	public void generazioneCUPOK() throws Exception {
		mockAngraficaFascicolo("PZZNDR98A30C794D", "PDESMN94T13C794S", "GSTMTT94M01C794Q", "02522620224", "GRZGLC92B07L378V", "NDRMTT91S18C794L", "MNGPRC95D10C794N", "DLPGRL97S04C794C", "MLFGGC68A28Z401O", "PFFPLP82E10F187N");
		byte[] expectedResult = null; // FileUtils.readFileToByteArray(ResourceUtils.getFile(this.getClass().getResource("/stampa/output/stampaJSON-20181017-101853.pdf")));
		File template = ResourceUtils.getFile(this.getClass().getResource("/cup/investimentiM411.csv"));
		MockMultipartFile multipartFile = 
				new MockMultipartFile("file", 
									"investimentiM411.csv", 
									"multipart/form-data", 
									new FileInputStream(template));
		this.mvc.perform(multipart(ApiUrls.CUP_V1)
					.file(multipartFile))
				.andExpect(status().isOk())
				;
				// .andExpect(content().bytes(expectedResult));
		
	}

	protected void mockAngraficaFascicolo(String... cuaa) throws Exception {
		for (String c : cuaa) {
			String resource = configurazione.getUriAgs().concat("fascicoli/").concat(c).concat("/anagrafica");
			Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(resource)), Mockito.eq(AnagraficaAzienda.class))).thenReturn(getFascicoloAnagrafica(c));
		}
	}

	protected AnagraficaAzienda getFascicoloAnagrafica(String cuaa) throws IOException {
		return objectMapper.readValue(new File("src/test/resources/fascicolo/" + cuaa + ".json"), AnagraficaAzienda.class);
	}
}
