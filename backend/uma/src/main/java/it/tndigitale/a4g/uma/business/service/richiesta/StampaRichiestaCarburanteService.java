package it.tndigitale.a4g.uma.business.service.richiesta;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
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
	
	private final String dummyPdf = "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uDO+xD52P7VICHYgIvawAw3VLER8ujn4ddb4pHUzO89WvTKUz3lZFs0/gpjYSvdAAaFk6p2NjsYDVETqH83MCMKC0MV505rvlBblU+GxHtpLm9x5fysa3xIEg0h0SVOPadh4zDSIOGTH9pF9Lk51KVMaPmYWDRwzK5viqRjbNI/8x6TCbny9m8ADnW+rJtvl6K6txdlru8SPbAQPZKEqtHc3eDQNz3Z7ZB9MGzvN0e0eDyTLkkuWereQI/8BL3eiPOa8Va2dhMj5SbCdd4u7G1b/ObD2tfsy8SXZDaw/Uf5qiOZVeTyV/MDJe1HBgyLyLnbqI8/AwPE5YyL5H4SRtNv1c38YOyMzlEY0nAYfLv8pgdmpaZep9/fu3doe59A6+nYzcnZD9QTzvH2Yz9Bmdvv4MJ/7Ds51Q+RPVxBTOYtxHpvE/oycA/gv+GEZqQplbmRzdHJlYW0KZW5kb2JqCjUgMCBvYmoKPDwvVHlwZS9QYWdlL01lZGlhQm94WzAgMCA1OTUgODQyXS9SZXNvdXJjZXM8PC9Gb250PDwvRjEgMSAwIFIvRjIgMiAwIFI+Pj4+L0NvbnRlbnRzIDMgMCBSL1BhcmVudCA0IDAgUj4+CmVuZG9iagoxIDAgb2JqCjw8L1R5cGUvRm9udC9TdWJ0eXBlL1R5cGUxL0Jhc2VGb250L0hlbHZldGljYS1Cb2xkL0VuY29kaW5nL1dpbkFuc2lFbmNvZGluZz4+CmVuZG9iagoyIDAgb2JqCjw8L1R5cGUvRm9udC9TdWJ0eXBlL1R5cGUxL0Jhc2VGb250L0hlbHZldGljYS9FbmNvZGluZy9XaW5BbnNpRW5jb2Rpbmc+PgplbmRvYmoKNCAwIG9iago8PC9UeXBlL1BhZ2VzL0NvdW50IDEvS2lkc1s1IDAgUl0+PgplbmRvYmoKNiAwIG9iago8PC9UeXBlL0NhdGFsb2cvUGFnZXMgNCAwIFI+PgplbmRvYmoKNyAwIG9iago8PC9Qcm9kdWNlcihpVGV4dFNoYXJwkiA1LjUuMTMuMiCpMjAwMC0yMDIwIGlUZXh0IEdyb3VwIE5WIFwoQUdQTC12ZXJzaW9uXCkpL0NyZWF0aW9uRGF0ZShEOjIwMjIwOTIzMDg0NzQzKzAyJzAwJykvTW9kRGF0ZShEOjIwMjIwOTIzMDg0NzQzKzAyJzAwJyk+PgplbmRvYmoKeHJlZgowIDgKMDAwMDAwMDAwMCA2NTUzNSBmIAowMDAwMDAyMjczIDAwMDAwIG4gCjAwMDAwMDIzNjYgMDAwMDAgbiAKMDAwMDAwMDAxNSAwMDAwMCBuIAowMDAwMDAyNDU0IDAwMDAwIG4gCjAwMDAwMDIxNTIgMDAwMDAgbiAKMDAwMDAwMjUwNSAwMDAwMCBuIAowMDAwMDAyNTUwIDAwMDAwIG4gCnRyYWlsZXIKPDwvU2l6ZSA4L1Jvb3QgNiAwIFIvSW5mbyA3IDAgUi9JRCBbPGVjMDhjZjdhNzJjYmMyOWY0MDZhNTllZTc0NDAwY2U1PjxlYzA4Y2Y3YTcyY2JjMjlmNDA2YTU5ZWU3NDQwMGNlNT5dPj4KJWlUZXh0LTUuNS4xMy4yCnN0YXJ0eHJlZgoyNzE1CiUlRU9GCg==";
	
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
	
		//		var json = objectMapper.writeValueAsString(buildStampaDto(richiestaModel));
		//		return new ByteArrayResource(proxyClient.stampa(json, TEMPLATE_PATH));
		return new ByteArrayResource(Base64.getDecoder().decode(dummyPdf.getBytes()));
	}
	
	private StampaRichiestaCarburanteDto buildStampaDto(RichiestaCarburanteModel richiestaModel) {
		var stampaDto = new StampaRichiestaCarburanteDto();
		var idRichiesta = richiestaModel.getId();
		
		boolean isRettifica = ricercaRichiestaCarburanteService.getIdRettificata(richiestaModel.getCuaa(),
				richiestaModel.getCampagna(), richiestaModel.getDataPresentazione()).isPresent();
		
		// Rettifica
		stampaDto.setIsRettifica(isRettifica);
		
		//Anagrafica
		stampaDto.setIdRichiesta(richiestaModel.getId());
		stampaDto.setCampagna(richiestaModel.getCampagna());
		stampaDto.setCuaa(richiestaModel.getCuaa());
		stampaDto.setDenominazione(
				richiestaModel.getDenominazione().equals("NO_DENOM") ? null : richiestaModel.getDenominazione());
		
		//Macchine
		if (!CollectionUtils.isEmpty(richiestaModel.getMacchine())) {
			List<MacchinaDto> macchineDto = new MacchinaUmaBuilder().from(richiestaModel.getMacchine());
			// Stampiamo solo le macchine utilizzate
			stampaDto.setMacchine(
					macchineDto.stream().filter(MacchinaDto::getIsUtilizzata).collect(Collectors.toList()));
		}
		
		// Filtro i raggruppamenti per i soli aventi fabbisogni dichiarati
		// Costruisco i dto di stampa in base ai raggruppamenti trovati
		
		// Lavorazioni Superficie
		List<RaggruppamentoLavorazioniDto> raggSup = lavorazioniService.getCategorieLavorazioni(idRichiesta,
				AmbitoLavorazione.SUPERFICIE);
		List<DichiarazioneDto> dichSup = lavorazioniService.getFabbisogni(idRichiesta,
				LavorazioneFilter.Lavorazioni.SUPERFICIE);
		
		List<RaggruppamentoLavorazioniDto> raggSupFiltered = filterRaggruppramenti(raggSup, dichSup);
		
		// Raggruppamento Lavorazioni Superficie
		stampaDto.setRaggruppamentoLavorazioneSuperficie(buildRaggDto(raggSupFiltered, dichSup, false));
		
		// Lavorazioni Altro
		List<RaggruppamentoLavorazioniDto> raggAltro = lavorazioniService.getCategorieLavorazioni(idRichiesta,
				AmbitoLavorazione.ALTRO);
		List<DichiarazioneDto> dichAltro = lavorazioniService.getFabbisogni(idRichiesta,
				LavorazioneFilter.Lavorazioni.ALTRO);
		
		List<RaggruppamentoLavorazioniDto> raggAltroFiltered = filterRaggruppramenti(raggAltro, dichAltro);
		
		// Raggruppamento Lavorazioni Altro
		stampaDto.setRaggruppamentoLavorazioneAltro(buildRaggDto(raggAltroFiltered, dichAltro, false));
		
		// Lavorazioni Zootecnia
		List<RaggruppamentoLavorazioniDto> raggZoo = lavorazioniService.getCategorieLavorazioni(idRichiesta,
				AmbitoLavorazione.ZOOTECNIA);
		List<DichiarazioneDto> dichZoo = lavorazioniService.getFabbisogni(idRichiesta,
				LavorazioneFilter.Lavorazioni.ZOOTECNIA);
		
		List<RaggruppamentoLavorazioniDto> raggZooFiltered = filterRaggruppramenti(raggZoo, dichZoo);
		
		// Raggruppamento Lavorazioni Zootecnia
		stampaDto.setRaggruppamentoLavorazioneZootecnia(buildRaggDto(raggZooFiltered, dichZoo, true));
		
		// Filtro i raggruppamenti per i soli aventi fabbricati con fabbisogni dichiarati	
		// Costruisco i dto di stampa in base ai raggruppamenti trovati
		
		// Lavorazioni Serre
		List<RaggruppamentoLavorazioniDto> raggSerre = lavorazioniService.getCategorieLavorazioni(idRichiesta,
				AmbitoLavorazione.SERRE);
		List<DichiarazioneFabbricatoDto> dichSerre = lavorazioniService.getFabbisogniFabbricati(idRichiesta,
				LavorazioneFilter.LavorazioniFabbricati.SERRE);
		
		List<RaggruppamentoLavorazioniDto> raggSerreFiltered = filterRaggruppramentiFabbricati(raggSerre, dichSerre);
		
		// Raggruppamento Lavorazioni Serre
		stampaDto.setRaggruppamentoLavorazioneSerre(
				buildRaggruppamentoDtoFabbricati(raggSerreFiltered, dichSerre, AmbitoLavorazione.SERRE));
		
		// Lavorazioni Fabbricati
		List<RaggruppamentoLavorazioniDto> raggFab = lavorazioniService.getCategorieLavorazioni(idRichiesta,
				AmbitoLavorazione.FABBRICATI);
		List<DichiarazioneFabbricatoDto> dichFab = lavorazioniService.getFabbisogniFabbricati(idRichiesta,
				LavorazioneFilter.LavorazioniFabbricati.FABBRICATI);
		
		List<RaggruppamentoLavorazioniDto> raggFabFiltered = filterRaggruppramentiFabbricati(raggFab, dichFab);
		
		// Raggruppamento Lavorazioni Fabbricati
		stampaDto.setRaggruppamentoLavorazioneFabbricati(
				buildRaggruppamentoDtoFabbricati(raggFabFiltered, dichFab, AmbitoLavorazione.FABBRICATI));
		
		// Fabbisogno
		// Residuo
		stampaDto.setResiduo(
				carburanteHelper.getResiduoAnnoPrecedente(richiestaModel.getCuaa(), richiestaModel.getCampagna()));
		
		// Ammissibile
		stampaDto.setAmmissibile(richiestaCarburanteService.calcolaCarburanteAmmissibile(richiestaModel.getId()));
		
		// Richiesto
		stampaDto.setRichiesto(new CarburanteCompletoDto().setBenzina(richiestaModel.getBenzina())
				.setGasolio(richiestaModel.getGasolio()).setGasolioSerre(richiestaModel.getGasolioSerre())
				.setGasolioTerzi(richiestaModel.getGasolioTerzi()));
		stampaDto.setNote(richiestaModel.getNote());
		
		// Prelevato
		if (isRettifica) {
			CarburanteTotale<PrelievoDto> prelievi = richiestaCarburanteService.getPrelievi(richiestaModel.getCuaa(),
					richiestaModel.getCampagna(), richiestaModel.getDataPresentazione());
			stampaDto.setPrelevato(new CarburanteDto().setBenzina(prelievi.getTotale().getBenzina())
					.setGasolioSerre(prelievi.getTotale().getGasolioSerre())
					.setGasolio(prelievi.getTotale().getGasolio()));
		}
		
		stampaDto.setData(clock.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		
		return stampaDto;
	}
	
	private List<StampaRaggruppamentoLavorazioniDto> buildRaggDto(List<RaggruppamentoLavorazioniDto> raggruppamenti,
			List<DichiarazioneDto> dichiarazioni, Boolean zootecnia) {
		List<StampaRaggruppamentoLavorazioniDto> stampaRaggruppamentoLavorazioniDto = new ArrayList<>();
		if (!CollectionUtils.isEmpty(raggruppamenti)) {
			raggruppamenti.forEach(raggruppamento -> {
				var stampaRaggruppamento = new StampaRaggruppamentoLavorazioniDto();
				stampaRaggruppamento.setNome(raggruppamento.getNome());
				stampaRaggruppamento.setIndice(raggruppamento.getIndice());
				
				List<StampaTipoLavorazioneDto> stampaTipoLavorazioneDtoList = new ArrayList<>();
				Map<TipologiaLavorazione, List<LavorazioneDto>> tipiLavorazione = raggruppamento.getLavorazioni()
						.stream().collect(Collectors.groupingBy(LavorazioneDto::getTipologia));
				
				tipiLavorazione.keySet().forEach(tipo -> {
					var stampaTipoLavorazioneDto = new StampaTipoLavorazioneDto();
					stampaTipoLavorazioneDto.setNome(tipo.getDescrizione());
					stampaTipoLavorazioneDto.setSuperficieMassima(raggruppamento.getSuperficieMassima());
					
					List<StampaLavorazioneDto> stampaLavorazioneDtoList = new ArrayList<>();
					tipiLavorazione.getOrDefault(tipo, new ArrayList<>()).forEach(lavorazione -> {
						Optional<DichiarazioneDto> dichiarazioneToBind = dichiarazioni.stream()
								.filter(dichiarazione -> dichiarazione.getLavorazioneId().equals(lavorazione.getId()))
								.findFirst();
						if (dichiarazioneToBind.isPresent()) {
							var stampaLavorazioneDto = new StampaLavorazioneDto();
							stampaLavorazioneDto.setNome(lavorazione.getNome()).setIndice(lavorazione.getIndice())
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
					stampaTipoLavorazioneDtoList
							.forEach(tipo -> tipo.setIndice(tipo.getLavorazioni().get(0).getIndice()));
					stampaRaggruppamento.setTipi(stampaTipoLavorazioneDtoList.stream()
							.sorted(Comparator.comparing(StampaTipoLavorazioneDto::getIndice))
							.collect(Collectors.toList()));
				}
				else {
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
	
	private List<StampaRaggruppamentoLavorazioniDto> buildRaggruppamentoDtoFabbricati(
			List<RaggruppamentoLavorazioniDto> raggruppamenti, List<DichiarazioneFabbricatoDto> dichiarazioni,
			AmbitoLavorazione ambito) {
		List<StampaRaggruppamentoLavorazioniDto> stampaRaggruppamentoLavorazioniDto = new ArrayList<>();
		if (!CollectionUtils.isEmpty(raggruppamenti)) {
			raggruppamenti.forEach(raggruppamento -> {
				var stampaRaggruppamento = new StampaRaggruppamentoLavorazioniDto();
				stampaRaggruppamento.setNome(raggruppamento.getNome());
				stampaRaggruppamento.setIndice(raggruppamento.getIndice());
				
				List<StampaTipoLavorazioneDto> stampaTipoLavorazioneDtoList = new ArrayList<>();
				List<FabbricatoDto> fabbricati = raggruppamento.getFabbricati().stream()
						.filter(fabbricato -> dichiarazioni.stream()
								.anyMatch(dichiarazione -> fabbricato.getId().equals(dichiarazione.getIdFabbricato())
										&& !CollectionUtils.isEmpty(dichiarazione.getDichiarazioni())))
						.collect(Collectors.toList());
				
				fabbricati.forEach(fabbricato -> {
					Optional<DichiarazioneFabbricatoDto> dichiarazioneFabbricatoDto = dichiarazioni.stream()
							.filter(dichiarazione -> dichiarazione.getIdFabbricato().equals(fabbricato.getId()))
							.findFirst();
					if (dichiarazioneFabbricatoDto.isPresent()) {
						var stampaTipoLavorazione = new StampaTipoLavorazioneDto();
						stampaTipoLavorazione.setFabbricato(fabbricato);
						List<StampaLavorazioneDto> stampaLavorazioneDtoList = new ArrayList<>();
						raggruppamento.getLavorazioni().forEach(lavorazione -> {
							Optional<DichiarazioneDto> dichiarazioneToBind = dichiarazioneFabbricatoDto.get()
									.getDichiarazioni().stream().filter(dichiarazione -> dichiarazione
											.getLavorazioneId().equals(lavorazione.getId()))
									.findFirst();
							if (dichiarazioneToBind.isPresent()) {
								var stampaLavorazioneDto = new StampaLavorazioneDto();
								stampaLavorazioneDto.setNome(lavorazione.getNome()).setIndice(lavorazione.getIndice())
										.setTipologia(lavorazione.getTipologia().getDescrizione())
										.setUnitaDiMisura(lavorazione.getUnitaDiMisura().getDescrizione())
										.setDichiarazioni(dichiarazioneToBind.get().getFabbisogni());
								if (ambito.name().equals(AmbitoLavorazione.SERRE.name())) {
									stampaTipoLavorazione
											.setMesi(calcoloMesiByQuantitaAndVolume(dichiarazioneToBind, fabbricato));
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
	
	private Integer calcoloMesiByQuantitaAndVolume(Optional<DichiarazioneDto> dichiarazione, FabbricatoDto fabbricato) {
		if (!dichiarazione.isPresent() || CollectionUtils.isEmpty(dichiarazione.get().getFabbisogni())) {
			return null;
		}
		if (dichiarazione.get().getFabbisogni().get(0).getQuantita() == null
				|| dichiarazione.get().getFabbisogni().get(0).getQuantita().intValue() <= 0) {
			return null;
		}
		if (fabbricato == null || fabbricato.getVolume() == null || fabbricato.getVolume().intValue() <= 0) {
			return null;
		}
		return dichiarazione.get().getFabbisogni().get(0).getQuantita().intValue() / fabbricato.getVolume().intValue();
	}
	
	private List<RaggruppamentoLavorazioniDto> filterRaggruppramenti(List<RaggruppamentoLavorazioniDto> raggruppamenti,
			List<DichiarazioneDto> dichiarazioni) {
		List<RaggruppamentoLavorazioniDto> filtered = new ArrayList<>();
		
		raggruppamenti.forEach(raggruppamento -> {
			Optional<LavorazioneDto> opt = raggruppamento.getLavorazioni().stream()
					.filter(lavorazione -> dichiarazioni.stream()
							.anyMatch(dichiarazione -> dichiarazione.getLavorazioneId().equals(lavorazione.getId())))
					.findAny();
			if (opt.isPresent()) {
				filtered.add(raggruppamento);
			}
		});
		return filtered;
	}
	
	private List<RaggruppamentoLavorazioniDto> filterRaggruppramentiFabbricati(
			List<RaggruppamentoLavorazioniDto> raggruppamenti, List<DichiarazioneFabbricatoDto> dichiarazioni) {
		List<RaggruppamentoLavorazioniDto> filtered = new ArrayList<>();
		
		raggruppamenti.forEach(raggruppamento -> {
			Optional<FabbricatoDto> opt = raggruppamento.getFabbricati().stream()
					.filter(fabbricato -> dichiarazioni.stream()
							.anyMatch(dichiarazione -> !CollectionUtils.isEmpty(dichiarazione.getDichiarazioni())
									&& dichiarazione.getIdFabbricato().equals(fabbricato.getId())))
					.findAny();
			if (opt.isPresent()) {
				filtered.add(raggruppamento);
			}
		});
		return filtered;
	}
}
