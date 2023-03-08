package it.tndigitale.a4gistruttoria.dto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiSintesi;
import it.tndigitale.a4gistruttoria.dto.lavorazione.EsitoControllo;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipoControllo.LivelloControllo;

public class EsitoControlloBuilder {

	private EsitoControlloBuilder() {}

	private static final Logger logger = LoggerFactory.getLogger(EsitoControlloBuilder.class);

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static List<EsitoControllo> from(List<TransizioneIstruttoriaModel> transizioneSostegni){

		List<EsitoControllo> esitiControlli = new ArrayList<>();
		transizioneSostegni.forEach(transizione -> 
		transizione.getPassiTransizione().forEach(passo -> {
			try {
				DatiSintesi datiSintesi = objectMapper.readValue(passo.getDatiSintesiLavorazione(), DatiSintesi.class);
// 				decomettentare per recuperare solo le anomalie warning ed error
//				esitiControlli.addAll(filtraLivelloControllo(datiSintesi.getEsitiControlli()));
				esitiControlli.addAll(datiSintesi.getEsitiControlli());
			} catch (IOException e) {
				logger.error(String.format("Fallita conversione JSON dati sintesi lavorazione. Passo Lavorazione %s", passo.getId().toString()));
			}
		}));
		return esitiControlli;
	}

	// filtra i dati sintesi lavorazione solo per le anomalie error e warning
	private static List<EsitoControllo> filtraLivelloControllo(List<EsitoControllo> esitiControlli) {

		Set<LivelloControllo> livelliControlloFilter = new HashSet<>();
		livelliControlloFilter.add(LivelloControllo.ERROR);
		livelliControlloFilter.add(LivelloControllo.WARNING);

		return esitiControlli
				.stream()
				.filter(x -> livelliControlloFilter.contains(x.getLivelloControllo()))
				.collect(Collectors.toList());
	}
}
