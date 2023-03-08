package it.tndigitale.a4gistruttoria.service.businesslogic.domanda;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4gistruttoria.dto.IstruttoriaDomandaUnicaFilter;
import it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria.ConfIstruttorieDto;
import it.tndigitale.a4gistruttoria.dto.istruttoria.IstruttoriaDomandaUnica;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.service.businesslogic.IstruttoriaService;
import it.tndigitale.a4gistruttoria.service.businesslogic.configurazioneistruttoria.ConfigurazioneIstruttoriaService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AvvioIstruttorieDomandaScheduledComponentTest {

    private static final int CAMPAGNA = 2020;
    private static final LocalDate TODAY_SALDO = LocalDate.of(CAMPAGNA, 12, 1);
    private static final LocalDate TODAY_ANTICIPO = LocalDate.of(CAMPAGNA, 9, 1);
	
    @MockBean
    private Clock clock;
    
	@Autowired
	private AvvioIstruttorieDomandaScheduledComponent service;
	
	@Autowired
	private IstruttoriaService istruttorieService;
	
	@Autowired
	private ConfigurazioneIstruttoriaService confIstruttoriaService;
	
	/**
	 * Caso di test che verifica
	 * senza configurazione istruttorie per l'anno (2020), 1 domanda che
	 * ha richiesto disaccoppiato
	 * senza istruttorie 
	 * =>
	 * Registra l'istruttoria di disaccoppiato, tipo saldo, stato richiesto
	 * 
	 * @throws Exception
	 */
    @Test
    @Transactional
    @Sql(scripts = {"/DomandaUnica/avvioIstruttorie/domanda1_2020_disaccoppiato_senzaIstruttorie_insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/DomandaUnica/avvioIstruttorie/domanda1_2020_disaccoppiato_senzaIstruttorie_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testAvvioIstruttorieDomandaDisaccoppiatoSenzaIstruttorie() throws Exception {
    	// verifico che la domanda in partenza non abbia istruttorie collegate
		IstruttoriaDomandaUnicaFilter filtro = new IstruttoriaDomandaUnicaFilter();
		filtro.setCampagna(CAMPAGNA);
		filtro.setNumeroDomanda(1L);
		List<IstruttoriaDomandaUnica> istruttorie = istruttorieService.ricerca(filtro);
		assertNotNull(istruttorie);
		assertThat(istruttorie).isEmpty();

		// MOCK
		Mockito.doReturn(TODAY_SALDO).when(clock).today();

		// SERVIZIO DI BUSINESS DA TESTARE
		service.avvioSaldi();
		
		istruttorie = istruttorieService.ricerca(filtro);
		assertNotNull(istruttorie);
		assertThat(istruttorie).hasSize(1);
		IstruttoriaDomandaUnica istruttoria = istruttorie.get(0);
		assertThat(istruttoria.getSostegno()).isEqualTo(Sostegno.DISACCOPPIATO);		
		assertThat(istruttoria.getStato()).isEqualTo(StatoIstruttoria.RICHIESTO);		
		assertThat(istruttoria.getTipo()).isEqualTo(TipoIstruttoria.SALDO);		
	}

	/**
	 * Caso di test che verifica
	 * senza configurazione istruttorie per l'anno (2020), 1 domanda che
	 * ha richiesto disaccoppiato e superficie
	 * senza istruttorie 
	 * =>
	 * Registra 2 istruttorie: 
	 * l'istruttoria di disaccoppiato, tipo saldo, stato richiesto
	 * l'istruttoria di superficie, tipo saldo, stato richiesto
	 * 
	 * @throws Exception
	 */
    @Test
    @Transactional
    @Sql(scripts = {"/DomandaUnica/avvioIstruttorie/domanda1_2020_multiSostegno_senzaIstruttorie_insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/DomandaUnica/avvioIstruttorie/domanda1_2020_disaccoppiato_senzaIstruttorie_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testAvvioIstruttorieDomandaDisaccoppiatoSuperficieSenzaIstruttorie() throws Exception {
    	// verifico che la domanda in partenza non abbia istruttorie collegate
		IstruttoriaDomandaUnicaFilter filtro = new IstruttoriaDomandaUnicaFilter();
		filtro.setCampagna(CAMPAGNA);
		filtro.setNumeroDomanda(1L);
		List<IstruttoriaDomandaUnica> istruttorie = istruttorieService.ricerca(filtro);
		assertNotNull(istruttorie);
		assertThat(istruttorie).isEmpty();

		// MOCK
		Mockito.doReturn(TODAY_SALDO).when(clock).today();

		// SERVIZIO DI BUSINESS DA TESTARE
		service.avvioSaldi();

		//VERIFICHE
		istruttorie = istruttorieService.ricerca(filtro);
		assertNotNull(istruttorie);
		assertThat(istruttorie).hasSize(2);
		IstruttoriaDomandaUnica istruttoria = istruttorie.stream().filter(i -> Sostegno.DISACCOPPIATO.equals(i.getSostegno())).findFirst().orElse(null);
		assertNotNull(istruttoria);
		assertThat(istruttoria.getSostegno()).isEqualTo(Sostegno.DISACCOPPIATO);		
		assertThat(istruttoria.getStato()).isEqualTo(StatoIstruttoria.RICHIESTO);		
		assertThat(istruttoria.getTipo()).isEqualTo(TipoIstruttoria.SALDO);	
		
		istruttoria = istruttorie.stream().filter(i -> Sostegno.SUPERFICIE.equals(i.getSostegno())).findFirst().orElse(null);
		assertNotNull(istruttoria);
		assertThat(istruttoria.getSostegno()).isEqualTo(Sostegno.SUPERFICIE);		
		assertThat(istruttoria.getStato()).isEqualTo(StatoIstruttoria.RICHIESTO);		
		assertThat(istruttoria.getTipo()).isEqualTo(TipoIstruttoria.SALDO);	
	}

	/**
	 * Caso di test che verifica
	 * con configurazione istruttorie per l'anno (2020), 1 domanda che
	 * ha richiesto disaccoppiato con istruttoria di anticipo
	 * =>
	 * La configurazione passa a percentuale pagamento 100% 
	 * Registra l'istruttoria di disaccoppiato, tipo saldo, stato richiesto
	 * 
	 * @throws Exception
	 */
    @Test
    @Transactional
    @Sql(scripts = {"/DomandaUnica/avvioIstruttorie/domanda1_2020_percentualiConfigurate_disaccoppiato_conAnticipo_insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/DomandaUnica/avvioIstruttorie/domanda1_2020_disaccoppiato_senzaIstruttorie_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Ignore
	public void testAvvioIstruttorieDomandaDisaccoppiatoConAnticipoeConfigurazione() throws Exception {
    	// verifico che la domanda in partenza non abbia istruttorie di saldo collegate (ma solo quella di anticipo)
		IstruttoriaDomandaUnicaFilter filtro = new IstruttoriaDomandaUnicaFilter();
		filtro.setCampagna(CAMPAGNA);
		filtro.setNumeroDomanda(1L);
		List<IstruttoriaDomandaUnica> istruttorie = istruttorieService.ricerca(filtro);
		assertNotNull(istruttorie);
		assertThat(istruttorie).hasSize(1);
		IstruttoriaDomandaUnica istruttoria = istruttorie.get(0);
		assertThat(istruttoria.getSostegno()).isEqualTo(Sostegno.DISACCOPPIATO);		
		assertThat(istruttoria.getStato()).isEqualTo(StatoIstruttoria.RICHIESTO);		
		assertThat(istruttoria.getTipo()).isEqualTo(TipoIstruttoria.ANTICIPO);		

		// MOCK
		Mockito.doReturn(TODAY_SALDO).when(clock).today();

		// SERVIZIO DI BUSINESS DA TESTARE
		service.avvioSaldi();
		
		//VERIFICHE
		ConfIstruttorieDto configurazione = confIstruttoriaService.getConfIstruttorie(CAMPAGNA);
		assertNotNull(configurazione);
		assertThat(configurazione.getPercentualePagamento()).isEqualByComparingTo(1D);
		
		istruttorie = istruttorieService.ricerca(filtro);
		assertNotNull(istruttorie);
		assertThat(istruttorie).hasSize(2);
		istruttoria = istruttorie.stream().filter(i -> TipoIstruttoria.SALDO.equals(i.getTipo())).findFirst().orElse(null);
		assertNotNull(istruttoria);
		assertThat(istruttoria.getSostegno()).isEqualTo(Sostegno.DISACCOPPIATO);		
		assertThat(istruttoria.getStato()).isEqualTo(StatoIstruttoria.RICHIESTO);		
		assertThat(istruttoria.getTipo()).isEqualTo(TipoIstruttoria.SALDO);		
	}

	/**
	 * Caso di test che verifica se metodo gira a settmbre 
	 * senza configurazione istruttorie per l'anno (2020), 1 domanda che
	 * ha richiesto disaccoppiato
	 * senza istruttorie 
	 * =>
	 * Registra l'istruttoria di disaccoppiato, tipo saldo, stato richiesto
	 * 
	 * @throws Exception
	 */
    @Test
    @Transactional
    @Sql(scripts = {"/DomandaUnica/avvioIstruttorie/domanda1_2020_disaccoppiato_senzaIstruttorie_insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/DomandaUnica/avvioIstruttorie/domanda1_2020_disaccoppiato_senzaIstruttorie_delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testAvvioIstruttoriaSettembreDomandaDisaccoppiatoSenzaIstruttorie() throws Exception {
    	// verifico che la domanda in partenza non abbia istruttorie collegate
		IstruttoriaDomandaUnicaFilter filtro = new IstruttoriaDomandaUnicaFilter();
		filtro.setCampagna(CAMPAGNA);
		filtro.setNumeroDomanda(1L);
		List<IstruttoriaDomandaUnica> istruttorie = istruttorieService.ricerca(filtro);
		assertNotNull(istruttorie);
		assertThat(istruttorie).isEmpty();

		// MOCK
		Mockito.doReturn(TODAY_ANTICIPO).when(clock).today();

		// SERVIZIO DI BUSINESS DA TESTARE
		service.avvioSaldi();
		
		istruttorie = istruttorieService.ricerca(filtro);
		assertNotNull(istruttorie);
		assertThat(istruttorie).hasSize(1);
		IstruttoriaDomandaUnica istruttoria = istruttorie.get(0);
		assertThat(istruttoria.getSostegno()).isEqualTo(Sostegno.DISACCOPPIATO);		
		assertThat(istruttoria.getStato()).isEqualTo(StatoIstruttoria.RICHIESTO);		
		assertThat(istruttoria.getTipo()).isEqualTo(TipoIstruttoria.ANTICIPO);		
	}
}
