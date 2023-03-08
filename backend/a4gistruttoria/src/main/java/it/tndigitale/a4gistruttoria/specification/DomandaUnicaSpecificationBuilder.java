package it.tndigitale.a4gistruttoria.specification;

import it.tndigitale.a4gistruttoria.dto.DomandaUnicaRicercaFilter;
import it.tndigitale.a4gistruttoria.repository.model.*;
import it.tndigitale.a4gistruttoria.util.StringSupport;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

import static org.springframework.data.jpa.domain.Specification.where;

public class DomandaUnicaSpecificationBuilder {

	public static Specification<DomandaUnicaModel> getDomandaUnicaFilter(final DomandaUnicaRicercaFilter domandaUnicaRicercaFilter) {
		 return (root, query, cb) -> {
			 query.distinct(true);
			 return where(cuaaFilter(domandaUnicaRicercaFilter.getCuaaIntestatario()))
						 .and(ragioneSocialeFilter(domandaUnicaRicercaFilter.getRagioneSociale()))
						 .and(numeroDomandaFilter(domandaUnicaRicercaFilter.getNumeroDomanda()))
						 .and(statoDomandaFilter(domandaUnicaRicercaFilter.getStatoDomanda()))
						 .and(annoCampagnaFilter(domandaUnicaRicercaFilter.getAnnoCampagna()))
						 .toPredicate(root, query, cb);
		 };
	}

	private static Specification<DomandaUnicaModel> annoCampagnaFilter(Integer annoCampagna) {
		return (root, query, cb) -> {
			if (annoCampagna == null) {
				return null;
			}
			return cb.equal(
					root.get(DomandaUnicaModel_.campagna),
					annoCampagna);
		};
	}

	protected static Specification<DomandaUnicaModel> cuaaFilter(final String cuaa) {
		return (root, query, cb) -> {
			if (cuaa == null) {
				return null;
			}
			return cb.like(
					cb.upper(root.get(DomandaUnicaModel_.cuaaIntestatario)),
					StringSupport.upperlike(cuaa));
		};
	}

	protected static Specification<DomandaUnicaModel> ragioneSocialeFilter(final String ragioneSociale) {
		return (root, query, cb) -> {
			if (ragioneSociale == null) {
				return null;
			}
			return cb.like(
					cb.upper(root.get(DomandaUnicaModel_.ragioneSociale)),
					StringSupport.upperlike(ragioneSociale));
		};
	}

	protected static Specification<DomandaUnicaModel> numeroDomandaFilter(final Long numeroDomanda) {
		return (root, query, cb) -> {
			if (numeroDomanda == null) {
				return null;
			}
			return cb.equal(root.get(DomandaUnicaModel_.numeroDomanda), numeroDomanda);
		};
	}

	protected static Specification<DomandaUnicaModel> statoDomandaFilter(final StatoDomanda statoDomanda) {
		return (root, query, cb) -> {
			if (statoDomanda == null) {
				return null;
			}
			return cb.equal(root.get(DomandaUnicaModel_.stato), statoDomanda);
		};
	}

    public static Specification<DomandaUnicaModel> statoNotPiccoliAgricoltoriAndNotTipoIstruttoriaAndNotNonRicevibile(
    		final Integer campagna, final TipoIstruttoria tipologia) {
        return (root, query, cb) -> {
            if ((campagna == null) || (tipologia == null)) {
                return null;
            }

            Predicate predicateCampagna = cb.equal(root.get(DomandaUnicaModel_.campagna), campagna);
			Predicate isNotNonRicevibile = cb.notEqual(root.get(DomandaUnicaModel_.stato), StatoDomanda.NON_RICEVIBILE);
            Predicate notExistTipoIstruttoria = getPredicateNotExistTipoIstruttoria(tipologia, root, query, cb);
            Predicate isNotPiccoloAgricoltore = getPredicateIsNotPiccoloAgricoltore(campagna, root, query, cb);

            return cb.and(predicateCampagna, isNotNonRicevibile, notExistTipoIstruttoria, isNotPiccoloAgricoltore);
        };
    }

	private static Predicate getPredicateNotExistTipoIstruttoria(TipoIstruttoria tipologia, Root<DomandaUnicaModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Subquery<IstruttoriaModel> sqTipoIstruttoria = query.subquery(IstruttoriaModel.class);
        Root<IstruttoriaModel> rootTipoIstruttoria = sqTipoIstruttoria.from(IstruttoriaModel.class);
        Predicate predicateTipoIstruttoria = cb.and(
                cb.equal(rootTipoIstruttoria.get(IstruttoriaModel_.domandaUnicaModel), root.get(DomandaUnicaModel_.id)),
                cb.equal(rootTipoIstruttoria.get(IstruttoriaModel_.tipologia), tipologia)
        );
        Subquery<IstruttoriaModel> subQueryFilterTipoIstruttoria = sqTipoIstruttoria.select(rootTipoIstruttoria)
                .where(predicateTipoIstruttoria);
        return cb.not(cb.exists(subQueryFilterTipoIstruttoria));
    }

    private static Predicate getPredicateIsNotPiccoloAgricoltore(Integer campagna, Root<DomandaUnicaModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Subquery<A4gtPiccoloAgricoltore> sqPiccoloAgricoltore = query.subquery(A4gtPiccoloAgricoltore.class);
        Root<A4gtPiccoloAgricoltore> rootPiccoloAgricolotore = sqPiccoloAgricoltore.from(A4gtPiccoloAgricoltore.class);

        Predicate predicateInizioPiccoloAgricoltore = cb.and(
                cb.isNull(rootPiccoloAgricolotore.get(A4gtPiccoloAgricoltore_.annoFine)),
                cb.le(rootPiccoloAgricolotore.get(A4gtPiccoloAgricoltore_.annoInizio), campagna));

        Predicate predicateFinePiccoloAgricoltore = cb.and(
                cb.isNotNull(rootPiccoloAgricolotore.get(A4gtPiccoloAgricoltore_.annoFine)),
                cb.ge(rootPiccoloAgricolotore.get(A4gtPiccoloAgricoltore_.annoFine), campagna));

        Predicate predicatePiccoloAgricoltore = cb.or(predicateInizioPiccoloAgricoltore, predicateFinePiccoloAgricoltore);

        Predicate predicateCuaa = cb.equal(
                root.get(DomandaUnicaModel_.cuaaIntestatario),
                rootPiccoloAgricolotore.get(A4gtPiccoloAgricoltore_.cuaa));

        Subquery<A4gtPiccoloAgricoltore> subQueryFilter = sqPiccoloAgricoltore.select(rootPiccoloAgricolotore)
                .where(cb.and(predicatePiccoloAgricoltore, predicateCuaa));

        return cb.not(cb.exists(subQueryFilter));
    }
}
