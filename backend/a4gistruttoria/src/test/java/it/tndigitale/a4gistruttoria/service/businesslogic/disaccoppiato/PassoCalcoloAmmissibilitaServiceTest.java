package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.dao.StatoLavSostegnoDao;
import it.tndigitale.a4gistruttoria.repository.dao.TransizioneIstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, statements = "alter sequence NXTNBR restart with 1000000")
public class PassoCalcoloAmmissibilitaServiceTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private PassoCalcoloAmmissibilitaService ammissibilita;

	@Autowired
	private TransizioneIstruttoriaDao transizioneIstruttoriaDao;

	@Autowired
	private StatoLavSostegnoDao daoStatoLavSostegno;
	
	@Autowired
	IstruttoriaDao daoIstruttoria;

	@Autowired
	private DomandaUnicaDao daoDomanda;

	@Autowired
	private ObjectMapper mapper;

	@Test
	@Transactional
	public void dut001Test() throws Exception {

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_agricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, true));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, new BigDecimal(1)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.5001)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, true));
		
		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		assertEquals("DUT_001", res.getCodiceEsito());

	}

	@Test
	@Transactional

	public void duf001Test() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_agricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, new BigDecimal(-1)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, new BigDecimal(1)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.5001)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);
 
		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		assertEquals("DUF_001", res.getCodiceEsito());

	}

	@Test
	@Transactional
	// aspettare push di alessio per far si che Aggratt sia null
	public void duf002Test() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_infoAgricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, new BigDecimal(-1)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, BigDecimal.ZERO));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.5001)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		assertEquals("DUF_002", res.getCodiceEsito());

	}

	@Test
	@Transactional
	// aspettare push di alessio per far si che Aggratt sia null
	public void duf003Test() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_infoAgricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, new BigDecimal(-1)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, new BigDecimal(1)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.4000)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		assertEquals("DUF_003", res.getCodiceEsito());

	}

	@Test
	@Transactional
	// aspettare push di alessio per far si che Aggratt sia null
	public void duf004Test() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_infoAgricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, new BigDecimal(-1)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, BigDecimal.ZERO));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.4000)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		assertEquals("DUF_004", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void duf005Test() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_infoAgricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, false));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, new BigDecimal(1)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.5001)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		assertEquals("DUF_005", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void duf006Test() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_infoAgricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, false));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, BigDecimal.ZERO));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.5001)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		assertEquals("DUF_006", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void duf007Test() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_infoAgricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, false));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, new BigDecimal(1)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.40001)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		assertEquals("DUF_007", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void duf008Test() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_infoAgricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, false));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, BigDecimal.ZERO));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.4001)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		assertEquals("DUF_008", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void duf009Test() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_infoAgricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, true));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, BigDecimal.ZERO));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.5001)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		assertEquals("DUF_009", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void duf010Test() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_infoAgricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, true));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, new BigDecimal(1)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.4001)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		assertEquals("DUF_010", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void duf011Test() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_infoAgricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, true));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, BigDecimal.ZERO));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.4001)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		assertEquals("DUF_011", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void calcoloVariabiliTitoliFalse() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_infoAgricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, true));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79).setScale(4, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00).setScale(4, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000).setScale(4, RoundingMode.HALF_UP)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, BigDecimal.ZERO));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000).setScale(4, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000).setScale(4, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.5001).setScale(4, RoundingMode.HALF_UP)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		DatiOutput datiOutput = mapper.readValue(res.getDatiOutput(), DatiOutput.class);

		datiOutput.getVariabiliCalcolo().forEach(v -> {
			if (v.getTipoVariabile().equals(TipoVariabile.BPSSUPRIC)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.BPSIMPRIC)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.BPSSUPAMM)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.BPSIMPAMM)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREIMPRIC)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPAMM)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREIMPAMM)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GIOSUPRIC)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GIOIMPRIC)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GIOSUPAMM)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GIOIMPAMM)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPRIC)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP));
			}

		});
	}

	// andando in debug entra nell'if giusto , controllare i calcoli.

	// DA RIVEDERE PERCHE' GIOPERC LO TRASFORMA IN 5 ANCHE SE METTIAMO 0.5000
	@Test
	@Transactional
	public void calcoloAgrattESupMinFalse() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_infoAgricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, false));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79).setScale(2, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00).setScale(2, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000).setScale(4, RoundingMode.HALF_UP)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, new BigDecimal(1)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000).setScale(4, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000).setScale(4, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.4000).setScale(4, RoundingMode.HALF_UP)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		DatiOutput datiOutput = mapper.readValue(res.getDatiOutput(), DatiOutput.class);

		datiOutput.getVariabiliCalcolo().forEach(v -> {
			if (v.getTipoVariabile().equals(TipoVariabile.BPSSUPRIC)) {
				assertEquals(v.getValNumber(), new BigDecimal(0.2000).setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.BPSIMPRIC)) {
				assertEquals(v.getValNumber(), new BigDecimal(0.0200).setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.BPSSUPAMM)) {
				assertEquals(v.getValNumber(), new BigDecimal(0).setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.BPSIMPAMM)) {
				assertEquals(v.getValNumber(), new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREIMPRIC)) {
				assertEquals(v.getValNumber(), new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPAMM)) {
				assertEquals(v.getValNumber(), new BigDecimal(0).setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREIMPAMM)) {
				assertEquals(v.getValNumber(), new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
			}

			if (v.getTipoVariabile().equals(TipoVariabile.BPSIMPAMM)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));

				if (v.getTipoVariabile().equals(TipoVariabile.GIOSUPRIC)) {
					assertEquals(v.getValNumber(), new BigDecimal(0.2000).setScale(4, RoundingMode.HALF_UP));
				}

				if (v.getTipoVariabile().equals(TipoVariabile.GIOSUPAMM)) {
					assertEquals(v.getValNumber(), new BigDecimal(0).setScale(4, RoundingMode.HALF_UP));
				}
				if (v.getTipoVariabile().equals(TipoVariabile.GIOIMPAMM)) {
					assertEquals(v.getValNumber(), new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
				}
				if (v.getTipoVariabile().equals(TipoVariabile.GRESUPRIC)) {
					assertEquals(v.getValNumber(), new BigDecimal(0).setScale(4, RoundingMode.HALF_UP));

					assertEquals(v.getValNumber(), new BigDecimal(0.1000).setScale(4, RoundingMode.HALF_UP));
				}
			}
		});
	}

	@Test
	@Transactional
	public void calcoloGioRicFalse() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_infoAgricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, true));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79).setScale(4, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00).setScale(4, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000).setScale(4, RoundingMode.HALF_UP)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, new BigDecimal(1)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000).setScale(4, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000).setScale(4, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.4000).setScale(4, RoundingMode.HALF_UP)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, false));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		DatiOutput datiOutput = mapper.readValue(res.getDatiOutput(), DatiOutput.class);

		datiOutput.getVariabiliCalcolo().forEach(v -> {
			if (v.getTipoVariabile().equals(TipoVariabile.BPSSUPRIC)) {
				assertEquals(v.getValNumber(), new BigDecimal(0.2000).setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.BPSIMPRIC)) {
				assertEquals(v.getValNumber(), new BigDecimal(0.0200).setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.BPSSUPAMM)) {

				assertEquals(v.getValNumber(), new BigDecimal(0).setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.BPSIMPAMM)) {
				assertEquals(v.getValNumber(), new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREIMPRIC)) {
				assertEquals(v.getValNumber().setScale(4, RoundingMode.HALF_UP), new BigDecimal(0).setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPRIC)) {
				assertEquals(v.getValNumber().setScale(4, RoundingMode.HALF_UP), new BigDecimal(0).setScale(4, RoundingMode.HALF_UP));

				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.BPSIMPAMM)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREIMPRIC)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPRIC)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPAMM)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREIMPAMM)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GIOSUPRIC)) {
				assertNull(v.getValNumber());
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GIOIMPRIC)) {
				assertNull(v.getValNumber());
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GIOSUPAMM)) {
				assertNull(v.getValNumber());
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GIOIMPAMM)) {
				assertNull(v.getValNumber());
			}

		});
	}

	@Test
	@Transactional
	public void calcoloAmmissibile() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = daoIstruttoria.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizioneIstruttoriaDao.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// verifica se agricoltore è attivo BRIDUSDC010_agricoltoreAttivo e BRIDUSDC009_infoAgricoltoreAttivo
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.AGRATT, true));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, new BigDecimal(49.79).setScale(4, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, new BigDecimal(50.00).setScale(4, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOLIMITE, new BigDecimal(0.3000).setScale(4, RoundingMode.HALF_UP)));

		// verifica se ci sono titoli BRIDUSDC011_impegnoTitoli
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITONUM, new BigDecimal(1)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITSUP, new BigDecimal(0.2000).setScale(4, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVAL, new BigDecimal(0.1000).setScale(4, RoundingMode.HALF_UP)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIO, new BigDecimal(0.1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLINTIT, new BigDecimal(0.1000)));

		// verifica la sup. imp. BRIDUSDC012_superficieMinima
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPIMP, new BigDecimal(0.4000).setScale(4, RoundingMode.HALF_UP)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRERIC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, false));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = ammissibilita.eseguiPasso(dati);

		DatiOutput datiOutput = mapper.readValue(res.getDatiOutput(), DatiOutput.class);

		datiOutput.getVariabiliCalcolo().forEach(v -> {
			if (v.getTipoVariabile().equals(TipoVariabile.BPSSUPRIC)) {
				assertEquals(v.getValNumber(), new BigDecimal(0.2000).setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.BPSIMPRIC)) {
				assertEquals(v.getValNumber(), new BigDecimal(0.0200).setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.BPSSUPAMM)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.BPSIMPAMM)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREIMPRIC)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPRIC)) {
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP));
			}

			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPAMM)) {
				assertEquals(v.getValNumber().setScale(4, RoundingMode.HALF_UP), new BigDecimal(0).setScale(4, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREIMPAMM)) {
				assertEquals(v.getValNumber().setScale(4, RoundingMode.HALF_UP), new BigDecimal(0).setScale(4, RoundingMode.HALF_UP));
				assertEquals(v.getValNumber(), BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
			}

			if (v.getTipoVariabile().equals(TipoVariabile.GIOSUPRIC)) {
				assertNull(v.getValNumber());
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GIOIMPRIC)) {
				assertNull(v.getValNumber());
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GIOSUPAMM)) {
				assertNull(v.getValNumber());
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GIOIMPAMM)) {
				assertNull(v.getValNumber());
			}

		});
	}

}
