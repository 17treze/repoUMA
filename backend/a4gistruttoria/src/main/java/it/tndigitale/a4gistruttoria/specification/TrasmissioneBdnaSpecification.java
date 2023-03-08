package it.tndigitale.a4gistruttoria.specification;

import static org.springframework.data.jpa.domain.Specification.where;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import it.tndigitale.a4gistruttoria.dto.TrasmissioneBdnaFilter;
import it.tndigitale.a4gistruttoria.dto.TrasmissioneBdnaFilter.StatoTrasmissioneBdna;
import it.tndigitale.a4gistruttoria.repository.model.A4gtTrasmissioneBdna;

@Component
public class TrasmissioneBdnaSpecification {

	private final String wildcard = "%";


	public Specification<A4gtTrasmissioneBdna> getFilter(TrasmissioneBdnaFilter trasmissioneBdnaFilter) {
		return (root, query, cb) -> {

			return where(dtConferma(trasmissioneBdnaFilter.getStatoTrasmissione())) 
					.and(cfOperatoreFilter(trasmissioneBdnaFilter.getCfOperatore()))
					.and(idFilter(trasmissioneBdnaFilter.getId()))
					.toPredicate(root, query, cb);
		};
	}

	private Specification<A4gtTrasmissioneBdna> dtConferma(StatoTrasmissioneBdna statoTrasmissioneBdna) {
		return (root, query, cb) -> {
			if (statoTrasmissioneBdna == null) {
				return null;
			}
			if (StatoTrasmissioneBdna.NON_CONFERMATA.equals(statoTrasmissioneBdna)) {
				return cb.isNull(root.get("dtConferma"));
			} else if (StatoTrasmissioneBdna.CONFERMATA.equals(statoTrasmissioneBdna)) {
				return cb.isNotNull(root.get("dtConferma"));
			}
			return null;
		};
	}

	private Specification<A4gtTrasmissioneBdna> cfOperatoreFilter(String cfOperatore) {
		return attributeContains("cfOperatore", cfOperatore);
	}

	private Specification<A4gtTrasmissioneBdna> idFilter(Long id) {
		return (root, query, cb) -> {
			if (id == null) {
				return null;
			}
			return cb.equal(root.get("id"), id);
		};
	}

	private Specification<A4gtTrasmissioneBdna> attributeContains(String attribute, String value) {
		return (root, query, cb) -> {
			if(value == null) {
				return null;
			}

			return cb.like(
					cb.lower(root.get(attribute)),
					containsLowerCase(value)
					);
		};
	}

	private String containsLowerCase(String searchField) {
		return wildcard + searchField.toLowerCase() + wildcard;
	}
}
