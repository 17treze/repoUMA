package it.tndigitale.a4g.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.proxy.dto.DatiPagamentiDto;
import it.tndigitale.a4g.proxy.dto.SuperficiAccertateDto;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.AppoSupeAccertDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.AziendeCollegateAntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.DichiarazioneAntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.PagamentiDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.SoggettiAntimafiaDao;
import it.tndigitale.a4g.proxy.services.EmailService;
import it.tndigitale.a4g.proxy.services.SincronizzazioneService;

@RunWith(SpringRunner.class)
@SpringBootTest
@SqlGroup({ 
	@Sql({ "classpath:sincronizzazione/schema.sql" }),
	@Sql({ "classpath:sincronizzazione/data.sql" })
})
public class SincronizzazioneServiceTest {
	
	@Autowired
	private DichiarazioneAntimafiaDao dichiarazioneAntimafiaDao;
	@Autowired
	private SoggettiAntimafiaDao soggettiAntimafiaDao;
	@Autowired
	private AziendeCollegateAntimafiaDao aziendeCollegateAntimafiaDao;
	@Autowired
    SincronizzazioneService sincronizzazioneService;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private EmailService emailService;
	@Autowired
	private AppoSupeAccertDao appoSupeAccertDao;
	@Autowired
	private PagamentiDao pagamentiDao;
	
	@Test
	public void testInvioDatiPersonaGiuridicaOK() throws IOException, Exception {
		Future<String> response = sincronizzazioneService.dichiarazioneAntimafia(getRequestPersonaGiuridicaOK());
		assertEquals(response.get(), "OK");//response.get() aspetta il completamento dell'elaborazione
		assertTrue(dichiarazioneAntimafiaDao.findById(8178l).isPresent());
		assertTrue(aziendeCollegateAntimafiaDao.count()==1);
		assertTrue(soggettiAntimafiaDao.count()==2);
	}

	@Test(expected = ExecutionException.class)
	public void testInvioDatiPersonaGiuridicaKO() throws IOException, Exception {
		Mockito.doNothing().when(emailService).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Future<String> response = sincronizzazioneService.dichiarazioneAntimafia(getRequestPersonaGiuridicaKO());
		assertEquals("KO",response.get());//response.get() aspetta il completamento dell'elaborazione
	}
	
	@Test
	public void testInvioDatiRequestDittaIndividualeOK() throws IOException, Exception {
		Future<String>  response = sincronizzazioneService.dichiarazioneAntimafia(getRequestDittaIndividualeOK());
		assertEquals(response.get(), "OK");//response.get() aspetta il completamento dell'elaborazione
		assertTrue(dichiarazioneAntimafiaDao.findById(447l).isPresent());
		assertTrue(aziendeCollegateAntimafiaDao.count()==0);
		assertTrue(soggettiAntimafiaDao.count()==1);		
	}
	
	@Test
	public void testInvioDatiRequestDittaIndividualeConDirettoreOK() throws IOException, Exception {
		Future<String>  response = sincronizzazioneService.dichiarazioneAntimafia(getRequestDittaIndividualeConDirettoreOK());
		assertEquals(response.get(), "OK");//response.get() aspetta il completamento dell'elaborazione
		assertTrue(dichiarazioneAntimafiaDao.findById(7945l).isPresent());
		assertTrue(aziendeCollegateAntimafiaDao.count()==0);
		assertTrue(soggettiAntimafiaDao.count()==2);		
	}
	
	@Test
	public void testPersonaGiuridicaEaziendeCollegate() throws IOException, Exception {
		Future<String> response = sincronizzazioneService.dichiarazioneAntimafia(getJsonRequest("requestPersonaGiuridicaEaziendeCollegate"));
		assertEquals(response.get(), "OK");//response.get() aspetta il completamento dell'elaborazione
		assertTrue(dichiarazioneAntimafiaDao.findById(10237l).isPresent());
		assertTrue(aziendeCollegateAntimafiaDao.count()==3);
		assertTrue(soggettiAntimafiaDao.count()==119);
	}
	
