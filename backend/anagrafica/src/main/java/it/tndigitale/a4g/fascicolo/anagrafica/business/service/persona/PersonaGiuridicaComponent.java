package it.tndigitale.a4g.fascicolo.anagrafica.business.service.persona;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.IscrizioneSezioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaGiuridicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.AttivitaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAnagraficiDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.DatiAperturaFascicoloDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.IndirizzoDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.IscrizioneRepertorioEconomicoDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.IscrizioneSezioneConverter;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.IscrizioneSezioneDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.PersonaGiuridicaDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.RappresentanteLegaleDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.SedeDto;

@Component
public class PersonaGiuridicaComponent implements IPersonaComponent<PersonaGiuridicaDto> {
	private static final Logger logger = LoggerFactory.getLogger(PersonaGiuridicaComponent.class);

	@Autowired
	private PersonaGiuridicaDao personaGiuridicaDao;
	@Autowired
	private FascicoloDao fascicoloDao;
	@Autowired
	protected IscrizioneSezioneConverter iscrizioneSezioneConverter;

	@Override
	public DatiAperturaFascicoloDto getDatiAnagraficiSintesi(String codiceFiscale, final Integer idValidazione) {
		DatiAperturaFascicoloDto dati = new DatiAperturaFascicoloDto();
		Optional<FascicoloModel> fascicoloOpt = fascicoloDao.findByCuaaAndIdValidazione(codiceFiscale, idValidazione);
		Optional<PersonaGiuridicaModel> personaGiuridicaOpt = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(codiceFiscale, idValidazione);
		if (!personaGiuridicaOpt.isPresent()) {
			return null;
		}
		PersonaGiuridicaModel personaGiuridica = personaGiuridicaOpt.get();
		if (personaGiuridica == null) {
			String msgErr = String.format("Nessuna persona giuridica trovata con codice fiscale %s" , codiceFiscale);
			logger.error(msgErr);
			throw new EntityNotFoundException(msgErr);  
		}
		if (fascicoloOpt.isPresent() && fascicoloOpt.get().getDenominazione() != null) {
			dati.setDenominazioneFascicolo(fascicoloOpt.get().getDenominazione());
		}
		dati.setCodiceFiscale(personaGiuridica.getCodiceFiscale());
		dati.setDenominazione(personaGiuridica.getDenominazione());
		dati.setNaturaGiuridica(personaGiuridica.getFormaGiuridica());
		dati.setPartitaIva(personaGiuridica.getPartitaIVA());

		DatiAnagraficiDto anag = new DatiAnagraficiDto();
		anag.setNominativo(personaGiuridica.getNominativoRappresentanteLegale());
		anag.setCodiceFiscale(personaGiuridica.getCodiceFiscaleRappresentanteLegale());
		anag.setPec(personaGiuridica.getPec());
		dati.setDatiAnagraficiRappresentante(anag);

		IndirizzoDto ubicazione = new IndirizzoDto();
		if (personaGiuridica.getSedeLegale() != null && personaGiuridica.getSedeLegale().getIndirizzo() != null) {
			ubicazione.setCap(personaGiuridica.getSedeLegale().getIndirizzo().getCap());
			ubicazione.setComune(personaGiuridica.getSedeLegale().getIndirizzo().getComune());
			ubicazione.setToponimo(personaGiuridica.getSedeLegale().getIndirizzo().getDescrizioneEstesa());
			ubicazione.setLocalita(personaGiuridica.getSedeLegale().getIndirizzo().getFrazione());
			ubicazione.setProvincia(personaGiuridica.getSedeLegale().getIndirizzo().getProvincia());
		}
		dati.setUbicazioneDitta(ubicazione);
		return dati;
	}

