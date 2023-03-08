package it.tndigitale.a4gutente.repository.specification;

import static it.tndigitale.a4g.framework.time.Clock.ofEndOfDay;
import static it.tndigitale.a4g.framework.time.Clock.ofStartOfDay;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import it.tndigitale.a4g.framework.support.SQLSupport;
import it.tndigitale.a4g.framework.support.StringSupport;
import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.DomandaRegistrazioneUtenteFilter;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;


/**
 * Si occupa di costruire le Specification per effettuare 
 * le ricerche di domande registrazione utente
 * 
 * @author IT417
 *
 */
public class DomandaRegistrazioneUtenteSpecificationBuilder {
	
	public static Specification<DomandaRegistrazioneUtente> getFilter(DomandaRegistrazioneUtenteFilter filtri) {
		return (root, query, cb) -> {
			if (filtri == null) {
				return null;
			}
			return Specification.where(getFilterStato(filtri.getStato()))
								.and(getFilterCodiceFiscale(filtri.getCodiceFiscale()))
					            .and(getFiltroGenerico(filtri.getFiltroGenerico()))
								.and(getFiltroUpperLike(filtri.getCodiceFiscaleUpperLike(), "codiceFiscale"))
								.and(getFiltroUpperLike(filtri.getNome(), "nome"))
								.and(getFiltroUpperLike(filtri.getCognome(), "cognome"))
								.and(getFiltroUpperLike(filtri.getIdProtocollo(), "idProtocollo"))
								.and(getFilterDataProtocollazioneInRange(filtri.getDataInizio(), filtri.getDataFine()))
								.toPredicate(root, query, cb);
		};
	}

	public static Specification<DomandaRegistrazioneUtente> getFilterForCount(DomandaRegistrazioneUtenteFilter filtri) {
		return (root, query, cb) -> {
			if (filtri == null) {
				return null;
			}
			return Specification.where(getFilterStato(filtri.getStato()))
								.and(getFiltroUpperLike(filtri.getCodiceFiscaleUpperLike(), "codiceFiscale"))
								.and(getFiltroUpperLike(filtri.getNome(), "nome"))
								.and(getFiltroUpperLike(filtri.getCognome(), "cognome"))
								.and(getFiltroUpperLike(filtri.getIdProtocollo(), "idProtocollo"))
								.and(getFilterDataProtocollazioneInRange(filtri.getDataInizio(), filtri.getDataFine()))
								.toPredicate(root, query, cb);
		};
	}


	public static Specification<DomandaRegistrazioneUtente> getFilterCodiceFiscale(String codiceFiscale) {
		return (root, query, cb) -> {
			if (StringSupport.isEmptyOrNull(codiceFiscale)) {
				return null;
			}
			return cb.equal(root.get("codiceFiscale"), codiceFiscale);
		};
	}

	public static Specification<DomandaRegistrazioneUtente> getFilterStato(StatoDomandaRegistrazioneUtente stato) {
		return (root, query, cb) -> {
			if (stato == null) {
				return null;
			}
			return cb.equal(root.get("stato"), stato);
		};
	}

	public static Specification<DomandaRegistrazioneUtente> getFiltroGenerico(String filtroGenerico) {
		return (root, query, cb) -> {
			if (StringSupport.isEmptyOrNull(filtroGenerico)) {
				return null;
			}
			return Specification.where(getFiltroUpperLike(filtroGenerico, "nome"))
								.or(getFiltroUpperLike(filtroGenerico, "cognome"))
								.or(getFiltroUpperLike(filtroGenerico, "codiceFiscale"))
								.or(getFiltroUpperLike(filtroGenerico, "idProtocollo"))
								.toPredicate(root, query, cb);
		};
	}

	private static Specification<DomandaRegistrazioneUtente> getFiltroUpperLike(String filtroGenerico, String property) {
		return (root, query, cb) -> {
			if (StringSupport.isEmptyOrNull(filtroGenerico) || StringSupport.isEmptyOrNull(property)) {
				return null;
			}
			return cb.like(cb.upper(root.get(property)), SQLSupport.upperLike(filtroGenerico));
		};
	}

	private static Specification<DomandaRegistrazioneUtente> getFilterDataProtocollazioneGreaterOrEqualOf(LocalDate datatInizio) {
		return (root, query, cb) -> {
			if (datatInizio == null) {
				return null;
			}
			return cb.greaterThanOrEqualTo(root.get("dtProtocollazione"), ofStartOfDay(datatInizio));
		};
	}

	private static Specification<DomandaRegistrazioneUtente> getFilterDataProtocollazioneLessOrEqualOf(LocalDate dataFine) {
		return (root, query, cb) -> {
			if (dataFine == null) {
				return null;
			}
			return cb.lessThanOrEqualTo(root.get("dtProtocollazione"), ofEndOfDay(dataFine));
		};
	}

	private static Specification<DomandaRegistrazioneUtente> getFilterDataProtocollazioneInRange(LocalDate datatInizio, LocalDate dataFine) {
		return (root, query, cb) -> {
			if (datatInizio == null || dataFine == null) {
				return null;
			}
			return Specification.where(getFilterDataProtocollazioneLessOrEqualOf(dataFine))
								.and(getFilterDataProtocollazioneGreaterOrEqualOf(datatInizio))
								.toPredicate(root, query, cb);
		};
	}
}
