package it.tndigitale.a4g.proxy.services;

import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.soap.SoapMessage;

import it.tndigitale.a4g.proxy.config.WSKSTSSupport;
import it.tndigitale.a4g.proxy.dto.DettaglioImpresa;
import it.tndigitale.a4g.proxy.dto.mapper.PersonaFisicaMapper;
import it.tndigitale.a4g.proxy.dto.mapper.PersonaGiuridicaMapper;
import it.tndigitale.a4g.proxy.dto.mapper.UnitaLocaleMapper;
import it.tndigitale.a4g.proxy.dto.persona.PersonaFisicaDto;
import it.tndigitale.a4g.proxy.dto.persona.PersonaGiuridicaDto;
import it.tndigitale.a4g.proxy.dto.persona.UnitaLocaleDto;
import it.tndigitale.ws.wssanagraficaimprese.DATIISCRIZIONEREA;
import it.tndigitale.ws.wssanagraficaimprese.DettaglioImpresaType;
import it.tndigitale.ws.wssanagraficaimprese.ESTREMIIMPRESA;
import it.tndigitale.ws.wssanagraficaimprese.ObjectFactory;
import it.tndigitale.ws.wssanagraficaimprese.RISPOSTA;
import it.tndigitale.ws.wssanagraficaimprese.RicercaImpresaPerCFType;
import it.tndigitale.ws.wssanagraficaimprese.RicercaPersonaPerCFType;

// Per qualche ragione non chiarissima il codice generato dal plugin per questo WS non contiene l'annotation XMLRootElement.
// Per ovviare al problema, è stato eseguito il workaround riportato a questo indirizzo:
// https://stackoverflow.com/questions/819720/no-xmlrootelement-generated-by-jaxb
// Estratto:
// Thankfully, when XJC generates a class model, it also generates a class called ObjectFactory. This is partly there for backwards compatibility with JAXB v1, 
// but it's also there as a place for XJC to put generated factory methods which create JAXBElement wrappers around your own objects. It handles the XML name and 
// namespace for you, so you don't need to worry about it. You just need to look through the ObjectFactory methods (and for large schema, there can be hundreds of them)
// to find the one you need.

@Service
public class AnagraficaImpresaServiceImpl extends WSKSTSSupport implements AnagraficaImpresaService {

	private static final Logger log = LoggerFactory.getLogger(AnagraficaImpresaServiceImpl.class);

	@Value("${anagraficaimpresa.uri}")
	private String wsUri;

	@Value("${anagraficaimpresa.username}")
	private String wsUsername;

	@Value("${anagraficaimpresa.password}")
	private String wsPassword;

	@Value("${anagraficaimpresa.key.alias}")
	private String keyAlias;

	private final static String PROVINCIA_DEFAULT = "TN";

	private static final String ISCRIZIONE_SEDE = "SI";

	@PostConstruct
	private void buildWebTemplate() throws Exception {
		super.buildWebTemplate("it.tndigitale.ws.wssanagraficaimprese", wsUri);
	}

	@Deprecated
	public RISPOSTA getAnagraficaImpresaNonCessata(String codiceFiscale) throws Exception {
		RicercaImpresaPerCFType ricercaImpresaPerCFType = new RicercaImpresaPerCFType();
		ricercaImpresaPerCFType.setCodiceFiscale(codiceFiscale);
		ricercaImpresaPerCFType.setUser(wsUsername);
		ricercaImpresaPerCFType.setPassword(wsPassword);
		JAXBElement<RicercaImpresaPerCFType> request = new ObjectFactory()
				.createRicercaImpresaPerCF(ricercaImpresaPerCFType);
		@SuppressWarnings("unchecked")
		JAXBElement<RISPOSTA> response = (JAXBElement<RISPOSTA>) getWebServiceTemplate().marshalSendAndReceive(request,
				(message) -> ((SoapMessage) message).setSoapAction("/RicercaImpreseNonCessPerCF"));
		return response.getValue();
	}

	protected RicercaImpresaPerCFType creaRicercaImpresaPerCFType(String codiceFiscale) {
		RicercaImpresaPerCFType requestType = new RicercaImpresaPerCFType();
		requestType.setCodiceFiscale(codiceFiscale);
		requestType.setUser(wsUsername);
		requestType.setPassword(wsPassword);
		return requestType;
	}

	protected String formattaCodiceFiscale(String codiceFiscale) {
		if (codiceFiscale == null)
			return null;
		return codiceFiscale.toUpperCase();
	}

