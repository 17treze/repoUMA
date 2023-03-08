package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.RichiestaRevocaImmediataDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.caa.SportelloCAADto;
import it.tndigitale.a4g.utente.client.model.Persona;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class MailNotificaTest {
	
	@MockBean
	public JavaMailSender emailSender;
	
	@MockBean
	AnagraficaUtenteClient anagraficaUtenteClient;
	
	@Autowired
	MandatoService mandatoService;
	
	@Autowired
	MockMvc mockMvc;

    private void mockRappresentanteLegale(String codiceFiscale, String nome, String cognome) {
		List<Persona> persone = new ArrayList<>();
		Persona p = new Persona();
		p.setCodiceFiscale(codiceFiscale);
		p.setNome(nome);
		p.setCognome(cognome);
		persone.add(p);
		
		Mockito.when(anagraficaUtenteClient.ricercaPerCodiceFiscale(Mockito.anyString())).thenReturn(persone);
	}
    
	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
			})
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_giuridica_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void daPersonaGiuridicaNotificaCaaRichiestaValidazioneAccettata() throws Exception {
		String cuaa = "10959460221";
		String utenteAzienda = "YPDNDR77B03L378X";
		mockRappresentanteLegale(utenteAzienda, "Mario", "Rossi");
		
		// invio mail CAA
		mandatoService.notificaMailCaaRichiestaValidazioneAccettata(cuaa);
		verify(emailSender, times(1)).send(Mockito.any(SimpleMailMessage.class));
	}
	
	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
			})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void daPersonaFisicaNotificaCaaRichiestaValidazioneAccettata() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		
		mockRappresentanteLegale(cuaa, "ANDREA", "DEPEDRI");
		// invio mail CAA
		mandatoService.notificaMailCaaRichiestaValidazioneAccettata(cuaa);
		
		verify(emailSender, times(1)).send(Mockito.any(SimpleMailMessage.class));
	}
	
	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
			})
	@Sql(scripts = "/sql/fascicolo/fascicolo_persona_giuridica_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void daPersonaGiuridicaNotificaAppagRichiestaValidazioneAccettata() throws Exception {
		String cuaa = "10959460221";
		String utenteAzienda = "YPDNDR77B03L378X";
		mockRappresentanteLegale(utenteAzienda, "Mario", "Rossi");
		Path path = Paths.get("src/test/resources/fascicolo/MANDATO_ftoDPDNDR77B03L378L.pdf");
		MockMultipartFile moduloRevocaFirmato = new MockMultipartFile("moduloRevocaFirmato.pdf", Files.readAllBytes(path));
		
		SportelloCAADto sportelloCAADto = new SportelloCAADto();
		sportelloCAADto.setIdentificativo(28L);
		sportelloCAADto.setDenominazione("Sportello futuro");

		RichiestaRevocaImmediataDto richiestaRevocaImmediataDto = new RichiestaRevocaImmediataDto()
				.setCodiceFiscale(cuaa)
				.setCodiceFiscaleRappresentante(cuaa)
				.setRichiestaRevocaImmediataFirmata(new ByteArrayResource(moduloRevocaFirmato.getBytes(), moduloRevocaFirmato.getOriginalFilename()))
				.setCausaRichiesta("")
				.setIdentificativoSportello(28L)
				.setMotivazioneRifiuto("Rifiuto")
				.setDataValutazione(LocalDate.of(2020, Month.NOVEMBER, 1))
				.setSportello(sportelloCAADto);
		
		// invio mail CAA
		mandatoService.notificaMailAppag(richiestaRevocaImmediataDto, true);
		verify(emailSender, times(1)).send(Mockito.any(SimpleMailMessage.class));
	}
	
	@Test
	@WithMockUser(username = "utente", roles = {
			"a4gfascicolo.fascicolo.ricerca.ente",
			"a4ganagrafica.fascicolo.apertura.ente",
			"a4gfascicolo.fascicolo.ricerca.tutti"
			})
	@Sql(scripts = "/sql/fascicolo/fascicolo_insert.sql" , executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/sql/fascicolo/fascicolo_delete.sql" , executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	void daPersonaFisicaNotificaAppagRichiestaValidazioneAccettata() throws Exception {
		String cuaa = "XPDNDR77B03L378X";
		
		mockRappresentanteLegale(cuaa, "ANDREA", "DEPEDRI");
		Path path = Paths.get("src/test/resources/fascicolo/MANDATO_ftoDPDNDR77B03L378L.pdf");
		MockMultipartFile moduloRevocaFirmato = new MockMultipartFile("moduloRevocaFirmato.pdf", Files.readAllBytes(path));
		
		SportelloCAADto sportelloCAADto = new SportelloCAADto();
		sportelloCAADto.setIdentificativo(13L);
		sportelloCAADto.setDenominazione("Sportello futuro");

		RichiestaRevocaImmediataDto richiestaRevocaImmediataDto = new RichiestaRevocaImmediataDto()
				.setCodiceFiscale(cuaa)
				.setCodiceFiscaleRappresentante(cuaa)
				.setRichiestaRevocaImmediataFirmata(new ByteArrayResource(moduloRevocaFirmato.getBytes(), moduloRevocaFirmato.getOriginalFilename()))
				.setCausaRichiesta("")
				.setIdentificativoSportello(13L)
				.setMotivazioneRifiuto("Rifiuto")
				.setDataValutazione(LocalDate.of(2020, Month.NOVEMBER, 1))
				.setSportello(sportelloCAADto);
		
		// invio mail CAA
		mandatoService.notificaMailAppag(richiestaRevocaImmediataDto, true);
		
		verify(emailSender, times(1)).send(Mockito.any(SimpleMailMessage.class));
	}

	
}
