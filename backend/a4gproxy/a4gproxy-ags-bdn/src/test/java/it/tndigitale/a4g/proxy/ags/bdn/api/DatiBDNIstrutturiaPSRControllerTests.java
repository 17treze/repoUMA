package it.tndigitale.a4g.proxy.ags.bdn.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ws.test.client.MockWebServiceServer;

import it.tndigitale.a4g.proxy.ags.bdn.business.persistence.repository.DomandaPsrDao;
import it.tndigitale.a4g.proxy.ags.bdn.business.persistence.repository.ZootecniaDao;
import it.tndigitale.a4g.proxy.ags.bdn.business.service.AnagraficaAllevamentoClient;
import it.tndigitale.a4g.proxy.ags.bdn.business.service.CapiAllevamentoClient;



@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class DatiBDNIstrutturiaPSRControllerTests {

	private MockWebServiceServer mockServerAnagraficaAllevamento;
	private MockWebServiceServer mockServerCapiAllevamento;
	
	@Autowired
	private AnagraficaAllevamentoClient anagraficaAllevamentoClient;
	@Autowired
	private CapiAllevamentoClient capiAllevamentoClient;
	@MockBean
	private DomandaPsrDao domandaPsrDao;
	@MockBean
	private ZootecniaDao zootecniaDao;
	@Before
	public void init() {
		mockServerAnagraficaAllevamento = MockWebServiceServer.createServer(anagraficaAllevamentoClient);
		mockServerCapiAllevamento = MockWebServiceServer.createServer(capiAllevamentoClient);
	}
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void avviaSincronizzazioneSingolaSuccess() throws Exception {
		Source anagraficaAllevamentoRequest = new StreamSource(new FileReader("src/test/resources/bdn/request-02168330229.xml"));
		Source anagraficaAllevamentoResponse = new StreamSource(new FileReader("src/test/resources/bdn/response-02168330229.xml"));

		Source capiAllevamentoRequest = new StreamSource(new FileReader("src/test/resources/bdn/request-02168330229-3828011.xml"));
		Source capiAllevamentoResponse = new StreamSource(new FileReader("src/test/resources/bdn/response-02168330229-3828011.xml"));
		
		// secondo allevamento 
		Source capiAllevamentoRequest3862736 = new StreamSource(new FileReader("src/test/resources/bdn/request-02168330229-3862736.xml"));
		Source capiAllevamentoResponse3862736 = new StreamSource(new FileReader("src/test/resources/bdn/response-02168330229-3862736.xml"));

		List<String> asList = Arrays.asList("02168330229");
		Mockito.when(domandaPsrDao.getDomandePresentatoPsrInCampagna(Mockito.anyInt())).thenReturn(asList);
		mockServerAnagraficaAllevamento.expect(payload(anagraficaAllevamentoRequest)).andRespond(withPayload(anagraficaAllevamentoResponse));
		mockServerCapiAllevamento.expect(payload(capiAllevamentoRequest)).andRespond(withPayload(capiAllevamentoResponse));
		mockServerCapiAllevamento.expect(payload(capiAllevamentoRequest3862736)).andRespond(withPayload(capiAllevamentoResponse3862736));

		String result = this.mockMvc.perform(get("/sincronizza/02168330229").param("dataRiferimento", "2020-09-15"))
		.andExpect(status().is2xxSuccessful())
		.andReturn().getResponse().getContentAsString();
		
		assertTrue(Boolean.valueOf(result));
		mockServerAnagraficaAllevamento.verify();
		mockServerCapiAllevamento.verify();
	}
}
