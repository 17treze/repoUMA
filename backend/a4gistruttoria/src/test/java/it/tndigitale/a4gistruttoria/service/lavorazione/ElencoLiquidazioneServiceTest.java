package it.tndigitale.a4gistruttoria.service.lavorazione;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.DomandaCollegata;
import it.tndigitale.a4gistruttoria.dto.lavorazione.Cuaa;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno.ControlloIntersostegnoService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita" })
// @AutoConfigureTestDatabase
@Ignore
public class ElencoLiquidazioneServiceTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private DomandaUnicaDao daoDomanda;

	@Autowired
	private IstruttoriaDao istruttoriaDao;


	@Autowired
	private ControlloIntersostegnoService controlloIntersostegno;

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_liquidabile.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Disaccoppiato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void produciElencoLiquidazioneDisaccoppiato() throws Exception {

		List<Long> domandeDaElaborare = new ArrayList<>();
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(181662));
		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B%22azienda%22%3A%7B%22cuaa%22%3A%22BRTSRG86A24L174X%22%7D%7D";
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/antimafia.json"));
		String resString = objectMapper.writeValueAsString(response);

		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

		String serviceUrl1 = "http://localhost:8080/a4gistruttoria/api/v1/antimafia/domandecollegate?params=%7B+%22cuaa%22%3A%22BRTSRG86A24L174X%22%2C+%22tipoDomanda%22%3A+%22DOMANDA_UNICA%22%2C+%22idDomanda%22%3A%22181662%22+%7D";
		String resString1 = objectMapper.writeValueAsString(new ArrayList<DomandaCollegata>());

		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl1)), Mockito.eq(String.class))).thenReturn(resString1);

		controlloIntersostegno.elabora(istruttoria.getId());

		domandeDaElaborare.add(d.getId());

		String serviceUrlAgsCuaa = "http://localhost:8080/ags/api/v1/cuaa?cuaa=BRTSRG86A24L174X";
		Cuaa infoCuaa = objectMapper.readValue(getInfoCuaaAgs(), Cuaa.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgsCuaa)), Mockito.eq(Cuaa.class))).thenReturn(infoCuaa);

//		Long idElencoLiqudazione = elencoLiqService.generaElencoLiquidazione(null, domandeDaElaborare,
//				Sostegno.DISACCOPPIATO, TipoPagamento.SALDO);
//
//		Optional<ElencoLiquidazioneModel> elenco = daoElencoLiquidazione.findById(idElencoLiqudazione);
//		assertTrue(elenco.isPresent());

	}

	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Zootecnia.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Zootecnia_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void produciElencoLiquidazioneZootecnia() throws Exception {

		List<Long> domandeDaElaborare = new ArrayList<>();
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(181666));

		String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B+%22cuaa%22%3A%22BRTSRG86A24L174X%22%7D";
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/antimafia.json"));
		String resString = objectMapper.writeValueAsString(response);

		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

		String serviceUrl1 = "http://localhost:8080/a4gistruttoria/api/v1/antimafia/domandecollegate?params=%7B+%22cuaa%22%3A%22BRTSRG86A24L174X%22%2C+%22tipoDomanda%22%3A+%22DOMANDA_UNICA%22%2C+%22idDomanda%22%3A%22181662%22+%7D";
		String resString1 = objectMapper.writeValueAsString(new ArrayList<DomandaCollegata>());

		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl1)), Mockito.eq(String.class))).thenReturn(resString1);

		// controlloIntersostegno.eseguiControlliIntersostegno(d.getId(), getSostegno.ZOOTECNIA);

		domandeDaElaborare.add(d.getId());

		String serviceUrlAgsCuaa = "http://localhost:8080/ags/api/v1/cuaa?cuaa=BRTSRG86A24L174X";
		Cuaa infoCuaa = objectMapper.readValue(getInfoCuaaAgs(), Cuaa.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgsCuaa)), Mockito.eq(Cuaa.class))).thenReturn(infoCuaa);

