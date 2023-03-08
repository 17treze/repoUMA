package it.tndigitale.a4g.uma.business.service.consumi;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4g.framework.support.CustomCollectors;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.ConsuntivoConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburanteConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoConsuntivo;
import it.tndigitale.a4g.uma.business.persistence.repository.DichiarazioneConsumiDao;
import it.tndigitale.a4g.uma.business.service.client.UmaProxyClient;
import it.tndigitale.a4g.uma.business.service.consumi.calcoli.CarburanteHelper;
import it.tndigitale.a4g.uma.business.service.richiesta.RichiestaCarburanteService;
import it.tndigitale.a4g.uma.dto.consumi.CarburanteDto;
import it.tndigitale.a4g.uma.dto.consumi.StampaDichiarazioneConsumiDto;
import it.tndigitale.a4g.uma.dto.consumi.StampaPrelievoDto;
import it.tndigitale.a4g.uma.dto.consumi.builder.CarburanteCompletoDtoBuilder;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteCompletoDto;
import it.tndigitale.a4g.uma.dto.richiesta.CarburanteTotale;
import it.tndigitale.a4g.uma.dto.richiesta.PrelievoDto;

@Service
public class StampaDichiarazioneConsumiService {

	@Autowired
	private DichiarazioneConsumiDao dichiarazioneConsumiDao;
	@Autowired
	private RichiestaCarburanteService prelieviService;
	@Autowired
	private UmaProxyClient proxyClient;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private Clock clock;
	@Autowired
	private CarburanteHelper carburanteHelper;

	private static final String TEMPLATE_PATH = "resources/templateDichiarazioneConsumi.docx";

	public Resource stampaDichiarazioneConsumi(Long idDichiarazione) throws IOException {
		DichiarazioneConsumiModel dichiarazioneConsumi = dichiarazioneConsumiDao.findById(idDichiarazione).orElseThrow(() -> new EntityNotFoundException("Nessuna Dichiarazione Consumi trovata per id ".concat(String.valueOf(idDichiarazione))));

		// Se la dichiarazione consumi è protocollata , viene scaricato il documento caricato in fase di protocollazione
		if (dichiarazioneConsumi.getStato().equals(StatoDichiarazioneConsumi.PROTOCOLLATA)) {
			return new ByteArrayResource(dichiarazioneConsumi.getDocumento());
		}

		var json = objectMapper.writeValueAsString(buildStampaDto(dichiarazioneConsumi));
		return new ByteArrayResource(proxyClient.stampa(json, TEMPLATE_PATH));
	}

	private StampaDichiarazioneConsumiDto buildStampaDto(DichiarazioneConsumiModel dichiarazioneConsumi) {
		var stampaDto = new StampaDichiarazioneConsumiDto();

		List<ConsuntivoConsumiModel> consuntivi = dichiarazioneConsumi.getConsuntivi();
		RichiestaCarburanteModel richiesta = dichiarazioneConsumi.getRichiestaCarburante();

		//Residuo
		var residuo = carburanteHelper.getResiduoAnnoPrecedente(richiesta.getCuaa(), richiesta.getCampagna());

		//Ammissibile
		CarburanteCompletoDto ammissibileCompleto = carburanteHelper.getAmmissibileCompleto(dichiarazioneConsumi);

		//Richiesto
		var carburanteRichiesto = new CarburanteCompletoDto()
				.setBenzina(richiesta.getBenzina())
				.setGasolio(richiesta.getGasolio())
				.setGasolioSerre(richiesta.getGasolioSerre())
				.setGasolioTerzi(richiesta.getGasolioTerzi());

		String cuaa = richiesta.getCuaa();
		Long campagna = richiesta.getCampagna();

		// Prelievi
		CarburanteTotale<PrelievoDto> prelievi = prelieviService.getPrelievi(cuaa, campagna, null);
		List<StampaPrelievoDto> stampaPrelievi = new ArrayList<>();
		if (!CollectionUtils.isEmpty(prelievi.getDati())) {
			prelievi.getDati().forEach(prelievo -> stampaPrelievi.add(new StampaPrelievoDto()
					.setDistributore(prelievo.getDistributore())
					.setCarburante(prelievo.getCarburante())
					.setData(prelievo.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))));
		}

