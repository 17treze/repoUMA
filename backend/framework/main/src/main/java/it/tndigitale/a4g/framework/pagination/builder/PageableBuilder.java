package it.tndigitale.a4g.framework.pagination.builder;

import static java.util.Arrays.asList;

import java.util.List;

import it.tndigitale.a4g.framework.pagination.model.Paginazione;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import it.tndigitale.a4g.framework.pagination.model.Ordinamento;

public class PageableBuilder {

    private PageableBuilder() {
    		super();
    }
    
    public static PageableBuilder build() {
    		return new PageableBuilder();
    }
    
    public Pageable from(Paginazione paginazione, List<Ordinamento> ordinamenti) {
        Sort sort = SortBuilder.build().from(ordinamenti);
    	if (paginazione == null) {
            return PageRequest.of(Paginazione.PAGINAZIONE_DEFAULT.getPagina(),
                                  Paginazione.PAGINAZIONE_DEFAULT.getNumeroElementiPagina(),
                                  sort);
        }
    	// perche' unpaged?
        return PageRequest.of(paginazione.getPagina(),
                              paginazione.getNumeroElementiPagina(),
                              sort);
    }
    
    public Pageable from(Paginazione paginazione) {
        List<Ordinamento> ordinamenti = null;
        return from(paginazione, ordinamenti);
    }

    public Pageable from(Paginazione paginazione, Ordinamento...ordinamenti) {
        return from(paginazione, asList(ordinamenti));
    }

}
