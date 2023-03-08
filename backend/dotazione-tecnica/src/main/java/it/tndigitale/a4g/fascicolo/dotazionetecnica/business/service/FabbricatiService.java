package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.DatiCatastaliModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.FabbricatoModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MappingFabbricatiAgsModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.SerreModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.SottotipoModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.StoccaggioModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.StrumentaliModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.TipologiaModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.DatiCatastaliDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.FabbricatiDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.MappingFabbricatiAgsDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.SottotipoDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.TipoConduzione;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.TipoFabbricato;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.client.DotazioneTecnicaAnagraficaClient;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.exceptions.CatastoEdificialeClasseCatastaleNonAmmessaException;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service.exceptions.CatastoParticellaEstintaException;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DatiCatastaliDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioFabbricatoAbstract;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioFabbricatoDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioSerreDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioStoccaggioDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioStrumentaliDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.FabbricatoDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.TipologiaParticellaCatastale;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.builder.DettaglioFabbricatoDtoBuilder;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.builder.FabbricatoDtoBuilder;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.FabbricatoAgsDto;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.proxy.client.model.DatiClassamentoDto;
import it.tndigitale.a4g.proxy.client.model.InformazioniImmobileDto;
import it.tndigitale.a4g.proxy.client.model.InformazioniParticellaDto;

@Service
public class FabbricatiService {
	private static final Logger logger = LoggerFactory.getLogger(FabbricatiService.class);

	public static final String DATI_CATASTALI_MANCANTI = "Attenzione! Non sono presenti i dati catastali";

	@Autowired
	private FabbricatiDao fabbricatiDao;
	@Autowired
	private FascicoloDao fascicoloDao;
	@Autowired
	private SottotipoDao sottotipoDao;
	@Autowired
	private DatiCatastaliDao datiCatastaliDao;
	@Autowired
	private DotazioneTecnicaAnagraficaClient anagraficaClient;
	@Autowired
	private FabbricatoDtoBuilder fabbricatoBuilder;
	@Autowired
	private MappingFabbricatiAgsDao mappingAgsFabbricatiDao;

	@Autowired
	private DettaglioFabbricatoDtoBuilder dettaglioFabbricatoBuilder;
	@Autowired
	private DotazioneTecnicaAnagraficaClient dotazioneTecnicaAnagraficaClient;

	@Transactional
	public Long postFabbricato(String cuaa, DettaglioFabbricatoAbstract dettaglioFabbricatoDto) throws NullPointerException {
		// Reperisco il fascicolo se esiste
		Optional<FascicoloModel> fascicoloModel = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);

		// Se presente l'id siamo in modifica
		if (dettaglioFabbricatoDto.getId() != null) {
			// Distruggo per ricreare
			cancellaFabbricato(dettaglioFabbricatoDto.getId());
		}

		// Creo il fascicolo se non esiste
		var fascicolo = new FascicoloModel();
		if (fascicoloModel.isEmpty()) {
			var fascicoloNew = new FascicoloModel();
			fascicoloNew.setCuaa(cuaa);
			fascicoloNew.setIdValidazione(0);
			fascicolo = fascicoloDao.save(fascicoloNew);
		} else {
			fascicolo = fascicoloModel.get();
		}

		FabbricatoModel fabbricatoModel = new FabbricatoModel();
		// Verifico che tipo di fabbricato sto inserendo
		if (dettaglioFabbricatoDto instanceof DettaglioStrumentaliDto) {
			// Controllo se la somma delle superficie inserite non sia maggiore del totale
			checkSuperficieTotale.accept((DettaglioStrumentaliDto) dettaglioFabbricatoDto);
			fabbricatoModel = fabbricatiDao.save(buildModel(buildStrumentaliModel((DettaglioStrumentaliDto) dettaglioFabbricatoDto), fascicolo, dettaglioFabbricatoDto));

		} else if (dettaglioFabbricatoDto instanceof DettaglioSerreDto) {
			fabbricatoModel = fabbricatiDao.save(buildModel(buildSerreModel((DettaglioSerreDto) dettaglioFabbricatoDto), fascicolo, dettaglioFabbricatoDto));

		} else if (dettaglioFabbricatoDto instanceof DettaglioStoccaggioDto) {
			fabbricatoModel = fabbricatiDao.save(buildModel(buildStoccaggioModel((DettaglioStoccaggioDto) dettaglioFabbricatoDto), fascicolo, dettaglioFabbricatoDto));

		} else if (dettaglioFabbricatoDto instanceof DettaglioFabbricatoDto) {
			fabbricatoModel = fabbricatiDao.save(buildModel(new FabbricatoModel(), fascicolo, dettaglioFabbricatoDto));
		}

