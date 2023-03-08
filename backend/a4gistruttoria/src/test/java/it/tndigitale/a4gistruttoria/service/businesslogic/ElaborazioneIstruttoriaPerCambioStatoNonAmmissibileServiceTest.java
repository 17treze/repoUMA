package it.tndigitale.a4gistruttoria.service.businesslogic;

import static it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria.NON_AMMISSIBILE;
import static it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria.PAGAMENTO_AUTORIZZATO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.CaricaAgsDto;
import it.tndigitale.a4g.proxy.client.model.ComunicationDto;
import it.tndigitale.a4gistruttoria.action.IstruttoriaComponent;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;
import it.tndigitale.a4gistruttoria.service.businesslogic.iotialia.IoItaliaConsumerApi;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi4Anagrafica;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ElaborazioneIstruttoriaPerCambioStatoNonAmmissibileServiceTest {

	@MockBean
	private RestTemplate restTemplate;

	@PersistenceContext
	private EntityManager persistenceM;

	@Autowired
	private ElaborazioneIstruttoriaPerCambioStatoNonAmmissibileService elaborazioneIstruttoriaPerCambioStatoNonAmmissibileService;

	@Autowired
	private IstruttoriaComponent istruttoriaComponent;

	@SpyBean
	private ConsumeExternalRestApi4Anagrafica consAnagrafica;

	@SpyBean
	private IoItaliaConsumerApi consIoIt;

	private static final Long ID_ISTRUTTORIA_OK = 1240981L;
	private static final Long ID_ISTRUTTORIA_KO = 1240982L;
	private static final Long ID_ISTRUTTORIA_INVIA_MSG = 9417577L;
	
	@Test
	@Transactional
	@Sql(scripts = { "/sql/ElaborazioneIstruttoriaPerCambioStatoNonAmmissibile.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/sql/ElaborazioneIstruttoriaPerCambioStatoNonAmmissibileDelete.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void elaboraCambiamentoDiStato() throws ElaborazioneIstruttoriaException {
		List<CaricaAgsDto> listPersona = new ArrayList<CaricaAgsDto>();
		CaricaAgsDto persona = new CaricaAgsDto();
		persona.setCodiceFiscale("URRRED82B02F839U");
		listPersona.add(persona);
		ComunicationDto com = new ComunicationDto();
		com.setCodiceFiscale("URRRED82B02F839U");
		doReturn(listPersona).when(consAnagrafica).getSoggettiFascicoloAziendale("URRRED82B02F839U");
		doReturn(null).when(consIoIt).inviaMessaggio(com);
		
		elaborazioneIstruttoriaPerCambioStatoNonAmmissibileService.elabora(ID_ISTRUTTORIA_OK);
		persistenceM.flush();

		IstruttoriaModel istruttoriaModel = istruttoriaComponent.load(ID_ISTRUTTORIA_OK);
		persistenceM.refresh(istruttoriaModel);
		assertThat(istruttoriaModel.getA4gdStatoLavSostegno().getIdentificativo()).isEqualTo(NON_AMMISSIBILE.getStatoIstruttoria());
		assertThat(istruttoriaModel.getTransizioni()).hasSize(1);
		
		verify(consIoIt, times(1)).inviaMessaggio(Mockito.any());
		ArgumentCaptor<ComunicationDto> argument = ArgumentCaptor.forClass(ComunicationDto.class);
		verify(consIoIt).inviaMessaggio(argument.capture());
		assertEquals("Domanda Unica: Sostegno Non Ammissibile", argument.getValue().getOggetto());
	}

	@Test
	@Transactional
	@Sql(scripts = { "/sql/ElaborazioneIstruttoriaPerCambioStatoNonAmmissibile.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/sql/ElaborazioneIstruttoriaPerCambioStatoNonAmmissibileDelete.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void elaboraCambiamentoDiStatoKOPerStatoAttualeNonConsono() {
		assertThatExceptionOfType(ElaborazioneIstruttoriaException.class)
		.isThrownBy(() -> elaborazioneIstruttoriaPerCambioStatoNonAmmissibileService.elabora(ID_ISTRUTTORIA_KO));
		persistenceM.flush();

		IstruttoriaModel istruttoriaModel = istruttoriaComponent.load(ID_ISTRUTTORIA_KO);
		persistenceM.refresh(istruttoriaModel);
		assertThat(istruttoriaModel.getStato()).isEqualTo(PAGAMENTO_AUTORIZZATO);
		assertThat(istruttoriaModel.getTransizioni()).hasSize(0);
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/sql/elaboraCambiamentoDiStatoConInviaMessaggio_insert.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/sql/elaboraCambiamentoDiStatoConInviaMessaggio_delete.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void elaboraCambiamentoDiStatoConInviaMessaggio() throws ElaborazioneIstruttoriaException {
		List<CaricaAgsDto> listPersona = new ArrayList<>();
		CaricaAgsDto persona = new CaricaAgsDto();
		persona.setCodiceFiscale("01083600229");
		listPersona.add(persona);
		ComunicationDto com = new ComunicationDto();
		com.setCodiceFiscale("01083600229");
		doReturn(listPersona).when(consAnagrafica).getSoggettiFascicoloAziendale("01083600229");
		doReturn(null).when(consIoIt).inviaMessaggio(com);

		elaborazioneIstruttoriaPerCambioStatoNonAmmissibileService.elabora(ID_ISTRUTTORIA_INVIA_MSG);
		persistenceM.flush();

		IstruttoriaModel istruttoriaModel = istruttoriaComponent.load(ID_ISTRUTTORIA_INVIA_MSG);
		persistenceM.refresh(istruttoriaModel);
		assertThat(istruttoriaModel.getA4gdStatoLavSostegno().getIdentificativo()).isEqualTo(NON_AMMISSIBILE.getStatoIstruttoria());
		assertThat(istruttoriaModel.getTransizioni()).hasSize(1);

		verify(consIoIt, times(1)).inviaMessaggio(Mockito.any());
		ArgumentCaptor<ComunicationDto> argument = ArgumentCaptor.forClass(ComunicationDto.class);
		verify(consIoIt).inviaMessaggio(argument.capture());
		assertEquals("Domanda Unica: Sostegno Non Ammissibile", argument.getValue().getOggetto());
	}
	
//  Nonostrante il servizio di invio del messaggio sulla piattaforma IoItalia vada in eccezione, lo stato dell'istruttoria deve cambiare comunque
	@Test
	@Transactional
	@Sql(scripts = { "/sql/elaboraCambiamentoDiStatoConInviaMessaggio_insert.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/sql/elaboraCambiamentoDiStatoConInviaMessaggio_delete.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	public void elaboraCambiamentoDiStatoConInviaMessaggioKO() throws ElaborazioneIstruttoriaException {
		doReturn(null).when(consAnagrafica).getSoggettiFascicoloAziendale("01083600229");
		elaborazioneIstruttoriaPerCambioStatoNonAmmissibileService.elabora(ID_ISTRUTTORIA_INVIA_MSG);
		persistenceM.flush();
		
		IstruttoriaModel istruttoriaModel = istruttoriaComponent.load(ID_ISTRUTTORIA_INVIA_MSG);
		persistenceM.refresh(istruttoriaModel);
		assertThat(istruttoriaModel.getA4gdStatoLavSostegno().getIdentificativo()).isEqualTo(NON_AMMISSIBILE.getStatoIstruttoria());
		assertThat(istruttoriaModel.getTransizioni()).hasSize(1);
	}

}
