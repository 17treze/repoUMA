package it.tndigitale.a4gutente.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gutente.dto.Persona;
import it.tndigitale.a4gutente.repository.dao.IAccettazioneInformativaGeneraleDao;
import it.tndigitale.a4gutente.repository.dao.IPersonaDao;
import it.tndigitale.a4gutente.repository.model.AccettazioneInformativaEntita;
import it.tndigitale.a4gutente.repository.model.AccettazioneInformativaGeneraleEntita;
import it.tndigitale.a4gutente.repository.model.PersonaEntita;

@Service
public class PersonaService implements IPersonaService {

	private static final Logger logger = LoggerFactory.getLogger(PersonaService.class);
	@Autowired
	private IPersonaDao personaRepository;

	@Autowired
	private IAccettazioneInformativaGeneraleDao accettazioneRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.tndigitale.a4gutente.service.IPersonaService#caricaPersona(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Persona caricaPersona(Long id) throws Exception {
		PersonaEntita persona = personaRepository.getOne(id);
		Persona result = convert(persona);
		AccettazioneInformativaEntita privacy = accettazioneRepository.findByPersona(persona);
		if (privacy != null) {
			result.setNrProtocolloPrivacyGenerale(privacy.getNumeroProtocollazione());
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Persona> ricercaPersone(String codiceFiscale) throws Exception {
		List<PersonaEntita> personeEntita = personaRepository.findByCodiceFiscale(codiceFiscale);
		if (personeEntita == null) {
			return null;
		}
		List<Persona> result = new ArrayList<>();
		personeEntita.stream().forEach((entita) -> {
			Persona personaResult = convert(entita);
			AccettazioneInformativaEntita privacy = accettazioneRepository.findByPersona(entita);
			if (privacy != null) {
				personaResult.setNrProtocolloPrivacyGenerale(privacy.getNumeroProtocollazione());
			}
			result.add(personaResult);
		});
		return result;
	}

	protected Persona convert(PersonaEntita persona) {
		Persona result = new Persona();
		result.setId(persona.getId());
		result.setVersion(persona.getVersion());
		result.setCodiceFiscale(persona.getCodiceFiscale());
		result.setNome(persona.getNome());
		result.setCognome(persona.getCognome());
		return result;
	}

	private Long inserisciPersona(Persona persona) throws Exception {
		PersonaEntita personaEntity = new PersonaEntita();
		Long id = null;
		try {
			BeanUtils.copyProperties(persona, personaEntity);
			personaEntity = personaRepository.save(personaEntity);
			logger.debug("Salvataggio della persona avvenuto con successo");
			if (Objects.nonNull(persona.getNrProtocolloPrivacyGenerale())) {
				AccettazioneInformativaGeneraleEntita privacy = new AccettazioneInformativaGeneraleEntita();
				LocalDateTime ldtPrivacy = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
				privacy.setDataAccettazione(ldtPrivacy.toLocalDate());
				privacy.setDataProtocollazione(ldtPrivacy.toLocalDate());
				privacy.setNumeroProtocollazione(persona.getNrProtocolloPrivacyGenerale());
				privacy.setPersona(personaEntity);
				accettazioneRepository.save(privacy);
				logger.debug("Accettazione informativa avvenuta con successo");
			}
			Persona personaResult = convert(personaEntity);
			id = personaResult.getId();
		} catch (Exception e) {
			logger.error("Impossibile salvare l'entity Persona", e);
			throw e;
		}

		return id;
	}

	@Override
	@Transactional
	public Persona aggiornaPersona(Persona persona) throws Exception {
		PersonaEntita personaEntity = personaRepository.getOne(persona.getId());
		try {
			BeanUtils.copyProperties(persona, personaEntity);
			personaEntity = personaRepository.save(personaEntity);
			BeanUtils.copyProperties(personaEntity, persona);
			if (persona.getNrProtocolloPrivacyGenerale() != null
					&& !persona.getNrProtocolloPrivacyGenerale().isEmpty()) {
				LocalDateTime ldtPrivacy = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
				AccettazioneInformativaGeneraleEntita privacy = accettazioneRepository.findByPersona(personaEntity);
				if (privacy == null) {
					privacy = new AccettazioneInformativaGeneraleEntita();				
					privacy.setPersona(personaEntity);
				}

				privacy.setDataAccettazione(ldtPrivacy.toLocalDate());
				privacy.setDataProtocollazione(ldtPrivacy.toLocalDate());
				privacy.setNumeroProtocollazione(persona.getNrProtocolloPrivacyGenerale());
				accettazioneRepository.save(privacy);
				persona.setNrProtocolloPrivacyGenerale(privacy.getNumeroProtocollazione());
			}
		} catch (Exception e) {
			logger.error("Impossibile aggiornare l'entity Persona", e);
			throw e;
		}
		return persona;
	}

	@Override
	@Transactional
	public Long inserisciAggiornaPersona(Persona persona) throws Exception {
		PersonaEntita personaEntity = new PersonaEntita();
		personaEntity.setCodiceFiscale(persona.getCodiceFiscale());

		Optional<PersonaEntita> personaeEsistente = personaRepository.findOne(Example.of(personaEntity));

		if (personaeEsistente.isPresent()) {
			persona.setId(personaeEsistente.get().getId());
			return aggiornaPersona(persona).getId();
		} else {
			return inserisciPersona(persona);
		}
	}
}
