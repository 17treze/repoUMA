package it.tndigitale.a4gistruttoria.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import it.tndigitale.a4gistruttoria.dto.IstruttoriaDomandaUnicaFilter;
import it.tndigitale.a4gistruttoria.repository.model.A4gdStatoLavSostegno_;
import it.tndigitale.a4gistruttoria.repository.model.A4gtAnomDomandaSostegno;
import it.tndigitale.a4gistruttoria.repository.model.A4gtAnomDomandaSostegno_;
import it.tndigitale.a4gistruttoria.repository.model.A4gtPascoloParticella;
import it.tndigitale.a4gistruttoria.repository.model.A4gtPascoloParticella_;
import it.tndigitale.a4gistruttoria.repository.model.A4gtRichiestaSuperficie;
import it.tndigitale.a4gistruttoria.repository.model.A4gtRichiestaSuperficie_;
import it.tndigitale.a4gistruttoria.repository.model.AllevamentoImpegnatoModel;
import it.tndigitale.a4gistruttoria.repository.model.AllevamentoImpegnatoModel_;
import it.tndigitale.a4gistruttoria.repository.model.AmbitoCampione;
import it.tndigitale.a4gistruttoria.repository.model.CampioneModel;
import it.tndigitale.a4gistruttoria.repository.model.CampioneModel_;
import it.tndigitale.a4gistruttoria.repository.model.DichiarazioneDomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.DichiarazioneDomandaUnicaModel_;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel_;
import it.tndigitale.a4gistruttoria.repository.model.InterventoModel_;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel_;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel;
import it.tndigitale.a4gistruttoria.repository.model.PassoTransizioneModel_;
import it.tndigitale.a4gistruttoria.repository.model.Quadro;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel;
import it.tndigitale.a4gistruttoria.repository.model.TransizioneIstruttoriaModel_;
import it.tndigitale.a4gistruttoria.service.businesslogic.disaccoppiato.DisaccoppiatoService;
import it.tndigitale.a4gistruttoria.util.StringSupport;

public class IstruttoriaSpecificationBuilder {

	private static Predicate subQueryPascoloParticella(
			final Root<IstruttoriaModel> root, final CriteriaQuery<?> query, final CriteriaBuilder cb,
			final boolean toMatch) {
		Subquery<A4gtPascoloParticella> subQuery = query.subquery(A4gtPascoloParticella.class);
		Root<A4gtPascoloParticella> subRoot = subQuery.from(A4gtPascoloParticella.class);
		Predicate filtroDomandalPredicate = cb.equal(root.get(IstruttoriaModel_.domandaUnicaModel), subRoot.get(A4gtPascoloParticella_.domandaUnicaModel));
		if (toMatch) {
			return cb.exists(subQuery.select(subRoot).where(filtroDomandalPredicate));
		} else {
			return cb.not(cb.exists(subQuery.select(subRoot).where(filtroDomandalPredicate)));
		}
	}

	private static Predicate subQueryGiovaneDomanda(
			final Root<IstruttoriaModel> root, final CriteriaQuery<?> query, final CriteriaBuilder cb,
			final boolean toMatch) {
		Subquery<DichiarazioneDomandaUnicaModel> subQuery = query.subquery(DichiarazioneDomandaUnicaModel.class);
		Root<DichiarazioneDomandaUnicaModel> subRoot = subQuery.from(DichiarazioneDomandaUnicaModel.class);
		Predicate filtroDomandalPredicate = cb.equal(root.get(IstruttoriaModel_.domandaUnicaModel), subRoot.get(DichiarazioneDomandaUnicaModel_.domandaUnicaModel));
		Predicate filtroPred = cb.equal(subRoot.get(DichiarazioneDomandaUnicaModel_.codice), DisaccoppiatoService.CODICE_GIOVANE);
		if (toMatch) {
			return cb.exists(subQuery.select(subRoot).where(filtroDomandalPredicate, filtroPred));
		} else {
			return cb.not(cb.exists(subQuery.select(subRoot).where(filtroDomandalPredicate, filtroPred)));
		}
	}

