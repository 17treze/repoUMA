package it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaFisicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaGiuridicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.StatusMessagesEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.client.AnagraficaUtenteClient;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail.EmailTemplate;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail.EmailTemplateFactoryFactory;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mail.EmailTemplateList;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato.DetenzioneService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.persona.PersonaFisicaOGiuridicaConCaricaService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.MetadatiTemplateMandato;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.SchedaValidazioneFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaFisicaConCaricaDto;
import it.tndigitale.a4g.framework.client.custom.DocumentDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto.TipologiaDocumentoPrincipale;
import it.tndigitale.a4g.framework.client.custom.MittenteDto;
import it.tndigitale.a4g.framework.client.custom.ProtocolloClient;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.FascicoloNonValidabileException;
import it.tndigitale.a4g.framework.support.PersonaSelector;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.utente.client.model.Persona;

@Service
public class ProtocollazioneSchedaValidazioneService {
	
	private static Logger log = LoggerFactory.getLogger(ProtocollazioneSchedaValidazioneService.class);

	@Autowired
	private Clock clock;
	@Autowired
	private PersonaFisicaDao personaFisicaDao;
	@Autowired
	private FascicoloDao fascicoloDao;
	@Autowired
	private PersonaGiuridicaDao personaGiuridicaDao;
	@Autowired
	private EmailTemplateFactoryFactory emailTemplateFactoryFactory;
	@Autowired
	private AnagraficaUtenteClient anagraficaUtenteClient;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired 
	private PersonaFisicaOGiuridicaConCaricaService personaFisicaOGiuridicaConCaricaService;
	@Autowired
	private ProtocolloClient clientProtocollo;
	@Autowired
	private DetenzioneService detenzioneService;
	
	private static final String NOME_FILE = "SchedaDiValidazione";

	private DocumentDto getDocumentDto(SchedaValidazioneFascicoloDto schedaFascicoloDto) throws NoSuchElementException {
		var metadati = new MetadatiDto();
		// PERSONA FISICA
		if (PersonaSelector.isPersonaFisica(schedaFascicoloDto.getCodiceFiscale())) {
			PersonaFisicaModel persona = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(schedaFascicoloDto.getCodiceFiscale(), 0).orElseThrow();
			var mittenteDto = buildMittentePersonaFisica(persona);
			metadati.setOggetto(
					"A4G - SCHEDA DI VALIDAZIONE - " + schedaFascicoloDto.getCodiceFiscale() + " - " + persona.getNome() + " " + persona.getCognome());
			metadati.setMittente(mittenteDto);
		} else {
			Optional<PersonaGiuridicaModel> personaOpt = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(schedaFascicoloDto.getCodiceFiscale(), 0);
			if (!personaOpt.isPresent()) {
				throw new IllegalArgumentException("Codice Fiscale di persona giuridica inserito non valido");
			}
			PersonaGiuridicaModel persona = personaOpt.get();
			var mittenteDto = buildMittentePersonaGiuridica(persona);
			metadati.setOggetto("A4G - SCHEDA DI VALIDAZIONE - " + schedaFascicoloDto.getCodiceFiscale() + " - " + persona.getDenominazione());
			metadati.setMittente(mittenteDto);
		}

		// SET METADATI
		metadati.setMetadatiTemplate(this.prepareMapFields(schedaFascicoloDto.getCodiceFiscale()));
		metadati.setTipologiaDocumentoPrincipale(TipologiaDocumentoPrincipale.SCHEDA_VALIDAZIONE);

		// SET CONTRATTO
		ByteArrayResource documentoByteAsResource = new ByteArrayResource(schedaFascicoloDto.getReport().getByteArray()) {
			@Override
			public String getFilename() {
				return NOME_FILE + ".pdf";
			}
		};

		// SET ALLEGATI
		List<ByteArrayResource> allegatiConv = new ArrayList<>();
		if (!CollectionUtils.isEmpty(schedaFascicoloDto.getAllegati())) {
			allegatiConv.addAll(schedaFascicoloDto.getAllegati());
		}

		return buildDocumentDto(metadati, documentoByteAsResource, allegatiConv);
	}

// TODO
//	@Transactional
//	public void protocolla(final String cuaa, final Integer nextIdValidazione  ) throws FascicoloNonValidabileException {
//		var schedaDto = new SchedaValidazioneFascicoloDto();
//		schedaDto.setCodiceFiscale(cuaa);
//		schedaDto.setNextIdValidazione(nextIdValidazione);
//		schedaDto.setReport()
//	}
	
