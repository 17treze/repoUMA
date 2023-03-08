package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.ClasseFunzionaleModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MacchinaModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MacchinaMotorizzataModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MappingAgsModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.SottotipoModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.TipologiaModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.Alimentazione;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.ClasseFunzionaleDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.MacchinaDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.MappingAgsDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.SottotipoDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.TipologiaDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.TipologiaPossesso;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.TipologiaPra;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.DettaglioMacchinaDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.MacchinaDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.builder.DettaglioMacchinaDtoBuilder;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.builder.MacchinaDtoBuilder;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.MacchinaAgsDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.TipoCarburante;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

@Service
public class MacchineService {
	private static final Logger logger = LoggerFactory.getLogger(MacchineService.class);

	public static final String MACCHINA_GIA_ESISTENTE = "Attenzione! Il macchinario è già stato inserito nel fascicolo";
	// private static final String TIPO_MACCHINA_NON_ESISTENTE = "Attenzione! Sotto tipo macchina AGS non trovato";

	@Autowired
	private FascicoloDao fascicoloDao;
	@Autowired
	private TipologiaDao tipologiaDao;
	@Autowired
	private MacchinaDao macchinaDao;
	@Autowired
	private SottotipoDao sottotipoDao;
	@Autowired
	private ClasseFunzionaleDao classifunzionaliDao;

	@Autowired
	private MappingAgsDao mappingAgsDao;
	@Autowired
	private DettaglioMacchinaDtoBuilder dettaglioMacchinaBuilder;
	@Autowired
	private MacchinaDtoBuilder macchinaBuilder;

	public List<MacchinaDto> getMacchine(String cuaa, Integer idValidazione) {
		var macchine = new ArrayList<MacchinaDto>();
		List<MacchinaModel> macchineModel = macchinaDao.findByFascicolo_cuaaAndFascicolo_idValidazione(cuaa, idValidazione);

		if (!CollectionUtils.isEmpty(macchineModel)) {
			macchineModel.forEach(macchina -> macchine.add(macchinaBuilder.newDto().from(macchina).withAlimentazione(macchina).build()));
		}
		return macchine;
	}

	public DettaglioMacchinaDto getMacchina(Long id, Integer idValidazione) {
		MacchinaModel macchina = macchinaDao.findById(new EntitaDominioFascicoloId(id, idValidazione))
				.orElseThrow(() -> new EntityNotFoundException("Macchina con id: ".concat(String.valueOf(id)).concat("non trovata")));

		return dettaglioMacchinaBuilder.newDto().from(macchina).withMotore(macchina).build();
	}

