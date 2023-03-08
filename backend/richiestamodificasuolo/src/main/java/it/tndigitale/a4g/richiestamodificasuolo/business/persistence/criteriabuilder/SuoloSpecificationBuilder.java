package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.criteriabuilder;

import java.time.LocalDateTime;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.GrigliaSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.GrigliaSuoloModel_;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel_;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloModel_;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.SuoloFilter;

public class SuoloSpecificationBuilder {

	private SuoloSpecificationBuilder() {
	}

	public static Specification<SuoloModel> getFilter(SuoloFilter filtri) {
		return (root, query, cb) -> {
			if (filtri == null) {
				return null;
			}
			return Specification.where(
					getId(filtri.getId()).and(getSorgente(filtri.getSorgente())).and(getDataInizioValidita(filtri.getDataInizioValidita())).and(getDataFineValidita(filtri.getDataFineValidita()))
							.and(getNote(filtri.getNote())).and(getCampagna(filtri.getCampagna())).and(getIdLavorazioneInizio(filtri.getIdLavorazioneInizio()))
							.and(getIdLavorazioneInCorso(filtri.getIdLavorazioneInCorso())).and(getIdLavorazioneFine(filtri.getIdLavorazioneFine())).and(getIdGrid(filtri.getIdGrid())))
					.toPredicate(root, query, cb);
		};
	}

	private static Specification<SuoloModel> getIdGrid(Long idGrid) {
		return (root, query, cb) -> {
			if (idGrid == null) {
				return null;
			}
			Join<SuoloModel, GrigliaSuoloModel> suoloJoin = root.join(SuoloModel_.idGrid, JoinType.INNER);
			query.distinct(true);
			return cb.equal(suoloJoin.get(GrigliaSuoloModel_.id), idGrid);
		};
	}

	private static Specification<SuoloModel> getIdLavorazioneFine(Long idLavorazioneFine) {
		return (root, query, cb) -> {
			if (idLavorazioneFine == null) {
				return null;
			}
			Join<SuoloModel, LavorazioneSuoloModel> suoloJoin = root.join(SuoloModel_.idLavorazioneFine, JoinType.INNER);
			query.distinct(true);
			return cb.equal(suoloJoin.get(LavorazioneSuoloModel_.id), idLavorazioneFine);
		};
	}

	private static Specification<SuoloModel> getIdLavorazioneInCorso(Long idLavorazioneInCorso) {
		return (root, query, cb) -> {
			if (idLavorazioneInCorso == null) {
				return null;
			}
			Join<SuoloModel, LavorazioneSuoloModel> suoloJoin = root.join(SuoloModel_.idLavorazioneInCorso, JoinType.INNER);
			query.distinct(true);
			return cb.equal(suoloJoin.get(LavorazioneSuoloModel_.id), idLavorazioneInCorso);
		};
	}

	private static Specification<SuoloModel> getIdLavorazioneInizio(Long idLavorazioneInizio) {
		return (root, query, cb) -> {
			if (idLavorazioneInizio == null) {
				return null;
			}
			Join<SuoloModel, LavorazioneSuoloModel> suoloJoin = root.join(SuoloModel_.idLavorazioneInizio, JoinType.INNER);
			query.distinct(true);
			return cb.equal(suoloJoin.get(LavorazioneSuoloModel_.id), idLavorazioneInizio);
		};
	}

	private static Specification<SuoloModel> getCampagna(Integer campagna) {
		return (root, query, cb) -> {
			if (campagna == null) {
				return null;
			}
			return cb.equal(root.get(SuoloModel_.campagna), campagna);
		};

	}

	private static Specification<SuoloModel> getDataFineValidita(LocalDateTime dataFineValidita) {
		return (root, query, cb) -> {
			if (dataFineValidita == null) {
				return null;
			}
			return cb.equal(root.get(SuoloModel_.dataFineValidita), dataFineValidita);
		};

	}

	private static Specification<SuoloModel> getNote(String note) {
		return (root, query, cb) -> {
			if (note == null) {
				return null;
			}
			return cb.equal(root.get(SuoloModel_.note), note);
		};

	}

	private static Specification<SuoloModel> getDataInizioValidita(LocalDateTime dataInizioValidita) {
		return (root, query, cb) -> {
			if (dataInizioValidita == null) {
				return null;
			}
			return cb.equal(root.get(SuoloModel_.dataInizioValidita), dataInizioValidita);
		};

	}

	private static Specification<SuoloModel> getSorgente(String sorgente) {
		return (root, query, cb) -> {
			if (sorgente == null) {
				return null;
			}
			return cb.equal(root.get(SuoloModel_.sorgente), sorgente);
		};

	}

	private static Specification<SuoloModel> getId(Long id) {
		return (root, query, cb) -> {
			if (id == null) {
				return null;
			}

			return cb.equal(root.get(SuoloModel_.id), id);
		};
	}

}
