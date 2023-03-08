package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.DatiCatastaliModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.FabbricatoModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MacchinaModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.MacchinaMotorizzataModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.SerreModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.StrumentaliModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.DatiCatastaliDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.FabbricatiDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.FascicoloDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository.MacchinaDao;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.ReportValidazioneDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.ReportValidazioneFabbricatoDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.ReportValidazioneMacchinarioDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.business.service.FabbricatiAgsService;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.business.service.MacchineAgsService;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.FabbricatoAgsDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.FabbricatoAgsFilter;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.MacchinaAgsDto;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.legacy.dto.MacchinaAgsFilter;

@Service
public class DotazioneTecnicaService {

	private static final Logger logger = LoggerFactory.getLogger(DotazioneTecnicaService.class);

	@Autowired
	private FascicoloDao fascicoloDao;
	@Autowired
	private MacchinaDao macchinaDao;
	@Autowired
	private FabbricatiDao fabbricatiDao;
	@Autowired
	private MacchineAgsService macchineAgsService;
	@Autowired
	private FabbricatiAgsService fabbricatiAgsService;
	@Autowired
	private MacchineService macchineService;
	@Autowired
	private FabbricatiService fabbricatiService;
	@Autowired
	private DatiCatastaliDao datiCatastaliDao;

	public ReportValidazioneDto getReportValidazione(String cuaa) {
		var result = new ReportValidazioneDto().setMacchine(new ArrayList<>()).setFabbricati(new ArrayList<>());
		List<MacchinaModel> macchine = macchinaDao.findByFascicolo_cuaaAndFascicolo_idValidazione(cuaa, 0);
		List<FabbricatoModel> fabbricati = fabbricatiDao.findByFascicolo_cuaaAndFascicolo_idValidazione(cuaa, 0);

		if (CollectionUtils.isEmpty(macchine) && CollectionUtils.isEmpty(fabbricati)) {
			return result;
		}

		if (!CollectionUtils.isEmpty(macchine)) {
			macchine.stream()
					.forEach(macchina -> result.addMacchina(new ReportValidazioneMacchinarioDto()
							.setClasse(StringUtils.replace(macchina.getSottotipoMacchinario().getClassefunzionale().getTipologia().getDescrizione(), "_", " "))
							.setSottoClasse(StringUtils.replace(macchina.getSottotipoMacchinario().getDescrizione(), "_", " ")).setTarga(macchina.getTarga()).setTelaio(macchina.getNumeroTelaio())));
		}

		if (!CollectionUtils.isEmpty(fabbricati)) {
			fabbricati.stream()
					.forEach(fabbricato -> result.addFabbricato(new ReportValidazioneFabbricatoDto().setComune(fabbricato.getComune()).setSuperficie(fabbricato.getSuperficie())
							.setVolume(fabbricato.getVolume()).setTipologia(org.apache.commons.lang3.StringUtils.isBlank(fabbricato.getSottotipo().getTipologia().getDescrizione()) ? ""
									: StringUtils.replace(fabbricato.getSottotipo().getTipologia().getDescrizione(), "_", " "))));
		}

		return result;
	}

	@Transactional
	public void startValidazioneFascicolo(final String cuaa, final Integer idValidazione) {
		Optional<FascicoloModel> fascicoloLiveOpt = fascicoloDao.findByCuaaAndIdValidazione(cuaa, 0);
		if (!fascicoloLiveOpt.isPresent()) {
			return;
		}
		FascicoloModel fascicoloLive = fascicoloLiveOpt.get();
		FascicoloModel tmpFascicoloValidato = new FascicoloModel();
		tmpFascicoloValidato.setId(fascicoloLive.getId());
		tmpFascicoloValidato.setIdValidazione(idValidazione);
		tmpFascicoloValidato.setCuaa(cuaa);

		FascicoloModel fascicoloValidato = fascicoloDao.save(tmpFascicoloValidato);
		fascicoloValidato.ignoreValidazioneCheck();

		List<MacchinaModel> macchineLive = fascicoloLive.getMacchine();
		List<MacchinaModel> macchineValidate = macchineLive.stream().map(a -> startValidazioneMacchina(a, fascicoloValidato, idValidazione)).collect(Collectors.toList());

		List<FabbricatoModel> fabbricatiLive = fascicoloLive.getFabbricati();
		List<FabbricatoModel> fabbricatiValidati = fabbricatiLive.stream().map(a -> startValidazioneFabbricato(a, fascicoloValidato, idValidazione)).collect(Collectors.toList());

		fascicoloValidato.setMacchine(macchineValidate);
		fascicoloValidato.setFabbricati(fabbricatiValidati);
		fascicoloDao.save(fascicoloValidato);
	}

