package it.tndigitale.a4g.uma.business.service.lavorazioni;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.uma.GeneralFactory;
import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;
import it.tndigitale.a4g.uma.business.persistence.entity.ConsumiClienteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbisognoFabbricatoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbisognoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbricatoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;
import it.tndigitale.a4g.uma.business.persistence.repository.ClienteDao;
import it.tndigitale.a4g.uma.business.persistence.repository.ConsumiClientiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.FabbisognoDao;
import it.tndigitale.a4g.uma.business.persistence.repository.FabbisognoFabbricatoDao;
import it.tndigitale.a4g.uma.business.persistence.repository.FabbricatiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.LavorazioneDao;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.dto.richiesta.DichiarazioneDto;
import it.tndigitale.a4g.uma.dto.richiesta.DichiarazioneFabbricatoDto;
import it.tndigitale.a4g.uma.dto.richiesta.LavorazioneFilter;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;
import it.tndigitale.a4g.uma.dto.richiesta.builder.FabbisogniBuilder;

@Service
public class LavorazioniService {

	private static final Logger logger = LoggerFactory.getLogger(LavorazioniService.class);

	@Autowired
	private GeneralFactory lavorazioniFactory;
	@Autowired
	private FabbisognoDao fabbisognoDao;
	@Autowired
	private FabbisognoFabbricatoDao fabbisognoFabbricatoDao;
	@Autowired
	private FabbricatiDao fabbricatiDao;
	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private LavorazioneDao lavorazioneDao;
	@Autowired
	private ConsumiClientiDao consumiClientiDao;
	@Autowired
	private ClienteDao clienteDao;

	private static final String RICHIESTA_NOT_FOUND = "Nessuna Richiesta con id %s trovata";
	private static final String LAVORAZIONE_NOT_FOUND = "Nessuna lavorazione uma trovata con id %s";
	private static final String FABBRICATO_NOT_FOUND = "Nessun Fabbricato uma trovato con id %s";
	private static final String CLIENTE_FIND_BY_ERROR = "Non esiste un cliente con id ";

	@Transactional
	public void dichiaraFabbisogni(Long idRichiesta, List<DichiarazioneDto> dichiarazioni) {
		RichiestaCarburanteModel richiesta = richiestaCarburanteDao.findById(idRichiesta).orElseThrow(() -> new EntityNotFoundException(String.format(RICHIESTA_NOT_FOUND, idRichiesta)));

		dichiarazioni.forEach(dichiarazione -> {

			if (dichiarazione.getLavorazioneId() == null) {
				var errMsg = String.format("[Lavorazioni UMA] - Dichiarazione Fabbrisogni - ID Lavorazione null - Richiesta %s" ,richiesta.getId());
				logger.error(errMsg);
				throw new IllegalArgumentException(errMsg);
			}

			var lavorazioneModel = lavorazioneDao.findById(dichiarazione.getLavorazioneId()).orElseThrow(() -> new EntityNotFoundException(String.format(LAVORAZIONE_NOT_FOUND, dichiarazione.getLavorazioneId())));

			// cancella le precedenti dichiarazioni di fabbisogni per la lavorazione/domanda corrente
			fabbisognoDao.deleteByLavorazioneModelAndRichiestaCarburante(lavorazioneModel, richiesta);
			if (!CollectionUtils.isEmpty(dichiarazione.getFabbisogni())) {
				dichiarazione.getFabbisogni().forEach(fabbisogno -> {
					// aggiunge solo dichiarazioni con una quantità dichiarata > 0
					if (fabbisogno.getQuantita() != null && fabbisogno.getQuantita().compareTo(BigDecimal.ZERO) > 0) {
						var fabbisognoModel = new FabbisognoModel()
								.setRichiestaCarburante(richiesta)
								.setQuantita(fabbisogno.getQuantita())
								.setCarburante(fabbisogno.getCarburante())
								.setLavorazioneModel(lavorazioneModel);

						fabbisognoDao.save(fabbisognoModel);
					}
				});
			}
		});
		logger.info("[Lavorazioni UMA] - Salvataggio lavorazioni richiesta {}", idRichiesta);
	}