	private static Predicate subQueryRiservaNazionaleDomanda(
			final Root<IstruttoriaModel> root, final CriteriaQuery<?> query, final CriteriaBuilder cb,
			final String valueToMatch) {
		Subquery<DichiarazioneDomandaUnicaModel> subQuery = query.subquery(DichiarazioneDomandaUnicaModel.class);
		Root<DichiarazioneDomandaUnicaModel> subRoot = subQuery.from(DichiarazioneDomandaUnicaModel.class);
		Predicate filtroDomandalPredicate = cb.equal(root.get(IstruttoriaModel_.domandaUnicaModel), subRoot.get(DichiarazioneDomandaUnicaModel_.domandaUnicaModel));
		Predicate filtroPred = cb.equal(subRoot.get(DichiarazioneDomandaUnicaModel_.quadro), Quadro.RISERVA_NAZIONALE);
		if (valueToMatch != null) {
			Predicate valorePred = cb.equal(subRoot.get(DichiarazioneDomandaUnicaModel_.descrizione), valueToMatch);
			return cb.exists(subQuery.select(subRoot).where(filtroDomandalPredicate, filtroPred, valorePred));
		} else {
			return cb.not(cb.exists(subQuery.select(subRoot).where(filtroDomandalPredicate, filtroPred)));
		}
	}

	private static Predicate subQueryCampione(
			final Root<IstruttoriaModel> root, final CriteriaQuery<?> query,
			final CriteriaBuilder cb, final boolean toMatch) {
		Subquery<CampioneModel> subqueryCampione = query.subquery(CampioneModel.class);
		Root<CampioneModel> rootCampione = subqueryCampione.from(CampioneModel.class);		
		Predicate filtroDomandalPredicate = cb.equal(
				root.get(IstruttoriaModel_.domandaUnicaModel).get(DomandaUnicaModel_.cuaaIntestatario),
				rootCampione.get(CampioneModel_.cuaa));
		Predicate annoCampagnaPred = cb.equal(
				rootCampione.get(CampioneModel_.annoCampagna),
				root.get(IstruttoriaModel_.domandaUnicaModel).get(DomandaUnicaModel_.campagna));
		Predicate tipoCampioneSuperficiPred = cb.equal(
				rootCampione.get(CampioneModel_.ambitoCampione), AmbitoCampione.SUPERFICIE);
		Subquery<CampioneModel> subQueryFilter = subqueryCampione.select(rootCampione)
				.where(cb.and(filtroDomandalPredicate, annoCampagnaPred, tipoCampioneSuperficiPred));
		if (toMatch) {
			return cb.exists(subQueryFilter);
		} else {
			return cb.not(cb.exists(subQueryFilter));
		}
	}

	public static Specification<IstruttoriaModel> getFilter(IstruttoriaDomandaUnicaFilter filtri) {
		return (root, query, cb) -> {
			if (filtri == null) {
				return null;
			}
			Predicate filterPredicate =  Specification.where(getFilterStato(filtri.getStato()))
					.and(getFilterSostegno(filtri.getSostegno()))
		            .and(getFilterAnno(filtri.getCampagna()))
					.and(getFilterTipologia(filtri.getTipo()))
					.and(getFilterCuaa(filtri.getCuaa()))
					.and(getFilterRagioneSociale(filtri.getRagioneSociale()))
					.and(getFilterNumeroDomanda(filtri.getNumeroDomanda()))
					.and(getFilterIstruttoriaBloccata(filtri.getIstruttoriaBloccata()))
					.and(getFilterErroreCalcolo(filtri.getErroreCalcolo()))
					.and(getFilterGiovane(filtri.getGiovane()))
					.and(getFilterCampione(filtri.getCampione()))
					.and(getFilterPascoli(filtri.getPascoli()))
					.and(getFilterRiservaNazionale(filtri.getRiservaNazionale()))
					.and(getFilterInterventiGenerico(filtri.getSostegno(),filtri.getInterventi()))
					.and(getFilterAnomalie(filtri.getAnomalie()))
					.and(getFilterCodiciAnomalieInfo(filtri.getCodiciAnomalieInfo()))
					.and(getFilterCodiciAnomalieWarning(filtri.getCodiciAnomalieWarning()))
					.and(getFilterCodiciAnomalieError(filtri.getCodiciAnomalieError()))
					.and(getFilterIntegrazioni(filtri.getIntegrazione()))
					.toPredicate(root, query, cb);
			return filterPredicate;
		};
	}

