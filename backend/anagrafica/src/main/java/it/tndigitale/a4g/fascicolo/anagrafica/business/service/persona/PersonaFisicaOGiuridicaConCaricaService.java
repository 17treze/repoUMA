package it.tndigitale.a4g.fascicolo.anagrafica.business.service.persona;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.*;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.builder.persona.IndirizzoDtoBuilder;
import it.tndigitale.a4g.proxy.client.model.AnagraficaDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.CaricaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.DocumentoIdentitaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.EredeDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaFisicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaGiuridicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.legacy.CaricheRappresentanteLegaleMap;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.PersonaConCaricaService;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.StatusMessagesEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.fascicolo.DocumentoIdentitaInvalidoException;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.DocumentoIdentitaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.PersoneFisicheConCaricaExtDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.CaricaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaFisicaConCaricaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaGiuridicaConCaricaDto;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.support.CustomCollectors;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Objects;
import org.springframework.beans.BeanUtils;

@Service
public class PersonaFisicaOGiuridicaConCaricaService {
	
	private static final Logger log = LoggerFactory.getLogger(PersonaFisicaOGiuridicaConCaricaService.class);
	
	@Autowired private PersonaGiuridicaDao personaGiuridicaDao;
	@Autowired private PersonaFisicaDao personaFisicaDao;
	@Autowired private CaricaDao caricaDao;
	@Autowired private FascicoloDao fascicoloDao;
	@Autowired private DocumentoIdentitaDao documentoIdentitaDao;
	@Autowired private EredeDao eredeDao;
	@Autowired private PersonaConCaricaService personaConCaricaService;
	
    public List<PersonaFisicaConCaricaDto> getPersonaFisicaConCarica(final String cuaa, final Integer idValidazione, boolean completa) {
        Optional<? extends PersonaModel> persona;

        switch(cuaa.length()) {
            case 11:
                persona = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(cuaa, idValidazione); break;
            case 16:
                persona = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(cuaa, idValidazione); break;
            default:
                throw new AssertionError();
        }

        if(persona.isEmpty()) {
            return List.of();
        }

        var personeConCariche = persona
            .get()
            .getCariche()
            .stream()
            .filter(carica -> carica.getPersonaFisicaConCaricaModel() != null)
            .collect(Collectors.groupingBy(CaricaModel::getPersonaFisicaConCaricaModel, LinkedHashMap::new, Collectors.toList()));
        
        var persone = personeConCariche.keySet();
        
        if(completa && isIncompleto(persone)) {
            completa(cuaa, persone);
        }
        
        return personeConCariche
            .entrySet()
            .stream()
            .map(entry -> {
                var personaModel = entry.getKey();
                var caricheModel = entry.getValue();
                
                var personaDto = PersonaFisicaConCaricaDto.toDto(personaModel);
                var caricheDto = caricheModel
                    .stream()
                    .map(CaricaDto::toDto)
                    .collect(Collectors.toList());
                
                personaDto.setCariche(caricheDto);
                
                return personaDto;
            })
            .collect(Collectors.toList());
    }
    
    private static boolean isIncompleto(Collection<PersonaFisicaConCaricaModel> persone) {
        return persone
            .stream()
            .map(PersonaFisicaConCaricaModel::getNome)
            .anyMatch(Objects::isNull);
    }
    private void completa(String cuaa, Collection<PersonaFisicaConCaricaModel> persone) {
        var fascicoloId = fascicoloDao
            .findByCuaaAndIdValidazione(cuaa, 0)
            .get()
            .getId();
        
        var idPersona = persone
            .stream()
            .collect(Collectors.groupingBy(PersonaFisicaConCaricaModel::getId, CustomCollectors.toSingleton()));
        
        var personeIds = idPersona.keySet();

        personaConCaricaService
            .persistPersoneFisicheConCarica(new PersoneFisicheConCaricaExtDto(fascicoloId, personeIds))
            .forEach(personaCompleta -> {
                var personaIncompleta = idPersona.get(personaCompleta.getId());
                
                BeanUtils.copyProperties(personaCompleta, personaIncompleta);
            });
    }
	
