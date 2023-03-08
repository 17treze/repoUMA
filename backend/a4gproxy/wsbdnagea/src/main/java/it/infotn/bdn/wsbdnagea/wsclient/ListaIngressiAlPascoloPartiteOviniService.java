package it.infotn.bdn.wsbdnagea.wsclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.xml.transform.StringResult;

import it.infotn.bdn.wsbdnagea.config.BdnAgeaConfiguration;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiOVIINGRESSIPASCOLO;
import it.izs.wsdl.wsBDNAgea.ListaIngressiAlPascoloPartiteOvini;
import it.izs.wsdl.wsBDNAgea.ListaIngressiAlPascoloPartiteOviniResponse;
import it.izs.wsdl.wsBDNAgea.ListaIngressiAlPascoloPartiteOviniResult;
import it.izs.wsdl.wsBDNAgea.ListaIngressiAlPascoloPartiteOviniResult.Dati;

@Service
public class ListaIngressiAlPascoloPartiteOviniService {
	//Logger
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
	private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BdnAgeaConfiguration.class);
	private ListaIngressiAlPascoloPartiteOviniClient client = context.getBean(ListaIngressiAlPascoloPartiteOviniClient.class);

	private final static String contextPath = "it.izs.wsdl.wsBDNAgea";

	public ArrayOfRootDatiOVIINGRESSIPASCOLO getListaIngressiAlPascoloPartiteOvini( String cuaa, String dataInizioPeriodo, String dataFinePeriodo ) throws Exception{
		
		log.debug("Invocazione ws getListaIngressiAlPascoloPartiteOvini per "+ cuaa +
				", Data Inizio Periodo " + dataInizioPeriodo + 
				", Data Fine Periodo " + dataFinePeriodo);
		
		ListaIngressiAlPascoloPartiteOvini wsInput = new ListaIngressiAlPascoloPartiteOvini();
		wsInput.setPCuaa(cuaa);
		wsInput.setDataInizioPeriodo(dataInizioPeriodo);
		wsInput.setDataFinePeriodo(dataFinePeriodo);

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(ListaIngressiAlPascoloPartiteOviniService.contextPath);
		StringResult sresult = new StringResult();
		marshaller.marshal(wsInput, sresult);

		try {
			ListaIngressiAlPascoloPartiteOviniResponse response = client._getListaIngressiAlPascoloPartiteOvini(wsInput);  
			
			ListaIngressiAlPascoloPartiteOviniResult result = response.getListaIngressiAlPascoloPartiteOviniResult();
			Dati dati = result.getDati();
			ArrayOfRootDatiOVIINGRESSIPASCOLO arrayDati = dati.getDsOVIINGRESSIPASCOLO(); 
			
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
