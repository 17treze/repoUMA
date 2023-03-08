package it.tndigitale.a4g.richiestamodificasuolo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import it.tndigitale.a4g.richiestamodificasuolo.api.ApiUrls;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApplicationProxyTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private RestTemplate restTemplate;

	@Value("${it.tndigit.geoserver.protocol}")
	private String geoserverProtocol;

	@Value("${it.tndigit.geoserver.host}")
	private String geoserverHost;

	@Value("${it.tndigit.geoserver.port}")
	private int geoserverPort;

	@Test
	void get403() throws Exception {

		String requestUrl = "/geoserver/app_a4s/wms";
		String queryString = "SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&STYLES&LAYERS=app_a4s%3AA4SV_SUOLO_DICHIARATO_LAYER2&exceptions=application%2Fvnd.ogc.se_inimage&SRS=EPSG%3A25832&WIDTH=331&HEIGHT=768&BBOX=652652.2879386209%2C5084167.431158731%2C677848.2700420071%2C5142805.353144793";

		mockMvc.perform(get(ApiUrls.APPLICATION_PROXY.concat(requestUrl).concat("?").concat(queryString))).andExpect(status().isForbidden());

	}

	@Test
	void post403() throws Exception {

		String requestUrl = "/geoserver/app_a4s/wfs";

		mockMvc.perform(post(ApiUrls.APPLICATION_PROXY.concat(requestUrl))).andExpect(status().isForbidden());

	}

	@Test
	@WithMockUser(username = "utente", roles = {})
	void getWms() throws Exception {

		String requestUrl = "/geoserver/app_a4s/wms";
		String queryString = "SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&STYLES&LAYERS=app_a4s%3AA4SV_SUOLO_DICHIARATO_LAYER2&exceptions=application%2Fvnd.ogc.se_inimage&SRS=EPSG%3A25832&WIDTH=331&HEIGHT=768&BBOX=652652.2879386209%2C5084167.431158731%2C677848.2700420071%2C5142805.353144793";

		URI uri = new URI(geoserverProtocol, null, geoserverHost, geoserverPort, null, null, null);
		uri = UriComponentsBuilder.fromUri(uri).path(requestUrl).query(queryString).build(true).toUri();

		byte[] responseBody = new byte[10];
		ResponseEntity<byte[]> myEntity = new ResponseEntity<byte[]>(responseBody, HttpStatus.OK);

		when(restTemplate.exchange(ArgumentMatchers.any(URI.class), ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any(HttpEntity.class), ArgumentMatchers.eq(byte[].class)))
				.thenReturn(myEntity);

		mockMvc.perform(get(ApiUrls.APPLICATION_PROXY.concat(requestUrl).concat("?").concat(queryString))).andExpect(status().isOk());

	}

}
