package it.tndigitale.a4gistruttoria.service.businesslogic.liquidazione;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.A4gistruttoriaConfigurazione;
import it.tndigitale.a4gistruttoria.api.ApiUrls;
import it.tndigitale.a4gistruttoria.dto.lavorazione.Cuaa;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.ElencoLiquidazioneDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.ElencoLiquidazioneModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;

@RunWith(SpringRunner.class)
@SpringBootTest
// @AutoConfigureTestDatabase
@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.visualizza", "a4gistruttoria.pac.domandaUnica.edita" })
public class ElencoLiquidazioneZootecniaTests {
	

	@Autowired
	private DomandaUnicaDao daoDomanda;

	@Autowired
	private ElencoLiquidazioneDao daoElencoLiquidazione;
	
	@Autowired
	private IstruttoriaDao istruttoriaDao;

	@Autowired
	private A4gistruttoriaConfigurazione configurazione;
	
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private RestTemplate restTemplate;
	
	@Autowired
	private LiquidazioneIstruttoriaService liquidazioneIstruttoria;
	

	@Test
	@Transactional
	@Sql(scripts = { "/DomandaUnica/intersostegno/elencoLiquidazioneZootecniaOK_insert.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = {"/DomandaUnica/intersostegno/elencoLiquidazioneZootecniaOK_delete.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void creaElencoLiquidazioneOK() throws Exception {
		String cuaa = "MFFFBA75B28L1741";
		mockCUAAFromAgs(cuaa);
		Integer anno = 2018;
		
		DomandaUnicaModel domanda = daoDomanda.findByCuaaIntestatarioAndCampagna(cuaa, anno);
		IstruttoriaModel istruttoria = istruttoriaDao.findByDomandaUnicaModelAndSostegnoAndTipologia(domanda, Sostegno.ZOOTECNIA, TipoIstruttoria.SALDO);
		ElencoLiquidazioneModel elencoLiq = daoElencoLiquidazione.findByCodElenco("009-801-20190806-8517929");
		String rigaSoc = liquidazioneIstruttoria.generaRigaTracciato(istruttoria.getId(), anno, Sostegno.ZOOTECNIA, elencoLiq, 1);
		assertNotNull(rigaSoc);
		assertThat(rigaSoc.length()).isEqualTo(CampoElencoLiquidazione.FILLER.getEnd() + 1);
		String attesa = "D100000100920180520180615009201801191061MAFFEI FABIO                                                          MAFFEI                                            FABIO                                             M19750228022199TN MFFFBA75B28L1741VIA DELLE MASERE 4                           022143 TN38086010306935264IT45P100000000439000001683834800310260181310100011600000000002000008013102601813201000116000000000069610780131126018134010001160000000000787727      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000                                                                                                                                                                009   0008517929                                                                                                                                                                                       ";
		assertThat(rigaSoc).isEqualTo(attesa);
	}

	protected void mockCUAAFromAgs(String codiceFiscale) throws Exception {
		String resource = configurazione.getUriAgs().concat(ApiUrls.AGS_CUAA).concat("?cuaa=").concat(codiceFiscale);

        File mockDichiarazioni = new File("src/test/resources/DomandaUnica/intersostegno/cuaa_ags_" + codiceFiscale + ".json");
		Cuaa infoCuaa = objectMapper.readValue(mockDichiarazioni, Cuaa.class);

		Mockito.when(restTemplate.getForObject(Mockito.eq(new URI(resource)), Mockito.eq(Cuaa.class))).thenReturn(infoCuaa);
		
	}
}
