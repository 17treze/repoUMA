package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.Ruoli;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.RevocaImmediataModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.StatoRevocaImmediata;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaFisicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.PersonaGiuridicaDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.RevocaImmediataDao;
import it.tndigitale.a4g.fascicolo.anagrafica.business.service.CaaService;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.DescrizioneRichiestaRevocaImmediataMandatoDto;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.RichiestaRevocaImmediataDto;
import it.tndigitale.a4g.framework.client.custom.DocumentDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto;
import it.tndigitale.a4g.framework.client.custom.MetadatiDto.TipologiaDocumentoPrincipale;
import it.tndigitale.a4g.framework.client.custom.MittenteDto;
import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.support.PersonaSelector;

@Service
public class RevocaImmediataMandatoService {
	@Autowired
	private PersonaFisicaDao personaFisicaDao;
	@Autowired
	private PersonaGiuridicaDao personaGiuridicaDao;
	@Autowired
	RevocaImmediataDao revocaImmediataDao;
	@Autowired
	RichiestaRevocaImmediataConverter richiestaRevocaImmediataConverter;
	@Autowired
	UtenteComponent utenteComponent;
	@Autowired
	CaaService caaService;
	
	private static final String NOME_FILE = "RichiestaRevocaImmediataMandato";
	private static final String PREFISSO_OGGETTO_PROTOCOLLO = "A4G - RICHIESTA REVOCA IMMEDIATA MANDATO - ";
	
