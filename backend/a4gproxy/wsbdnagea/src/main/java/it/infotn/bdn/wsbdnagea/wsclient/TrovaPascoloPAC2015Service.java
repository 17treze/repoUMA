package it.infotn.bdn.wsbdnagea.wsclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.xml.transform.StringResult;

import it.infotn.bdn.wsbdnagea.config.BdnAgeaConfiguration;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiPASCOLOVISITATO;
import it.izs.wsdl.wsBDNAgea.TrovaPascoloPAC2015;
import it.izs.wsdl.wsBDNAgea.TrovaPascoloPAC2015Response;
import it.izs.wsdl.wsBDNAgea.TrovaPascoloPAC2015Result;
import it.izs.wsdl.wsBDNAgea.TrovaPascoloPAC2015Result.Dati;

@Service
public class TrovaPascoloPAC2015Service {
	//Logger
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
	private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BdnAgeaConfiguration.class);
	private TrovaPascoloPAC2015Client client = context.getBean(TrovaPascoloPAC2015Client.class);

	private final static String contextPath = "it.izs.wsdl.wsBDNAgea";

	public ArrayOfRootDatiPASCOLOVISITATO getTrovaPascoloPAC2015( String cuaa, String annoCampagna ) throws Exception{
		
		log.debug("Invocazione ws getTrovaPascoloPAC2015 per "+ cuaa +
				", Anno Campagna " + annoCampagna);
		
		TrovaPascoloPAC2015 wsInput = new TrovaPascoloPAC2015();
		wsInput.setPCuaa(cuaa);
		wsInput.setPAnnoCampagna(annoCampagna);

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(TrovaPascoloPAC2015Service.contextPath);
		StringResult sresult = new StringResult();
		marshaller.marshal(wsInput, sresult);

		try {
			TrovaPascoloPAC2015Response response = client._getTrovaPascoloPAC2015(wsInput);  
			
			TrovaPascoloPAC2015Result result = response.getTrovaPascoloPAC2015Result();
			Dati dati = result.getDati();
			ArrayOfRootDatiPASCOLOVISITATO arrayDati = dati.getDsPASCOLIVISITATI(); 
			
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
