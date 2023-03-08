package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class PassoCalcoliControlliFinaliServiceTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	PassoCalcoliControlliFinaliService controlliFinali;

	@Autowired
	private TransizioneIstruttoriaDao transizioneIstruttoriaDao;

	@Autowired
	private StatoLavSostegnoDao daoStatoLavSostegno;

	@Autowired
	private DomandaUnicaDao daoDomanda;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	IstruttoriaDao daoIstruttoria;

	@Test
	@Transactional
	public void duf014() throws Exception {
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

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DTPROTDOM, getData("2018-06-15")));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPAMM, new BigDecimal(500000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPAMM, new BigDecimal(5)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPAMM, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIT, BigDecimal.ZERO));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN1, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN2, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN3, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.IMPSALARI, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPEROGATO, new BigDecimal(2)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPEROGATO, new BigDecimal(2)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPEROGATO, new BigDecimal(2)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCPAGAMENTO, new BigDecimal(1)));
		

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = controlliFinali.eseguiPasso(dati);

		assertEquals("DUF_014", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void duf015() throws Exception {
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

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DTPROTDOM, getData("2018-06-15")));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPAMM, new BigDecimal(500000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPAMM, new BigDecimal(5)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPAMM, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIT, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN1, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN2, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN3, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.IMPSALARI, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPEROGATO, new BigDecimal(2)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPEROGATO, new BigDecimal(2)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCPAGAMENTO, new BigDecimal(1)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = controlliFinali.eseguiPasso(dati);

		assertEquals("DUF_015", res.getCodiceEsito());

	}

	private Date getData(String dataStr) {
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-mm-dd");
		Date data = null;
		try {
			data = sdformat.parse(dataStr);
		} catch (ParseException e) {
		}
		return data;
	}

	@Test
	@Transactional
	public void duf016() throws Exception {
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

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DTPROTDOM, getData("2018-06-15")));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPAMM, new BigDecimal(500000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPAMM, new BigDecimal(5)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPAMM, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPCALC, new BigDecimal(300000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIT, BigDecimal.ZERO));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN1, new BigDecimal(0.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN2, new BigDecimal(0.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN3, new BigDecimal(0.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.IMPSALARI, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPEROGATO, new BigDecimal(10000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPEROGATO, new BigDecimal(10000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCPAGAMENTO, new BigDecimal(1)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = controlliFinali.eseguiPasso(dati);

		assertEquals("DUF_016", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void duf017() throws Exception {
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

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DTPROTDOM, getData("2018-06-15")));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPAMM, new BigDecimal(500000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPAMM, new BigDecimal(5)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPAMM, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPCALC, new BigDecimal(300000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIT, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN1, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN2, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN3, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.IMPSALARI, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPEROGATO, new BigDecimal(10000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPEROGATO, new BigDecimal(10000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCPAGAMENTO, new BigDecimal(1)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = controlliFinali.eseguiPasso(dati);

		assertEquals("DUF_017", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void duf016Second() throws Exception {
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

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DTPROTDOM, getData("2018-06-15")));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPAMM, new BigDecimal(500000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPAMM, new BigDecimal(5)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPAMM, new BigDecimal(1)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPCALC, new BigDecimal(300000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIT, BigDecimal.ZERO));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN1, new BigDecimal(0.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN2, new BigDecimal(0.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN3, new BigDecimal(0.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.IMPSALARI, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRITISTR, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPBCCAP, new BigDecimal(10)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPEROGATO, new BigDecimal(10000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPEROGATO, new BigDecimal(10000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPEROGATO, new BigDecimal(10000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCPAGAMENTO, new BigDecimal(1)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = controlliFinali.eseguiPasso(dati);

		assertEquals("DUF_016", res.getCodiceEsito());

	}
	
	@Test
	@Transactional
	public void duf039() throws Exception {
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

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DTPROTDOM, getData("2018-06-15")));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPAMM, new BigDecimal(500000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPAMM, new BigDecimal(5)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPAMM, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIT, BigDecimal.ZERO));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN1, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN2, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN3, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.IMPSALARI, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPEROGATO, new BigDecimal(1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPEROGATO, new BigDecimal(1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPEROGATO, new BigDecimal(1000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCPAGAMENTO, new BigDecimal(1)));
		
		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);
		PassoTransizioneModel res = controlliFinali.eseguiPasso(dati);

		assertEquals("DUF_039", res.getCodiceEsito());
	}
	
	@Test
	@Transactional
	public void duf040() throws Exception {
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
		
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DTPROTDOM, getData("2018-06-15")));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPAMM, new BigDecimal(500000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPAMM, new BigDecimal(5)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPAMM, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIT, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN1, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN2, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN3, new BigDecimal(0.02)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.IMPSALARI, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPEROGATO, new BigDecimal(100)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPEROGATO, new BigDecimal(100)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCPAGAMENTO, new BigDecimal(1)));
		
		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);
		
		PassoTransizioneModel res = controlliFinali.eseguiPasso(dati);
		
		assertEquals("DUF_040", res.getCodiceEsito());
	}
	
	
	@Test
	@Transactional
	public void duf041() throws Exception {
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
		
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DTPROTDOM, getData("2018-06-15")));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPAMM, new BigDecimal(500000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPAMM, new BigDecimal(5)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPAMM, new BigDecimal(1)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPCALC, new BigDecimal(300000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIT, BigDecimal.ZERO));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN1, new BigDecimal(0.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN2, new BigDecimal(0.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN3, new BigDecimal(0.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.IMPSALARI, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRITISTR, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPBCCAP, new BigDecimal(10)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPEROGATO, new BigDecimal(200000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPEROGATO, new BigDecimal(200000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPEROGATO, new BigDecimal(200000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCPAGAMENTO, new BigDecimal(1)));
		
		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);
		
		PassoTransizioneModel res = controlliFinali.eseguiPasso(dati);
		
		assertEquals("DUF_041", res.getCodiceEsito());
	}
	
	@Test
	@Transactional
	public void duf042() throws Exception {
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
		
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DTPROTDOM, getData("2018-06-15")));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPAMM, new BigDecimal(500000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPAMM, new BigDecimal(5)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPAMM, new BigDecimal(1)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPCALC, new BigDecimal(300000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPCALC, new BigDecimal(7)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIT, new BigDecimal(0.2)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN1, new BigDecimal(0.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN2, new BigDecimal(0.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRIDLIN3, new BigDecimal(0.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.IMPSALARI, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCRITISTR, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPBCCAP, new BigDecimal(10)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPEROGATO, new BigDecimal(200000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREIMPEROGATO, new BigDecimal(200000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPEROGATO, new BigDecimal(200000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PERCPAGAMENTO, new BigDecimal(1)));
		
		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);
		
		PassoTransizioneModel res = controlliFinali.eseguiPasso(dati);
		
		assertEquals("DUF_042", res.getCodiceEsito());
	}
}
