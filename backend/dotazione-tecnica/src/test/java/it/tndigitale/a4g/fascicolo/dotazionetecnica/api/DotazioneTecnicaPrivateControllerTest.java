package it.tndigitale.a4g.fascicolo.dotazionetecnica.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.ApiUrls;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MacchinaModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MacchinaMotorizzataModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.Alimentazione;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.MacchinaDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.TipologiaPossesso;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.MacchineService;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioMacchinaDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.MotoreDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.SottotipoDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.TipologiaDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.MacchinaAgsDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.TipoCarburante;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class DotazioneTecnicaPrivateControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired FascicoloDao fascicoloDao;
	@Autowired MacchinaDao macchinaDao;
	@Autowired MacchineService macchineService;
	@Autowired ObjectMapper mapper;
//	@Autowired DotazioneTecnicaService dotazioneTecnicaService;

//	@MockBean MacchineAgsService macchineAgsService;

    private List<MacchinaAgsDto> setupMacchineAgs(String cuaa) throws Exception 
    {
    	List<MacchinaAgsDto> macchineAgs = new ArrayList<>();
//    	MacchinaAgsFilter filter = new MacchinaAgsFilter();
//    	filter.setCuaa(Mockito.anyString());
    	MacchinaAgsDto dto = new MacchinaAgsDto();
		dto.setDescrizione("Test");
		dto.setTarga("TT000TT");
		dto.setClasse("MOTOTREBBIATRICE");
		dto.setSottoClasse("XXX");
		dto.setMarca("TESLA");
		dto.setAlimentazione(TipoCarburante.GASOLIO);
		dto.setPossesso("P");
		dto.setCodiceClasse("000008");
		dto.setCodiceSottoClasse("000056");
		dto.setIdMacchina(Long.valueOf(19906));
		dto.setIdTipoMacchina(Long.valueOf(21940));
		macchineAgs.add(dto);
//		Mockito.when(macchineAgsService.getMacchine(filter)).thenReturn(macchineAgs);
		return macchineAgs;
	}
    
    @Test
	@Sql(scripts = "/sql/get_macchinari_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_macchinari_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void migraMacchinaAgs() throws Exception {
		String cuaa = "PDRTTR69M30C794R";
    	List<MacchinaAgsDto> macchineAgs = setupMacchineAgs(cuaa);
		// dotazioneTecnicaService.migra(cuaa);
		for (MacchinaAgsDto m : macchineAgs) {
			macchineService.migra(cuaa, m);
		}
		List<MacchinaModel> macchine = macchinaDao.findByFascicolo_cuaaAndFascicolo_idValidazione(cuaa, 0);
		assertTrue(macchine.size() > 4);
		MacchinaModel m = macchine.get(0);
		assertEquals("targa", m.getTarga());
	}
	
	@Test
	@Sql(scripts = "/sql/get_macchinari_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_macchinari_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void valida_macchinari_ok() throws Exception {
		final String cuaa = "PDRTTR69M30C794R";
		final Integer idValidazione = 1;
		mockMvc.perform(put(ApiUrls.DOTAZIONE_TECNICA_PRIVATE + "/" + cuaa + "/" + idValidazione + "/validazione"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());

		//		reimposta la security; per qualche motivo dopo mockMvc.perform() viene cancellato
		SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());

		Optional<FascicoloModel> fascicoloModelValidatoOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, idValidazione);
		assertTrue(fascicoloModelValidatoOpt.isPresent());
		FascicoloModel fascicoloModelValidato = fascicoloModelValidatoOpt.get();
		List<MacchinaModel> macchine = fascicoloModelValidato.getMacchine();
		assertEquals(4, macchine.size());
		
		MacchinaMotorizzataModel macchina = new MacchinaMotorizzataModel();
		macchina = (MacchinaMotorizzataModel) macchine.get(0);

		assertNotNull(macchina);
		assertEquals("marca", macchina.getMarca());
		assertEquals("modello", macchina.getModello());
		assertEquals("matricola", macchina.getNumeroMatricola());
		assertEquals("telaio", macchina.getNumeroTelaio());
		assertEquals(2021, macchina.getAnnoDiCostruzione());
		assertEquals(TipologiaPossesso.COMODATO, macchina.getTipoPossesso());
		assertEquals("targa", macchina.getTarga());
		assertEquals("marca", macchina.getMarcaMotore());
		assertEquals("tipo", macchina.getTipoMotore());
		assertEquals(Alimentazione.BENZINA, macchina.getAlimentazione());
		assertEquals(1, macchina.getPotenza());
		assertEquals("matricola", macchina.getMatricola());

		Optional<FascicoloModel> fascicoloModelLiveOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		assertTrue(fascicoloModelLiveOpt.isPresent());
	}
	
	@Test
	@Sql(scripts = "/sql/get_macchinari_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_macchinari_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postMacchinaOk() throws Exception {
		TipologiaDto tipologia = new TipologiaDto()
		.setId(894L)
		.setDescrizione("ALTRE_ATTREZZATURE");
		List<TipologiaDto> tipologie = new ArrayList<TipologiaDto>();
		tipologie.add(tipologia);
							
		SottotipoDto sottotipo = new SottotipoDto();
		sottotipo.setId(874L);
		sottotipo.setTipologie(tipologie);
		
		MotoreDto motore = new MotoreDto()
				.setAlimentazione(Alimentazione.BENZINA)
				.setMarca("Marca Motore")
				.setMatricola("Matricola motore")
				.setPotenza(Double.valueOf(22))
				.setTipo("Tipo Motore");
		
		DettaglioMacchinaDto dto = new DettaglioMacchinaDto()
				.setAnnoCostruzione(2022)
				.setDataImmatricolazione(LocalDateTime.now())
				.setMarca("Marca")
				.setModello("Modello")
				.setMotore(motore)
				.setNumeroMatricola("Numero Matricola")
				.setNumeroTelaio("Numero Telaio")
				.setSottotipo(sottotipo)
				.setTarga("Targa")
				.setTipoPossesso(TipologiaPossesso.COMODATO);
		mockMvc.perform(buildRequest(dto))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());
	}
	@Test
	@Sql(scripts = "/sql/get_macchinari_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_macchinari_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postMacchinaSenzaMotoreOk() throws Exception {
		TipologiaDto tipologia = new TipologiaDto()
		.setId(894L)
		.setDescrizione("ALTRE_ATTREZZATURE");
		List<TipologiaDto> tipologie = new ArrayList<TipologiaDto>();
		tipologie.add(tipologia);
							
		SottotipoDto sottotipo = new SottotipoDto();
		sottotipo.setId(874L);
		sottotipo.setTipologie(tipologie);
		
		DettaglioMacchinaDto dto = new DettaglioMacchinaDto()
				.setAnnoCostruzione(2022)
				.setDataImmatricolazione(LocalDateTime.now())
				.setMarca("Marca")
				.setModello("Modello")
				.setNumeroMatricola("Numero Matricola")
				.setNumeroTelaio("Numero Telaio")
				.setSottotipo(sottotipo)
				.setTipoPossesso(TipologiaPossesso.COMODATO);
		mockMvc.perform(buildRequest(dto))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());
	}
	
	@Test
	@Sql(scripts = "/sql/get_macchinari_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_macchinari_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void modificaMacchinaOk() throws Exception {
		TipologiaDto tipologia = new TipologiaDto()
		.setId(894L)
		.setDescrizione("ALTRE_ATTREZZATURE");
		List<TipologiaDto> tipologie = new ArrayList<TipologiaDto>();
		tipologie.add(tipologia);
					
		SottotipoDto sottotipo = new SottotipoDto();
		sottotipo.setId(874L);
		sottotipo.setTipologie(tipologie);
		
		MotoreDto motore = new MotoreDto()
				.setAlimentazione(Alimentazione.BENZINA)
				.setMarca("Marca Motore")
				.setMatricola("Matricola motore")
				.setPotenza(Double.valueOf(22))
				.setTipo("Tipo Motore");
		
		DettaglioMacchinaDto dto = new DettaglioMacchinaDto()
				.setId(999L)
				.setAnnoCostruzione(2022)
				.setDataImmatricolazione(LocalDateTime.now())
				.setMarca("Marca")
				.setModello("Modello")
				.setMotore(motore)
				.setNumeroMatricola("Numero Matricola")
				.setNumeroTelaio("Numero Telaio")
				.setSottotipo(sottotipo)
				.setTarga("Targa")
				.setTipoPossesso(TipologiaPossesso.COMODATO);
		mockMvc.perform(buildRequest(dto))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());
	}
	
	@Test
	@Sql(scripts = "/sql/get_macchinari_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_macchinari_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void postMacchinaGiaEsistenteError() throws Exception {
		TipologiaDto tipologia = new TipologiaDto()
		.setId(894L)
		.setDescrizione("ALTRE_ATTREZZATURE");
		List<TipologiaDto> tipologie = new ArrayList<TipologiaDto>();
		tipologie.add(tipologia);
				
		SottotipoDto sottotipo = new SottotipoDto();
		sottotipo.setId(874L);
		sottotipo.setTipologie(tipologie);
				
		DettaglioMacchinaDto dto = new DettaglioMacchinaDto()
		.setNumeroMatricola("matricola")
		.setNumeroTelaio("telaio new")
		.setSottotipo(sottotipo);
		
		MvcResult result = this.mockMvc.perform(buildRequest(dto)).andReturn();
		assertThat(result.getResponse().getStatus()).isEqualTo(MockHttpServletResponse.SC_CONFLICT);
	}
	

	@Test
	@Sql(scripts = "/sql/get_macchinari_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_macchinari_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void modificaMacchinaGiaEsistenteError() throws Exception {
		TipologiaDto tipologia = new TipologiaDto()
		.setId(894L)
		.setDescrizione("ALTRE_ATTREZZATURE");
		List<TipologiaDto> tipologie = new ArrayList<TipologiaDto>();
		tipologie.add(tipologia);
		
		SottotipoDto sottotipo = new SottotipoDto();
		sottotipo.setId(874L);
		sottotipo.setTipologie(tipologie);
		
		DettaglioMacchinaDto dto = new DettaglioMacchinaDto()
				.setId(991L)
				.setNumeroMatricola("matricola")
				.setNumeroTelaio("telaio")
				.setSottotipo(sottotipo);
		MvcResult result = this.mockMvc.perform(buildRequest(dto)).andReturn();
		assertThat(result.getResponse().getStatus()).isEqualTo(MockHttpServletResponse.SC_CONFLICT);
	}
	
	@Test
	@Sql(scripts = "/sql/get_macchinari_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/get_macchinari_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void cancellaMacchinaOk() throws Exception {
		mockMvc.perform(delete(ApiUrls.DOTAZIONE_TECNICA_PRIVATE + "/PDRTTR69M30C794R" + ApiUrls.MACCHINE + "/990"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(status().isOk());
	}
	
	private MockHttpServletRequestBuilder buildRequest(DettaglioMacchinaDto dto) throws IOException {
		MockMultipartFile jsonPart = new MockMultipartFile("dati", "dati", "application/json", mapper.writeValueAsString(dto).getBytes());

		MockHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.multipart(ApiUrls.DOTAZIONE_TECNICA_PRIVATE + "/PDRTTR69M30C794R" + ApiUrls.MACCHINE)
				.file("documento", "Hello, World!".getBytes())
				.file(jsonPart)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE);
		return builder;
	}
	
//	private MockHttpServletRequestBuilder buildRequestMigra(String cuaa) throws IOException {
//		String endpoint = String.format(ApiUrls.DOTAZIONE_TECNICA_PRIVATE + "/%s/migra", cuaa);
//		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(endpoint);
//		return builder;
//	}
	
}
