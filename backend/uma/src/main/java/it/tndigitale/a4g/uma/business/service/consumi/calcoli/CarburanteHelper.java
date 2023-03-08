package it.tndigitale.a4g.uma.business.service.consumi.calcoli;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.tndigitale.a4g.framework.support.CustomCollectors;
import it.tndigitale.a4g.uma.business.persistence.entity.ConsuntivoConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburanteConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiSpecification;
import it.tndigitale.a4g.uma.business.service.consumi.DichiarazioneConsumiService;
import it.tndigitale.a4g.uma.business.service.richiesta.RichiestaCarburanteService;
import it.tndigitale.a4g.uma.business.service.trasferimenti.TrasferimentiCarburanteService;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDtoBuilder;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiFilter;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiPagedFilter;
import it.tndigitale.a4g.uma.dto.consumi.builder.CarburanteCompletoDtoBuilder;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteCompletoDto;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteTotale;
import it.tndigitale.a4g.uma.dto.trasferimenti.TrasferimentiFilter;
import it.tndigitale.a4g.uma.dto.trasferimenti.TrasferimentoDto;

@Component
public class CarburanteHelper {

	private static final Logger logger = LoggerFactory.getLogger(CarburanteHelper.class);

	@Autowired
	private DichiarazioneConsumiService dichiarazioneConsumiService;
	@Autowired
	private TrasferimentiCarburanteService trasferimentiService;
	@Autowired
	private RichiestaCarburanteService prelieviService;
	@Autowired
	private CarburanteDtoBuilder carburanteDtoBuilder;
	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;

	public CarburanteDto calcolaAccisa(DichiarazioneConsumiModel dichiarazione) {
		RichiestaCarburanteModel richiesta = dichiarazione.getRichiestaCarburante();

		var ammissibile = getAmmissibileCompleto(dichiarazione).toCarburanteDto();
		var disponibile = calcolaDisponibile(richiesta);

		var rimanenza = getConsuntivo(Optional.of(dichiarazione), TipoConsuntivo.RIMANENZA).toCarburanteDto();
		var consumato = getConsuntivo(Optional.of(dichiarazione), TipoConsuntivo.CONSUMATO).toCarburanteDto();
		var recupero  = getConsuntivo(Optional.of(dichiarazione), TipoConsuntivo.RECUPERO).toCarburanteDto();

		var accisa = consumato.getGasolio() > ammissibile.getGasolio() ? 
				carburanteDtoBuilder.newDto().add(disponibile).subtract(ammissibile).subtract(rimanenza).subtract(recupero).build() :
					carburanteDtoBuilder.newDto().add(disponibile).subtract(consumato).subtract(rimanenza).subtract(recupero).build();

		logger.info("CarburanteHelper id dichiarazione {} - Accisa: {}", dichiarazione.getId(), accisa);
		return accisa;
	}

	public CarburanteDto calcolaAssegnatoAlNettoDelResiduo(RichiestaCarburanteModel richiesta) {

		var residuo = getResiduoAnnoPrecedente(richiesta.getCuaa(), richiesta.getCampagna()).toCarburanteDto();

		var richiesto = new CarburanteCompletoDto()
				.setGasolio(richiesta.getGasolio())
				.setBenzina(richiesta.getBenzina())
				.setGasolioSerre(richiesta.getGasolioSerre())
				.setGasolioTerzi(richiesta.getGasolioTerzi())
				.toCarburanteDto();

		var assegnatoAlNettoDelResiduo = carburanteDtoBuilder.newDto().add(richiesto).subtract(residuo).build();

		logger.info("CarburanteHelper id richiesta {} - Assegnato al netto del Residuo: {}", richiesta.getId(), assegnatoAlNettoDelResiduo);
		return assegnatoAlNettoDelResiduo;
	}

	public CarburanteDto calcolaPrelevabile(RichiestaCarburanteModel richiesta) {

		var assegnatoAlNettoDelResiduo = calcolaAssegnatoAlNettoDelResiduo(richiesta);
		var ricevuto = getTotaleCarburanteRicevuto(richiesta.getCuaa(), richiesta.getCampagna());
		var prelevato = getTotaleCarburantePrelevato(richiesta.getCuaa(), richiesta.getCampagna());
		var prelevabile =  carburanteDtoBuilder.newDto().add(assegnatoAlNettoDelResiduo).subtract(ricevuto).subtract(prelevato).build();

		logger.info("CarburanteHelper id richiesta {} - Prelevabile: {}", richiesta.getId(), prelevabile);
		return prelevabile;
	}

