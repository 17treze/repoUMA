package it.tndigitale.a4gistruttoria.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.A4gistruttoriaConfigurazione;
import it.tndigitale.a4gistruttoria.dto.AnagraficaAzienda;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CupServiceTest {

	@Autowired
	private CUPService service;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private RestTemplate restTemplate;
	
	@Autowired
	private A4gistruttoriaConfigurazione configurazione;
	
	
	@Test
	public void testElencoUnElementoPZZNDR98A30C794DOK() throws Exception {
		mockAngraficaFascicolo("PZZNDR98A30C794D");
		byte[] elencoInput = Files.readAllBytes(Paths.get("./src/test/resources/cup/investimentoM411.csv"));
		service.generaXMLCup(elencoInput);
		
	}

	@Test
	public void testElencoOK() throws Exception {
		mockAngraficaFascicolo("PZZNDR98A30C794D", "PDESMN94T13C794S", "GSTMTT94M01C794Q", "02522620224", "GRZGLC92B07L378V", "NDRMTT91S18C794L", "MNGPRC95D10C794N", "DLPGRL97S04C794C", "MLFGGC68A28Z401O", "PFFPLP82E10F187N");
		byte[] elencoInput = Files.readAllBytes(Paths.get("./src/test/resources/cup/investimentiM411.csv"));
		service.generaXMLCup(elencoInput);
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
