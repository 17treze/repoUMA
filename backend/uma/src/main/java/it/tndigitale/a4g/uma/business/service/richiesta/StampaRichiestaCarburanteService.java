package it.tndigitale.a4g.uma.business.service.richiesta;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.entity.TipologiaLavorazione;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.service.client.UmaProxyClient;
import it.tndigitale.a4g.uma.business.service.consumi.calcoli.CarburanteHelper;
import it.tndigitale.a4g.uma.business.service.lavorazioni.LavorazioniService;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteCompletoDto;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteTotale;
import it.tndigitale.a4g.uma.dto.richiesta.DichiarazioneDto;
import it.tndigitale.a4g.uma.dto.richiesta.DichiarazioneFabbricatoDto;
import it.tndigitale.a4g.uma.dto.richiesta.FabbricatoDto;
import it.tndigitale.a4g.uma.dto.richiesta.LavorazioneDto;
import it.tndigitale.a4g.uma.dto.richiesta.LavorazioneFilter;
import it.tndigitale.a4g.uma.dto.richiesta.MacchinaDto;
import it.tndigitale.a4g.uma.dto.richiesta.PrelievoDto;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;
import it.tndigitale.a4g.uma.dto.richiesta.builder.MacchinaUmaBuilder;
import it.tndigitale.a4g.uma.dto.richiesta.stampa.StampaLavorazioneDto;
import it.tndigitale.a4g.uma.dto.richiesta.stampa.StampaRaggruppamentoLavorazioniDto;
import it.tndigitale.a4g.uma.dto.richiesta.stampa.StampaRichiestaCarburanteDto;
import it.tndigitale.a4g.uma.dto.richiesta.stampa.StampaTipoLavorazioneDto;

@Service
public class StampaRichiestaCarburanteService {

	private static final String TEMPLATE_PATH = "resources/templateRichiestaCarburante.docx";

	@Autowired
	private LavorazioniService lavorazioniService;
	@Autowired
	private RichiestaCarburanteService richiestaCarburanteService;
	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private CarburanteHelper carburanteHelper;
	@Autowired
	private UmaProxyClient proxyClient;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private Clock clock;
	@Autowired
	private RicercaRichiestaCarburanteService ricercaRichiestaCarburanteService;


	public Resource stampaRichiestaCarburante(Long idRichiesta) throws IOException {
		RichiestaCarburanteModel richiestaModel = richiestaCarburanteDao.findById(idRichiesta).orElseThrow(() -> new EntityNotFoundException("Richiesta con id: ".concat(String.valueOf(idRichiesta)).concat("non trovata")));
		
		// Se la richiesta è autorizzata oppure rettificata, viene scaricato il documento caricato in fase di protocollazione
		if (richiestaModel.getStato().equals(StatoRichiestaCarburante.AUTORIZZATA) || richiestaModel.getStato().equals(StatoRichiestaCarburante.RETTIFICATA)) {
			return new ByteArrayResource(richiestaModel.getDocumento());
		}
		
		var json = objectMapper.writeValueAsString(buildStampaDto(richiestaModel));
		return new ByteArrayResource(proxyClient.stampa(json, TEMPLATE_PATH));
	}

