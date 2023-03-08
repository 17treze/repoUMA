package it.tndigitale.a4g.proxy.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Node;

import it.tndigitale.a4g.framework.support.CustomCollectors;
import it.tndigitale.a4g.proxy.config.WSKSTSSupport;
import it.tndigitale.a4g.proxy.dto.InfoVerificaFirma;
import it.tndigitale.a4g.proxy.utils.DateFormatUtils;
import it.tndigitale.ws.verificafirmedigitali.DettaglioFirmaDigitaleType;
import it.tndigitale.ws.verificafirmedigitali.FirmatarioType;
import it.tndigitale.ws.verificafirmedigitali.ObjectFactory;
import it.tndigitale.ws.verificafirmedigitali.VerificaFirmaResponseType;
import it.tndigitale.ws.verificafirmedigitali.VerificaFirmaType;
import it.tndigitale.ws.verificafirmedigitali.WarningResponseType;
import it.tndigitale.ws.verificafirmedigitali.WarningType;

//Per qualche ragione non chiarissima il codice generato dal plugin per questo WS non contiene l'annotation XMLRootElement.
//Per ovviare al problema, è stato eseguito il workaround riportato a questo indirizzo:
//https://stackoverflow.com/questions/819720/no-xmlrootelement-generated-by-jaxb
//Estratto:
//Thankfully, when XJC generates a class model, it also generates a class called ObjectFactory. This is partly there for backwards compatibility with JAXB v1, 
//but it's also there as a place for XJC to put generated factory methods which create JAXBElement wrappers around your own objects. It handles the XML name and 
//namespace for you, so you don't need to worry about it. You just need to look through the ObjectFactory methods (and for large schema, there can be hundreds of them)
//to find the one you need.

@Service
public class VerificaFirmaServiceImpl extends WSKSTSSupport implements VerificaFirmaService {

	@Value("${verificafirmedigitali.uri}")
	private String wsUri;

	@Value("${verificafirmedigitali.key.alias}")
	private String keyAlias;

	@Value("${it.tndigit.a4g.proxy.firma.verificaCodiceFiscaleFirmatario:true}")
	private boolean verificaCodiceFiscaleFirmatario;

	@PostConstruct
	private void buildWebTemplate() throws Exception {
		super.buildWebTemplate("it.tndigitale.ws.verificafirmedigitali", wsUri);
	}

	@SuppressWarnings("unchecked")
	@Override
	public WarningResponseType verificaFirma(MultipartFile documentoFirmato) throws IOException {
		VerificaFirmaType requestType = new VerificaFirmaType();
		requestType.setFileFirmato(documentoFirmato.getBytes());
		JAXBElement<VerificaFirmaType> request = new ObjectFactory().createVerificaFirma(requestType);
		JAXBElement<WarningResponseType> result = new ObjectFactory().createWarningResponse(new WarningResponseType());

		// Gestione del SoapFault effettuata tramite il set del FaultMessageResolver è necessaria perchè la risposta del servizio è diversa nel caso di success o error
		getWebServiceTemplate().setFaultMessageResolver(message -> {
			SoapFaultDetail faultDetail = ((SoapMessage) message).getSoapBody().getFault().getFaultDetail();
			Node node = ((DOMResult) faultDetail.getDetailEntries().next().getResult()).getNode();
			JAXBElement<WarningResponseType> serviceFault = (JAXBElement<WarningResponseType>) getUnmarshaller().unmarshal(new DOMSource(node));
			if (serviceFault != null && serviceFault.getValue() != null) {
				result.setValue(serviceFault.getValue());
			}

		});

		// Gestione del SoapMessage
		JAXBElement<VerificaFirmaResponseType> response = ((JAXBElement<VerificaFirmaResponseType>) getWebServiceTemplate().marshalSendAndReceive(request,
				message -> ((SoapMessage) message).setSoapAction("/VerificaFirma")));

		if (response != null) {
			result.getValue().setDettaglioFirmaDigitale(response.getValue().getEsito());
		}

		return result.getValue();

	}

	public List<InfoVerificaFirma> verificaFirmaMultipla(MultipartFile documentoFirmato, List<String> codiceFiscaleList) throws IOException, VerificaFirmaException {
		WarningResponseType response = verificaFirma(documentoFirmato);
		verificaPresenzaErroriResponse(response.getWarningFault());
		verificheFirmatarioMultiplo(response.getDettaglioFirmaDigitale(), codiceFiscaleList);
		// recupero data della firma e codice fiscale firmatario 
		var infoVerificaFirme = new ArrayList<InfoVerificaFirma>();
		List<FirmatarioType> datiFirmatari = response.getDettaglioFirmaDigitale().getDatiFirmatari();
		
		datiFirmatari.forEach( firmatario -> {
			var infoVerificaFirma = new InfoVerificaFirma();
			infoVerificaFirma.setDataFirma(DateFormatUtils.convertiLocalDate(firmatario.getFirmatario().getDataOraFirma()));
			infoVerificaFirma.setCfFirmatario(firmatario.getFirmatario().getCodiceFiscale());
			infoVerificaFirme.add(infoVerificaFirma);
		});
		
		return infoVerificaFirme;
	}
	
