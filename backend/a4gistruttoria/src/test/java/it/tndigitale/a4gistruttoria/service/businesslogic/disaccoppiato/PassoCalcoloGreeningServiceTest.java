package it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.KeyValueStringString;
import it.tndigitale.a4gistruttoria.dto.Particella;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
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
import it.tndigitale.a4gistruttoria.util.CodiceEsito;
import it.tndigitale.a4gistruttoria.util.TipoControllo;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PassoCalcoloGreeningServiceTest {

	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	PassoCalcoloGreeningService greening;

	@Autowired
	TransizioneIstruttoriaDao daoTransizioneSostegno;

	@Autowired
	private StatoLavSostegnoDao daoStatoLavSostegno;

	@Autowired
	private DomandaUnicaDao daoDomanda;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	IstruttoriaDao istruttoriaDao;
	// new

	// GREPERC
	BigDecimal gREPERC = BigDecimal.valueOf(0.5).setScale(2, RoundingMode.HALF_UP);

	// GREPERCPES
	BigDecimal gREPERCPES = BigDecimal.valueOf(0.85).setScale(2, RoundingMode.HALF_UP);
	// TITVALRID
	BigDecimal tITVAL = BigDecimal.valueOf(0.1000).setScale(4, RoundingMode.HALF_UP);
	// GRESUPRIDRIST
	BigDecimal gRESUPRIDIST = BigDecimal.valueOf(0.1000).setScale(4, RoundingMode.HALF_UP);

	Double double10000 = Double.valueOf(10000);

	Map<CodiceEsito, KeyValueStringString> esitiRes = new HashMap<CodiceEsito, KeyValueStringString>();

	public void init() {
		esitiRes = new HashMap<CodiceEsito, KeyValueStringString>();
		esitiRes.put(CodiceEsito.DUT_024, new KeyValueStringString(TipoControllo.BRIDUSDC023_infoGreening.name(), "false"));

		esitiRes.put(CodiceEsito.DUT_024, new KeyValueStringString(TipoControllo.BRIDUSDC023_infoGreening.name(), "false"));

		esitiRes.put(CodiceEsito.DUT_025, new KeyValueStringString(TipoControllo.BRIDUSDC023_infoGreening.name(), "true"));
		esitiRes.put(CodiceEsito.DUT_025, new KeyValueStringString(TipoControllo.BRIDUSDC024_aziendaBiologica.name(), "true"));

		esitiRes.put(CodiceEsito.DUT_026, new KeyValueStringString(TipoControllo.BRIDUSDC023_infoGreening.name(), "true"));
		esitiRes.put(CodiceEsito.DUT_026, new KeyValueStringString(TipoControllo.BRIDUSDC024_aziendaBiologica.name(), "false"));
		esitiRes.put(CodiceEsito.DUT_026, new KeyValueStringString(TipoControllo.BRIDUSDC025_impegniGreening.name(), PassoCalcoloGreeningService.esitoNoImpegni));

		esitiRes.put(CodiceEsito.DUT_027, new KeyValueStringString(TipoControllo.BRIDUSDC023_infoGreening.name(), "true"));
		esitiRes.put(CodiceEsito.DUT_027, new KeyValueStringString(TipoControllo.BRIDUSDC024_aziendaBiologica.name(), "false"));
		esitiRes.put(CodiceEsito.DUT_027, new KeyValueStringString(TipoControllo.BRIDUSDC025_impegniGreening.name(), PassoCalcoloGreeningService.esitoDiversificazione));
		esitiRes.put(CodiceEsito.DUT_027, new KeyValueStringString(TipoControllo.BRIDUSDC033_esenzioneDiversificazione.name(), "true"));

		esitiRes.put(CodiceEsito.DUT_028, new KeyValueStringString(TipoControllo.BRIDUSDC023_infoGreening.name(), "true"));
		esitiRes.put(CodiceEsito.DUT_028, new KeyValueStringString(TipoControllo.BRIDUSDC024_aziendaBiologica.name(), "false"));
		esitiRes.put(CodiceEsito.DUT_028, new KeyValueStringString(TipoControllo.BRIDUSDC025_impegniGreening.name(), PassoCalcoloGreeningService.esitoDiversificazione));
		esitiRes.put(CodiceEsito.DUT_028, new KeyValueStringString(TipoControllo.BRIDUSDC033_esenzioneDiversificazione.name(), "false"));
		esitiRes.put(CodiceEsito.DUT_028, new KeyValueStringString(TipoControllo.BRIDUSDC026_riduzioniGreening.name(), "false"));

		esitiRes.put(CodiceEsito.DUT_029, new KeyValueStringString(TipoControllo.BRIDUSDC023_infoGreening.name(), "true"));
		esitiRes.put(CodiceEsito.DUT_029, new KeyValueStringString(TipoControllo.BRIDUSDC024_aziendaBiologica.name(), "false"));
		esitiRes.put(CodiceEsito.DUT_029, new KeyValueStringString(TipoControllo.BRIDUSDC025_impegniGreening.name(), PassoCalcoloGreeningService.esitoDiversificazione));
		esitiRes.put(CodiceEsito.DUT_029, new KeyValueStringString(TipoControllo.BRIDUSDC033_esenzioneDiversificazione.name(), "false"));
		esitiRes.put(CodiceEsito.DUT_029, new KeyValueStringString(TipoControllo.BRIDUSDC026_riduzioniGreening.name(), "true"));
		esitiRes.put(CodiceEsito.DUT_029, new KeyValueStringString(TipoControllo.BRIDUSDC055_sanzioneGreening.name(), "false"));

		esitiRes.put(CodiceEsito.DUT_030, new KeyValueStringString(TipoControllo.BRIDUSDC023_infoGreening.name(), "true"));
		esitiRes.put(CodiceEsito.DUT_030, new KeyValueStringString(TipoControllo.BRIDUSDC024_aziendaBiologica.name(), "false"));
		esitiRes.put(CodiceEsito.DUT_030, new KeyValueStringString(TipoControllo.BRIDUSDC025_impegniGreening.name(), PassoCalcoloGreeningService.esitoDiversificazione));
		esitiRes.put(CodiceEsito.DUT_030, new KeyValueStringString(TipoControllo.BRIDUSDC033_esenzioneDiversificazione.name(), "false"));
		esitiRes.put(CodiceEsito.DUT_030, new KeyValueStringString(TipoControllo.BRIDUSDC026_riduzioniGreening.name(), "true"));
		esitiRes.put(CodiceEsito.DUT_030, new KeyValueStringString(TipoControllo.BRIDUSDC055_sanzioneGreening.name(), "true"));

		esitiRes.put(CodiceEsito.DUT_031, new KeyValueStringString(TipoControllo.BRIDUSDC023_infoGreening.name(), "true"));
		esitiRes.put(CodiceEsito.DUT_031, new KeyValueStringString(TipoControllo.BRIDUSDC024_aziendaBiologica.name(), "false"));
		esitiRes.put(CodiceEsito.DUT_031, new KeyValueStringString(TipoControllo.BRIDUSDC025_impegniGreening.name(), PassoCalcoloGreeningService.esitoDiversificazioneEfa));
		esitiRes.put(CodiceEsito.DUT_031, new KeyValueStringString(TipoControllo.BRIDUSDC026_riduzioniGreening.name(), "false"));

		esitiRes.put(CodiceEsito.DUT_032, new KeyValueStringString(TipoControllo.BRIDUSDC023_infoGreening.name(), "true"));
		esitiRes.put(CodiceEsito.DUT_032, new KeyValueStringString(TipoControllo.BRIDUSDC024_aziendaBiologica.name(), "false"));
		esitiRes.put(CodiceEsito.DUT_032, new KeyValueStringString(TipoControllo.BRIDUSDC025_impegniGreening.name(), PassoCalcoloGreeningService.esitoDiversificazioneEfa));
		esitiRes.put(CodiceEsito.DUT_032, new KeyValueStringString(TipoControllo.BRIDUSDC026_riduzioniGreening.name(), "true"));
		esitiRes.put(CodiceEsito.DUT_032, new KeyValueStringString(TipoControllo.BRIDUSDC055_sanzioneGreening.name(), "false"));

		esitiRes.put(CodiceEsito.DUT_033, new KeyValueStringString(TipoControllo.BRIDUSDC023_infoGreening.name(), "true"));
		esitiRes.put(CodiceEsito.DUT_033, new KeyValueStringString(TipoControllo.BRIDUSDC024_aziendaBiologica.name(), "false"));
		esitiRes.put(CodiceEsito.DUT_033, new KeyValueStringString(TipoControllo.BRIDUSDC025_impegniGreening.name(), PassoCalcoloGreeningService.esitoDiversificazioneEfa));
		esitiRes.put(CodiceEsito.DUT_033, new KeyValueStringString(TipoControllo.BRIDUSDC026_riduzioniGreening.name(), "true"));
		esitiRes.put(CodiceEsito.DUT_033, new KeyValueStringString(TipoControllo.BRIDUSDC055_sanzioneGreening.name(), "true"));
	}

	@Test
	@Transactional
	public void dut025() throws Exception {
		boolean gREAZIBIO = true;
		CodiceEsito codEsito = CodiceEsito.DUT_025;

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = istruttoriaDao.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = daoTransizioneSostegno.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, gREPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, tITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRESUPRIDIST, gRESUPRIDIST));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));

		// 1. Superfici per particella/coltura

		// 1.1 Superfici impegnate
		ArrayList<ParticellaColtura> supImpegnate = new ArrayList<>();

		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "112", 0.5 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "113", 0.5 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "114", 1000));

		BigDecimal bPSSUPAMM = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(1));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, bPSSUPAMM));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPDET, supImpegnate));

		List<TipoVariabile> presenzaVariabili = Arrays.asList(TipoVariabile.GREAZIBIO, TipoVariabile.GRESUPBASE, TipoVariabile.GREIMPBASE, TipoVariabile.GRESUPAMM, TipoVariabile.GREIMPAMM);

		istruttoria = istruttoriaDao.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = greening.eseguiPasso(dati);

		assertEquals("OK", res.getEsito());

		assertEquals(codEsito.getCodiceEsito(), res.getCodiceEsito());

		List<VariabileCalcolo> variabiliOutput = checkNumerositaOutput(res, presenzaVariabili);

		checkOutput(variabiliInput, supImpegnate, bPSSUPAMM, variabiliOutput);

		checkEsiti(codEsito, res);
	}

	@Test
	@Transactional
	public void dut026() throws Exception {
		boolean gREAZIBIO = false;
		CodiceEsito codEsito = CodiceEsito.DUT_026;

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = istruttoriaDao.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = daoTransizioneSostegno.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();


		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, gREPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, tITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRESUPRIDIST, gRESUPRIDIST));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));

		// 1. Superfici per particella/coltura

		// 1.1 Superfici impegnate
		ArrayList<ParticellaColtura> supImpegnate = new ArrayList<ParticellaColtura>();

		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "112", 0.5 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "113", 0.5 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "114", 1000));

		BigDecimal bPSSUPAMM = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(1));
		BigDecimal bPSSUPDET = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(2));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, bPSSUPAMM));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDET, bPSSUPDET));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPDET, supImpegnate));

		List<TipoVariabile> presenzaVariabili = Arrays.asList(TipoVariabile.GRESUPARB, TipoVariabile.GRESUPDET, TipoVariabile.GRESUPPP, TipoVariabile.GRESUPSEM, TipoVariabile.GREESEBIO,
				TipoVariabile.GREESESEM, TipoVariabile.GRESUPBASE, TipoVariabile.GREIMPBASE, TipoVariabile.GRESUPAMM, TipoVariabile.GREIMPAMM);

		istruttoria = istruttoriaDao.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = greening.eseguiPasso(dati);

		assertEquals("OK", res.getEsito());

		assertEquals(codEsito.getCodiceEsito(), res.getCodiceEsito());

		List<VariabileCalcolo> variabiliOutput = checkNumerositaOutput(res, presenzaVariabili);

		checkOutput(variabiliInput, supImpegnate, bPSSUPAMM, variabiliOutput);

		List<TipoVariabile> presenzaVariabiliSintesi = Arrays.asList(TipoVariabile.PFSUPDETARB, TipoVariabile.PFSUPDETPP, TipoVariabile.PFSUPDETSEM);

		List<VariabileCalcolo> variabiliSintesi = checkNumerositaSintesi(res, presenzaVariabiliSintesi);

		checkOutput(variabiliInput, supImpegnate, bPSSUPAMM, variabiliSintesi);

		checkEsiti(codEsito, res);
	}

	@Test
	@Transactional
	public void dut027() throws Exception {
		boolean gREAZIBIO = false;
		CodiceEsito codEsito = CodiceEsito.DUT_027;

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = istruttoriaDao.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = daoTransizioneSostegno.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, gREPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, tITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRESUPRIDIST, gRESUPRIDIST));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));

		// 1. Superfici per particella/coltura

		// 1.1 Superfici impegnate

		ArrayList<ParticellaColtura> supImpegnate = new ArrayList<ParticellaColtura>();

		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "112", 5 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "113", 5 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "114", double10000));

		BigDecimal bPSSUPAMM = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(1));
		BigDecimal bPSSUPDET = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(2));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, bPSSUPAMM));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDET, bPSSUPDET));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPDET, supImpegnate));

		List<TipoVariabile> presenzaVariabili = Arrays.asList(TipoVariabile.GRESUPDET, TipoVariabile.GRESUPARB, TipoVariabile.GRESUPPP, TipoVariabile.GRESUPSEM, TipoVariabile.GRESUPERBAI,
				TipoVariabile.GRESUPLEGUM, TipoVariabile.GRESUPRIPOSO, TipoVariabile.GRESUPSOMM, TipoVariabile.GREDIV1COL, TipoVariabile.GREESEBIO, TipoVariabile.GREESESEM, TipoVariabile.GREPERCPES,
				TipoVariabile.GREPERCELR, TipoVariabile.GREESEDIV, TipoVariabile.GREDIVSUP1COL, TipoVariabile.GRESUPBASE, TipoVariabile.GREIMPBASE, TipoVariabile.GRESUPAMM, TipoVariabile.GREIMPAMM,
				TipoVariabile.GREDIVFGS);

		istruttoria = istruttoriaDao.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = greening.eseguiPasso(dati);

		assertEquals("OK", res.getEsito());

		assertEquals(codEsito.getCodiceEsito(), res.getCodiceEsito());

		List<VariabileCalcolo> variabiliOutput = checkNumerositaOutput(res, presenzaVariabili);

		checkOutput(variabiliInput, supImpegnate, bPSSUPAMM, variabiliOutput);

		List<TipoVariabile> presenzaVariabiliSintesi = Arrays.asList(TipoVariabile.PFSUPDETARB, TipoVariabile.PFSUPDETPP, TipoVariabile.PFSUPDETSEM, TipoVariabile.PFSUPDETERBAI,
				TipoVariabile.PFSUPDETLEGUM, TipoVariabile.PFSUPDETRIPOSO, TipoVariabile.PFSUPDETSOMM, TipoVariabile.PFSUPDETPRIMA);

		List<VariabileCalcolo> variabiliSintesi = checkNumerositaSintesi(res, presenzaVariabiliSintesi);

		checkOutput(variabiliInput, supImpegnate, bPSSUPAMM, variabiliSintesi);

		checkEsiti(codEsito, res);
	}

	@Test
	@Transactional
	public void dut028() throws Exception {
		boolean gREAZIBIO = false;
		CodiceEsito codEsito = CodiceEsito.DUT_028;

		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = istruttoriaDao.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = daoTransizioneSostegno.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, gREPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, tITVAL));
		// variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRESUPRIDIST, GRESUPRIDIST));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));

		// 1. Superfici per particella/coltura

		// 1.1 Superfici impegnate
		ArrayList<ParticellaColtura> supImpegnate = new ArrayList<ParticellaColtura>();

		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "112", 2 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-000-000", "140", 4 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-000-000", "113", 3 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "114", 6 * double10000));

		BigDecimal bPSSUPAMM = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(1));
		BigDecimal bPSSUPDET = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(2));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, bPSSUPAMM));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDET, bPSSUPDET));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPDET, supImpegnate));

		List<TipoVariabile> presenzaVariabili = Arrays.asList(TipoVariabile.GRESUPDET, TipoVariabile.GRESUPARB, TipoVariabile.GRESUPPP, TipoVariabile.GRESUPSEM, TipoVariabile.GRESUPERBAI,
				TipoVariabile.GRESUPLEGUM, TipoVariabile.GRESUPRIPOSO, TipoVariabile.GRESUPSOMM, TipoVariabile.GREDIV1COL, TipoVariabile.GREESEBIO, TipoVariabile.GREESESEM, TipoVariabile.GREPERCPES,
				TipoVariabile.GREPERCELR, TipoVariabile.GREESEDIV, TipoVariabile.GREDIVSUP1COL, TipoVariabile.GRESUPBASE, TipoVariabile.GREIMPBASE, TipoVariabile.GRESUPRID, TipoVariabile.GREIMPRID,
				TipoVariabile.GREPERCSCOST, TipoVariabile.GRESUPAMM, TipoVariabile.GREIMPAMM, TipoVariabile.GREDIVPERC1COL, TipoVariabile.GREDIVSUPRID, TipoVariabile.GREDIVFGS);

		istruttoria = istruttoriaDao.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = greening.eseguiPasso(dati);

		assertEquals("OK", res.getEsito());

		assertEquals(codEsito.getCodiceEsito(), res.getCodiceEsito());

		List<VariabileCalcolo> variabiliOutput = checkNumerositaOutput(res, presenzaVariabili);

		checkOutput(variabiliInput, supImpegnate, bPSSUPAMM, variabiliOutput);

		List<TipoVariabile> presenzaVariabiliSintesi = Arrays.asList(TipoVariabile.PFSUPDETARB, TipoVariabile.PFSUPDETPP, TipoVariabile.PFSUPDETSEM, TipoVariabile.PFSUPDETERBAI,
				TipoVariabile.PFSUPDETLEGUM, TipoVariabile.PFSUPDETRIPOSO, TipoVariabile.PFSUPDETSOMM, TipoVariabile.PFSUPDETPRIMA);

		List<VariabileCalcolo> variabiliSintesi = checkNumerositaSintesi(res, presenzaVariabiliSintesi);

		checkOutput(variabiliInput, supImpegnate, bPSSUPAMM, variabiliSintesi);

		checkEsiti(codEsito, res);
	}

	@Test
	@Transactional
	public void dut029() throws Exception {
		boolean gREAZIBIO = false;
		CodiceEsito codEsito = CodiceEsito.DUT_029;


		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = istruttoriaDao.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = daoTransizioneSostegno.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, gREPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, tITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRESUPRIDIST, gRESUPRIDIST));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));

		// 1. Superfici per particella/coltura

		// 1.1 Superfici impegnate
		ArrayList<ParticellaColtura> supImpegnate = new ArrayList<ParticellaColtura>();

		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "112", 1 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "113", 1 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "114", double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "119", 9 * double10000 - 1000));

		BigDecimal bPSSUPAMM = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(1));
		BigDecimal bPSSUPDET = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(2));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, bPSSUPAMM));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDET, bPSSUPDET));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPDET, supImpegnate));

		List<TipoVariabile> presenzaVariabili = Arrays.asList(TipoVariabile.GRESUPDET, TipoVariabile.GRESUPARB, TipoVariabile.GRESUPPP, TipoVariabile.GRESUPSEM, TipoVariabile.GRESUPERBAI,
				TipoVariabile.GRESUPLEGUM, TipoVariabile.GRESUPRIPOSO, TipoVariabile.GRESUPSOMM, TipoVariabile.GREDIV1COL, TipoVariabile.GREESEBIO, TipoVariabile.GREESESEM, TipoVariabile.GREPERCPES,
				TipoVariabile.GREPERCELR, TipoVariabile.GREESEDIV, TipoVariabile.GREDIVSUP1COL, TipoVariabile.GRESUPBASE, TipoVariabile.GREIMPBASE, TipoVariabile.GRESUPRID, TipoVariabile.GREIMPRID,
				TipoVariabile.GREPERCSCOST, TipoVariabile.GRESUPAMM, TipoVariabile.GREIMPAMM, TipoVariabile.GREDIVPERC1COL, TipoVariabile.GREDIVSUPRID, TipoVariabile.GREDIVFGS);

		istruttoria = istruttoriaDao.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = greening.eseguiPasso(dati);

		assertEquals("OK", res.getEsito());

		assertEquals(codEsito.getCodiceEsito(), res.getCodiceEsito());

		List<VariabileCalcolo> variabiliOutput = checkNumerositaOutput(res, presenzaVariabili);

		checkOutput(variabiliInput, supImpegnate, bPSSUPAMM, variabiliOutput);

		List<TipoVariabile> presenzaVariabiliSintesi = Arrays.asList(TipoVariabile.PFSUPDETARB, TipoVariabile.PFSUPDETPP, TipoVariabile.PFSUPDETSEM, TipoVariabile.PFSUPDETERBAI,
				TipoVariabile.PFSUPDETLEGUM, TipoVariabile.PFSUPDETRIPOSO, TipoVariabile.PFSUPDETSOMM, TipoVariabile.PFSUPDETPRIMA);

		List<VariabileCalcolo> variabiliSintesi = checkNumerositaSintesi(res, presenzaVariabiliSintesi);

		checkOutput(variabiliInput, supImpegnate, bPSSUPAMM, variabiliSintesi);

		checkEsiti(codEsito, res);
	}

	@Test
	@Transactional
	public void dut030() throws Exception {
		boolean gREAZIBIO = false;
		CodiceEsito codEsito = CodiceEsito.DUT_030;// CodiceEsito.DUT_030;


		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = istruttoriaDao.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = daoTransizioneSostegno.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, gREPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, tITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRESUPRIDIST, BigDecimal.valueOf(3)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));

		// 1. Superfici per particella/coltura

		// 1.1 Superfici impegnate
		ArrayList<ParticellaColtura> supImpegnate = new ArrayList<ParticellaColtura>();

		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "112", 1 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "113", 1 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "114", double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "119", 9 * double10000 - 1000));

		BigDecimal bPSSUPAMM = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(1));
		BigDecimal bPSSUPDET = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(2));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, bPSSUPAMM));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDET, bPSSUPDET));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPDET, supImpegnate));

		List<TipoVariabile> presenzaVariabili = Arrays.asList(TipoVariabile.GRESUPDET, TipoVariabile.GRESUPARB, TipoVariabile.GRESUPPP, TipoVariabile.GRESUPSEM, TipoVariabile.GRESUPERBAI,
				TipoVariabile.GRESUPLEGUM, TipoVariabile.GRESUPRIPOSO, TipoVariabile.GRESUPSOMM, TipoVariabile.GREDIV1COL, TipoVariabile.GREESEBIO, TipoVariabile.GREESESEM, TipoVariabile.GREPERCPES,
				TipoVariabile.GREPERCELR, TipoVariabile.GREESEDIV, TipoVariabile.GREDIVSUP1COL, TipoVariabile.GRESUPBASE, TipoVariabile.GREIMPBASE, TipoVariabile.GRESUPRID, TipoVariabile.GREIMPRID,
				TipoVariabile.GREPERCSCOST, TipoVariabile.GRESUPAMM, TipoVariabile.GREIMPAMM, TipoVariabile.GREDIVPERC1COL, TipoVariabile.GREDIVSUPRID, TipoVariabile.GREDIVFGS,
				TipoVariabile.GREIMPSANZ);

		istruttoria = istruttoriaDao.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = greening.eseguiPasso(dati);

		assertEquals("OK", res.getEsito());

		assertEquals(codEsito.getCodiceEsito(), res.getCodiceEsito());

		List<VariabileCalcolo> variabiliOutput = checkNumerositaOutput(res, presenzaVariabili);

		checkOutput(variabiliInput, supImpegnate, bPSSUPAMM, variabiliOutput);

		List<TipoVariabile> presenzaVariabiliSintesi = Arrays.asList(TipoVariabile.PFSUPDETARB, TipoVariabile.PFSUPDETPP, TipoVariabile.PFSUPDETSEM, TipoVariabile.PFSUPDETERBAI,
				TipoVariabile.PFSUPDETLEGUM, TipoVariabile.PFSUPDETRIPOSO, TipoVariabile.PFSUPDETSOMM, TipoVariabile.PFSUPDETPRIMA);

		List<VariabileCalcolo> variabiliSintesi = checkNumerositaSintesi(res, presenzaVariabiliSintesi);

		checkOutput(variabiliInput, supImpegnate, bPSSUPAMM, variabiliSintesi);

		checkEsiti(codEsito, res);
	}

	@Test
	@Transactional
	public void dut070() throws Exception {
		boolean gREAZIBIO = false;

		CodiceEsito codEsito = CodiceEsito.DUT_070;// CodiceEsito.DUT_070;


		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = istruttoriaDao.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = daoTransizioneSostegno.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, gREPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, tITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRESUPRIDIST, BigDecimal.valueOf(3)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));

		// 1. Superfici per particella/coltura

		// 1.1 Superfici impegnate
		ArrayList<ParticellaColtura> supImpegnate = new ArrayList<ParticellaColtura>();

		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"211-163-000", "112", 7 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "113", 1 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "114", double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "119", 9 * double10000 - 1000));

		BigDecimal bPSSUPAMM = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(1));
		BigDecimal bPSSUPDET = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(10));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, bPSSUPAMM));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDET, bPSSUPDET));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREFPAZOTO, BigDecimal.ONE));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPIMP, supImpegnate));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPDET, supImpegnate));

		// Inserico tutte le variabili id/domanda che avrò in output
		List<TipoVariabile> presenzaVariabili = Arrays.asList(TipoVariabile.GRESUPDET, TipoVariabile.GRESUPARB, TipoVariabile.GRESUPPP, TipoVariabile.GRESUPSEM, TipoVariabile.GRESUPERBAI,
				TipoVariabile.GRESUPLEGUM, TipoVariabile.GRESUPRIPOSO, TipoVariabile.GRESUPSOMM, TipoVariabile.GREDIV1COL, TipoVariabile.GREESEBIO, TipoVariabile.GREESESEM, TipoVariabile.GREPERCPES,
				TipoVariabile.GREPERCELR, TipoVariabile.GREESEDIV, TipoVariabile.GRESUPAZOTOPOND, TipoVariabile.GRESUPBASE, TipoVariabile.GREIMPBASE, TipoVariabile.GRESUPAMM, TipoVariabile.GREIMPAMM,
				TipoVariabile.GREDIVFGS, TipoVariabile.GREESEEFA, TipoVariabile.GRESUPAZOTODIC, TipoVariabile.GRESUPAZOTODET, TipoVariabile.GREDIV1COL);

		// esegui il passo di lavorazione
		istruttoria = istruttoriaDao.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = greening.eseguiPasso(dati);

		// verifica esito
		assertEquals("OK", res.getEsito());

		assertEquals(codEsito.getCodiceEsito(), res.getCodiceEsito());

		// verifica che le variabili id/domanda inserite in precedenza coincidano con quelle restituite in output dal passo di lavorazione
		List<VariabileCalcolo> variabiliOutput = checkNumerositaOutput(res, presenzaVariabili);

		checkOutputEFA(variabiliInput, supImpegnate, bPSSUPAMM, variabiliOutput);

		// Inserisco tutte le variabili particella/coltura che avrò in output
		List<TipoVariabile> presenzaVariabiliSintesi = Arrays.asList(TipoVariabile.PFSUPDETARB, TipoVariabile.PFSUPDETPP, TipoVariabile.PFSUPDETSEM, TipoVariabile.PFSUPDETERBAI,
				TipoVariabile.PFSUPDETLEGUM, TipoVariabile.PFSUPDETRIPOSO, TipoVariabile.PFSUPDETSOMM, TipoVariabile.PFSUPDETPRIMA, TipoVariabile.PFSUPDETSECONDA, TipoVariabile.PFAZOTO);

		// verifica che le variabili particella/colture inserite in precedenza coincidano con quelle restituite in output dal passo di lavorazione
		List<VariabileCalcolo> variabiliSintesi = checkNumerositaSintesi(res, presenzaVariabiliSintesi);

		checkOutputEFA(variabiliInput, supImpegnate, bPSSUPAMM, variabiliSintesi);

		checkEsiti(codEsito, res);

	}

	@Test
	@Transactional
	public void dut031() throws Exception {
		boolean gREAZIBIO = false;
		CodiceEsito codEsito = CodiceEsito.DUT_031;// CodiceEsito.DUT_031;


		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = istruttoriaDao.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = daoTransizioneSostegno.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, gREPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, tITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRESUPRIDIST, BigDecimal.valueOf(0)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));

		// 1. Superfici per particella/coltura

		// 1.1 Superfici impegnate
		ArrayList<ParticellaColtura> supImpegnate = new ArrayList<ParticellaColtura>();

		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"211-163-000", "112", 7 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "113", 1 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "114", double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "119", 9 * double10000 - 1000));

		BigDecimal bPSSUPAMM = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(1));
		BigDecimal bPSSUPDET = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(2));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, bPSSUPAMM));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDET, bPSSUPDET));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREFPAZOTO, BigDecimal.ONE));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPIMP, supImpegnate));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPDET, supImpegnate));

		// Inserico tutte le variabili id/domanda che avrò in output
		List<TipoVariabile> presenzaVariabili = Arrays.asList(TipoVariabile.GRESUPDET, TipoVariabile.GRESUPARB, TipoVariabile.GRESUPPP, TipoVariabile.GRESUPSEM, TipoVariabile.GRESUPERBAI,
				TipoVariabile.GRESUPLEGUM, TipoVariabile.GRESUPRIPOSO, TipoVariabile.GRESUPSOMM, TipoVariabile.GREDIV1COL, TipoVariabile.GREESEBIO, TipoVariabile.GREESESEM, TipoVariabile.GREPERCPES,
				TipoVariabile.GREPERCELR, TipoVariabile.GREESEDIV, TipoVariabile.GREDIVSUP1COL, TipoVariabile.GRESUPBASE, TipoVariabile.GREIMPBASE, TipoVariabile.GRESUPAMM, TipoVariabile.GREIMPAMM,
				TipoVariabile.GREDIVFGS, TipoVariabile.GREESEEFA, TipoVariabile.GRESUPAZOTODIC, TipoVariabile.GRESUPAZOTODET, TipoVariabile.GREDIVPERC1COL, TipoVariabile.GREDIVSUPRID,
				TipoVariabile.GREEFAPERCAZOTO, TipoVariabile.GREEFASUPRID, TipoVariabile.GRESUPRID, TipoVariabile.GREIMPRID, TipoVariabile.GREPERCSCOST, TipoVariabile.GRESUPAZOTOPOND);

		// esegui il passo di lavorazione
		istruttoria = istruttoriaDao.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = greening.eseguiPasso(dati);

		// verifica esito
		assertEquals("OK", res.getEsito());

		assertEquals(codEsito.getCodiceEsito(), res.getCodiceEsito());

		// verifica che le variabili id/domanda inserite in precedenza coincidano con quelle restituite in output dal passo di lavorazione
		List<VariabileCalcolo> variabiliOutput = checkNumerositaOutput(res, presenzaVariabili);

		checkOutputEFA(variabiliInput, supImpegnate, bPSSUPAMM, variabiliOutput);

		// Inserisco tutte le variabili particella/coltura che avrò in output
		List<TipoVariabile> presenzaVariabiliSintesi = Arrays.asList(TipoVariabile.PFSUPDETARB, TipoVariabile.PFSUPDETPP, TipoVariabile.PFSUPDETSEM, TipoVariabile.PFSUPDETERBAI,
				TipoVariabile.PFSUPDETLEGUM, TipoVariabile.PFSUPDETRIPOSO, TipoVariabile.PFSUPDETSOMM, TipoVariabile.PFSUPDETPRIMA, TipoVariabile.PFSUPDETSECONDA, TipoVariabile.PFAZOTO);

		// verifica che le variabili particella/colture inserite in precedenza coincidano con quelle restituite in output dal passo di lavorazione
		List<VariabileCalcolo> variabiliSintesi = checkNumerositaSintesi(res, presenzaVariabiliSintesi);

		checkOutputEFA(variabiliInput, supImpegnate, bPSSUPAMM, variabiliSintesi);

		checkEsiti(codEsito, res);

	}

	@Test
	@Transactional
	public void dut_032() throws Exception {
		boolean gREAZIBIO = false;
		CodiceEsito codEsito = CodiceEsito.DUT_032;// CodiceEsito.DUT_032;


		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = istruttoriaDao.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = daoTransizioneSostegno.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, gREPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, tITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRESUPRIDIST, BigDecimal.valueOf(1)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));

		// 1. Superfici per particella/coltura

		// 1.1 Superfici impegnate
		ArrayList<ParticellaColtura> supImpegnate = new ArrayList<ParticellaColtura>();

		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"211-163-000", "112", 7 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "113", 1 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "114", double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "119", 9 * double10000 - 1000));

		BigDecimal bPSSUPAMM = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(1));
		BigDecimal bPSSUPDET = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(2));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, bPSSUPAMM));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDET, bPSSUPDET));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREFPAZOTO, BigDecimal.ONE));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPIMP, supImpegnate));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPDET, supImpegnate));

		// Inserico tutte le variabili id/domanda che avrò in output
		List<TipoVariabile> presenzaVariabili = Arrays.asList(TipoVariabile.GRESUPDET, TipoVariabile.GRESUPARB, TipoVariabile.GRESUPPP, TipoVariabile.GRESUPSEM, TipoVariabile.GRESUPERBAI,
				TipoVariabile.GRESUPLEGUM, TipoVariabile.GRESUPRIPOSO, TipoVariabile.GRESUPSOMM, TipoVariabile.GREDIV1COL, TipoVariabile.GREESEBIO, TipoVariabile.GREESESEM, TipoVariabile.GREPERCPES,
				TipoVariabile.GREPERCELR, TipoVariabile.GREESEDIV, TipoVariabile.GREDIVSUP1COL, TipoVariabile.GRESUPBASE, TipoVariabile.GREIMPBASE, TipoVariabile.GRESUPAMM, TipoVariabile.GREIMPAMM,
				TipoVariabile.GREDIVFGS, TipoVariabile.GREESEEFA, TipoVariabile.GRESUPAZOTODIC, TipoVariabile.GRESUPAZOTODET, TipoVariabile.GREDIVPERC1COL, TipoVariabile.GREDIVSUPRID,
				TipoVariabile.GREEFAPERCAZOTO, TipoVariabile.GREEFASUPRID, TipoVariabile.GRESUPRID, TipoVariabile.GREIMPRID, TipoVariabile.GREPERCSCOST, TipoVariabile.GRESUPAZOTOPOND);

		// esegui il passo di lavorazione
		istruttoria = istruttoriaDao.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = greening.eseguiPasso(dati);

		// verifica esito
		assertEquals("OK", res.getEsito());

		assertEquals(codEsito.getCodiceEsito(), res.getCodiceEsito());

		// verifica che le variabili id/domanda inserite in precedenza coincidano con quelle restituite in output dal passo di lavorazione
		List<VariabileCalcolo> variabiliOutput = checkNumerositaOutput(res, presenzaVariabili);

		checkOutputEFA(variabiliInput, supImpegnate, bPSSUPAMM, variabiliOutput);

		// Inserisco tutte le variabili particella/coltura che avrò in output
		List<TipoVariabile> presenzaVariabiliSintesi = Arrays.asList(TipoVariabile.PFSUPDETARB, TipoVariabile.PFSUPDETPP, TipoVariabile.PFSUPDETSEM, TipoVariabile.PFSUPDETERBAI,
				TipoVariabile.PFSUPDETLEGUM, TipoVariabile.PFSUPDETRIPOSO, TipoVariabile.PFSUPDETSOMM, TipoVariabile.PFSUPDETPRIMA, TipoVariabile.PFSUPDETSECONDA, TipoVariabile.PFAZOTO);

		// verifica che le variabili particella/colture inserite in precedenza coincidano con quelle restituite in output dal passo di lavorazione
		List<VariabileCalcolo> variabiliSintesi = checkNumerositaSintesi(res, presenzaVariabiliSintesi);

		checkOutputEFA(variabiliInput, supImpegnate, bPSSUPAMM, variabiliSintesi);

		checkEsiti(codEsito, res);
	}

	@Test
	@Transactional
	public void dut_033() throws Exception {
		boolean gREAZIBIO = false;
		CodiceEsito codEsito = CodiceEsito.DUT_033;// CodiceEsito.DUT_033;


		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = istruttoriaDao.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = daoTransizioneSostegno.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, gREPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, tITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRESUPRIDIST, BigDecimal.valueOf(6)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));

		// 1. Superfici per particella/coltura

		// 1.1 Superfici impegnate
		ArrayList<ParticellaColtura> supImpegnate = new ArrayList<ParticellaColtura>();

		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"211-163-000", "112", 7 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "113", 1 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "114", double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "119", 9 * double10000 - 1000));

		BigDecimal bPSSUPAMM = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(1));
		BigDecimal bPSSUPDET = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(2));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, bPSSUPAMM));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDET, bPSSUPDET));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREFPAZOTO, BigDecimal.ONE));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPIMP, supImpegnate));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPDET, supImpegnate));

		// Inserico tutte le variabili id/domanda che avrò in output
		List<TipoVariabile> presenzaVariabili = Arrays.asList(TipoVariabile.GRESUPDET, TipoVariabile.GRESUPARB, TipoVariabile.GRESUPPP, TipoVariabile.GRESUPSEM, TipoVariabile.GRESUPERBAI,
				TipoVariabile.GRESUPLEGUM, TipoVariabile.GRESUPRIPOSO, TipoVariabile.GRESUPSOMM, TipoVariabile.GREDIV1COL, TipoVariabile.GREESEBIO, TipoVariabile.GREESESEM, TipoVariabile.GREPERCPES,
				TipoVariabile.GREPERCELR, TipoVariabile.GREESEDIV, TipoVariabile.GREDIVSUP1COL, TipoVariabile.GRESUPBASE, TipoVariabile.GREIMPBASE, TipoVariabile.GRESUPAMM, TipoVariabile.GREIMPAMM,
				TipoVariabile.GREDIVFGS, TipoVariabile.GREESEEFA, TipoVariabile.GRESUPAZOTODIC, TipoVariabile.GRESUPAZOTODET, TipoVariabile.GREDIVPERC1COL, TipoVariabile.GREDIVSUPRID,
				TipoVariabile.GREEFAPERCAZOTO, TipoVariabile.GREEFASUPRID, TipoVariabile.GRESUPRID, TipoVariabile.GREIMPRID, TipoVariabile.GREPERCSCOST, TipoVariabile.GREIMPSANZ,
				TipoVariabile.GRESUPAZOTOPOND);

		// esegui il passo di lavorazione
		istruttoria = istruttoriaDao.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = greening.eseguiPasso(dati);

		// verifica esito
		assertEquals("OK", res.getEsito());

		assertEquals(codEsito.getCodiceEsito(), res.getCodiceEsito());

		// verifica che le variabili id/domanda inserite in precedenza coincidano con quelle restituite in output dal passo di lavorazione
		List<VariabileCalcolo> variabiliOutput = checkNumerositaOutput(res, presenzaVariabili);

		checkOutputEFA(variabiliInput, supImpegnate, bPSSUPAMM, variabiliOutput);

		// Inserisco tutte le variabili particella/coltura che avrò in output
		List<TipoVariabile> presenzaVariabiliSintesi = Arrays.asList(TipoVariabile.PFSUPDETARB, TipoVariabile.PFSUPDETPP, TipoVariabile.PFSUPDETSEM, TipoVariabile.PFSUPDETERBAI,
				TipoVariabile.PFSUPDETLEGUM, TipoVariabile.PFSUPDETRIPOSO, TipoVariabile.PFSUPDETSOMM, TipoVariabile.PFSUPDETPRIMA, TipoVariabile.PFSUPDETSECONDA, TipoVariabile.PFAZOTO);

		// verifica che le variabili particella/colture inserite in precedenza coincidano con quelle restituite in output dal passo di lavorazione
		List<VariabileCalcolo> variabiliSintesi = checkNumerositaSintesi(res, presenzaVariabiliSintesi);

		checkOutputEFA(variabiliInput, supImpegnate, bPSSUPAMM, variabiliSintesi);

		checkEsiti(codEsito, res);
	}

	@Test
	@Transactional
	public void Seminativi30() throws Exception {
		boolean gREAZIBIO = false;
		CodiceEsito codEsito = CodiceEsito.DUT_032;// CodiceEsito.DUT_032;


		A4gdStatoLavSostegno statoIstruttoria = daoStatoLavSostegno.findByIdentificativo(StatoIstruttoria.RICHIESTO.getStatoIstruttoria());
		DomandaUnicaModel domanda = daoDomanda.findByNumeroDomanda(new BigDecimal(183109));
		
		IstruttoriaModel istruttoria = new IstruttoriaModel();
		istruttoria.setSostegno(Sostegno.DISACCOPPIATO);
		istruttoria.setA4gdStatoLavSostegno(statoIstruttoria);
		istruttoria.setDomandaUnicaModel(domanda);
		istruttoria.setTipologia(TipoIstruttoria.SALDO);
		istruttoria = istruttoriaDao.save(istruttoria);
		
		TransizioneIstruttoriaModel transizione = new TransizioneIstruttoriaModel();
		
		transizione.setA4gdStatoLavSostegno2(statoIstruttoria);
		transizione.setDataEsecuzione(new Date());
		transizione.setIstruttoria(istruttoria);
		transizione = daoTransizioneSostegno.save(transizione);
		List<EsitoControllo> esitiInput = new ArrayList<>();
		List<VariabileCalcolo> variabiliInput = new ArrayList<>();

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREPERC, gREPERC));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.TITVALRID, tITVAL));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GRESUPRIDIST, BigDecimal.valueOf(1)));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREAZIBIO, gREAZIBIO));

		// 1. Superfici per particella/coltura

		// 1.1 Superfici impegnate
		ArrayList<ParticellaColtura> supImpegnate = new ArrayList<ParticellaColtura>();

		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"211-163-000", "112", 20 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "113", 4 * double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "114", double10000));
		supImpegnate.add(getParticellaColtura("{\"idParticella\":2,\"comune\":\"ISERA - LENZIMA (TN)\",\"codNazionale\":\"P461\",\"foglio\":9999,\"particella\":\"00001\",\"sub\":\"2\"}",
				"210-163-000", "119", 9 * double10000 - 1000));

		BigDecimal bPSSUPAMM = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(1));
		BigDecimal bPSSUPDET = getSuperficiImpegnate(supImpegnate, "").subtract(BigDecimal.valueOf(2));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPAMM, bPSSUPAMM));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.BPSSUPDET, bPSSUPDET));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.GREFPAZOTO, BigDecimal.ONE));

		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPIMP, supImpegnate));
		variabiliInput.add(new VariabileCalcolo(TipoVariabile.PFSUPDET, supImpegnate));

		// Inserico tutte le variabili id/domanda che avrò in output
		List<TipoVariabile> presenzaVariabili = Arrays.asList(TipoVariabile.GRESUPDET, TipoVariabile.GRESUPARB, TipoVariabile.GRESUPPP, TipoVariabile.GRESUPSEM, TipoVariabile.GRESUPERBAI,
				TipoVariabile.GRESUPLEGUM, TipoVariabile.GRESUPRIPOSO, TipoVariabile.GRESUPSOMM, TipoVariabile.GREDIV1COL, TipoVariabile.GREESEBIO, TipoVariabile.GREESESEM, TipoVariabile.GREPERCPES,
				TipoVariabile.GREPERCELR, TipoVariabile.GREESEDIV, TipoVariabile.GREDIVSUP1COL, TipoVariabile.GRESUPBASE, TipoVariabile.GREIMPBASE, TipoVariabile.GRESUPAMM, TipoVariabile.GREIMPAMM,
				TipoVariabile.GREDIVFGS, TipoVariabile.GREESEEFA, TipoVariabile.GRESUPAZOTODIC, TipoVariabile.GRESUPAZOTODET, TipoVariabile.GREDIVPERC1COL, TipoVariabile.GREDIVSUPRID,
				TipoVariabile.GREEFAPERCAZOTO, TipoVariabile.GREEFASUPRID, TipoVariabile.GRESUPRID, TipoVariabile.GREIMPRID, TipoVariabile.GREPERCSCOST, TipoVariabile.GRESUPAZOTOPOND,
				TipoVariabile.GREDIVSUP2COL, TipoVariabile.GREDIV2FGS, TipoVariabile.GREDIV2COL, TipoVariabile.GREDIVPERC2COL);

		// esegui il passo di lavorazione
		istruttoria = istruttoriaDao.findById(istruttoria.getId()).get();
		DatiElaborazioneIstruttoria dati = new DatiElaborazioneIstruttoria();
		dati.setVariabiliInputNext(variabiliInput);
		dati.setEsitiInputNext(esitiInput);
		dati.setIstruttoria(istruttoria);
		dati.setTransizione(transizione);

		PassoTransizioneModel res = greening.eseguiPasso(dati);

		// verifica esito
		assertEquals("OK", res.getEsito());

		assertEquals(codEsito.getCodiceEsito(), res.getCodiceEsito());

		// verifica che le variabili id/domanda inserite in precedenza coincidano con quelle restituite in output dal passo di lavorazione
		List<VariabileCalcolo> variabiliOutput = checkNumerositaOutput(res, presenzaVariabili);

		checkOutputEFA(variabiliInput, supImpegnate, bPSSUPAMM, variabiliOutput);

		// Inserisco tutte le variabili particella/coltura che avrò in output
		List<TipoVariabile> presenzaVariabiliSintesi = Arrays.asList(TipoVariabile.PFSUPDETARB, TipoVariabile.PFSUPDETPP, TipoVariabile.PFSUPDETSEM, TipoVariabile.PFSUPDETERBAI,
				TipoVariabile.PFSUPDETLEGUM, TipoVariabile.PFSUPDETRIPOSO, TipoVariabile.PFSUPDETSOMM, TipoVariabile.PFSUPDETPRIMA, TipoVariabile.PFSUPDETSECONDA, TipoVariabile.PFAZOTO);

		// verifica che le variabili particella/colture inserite in precedenza coincidano con quelle restituite in output dal passo di lavorazione
		List<VariabileCalcolo> variabiliSintesi = checkNumerositaSintesi(res, presenzaVariabiliSintesi);

		checkOutputEFA(variabiliInput, supImpegnate, bPSSUPAMM, variabiliSintesi);

		checkEsiti(codEsito, res);
	}

	private void checkOutputEFA(List<VariabileCalcolo> variabiliInput, ArrayList<ParticellaColtura> supImpegnate, BigDecimal bPSSUPAMM, List<VariabileCalcolo> variabiliOutput) {
		variabiliOutput.forEach(v -> {
			if (v.getTipoVariabile().equals(TipoVariabile.GREAZIBIO)) {
				assertEquals(v.getValBoolean(), true);
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPBASE)) {
				assertEquals(v.getValNumber(), bPSSUPAMM);
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREIMPBASE)) {
				assertEquals(v.getValNumber(), getVal(variabiliOutput, TipoVariabile.GRESUPBASE).multiply(tITVAL).multiply(gREPERC).setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPAMM)) {
				// presenti riduzioni
				assertThat(v.getValNumber().compareTo(getVal(variabiliOutput, TipoVariabile.GRESUPBASE)) < 0);
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREIMPAMM)) {
				assertEquals(v.getValNumber(), getVal(variabiliOutput, TipoVariabile.GRESUPAMM).multiply(getVal(variabiliInput, TipoVariabile.TITVALRID))
						.multiply(getVal(variabiliInput, TipoVariabile.GREPERC)).setScale(2, RoundingMode.HALF_UP));
			}

			// superifici
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPARB)) {
				assertEquals(v.getValNumber(), getSuperficiImpegnate(supImpegnate, "12"));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPPP)) {
				assertEquals(v.getValNumber(), getSuperficiImpegnate(supImpegnate, "13"));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPSEM)) {
				assertEquals(v.getValNumber(), getSuperficiImpegnate(supImpegnate, "11"));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPERBAI)) {
				assertEquals(v.getValNumber(), getSuperficiImpegnate(supImpegnate, "112"));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPRIPOSO)) {
				assertEquals(v.getValNumber(), getSuperficiImpegnate(supImpegnate, "113"));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPSOMM)) {
				assertEquals(v.getValNumber(), getSuperficiImpegnate(supImpegnate, "114"));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPLEGUM)) {
				assertEquals(v.getValNumber(), getLeguminose(supImpegnate));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREDIVSUP1COL)) {
				assertEquals(v.getValNumber(), getPrinc(supImpegnate, "11"));
			}

			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPDET)) {
				assertEquals(v.getValNumber(), getVal(variabiliInput, TipoVariabile.BPSSUPDET));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREESEBIO)) {
				assertEquals(v.getValBoolean(), false);
			}
		});
	}

	private void checkOutput(List<VariabileCalcolo> variabiliInput, ArrayList<ParticellaColtura> supImpegnate, BigDecimal bPSSUPAMM, List<VariabileCalcolo> variabiliOutput) {
		variabiliOutput.forEach(v -> {
			if (v.getTipoVariabile().equals(TipoVariabile.GREAZIBIO)) {
				assertEquals(v.getValBoolean(), true);
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPBASE)) {
				assertEquals(v.getValNumber(), bPSSUPAMM);
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREIMPBASE)) {
				assertEquals(v.getValNumber(), getVal(variabiliOutput, TipoVariabile.GRESUPBASE).multiply(tITVAL).multiply(gREPERC).setScale(2, RoundingMode.HALF_UP));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPAMM)) {
				// presenti riduzioni
				assertThat(v.getValNumber().compareTo(getVal(variabiliOutput, TipoVariabile.GRESUPBASE)) < 0);
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREIMPAMM)) {
				assertEquals(v.getValNumber(), getVal(variabiliOutput, TipoVariabile.GRESUPAMM).multiply(getVal(variabiliInput, TipoVariabile.TITVALRID))
						.multiply(getVal(variabiliInput, TipoVariabile.GREPERC)).setScale(2, RoundingMode.HALF_UP));
			}

			// superifici
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPARB)) {
				assertEquals(v.getValNumber(), getSuperficiImpegnate(supImpegnate, "12"));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPPP)) {
				assertEquals(v.getValNumber(), getSuperficiImpegnate(supImpegnate, "13"));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPSEM)) {
				assertEquals(v.getValNumber(), getSuperficiImpegnate(supImpegnate, "11"));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPERBAI)) {
				assertEquals(v.getValNumber(), getSuperficiImpegnate(supImpegnate, "112"));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPRIPOSO)) {
				assertEquals(v.getValNumber(), getSuperficiImpegnate(supImpegnate, "113"));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPSOMM)) {
				assertEquals(v.getValNumber(), getSuperficiImpegnate(supImpegnate, "114"));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPLEGUM)) {
				assertEquals(v.getValNumber(), getLeguminose(supImpegnate));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREDIVSUP1COL)) {
				assertEquals(v.getValNumber(), getPrinc(supImpegnate, "11"));
			}

			if (v.getTipoVariabile().equals(TipoVariabile.GRESUPDET)) {
				assertEquals(v.getValNumber(), getVal(variabiliInput, TipoVariabile.BPSSUPDET));
			}
			if (v.getTipoVariabile().equals(TipoVariabile.GREESEBIO)) {
				assertEquals(v.getValBoolean(), false);
			}
		});
	}

	private BigDecimal getSuperficiImpegnate(ArrayList<ParticellaColtura> supImpegnate, String ricerca) {
		Double sum = supImpegnate.stream().filter(x -> x.getLivello().startsWith(ricerca)).collect(Collectors.summingDouble(ParticellaColtura::getValNum));
		return BigDecimal.valueOf(sum).setScale(4, RoundingMode.HALF_UP);
	}

	private BigDecimal getLeguminose(ArrayList<ParticellaColtura> supImpegnate) {
		Double sum = supImpegnate.stream().filter(ParticellaColtura::getValBool).collect(Collectors.summingDouble(ParticellaColtura::getValNum));
		return BigDecimal.valueOf(sum).setScale(4, RoundingMode.HALF_UP);
	}

	private BigDecimal getPrinc(ArrayList<ParticellaColtura> supImpegnate, String ricerca) {

		/*
		 * "Limitatamente ai soli seminativi (livello coltura is like '11%') si raggruppano le superfici a seconda della colonna CODCOLTDIVERSA nella matrice delle colture caricata in A4G e si
		 * seleziona il CODCOLTDIVERSA con la maggiore superficie impegnata. A quel punto: IF il CODCOLTDIVERSA del record dell'impegno è = al CODCOLTDIVERSA ottenuto con la procedura di cui sopra
		 * THEN TRUE ELSE FALSE"
		 */

		String colt = greening.getCODCOLTDIVERSASupMax(supImpegnate);

		Double sum = supImpegnate.stream().filter(x -> x.getLivello().startsWith(ricerca) && x.getValString().equals(colt)).collect(Collectors.summingDouble(ParticellaColtura::getValNum));
		return BigDecimal.valueOf(sum).setScale(4, RoundingMode.HALF_UP);
	}

	private ParticellaColtura getParticellaColtura(String infoCatastali, String codiceColtura3, String livello, double supRichiestaNetta) {
		ParticellaColtura pc = new ParticellaColtura();
		try {
			pc.setParticella(mapper.readValue(infoCatastali, Particella.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		pc.setColtura(codiceColtura3);
		double d = supRichiestaNetta / double10000;
		pc.setValNum((float) d);
		pc.setLivello(livello);
		pc.setValBool(false);
		if (codiceColtura3.equals("210-163-000")) {
			pc.setValString("12");
		} else if (codiceColtura3.equals("211-163-000"))
			pc.setValString("13");
		else
			pc.setValString("11");
		return pc;
	}

	private BigDecimal getVal(List<VariabileCalcolo> variabili, TipoVariabile tipo) {
		return variabili.stream().filter(x -> x.getTipoVariabile() == tipo).findFirst().get().getValNumber();
	}

	// Metodo per il confronto tra le variabili id/domanda restituite dal passo di lavorazione e quelle inserite da me
	private List<VariabileCalcolo> checkNumerositaOutput(PassoTransizioneModel res, List<TipoVariabile> presenzaVariabili) throws Exception {
		List<VariabileCalcolo> variabili = mapper.readValue(res.getDatiOutput(), DatiOutput.class).getVariabiliCalcolo();

		return checkNumerosita(res, presenzaVariabili, variabili);
	}

	// Metodo per il confronto tra le variabili particella/coltura restituite dal passo di lavorazione e quelle inserite da me
	private List<VariabileCalcolo> checkNumerositaSintesi(PassoTransizioneModel res, List<TipoVariabile> presenzaVariabili) throws Exception {
		List<VariabileCalcolo> variabili = mapper.readValue(res.getDatiSintesiLavorazione(), DatiSintesi.class).getVariabiliParticellaColtura();
		if (variabili != null)
			return checkNumerosita(res, presenzaVariabili, variabili);
		else
			return new ArrayList<VariabileCalcolo>();
	}

	// Metodo che restituisce la lista delle variabili output dopo aver controllato che il numero di variabili recuperate nel passo lavorazione(?) coincida con il numero di variabili indicate nelle
	// lista 'presenzaVariabili'
	private List<VariabileCalcolo> checkNumerosita(PassoTransizioneModel res, List<TipoVariabile> presenzaVariabili, List<VariabileCalcolo> variabili) throws Exception {
		String result = variabili.stream().map(x -> x.getTipoVariabile().name() + ' ' + x.getTipoVariabile().getDescrizione() + ' ' + x.recuperaValoreString()).collect(Collectors.joining("\n"));
		// System.out.println(result);

		for (TipoVariabile tipo : presenzaVariabili) {
			boolean match = variabili.stream().anyMatch(x -> x.getTipoVariabile().equals(tipo));
			// System.out.println("Variabile " + tipo + " match " + match);
			assertEquals(match, true);
		}

		assertEquals(presenzaVariabili.size(), variabili.size());
		return variabili;
	}

	private void checkEsiti(CodiceEsito codEsito, PassoTransizioneModel res) throws Exception {
		// if (res.getDatiSintesiLavorazione() != null && !res.getDatiSintesiLavorazione().isEmpty()) {
		List<EsitoControllo> esiti = mapper.readValue(res.getDatiSintesiLavorazione(), DatiSintesi.class).getEsitiControlli();

		for (CodiceEsito ce : esitiRes.keySet()) {
			if (ce == codEsito) {
				KeyValueStringString a = esitiRes.get(ce);
				List<EsitoControllo> esitoPrevisto = esiti.stream().filter(x -> x.getTipoControllo().name().equals(a.getMkey())).collect(Collectors.toList());
				assertEquals(1, esitoPrevisto.size());
				assertEquals(getValoreEsito(esitoPrevisto.get(0)), a.getMvalue());
			}
		}
		// }
	}

	private String getValoreEsito(EsitoControllo d) {
		String val = "";
		if (d.getEsito() != null)
			val = d.getEsito().equals(false) ? "false" : "true";
		else if (d.getValString() != null && !d.getValString().isEmpty())
			val = d.getValString();
		else if (d.getValNumber() != null)
			val = d.getValNumber().toString();
		return val;
	}

}