	public List<PersonaFisicaConCaricaDto> getPossibiliRappresentantiLegali(final String cuaa) {
		List<PersonaFisicaConCaricaDto> personeFisicheConCaricaList = getPersonaFisicaConCarica(cuaa, 0, false);
		Set<String> identificativiCaricheValide = CaricheRappresentanteLegaleMap.getCaricheRappresentantiLegali().keySet();
		List<PersonaFisicaConCaricaDto> personeConCarica = personeFisicheConCaricaList.stream().filter(
				pf -> pf.getCariche().stream().anyMatch(
						c -> identificativiCaricheValide.contains(c.getIdentificativo()))).collect(Collectors.toList());
		Optional<PersonaGiuridicaModel> aziendaOpt = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(cuaa, 0);
		if (aziendaOpt.isPresent()) {
			PersonaGiuridicaModel azienda = aziendaOpt.get();
			String cfRapprLeg = azienda.getCodiceFiscaleRappresentanteLegale();
			// qui devo verificare se è già nella lista
			for (PersonaFisicaConCaricaDto pf : personeConCarica) {
				if (pf.getCodiceFiscale().equals(cfRapprLeg)) {
					return personeConCarica;
				}
			}
			// altrimenti lo aggiungo
			//PersonaFisicaDto rappLeg = personaService.getPersonaFisica(cfRapprLeg, 0); // sbagliato?
			//azienda.getCariche().get(0).getPersonaFisicaConCaricaModel();
			PersonaFisicaConCaricaModel firmatario = null;
			for (var carica : azienda.getCariche()) {
				PersonaFisicaConCaricaModel pf = carica.getPersonaFisicaConCaricaModel();
				if (pf != null && pf.getCodiceFiscale().equals(cfRapprLeg)) {
					firmatario = pf;
					PersonaFisicaConCaricaDto rl = new PersonaFisicaConCaricaDto();
					rl.setNome(pf.getNome());
					rl.setCognome(pf.getCognome());
					rl.setCodiceFiscale(pf.getCodiceFiscale());
					personeConCarica.add(rl);
					break;
				}
			}
			if (firmatario == null) {
				log.warn("Il codice fiscale '{}' indicato da anagrafe tributaria come rappresentante legale non è presente nel database delle persone fisiche",
						cfRapprLeg);
				PersonaFisicaConCaricaDto rlAt = new PersonaFisicaConCaricaDto();
				rlAt.setNome(azienda.getNominativoRappresentanteLegale());
				rlAt.setCognome("");
				rlAt.setCodiceFiscale(cfRapprLeg);
				personeConCarica.add(rlAt);
			}
		}
//		caso con persona fisica non iscritta in Parix. In tal caso il RL e' se stesso
		if (personeConCarica == null || personeConCarica.isEmpty()) {
			Optional<PersonaFisicaModel> pfOpt = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(cuaa, 0);
			if (!pfOpt.isPresent()) {
				return personeConCarica;
			}
			PersonaFisicaModel pf = pfOpt.get();
			IndirizzoDtoBuilder indirizzoDtoBuilder = new IndirizzoDtoBuilder();
			PersonaFisicaConCaricaDto dto = new PersonaFisicaConCaricaDto();
			dto.setCodiceFiscale(pf.getCodiceFiscale());
			dto.setNome(pf.getNome());
			dto.setCognome(pf.getCognome());
			Sesso sessoEnum = pf.getSesso();
			if (sessoEnum != null) {
				dto.setSesso(AnagraficaDto.SessoEnum.fromValue(sessoEnum.name()));
			}
			dto.setComuneNascita(pf.getComuneNascita());
			dto.setSiglaProvinciaNascita(pf.getProvinciaNascita());
			dto.setDataNascita(pf.getDataNascita());
			dto.setDomicilioFiscale(indirizzoDtoBuilder.withIndirizzo(pf.getDomicilioFiscale()).build());
			dto.setCariche(new ArrayList<CaricaDto>());
			personeConCarica.add(dto);
		}
		return personeConCarica;
	}
	