	protected void verificheFirmatarioMultiplo(final DettaglioFirmaDigitaleType dettaglio, final List<String> codiceFiscaleList) throws VerificaFirmaException {
		if (!verificaCodiceFiscaleFirmatario) {
			return;
		}
		// Se nullo DECIDO di non lanciare eccezioni perche' magari non mi interessa verificarne la corrispondenza
		// ad esempio perche' non ho il codice fiscale da confrontare
		if (codiceFiscaleList == null || codiceFiscaleList.isEmpty()) {
			return;
		}
		// Controllo che esistano i dati dei firmatari
		if (dettaglio.getDatiFirmatari().isEmpty()) {
			throw new VerificaFirmaException(VerificaFirmaErrorCodeEnum.DATI_FIRMATARIO_MANCANTI);
		}
		// Controllo la presenza di un numero di firmatari pari a quello fornito in input da codiceFiscaleList
		if (!(dettaglio.getDatiFirmatari().size() == codiceFiscaleList.size() && dettaglio.getParteFirmata() == null)) {
			throw new VerificaFirmaException(VerificaFirmaErrorCodeEnum.FIRMATARIO_NUMERO_ERRATO);
		}
		// Controllo che il cf dei firmatari inclusi coincidano con i cf ricevuti in input
		var numeroFirmatariVerificato = 0;
		
		for(String codiceFiscale : codiceFiscaleList) {	
			for (FirmatarioType firmatario : dettaglio.getDatiFirmatari()) {
				if(firmatario.getFirmatario().getCodiceFiscale().equalsIgnoreCase(codiceFiscale)) {
					numeroFirmatariVerificato++;	
					break;
				}
			}
		}
		
		if (numeroFirmatariVerificato != codiceFiscaleList.size()) {
			throw new VerificaFirmaException(VerificaFirmaErrorCodeEnum.FIRMATARIO_TITOLARE_DIVERSI);
		}
	}
	
	
	public InfoVerificaFirma verificaFirmaSingola(MultipartFile documentoFirmato, String codiceFiscale) throws IOException, VerificaFirmaException {
		WarningResponseType response = verificaFirma(documentoFirmato);
		verificaPresenzaErroriResponse(response.getWarningFault());
		verificheFirmatario(response.getDettaglioFirmaDigitale(), codiceFiscale);
		// recupero data della firma e codice fiscale firmatario 
		InfoVerificaFirma infoVerificaFirma = new InfoVerificaFirma();
		List<FirmatarioType> datiFirmatari = response.getDettaglioFirmaDigitale().getDatiFirmatari();
		FirmatarioType firmatario = datiFirmatari.stream().collect(CustomCollectors.toSingleton());
		infoVerificaFirma.setDataFirma(DateFormatUtils.convertiLocalDate(firmatario.getFirmatario().getDataOraFirma()));
		infoVerificaFirma.setCfFirmatario(firmatario.getFirmatario().getCodiceFiscale());
		return infoVerificaFirma;
	}

	protected void verificaPresenzaErroriResponse(List<WarningType> errorList) throws VerificaFirmaException {
		if (!errorList.isEmpty()) {
			if (errorList.get(0).getErrorCode().equals("1458")) {
				throw new VerificaFirmaException(VerificaFirmaErrorCodeEnum.FILE_NON_FIRMATO);
			} else {
				throw new  VerificaFirmaException(VerificaFirmaErrorCodeEnum.FIRMA_NON_VALIDA);
			}
		}
	}

	protected void verificheFirmatario(final DettaglioFirmaDigitaleType dettaglio, final String codiceFiscale) throws VerificaFirmaException {
		if (!verificaCodiceFiscaleFirmatario) {
			return;
		}
		// Se nullo DECIDO di non lanciare eccezioni perche' magari non mi interessa verificarne la corrispondenza
		// ad esempio perche' non ho il codice fiscale da confrontare
		if (codiceFiscale == null) {
			return;
		}
		// Controllo che esistano i dati dei firmatari
		if (dettaglio.getDatiFirmatari().isEmpty()) {
			throw new VerificaFirmaException(VerificaFirmaErrorCodeEnum.DATI_FIRMATARIO_MANCANTI);
		}
		// Controllo la presenza di un solo firmatario
		if (!(dettaglio.getDatiFirmatari().size() == 1 && dettaglio.getParteFirmata() == null)) {
			throw new VerificaFirmaException(VerificaFirmaErrorCodeEnum.FIRMATARIO_NON_UNIVOCO);
		}
		// Controllo che il cf del firmatario coincida con il cf ricevuto in input
		if (!(dettaglio.getDatiFirmatari().get(0).getFirmatario().getCodiceFiscale().equals(codiceFiscale))) {
			throw new VerificaFirmaException(VerificaFirmaErrorCodeEnum.FIRMATARIO_TITOLARE_DIVERSI);
		}
	}

	@Override
	protected String getAlias() {
		return keyAlias;
	}
}
