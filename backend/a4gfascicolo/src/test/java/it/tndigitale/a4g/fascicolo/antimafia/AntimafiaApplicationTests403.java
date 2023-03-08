package it.tndigitale.a4g.fascicolo.antimafia;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.antimafia.dto.AllegatoFamiliariConviventi;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Azienda;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DatiDichiarazioneAntimafia;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Dichiarazione;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Nota;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Richiedente;
import it.tndigitale.a4g.fascicolo.antimafia.dto.StatoDic;
import it.tndigitale.a4g.fascicolo.antimafia.dto.TipoNotaEnum;
import it.tndigitale.a4g.framework.security.service.UtenteClient;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class AntimafiaApplicationTests403 {
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private  UtenteClient abilitazioniUtente;
	
	@Before 
	public void initialize() throws Exception {
		Mockito.when(abilitazioniUtente.getAziendeUtente()).thenReturn(new ArrayList<String>());
	}
	
	@Test
	@WithMockUser(username = "utente", roles = { "a4gfascicolo.altro.ricerca.tutti" })
	public void getDichiarazioniNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia").param("params", "{\"azienda\":{\"cuaa\":\"TRRCST78B08C794X\"}}")).andExpect(status().isForbidden());
		this.mockMvc.perform(get("/api/v1/antimafia").param("params", "{\"azienda\":{\"cuaa\":\"TRRCST78B08C794X\"},\"expand\":\"pdfFirmato\"}")).andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(username = "utente", roles = { "a4gfascicolo.altro.ricerca.tutti" })
	public void getDichiarazioni2NonAutorizzato403() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia").param("params", "{\"stato\":{\"identificativo\":\"PROTOCOLLATA\"}}")).andExpect(status().isForbidden());
		this.mockMvc.perform(get("/api/v1/antimafia").param("params", "{\"stato\":{\"identificativo\":\"PROTOCOLLATA\"},\"expand\":\"pdfFirmato\"}")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "utente", roles = { "a4gfascicolo.altro.ricerca.tutti" })
	public void getDichiarazioneNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia/32")).andExpect(status().isForbidden());
		this.mockMvc.perform(get("/api/v1/antimafia/40?expand=pdfFirmato")).andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ANTIMAFIA_TUTTI_COD + "altro" })
	public void creaDichiarazioneNonAutorizzato403() throws Exception {
		Dichiarazione dichiarazione = new Dichiarazione();
		dichiarazione.setId(100L);
		Azienda azienda = new Azienda();
		azienda.setCuaa("TRRCST78B08C794X");
		dichiarazione.setAzienda(azienda);
		dichiarazione.setDtUltimoAggiornamento(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDtInizioCompilazione(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDatiDichiarazione(new DatiDichiarazioneAntimafia());
		dichiarazione.getDatiDichiarazione().setRichiedente(new Richiedente());
		dichiarazione.getDatiDichiarazione().getRichiedente().setCodiceFiscale("BZZPLA64A31A178M");
		this.mockMvc.perform(post("/api/v1/antimafia").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dichiarazione))).andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ANTIMAFIA_TUTTI_COD + "altro" })
	public void aggiornaDichiarazioneNonAutorizzato403() throws Exception {

		Dichiarazione dichiarazione = new Dichiarazione();
		Azienda azienda = new Azienda();
		azienda.setCuaa("TRRCST78B08C794M");
		StatoDic stato = new StatoDic();
		stato.setIdentificativo(StatoDichiarazioneEnum.CHIUSA.getIdentificativoStato());
		dichiarazione.setAzienda(azienda);
		dichiarazione.setStato(stato);
		dichiarazione.setDtUltimoAggiornamento(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setDtInizioCompilazione(new Date(java.util.Calendar.getInstance().getTimeInMillis()));
		dichiarazione.setIdProtocollo("PROTOCOLLO 32");
		dichiarazione.setDatiDichiarazione(new DatiDichiarazioneAntimafia());
		dichiarazione.getDatiDichiarazione().setRichiedente(new Richiedente());
		dichiarazione.getDatiDichiarazione().getRichiedente().setCodiceFiscale("BZZPLA64A31A178X");

		this.mockMvc.perform(put("/api/v1/antimafia/32").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dichiarazione))).andExpect(status().isForbidden());
	}	

	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ANTIMAFIA_TUTTI_COD + "altro" })
	public void allegaFamiliariConviventiConPdfConFirmaAutografaNonAutorizzato403() throws Exception {
		String fileName = "dichiarazioneFamigliariConviventi.pdf";
		Path filePdfPath = Paths.get("./src/test/resources/uploadFile/" + fileName);

		AllegatoFamiliariConviventi allegatoFamiliariConviventi = new AllegatoFamiliariConviventi();
		allegatoFamiliariConviventi.setCfSoggettoImpresa("TRRCST78B08C794X");
		allegatoFamiliariConviventi.setCodCarica("TIT");
		allegatoFamiliariConviventi.setTipoFile("pdf");
		allegatoFamiliariConviventi.setDtPdfDichFamConv(new java.util.Date());
		allegatoFamiliariConviventi.setFirmaDigitale(false);

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/antimafia/35/allegatoFamiliariConviventi");
		builder.with(request -> {
			request.setMethod(HttpMethod.PUT.name());
			return request;
		});

		MockMultipartFile documento = new MockMultipartFile("documento", Files.readAllBytes(filePdfPath));
		MockMultipartFile allegatoFamiliariConviventiJsonPart = new MockMultipartFile("datiFamiliareConvivente", "datiFamiliareConvivente", "application/json",
				objectMapper.writeValueAsString(allegatoFamiliariConviventi).getBytes(StandardCharsets.UTF_8));

		this.mockMvc.perform(builder.file(allegatoFamiliariConviventiJsonPart).file(documento)).andExpect(status().isForbidden());
	}
	
	@Test
	public void aggiungiNotaDichiarazioneNonAutenticato403() throws Exception {
		Nota nota = new Nota();
		nota.setDataInserimento(new java.util.Date());
		nota.setNota("testo nota esempio");
		nota.setTipoNota(TipoNotaEnum.RIFIUTO_DICHIARAZIONE_ANTIMAFIA);
		nota.setUtente("user01");
		this.mockMvc.perform(put("/api/v1/antimafia/32/note").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(nota))).andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ANTIMAFIA_TUTTI_COD + "altro" })
	public void aggiungiNotaDichiarazioneNonAutorizzato403() throws Exception {
		Nota nota = new Nota();
		nota.setDataInserimento(new java.util.Date());
		nota.setNota("testo nota esempio");
		nota.setTipoNota(TipoNotaEnum.RIFIUTO_DICHIARAZIONE_ANTIMAFIA);
		nota.setUtente("user01");
		this.mockMvc.perform(put("/api/v1/antimafia/32/note").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(nota))).andExpect(status().isForbidden());
	}	
	
	@Test
	@WithMockUser(username = "utente", roles = { Ruoli.EDITA_ANTIMAFIA_TUTTI_COD + "altro" })
	public void protocollaNonAutorizzato403() throws Exception {
		this.mockMvc.perform(put("/api/v1/antimafia/37/protocolla")).andExpect(status().isForbidden());
	}	
	
	@Test
	@WithMockUser(username = "utente")
	public void getAllegatoFamiliariConviventiNonAutorizzato403() throws Exception {
		this.mockMvc.perform(get("/api/v1/antimafia/37/allegatoFamiliariConviventi/13")).andExpect(status().isForbidden());
	}
	
}
