package it.tndigitale.a4g.uma.business.service.richiesta;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// import it.tndigitale.a4g.fascicolo.territorio.client.model.ParticellaDto;
import it.tndigitale.a4g.framework.pagination.builder.PageableBuilder;
import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import it.tndigitale.a4g.framework.security.model.UtenteComponent;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbricatoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;
import it.tndigitale.a4g.uma.business.persistence.entity.UtilizzoMacchinariModel;
import it.tndigitale.a4g.uma.business.persistence.repository.FabbricatiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteDao;
import it.tndigitale.a4g.uma.business.persistence.repository.RichiestaCarburanteSpecification;
import it.tndigitale.a4g.uma.business.service.client.UmaAnagraficaClient;
// import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;
import it.tndigitale.a4g.uma.business.service.client.UmaUtenteClient;
import it.tndigitale.a4g.uma.dto.DomandaUmaDto;
import it.tndigitale.a4g.uma.dto.protocollo.TipoDocumentoUma;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteCompletoDto;
import it.tndigitale.a4g.uma.dto.richiesta.MacchinaAualDto;
import it.tndigitale.a4g.uma.dto.richiesta.RespTerritorioAualDto;
import it.tndigitale.a4g.uma.dto.richiesta.RichiestaCarburanteDto;
import it.tndigitale.a4g.uma.dto.richiesta.RichiestaCarburanteFilter;
import it.tndigitale.a4g.uma.dto.richiesta.RichiestaCarburantePagedFilter;
import it.tndigitale.a4g.uma.dto.richiesta.TerritorioAualDto;
import it.tndigitale.a4g.uma.dto.richiesta.builder.RichiestaCarburanteDtoBuilder;

@Service
public class RicercaRichiestaCarburanteService {
	
	private static final int QUERY_LIMIT = 999;

	private static final Logger logger = LoggerFactory.getLogger(RicercaRichiestaCarburanteService.class);
	
	@Autowired
	private RichiestaCarburanteDao richiestaCarburanteDao;
	@Autowired
	private FabbricatiDao fabbricatiDao;
	// @Autowired
	// private UmaTerritorioClient territorioClient;
	@Autowired
	private RichiestaCarburanteDtoBuilder builder;
	@Autowired
	private UtenteComponent utenteComponent;
	@Autowired
	private UmaUtenteClient utenteClient;
	@Autowired
	private UmaAnagraficaClient anagraficaClient;
	@Autowired
	private Clock clock;
	
	// servizio performante pensato esclusivamente per la *ricerca* da parte dei CAA
	public List<DomandaUmaDto> getRichieste(Long campagna) {
		List<DomandaUmaDto> response = new ArrayList<>();
		
		// interrogo tutte le richieste a prescindere dalle abilitazioni al fine di valutare per ciascuna domanda se si tratta di una richiesta o di una rettifica.
		List<DomandaUmaDto> richiesteAll = richiestaCarburanteDao.findStrictByCampagna(campagna);
		
		// recupera sia deleghe che mandati
		//		List<SportelloFascicoloDto> sportelliFascicoliDto = anagraficaClient.getSportelliFascicoli();
		//		var cuaaList = sportelliFascicoliDto.stream().map(SportelloFascicoloDto::getCuaaList).flatMap(List::stream)
		//				.collect(Collectors.toList());
		
		//		final List<List<String>> subLists = Lists.partition(cuaaList, QUERY_LIMIT);
		//		subLists.stream().forEach(list -> {
		//			List<DomandaUmaDto> documenti = richiestaCarburanteDao.findByAbilitazioniAndCampagna(list, campagna)
		richiesteAll.stream()
				.map(documento -> isRichiestaRettificante(richiesteAll, documento).booleanValue()
						? documento.setTipo(TipoDocumentoUma.RETTIFICA)
						: documento.setTipo(TipoDocumentoUma.RICHIESTA))
				.collect(Collectors.toList());
		response.addAll(richiesteAll);
		//		});
		return response;
	}
	
