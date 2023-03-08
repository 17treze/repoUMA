package it.tndigitale.a4gistruttoria.service.businesslogic.processo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.dto.DatiElaborazioneProcesso;
import it.tndigitale.a4gistruttoria.dto.istruttoria.InputProcessoIstruttorieDto;
import it.tndigitale.a4gistruttoria.repository.dao.ProcessoDao;
import it.tndigitale.a4gistruttoria.repository.model.A4gtProcesso;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import it.tndigitale.a4gistruttoria.service.businesslogic.exceptions.ElaborazioneIstruttoriaException;
import it.tndigitale.a4gistruttoria.service.businesslogic.processo.istruttoria.ElaboraCalcoloCapi;
import it.tndigitale.a4gistruttoria.service.businesslogic.zootecnia.calcolocapi.CalcoloCapiService;
import it.tndigitale.a4gistruttoria.util.MapperWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest
// @AutoConfigureTestDatabase
public class ElaboraCalcoloCapiTest {

	@Autowired
    private ElaboraCalcoloCapi elaboraCalcoloCapi;
	@MockBean
	private	CalcoloCapiService calcoloCapiService;
	@Autowired
	private ProcessoDao processoDao;
	@Autowired
	private MapperWrapper mapperWrapper;
	
    private static final Long ID_PROCESSO = 986789L;
    
	
	@Test
	@WithMockUser(username = "istruttore", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
	@Transactional
	@Sql(scripts = "/IstruttoriaAntimafia/capiProcessoTestRun_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/IstruttoriaAntimafia/capiProcessoTestRun_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void avviaProcessoCalcoloCapi() throws Exception {
		final List<Long> idIstruttorie =  Arrays.asList(893656l,893657l,893658l);
		doThrow(ElaborazioneIstruttoriaException.class).when(calcoloCapiService).elabora(893657l);
		elaboraCalcoloCapi.avvia(datiProcessoCalcoloCapi(idIstruttorie));
		A4gtProcesso processo = processoDao.findById(ID_PROCESSO).orElseThrow(() -> new RuntimeException("Identificativo processo non trovato"));
		assertEquals(processo.getPercentualeAvanzamento(), BigDecimal.valueOf(100));
		assertEquals(processo.getStato(), StatoProcesso.OK);
		DatiElaborazioneProcesso elaborazioneProcessoIstruttoria = mapperWrapper.readValue(processo.getDatiElaborazione(),DatiElaborazioneProcesso.class);
		assertTrue(elaborazioneProcessoIstruttoria.getConProblemi().contains("893657"));
		assertTrue(elaborazioneProcessoIstruttoria.getGestite().containsAll(Arrays.asList("893656","893658")));
	}
	

	private InputProcessoIstruttorieDto datiProcessoCalcoloCapi(List<Long> idIstruttoria) {
		InputProcessoIstruttorieDto input = new InputProcessoIstruttorieDto();
		input.setIdIstruttorie(idIstruttoria);
		input.setTipoProcesso(TipoProcesso.CALCOLO_CAPI_ISTRUTTORIE);
		input.setIdProcesso(986789L);
		return input;
	}
}
