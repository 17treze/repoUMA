package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SportelloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaFisicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaGiuridicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.SportelloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.StatusMessagesEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.ApriFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.MetadatiTemplateMandato;
import it.tndigitale.a4g.framework.client.custom.DocumentDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto.TipologiaDocumentoPrincipale;
import it.tndigitale.a4g.framework.client.custom.MittenteDto;
import it.tndigitale.a4g.framework.support.PersonaSelector;

@Service
class ProtocollazioneMandatoService {

	private static final String NOME_ALLEGATO = "Allegato";
	private static final String CARTA_SERVIZI = "Carta servizi";

	@Autowired
	private SportelloDao sportelloDao;

	@Autowired
	private PersonaFisicaDao personaFisicaDao;

	@Autowired
	private PersonaGiuridicaDao personaGiuridicaDao;

	private static final String NOME_FILE = "MandatoUnicoEdEsclusivoDiAssistenza";

	@Transactional
	public DocumentDto getDocumentDto(ApriFascicoloDto apriFascicoloDto, final Integer idValidazione) throws NoSuchElementException {
		MetadatiDto metadati = new MetadatiDto();

		// SPORTELLO
		Optional<SportelloModel> sportelloModelOpt = sportelloDao.findByIdentificativo(apriFascicoloDto.getIdentificativoSportello());
		if(!sportelloModelOpt.isPresent()) {
			throw new IllegalArgumentException(StatusMessagesEnum.SPORTELLO_NON_TROVATO.name());
		}
		SportelloModel sportello = sportelloModelOpt.get();
		String denominazioneCaa = sportello.getCentroAssistenzaAgricola().getDenominazione();

		// PERSONA FISICA
		if (PersonaSelector.isPersonaFisica(apriFascicoloDto.getCodiceFiscale())) {
			PersonaFisicaModel persona = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(apriFascicoloDto.getCodiceFiscale(), 0).orElseThrow();
			MittenteDto mittenteDto = buildMittentePersonaFisica(persona);
			metadati.setOggetto(
					"A4G - MANDATO UNICO ED ESCLUSIVO DI ASSISTENZA - " + apriFascicoloDto.getCodiceFiscale() + " - " + persona.getNome() + " " + persona.getCognome() + " - " + denominazioneCaa);
			metadati.setMittente(mittenteDto);
		} else {
			Optional<PersonaGiuridicaModel> personaOpt = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(apriFascicoloDto.getCodiceFiscale(), 0);
			if (!personaOpt.isPresent()) {
				throw new IllegalArgumentException("Codice Fiscale di persona giuridica inserito non valido");
			}
			PersonaGiuridicaModel persona = personaOpt.get();
			MittenteDto mittenteDto = buildMittentePersonaGiuridica(persona);
			metadati.setOggetto("A4G - MANDATO UNICO ED ESCLUSIVO DI ASSISTENZA - " + apriFascicoloDto.getCodiceFiscale() + " - " + persona.getDenominazione() + " - " + denominazioneCaa);
			metadati.setMittente(mittenteDto);
		}

		// SET METADATI
		metadati.setMetadatiTemplate(this.prepareMapFields(denominazioneCaa, apriFascicoloDto.getCodiceFiscale()));
		metadati.setTipologiaDocumentoPrincipale(TipologiaDocumentoPrincipale.MANDATO);

		// SET CONTRATTO
		ByteArrayResource documentoByteAsResource = new ByteArrayResource(apriFascicoloDto.getContratto().getByteArray()) {
			@Override
			public String getFilename() {
				return NOME_FILE + ".pdf";
			}
		};

		// SET ALLEGATI
		List<ByteArrayResource> allegatiConv = new ArrayList<>();
		if (!CollectionUtils.isEmpty(apriFascicoloDto.getAllegati())) {
			int i = 0;
			for (ByteArrayResource allegato : apriFascicoloDto.getAllegati()) {
				String descrizione = i == 0 ? CARTA_SERVIZI : NOME_ALLEGATO + "_" + i;
				allegatiConv.add(new ByteArrayResource(
						allegato.getByteArray()) {
					@Override
					public String getFilename() {
						return new StringJoiner("$$").add(allegato.getFilename() + (".pdf")).add(descrizione).toString();
					}
				});
				i++;
			}
		}

		return buildDocumentDto(metadati, documentoByteAsResource, allegatiConv);
	}

	private DocumentDto buildDocumentDto(MetadatiDto metadati, ByteArrayResource documento, List<ByteArrayResource> allegati) {
		DocumentDto docDto = new DocumentDto();
		docDto.setMetadati(metadati);
		docDto.setDocumentoPrincipale(documento);
		docDto.setAllegati(allegati);
		return docDto;
	}

	private MittenteDto buildMittentePersonaFisica(PersonaFisicaModel persona) {
		MittenteDto mittente = new MittenteDto();
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
		MittenteDto mittente = new MittenteDto();
		mittente.setName(persona.getNominativoRappresentanteLegale());
		mittente.setSurname(persona.getNominativoRappresentanteLegale());
		mittente.setEmail(persona.getSedeLegale().getPec());
		mittente.setNationalIdentificationNumber(persona.getCodiceFiscale());
		mittente.setDescription(persona.getCodiceFiscale() + " - " + persona.getDenominazione());
		return mittente;
	}

	private Map<String, String> prepareMapFields(String denominazioneCaa, String cuaa) {
		Map<String, String> mapFields = new LinkedHashMap<>();
		mapFields.put(MetadatiTemplateMandato.CAA.name(), denominazioneCaa);
		mapFields.put(MetadatiTemplateMandato.CUAA.name(), cuaa);
		mapFields.put(MetadatiTemplateMandato.TIPO_DOCUMENTO.name(), TipologiaDocumentoPrincipale.MANDATO.name().toLowerCase());
		return mapFields;
	}
}
