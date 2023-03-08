package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio_;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel_;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel_;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.dto.consumi.DichiarazioneConsumiFilter;

@Component
public class DichiarazioneConsumiSpecification {

	private DichiarazioneConsumiSpecification() {}

	public static Specification<DichiarazioneConsumiModel> getFilter(DichiarazioneConsumiFilter filtri) {
		return getFilter(filtri, new ArrayList<>());
	}

	public static Specification<DichiarazioneConsumiModel> getFilter(DichiarazioneConsumiFilter filtri, List<String> cuaaList) {
		return (root, query, cb) -> {
			if (filtri == null) {
				return null;
			}
			return Specification
					.where(getFilterLikeCuaa(filtri.getCuaa()))
					.and(getAnnoCampagna(filtri.getCampagna()))
					.and(getStati(filtri.getStati()))
					.and(getDenominazione(filtri.getDenominazione()))
					.and(getId(filtri.getId()))
					.and(getCuaaList(cuaaList))
					.toPredicate(root, query, cb);
		};
	}

	private static Specification<DichiarazioneConsumiModel> getFilterLikeCuaa(final String cuaa) {
		return (root, query, cb) -> {
			if (cuaa == null) {
				return null;
			}
			return cb.like(
					cb.upper(root.get(DichiarazioneConsumiModel_.richiestaCarburante).get(RichiestaCarburanteModel_.cuaa)),
					upperlike(cuaa));
		};
	}
	private static Specification<DichiarazioneConsumiModel> getAnnoCampagna(List<Long> annoCampagna) {
		return (root, query, cb) -> {
			if (CollectionUtils.isEmpty(annoCampagna)) {
				return null;
			}
			return root.get(DichiarazioneConsumiModel_.richiestaCarburante).get(RichiestaCarburanteModel_.campagna).in(annoCampagna);
		};
	}
	private static Specification<DichiarazioneConsumiModel> getStati(Set<StatoDichiarazioneConsumi> stati) {
		return (root, query, cb) -> {
			if (CollectionUtils.isEmpty(stati)) {
				return null;
			}
			return root.get(DichiarazioneConsumiModel_.stato).in(stati);
		};
	}

	private static Specification<DichiarazioneConsumiModel> getDenominazione(final String denominazione) {
		return (root, query, cb) -> {
			if (denominazione == null) {
				return null;
			}
			return cb.like(
					cb.upper(root.get(DichiarazioneConsumiModel_.richiestaCarburante).get(RichiestaCarburanteModel_.denominazione)),
					upperlike(denominazione));
		};
	}

	private static Specification<DichiarazioneConsumiModel> getId(Long id) {
		return (root, query, cb) -> {
			if (id == null) {
				return null;
			}
			return cb.equal(
					root.get(EntitaDominio_.id),
					id);
		};
	}

	private static Specification<DichiarazioneConsumiModel> getCuaaList(List<String> cuaaList) {
		return (root, query, cb) -> {
			if (CollectionUtils.isEmpty(cuaaList)) {
				return null;
			}
			return root.get(DichiarazioneConsumiModel_.richiestaCarburante).get(RichiestaCarburanteModel_.cuaa).in(cuaaList);
		};
	}

	private static String upperlike(String str) {
		return "%" + str.toUpperCase() + "%";
	}
}
