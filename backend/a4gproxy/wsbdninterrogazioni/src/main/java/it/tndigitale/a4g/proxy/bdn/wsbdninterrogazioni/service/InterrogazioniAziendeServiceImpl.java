/**
 * 
 */
package it.tndigitale.a4g.proxy.bdn.wsbdninterrogazioni.service;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.StringSource;

import it.tndigitale.ws.bdn.wsinterrogazioni.wsaziendeqry.GetAziendaSTR;
import it.tndigitale.ws.bdn.wsinterrogazioni.wsaziendeqry.GetAziendaSTRResponse;
import it.tndigitale.ws.bdn.wsinterrogazioni.wsaziendeqry.ObjectFactory;
import it.tndigitale.ws.bdn.wsinterrogazioni.wsaziendeqry.SOAPAutenticazione;
import it.tndigitale.ws.dsaziendeg.DsAZIENDEG;

/**
 * @author it417
 *
 */
@Service
public class InterrogazioniAziendeServiceImpl extends WSBasicSupport {

	@Value("${bdn.interrogazioni.aziende.uri}")
	private String wsUri;
	@Value("${bdn.auth.password}")
	private String wsAuthPsw;
	@Value("${bdn.auth.username}")
	private String wsAuthUsn;

	@PostConstruct
	private void buildWebTemplate() throws Exception {
		super.buildWebTemplate("it.tndigitale.ws.bdn.wsinterrogazioni.wsaziendeqry", wsUri);
	}

	public DsAZIENDEG.AZIENDE getAzienda(String codiceAzienda) {

		SOAPAutenticazione autenticazione = new ObjectFactory().createSOAPAutenticazione();
		autenticazione.setPassword(wsAuthPsw);
		autenticazione.setUsername(wsAuthUsn);
		JAXBElement<SOAPAutenticazione> autenticazioneElement = new ObjectFactory().createSOAPAutenticazione(autenticazione);

		GetAziendaSTR getAziendaSTR = new ObjectFactory().createGetAziendaSTR();
		getAziendaSTR.setPAziendaCodice(codiceAzienda);
		QName qName = new QName("http://bdr.izs.it/webservices", "getAzienda_STR");
		JAXBElement<GetAziendaSTR> root = new JAXBElement<>(qName, GetAziendaSTR.class, getAziendaSTR);
		
		GetAziendaSTRResponse result = (GetAziendaSTRResponse)getWebServiceTemplate().marshalSendAndReceive(wsUri, root, message -> {
			SoapHeader soapHeader = ((SoapMessage) message).getSoapHeader();
			getMarshaller().marshal(autenticazioneElement, soapHeader.getResult());
			((SoapMessage) message).setSoapAction("http://bdr.izs.it/webservices/getAzienda_STR");
		});
		
		if (result != null) {
			String aziendeRootSTR = result.getGetAziendaSTRResult();
			if (aziendeRootSTR != null && !aziendeRootSTR.isEmpty()) {
				int idx1 = aziendeRootSTR.indexOf("<dsAZIENDE_G");
				if (idx1 > -1) {
					aziendeRootSTR = aziendeRootSTR.substring(idx1);
					idx1 = aziendeRootSTR.indexOf("</dsAZIENDE_G>");
					aziendeRootSTR = aziendeRootSTR.substring(0, idx1 + "</dsAZIENDE_G>".length());
					
					Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
					marshaller.setContextPath("it.tndigitale.ws.dsaziendeg");
					DsAZIENDEG aziende = (DsAZIENDEG)marshaller.unmarshal(new StringSource(aziendeRootSTR));
					return aziende.getAZIENDE().get(0);					
				}
			}
		}
		return null;
	}
}
