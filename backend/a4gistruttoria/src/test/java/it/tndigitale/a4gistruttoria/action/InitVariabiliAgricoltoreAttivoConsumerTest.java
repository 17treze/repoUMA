package it.tndigitale.a4gistruttoria.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.AgricoltoreSIAN;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.CalcoloAccoppiatoHandler;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InitVariabiliAgricoltoreAttivoConsumerTest {

	@Autowired
	private InitVariabiliAgricoltoreAttivoConsumer ivAgricoltoreAttivoCons;

	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private DomandaUnicaDao daoDomanda;
	
	@Value("${a4gistruttoria.proxy.agricoltore.uri}")
	private String agricoltoreAttivoUrl;

    @Autowired
    private IstruttoriaDao istruttoriaDao;
	
	@Test
	@Transactional
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void testInfoAgricoltoreAttivoNonPresente() throws Exception {
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		mockInfoAgricoltoreSIAN("PLOLNZ71B07E565W", this::getResponseAgricoltoreSianNonPresente);
		IstruttoriaModel istruttoria = istruttoriaPerDomanda183109();
		ivAgricoltoreAttivoCons.accept(handler, istruttoria);
		assertEquals(Boolean.FALSE, handler.getVariabiliInput().get(TipoVariabile.INFOAGRATT).getValBoolean());
		assertNull(handler.getVariabiliInput().get(TipoVariabile.AGRATT));
	}

	@Test
	@Transactional
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void testAgricoltoreAttivo() throws Exception {
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		mockInfoAgricoltoreSIAN("PLOLNZ71B07E565W", this::getResponseAgricoltoreSianAttivo);
        IstruttoriaModel istruttoria = istruttoriaPerDomanda183109();
		ivAgricoltoreAttivoCons.accept(handler, istruttoria);
		assertEquals(Boolean.TRUE, handler.getVariabiliInput().get(TipoVariabile.INFOAGRATT).getValBoolean());
		assertEquals(Boolean.TRUE, handler.getVariabiliInput().get(TipoVariabile.AGRATT).getValBoolean());
	}

	@Test
	@Transactional
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void testAgricoltorePresenteNonAttivo() throws Exception {
		CalcoloAccoppiatoHandler handler = new CalcoloAccoppiatoHandler();
		mockInfoAgricoltoreSIAN("PLOLNZ71B07E565W", this::getResponseAgricoltoreSianNonAttivo);
        IstruttoriaModel istruttoria = istruttoriaPerDomanda183109();
		ivAgricoltoreAttivoCons.accept(handler, istruttoria);
		assertEquals(Boolean.TRUE, handler.getVariabiliInput().get(TipoVariabile.INFOAGRATT).getValBoolean());
		assertEquals(Boolean.FALSE, handler.getVariabiliInput().get(TipoVariabile.AGRATT).getValBoolean());
	}
	
	protected void mockInfoAgricoltoreSIAN(String codiceFiscale, Function<String, String> getResponseFunction) throws Exception {
		String jsonRequest = "{ \"codFisc\":\"" + codiceFiscale + "\", \"annoCamp\":2018}";
		String serviceUrlA4GPROXYrecuperoAgricoltoreSian = agricoltoreAttivoUrl + "?params=".concat(URLEncoder.encode(jsonRequest, "UTF-8"));
		AgricoltoreSIAN responseAgricoltoreSian = objectMapper.readValue(getResponseFunction.apply(codiceFiscale), AgricoltoreSIAN.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlA4GPROXYrecuperoAgricoltoreSian)), Mockito.eq(AgricoltoreSIAN.class))).thenReturn(responseAgricoltoreSian);
		
	}

	protected String getResponseAgricoltoreSianNonPresente(String s) {
		return getResponseAgricoltoreSian("infoAgricoltoreAttivoNonPresente");
	}

	protected String getResponseAgricoltoreSianAttivo(String s)  {
		return getResponseAgricoltoreSian("agricoltoreAttivo");
	}

	protected String getResponseAgricoltoreSianNonAttivo(String s) {
		return getResponseAgricoltoreSian("agricoltoreNonAttivo");
	}

	protected String getResponseAgricoltoreSian(String s)  {
		try {
			JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/" + s + ".json"));
			return objectMapper.writeValueAsString(response);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

    private IstruttoriaModel istruttoriaPerDomanda183109() {
        DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
        return istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.DISACCOPPIATO, TipoIstruttoria.ANTICIPO);
    }
}

