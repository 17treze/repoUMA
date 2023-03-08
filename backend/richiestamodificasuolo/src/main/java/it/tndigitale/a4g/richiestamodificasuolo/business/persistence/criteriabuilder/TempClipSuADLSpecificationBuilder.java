package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.criteriabuilder;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TempClipSuADLModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TempClipSuADLModel_;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.TempClipSuADLFilter;

public class TempClipSuADLSpecificationBuilder {

	private TempClipSuADLSpecificationBuilder() {
	}

	public static Specification<TempClipSuADLModel> getFilter(TempClipSuADLFilter filtri) {
		return (root, query, cb) -> {
			if (filtri == null) {
				return null;
			}
			return Specification.where(getId(filtri.getId()).and(getIdLavorazione(filtri.getIdLavorazione())).and(getPosizionePoligono(filtri.getPosizionePoligono()))).toPredicate(root, query, cb);
		};
	}

	private static Specification<TempClipSuADLModel> getIdLavorazione(Long idLavorazione) {
		return (root, query, cb) -> {
			if (idLavorazione == null) {
				return null;
			}
			Join<TempClipSuADLModel, LavorazioneSuoloModel> suoloJoin = root.join(TempClipSuADLModel_.lavorazioneSuolo, JoinType.INNER);
			query.distinct(true);
			return cb.equal(suoloJoin.get(TempClipSuADLModel_.id), idLavorazione);
		};
	}

	private static Specification<TempClipSuADLModel> getPosizionePoligono(String posizionePoligono) {
		return (root, query, cb) -> {
			if (posizionePoligono == null) {
				return null;
			}
			return cb.equal(root.get(TempClipSuADLModel_.posizionePoligono), posizionePoligono);
		};
	}

	private static Specification<TempClipSuADLModel> getId(Long id) {
		return (root, query, cb) -> {
			if (id == null) {
				return null;
			}

			return cb.equal(root.get(TempClipSuADLModel_.id), id);
		};
	}
}