	@Transactional
	public Long dichiaraMacchina(DettaglioMacchinaDto dettaglioMacchinaDto, String cuaa, ByteArrayResource documentoByteAsResource) {
		// Reperisco il fascicolo se esiste
		Optional<FascicoloModel> fascicoloModel = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		SottotipoModel sottoTipologiaMacchinario = sottotipoDao.findById(dettaglioMacchinaDto.getSottotipo().getId())
				.orElseThrow(() -> new EntityNotFoundException("Sottotipologia con id: ".concat(String.valueOf(dettaglioMacchinaDto.getSottotipo().getId())).concat("non trovata")));

		boolean isPra = false;
		if (sottoTipologiaMacchinario.getTipologia() == null) {
			Optional<ClasseFunzionaleModel> classeFunzionaleMacchinario = classifunzionaliDao.findById(sottoTipologiaMacchinario.getClassefunzionale().getId());
			Optional<TipologiaModel> tipologiaMacchinario = tipologiaDao.findById(classeFunzionaleMacchinario.get().getTipologia().getId());
			isPra = Arrays.asList(TipologiaPra.values()).stream().anyMatch(type -> type.getTipologia().equals(tipologiaMacchinario.get().getDescrizione()));
			logger.debug("Tipologia Pra: {}", isPra);
		} else {
			TipologiaModel tipologiaMacchinario = tipologiaDao.findById(sottoTipologiaMacchinario.getTipologia().getId())
					.orElseThrow(() -> new EntityNotFoundException("Tipologia con id: ".concat(String.valueOf(sottoTipologiaMacchinario.getTipologia().getId())).concat("non trovata")));

			isPra = Arrays.asList(TipologiaPra.values()).stream().anyMatch(type -> type.getTipologia().equals(tipologiaMacchinario.getDescrizione()));
			logger.debug("Tipologia Pra: {}", isPra);
		}

		// Reperisco l'eventuale macchina del fascicolo con stessi numero di matricola e/o numero del telaio e/o targa
		List<MacchinaModel> macchineDuplicate = null;
		if (fascicoloModel.isPresent()) {
			macchineDuplicate = isPra ? checkMacchinaDuplicataConTarga.apply(fascicoloModel.get().getMacchine(), dettaglioMacchinaDto)
					: checkMacchinaDuplicataSenzaTarga.apply(fascicoloModel.get().getMacchine(), dettaglioMacchinaDto);
		}

		// Se presente l'id siamo in modifica
		if (dettaglioMacchinaDto.getId() != null) {
			// Se sono in modifica controllo che esista almeno una macchina in più oltre a quella che sto modificando
			if (!CollectionUtils.isEmpty(macchineDuplicate) && (macchineDuplicate.stream().count() > 1) && dettaglioMacchinaDto.getFlagMigrato() == 0) {
				throw new IllegalArgumentException(MACCHINA_GIA_ESISTENTE);
			}
			// Distruggo per ricreare
			cancellaMacchina(dettaglioMacchinaDto.getId());
			macchineDuplicate = null;
		}

		// Se esiste una macchina nel fascicolo con stesso numero di matricola e telaio, lancio errore
		if (!CollectionUtils.isEmpty(macchineDuplicate)) {
			throw new IllegalArgumentException(MACCHINA_GIA_ESISTENTE);
		} else {
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
			if (dettaglioMacchinaDto.getFlagMigrato() == 1) {
				dettaglioMacchinaDto.setFlagMigrato(0);
			}

			// Controllo se c'è il motore
			if (dettaglioMacchinaDto.getMotore() != null) {
				var macchinaNew = new MacchinaMotorizzataModel();
				var motoreNew = dettaglioMacchinaDto.getMotore();
				macchinaNew.setAlimentazione(motoreNew.getAlimentazione()).setMarcaMotore(motoreNew.getMarca()).setTipoMotore(motoreNew.getTipo()).setPotenza(motoreNew.getPotenza())
						.setMatricola(motoreNew.getMatricola());
				return macchinaDao.save(buildModel(macchinaNew, fascicolo, dettaglioMacchinaDto, documentoByteAsResource)).getId();
			}
			return macchinaDao.save(buildModel(new MacchinaModel(), fascicolo, dettaglioMacchinaDto, documentoByteAsResource)).getId();
		}
	}