	public List<PersonaGiuridicaConCaricaDto> getPersoneGiuridicheConCarica(final String cuaa, final Integer idValidazione) {
		List<PersonaGiuridicaConCaricaDto> pgConCaricheDto = new ArrayList<>();
		List<CaricaModel> caricheInAzienda;
		if (cuaa.length() == 16) {
			Optional<PersonaFisicaModel> aziendaOpt = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(cuaa, idValidazione);
			if (!aziendaOpt.isPresent()) {
				return pgConCaricheDto;
			}
			PersonaFisicaModel azienda = aziendaOpt.get();
			caricheInAzienda = azienda.getCariche();
		}
		else {
			Optional<PersonaGiuridicaModel> aziendaOpt = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(cuaa, idValidazione);
			if (!aziendaOpt.isPresent()) {
				return pgConCaricheDto;
			}
			PersonaGiuridicaModel azienda = aziendaOpt.get();
			caricheInAzienda = azienda.getCariche();
		}
		for (CaricaModel caricaInAzienda : caricheInAzienda) {
			PersonaGiuridicaConCaricaModel pgConCaricaModel = caricaInAzienda.getPersonaGiuridicaConCaricaModel();
			if (pgConCaricaModel == null) {
				continue;
			}
			Optional<PersonaGiuridicaConCaricaDto> pgConCaricaDtoOpt = pgConCaricheDto.stream().filter(
					el -> el.getCodiceFiscale().equals(pgConCaricaModel.getCodiceFiscale())).findAny();
			List<CaricaDto> caricheDto;
			PersonaGiuridicaConCaricaDto pgConCaricaDto;
			if (pgConCaricaDtoOpt.isPresent()) {
				pgConCaricaDto = pgConCaricaDtoOpt.get();
				caricheDto = pgConCaricaDto.getCariche();
			} else {
				caricheDto = new ArrayList<>();
				pgConCaricaDto = PersonaGiuridicaConCaricaDto.toDto(pgConCaricaModel);
				pgConCaricaDto.setCariche(caricheDto);
				pgConCaricheDto.add(pgConCaricaDto);
			}
			caricheDto.add(CaricaDto.toDto(caricaInAzienda));
		}
		return pgConCaricheDto;
	}
	
