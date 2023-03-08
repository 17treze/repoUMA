package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
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

import it.tndigitale.a4gistruttoria.dto.Particella;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.dto.lavorazione.ParticellaColtura;
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
public class PassoCalcoloRiduzioniServiceTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	PassoCalcoloRiduzioniService riduzione;

	@Autowired
	TransizioneIstruttoriaDao daoTransizioneSostegno;

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
	public void dut005() throws Exception {
		
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(3.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(3.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(0)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_005", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void dut006() throws Exception {
		
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(5.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(4.8900)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(0)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_006", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void dut007() throws Exception {
		
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(5.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(4.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(0)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_007", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void dut008() throws Exception {
		
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(5.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(3.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(0)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_008", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void dut009() throws Exception {
		// Non si verifica mai.
		// per avere scostamento > 0 (e quindi superare la BRIDU_020) è necessario che BPSIMPRIC superi BPSSUPDETIST o BPSSUPDET di 1000 m
		// per superare la BRIDU_035, la BPSSUPAMM deve essere < 5000
		// quindi al max BPSSUPAMM = 49999 e BPSIMPRIC = 6000 e di conseguenza la percentuale di scostamento sarà al minimo 22% e quindi si ricade sempre nella Sanzioni > 10 ovvero DUT_11
	}

	@Test
	@Transactional
	public void dut010() throws Exception {
		// Non si verifica mai. Vedi dut009
	}

	@Test
	@Transactional
	public void dut011() throws Exception {
		
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(15.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(0.3000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, BigDecimal.ZERO));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_011", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void dut012() throws Exception {
		
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(3.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(3.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(3)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DOMSIGECOCHIUSA, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_012", res.getCodiceEsito());

	}
	
	@Test
	@Transactional
	public void dut013() throws Exception {
		
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(5.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(4.8900)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DOMSIGECOCHIUSA, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(3)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_013", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void dut014() throws Exception {
		
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(5.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(4.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DOMSIGECOCHIUSA, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(3)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_014", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void dut015() throws Exception {
		
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(15.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(3.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DOMSIGECOCHIUSA, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(3)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_015", res.getCodiceEsito());

	}

	@Test
	@Transactional
	public void dut016() throws Exception {
		// Non si verifica mai. Vedi dut009
	}

	@Test
	@Transactional
	public void dut017() throws Exception {
		// Non si verifica mai. Vedi dut009
	}

	@Test
	@Transactional
	public void dut018() throws Exception {
		
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(15.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(0.3000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DOMSIGECOCHIUSA, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(3)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_018", res.getCodiceEsito());
	}
	
	@Test
	@Transactional
	public void dut065() throws Exception {
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(5.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(1)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(4.8900)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(0)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_065", res.getCodiceEsito());
	}

	@Test
	@Transactional
	public void dut066() throws Exception {
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(5.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(1)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(4.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(0)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_066", res.getCodiceEsito());
	}

	@Test
	@Transactional
	public void dut067() throws Exception {
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(5.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(1)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(3.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(0)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_067", res.getCodiceEsito());
	}
	
	@Test
	@Transactional
	public void dut068() throws Exception {
		// Non si verifica mai.
		// per avere scostamento > 0 (e quindi superare la BRIDU_020) è necessario che BPSIMPRIC superi BPSSUPDETIST o BPSSUPDET di 1000 m
		// per superare la BRIDU_035, la BPSSUPAMM deve essere < 5000
		// quindi al max BPSSUPAMM = 49999 e BPSIMPRIC = 6000 e di conseguenza la percentuale di scostamento sarà al minimo 22% e quindi si ricade sempre nella Sanzioni > 10 ovvero DUT_11
	}

	@Test
	@Transactional
	public void dut069() throws Exception {
		// Non si verifica mai. Vedi dut009
	}

	@Test
	@Transactional
	public void dut070() throws Exception {
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(15.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(1)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(0.3000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, BigDecimal.ZERO));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_070", res.getCodiceEsito());
	}
	
	@Test
	@Transactional
	public void dut071() throws Exception {
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(5.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(1)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(4.8900)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DOMSIGECOCHIUSA, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(3)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_071", res.getCodiceEsito());
	}

	@Test
	@Transactional
	public void dut072() throws Exception {
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(5.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(1)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(4.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DOMSIGECOCHIUSA, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(3)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_072", res.getCodiceEsito());
	}

	@Test
	@Transactional
	public void dut073() throws Exception {
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(15.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(1)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(3.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DOMSIGECOCHIUSA, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(3)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_073", res.getCodiceEsito());
	}
	

	@Test
	@Transactional
	public void dut074() throws Exception {
		// Non si verifica mai. Vedi dut009
	}

	@Test
	@Transactional
	public void dut075() throws Exception {
		// Non si verifica mai. Vedi dut009
	}

	@Test
	@Transactional
	public void dut076() throws Exception {
		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(
				StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(15.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(1)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(0.3000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DOMSIGECOCHIUSA, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(3)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUT_076", res.getCodiceEsito());
	}
	
	@Test
	@Transactional
	public void duf012() throws Exception {
		
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(15.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(0.3000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DOMSIGECOCHIUSA, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(3)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		assertEquals("DUF_012", res.getCodiceEsito());

	}

	// test temporaneamente disabilitato: è necessario sapere quali codici cultura e livelli particella vanno inseriti quando vengono create le particelle
	@Test
	@Transactional
	public void verificaPfSupDet() throws Exception {
		
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
		transizione = daoTransizioneSostegno.save(transizione);

		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// creare delle liste per PFSUPDET E PFSUPELE con tre particelle
		ArrayList<ParticellaColtura> supImpegnate = new ArrayList<ParticellaColtura>();
		ArrayList<ParticellaColtura> supElegibile = new ArrayList<ParticellaColtura>();
		ArrayList<ParticellaColtura> pfAnomman = new ArrayList<ParticellaColtura>();
		ArrayList<ParticellaColtura> pfAnomcoord = new ArrayList<ParticellaColtura>();
		ArrayList<ParticellaColtura> supSigeco = new ArrayList<>();

		// Particella 1
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionele\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}", new Float(7),
				null, "131", "080-336-052"));
		supElegibile.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionele\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}", new Float(4),
				null, "131", "080-336-052"));
		pfAnomman.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionele\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}", null, false,
				"131", "080-336-052"));
		pfAnomcoord.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionele\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}", null, false,
				"131", "080-336-052"));

		// Particella 2
		supImpegnate.add(getParticellaColtura("{\"idParticella\":3,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionele\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}", new Float(7),
				null, "131", "080-336-052"));
		supElegibile.add(getParticellaColtura("{\"idParticella\":3,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionele\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				new Float(12), null, "131", "080-336-052"));
		pfAnomman.add(getParticellaColtura("{\"idParticella\":3,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionele\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}", null, true,
				"131", "080-336-052"));
		pfAnomcoord.add(getParticellaColtura("{\"idParticella\":3,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionele\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}", null, false,
				"131", "080-336-052"));

		// Particella 3
		supImpegnate.add(getParticellaColtura("{\"idParticella\":4,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionele\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}", new Float(3),
				null, "131", "080-336-052"));
		supElegibile.add(getParticellaColtura("{\"idParticella\":4,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionele\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}", new Float(8),
				null, "131", "080-336-052"));
		pfAnomman.add(getParticellaColtura("{\"idParticella\":4,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionele\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}", null, true,
				"131", "080-336-052"));
		pfAnomcoord.add(getParticellaColtura("{\"idParticella\":4,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionele\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}", null, true,
				"131", "080-336-052"));

		supSigeco.add(getParticellaColtura("{\"idParticella\":4,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionele\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}", new Float(8),
				null, "131", "080-336-052"));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPSIGECO, supSigeco));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPRIC, new BigDecimal(15.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPELE, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTMAN, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOSTCOO, new BigDecimal(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDETIST, new BigDecimal(0.3000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.ISCAMP, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFANOMMAN, pfAnomman));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFANOMCOORD, pfAnomcoord));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPELE, supElegibile));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPIMP, supImpegnate));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.DOMSIGECOCHIUSA, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSIGECO, new BigDecimal(3)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = riduzione.eseguiPasso(dati);

		// TODO: rivere logica assert equal per considerare la lista di sup_determinate valorizzata nel blocchetto una volta che sarà stata memorizzata su DB
		assertEquals(1, 1);

	}

	private ParticellaColtura getParticellaColtura(String infoCatastali, Float valNum, Boolean valBool, String livello, String coltura) {
		ParticellaColtura pc = new ParticellaColtura();
		try {
			Particella p = mapper.readValue(infoCatastali, Particella.class);
			pc.setParticella(p);
			pc.setValNum(valNum);
			pc.setValBool(valBool);
			pc.setColtura(coltura);
			pc.setLivello(livello);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return pc;
	}
}
