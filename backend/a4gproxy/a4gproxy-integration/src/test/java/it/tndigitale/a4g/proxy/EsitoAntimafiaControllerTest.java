package it.tndigitale.a4g.proxy;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import it.tndigitale.a4g.framework.time.LocalDateConverter;
import it.tndigitale.a4g.proxy.dto.AntimafiaEsitoDto;
import it.tndigitale.a4g.proxy.repository.esiti.dao.AntimafiaDescrizioneEsitiDao;
import it.tndigitale.a4g.proxy.repository.esiti.dao.AntimafiaEsitiDao;
import it.tndigitale.a4g.proxy.repository.esiti.model.AntimafiaDescrizioneEsitiModel;
import it.tndigitale.a4g.proxy.repository.esiti.model.AntimafiaEsitiId;
import it.tndigitale.a4g.proxy.repository.esiti.model.AntimafiaEsitiModel;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.dao.DichiarazioneAntimafiaDao;
import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabaantiTab;
import it.tndigitale.a4g.proxy.services.EsitoAntimafiaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EsitoAntimafiaControllerTest {
	
	@MockBean
	private AntimafiaEsitiDao daoAntimafiaEsiti;
	
	@MockBean
	private AntimafiaDescrizioneEsitiDao daoAntimafiaDescrizioneEsiti;
	
	@Autowired
	private EsitoAntimafiaService esitoAntimafiaService;
	
	@MockBean
	private DichiarazioneAntimafiaDao dichiarazioneAntimafiaDao;
	
	@Test
	public void getEsitoAntimafiaSuccessful() {
		List<AntimafiaEsitiModel> esiti = new ArrayList<AntimafiaEsitiModel>();
		AntimafiaEsitiModel esito = new AntimafiaEsitiModel();
		LocalDateTime dt = LocalDateTime.now();
		AntimafiaEsitiId compositeId = new AntimafiaEsitiId("PZZNNA40P65H146L", dt);
		esito.setId(compositeId);
		esito.setCodice("20012");
		esito.setDescrizione("AUTODICHIARAZIONE INSERITA IN AGEA");
		esito.setEsito("OK");
		esiti.add(esito);
		
		Mockito.when(daoAntimafiaEsiti.findByIdCuaa(Mockito.any())).thenReturn(esiti);
		
		AntimafiaDescrizioneEsitiModel esitoDescrizione = new AntimafiaDescrizioneEsitiModel();
		esitoDescrizione.setCodice("20012");
		esitoDescrizione.setDescrizione("AUTODICHIARAZIONE INSERITA IN AGEA");
		esitoDescrizione.setEsitoInvioAgea("OK");
		esitoDescrizione.setEsitoInvioBdna("ND");
		
		Mockito.when(daoAntimafiaDescrizioneEsiti.findByCodice(ArgumentMatchers.eq("20012"))).thenReturn(esitoDescrizione);
		
		AabaantiTab dichiarazioneAntimafia = new AabaantiTab();
		dichiarazioneAntimafia.setCuaa("PZZNNA40P65H146L");
		dichiarazioneAntimafia.setDataInizVali(Date.valueOf("2020-06-04"));
		List<AabaantiTab> listDichiarazioni = new ArrayList<>();
		listDichiarazioni.add(dichiarazioneAntimafia);
		
		
		Mockito.when(dichiarazioneAntimafiaDao.findByCuaa(Mockito.any())).thenReturn(listDichiarazioni);
		
		AntimafiaEsitoDto esitoDto = esitoAntimafiaService.getEsitoAntimafia("PZZNNA40P65H146L");
		assertEquals(esitoDescrizione.getDescrizione(), esitoDto.getDescrizioneEsito());
		assertEquals(esito.getEsito(), esitoDto.getEsitoTrasmissione());
		assertEquals(esito.getId().getDtElaborazione(), esitoDto.getDtElaborazione());
		assertEquals(esitoDescrizione.getEsitoInvioAgea(), esitoDto.getEsitoInvioAgea());
		assertEquals(esitoDescrizione.getEsitoInvioBdna(), esitoDto.getEsitoInvioBdna());
	}
	
	@Test
	public void getEsitoAntimafiaSuccessfulEsitoRecente() {
		List<AntimafiaEsitiModel> esiti = new ArrayList<AntimafiaEsitiModel>();
		AntimafiaEsitiModel esito1 = new AntimafiaEsitiModel();
		LocalDateTime dt1 = LocalDateTime.now();
		AntimafiaEsitiId compositeId1 = new AntimafiaEsitiId("PZZNNA40P65H146L", dt1);
		esito1.setId(compositeId1);
		esito1.setCodice("20012");
		esito1.setDescrizione("AUTODICHIARAZIONE INSERITA IN AGEA");
		esito1.setEsito("OK");
		esiti.add(esito1);
		AntimafiaEsitiModel esito2 = new AntimafiaEsitiModel();
		LocalDateTime dt2 = LocalDateTime.of(2019, 11, 5, 0, 0);
		AntimafiaEsitiId compositeId2 = new AntimafiaEsitiId("PZZNNA40P65H146L", dt2);
		esito2.setId(compositeId2);
		esito2.setCodice("20012");
		esito2.setDescrizione("AUTODICHIARAZIONE INSERITA IN AGEA");
		esito2.setEsito("OK");
		esiti.add(esito2);
		
		Mockito.when(daoAntimafiaEsiti.findByIdCuaa(Mockito.any())).thenReturn(esiti);
		
		AntimafiaDescrizioneEsitiModel esitoDescrizione = new AntimafiaDescrizioneEsitiModel();
		esitoDescrizione.setCodice("20012");
		esitoDescrizione.setDescrizione("AUTODICHIARAZIONE INSERITA IN AGEA");
		esitoDescrizione.setEsitoInvioAgea("OK");
		esitoDescrizione.setEsitoInvioBdna("ND");
		
		Mockito.when(daoAntimafiaDescrizioneEsiti.findByCodice(ArgumentMatchers.eq("20012"))).thenReturn(esitoDescrizione);
		
		List<AabaantiTab> listDichiarazioni = new ArrayList<>();
		AabaantiTab dichiarazioneAntimafia1 = new AabaantiTab();
		dichiarazioneAntimafia1.setCuaa("PZZNNA40P65H146L");
		dichiarazioneAntimafia1.setDataInizVali(Date.valueOf("2020-06-04"));
		AabaantiTab dichiarazioneAntimafia2 = new AabaantiTab();
		dichiarazioneAntimafia2.setCuaa("PZZNNA40P65H146L");
		dichiarazioneAntimafia2.setDataInizVali(Date.valueOf("2020-06-01"));
		listDichiarazioni.add(dichiarazioneAntimafia1);
		listDichiarazioni.add(dichiarazioneAntimafia2);
		
		
		Mockito.when(dichiarazioneAntimafiaDao.findByCuaa(Mockito.any())).thenReturn(listDichiarazioni);
		
		AntimafiaEsitoDto esitoDto = esitoAntimafiaService.getEsitoAntimafia("PZZNNA40P65H146L");
		assertEquals(esitoDescrizione.getDescrizione(), esitoDto.getDescrizioneEsito());
		assertEquals(esito1.getEsito(), esitoDto.getEsitoTrasmissione());
		assertEquals(esito1.getId().getDtElaborazione(), esitoDto.getDtElaborazione());
		assertEquals(esitoDescrizione.getEsitoInvioAgea(), esitoDto.getEsitoInvioAgea());
		assertEquals(esitoDescrizione.getEsitoInvioBdna(), esitoDto.getEsitoInvioBdna());
		assertEquals(LocalDateConverter.fromDateTime(dichiarazioneAntimafia1.getDataInizVali()), esitoDto.getDtValidita());
	}
	
	@Test
	public void getEsitoAntimafiaFailAntimafiaEsitiNull() throws Exception {
		Mockito.when(daoAntimafiaEsiti.findByIdCuaa(Mockito.any())).thenReturn(null);
				
		AntimafiaEsitoDto esitoDto = esitoAntimafiaService.getEsitoAntimafia("PZZNNA40P65H146L");
		assertEquals(null, esitoDto);
	}
	
	@Test
	public void getEsitoAntimafiaFailAntimafiaDescEsitiNull() throws Exception {
		List<AntimafiaEsitiModel> esiti = new ArrayList<AntimafiaEsitiModel>();
		AntimafiaEsitiModel esito = new AntimafiaEsitiModel();
		LocalDateTime dt = LocalDateTime.now();
		AntimafiaEsitiId compositeId = new AntimafiaEsitiId("PZZNNA40P65H146L", dt);
		esito.setId(compositeId);
		esito.setCodice("20012");
		esito.setDescrizione("AUTODICHIARAZIONE INSERITA IN AGEA");
		esito.setEsito("OK");
		esiti.add(esito);
		
		Mockito.when(daoAntimafiaEsiti.findByIdCuaa(Mockito.any())).thenReturn(esiti);		
		Mockito.when(daoAntimafiaDescrizioneEsiti.findByCodice(ArgumentMatchers.eq("20012"))).thenReturn(null);
		
		AabaantiTab dichiarazioneAntimafia = new AabaantiTab();
		dichiarazioneAntimafia.setCuaa("PZZNNA40P65H146L");
		dichiarazioneAntimafia.setDataInizVali(Date.valueOf("2020-06-04"));
		List<AabaantiTab> listDichiarazioni = new ArrayList<>();
		listDichiarazioni.add(dichiarazioneAntimafia);
		
		
		Mockito.when(dichiarazioneAntimafiaDao.findByCuaa(Mockito.any())).thenReturn(listDichiarazioni);
		
		AntimafiaEsitoDto esitoDto = esitoAntimafiaService.getEsitoAntimafia("PZZNNA40P65H146L");
		assertEquals(esito.getDescrizione(), esitoDto.getDescrizioneEsito());
		assertEquals(esito.getEsito(), esitoDto.getEsitoTrasmissione());
		assertEquals(esito.getId().getDtElaborazione(), esitoDto.getDtElaborazione());
		assertEquals("ND", esitoDto.getEsitoInvioAgea());
		assertEquals("ND", esitoDto.getEsitoInvioBdna());
	}
}
