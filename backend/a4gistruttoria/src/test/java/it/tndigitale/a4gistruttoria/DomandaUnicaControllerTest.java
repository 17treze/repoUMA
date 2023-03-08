package it.tndigitale.a4gistruttoria;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.tndigitale.a4g.soc.client.model.ImportoLiquidato;
import it.tndigitale.a4g.soc.client.model.Debito;
import it.tndigitale.a4gistruttoria.repository.dao.DomandaUnicaDao;
import it.tndigitale.a4gistruttoria.repository.dao.IstruttoriaDao;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi4Ags;
import it.tndigitale.a4gistruttoria.util.ConsumeExternalRestApi4Soc;

@RunWith(SpringRunner.class)
@SpringBootTest
// @AutoConfigureTestDatabase
@AutoConfigureMockMvc
@WithMockUser(username = "utente", roles = { "a4gistruttoria.pac.domandaUnica.edita" })
public class DomandaUnicaControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private DomandaUnicaDao domandaUnicaDao;
	@Autowired
	private IstruttoriaDao istruttoriaDao;
	
	@MockBean
	private ConsumeExternalRestApi4Ags consumeExternalRestApiA4gs;
	
	@MockBean
	private ConsumeExternalRestApi4Soc consumeExternalRestApi4Soc;
	
	@Test
	@Sql(scripts = "/DomandaUnica/annulloDomanda/annulloDomanda_Success_FailSpostaDomandaInProtocollato_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/annulloDomanda/annulloDomanda_Success_FailSpostaDomandaInProtocollato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void annullaIstruttoriaDomandaSuccess() throws Exception {
		Mockito.when(consumeExternalRestApiA4gs.spostaDomandaInProtocollata(Mockito.any())).thenReturn(true);
		
		mockMvc.perform(post("/api/v1/domandaunica/844019/annulla")).andDo(print());
		assertThat(domandaUnicaDao.findById(844019L).isPresent()).isFalse();
		assertThat(istruttoriaDao.findById(953088L).isPresent()).isFalse();
		assertThat(istruttoriaDao.findById(953089L).isPresent()).isFalse();
	}
	
	@Test
	@Sql(scripts = "/DomandaUnica/annulloDomanda/annulloDomanda_FailPagamentoAutorizzato_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/annulloDomanda/annulloDomanda_FailPagamentoAutorizzato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void annullaIstruttoriaDomandaFailPagamentoAutorizzato() throws Exception {
		 Assertions.assertThatThrownBy(() ->
         mockMvc.perform(post("/api/v1/domandaunica/844491/annulla")).andExpect(status().isInternalServerError()))
         .hasCause(new IllegalArgumentException("ANNULLA_ISTRUTTORIA_DOMANDA_IN_PAGAMENTO"));
    }
	
	@Test
	@Sql(scripts = "/DomandaUnica/annulloDomanda/annulloDomanda_Success_FailSpostaDomandaInProtocollato_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/annulloDomanda/annulloDomanda_Success_FailSpostaDomandaInProtocollato_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void annullaIstruttoriaDomandaFailSpostaDomandaInProtocollato() throws Exception {
		Mockito.when(consumeExternalRestApiA4gs.spostaDomandaInProtocollata(Mockito.any())).thenThrow(RuntimeException.class);
		
		Assertions.assertThatThrownBy(() ->
        mockMvc.perform(post("/api/v1/domandaunica/844019/annulla")).andExpect(status().isInternalServerError()))
        .hasCause(new IllegalArgumentException("ANNULLA_ISTRUTTORIA_MOVIMENTAZIONE_AGS"));
		
		assertThat(domandaUnicaDao.getOne(844019L)).isNotNull();
		assertThat(istruttoriaDao.getOne(953088L)).isNotNull();
		assertThat(istruttoriaDao.getOne(953089L)).isNotNull();
	}
	
	@Test
	@Sql(scripts = "/DomandaUnica/sintesiPagamento/getSintesiPagamento_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/DomandaUnica/sintesiPagamento/getSintesiPagamento_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    public void getSintesiPagamento() throws Exception {
		// Mock dati retrieveImpLiquidazioneByApi
		ImportoLiquidato importoLiquidato = new ImportoLiquidato();
		importoLiquidato.setTotaleRecuperato(new BigDecimal(123.45));
		importoLiquidato.setIncassatoNetto(new BigDecimal(678.91));
		Debito debito = new Debito();
		debito.setDescrizioneCapitolo("Debito di test");
		debito.setImporto(new BigDecimal(234.56));
		List<Debito> debiti = new ArrayList<Debito>();
		debiti.add(debito);
		importoLiquidato.setDebiti(debiti);
		List<ImportoLiquidato> importi = new ArrayList<ImportoLiquidato>();
		importi.add(importoLiquidato);
		
		Mockito.when(consumeExternalRestApi4Soc.retrieveImpLiquidazioneByApi(Mockito.any())).thenReturn(importi);
		
		mockMvc.perform(get("/api/v1/domandaunica/182439/sintesipagamentiByNumeroDomanda")).andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.importoCalcolato").value(1500.30))
				.andExpect(jsonPath("$.importoLiquidato").value(678.91));
	}
	
}