	@Test
	public void testCreaSuperficiAccertateOK() throws Exception {
		sincronizzazioneService.creaSuperficiAccertate(getSuperficiAccertate(2018l));
		sincronizzazioneService.creaSuperficiAccertate(getSuperficiAccertate(2018l));
		sincronizzazioneService.creaSuperficiAccertate(getSuperficiAccertate(2018l));
		assertTrue(appoSupeAccertDao.count() == 3);
		sincronizzazioneService.creaSuperficiAccertate(getSuperficiAccertate(2019l));
		assertTrue(appoSupeAccertDao.count() == 4);
		sincronizzazioneService.pulisciSuperficiAccertate(2018l);
		assertTrue(appoSupeAccertDao.count() == 1);
		sincronizzazioneService.pulisciSuperficiAccertate(2019l);
		assertTrue(appoSupeAccertDao.count() == 0);
	}
	
	@Test
	public void testCreaDatiPagamentiOK() throws Exception {
		sincronizzazioneService.creaDatiPagamenti(getDatiPagamenti(2018l));
		sincronizzazioneService.creaDatiPagamenti(getDatiPagamenti(2018l));
		sincronizzazioneService.creaDatiPagamenti(getDatiPagamenti(2018l));
		assertTrue(pagamentiDao.count() == 3);
		sincronizzazioneService.creaDatiPagamenti(getDatiPagamenti(2019l));
		assertTrue(pagamentiDao.count() == 4);
		sincronizzazioneService.pulisciDatiPagamenti(2018l);
		assertTrue(pagamentiDao.count() == 1);
		sincronizzazioneService.pulisciDatiPagamenti(2019l);
		assertTrue(pagamentiDao.count() == 0);
	}
	
	private String getJsonRequest(String fileName) throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/sincronizzazione/".concat(fileName).concat(".json")));
		return objectMapper.writeValueAsString(response);
	}
	
	private String getRequestPersonaGiuridicaOK() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/sincronizzazione/requestPersonaGiuridicaOK.json"));
		return objectMapper.writeValueAsString(response);
	}
	private String getRequestPersonaGiuridicaKO() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/sincronizzazione/requestPersonaGiuridicaKO.json"));
		return objectMapper.writeValueAsString(response);
	}	
	private String getRequestDittaIndividualeOK() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/sincronizzazione/requestDittaIndividualeOK.json"));
		return objectMapper.writeValueAsString(response);
	}
	private String getRequestDittaIndividualeConDirettoreOK() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/sincronizzazione/requestDittaIndividualeConDirettoreOK.json"));
		return objectMapper.writeValueAsString(response);
	}	
	private SuperficiAccertateDto getSuperficiAccertate(Long anno) throws Exception {
		return objectMapper.readValue("{\"annoCampagna\":".concat(anno.toString()).concat(",\"cuaa\":\"cuaatest\",\"identificativoDomanda\":\"123456\",\"motivazioneA1\":\"SI\",\"motivazioneA2\":\"SI\",\"motivazioneA3\":\"NO\",\"motivazioneB0\":\"SI\",\"superficieAccertata\":123.11,\"superficieDeterminata\":665.14}"), SuperficiAccertateDto.class);
	}
	private DatiPagamentiDto getDatiPagamenti(Long anno) throws Exception {
		return objectMapper.readValue("{\"annoCampagna\":".concat(anno.toString()).concat(",\"codiceIntervento\":132,\"cuaa\":\"CCCDDD123D5D5111\",\"importoDeterminato\":10.11,\"importoLiquidato\":12.05,\"importoRichiesto\":45.23,\"numeroDomanda\":\"123456\",\"numeroProgressivoLavorazione\":111223,\"pagamentoAutorizzato\":true}"), DatiPagamentiDto.class);
	}
}
