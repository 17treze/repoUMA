package it.tndigitale.a4g.fascicolo.mediator.stampa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.FascicoloDto;
import it.tndigitale.a4g.fascicolo.mediator.api.ApiUrls;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.AnagraficaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.DotazioneTecnicaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.ProxyClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.TerritorioPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.UtentePrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.client.ZootecniaPrivateClient;
import it.tndigitale.a4g.fascicolo.mediator.business.service.fascicolo.ControlliCompletezzaFascicoloService;
import it.tndigitale.a4g.fascicolo.mediator.business.service.utente.AbilitazioniComponent;
import it.tndigitale.a4g.fascicolo.mediator.dto.EsitoControlloDto;
import it.tndigitale.a4g.fascicolo.territorio.client.model.ReportValidazioneTerreniAgsDto;
import it.tndigitale.a4g.fascicolo.zootecnia.client.model.ReportValidazioneAllevamentoDto;
import it.tndigitale.a4g.fascicolo.zootecnia.client.model.ReportValidazioneDto;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class SchedaValidazioneFascicoloControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AnagraficaPrivateClient anagraficaPrivateClient;
	@MockBean
	private ZootecniaPrivateClient zootecniaPrivateClient;
	@MockBean
	private TerritorioPrivateClient territorioPrivateClient;
	@MockBean
	private DotazioneTecnicaPrivateClient dotazioneTecnicaPrivateClient;
	@MockBean
	private ProxyClient proxyClient;
	@MockBean
	private UtentePrivateClient utentePrivateClient;
	@MockBean
	private ControlliCompletezzaFascicoloService controlliCompletezzaFascicoloService;

	@MockBean AbilitazioniComponent abilitazioniComponent;

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	void stampaSchedaDiValidazioneSuccessful() throws Exception {
		String cuaa = "CRLMRC71B01H330G";
		ReportValidazioneAllevamentoDto mockReportValidazioneAllevamento = new ReportValidazioneAllevamentoDto();

		mockReportValidazioneAllevamento.setCodiceFiscaleDetentore(null);
		mockReportValidazioneAllevamento.setComuneStruttura(null);
		mockReportValidazioneAllevamento.setIdentificativoStruttura(null);
		mockReportValidazioneAllevamento.setIndirizzoStruttura(null);
		mockReportValidazioneAllevamento.setSpecie(null);
		var esitoControlloDtoOk = new EsitoControlloDto();
		esitoControlloDtoOk.setEsito(0);
		Mockito.when(abilitazioniComponent.checkLetturaFascicolo(Mockito.anyString())).thenReturn(true);
		Mockito.when(controlliCompletezzaFascicoloService.queryControlloCompletezzaFascicolo(Mockito.anyString())).thenReturn(Map.ofEntries(Map.entry("String", esitoControlloDtoOk)));
		Mockito.when(anagraficaPrivateClient.getReportValidazione(Mockito.anyString())).thenReturn(new it.tndigitale.a4g.fascicolo.anagrafica.client.model.ReportValidazioneDto());
		Mockito.when(zootecniaPrivateClient.getReportValidazione(Mockito.anyString())).thenReturn(new ReportValidazioneDto().setReportValidazioneAllevamenti(Arrays.asList(mockReportValidazioneAllevamento)));
		Mockito.when(proxyClient.stampaPdfA(Mockito.anyString(), Mockito.anyString())).thenReturn("test".getBytes());
		Mockito.doNothing().when(anagraficaPrivateClient).aggiornaStatoFascicoloAllaFirmaCaa(Mockito.anyString(), Mockito.anyLong(), Mockito.any());
		Mockito.when(territorioPrivateClient.getReportValidazione(Mockito.anyString())).thenReturn(new ReportValidazioneTerreniAgsDto());
		Mockito.when(dotazioneTecnicaPrivateClient.getReportValidazione(Mockito.anyString())).thenReturn(new it.tndigitale.a4g.fascicolo.dotazionetecnica.client.model.ReportValidazioneDto());
		FascicoloDto fascicoloDto = new FascicoloDto();
		fascicoloDto.setStato(FascicoloDto.StatoEnum.CONTROLLATO_OK);
		Mockito.when(anagraficaPrivateClient.getByCuaaUsingGET1(cuaa, 0)).thenReturn(fascicoloDto);

		MvcResult mvcResult = mockMvc.perform(get(String.format(ApiUrls.FASCICOLO + "/%s/report-scheda-validazione", cuaa))
				.contentType(MediaType.APPLICATION_OCTET_STREAM))
				.andExpect(status().isOk())
				.andReturn();

		assertEquals(MediaType.APPLICATION_PDF_VALUE, mvcResult.getResponse().getContentType());
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	void stampaSchedaDiValidazioneException() throws Exception {
		String cuaa = "CRLMRC71B01H330G";
		ReportValidazioneAllevamentoDto mockReportValidazioneAllevamento = new ReportValidazioneAllevamentoDto();

		mockReportValidazioneAllevamento.setCodiceFiscaleDetentore(null);
		mockReportValidazioneAllevamento.setComuneStruttura(null);
		mockReportValidazioneAllevamento.setIdentificativoStruttura(null);
		mockReportValidazioneAllevamento.setIndirizzoStruttura(null);
		mockReportValidazioneAllevamento.setSpecie(null);
		var esitoControlloDtoOk = new EsitoControlloDto();
		esitoControlloDtoOk.setEsito(0);

		Mockito.when(abilitazioniComponent.checkLetturaFascicolo(Mockito.anyString())).thenReturn(true);
		Mockito.when(controlliCompletezzaFascicoloService.queryControlloCompletezzaFascicolo(Mockito.anyString())).thenReturn(Map.ofEntries(Map.entry("String", esitoControlloDtoOk)));
		Mockito.when(anagraficaPrivateClient.getReportValidazione(Mockito.anyString())).thenReturn(new it.tndigitale.a4g.fascicolo.anagrafica.client.model.ReportValidazioneDto());
		Mockito.when(zootecniaPrivateClient.getReportValidazione(Mockito.anyString())).thenReturn(new ReportValidazioneDto().setReportValidazioneAllevamenti(Arrays.asList(mockReportValidazioneAllevamento)));
		Mockito.when(proxyClient.stampaPdfA(Mockito.anyString(), Mockito.anyString())).thenThrow(IOException.class);
		Mockito.doNothing().when(anagraficaPrivateClient).aggiornaStatoFascicoloAllaFirmaCaa(Mockito.anyString(), Mockito.anyLong(), Mockito.any());
		Mockito.when(territorioPrivateClient.getReportValidazione(Mockito.anyString())).thenReturn(new ReportValidazioneTerreniAgsDto());
		FascicoloDto fascicoloDto = new FascicoloDto();
		fascicoloDto.setStato(FascicoloDto.StatoEnum.CONTROLLATO_OK);
		Mockito.when(anagraficaPrivateClient.getByCuaaUsingGET1(cuaa, 0)).thenReturn(fascicoloDto);
		Mockito.when(utentePrivateClient.getReportValidazione(Mockito.anyString())).thenReturn(new it.tndigitale.a4g.utente.client.model.ReportValidazioneDto());
		Mockito.when(dotazioneTecnicaPrivateClient.getReportValidazione(Mockito.anyString())).thenReturn(new it.tndigitale.a4g.fascicolo.dotazionetecnica.client.model.ReportValidazioneDto());

		assertThrows(IOException.class, () -> {
			mockMvc.perform(get(String.format(ApiUrls.FASCICOLO + "/%s/report-scheda-validazione", cuaa)).contentType(MediaType.APPLICATION_OCTET_STREAM));
		});
	}

	@Test
	@WithMockUser(username = "utente", roles = {"a4ganagrafica.fascicolo.apertura.ente"})
	void get_report_scheda_validazione_bozza() throws Exception {
		ReportValidazioneAllevamentoDto mockReportValidazioneAllevamento = new ReportValidazioneAllevamentoDto();

		mockReportValidazioneAllevamento.setCodiceFiscaleDetentore(null);
		mockReportValidazioneAllevamento.setComuneStruttura(null);
		mockReportValidazioneAllevamento.setIdentificativoStruttura(null);
		mockReportValidazioneAllevamento.setIndirizzoStruttura(null);
		mockReportValidazioneAllevamento.setSpecie(null);

		Mockito.when(abilitazioniComponent.checkLetturaFascicolo(Mockito.anyString())).thenReturn(true);
		Mockito.when(anagraficaPrivateClient.getReportValidazione(Mockito.anyString())).thenReturn(new it.tndigitale.a4g.fascicolo.anagrafica.client.model.ReportValidazioneDto());
		Mockito.when(zootecniaPrivateClient.getReportValidazione(Mockito.anyString())).thenReturn(new ReportValidazioneDto().setReportValidazioneAllevamenti(Arrays.asList(mockReportValidazioneAllevamento)));
		Mockito.when(proxyClient.stampaPdfA(Mockito.anyString(), Mockito.anyString())).thenReturn("test".getBytes());
		Mockito.when(territorioPrivateClient.getReportValidazione(Mockito.anyString())).thenReturn(new ReportValidazioneTerreniAgsDto());
		Mockito.when(dotazioneTecnicaPrivateClient.getReportValidazione(Mockito.anyString())).thenReturn(new it.tndigitale.a4g.fascicolo.dotazionetecnica.client.model.ReportValidazioneDto());

		String cuaa = "CRLMRC71B01H330G";
		MvcResult mvcResult = mockMvc.perform(get(String.format(ApiUrls.FASCICOLO + "/%s/report-scheda-validazione-bozza", cuaa))
				.contentType(MediaType.APPLICATION_OCTET_STREAM))
				.andExpect(status().isOk())
				.andReturn();

		assertEquals(MediaType.APPLICATION_PDF_VALUE, mvcResult.getResponse().getContentType());
	}

}
