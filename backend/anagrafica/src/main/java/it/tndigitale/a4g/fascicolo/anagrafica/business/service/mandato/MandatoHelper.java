package it.tndigitale.a4g.fascicolo.anagrafica.business.service.mandato;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;

public class MandatoHelper {

	private MandatoHelper() {}

	/**
	 * Ottengo tutti i mandati iniziati da @date data in poi.
	 * 
	 * @param mandati
	 * @param date
	 * @return Lista dei mandati
	 */
	public static List<MandatoModel> getMandatiStartingFromLocalDate(List<MandatoModel> mandati, LocalDate date) {
		return mandati
				.stream()
				.filter(mandato -> mandato.getDataInizio().compareTo(date) >= 0)
				.collect(Collectors.toList());
	}

	/**
	 * Ottengo l'ultimo mandato iniziato strettamente prima di @date. Deve esistere. 
	 * 
	 * @param mandati
	 * @param date
	 * @return il mandato, se esiste
	 * @throws EntityNotFoundException 
	 */
	public static MandatoModel getLastMandatoStartingStrictlyBeforeLocalDate (List<MandatoModel> mandati, LocalDate date) {
		return mandati.stream()
				.filter(mandato -> mandato.getDataInizio().compareTo(date) < 0)
				.max(Comparator.comparing(MandatoModel::getDataInizio))
				.orElseThrow(() -> new EntityNotFoundException("Mandato non trovato"));
	}
}
