package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.specification;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel_;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;

public class ValidazioneFascicoloSpecificationBuilder {

	private ValidazioneFascicoloSpecificationBuilder() {}
	
	public static Specification<FascicoloModel> getFilterId(final Long id) {
		return (root, query, cb) -> {
			if (id == null) {
				return null;
			}
			return cb.equal(root.get(FascicoloModel_.id), id);
		};
	}
	
	public static Specification<FascicoloModel> getFilterStato(final StatoFascicoloEnum stato) {
		return (root, query, cb) -> {
			if (stato == null) {
				return null;
			}
			return cb.equal(root.get(FascicoloModel_.stato), stato);
		};
	}
	
	public static Specification<FascicoloModel> getFilterVersioneCorrente() {
		return (root, query, cb) -> {
			return cb.notEqual(root.get(FascicoloModel_.idValidazione), 0);
		};
	}
	
	public static Specification<FascicoloModel> getFilterAnnoValidazioneLess(
			final Integer annoValidazione) {
		return (root, query, cb) -> {
			if (annoValidazione == null) {
				return null;
			}
			LocalDate dataFine = LocalDate.of(annoValidazione, Month.DECEMBER, 31);
			return cb.lessThanOrEqualTo(root.get(FascicoloModel_.dataValidazione), dataFine);
		};
	}
	
	public static Specification<FascicoloModel> getFilterAnnoValidazioneGreater(
			final Integer annoValidazione) {
		return (root, query, cb) -> {
			if (annoValidazione == null) {
				return null;
			}
			LocalDate dataInizio = LocalDate.of(annoValidazione, Month.JANUARY, 1);
			return cb.greaterThanOrEqualTo(root.get(FascicoloModel_.dataValidazione), dataInizio);
		};
	}
	
	public static Specification<FascicoloModel> getFilter(ValidazioneFascicoloFilter filtri) {
		return (root, query, cb) -> {
			if (filtri == null) {
				return null;
			}
			return Specification
					.where(getFilterId(filtri.getId()))
					.and(getFilterVersioneCorrente())
					.and(getFilterStato(filtri.getStato()))
					.and(getFilterAnnoValidazioneLess(filtri.getAnnoValidazione()))
					.and(getFilterAnnoValidazioneGreater(filtri.getAnnoValidazione())).toPredicate(root, query, cb);
		};
	}
}