	private static Join<PassoTransizioneModel, A4gtAnomDomandaSostegno> getJoinAnomalieFrom(Root<IstruttoriaModel> root) {
		final Join<IstruttoriaModel, TransizioneIstruttoriaModel> transizioni = root.join(IstruttoriaModel_.transizioni, JoinType.INNER);
		final Join<TransizioneIstruttoriaModel, PassoTransizioneModel> passi = transizioni.join(TransizioneIstruttoriaModel_.passiTransizione, JoinType.INNER);
		return passi.join(PassoTransizioneModel_.anomalie, JoinType.INNER);
	}
	
	public static Specification<IstruttoriaModel> getFilterCodiciAnomalieInfo(List<String> codiciAnomalieInfo) {
		return (root, query, cb) -> {
			if ((codiciAnomalieInfo == null) || (codiciAnomalieInfo.isEmpty())) {
				return null;
			}
			final Join<PassoTransizioneModel, A4gtAnomDomandaSostegno> anomalie = getJoinAnomalieFrom(root);
			query.distinct(true);
			return cb.and(cb.equal(anomalie.get(A4gtAnomDomandaSostegno_.livelloAnomalia), LivelloAnomalia.INFO.name()),
					anomalie.get(A4gtAnomDomandaSostegno_.codiceAnomalia).in(codiciAnomalieInfo));
		};
	}

	public static Specification<IstruttoriaModel> getFilterCodiciAnomalieWarning(List<String> codiciAnomalieWarning) {
		return (root, query, cb) -> {
			if ((codiciAnomalieWarning == null) || (codiciAnomalieWarning.isEmpty())) {
				return null;
			}
			final Join<PassoTransizioneModel, A4gtAnomDomandaSostegno> anomalie = getJoinAnomalieFrom(root);
			query.distinct(true);
			return cb.and(cb.equal(anomalie.get(A4gtAnomDomandaSostegno_.livelloAnomalia), LivelloAnomalia.WARNING.name()),
					anomalie.get(A4gtAnomDomandaSostegno_.codiceAnomalia).in(codiciAnomalieWarning));
		};
	}

	public static Specification<IstruttoriaModel> getFilterCodiciAnomalieError(List<String> codiciAnomalieError) {
		return (root, query, cb) -> {
			if ((codiciAnomalieError == null) || (codiciAnomalieError.isEmpty())) {
				return null;
			}
			final Join<PassoTransizioneModel, A4gtAnomDomandaSostegno> anomalie = getJoinAnomalieFrom(root);
			query.distinct(true);
			return cb.and(cb.equal(anomalie.get(A4gtAnomDomandaSostegno_.livelloAnomalia), LivelloAnomalia.ERROR.name()),
					anomalie.get(A4gtAnomDomandaSostegno_.codiceAnomalia).in(codiciAnomalieError));
		};
	}
	
