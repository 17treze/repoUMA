package it.infotn.bdn.wsbdnagea.wsclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.xml.transform.StringResult;

import it.infotn.bdn.wsbdnagea.config.BdnAgeaConfiguration;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiOVIUSCITEPASCOLO;
import it.izs.wsdl.wsBDNAgea.ListaUsciteDaPascoloPartiteOvini;
import it.izs.wsdl.wsBDNAgea.ListaUsciteDaPascoloPartiteOviniResponse;
import it.izs.wsdl.wsBDNAgea.ListaUsciteDaPascoloPartiteOviniResult;
import it.izs.wsdl.wsBDNAgea.ListaUsciteDaPascoloPartiteOviniResult.Dati;

@Service
public class ListaUsciteDaPascoloPartiteOviniService {
	//Logger
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
	private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BdnAgeaConfiguration.class);
	private ListaUsciteDaPascoloPartiteOviniClient client = context.getBean(ListaUsciteDaPascoloPartiteOviniClient.class);

	private final static String contextPath = "it.izs.wsdl.wsBDNAgea";

	public ArrayOfRootDatiOVIUSCITEPASCOLO getListaUsciteDaPascoloPartiteOvini( String cuaa, String dataInizioPeriodo, String dataFinePeriodo ) throws Exception{
		
		log.debug("Invocazione ws getListaUsciteDaPascoloPartiteOvini per "+ cuaa +
				", Data Inizio Periodo " + dataInizioPeriodo + 
				", Data Fine Periodo " + dataFinePeriodo);
		
		ListaUsciteDaPascoloPartiteOvini wsInput = new ListaUsciteDaPascoloPartiteOvini();
		wsInput.setPCuaa(cuaa);
		wsInput.setDataInizioPeriodo(dataInizioPeriodo);
		wsInput.setDataFinePeriodo(dataFinePeriodo);

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(ListaUsciteDaPascoloPartiteOviniService.contextPath);
		StringResult sresult = new StringResult();
		marshaller.marshal(wsInput, sresult);

		try {
			ListaUsciteDaPascoloPartiteOviniResponse response = client._getListaUsciteDaPascoloPartiteOvini(wsInput);  
			
			ListaUsciteDaPascoloPartiteOviniResult result = response.getListaUsciteDaPascoloPartiteOviniResult();
			Dati dati = result.getDati();
			ArrayOfRootDatiOVIUSCITEPASCOLO arrayDati = dati.getDsOVIUSCITEPASCOLO(); 
			
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
