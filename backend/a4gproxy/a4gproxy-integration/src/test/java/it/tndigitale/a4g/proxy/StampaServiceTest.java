package it.tndigitale.a4g.proxy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import it.tndigitale.a4g.proxy.services.StampaService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class StampaServiceTest {
	
	@Autowired
	private StampaService esecutore;

	//@Test
	public void integrationTestXML( ) throws Exception {
		String xmlData = "";
		try(FileInputStream inputStream = new FileInputStream("src/test/resources/stampa/xml/domandaNuovoUtente.xml")) {     
			xmlData = IOUtils.toString(inputStream);
		    // do something with everything string
		}
		File template = ResourceUtils.getFile(this.getClass().getResource("/stampa/template/templateRichiestaDomandaNuovoUtente.docx"));
		byte[] result = esecutore.stampaXML2PDF(
				xmlData,
				new FileInputStream(template));
		assertNotNull(result);
		assertTrue(true);
	}

	//@Test
	public void integrationTestJson( ) throws Exception {
		String data = "";
		try(FileInputStream inputStream = new FileInputStream("src/test/resources/stampa/json/domandaNuovoUtenteElisabettaFreschi.json")) {     
			data = IOUtils.toString(inputStream);
		    // do something with everything string
		}
		File template = ResourceUtils.getFile(this.getClass().getResource("/stampa/template/templateJSonRichiestaDomandaNuovoUtente.docx"));
		byte[] result = esecutore.stampaJSON2PDF(
				data,
				new FileInputStream(template));
		assertNotNull(result);
		assertTrue(true);
	}

	//@Test
	public void integrationTestJsonPDFA( ) throws Exception {
		String data = "";
		try(FileInputStream inputStream = new FileInputStream("src/test/resources/stampa/json/domandaNuovoUtenteElisabettaFreschi.json")) {     
			data = IOUtils.toString(inputStream);
		    // do something with everything string
		}
		File template = ResourceUtils.getFile(this.getClass().getResource("/stampa/template/templateJSonRichiestaDomandaNuovoUtentePDFA.docx"));
		byte[] result = esecutore.stampaJSON2PDFA(
				data,
				new FileInputStream(template));
		assertNotNull(result);
		assertTrue(true);
	}
}