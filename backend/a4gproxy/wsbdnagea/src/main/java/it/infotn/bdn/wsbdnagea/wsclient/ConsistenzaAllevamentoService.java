package it.infotn.bdn.wsbdnagea.wsclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.xml.transform.StringResult;

import it.infotn.bdn.wsbdnagea.config.BdnAgeaConfiguration;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiCONSISTENZAALLEVAMENTO;
import it.izs.wsdl.wsBDNAgea.ConsistenzaAllevamento;
import it.izs.wsdl.wsBDNAgea.ConsistenzaAllevamentoResponse;
import it.izs.wsdl.wsBDNAgea.ConsistenzaAllevamentoResult;
import it.izs.wsdl.wsBDNAgea.ConsistenzaAllevamentoResult.Dati;

@Service
public class ConsistenzaAllevamentoService {

	//Logger
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    

	public ArrayOfRootDatiCONSISTENZAALLEVAMENTO inviaConsistenzaAllevamento(String cuaa, String dataInizioPeriodo, String dataFinePeriodo, String responsabilita) throws Exception{
		
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BdnAgeaConfiguration.class);
		ConsistenzaAllevamentoClient client = context.getBean(ConsistenzaAllevamentoClient.class);
		
		ArrayOfRootDatiCONSISTENZAALLEVAMENTO arrayDati = null;
			log.debug("Invocazione ws per ("+cuaa+", Data Inizio "+dataInizioPeriodo+", Data Fine "+dataFinePeriodo+")");
			
			ConsistenzaAllevamento wsInput = new ConsistenzaAllevamento();
			wsInput.setPCuaa(cuaa);
			wsInput.setDataInizioPeriodo(dataInizioPeriodo);
			wsInput.setDataFinePeriodo(dataFinePeriodo);
			wsInput.setPTipoResponsabilita("");
			Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    		marshaller.setContextPath(BdnAgeaConfiguration.contextPath);
    		StringResult sresult = new StringResult();
    		marshaller.marshal(wsInput, sresult);

    		try {
	    		ConsistenzaAllevamentoResponse response = client._inviaConsistenzaAllevamento(wsInput);   
	    		ConsistenzaAllevamentoResult result = response.getConsistenzaAllevamentoResult();
	    		Dati dati = result.getDati();
	    		arrayDati = dati.getDsCONSISTENZAALLEVAMENTI();
	    		
	    		
    		} catch (Exception e) {
    			log.error("errore chiamata del WebService per codice: "+cuaa);
    			context.close();
    			return null;
    		}
			
		context.close(); // TODO sistemare context
		return arrayDati;
	}
	

}
