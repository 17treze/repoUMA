package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.criteriabuilder;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoLavorazioneSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.StatoRichiestaModificaSuolo;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoLavorazioneModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoLavorazioneModel_;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TagDichiarato;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TagRilevato;
import it.tndigitale.a4g.richiestamodificasuolo.dto.filter.SuoloDichiaratoLavorazioneFilter;

public class SuoloDichiaratoLavorazioneSpecificationBuilder {

    private SuoloDichiaratoLavorazioneSpecificationBuilder() {
    }

    public static Specification<SuoloDichiaratoLavorazioneModel> getFilter(SuoloDichiaratoLavorazioneFilter filtri) {
        return (root, query, cb) -> {
            if (filtri == null) {
                return null;
            }

            return Specification
                .where(getRichiesta(filtri.getIdRichiesta()))
                .and(getCuaa(filtri.getCuaa()))
                .and(getComuneCatastale(filtri.getComuneCatastale()))
                .and(getCampagna(filtri.getCampagna()))
                .and(getVisibileOrtofoto(filtri.getVisibileOrtofoto()))
                .and(getTipoSuoloRilevato(filtri.getTipoSuoloRilevato(), filtri.getIsViticolo()))
                .and(getTipoSuoloDichiarato(filtri.getTipoSuoloDichiarato(), filtri.getIsViticolo()))
                .and(getStatoRichiesta(filtri.getStatoRichiesta(), filtri.getIdLavorazione()))
                .and(getStatoLavorazione(filtri.getStatoLavorazione()))
                .and(getIdLavorazione(filtri.getIdLavorazione()))
                .and(getIsViticolo(filtri.getIsViticolo()))
                .toPredicate(root, query, cb);
        };
    }

    private static Specification<SuoloDichiaratoLavorazioneModel> getCampagna(Long campagna) {
        return (root, query, cb) -> {
            if (campagna == null) {
                return null;
            }

            return cb.equal(root.get(SuoloDichiaratoLavorazioneModel_.campagna), campagna);
        };
    }

    private static Specification<SuoloDichiaratoLavorazioneModel> getVisibileOrtofoto(Boolean visibileOrtofoto) {
        return (root, query, cb) -> {
            if (visibileOrtofoto == null) {
                return null;
            }

            return cb.equal(root.get(SuoloDichiaratoLavorazioneModel_.visibileInOrtofoto), visibileOrtofoto);
        };
    }
    
    private static Specification<SuoloDichiaratoLavorazioneModel> getCuaa(String cuaa) {
        return (root, query, cb) -> {
            if (cuaa == null) {
                return null;
            }

            return cb.equal(root.get(SuoloDichiaratoLavorazioneModel_.cuaa), cuaa);
        };
    }

    private static Specification<SuoloDichiaratoLavorazioneModel> getRichiesta(Long idRichiesta) {
        return (root, query, cb) -> {
            if (idRichiesta == null) {
                return null;
            }

            return cb.equal(root.get(SuoloDichiaratoLavorazioneModel_.idRichiesta), idRichiesta);
        };
    }

    private static Specification<SuoloDichiaratoLavorazioneModel> getComuneCatastale(String comuneCatastale) {
        return (root, query, cb) -> {
            if (comuneCatastale == null) {
                return null;
            }

            return cb.equal(root.get(SuoloDichiaratoLavorazioneModel_.codSezione), comuneCatastale);
        };
    }

    private static Specification<SuoloDichiaratoLavorazioneModel> getTipoSuoloRilevato(String tipoSuoloRilevato, Boolean isViticolo) {
        return (root, query, cb) -> {
            if (tipoSuoloRilevato == null || (isViticolo != null && isViticolo)) {
                return null;
            }

            return cb.equal(root.get(SuoloDichiaratoLavorazioneModel_.tipoSuoloRilevato), TagRilevato.valueOf(tipoSuoloRilevato));
        };
    }

    private static Specification<SuoloDichiaratoLavorazioneModel> getTipoSuoloDichiarato(String tipoSuoloDichiarato, Boolean isViticolo) {
        return (root, query, cb) -> {
            if (tipoSuoloDichiarato == null || (isViticolo != null && isViticolo)) {
                return null;
            }

            return cb.equal(root.get(SuoloDichiaratoLavorazioneModel_.tipoSuoloDichiarato), TagDichiarato.valueOf(tipoSuoloDichiarato));
        };
    }

    /*
     * Questo metodo è usato per ottenere tutti i suoli dichiarati associabili ad una lavorazione Se stato=LAVORABILE AND IDLAVORAZIONE IS NULL applica anche gli altri filtri Altrimenti filtro solo su
     * stato
     */
    private static Specification<SuoloDichiaratoLavorazioneModel> getStatoRichiesta(String statoRichiesta, Long idLavorazione) {
        return (root, query, cb) -> {
            if (statoRichiesta == null) {
                return null;
            } else if (statoRichiesta.equals(StatoRichiestaModificaSuolo.LAVORABILE.toString()) && idLavorazione == null) {
                Predicate statoLavorabile = cb.equal(root.get(SuoloDichiaratoLavorazioneModel_.statoRichiesta), StatoRichiestaModificaSuolo.valueOf(statoRichiesta));
                Predicate statoInLavorazione = cb.equal(root.get(SuoloDichiaratoLavorazioneModel_.statoRichiesta), StatoRichiestaModificaSuolo.IN_LAVORAZIONE);
                Predicate idLavorazionePredicate = cb.isNull(root.get(SuoloDichiaratoLavorazioneModel_.idLavorazione));

                Predicate predicateStateRichiesta = cb.or(statoLavorabile, statoInLavorazione);

                return cb.and(predicateStateRichiesta, idLavorazionePredicate);
            } else {
                return cb.equal(root.get(SuoloDichiaratoLavorazioneModel_.statoRichiesta), StatoRichiestaModificaSuolo.valueOf(statoRichiesta));
            }
        };
    }

    private static Specification<SuoloDichiaratoLavorazioneModel> getIdLavorazione(Long idLavorazione) {
        return (root, query, cb) -> {
            if (idLavorazione == null) {
                return null;
            }
            return cb.equal(root.get(SuoloDichiaratoLavorazioneModel_.idLavorazione), idLavorazione);
        };
    }

    private static Specification<SuoloDichiaratoLavorazioneModel> getStatoLavorazione(String statoLavorazione) {
        return (root, query, cb) -> {
            if (statoLavorazione == null) {
                return null;
            }
            return cb.equal(root.get(SuoloDichiaratoLavorazioneModel_.statoLavorazione), StatoLavorazioneSuolo.valueOf(statoLavorazione));
        };
    }

    private static Specification<SuoloDichiaratoLavorazioneModel> getIsViticolo(Boolean isViticolo) {
        return (root, query, cb) -> {
            if (isViticolo == null || !isViticolo) {
                return null;
            }

            Predicate tagRileVITE = cb.equal(root.get(SuoloDichiaratoLavorazioneModel_.tipoSuoloRilevato), TagRilevato.VIT);
            Predicate tagDichVITE = cb.equal(root.get(SuoloDichiaratoLavorazioneModel_.tipoSuoloDichiarato), TagDichiarato.VIT);
            return cb.or(tagRileVITE, tagDichVITE);
        };
    }

}