		// salvataggio dati catastali
		if (dettaglioFabbricatoDto.getDatiCatastali() != null) {
			for (DatiCatastaliDto part : dettaglioFabbricatoDto.getDatiCatastali()) {
				DatiCatastaliModel datiCatastaliModel = new DatiCatastaliModel();
				datiCatastaliModel.setCategoria(part.getCategoria());
				datiCatastaliModel.setComune(part.getComune());
				datiCatastaliModel.setConsistenza(part.getConsistenza());
				datiCatastaliModel.setDenominatore(part.getDenominatore());
				datiCatastaliModel.setFabbricato(fabbricatoModel);
				datiCatastaliModel.setFoglio(part.getFoglio());
				datiCatastaliModel.setIndirizzo(part.getIndirizzo());
				datiCatastaliModel.setInTrentino(part.getInTrentino());
				datiCatastaliModel.setNote(part.getNote());
				datiCatastaliModel.setParticella(part.getParticella());
				datiCatastaliModel.setSezione(part.getSezione());
				datiCatastaliModel.setSub(part.getSub());
				datiCatastaliModel.setSuperficie(part.getSuperficie());
				datiCatastaliModel.setTipologia(part.getTipologia());
				if (datiCatastaliModel.getInTrentino() != null && datiCatastaliModel.getInTrentino()) {
					datiCatastaliModel.setEsito(part.getEsito());
				}
				fabbricatoModel.getDatiCatastali().add(datiCatastaliDao.save(datiCatastaliModel));
			}
		} else {
			throw new NullPointerException(DATI_CATASTALI_MANCANTI);
		}

		// Aggiornamento fascicolo in anagrafica
		anagraficaClient.putFascicoloStatoControlliInAggiornamentoUsingPUT(cuaa);

