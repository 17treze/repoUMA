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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VerificaPresenzaSanzioniGiovaneComponentTest extends AVerificaPresenzaSanzioniComponent {

	@Autowired
	private InitVariabiliInputCalcoloGiovane component;
	@Autowired
	private DomandaUnicaDao daoDomanda;
	@Autowired
	private IstruttoriaDao daoIstruttoria;
	@Autowired
	private TransizioneIstruttoriaService transizioneIstruttoriaService;
	
	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_conDatiIstruttoreGiovaneSenzaSanzioni_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_conDatiIstruttoreGiovaneSenzaSanzioni_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void conDatiIstruttoreSenzaSanzioni() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("9183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputGiovaneAgricoltore(datiIntermediLavorazione);
		
		assertFalse(getVC(input, TipoVariabile.GIOYCSANZANNIPREC).getValBoolean());
		assertEquals(BigDecimal.ZERO.doubleValue(), getVC(input, TipoVariabile.GIOYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
	}
	
	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_conDatiIstruttoreGiovaneConSanzioni_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_conDatiIstruttoreGiovaneConSanzioni_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void conDatiIstruttoreConSanzioni() throws Exception {
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("9183430"));

		
		IstruttoriaModel istruttoria = 
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.SALDO);
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputGiovaneAgricoltore(datiIntermediLavorazione);
		
		assertTrue(getVC(input, TipoVariabile.GIOYCSANZANNIPREC).getValBoolean());
		assertEquals((new BigDecimal("1000")).doubleValue(), getVC(input, TipoVariabile.GIOYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
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
		
		List<VariabileCalcolo> input = component.initInputGiovaneAgricoltore(datiIntermediLavorazione);
		
		assertFalse(getVC(input, TipoVariabile.GIOYCSANZANNIPREC).getValBoolean());
		assertEquals(BigDecimal.ZERO.doubleValue(), getVC(input, TipoVariabile.GIOYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
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
		
		List<VariabileCalcolo> input = component.initInputGiovaneAgricoltore(datiIntermediLavorazione);
		assertFalse(getVC(input, TipoVariabile.GIOYCSANZANNIPREC).getValBoolean());
		assertEquals(BigDecimal.ZERO.doubleValue(), getVC(input, TipoVariabile.GIOYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
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
		
		List<VariabileCalcolo> input = component.initInputGiovaneAgricoltore(datiIntermediLavorazione);
		
		assertTrue(getVC(input, TipoVariabile.GIOYCSANZANNIPREC).getValBoolean());
		assertEquals(BigDecimal.ZERO.doubleValue(), getVC(input, TipoVariabile.GIOYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
	}


	@Test
	@Transactional 
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_annoPrecedenteLiquidabileSenzaSanzioniA4G_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/calcoloDisaccoppiato/9183430_annoPrecedenteLiquidabileSenzaSanzioniA4G_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void senzaDatiIstruttoreAnnoPrecedenteA4GLiquidabileSenzaSanzioni() throws Exception {
		// Numero domanda relativo al 2019
		DomandaUnicaModel d = daoDomanda.findByNumeroDomanda(new BigDecimal("9183430"));
		IstruttoriaModel istruttoria =
				daoIstruttoria.findByDomandaUnicaModelAndSostegnoAndTipologia(d, Sostegno.DISACCOPPIATO, TipoIstruttoria.ANTICIPO);
		mockCheckDomandaPresentataAnnoPrecedente(d.getCuaaIntestatario(), component.annoPrecedente(d), true);
		
		DatiElaborazioneIstruttoria datiIntermediLavorazione = new DatiElaborazioneIstruttoria();
		TransizioneIstruttoriaModel nuovaTransizione = transizioneIstruttoriaService.avviaTransizioneCalcolo(istruttoria);
		datiIntermediLavorazione.setTransizione(nuovaTransizione);
		
		List<VariabileCalcolo> input = component.initInputGiovaneAgricoltore(datiIntermediLavorazione);
		
		assertFalse(getVC(input, TipoVariabile.GIOYCSANZANNIPREC).getValBoolean());
		assertEquals(BigDecimal.ZERO.doubleValue(), getVC(input, TipoVariabile.GIOYCIMPSANZAPREC).getValNumber().doubleValue(), 0D);
	}
}