	/**
	 * 
	 * @param cuaa: cuaa azienda
	 * @param documentoIdentitaFirmatarioDto: documento di identita del firmatario
	 * @throws EntityNotFoundException
	 * @throws DocumentoIdentitaInvalidoException 
	 */
	@Transactional
    public void salvaFirmatario(final String cuaa, final DocumentoIdentitaDto documentoIdentitaFirmatarioDto) throws EntityNotFoundException, DocumentoIdentitaInvalidoException {
		// controlli validita' del documento di identita'
		checkValiditaDocumentoIdentita(documentoIdentitaFirmatarioDto);

		var codiceFiscaleFirmatario = documentoIdentitaFirmatarioDto.getCodiceFiscale();
		
		Optional<FascicoloModel> fascicoloModelOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		FascicoloModel fascicoloModel = fascicoloModelOpt.orElseThrow(() -> new EntityNotFoundException(
				String.format("%s: %s", StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name(), cuaa)));
		
		if(iSPersonaGiuridicaOrPersonaFisicaNonDeceduta(fascicoloModel)) {
			Set<String> identificativiCaricheValide = CaricheRappresentanteLegaleMap.getCaricheRappresentantiLegali().keySet();
			List<CaricaModel> caricheList = new ArrayList<CaricaModel>();
			String codiceFiscaleFirmatarioFound = new String();
			if (cuaa.length() == 16) {
				Optional<PersonaFisicaModel> aziendaOpt = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(cuaa, 0);
				if (aziendaOpt.isPresent()) {
					PersonaFisicaModel personaFisicaModel = aziendaOpt.get();
					caricheList = caricaDao.findByPersonaFisicaModel(personaFisicaModel);
					//				rappresentante legale è il titolare del fascicolo
					codiceFiscaleFirmatarioFound = personaFisicaModel.getCodiceFiscale();
				}
			} else {
				Optional<PersonaGiuridicaModel> aziendaOpt = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(cuaa, 0);
				if (aziendaOpt.isPresent()) {
					PersonaGiuridicaModel personaGiuridicaModel = aziendaOpt.get();
					caricheList = caricaDao.findByPersonaGiuridicaModel(personaGiuridicaModel);
					//				rappresentante legale da AT
					codiceFiscaleFirmatarioFound = personaGiuridicaModel.getCodiceFiscaleRappresentanteLegale();
				}
			}

			//			deve mettere il firmatario a true per la persona fisica in input, se ha una carica idonea, 
			//			a false per tutte le altre persone fisiche con carica della persona giuridica
			for (CaricaModel caricaModel : caricheList) {
				//				le cariche generalmente sono associate a persone fisiche o giuridiche. 
				//				Nel caso in cui caricaModel.getPersonaFisicaConCaricaModel() == null significa che la carica
				//				e' associata ad una persona giuridica
				PersonaFisicaConCaricaModel personaFisicaConCaricaModel = caricaModel.getPersonaFisicaConCaricaModel();
				if (personaFisicaConCaricaModel != null) {
					boolean isFirmatario = personaFisicaConCaricaModel.getCodiceFiscale().equals(codiceFiscaleFirmatario)
							&& identificativiCaricheValide.contains(caricaModel.getIdentificativo());
					caricaModel.setFirmatario(isFirmatario);
					if (isFirmatario) {
						//						se si trova una carica salvata che e' firmatario, esso ha priorità su AT 
						codiceFiscaleFirmatarioFound = personaFisicaConCaricaModel.getCodiceFiscale(); 
					}
				}
			}
			//			A questo punto esistono 2 possibilita':
			//			- caricheList non ha alcun firmatario o non ci sono cariche (ad es: non è più iscritto a Parix); quindi è quello di AT
			//			- caricheList ha un firmatario
			if (!codiceFiscaleFirmatarioFound.equalsIgnoreCase(documentoIdentitaFirmatarioDto.getCodiceFiscale())) {
				throw new DocumentoIdentitaInvalidoException("Il codice fiscale del firmatario scelto non coincide con quello fornito insieme al documento di identita");
			}
			caricaDao.saveAll(caricheList);
			
			fascicoloModel.setStato(StatoFascicoloEnum.IN_AGGIORNAMENTO);
		} else {
			List<EredeModel> eredi = eredeDao.findByFascicolo_Cuaa(cuaa);
			List<EredeModel> eredeFirmatarioOld = eredi.stream().filter(x -> x.isFirmatario().booleanValue()).collect(Collectors.toList());
			eredeFirmatarioOld.forEach(e -> {
				e.setFirmatario(false);
				eredeDao.save(e);
			});

			Optional<EredeModel> erede = eredeDao.findByFascicolo_CuaaAndPersonaFisica_CodiceFiscale(cuaa, documentoIdentitaFirmatarioDto.getCodiceFiscale());
			if (!erede.isPresent()) {
				log.error("Erede non trovato!");
				throw new IllegalArgumentException("Erede non trovato!");
			} else {
				EredeModel eredeFirmatarioNew = erede.get();
				eredeFirmatarioNew.setFirmatario(true);
				eredeDao.save(eredeFirmatarioNew);
			}
		}
		
		DocumentoIdentitaModel documentoIdentitaModelOld = fascicoloModel.getDocumentoIdentita();
		if (documentoIdentitaModelOld != null) {
//				cancellare le vecchie informazioni
			documentoIdentitaDao.deleteById(new EntitaDominioFascicoloId(documentoIdentitaModelOld.getId(), documentoIdentitaModelOld.getIdValidazione()));
			fascicoloModel.setDocumentoIdentita(null);
		}
//			salvataggio dati documento di identita'. Le informazioni di protocollazione verranno salvate in modo asincrono 
		DocumentoIdentitaModel documentoIdentitaModel = new DocumentoIdentitaModel();
		documentoIdentitaModel.setCodiceFiscale(documentoIdentitaFirmatarioDto.getCodiceFiscale());
		documentoIdentitaModel.setDataRilascio(documentoIdentitaFirmatarioDto.getDataRilascio());
		documentoIdentitaModel.setDataScadenza(documentoIdentitaFirmatarioDto.getDataScadenza());
		documentoIdentitaModel.setDocumento(documentoIdentitaFirmatarioDto.getDocumento());
		documentoIdentitaModel.setNumero(documentoIdentitaFirmatarioDto.getNumeroDocumento());
		documentoIdentitaModel.setTipologia(documentoIdentitaFirmatarioDto.getTipoDocumento().name());
		documentoIdentitaModel.setFascicolo(fascicoloModel);
		DocumentoIdentitaModel documentoIdentitaModelSaved= documentoIdentitaDao.save(documentoIdentitaModel);
		fascicoloModel.setDocumentoIdentita(documentoIdentitaModelSaved);
		
		fascicoloDao.save(fascicoloModel);
    }
	