	private BiFunction<List<MacchinaModel>, DettaglioMacchinaDto, List<MacchinaModel>> checkMacchinaDuplicataConTarga = (macchine, dettaglioMacchinaDto) -> {
		List<MacchinaModel> macchineDuplicate = macchine.stream()
				.filter(macchina -> (macchina.getNumeroMatricola() != null ? macchina.getNumeroMatricola().equalsIgnoreCase(dettaglioMacchinaDto.getNumeroMatricola()) : false)
						|| (macchina.getNumeroTelaio() != null ? macchina.getNumeroTelaio().equalsIgnoreCase(dettaglioMacchinaDto.getNumeroTelaio()) : false)
						|| (macchina.getTarga() != null ? macchina.getTarga().equalsIgnoreCase(dettaglioMacchinaDto.getTarga()) : false))
				.collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(macchineDuplicate)) {
			return macchineDuplicate;
		} else {
			return null;
		}
	};

	private BiFunction<List<MacchinaModel>, DettaglioMacchinaDto, List<MacchinaModel>> checkMacchinaDuplicataSenzaTarga = (macchine, dettaglioMacchinaDto) -> {
		List<MacchinaModel> macchineDuplicate = macchine.stream()
				.filter(macchina -> (macchina.getNumeroMatricola() != null ? macchina.getNumeroMatricola().equalsIgnoreCase(dettaglioMacchinaDto.getNumeroMatricola()) : false)
						|| (macchina.getNumeroTelaio() != null ? macchina.getNumeroTelaio().equalsIgnoreCase(dettaglioMacchinaDto.getNumeroTelaio()) : false))
				.collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(macchineDuplicate)) {
			return macchineDuplicate;
		} else {
			return null;
		}
	};

	private MacchinaModel buildModel(MacchinaModel toSave, FascicoloModel fascicolo, DettaglioMacchinaDto dettaglioMacchinaDto, ByteArrayResource documentoByteAsResource) {
		toSave.setFascicolo(fascicolo);

		toSave.setAnnoDiCostruzione(dettaglioMacchinaDto.getAnnoCostruzione()).setDataImmatricolazione(dettaglioMacchinaDto.getDataImmatricolazione())
				.setDocumentoPossesso(documentoByteAsResource.getByteArray()).setMarca(dettaglioMacchinaDto.getMarca()).setModello(dettaglioMacchinaDto.getModello())
				.setNumeroMatricola(dettaglioMacchinaDto.getNumeroMatricola()).setNumeroTelaio(dettaglioMacchinaDto.getNumeroTelaio())
				.setSottotipoMacchinario(sottotipoDao.findById(dettaglioMacchinaDto.getSottotipo().getId())
						.orElseThrow(() -> new EntityNotFoundException("Sottotipo con id: ".concat(String.valueOf(dettaglioMacchinaDto.getSottotipo().getId())).concat("non trovato"))))
				.setTarga(dettaglioMacchinaDto.getTarga()).setTipoPossesso(dettaglioMacchinaDto.getTipoPossesso()).setIdValidazione(0);

		toSave.setCodiceFiscale(dettaglioMacchinaDto.getCodiceFiscale());
		toSave.setRagioneSociale(dettaglioMacchinaDto.getRagioneSociale());
		toSave.setFlagMigrato(dettaglioMacchinaDto.getFlagMigrato());

		return toSave;
	}

	public ResponseEntity<Resource> getAllegato(Long id, Integer idValidazione) {
		// Find by sulla macchina per ottenere il documento allegato
		MacchinaModel macchina = macchinaDao.findById(new EntitaDominioFascicoloId(id, idValidazione))
				.orElseThrow(() -> new EntityNotFoundException("Macchina con id: ".concat(String.valueOf(id)).concat("non trovata")));

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=".concat(id.toString()).concat("_macchina_documento possesso.pdf"))
				.body(macchina.getDocumentoPossesso() != null ? new ByteArrayResource(macchina.getDocumentoPossesso()) : null);
	}

	public void cancellaMacchina(Long id) {
		macchinaDao.deleteById(new EntitaDominioFascicoloId(id, 0));
	}

	public void migra(String cuaa, MacchinaAgsDto macchinaAgsDto) {
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
		MacchinaModel macchinaNew;
		if (macchinaAgsDto.getAlimentazione() == TipoCarburante.GASOLIO || macchinaAgsDto.getAlimentazione() == TipoCarburante.BENZINA) {
			macchinaNew = new MacchinaMotorizzataModel();
			if (macchinaAgsDto.getAlimentazione() == TipoCarburante.GASOLIO) {
				((MacchinaMotorizzataModel) macchinaNew).setAlimentazione(Alimentazione.GASOLIO);
			}
			if (macchinaAgsDto.getAlimentazione() == TipoCarburante.BENZINA) {
				((MacchinaMotorizzataModel) macchinaNew).setAlimentazione(Alimentazione.BENZINA);
			}
			((MacchinaMotorizzataModel) macchinaNew).setTipoMotore(macchinaAgsDto.getTipoMotore());
			((MacchinaMotorizzataModel) macchinaNew).setMarcaMotore(macchinaAgsDto.getMarcaMotore());
			((MacchinaMotorizzataModel) macchinaNew).setPotenza(macchinaAgsDto.getPotenzaKw());
			((MacchinaMotorizzataModel) macchinaNew).setMatricola(macchinaAgsDto.getMatricola());
		} else {
			macchinaNew = new MacchinaModel();
		}
		macchinaNew.setMarca(macchinaAgsDto.getMarca());
		macchinaNew.setTarga(macchinaAgsDto.getTarga());
		macchinaNew.setModello(macchinaAgsDto.getDescrizione());
		macchinaNew.setNumeroTelaio(macchinaAgsDto.getTelaio());
		// macchinaNew.setNumeroMatricola(macchinaAgsDto.getMatricola());
		if (macchinaAgsDto.getPossesso().startsWith("PROPRIETA")) {
			macchinaNew.setTipoPossesso(TipologiaPossesso.PROPRIETA);
		} else if (macchinaAgsDto.getPossesso().startsWith("COMPROPRIETA")) {
			macchinaNew.setTipoPossesso(TipologiaPossesso.COMPROPRIETA);
		} else if (macchinaAgsDto.getPossesso().equals("LEASING")) {
			macchinaNew.setTipoPossesso(TipologiaPossesso.LEASING);
		} else if (macchinaAgsDto.getPossesso().equals("NOLEGGIO")) {
			macchinaNew.setTipoPossesso(TipologiaPossesso.NOLEGGIO);
		} else if (macchinaAgsDto.getPossesso().equals("COMODATO")) {
			macchinaNew.setTipoPossesso(TipologiaPossesso.COMODATO);
		} else {
			macchinaNew.setTipoPossesso(TipologiaPossesso.IN_USO);
		}
		String codiceClasse = macchinaAgsDto.getCodiceClasse();
		String codiceSottoClasse = macchinaAgsDto.getCodiceSottoClasse();
		Long idMacchina = macchinaAgsDto.getIdMacchina();
		Long idTipoMacchina = macchinaAgsDto.getIdTipoMacchina();

		List<MappingAgsModel> mapIdMacchina = new ArrayList<MappingAgsModel>();
		//
		// 1. Verifico che l'idMacchina sia presente all'interno della tabella A4GD_MAPPING_AGS
		if (idMacchina != null) {
			mapIdMacchina = mappingAgsDao.findByIdMacchina(idMacchina);
		}
		// idMacchina va null e si interrome lo unit TEST aggiustare


		SottotipoModel sottoTipo = null;

		if (!mapIdMacchina.isEmpty()) {
			// 1.1 Esiste nella A4GD_MAPPING_AGS (mappingAgsDao.findByIdMacchina())-> Setto sull'oggetto macchinaNew l'id_sottotipo estratto da A4GD_MAPPING_AGS
			sottoTipo = mapIdMacchina.get(0).getSottotipo();
			// macchinaNew.setSottotipoMacchinario(m);
		} else {
			// 1.2 Non Esiste nella A4GD_MAPPING_AGS ->
			// 2. Verifico se l'idTipoMacchina è presente nella tabella A4GD_MAPPING_AGS
			mapIdMacchina = mappingAgsDao.findByIdTipoMacchina(idTipoMacchina);

			if (!mapIdMacchina.isEmpty()) {
				// 2.1 Esiste nella A4GD_MAPPING_AGS (mappingAgsDao.findByIdTipoMacchina())-> Setto sull'oggetto macchinaNew l'id_sottotipo estratto da A4GD_MAPPING_AGS
				sottoTipo = mapIdMacchina.get(0).getSottotipo();
				// macchinaNew.setSottotipoMacchinario(m);
			} else {
				// 2.2 Non Esiste nella A4GD_MAPPING_AGS ->
				// 3. Verifico se la coppia codiceClasse e codiceSottoClasse esistono all'interno della tabella A4GD_MAPPING_AGS
				mapIdMacchina = null;
				mapIdMacchina = mappingAgsDao.findByCodiceAgsAndSottoCodiceAgs(codiceClasse, codiceSottoClasse);
				if (!mapIdMacchina.isEmpty()) {
					sottoTipo = mapIdMacchina.get(0).getSottotipo();
					// macchinaNew.setSottotipoMacchinario(m);

				} else {
					throw new IllegalArgumentException("Sottotipo macchina " + codiceClasse + "-" + codiceSottoClasse + " non trovato!");
				}
			}
		}

		// Per ora non salvo
		if (sottoTipo == null) {
			logger.warn("Scarto il macchinario mapIdMacchina = [{}] codice classe = {} codice sotto classe = {} idMacchina = {} idTipoMacchia = {} idAgs = {} per il cuaa {}", mapIdMacchina.get(0),
					macchinaAgsDto.getCodiceClasse(), macchinaAgsDto.getCodiceSottoClasse(), macchinaAgsDto.getIdMacchina(), macchinaAgsDto.getIdTipoMacchina(), macchinaAgsDto.getIdAgs(), cuaa);
			return;
		}

		macchinaNew.setSottotipoMacchinario(sottoTipo);

		// 3.1 Esiste nella A4GD_MAPPING_AGS -> Setto sull'oggetto macchinaNew l'id_sottotipo estratto da A4GD_MAPPING_AGS
		// 3.2 Non Esiste nella A4GD_MAPPING_AGS -> throw new IllegalArgumentException("Sottotipo macchina " + codiceClasse + "-" + codiceSottoClasse + " non trovato!");
		//

		/*
		 * mapping = mappingAgsDao.findByCodiceAgsAndSottoCodiceAgs(codiceClasse, codiceSottoClasse); if (!mapping.isEmpty()) { SottotipoModel sottotipo = mapping.get(0).getSottotipo();
		 * macchinaNew.setSottotipoMacchinario(sottotipo); } else { throw new IllegalArgumentException("Sottotipo macchina " + codiceClasse + "-" + codiceSottoClasse + " non trovato!"); }
		 */
		macchinaNew.setFlagMigrato(new Long(1));
		macchinaNew.setFascicolo(fascicolo);
		macchinaDao.save(macchinaNew);
	}
}
