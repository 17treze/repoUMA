package it.tndigitale.a4g.fascicolo.anagrafica.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.web.server.ResponseStatusException;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.RevocaImmediataModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.StatoRevocaImmediata;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.MandatoDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.RevocaImmediataDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.DetenzioneService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.MandatoService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.BusinessResponsesDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.BusinessResponsesDto.Esiti;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.RichiestaRevocaImmediataDto;
import it.tndigitale.a4g.framework.time.Clock;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = {"a4gfascicolo.fascicolo.ricerca.tutti"})
class MandatoControllerConEccezioniTest {
	
	@Value("#{T(java.time.LocalDate).parse('${revoca-immediata.periodo.da}' + '/' + T(java.time.LocalDate).now().getYear(), T(java.time.format.DateTimeFormatter).ofPattern('${revoca-immediata.periodo.formato}' + '/yyyy'))}")
	LocalDate periodoRevocaImmediataDa;
	
	@Value("#{T(java.time.LocalDate).parse('${revoca-immediata.periodo.a}' + '/' + T(java.time.LocalDate).now().getYear(), T(java.time.format.DateTimeFormatter).ofPattern('${revoca-immediata.periodo.formato}' + '/yyyy'))}")
	LocalDate periodoRevocaImmediataA;

	@Autowired MandatoController mandatoController;
	@Autowired FascicoloDao fascicoloDao;
	@Autowired MandatoDao mandatoDao;
	@Autowired RevocaImmediataDao revocaImmediataDao;
	@Autowired DetenzioneService detenzioneService;
	
	@MockBean private MandatoService mandatoService;
	@MockBean private AnagraficaUtenteClient anagraficaUtenteClient;
	@MockBean Clock clock;
	
	private LocalDate testDay;
	
	public 	MandatoControllerConEccezioniTest() {
		testDay = LocalDate.of(2021, Month.JANUARY, 1);
	}
	
	@BeforeEach
	void testSetup() {
		List<String> entiUtenteConnesso = new ArrayList<String>();
		entiUtenteConnesso.add("94");
		Mockito.when(anagraficaUtenteClient.getEntiUtenteConnesso()).thenReturn(entiUtenteConnesso);
		
		Mockito.when(clock.today()).thenReturn(testDay);
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.revocaimmediata.accettazione"})
	@Transactional
	void richiesteRevocaImmediataValutateThenThrowResponseStatusException() {
		String cuaa = "LRCPLA50M11H612B";
		List<RevocaImmediataModel> revocaImmediataList = revocaImmediataDao.findByCodiceFiscale(cuaa);
		Optional<RevocaImmediataModel> revocaImmediataOptional = revocaImmediataList.stream().filter(
				revocaImmediata -> revocaImmediata.getStato().equals(StatoRevocaImmediata.DA_VALUTARE)).findFirst();
		assertTrue(revocaImmediataOptional.isPresent());
		RevocaImmediataModel revocaImmediataModel = revocaImmediataOptional.get();
		assertEquals(StatoRevocaImmediata.DA_VALUTARE, revocaImmediataModel.getStato());
		
		Optional<FascicoloModel> fascicoloModelOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		assertEquals(true, fascicoloModelOpt.isPresent());
		Mockito.when(mandatoService.valutaRichiestaRevocaImmediata(
				Mockito.any(), Mockito.any(Boolean.class))).thenThrow(ResponseStatusException.class);
		
		Optional<MandatoModel> detenzioneModelOpt = Optional.of((MandatoModel)(detenzioneService.getDetenzioneCorrente(fascicoloModelOpt.get())).get());
		Mockito.when(mandatoService.getMandatoCorrente(Mockito.any())).thenReturn(detenzioneModelOpt);
		
		BusinessResponsesDto valutaRichiestaRevocaImmediata = mandatoController.valutaRichiestaRevocaImmediata(cuaa, true, null);
		assertEquals(Esiti.KO, valutaRichiestaRevocaImmediata.getEsito());
	}

	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.revocaimmediata.accettazione"})
	@Transactional
	void richiesteRevocaImmediataValutateThenThrowCustomException() {
		String cuaa = "LRCPLA50M11H612B";
		List<RevocaImmediataModel> revocaImmediataList = revocaImmediataDao.findByCodiceFiscale(cuaa);
		Optional<RevocaImmediataModel> revocaImmediataOptional = revocaImmediataList.stream().filter(
				revocaImmediata -> revocaImmediata.getStato().equals(StatoRevocaImmediata.DA_VALUTARE)).findFirst();
		assertTrue(revocaImmediataOptional.isPresent());
		RevocaImmediataModel revocaImmediataModel = revocaImmediataOptional.get();
		assertEquals(StatoRevocaImmediata.DA_VALUTARE, revocaImmediataModel.getStato());
		
		Optional<FascicoloModel> fascicoloModelOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		assertEquals(true, fascicoloModelOpt.isPresent());
		
		Mockito.when(mandatoService.valutaRichiestaRevocaImmediata(Mockito.any(), Mockito.any(Boolean.class))).thenThrow(IllegalArgumentException.class);
		Optional<MandatoModel> detenzioneModelOpt = Optional.of((MandatoModel)(detenzioneService.getDetenzioneCorrente(fascicoloModelOpt.get())).get());
		Mockito.when(mandatoService.getMandatoCorrente(Mockito.any())).thenReturn(detenzioneModelOpt);
		
		BusinessResponsesDto valutaRichiestaRevocaImmediata = mandatoController.valutaRichiestaRevocaImmediata(cuaa, true, null);
		assertEquals(Esiti.KO, valutaRichiestaRevocaImmediata.getEsito());
		
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.revocaimmediata.accettazione"})
	@Transactional
	void richiesteRevocaImmediataValutateThenThrowWhenSendMailRL() {
		String cuaa = "LRCPLA50M11H612B";
		List<RevocaImmediataModel> revocaImmediataList = revocaImmediataDao.findByCodiceFiscale(cuaa);
		Optional<RevocaImmediataModel> revocaImmediataOptional = revocaImmediataList.stream().filter(
				revocaImmediata -> revocaImmediata.getStato().equals(StatoRevocaImmediata.DA_VALUTARE)).findFirst();
		assertTrue(revocaImmediataOptional.isPresent());
		RevocaImmediataModel revocaImmediataModel = revocaImmediataOptional.get();
		assertEquals(StatoRevocaImmediata.DA_VALUTARE, revocaImmediataModel.getStato());
		
		Optional<FascicoloModel> fascicoloModelOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		assertEquals(true, fascicoloModelOpt.isPresent());
		RichiestaRevocaImmediataDto richiestaRevocaImmediataDto = new RichiestaRevocaImmediataDto();
		Mockito.when(mandatoService.valutaRichiestaRevocaImmediata(Mockito.any(), Mockito.any(Boolean.class))).thenReturn(richiestaRevocaImmediataDto);
		Optional<MandatoModel> detenzioneModelOpt = Optional.of((MandatoModel)(detenzioneService.getDetenzioneCorrente(fascicoloModelOpt.get())).get());
		Mockito.when(mandatoService.getMandatoCorrente(Mockito.any())).thenReturn(detenzioneModelOpt);
		Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST)).when(mandatoService).notificaMailTitolareRappresentanteLegale(Mockito.any(), Mockito.any(Boolean.class));
		
		BusinessResponsesDto valutaRichiestaRevocaImmediata = mandatoController.valutaRichiestaRevocaImmediata(cuaa, true, null);
		assertEquals(Esiti.NON_BLOCCANTE, valutaRichiestaRevocaImmediata.getEsito());
		
	}
	