	public boolean iSPersonaGiuridicaOrPersonaFisicaNonDeceduta(FascicoloModel fascicoloModel) {
		if (fascicoloModel.getPersona() instanceof PersonaFisicaModel) {
			Optional<PersonaFisicaModel> aziendaOpt = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(fascicoloModel.getCuaa(), 0);
			if (!aziendaOpt.isPresent() || aziendaOpt.get().getDeceduto().booleanValue()) {
				return false;
			}
		}
		return true;
	}
	
	@Transactional
	public DocumentoIdentitaDto getDocumentoIdentitaFirmatario(final String cuaa, final Integer idValidazione) {
		return DocumentoIdentitaDto.build(documentoIdentitaDao.findByFascicolo_CuaaAndFascicolo_IdValidazione(cuaa, idValidazione));
	}

	private void checkValiditaDocumentoIdentita(DocumentoIdentitaDto documentoIdentitaFirmatarioDto) throws DocumentoIdentitaInvalidoException {
		if (documentoIdentitaFirmatarioDto == null) {
			throw new EntityNotFoundException("Documento di identita' non fornito");	
		}
		
		if (StringUtils.isBlank(documentoIdentitaFirmatarioDto.getCodiceFiscale()) || StringUtils.isBlank(documentoIdentitaFirmatarioDto.getNumeroDocumento()) || 
				documentoIdentitaFirmatarioDto.getTipoDocumento() == null || documentoIdentitaFirmatarioDto.getDocumento() == null || 
				documentoIdentitaFirmatarioDto.getDataRilascio() == null || documentoIdentitaFirmatarioDto.getDataScadenza() == null) {
			throw new DocumentoIdentitaInvalidoException();
		}
		
		LocalDate dataRilascio = documentoIdentitaFirmatarioDto.getDataRilascio();
		LocalDate dataScadenza = documentoIdentitaFirmatarioDto.getDataScadenza();
		
		if (dataRilascio.isAfter(dataScadenza)) {
			throw new DocumentoIdentitaInvalidoException("La data di rilascio e' successiva alla data di scadenza");	
		}
		
		if (dataScadenza.isBefore(LocalDate.now())) {
			throw new DocumentoIdentitaInvalidoException("La data di scadenza e' antecedente alla data odierna");	
		}
		
		if (dataRilascio.isAfter(LocalDate.now())) {
			throw new DocumentoIdentitaInvalidoException("La data di rilascio e' successiva alla data odierna");	
		}
	}

