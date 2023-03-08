package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository.specification;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DetenzioneModel_;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel_;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel_;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.OrganismoPagatoreEnum;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.SportelloModel_;
import it.tndigitale.a4g.fascicolo.anagrafica.dto.FascicoloFilter;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicolo_;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;
import it.tndigitale.a4g.framework.time.Clock;

public class FascicoloSpecificationBuilder {

	private FascicoloSpecificationBuilder() {}

	@Autowired
	private static Clock clock;

	public static Specification<FascicoloModel> getFilter(FascicoloFilter filtri) {
		return (root, query, cb) -> {
			if (filtri == null) {
				return null;
			}
			return Specification
					.where(getFilterCuaa(filtri.getCuaa()))
					.and(getFilterEntiAbilitati(filtri.getEntiUtenteConnesso()))
					.and(getFilterRagioneSociale(filtri.getRagioneSociale()))
					.and(getRevocaOrdinaria(filtri.getRevocaOrdinariaMandato()))
					.and(getIdValidazioneCorrente())
					.toPredicate(root, query, cb);
		};
	}
	
	private static Specification<FascicoloModel> getIdValidazioneCorrente() {
		return (root, query, cb) -> cb.equal(
				root.get(EntitaDominioFascicolo_.idValidazione), Integer.valueOf(0));
	}

	private static Specification<FascicoloModel> getFilterRagioneSociale(String ragioneSociale) {
		return (root, query, cb) -> {
			if (ragioneSociale == null) {
				return null;
			}
			return cb.like(
					cb.upper(root.get(FascicoloModel_.denominazione)),
					upperlike(ragioneSociale));
		};
	}

	private static Specification<FascicoloModel> getFilterEntiAbilitati(List<String> entiUtenteConnesso) {
		return (root, query, cb) -> {
			if (!CollectionUtils.isEmpty(entiUtenteConnesso)) {
				//prendendo solo le detenzioni con sportelli abilitati e correnti
				ListJoin<FascicoloModel, DetenzioneModel> detenzioni = root.join(FascicoloModel_.detenzioni);
				SingularAttribute sportello = MandatoModel_.sportello;
				SingularAttribute identificativo = SportelloModel_.identificativo;
				Expression<LocalDate> currentDate = cb.literal(LocalDate.now());
				Predicate dataInizioGreaterThanOrEqualToCurrentDate = cb.greaterThanOrEqualTo(currentDate, detenzioni.get("dataInizio"));
				Predicate dataFineLessThanOrEqualToCurrentDate = cb.or(cb.isNull(detenzioni.get("dataFine")), cb.lessThanOrEqualTo(currentDate, detenzioni.get("dataFine")));
				Predicate detenzioneCorrentePredicate = cb.and(dataInizioGreaterThanOrEqualToCurrentDate,dataFineLessThanOrEqualToCurrentDate);
				Predicate organismoPagatoreAppagOrStatoInAttesaTrasferimentoPredicate = cb.or(cb.equal(root.get(FascicoloModel_.organismoPagatore), OrganismoPagatoreEnum.APPAG), cb.equal(root.get(FascicoloModel_.stato), StatoFascicoloEnum.IN_ATTESA_TRASFERIMENTO));
				return cb.and(organismoPagatoreAppagOrStatoInAttesaTrasferimentoPredicate, cb.and(detenzioneCorrentePredicate, detenzioni.get(sportello).get(identificativo).in(entiUtenteConnesso))) ;
			} else {
				return null;
			}
		};
	}

	public static Specification<FascicoloModel> getFilterCuaa(final String cuaa) {
		return (root, query, cb) -> {
			if (cuaa == null) {
				return null;
			}
			return cb.like(
					cb.upper(root.get(FascicoloModel_.cuaa)),
					upperlike(cuaa));
		};
	}

	private static Specification<FascicoloModel> getRevocaOrdinaria(Boolean getRevocaOrdinaria) {
		return (root, query, cb) -> {
			if (getRevocaOrdinaria == null) {
				return null;
			}
			if (getRevocaOrdinaria.booleanValue()) {
				LocalDate primoGennaio = LocalDate.of(clock.today().plusYears(1).getYear(), Month.JANUARY, 1);
				Expression<Timestamp> primoGennaioExpression = cb.literal(Timestamp.valueOf(Clock.ofStartOfDay(primoGennaio)));
				ListJoin<FascicoloModel, DetenzioneModel> join = root.join(FascicoloModel_.detenzioni);
				return cb.equal(join.get(DetenzioneModel_.DATA_INIZIO), primoGennaioExpression);
			} else {
				return null;
			}
		};
	}

	public static String upperlike(String str) {
		return "%" + str.toUpperCase() + "%";
	}

}
