package it.tndigitale.a4gistruttoria.component.acs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.DatiCatastaliRegione;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControlloRegioniTest {

	@Autowired
	private ControlloRegioni controlloRegioni;

	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private RestTemplate restTemplate;
	
	@Value("${a4gistruttoria.ags.uri}")
	private String agsUri;

	@Test
	public void testCheckControlloRegioniFalse() throws Exception {
		String codNazionale = "P370";
		DatiCatastaliRegione response = objectMapper.readValue("{\"idRegione\":7,\"denominazione\":\"TRENTINO\",\"codiceIstat\":\"04\"}", DatiCatastaliRegione.class);
		Mockito.when(
				restTemplate.getForObject(
						Mockito.eq(agsUri.concat("daticatastali/sezioni/{codNazionale}/regione")),
						Mockito.eq(DatiCatastaliRegione.class),
						Mockito.eq(codNazionale))
				).thenReturn(response);
		Boolean result = controlloRegioni.checkControlloRegioni(codNazionale, CodiceInterventoAgs.SOIA);
		assertEquals(Boolean.FALSE, result);
	}
	
	@Test
	public void testCheckControlloRegioniTrue() throws Exception {
		String codNazionale = "A370";
		DatiCatastaliRegione response = objectMapper.readValue("{\"idRegione\":11,\"denominazione\":\"LAZIO\",\"codiceIstat\":\"12\"}", DatiCatastaliRegione.class);
		Mockito.when(
				restTemplate.getForObject(
						Mockito.eq(agsUri.concat("daticatastali/sezioni/{codNazionale}/regione")),
						Mockito.eq(DatiCatastaliRegione.class),
						Mockito.eq(codNazionale))
				).thenReturn(response);
		Boolean result = controlloRegioni.checkControlloRegioni(codNazionale, CodiceInterventoAgs.GDURO);
		assertEquals(Boolean.TRUE, result);
	}
}