	@Transactional
	public void protocolla(final SchedaValidazioneFascicoloDto schedaDto) throws FascicoloNonValidabileException {
		Optional<FascicoloModel> fascicoloLiveOpt = fascicoloDao.findByCuaaAndIdValidazione(schedaDto.getCodiceFiscale(), schedaDto.getNextIdValidazione());
		FascicoloModel fascicoloLive = fascicoloLiveOpt.orElseThrow(() -> new FascicoloNonValidabileException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));
		var docDto = getDocumentDto(schedaDto);
		String numeroProtocollazione = clientProtocollo.protocolla(docDto);
		LocalDate dataProtocollazione = clock.today();
		
		fascicoloLive.setIdProtocollo(numeroProtocollazione);
		fascicoloLive.setDtProtocollazione(dataProtocollazione);
		fascicoloDao.save(fascicoloLive);
	}

	private DocumentDto buildDocumentDto(MetadatiDto metadati, ByteArrayResource documento, List<ByteArrayResource> allegati) {
		var docDto = new DocumentDto();
		docDto.setMetadati(metadati);
		docDto.setDocumentoPrincipale(documento);
		docDto.setAllegati(allegati);
		return docDto;
	}

	private MittenteDto buildMittentePersonaFisica(PersonaFisicaModel persona) {
		var mittente = new MittenteDto();
		mittente.setName(persona.getNome());
		mittente.setSurname(persona.getCognome());
		mittente.setNationalIdentificationNumber(persona.getCodiceFiscale());
		if (persona.getImpresaIndividuale() != null) {
			mittente.setDescription(persona.getCodiceFiscale() + " - " + persona.getNome() + " " + persona.getCognome());
		} else {
			mittente.setDescription(persona.getCodiceFiscale());
		}
		return mittente;
	}

	private MittenteDto buildMittentePersonaGiuridica(PersonaGiuridicaModel persona) {
		var mittente = new MittenteDto();
		mittente.setName(persona.getNominativoRappresentanteLegale());
		mittente.setSurname(persona.getNominativoRappresentanteLegale());
		mittente.setEmail(persona.getSedeLegale().getPec());
		mittente.setNationalIdentificationNumber(persona.getCodiceFiscale());
		mittente.setDescription(persona.getCodiceFiscale() + " - " + persona.getDenominazione());
		return mittente;
	}

	private Map<String, String> prepareMapFields(String cuaa) {
		Map<String, String> mapFields = new LinkedHashMap<>();
		mapFields.put(MetadatiTemplateMandato.CUAA.name(), cuaa);
		mapFields.put(MetadatiTemplateMandato.TIPO_DOCUMENTO.name(), TipologiaDocumentoPrincipale.SCHEDA_VALIDAZIONE.name().toLowerCase());
		return mapFields;
	}
	
	@Transactional
	public void notificaMailCaaSchedaValidazioneAccettata(String cuaa) {
		try {
			var fascicoloModel = fascicoloDao
					.findByCuaaAndIdValidazione(cuaa, 0)
					.orElseThrow(() -> new EntityNotFoundException(StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name()));
	    	PersonaFisicaConCaricaDto firmatario = personaFisicaOGiuridicaConCaricaService.getFirmatario(cuaa);
	    	var p = new Persona();
			String codiceFiscale = firmatario.getCodiceFiscale();
			p.setCodiceFiscale(codiceFiscale);
			List<Persona> persone = anagraficaUtenteClient.ricercaPerCodiceFiscale(objectMapper.writeValueAsString(p));
			if (persone == null || persone.isEmpty() || persone.size() > 1) {
				throw new IllegalArgumentException("per cuaa=[" + cuaa + "] il firmatario =[" + codiceFiscale + "] non e' censito nella base dati A4g" );
			}
			Optional<DetenzioneModel> detenzioneCorrenteOpt = detenzioneService.getDetenzioneCorrente(fascicoloModel);
			if (!detenzioneCorrenteOpt.isPresent()) {
				return;
			}
			MandatoModel detenzioneCorrente = (MandatoModel)detenzioneCorrenteOpt.get();
			var sportelloModel = detenzioneCorrente.getSportello();

			//		invio mail caa dello sportello mandatario di cui fa parte
			String caaEmail = sportelloModel.getCentroAssistenzaAgricola().getEmail();
			var emailTemplateByName = emailTemplateFactoryFactory.getEmailTemplateByName(
					EmailTemplate.getNomeQualificatore(EmailTemplateList.NOTIFICA_VALIDAZIONE_APPROVATA_AZIENDA.name()));

			String[] oggettoArgs =  {
					fascicoloModel.getDenominazione()
			};

			String[] mailArgs =  {
					persone.get(0).getNome(),
					persone.get(0).getCognome(),
					fascicoloModel.getDenominazione(),
					cuaa
			};
			emailTemplateByName.sendMail(caaEmail, oggettoArgs, mailArgs); 
		} catch (Exception e) {
			log.error("NOTIFICA_MAIL_CAA_VALIDAZIONE_ACCETTATA_FALLITA");
		}
	}
}
