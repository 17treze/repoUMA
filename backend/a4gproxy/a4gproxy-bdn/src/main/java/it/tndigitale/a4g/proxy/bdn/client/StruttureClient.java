package it.tndigitale.a4g.proxy.bdn.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.proxy.ws.bdn.wsStrutture.ElencoEQUIRegistriPascoloCod;
import it.tndigitale.a4g.proxy.ws.bdn.wsStrutture.ElencoEQUIRegistriPascoloCodResponse;
import it.tndigitale.a4g.proxy.ws.bdn.wsStrutture.ElencoEQUIRegistriPascoloCodResponse.ElencoEQUIRegistriPascoloCodResult;
import it.tndigitale.a4g.proxy.ws.bdn.wsStrutture.ElencoOVIRegistriPascoloCodPeriodo;
import it.tndigitale.a4g.proxy.ws.bdn.wsStrutture.ElencoOVIRegistriPascoloCodPeriodoResponse;
import it.tndigitale.a4g.proxy.ws.bdn.wsStrutture.ElencoOVIRegistriPascoloCodPeriodoResponse.ElencoOVIRegistriPascoloCodPeriodoResult;
import it.tndigitale.a4g.proxy.ws.bdn.wsStrutture.ElencoRegistriPascoloCodPeriodo;
import it.tndigitale.a4g.proxy.ws.bdn.wsStrutture.ElencoRegistriPascoloCodPeriodoResponse;
import it.tndigitale.a4g.proxy.ws.bdn.wsStrutture.ElencoRegistriPascoloCodPeriodoResponse.ElencoRegistriPascoloCodPeriodoResult;

@Component
public class StruttureClient extends WsdlClient {		

	private static final Logger log = LoggerFactory.getLogger(StruttureClient.class);

	@Value("${bdn.client.host.url}")
	private String serverUrl;

	@Value("${bdn.client.resource.wsstrutturequery.url}")
	private String resourceUrl;

	@Value("${bdn.client.resource.wsstrutturequery.elenco_registripascolo_cod_periodo.soapaction}")
	private String soapActionRegistriPascoloCodPeriodo;
	
	@Value("${bdn.client.resource.wsstrutturequery.elencoOVI_registripascolo_cod_periodo.soapaction}")
	private String soapActionRegistriOVIPascoloCodPeriodo;
	
	@Value("${bdn.client.resource.wsstrutturequery.elencoEQUI_registripascolo_cod.soapaction}")
	private String soapActionRegistriEquiPascoloCod;

	private static final String CONTEXT_PATH = "it.tndigitale.a4g.proxy.ws.bdn.wsStrutture";

	public ElencoRegistriPascoloCodPeriodoResult getElencoRegistriPascoloCodPeriodo(String codicePascolo, String dataInizio, String dataFine) {
		log.debug("Invocazione ws get elenco registro pascolo cod periodo con codice pascolo {}", codicePascolo);

		var wsInput = new ElencoRegistriPascoloCodPeriodo();
		wsInput.setPCodicePascolo(codicePascolo);
		wsInput.setPDataInizioPeriodo(dataInizio);
		wsInput.setPDataFinePeriodo(dataFine);

		prepareMarshaller(CONTEXT_PATH, wsInput, serverUrl, resourceUrl);

		try {
			log.debug("Chiamata SOAP");
			ElencoRegistriPascoloCodPeriodoResponse response = _getElencoRegistriPascoloCodPeriodo(wsInput);
			return response.getElencoRegistriPascoloCodPeriodoResult();
		} catch (Exception e) {
			log.error("Errore ws get elenco registro pascolo cod pascolo {}", codicePascolo);
			throw e;
		}
	}

	private ElencoRegistriPascoloCodPeriodoResponse _getElencoRegistriPascoloCodPeriodo(ElencoRegistriPascoloCodPeriodo request) {
		this.soapAction = this.soapActionRegistriPascoloCodPeriodo;
		return (ElencoRegistriPascoloCodPeriodoResponse) getWebServiceTemplate().marshalSendAndReceive(request, this);
	}
	
	public ElencoOVIRegistriPascoloCodPeriodoResult getElencoOVIRegistriPascoloCodPeriodo(String codicePascolo, String dataInizio, String dataFine) {
		log.debug("Invocazione ws get elenco ovi registro pascolo cod periodo con codice pascolo: {}", codicePascolo);

		var wsInput = new ElencoOVIRegistriPascoloCodPeriodo();
		wsInput.setPCodicePascolo(codicePascolo);
		wsInput.setPDataInizioPeriodo(dataInizio);
		wsInput.setPDataFinePeriodo(dataFine);

		prepareMarshaller(CONTEXT_PATH, wsInput, serverUrl, resourceUrl);

		try {
			log.debug("Chiamata SOAP");
			ElencoOVIRegistriPascoloCodPeriodoResponse response = _getElencoOVIRegistriPascoloCodPeriodo(wsInput);
			return response.getElencoOVIRegistriPascoloCodPeriodoResult();
		} catch (Exception e) {
			log.error("Errore ws get elenco registro pascolo cod pascolo {}", codicePascolo);
			throw e;
		}
	}

	private ElencoOVIRegistriPascoloCodPeriodoResponse _getElencoOVIRegistriPascoloCodPeriodo(ElencoOVIRegistriPascoloCodPeriodo request) {
		this.soapAction = this.soapActionRegistriOVIPascoloCodPeriodo;
		return (ElencoOVIRegistriPascoloCodPeriodoResponse) getWebServiceTemplate().marshalSendAndReceive(request, this);
	}
	
	public ElencoEQUIRegistriPascoloCodResult getElencoEQUIRegistriPascoloCod(String codicePascolo, String dataInizio, String dataFine) {
		log.debug("Invocazione ws get elenco equi registro pascolo cod con codice pascolo {}", codicePascolo);

		var wsInput = new ElencoEQUIRegistriPascoloCod();
		wsInput.setPCodicePascolo(codicePascolo);
		wsInput.setPDataInizioPeriodo(dataInizio);
		wsInput.setPDataFinePeriodo(dataFine);

		prepareMarshaller(CONTEXT_PATH, wsInput, serverUrl, resourceUrl);

		try {
			log.debug("Chiamata SOAP");
			ElencoEQUIRegistriPascoloCodResponse response = _getElencoEQUIRegistriPascoloCod(wsInput);
			return response.getElencoEQUIRegistriPascoloCodResult();
		} catch (Exception e) {
			log.error("Errore ws get elenco registro pascolo cod pascolo {}", codicePascolo);
			throw e;
		}
	}

	private ElencoEQUIRegistriPascoloCodResponse _getElencoEQUIRegistriPascoloCod(ElencoEQUIRegistriPascoloCod request) {
		this.soapAction = this.soapActionRegistriEquiPascoloCod;
		return (ElencoEQUIRegistriPascoloCodResponse) getWebServiceTemplate().marshalSendAndReceive(request, this);
	}
}
