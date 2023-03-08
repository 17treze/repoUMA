package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import static org.junit.Assert.assertEquals;

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
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, statements = "alter sequence NXTNBR restart with 1000000")
public class PassoCalcoloSanzioniServiceTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	PassoCalcoloSanzioniService sanzione;

	@Autowired
	TransizioneIstruttoriaDao daoTransizioneSostegno;

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
	public void dut019() throws Exception {

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

		esitiInput.add(new EsitoControllo(TipoControllo.BRIDUSDC021_idDomandaSanzioni, "false"));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSYCSANZANNIPREC, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSYCIMPSANZAPREC, new BigDecimal(20.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPAMM, new BigDecimal(3.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPRIC, new BigDecimal(20.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOST, new BigDecimal(1.7000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSPERCSCOST, new BigDecimal(2.8000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSDOMLIQANNOPREC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = sanzione.eseguiPasso(dati);

		assertEquals("DUT_019", res.getCodiceEsito());
	}

	@Test
	@Transactional
	public void dut020() throws Exception {


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

		esitiInput.add(new EsitoControllo(TipoControllo.BRIDUSDC021_idDomandaSanzioni, "inf_10"));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSYCSANZANNIPREC, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSYCIMPSANZAPREC, new BigDecimal(20.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPAMM, new BigDecimal(3.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPRIC, new BigDecimal(20.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOST, new BigDecimal(6.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSPERCSCOST, new BigDecimal(0.0300)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSDOMLIQANNOPREC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = sanzione.eseguiPasso(dati);

		assertEquals("DUT_020", res.getCodiceEsito());
	}

	@Test
	@Transactional
	public void dut021() throws Exception {


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

		esitiInput.add(new EsitoControllo(TipoControllo.BRIDUSDC021_idDomandaSanzioni, "sup_10"));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSYCSANZANNIPREC, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSYCIMPSANZAPREC, new BigDecimal(20.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPAMM, new BigDecimal(3.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPRIC, new BigDecimal(20.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOST, new BigDecimal(6.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSPERCSCOST, new BigDecimal(0.1100)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSDOMLIQANNOPREC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = sanzione.eseguiPasso(dati);

		assertEquals("DUT_021", res.getCodiceEsito());
	}

	@Test
	@Transactional
	public void dut023() throws Exception {


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

		esitiInput.add(new EsitoControllo(TipoControllo.BRIDUSDC021_idDomandaSanzioni, "inf_10"));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSYCSANZANNIPREC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSYCIMPSANZAPREC, new BigDecimal(20.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPAMM, new BigDecimal(3.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPRIC, new BigDecimal(20.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOST, new BigDecimal(6.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSPERCSCOST, new BigDecimal(0.0300)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSDOMLIQANNOPREC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = sanzione.eseguiPasso(dati);

		assertEquals("DUT_023", res.getCodiceEsito());
	}

	@Test
	@Transactional
	public void dut022() throws Exception {


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

		esitiInput.add(new EsitoControllo(TipoControllo.BRIDUSDC021_idDomandaSanzioni, "inf_10"));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSYCSANZANNIPREC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSYCIMPSANZAPREC, new BigDecimal(0.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPAMM, new BigDecimal(3.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPRIC, new BigDecimal(20.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOST, new BigDecimal(6.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSPERCSCOST, new BigDecimal(0.0300)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSDOMLIQANNOPREC, true));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = sanzione.eseguiPasso(dati);

		assertEquals("DUT_022", res.getCodiceEsito());
	}

	@Test
	@Transactional
	public void duf027() throws Exception {


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

		esitiInput.add(new EsitoControllo(TipoControllo.BRIDUSDC021_idDomandaSanzioni, "inf_10"));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSYCSANZANNIPREC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSYCIMPSANZAPREC, new BigDecimal(0.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, new BigDecimal(10.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPAMM, new BigDecimal(3.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSIMPRIC, new BigDecimal(20.00)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPSCOST, new BigDecimal(6.0000)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSPERCSCOST, new BigDecimal(0.0300)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSDOMLIQANNOPREC, false));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = sanzione.eseguiPasso(dati);

		assertEquals("DUF_027", res.getCodiceEsito());
	}
}
