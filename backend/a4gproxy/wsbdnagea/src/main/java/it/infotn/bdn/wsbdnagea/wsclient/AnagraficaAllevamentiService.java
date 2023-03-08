package it.infotn.bdn.wsbdnagea.wsclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.xml.transform.StringResult;

import it.infotn.bdn.wsbdnagea.config.BdnAgeaConfiguration;
import it.izs.wsdl.wsBDNAgea.AnagraficaAllevamenti;
import it.izs.wsdl.wsBDNAgea.AnagraficaAllevamentiResponse;
import it.izs.wsdl.wsBDNAgea.AnagraficaAllevamentiResult;
import it.izs.wsdl.wsBDNAgea.AnagraficaAllevamentiResult.Dati;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiANAGRAFICAALLEVAMENTO;

@Service
public class AnagraficaAllevamentiService {

	//Logger
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BdnAgeaConfiguration.class);
	private AnagraficaAllevamentiClient client = context.getBean(AnagraficaAllevamentiClient.class);
	
	private final static String contextPath = "it.izs.wsdl.wsBDNAgea";

	public ArrayOfRootDatiANAGRAFICAALLEVAMENTO getAnagraficaAllevamenti( String cuaa, String dataRichiesta ) throws Exception{

		
		log.debug("Invocazione ws per "+cuaa+", Data Richiesta "+dataRichiesta );

		AnagraficaAllevamenti wsInput = new AnagraficaAllevamenti();
		wsInput.setCUUA(cuaa);
		wsInput.setPDataRichiesta(dataRichiesta);

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(AnagraficaAllevamentiService.contextPath);
		StringResult sresult = new StringResult();
		marshaller.marshal(wsInput, sresult);

		try {
			AnagraficaAllevamentiResponse response = client._getAnagraficaAllevamenti(wsInput);   
			AnagraficaAllevamentiResult result = response.getAnagraficaAllevamentiResult();
			Dati dati = result.getDati();
			ArrayOfRootDatiANAGRAFICAALLEVAMENTO arrayDati = dati.getDsANAGRAFICAALLEVAMENTI();
			
			return arrayDati;

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
