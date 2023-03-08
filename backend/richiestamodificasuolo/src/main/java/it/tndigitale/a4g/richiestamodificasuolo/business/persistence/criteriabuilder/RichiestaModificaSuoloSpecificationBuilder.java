package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.criteriabuilder;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.jpa.domain.Specification;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.RichiestaModificaSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.RichiestaModificaSuoloModel_;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel_;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.dto.DateHelper;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.RichiestaModificaSuoloFilter;

public class RichiestaModificaSuoloSpecificationBuilder {

	private RichiestaModificaSuoloSpecificationBuilder() {
	}

	public static Specification<RichiestaModificaSuoloModel> getFilter(RichiestaModificaSuoloFilter filtri) {
		return (root, query, cb) -> {
			if (filtri == null) {
				return null;
			}

			return Specification.where(getFilterCuaa(filtri.getCuaa())).and(getFilterIdRichiesta(filtri.getIdRichiesta())).and(getFilterComuneCatastale(filtri.getComuneCatastale()))
					.and(getFilterCampagna(filtri.getCampagna())).and(getFilterDataInserimento(filtri.getData())).and(getFilterStato(filtri.getStato())).and(getFilterTipo(filtri.getTipo()))
					.and(getFilterListMandatiCAA(filtri.getListCuaaMandatoCaa())).toPredicate(root, query, cb);
		};
	}

	private static Specification<RichiestaModificaSuoloModel> getFilterIdRichiesta(Long idRichiesta) {
		return (root, query, cb) -> {
			if (idRichiesta == null) {
				return null;
			}
			return cb.equal(root.get(RichiestaModificaSuoloModel_.id), idRichiesta);
		};
	}

	public static Specification<RichiestaModificaSuoloModel> getFilterCuaa(String cuaa) {
		return (root, query, cb) -> {
			if (cuaa == null) {
				return null;
			}
			return cb.equal(root.get(RichiestaModificaSuoloModel_.cuaa), cuaa);
		};

	}

	private static Specification<RichiestaModificaSuoloModel> getFilterTipo(TipoRichiestaModificaSuolo tipo) {
		return (root, query, cb) -> {
			if (tipo == null) {
				return null;
			}
			return cb.equal(root.get(RichiestaModificaSuoloModel_.tipo), tipo);
		};
	}

	private static Specification<RichiestaModificaSuoloModel> getFilterStato(StatoRichiestaModificaSuolo stato) {
		return (root, query, cb) -> {
			if (stato == null) {
				return null;
			}
			return cb.equal(root.get(RichiestaModificaSuoloModel_.stato), stato);
		};
	}

	private static Specification<RichiestaModificaSuoloModel> getFilterCampagna(Long campagna) {
		return (root, query, cb) -> {
			if (campagna == null) {
				return null;
			}
			return cb.equal(root.get(RichiestaModificaSuoloModel_.campagna), campagna);
		};
	}

	private static Specification<RichiestaModificaSuoloModel> getFilterComuneCatastale(String comuneCatastale) {
		return (root, query, cb) -> {
			if (comuneCatastale == null) {
				return null;
			}

			Join<RichiestaModificaSuoloModel, SuoloDichiaratoModel> suoloJoin = root.join(RichiestaModificaSuoloModel_.suoloDichiaratoModel, JoinType.INNER);
			query.distinct(true);
			return cb.equal(suoloJoin.get(SuoloDichiaratoModel_.codSezione), comuneCatastale);
		};
	}

	private static Specification<RichiestaModificaSuoloModel> getFilterDataInserimento(LocalDateTime data) {
		return (root, query, cb) -> {
			if (data == null) {
				return null;
			}

			Predicate startDate = cb.greaterThanOrEqualTo(root.get(RichiestaModificaSuoloModel_.dataRichiesta), DateHelper.getDateStartDate(data));
			Predicate endDate = cb.lessThanOrEqualTo(root.get(RichiestaModificaSuoloModel_.dataRichiesta), DateHelper.getDateEndDate(data));

			return cb.and(startDate, endDate);

			// return cb.equal(root.get(RichiestaModificaSuoloModel_.dataRichiesta), data.toLocalDate());
		};

	}

	private static Specification<RichiestaModificaSuoloModel> getFilterListMandatiCAA(List<String> listCuaaMandatoCaa) {
		return (root, query, cb) -> {
			if (listCuaaMandatoCaa == null) {// se la lista è null vuol dire che non sei un profilo CAA
				return null;
			}
			if (listCuaaMandatoCaa.isEmpty()) {
				return cb.disjunction(); // se la lista dei mandati è nulla vuol dire sei un profilo CAA ma che non hai mandati e ritorna sempre false
			} else {
				
				Predicate predicate = null;
				
				int listSize = listCuaaMandatoCaa.size();
				for(int i=0; i< listSize; i += 999) {
					List subList;
					if (listSize > i + 999) {
						subList= listCuaaMandatoCaa.subList(i, (i + 999));
					} else {
		                subList = listCuaaMandatoCaa.subList(i, listSize);
		            }
					if (predicate == null) {
			            predicate = root.get(RichiestaModificaSuoloModel_.cuaa).in(subList);
			        } else {
			            predicate = cb.or(predicate, root.get(RichiestaModificaSuoloModel_.cuaa).in(subList));
			        }
				 }
				
				return predicate;
			}
		};
	}

	public static String upperlike(String str) {
		return "%" + str.toUpperCase() + "%";
	}
	
}
