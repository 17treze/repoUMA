package it.tndigitale.a4g.uma.business.service.richiesta;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbricatoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.PrelievoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;
import it.tndigitale.a4g.uma.business.persistence.entity.UtilizzoMacchinariModel;
import it.tndigitale.a4g.uma.business.persistence.repository.FabbisognoDao;
import it.tndigitale.a4g.uma.business.persistence.repository.FabbricatiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.PrelieviDao;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.persistence.repository.UtilizzoMacchinariDao;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaDotazioneTecnicaClient;
import it.tndigitale.a4g.uma.business.service.lavorazioni.RecuperaLavorazioniFabbricati;
import it.tndigitale.a4g.uma.dto.aual.FabbricatoAualDto;
import it.tndigitale.a4g.uma.dto.aual.FascicoloAualDto;
import it.tndigitale.a4g.uma.dto.aual.MacchinaAualDto;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDtoBuilder;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteCompletoDto;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteRichiestoDto;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteTotale;
import it.tndigitale.a4g.uma.dto.richiesta.PrelievoDto;
import it.tndigitale.a4g.uma.dto.richiesta.PresentaRichiestaDto;
import it.tndigitale.a4g.uma.dto.richiesta.builder.PrelievoBuilder;

@Service
public class RichiestaCarburanteService {

	private static final Logger logger = LoggerFactory.getLogger(RichiestaCarburanteService.class);

	@Autowired
	private RichiestaCarburanteValidator richiestaCarburanteValidator;
	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private UtilizzoMacchinariDao macchineDao;
	@Autowired
	private FabbisognoDao fabbisognoDao;
	@Autowired
	private FabbricatiDao fabbricatiDao;
	@Autowired
	private Clock clock;
	@Autowired
	private UmaAnagraficaClient anagraficaClient;
	@Autowired
	private UmaDotazioneTecnicaClient dotazioneTecnicaClient;
	@Autowired
	private RecuperaLavorazioniFabbricati recuperaLavorazioniFabbricati;
	@Autowired
	private PrelieviDao prelieviDao;

	@Transactional
	public Long presenta(PresentaRichiestaDto presentaRichiestaDto) {
		// richiestaCarburanteValidator.validaPresentazioneRichiesta(presentaRichiestaDto.getCuaa());

		FascicoloAualDto fascicoloAgs = new FascicoloAualDto();
		
		// anagraficaClient.getFascicolo(presentaRichiestaDto.getCuaa());
		fascicoloAgs.setCodiCuaa(presentaRichiestaDto.getCuaa());
		fascicoloAgs.setDescDeno(presentaRichiestaDto.getCuaa());
		
		// Reperisco la detenzione del fascicolo ags
//		var det = fascicoloAgs.getDetenzioni().stream()
//				.filter(detenzione -> detenzione.getTipoDetenzione().equals(TipoDetenzioneEnum.MANDATO))
//				.findFirst()
//				.orElseGet(() -> fascicoloAgs.getDetenzioni().stream()
//						.filter(detenzione -> fascicoloAgs.getDetenzioni().size() == 1 && detenzione.getTipoDetenzione().equals(TipoDetenzioneEnum.DELEGA))
//						.findFirst()
//						.orElseThrow(() -> new IllegalArgumentException("Errore nel reperimento della detenzione")));

		var richiestaDaSalvare = new RichiestaCarburanteModel()
				.setCuaa(presentaRichiestaDto.getCuaa())
				.setCfRichiedente(presentaRichiestaDto.getCodiceFiscaleRichiedente())
				.setDataPresentazione(clock.now())
				.setCampagna(Long.valueOf(clock.now().getYear()))
				.setStato(StatoRichiestaCarburante.IN_COMPILAZIONE)
				.setDenominazione(fascicoloAgs.getDescDeno()) // reperisco la denominazione del fascicolo da anagrafica legacy
//				.setEntePresentatore(det.getSportello())
				;
				
		RichiestaCarburanteModel richiestaSalvata = richiestaCarburanteDao.save(richiestaDaSalvare);

		try {
			// importazione macchine dal fascicolo 
			macchineDao.saveAll(getMacchineFromAual(presentaRichiestaDto.getCuaa(), richiestaSalvata));
			// importazione fabbricati dal fascicolo
			fabbricatiDao.saveAll(getFabbricatiFromAual(presentaRichiestaDto.getCuaa(), richiestaSalvata));
		}
		catch (MalformedURLException e) {
			logger.error(e.getMessage());
		}
		catch (IOException e) {
			logger.error(e.getMessage());
		}

		logger.info("[UMA] - Creazione richiesta carburante - ID {} - CUAA: {} RICHIEDENTE: {}", richiestaSalvata.getId(), richiestaSalvata.getCuaa(), richiestaSalvata.getCfRichiedente());
		return richiestaSalvata.getId();
	}

