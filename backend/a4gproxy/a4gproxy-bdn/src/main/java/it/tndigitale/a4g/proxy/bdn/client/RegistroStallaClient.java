package it.tndigitale.a4g.proxy.bdn.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.proxy.ws.bdn.wsRegistroStalla.ElencoEquiRegistriStalla;
import it.tndigitale.a4g.proxy.ws.bdn.wsRegistroStalla.ElencoEquiRegistriStallaResponse;
import it.tndigitale.a4g.proxy.ws.bdn.wsRegistroStalla.ElencoEquiRegistriStallaResponse.ElencoEquiRegistriStallaResult;

@Component
public class RegistroStallaClient extends WsdlClient {

	private static final Logger log = LoggerFactory.getLogger(RegistroStallaClient.class);

	@Value("${bdn.client.host.url}")
	private String serverUrl;

	@Value("${bdn.client.resource.wsregistristallaquery.url}")
	private String resourceUrl;

	@Value("${bdn.client.resource.wsregistristallaquery.elenco_equi_registristalla.soapaction}")
	private String soapActionElencoEquiRegistriStalla;

	private static final String CONTEXT_PATH = "it.tndigitale.a4g.proxy.ws.bdn.wsRegistroStalla";

	public ElencoEquiRegistriStallaResult getElencoEquiRegistroStalla(String pAllevId, String pTipo) {
		log.debug("Invocazione ws per id allevamento {}", pAllevId);

		var wsInput = new ElencoEquiRegistriStalla();
		wsInput.setPAllevId(pAllevId);
		wsInput.setPTipo(pTipo);
		wsInput.setPOrdinamento("matricola");

		prepareMarshaller(CONTEXT_PATH, wsInput, serverUrl, resourceUrl);

		try {
			log.debug("Chiamata SOAP");
			ElencoEquiRegistriStallaResponse response = _getElencoEquiRegistroStalla(wsInput);
			return response.getElencoEquiRegistriStallaResult();
		} catch (Exception e) {
			log.error("errore ws per id allevamento {}", pAllevId);
			throw e;
		}
	}

	private ElencoEquiRegistriStallaResponse _getElencoEquiRegistroStalla(ElencoEquiRegistriStalla request) {
		this.soapAction = this.soapActionElencoEquiRegistriStalla;
		return (ElencoEquiRegistriStallaResponse) getWebServiceTemplate().marshalSendAndReceive(request, this);
	}
}
