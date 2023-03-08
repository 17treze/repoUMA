package it.tndigitale.a4gistruttoria.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;

import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoBdn;
import it.tndigitale.a4gistruttoria.dto.zootecnia.CapoLatteDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.DetenzioneDto;
import it.tndigitale.a4gistruttoria.dto.zootecnia.VitelloBdn;
import it.tndigitale.a4gistruttoria.dto.zootecnia.VitelloDto;

public class ControlloCapiUtil {

	private static final Logger logger = LoggerFactory.getLogger(ControlloCapiUtil.class);

	private ControlloCapiUtil() {
	}


	/**
	 * Trova la detenzione in cui è avvenuto il primo parto.
	 * Se la data nascita del vitello coincide con data inizio e data fine detenzione, viene scelta la prima detenzione in ordine cronologico
	 * @param primoParto
	 * @param detenzioni
	 * @return  Optional<DetenzioneDto>
	 */
	public static Optional<DetenzioneDto> trovaDetenzioneDelPrimoParto(VitelloDto primoParto, List<DetenzioneDto> detenzioni) {
		LocalDate dataNascitaVitello = primoParto.getDataNascita();
		return detenzioni.stream().filter(detenzione -> 
		dataNascitaVitello.compareTo(detenzione.getDtInizioDetenzione()) >= 0 &&
		dataNascitaVitello.compareTo(detenzione.getDtFineDetenzione()) <= 0)
				.sorted((d1,d2) -> d1.getDtInizioDetenzione().compareTo(d2.getDtInizioDetenzione()))
				.findFirst();
	}

	//Scelgo il primo parto
	public static Optional<JsonNode> trovaPrimoParto(List<JsonNode> clsCapiVacca) {
		return trovaVitelli(clsCapiVacca)
				.stream()
				.sorted((JsonNode capo1,JsonNode capo2) -> 
				Long.valueOf(capo1.path("dtNascitaVitello").longValue()).compareTo(capo2.path("dtNascitaVitello").longValue()))
				.findFirst();
	}

	//	public static List<JsonNode> trovaVitelliOK(List<JsonNode> clsCapiVacca) {
	//		return clsCapiVacca.stream()
	//				.filter(clsCapoVacca -> { 
	//					//Dt_Nascita_Vitello sia compresa tra Dt_Inizio_Detenzione e Dt_Fine_Detenzione
	//					long dtNascitaVitello = clsCapoVacca.path("dtNascitaVitello").longValue();
	//					long dtInizioDetenzione = clsCapoVacca.path("dtInizioDetenzione").longValue();
	//					long dtFineDetenzione = clsCapoVacca.path("dtFineDetenzione").longValue();
	//					return (dtNascitaVitello>=dtInizioDetenzione && dtNascitaVitello<=dtFineDetenzione) ? true:false;
	//				})
	//				.collect(Collectors.toList());
	//	}

	public static List<JsonNode> trovaVitelli(List<JsonNode> clsCapiVacca) {
		return clsCapiVacca.stream()
				.filter(clsCapoVacca -> { 
					return !checkIfNull(clsCapoVacca.path("vitelloCapoId"));
				})
				.collect(Collectors.toList());
	}

	private static boolean checkIfNull(JsonNode node) {
		return node.isNull() || node.isMissingNode();
	}

	//In caso di più parti nel corso dell’anno va selezionato solo il primo e scartati i successivi.
	@Deprecated
	public static Optional<VitelloBdn> getPrimoParto(CapoBdn capoBdn) {
		return 
				Optional
				.ofNullable(capoBdn.getCapoLatteBdn().getVitelli())
				.map(listaVitelli -> 
				listaVitelli
				.stream()
				.filter(vitello -> { 
					//Dt_Nascita_Vitello sia compresa tra Dt_Inizio_Detenzione e Dt_Fine_Detenzione
					long dtNascitaVitello = vitello.getDtNascita().getTime();
					long dtInizioDetenzione = capoBdn.getDtInizioDetenzione().getTime();
					long dtFineDetenzione = capoBdn.getDtFineDetenzione().getTime();
					return (dtNascitaVitello>=dtInizioDetenzione && dtNascitaVitello<=dtFineDetenzione) ? true:false;
				})							
				.sorted((VitelloBdn capo1,VitelloBdn capo2) -> capo1.getDtNascita().compareTo(capo2.getDtNascita()))
				.findFirst())
				.orElse(Optional.empty());
	}

