package it.tndigitale.a4gistruttoria.specification;

import it.tndigitale.a4gistruttoria.dto.IstruttoriaDomandaUnicaFilter;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;

public class DomandaUnicaRicercaSpecificationBuilder {

    private DomandaUnicaRicercaSpecificationBuilder() {}

    public static Specification<DomandaUnicaModel> searchDomandeUnicheByFilters(IstruttoriaDomandaUnicaFilter filter) {
        return (root, query, builder) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getNumeroDomanda() != null) {
                predicates.add(builder.equal(root.get("numeroDomanda"), filter.getNumeroDomanda()));
            }
            if (filter.getCuaa() != null) {
                predicates.add(builder.like(root.get("cuaaIntestatario"), "%" + filter.getCuaa().toUpperCase() + "%"));
            }
            if (filter.getRagioneSociale() != null) {
                predicates.add(builder.like(root.get("ragioneSociale"), "%" + filter.getRagioneSociale().toUpperCase() + "%"));
            }
            if (filter.getStatoDomanda() != null) {
                predicates.add(builder.equal(root.get("stato"), filter.getStatoDomanda()));
            }
            if (filter.getSostegno() != null) {
                predicates.add(getPredicateExistTipoSostegno(filter.getSostegno(), root, query, builder));
            }
            if (filter.getCampagna() != null) {
                predicates.add(builder.equal(root.get("campagna"), filter.getCampagna()));
            }
            if (filter.getTipo() != null) {
                predicates.add(getPredicateExistTipoIstruttoria(filter.getTipo(), root, query, builder));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate getPredicateExistTipoSostegno(Sostegno sostegno, Root<DomandaUnicaModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Subquery<IstruttoriaModel> sqTipoSostegno = query.subquery(IstruttoriaModel.class);
        Root<IstruttoriaModel> rootIstruttoriaSostegno = sqTipoSostegno.from(IstruttoriaModel.class);
        Predicate predicateSostegnoIstruttoria = cb.and(
                cb.equal(rootIstruttoriaSostegno.get("domandaUnicaModel"), root.get("id")),
                cb.equal(rootIstruttoriaSostegno.get("sostegno"), sostegno));
        Subquery<IstruttoriaModel> subQueryFilterTipoSostegno = sqTipoSostegno.select(rootIstruttoriaSostegno)
                                                                                    .where(predicateSostegnoIstruttoria);
        return cb.exists(subQueryFilterTipoSostegno);
    }

    private static Predicate getPredicateExistTipoIstruttoria(TipoIstruttoria tipoIstruttoria, Root<DomandaUnicaModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Subquery<IstruttoriaModel> sqTipoIstruttoria = query.subquery(IstruttoriaModel.class);
        Root<IstruttoriaModel> rootIstruttoriaTipo = sqTipoIstruttoria.from(IstruttoriaModel.class);
        Predicate predicateTipoIstruttoria = cb.and(
                cb.equal(rootIstruttoriaTipo.get("domandaUnicaModel"), root.get("id")),
                cb.equal(rootIstruttoriaTipo.get("tipologia"), tipoIstruttoria));
        Subquery<IstruttoriaModel> subQueryFilterTipoIstuttoria = sqTipoIstruttoria.select(rootIstruttoriaTipo)
                                                                              .where(predicateTipoIstruttoria);
        return cb.exists(subQueryFilterTipoIstuttoria);
    }

}
