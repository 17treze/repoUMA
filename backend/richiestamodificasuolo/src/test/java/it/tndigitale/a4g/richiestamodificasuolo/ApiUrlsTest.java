package it.tndigitale.a4g.richiestamodificasuolo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import it.tndigitale.a4g.richiestamodificasuolo.api.ApiUrls;

@SpringBootTest
public class ApiUrlsTest {

	@Test
	void testApi() {

		String api_v1 = "/api/v1";
		String richiestaModificaSuolo = api_v1 + "/richiestamodificasuolo";
		String lavorazioneSuolo = api_v1 + "/lavorazionesuolo";
		String suoloDichiarato = api_v1 + "/suolodichiarato";
		String applicationProxy = api_v1 + "/proxy";
		String layer = api_v1 + "/layer";

		assertEquals(ApiUrls.API_V1, api_v1);
		assertEquals(ApiUrls.RICHIESTA_MODIFICA_SUOLO, richiestaModificaSuolo);
		assertEquals(ApiUrls.LAVORAZIONE_SUOLO, lavorazioneSuolo);
		assertEquals(ApiUrls.SUOLO_DICHIARATO, suoloDichiarato);
		assertEquals(ApiUrls.APPLICATION_PROXY, applicationProxy);
		assertEquals(ApiUrls.LAYER, layer);

	}
}