	// Servizio non paginato utilizzato per la *logica* dell'applicativo da parte dei CAA , Aziende e Istruttori e Distributori
	public List<RichiestaCarburanteDto> getRichieste(RichiestaCarburanteFilter richiestaCarburanteFilter) {
		
		List<RichiestaCarburanteDto> response = new ArrayList<>();
		//		List<String> cuaaList = new ArrayList<>();
		//		if (utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_ENTE)) {
		//			// recupera sia deleghe che mandati
		//			List<SportelloFascicoloDto> sportelliFascicoliDto = anagraficaClient.getSportelliFascicoli();
		//			cuaaList = sportelliFascicoliDto.stream().map(SportelloFascicoloDto::getCuaaList).flatMap(List::stream)
		//					.collect(Collectors.toList());
		//		}
		//		else if (utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_UTENTE)) {
		//			cuaaList = utenteClient.getAziende();
		//		}
		//		else if (utenteComponent.haRuolo(Ruoli.DOMANDE_UMA_RICERCA_TUTTI)) {
		response.addAll(
				richiestaCarburanteDao.findAll(RichiestaCarburanteSpecification.getFilter(richiestaCarburanteFilter))
						.stream().map(richiestaMapper).collect(Collectors.toList()));
		return response;
		//		}
		//		final List<List<String>> subLists = Lists.partition(cuaaList, QUERY_LIMIT);
		//		
		//		if (CollectionUtils.isEmpty(subLists)) {
		//			return response;
		//		}
		//		
		//		subLists.stream()
		//				.forEach(subList -> response.addAll(richiestaCarburanteDao
		//						.findAll(RichiestaCarburanteSpecification.getFilter(richiestaCarburanteFilter, subList))
		//						.stream().map(richiestaMapper).collect(Collectors.toList())));
		//		return response;
	}
	
	// Servizio paginato utilizzato per la *ricerca* dell'applicativo da parte degli istruttori e distributori
	public RisultatiPaginati<RichiestaCarburanteDto> getRichiestePaged(
			RichiestaCarburanteFilter richiestaCarburanteFilter) throws Exception {
		List<RichiestaCarburanteDto> richieste = new ArrayList<>();
		
		Pageable pageable = PageableBuilder.build().from(Paginazione.PAGINAZIONE_DEFAULT, Ordinamento.DEFAULT_ORDER_BY);
		if (richiestaCarburanteFilter instanceof RichiestaCarburantePagedFilter) {
			RichiestaCarburantePagedFilter pagFilter = (RichiestaCarburantePagedFilter) richiestaCarburanteFilter;
			pageable = PageableBuilder.build().from(
					Paginazione
							.getOrDefault(new Paginazione(pagFilter.getNumeroElementiPagina(), pagFilter.getPagina())),
					Optional.ofNullable(
							Ordinamento.getOrDefault(new Ordinamento(pagFilter.getProprieta(), pagFilter.getOrdine())))
							.filter(o -> o.getProprieta() != null).orElse(Ordinamento.DEFAULT_ORDER_BY));
		}
		
		Page<RichiestaCarburanteModel> richiesteModelPage = richiestaCarburanteDao
				.findAll(RichiestaCarburanteSpecification.getFilter(richiestaCarburanteFilter), pageable);
		
		if (!richiesteModelPage.isEmpty()) {
			richieste = richiesteModelPage.stream().map(richiestaMapper).collect(Collectors.toList());
		}
		return RisultatiPaginati.of(richieste, richiesteModelPage.getTotalElements());
	}
	
	public RichiestaCarburanteDto getRichiesta(Long id) {
		RichiestaCarburanteModel richiesta = richiestaCarburanteDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(
						"Richiesta con id: ".concat(String.valueOf(id)).concat("non trovata")));
		
		// Controllo se la richiesta ha associati dei fabbricati - Già importati in fase di creazione della richiesta
		List<FabbricatoModel> fabbricati = fabbricatiDao.findByRichiestaCarburante_id(id);

//		// reperisco informazioni sulle colture da ags
//		List<ParticellaDto> particelle = territorioClient.getColture(richiesta.getCuaa(),
//				richiesta.getDataPresentazione());
		List<TerritorioAualDto> particelle = this.getColtureFromAual(richiesta.getCuaa(), richiesta.getDataPresentazione());
		
		var idRettificataOpt = getIdRettificata(richiesta.getCuaa(), richiesta.getCampagna(),
				richiesta.getDataPresentazione());
		
