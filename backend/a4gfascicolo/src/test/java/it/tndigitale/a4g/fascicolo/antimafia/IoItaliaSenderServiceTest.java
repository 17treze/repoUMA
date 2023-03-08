package it.tndigitale.a4g.fascicolo.antimafia;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.CaricaAgsDto;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Azienda;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Dichiarazione;
import it.tndigitale.a4g.fascicolo.antimafia.dto.PageResultWrapper;
import it.tndigitale.a4g.fascicolo.antimafia.ioitalia.IoItaliaConsumerApi;
import it.tndigitale.a4g.fascicolo.antimafia.ioitalia.IoItaliaSenderService;
import it.tndigitale.a4g.fascicolo.antimafia.service.AntimafiaServiceImpl;
import it.tndigitale.a4g.fascicolo.antimafia.service.ext.ConsumeExternalRestApi4Anagrafica;
import it.tndigitale.a4g.proxy.client.model.ComunicationDto;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class IoItaliaSenderServiceTest {
	
	@MockBean
	private AntimafiaServiceImpl serviceAntimafia;
	
	@MockBean
	private ConsumeExternalRestApi4Anagrafica apiAnagrafica;
	
	@MockBean
	private IoItaliaConsumerApi ioItaliaConsumerApi;
	
	@Autowired
	private IoItaliaSenderService ioItSenderService;

	@Test
	public void testNotifica30GiorniScadenza() throws Exception {
		PageResultWrapper<Dichiarazione> dichPageWrapp = new PageResultWrapper<Dichiarazione>();
		List<Dichiarazione> dichList = new ArrayList<Dichiarazione>();
		Dichiarazione dich = new Dichiarazione();
		Azienda azienda = new Azienda();
		azienda.setCuaa("MSTFBA79L10H612L");
		LocalDate dtScad30gg = LocalDate.now().minusDays(150);
		dich.setAzienda(azienda);
		dich.setDenominazioneImpresa("DENOMINAZIONE IMPRESA");
		dich.setDtProtocollazione(Date.from(dtScad30gg.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		dichList.add(dich);
		dichPageWrapp.setResults(dichList);
		Mockito.when(serviceAntimafia.getDichiarazioniPaginata(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(dichPageWrapp);
		
		List<CaricaAgsDto> persList = new ArrayList<CaricaAgsDto>();
		CaricaAgsDto pers = new CaricaAgsDto();
		pers.setCodiceFiscale("MSTFBA79L10H612L");
		persList.add(pers);
		
		Mockito.when(apiAnagrafica.getSoggettiFascicoloAziendale(Mockito.any())).thenReturn(persList);
		
		Mockito.when(ioItaliaConsumerApi.inviaMessaggio(Mockito.any())).thenReturn(null);
		
		ioItSenderService.jobReminderScadenzaDichiarazioniAntimafia();
		
		verify(ioItaliaConsumerApi, times(1)).inviaMessaggio(Mockito.any());
		ArgumentCaptor<ComunicationDto> argument = ArgumentCaptor.forClass(ComunicationDto.class);
		verify(ioItaliaConsumerApi).inviaMessaggio(argument.capture());
		assertEquals("Scadenza Certificazione Antimafia", argument.getValue().getOggetto());
	}
	
	@Test
	public void testNotifica20GiorniScadenza() throws Exception {
		PageResultWrapper<Dichiarazione> dichPageWrapp = new PageResultWrapper<Dichiarazione>();
		List<Dichiarazione> dichList = new ArrayList<Dichiarazione>();
		Dichiarazione dich = new Dichiarazione();
		Azienda azienda = new Azienda();
		azienda.setCuaa("MSTFBA79L10H612L");
		LocalDate dtScad30gg = LocalDate.now().minusDays(160);
		dich.setAzienda(azienda);
		dich.setDenominazioneImpresa("DENOMINAZIONE IMPRESA");
		dich.setDtProtocollazione(Date.from(dtScad30gg.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		dichList.add(dich);
		dichPageWrapp.setResults(dichList);
		Mockito.when(serviceAntimafia.getDichiarazioniPaginata(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(dichPageWrapp);
		
		List<CaricaAgsDto> persList = new ArrayList<CaricaAgsDto>();
		CaricaAgsDto pers = new CaricaAgsDto();
		pers.setCodiceFiscale("MSTFBA79L10H612L");
		persList.add(pers);
		
		Mockito.when(apiAnagrafica.getSoggettiFascicoloAziendale(Mockito.any())).thenReturn(persList);
		
		Mockito.when(ioItaliaConsumerApi.inviaMessaggio(Mockito.any())).thenReturn(null);
		
		ioItSenderService.jobReminderScadenzaDichiarazioniAntimafia();
		
		verify(ioItaliaConsumerApi, times(1)).inviaMessaggio(Mockito.any());
		ArgumentCaptor<ComunicationDto> argument = ArgumentCaptor.forClass(ComunicationDto.class);
		verify(ioItaliaConsumerApi).inviaMessaggio(argument.capture());
		assertEquals("Scadenza Certificazione Antimafia", argument.getValue().getOggetto());
	}
	
	@Test
	public void testNotifica10GiorniScadenza() throws Exception {
		PageResultWrapper<Dichiarazione> dichPageWrapp = new PageResultWrapper<Dichiarazione>();
		List<Dichiarazione> dichList = new ArrayList<Dichiarazione>();
		Dichiarazione dich = new Dichiarazione();
		Azienda azienda = new Azienda();
		azienda.setCuaa("MSTFBA79L10H612L");
		LocalDate dtScad30gg = LocalDate.now().minusDays(170);
		dich.setAzienda(azienda);
		dich.setDenominazioneImpresa("DENOMINAZIONE IMPRESA");
		dich.setDtProtocollazione(Date.from(dtScad30gg.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		dichList.add(dich);
		dichPageWrapp.setResults(dichList);
		Mockito.when(serviceAntimafia.getDichiarazioniPaginata(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(dichPageWrapp);
		
		List<CaricaAgsDto> persList = new ArrayList<CaricaAgsDto>();
		CaricaAgsDto pers = new CaricaAgsDto();
		pers.setCodiceFiscale("MSTFBA79L10H612L");
		persList.add(pers);
		
		Mockito.when(apiAnagrafica.getSoggettiFascicoloAziendale(Mockito.any())).thenReturn(persList);
		
		Mockito.when(ioItaliaConsumerApi.inviaMessaggio(Mockito.any())).thenReturn(null);
		
		ioItSenderService.jobReminderScadenzaDichiarazioniAntimafia();
		
		verify(ioItaliaConsumerApi, times(1)).inviaMessaggio(Mockito.any());
		ArgumentCaptor<ComunicationDto> argument = ArgumentCaptor.forClass(ComunicationDto.class);
		verify(ioItaliaConsumerApi).inviaMessaggio(argument.capture());
		assertEquals("Scadenza Certificazione Antimafia", argument.getValue().getOggetto());
	}
	
	@Test
	public void testNotifica5GiorniScadenza() throws Exception {
		PageResultWrapper<Dichiarazione> dichPageWrapp = new PageResultWrapper<Dichiarazione>();
		List<Dichiarazione> dichList = new ArrayList<Dichiarazione>();
		Dichiarazione dich = new Dichiarazione();
		Azienda azienda = new Azienda();
		azienda.setCuaa("MSTFBA79L10H612L");
		LocalDate dtScad30gg = LocalDate.now().minusDays(175);
		dich.setAzienda(azienda);
		dich.setDenominazioneImpresa("DENOMINAZIONE IMPRESA");
		dich.setDtProtocollazione(Date.from(dtScad30gg.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		dichList.add(dich);
		dichPageWrapp.setResults(dichList);
		Mockito.when(serviceAntimafia.getDichiarazioniPaginata(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(dichPageWrapp);
		
		List<CaricaAgsDto> persList = new ArrayList<CaricaAgsDto>();
		CaricaAgsDto pers = new CaricaAgsDto();
		pers.setCodiceFiscale("MSTFBA79L10H612L");
		persList.add(pers);
		
		Mockito.when(apiAnagrafica.getSoggettiFascicoloAziendale(Mockito.any())).thenReturn(persList);
		
		Mockito.when(ioItaliaConsumerApi.inviaMessaggio(Mockito.any())).thenReturn(null);
		
		ioItSenderService.jobReminderScadenzaDichiarazioniAntimafia();
		
		verify(ioItaliaConsumerApi, times(1)).inviaMessaggio(Mockito.any());
		ArgumentCaptor<ComunicationDto> argument = ArgumentCaptor.forClass(ComunicationDto.class);
		verify(ioItaliaConsumerApi).inviaMessaggio(argument.capture());
		assertEquals("Scadenza Certificazione Antimafia", argument.getValue().getOggetto());
	}
	

	@Test
	public void testNotifica1GiornoScadenza() throws Exception {
		PageResultWrapper<Dichiarazione> dichPageWrapp = new PageResultWrapper<Dichiarazione>();
		List<Dichiarazione> dichList = new ArrayList<Dichiarazione>();
		Dichiarazione dich = new Dichiarazione();
		Azienda azienda = new Azienda();
		azienda.setCuaa("MSTFBA79L10H612L");
		LocalDate dtScad30gg = LocalDate.now().minusDays(179);
		dich.setAzienda(azienda);
		dich.setDenominazioneImpresa("DENOMINAZIONE IMPRESA");
		dich.setDtProtocollazione(Date.from(dtScad30gg.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		dichList.add(dich);
		dichPageWrapp.setResults(dichList);
		Mockito.when(serviceAntimafia.getDichiarazioniPaginata(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(dichPageWrapp);
		
		List<CaricaAgsDto> persList = new ArrayList<CaricaAgsDto>();
		CaricaAgsDto pers = new CaricaAgsDto();
		pers.setCodiceFiscale("MSTFBA79L10H612L");
		persList.add(pers);
		
		Mockito.when(apiAnagrafica.getSoggettiFascicoloAziendale(Mockito.any())).thenReturn(persList);
		
		Mockito.when(ioItaliaConsumerApi.inviaMessaggio(Mockito.any())).thenReturn(null);
		
		ioItSenderService.jobReminderScadenzaDichiarazioniAntimafia();
		
		verify(ioItaliaConsumerApi, times(1)).inviaMessaggio(Mockito.any());
		ArgumentCaptor<ComunicationDto> argument = ArgumentCaptor.forClass(ComunicationDto.class);
		verify(ioItaliaConsumerApi).inviaMessaggio(argument.capture());
		assertEquals("Scadenza Certificazione Antimafia", argument.getValue().getOggetto());
	}
	
}
