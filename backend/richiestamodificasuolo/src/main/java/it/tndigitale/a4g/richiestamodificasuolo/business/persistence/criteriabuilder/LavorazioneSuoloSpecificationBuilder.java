package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.criteriabuilder;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel_;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.LavorazioneSuoloFilter;

public class LavorazioneSuoloSpecificationBuilder {

	private LavorazioneSuoloSpecificationBuilder() {
	}

	public static Specification<LavorazioneSuoloModel> getFilter(LavorazioneSuoloFilter filtri) {
		return (root, query, cb) -> {
			if (filtri == null) {
				return null;
			}

			return Specification.where(getFilterIdLavorazione(filtri.getIdLavorazione())).and(getFilterUtente(filtri.getUtente())).and(getFilterStati(filtri.getListStatiLavorazione()))
					.and(getFilterDataInizioLavorazione(filtri.getDataInizioLavorazione())).and(getFilterDataFineLavorazione(filtri.getDataFineLavorazione())).and(getFilterNote(filtri.getNote()))
					.and(getFilterTitolo(filtri.getTitolo())).and(getFilterSopralluogo(filtri.getSopralluogo())).and(getFilterDataUltimaModifica(filtri.getDataUltimaModifica()))
					.toPredicate(root, query, cb);
		};
	}

	private static Specification<LavorazioneSuoloModel> getFilterNote(String note) {
		return (root, query, cb) -> {
			if (note == null) {
				return null;
			}
			return cb.equal(root.get(LavorazioneSuoloModel_.note), note);
		};
	}

	private static Specification<LavorazioneSuoloModel> getFilterDataFineLavorazione(LocalDateTime dataFineLavorazione) {
		return (root, query, cb) -> {
			if (dataFineLavorazione == null) {
				return null;
			}
			return cb.equal(root.get(LavorazioneSuoloModel_.dataFineLavorazione), dataFineLavorazione);
		};
	}

	private static Specification<LavorazioneSuoloModel> getFilterTitolo(String titolo) {
		return (root, query, cb) -> {
			if (titolo == null) {
				return null;
			}
			return cb.equal(root.get(LavorazioneSuoloModel_.titolo), titolo);
		};
	}

	private static Specification<LavorazioneSuoloModel> getFilterSopralluogo(String sopralluogo) {
		return (root, query, cb) -> {
			if (sopralluogo == null) {
				return null;
			}
			return cb.equal(root.get(LavorazioneSuoloModel_.sopralluogo), sopralluogo);
		};
	}

	private static Specification<LavorazioneSuoloModel> getFilterIdLavorazione(Long idLavorazione) {
		return (root, query, cb) -> {
			if (idLavorazione == null) {
				return null;
			}
			return cb.equal(root.get(LavorazioneSuoloModel_.id), idLavorazione);
		};
	}

	private static Specification<LavorazioneSuoloModel> getFilterUtente(String utente) {
		return (root, query, cb) -> {
			if (utente == null) {
				return null;
			}
			return cb.equal(root.get(LavorazioneSuoloModel_.utente), utente);
		};
	}

	private static Specification<LavorazioneSuoloModel> getFilterStati(List<StatoLavorazioneSuolo> listStatiLavorazioneSuolo) {
		return (root, query, cb) -> {
			if (listStatiLavorazioneSuolo == null || listStatiLavorazioneSuolo.isEmpty()) {
				return null;
			}
			return root.get(LavorazioneSuoloModel_.stato).in(listStatiLavorazioneSuolo);
		};
	}

	private static Specification<LavorazioneSuoloModel> getFilterDataInizioLavorazione(LocalDateTime dataInizioLavorazione) {
		return (root, query, cb) -> {
			if (dataInizioLavorazione == null) {
				return null;
			}
			return cb.equal(root.get(LavorazioneSuoloModel_.dataInizioLavorazione), dataInizioLavorazione);
		};
	}

	private static Specification<LavorazioneSuoloModel> getFilterDataUltimaModifica(LocalDateTime dataUltimaModifica) {
		return (root, query, cb) -> {
			if (dataUltimaModifica == null) {
				return null;
			}
			return cb.equal(root.get(LavorazioneSuoloModel_.dataUltimaModifica), dataUltimaModifica);
		};
	}

}