		return builder.newDto().from(richiesta)
				.withCarburante(new CarburanteCompletoDto().setBenzina(richiesta.getBenzina())
						.setGasolio(richiesta.getGasolio()).setGasolioSerre(richiesta.getGasolioSerre())
						.setGasolioTerzi(richiesta.getGasolioTerzi()))
				.withFlagMacchineDichiarate(richiesta.getMacchine())
				.withFlagDichiarazioniPresenti(richiesta.getFabbisogni())
				.withFlagSuperficiPresenti(particelle, richiesta).withFlagFabbricatiPresenti(fabbricati)
				.withFlagSerrePresenti(fabbricati)
				.withIdRettificata(idRettificataOpt.isPresent() ? idRettificataOpt.get() : null).build();
	}
	
	// Reperisce la richiesta di carburante più recente in stato Autorizzata dato un CUAA 
	public Optional<RichiestaCarburanteModel> getRichiestaAutorizzataPiuRecente(String cuaa) {
		RichiestaCarburanteFilter filter = new RichiestaCarburanteFilter().setCuaa(cuaa)
				.setStati(Collections.singleton(StatoRichiestaCarburante.AUTORIZZATA));
		
		List<RichiestaCarburanteModel> richieste = richiestaCarburanteDao
				.findAll(RichiestaCarburanteSpecification.getFilter(filter));
		return richieste.stream()
				.filter(richiesta -> richiesta.getCampagna().equals(Long.valueOf(clock.now().getYear()))
						|| richiesta.getCampagna().equals(Long.valueOf(clock.now().getYear()) - 1))
				.max(Comparator.comparing(RichiestaCarburanteModel::getCampagna));
	}
	
	// Trova l'id della domanda rettificata, se esiste
	public Optional<Long> getIdRettificata(String cuaa, Long campagna, LocalDateTime dataPresentazione) {
		
		List<RichiestaCarburanteModel> richieste = richiestaCarburanteDao.findByCuaaAndCampagna(cuaa, campagna);
		Optional<RichiestaCarburanteModel> richiestaRettificataOpt = richieste.stream()
				.filter(r -> r.getDataPresentazione().isBefore(dataPresentazione))
				.max(Comparator.comparing(RichiestaCarburanteModel::getDataPresentazione));
		
		return richiestaRettificataOpt.isPresent() ? Optional.of(richiestaRettificataOpt.get().getId())
				: Optional.empty();
		
	}
	
	/**
	 * Valuta se nella lista di richieste fornita , la richiesta è di rettifica o meno per quell'anno di campagna. Il
	 * metodo valuta se ci sono richieste di carburante per quel cuaa e anno prima della data di presenzione della
	 * richiesta oggetto di valutazione. Se non ve ne sono, la richiesta oggetto di valutazione è necessariamente una
	 * richiesta semplice, altrimenti è di rettifica.
	 * 
	 * @param richieste
	 * @param richiesta
	 * @return
	 */
	public Boolean isRichiestaRettificante(List<DomandaUmaDto> richieste, DomandaUmaDto richiesta) {
		return !richieste.stream().filter(r -> richiesta.getCuaa().equals(r.getCuaa()))
				.filter(r -> richiesta.getCampagna().equals(r.getCampagna()))
				.filter(r -> richiesta.getDataPresentazione().compareTo(r.getDataPresentazione()) > 0)
				.collect(Collectors.toList()).isEmpty();
		
	}
	
	private Function<RichiestaCarburanteModel, RichiestaCarburanteDto> richiestaMapper = richiestaModel -> {
		var idRettificataOpt = getIdRettificata(richiestaModel.getCuaa(), richiestaModel.getCampagna(),
				richiestaModel.getDataPresentazione());
		return builder.newDto().from(richiestaModel)
				.withIdRettificata(idRettificataOpt.isPresent() ? idRettificataOpt.get() : null).build();
	};

	// al momento della crezione della richiesta di carburante importa le macchine che è possibile utilizzare per richiedere carburante
	private List<TerritorioAualDto> getColtureFromAual(String cuaa, LocalDateTime data) {
		
        final String uri = "http://localhost:8888/anagrafeWSNew/fascicoloFS6/leggiConsistenzaFS7?cuaa=" + cuaa;
        
		try {
	        RestTemplate restTemplate = new RestTemplate();
	        URL url = new URL(uri);
	        HttpURLConnection http = (HttpURLConnection)url.openConnection();
	        http.setRequestProperty("Content-Type", "application/json");
	        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
	        logger.info(response.getBody());
	        ObjectMapper objectMapper = new ObjectMapper();
	        RespTerritorioAualDto responseAual = objectMapper.readValue(response.getBody(), new TypeReference<RespTerritorioAualDto>(){});
	        return responseAual.getData();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
