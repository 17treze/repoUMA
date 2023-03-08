package it.tndigitale.a4g.proxy.bdn.client;

import java.io.IOException;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

public class WsdlClient extends WebServiceGatewaySupport implements WebServiceMessageCallback {

	@Value("${bdn.username}")
	private String username;

	@Value("${bdn.password}")
	private String password;
	
	private StringSource headerSource;
	
	protected String soapAction;
	
	public void prepareMarshaller(String contextPath, Object wsInput, String serverUrl, String resourceUrl) {
		var marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(contextPath);
		var sresult = new StringResult();
		marshaller.marshal(wsInput, sresult);

		setDefaultUri(serverUrl + resourceUrl);
		setMarshaller(marshaller);
		setUnmarshaller(marshaller);
		
		this.headerSource = new StringSource("<SOAPAutenticazione xmlns=\"http://bdr.izs.it/webservices\">\n" + "<username>" + username + "</username>\n" + "<password>" + password + "</password>\n" + "</SOAPAutenticazione>");

	}

	@Override
	public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
		try {
			SoapMessage soapMessage = (SoapMessage) message;
			soapMessage.setSoapAction(soapAction);
			var header = soapMessage.getSoapHeader();
			var transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(headerSource, header.getResult());
		} catch (Exception e) {
			logger.error("Errore nell'impostazione dell'header", e);
		}
		
	}
}
