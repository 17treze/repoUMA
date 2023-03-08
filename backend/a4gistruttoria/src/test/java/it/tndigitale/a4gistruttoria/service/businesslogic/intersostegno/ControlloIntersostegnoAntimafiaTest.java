package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.MockIoItalia;
import it.tndigitale.a4gistruttoria.dto.DomandaCollegata;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = {"a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita"})
public class ControlloIntersostegnoAntimafiaTest extends MockIoItalia {
	
	 @MockBean
	 private RestTemplate restTemplate;
	 
	 @Autowired
	 private IstruttoriaDao istruttoriaDao;
	 
	 @Autowired
	 private DomandaUnicaDao daoDomanda;
	 
	 @Autowired
		private ObjectMapper objectMapper;
	 
	 @Autowired
	 private VerificaEsitoDichiarazioneAntimafia verificaEsito;
	 
	 
	 
	    @Test
		@Transactional
		@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
		@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
        public void checkControlloEsitoAntimafiaConAnticipo() throws Exception {
		 
		 String numeroDomanda = "181662";
		 
		 String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B+%22azienda%22%3A%7B%22cuaa%22%3A%22BRTSRG86A24L174X%22%7D%2C+%22stato%22+%3A+%7B%22identificativo%22%3A+%22CONTROLLATA%22%7D%7D";
		 JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/antimafia.json"));
		 String resString = "[]";//objectMapper.writeValueAsString(response);
		 Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

		 String response1 = null;
		 String serviceUrl1 = "http://localhost:8080/a4gistruttoria/api/v1/antimafia/domandecollegate?params=%7B+%22cuaa%22%3A%22BRTSRG86A24L174X%22%2C+%22tipoDomanda%22%3A+%22DOMANDA_UNICA%22%2C+%22idDomanda%22%3A%22181662%22+%7D";
		 String resString1 = response1;
		 if (response1 == null || response1.isEmpty()) {
		 	resString1 = objectMapper.writeValueAsString(new ArrayList<DomandaCollegata>());
		 }
		 Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl1)), Mockito.eq(String.class))).thenReturn(resString1);
	
	 	 DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
		 assertThat(d).isNotNull();

		 IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.ANTICIPO);
 	
		 Boolean esitoAntimafiaOK =  verificaEsito.apply(istruttoria);
		 assertThat(esitoAntimafiaOK).isTrue();
 	
      }
	    
	    
	    @Test
		@Transactional
		@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
		@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void checkControlloEsitoAntimafia() throws Exception {
		 
		 String numeroDomanda = "181662";
		 
		 String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B+%22azienda%22%3A%7B%22cuaa%22%3A%22BRTSRG86A24L174X%22%7D%2C+%22stato%22+%3A+%7B%22identificativo%22%3A+%22CONTROLLATA%22%7D%7D";
		 JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/antimafia.json"));
		 String resString = objectMapper.writeValueAsString(response);
		 Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

		 String response1 = "";
		 String serviceUrl1 = "http://localhost:8080/a4gistruttoria/api/v1/antimafia/domandecollegate?params=%7B+%22cuaa%22%3A%22BRTSRG86A24L174X%22%2C+%22tipoDomanda%22%3A+%22DOMANDA_UNICA%22%2C+%22idDomanda%22%3A%22181662%22+%7D";
		 String resString1 = response1;
		 if (response1 == null || response1.isEmpty()) {
		 	resString1 = objectMapper.writeValueAsString(new ArrayList<DomandaCollegata>());
		 }
		 Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl1)), Mockito.eq(String.class))).thenReturn(resString1);
	
	 	 DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
		 assertThat(d).isNotNull();

		 IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
    	
		 Boolean esitoAntimafiaOK =  verificaEsito.apply(istruttoria);
		 assertThat(esitoAntimafiaOK).isTrue();
    	
    }
	 
	    @Test
		@Transactional
		@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
		@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	 	public void checkControlloEsitoAntimafiaNoAntimafiaNoDomanda() throws Exception {
		 
		 String numeroDomanda = "181662";
		 
		 String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B+%22azienda%22%3A%7B%22cuaa%22%3A%22BRTSRG86A24L174X%22%7D%2C+%22stato%22+%3A+%7B%22identificativo%22%3A+%22CONTROLLATA%22%7D%7D";
		 //JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/antimafia.json"));
		 String resString = "[]";//objectMapper.writeValueAsString(response);
		 Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

		 String response1 = "";
		 String serviceUrl1 = "http://localhost:8080/a4gistruttoria/api/v1/antimafia/domandecollegate?params=%7B+%22cuaa%22%3A%22BRTSRG86A24L174X%22%2C+%22tipoDomanda%22%3A+%22DOMANDA_UNICA%22%2C+%22idDomanda%22%3A%22181662%22+%7D";
		 String resString1 = response1;
		 if (response1 == null || response1.isEmpty()) {
		 	resString1 = objectMapper.writeValueAsString(new ArrayList<DomandaCollegata>());
		 }
		 Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl1)), Mockito.eq(String.class))).thenReturn(resString1);
	
	 	 DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
		 assertThat(d).isNotNull();

		 IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
 	
		 Boolean esitoAntimafiaOK =  verificaEsito.apply(istruttoria);
		 assertThat(esitoAntimafiaOK).isFalse();
 	
	    }
	    
	    @Test
		@Transactional
		@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
		@Sql(scripts = "/DomandaUnica/domanda_Anticipo_Saldo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	 	public void checkControlloEsitoAntimafiaNoAntimafiaSiDomanda() throws Exception {
		 
		 String numeroDomanda = "181662";
		 
		 String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B+%22azienda%22%3A%7B%22cuaa%22%3A%22BRTSRG86A24L174X%22%7D%2C+%22stato%22+%3A+%7B%22identificativo%22%3A+%22CONTROLLATA%22%7D%7D";
		 //JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/antimafia.json"));
		 String resString = "[]";//objectMapper.writeValueAsString(response);
		 Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

		 String serviceUrl1 = "http://localhost:8080/a4gistruttoria/api/v1/antimafia/domandecollegate?params=%7B+%22cuaa%22%3A%22BRTSRG86A24L174X%22%2C+%22tipoDomanda%22%3A+%22DOMANDA_UNICA%22%2C+%22idDomanda%22%3A%22181662%22+%7D";
		 JsonNode response1 = objectMapper.readTree(new File("src/test/resources/DomandaUnica/domandeCollegateResponse.json"));
		 String resString1 = objectMapper.writeValueAsString(response1);
		 
		 if (response1 == null) {
		 	resString1 = objectMapper.writeValueAsString(new ArrayList<DomandaCollegata>());
		 }
		 Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl1)), Mockito.eq(String.class))).thenReturn(resString1);
	
	 	 DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(new Long(numeroDomanda)));
		 assertThat(d).isNotNull();

		 IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
 	
		 Boolean esitoAntimafiaOK =  verificaEsito.apply(istruttoria);
		 assertThat(esitoAntimafiaOK).isTrue();
 	
	    }
	    

}
