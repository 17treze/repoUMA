package it.tndigitale.a4gistruttoria.component.acs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControlloRegistroOlioNazionaleTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
	private RestTemplate restTemplate;

    @Autowired
    private ControlloRegistroOlioNazionale controlloRegistroOlioNazionale;
	
	@Test
	public void testCheckControlloRegistroOlioNazionaleFalseCuaa() throws Exception {
		String cuaaIntestatario = "nonExistent";
		Integer annoCampagna = 2017;
		Boolean result = controlloRegistroOlioNazionale.checkRegistroOlioNazionale(cuaaIntestatario, annoCampagna);
		assertFalse(result);
	}
	
	@Test
	public void testCheckControlloRegistroOlioNazionaleTrue() throws Exception {
		String cuaaIntestatario = "CLLLCU76M05C794R";
		Integer annoCampagna = 2018;
		Boolean result = controlloRegistroOlioNazionale.checkRegistroOlioNazionale(cuaaIntestatario, annoCampagna);
		assertTrue(result);
	}
	
	@Test
	public void testCheckControlloRegistroOlioNazionaleFalseAnno() throws Exception {
		String cuaaIntestatario = "CLLLCU76M05C794R";
		Integer annoCampagna = 2015;
		Boolean result = controlloRegistroOlioNazionale.checkRegistroOlioNazionale(cuaaIntestatario, annoCampagna);
		assertFalse(result);
	}
}
