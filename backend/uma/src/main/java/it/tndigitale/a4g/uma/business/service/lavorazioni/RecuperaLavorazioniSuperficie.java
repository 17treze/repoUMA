package it.tndigitale.a4g.uma.business.service.lavorazioni;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

// import it.tndigitale.a4g.fascicolo.territorio.client.model.CodificaColtura;
// import it.tndigitale.a4g.fascicolo.territorio.client.model.ColturaDto;
// import it.tndigitale.a4g.fascicolo.territorio.client.model.ParticellaDto;
import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.ColturaGruppiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.GruppoLavorazioneModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.business.persistence.entity.SuperficieMassimaModel;
import it.tndigitale.a4g.uma.business.persistence.repository.ColturaGruppiDao;
import it.tndigitale.a4g.uma.business.service.client.UmaTerritorioClient;
import it.tndigitale.a4g.uma.dto.richiesta.RaggruppamentoLavorazioniDto;
import it.tndigitale.a4g.uma.dto.richiesta.TerritorioAualDto;
import it.tndigitale.a4g.uma.dto.richiesta.UtilizzoSuoloAualDto;
import it.tndigitale.a4g.uma.dto.richiesta.UtilizzoTerrenoAualDto;
import it.tndigitale.a4g.uma.dto.richiesta.builder.RaggruppamentoLavorazioniBuilder;

@Component("RECUPERO_LAVORAZIONI_SUPERFICIE")
public class RecuperaLavorazioniSuperficie extends RecuperaLavorazioniStrategy {

	private static Logger logger = LoggerFactory.getLogger(RecuperaLavorazioniSuperficie.class);

	@Autowired
	private UmaTerritorioClient territorioClient;
	@Autowired
	private Clock clock;
	@Autowired
	private ColturaGruppiDao colturaGruppiDao;

	// Se la richiesta è in compilazione
	// Recupero tutte le quintuple associate ai piani colturali per il cuaa della domanda e la data attuale e calcola anche la superficie massima 
	// Se la richiesta è autorizzata o rettificata reperisco i raggruppamenti con le superfici massime salvate in fase di protocollazione
	@Override
	public List<RaggruppamentoLavorazioniDto> getRaggruppamenti(RichiestaCarburanteModel richiestaCarburante) {
		logger.info("[Lavorazioni UMA] - recupero lavorazioni superficie richiesta {}" , richiestaCarburante.getId());
		if (!StatoRichiestaCarburante.IN_COMPILAZIONE.equals(richiestaCarburante.getStato())) {
			return buildDtoSuperficiWithSuperficiMassime(richiestaCarburante.getSuperficiMassime());
		}
		// List<ParticellaDto> particelle = territorioClient.getColture(richiestaCarburante.getCuaa(), richiestaCarburante.getDataPresentazione());
		List<TerritorioAualDto> particelle = territorioClient.getColtureFromAual(richiestaCarburante.getCuaa(), richiestaCarburante.getDataPresentazione());
		return buildDtoSuperfici(particelle);

	}

	// utilizzato per recupero lavorazioni a superficie dichiarazioni consumi - Clienti conto terzi 
	public List<RaggruppamentoLavorazioniDto> getRaggruppamenti(String cuaaCliente, LocalDateTime dataConduzione) {
		// List<ParticellaDto> particelle = territorioClient.getColture(cuaaCliente, dataConduzione);
		List<TerritorioAualDto> particelle = territorioClient.getColtureFromAual(cuaaCliente, dataConduzione);
		return buildDtoSuperfici(particelle);
	}

