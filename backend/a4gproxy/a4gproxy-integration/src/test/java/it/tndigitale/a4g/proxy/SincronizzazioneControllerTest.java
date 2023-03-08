package it.tndigitale.a4g.proxy;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

import it.tndigitale.a4g.proxy.dto.DatiPagamentiDto;
import it.tndigitale.a4g.proxy.dto.Dichiarazione;
import it.tndigitale.a4g.proxy.dto.SuperficiAccertateDto;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.DichiarazioneAntimafiaDao;
import it.tndigitale.a4g.proxy.services.EmailService;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@SqlGroup({ 
	@Sql({ "classpath:sincronizzazione/schema.sql" }),
	@Sql({ "classpath:sincronizzazione/data.sql" })
})
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
public class SincronizzazioneControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Resource
	private DichiarazioneAntimafiaDao dao;
	
	@MockBean
	private EmailService emailService;
	
	@Test
	public void getDichiarazione() throws IOException, Exception {
		MvcResult andReturn = this.mockMvc.perform(get("/api/v1/sincronizzazione/antimafia/666")).andExpect(status().is2xxSuccessful()).andReturn();
		Dichiarazione dichiarazione = objectMapper.readValue(andReturn.getResponse().getContentAsString(),Dichiarazione.class);
		assertEquals(dichiarazione.getIdAnti(), 666l);
	}
	
	@Test
	public void aggiornaDichiarazione() throws IOException, Exception {
		MvcResult andReturn = this.mockMvc.perform(get("/api/v1/sincronizzazione/antimafia/667")).andExpect(status().is2xxSuccessful()).andReturn();
		Dichiarazione dichiarazione = objectMapper.readValue(andReturn.getResponse().getContentAsString(),Dichiarazione.class);
		assertEquals(dichiarazione.getIdAnti(), 667l);
		
		dichiarazione.setDataFineVali(new Date());
		MvcResult mvcResult = this.mockMvc.perform(put("/api/v1/sincronizzazione/antimafia/667").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dichiarazione))).andExpect(status().is2xxSuccessful()).andReturn();
		Dichiarazione dichiarazioneAggiornata = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),Dichiarazione.class);
		assertEquals(dichiarazione.getIdAnti(), 667l);
		assertEquals(dichiarazione.getDataFineVali(), dichiarazioneAggiornata.getDataFineVali());
	}	
	
	@Test(expected=Exception.class)
	public void aggiornaDichiarazioneKO() throws IOException, Exception {
		Mockito.doNothing().when(emailService).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		MvcResult andReturn = this.mockMvc.perform(get("/api/v1/sincronizzazione/antimafia/667")).andExpect(status().is2xxSuccessful()).andReturn();
		Dichiarazione dichiarazione = objectMapper.readValue(andReturn.getResponse().getContentAsString(),Dichiarazione.class);
		assertEquals(dichiarazione.getIdAnti(), 667l);
		
		dichiarazione.setDataFineVali(new Date());
//		MvcResult mvcResult = 
		this.mockMvc.perform(put("/api/v1/sincronizzazione/antimafia/1020304050").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dichiarazione))).andExpect(status().is5xxServerError());
	}
	
	@Test
	public void creaSuperficiAccertateOk() throws Exception {
		this.mockMvc.perform(post("/api/v1/sincronizzazione/superfici-accertate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getSuperficiAccertateOK()))
			.andExpect(status().is2xxSuccessful()).andExpect(content().string("OK"));
	}
	
	@Test
	public void creaDatiPagamentiOk() throws Exception {
		this.mockMvc.perform(post("/api/v1/sincronizzazione/pagamenti")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getDatiPagamentiOK()))
			.andExpect(status().is2xxSuccessful()).andExpect(content().string("OK"));
	}
	
	@Test
	public void testInvioDatiPersonaGiuridicaOK() throws IOException, Exception {
		this.mockMvc.perform(post("/api/v1/sincronizzazione/antimafia").contentType(MediaType.APPLICATION_JSON).content(getRequestPersonaGiuridicaOK())).andExpect(status().is2xxSuccessful()).andExpect(content().string("OK"));
	}
	
	@Test
	public void testInvioDatiRequestDittaIndividualeOK() throws IOException, Exception {
		this.mockMvc.perform(post("/api/v1/sincronizzazione/antimafia").contentType(MediaType.APPLICATION_JSON).content(getRequestDittaIndividualeOK())).andExpect(status().is2xxSuccessful()).andExpect(content().string("OK"));
	}
	
	@Test
	public void testInvioDatiRequestDittaIndividualeConDirettoreOK() throws IOException, Exception {
		this.mockMvc.perform(post("/api/v1/sincronizzazione/antimafia").contentType(MediaType.APPLICATION_JSON).content(getRequestDittaIndividualeConDirettoreOK())).andExpect(status().is2xxSuccessful()).andExpect(content().string("OK"));
	}
		
	private String getRequestPersonaGiuridicaOK() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/sincronizzazione/requestPersonaGiuridicaOK.json"));
		return objectMapper.writeValueAsString(response);
	}
	private String getRequestDittaIndividualeOK() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/sincronizzazione/requestDittaIndividualeOK.json"));
		return objectMapper.writeValueAsString(response);
	}
	private String getRequestDittaIndividualeConDirettoreOK() throws IOException {
		JsonNode response = objectMapper.readTree(new File("src/test/resources/sincronizzazione/requestDittaIndividualeConDirettoreOK.json"));
		return objectMapper.writeValueAsString(response);
	}	
	
	private String getSuperficiAccertateOK() throws Exception {
		SuperficiAccertateDto dto = objectMapper.readValue("{\"annoCampagna\":2018,\"cuaa\":\"cuaatest\",\"identificativoDomanda\":\"123456\",\"motivazioneA1\":\"SI\",\"motivazioneA2\":\"SI\",\"motivazioneA3\":\"NO\",\"motivazioneB0\":\"SI\",\"superficieAccertata\":123.11,\"superficieDeterminata\":665.14}", SuperficiAccertateDto.class);
		return objectMapper.writeValueAsString(dto);
	}
	
	private String getDatiPagamentiOK() throws Exception {
		DatiPagamentiDto dto = objectMapper.readValue("{\"annoCampagna\":2018,\"codiceIntervento\":132,\"cuaa\":\"CCCDDD123D5D5111\",\"importoDeterminato\":10.11,\"importoLiquidato\":12.05,\"importoRichiesto\":45.23,\"numeroDomanda\":\"123456\",\"numeroProgressivoLavorazione\":111223,\"pagamentoAutorizzato\":true}", DatiPagamentiDto.class);
		return objectMapper.writeValueAsString(dto);
	}
}
