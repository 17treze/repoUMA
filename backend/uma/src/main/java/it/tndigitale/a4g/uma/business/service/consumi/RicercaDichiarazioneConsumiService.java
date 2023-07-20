package it.tndigitale.a4g.uma.business.service.consumi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import it.tndigitale.a4g.fascicolo.anagrafica.client.model.SportelloFascicoloDto;
import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiSpecification;
import it.tndigitale.a4g.uma.business.persistence.repository.PrelieviDao;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
import it.tndigitale.a4g.uma.business.service.client.UmaUtenteClient;
import it.tndigitale.a4g.uma.business.service.configurazione.ConfigurazioneService;
import it.tndigitale.a4g.uma.dto.DomandaUmaDto;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiDto;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiFilter;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiPagedFilter;
import it.tndigitale.a4g.uma.dto.consumi.builder.CarburanteCompletoDtoBuilder;
import it.tndigitale.a4g.uma.dto.consumi.builder.DichiarazioneConsumiBuilder;
import it.tndigitale.a4g.uma.dto.protocollo.TipoDocumentoUma;

@Service
public class RicercaDichiarazioneConsumiService {
	
	private static final String PREFISSO_CAMPI_RICHIESTA = "richiestaCarburante.";
	private static final int QUERY_LIMIT = 999;
	
	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;
	@Autowired
	private UtenteComponent utenteComponent;
	@Autowired
	private UmaAnagraficaClient anagraficaClient;
	@Autowired
	private ConfigurazioneService configurazioneService;
	@Autowired
	private UmaUtenteClient utenteClient;
	@Autowired
	private PrelieviDao prelieviDao;
	
	// servizio performante pensato esclusivamente per la *ricerca* da parte dei CAA
	public List<DomandaUmaDto> getDichiarazioniConsumi(Long campagna) {
		List<DomandaUmaDto> response = new ArrayList<>();
		// recupera sia deleghe che mandati
		List<SportelloFascicoloDto> sportelliFascicoliDto = anagraficaClient.getSportelliFascicoli();
		var cuaaList = sportelliFascicoliDto.stream().map(SportelloFascicoloDto::getCuaaList).flatMap(List::stream)
				.collect(Collectors.toList());
		final List<List<String>> subLists = Lists.partition(cuaaList, QUERY_LIMIT);
		subLists.stream().forEach(list -> {
			List<DomandaUmaDto> documenti = dichiarazioneConsumiDao.findByAbilitazioniAndCampagna(list, campagna)
					.stream().map(documento -> documento.setTipo(TipoDocumentoUma.DICHIARAZIONE_CONSUMI))
					.collect(Collectors.toList());
			response.addAll(documenti);
		});
		return response;
	}
	
	// Servizio non paginato utilizzato per la *logica* dell'applicativo da parte dei CAA , Aziende e Istruttori e Distributori
	public List<DichiarazioneConsumiDto> getDichiarazioniConsumi(DichiarazioneConsumiFilter dcFilter) {
		
		List<DichiarazioneConsumiDto> response = new ArrayList<>();
		List<String> cuaaList = new ArrayList<>();
		
		//		if (utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)) {
		//			// recupera sia deleghe che mandati
		//			List<SportelloFascicoloDto> sportelliFascicoliDto = anagraficaClient.getSportelliFascicoli();
		//			cuaaList = sportelliFascicoliDto.stream().map(SportelloFascicoloDto::getCuaaList).flatMap(List::stream).collect(Collectors.toList());
		//		} else if (utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_UTENTE)) {
		//			cuaaList = utenteClient.getAziende();
		//		} else if (utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)) { // gli istruttori vedono le domanda come le vedono i caa e le aziende. 
		response.addAll(dichiarazioneConsumiDao.findAll(DichiarazioneConsumiSpecification.getFilter(dcFilter)).stream()
				.map(dichiarazioneModel -> new DichiarazioneConsumiBuilder().from(dichiarazioneModel)
						.from(dichiarazioneModel.getRichiestaCarburante())
						.withRimanenza(new CarburanteCompletoDtoBuilder()
								.from(dichiarazioneModel.getConsuntivi(), TipoConsuntivo.RIMANENZA).build())
						.build())
				.collect(Collectors.toList()));
		
		return response;
		//		}
		//		final List<List<String>> subLists = Lists.partition(cuaaList, QUERY_LIMIT);
		//		if (CollectionUtils.isEmpty(subLists)) {
		//			return response;
		//		}
		//		subLists.stream().forEach(subList -> response.addAll(dichiarazioneConsumiDao.findAll(DichiarazioneConsumiSpecification.getFilter(dcFilter, subList))
		//				.stream()
		//				.map(dichiarazioneModel -> new DichiarazioneConsumiBuilder()
		//						.from(dichiarazioneModel)
		//						.from(dichiarazioneModel.getRichiestaCarburante())
		//						.withRimanenza(new CarburanteCompletoDtoBuilder().from(dichiarazioneModel.getConsuntivi(), TipoConsuntivo.RIMANENZA).build())
		//						.build()).collect(Collectors.toList())));
		//
		//		return response;
	}
	