	@Transactional
	public void dichiaraFabbisogniFabbricati(Long idRichiesta, List<DichiarazioneFabbricatoDto> fabbricati) {
		RichiestaCarburanteModel richiesta = richiestaCarburanteDao.findById(idRichiesta).orElseThrow(() -> new EntityNotFoundException(String.format(RICHIESTA_NOT_FOUND, idRichiesta)));

		fabbricati.forEach(fabbricato -> {
			var fabbricatoModel = fabbricatiDao.findById(fabbricato.getIdFabbricato()).orElseThrow(() -> new EntityNotFoundException(String.format(FABBRICATO_NOT_FOUND, fabbricato.getIdFabbricato())));
			fabbricato.getDichiarazioni().forEach(dichiarazione -> {

				if (dichiarazione.getLavorazioneId() == null) {
					var errMsg = String.format("[Lavorazioni UMA] - Dichiarazione Fabbrisogni Fabbricati - ID Lavorazione null - Richiesta %s" ,richiesta.getId());
					logger.error(errMsg);
					throw new IllegalArgumentException(errMsg);
				}

				var lavorazioneModel = lavorazioneDao.findById(dichiarazione.getLavorazioneId()).orElseThrow(() -> new EntityNotFoundException(String.format(LAVORAZIONE_NOT_FOUND, dichiarazione.getLavorazioneId())));

				// cancella le precedenti dichiarazioni di fabbisogni per la lavorazione/domanda corrente
				fabbisognoFabbricatoDao.deleteByLavorazioneModelAndRichiestaCarburanteAndFabbricatoModel(lavorazioneModel, richiesta, fabbricatoModel);
				if (!CollectionUtils.isEmpty(dichiarazione.getFabbisogni())) {
					dichiarazione.getFabbisogni().forEach(fabbisogno -> {
						// aggiunge solo dichiarazioni con una quantità dichiarata > 0
						if (fabbisogno.getQuantita() != null && fabbisogno.getQuantita().compareTo(BigDecimal.ZERO) > 0) {
							var fabbisognoModel = new FabbisognoFabbricatoModel();
							fabbisognoModel.setRichiestaCarburante(richiesta);
							fabbisognoModel.setQuantita(fabbisogno.getQuantita());
							fabbisognoModel.setCarburante(fabbisogno.getCarburante());
							fabbisognoModel.setLavorazioneModel(lavorazioneModel);
							fabbisognoModel.setFabbricatoModel(fabbricatoModel);
							fabbisognoFabbricatoDao.save(fabbisognoModel);
						}
					});
				}
			});
		});
		logger.info("[Lavorazioni UMA] - Salvataggio lavorazioni fabbricati {}", idRichiesta);
	}

	@Transactional
	public void dichiaraFabbisogniCliente(Long idCliente, List<DichiarazioneDto> dichiarazioni) {
		var clienteModel = clienteDao.findById(idCliente).orElseThrow(() -> new IllegalArgumentException(CLIENTE_FIND_BY_ERROR + idCliente.toString()));

		dichiarazioni.forEach(dichiarazione -> {
			var lavorazioneModel = lavorazioneDao.findById(dichiarazione.getLavorazioneId()).orElseThrow(() -> new EntityNotFoundException(String.format(LAVORAZIONE_NOT_FOUND, dichiarazione.getLavorazioneId())));

			// cancella le precedenti dichiarazioni di fabbisogni per la lavorazione/domanda corrente
			consumiClientiDao.deleteByLavorazioneModelAndCliente(lavorazioneModel, clienteModel);

			dichiarazione.getFabbisogni().forEach(fabbisogno -> {
				// aggiunge solo dichiarazioni con una quantità dichiarata > 0
				if (fabbisogno.getQuantita() != null && fabbisogno.getQuantita().compareTo(BigDecimal.ZERO) > 0) {
					var consumiClienteModel = new ConsumiClienteModel()
							.setCarburante(fabbisogno.getCarburante())
							.setCliente(clienteModel)
							.setLavorazioneModel(lavorazioneModel)
							.setQuantita(fabbisogno.getQuantita());
					consumiClientiDao.save(consumiClienteModel);
				}
			});
		});
		logger.info("[Lavorazioni UMA] - Salvataggio lavorazioni superficie dichiarazione consumi - cliente {}", idCliente);
	}

