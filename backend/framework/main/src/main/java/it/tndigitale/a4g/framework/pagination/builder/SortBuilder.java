package it.tndigitale.a4g.framework.pagination.builder;

import it.tndigitale.a4g.framework.pagination.model.Ordinamento;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SortBuilder {

	private Ordinamento disabiguazioneOrderBy = Ordinamento.DEFAULT_ORDER_BY;
		
	private SortBuilder() { }
    
    public static SortBuilder build() {
		return new SortBuilder();
    }

    public SortBuilder disambiguate(Ordinamento disabiguazione) {
		this.disabiguazioneOrderBy = disabiguazione;
		return this;
    }

	public Sort from(List<Ordinamento> ordinamenti) {
		// Per evitare UnsupportedOperationException
		// vedi: https://stackoverflow.com/questions/5755477/java-list-add-unsupportedoperationexception
		List<Ordinamento> tmp = createOrderArray(ordinamenti);
		if (!tmp.contains(disabiguazioneOrderBy)) {
			tmp.add(disabiguazioneOrderBy);
		}
        return createSortFromList(tmp);
    }

	private List<Ordinamento> createOrderArray(List<Ordinamento> ordinamenti) {
		return Optional.ofNullable(ordinamenti)
				       .orElse(new ArrayList<>())
					   .stream().collect(Collectors.toList());
	}

	private Sort createSortFromList(List<Ordinamento> tmp) {
		return tmp.stream()
				  .map(ord -> getBy(ord))
				  .reduce(Sort.unsorted(), (sort1, sort2) -> sort1.and(sort2));
	}

	private Sort getBy(Ordinamento o) {
		return Sort.by(Direction.valueOf(o.getOrdine().name()), o.getProprieta());
	}

	public Sort from(Ordinamento ordinamento) {
		List<Ordinamento> ordinamenti = new ArrayList<>();
        if (ordinamento != null) {
        		ordinamenti.add(ordinamento);
        }
        return from(ordinamenti);
    }

}
