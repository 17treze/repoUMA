package it.tndigitale.a4g.framework.pagination.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;

import java.util.List;

@JsonPropertyOrder({ "count", "risultati" })
@ApiModel("Rappresenta l'oggetto contenente i risultati di una ricerca paginata")
public class RisultatiPaginati<T> {

    @ApiParam(value = "Risultati della ricerca paginata")
    private List<T> risultati;
    @ApiParam(value = "Numero totale dei risultati di una ricerca paginata")
    private Long count;

    public RisultatiPaginati() {
    }

    public static<T> RisultatiPaginati<T> of(List<T> risultati, Long count) {
        return new RisultatiPaginati<T>().setCount(count)
                                         .setRisultati(risultati);
    }

    public List<T> getRisultati() {
        return risultati;
    }

    public RisultatiPaginati<T> setRisultati(List<T> risultati) {
        this.risultati = risultati;
        return this;
    }

    public Long getCount() {
        return count;
    }

    public RisultatiPaginati<T> setCount(Long count) {
    		if (count == null) {
    				this.count = 0L;
    		} else {
    				this.count = count;
    		}
        
        return this;
    }

}
