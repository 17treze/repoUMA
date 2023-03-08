package it.tndigitale.a4g.ags;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.ags.dto.EsitoAntimafia;
import it.tndigitale.a4g.ags.utils.EmailUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EsitiAntimafiaTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private RestTemplate restTemplate;
	@SpyBean
	private EmailUtils emailUtils;

	@Test
	@WithMockUser(username = "UTENTEADMIN")
	public void salva() throws Exception {
		List<EsitoAntimafia> lista = new ArrayList<>();
		EsitoAntimafia esitoAntimafia = new EsitoAntimafia();
		esitoAntimafia.setCuaa("BRTLNZ86S21L378O");
		esitoAntimafia.setTipoDomanda("PSR_SUPERFICIE_EU");
		esitoAntimafia.setIdDomanda(190462L);
		esitoAntimafia.setDtInizioSilenzioAssenso(new Date());
		esitoAntimafia.setDtFineSilenzioAssenso(new Date());
		esitoAntimafia.setDtInizioEsitoNegativo(new Date());
		esitoAntimafia.setDtFineEsitoNegativo(new Date());
		
		EsitoAntimafia esitoAntimafia2 = new EsitoAntimafia();
		esitoAntimafia2.setCuaa("BRTLNZ86S21L378O");
		esitoAntimafia2.setTipoDomanda("DOMANDA_UNICA");
		esitoAntimafia2.setIdDomanda(190463L);
		esitoAntimafia2.setDtInizioSilenzioAssenso(new Date());
		esitoAntimafia2.setDtFineSilenzioAssenso(new Date());
		esitoAntimafia2.setDtInizioEsitoNegativo(new Date());
		esitoAntimafia2.setDtFineEsitoNegativo(new Date());
		
		EsitoAntimafia esitoAntimafia3 = new EsitoAntimafia();
		esitoAntimafia3.setCuaa("BRTLNZ86S21L378O");
		esitoAntimafia3.setTipoDomanda("PSR_STRUTTURALI_EU");
		esitoAntimafia3.setIdDomanda(190464L);
		esitoAntimafia3.setDtInizioSilenzioAssenso(new Date());
		esitoAntimafia3.setDtFineSilenzioAssenso(new Date());
		esitoAntimafia3.setDtInizioEsitoNegativo(new Date());
		esitoAntimafia3.setDtFineEsitoNegativo(new Date());
		
		lista.add(esitoAntimafia);
		lista.add(esitoAntimafia2);
		lista.add(esitoAntimafia3);
		this.mockMvc.perform(put("/api/v1/esiti/antimafia").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(lista))).andExpect(status().isOk()).andReturn();
	}
	
	@Test(expected = NestedServletException.class)
	@WithMockUser(username = "UTENTEADMIN")
	public void salvaKO() throws Exception {
		List<EsitoAntimafia> lista = new ArrayList<>();
		EsitoAntimafia esitoAntimafia = new EsitoAntimafia();
		esitoAntimafia.setCuaa("BRTLNZ86S21L378O");
		esitoAntimafia.setTipoDomanda("PSR_SUPERFICIE_EU");
		esitoAntimafia.setIdDomanda(190462L);
		esitoAntimafia.setDtInizioSilenzioAssenso(new Date());
		esitoAntimafia.setDtFineSilenzioAssenso(new Date());
		esitoAntimafia.setDtInizioEsitoNegativo(new Date());
		esitoAntimafia.setDtFineEsitoNegativo(new Date());
		
		EsitoAntimafia esitoAntimafia2 = new EsitoAntimafia();
		esitoAntimafia2.setCuaa("BRTLNZ86S21L378O");
		esitoAntimafia2.setTipoDomanda("DOMANDA_UNICA");
		esitoAntimafia2.setIdDomanda(190462L);
		esitoAntimafia2.setDtInizioSilenzioAssenso(new Date());
		esitoAntimafia2.setDtFineSilenzioAssenso(new Date());
		esitoAntimafia2.setDtInizioEsitoNegativo(new Date());
		esitoAntimafia2.setDtFineEsitoNegativo(new Date());
		
		lista.add(esitoAntimafia);
		lista.add(esitoAntimafia2);
		Mockito.doNothing().when(emailUtils).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		this.mockMvc.perform(put("/api/v1/esiti/antimafia").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(lista))).andExpect(status().isOk());
		Mockito.verify(emailUtils, times(1)).sendSimpleMessage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}	

	@Test
	@WithMockUser(username = "UTENTEADMIN")
	public void cancella() throws Exception {
		List<EsitoAntimafia> lista = new ArrayList<>();
		EsitoAntimafia esitoAntimafia = new EsitoAntimafia();
		esitoAntimafia.setCuaa("TLNZ86S21L378O");
		EsitoAntimafia esitoAntimafia2 = new EsitoAntimafia();
		esitoAntimafia2.setCuaa("TLNZ86S21L378O");
		lista.add(esitoAntimafia);
		lista.add(esitoAntimafia2);
		this.mockMvc.perform(put("/api/v1/esiti/antimafia").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(lista))).andExpect(status().isOk()).andReturn();
	}

	@Test
	@WithMockUser(username = "UTENTEADMIN")
	public void elimina() throws Exception {
		String paramsCancellaEsiti="?params=".concat(URLEncoder.encode("{\"cuaa\":\"MCHGDU38B23E500I\"}", StandardCharsets.UTF_8.name()));
		URI uriAgs = new URI("/api/v1/esiti/antimafia/".concat(paramsCancellaEsiti));
		this.mockMvc.perform(delete(uriAgs).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
	}
	
	@Test
	@WithMockUser(username = "UTENTEADMIN")
	public void recupera() throws Exception {
		String paramsRecuperaEsiti="?params=".concat(URLEncoder.encode("{\"cuaa\":\"TLNZ86S21L378O\"}", StandardCharsets.UTF_8.name()));
		URI uriAgs = new URI("/api/v1/esiti/antimafia".concat(paramsRecuperaEsiti));
		this.mockMvc.perform(get(uriAgs).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
	}
	
}
