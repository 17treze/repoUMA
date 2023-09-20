package it.tndigitale.a4g.uma.business.service.consumi;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.uma.business.persistence.entity.AllegatoConsuntivoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.ConsuntivoConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoAllegatoConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.repository.AllegatiConsuntivoDao;
import it.tndigitale.a4g.uma.business.persistence.repository.ConsuntiviConsumiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.service.protocollo.SalvaDocumentoException;
import it.tndigitale.a4g.uma.dto.consumi.ConsuntivoDto;
import it.tndigitale.a4g.uma.dto.consumi.InfoAllegatoConsuntivoDto;

@Service
public class ConsuntiviService {

	private static final Logger logger = LoggerFactory.getLogger(ConsuntiviService.class);
	private static final String DICHIARAZIONE_NOT_FOUND = "Nessuna Dichiarazione Consumi trovata per id ";
	protected static final String SUB_DIRECTORY_RICHIESTE = "/richieste-carburante";

	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;
	@Autowired
	private ConsuntiviConsumiDao consuntiviConsumiDao;
	@Autowired
	private AllegatiConsuntivoDao allegatiConsuntivoDao;
	@Autowired
	private ConsuntiviConsumiValidator consuntiviConsumiValidator;

	@Value("${pathDownload}")
	private String pathDownload;
	
	public List<ConsuntivoDto> getConsuntivi(Long id) {
		List<ConsuntivoConsumiModel> consuntiviModel = consuntiviConsumiDao.findByDichiarazioneConsumi_id(id);
		List<ConsuntivoDto> response = new ArrayList<>();
		if (CollectionUtils.isEmpty(consuntiviModel)) { return response; }

		consuntiviModel.stream().forEach(consuntivo -> {
			var consuntivoDto = new ConsuntivoDto()
					.setId(consuntivo.getId())
					.setCarburante(consuntivo.getTipoCarburante())
					.setMotivazione(consuntivo.getMotivazione())
					.setQuantita(consuntivo.getQuantita().intValue())
					.setTipo(consuntivo.getTipoConsuntivo())
					.setInfoAllegati(new ArrayList<>());

			consuntivo.getAllegati().forEach(allegato -> consuntivoDto.addInfoAllegato(
					new InfoAllegatoConsuntivoDto()
					.setId(allegato.getId())
					.setDescrizione(allegato.getDescrizione())
					.setNome(allegato.getNomeFile())
					.setTipoDocumento(allegato.getTipoAllegato())));
			response.add(consuntivoDto);
		});
		logger.info("Recupero consuntivi dichiarazione consumi id {}", id);
		return response;
	}

	public AllegatoConsuntivoModel getAllegatoConsuntivo(Long idAllegato) {
		return allegatiConsuntivoDao.findById(idAllegato).orElseThrow(() -> new EntityNotFoundException(String.format("Nessun allegato consuntivo trovato con id %s", idAllegato)));
	}