//		elencoLiqService.generaElencoLiquidazione(null, domandeDaElaborare, Sostegno.ZOOTECNIA,
//				TipoPagamento.SALDO);
	}
	
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Zootecnia_erede.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Zootecnia_erede_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void produciElencoLiquidazioneZootecniaErede() throws Exception {

		List<Long> domandeDaElaborare = new ArrayList<>();
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(181666));

		String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B+%22cuaa%22%3A%22BRTSRG86A24L174X%22%7D";
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/antimafia.json"));
		String resString = objectMapper.writeValueAsString(response);

		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

		String serviceUrl1 = "http://localhost:8080/a4gistruttoria/api/v1/antimafia/domandecollegate?params=%7B+%22cuaa%22%3A%22BRTSRG86A24L174X%22%2C+%22tipoDomanda%22%3A+%22DOMANDA_UNICA%22%2C+%22idDomanda%22%3A%22181662%22+%7D";
		String resString1 = objectMapper.writeValueAsString(new ArrayList<DomandaCollegata>());

		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl1)), Mockito.eq(String.class))).thenReturn(resString1);

		// controlloIntersostegno.eseguiControlliIntersostegno(d.getId(), getSostegno.ZOOTECNIA);

		domandeDaElaborare.add(d.getId());

		String serviceUrlAgsCuaa = "http://localhost:8080/ags/api/v1/cuaa?cuaa=BRTSRG86A24L174X";
		Cuaa infoCuaa = objectMapper.readValue(getInfoCuaaAgs(), Cuaa.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgsCuaa)), Mockito.eq(Cuaa.class))).thenReturn(infoCuaa);

//		elencoLiqService.generaElencoLiquidazione(null, domandeDaElaborare, Sostegno.ZOOTECNIA,
//				TipoPagamento.SALDO);
	}
	
	
	@Test
	@Transactional
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Superficie.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/domanda_Calcolo_Superficie_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void produciElencoLiquidazioneSuperficie() throws Exception {

		List<Long> domandeDaElaborare = new ArrayList<>();
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(BigDecimal.valueOf(181666));

		String serviceUrl = "http://localhost:9001/a4gfascicolo/api/v1/antimafia?params=%7B+%22cuaa%22%3A%22BRTSRG86A24L174X%22%7D";
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/antimafia.json"));
		String resString = objectMapper.writeValueAsString(response);

		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl)), Mockito.eq(String.class))).thenReturn(resString);

		String serviceUrl1 = "http://localhost:8080/a4gistruttoria/api/v1/antimafia/domandecollegate?params=%7B+%22cuaa%22%3A%22BRTSRG86A24L174X%22%2C+%22tipoDomanda%22%3A+%22DOMANDA_UNICA%22%2C+%22idDomanda%22%3A%22181662%22+%7D";
		String resString1 = objectMapper.writeValueAsString(new ArrayList<DomandaCollegata>());

		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrl1)), Mockito.eq(String.class))).thenReturn(resString1);

		// controlloIntersostegno.eseguiControlliIntersostegno(d.getId(), getSostegno.ZOOTECNIA);

		domandeDaElaborare.add(d.getId());

		String serviceUrlAgsCuaa = "http://localhost:8080/ags/api/v1/cuaa?cuaa=BRTSRG86A24L174X";
		Cuaa infoCuaa = objectMapper.readValue(getInfoCuaaAgs(), Cuaa.class);
		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlAgsCuaa)), Mockito.eq(Cuaa.class))).thenReturn(infoCuaa);

//		elencoLiqService.generaElencoLiquidazione(null, domandeDaElaborare, Sostegno.SUPERFICIE,
//				TipoPagamento.SALDO);
	}

	private String getInfoCuaaAgs() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/181662_Cuaa.json"));
		return objectMapper.writeValueAsString(response);
	}

}