	public static Optional<VitelloDto> getPrimoParto(CapoLatteDto capoDto) {
		return capoDto.getVitelli().stream()
				.sorted((v1,v2) -> v1.getDataNascita().compareTo(v2.getDataNascita()))
				.findFirst();
	}

	public static Boolean isPrimoPartoGemellare(CapoLatteDto capoDto) {
		List<VitelloDto> vitelli = capoDto.getVitelli().stream()
				.sorted((v1,v2) -> v1.getDataNascita().compareTo(v2.getDataNascita()))
				.collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(vitelli) && "W".equalsIgnoreCase(vitelli.get(0).getTipoOrigine())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public static List<VitelloDto> getPrimoPartoGemellare(CapoLatteDto capoDto) {
		Optional<List<VitelloDto>> gemelliOpt = capoDto.getVitelli().stream()
				.filter(v -> "W".equalsIgnoreCase(v.getTipoOrigine()))
				.collect(Collectors.groupingBy(VitelloDto::getDataNascita))
				.entrySet()
				.stream()
				.sorted((v1,v2) -> v1.getKey().compareTo(v2.getKey()))
				.map(Entry::getValue)
				.findFirst();

		return gemelliOpt.isPresent() ? gemelliOpt.get() : new ArrayList<>();
	}

	/**
	 * 
	 * ordina i capi per data detenzione e restituisce una lista di detenzione che continue e senza buchi
	 * @param capiMacellati
	 * @return
	 */
	public static List<DetenzioneDto> ordinaEfiltraCapi(List<DetenzioneDto> capiMacellati) {

		if (capiMacellati.size() == 1) {
			return capiMacellati;
		}

		List<DetenzioneDto> detenzioniOrdinate = capiMacellati.stream()
				.sorted((DetenzioneDto c1, DetenzioneDto c2) -> c2.getDtInizioDetenzione().compareTo(c1.getDtInizioDetenzione())) // ordino inverso per data inizio
				.collect(Collectors.toList());

		List<DetenzioneDto> detenzioniSenzaBuchi = new ArrayList<>();
		detenzioniSenzaBuchi.add(detenzioniOrdinate.get(0));
		//prendo solo detenzioni contigue/continue e senza buchi
		for (int i = 0; i < detenzioniOrdinate.size()-1; i++) {
			var current = detenzioniOrdinate.get(i);
			var next = detenzioniOrdinate.get(i+1);

			//dtInizioDetenzione == dtFineDetenzione perchè è in ordine inverso
			if (current.getDtInizioDetenzione().compareTo(next.getDtFineDetenzione()) == 0) {
				detenzioniSenzaBuchi.add(next);
			} else {
				break;
			}
		}
		return detenzioniSenzaBuchi;
	}

	/** Quando eseguo il confronto tra date dove almeno una delle due è una registrazione in BDN, prima di eseguire tale confronto 
	devo verificare se sono comprese le giornate del 10, 11, 12 maggio 2021.
	Nel caso in cui fossero comprese tra 1 e 3 giornate devo “ridurre il periodo” tra 1 e 3 giorni.*/
	public static long getNumeroGiorniOffline(LocalDate startDate, LocalDate endDate) {
		LocalDate day10_5 = LocalDate.of(2021, 5, 10);
		LocalDate day11_5 = LocalDate.of(2021, 5, 11);
		LocalDate day12_5 = LocalDate.of(2021, 5, 12);
		long offset = 0L;
		List<LocalDate> offlineDays = Arrays.asList(day10_5, day11_5, day12_5 );
		// d >= startDate || <= endDate
		offset = offlineDays.stream().filter(offDay -> offDay.isAfter(startDate) || offDay.isEqual(startDate) || 
				offDay.isBefore(endDate) || offDay.isEqual(endDate)).count();
		logger.debug("startDate: {}, endDate: {}, offlineDays: {}", startDate, endDate, offset );
		return offset;
	}
}
