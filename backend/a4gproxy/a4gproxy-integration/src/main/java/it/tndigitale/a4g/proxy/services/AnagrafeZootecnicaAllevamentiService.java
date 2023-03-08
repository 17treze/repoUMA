package it.tndigitale.a4g.proxy.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.proxy.config.WSBasicSupport;
import it.tndigitale.a4g.proxy.dto.mapper.AnagraficaAllevamentoMapper;
import it.tndigitale.a4g.proxy.dto.zootecnia.AnagraficaAllevamentoDto;
import it.tndigitale.a4g.proxy.dto.zootecnia.ConsistenzaUbaOviniDto;
import it.tndigitale.a4g.proxy.ws.bdn.allevamenti.AnagraficaAllevamenti;
import it.tndigitale.a4g.proxy.ws.bdn.allevamenti.AnagraficaAllevamentiResponse;
import it.tndigitale.a4g.proxy.ws.bdn.allevamenti.AnagraficaAllevamentiResult;
import it.tndigitale.a4g.proxy.ws.bdn.allevamenti.ArrayOfRootDatiUBACONSISTENZAOVINI.UBACONSISTENZAOVINI;
import it.tndigitale.a4g.proxy.ws.bdn.allevamenti.ConsistenzaUBAOvini;
import it.tndigitale.a4g.proxy.ws.bdn.allevamenti.ConsistenzaUBAOviniResponse;
import it.tndigitale.a4g.proxy.ws.bdn.allevamenti.ConsistenzaUBAOviniResult;
import it.tndigitale.a4g.proxy.ws.bdn.allevamenti.ObjectFactory;
import it.tndigitale.a4g.proxy.ws.bdn.allevamenti.SOAPAutenticazione;

@Component
public class AnagrafeZootecnicaAllevamentiService extends WSBasicSupport {

	private static Logger log = LoggerFactory.getLogger(AnagrafeZootecnicaAllevamentiService.class);

	@Value("${bdn.agea.uri}")
	private String wsUri;

	@Value("${bdn.auth.password}")
	private String wsAuthPsw;

	@Value("${bdn.auth.username}")
	private String wsAuthUsn;

	@Autowired
	private Clock clock;

	private ObjectFactory factory = new ObjectFactory();

	@PostConstruct
	private void buildWebTemplate() throws Exception {
		super.buildWebTemplate("it.tndigitale.a4g.proxy.ws.bdn.allevamenti", wsUri);
	}


	public List<AnagraficaAllevamentoDto> getAllevamenti(String cuaa, LocalDate dataRichiesta) {
		AnagraficaAllevamenti input = factory.createAnagraficaAllevamenti();
		input.setCUUA(cuaa);

		if(dataRichiesta == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			input.setPDataRichiesta(sdf.format(new Date()));	
		}else {
			input.setPDataRichiesta(dataRichiesta.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		}


		QName qName = new QName("http://bdr.izs.it/webservices", "Anagrafica_Allevamenti");
		JAXBElement<AnagraficaAllevamenti> root = new JAXBElement<>(qName, AnagraficaAllevamenti.class, input);


		AnagraficaAllevamentiResult resultWs =  ((AnagraficaAllevamentiResponse) getWebServiceTemplate().marshalSendAndReceive(
				wsUri,
				root,
				new MyCallback("Anagrafica_Allevamenti")))
				.getAnagraficaAllevamentiResult();

		if (resultWs != null && resultWs.getErrorInfo() != null && resultWs.getErrorInfo().getError() != null && !StringUtils.isEmpty(resultWs.getErrorInfo().getError().getDes()) ) {
			log.warn("Il servizio di BDN e' andato in errore codice[" + resultWs.getErrorInfo().getError().getId() + "] - descrizione [" + resultWs.getErrorInfo().getError().getDes() + "]");
			if(resultWs.getErrorInfo().getError().getId().equals("E043") &&  resultWs.getErrorInfo().getError().getDes().equals("PERSONA NON PRESENTE IN ANAGRAFE")) {
				//            errore di "PERSONA NON PRESENTE IN ANAGRAFE": in tal caso è come se non avesse allevamenti
				return new ArrayList<AnagraficaAllevamentoDto>();
			}
			throw new RuntimeException("Il servizio di BDN e' andato in errore codice[" + resultWs.getErrorInfo().getError().getId() + "] : " + resultWs.getErrorInfo().getError().getDes());
		}

		List<AnagraficaAllevamentoDto> result = new ArrayList<AnagraficaAllevamentoDto>();
		if(resultWs.getDati() != null && resultWs.getDati().getDsANAGRAFICAALLEVAMENTI()!= null && resultWs.getDati().getDsANAGRAFICAALLEVAMENTI().getANAGRAFICAALLEVAMENTO() != null && resultWs.getDati().getDsANAGRAFICAALLEVAMENTI().getANAGRAFICAALLEVAMENTO().size() > 0) {
			resultWs.getDati().getDsANAGRAFICAALLEVAMENTI().getANAGRAFICAALLEVAMENTO().forEach(anagraficaAllevamento -> {
				result.add(AnagraficaAllevamentoMapper.from(anagraficaAllevamento));
			});
		}

		return result;
	}

	public List<ConsistenzaUbaOviniDto> getConsistenzaUbaOvini(String cuaa, LocalDate dtInizio, LocalDate dtFine, String tipoResponsabilita) {
		ConsistenzaUBAOvini input = factory.createConsistenzaUBAOvini();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		input.setPCuaa(cuaa);
		input.setPTipoResponsabilita(tipoResponsabilita);
		input.setDataInizioPeriodo(dtInizio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		input.setDataFinePeriodo(dtFine.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

		QName qName = new QName("http://bdr.izs.it/webservices", "Consistenza_UBA_Ovini");
		JAXBElement<ConsistenzaUBAOvini> root = new JAXBElement<>(qName, ConsistenzaUBAOvini.class, input);

		ConsistenzaUBAOviniResult resultWs =  ((ConsistenzaUBAOviniResponse) getWebServiceTemplate().marshalSendAndReceive(
				wsUri,
				root,
				new MyCallback("Consistenza_UBA_Ovini")))
				.getConsistenzaUBAOviniResult();


		// mappa la risposta in un dto compliance per il business
		if (resultWs.getDati().getDsUBACONSISTENZAOVINI() == null) {
			return new ArrayList<>();
		}
		List<UBACONSISTENZAOVINI> ubaconsistenzaovini = resultWs.getDati().getDsUBACONSISTENZAOVINI().getUBACONSISTENZAOVINI();

		return ubaconsistenzaovini.stream().map(response -> new ConsistenzaUbaOviniDto()
//				Double.parseDouble( number.replace(",",".")
				.setIdAllevamentoBdn(response.getPALLEVID().longValue())
				.setCodiceAzienda(response.getAZIENDACODICE())
				.setCodiceFiscaleDetentore(response.getCODFISCALEDETE())
				.setCodiceSpecie(response.getSPECODICE())
				.setOviniMaschi(Double.parseDouble(response.getOVINIMASCHI().replace(",",".")))
				.setOviniFemmine(Double.parseDouble(response.getOVINIFEMMINE().replace(",",".")))
				.setOviniTotali(Double.parseDouble(response.getOVINITOTALI().replace(",",".")))
				.setCapriniMaschi(Double.parseDouble(response.getCAPRINIMASCHI().replace(",",".")))
				.setCapriniFemmine(Double.parseDouble(response.getCAPRINIFEMMINE().replace(",",".")))
				.setCapriniTotali(Double.parseDouble(response.getCAPRINITOTALI().replace(",","."))))
				.collect(Collectors.toList());
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