	public Long aggiorna(Long id, CarburanteRichiestoDto carburanteRichiestoDto) {

		// Reperisco la domanda UMA
		RichiestaCarburanteModel richiesta = getRichiestaOrThrowException(id);
		// Set dei dati del fabbisogno
		CarburanteCompletoDto carburante = carburanteRichiestoDto.getCarburanteRichiesto();

		richiesta
		.setBenzina(carburante.getBenzina())
		.setGasolio(carburante.getGasolio())
		.setGasolioSerre(carburante.getGasolioSerre())
		.setGasolioTerzi(carburante.getGasolioTerzi())
		.setNote(carburanteRichiestoDto.getNote() != null ? carburanteRichiestoDto.getNote().trim() : null);

		// Aggiornamento della richiesta
		richiestaCarburanteDao.save(richiesta);
		logger.info("[UMA] - Aggioramento richiesta carburante: carburante richiesto - ID Richiesta UMA: {}", id);
		return richiesta.getId();
	}

	@Transactional
	public void cancellaRichiesta(Long id) {
		Optional<RichiestaCarburanteModel> richiestaOpt = richiestaCarburanteDao.findById(id);
		if (richiestaOpt.isPresent() && StatoRichiestaCarburante.IN_COMPILAZIONE.equals(richiestaOpt.get().getStato())) {
			RichiestaCarburanteModel richiesta = richiestaOpt.get();
			// cancella macchine
			macchineDao.deleteByRichiestaCarburante(richiesta);
			// cancella fabbisogni 
			fabbisognoDao.deleteByRichiestaCarburante(richiesta);
			// cancella fabbricati annessi
			fabbricatiDao.deleteByRichiestaCarburante(richiesta);
			// cancella domanda 
			richiestaCarburanteDao.deleteById(richiesta.getId());
			logger.info("[UMA] - Cancellazione Richiesta Carburante CUAA {} , anno {} " ,richiesta.getCuaa(), richiesta.getCampagna());
		}
	}

	public void valida(Long id) {
		RichiestaCarburanteModel richiesta = getRichiestaOrThrowException(id);
		richiestaCarburanteValidator.validaProtocollazione(richiesta);
	}

	public CarburanteDto calcolaCarburanteAmmissibile(Long id) {
		RichiestaCarburanteModel richiesta = getRichiestaOrThrowException(id);
		return new CarburanteConverter(richiesta.getCampagna()).calcola(richiesta.getFabbisogni()).round().build();
	}

	public CarburanteTotale<PrelievoDto> getPrelievi(String cuaa, Long campagna, LocalDateTime dataPresentazione) {
		List<PrelievoModel> prelievi = prelieviDao.findByRichiestaCarburante_cuaaAndRichiestaCarburante_campagna(cuaa, campagna);
		
		// Se presente la data di Presentazione, restituisco i soli prelievi fino a quella data relativi alla campagna in corso
		if (dataPresentazione != null && !CollectionUtils.isEmpty(prelievi)) {
			List<PrelievoModel> prelieviFinoAllaDataDiPresentazione = prelievi
			.stream()
			.filter(prelievo -> prelievo.getData().getYear() == campagna 
					&& (prelievo.getData().isBefore(dataPresentazione) || prelievo.getData().isEqual(dataPresentazione)))
			.collect(Collectors.toList());
			if (CollectionUtils.isEmpty(prelieviFinoAllaDataDiPresentazione)) { 
				return new CarburanteTotale<>(); 
			} else {
				prelievi.clear();
				prelievi.addAll(prelieviFinoAllaDataDiPresentazione);
			}
		}

		if (CollectionUtils.isEmpty(prelievi)) { return new CarburanteTotale<>(); }

		List<PrelievoDto> prelieviDto = prelievi.stream().map(p -> new PrelievoBuilder()
				.newDto()
				.withDistributore(p.getDistributore())
				.withPrelievo(p)
				.build()).collect(Collectors.toList());

		CarburanteDto totale = new CarburanteDtoBuilder()
				.from(prelieviDto.stream().map(PrelievoDto::getCarburante).collect(Collectors.toList()))
				.build();
		
		return new CarburanteTotale<PrelievoDto>()
				.setDati(prelieviDto)
				.setTotale(totale);
	}

