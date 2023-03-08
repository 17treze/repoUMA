package it.tndigitale.a4g.proxy.ags.bdn.business.service;


import it.izs.bdr.webservices.GetCapiAllevamentoPeriodo;
import it.izs.bdr.webservices.GetCapiAllevamentoPeriodoResponse;
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
public class CapiAllevamentoClient extends WebServiceGatewaySupport {
	private final Logger log = LoggerFactory.getLogger(CapiAllevamentoClient.class);

	/**
	 * bdn.username=appag_AGEA
	 * bdn.password=t.org956
	 * bdn.agea.url=http\://premi.izs.it/wsBDNAgea/wsAgeaAut.asmx?wsdl
	 * bdn.interrogazioni.url=http\://premi.izs.it\:9090/wsBDNInterrogazioni/wsAnagraficaCapoQry.asmx?wsdl
	 */

	@Value(BdnParameterConfiguration.USERNAME_KEY)
	private String username;

	@Value(BdnParameterConfiguration.PASSWORD_KEY)
	private String password;

	@Value(BdnParameterConfiguration.URL_KEY)
	private String serverUrl;

	private final static String CONTEXT_RESOURCE = "wsBDNInterrogazioni/wsAnagraficaCapoQry.asmx";

	private static final String SOAP_ACTION = "http://bdr.izs.it/webservices/Get_Capi_Allevamento_Periodo";

	private final static String CONTEXT = "it.izs.bdr.webservices";

	GetCapiAllevamentoPeriodoResponse.GetCapiAllevamentoPeriodoResult getCapiAllevamentoPeriodo(String idAllevamento,
																								LocalDate dataInizioPeriodo,
																								LocalDate dataFinePeriodo) {

		log.debug("Invocazione ws per ({}, Data Inizio {}, Data Fine {})", idAllevamento, dataInizioPeriodo, dataFinePeriodo);

		GetCapiAllevamentoPeriodo wsInput = new GetCapiAllevamentoPeriodo();
		wsInput.setPAllevId(idAllevamento);
		wsInput.setPCodCapo("");
		wsInput.setPDataDal(dataInizioPeriodo.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		wsInput.setPDataAl(dataFinePeriodo.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		
		// Build Marshaller
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(CONTEXT);
		StringResult sresult = new StringResult();
		marshaller.marshal(wsInput, sresult);
		
		// Set Uri and Marshaller
		setDefaultUri(serverUrl + CONTEXT_RESOURCE);
		setMarshaller(marshaller);
		setUnmarshaller(marshaller);

		try {
			GetCapiAllevamentoPeriodoResponse response = _getCapiAllevamentoPeriodo(wsInput);
			return response.getGetCapiAllevamentoPeriodoResult();
		} catch (Exception e) {
			log.error("errore ricerca dei capi per l'allevamento {} nel periodo dal {} al {}",
					idAllevamento, dataInizioPeriodo, dataFinePeriodo);
			throw e;
		}
	}

	private GetCapiAllevamentoPeriodoResponse _getCapiAllevamentoPeriodo(GetCapiAllevamentoPeriodo request) {


		GetCapiAllevamentoPeriodoResponse response = (GetCapiAllevamentoPeriodoResponse) getWebServiceTemplate()
				.marshalSendAndReceive(request, new WebServiceMessageCallback() {
					private StringSource headerSource =  new StringSource("<SOAPAutenticazione xmlns=\"http://bdr.izs.it/webservices\">\n" +
							"<username>"+ username +"</username>\n" +
							"<password>"+ password +"</password>\n" +
							"</SOAPAutenticazione>");

					public void doWithMessage(WebServiceMessage message) {
						try {
							SoapMessage soapMessage = (SoapMessage) message;
							soapMessage.setSoapAction(SOAP_ACTION);
							SoapHeader header = soapMessage.getSoapHeader();
							Transformer transformer = TransformerFactory.newInstance().newTransformer();
							transformer.transform(headerSource, header.getResult());
						} catch (Exception e) {
							// exception handling
							log.error("Errore nell'impostazione dell'header", e);
						}
					}
				});

		return response;
	}
}
