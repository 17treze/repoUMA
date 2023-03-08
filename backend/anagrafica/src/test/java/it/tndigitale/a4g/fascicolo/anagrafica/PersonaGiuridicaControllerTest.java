package it.tndigitale.a4g.fascicolo.anagrafica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FonteDatoAnagrafico;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.ImportanzaAttivita;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.AttivitaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.IscrizioneRepertorioEconomicoDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaGiuridicaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.RappresentanteLegaleDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.SedeDto;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class PersonaGiuridicaControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@Sql(scripts = "/sql/persone/personaGiuridicaControllerTest_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/persone/personaGiuridicaControllerTest_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getPersonaGiuridica() throws Exception {
		String codiceFiscale = "00959460221";
		String stringResponse = mockMvc.perform(get("/api/v1/personagiuridica/".concat(codiceFiscale)))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();
		PersonaGiuridicaDto response = objectMapper.readValue(stringResponse, PersonaGiuridicaDto.class);
		
		assertEquals("00959460221", response.getCodiceFiscale());
		assertEquals("SOCIETA' PER AZIONI", response.getFormaGiuridica());
		assertEquals("SEVEN S.P.A.", response.getDenominazione());
		assertEquals("00959460221", response.getPartitaIva());
		assertEquals("OGGETTO SOCIALE", response.getOggettoSociale());
		assertEquals("1982-09-20", response.getDataCostituzione().toString());
		assertEquals("2025-12-31", response.getDataTermine().toString());
		assertEquals(1500000L, response.getCapitaleSocialeDeliberato());
		
		RappresentanteLegaleDto rlDto = response.getRappresentanteLegale();
		assertEquals("PLOPLA44H15L821Q", rlDto.getCodiceFiscale());
		assertEquals("POLI PAOLO", rlDto.getNominativo());
		
		SedeDto sede = response.getSedeLegale();
		assertEquals("TN", sede.getIndirizzo().getProvincia());
		assertEquals("TRENTO", sede.getIndirizzo().getComune());
		assertEquals("VIA ALTO ADIGE 242", sede.getIndirizzo().getToponimo());
		assertEquals("38121", sede.getIndirizzo().getCap());
		
		assertEquals("TRENTO", sede.getIndirizzoCameraCommercio().getProvincia());
		assertEquals("TRENTO", sede.getIndirizzoCameraCommercio().getComune());
		assertEquals("VIA", sede.getIndirizzoCameraCommercio().getToponimo());
		assertEquals("38121", sede.getIndirizzoCameraCommercio().getCap());
		assertEquals("242", sede.getIndirizzoCameraCommercio().getCivico());
		assertEquals("GARDOLO", sede.getIndirizzoCameraCommercio().getFrazione());
		assertEquals("ALTO ADIGE", sede.getIndirizzoCameraCommercio().getVia());
		assertEquals("022205", sede.getIndirizzoCameraCommercio().getCodiceIstat());
		
		IscrizioneRepertorioEconomicoDto ireDto = sede.getIscrizioneRegistroImprese();
		assertEquals("1982-12-17", ireDto.getDataIscrizione().toString());
		assertEquals(106862L, ireDto.getCodiceRea());
		assertEquals("TN", ireDto.getProvinciaRea());
		assertEquals(false, ireDto.isCessata());
		
		List<AttivitaDto> attivitaAteco = sede.getAttivitaAteco();

		assertEquals("46341", attivitaAteco.get(1).getCodice());
		assertEquals("Commercio all'ingrosso di bevande alcoliche", attivitaAteco.get(1).getDescrizione());
		assertEquals(ImportanzaAttivita.PRIMARIO_IMPRESA, attivitaAteco.get(1).getImportanza());
		assertEquals(FonteDatoAnagrafico.CAMERA_COMMERCIO, attivitaAteco.get(1).getFonteDato());
		
		assertEquals("11021", attivitaAteco.get(0).getCodice());
		assertEquals("Produzione di vini da tavola e v.q.p.r.d.", attivitaAteco.get(0).getDescrizione());
		assertEquals(ImportanzaAttivita.SECONDARIO, attivitaAteco.get(0).getImportanza());
		assertEquals(FonteDatoAnagrafico.CAMERA_COMMERCIO, attivitaAteco.get(0).getFonteDato());
		
		assertEquals("046197654", sede.getTelefono());
		assertEquals("AMMINISTRAZIONE@PEC.GRUPPOPOLI.IT", sede.getIndirizzoPec());
	}
}