		return fabbricatoModel.getId();
	}

	@Transactional
	public void cancellaFabbricato(Long idFabbricato) {
		datiCatastaliDao.deleteByFabbricatoIdAndFabbricato_IdValidazione(idFabbricato, 0);
		fabbricatiDao.deleteById(new EntitaDominioFascicoloId(idFabbricato, 0));
	}

	private FabbricatoModel buildModel(FabbricatoModel toSave, FascicoloModel fascicolo, DettaglioFabbricatoAbstract dettaglioFabbricatoDto) {
		// Se presenti i dati catastali allora vengono salvati
		if (dettaglioFabbricatoDto.getDatiCatastali() != null) {
			List<DatiCatastaliModel> datiCatastali = new ArrayList<DatiCatastaliModel>();
			dettaglioFabbricatoDto.getDatiCatastali().forEach(el -> {
				DatiCatastaliModel dato = new DatiCatastaliModel();
				dato.setCategoria(el.getCategoria());
				dato.setConsistenza(el.getConsistenza());
				dato.setDenominatore(el.getDenominatore());
				dato.setFabbricato(toSave);
				dato.setIndirizzo(el.getIndirizzo());
				dato.setInTrentino(el.getInTrentino());
				dato.setNote(el.getNote());
				dato.setTipologia(el.getTipologia());
				dato.setSuperficie(el.getSuperficie());
				dato.setComune(el.getComune());
				dato.setSezione(el.getSezione());
				dato.setFoglio(el.getFoglio());
				dato.setParticella(el.getParticella());
				dato.setSub(el.getSub());
				datiCatastali.add(dato);
			});
			toSave.setDatiCatastali(datiCatastali);
		}
		toSave.setComune(dettaglioFabbricatoDto.getComune()).setDenominazione(dettaglioFabbricatoDto.getDenominazione()).setDescrizione(dettaglioFabbricatoDto.getDescrizione()).setFascicolo(fascicolo)
				.setIndirizzo(dettaglioFabbricatoDto.getIndirizzo())
				.setSottotipo(sottotipoDao.findById(dettaglioFabbricatoDto.getSottotipo().getId())
						.orElseThrow(() -> new EntityNotFoundException("Sottotipo con id: ".concat(String.valueOf(dettaglioFabbricatoDto.getSottotipo().getId())).concat(" non trovato"))))
				.setSuperficie(dettaglioFabbricatoDto.getSuperficie()).setTipoConduzione(dettaglioFabbricatoDto.getTipoConduzione()).setVolume(dettaglioFabbricatoDto.getVolume());

		return toSave;
	}

	private StrumentaliModel buildStrumentaliModel(DettaglioStrumentaliDto dto) {
		return new StrumentaliModel().setSuperficiecoperta(dto.getSuperficieCoperta()).setSuperficieScoperta(dto.getSuperficieScoperta());
	}

	private SerreModel buildSerreModel(DettaglioSerreDto dto) {
		return new SerreModel().setAnnoAcquisto(dto.getAnnoAcquisto()).setAnnoCostruzione(dto.getAnnoCostruzione()).setTitoloConformitaUrbanistica(dto.getTitoloConformitaUrbanistica())
				.setTipologiaMateriale(dto.getTipologiaMateriale()).setImpiantoRiscaldamento(dto.isImpiantoRiscaldamento());
	}

	private StoccaggioModel buildStoccaggioModel(DettaglioStoccaggioDto dto) {
		return new StoccaggioModel().setAltezza(dto.getAltezza()).setCopertura(dto.getCopertura());
	}

	public List<FabbricatoDto> getFabbricati(String cuaa, Integer idValidazione) {
		var fabbricati = new ArrayList<FabbricatoDto>();
		List<FabbricatoModel> fabbricatiModel = fabbricatiDao.findByFascicolo_cuaaAndFascicolo_idValidazione(cuaa, idValidazione);

		if (!CollectionUtils.isEmpty(fabbricatiModel)) {
			fabbricatiModel.forEach(fabbricato -> fabbricati.add(fabbricatoBuilder.newDto().from(fabbricato).build()));
		}
		return fabbricati;
	}

	public DettaglioFabbricatoAbstract getFabbricato(Long id, Integer idValidazione) {
		FabbricatoModel fabbricatoModel = fabbricatiDao.findById(new EntitaDominioFascicoloId(id, idValidazione))
				.orElseThrow(() -> new EntityNotFoundException("Fabbricato con id: ".concat(String.valueOf(id)).concat("non trovato")));

		return dettaglioFabbricatoBuilder.from(fabbricatoModel);
	}

	public Consumer<DettaglioStrumentaliDto> checkSuperficieTotale = fabbricato -> {
		if (((fabbricato.getSuperficieCoperta() != null ? fabbricato.getSuperficieCoperta() : 0)
				+ (fabbricato.getSuperficieScoperta() != null ? fabbricato.getSuperficieScoperta() : 0)) > (fabbricato.getSuperficie() != null ? fabbricato.getSuperficie() : 0)) {
			throw new IllegalArgumentException("La somma di superficie coperta e scoperta non può essere maggiore della superficie totale");
		}
	};

	public void migra(String cuaa, FabbricatoAgsDto fabbricatoAgsDto) {
		// Reperisco il fascicolo se esiste
		var fascicolo = new FascicoloModel();
		Optional<FascicoloModel> fascicoloModel = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		if (!fascicoloModel.isPresent()) {
			// Creo il fascicolo se non esiste
			if (fascicoloModel.isEmpty()) {
				var fascicoloNew = new FascicoloModel();
				fascicoloNew.setCuaa(cuaa);
				fascicoloNew.setIdValidazione(0);
				fascicolo = fascicoloDao.save(fascicoloNew);
			}
		} else {
			fascicolo = fascicoloModel.get();
		}
		String codiceFabbricato = fabbricatoAgsDto.getTipoFabbricatoCodice();
		List<MappingFabbricatiAgsModel> mapping = mappingAgsFabbricatiDao.findByCodiceAgs(codiceFabbricato);
		SottotipoModel sottotipo = null;
		FabbricatoModel fabbricatoNew = null;
		if (!mapping.isEmpty()) {
			sottotipo = mapping.get(0).getSottotipo();
			// TODO salvare in A4GT_SERRE/A4GT_STOCCAGGIO/A4GT_STRUMENTALI
			TipologiaModel tipologiaModel = sottotipo.getTipologia();
			if (tipologiaModel.getDescrizione().equals(TipoFabbricato.FABBRICATI_STRUMENTALI.name())) {
				// A4GT_STRUMENTALI
				fabbricatoNew = new StrumentaliModel();
				// ((StrumentaliModel)fabbricatoNew).setSuperficiecoperta();
				// ((StrumentaliModel)fabbricatoNew).setSuperficieScoperta();
			} else if (tipologiaModel.getDescrizione().equals(TipoFabbricato.SERRE_E_PROTEZIONI.name())) {
				// A4GT_SERRE
				fabbricatoNew = new SerreModel();
				// ((SerreModel)fabbricatoNew).setAnnoAcquisto();
				// ((SerreModel)fabbricatoNew).setAnnoCostruzione();
				// ((SerreModel)fabbricatoNew).setTitoloConformitaUrbanistica();
				// ((SerreModel)fabbricatoNew).setTipologiaMateriale();
				// ((SerreModel)fabbricatoNew).setImpiantoRiscaldamento();
			} else if (tipologiaModel.getDescrizione().equals(TipoFabbricato.STRUTTURE_ZOOTECNICHE.name())) {
				// A4GT_STOCCAGGIO
				fabbricatoNew = new StoccaggioModel();
				// ((StoccaggioModel)fabbricatoNew).setAltezza();
				// ((StoccaggioModel)fabbricatoNew).setCopertura();
			} else {
				fabbricatoNew = new FabbricatoModel();
			}
		} else {
			logger.error("Codice fabbricato " + codiceFabbricato + " non trovato! per cuaa " + cuaa);
			throw new IllegalArgumentException("Codice fabbricato " + codiceFabbricato + " non trovato!");
		}
		fabbricatoNew.setSottotipo(sottotipo);
		fabbricatoNew.setComune(fabbricatoAgsDto.getComune());
		fabbricatoNew.setVolume(fabbricatoAgsDto.getVolume().longValue());
		fabbricatoNew.setSuperficie(fabbricatoAgsDto.getSuperficie().longValue());
		fabbricatoNew.setDescrizione(fabbricatoAgsDto.getDescrizione());
		if (fabbricatoAgsDto.getTitoloConduzione().startsWith("PROPRIETA")) {
			fabbricatoNew.setTipoConduzione(TipoConduzione.PROPRIETA_O_COMPROPRIETA);
		} else if (fabbricatoAgsDto.getTitoloConduzione().startsWith("AFFITTO")) {
			fabbricatoNew.setTipoConduzione(TipoConduzione.AFFITTO);
		} else if (fabbricatoAgsDto.getTitoloConduzione().equals("MEZZADRIA")) {
			fabbricatoNew.setTipoConduzione(TipoConduzione.MEZZADRIA);
		} else {
			fabbricatoNew.setTipoConduzione(TipoConduzione.ALTRA_FORMA);
		}
		fabbricatoNew.setFascicolo(fascicolo);
		fabbricatoNew = fabbricatiDao.save(fabbricatoNew);

		DatiCatastaliModel datiCatastaliModel = new DatiCatastaliModel();
		datiCatastaliModel.setComune(fabbricatoAgsDto.getComuneCatastale());
		datiCatastaliModel.setDenominatore(fabbricatoAgsDto.getSubalterno());
		datiCatastaliModel.setFabbricato(fabbricatoNew);
		datiCatastaliModel.setFoglio(fabbricatoAgsDto.getFoglio());
		datiCatastaliModel.setInTrentino(isInTrentino(fabbricatoAgsDto.getComuneCatastale()));
		datiCatastaliModel.setNote(fabbricatoAgsDto.getNote());
		datiCatastaliModel.setParticella(fabbricatoAgsDto.getParticella());
		datiCatastaliModel.setSezione(fabbricatoAgsDto.getSezione());
		datiCatastaliModel.setSuperficie(fabbricatoAgsDto.getSuperficie().longValue());
		if (datiCatastaliModel.getInTrentino() != null && datiCatastaliModel.getInTrentino()) {
			// la particella dovrà essere validata in catasto in fase di validazione fascicolo
			datiCatastaliModel.setEsito(EsitoValidazioneParticellaCatastoEnum.INVALIDA);
		}
		if (fabbricatoAgsDto.getParticella().contains(".")) {
			datiCatastaliModel.setTipologia(TipologiaParticellaCatastale.EDIFICIALE);
		} else {
			datiCatastaliModel.setTipologia(TipologiaParticellaCatastale.FONDIARIA);
		}
		List<DatiCatastaliModel> datiCatastali = new ArrayList<>();
		datiCatastali.add(datiCatastaliDao.save(datiCatastaliModel));
		fabbricatoNew.setDatiCatastali(datiCatastali);
	}

	public boolean isInTrentino(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public DatiCatastaliDto getInfoFabbricati(String numeroParticella, String denominatore, TipologiaParticellaCatastale tipologia, Integer codiceComuneCatastale, Integer subalterno)
			throws EntityNotFoundException, CatastoParticellaEstintaException, CatastoEdificialeClasseCatastaleNonAmmessaException {
		try {
			String numeroParticellaCompleta = (StringUtils.isBlank(denominatore)) ? numeroParticella : numeroParticella + "/" + denominatore;
			DatiCatastaliDto datiCatastaliDto = null;
			InformazioniParticellaDto informazioniParticellaDto = dotazioneTecnicaAnagraficaClient.getInfoParticella(numeroParticellaCompleta, tipologia, codiceComuneCatastale);
			if (informazioniParticellaDto == null) {
				throw new EntityNotFoundException(
						"La particella [numPart=" + numeroParticellaCompleta + "; codiceComuneCatastale=" + codiceComuneCatastale + "] non e' stata trovata nel catasto fondiario");
			}

			if (informazioniParticellaDto.getStato().equals(InformazioniParticellaDto.StatoEnum.ESTINTA)) {
				throw new CatastoParticellaEstintaException("La particella [numPart=" + numeroParticellaCompleta + "; codiceComuneCatastale=" + codiceComuneCatastale + "] risulta estinta");
			}
			// dati comuni a fondiario e immobiliare
			datiCatastaliDto = new DatiCatastaliDto();
			datiCatastaliDto.setComune(String.valueOf(informazioniParticellaDto.getComuneCatastale()));
			datiCatastaliDto.setTipologia(TipologiaParticellaCatastale.valueOf(informazioniParticellaDto.getTipologia().getValue()));
			// informazioniParticellaDto.getQualita(); // TODO e' una lista
			datiCatastaliDto.setParticella(numeroParticella);
			if (!StringUtils.isBlank(denominatore)) {
				datiCatastaliDto.setDenominatore(denominatore);
			}
			datiCatastaliDto.setSezione(informazioniParticellaDto.getPartitaTavolare().getSezione().getValue());
			// Attualmente a4gproxy di catasto calcola le superfici per le sole particelle edificiali. Per l'eventuale inclusione anche delle colture si deve intervenire nella logica nel proxy
			datiCatastaliDto.setSuperficie(informazioniParticellaDto.getSuperficie() != null ? Integer.toUnsignedLong(informazioniParticellaDto.getSuperficie()) : null);
			if (tipologia.equals(TipologiaParticellaCatastale.EDIFICIALE) && subalterno != null) {
				InformazioniImmobileDto informazioniImmobileDto = dotazioneTecnicaAnagraficaClient.getInfoImmobile(numeroParticellaCompleta, codiceComuneCatastale, subalterno);
				if (informazioniImmobileDto == null) {
					throw new EntityNotFoundException("L'immobile' [numPart=" + numeroParticellaCompleta + "; codiceComuneCatastale=" + codiceComuneCatastale + "; subalterno=" + subalterno
							+ "] non e' stata trovata nel catasto immobiliare");
				}
				// verifica categorie edificiali ammesse
				if (informazioniImmobileDto.getDatiClassamento() == null || (!informazioniImmobileDto.getDatiClassamento().getCategoria().equals(DatiClassamentoDto.CategoriaEnum.C_02)
						&& !informazioniImmobileDto.getDatiClassamento().getCategoria().equals(DatiClassamentoDto.CategoriaEnum.C_03)
						&& !informazioniImmobileDto.getDatiClassamento().getCategoria().equals(DatiClassamentoDto.CategoriaEnum.D_10))) {
					throw new CatastoEdificialeClasseCatastaleNonAmmessaException("Per la particella edificiale [numPart=" + numeroParticellaCompleta + "; codiceComuneCatastale="
							+ codiceComuneCatastale + "] la categoria [" + informazioniImmobileDto.getDatiClassamento().getCategoria() + "] non e' ammessa. Sono ammesse solo classi C2, C3 o D10");
				}

				datiCatastaliDto.setCategoria(informazioniImmobileDto.getDatiClassamento().getCategoria().getValue());
				datiCatastaliDto.setConsistenza(String.valueOf(informazioniImmobileDto.getDatiClassamento().getConsistenza()));
				datiCatastaliDto.setSub(String.valueOf(subalterno));
				String indirizziStr = "";
				String delimiter = "; ";
				for (String word : informazioniImmobileDto.getIndirizzo()) {
					indirizziStr += indirizziStr.equals("") ? word : delimiter + word;
				}
				datiCatastaliDto.setIndirizzo(indirizziStr);
			}
			datiCatastaliDto.setInTrentino(true);

			return datiCatastaliDto;
		} catch (CatastoEdificialeClasseCatastaleNonAmmessaException e) {
			throw e;
		} catch (CatastoParticellaEstintaException e) {
			throw e;
		} catch (EntityNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public List<String> getElencoSubalterniParticella(String numeroParticella, Integer codiceComuneCatastale) {
		List<String> result = anagraficaClient.getElencoSubalterniParticella(numeroParticella, codiceComuneCatastale);
		if (result != null && !result.isEmpty()) {
			return result.stream().sorted().collect(Collectors.toList());
		}
		return result;
	}
}