	private List<RaggruppamentoLavorazioniDto> buildDtoSuperfici(List<TerritorioAualDto> particelle) {
		List<RaggruppamentoLavorazioniDto> listRaggruppamentoLavorazioniDto = new ArrayList<>();
		calcolaSuperficieMassima(particelle)
		.entrySet().stream()
		.forEach(entrySet -> 
		listRaggruppamentoLavorazioniDto.add(new RaggruppamentoLavorazioniBuilder()
				.withGruppo(entrySet.getKey())
				.withSuperficieMassima(entrySet.getValue().longValue())
				.withLavorazioni(entrySet.getKey().getLavorazioneModels())
				.build()));
		return listRaggruppamentoLavorazioniDto;
	}

	private List<RaggruppamentoLavorazioniDto> buildDtoSuperficiWithSuperficiMassime(List<SuperficieMassimaModel> superficiMassime) {
		return superficiMassime.stream().map(supMax -> new RaggruppamentoLavorazioniBuilder()
				.withGruppo(supMax.getGruppoLavorazione())
				.withSuperficieMassima(supMax.getSuperficieMassima())
				.withLavorazioni(supMax.getGruppoLavorazione().getLavorazioneModels())
				.build())
				.collect(Collectors.toList());
	}

	// restituisce una mappa con chiave il raggruppamento e valore la somma delle superfici a cui sono associate le colture presenti nel fascicolo 
	public Map<GruppoLavorazioneModel, Integer> calcolaSuperficieMassima(List<TerritorioAualDto> particelle) {
		HashMap<GruppoLavorazioneModel, Integer> mappaGruppoSuperficie = new HashMap<>();
		if (CollectionUtils.isEmpty(particelle)) {
			return new HashMap<>();
		}
		for (TerritorioAualDto p : particelle) {
			for (UtilizzoTerrenoAualDto t : p.getListaUtilizzoTerreno()) {
				for (UtilizzoSuoloAualDto u : t.getListaUtilizzoSuolo()) {
					Optional<GruppoLavorazioneModel> gruppoOpt = codificaToGruppo.apply(u);
					if (gruppoOpt.isPresent()) {
						mappaGruppoSuperficie.merge(gruppoOpt.get(), Integer.parseInt(u.getNumeSupeUtil()), (a,b) -> a + b);
					}
				}
			}
		}
		/*
		particelle.stream()
		.map(TerritorioAualDto::getListaUtilizzoTerreno)
		.flatMap(List::stream)
		.map(UtilizzoTerrenoAualDto::getListaUtilizzoSuolo)
		.flatMap(List::stream)
		// TASK-UMA-45: Recupero Superficie Dichiarata - Se il valore della superficie accertata è null o è zero, recuperare il valore della sup. Dichiarata.
		.collect(Collectors.groupingBy(UtilizzoSuoloAualDto::, Collectors.summingInt(x -> x.getSupeElig() != 0 ? x.getSupeElig() : x.getNumeSupeUtil())))
		.forEach((utilizzoSuoloAualDto, superficie) -> {
			Optional<GruppoLavorazioneModel> gruppoOpt = codificaToGruppo.apply(utilizzoSuoloAualDto);
			if (gruppoOpt.isPresent()) {
				mappaGruppoSuperficie.merge(gruppoOpt.get(), superficie, (a,b) -> a + b);
			}
		});
		*/	
		mappaGruppoSuperficie.forEach((g,s) -> logger.info("[Lavorazioni UMA] - Calcolo superficie massima: gruppo {} superficie {}" , g.getIndice() ,s));
		return mappaGruppoSuperficie;
	}

	private Function<UtilizzoSuoloAualDto, Optional<GruppoLavorazioneModel>> codificaToGruppo = c -> {
		Optional<ColturaGruppiModel> colturaGruppiModelOpt = Optional.ofNullable(colturaGruppiDao.findByCodificaAndAnno(c.getCodiOccu(), c.getCodiDest(), c.getCodiUso(), c.getCodiQual(), c.getCodiOccuVari(), clock.today().getYear()));
		if (colturaGruppiModelOpt.isPresent()) {
			return Optional.of(colturaGruppiModelOpt.get().getGruppoLavorazione());
		}
		return Optional.empty();
	};
}
