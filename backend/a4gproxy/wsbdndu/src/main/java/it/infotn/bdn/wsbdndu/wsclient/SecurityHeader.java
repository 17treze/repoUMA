package it.infotn.bdn.wsbdndu.wsclient;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.TransformerException;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;

import it.izs.wsdl.wsBDNDomandaUnica.ObjectFactory;
import it.izs.wsdl.wsBDNDomandaUnica.SOAPAutenticazione;

public class SecurityHeader implements WebServiceMessageCallback {

    private SOAPAutenticazione authentication;

    public SecurityHeader(SOAPAutenticazione authentication) {
        this.authentication = authentication;
    }

    @Override
    public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
        SoapHeader soapHeader = ((SoapMessage)message).getSoapHeader();

        try {
            JAXBContext context = JAXBContext.newInstance(JAXBElement.class);
            JAXBElement<SOAPAutenticazione> elemen = new ObjectFactory().createSOAPAutenticazione(authentication);
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(elemen, soapHeader.getResult());

        } catch (JAXBException e) {
            throw new IOException("error while marshalling authentication.",e);
        }
    }
}
