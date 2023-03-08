package it.tndigitale.a4g.proxy.services;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.TransformerException;

import it.tndigitale.a4g.proxy.dto.mapper.SianConduzioneTerreniMapper;
import it.tndigitale.a4g.proxy.dto.sian.conduzioneterreno.ConduzioneDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;

import it.tndigitale.a4g.proxy.config.WSBasicSupport;
import it.tndigitale.a4g.proxy.dto.fascicolo.sian.FascicoloSian;
import it.tndigitale.a4g.proxy.dto.mapper.FascicoloSianMapper;
import it.tndigitale.a4g.proxy.ws.siap.ISWSToOprResponse;
import it.tndigitale.a4g.proxy.ws.siap.ObjectFactory;
import it.tndigitale.a4g.proxy.ws.siap.SOAPAutenticazione;

@Service
public class SianService extends WSBasicSupport{

	@Value("${sian.uri}")
	private String wsUri;
	@Value("${sian.username}")
	private String wsUsername;
	@Value("${sian.password}")
	private String wsPassword;
	
	private ObjectFactory factory = new ObjectFactory();
	
    @PostConstruct
    private void buildWebTemplate() throws Exception {
        super.buildWebTemplate("it.tndigitale.a4g.proxy.ws.siap", wsUri);
    }
	
	public FascicoloSian trovaFascicolo(String cuaa)  {
		JAXBElement<String> request = new ObjectFactory().createCuaa(cuaa);
		ISWSToOprResponse response =
			((ISWSToOprResponse) getWebServiceTemplate().marshalSendAndReceive(
					wsUri,
					request,
					new SianCallback("TrovaFascicoloFS6")));
		return FascicoloSianMapper.from(response);
	}

    public List<ConduzioneDto> leggiConsistenza(String cuaa)  {
        JAXBElement<String> request = new ObjectFactory().createCuaa(cuaa);
        ISWSToOprResponse response =
                ((ISWSToOprResponse) getWebServiceTemplate().marshalSendAndReceive(
                        wsUri,
                        request,
                        new SianCallback("LeggiConsistenzaFS6")));
//      1.      response.iswsResponse ritorna in caso di segnalazione:"Dati non trovati" - codRet:016
//      2.      012 e segnalazione vuota in caso di risultato ok?   La risposta è un arraylist di ISWSTerritorioFS6
        return SianConduzioneTerreniMapper.from(cuaa, response);
    }
	
	
    private class SianCallback implements WebServiceMessageCallback {
        private String method;

        private SianCallback(String method ) {
            super();
            this.method = method;
        }

        @Override
        public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {

            SoapHeader soapHeader = ((SoapMessage) message).getSoapHeader();
            getMarshaller().marshal(getAuth(), soapHeader.getResult());
            ((SoapMessage) message).setSoapAction(wsUri + getMethod());
        }

        private String getMethod() {
            return this.method;
        }

        private JAXBElement<SOAPAutenticazione> getAuth() {
            SOAPAutenticazione autenticazione = factory.createSOAPAutenticazione();
            autenticazione.setNomeServizio(method);
            autenticazione.setPassword(wsPassword);
            autenticazione.setUsername(wsUsername);
            return factory.createSOAPAutenticazione(autenticazione);
        }
    }
}