	private static Subquery<A4gtAnomDomandaSostegno> subQueryAnomalie(
			final Root<IstruttoriaModel> root, final CriteriaQuery<?> query,
			final CriteriaBuilder cb) {
		Subquery<A4gtAnomDomandaSostegno> subqueryAnomalie = query.subquery(A4gtAnomDomandaSostegno.class);
		Root<A4gtAnomDomandaSostegno> rootAnomalie = subqueryAnomalie.from(A4gtAnomDomandaSostegno.class);		
		Predicate filtroLivelloAnomaliaPredicate = cb.or(
				cb.equal(rootAnomalie.get(A4gtAnomDomandaSostegno_.livelloAnomalia), LivelloAnomalia.ERROR.name()),
				cb.equal(rootAnomalie.get(A4gtAnomDomandaSostegno_.livelloAnomalia), LivelloAnomalia.WARNING.name()));
		Predicate filtroIstruttoriaAnomalia = cb.equal(rootAnomalie.get(A4gtAnomDomandaSostegno_.passoLavorazione)
					.get(PassoTransizioneModel_.transizioneIstruttoria)
					.get(TransizioneIstruttoriaModel_.istruttoria)
					.get(IstruttoriaModel_.id), root.get(IstruttoriaModel_.id));
		return subqueryAnomalie.select(rootAnomalie)
				.where(cb.and(filtroLivelloAnomaliaPredicate, filtroIstruttoriaAnomalia));
	}

	public static Specification<IstruttoriaModel> getFilterIntegrazioni(YesNoEnum integrazioni) {
		return (root, query, cb) -> {
			if (integrazioni == null) {
				return null;
			}
			//YES=Integrate/NO=Integrabili
			if (integrazioni.equals(YesNoEnum.YES)) {
				return cb.exists(subQueryIntegrazione(root, query, cb));
			} else {
				return cb.not(cb.exists(subQueryIntegrazione(root, query, cb)));
			}
		};
	}
	
	private static Subquery<IstruttoriaModel> subQueryIntegrazione(
			final Root<IstruttoriaModel> root, final CriteriaQuery<?> query,
			final CriteriaBuilder cb) {
		Subquery<IstruttoriaModel> subqueryIntegrazione = query.subquery(IstruttoriaModel.class);
		Root<IstruttoriaModel> rootIstruttoria = subqueryIntegrazione.from(IstruttoriaModel.class);		
		Predicate filtro1 = cb.equal(
				rootIstruttoria.get(IstruttoriaModel_.tipologia), 
				TipoIstruttoria.INTEGRAZIONE
				);
		Predicate filtro2 = cb.equal(
				rootIstruttoria.get(IstruttoriaModel_.domandaUnicaModel).get(DomandaUnicaModel_.id), 
				root.get(IstruttoriaModel_.domandaUnicaModel).get(DomandaUnicaModel_.id)
				);
		return subqueryIntegrazione.select(rootIstruttoria)
				.where(cb.and(filtro1,filtro2));
	}
	
	public static Specification<IstruttoriaModel> getFilterAnomalie(YesNoEnum anomalia) {
		return (root, query, cb) -> {
			if (anomalia == null) {
				return null;
			}
			if (anomalia.equals(YesNoEnum.YES)) {
				return cb.exists(subQueryAnomalie(root, query, cb));
			} else {
				return cb.not(cb.exists(subQueryAnomalie(root, query, cb)));
			}
		};
	}

	private static Specification<IstruttoriaModel> getFilterInterventiGenerico(Sostegno sostegno,List<String> codiciAgea) {
		if (Sostegno.ZOOTECNIA.equals(sostegno)) {
			return getFilterInterventiAcz(codiciAgea);
		}
		return getFilterInterventi(codiciAgea);
	}

	public static Specification<IstruttoriaModel> getFilterInterventi(List<String> codiciAgea) {
		return (root, query, cb) -> {
			if (codiciAgea == null) {
				return null;
			}
			Subquery<A4gtRichiestaSuperficie> subQuery = query.subquery(A4gtRichiestaSuperficie.class);
			Root<A4gtRichiestaSuperficie> subRootCampione = subQuery.from(A4gtRichiestaSuperficie.class);
			Predicate richiestaSuperficiePred = cb.equal(
					root.get(IstruttoriaModel_.domandaUnicaModel),
					subRootCampione.get(A4gtRichiestaSuperficie_.domandaUnicaModel));
			ArrayList<Predicate> preds = new ArrayList<Predicate>();
			for (String codiceAgea : codiciAgea) {
				preds.add(cb.equal(
						subRootCampione.get(A4gtRichiestaSuperficie_.intervento).get(InterventoModel_.codiceAgea),
						codiceAgea));
			}
			Predicate ageaPred = cb.or(preds.toArray(new Predicate[codiciAgea.size()]));
			Subquery<A4gtRichiestaSuperficie> subQueryFilter = subQuery.select(subRootCampione)
					.where(richiestaSuperficiePred, ageaPred);
				return cb.exists(subQueryFilter);
		};
	}
	
