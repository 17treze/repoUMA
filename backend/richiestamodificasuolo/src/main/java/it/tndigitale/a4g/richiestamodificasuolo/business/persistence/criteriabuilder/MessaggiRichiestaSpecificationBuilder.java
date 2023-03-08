package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.criteriabuilder;

import org.springframework.data.jpa.domain.Specification;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.MessaggioRichiestaModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.MessaggioRichiestaModel_;
public class MessaggiRichiestaSpecificationBuilder {
	
	private MessaggiRichiestaSpecificationBuilder() {
	}
	
	public static Specification<MessaggioRichiestaModel> getFilter(Long idRichiesta) {
		return (root, query, cb) -> {
			if (idRichiesta == null) {
				return null;
			}
			
			return Specification.where(getFilterIdRichiesta(idRichiesta))
					.toPredicate(root, query, cb);
		};
	}

	private static Specification<MessaggioRichiestaModel> getFilterIdRichiesta(Long idRichiesta) {
		return (root, query, cb) -> {
			if (idRichiesta == null) {
				return null;
			}
			return cb.equal(root.get(MessaggioRichiestaModel_.richiestaModificaSuolo), idRichiesta);
		};
	}

}