	@Transactional
	public List<ConsuntivoDto> dichiaraConsuntivi(Long id, List<ConsuntivoDto> consuntivi) {
		DichiarazioneConsumiModel dichiarazioneConsumi = dichiarazioneConsumiDao.findById(id).orElseThrow(() -> new EntityNotFoundException(DICHIARAZIONE_NOT_FOUND.concat(String.valueOf(id))));
		List<ConsuntivoConsumiModel> consuntiviToSave = consuntivi.stream()
				.filter(c -> c.getQuantita() != null && c.getQuantita() >= 0) 		// scarta i consuntivi nulli o non valorizzati
				.map(c -> {
					Optional<ConsuntivoConsumiModel> consuntivoModelOpt = consuntiviConsumiDao.findByDichiarazioneConsumiAndTipoCarburanteAndTipoConsuntivo(dichiarazioneConsumi,c.getCarburante(), c.getTipo());
					var consuntivoToSave = consuntivoModelOpt.isPresent() ? consuntivoModelOpt.get() : new ConsuntivoConsumiModel();
					return consuntivoToSave
							.setDichiarazioneConsumi(dichiarazioneConsumi)
							.setMotivazione(consuntivoModelOpt.isPresent() ? consuntivoToSave.getMotivazione() : c.getMotivazione())
							.setQuantita(new BigDecimal(c.getQuantita()))
							.setTipoCarburante(c.getCarburante())
							.setTipoConsuntivo(c.getTipo());
				}).collect(Collectors.toList());

		consuntivi.stream()
		.filter(c -> c.getQuantita() == null)
		.map(c -> consuntiviConsumiDao.findByDichiarazioneConsumiAndTipoCarburanteAndTipoConsuntivo(dichiarazioneConsumi,c.getCarburante(), c.getTipo()))
		.filter(Optional::isPresent)
		.map(Optional::get)
		.map(ConsuntivoConsumiModel::getId)
		.forEach(this::deleteConsuntivo);

		List<ConsuntivoConsumiModel> saved = consuntiviConsumiDao.saveAll(consuntiviToSave);

		consuntiviConsumiDao.flush();
		DichiarazioneConsumiModel dichNew = dichiarazioneConsumiDao.findById(id).orElseThrow(() -> new EntityNotFoundException(DICHIARAZIONE_NOT_FOUND.concat(String.valueOf(id))));
		consuntiviConsumiValidator.validaConsuntivi(dichNew);

		ArrayList<ConsuntivoDto> response = new ArrayList<>();
		saved.stream().forEach(c -> response.add(new ConsuntivoDto()
				.setCarburante(c.getTipoCarburante())
				.setId(c.getId())
				.setMotivazione(c.getMotivazione())
				.setQuantita(c.getQuantita().intValue())
				.setTipo(c.getTipoConsuntivo())));
		logger.info("Dichiarazione consuntivi ids  {}" , response.stream().map(ConsuntivoDto::getId).collect(Collectors.toList()));
		return response;
	}


	@Transactional
	public void deleteConsuntivo(Long id) {
		allegatiConsuntivoDao.deleteByConsuntivoModel_id(id);
		consuntiviConsumiDao.deleteById(id);
	}