		//Ricevuto
		CarburanteDto ricevutiTotale = carburanteHelper.getTotaleCarburanteRicevuto(cuaa,campagna);

		//Trasferito
		CarburanteDto trasferitiTotale = carburanteHelper.getTotaleCarburanteTrasferito(cuaa,campagna);

		//Disponibile
		CarburanteDto disponibile = carburanteHelper.calcolaDisponibile(richiesta);

		//Accisa
		CarburanteDto accisa = carburanteHelper.calcolaAccisa(dichiarazioneConsumi);

		//Motivazioni Consuntivi
		consuntivi.stream()
		.filter(c -> c.getMotivazione() != null)
		.collect(Collectors.groupingBy(ConsuntivoConsumiModel::getTipoConsuntivo , Collectors.groupingBy(ConsuntivoConsumiModel::getTipoCarburante, CustomCollectors.toSingleton())))
		.entrySet()
		.stream().forEach(entrySet -> {
			var value = entrySet.getValue();
			if (TipoConsuntivo.AMMISSIBILE.equals(entrySet.getKey())) {
				stampaDto.setMotivazioneAmmissibile(value.get(TipoCarburanteConsuntivo.GASOLIO_TERZI) != null ? value.get(TipoCarburanteConsuntivo.GASOLIO_TERZI).getMotivazione().name() : null);
			}

			if (TipoConsuntivo.RECUPERO.equals(entrySet.getKey())) {
				stampaDto
				.setMotivazioneRecuperoBenzina(value.get(TipoCarburanteConsuntivo.BENZINA) != null ? value.get(TipoCarburanteConsuntivo.BENZINA).getMotivazione().name() : null)
				.setMotivazioneRecuperoGasolio(value.get(TipoCarburanteConsuntivo.GASOLIO) != null ? value.get(TipoCarburanteConsuntivo.GASOLIO).getMotivazione().name() : null)
				.setMotivazioneRecuperoGasolioSerre(value.get(TipoCarburanteConsuntivo.GASOLIO_SERRE) != null ? value.get(TipoCarburanteConsuntivo.GASOLIO_SERRE).getMotivazione().name() : null)
				.setMotivazioneRecuperoGasolioTerzi(value.get(TipoCarburanteConsuntivo.GASOLIO_TERZI) != null ? value.get(TipoCarburanteConsuntivo.GASOLIO_TERZI).getMotivazione().name() : null);
			}
		});
		var builder = new CarburanteCompletoDtoBuilder();

		return stampaDto
				.setIdDichiarazioneConsumi(dichiarazioneConsumi.getId())
				.setAnnoCampagna(richiesta.getCampagna())
				.setCuaa(richiesta.getCuaa())
				.setDenominazione(richiesta.getDenominazione().equals("NO_DENOM") ? null : richiesta.getDenominazione())
				.setPrelievi(stampaPrelievi)
				.setAnnoCampagnaRimanenza(richiesta.getCampagna() - 1)
				.setResiduo(residuo)
				.setRichiesto(carburanteRichiesto)
				.setRicevuto(ricevutiTotale)
				.setTrasferito(trasferitiTotale)
				.setPrelevato(prelievi.getTotale())
				.setDisponibile(disponibile)
				.setAccisa(accisa)
				.setMotivazioneAccisa(dichiarazioneConsumi.getMotivazioneAccisa())
				.setAmmissibile(ammissibileCompleto)
				.setConsumato(builder.newDto().from(consuntivi, TipoConsuntivo.CONSUMATO).build())
				.setRimanenza(builder.newDto().from(consuntivi, TipoConsuntivo.RIMANENZA).build())
				.setRecupero(builder.newDto().from(consuntivi, TipoConsuntivo.RECUPERO).build())
				.setDataStampa(clock.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	}
}
