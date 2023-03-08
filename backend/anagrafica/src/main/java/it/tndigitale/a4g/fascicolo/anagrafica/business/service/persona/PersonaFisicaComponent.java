package it.tndigitale.a4g.fascicolo.anagrafica.business.service.persona;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.IscrizioneSezioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaFisicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.builder.persona.PersonaFisicaBuilder;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.persona.*;
import it.tndigitale.a4g.proxy.client.model.AnagraficaDto.SessoEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PersonaFisicaComponent implements IPersonaComponent<PersonaFisicaDto> {

	private static final Logger logger = LoggerFactory.getLogger(PersonaFisicaComponent.class);
	@Autowired
	private PersonaFisicaDao personaFisicaDao;
	@Autowired
	private FascicoloDao fascicoloDao;
	@Autowired
	protected IscrizioneSezioneConverter iscrizioneSezioneConverter;
	
	@Override
	public DatiAperturaFascicoloDto getDatiAnagraficiSintesi(String codiceFiscale, final Integer idValidazione) throws NoSuchElementException {
		PersonaFisicaModel personaFisica = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(codiceFiscale, idValidazione).orElseThrow();
		Optional<FascicoloModel> fascicoloOpt = fascicoloDao.findByCuaaAndIdValidazione(codiceFiscale, idValidazione);
		return new PersonaFisicaBuilder()
				.withDenominazioneFascicolo(fascicoloOpt.isPresent() ? fascicoloOpt.get() : null)
				.withDatiAnagraficiRappresentante(personaFisica)
				.withDomicilioFiscaleRappresentante(personaFisica.getDomicilioFiscale())
				.withDittaIndividuale(personaFisica.getImpresaIndividuale())
				.build();
	}

	@Override
	public PersonaFisicaDto getDatiPersona(final String codiceFiscale, final Integer idValidazione) throws NoSuchElementException {
		PersonaFisicaModel personaF = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(codiceFiscale, idValidazione).orElseThrow();
		PersonaFisicaDto personaFisicaDto = new PersonaFisicaDto();

		if (personaF == null) {
			String msgErr = String.format("Nessuna persona fisica trovata con codice fiscale %s" , codiceFiscale);
			logger.error(msgErr);
			throw new EntityNotFoundException(msgErr);  
		}

		// Anagrafe Tributaria
		// Dati identificativi persona fisica

		personaFisicaDto.setCodiceFiscale(personaF.getCodiceFiscale());

		AnagraficaDto anag = new AnagraficaDto();
		anag.setComuneNascita(personaF.getComuneNascita());
		anag.setDataNascita(personaF.getDataNascita());
		anag.setNome(personaF.getNome());
		anag.setCognome(personaF.getCognome());
		anag.setProvinciaNascita(personaF.getProvinciaNascita());
		anag.setSesso(SessoEnum.valueOf(personaF.getSesso().name()).name());
		anag.setDeceduto(personaF.getDeceduto());
		anag.setDataMorte(personaF.getDataMorte());
		personaFisicaDto.setAnagrafica(anag);

		// Domicilio fiscale
		if (personaF.getDomicilioFiscale() != null) {
			IndirizzoDto domicilioFiscaleDto = new IndirizzoDto();
			domicilioFiscaleDto.setCap(personaF.getDomicilioFiscale().getCap());
			domicilioFiscaleDto.setComune(personaF.getDomicilioFiscale().getComune());
			domicilioFiscaleDto.setToponimo(personaF.getDomicilioFiscale().getDescrizioneEstesa()); 
			domicilioFiscaleDto.setProvincia(personaF.getDomicilioFiscale().getProvincia());
			personaFisicaDto.setDomicilioFiscale(domicilioFiscaleDto);
		}

		// Dati ditta individuale
		if (personaF.getImpresaIndividuale() != null && !personaF.getImpresaIndividuale().isEmpty()) {
			ImpresaIndividualeDto impresa = new ImpresaIndividualeDto();
			impresa.setDenominazione(personaF.getImpresaIndividuale().getDenominazione());
			impresa.setPartitaIva(personaF.getImpresaIndividuale().getPartitaIVA());
			if (personaF.getImpresaIndividuale().getSedeLegale() != null) {
				SedeDto sede = new SedeDto();
				// Camera di Commercio
				if (personaF.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese() != null) {
					// Dati ditta individuale
					impresa.setFormaGiuridica(personaF.getImpresaIndividuale().getFormaGiuridica()); 
					sede.setIndirizzoPec(personaF.getImpresaIndividuale().getSedeLegale().getPec()); 
					sede.setTelefono(personaF.getImpresaIndividuale().getSedeLegale().getTelefono());

					// Iscrizione camera di commercio della sede legale
					IscrizioneRepertorioEconomicoDto ireDto = new IscrizioneRepertorioEconomicoDto();
					ireDto.setCessata(personaF.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese().getCessata());
					ireDto.setCodiceRea(personaF.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese().getNumeroRepertorioEconomicoAmministrativo());
					ireDto.setDataIscrizione(personaF.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese().getDataIscrizione());
					ireDto.setProvinciaRea(personaF.getImpresaIndividuale().getSedeLegale().getIscrizioneRegistroImprese().getProvinciaCameraCommercio());
					sede.setIscrizioneRegistroImprese(ireDto);
				}

				// Sede legale della ditta individuale - Camera Di Commercio
				if (personaF.getImpresaIndividuale().getSedeLegale().getIndirizzoCameraCommercio() != null) {
					IndirizzoDto indirizzoCC = new IndirizzoDto();
					indirizzoCC.setToponimo(personaF.getImpresaIndividuale().getSedeLegale().getIndirizzoCameraCommercio().getToponimo());
					indirizzoCC.setVia(personaF.getImpresaIndividuale().getSedeLegale().getIndirizzoCameraCommercio().getVia());
					indirizzoCC.setCivico(personaF.getImpresaIndividuale().getSedeLegale().getIndirizzoCameraCommercio().getNumeroCivico());
					indirizzoCC.setComune(personaF.getImpresaIndividuale().getSedeLegale().getIndirizzoCameraCommercio().getComune());
					indirizzoCC.setCap(personaF.getImpresaIndividuale().getSedeLegale().getIndirizzoCameraCommercio().getCap());
					indirizzoCC.setCodiceIstat(personaF.getImpresaIndividuale().getSedeLegale().getIndirizzoCameraCommercio().getCodiceIstat());
					indirizzoCC.setFrazione(personaF.getImpresaIndividuale().getSedeLegale().getIndirizzoCameraCommercio().getFrazione());
					indirizzoCC.setProvincia(personaF.getImpresaIndividuale().getSedeLegale().getIndirizzoCameraCommercio().getProvincia());
					sede.setIndirizzoCameraCommercio(indirizzoCC);
				}

				// Sede legale della ditta individuale - Anagrafe Tributaria
				if (personaF.getImpresaIndividuale().getSedeLegale().getIndirizzo() != null) {
					IndirizzoDto indirizzo = new IndirizzoDto();
					indirizzo.setCap(personaF.getImpresaIndividuale().getSedeLegale().getIndirizzo().getCap());
					indirizzo.setComune(personaF.getImpresaIndividuale().getSedeLegale().getIndirizzo().getComune());
					indirizzo.setProvincia(personaF.getImpresaIndividuale().getSedeLegale().getIndirizzo().getProvincia());
					indirizzo.setToponimo(personaF.getImpresaIndividuale().getSedeLegale().getIndirizzo().getDescrizioneEstesa()); 
					sede.setIndirizzo(indirizzo);
				}


				// AttivitÃ  Ateco
				List<AttivitaDto> atecoDtos = new ArrayList<>();
				personaF.getImpresaIndividuale().getSedeLegale().getAttivita().forEach(ateco -> {
					AttivitaDto att = new AttivitaDto();
					att.setCodice(ateco.getCodice());
					att.setDescrizione(ateco.getDescrizione());
					att.setImportanza(ateco.getImportanza());
					att.setFonteDato(ateco.getFonte());
					atecoDtos.add(att);
				});
				sede.setAttivitaAteco(atecoDtos);
				impresa.setSedeLegale(sede);
			}
			personaFisicaDto.setImpresaIndividuale(impresa);
			List<IscrizioneSezioneModel> iscrizioniSezioneModel = personaF.getIscrizioniSezione();
			List<IscrizioneSezioneDto> iscrizioniSezioneDto = iscrizioniSezioneModel == null ?
					null : iscrizioniSezioneModel.stream().map(is -> iscrizioneSezioneConverter.convert(is)).collect(Collectors.toList());
			personaFisicaDto.setIscrizioniSezione(iscrizioniSezioneDto);
		}
		return personaFisicaDto;
	}
}