	@Deprecated
	public RISPOSTA getDettaglioCompletoImpresa(DettaglioImpresa dettaglioImpresa) throws Exception {
		log.debug("chiamaWSDettagliCompletoImpresa: " + dettaglioImpresa.toString());
		DettaglioImpresaType dettaglioImpresaType = creaDettaglioImpresaType(dettaglioImpresa);
		JAXBElement<DettaglioImpresaType> request = new ObjectFactory().createDettaglioImpresa(dettaglioImpresaType);
		@SuppressWarnings("unchecked")
		JAXBElement<RISPOSTA> response = (JAXBElement<RISPOSTA>) getWebServiceTemplate().marshalSendAndReceive(request,
				(message) -> ((SoapMessage) message).setSoapAction("/DettaglioCompletoImpresa"));

		return response.getValue();
	}

	protected DettaglioImpresaType creaDettaglioImpresaType(DettaglioImpresa dettaglioImpresa) {
		DettaglioImpresaType requestType = new DettaglioImpresaType();
		requestType.setNumeroREASede(dettaglioImpresa.getNumeroREASede());
		requestType.setProvinciaSede(dettaglioImpresa.getProvinciaSede());
		requestType.setUser(wsUsername);
		requestType.setPassword(wsPassword);
		return requestType;
	}

	@Override
	public RISPOSTA getPersoneNonCessata(String codiceFiscale) throws Exception {
		RicercaPersonaPerCFType ricercaPersonaPerCFType = new RicercaPersonaPerCFType();
		ricercaPersonaPerCFType.setCodiceFiscale(codiceFiscale);
		ricercaPersonaPerCFType.setUser(wsUsername);
		ricercaPersonaPerCFType.setPassword(wsPassword);
		JAXBElement<RicercaPersonaPerCFType> request = new ObjectFactory()
				.createRicercaPersonaPerCF(ricercaPersonaPerCFType);
		@SuppressWarnings("unchecked")
		JAXBElement<RISPOSTA> response = (JAXBElement<RISPOSTA>) getWebServiceTemplate().marshalSendAndReceive(request,
				(message) -> ((SoapMessage) message).setSoapAction("/RicercaPersoneNonCessatePerCodiceFiscale"));

		return response.getValue();
	}

	private JAXBElement<RISPOSTA> getDettaglioImpresa(String codiceFiscale, String provinciaIscrizione) throws Exception {
		log.debug("url : {} ; username :  {} ; password ********* ; alias : {}", wsUri, wsUsername, keyAlias);
		RicercaImpresaPerCFType ricercaImpresaPerCFType = new RicercaImpresaPerCFType();
		ricercaImpresaPerCFType.setCodiceFiscale(codiceFiscale);
		ricercaImpresaPerCFType.setUser(wsUsername);
		ricercaImpresaPerCFType.setPassword(wsPassword);
		JAXBElement<RicercaImpresaPerCFType> request = new ObjectFactory().createRicercaImpresaPerCF(ricercaImpresaPerCFType);
		@SuppressWarnings("unchecked")
		JAXBElement<RISPOSTA> response = (JAXBElement<RISPOSTA>) getWebServiceTemplate().marshalSendAndReceive(request,
				(message) -> ((SoapMessage) message).setSoapAction("/RicercaImpresePerCF"));
		//Controllo l'esito della ricerca
		if (checkEsito(codiceFiscale, response) == null) {
			return null;
		}
		// seconda chiamata
		List<ESTREMIIMPRESA> estremiImpresaList = response.getValue().getDATI().getLISTAIMPRESE().getESTREMIIMPRESA();
		if (estremiImpresaList.isEmpty()) {
			return null;
		}

		// Individuo l'iscrizione presso la provincia di interesse individuando la sede con FLAG a SI
		// Di queste devo trovare l'ultima iscrizione...
		DATIISCRIZIONEREA iscrizione =
				estremiImpresaList.stream().flatMap(estremiImpresa -> estremiImpresa.getDATIISCRIZIONEREA().stream())
						.filter(i -> ISCRIZIONE_SEDE.equals(i.getFLAGSEDE()) && provinciaIscrizione.equals(i.getCCIAA()))
						.max(Comparator.comparing(DATIISCRIZIONEREA::getDATA)).get();

		DettaglioImpresa dettaglioImpresa = new DettaglioImpresa();
		if (iscrizione != null) {
			dettaglioImpresa.setNumeroREASede(iscrizione.getNREA());
			dettaglioImpresa.setProvinciaSede(iscrizione.getCCIAA());
		}


		if (dettaglioImpresa.getNumeroREASede() != null && dettaglioImpresa.getProvinciaSede() != null) {
			log.debug("chiamaWSDettagliCompletoImpresa: " + dettaglioImpresa.toString());
			DettaglioImpresaType dettaglioImpresaType = creaDettaglioImpresaType(dettaglioImpresa);
			JAXBElement<DettaglioImpresaType> requestDettaglioCompleto = new ObjectFactory().createDettaglioImpresa(dettaglioImpresaType);
			@SuppressWarnings("unchecked")
			JAXBElement<RISPOSTA> responseDettaglioCompleto = (JAXBElement<RISPOSTA>) getWebServiceTemplate().marshalSendAndReceive(
					requestDettaglioCompleto,
					(message) -> ((SoapMessage) message).setSoapAction("/DettaglioCompletoImpresa"));
			// Controllo l'esito della ricerca
			if (checkEsito(codiceFiscale, responseDettaglioCompleto) == null) {
				return null;
			} 
			return responseDettaglioCompleto;
		} else {
			throw new Exception("Errore nel recupero numero rea e provincia sede per codice fiscale: ".concat(codiceFiscale));
		}
	}