	private StampaRichiestaCarburanteDto buildStampaDto(RichiestaCarburanteModel richiestaModel) {
		var stampaDto = new StampaRichiestaCarburanteDto();
		var idRichiesta = richiestaModel.getId();
		
		boolean isRettifica = ricercaRichiestaCarburanteService.getIdRettificata(richiestaModel.getCuaa(), richiestaModel.getCampagna(), richiestaModel.getDataPresentazione()).isPresent();
		
		// Rettifica
		stampaDto.setIsRettifica(isRettifica);

		//Anagrafica
		stampaDto.setIdRichiesta(richiestaModel.getId());
		stampaDto.setCampagna(richiestaModel.getCampagna());
		stampaDto.setCuaa(richiestaModel.getCuaa());
		stampaDto.setDenominazione(richiestaModel.getDenominazione().equals("NO_DENOM") ? null : richiestaModel.getDenominazione());

		//Macchine
		if (!CollectionUtils.isEmpty(richiestaModel.getMacchine())) {
			List<MacchinaDto> macchineDto = new MacchinaUmaBuilder().from(richiestaModel.getMacchine());
			// Stampiamo solo le macchine utilizzate
			stampaDto.setMacchine(macchineDto.stream().filter(MacchinaDto::getIsUtilizzata).collect(Collectors.toList()));
		}

		// Filtro i raggruppamenti per i soli aventi fabbisogni dichiarati
		// Costruisco i dto di stampa in base ai raggruppamenti trovati

		// Lavorazioni Superficie
		List<RaggruppamentoLavorazioniDto> raggSup = lavorazioniService.getCategorieLavorazioni(idRichiesta, AmbitoLavorazione.SUPERFICIE);
		List<DichiarazioneDto> dichSup = lavorazioniService.getFabbisogni(idRichiesta, LavorazioneFilter.Lavorazioni.SUPERFICIE);

		List<RaggruppamentoLavorazioniDto> raggSupFiltered = filterRaggruppramenti(raggSup, dichSup);

		// Raggruppamento Lavorazioni Superficie
		stampaDto.setRaggruppamentoLavorazioneSuperficie(buildRaggDto(raggSupFiltered, dichSup, false));

		// Lavorazioni Altro
		List<RaggruppamentoLavorazioniDto> raggAltro = lavorazioniService.getCategorieLavorazioni(idRichiesta, AmbitoLavorazione.ALTRO);
		List<DichiarazioneDto> dichAltro = lavorazioniService.getFabbisogni(idRichiesta, LavorazioneFilter.Lavorazioni.ALTRO);

		List<RaggruppamentoLavorazioniDto> raggAltroFiltered = filterRaggruppramenti(raggAltro, dichAltro);

		// Raggruppamento Lavorazioni Altro
		stampaDto.setRaggruppamentoLavorazioneAltro(buildRaggDto(raggAltroFiltered, dichAltro, false));

		// Lavorazioni Zootecnia
		List<RaggruppamentoLavorazioniDto> raggZoo = lavorazioniService.getCategorieLavorazioni(idRichiesta, AmbitoLavorazione.ZOOTECNIA);
		List<DichiarazioneDto> dichZoo = lavorazioniService.getFabbisogni(idRichiesta, LavorazioneFilter.Lavorazioni.ZOOTECNIA);

		List<RaggruppamentoLavorazioniDto> raggZooFiltered = filterRaggruppramenti(raggZoo, dichZoo);

		// Raggruppamento Lavorazioni Zootecnia
		stampaDto.setRaggruppamentoLavorazioneZootecnia(buildRaggDto(raggZooFiltered, dichZoo, true));

		// Filtro i raggruppamenti per i soli aventi fabbricati con fabbisogni dichiarati	
		// Costruisco i dto di stampa in base ai raggruppamenti trovati

		// Lavorazioni Serre
		List<RaggruppamentoLavorazioniDto> raggSerre = lavorazioniService.getCategorieLavorazioni(idRichiesta, AmbitoLavorazione.SERRE);
		List<DichiarazioneFabbricatoDto> dichSerre = lavorazioniService.getFabbisogniFabbricati(idRichiesta, LavorazioneFilter.LavorazioniFabbricati.SERRE);

		List<RaggruppamentoLavorazioniDto> raggSerreFiltered = filterRaggruppramentiFabbricati(raggSerre, dichSerre);

		// Raggruppamento Lavorazioni Serre
		stampaDto.setRaggruppamentoLavorazioneSerre(buildRaggruppamentoDtoFabbricati(raggSerreFiltered, dichSerre, AmbitoLavorazione.SERRE));

		// Lavorazioni Fabbricati
		List<RaggruppamentoLavorazioniDto> raggFab = lavorazioniService.getCategorieLavorazioni(idRichiesta, AmbitoLavorazione.FABBRICATI);
		List<DichiarazioneFabbricatoDto> dichFab = lavorazioniService.getFabbisogniFabbricati(idRichiesta, LavorazioneFilter.LavorazioniFabbricati.FABBRICATI);

		List<RaggruppamentoLavorazioniDto> raggFabFiltered = filterRaggruppramentiFabbricati(raggFab, dichFab);

		// Raggruppamento Lavorazioni Fabbricati
		stampaDto.setRaggruppamentoLavorazioneFabbricati(buildRaggruppamentoDtoFabbricati(raggFabFiltered, dichFab, AmbitoLavorazione.FABBRICATI));

		// Fabbisogno
		// Residuo
		stampaDto.setResiduo(carburanteHelper.getResiduoAnnoPrecedente(richiestaModel.getCuaa(), richiestaModel.getCampagna()));

		// Ammissibile
		stampaDto.setAmmissibile(richiestaCarburanteService.calcolaCarburanteAmmissibile(richiestaModel.getId()));

		// Richiesto
		stampaDto.setRichiesto(new CarburanteCompletoDto()
				.setBenzina(richiestaModel.getBenzina())
				.setGasolio(richiestaModel.getGasolio())
				.setGasolioSerre(richiestaModel.getGasolioSerre())
				.setGasolioTerzi(richiestaModel.getGasolioTerzi()));
		stampaDto.setNote(richiestaModel.getNote());
		
		// Prelevato
		if (isRettifica) {
			CarburanteTotale<PrelievoDto> prelievi = richiestaCarburanteService.getPrelievi(richiestaModel.getCuaa(), richiestaModel.getCampagna(), richiestaModel.getDataPresentazione());
			stampaDto.setPrelevato(new CarburanteDto()
					.setBenzina(prelievi.getTotale().getBenzina())
					.setGasolioSerre(prelievi.getTotale().getGasolioSerre())
					.setGasolio(prelievi.getTotale().getGasolio()));
		}
		

		stampaDto.setData(clock.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

		return stampaDto;
	}

	private List<StampaRaggruppamentoLavorazioniDto> buildRaggDto(List<RaggruppamentoLavorazioniDto> raggruppamenti, List<DichiarazioneDto> dichiarazioni, Boolean zootecnia) {
		List<StampaRaggruppamentoLavorazioniDto> stampaRaggruppamentoLavorazioniDto = new ArrayList<>();
		if (!CollectionUtils.isEmpty(raggruppamenti)) {
			raggruppamenti.forEach(raggruppamento -> {
				var stampaRaggruppamento = new StampaRaggruppamentoLavorazioniDto();
				stampaRaggruppamento.setNome(raggruppamento.getNome());
				stampaRaggruppamento.setIndice(raggruppamento.getIndice());

				List<StampaTipoLavorazioneDto> stampaTipoLavorazioneDtoList = new ArrayList<>();
				Map<TipologiaLavorazione, List<LavorazioneDto>> tipiLavorazione = raggruppamento.getLavorazioni().stream().collect(Collectors.groupingBy(LavorazioneDto::getTipologia));

				tipiLavorazione.keySet().forEach(tipo -> {
					var stampaTipoLavorazioneDto = new StampaTipoLavorazioneDto();
					stampaTipoLavorazioneDto.setNome(tipo.getDescrizione());
					stampaTipoLavorazioneDto.setSuperficieMassima(raggruppamento.getSuperficieMassima());

					List<StampaLavorazioneDto> stampaLavorazioneDtoList = new ArrayList<>();
					tipiLavorazione.getOrDefault(tipo, new ArrayList<>()).forEach(lavorazione -> {
						Optional<DichiarazioneDto> dichiarazioneToBind = dichiarazioni.stream().filter(dichiarazione -> dichiarazione.getLavorazioneId().equals(lavorazione.getId())).findFirst();
						if (dichiarazioneToBind.isPresent()) {
							var stampaLavorazioneDto = new StampaLavorazioneDto();
							stampaLavorazioneDto
							.setNome(lavorazione.getNome())
							.setIndice(lavorazione.getIndice())
							.setTipologia(lavorazione.getTipologia().getDescrizione())
							.setUnitaDiMisura(lavorazione.getUnitaDiMisura().getDescrizione())
							.setDichiarazioni(dichiarazioneToBind.get().getFabbisogni());
							stampaLavorazioneDtoList.add(stampaLavorazioneDto);
						}
					});
					if (!CollectionUtils.isEmpty(stampaLavorazioneDtoList)) {
						stampaTipoLavorazioneDto.setLavorazioni(stampaLavorazioneDtoList.stream()
								.sorted(Comparator.comparingInt(StampaLavorazioneDto::getIndice))
								.collect(Collectors.toList()));
						stampaTipoLavorazioneDtoList.add(stampaTipoLavorazioneDto);
					}
				});

				if (zootecnia.booleanValue()) {
					stampaTipoLavorazioneDtoList.forEach(tipo -> tipo.setIndice(tipo.getLavorazioni().get(0).getIndice()));
					stampaRaggruppamento.setTipi(stampaTipoLavorazioneDtoList.stream()
							.sorted(Comparator.comparing(StampaTipoLavorazioneDto::getIndice))
							.collect(Collectors.toList()));
				} else {
					stampaRaggruppamento.setTipi(stampaTipoLavorazioneDtoList.stream()
							.sorted(Comparator.comparing(StampaTipoLavorazioneDto::getNome))
							.collect(Collectors.toList()));	
				}
				stampaRaggruppamentoLavorazioniDto.add(stampaRaggruppamento);
			});
		}
		return stampaRaggruppamentoLavorazioniDto.stream()
				.sorted(Comparator.comparingInt(StampaRaggruppamentoLavorazioniDto::getIndice))
				.collect(Collectors.toList());
	}

	private List<StampaRaggruppamentoLavorazioniDto> buildRaggruppamentoDtoFabbricati(List<RaggruppamentoLavorazioniDto> raggruppamenti, List<DichiarazioneFabbricatoDto> dichiarazioni, AmbitoLavorazione ambito) {
		List<StampaRaggruppamentoLavorazioniDto> stampaRaggruppamentoLavorazioniDto = new ArrayList<>();
		if (!CollectionUtils.isEmpty(raggruppamenti)) {
			raggruppamenti.forEach(raggruppamento -> {
				var stampaRaggruppamento = new StampaRaggruppamentoLavorazioniDto();
				stampaRaggruppamento.setNome(raggruppamento.getNome());
				stampaRaggruppamento.setIndice(raggruppamento.getIndice());

				List<StampaTipoLavorazioneDto> stampaTipoLavorazioneDtoList = new ArrayList<>();				
				List<FabbricatoDto> fabbricati = raggruppamento.getFabbricati()
						.stream()
						.filter(fabbricato -> dichiarazioni
								.stream()
								.anyMatch(dichiarazione -> fabbricato.getId().equals(dichiarazione.getIdFabbricato()) && !CollectionUtils.isEmpty(dichiarazione.getDichiarazioni())))
						.collect(Collectors.toList());

				fabbricati.forEach(fabbricato -> {
					Optional<DichiarazioneFabbricatoDto> dichiarazioneFabbricatoDto = dichiarazioni
							.stream()
							.filter(dichiarazione -> dichiarazione.getIdFabbricato().equals(fabbricato.getId()))
							.findFirst();
					if (dichiarazioneFabbricatoDto.isPresent()) {
						var stampaTipoLavorazione = new StampaTipoLavorazioneDto();
						stampaTipoLavorazione.setFabbricato(fabbricato);
						List<StampaLavorazioneDto> stampaLavorazioneDtoList = new ArrayList<>();
						raggruppamento.getLavorazioni().forEach(lavorazione -> {
							Optional<DichiarazioneDto> dichiarazioneToBind = dichiarazioneFabbricatoDto.get().getDichiarazioni().stream().filter(dichiarazione -> dichiarazione.getLavorazioneId().equals(lavorazione.getId())).findFirst();
							if (dichiarazioneToBind.isPresent()) {
								var stampaLavorazioneDto = new StampaLavorazioneDto();
								stampaLavorazioneDto
								.setNome(lavorazione.getNome())
								.setIndice(lavorazione.getIndice())
								.setTipologia(lavorazione.getTipologia().getDescrizione())
								.setUnitaDiMisura(lavorazione.getUnitaDiMisura().getDescrizione())
								.setDichiarazioni(dichiarazioneToBind.get().getFabbisogni());
								if (ambito.name().equals( AmbitoLavorazione.SERRE.name())) {
									stampaTipoLavorazione.setMesi(calcoloMesiByQuantitaAndVolume(dichiarazioneToBind, fabbricato));
								}
								stampaLavorazioneDtoList.add(stampaLavorazioneDto);
							}

						});
						stampaTipoLavorazione.setLavorazioni(stampaLavorazioneDtoList.stream()
								.sorted(Comparator.comparingInt(StampaLavorazioneDto::getIndice))
								.collect(Collectors.toList()));
						stampaTipoLavorazioneDtoList.add(stampaTipoLavorazione);
					}
				});
				stampaRaggruppamento.setTipi(stampaTipoLavorazioneDtoList);
				stampaRaggruppamentoLavorazioniDto.add(stampaRaggruppamento);
			});
		}
		return stampaRaggruppamentoLavorazioniDto;
	}
	
	private Integer calcoloMesiByQuantitaAndVolume(Optional<DichiarazioneDto> dichiarazione,FabbricatoDto fabbricato ) {
		if (!dichiarazione.isPresent() || CollectionUtils.isEmpty(dichiarazione.get().getFabbisogni())) {
			return null;
		}
		if (dichiarazione.get().getFabbisogni().get(0).getQuantita() == null || dichiarazione.get().getFabbisogni().get(0).getQuantita().intValue() <= 0 ) {
			return null;
		}
		if (fabbricato == null || fabbricato.getVolume() == null || fabbricato.getVolume().intValue() <= 0) {
			return null;
		}
		return dichiarazione.get().getFabbisogni().get(0).getQuantita().intValue()/fabbricato.getVolume().intValue();
	}

	private List<RaggruppamentoLavorazioniDto> filterRaggruppramenti(List<RaggruppamentoLavorazioniDto> raggruppamenti, List<DichiarazioneDto> dichiarazioni) {
		List<RaggruppamentoLavorazioniDto> filtered = new ArrayList<>();

		raggruppamenti.forEach(raggruppamento -> {
			Optional<LavorazioneDto> opt = raggruppamento.getLavorazioni()
					.stream()
					.filter(lavorazione -> dichiarazioni.stream().anyMatch(dichiarazione -> dichiarazione.getLavorazioneId().equals(lavorazione.getId())))
					.findAny();
			if (opt.isPresent()) {
				filtered.add(raggruppamento);
			}
		});
		return filtered;
	}

	private List<RaggruppamentoLavorazioniDto> filterRaggruppramentiFabbricati(List<RaggruppamentoLavorazioniDto> raggruppamenti, List<DichiarazioneFabbricatoDto> dichiarazioni) {
		List<RaggruppamentoLavorazioniDto> filtered = new ArrayList<>();

		raggruppamenti.forEach(raggruppamento -> {
			Optional<FabbricatoDto> opt = raggruppamento.getFabbricati().
					stream()
					.filter(fabbricato -> dichiarazioni.stream().anyMatch(dichiarazione -> !CollectionUtils.isEmpty(dichiarazione.getDichiarazioni()) && dichiarazione.getIdFabbricato().equals(fabbricato.getId())))
					.findAny();
			if (opt.isPresent()) {
				filtered.add(raggruppamento);
			}
		});
		return filtered;
	}
}