	@Test
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/mandato/revocaMandatoTest_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.mandato.revocaimmediata.accettazione"})
	@Transactional
	void richiesteRevocaImmediataValutateThenThrowWhenSendMailAppag() {
		String cuaa = "LRCPLA50M11H612B";
		List<RevocaImmediataModel> revocaImmediataList = revocaImmediataDao.findByCodiceFiscale(cuaa);
		Optional<RevocaImmediataModel> revocaImmediataOptional = revocaImmediataList.stream().filter(
				revocaImmediata -> revocaImmediata.getStato().equals(StatoRevocaImmediata.DA_VALUTARE)).findFirst();
		assertTrue(revocaImmediataOptional.isPresent());
		RevocaImmediataModel revocaImmediataModel = revocaImmediataOptional.get();
		assertEquals(StatoRevocaImmediata.DA_VALUTARE, revocaImmediataModel.getStato());
		
		Optional<FascicoloModel> fascicoloModelOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		assertEquals(true, fascicoloModelOpt.isPresent());
		RichiestaRevocaImmediataDto richiestaRevocaImmediataDto = new RichiestaRevocaImmediataDto();
		Mockito.when(mandatoService.valutaRichiestaRevocaImmediata(Mockito.any(), Mockito.any(Boolean.class))).thenReturn(richiestaRevocaImmediataDto);
		Optional<MandatoModel> detenzioneModelOpt = Optional.of((MandatoModel)(detenzioneService.getDetenzioneCorrente(fascicoloModelOpt.get())).get());
		Mockito.when(mandatoService.getMandatoCorrente(Mockito.any())).thenReturn(detenzioneModelOpt);
		Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST)).when(mandatoService).notificaMailAppag(Mockito.any(), Mockito.any(Boolean.class));
		
		BusinessResponsesDto valutaRichiestaRevocaImmediata = mandatoController.valutaRichiestaRevocaImmediata(cuaa, true, null);
		assertEquals(Esiti.NON_BLOCCANTE, valutaRichiestaRevocaImmediata.getEsito());
		
	}
}