	@Override
	public PersonaFisicaDto getPersonaFisica(String codiceFiscale, String provinciaIscrizione) throws Exception {
		try {
			JAXBElement<RISPOSTA> responseDettaglioCompleto = getDettaglioImpresa(codiceFiscale,
					(provinciaIscrizione == null || provinciaIscrizione.isEmpty()) ? PROVINCIA_DEFAULT : provinciaIscrizione);
			return PersonaFisicaMapper.from(responseDettaglioCompleto);
		} catch (Exception e) {
			log.error("Errori non gestiti nel recupero dei dati della persona fisica {} per l'iscriizione nella provincia {}",
					codiceFiscale, provinciaIscrizione, e);
			throw e;
		}
	}

	@Override
	public PersonaGiuridicaDto getPersonaGiuridica(String codiceFiscale, String provinciaIscrizione) throws Exception {
		try {
			JAXBElement<RISPOSTA> responseDettaglioCompleto = getDettaglioImpresa(codiceFiscale,
					(provinciaIscrizione == null || provinciaIscrizione.isEmpty()) ? PROVINCIA_DEFAULT : provinciaIscrizione);
			return PersonaGiuridicaMapper.from(responseDettaglioCompleto);
		} catch (Exception e) {
			log.error("Errori non gestiti nel recupero dei dati della persona giuridica {} per l'iscrizione nella provincia {}",
					codiceFiscale, provinciaIscrizione, e);
			throw e;
		}
	}

	private JAXBElement<RISPOSTA> checkEsito(String codiceFiscale, JAXBElement<RISPOSTA> response) {
		if (!response.getValue().getHEADER().getESITO().equals("OK")) {
			String messaggio = response.getValue().getDATI().getERRORE().getMSGERR();
			String tipoErrore = response.getValue().getDATI().getERRORE().getTIPO();
			// tipo IMP_occorrenza_0 = cf non iscritto a camera di commercio. (impresa non trovata)
			// tipo CF_PI_errato = cf formalmente scorretto 
			if ("IMP_occorrenza_0".contains(tipoErrore)) {
				logger.info(codiceFiscale.concat(" non risulta iscritto alla Camera di Commercio"));
				return null;
			} else {
				logger.error("[EXT] - ".concat(messaggio).concat(" ").concat(codiceFiscale));
				throw new RuntimeException("[EXT] - ".concat(messaggio).concat(" ").concat(codiceFiscale));
			}		
		}
		return response;
	}

	@Override
	protected String getAlias() {
		return keyAlias;
	}

//	@Override
//	public List<UnitaLocaleDto> getUnitaLocaliPersonaGiuridica(String codiceFiscale, String provinciaIscrizione)  throws Exception {
//		try {
//			JAXBElement<RISPOSTA> responseDettaglioCompleto = getDettaglioImpresa(codiceFiscale,
//					(provinciaIscrizione == null || provinciaIscrizione.isEmpty()) ? PROVINCIA_DEFAULT : provinciaIscrizione);
//			
//			return UnitaLocaleMapper.from(responseDettaglioCompleto);	
//		} catch (Exception e) {
//			log.error("Errori non gestiti nel recupero dei dati delle unita' locali per la persona giuridica {} nella provincia {}",
//					codiceFiscale, provinciaIscrizione, e);
//			throw e;
//		}
//	}
//
//	@Override
//	public List<UnitaLocaleDto>  getUnitaLocaliPersonaFisica(String codiceFiscale, String provinciaIscrizione) throws Exception {
//		try {
//			JAXBElement<RISPOSTA> responseDettaglioCompleto = getDettaglioImpresa(codiceFiscale,
//					(provinciaIscrizione == null || provinciaIscrizione.isEmpty()) ? PROVINCIA_DEFAULT : provinciaIscrizione);
//			return UnitaLocaleMapper.from(responseDettaglioCompleto);	
//		} catch (Exception e) {
//			log.error("Errori non gestiti nel recupero dei dati delle unita' locali per la persona fisica {} nella provincia {}",
//					codiceFiscale, provinciaIscrizione, e);
//			throw e;
//		}
//	}

}
