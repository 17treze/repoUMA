package it.tndigitale.a4g.fascicolo.antimafia.repository.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;

import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gtDichiarazioneAntimafia;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.fascicolo.antimafia.StatoDichiarazioneEnum;
import it.tndigitale.a4g.fascicolo.antimafia.dto.DichiarazionePaginataFilter;
import it.tndigitale.a4g.fascicolo.antimafia.dto.Sort;
import it.tndigitale.a4g.fascicolo.antimafia.dto.StatoDic;
import it.tndigitale.a4g.fascicolo.antimafia.repository.model.A4gtDichiarazioneAntimafia;

public class DichiarazioneAntimafiaSpecificationsFilter {
	
	private final static String wildcard = "%";
	
	private DichiarazioneAntimafiaSpecificationsFilter() {
	}

	public static Specification<A4gtDichiarazioneAntimafia> findByCriteria(DichiarazionePaginataFilter dichiarazione, Sort sort) {
		return (root, query, cb) -> {
			query.distinct(true);
			//eager loading data
			//FIX error = org.hibernate.QueryException: query specified join fetching, but the owner of the fetched association was not present in the select list
			Join<Object, Object> fetchStato;
			Join<Object, Object> fetchAzienda;
			if (query.getResultType() != Long.class && query.getResultType() != long.class) {
				fetchStato = (Join) root.fetch("a4gdStatoDicAntimafia");
				fetchAzienda = (Join) root.fetch("a4gtAziendaAgricola");
			} else {
				//nel caso della count viene fatto una semplice join
				fetchStato = root.join("a4gdStatoDicAntimafia");
				fetchAzienda =  root.join("a4gtAziendaAgricola");
			}
			Specification<A4gtDichiarazioneAntimafia> whereNull= Specification.where(null);
			if(sort != null && sort.getSortBy() != null && sort.getSortBy().length > 0) {
				List<Order> order = new ArrayList<>();
				for (String ordine : sort.getSortBy()) {
					String property = ordine.substring(1);
					Path<Object> path;
					if ("cuaa".equals(property)) {
						path = fetchAzienda.get("cuaa");
					} else {
						path = root.get(property);
					}
					order.add(ordine.startsWith("-")?cb.desc(path):cb.asc(path));
				}
				order.add(cb.asc(root.get("id")));
				query.orderBy(order);
			}

			return whereNull
					.and(withState(fetchStato,dichiarazione.getStatiDichiarazione()))
					.and(withAzienda(fetchAzienda,dichiarazione.getFiltroGenerico()).or(withCampoGenerico(dichiarazione.getFiltroGenerico())))
					.toPredicate(root, query, cb);
		};
	}

	public static Specification<A4gtDichiarazioneAntimafia> withState(Join<Object, Object> fetchStato, List<StatoDichiarazioneEnum> stati) {
		if (stati == null) return null;
		List<String> statiString = stati.stream().map(x -> x.getIdentificativoStato()).collect(Collectors.toList());
		return (root, query, cb) -> stati.isEmpty() ? null : fetchStato.get("identificativo").in(statiString);
	}
	public static Specification<A4gtDichiarazioneAntimafia> withState(Join<Object, Object> fetchStato, String campo,Object valore) {
		return (root, query, cb) ->  valore == null ? null : cb.equal(fetchStato.get(campo), valore);
	}	
	public static Specification<A4gtDichiarazioneAntimafia> withState(Join<Object, Object> fetchStato, StatoDic stato) {
		Specification<A4gtDichiarazioneAntimafia> specs= Specification.where(null);
		return specs
				.and(withState(fetchStato,"id",stato.getId()))
				.and(withState(fetchStato,"descrizione",stato.getDescrizione()))
				.and(withState(fetchStato,"identificativo",stato.getIdentificativo()));
	}	
	public static Specification<A4gtDichiarazioneAntimafia> withAzienda(Join<Object, Object> fetchAzienda, String cuaaValue) {
		return (root, query, cb) -> cuaaValue == null ? null : 
			cb.like(cb.lower(fetchAzienda.get("cuaa")), containsLowerCase(cuaaValue));
	}	
	
	public static Specification<A4gtDichiarazioneAntimafia> withCampoGenerico(String filtroGenerico) {
		return (root, query, cb) -> {
			if (filtroGenerico == null || filtroGenerico.isEmpty()) {
				return null;
			}
			return Specification.where(attributeContains("denominazioneImpresa", filtroGenerico))
								.toPredicate(root, query, cb);
		};
	}	
	
	private static Specification<A4gtDichiarazioneAntimafia> attributeContains(String attribute, String value) {
		return (root, query, cb) -> {
			if(value == null) {
				return null;
			}

			return cb.like(
					cb.lower(root.get(attribute)),
					containsLowerCase(value)
					);
		};
	}

	private static String containsLowerCase(String searchField) {
		return wildcard + searchField.toLowerCase() + wildcard;
	}

}
