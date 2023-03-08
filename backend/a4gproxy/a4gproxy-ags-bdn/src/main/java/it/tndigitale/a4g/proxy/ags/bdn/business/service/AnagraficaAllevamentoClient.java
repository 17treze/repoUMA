package it.tndigitale.a4g.proxy.ags.bdn.business.service;

import it.infotn.bdn.wsbdnagea.config.BdnAgeaConfiguration;
import it.infotn.bdn.wsbdnagea.config.PropertyLoader;
import it.infotn.bdn.wsbdnagea.wsclient.AnagraficaAllevamentiClient;
import it.infotn.bdn.wsbdnagea.wsclient.ConsistenzaAllevamentoClient;
import it.infotn.bdn.wsbdnagea.wsclient.SecurityHeader;
import it.izs.wsdl.wsBDNAgea.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class AnagraficaAllevamentoClient extends WebServiceGatewaySupport {
	private final Logger logger = LoggerFactory.getLogger(AnagraficaAllevamentoClient.class);

	/**
	 * bdn.username=appag_AGEA bdn.password=t.org956 bdn.agea.url=http\://premi.izs.it/wsBDNAgea/wsAgeaAut.asmx?wsdl
	 *
	 * // https://premi.izs.it/wsBDNAgea/wsAgeaAut.asmx?wsdl // http://premi.izs.it:9090/wsBDNAgea/wsAgeaAut.asmx // ;
	 */
	@Value(BdnParameterConfiguration.USERNAME_KEY)
	private String username;

	@Value(BdnParameterConfiguration.PASSWORD_KEY)
	private String password;

	@Value(BdnParameterConfiguration.URL_KEY)
	private String serverUrl;

	private final static String CONTEXT_RESOURCE = "wsBDNAgea/wsAgeaAut.asmx";

	private final static String SOAP_ACTION = "http://bdr.izs.it/webservices/Anagrafica_Allevamenti";

	private final static String CONTEXT_PATH = "it.izs.wsdl.wsBDNAgea";

	ArrayOfRootDatiANAGRAFICAALLEVAMENTO getAnagraficaAllevamenti(String cuaa, LocalDate dataRichiesta) throws Exception {

		logger.debug("Invocazione ws per {}, Data Richiesta {}", cuaa, dataRichiesta);

		AnagraficaAllevamenti wsInput = new AnagraficaAllevamenti();
		wsInput.setCUUA(cuaa);
		// 15/05/2018
		wsInput.setPDataRichiesta(dataRichiesta.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

		// Build Marshaller
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(CONTEXT_PATH);
		StringResult sresult = new StringResult();
		marshaller.marshal(wsInput, sresult);

		// Set Uri and Marshaller
		setDefaultUri(serverUrl + CONTEXT_RESOURCE);
		setMarshaller(marshaller);
		setUnmarshaller(marshaller);

		try {
			AnagraficaAllevamentiResponse response = _getAnagraficaAllevamenti(wsInput);
			AnagraficaAllevamentiResult result = response.getAnagraficaAllevamentiResult();
			if (result.getTipoOutput().equals("errore")) {
				logger.warn("Errore BDN id: {}, descrizione: {} ", result.getErrorInfo().getError().getId(),
						result.getErrorInfo().getError().getDes());
				if (result.getErrorInfo().getError().getId().equals("E043")) {
					return null;
				}
			}
			AnagraficaAllevamentiResult.Dati dati = result.getDati();
			ArrayOfRootDatiANAGRAFICAALLEVAMENTO arrayDati = dati.getDsANAGRAFICAALLEVAMENTI();

			return arrayDati;

		} catch (Exception e) {
			logger.error("errore ws per codice: " + cuaa);
			throw e;
		}
	}

	private AnagraficaAllevamentiResponse _getAnagraficaAllevamenti(AnagraficaAllevamenti request) {
		AnagraficaAllevamentiResponse response = (AnagraficaAllevamentiResponse) getWebServiceTemplate().marshalSendAndReceive(request, new WebServiceMessageCallback() {
			private StringSource headerSource = new StringSource(
					"<SOAPAutenticazione xmlns=\"http://bdr.izs.it/webservices\">\n" + "<username>" + username + "</username>\n" + "<password>" + password + "</password>\n" + "</SOAPAutenticazione>");

			public void doWithMessage(WebServiceMessage message) {
				try {
					SoapMessage soapMessage = (SoapMessage) message;
					soapMessage.setSoapAction(SOAP_ACTION);
					SoapHeader header = soapMessage.getSoapHeader();
					Transformer transformer = TransformerFactory.newInstance().newTransformer();
					transformer.transform(headerSource, header.getResult());
				} catch (Exception e) {
					logger.error("Errore nell'impostazione dell'header", e);
				}
			}
		});

		return response;
	}
}
