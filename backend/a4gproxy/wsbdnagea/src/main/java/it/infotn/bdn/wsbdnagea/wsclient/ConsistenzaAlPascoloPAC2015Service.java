package it.infotn.bdn.wsbdnagea.wsclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.xml.transform.StringResult;

import it.infotn.bdn.wsbdnagea.config.BdnAgeaConfiguration;
import it.izs.wsdl.wsBDNAgea.ArrayOfRootDatiCONSISTENZAPASCOLO2015;
import it.izs.wsdl.wsBDNAgea.ConsistenzaAlPascoloPAC2015;
import it.izs.wsdl.wsBDNAgea.ConsistenzaAlPascoloPAC2015Response;
import it.izs.wsdl.wsBDNAgea.ConsistenzaAlPascoloPAC2015Result;
import it.izs.wsdl.wsBDNAgea.ConsistenzaAlPascoloPAC2015Result.Dati;

@Service
public class ConsistenzaAlPascoloPAC2015Service {
	// Logger
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BdnAgeaConfiguration.class);
	private ConsistenzaAlPascoloPAC2015Client client = context.getBean(ConsistenzaAlPascoloPAC2015Client.class);

	private final static String contextPath = "it.izs.wsdl.wsBDNAgea";

	public ArrayOfRootDatiCONSISTENZAPASCOLO2015 getConsistenzaAlPascoloPAC2015(String annoCampagna, String codicePascolo) throws Exception {

		log.debug("Invocazione ws getConsistenzaAlPascoloPAC2015 per anno campagna " + annoCampagna + ", Codice Pascolo  " + codicePascolo);

		ConsistenzaAlPascoloPAC2015 wsInput = new ConsistenzaAlPascoloPAC2015();
		wsInput.setPAnnoCampagna(annoCampagna);
		wsInput.setPCodicePascolo(codicePascolo);

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(ConsistenzaAlPascoloPAC2015Service.contextPath);
		StringResult sresult = new StringResult();
		marshaller.marshal(wsInput, sresult);

		try {
			ConsistenzaAlPascoloPAC2015Response response = client._getConsistenzaAlPascoloPAC2015(wsInput);

			ConsistenzaAlPascoloPAC2015Result result = response.getConsistenzaAlPascoloPAC2015Result();
			Dati dati = result.getDati();
			ArrayOfRootDatiCONSISTENZAPASCOLO2015 arrayDati = dati.getDsCONSISTENZAPASCOLO2015();

			return arrayDati;

		} catch (Exception e) {
			log.error("errore ws per codice pascolo: {}", codicePascolo, e);
			context.close();
			throw e;
		}
	}

	protected void finalize() {
		context.close();
	}

}
