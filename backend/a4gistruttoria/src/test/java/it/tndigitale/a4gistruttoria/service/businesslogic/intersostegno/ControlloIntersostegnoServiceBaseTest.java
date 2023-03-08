package it.tndigitale.a4gistruttoria.service.businesslogic.intersostegno;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.MockIoItalia;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiCalcoli;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.PassoTransizioneDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.StatoSostegnoDomandaService;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;

public abstract class ControlloIntersostegnoServiceBaseTest extends MockIoItalia {

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private PassoTransizioneDao passiLavorazioneSostegnoDao;

	@Autowired
	private StatoSostegnoDomandaService statoDomandaService;
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;

	@Autowired
	private TransizioneIstruttoriaDao transizioneDao;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Value("${a4gistruttoria.a4gfascicolo.uri}")
	private String uriFascicolo;
	
	@Value("${a4gistruttoria.uri}")
	private String uriIstruttoria;
	
	protected abstract Sostegno getIdentificativoSostegno();
	
	protected void checkStatoSostegno(IstruttoriaModel istruttoria, StatoIstruttoria statoAtteso) throws Exception {
		istruttoria = istruttoriaDao.getOne(istruttoria.getId());
		entityManager.refresh(istruttoria);
		assertNotNull(istruttoria);
		assertEquals(statoAtteso.getStatoIstruttoria(), istruttoria.getA4gdStatoLavSostegno().getIdentificativo());
	}

	protected void checkPasso(IstruttoriaModel istruttoria, TipologiaPassoTransizione passolavorazioneDaVerificare, String esito, String codiceEsito) throws Exception {
		Set<TransizioneIstruttoriaModel> transizioni = istruttoria.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		// assertEquals(1, passi.size());
		PassoTransizioneModel passoLavorazione = passi.stream().filter(passo -> passolavorazioneDaVerificare.equals(passo.getCodicePasso())).findFirst().get();
		if (codiceEsito != null) {
			assertEquals(codiceEsito, passoLavorazione.getCodiceEsito());
		}
		
		assertEquals(esito, passoLavorazione.getEsito());
	}

