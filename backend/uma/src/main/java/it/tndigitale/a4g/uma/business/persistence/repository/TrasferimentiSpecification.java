package it.tndigitale.a4g.uma.business.persistence.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import it.tndigitale.a4g.framework.time.Clock;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel_;
import it.tndigitale.a4g.uma.business.persistence.entity.TrasferimentoCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.TrasferimentoCarburanteModel_;
import it.tndigitale.a4g.uma.dto.trasferimenti.TrasferimentiFilter;

public class TrasferimentiSpecification {

	private TrasferimentiSpecification() {}

	public static Specification<TrasferimentoCarburanteModel> getFilter(TrasferimentiFilter filtri) {
		return (root, query, cb) -> {
			if (filtri == null) {
				return null;
			}
			return Specification
					.where(getFilterMittente(filtri.getCuaaMittente()))
					.and(getAnnoCampagna(filtri.getCampagna()))
					.and(getFilterDestinatario(filtri.getCuaaDestinatario()))
					.and(getFilterData(filtri.getData()))
					.toPredicate(root, query, cb);
		};
	}

	private static Specification<TrasferimentoCarburanteModel> getFilterMittente(final String cuaa) {
		return (root, query, cb) -> {
			if (cuaa == null) {
				return null;
			}
			return cb.like(
					cb.upper(root.get(TrasferimentoCarburanteModel_.richiestaCarburante).get(RichiestaCarburanteModel_.cuaa)),
					upperlike(cuaa));
		};
	}

	private static Specification<TrasferimentoCarburanteModel> getAnnoCampagna(Long annoCampagna) {
		return (root, query, cb) -> {
			if (annoCampagna == null) {
				return null;
			}
			return cb.equal(
					root.get(TrasferimentoCarburanteModel_.richiestaCarburante).get(RichiestaCarburanteModel_.campagna),
					annoCampagna);
		};
	}


	private static Specification<TrasferimentoCarburanteModel> getFilterDestinatario(final String cuaa) {
		return (root, query, cb) -> {
			if (cuaa == null) {
				return null;
			}
			return cb.like(
					cb.upper(root.get(TrasferimentoCarburanteModel_.cuaaDestinatario)),
					upperlike(cuaa));
		};
	}

	private static Specification<TrasferimentoCarburanteModel> getFilterData(final LocalDate data) {
		return (root, query, cb) -> {
			if (data == null) {
				return null;
			}
			var start = Clock.ofStartOfDay(data);
			var end = Clock.ofEndOfDay(data);
			return cb.between(root.get(TrasferimentoCarburanteModel_.data), start, end);
		};
	}


	private static String upperlike(String str) {
		return "%" + str.toUpperCase() + "%";
	}
}