	@Override
	public PersonaGiuridicaDto getDatiPersona(String codiceFiscale, final Integer idValidazione) {
		Optional<PersonaGiuridicaModel> personaGOpt = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(codiceFiscale, idValidazione);
		if (!personaGOpt.isPresent()) {
			return null;
		}
		PersonaGiuridicaModel personaG = personaGOpt.get();
		PersonaGiuridicaDto personaGiuridicaDto = new PersonaGiuridicaDto();

		// Anagrafe Tributaria
		// Dati identificativi persona giuridica
		personaGiuridicaDto.setCodiceFiscale(personaG.getCodiceFiscale());
		personaGiuridicaDto.setPartitaIva(personaG.getPartitaIVA());
		personaGiuridicaDto.setDenominazione(personaG.getDenominazione());
		personaGiuridicaDto.setFormaGiuridica(personaG.getFormaGiuridica());

		// Rappresentante legale
		if (personaG.getCodiceFiscaleRappresentanteLegale() != null && personaG.getNominativoRappresentanteLegale() != null 
				&& !personaG.getNominativoRappresentanteLegale().isEmpty()) {
			RappresentanteLegaleDto rlDto = new RappresentanteLegaleDto();
			rlDto.setCodiceFiscale(personaG.getCodiceFiscaleRappresentanteLegale());
			rlDto.setNominativo(personaG.getNominativoRappresentanteLegale());
			personaGiuridicaDto.setRappresentanteLegale(rlDto);
		}

		if (!personaG.getSedeLegale().isEmpty()) {
			SedeDto sedeDto = new SedeDto();
			// Sede legale - Anagrafe Tributaria
			if (personaG.getSedeLegale().getIndirizzo() != null) {
				IndirizzoDto indirizzo = new IndirizzoDto();
				indirizzo.setToponimo(personaG.getSedeLegale().getIndirizzo().getDescrizioneEstesa());
				indirizzo.setComune(personaG.getSedeLegale().getIndirizzo().getComune());
				indirizzo.setCap(personaG.getSedeLegale().getIndirizzo().getCap());
				indirizzo.setProvincia(personaG.getSedeLegale().getIndirizzo().getProvincia());
				sedeDto.setIndirizzo(indirizzo);
			}
			// Iscrizione Camera Di Commercio Della Sede Legale
			if (personaG.getSedeLegale().getIscrizioneRegistroImprese() != null) {
				IscrizioneRepertorioEconomicoDto ireDto = new IscrizioneRepertorioEconomicoDto();
				ireDto.setDataIscrizione(personaG.getSedeLegale().getIscrizioneRegistroImprese().getDataIscrizione());
				ireDto.setCodiceRea(personaG.getSedeLegale().getIscrizioneRegistroImprese().getNumeroRepertorioEconomicoAmministrativo());
				ireDto.setProvinciaRea(personaG.getSedeLegale().getIscrizioneRegistroImprese().getProvinciaCameraCommercio());
				ireDto.setCessata(personaG.getSedeLegale().getIscrizioneRegistroImprese().getCessata());
				sedeDto.setIscrizioneRegistroImprese(ireDto);
			}
			// Sede legale - Camera Di Commercio
			if (personaG.getSedeLegale().getIndirizzoCameraCommercio() != null) {
				IndirizzoDto indirizzo = new IndirizzoDto();
				indirizzo.setToponimo(personaG.getSedeLegale().getIndirizzoCameraCommercio().getToponimo());
				indirizzo.setVia(personaG.getSedeLegale().getIndirizzoCameraCommercio().getVia());
				indirizzo.setCivico(personaG.getSedeLegale().getIndirizzoCameraCommercio().getNumeroCivico());
				indirizzo.setComune(personaG.getSedeLegale().getIndirizzoCameraCommercio().getComune());
				indirizzo.setCap(personaG.getSedeLegale().getIndirizzoCameraCommercio().getCap());
				indirizzo.setCodiceIstat(personaG.getSedeLegale().getIndirizzoCameraCommercio().getCodiceIstat());
				indirizzo.setFrazione(personaG.getSedeLegale().getIndirizzoCameraCommercio().getFrazione());
				indirizzo.setProvincia(personaG.getSedeLegale().getIndirizzoCameraCommercio().getProvincia());
				sedeDto.setIndirizzoCameraCommercio(indirizzo);
			}

			// Attività Ateco
			if ( personaG.getSedeLegale().getAttivita() != null && !personaG.getSedeLegale().getAttivita().isEmpty()) {
				List<AttivitaDto> atecoDtos = new ArrayList<>();
				personaG.getSedeLegale().getAttivita().forEach(ateco -> {
					AttivitaDto att = new AttivitaDto();
					att.setCodice(ateco.getCodice());
					att.setDescrizione(ateco.getDescrizione());
					att.setImportanza(ateco.getImportanza());
					att.setFonteDato(ateco.getFonte());
					atecoDtos.add(att);
				});
				sedeDto.setAttivitaAteco(atecoDtos);
			}
			sedeDto.setTelefono(personaG.getSedeLegale().getTelefono());
			sedeDto.setIndirizzoPec(personaG.getSedeLegale().getPec());
			personaGiuridicaDto.setSedeLegale(sedeDto);
		}

		// Camera Di Commercio
		// Dati identificativi persona giuridica
		personaGiuridicaDto.setOggettoSociale(personaG.getOggettoSociale());
		personaGiuridicaDto.setDataCostituzione(personaG.getDataCostituzione());
		personaGiuridicaDto.setDataTermine(personaG.getDataTermine());
		personaGiuridicaDto.setCapitaleSocialeDeliberato(personaG.getCapitaleSocialeDeliberato());
		List<IscrizioneSezioneModel> iscrizioniSezioneModel = personaG.getIscrizioniSezione();
		List<IscrizioneSezioneDto> iscrizioniSezioneDto = iscrizioniSezioneModel.stream().map(is -> iscrizioneSezioneConverter.convert(is)).collect(Collectors.toList());
		personaGiuridicaDto.setIscrizioniSezione(iscrizioniSezioneDto);

		return personaGiuridicaDto;
	}
}
