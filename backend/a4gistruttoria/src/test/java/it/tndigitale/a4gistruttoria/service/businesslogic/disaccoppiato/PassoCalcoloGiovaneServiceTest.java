package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import static org.junit.Assert.assertEquals;

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
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PassoCalcoloGiovaneServiceTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private PassoCalcoloGiovaneService calcoloGiovane;

	@Autowired
	private TransizioneIstruttoriaDao daoTransizioneSostegno;

	@Autowired
	private StatoLavSostegnoDao daoStatoLavSostegno;

	@Autowired
	private DomandaUnicaDao daoDomanda;

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	IstruttoriaDao daoIstruttoria;


	BigDecimal numeroDomanda = new BigDecimal(183109);

	BigDecimal GIOIMPRIC = new BigDecimal(525.69).setScale(2, RoundingMode.HALF_UP);
	BigDecimal GIOSUPRIC = new BigDecimal(10.3).setScale(4, RoundingMode.HALF_UP);
	BigDecimal TITVAL = new BigDecimal(204.15).setScale(4, RoundingMode.HALF_UP);
	BigDecimal GIOPERC = new BigDecimal(0.25).setScale(2, RoundingMode.HALF_UP);
	BigDecimal GRESUPRIDIST = new BigDecimal(0.1000).setScale(4, RoundingMode.HALF_UP);
	List<EsitoControllo> esitiInput = new ArrayList<>();

	@Test
	@Transactional
	public void algoritmoNoGiovane() throws Exception {
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

		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, new Boolean(false)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOYCSANZANNIPREC, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOYCIMPSANZAPREC, new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = calcoloGiovane.eseguiPasso(dati);

		assertEquals(CodiceEsito.DUT_034.getCodiceEsito(), res.getCodiceEsito());
		List<VariabileCalcolo> variabiliOutput = mapper.readValue(res.getDatiOutput(), DatiOutput.class).getVariabiliCalcolo();
		assertEquals(0, variabiliOutput.size());

	}

	@Test
	@Transactional
	public void AlgoritmoGiovaneNoRequisiti() throws Exception {
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

		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, GIOPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, TITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIORID, TITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.REQGIOVANE, new Boolean(false)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOSUPRIC, GIOSUPRIC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPRIC, GIOIMPRIC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, new Boolean(true)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, GIOSUPRIC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOYCSANZANNIPREC, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOYCIMPSANZAPREC, new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = calcoloGiovane.eseguiPasso(dati);

		assertEquals(CodiceEsito.DUT_035.getCodiceEsito(), res.getCodiceEsito());
		List<VariabileCalcolo> variabiliOutput = mapper.readValue(res.getDatiOutput(), DatiOutput.class).getVariabiliCalcolo();
		assertEquals(3, variabiliOutput.size());

	}

	@Test
	@Transactional
	public void AlgoritmoGiovaneSenzaRiduzioni() throws Exception {
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

		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		// Variabili caricate da algoritmo ammissibilita
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, GIOPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, TITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIORID, TITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.REQGIOVANE, new Boolean(true)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOSUPRIC, GIOSUPRIC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPRIC, GIOIMPRIC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, new Boolean(true)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, GIOSUPRIC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOYCSANZANNIPREC, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOYCIMPSANZAPREC, new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = calcoloGiovane.eseguiPasso(dati);
		assertEquals(CodiceEsito.DUT_036.getCodiceEsito(), res.getCodiceEsito());

		List<VariabileCalcolo> variabiliOutput = mapper.readValue(res.getDatiOutput(), DatiOutput.class).getVariabiliCalcolo();
		assertEquals(5, variabiliOutput.size());

	}

	@Test
	@Transactional
	public void algoritmoGiovaneSanzionatoScostamentoMaggiore10Perc() throws Exception {
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

		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, new Boolean(true)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.REQGIOVANE, new Boolean(true)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOSUPRIC, GIOSUPRIC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPRIC, GIOIMPRIC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, TITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIORID, TITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, GIOPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, new BigDecimal(7.00).setScale(2, RoundingMode.HALF_UP)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOYCSANZANNIPREC, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOYCIMPSANZAPREC, new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = calcoloGiovane.eseguiPasso(dati);
		assertEquals(CodiceEsito.DUT_039.getCodiceEsito(), res.getCodiceEsito());

		List<VariabileCalcolo> variabiliOutput = mapper.readValue(res.getDatiOutput(), DatiOutput.class).getVariabiliCalcolo();
		assertEquals(9, variabiliOutput.size());

	}

	@Test
	@Transactional
	public void algoritmoGiovaneSanzionatoYC() throws Exception {
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

		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, new Boolean(true)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.REQGIOVANE, new Boolean(true)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOSUPRIC, GIOSUPRIC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPRIC, GIOIMPRIC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, TITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIORID, TITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, GIOPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, new BigDecimal(9.80).setScale(2, RoundingMode.HALF_UP)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOYCSANZANNIPREC, false));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOYCIMPSANZAPREC, new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = calcoloGiovane.eseguiPasso(dati);
		assertEquals(CodiceEsito.DUT_038.getCodiceEsito(), res.getCodiceEsito());

		List<VariabileCalcolo> variabiliOutput = mapper.readValue(res.getDatiOutput(), DatiOutput.class).getVariabiliCalcolo();
		assertEquals(9, variabiliOutput.size());

	}

	@Test
	@Transactional
	public void algoritmoGiovaneSanzionatoConRecidiva() throws Exception {
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

		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIORIC, new Boolean(true)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.REQGIOVANE, new Boolean(true)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOSUPRIC, GIOSUPRIC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOIMPRIC, GIOIMPRIC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, TITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALGIORID, TITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOPERC, GIOPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, new BigDecimal(7.00).setScale(2, RoundingMode.HALF_UP)));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOYCSANZANNIPREC, true));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GIOYCIMPSANZAPREC, new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP)));

		istruttoria = daoIstruttoria.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = calcoloGiovane.eseguiPasso(dati);
		assertEquals(CodiceEsito.DUT_040.getCodiceEsito(), res.getCodiceEsito());

		List<VariabileCalcolo> variabiliOutput = mapper.readValue(res.getDatiOutput(), DatiOutput.class).getVariabiliCalcolo();
		assertEquals(9, variabiliOutput.size());

	}

}
