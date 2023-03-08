package it.tndigitale.a4gistruttoria.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import it.tndigitale.a4gistruttoria.dto.Ordinamento.Ordine;

public class ConvertitorePaginazione {

	private static final Map<Ordine, Direction> ordineDirezioneMap = new HashMap<Ordine, Direction>();
	static {
		ordineDirezioneMap.put(Ordine.ASC, Direction.ASC);
	}

	private ConvertitorePaginazione() {

	}

	public static Pageable converti(Paginazione paginazione) {
		if (paginazione == null)
			return Pageable.unpaged();
		return PageRequest.of(paginazione.getPagina(), paginazione.getNumeroElementiPagina());
	}

	@Deprecated
	public static Pageable converti(Paginazione paginazione, List<Ordinamento> ordinamenti) {
		if (ordinamenti == null)
			return converti(paginazione);
		Sort sort = converti(ordinamenti);
		return PageRequest.of(paginazione.getPagina(), paginazione.getNumeroElementiPagina(), sort);
	}

	public static Sort converti(List<Ordinamento> ordinamenti) {
		Sort result = Sort.unsorted();
		if (ordinamenti != null) {
			for (Ordinamento o : ordinamenti) {
				result = result.and(converti(o));
			}
		}
		if (result.isUnsorted()) {
			return Sort.by(Direction.DESC, "id");
		}
		return result;
	}

	public static Sort converti(Ordinamento ordinamento) {
		if (ordinamento == null)
			return Sort.unsorted();
		return Sort.by(Direction.valueOf(ordinamento.getOrdine().name()), ordinamento.getProprieta());
	}

	public static <T> Pagina<T> converti(Page<T> pagina) {
		Pagina<T> risultato = new Pagina<>();
		risultato.setElementiTotali(pagina.getTotalElements());
		risultato.setRisultati(pagina.getContent());
		return risultato;
	}
}
