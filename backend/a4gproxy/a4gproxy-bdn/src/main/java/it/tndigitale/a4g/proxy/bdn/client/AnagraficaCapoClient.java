package it.tndigitale.a4g.proxy.bdn.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.proxy.ws.bdn.wsAnagraficaCapo.GetCapoEquino;
import it.tndigitale.a4g.proxy.ws.bdn.wsAnagraficaCapo.GetCapoEquinoResponse;
import it.tndigitale.a4g.proxy.ws.bdn.wsAnagraficaCapo.GetCapoEquinoResponse.GetCapoEquinoResult;

@Component
public class AnagraficaCapoClient extends WsdlClient {

	private static final Logger log = LoggerFactory.getLogger(AnagraficaCapoClient.class);

	@Value("${bdn.client.host.url}")
	private String serverUrl;

	@Value("${bdn.client.resource.wsanagraficacapoquery.url}")
	private String resourceUrl;

	@Value("${bdn.client.resource.wsanagraficacapoquery.getCapoEquino.soapaction}")
	private String soapActionGetCapoEquino;

	private static final String CONTEXT_PATH = "it.tndigitale.a4g.proxy.ws.bdn.wsAnagraficaCapo";

	public GetCapoEquinoResult getCapoEquino(String pCodiceElettronico, String pPassaporto, String pCodiceUeln) {
		log.debug("Invocazione ws per codice elettronico {} , passaporto {}, codice ueln {}", pCodiceElettronico, pPassaporto, pCodiceUeln);

		var wsInput = new GetCapoEquino();
		wsInput.setPCodiceElettronico(pCodiceElettronico == null ? "" : pCodiceElettronico);
		wsInput.setPPassaporto(pPassaporto == null ? "" : pPassaporto);
		wsInput.setPCodiceUeln(pCodiceUeln == null ? "" : pCodiceUeln);

		prepareMarshaller(CONTEXT_PATH, wsInput, serverUrl, resourceUrl);

		try {
			log.debug("Chiamata SOAP");
			GetCapoEquinoResponse response = _getCapoEquino(wsInput);
			return response.getGetCapoEquinoResult();
		} catch (Exception e) {
			log.error("errore wsws per codice elettronico {} , passaporto {}, codice ueln {}", pCodiceElettronico, pPassaporto, pCodiceUeln);
			throw e;
		}
	}

	private GetCapoEquinoResponse _getCapoEquino(GetCapoEquino request) {
		this.soapAction = this.soapActionGetCapoEquino;
		return (GetCapoEquinoResponse) getWebServiceTemplate().marshalSendAndReceive(request, this);
	}
}
