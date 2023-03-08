package it.tndigitale.a4gistruttoria.service.businesslogic.configurazioneistruttoria;

import it.tndigitale.a4gistruttoria.dto.DatiConfigurazioneAccoppiati;
import it.tndigitale.a4gistruttoria.dto.InterventoDuPremio;
import it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria.ConfIstruttoriaDisaccoppiatoDto;
import it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria.ConfIstruttoriaRicevibilitaDto;
import it.tndigitale.a4gistruttoria.dto.configurazioneistruttoria.ConfIstruttorieDto;
import it.tndigitale.a4gistruttoria.repository.dao.*;
import it.tndigitale.a4gistruttoria.repository.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.tndigitale.a4gistruttoria.service.businesslogic.configurazioneistruttoria.InterventoDuPremioBuilder.from;

@Service
public class ConfigurazioneIstruttoriaService {

	private static Logger logger = LoggerFactory.getLogger(ConfigurazioneIstruttoriaService.class);

	@Autowired

	private ImportoUnitarioInterventoDao importoUnitarioInterventoDao;
	@Autowired
	private InterventoDao interventoDuDao;
	@Autowired
	private ConfigurazioneIstruttoriaDisaccoppiatoDao configurazioneIstruttoriaDisaccoppiatoDao;
	@Autowired
	private ConfigurazioneRicevibilitaDao configurazioneIstruttoriaRicevibilitaDao;
	@Autowired
	private ConfigurazioneIstruttoriaDao configurazioneIstruttoriaDao;

	public DatiConfigurazioneAccoppiati getImportiUnitari(Integer annoCampagna, Sostegno sostegno) {
		if (sostegno == null || annoCampagna == null) {
			logger.error("Nessun sostegno/annoCampagna fornito in input, impossibile recuperare i premi.");
			throw new IllegalArgumentException("Nessun sostegno fornito in input, impossibile recuperare i premi.");
		}
		DatiConfigurazioneAccoppiati datiDettaglio = new DatiConfigurazioneAccoppiati();
		List<InterventoDuPremio> premi = new ArrayList<>();
		List<ImportoUnitarioInterventoModel> importiUnitari = importoUnitarioInterventoDao
				.findByCampagnaAndSostegno(annoCampagna, sostegno);
		// aggiunta interventi non presenti nel db
		Stream<String> streamOfCod = Sostegno.SUPERFICIE.equals(sostegno) ? InterventoSuperficie.streamOfCodiciAgea()
				: InterventoZootecnico.streamOfCodiciAgea();
		streamOfCod.forEach(codAgea -> {
			premi.add(importiUnitari.stream().filter(imp -> imp.getIntervento().getCodiceAgea().equals(codAgea))
					.findFirst().map(imp -> {
						InterventoDuPremio interventoDuPremio = from(codAgea, sostegno);
						interventoDuPremio.setValoreUnitarioIntervento(imp.getValoreUnitario());
						interventoDuPremio.setPriorita(imp.getPriorita());
						interventoDuPremio.setId(imp.getId());
						return interventoDuPremio;
					}).orElseGet(() -> from(codAgea, sostegno)));
		});
		// Ordinamento premi per le labels
		premi.sort((m1, m2) -> Integer.valueOf(m1.getIntervento().getCodiceAgea())
				.compareTo(Integer.valueOf(m2.getIntervento().getCodiceAgea())));
		datiDettaglio.setInterventoDuPremi(premi);
		return datiDettaglio;
	}