	private MacchinaModel startValidazioneMacchina(final MacchinaModel macchina, final FascicoloModel fascicoloValidatoModel, final Integer idValidazione) {

		MacchinaModel tmpMacchinaValidataModel;
		if (macchina instanceof MacchinaMotorizzataModel) {
			tmpMacchinaValidataModel = new MacchinaMotorizzataModel();
		} else {
			tmpMacchinaValidataModel = new MacchinaModel();
		}

		BeanUtils.copyProperties(macchina, tmpMacchinaValidataModel, "version");
		tmpMacchinaValidataModel.setFascicolo(fascicoloValidatoModel);
		tmpMacchinaValidataModel.setIdValidazione(idValidazione);

		return macchinaDao.save(tmpMacchinaValidataModel);
	}

	private FabbricatoModel startValidazioneFabbricato(final FabbricatoModel fabbricato, final FascicoloModel fascicoloValidatoModel, final Integer idValidazione) {

		FabbricatoModel tmpFabbricatoValidataModel;
		if (fabbricato instanceof StrumentaliModel) {
			tmpFabbricatoValidataModel = new StrumentaliModel();
		} else if (fabbricato instanceof SerreModel) {
			tmpFabbricatoValidataModel = new SerreModel();
		} else {
			tmpFabbricatoValidataModel = new FabbricatoModel();
		}

		BeanUtils.copyProperties(fabbricato, tmpFabbricatoValidataModel, "version");
		tmpFabbricatoValidataModel.setFascicolo(fascicoloValidatoModel);
		tmpFabbricatoValidataModel.setIdValidazione(idValidazione);

		FabbricatoModel fabbricatoValidato = fabbricatiDao.save(tmpFabbricatoValidataModel);
		fabbricatoValidato.ignoreValidazioneCheck();
		fabbricatoValidato.setDatiCatastali(validazioneDatiCatastali(fabbricato, fabbricatoValidato));

		return fabbricatoValidato;
	}

	private List<DatiCatastaliModel> validazioneDatiCatastali(FabbricatoModel fabbricatoLive, FabbricatoModel fabbricatoValidato) {
		List<DatiCatastaliModel> result = new ArrayList<>();
		List<DatiCatastaliModel> datiCatastaliLive = fabbricatoLive.getDatiCatastali();
		if (datiCatastaliLive != null && !datiCatastaliLive.isEmpty()) {
			datiCatastaliLive.forEach(datiLive -> {
				var datiCatastaliDaValidare = new DatiCatastaliModel();
				BeanUtils.copyProperties(datiLive, datiCatastaliDaValidare, "idValidazione", "fabbricato");
				datiCatastaliDaValidare.setIdValidazione(fabbricatoValidato.getIdValidazione());
				datiCatastaliDaValidare.setFabbricato(fabbricatoValidato);
				result.add(datiCatastaliDao.save(datiCatastaliDaValidare));
			});
		}
		return result;
	}

	@Transactional
	public void migraMacchine(final String cuaa) throws Exception {
		MacchinaAgsFilter filter = new MacchinaAgsFilter();
		filter.setCuaa(cuaa);
		filter.setData(LocalDateTime.now());
		List<MacchinaAgsDto> macchineAgs = macchineAgsService.getMacchine(filter);
		logger.debug("macchine trovate: " + macchineAgs.size());
		// inserire le macchine recuperate in A4G
		for (MacchinaAgsDto m : macchineAgs) {
			try {
				macchineService.migra(cuaa, m);
			} catch (IllegalArgumentException iae) {
				logger.error(iae.getMessage());
			}
		}
	}

	@Transactional
	public void migraFabbricati(final String cuaa) throws Exception {
		logger.debug("inizio migrazione fabbricati di " + cuaa);
		FabbricatoAgsFilter filter = new FabbricatoAgsFilter();
		filter.setCuaa(cuaa);
		filter.setData(LocalDateTime.now());
		List<FabbricatoAgsDto> fabbricatiAgs = fabbricatiAgsService.getFabbricati(filter);
		logger.debug("fabbricati trovati: " + fabbricatiAgs.size());
		for (FabbricatoAgsDto fabb : fabbricatiAgs) {
			try {
				fabbricatiService.migra(cuaa, fabb);
			} catch (IllegalArgumentException iae) {
				logger.error(iae.getMessage());
			} catch (Throwable e) {
				logger.error("Errore inatteso per il cuaa [" + cuaa + "] e fabbricato [" + fabb.getIdAgs() + "]", e);
				throw e;
			}
		}
	}
}