	public byte[] getDocumentoRichiestaFirmato(final String idProtocollo) {
		Optional<RevocaImmediataModel> revocaImmediataOptional = revocaImmediataDao.findByIdProtocollo(idProtocollo);
		if (!revocaImmediataOptional.isPresent()) {
			return null;
		}
		RevocaImmediataModel revocaImmediataModel = revocaImmediataOptional.get();
		return revocaImmediataModel.getRichiestaFirmata();
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateRevocaImmediataConProtocollo(RichiestaRevocaImmediataDto data, String numeroProtocollo) {
		RevocaImmediataModel revocaImmediataModel = revocaImmediataDao.getOne(data.getId());
		revocaImmediataModel.setIdProtocollo(numeroProtocollo);
		revocaImmediataModel.setDtProtocollo(LocalDate.now());
		revocaImmediataModel.setRichiestaFirmata(data.getRichiestaRevocaImmediataFirmata().getByteArray());
		revocaImmediataDao.save(revocaImmediataModel);
	}
	
	
	public List<DescrizioneRichiestaRevocaImmediataMandatoDto> getRichiesteRevocaImmediataDto(final boolean isValutata) throws Exception {
		Paginazione paginazione = Paginazione.of().setNumeroElementiPagina(1000).setPagina(0);
		RisultatiPaginati<DescrizioneRichiestaRevocaImmediataMandatoDto> pagedResults = getRichiesteRevocaImmediataDto(isValutata, paginazione, null);
		return pagedResults.getRisultati();
	}
	
	public RisultatiPaginati<DescrizioneRichiestaRevocaImmediataMandatoDto> getRichiesteRevocaImmediataDto(
			final boolean isValutata, final Paginazione paginazione, final Ordinamento ordinamento) throws Exception {
		List<RevocaImmediataModel> findAll;
		long count;
		Pageable pageable = PageableBuilder.build().from(
				Paginazione.getOrDefault(paginazione), Ordinamento.getOrDefault(ordinamento));
		if (utenteComponent.haUnRuolo(Ruoli.CRUSCOTTO_REVOCA_IMMEDIATA_MANDATO_RICHIESTE_VALUTATE_TUTTI)) {
			if (isValutata) {
				findAll = revocaImmediataDao.findByStatoNot(StatoRevocaImmediata.DA_VALUTARE, pageable);
				count = revocaImmediataDao.countByStatoNot(StatoRevocaImmediata.DA_VALUTARE);
			} else {
				findAll = revocaImmediataDao.findByStato(StatoRevocaImmediata.DA_VALUTARE, pageable);
				count = revocaImmediataDao.countByStato(StatoRevocaImmediata.DA_VALUTARE);
			}
		} else {
			List<Long> sportelliAbilitati = caaService.getSportelliAbilitatiCaa().stream().map(Long::valueOf).collect(Collectors.toList());
			if (isValutata) {
				findAll = revocaImmediataDao.findByStatoNotAndMandatoSportelloIdentificativoIn(StatoRevocaImmediata.DA_VALUTARE, sportelliAbilitati, pageable);
				count = revocaImmediataDao.countByStatoNotAndMandatoSportelloIdentificativoIn(StatoRevocaImmediata.DA_VALUTARE, sportelliAbilitati);
			} else {
				findAll = revocaImmediataDao.findByStatoAndMandatoSportelloIdentificativoIn(StatoRevocaImmediata.DA_VALUTARE, sportelliAbilitati, pageable);
				count = revocaImmediataDao.countByStatoAndMandatoSportelloIdentificativoIn(StatoRevocaImmediata.DA_VALUTARE, sportelliAbilitati);
			}
		}
		List<DescrizioneRichiestaRevocaImmediataMandatoDto> result = richiestaRevocaImmediataConverter.convertList(findAll);
		return RisultatiPaginati.of(result, count);			
	}

	@Transactional
	public DocumentDto getDocumentDto(RichiestaRevocaImmediataDto richiestaRevocaImmediataDto, final Integer idValidazione) throws NoSuchElementException {
		MetadatiDto metadati = new MetadatiDto();
		// PERSONA FISICA
		if (PersonaSelector.isPersonaFisica(richiestaRevocaImmediataDto.getCodiceFiscale())) {
			PersonaFisicaModel persona = personaFisicaDao.findByCodiceFiscaleAndIdValidazione(richiestaRevocaImmediataDto.getCodiceFiscale(), 0).orElseThrow();
			MittenteDto mittenteDto = buildMittentePersonaFisica(persona);
			metadati.setOggetto(PREFISSO_OGGETTO_PROTOCOLLO + richiestaRevocaImmediataDto.getCodiceFiscale()
					+ " - " + persona.getNome() + " " + persona.getCognome());
			metadati.setMittente(mittenteDto);
		} else {
			Optional<PersonaGiuridicaModel> personaOpt = personaGiuridicaDao.findByCodiceFiscaleAndIdValidazione(richiestaRevocaImmediataDto.getCodiceFiscale(), 0);
			if (!personaOpt.isPresent()) {
				throw new IllegalArgumentException("Codice Fiscale di persona giuridica inserito non valido");
			}
			PersonaGiuridicaModel persona = personaOpt.get();
			MittenteDto mittenteDto = buildMittentePersonaGiuridica(persona);
			metadati.setOggetto(PREFISSO_OGGETTO_PROTOCOLLO + richiestaRevocaImmediataDto.getCodiceFiscale()
					+ " - " + persona.getDenominazione());
			metadati.setMittente(mittenteDto);
		}

		metadati.setTipologiaDocumentoPrincipale(TipologiaDocumentoPrincipale.MANDATO_REVOCA_IMMEDIATA);

		// SET MODULO RICHIESTA REVOCA IMMEDIATA
		ByteArrayResource documentoByteAsResource = new ByteArrayResource(
				richiestaRevocaImmediataDto.getRichiestaRevocaImmediataFirmata().getByteArray()) {
			@Override
			public String getFilename() {
				return NOME_FILE + ".pdf";
			}
		};

		// SET ALLEGATI
		List<ByteArrayResource> allegatiConv = new ArrayList<ByteArrayResource>();
		if (!CollectionUtils.isEmpty(richiestaRevocaImmediataDto.getAllegati())) {
			allegatiConv.addAll(richiestaRevocaImmediataDto.getAllegati());
		}

		return buildDocumentDto(metadati, documentoByteAsResource, allegatiConv);
	}

	private DocumentDto buildDocumentDto(MetadatiDto metadati, ByteArrayResource documento,
			List<ByteArrayResource> allegati) {
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
			mittente.setDescription(
					persona.getCodiceFiscale() + " - " + persona.getNome() + " " + persona.getCognome());
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

    public static boolean isRevocaInCorso(MandatoModel mandato) {
    	List<RevocaImmediataModel> revoche = mandato.getRevocheImmediate();
		for (RevocaImmediataModel revoca : revoche) {
			StatoRevocaImmediata stato = revoca.getStato();
			if (stato.equals(StatoRevocaImmediata.DA_VALUTARE)) {
				return true;
			}
		}
		return false;
	}
}
