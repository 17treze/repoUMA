/**
 * 
 */
package it.tndigitale.a4gistruttoria.component.dis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.component.AVerificaPresenzaSanzioniComponent;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiElaborazioneIstruttoria;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.TransizioneIstruttoriaService;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;

/**
 * @author IT417
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VerificaPresenzaSanzioniBPSComponentTest extends AVerificaPresenzaSanzioniComponent {
	
	@Autowired
	private InitVariabiliInputSanzioniBPS component;
	@Autowired
	private DomandaUnicaDao daoDomanda;
	@Autowired
	private IstruttoriaDao daoIstruttoria;
	@Autowired
	private TransizioneIstruttoriaService transizioneIstruttoriaService;
	
	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_conDatiIstruttoreSenzaSanzioni_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_conDatiIstruttoreSenzaSanzioni_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void conDatiIstruttoreSenzaSanzioni() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("9183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		datiIntermediLavorazione.setTransizione(istruttoria.getTransizioni().stream().findFirst().get());
		
		List<VariabileCalcolo> input = component.initInputSanzioniBps(datiIntermediLavorazione);
		
		assertTrue(getVC(input, TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean());
		assertFalse(getVC(input, TipoVariabile.BPSYCSANZANNIPREC).getValBoolean());
		assertEquals(BigDecimal.ZERO.doubleValue(), getVC(input, TipoVariabile.BPSYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
	}
	
	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_conDatiIstruttoreConSanzioni_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_conDatiIstruttoreConSanzioni_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void conDatiIstruttoreConSanzioni() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("9183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputSanzioniBps(datiIntermediLavorazione);
		
		assertTrue(getVC(input, TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean());
		assertTrue(getVC(input, TipoVariabile.BPSYCSANZANNIPREC).getValBoolean());
		assertEquals((new BigDecimal("1000")).doubleValue(), getVC(input, TipoVariabile.BPSYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
	}

	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_senzaDatiIstruttore_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_senzaDatiIstruttore_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void senzaDatiIstruttoreSenzaDomandaAnnoPrecedente() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("9183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
		mockCheckDomandaPresentataAnnoPrecedente(d.getCuaaIntestatario(), component.annoPrecedente(d), false);
		
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputSanzioniBps(datiIntermediLavorazione);
		
		assertTrue(getVC(input, TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean());
		assertFalse(getVC(input, TipoVariabile.BPSYCSANZANNIPREC).getValBoolean());
		assertEquals(BigDecimal.ZERO.doubleValue(), getVC(input, TipoVariabile.BPSYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
	}
	
	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_senzaDatiIstruttore_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_senzaDatiIstruttore_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void senzaDatiIstruttoreAnnoPrecedenteAGSSenzaSanzioni() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("9183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
		mockCheckDomandaPresentataAnnoPrecedente(d.getCuaaIntestatario(), component.annoPrecedente(d), true);
		mockCalcoliAnnoPrecedenteAGS(d.getNumeroDomanda().toString());
		
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputSanzioniBps(datiIntermediLavorazione);
		
		assertTrue(getVC(input, TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean());
		assertFalse(getVC(input, TipoVariabile.BPSYCSANZANNIPREC).getValBoolean());
		assertEquals(BigDecimal.ZERO.doubleValue(), getVC(input, TipoVariabile.BPSYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
	}
	
	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/8183430_senzaDatiIstruttore_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/8183430_senzaDatiIstruttore_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void senzaDatiIstruttoreAnnoPrecedenteAGSSenzaIstruttoria() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("8183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		mockCheckDomandaPresentataAnnoPrecedente(d.getCuaaIntestatario(), component.annoPrecedente(d), true);
		mockCalcoliAnnoPrecedenteAGS(d.getNumeroDomanda().toString());
		
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputSanzioniBps(datiIntermediLavorazione);
		
		assertFalse(getVC(input, TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean());
	}

	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/7183430_senzaDatiIstruttore_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/7183430_senzaDatiIstruttore_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void senzaDatiIstruttoreAnnoPrecedenteAGSSanzioni() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("7183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
		mockCheckDomandaPresentataAnnoPrecedente(d.getCuaaIntestatario(), component.annoPrecedente(d), true);
		mockCalcoliAnnoPrecedenteAGS(d.getNumeroDomanda().toString());
		
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputSanzioniBps(datiIntermediLavorazione);
		
		assertTrue(getVC(input, TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean());
		assertTrue(getVC(input, TipoVariabile.BPSYCSANZANNIPREC).getValBoolean());
		assertEquals((new BigDecimal("1000")).doubleValue(), getVC(input, TipoVariabile.BPSYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
	}


	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/6183430_senzaDatiIstruttore_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/6183430_senzaDatiIstruttore_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void senzaDatiIstruttoreAnnoPrecedenteAGSSanzioniOver66() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("6183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);

		mockCheckDomandaPresentataAnnoPrecedente(d.getCuaaIntestatario(), component.annoPrecedente(d), true);
		mockCalcoliAnnoPrecedenteAGS(d.getNumeroDomanda().toString());
		
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputSanzioniBps(datiIntermediLavorazione);
		
		assertTrue(getVC(input, TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean());
		assertTrue(getVC(input, TipoVariabile.BPSYCSANZANNIPREC).getValBoolean());
		assertEquals((new BigDecimal("0")).doubleValue(), getVC(input, TipoVariabile.BPSYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
	}

	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_annoPrecedenteLiquidabileSenzaSanzioniA4G_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_annoPrecedenteLiquidabileSenzaSanzioniA4G_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void senzaDatiIstruttoreAnnoPrecedenteA4GLiquidabileSenzaSanzioni() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("9183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.ANTICIPO);
		mockCheckDomandaPresentataAnnoPrecedente(d.getCuaaIntestatario(), component.annoPrecedente(d), true);
		
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputSanzioniBps(datiIntermediLavorazione);
		
		assertTrue(getVC(input, TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean());
		assertFalse(getVC(input, TipoVariabile.BPSYCSANZANNIPREC).getValBoolean());
		assertEquals(BigDecimal.ZERO.doubleValue(), getVC(input, TipoVariabile.BPSYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
	}
	
	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_annoPrecedente2IstruttorieA4G_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_annoPrecedente2IstruttorieA4G_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void senzaDatiIstruttoreAnnoPrecedente2IstruttorieA4G() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("9183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.ANTICIPO);
		mockCheckDomandaPresentataAnnoPrecedente(d.getCuaaIntestatario(), component.annoPrecedente(d), true);
		
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputSanzioniBps(datiIntermediLavorazione);
		
		assertTrue(getVC(input, TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean());
		assertFalse(getVC(input, TipoVariabile.BPSYCSANZANNIPREC).getValBoolean());
		assertEquals(BigDecimal.ZERO.doubleValue(), getVC(input, TipoVariabile.BPSYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
	}
	
	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_NoAnnoPrecedente2IstruttorieA4G_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_NoAnnoPrecedente2IstruttorieA4G_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void senzaDatiIstruttoreNoAnnoPrecedente2IstruttorieA4G() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("9183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.ANTICIPO);
		mockCheckDomandaPresentataAnnoPrecedente(d.getCuaaIntestatario(), component.annoPrecedente(d), true);
		
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputSanzioniBps(datiIntermediLavorazione);
		
		assertFalse(getVC(input, TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean());
	}

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_annoPrecedenteLiquidabileRecidivaA4G_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_annoPrecedenteLiquidabileRecidivaA4G_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void senzaDatiIstruttoreAnnoPrecedenteA4GLiquidabileRecidiva() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("9183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.ANTICIPO);
		mockCheckDomandaPresentataAnnoPrecedente(d.getCuaaIntestatario(), component.annoPrecedente(d), true);
		
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputSanzioniBps(datiIntermediLavorazione);
		assertTrue(getVC(input, TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean());
		assertTrue(getVC(input, TipoVariabile.BPSYCSANZANNIPREC).getValBoolean());
		assertEquals(BigDecimal.ZERO.doubleValue(), getVC(input, TipoVariabile.BPSYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
	}

	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_annoPrecedenteLiquidabileYCA4G_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_annoPrecedenteLiquidabileYCA4G_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void senzaDatiIstruttoreAnnoPrecedenteA4GLiquidabileYC() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("9183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.ANTICIPO);
		mockCheckDomandaPresentataAnnoPrecedente(d.getCuaaIntestatario(), component.annoPrecedente(d), true);
		
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputSanzioniBps(datiIntermediLavorazione);
		
		assertTrue(getVC(input, TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean());
		assertTrue(getVC(input, TipoVariabile.BPSYCSANZANNIPREC).getValBoolean());
		assertEquals((new BigDecimal("25")).doubleValue(), getVC(input, TipoVariabile.BPSYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
	}

	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_annoPrecedenteNonAmmissibileRecidivaA4G_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_annoPrecedenteNonAmmissibileRecidivaA4G_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void senzaDatiIstruttoreAnnoPrecedenteA4GNonAmmissibileRecidiva() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("9183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.ANTICIPO);
		mockCheckDomandaPresentataAnnoPrecedente(d.getCuaaIntestatario(), component.annoPrecedente(d), true);
		
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputSanzioniBps(datiIntermediLavorazione);
		
		assertTrue(getVC(input, TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean());
		assertTrue(getVC(input, TipoVariabile.BPSYCSANZANNIPREC).getValBoolean());
		assertEquals(BigDecimal.ZERO.doubleValue(), getVC(input, TipoVariabile.BPSYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
	}

	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_annoPrecedenteInLavorazioneA4G_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_annoPrecedenteInLavorazioneA4G_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void senzaDatiIstruttoreAnnoPrecedenteA4GInLavorazione() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("9183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.ANTICIPO);
		mockCheckDomandaPresentataAnnoPrecedente(d.getCuaaIntestatario(), component.annoPrecedente(d), true);
		
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputSanzioniBps(datiIntermediLavorazione);
		
		assertFalse(getVC(input, TipoVariabile.BPSDOMLIQANNOPREC).getValBoolean());
	}
}
