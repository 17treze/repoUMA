package it.tndigitale.a4gutente.codici;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum StatoDomandaRegistrazioneUtente {

	IN_COMPILAZIONE,
	PROTOCOLLATA,
	CHIUSA,
	IN_LAVORAZIONE,
	APPROVATA,
	RIFIUTATA;

	public static List<StatoDomandaRegistrazioneUtente> ritornaComplementari(List<StatoDomandaRegistrazioneUtente> stati) {
		List<StatoDomandaRegistrazioneUtente> result = Arrays.asList(StatoDomandaRegistrazioneUtente.values());
		if (stati!=null && !stati.isEmpty()) {
			return result.stream()
						 .filter(stato -> !stati.contains(stato))
						 .collect(Collectors.toList());
		}
		return result;
	}
}
