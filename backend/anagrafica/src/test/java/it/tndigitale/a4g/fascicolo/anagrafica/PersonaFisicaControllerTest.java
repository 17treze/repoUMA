package it.tndigitale.a4g.fascicolo.anagrafica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FonteDatoAnagrafico;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.ImportanzaAttivita;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.AnagraficaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.AttivitaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.ImpresaIndividualeDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.IndirizzoDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.IscrizioneRepertorioEconomicoDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaFisicaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.SedeDto;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "utente")
class PersonaFisicaControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@Sql(scripts = "/sql/persone/personaControllerTest_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/persone/personaControllerTest_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDatiPersonaFisicaSuccessful() throws Exception {

		String codiceFiscale = "DLPCDD44C16E658A";
		String stringResponse = mockMvc.perform(get("/api/v1/personafisica/".concat(codiceFiscale)))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();
		PersonaFisicaDto response = objectMapper.readValue(stringResponse, PersonaFisicaDto.class);

		// assertions
		assertEquals(codiceFiscale, response.getCodiceFiscale());

		AnagraficaDto anagrafica = response.getAnagrafica();

		assertEquals("DALPONTE", anagrafica.getCognome());
		assertEquals("LOMASO", anagrafica.getComuneNascita());
		assertEquals(LocalDate.of(1944, Month.MARCH, 16), anagrafica.getDataNascita());
		assertEquals("CANDIDO", anagrafica.getNome());
		assertEquals("TN", anagrafica.getProvinciaNascita());
		assertEquals("MASCHIO", anagrafica.getSesso());

		IndirizzoDto domicilioFiscale = response.getDomicilioFiscale();

		assertEquals("80123", domicilioFiscale.getCap());
		assertEquals("ROVERETO", domicilioFiscale.getComune());
		assertEquals("descrizione estesa persona", domicilioFiscale.getToponimo());
		assertEquals(null, domicilioFiscale.getLocalita());
//		assertEquals("TN", domicilioFiscale.getProvincia());


		ImpresaIndividualeDto impresaIndividuale = response.getImpresaIndividuale();

		assertEquals("DALPONTE                 CANDIDO", impresaIndividuale.getDenominazione());
		assertEquals(null, impresaIndividuale.getOggettoSociale());
		assertEquals("00274690221", impresaIndividuale.getPartitaIva());
		assertEquals("IMPRESA INDIVIDUALE", impresaIndividuale.getFormaGiuridica());

		SedeDto sedeLegale = impresaIndividuale.getSedeLegale();

		assertEquals("CANDIDODALPONTE@PEC.IT", sedeLegale.getIndirizzoPec());
		assertEquals("0465701599", sedeLegale.getTelefono());

		IscrizioneRepertorioEconomicoDto iscrizioneRegistroImprese = sedeLegale.getIscrizioneRegistroImprese();

		assertEquals(150387L, iscrizioneRegistroImprese.getCodiceRea());
		assertEquals(LocalDate.of(1996, Month.DECEMBER, 4), iscrizioneRegistroImprese.getDataIscrizione());
		assertEquals("TN", iscrizioneRegistroImprese.getProvinciaRea());

		IndirizzoDto indirizzo = sedeLegale.getIndirizzo();

		assertEquals("38024", indirizzo.getCap());
		assertEquals("COMANO TERME", indirizzo.getComune());
		assertEquals("FRAZIONE VIGO 24", indirizzo.getToponimo());
		assertEquals(null, indirizzo.getLocalita());
//		assertEquals("TN", indirizzo.getProvincia());

		List<AttivitaDto> attivitaAteco = sedeLegale.getAttivitaAteco();
		assertEquals("46341", attivitaAteco.get(0).getCodice());
		assertEquals("Commercio all ingrosso di bevande alcoliche", attivitaAteco.get(0).getDescrizione());
		assertEquals(ImportanzaAttivita.PRIMARIO_IMPRESA, attivitaAteco.get(0).getImportanza());
		assertEquals(FonteDatoAnagrafico.ANAGRAFE_TRIBUTARIA, attivitaAteco.get(0).getFonteDato());
		
		assertEquals("7211", attivitaAteco.get(1).getCodice());
		assertEquals("Ricerca e sviluppo sperimentale nel campo delle biotecnologie", attivitaAteco.get(1).getDescrizione());
		assertEquals(ImportanzaAttivita.SECONDARIO, attivitaAteco.get(1).getImportanza());
		assertEquals(FonteDatoAnagrafico.ANAGRAFE_TRIBUTARIA, attivitaAteco.get(1).getFonteDato());
	}

	@Test
	@Sql(scripts = "/sql/persone/personaControllerTest_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/persone/personaControllerTest_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDatiPersonaFisicaSuccessfulNoDittaIndividuale() throws Exception {

		String codiceFiscale = "DPDNDR77B03L378L";
		String stringResponse = mockMvc.perform(get("/api/v1/personafisica/".concat(codiceFiscale)))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();
		PersonaFisicaDto response = objectMapper.readValue(stringResponse, PersonaFisicaDto.class);

		// assertions
		assertEquals(codiceFiscale, response.getCodiceFiscale());

		AnagraficaDto anagrafica = response.getAnagrafica();

		assertEquals("DEPEDRI", anagrafica.getCognome());
		assertEquals("TRENTO", anagrafica.getComuneNascita());
		assertEquals(LocalDate.of(1977, Month.FEBRUARY, 3), anagrafica.getDataNascita());
		assertEquals("ANDREA", anagrafica.getNome());
		assertEquals("TN", anagrafica.getProvinciaNascita());
		assertEquals("MASCHIO", anagrafica.getSesso());

		IndirizzoDto domicilioFiscale = response.getDomicilioFiscale();

		assertNotNull(domicilioFiscale);
		assertEquals("38068", domicilioFiscale.getCap());
		assertEquals("TRENTO", domicilioFiscale.getComune());
		assertEquals("VIA SANT'AGATA N 12 A", domicilioFiscale.getToponimo());
//		assertEquals("TN", domicilioFiscale.getProvincia());


		ImpresaIndividualeDto impresaIndividuale = response.getImpresaIndividuale();

		assertNull(impresaIndividuale);
	}
	
	@Test
	@Sql(scripts = "/sql/persone/personaControllerTest_insert.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/persone/personaControllerTest_delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	void getDatiPersonaFisicaCameraDiCommercio() throws Exception {
		String codiceFiscale = "FNTDNL86E05L378J";
		String stringResponse = mockMvc.perform(get("/api/v1/personafisica/".concat(codiceFiscale)))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();
		PersonaFisicaDto response = objectMapper.readValue(stringResponse, PersonaFisicaDto.class);

		// assertions
		assertEquals(codiceFiscale, response.getCodiceFiscale());

		ImpresaIndividualeDto impresa = response.getImpresaIndividuale();

		assertEquals("IMPRESA INDIVIDUALE", impresa.getFormaGiuridica());
		
		SedeDto sedeLegale = impresa.getSedeLegale();
		
		IscrizioneRepertorioEconomicoDto ireDto = sedeLegale.getIscrizioneRegistroImprese();
		
		assertEquals(LocalDate.of(2005, 3, 21), ireDto.getDataIscrizione());
		assertEquals(187463L, ireDto.getCodiceRea());
		assertEquals("TN", ireDto.getProvinciaRea());
		assertEquals(false, ireDto.isCessata());
		
		IndirizzoDto indirizzoCC = sedeLegale.getIndirizzoCameraCommercio();
		assertEquals("VIA", indirizzoCC.getToponimo());
		assertEquals("S. AGATA", indirizzoCC.getVia());
		assertEquals("12/A", indirizzoCC.getCivico());
		assertEquals("FAEDO", indirizzoCC.getComune());
		assertEquals("38010", indirizzoCC.getCap());
		assertEquals("022080", indirizzoCC.getCodiceIstat());
		assertEquals("TRENTO", indirizzoCC.getProvincia());
		
		assertEquals("0461669024", sedeLegale.getTelefono());
		assertEquals("daniele.fontana1986@pec.agritel.it", sedeLegale.getIndirizzoPec());
	}
	
	
	@Test
	void getDatiPersonaFisicaCFErrato() throws Exception {
		String codiceFiscale = "01896970223";

		Exception exception = assertThrows(NestedServletException.class, () -> {
			mockMvc.perform(get("/api/v1/personafisica/".concat(codiceFiscale)));
		});
		String msgErr = "Codice Fiscale inserito non valido";

		assertTrue(exception.getMessage().contains(msgErr));
	}
}
