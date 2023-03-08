package it.tndigitale.a4g.proxy.services;

import it.tndigitale.a4g.proxy.config.WSBasicSupport;
import it.tndigitale.a4g.proxy.dto.zootecnia.CapiAziendaPerInterventoFilter;
import it.tndigitale.a4g.proxy.ws.bdn.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;
import java.io.IOException;

@Component
public class AnagrafeZootecnicaService extends WSBasicSupport {

    @Value("${bdn.domandaunica.uri}")
    private String wsUri;

    @Value("${bdn.auth.password}")
    private String wsAuthPsw;

    @Value("${bdn.auth.username}")
    private String wsAuthUsn;

    private ObjectFactory factory = new ObjectFactory();

    @PostConstruct
    private void buildWebTemplate() throws Exception {
        super.buildWebTemplate("it.tndigitale.a4g.proxy.ws.bdn", wsUri);
    }

    public ClsPremioValidazioneResponse getCapiDetenuti(CapiAziendaPerInterventoFilter filter) {
        if (filter.getCuaaSubentrante() != null && filter.getCuaaSubentrante().length() > 0) {
            return getCapiDetenutiSubentrante(filter);
        }

        return getCapiDetenutiRichiedente(filter);
    }

    private ClsPremioValidazioneResponse getCapiDetenutiRichiedente(CapiAziendaPerInterventoFilter filter) {
        ClsPremioValidazione inputBDN = factory.createClsPremioValidazione();
        inputBDN.setAnnoCampagna(filter.getCampagna());
        inputBDN.setCodiceIntervento(filter.getCodiceAgeaIntervento().toString());
        inputBDN.setCUAA(filter.getCuaa());
        if (filter.getIdAllevamento() != null)
            inputBDN.setIdAlleBDN(filter.getIdAllevamento());

        GetElencoCapiPremio bdnService = factory.createGetElencoCapiPremio();
        bdnService.setPPremio(inputBDN);

        QName qName = new QName("http://bdr.izs.it/webservices", "get_Elenco_Capi_Premio");
        JAXBElement<GetElencoCapiPremio> root = new JAXBElement<>(qName, GetElencoCapiPremio.class, bdnService);

        return ((GetElencoCapiPremioResponse) getWebServiceTemplate().marshalSendAndReceive(
                wsUri,
                root,
                new MyCallback("get_Elenco_Capi_Premio"))).getGetElencoCapiPremioResult();
    }

    private ClsPremioValidazioneResponse getCapiDetenutiSubentrante(CapiAziendaPerInterventoFilter filter) {
        ClsPremioValidazionePP inputBDN = factory.createClsPremioValidazionePP();
        inputBDN.setAnnoCampagna(filter.getCampagna());
        inputBDN.setCodiceIntervento(filter.getCodiceAgeaIntervento().toString());
        inputBDN.setCUAA(filter.getCuaa());
        inputBDN.setCUAA2(filter.getCuaaSubentrante());
        if (filter.getIdAllevamento() != null)
            inputBDN.setIdAlleBDN(filter.getIdAllevamento());

        GetElencoCapiPremio2 bdnService = factory.createGetElencoCapiPremio2();
        bdnService.setPPremio(inputBDN);

        QName qName = new QName("http://bdr.izs.it/webservices", "get_Elenco_Capi_Premio2");
        JAXBElement<GetElencoCapiPremio2> root = new JAXBElement<>(qName, GetElencoCapiPremio2.class, bdnService);

       return ((GetElencoCapiPremio2Response) getWebServiceTemplate().marshalSendAndReceive(
                wsUri,
                root,
                new MyCallback("get_Elenco_Capi_Premio2"))).getGetElencoCapiPremio2Result();
    }

    private class MyCallback implements WebServiceMessageCallback {
        private String method;

        private MyCallback(String method) {
            super();
            this.method = method;
        }

        @Override
        public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
            String url = "http://bdr.izs.it/webservices/";

            SoapHeader soapHeader = ((SoapMessage) message).getSoapHeader();
            getMarshaller().marshal(getAuth(), soapHeader.getResult());
            ((SoapMessage) message).setSoapAction(url + getMethod());
        }

        private String getMethod() {
            return this.method;
        }

        private JAXBElement<SOAPAutenticazione> getAuth() {
            SOAPAutenticazione autenticazione = factory.createSOAPAutenticazione();
            autenticazione.setUsername(wsAuthUsn);
            autenticazione.setPassword(wsAuthPsw);
            return factory.createSOAPAutenticazione(autenticazione);
        }
    }
}
