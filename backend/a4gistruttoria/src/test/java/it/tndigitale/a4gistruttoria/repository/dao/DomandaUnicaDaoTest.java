package it.tndigitale.a4gistruttoria.repository.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.specification.DomandaUnicaSpecificationBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
// @AutoConfigureTestDatabase
public class DomandaUnicaDaoTest {
	@Autowired
	private DomandaUnicaDao domandaUnicaDao;
	
	@MockBean
	private RestTemplate restTemplate;
	
	
	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/piccoloAgricoltore/piccolo_agricoltore_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/piccoloAgricoltore/piccolo_agricoltore_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void controllaPiccoliAgricoltori() {
		final Map<Integer, List<String>> piccoliAgricoltori  = new HashMap<Integer, List<String>>() {
			private static final long serialVersionUID = -9032068096462196991L;
			{
				put(2018, Arrays.asList("BNLLNI74P06C372M", "PDRMRC34D26E850W", "PRGLDI55M62L378W", "SGHFNC59M27L378J", "SGHVNI81A06L378I", "TVNRNT59P66H639V"));
				put(2019, Arrays.asList("BNLLNI74P06C372M", "PRGLDI55M62L378W", "SGHFNC59M27L378J", "SGHVNI81A06L378I", "TVNRNT59P66H639V"));
			}
		};
		List<Integer> anniCampagna = new ArrayList<Integer>();
		anniCampagna.add(2018);
		anniCampagna.add(2019);
		boolean match = true;
		for (Integer annoCampagna : anniCampagna) {
			List<String> listaCuaa = piccoliAgricoltori.get(annoCampagna);
			List<DomandaUnicaModel> domande =
					domandaUnicaDao.findByCampagnaAndStatoNotPiccoliAgricoltori(
							annoCampagna, StatoDomanda.ACQUISITA);
			assertNotNull(domande);
			match = domande.stream().anyMatch(d -> listaCuaa.contains(d.getCuaaIntestatario()));
			assertFalse(match);
		}
	}
	
	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/piccoloAgricoltore/piccolo_agricoltore_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "/DomandaUnica/piccoloAgricoltore/piccolo_agricoltore_delete.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void controllaNonPiccoloAgricoltore_PDRMRC34D26E850W() {
		final String nonPiccoloAgricoltore = "PDRMRC34D26E850W";
		List<DomandaUnicaModel> domande =
				domandaUnicaDao.findByCampagnaAndStatoNotPiccoliAgricoltori(
						2019, StatoDomanda.ACQUISITA);
		assertNotNull(domande);
		assertTrue(domande.stream().anyMatch(d -> d.getCuaaIntestatario().contains(nonPiccoloAgricoltore)));
	}

	@Test
	@Transactional
	@Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_insert.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/DomandaUnica/ricevibilita/avvio_istruttoria_delete.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void domandeIstruttoria() {
		final String nonPiccoloAgricoltore = "PDRMRC34D26E850W";
		final TipoIstruttoria tipoIstruttoria = TipoIstruttoria.ANTICIPO;
		final Integer campagna = 2019;
		List<DomandaUnicaModel> domande = domandaUnicaDao.findAll(DomandaUnicaSpecificationBuilder.statoNotPiccoliAgricoltoriAndNotTipoIstruttoriaAndNotNonRicevibile(campagna, tipoIstruttoria));
		assertNotNull(domande);
		assertTrue(domande.stream().anyMatch(d -> d.getCuaaIntestatario().contains(nonPiccoloAgricoltore)));
	}
}
