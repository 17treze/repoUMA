package it.infotn.bdn.wsbdndu.wsclient;

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

import it.infotn.bdn.wsbdndu.config.BdnDomandaUnicaConfiguration;
import it.infotn.bdn.wsbdndu.config.PropertyLoader;
import it.izs.wsdl.wsBDNDomandaUnica.GetElencoCapiPremio;
import it.izs.wsdl.wsBDNDomandaUnica.GetElencoCapiPremioResponse;
import it.izs.wsdl.wsBDNDomandaUnica.SOAPAutenticazione;

public class DomandaUnicaClient extends WebServiceGatewaySupport {

	private static final String usernameKey = BdnDomandaUnicaConfiguration.webServiceUsername;
	private static final String passwordKey = BdnDomandaUnicaConfiguration.webServicePassword;
	
	private PropertyLoader loader = PropertyLoader.getInstance();

	private static final Logger log = LoggerFactory.getLogger(DomandaUnicaClient.class);
	public static final String soapAction = "http://bdr.izs.it/webservices/get_Elenco_Capi_Premio";
	
	private StringSource headerSource =  new StringSource("<SOAPAutenticazione xmlns=\"http://bdr.izs.it/webservices\">\n" +        
            "<username>"+ loader.getValue(usernameKey)+"</username>\n" +
            "<password>"+ loader.getValue(passwordKey)+"</password>\n" +
            "</SOAPAutenticazione>");	
		
	public GetElencoCapiPremioResponse getElencoCapiPremio(GetElencoCapiPremio request) {
		 
		PropertyLoader loader = PropertyLoader.getInstance();
		String username = loader.getValue(usernameKey);
		String password = loader.getValue(passwordKey);
	 	
	   
		SOAPAutenticazione autenticazione = new SOAPAutenticazione();
		autenticazione.setUsername(username);
		autenticazione.setPassword(password);
	 			 	
		return (GetElencoCapiPremioResponse) getWebServiceTemplate()
				.marshalSendAndReceive(request, new SecurityHeader(autenticazione));
	}
	
	
	 public GetElencoCapiPremioResponse _getElencoCapiPremio(GetElencoCapiPremio wsInput) {

			
		 GetElencoCapiPremioResponse response = (GetElencoCapiPremioResponse) getWebServiceTemplate()
					.marshalSendAndReceive(wsInput, new WebServiceMessageCallback() {

				        public void doWithMessage(WebServiceMessage message) {
				            try {
				                SoapMessage soapMessage = (SoapMessage) message;
				                soapMessage.setSoapAction(DomandaUnicaClient.soapAction);
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
