package it.tndigitale.a4gistruttoria.dto.domandaunica;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tndigitale.a4gistruttoria.dto.lavorazione.DatiOutput;
import it.tndigitale.a4gistruttoria.dto.lavorazione.VariabileCalcolo;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.util.TipoVariabile;
import it.tndigitale.a4gistruttoria.util.TipologiaPassoTransizione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class TransizioniHelper {

	private TransizioniHelper() {}

	private static final Logger logger = LoggerFactory.getLogger(TransizioniHelper.class);
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	// recupera tutte le transizioni di stato eliminandone i duplicati in base alla più recente 
	public static List<TransizioneIstruttoriaModel> recuperaTransizioniSostegno (IstruttoriaModel istruttoria) {
		Map<String, List<TransizioneIstruttoriaModel>> collect =
				// domandaUnicaModel.getA4gtTransizioneSostegnos().stream().filter(
				istruttoria.getTransizioni().stream().filter(
						x -> x.getIstruttoria().getSostegno().equals(istruttoria.getSostegno())
				).collect(
						Collectors.groupingBy(x -> x.getA4gdStatoLavSostegno1().getIdentificativo())
				);

		// recupera tutte le transizioni del sostegno filtrandone i duplicati in base alla più recente 
		List<TransizioneIstruttoriaModel> transizioniIstruttoria = new ArrayList<>();
		collect.forEach((a,b) -> {
			TransizioneIstruttoriaModel transizione = b.stream()
					.filter(x -> x.getDataEsecuzione() != null)
					.max(Comparator.comparing(TransizioneIstruttoriaModel::getDataEsecuzione))
					.get();
			transizioniIstruttoria.add(transizione);
		});

		return transizioniIstruttoria;
	}
	
	// Recupera tutti i passi da una transizione e li filtra per tipologiaPasso. Dopodichè prende datiOutput con la variabile che si vuole recuperare
	public static BigDecimal recuperaImporto(TipoVariabile tipoVariabile, TipologiaPassoTransizione tipologiaPasso, TransizioneIstruttoriaModel transizione) {
		Optional<PassoTransizioneModel> passoControlliFinaliOpt = transizione
				.getPassiTransizione()
				.stream()
				.filter(passo -> tipologiaPasso.equals(passo.getCodicePasso()))
				.findFirst();

		BigDecimal returnValue = null;
		if (passoControlliFinaliOpt.isPresent()) {
			try {
				DatiOutput datiOutput = objectMapper.readValue(passoControlliFinaliOpt.get().getDatiOutput(), DatiOutput.class);
				Optional<VariabileCalcolo> variabileCalcoloOpt = datiOutput.getVariabiliCalcolo()
						.stream()
						.filter(dati -> tipoVariabile.equals(dati.getTipoVariabile()))
						.findFirst();
				if (variabileCalcoloOpt.isPresent()) {
					returnValue = variabileCalcoloOpt.get().getValNumber();				
				}
			} catch (IOException e) {
				logger.warn(String.format("Fallita conversione JSON dati output. Passo Lavorazione %s", passoControlliFinaliOpt.get().getId().toString()));
			}
		}
		return returnValue;
	}
	
	// filtro le transizioni nei cui passi sono settate le variabili che mi interessa recuperare.
	// transizioniSostegno in input al metodo dovrebbe essere già filtrato in base alla più recente e raggruppate in base al sostegno 
	public static Optional<TransizioneIstruttoriaModel> recuperaTransizioneSostegnoByStato(List<TransizioneIstruttoriaModel> transizioniSostegno, StatoIstruttoria statoLavorazioneSostegno) {
		return Optional.ofNullable(transizioniSostegno)
				.orElse(Collections.emptyList())
				.stream()
				.filter(transizione -> statoLavorazioneSostegno.getStatoIstruttoria().equals(transizione.getA4gdStatoLavSostegno1().getIdentificativo()))
				.findFirst();
	}
}
