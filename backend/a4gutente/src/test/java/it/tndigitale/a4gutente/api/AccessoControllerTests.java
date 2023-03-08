package it.tndigitale.a4gutente.api;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gutente.config.Costanti;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccessoControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Value("${a4gutente.integrazioni.uri}")
	private String a4gproxyUri;
	

	@Test
	public void conHeaderSoloCodiceFiscaleRitornaPopolatoSoloCodiceFiscale() throws Exception {
		String codiceFiscale = "FRSLBT76H42E625Z";
		// setto il codice fiscale nell'header e chiamo
		ResultActions resultActions = mockMvc.perform(get(ApiUrls.UTENTI_V1 + ApiUrls.UTENTE_DATI)
				.header(Costanti.HEADER_CF, codiceFiscale));
		
		// verifico il risultato
		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$.codiceFiscale").value(codiceFiscale))
				.andExpect(jsonPath("$.nome").isEmpty())
				.andExpect(jsonPath("$.email").isEmpty());
	}
	
	@Test
	public void firmaBettyCpsTest() throws Exception {
		mockServizioStampe();
		// setto il codice fiscale nell'header e chiamo
		Map<String, String> headers = new HashMap<String, String>();
		popolaMappaBettyCpsTest(headers);		
		String documentFileName = "informativa_privacy.pdf";
		ResultActions resultActions = chiamaFirma(headers, documentFileName);
		// verifico il risultato
		resultActions.andDo(print()).andExpect(status().isOk());
	}

	protected ResultActions chiamaFirma(Map<String, String> headers, String documentFileName)
			throws FileNotFoundException, IOException, Exception {
		File documento = 
				ResourceUtils.getFile(this.getClass().getResource("/firma/input/" + documentFileName));
		MockMultipartFile multipartFile = 
				new MockMultipartFile("documento", 
									documentFileName, 
									"multipart/form-data", 
									new FileInputStream(documento));
		
		MockHttpServletRequestBuilder reqBuilder = multipart(ApiUrls.UTENTI_V1 + ApiUrls.FIRMA).file(multipartFile);
		for (String key : headers.keySet()) {
			reqBuilder = reqBuilder.header(key, headers.get(key));
		}
		ResultActions resultActions = mockMvc.perform(reqBuilder);
		return resultActions;
	}

	protected void popolaMappaBettyCpsTest(Map<String, String> headers) {
		headers.put("Shib-Identity-Provider", "https://idp-test.infotn.it/icar-lp/metadata");
		headers.put("titolo", "Universit?");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
		headers.put("Shib-Authentication-Method", "urn:oasis:names:tc:SAML:2.0:ac:classes:Smartcard");
		headers.put("nascitaprovincia", "LI");
		headers.put("Shib-AuthnContext-Decl", "");
		headers.put("Accept-Encoding", "gzip, deflate, br");
		headers.put("nascitadata", "02/06/1976");
		headers.put("x509base64", "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");
		headers.put("x509issuerdn", "CN=InfoCert Servizi di Certificazione 2, SERIALNUMBER=07945211006, OU=Ente Certificatore, O=INFOCERT SPA, C=IT");
		headers.put("Shib-Session-Index", "3O8dy0VUlTqztvct3XwPAJ2w");
		headers.put("domicilioprovincia", "");
		headers.put("codicefiscale", "FRSLBT76H42E625Z");
		headers.put("Shib-Application-ID", "a4gcitts");
		headers.put("nascitaluogo", "LIVORNO");
		headers.put("Upgrade-Insecure-Requests", "1");
		headers.put("givenname", "ELISABETTA");
		headers.put("surname", "FRESCHI");
		headers.put("domicilioindirizzo", "");
		headers.put("Shib-Authentication-Instant", "2018-11-26T09:41:32.533Z");
		headers.put("telefono", "");
		headers.put("email", "elisabetta.freschi@infotn.it");
		headers.put("residenzaprovincia", "TN");
		headers.put("Cookie", "_ga=GA1.2.1829539041.1540891430; _gid=GA1.2.1253866303.1543223746; _shibsession_613467646970747368747470733a2f2f6134672d746573742e696e666f746e2e69742f646970656e64656e74652f73686962626f6c657468=_30d4dee15d5e404e5e4b3d87b2f9e3e5; _shibsession_613467636974747368747470733a2f2f6134672d746573742e696e666f746e2e69742f636974746164696e6f2f73686962626f6c657468=_fb4ea71c043d919eac22f26b27aeef29");
		headers.put("domiciliocitta", "");
		headers.put("residenzaindirizzo", "VIA DELLE ARE, 49");
		headers.put("domiciliocap", "");
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		headers.put("pat_attribute_spidcode", "");
		headers.put("cellulare", "");
		headers.put("x509subjectdn", "GIVENNAME=ELISABETTA, SURNAME=FRESCHI, SERIALNUMBER=FRSLBT76H42E625Z, DNQ=2018500471A92, EMAILADDRESS=elisabetta.freschi@infotn.it, CN=\"FRSLBT76H42E625Z/1206100000180914.xgbhEDb+EoYxa4uez7FjTBCpQQc=\", O=INFORMATICA TRENTINA S.P.A., C=IT");
		headers.put("Connection", "keep-alive");
		headers.put("Referer", "https://idp-test.infotn.it/icar-lp/AssertionConsumerServiceProxy");
		headers.put("Host", "a4g-test.infotn.it");
		headers.put("domiciliostato", "");
		headers.put("Shib-Assertion-Count", "");
		headers.put("residenzacap", "38123");
		headers.put("REMOTE_USER", "");
		headers.put("emailpersonale", "elisabetta.freschi@infotn.it");
		headers.put("residenzacitta", "TRENTO");
		headers.put("Cache-Control", "max-age=0");
		headers.put("Shib-AuthnContext-Class", "urn:oasis:names:tc:SAML:2.0:ac:classes:Smartcard");
		headers.put("nascitastato", "IT");
		headers.put("sesso", "F");
		headers.put("residenzastato", "IT");
		headers.put("Accept-Language", "it-IT,it;q=0.9,en-US;q=0.8,en;q=0.7");
		headers.put("Shib-Cookie-Name", "");
		headers.put("Shib-Session-ID", "_fb4ea71c043d919eac22f26b27aeef29");
	}

	@Test
	public void firmaBettyAddTest() throws Exception {
		// setto il codice fiscale nell'header e chiamo
		Map<String, String> headers = new HashMap<String, String>();
		popolaMappaBettyAddTest(headers);		
		String documentFileName = "informativa_privacy.pdf";
		ResultActions resultActions = chiamaFirma(headers, documentFileName);
		// verifico il risultato
		resultActions.andDo(print()).andExpect(status().is5xxServerError()).andExpect(content().string("Per poter firmare è necessario autenticarsi con Carta Provinciale dei Servizi/Carta Nazionale dei Servizi o le credenziali SPID"));
	}

	protected void popolaMappaBettyAddTest(Map<String, String> headers) {
		headers.put("Shib-Identity-Provider", "http://gwfed.test.infotn.it/adfs/services/trust");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
		headers.put("Shib-Authentication-Method", "urn:federation:authentication:windows");
		headers.put("Accept-Encoding", "gzip, deflate, br");
		headers.put("Shib-AuthnContext-Decl", "");
		headers.put("Shib-Session-Index", "");
		headers.put("commonname", "IT417");
		headers.put("codicefiscale", "FRSLBT76H42E625Z");
		headers.put("Shib-Application-ID", "a4gdipts");
		headers.put("Upgrade-Insecure-Requests", "1");
		headers.put("givenname", "ELISABETTA ");
		headers.put("surname", "FRESCHI");
		headers.put("Shib-Authentication-Instant", "2018-11-26T07:59:47.046Z");
		headers.put("department", "");
		headers.put("email", "elisabetta.freschi@infotn.it");
		headers.put("Cookie", "_ga=GA1.2.1829539041.1540891430; _gid=GA1.2.1253866303.1543223746; _shibsession_613467646970747368747470733a2f2f6134672d746573742e696e666f746e2e69742f646970656e64656e74652f73686962626f6c657468=_30d4dee15d5e404e5e4b3d87b2f9e3e5");
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		headers.put("Connection", "keep-alive");
		headers.put("Referer", "https://gwfed.test.infotn.it/adfs/ls/");
		headers.put("Host", "a4g-test.infotn.it");
		headers.put("Shib-Assertion-Count", "");
		headers.put("REMOTE_USER", "");
		headers.put("upn", "IT417@itad.infotn.it");
		headers.put("Cache-Control", "max-age=0");
		headers.put("name", "Freschi Elisabetta");
		headers.put("Shib-AuthnContext-Class", "urn:federation:authentication:windows");
		headers.put("Accept-Language", "it-IT,it;q=0.9,en-US;q=0.8,en;q=0.7");
		headers.put("Shib-Cookie-Name", "");
		headers.put("Shib-Session-ID", "_30d4dee15d5e404e5e4b3d87b2f9e3e5");
	}
	
	private void mockServizioStampe() throws Exception {
		String servizioStampa = a4gproxyUri + "/api/v1/stampa";
		
		byte[] expectedResult = FileCopyUtils.copyToByteArray(new ClassPathResource("/firma/output/mockStampa.pdf").getInputStream());
		ResponseEntity<byte[]> mockResponse = new ResponseEntity<byte[]>(expectedResult, null, HttpStatus.OK);
		
		Mockito.when(restTemplate.exchange(Mockito.eq(new URI(servizioStampa)), Mockito.eq(HttpMethod.POST), any(HttpEntity.class), Mockito.eq(byte[].class))).thenReturn(mockResponse);
		
	}
}
