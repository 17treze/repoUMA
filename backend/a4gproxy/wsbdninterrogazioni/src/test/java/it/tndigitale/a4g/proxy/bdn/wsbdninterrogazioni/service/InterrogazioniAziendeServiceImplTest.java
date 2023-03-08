package it.tndigitale.a4g.proxy.bdn.wsbdninterrogazioni.service;

import it.tndigitale.ws.dsaziendeg.DsAZIENDEG.AZIENDE;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.test.client.MockWebServiceServer;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InterrogazioniAziendeServiceImplTest {

	@Autowired
	private InterrogazioniAziendeServiceImpl service;
	
	private MockWebServiceServer mockServer;

	@Before
	public void init() {
		mockServer = MockWebServiceServer.createServer(service);
	}

	@Test
	public void testAzienda012TN004() throws Exception {
		mockTestAzienda012TN004();
		AZIENDE azienda = service.getAzienda("012TN004");
		assertNotNull(azienda);
		assertEquals("232", azienda.getCOMCODICE());
		assertEquals("012TN004", azienda.getCODICE());
		mockServer.verify();
	}
	
	private void mockTestAzienda012TN004() {
		Source requestPayload = new StreamSource(new File("src/test/resources/getAziendaSTR/richiestaGetAziendaSTR_012TN004.xml"));
		Source responsePayload = new StreamSource(new File("src/test/resources/getAziendaSTR/responseGetAziendaSTR_012TN004.xml"));
		mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
		
	}
}
