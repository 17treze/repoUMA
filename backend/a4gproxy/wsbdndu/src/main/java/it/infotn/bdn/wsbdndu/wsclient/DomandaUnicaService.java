package it.infotn.bdn.wsbdndu.wsclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.xml.transform.StringResult;

import it.infotn.bdn.wsbdndu.config.BdnDomandaUnicaConfiguration;
import it.izs.wsdl.wsBDNDomandaUnica.ClsPremioValidazione;
import it.izs.wsdl.wsBDNDomandaUnica.ClsPremioValidazioneResponse;
import it.izs.wsdl.wsBDNDomandaUnica.GetElencoCapiPremio;
import it.izs.wsdl.wsBDNDomandaUnica.GetElencoCapiPremioResponse;

@Service
public class DomandaUnicaService {

	//Logger
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BdnDomandaUnicaConfiguration.class);
	private DomandaUnicaClient client = context.getBean(DomandaUnicaClient.class);
	
	private final static String contextPath = "it.izs.wsdl.wsBDNDomandaUnica";

	public ClsPremioValidazioneResponse getElencoCapiPremio (String cuaa, String codiceIntervento, int annoCampagna) throws Exception{

		log.debug("Invocazione getElencoCapiPremio per ["+cuaa+", "+codiceIntervento+ ", "+ annoCampagna+  "]");

		GetElencoCapiPremio wsInput = new GetElencoCapiPremio();
		
		ClsPremioValidazione clsPremio = new ClsPremioValidazione();
		clsPremio.setCUAA(cuaa);
		clsPremio.setCodiceIntervento(codiceIntervento);
		clsPremio.setAnnoCampagna(annoCampagna);
		
		wsInput.setPPremio(clsPremio);

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(DomandaUnicaService.contextPath);
		StringResult sresult = new StringResult();
		marshaller.marshal(wsInput, sresult);

		try {
			GetElencoCapiPremioResponse response = client._getElencoCapiPremio(wsInput);
			
			return response.getGetElencoCapiPremioResult();

		} catch (Exception e) {
			log.error("errore ws per codice: "+cuaa);
			context.close();
			throw e;
		}
	}
	
	protected void finalize(){
		context.close();
	}
}