	public CarburanteDto calcolaCarburanteTrasferibile(String cuaa, Long campagna) {
		var carburanteTrasferibile = carburanteDtoBuilder.newDto()
				.add(getResiduoAnnoPrecedente(cuaa, campagna).toCarburanteDto())
				.add(getTotaleCarburantePrelevato(cuaa, campagna))
				.build();
		logger.info("CarburanteHelper: Carburante Trasferibile {}", carburanteTrasferibile);
		return carburanteTrasferibile;
	}

	public CarburanteDto getTotaleCarburanteTrasferito(String cuaa, Long campagna) {
		TrasferimentiFilter filter = new TrasferimentiFilter()
				.setCampagna(campagna)
				.setCuaaMittente(cuaa);
		CarburanteTotale<TrasferimentoDto> trasferito = trasferimentiService.getTrasferimenti(filter);
		return trasferito.getTotale();
	}

	public CarburanteDto getTotaleCarburanteRicevuto(String cuaa, Long campagna) {
		TrasferimentiFilter filter = new TrasferimentiFilter()
				.setCampagna(campagna)
				.setCuaaDestinatario(cuaa);
		CarburanteTotale<TrasferimentoDto> trasferito = trasferimentiService.getTrasferimenti(filter);
		return trasferito.getTotale();
	}


	public CarburanteCompletoDto getAmmissibileCompleto(DichiarazioneConsumiModel dichiarazioneConsumi) {
		CarburanteDto calcoloAmmissibile = dichiarazioneConsumiService.calcolaCarburanteAmmissibile(dichiarazioneConsumi.getId());

		// il carburante ammissibile è quello che viene settato dall'utente altrimenti il valore assegnato/richiesto gasolio terzi della richiesta di carburante
		Optional<ConsuntivoConsumiModel> ammissibileOpt = dichiarazioneConsumi.getConsuntivi().stream()
				.filter(c -> TipoConsuntivo.AMMISSIBILE.equals(c.getTipoConsuntivo()))
				.filter(c -> TipoCarburanteConsuntivo.GASOLIO_TERZI.equals(c.getTipoCarburante()))
				.collect(CustomCollectors.collectOne());

		return new CarburanteCompletoDto()
				.setBenzina(calcoloAmmissibile.getBenzina())
				.setGasolio(calcoloAmmissibile.getGasolio())
				.setGasolioSerre(calcoloAmmissibile.getGasolioSerre())
				.setGasolioTerzi(ammissibileOpt.isPresent() ? ammissibileOpt.get().getQuantita().intValue() : dichiarazioneConsumi.getRichiestaCarburante().getGasolioTerzi());
	}

	// servizio inteso come recupero di una dichiarazione consumi dell'anno precedente - non è esposto - utilizzato da altri service
	public CarburanteCompletoDto getResiduoAnnoPrecedente(String cuaa, Long campagna) {
		DichiarazioneConsumiFilter filtro = new DichiarazioneConsumiPagedFilter()
				.setNumeroElementiPagina(1)
				.setCuaa(cuaa)
				.setCampagna(Arrays.asList(campagna - 1))
				.setStati(Collections.singleton(StatoDichiarazioneConsumi.PROTOCOLLATA));
		Optional<DichiarazioneConsumiModel> dichiarazioneAnnoPrecedenteOpt = dichiarazioneConsumiDao.findOne(DichiarazioneConsumiSpecification.getFilter(filtro));

		return getConsuntivo(dichiarazioneAnnoPrecedenteOpt, TipoConsuntivo.RIMANENZA);
	}

	public CarburanteDto calcolaDisponibile(RichiestaCarburanteModel richiesta) {
		String cuaa = richiesta.getCuaa();
		Long campagna = richiesta.getCampagna();

		var residuo = getResiduoAnnoPrecedente(cuaa, campagna).toCarburanteDto();
		var trasferito = getTotaleCarburanteTrasferito(cuaa, campagna);
		var ricevuto = getTotaleCarburanteRicevuto(cuaa, campagna);
		var prelevato = getTotaleCarburantePrelevato(cuaa, campagna);
		var disponibile = carburanteDtoBuilder.newDto().add(residuo).add(ricevuto).add(prelevato).subtract(trasferito).build();

		logger.info("CarburanteHelper id richiesta {} - Disponibile: {}", richiesta.getId(), disponibile);
		return disponibile;
	}

	public CarburanteDto getTotaleCarburantePrelevato(String cuaa, Long campagna) {
		return prelieviService.getPrelievi(cuaa, campagna, null).getTotale();
	}

	public CarburanteCompletoDto getConsuntivo(Optional<DichiarazioneConsumiModel> dichiarazioneOpt, TipoConsuntivo tipoConsuntivo) {
		if (!dichiarazioneOpt.isPresent()) {return new CarburanteCompletoDtoBuilder().build();}
		return new CarburanteCompletoDtoBuilder()
				.from(dichiarazioneOpt.get().getConsuntivi(), tipoConsuntivo)
				.build();
	}
}
