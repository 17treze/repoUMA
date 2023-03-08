package it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.dto.AgricoltoreSIAN;
import it.tndigitale.a4gistruttoria.dto.lavorazione.MappaEsitiFoglieAmmissibilitaAccoppiatoZootecnia.FoglieAmmissibilitaAccoppiatoZootecnia;
import it.tndigitale.a4gistruttoria.repository.dao.PassoTransizioneDao;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CalcoloAccoppiatoZootecniaTest {

	@Autowired
	private IstruttoriaComponent istruttoriaComponent;

	@Autowired
	private CalcoloAccoppiatoZootecniaService service;
	
	@Autowired
	private PassoTransizioneDao passiLavorazioneSostegnoDao;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private RestTemplate restTemplate;

	@Value("${a4gistruttoria.proxy.agricoltore.uri}")
	private String agricoltoreAttivoUrl;

	@Value("${a4gistruttoria.proxy.sigeco.uri}")
	private String esitoSigecoUrl;
	
	@Value("${a4gistruttoria.ags.uri}")
	private String agsUri;

	private static final Long ID_ISTRUTTORIA = 4449871234L;
	
	//30/03/2020 Veniva fatto il check sulla domanda integrativo e se non c'era andava in non ammissibile. Credo sia superato
	@Ignore
	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_000_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_000_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_000_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794a", this::getResponseAgricoltoreSianAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.NON_AMMISSIBILE, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_000, "KO");
	}

	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_001_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_001_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_001_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794-", this::getResponseAgricoltoreSianAttivo);

		service.elabora(ID_ISTRUTTORIA);

		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_001, "KO");
	}

	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_002_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_002_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_002_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794W", this::getResponseAgricoltoreSianAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_002, "KO");
	}

	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_003_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_003_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_003_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794_", this::getResponseAgricoltoreSianAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_OK, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_003, "OK");
	}

	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_004_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_004_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_004_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794b", this::getResponseAgricoltoreSianAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_OK, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_004, "OK");
	}

	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_005_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_005_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_005_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794c", this::getResponseAgricoltoreSianAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_005, "KO");
	}

	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_009_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_009_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_009_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794d", this::getResponseAgricoltoreSianAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_009, "KO");
	}
	
	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_010_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_010_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_010_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794Q", this::getResponseAgricoltoreSianAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_OK, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_010, "OK");
	}

	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_011_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_011_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_011_f() throws Exception {
		mockInfoAgricoltoreSIAN("02228060220", this::getResponseAgricoltoreSianAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_OK, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_011, "OK");
	}
	
	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_012_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_012_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_012_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794f", this::getResponseAgricoltoreSianAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_012, "KO");
	}
	
	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_016_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_016_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_016_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C7940", this::getResponseAgricoltoreSianNonAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_016, "KO");
	}

	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_017_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_017_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_017_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794J", this::getResponseAgricoltoreSianNonAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_017, "KO");
	}
	
	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_018_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_018_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_018_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794K", this::getResponseAgricoltoreSianNonAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_018, "KO");
	}
	
	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_019_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_019_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_019_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794L", this::getResponseAgricoltoreSianNonAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_019, "KO");
	}

	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_023_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_023_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_023_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794M", this::getResponseAgricoltoreSianNonAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_023, "KO");
	}
	
	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_024_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_024_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_024_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794N", this::getResponseAgricoltoreSianNonAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_024, "KO");
	}

	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_025_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_025_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_025_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794h", this::getResponseAgricoltoreSianNonAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_025, "KO");
	}

	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_026_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_026_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_026_f() throws Exception {
		mockInfoAgricoltoreSIAN("02228060220a", this::getResponseAgricoltoreSianNonAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_026, "KO");
	}
	
	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_027_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_027_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_027_f() throws Exception {
		mockInfoAgricoltoreSIAN("02228060220b", this::getResponseAgricoltoreSianNonAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_027, "KO");
	}
	
	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_031_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_031_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_031_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794O", this::getResponseAgricoltoreSianNonPresente);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_031, "KO");
	}

	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_032_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_032_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_032_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794P", this::getResponseAgricoltoreSianNonPresente);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_032, "KO");
	}
	
	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_036_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_036_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_036_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794Q", this::getResponseAgricoltoreSianNonPresente);

		service.elabora(ID_ISTRUTTORIA);

		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_036, "KO");
	}
	
	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_037_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_037_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_037_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794R", this::getResponseAgricoltoreSianNonPresente);

		service.elabora(ID_ISTRUTTORIA);

		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_037, "KO");
	}
	
	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_038_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_038_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_038_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794S", this::getResponseAgricoltoreSianNonPresente);

		service.elabora(ID_ISTRUTTORIA);

		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_038, "KO");
	}

	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_039_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_039_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_039_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794T", this::getResponseAgricoltoreSianNonPresente);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_039, "KO");
	}

	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_040_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_040_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_040_f() throws Exception {
		mockInfoAgricoltoreSIAN("FLMWFG75M12C794U", this::getResponseAgricoltoreSianNonPresente);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_040, "KO");
	}
	
	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_041_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_041_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_041_f() throws Exception {
		mockInfoAgricoltoreSIAN("02228060220v", this::getResponseAgricoltoreSianNonPresente);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_041, "KO");
	}
	
	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_042_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/duf_042_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void duf_042_f() throws Exception {
		mockInfoAgricoltoreSIAN("02228060220z", this::getResponseAgricoltoreSianNonPresente);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_KO, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_042, "KO");
	}

	@Test
	@Ignore
	@Transactional
	@Sql(scripts = { "/DomandaUnica/zootecnia/acz_test_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/zootecnia/acz_test_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void test_algoritmo() throws Exception {
		mockInfoAgricoltoreSIAN("cuaa_test", this::getResponseAgricoltoreSianAttivo);

		service.elabora(ID_ISTRUTTORIA);
		
		checkStato(ID_ISTRUTTORIA, StatoIstruttoria.CONTROLLI_CALCOLO_OK, FoglieAmmissibilitaAccoppiatoZootecnia.DUF_011, "OK");
	}

	protected void checkStato(Long idIstruttoria, StatoIstruttoria statoAtteso, FoglieAmmissibilitaAccoppiatoZootecnia foglia, String esito) throws Exception {
		IstruttoriaModel istruttoria = istruttoriaComponent.load(idIstruttoria);

		assertThat(istruttoria).isNotNull();
		Set<TransizioneIstruttoriaModel> transizioni = istruttoria.getTransizioni();
		assertThat(transizioni).isNotEmpty();
		Optional<TransizioneIstruttoriaModel> optTrans = 
				transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertThat(optTrans.isPresent()).isTrue();
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertThat(passi).hasSize(1);
		PassoTransizioneModel passoLavorazione = passi.get(0);
		assertThat(TipologiaPassoTransizione.CALCOLO_ACZ).isEqualTo(passoLavorazione.getCodicePasso());
		assertThat(foglia.getCodiceEsito()).isEqualTo(passoLavorazione.getCodiceEsito());
		assertThat(statoAtteso.getStatoIstruttoria()).isEqualTo(istruttoria.getA4gdStatoLavSostegno().getIdentificativo());
		assertThat(esito).isEqualTo(passoLavorazione.getEsito());
	}

	protected void mockInfoAgricoltoreSIAN(String codiceFiscale, Function<String, String> getResponseFunction) throws Exception {
		String jsonRequest = "{ \"codFisc\":\"" + codiceFiscale + "\", \"annoCamp\":2018}";
		String serviceUrlA4GPROXYrecuperoAgricoltoreSian = agricoltoreAttivoUrl + "?params=".concat(URLEncoder.encode(jsonRequest, "UTF-8"));
		AgricoltoreSIAN responseAgricoltoreSian = objectMapper.readValue(getResponseFunction.apply(codiceFiscale), AgricoltoreSIAN.class);
		when(restTemplate.getForObject(Mockito.eq(new URI(serviceUrlA4GPROXYrecuperoAgricoltoreSian)), Mockito.eq(AgricoltoreSIAN.class)))
				.thenReturn(responseAgricoltoreSian);
	}

	protected String getResponseAgricoltoreSianNonPresente(String s) {
		return getResponseAgricoltoreSian("infoAgricoltoreAttivoNonPresente");
	}

	protected String getResponseAgricoltoreSianAttivo(String s) {
		return getResponseAgricoltoreSian("agricoltoreAttivo");
	}

	protected String getResponseAgricoltoreSianNonAttivo(String s) {
		return getResponseAgricoltoreSian("agricoltoreNonAttivo");
	}

	protected String getResponseAgricoltoreSian(String s) {
		try {
			JsonNode response = objectMapper.readTree(new File("src/test/resources/DomandaUnica/" + s + ".json"));
			return objectMapper.writeValueAsString(response);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
