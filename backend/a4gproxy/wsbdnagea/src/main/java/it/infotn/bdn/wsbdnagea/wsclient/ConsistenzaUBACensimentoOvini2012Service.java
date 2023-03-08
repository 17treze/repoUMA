package it.infotn.bdn.wsbdnagea.wsclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.xml.transform.StringResult;

import it.infotn.bdn.wsbdnagea.config.BdnAgeaConfiguration;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiUBACENSIMENTOOVINO2012;
import it.izs.wsdl.wsBDNAgea.ConsistenzaUBACensimOvini2012;
import it.izs.wsdl.wsBDNAgea.ConsistenzaUBACensimOvini2012Response;
import it.izs.wsdl.wsBDNAgea.ConsistenzaUBACensimOvini2012Result;
import it.izs.wsdl.wsBDNAgea.ConsistenzaUBACensimOvini2012Result.Dati;

@Service
public class ConsistenzaUBACensimentoOvini2012Service {
	//Logger
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
	private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BdnAgeaConfiguration.class);
	private ConsistenzaUBACensimentoOvini2012Client client = context.getBean(ConsistenzaUBACensimentoOvini2012Client.class);

	private final static String contextPath = "it.izs.wsdl.wsBDNAgea";

	public ArrayOfRootDatiUBACENSIMENTOOVINO2012 getConsistenzaUBACensimOvini2012( String cuaa, String dataInizioPeriodo, String dataFinePeriodo, String tipoResponsabilita ) throws Exception{
		
		log.debug("Invocazione ws getConsistenzaUBACensimOvini2012 per "+ cuaa +
				", Data Inizio Periodo " + dataInizioPeriodo + 
				", Data Fine Periodo " + dataFinePeriodo + 
				", Tipo Responsabilità " + tipoResponsabilita );

		ConsistenzaUBACensimOvini2012 wsInput = new ConsistenzaUBACensimOvini2012();
		wsInput.setPCuaa(cuaa);
		wsInput.setDataInizioPeriodo(dataInizioPeriodo);
		wsInput.setDataFinePeriodo(dataFinePeriodo);
		wsInput.setPTipoResponsabilita(tipoResponsabilita);

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(ConsistenzaUBACensimentoOvini2012Service.contextPath);
		StringResult sresult = new StringResult();
		marshaller.marshal(wsInput, sresult);

		try {
			ConsistenzaUBACensimOvini2012Response response = client._getConsistenzaUBACensimOvini2012(wsInput);  
			
			ConsistenzaUBACensimOvini2012Result result = response.getConsistenzaUBACensimOvini2012Result();
			Dati dati = result.getDati();
			ArrayOfRootDatiUBACENSIMENTOOVINO2012 arrayDati = dati.getDsUBACENSIMENTIOVINI2012();
			
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