	@Transactional
	public Long dichiaraAllegati(Long id, ConsuntivoDto consuntivo, List<ByteArrayResource> allegati) {
		DichiarazioneConsumiModel dichiarazioneConsumi = dichiarazioneConsumiDao.findById(id).orElseThrow(() -> new EntityNotFoundException(DICHIARAZIONE_NOT_FOUND.concat(String.valueOf(id))));

		// cancella allegati non presenti nel dto per quel consuntivo
		Optional<ConsuntivoConsumiModel> consuntivoOpt = consuntiviConsumiDao.findByDichiarazioneConsumiAndTipoCarburanteAndTipoConsuntivo(dichiarazioneConsumi,consuntivo.getCarburante(), consuntivo.getTipo());
		if (consuntivoOpt.isPresent()) {
			List<Long> idsAllegatiModel = consuntivoOpt.get().getAllegati().stream().map(AllegatoConsuntivoModel::getId)
					.filter(idAllegatoModel -> !consuntivo.getInfoAllegati().stream().map(InfoAllegatoConsuntivoDto::getId).collect(Collectors.toList()).contains(idAllegatoModel))
					.collect(Collectors.toList());

			allegatiConsuntivoDao.deleteByIdIn(idsAllegatiModel);
		}

		ConsuntivoConsumiModel consuntivoToSave = consuntivoOpt.isPresent() ? consuntivoOpt.get() : new ConsuntivoConsumiModel();
		consuntivoToSave.setDichiarazioneConsumi(dichiarazioneConsumi)
		.setMotivazione(consuntivo.getMotivazione())
		.setQuantita(new BigDecimal(consuntivo.getQuantita()))
		.setTipoCarburante(consuntivo.getCarburante())
		.setTipoConsuntivo(consuntivo.getTipo());

		ConsuntivoConsumiModel consutivoSalvato = consuntiviConsumiDao.save(consuntivoToSave);

		List<AllegatoConsuntivoModel> allegatiModel = new ArrayList<>();
		// se sto dichiarando ammissibile devo poter estrarre dal nome del file le proprietà: nomefile , tipoDocumento, descrizione. Per convenzione separate da $$
		if (TipoConsuntivo.AMMISSIBILE.equals(consutivoSalvato.getTipoConsuntivo())) {
			allegati.stream().forEach(allegato -> {
				List<String> collect = Arrays.stream(allegato.getFilename().split("\\$\\$")).collect(Collectors.toList());
				if (collect.size() != 3) {
					throw new IllegalArgumentException("Errore reperimento dati consuntivo ammissibile");
				}
				allegatiModel.add(new AllegatoConsuntivoModel()
						.setNomeFile(collect.get(0))
						.setTipoAllegato(TipoAllegatoConsuntivo.valueOf(collect.get(1)))
						.setDescrizione(collect.get(2))
						// .setDocumento(allegato.getByteArray())
						.setConsuntivoModel(consutivoSalvato));
				
				this.salvaAllegato(id, Calendar.YEAR, allegato, collect.get(0));
			});
		} else if (TipoConsuntivo.RECUPERO.equals(consutivoSalvato.getTipoConsuntivo())) {
			allegati.stream().forEach(allegato -> {
				allegatiModel.add(new AllegatoConsuntivoModel()
						.setNomeFile(allegato.getFilename())
						// .setDocumento(allegato.getByteArray())
						.setConsuntivoModel(consutivoSalvato));

				this.salvaAllegato(id, Calendar.YEAR, allegato, allegato.getFilename());
			});

		} else { // TipoConsuntivo.CONSUMATO || TipoConsuntivo.RIMANENZA
			throw new IllegalArgumentException(String.format("Non è possibile salvare allegati al consuntivo %s %s", consuntivo.getTipo(), consuntivo.getCarburante()));
		}

		// recupero gli allegati che non ho cancellato, e li unisco a quelli nuovi in allegatiModel. 
		List<AllegatoConsuntivoModel> allegatiPreEsistenti = allegatiConsuntivoDao.findAllById(consuntivo.getInfoAllegati().stream().map(InfoAllegatoConsuntivoDto::getId).collect(Collectors.toList()));
		final var all = Stream.concat(allegatiPreEsistenti.stream(), allegatiModel.stream()).collect(Collectors.toList());
		allegatiConsuntivoDao.saveAll(all);	
		consutivoSalvato.setAllegati(all);
		dichiarazioneConsumiDao.flush();

		if (TipoConsuntivo.AMMISSIBILE.equals(consutivoSalvato.getTipoConsuntivo())) {
			consuntiviConsumiValidator.validaAllegatiAmmissibile(dichiarazioneConsumi);
		} else if (TipoConsuntivo.RECUPERO.equals(consutivoSalvato.getTipoConsuntivo())) {
			consuntiviConsumiValidator.validaAllegatiRecupero(dichiarazioneConsumi);
		}
		return consutivoSalvato.getId();
	}
	
	protected String salvaAllegato(Long id, Integer anno, ByteArrayResource documento, String filename)
	{		
		try {
			if (id != null && anno != null && documento != null && filename != null) {
				Path filePath = Paths.get(this.pathDownload + SUB_DIRECTORY_RICHIESTE + "/" + 
						anno + "/allegati/" + filename);
				
				Path parentDir = filePath.getParent();
				if (!Files.exists(parentDir)) {
					Files.createDirectories(parentDir);
				}
				Files.write(filePath, documento.getByteArray(), StandardOpenOption.CREATE_NEW);
				return filename;
			}
			throw new SalvaDocumentoException ("Dati incompleti. Id: " + id + ", anno:" + anno + ", documento: " + 
					documento + ", filename: " + filename);
		}
		catch (IOException e) {
			throw new SalvaDocumentoException ("Errore nel salvataggio del file: " + filename);
		}
	}
	
}