	// Servizio paginato utilizzato per la *ricerca* dell'applicativo da parte degli istruttori e distributori
	public RisultatiPaginati<DichiarazioneConsumiDto> getDichiarazioniConsumiPaged(DichiarazioneConsumiFilter dcFilter)
			throws Exception {
		// Reperisco dichiarazione consumi per richiesta carburante(è unica per anno).
		
		Pageable pageable = PageableBuilder.build().from(Paginazione.PAGINAZIONE_DEFAULT, Ordinamento.DEFAULT_ORDER_BY);
		if (dcFilter instanceof DichiarazioneConsumiPagedFilter) {
			DichiarazioneConsumiPagedFilter pagFilter = (DichiarazioneConsumiPagedFilter) dcFilter;
			pageable = PageableBuilder.build().from(
					Paginazione
							.getOrDefault(new Paginazione(pagFilter.getNumeroElementiPagina(), pagFilter.getPagina())),
					Optional.ofNullable(Ordinamento.getOrDefault(
							new Ordinamento(filterOrdinamento(pagFilter.getProprieta()), pagFilter.getOrdine())))
							.filter(o -> o.getProprieta() != null).orElse(Ordinamento.DEFAULT_ORDER_BY));
		}
		
		Page<DichiarazioneConsumiModel> dichiarazioniConsumiPage = dichiarazioneConsumiDao
				.findAll(DichiarazioneConsumiSpecification.getFilter(dcFilter), pageable);
		
		List<DichiarazioneConsumiDto> dichiarazioni = new ArrayList<>();
		
		if (!dichiarazioniConsumiPage.isEmpty()) {
			dichiarazioniConsumiPage.forEach(dichiarazione -> dichiarazioni.add(new DichiarazioneConsumiBuilder()
					.from(dichiarazione).from(dichiarazione.getRichiestaCarburante()).build()));
		}
		return RisultatiPaginati.of(dichiarazioni, dichiarazioniConsumiPage.getTotalElements());
	}
	
	public DichiarazioneConsumiDto getDichiarazioneConsumi(Long id) {
		DichiarazioneConsumiModel dichiarazioneConsumi = dichiarazioneConsumiDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(
						"Nessuna Dichiarazione Consumi trovata per id ".concat(String.valueOf(id))));
		
		return new DichiarazioneConsumiBuilder().from(dichiarazioneConsumi)
				.from(dichiarazioneConsumi.getRichiestaCarburante())
				.withRimanenza(new CarburanteCompletoDtoBuilder()
						.from(dichiarazioneConsumi.getConsuntivi(), TipoConsuntivo.RIMANENZA).build())
				.withDatiPrelievi(
						configurazioneService.getDataLimitePrelievi(
								dichiarazioneConsumi.getRichiestaCarburante().getCampagna().intValue()),
						prelieviDao.findByRichiestaCarburante_cuaaAndRichiestaCarburante_campagna(
								dichiarazioneConsumi.getRichiestaCarburante().getCuaa(),
								dichiarazioneConsumi.getRichiestaCarburante().getCampagna()))
				.build();
	}
	
	private String filterOrdinamento(String fieldToFilter) {
		if (fieldToFilter != null && !fieldToFilter.isBlank()
				&& (fieldToFilter.equals("campagna") || fieldToFilter.equals("cuaa")
						|| fieldToFilter.equals("idRichiesta") || fieldToFilter.equals("denominazione"))) {
			return PREFISSO_CAMPI_RICHIESTA.concat(fieldToFilter);
		}
		return fieldToFilter;
	}
}