	public List<DichiarazioneDto> getFabbisogni(Long id, LavorazioneFilter.Lavorazioni ambito) {
		List<FabbisognoModel> fabbisogni = fabbisognoDao.findByRichiestaCarburante_id(id);
		return buildDichiarazioniDto(AmbitoLavorazione.valueOf(ambito.name()), fabbisogni);
	}

	public List<DichiarazioneFabbricatoDto> getFabbisogniFabbricati(Long id, LavorazioneFilter.LavorazioniFabbricati ambito) {
		List<DichiarazioneFabbricatoDto> response = new ArrayList<>();
		List<FabbricatoModel> fabbricati = fabbricatiDao.findByRichiestaCarburante_id(id);
		if (CollectionUtils.isEmpty(fabbricati)) {return new ArrayList<>();}

		fabbricati.forEach(fabbricato -> 
		response.add(new DichiarazioneFabbricatoDto()
				.setIdFabbricato(fabbricato.getId())
				.setDichiarazioni(buildDichiarazioniDto(AmbitoLavorazione.valueOf(ambito.name()), fabbricato.getFabbisogni()))));
		return response;
	}

	public List<DichiarazioneDto> getFabbisogniCliente(Long idCliente) {
		List<ConsumiClienteModel> consumiClienti = consumiClientiDao.findByCliente_id(idCliente);
		if (CollectionUtils.isEmpty(consumiClienti)) {return new ArrayList<>();}
		List<DichiarazioneDto> dichiarazioni = new ArrayList<>();
		consumiClienti.stream()
		.collect(Collectors.groupingBy(ConsumiClienteModel::getLavorazioneModel))
		.forEach((lav,consumi) -> dichiarazioni.add(new DichiarazioneDto()
				.setLavorazioneId(lav.getId())
				.setFabbisogni(new FabbisogniBuilder().withFabbisogniClienti(consumi).build())));
		return dichiarazioni;
	}

	public List<DichiarazioneDto> getFabbisogniRichiestaCliente(Long idCliente) {
		var clienteModel = clienteDao.findById(idCliente).orElseThrow(() -> new EntityNotFoundException(CLIENTE_FIND_BY_ERROR + idCliente.toString()));
		Long campagna = clienteModel.getDichiarazioneConsumi().getRichiestaCarburante().getCampagna();
		Optional<RichiestaCarburanteModel> richiestaCarburanteModel = richiestaCarburanteDao.findByCuaaAndCampagnaAndStato(clienteModel.getCuaa(), campagna, StatoRichiestaCarburante.AUTORIZZATA);
		if (!richiestaCarburanteModel.isPresent()) {
			return new ArrayList<>();
		}
		// Costruisco la lista dei fabbisogni
		return buildDichiarazioniDto(AmbitoLavorazione.SUPERFICIE, richiestaCarburanteModel.get().getFabbisogni());
	}

	public List<RaggruppamentoLavorazioniDto> getCategorieLavorazioni(Long id, AmbitoLavorazione ambito) {
		RecuperaLavorazioniStrategy strategy = lavorazioniFactory.getLavorazioniStrategy("RECUPERO_LAVORAZIONI_" + ambito.name());
		RichiestaCarburanteModel richiesta = richiestaCarburanteDao.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(RICHIESTA_NOT_FOUND, id)));
		return strategy.getRaggruppamenti(richiesta);
	}

	@Transactional
	public void deleteFabbisogni(Long id, Set<TipoCarburante> tipiCarburante) {
		fabbisognoDao.deleteByRichiestaCarburante_idAndCarburanteIn(id, tipiCarburante);
	}

	private List<DichiarazioneDto> buildDichiarazioniDto(AmbitoLavorazione ambito, List<FabbisognoModel> fabbisogni) {
		if (CollectionUtils.isEmpty(fabbisogni)) {return new ArrayList<>();}
		List<DichiarazioneDto> dichiarazioni = new ArrayList<>();
		fabbisogni.stream()
		.filter(f -> ambito.equals(f.getLavorazioneModel().getGruppoLavorazione().getAmbitoLavorazione()))
		.collect(Collectors.groupingBy(FabbisognoModel::getLavorazioneModel))
		.forEach((lav,fab) -> 
		dichiarazioni.add(new DichiarazioneDto()
				.setLavorazioneId(lav.getId())
				.setFabbisogni(new FabbisogniBuilder().withFabbisogni(fab).build())));
		return dichiarazioni;
	}

}