	protected void mockAntimafia(DomandaUnicaModel domanda) throws Exception {
		String cuaa = domanda.getCuaaIntestatario();
        String antimafiaURI = uriFascicolo.concat("antimafia?params=");
        antimafiaURI = antimafiaURI.concat(URLEncoder.encode(String.format("%s%s%s", "{ \"azienda\":{\"cuaa\":\"", cuaa, "\"}, \"stato\" : {\"identificativo\": \"CONTROLLATA\"}}"), "UTF-8"));
        String resString = null;
        File mockDichiarazioni = new File("src/test/resources/DomandaUnica/intersostegno/dichiarazioneAntimafiaControllata_" + cuaa + ".json");
        if (mockDichiarazioni.exists()) {
        	System.out.println("trovato mock per antimafia, cuaa " + cuaa);
    		JsonNode response = objectMapper.readTree(mockDichiarazioni);
    		resString = objectMapper.writeValueAsString(response);
        }

		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(antimafiaURI)), Mockito.eq(String.class))).thenReturn(resString);

		mockDichiarazioni = new File("src/test/resources/DomandaUnica/intersostegno/domandeDUCollegate_" + cuaa + ".json");
		resString = null;
		if (mockDichiarazioni.exists()) {
        	System.out.println("trovato mock per antimafia; cuaa " + cuaa);
    		JsonNode response = objectMapper.readTree(mockDichiarazioni);
    		resString = objectMapper.writeValueAsString(response);
        }
		antimafiaURI = uriIstruttoria.concat("antimafia/domandecollegate?params=");
        antimafiaURI = antimafiaURI.concat(URLEncoder.encode(String.format("%s%s%s%s%s", "{ \"cuaa\":\"", cuaa,
    			"\", \"tipoDomanda\": \"DOMANDA_UNICA\", \"idDomanda\":\"", domanda.getNumeroDomanda(), "\" }"), "UTF-8"));
        Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(antimafiaURI)), Mockito.eq(String.class))).thenReturn(resString);
	}
	
	protected void checkPassoValori(IstruttoriaModel istruttoria, TipologiaPassoTransizione passolavorazioneDaVerificare) throws Exception {
		Set<TransizioneIstruttoriaModel> transizioni = istruttoria.getTransizioni();
		assertNotNull(transizioni);
		assertTrue(!transizioni.isEmpty());
		Optional<TransizioneIstruttoriaModel> optTrans = transizioni.stream().sorted(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione).reversed()).findFirst();
		assertTrue(optTrans.isPresent());
		TransizioneIstruttoriaModel transizione = optTrans.get();
		List<PassoTransizioneModel> passi = passiLavorazioneSostegnoDao.findByTransizioneIstruttoria(transizione);
		assertNotNull(passi);
		PassoTransizioneModel passoLavorazione = passi.stream().filter(passo -> {
			return passolavorazioneDaVerificare.equals(passo.getCodicePasso());
		}).findFirst().get();
	
		assertVariabiliAttesePerTest(istruttoria, passoLavorazione);
	}
	
	protected Boolean checkTipoVariabileOutput(PassoTransizioneModel passo, TipoVariabile tipoVariabile, Object valoreAtteso) throws Exception {
		DatiCalcoli datiCalcoli = objectMapper.readValue(passo.getDatiOutput(), DatiCalcoli.class);
		return checkTipoVariabile(datiCalcoli, tipoVariabile, valoreAtteso);
	}

	protected Boolean checkTipoVariabileInput(PassoTransizioneModel passo, TipoVariabile tipoVariabile, Object valoreAtteso) throws Exception {
		DatiCalcoli datiCalcoli = objectMapper.readValue(passo.getDatiInput(), DatiCalcoli.class);
		return checkTipoVariabile(datiCalcoli, tipoVariabile, valoreAtteso);
	}

	protected Boolean checkTipoVariabile(DatiCalcoli datiCalcoli, TipoVariabile tipoVariabile, Object valoreAtteso) throws Exception {
		Optional<VariabileCalcolo> variabileCalcolo = datiCalcoli.getVariabiliCalcolo().stream().filter(variabile -> {
			return (variabile.getTipoVariabile().compareTo(tipoVariabile) == 0);
		}).findFirst();
		
		if (!variabileCalcolo.isPresent()) { return false;}
		
		switch (variabileCalcolo.get().getTipoVariabile().getFormato()) {
		case NUMERO2DECIMALI:
		case NUMERO4DECIMALI:
		case PERCENTUALE:
		case PERCENTUALE6DECIMALI:
			return ((BigDecimal) valoreAtteso).compareTo(variabileCalcolo.get().getValNumber()) == 0;
		case BOOL:
			return ((Boolean) valoreAtteso).compareTo(variabileCalcolo.get().getValBoolean()) == 0;
		case STRING:
			return ((String) valoreAtteso).compareTo(variabileCalcolo.get().getValString()) == 0;
		default:
			fail();
			break;
		}
		return false;
	}

	private void assertVariabiliAttesePerTest(IstruttoriaModel istruttoria, PassoTransizioneModel passoLavorazione) throws Exception {
		DomandaUnicaModel domanda = istruttoria.getDomandaUnicaModel();
		if (domanda.getCuaaIntestatario().equalsIgnoreCase("DLCSVR70S20B006-")) {
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.ACZIMPCALC, new BigDecimal("959.24")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFFRPAGACZ, new BigDecimal("959.24")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFIMPRIDACZ, new BigDecimal("0.0")));
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.DFFR, new BigDecimal("2000.0")));
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.DFPERC, new BigDecimal("0.5")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFFRPAGACZ_322, new BigDecimal("625.73")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFFRPAGACZ_313, new BigDecimal("333.51")));
		}
		
		if (domanda.getCuaaIntestatario().equalsIgnoreCase("01801280221")) {
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.DFFR, new BigDecimal("2000.0")));
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.DFPERC, new BigDecimal("0.01411917")));
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.ACSIMPCALC_M17, new BigDecimal("449.03")));
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.ACSIMPCALC, new BigDecimal("449.03")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFFRPAGACS, new BigDecimal("449.03")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFIMPLIQACS, new BigDecimal("449.03")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFFRPAGACS_M17, new BigDecimal("449.03")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFIMPRIDACS, new BigDecimal("0.0")));
		}
		if (domanda.getCuaaIntestatario().equalsIgnoreCase("DLCSVR70S20B005-")) {
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.ACZIMPCALC, new BigDecimal("959.24")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFFRPAGACZ, new BigDecimal("959.24")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFIMPRIDACZ, new BigDecimal("0.0")));
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.DFFR, new BigDecimal("2000.0")));
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.DFPERC, new BigDecimal("0.4")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFFRPAGACZ_322, new BigDecimal("625.73")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFFRPAGACZ_313, new BigDecimal("333.51")));
		}
		if (domanda.getCuaaIntestatario().equalsIgnoreCase("LNGNTN64B03H7641")) {
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.DFFR, new BigDecimal("2000.0")));
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.DFPERC, new BigDecimal("0.01411917")));
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.DFFRAPPDIS, new BigDecimal("1707.75")));
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.DFFRAPPACZ, new BigDecimal("0.0")));
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.DFFRAPPACS, new BigDecimal("0.0")));
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.ACSIMPCALC, new BigDecimal("826.04")));
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.ACSIMPCALC_M9, new BigDecimal("574.18")));
			assertTrue(checkTipoVariabileInput(passoLavorazione, TipoVariabile.ACSIMPCALC_M11, new BigDecimal("251.86")));

			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFFRPAGACS, new BigDecimal("292.25")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFIMPRIDACS, new BigDecimal("7.54")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFFRPAGACS_M9, new BigDecimal("292.25")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFIMPDFDISACS_M9, new BigDecimal("277.95")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFIMPDFDISACS_M11, new BigDecimal("248.3")));
			assertTrue(checkTipoVariabileOutput(passoLavorazione, TipoVariabile.DFIMPLIQACS, new BigDecimal("818.5")));
		}
	}
}