	public DatiConfigurazioneAccoppiati saveOrUpdateConfImportiUnitari(Integer annoCampagna,
			DatiConfigurazioneAccoppiati datiConfigurazioneAccoppiati) {
		datiConfigurazioneAccoppiati.setInterventoDuPremi(
				datiConfigurazioneAccoppiati.getInterventoDuPremi().stream().map(interventoDuPremio -> {
					ImportoUnitarioInterventoModel impUnitario = Optional.ofNullable(interventoDuPremio.getId())
							// ricerca nel DB altrimenti ne crea uno nuovo
							.map(id -> importoUnitarioInterventoDao.getOne(interventoDuPremio.getId()))
							// creazione nuovo ImportoUnitarioInterventoModel settandogli il rispettivo
							// intervento
							.orElseGet(() -> {
								ImportoUnitarioInterventoModel importoUnitarioInterventoModel = new ImportoUnitarioInterventoModel();
								importoUnitarioInterventoModel.setIntervento(interventoDuDao
										.findByCodiceAgea(interventoDuPremio.getIntervento().getCodiceAgea()));
								return importoUnitarioInterventoModel;
							});
					impUnitario.setValoreUnitario(interventoDuPremio.getValoreUnitarioIntervento());
					impUnitario.setPriorita(interventoDuPremio.getPriorita());
					impUnitario.setCampagna(annoCampagna);
					ImportoUnitarioInterventoModel importoUnitarioInterventoUpdated = importoUnitarioInterventoDao
							.save(impUnitario);
					InterventoDuPremio interventoDuPremioUpdated = from(
							importoUnitarioInterventoUpdated.getIntervento().getCodiceAgea(),
							importoUnitarioInterventoUpdated.getIntervento().getSostegno());
					interventoDuPremioUpdated
							.setValoreUnitarioIntervento(importoUnitarioInterventoUpdated.getValoreUnitario());
					interventoDuPremioUpdated.setPriorita(importoUnitarioInterventoUpdated.getPriorita());
					interventoDuPremioUpdated.setId(importoUnitarioInterventoUpdated.getId());
					return interventoDuPremioUpdated;
				}).collect(Collectors.toList()));
		return datiConfigurazioneAccoppiati;
	}

	public ConfIstruttoriaDisaccoppiatoDto getConfIstruttoriaDisaccoppiato(Integer annoCampagna) {
		return configurazioneIstruttoriaDisaccoppiatoDao.findByCampagna(annoCampagna).map(conf -> {
			ConfIstruttoriaDisaccoppiatoDto confDto = new ConfIstruttoriaDisaccoppiatoDto();
			BeanUtils.copyProperties(conf, confDto);
			return confDto;
		}).orElse(null);
	}

	public ConfIstruttoriaDisaccoppiatoDto saveOrUpdateConfIstruttoriaDisaccoppiato(
			ConfIstruttoriaDisaccoppiatoDto confDto) {
		ConfigurazioneIstruttoriaDisaccoppiatoModel conf = Optional.ofNullable(confDto.getId())
				.map(id -> configurazioneIstruttoriaDisaccoppiatoDao.getOne(confDto.getId()))
				.orElse(new ConfigurazioneIstruttoriaDisaccoppiatoModel());
		BeanUtils.copyProperties(confDto, conf);
		conf = configurazioneIstruttoriaDisaccoppiatoDao.save(conf);
		BeanUtils.copyProperties(conf, confDto);
		return confDto;
	}

	public ConfIstruttoriaRicevibilitaDto getConfIstruttoriaRicevibilita(Integer annoCampagna) {
		return configurazioneIstruttoriaRicevibilitaDao.findByCampagna(annoCampagna).map(configurazione -> {
			ConfIstruttoriaRicevibilitaDto configurazioneRicDto = new ConfIstruttoriaRicevibilitaDto();
			BeanUtils.copyProperties(configurazione, configurazioneRicDto);
			return configurazioneRicDto;
		}).orElse(null);
	}

	public ConfIstruttoriaRicevibilitaDto saveOrUpdateConfIstruttoriaRicevibilita(
			ConfIstruttoriaRicevibilitaDto confDto) {
		ConfigurazioneRicevibilitaModel conf = Optional.ofNullable(confDto.getId())
				.map(id -> configurazioneIstruttoriaRicevibilitaDao.getOne(confDto.getId()))
				.orElse(new ConfigurazioneRicevibilitaModel());
		BeanUtils.copyProperties(confDto, conf);
		conf = configurazioneIstruttoriaRicevibilitaDao.save(conf);
		BeanUtils.copyProperties(conf, confDto);
		return confDto;
	}
	
	public ConfIstruttorieDto getConfIstruttorie(Integer annoCampagna) {
		return configurazioneIstruttoriaDao.findByCampagna(annoCampagna).map(configurazione -> {
			ConfIstruttorieDto configurazioneIstDto = new ConfIstruttorieDto();
			BeanUtils.copyProperties(configurazione, configurazioneIstDto);
			return configurazioneIstDto;
		}).orElse(null);
	}

	public ConfIstruttorieDto saveOrUpdateConfIstruttorie(
			ConfIstruttorieDto confDto) {
		ConfigurazioneIstruttoriaModel conf = Optional.ofNullable(confDto.getId())
				.map(id -> configurazioneIstruttoriaDao.getOne(confDto.getId()))
				.orElse(new ConfigurazioneIstruttoriaModel());
		BeanUtils.copyProperties(confDto, conf);
		conf = configurazioneIstruttoriaDao.save(conf);
		BeanUtils.copyProperties(conf, confDto);
		return confDto;
	}
}
