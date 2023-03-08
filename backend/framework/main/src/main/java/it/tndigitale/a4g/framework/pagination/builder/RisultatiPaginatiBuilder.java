package it.tndigitale.a4g.framework.pagination.builder;

import it.tndigitale.a4g.framework.pagination.model.RisultatiPaginati;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class RisultatiPaginatiBuilder {

	private RisultatiPaginatiBuilder() {
    		super();
    }
    
    public static RisultatiPaginatiBuilder build() {
    		return new RisultatiPaginatiBuilder();
    }
    
    public <T> RisultatiPaginati<T> from(Page<T> pagina) {
   		if (pagina == null) {
   			return new RisultatiPaginati<T>().setRisultati(null).setCount(null);
   		}
        return new RisultatiPaginati<T>().setRisultati(pagina.stream().collect(Collectors.toList()))
									     .setCount(pagina.getTotalElements());
    }
}