	public static Specification<IstruttoriaModel> getFilterInterventiAcz(List<String> codiciAgea) {
		return (root, query, cb) -> {
			if (codiciAgea == null) {
				return null;
			}
			//per zootecnia
			Subquery<AllevamentoImpegnatoModel> subQueryAcz = query.subquery(AllevamentoImpegnatoModel.class);
			Root<AllevamentoImpegnatoModel> subRootAcz= subQueryAcz.from(AllevamentoImpegnatoModel.class);
			Predicate richiestaAcz = cb.equal(
					root.get(IstruttoriaModel_.domandaUnicaModel),
					subRootAcz.get(AllevamentoImpegnatoModel_.domandaUnica));
			ArrayList<Predicate> predsAcz = new ArrayList<Predicate>();
			for (String codiceAgea : codiciAgea) {
				predsAcz.add(cb.equal(
						subRootAcz.get(AllevamentoImpegnatoModel_.intervento).get(InterventoModel_.codiceAgea),
						codiceAgea));
			}
			Predicate ageaPredAcz = cb.or(predsAcz.toArray(new Predicate[codiciAgea.size()]));			
			Subquery<AllevamentoImpegnatoModel> subQueryFilter = subQueryAcz.select(subRootAcz)
					.where(richiestaAcz, ageaPredAcz);
			return cb.exists(subQueryFilter);
		};
	}

	public static Specification<IstruttoriaModel> getFilterStato(StatoIstruttoria stato) {
		return (root, query, cb) -> {
			if (stato == null) {
				return null;
			}
			return cb.equal(root.get(IstruttoriaModel_.a4gdStatoLavSostegno).get(A4gdStatoLavSostegno_.identificativo), stato.getStatoIstruttoria());
		};
	}

	public static Specification<IstruttoriaModel> getFilterSostegno(Sostegno sostegno) {
		return (root, query, cb) -> {
			if (sostegno == null) {
				return null;
			}
			return cb.equal(root.get(IstruttoriaModel_.sostegno), sostegno);
		};
	}

	public static Specification<IstruttoriaModel> getFilterAnno(Integer anno) {
		return (root, query, cb) -> {
			if (anno == null) {
				return null;
			}
			return cb.equal(root.get(IstruttoriaModel_.domandaUnicaModel).get(DomandaUnicaModel_.campagna), anno);
		};
	}

	public static Specification<IstruttoriaModel> getFilterTipologia(TipoIstruttoria tipologia) {
		return (root, query, cb) -> {
			if (tipologia == null) {
				return null;
			}
			return cb.equal(root.get(IstruttoriaModel_.tipologia), tipologia);
		};
	}
	
	public static Specification<IstruttoriaModel> getFilterCuaa(final String cuaa) {
		return (root, query, cb) -> {
			if (cuaa == null) {
				return null;
			}
			return cb.like(
					cb.upper(root.get(IstruttoriaModel_.domandaUnicaModel).get(DomandaUnicaModel_.cuaaIntestatario)),
					StringSupport.upperlike(cuaa));
		};
	}
	
	public static Specification<IstruttoriaModel> getFilterRagioneSociale(final String ragioneSociale) {
		return (root, query, cb) -> {
			if (ragioneSociale == null) {
				return null;
			}
			return cb.like(
					cb.upper(root.get(IstruttoriaModel_.domandaUnicaModel).get(DomandaUnicaModel_.ragioneSociale)),
					StringSupport.upperlike(ragioneSociale));
		};
	}
	
