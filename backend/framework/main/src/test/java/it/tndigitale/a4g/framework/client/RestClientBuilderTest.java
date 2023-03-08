package it.tndigitale.a4g.framework.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import it.tndigitale.a4g.framework.client.RestClientBuilder.BuilderClientService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RestClientBuilderTest {

	@Autowired
	private RestClientBuilder builder;

	@MockBean
	private RestTemplate restTemplate;

	private final static String MOCK_URL = "MOCK_URL";

		@Test
		public void DATO_UN_CLIENT_E_URL_VERIFICO_ISTANZA_CREATA() {
				BuilderClientService<MockClientApiRest> builderClient =
								builder.from(MockClientApiRest.class).setBasePath(MOCK_URL);

				assertNotNull(builderClient);
				assertEquals(MOCK_URL, builderClient.basePath);

				MockClientApiRest client = builderClient.newInstance();
				assertNotNull(client);
				assertEquals(MockClientApiRest.class, client.getClass());
		}

		/**
		 * Classe mock per testare il builder
		 * 
		 * @author Lorenzo Martinelli
		 *
		 */
		static class MockClientApiRest implements RestApiClient<MockClientApiRest> {
				@Override
				public MockClientApiRest set(RestTemplate restTemplate, String basePath) {
						return this;
				}

				@Override
				public MockClientApiRest setRestTemplate(RestTemplate restTemplate) {
						return this;
				}

				@Override
				public MockClientApiRest setBasePath(String basePath) {
						return this;
				}
		}
}
