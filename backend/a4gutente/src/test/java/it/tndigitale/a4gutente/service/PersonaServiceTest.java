package it.tndigitale.a4gutente.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityNotFoundException;

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

import it.tndigitale.a4gutente.dto.Persona;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonaServiceTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private PersonaService service;

	@Test
	public void personaConPrivacyRestituisceDatiCompleti() throws Exception {
		Long id = 249L;
		Persona persona = service.caricaPersona(id);
		assertNotNull(persona);
		assertEquals("BRDMRN71L08C794F", persona.getCodiceFiscale());
		assertNotNull(persona.getNrProtocolloPrivacyGenerale());
		assertTrue(!persona.getNrProtocolloPrivacyGenerale().isEmpty());
	}

	@Test
	public void personaSenzaPrivacyRestituisceSoloDatiAnagrafici() throws Exception {
		Long id = 250L;
		Persona persona = service.caricaPersona(id);
		assertNotNull(persona);
		assertEquals("TRRCST78B08C794X", persona.getCodiceFiscale());
		assertNull(persona.getNrProtocolloPrivacyGenerale());
	}

	@Test
	public void personaInesistenteDaErrore() throws Exception {
		Long id = 3L;
		try {
			Persona persona = service.caricaPersona(id);
			assertTrue(false);
		} catch (EntityNotFoundException e) {
			assertTrue(true);
		}
	}
}