	public static Specification<IstruttoriaModel> getFilterNumeroDomanda(final Long numeroDomanda) {
		return (root, query, cb) -> {
			if (numeroDomanda == null) {
				return null;
			}
			return cb.equal(root.get(IstruttoriaModel_.domandaUnicaModel).get(DomandaUnicaModel_.numeroDomanda), numeroDomanda.toString());
		};
	}
	
	public static Specification<IstruttoriaModel> getFilterIstruttoriaBloccata(final YesNoEnum bloccata) {
		return (root, query, cb) -> {
			if (bloccata == null) {
				return null;
			}
			return cb.equal(root.get(IstruttoriaModel_.bloccataBool), bloccata.equals(YesNoEnum.YES) ? 1 : 0);
		};
	}

	public static Specification<IstruttoriaModel> getFilterErroreCalcolo(final YesNoEnum value) {
		return (root, query, cb) -> {
			if (value == null) {
				return null;
			}
			if (value.equals(YesNoEnum.YES)) {
				return cb.equal(root.get(IstruttoriaModel_.erroreCalcolo), 1);
			} else {
				return cb.or(cb.equal(root.get(IstruttoriaModel_.erroreCalcolo), 0), cb.isNull(root.get(IstruttoriaModel_.erroreCalcolo)));
				//return cb.equal(root.get(IstruttoriaModel_.erroreCalcolo), 0);
			}
			//return cb.equal(root.get(IstruttoriaModel_.erroreCalcolo), value.equals(YesNoEnum.YES)  ? 1 : 0);
		};
	}
	
	public static Specification<IstruttoriaModel> getFilterGiovane(final YesNoEnum value) {
		return (root, query, cb) -> {
			if (value == null) {
				return null;
			}
			return subQueryGiovaneDomanda(root, query, cb, value.equals(YesNoEnum.YES));
		};
	}
	
	public static Specification<IstruttoriaModel> getFilterCampione(final YesNoEnum value) {
		return (root, query, cb) -> {
			if (value == null) {
				return null;
			}
			boolean toMatch = false;
			if (value.equals(YesNoEnum.YES)) {
				toMatch = true;
			}
			return subQueryCampione(root, query, cb, toMatch);
		};
	}
	
	public static Specification<IstruttoriaModel> getFilterPascoli(final YesNoEnum value) {
		return (root, query, cb) -> {
			if (value == null) {
				return null;
			}
			return subQueryPascoloParticella(root, query, cb, value.equals(YesNoEnum.YES));
		};
	}
	
	public static Specification<IstruttoriaModel> getFilterRiservaNazionale(final RiservaNazionaleEnum value) {
		return (root, query, cb) -> {
			if (value == null) {
				return null;
			}
			String valueToMatch;
			switch (value) {
			case A_GIOVANE_AGRICOLTORE:
				valueToMatch = "A - GIOVANE AGRICOLTORE";
				break;
			case B_NUOVO_AGRICOLTORE:
				valueToMatch = "B - NUOVO AGRICOLTORE";
				break;
			case C_ABBANDONO_TERRE:
				valueToMatch = "C - ABBANDONO DI TERRE";
				break;
			case D_COMPENSAZIONE_SVANTAGGI_SPECIFICI:
				valueToMatch = "D - COMPENSAZIONE DI SVANTAGGI SPECIFICI";
				break;
			case F_PROVVEDIMENTI_AMMINISTRATIVI_DECISIONI_GIUDIZIARIE:
				valueToMatch = "F – PROVVEDIMENTI AMMINISTRATIVI E DECISIONI GIUDIZIARIE";
				break;
			case NON_RICHIESTA:
				valueToMatch = null;
				break;
			default:
				return null;
			}
			return subQueryRiservaNazionaleDomanda(root, query, cb, valueToMatch);
		};
	}

	private enum LivelloAnomalia {
		WARNING, ERROR, INFO, SUCCESS, NULL;
	}
}