	public PersonaFisicaConCaricaDto getFirmatario(final String cuaa) throws NoSuchElementException {
		PersonaFisicaConCaricaDto firmatario = new PersonaFisicaConCaricaDto();
		// se il cuaa è di 16 devo restituire il titolare
		if (cuaa.length() == 16) {
			Optional<PersonaFisicaModel> aziendaOpt = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(cuaa, 0);
			if (!aziendaOpt.isPresent()) {
				log.error("Persona fisica non trovata!");
				throw new IllegalArgumentException("Persona fisica non trovata!");
			}
			PersonaFisicaModel personaFisicaModel = aziendaOpt.get();

			Optional<FascicoloModel> fascicoloModelOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
			FascicoloModel fascicoloModel = fascicoloModelOpt.orElseThrow(() -> new EntityNotFoundException(
					String.format("%s: %s", StatusMessagesEnum.FASCICOLO_LOCALE_NON_ESISTENTE.name(), cuaa)));
			if(iSPersonaGiuridicaOrPersonaFisicaNonDeceduta(fascicoloModel)) {
				List<CaricaModel> caricheList = caricaDao.findByPersonaFisicaModelAndFirmatario(personaFisicaModel, true);
				// se la lista restituita è vuota devo restituire il rappr.legale
				if (caricheList.isEmpty()) {
					firmatario.setNome(personaFisicaModel.getNome());
					firmatario.setCognome(personaFisicaModel.getCognome());
					firmatario.setCodiceFiscale(personaFisicaModel.getCodiceFiscale());
				} else {
					// altrimenti devo cercare il firmatario nella lista delle persone con carica
					CaricaModel caricaModel = caricheList.get(0);
					firmatario.setNome(caricaModel.getPersonaFisicaConCaricaModel().getNome());
					firmatario.setCognome(caricaModel.getPersonaFisicaConCaricaModel().getCognome());
					firmatario.setCodiceFiscale(caricaModel.getPersonaFisicaConCaricaModel().getCodiceFiscale());
				}
			} else {
				if (fascicoloModel.getDocumentoIdentita() != null) {
					Optional<PersonaFisicaModel> erede = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(fascicoloModel.getDocumentoIdentita().getCodiceFiscale(), 0);
					if (!erede.isPresent()) {
						log.error("Erede non trovato!");
						throw new IllegalArgumentException("Erede non trovato!");
					} else {
						firmatario.setNome(erede.get().getNome());
						firmatario.setCognome(erede.get().getCognome());
						firmatario.setCodiceFiscale(erede.get().getCodiceFiscale());
					}
				} else {
					firmatario.setNome("");
					firmatario.setCognome("");
					firmatario.setCodiceFiscale("");
				}
			}
		} else {
			Optional<PersonaGiuridicaModel> aziendaOpt = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(cuaa, 0);
			if (!aziendaOpt.isPresent()) {
				log.error("Persona giuridica non trovata!");
				throw new IllegalArgumentException("Persona giuridica non trovata!");
			}
			PersonaGiuridicaModel personaGiuridicaModel = aziendaOpt.get();
			List<CaricaModel> caricheList = caricaDao.findByPersonaGiuridicaModelAndFirmatario(personaGiuridicaModel, true);
			// se la lista restituita è vuota devo restituire il rappr.legale
			if (caricheList.isEmpty()) {
				firmatario.setNome(personaGiuridicaModel.getNominativoRappresentanteLegale());
				firmatario.setCognome("");
				firmatario.setCodiceFiscale(personaGiuridicaModel.getCodiceFiscaleRappresentanteLegale());
			} else {
				// altrimenti devo cercare il firmatario nella lista delle persone con carica
				CaricaModel caricaModel = caricheList.get(0);
				firmatario.setNome(caricaModel.getPersonaFisicaConCaricaModel().getNome());
				firmatario.setCognome(caricaModel.getPersonaFisicaConCaricaModel().getCognome());
				firmatario.setCodiceFiscale(caricaModel.getPersonaFisicaConCaricaModel().getCodiceFiscale());
			}
		}
		return firmatario;
	}
}
