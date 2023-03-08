package it.infotn.bdn.wsbdnagea.wsclient;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.StringSource;

import it.infotn.bdn.wsbdnagea.config.BdnAgeaConfiguration;
import it.infotn.bdn.wsbdnagea.config.PropertyLoader;
import it.izs.wsdl.wsBDNAgea.ConsistenzaAlPascoloPAC2015;
import it.izs.wsdl.wsBDNAgea.ConsistenzaAlPascoloPAC2015Response;
import it.izs.wsdl.wsBDNAgea.SOAPAutenticazione;

public class ConsistenzaAlPascoloPAC2015Client extends WebServiceGatewaySupport {

	private static final String usernameKey = BdnAgeaConfiguration.webServiceUsername;
	private static final String passwordKey = BdnAgeaConfiguration.webServicePassword;
	private PropertyLoader loader = PropertyLoader.getInstance();

	private static final Logger log = LoggerFactory.getLogger(ConsistenzaAlPascoloPAC2015Client.class);
	public static final String soapAction = "http://bdr.izs.it/webservices/Consistenza_Al_Pascolo_PAC2015";

	private StringSource headerSource = new StringSource("<SOAPAutenticazione xmlns=\"http://bdr.izs.it/webservices\">\n" + "<username>" + loader.getValue(usernameKey) + "</username>\n" + "<password>"
			+ loader.getValue(passwordKey) + "</password>\n" + "</SOAPAutenticazione>");

	public ConsistenzaAlPascoloPAC2015Response getConsistenzaAlPascoloPAC2015(ConsistenzaAlPascoloPAC2015 request) {

		PropertyLoader loader = PropertyLoader.getInstance();
		String username = loader.getValue(usernameKey);
		String password = loader.getValue(passwordKey);

		SOAPAutenticazione autenticazione = new SOAPAutenticazione();
		autenticazione.setUsername(username);
		autenticazione.setPassword(password);

		return (ConsistenzaAlPascoloPAC2015Response) getWebServiceTemplate().marshalSendAndReceive(request, new SecurityHeader(autenticazione));
	}

	public ConsistenzaAlPascoloPAC2015Response _getConsistenzaAlPascoloPAC2015(ConsistenzaAlPascoloPAC2015 request) {

		ConsistenzaAlPascoloPAC2015Response response = (ConsistenzaAlPascoloPAC2015Response) getWebServiceTemplate().marshalSendAndReceive(request, new WebServiceMessageCallback() {
			public void doWithMessage(WebServiceMessage message) {
				try {
					SoapMessage soapMessage = (SoapMessage) message;
					soapMessage.setSoapAction(ConsistenzaAlPascoloPAC2015Client.soapAction);
					SoapHeader header = soapMessage.getSoapHeader();
					Transformer transformer = TransformerFactory.newInstance().newTransformer();
					transformer.transform(headerSource, header.getResult());
				} catch (Exception e) {
					// exception handling
					log.error("Errore nell'impostazione dell'header");
					e.printStackTrace();
				}
			}
		});

		return response;
	}

}