	private RichiestaCarburanteModel getRichiestaOrThrowException(Long idRichiesta) {
		return richiestaCarburanteDao
				.findById(idRichiesta)
				.orElseThrow(() -> new EntityNotFoundException("Richiesta con id: ".concat(String.valueOf(idRichiesta)).concat(" non trovata")));
	}

//	// al momento di creazione della richiesta di carburante importa i fabbricati significativi per cui si portrebbe richiedere carburante
//	private List<FabbricatoModel> getFabbricatiFromAgs(String cuaa, RichiestaCarburanteModel richiesta) {
//
//		List<FabbricatoAgsDto> fabbricatiAgs = dotazioneTecnicaClient.getFabbricati(cuaa, clock.now());
//		List<FabbricatoModel> fabbricatiToSave = new ArrayList<>();
//
//		if (CollectionUtils.isEmpty(fabbricatiAgs)) {
//			return new ArrayList<>();
//		}
//
//		fabbricatiAgs.stream()
//		.collect(Collectors.groupingBy(recuperaLavorazioniFabbricati.tipoToFabbricatoGruppoModel))
//		.forEach((tipoFabbricato, fabbricati) -> {
//			if (tipoFabbricato.isPresent()) {
//				fabbricati.forEach(f ->
//				fabbricatiToSave.add(new FabbricatoModel()
//						.setTipoFabbricato(tipoFabbricato.get())
//						.setRichiestaCarburante(richiesta)
//						.setComune(f.getComune())
//						.setIdentificativoAgs(f.getIdAgs())
//						.setParticella(f.getParticella())
//						.setVolume(f.getVolume())
//						.setSubalterno(f.getSubalterno())
//						.setProvincia(f.getProvincia())
//						.setSiglaProvincia(f.getSiglaProvincia())));
//			}
//		});
//		return fabbricatiToSave;
//	}
	// al momento di creazione della richiesta di carburante importa i fabbricati significativi per cui si portrebbe richiedere carburante
	private List<FabbricatoModel> getFabbricatiFromAual(String cuaa, RichiestaCarburanteModel richiesta)
		throws MalformedURLException, IOException {

		List<FabbricatoModel> fabbricatiToSave = new ArrayList<>();
		
        List<FabbricatoAualDto> fabbricatiAual = dotazioneTecnicaClient.getFabbricati(cuaa);
        
		if (CollectionUtils.isEmpty(fabbricatiAual)) {
			return new ArrayList<>();
		}

		fabbricatiAual.stream()
		.collect(Collectors.groupingBy(recuperaLavorazioniFabbricati.tipoAualToFabbricatoGruppoModel))
		.forEach((tipoFabbricato, fabbricati) -> {
			if (tipoFabbricato.isPresent()) {
				fabbricati.forEach(f ->
				fabbricatiToSave.add(new FabbricatoModel()
						.setTipoFabbricato(tipoFabbricato.get())
						.setRichiestaCarburante(richiesta)
						.setComune(f.getCodiComu())
						.setIdentificativoAgs(f.getCodiFabb())
						.setParticella(f.getDescPart())
						.setVolume(Integer.parseInt(f.getNumeVolu()))
						.setSubalterno(f.getDescSuba())
						.setProvincia(f.getCodiProv())
						//.setSiglaProvincia(f.getSiglaProvincia())
						));
			}
		});
		return fabbricatiToSave;
	}
	
//	// al momento della crezione della richiesta di carburante importa le macchine che è possibile utilizzare per richiedere carburante
//	private List<UtilizzoMacchinariModel> getMacchineFromAgs(String cuaa, RichiestaCarburanteModel richiesta) {
//		List<MacchinaAgsDto> macchineAgs = dotazioneTecnicaClient.getMacchine(cuaa, clock.now());
//		return macchineAgs.stream().map(macchinaAgs -> new UtilizzoMacchinariModel()
//				.setFlagUtilizzo(false)
//				.setRichiestaCarburante(richiesta)
//				.setAlimentazione(TipoCarburante.valueOf(macchinaAgs.getAlimentazione().name()))
//				.setClasse(macchinaAgs.getClasse())
//				.setDescrizione(macchinaAgs.getDescrizione())
//				.setIdentificativoAgs(macchinaAgs.getIdAgs())
//				.setMarca(macchinaAgs.getMarca())
//				.setPossesso(macchinaAgs.getPossesso())
//				.setTarga(macchinaAgs.getTarga()))
//				.collect(Collectors.toList());
//	}
	// al momento della crezione della richiesta di carburante importa le macchine che è possibile utilizzare per richiedere carburante
	private List<UtilizzoMacchinariModel> getMacchineFromAual(String cuaa, RichiestaCarburanteModel richiesta) 
			throws MalformedURLException, IOException {
		
        List<MacchinaAualDto> macchineAual = dotazioneTecnicaClient.getMacchine(cuaa);
        logger.info("N.macchine: " + macchineAual.size());
        return macchineAual.stream().map(macchinaAual -> new UtilizzoMacchinariModel()
				.setFlagUtilizzo(false)
				.setRichiestaCarburante(richiesta)
				.setAlimentazione(TipoCarburante.BENZINA) // valueOf(macchinaAual.getAlimentazione().name()))
				// .setClasse(macchinaAgs.getClasse())
				.setDescrizione(macchinaAual.getDescMode())
				.setIdentificativoAgs(macchinaAual.getCodiMacc())
				.setMarca(macchinaAual.getDescMarc())
				// .setPossesso(macchinaAual.getPossesso())
				.setTarga(macchinaAual.getDescTarg()))
				.collect(Collectors.toList());
	}
}
