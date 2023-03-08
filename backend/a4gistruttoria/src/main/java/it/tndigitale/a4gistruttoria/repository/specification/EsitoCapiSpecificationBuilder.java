package it.tndigitale.a4gistruttoria.repository.specification;

import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import it.tndigitale.a4gistruttoria.dto.zootecnia.EsitoCapiFilter;
import it.tndigitale.a4gistruttoria.repository.model.AllevamentoImpegnatoModel;
import it.tndigitale.a4gistruttoria.repository.model.AllevamentoImpegnatoModel_;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel_;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel;
import it.tndigitale.a4gistruttoria.repository.model.EsitoCalcoloCapoModel_;
import it.tndigitale.a4gistruttoria.util.StringSupport;

public class EsitoCapiSpecificationBuilder {

	
	public static Specification<EsitoCalcoloCapoModel> getFilter(EsitoCapiFilter filtri) {
		return (root, query, cb) -> {
			if (filtri == null) {
				return null;
			}
			//distinc messo nello stream download capi agea
			//query.distinct(true); //commentato perchè da questo errore=ORA-00932: tipi di dati incoerenti: previsto -, ottenuto CLOB
			if (Boolean.TRUE.equals(filtri.getFetchCapotraking())) {
				//eager loading data
				//utilizzato per migliorare le prestazioni del download
				root.fetch(EsitoCalcoloCapoModel_.a4gtCapoTrackings);
				Fetch<EsitoCalcoloCapoModel, AllevamentoImpegnatoModel> allevamento = root.fetch(EsitoCalcoloCapoModel_.allevamentoImpegnato);
				allevamento.fetch(AllevamentoImpegnatoModel_.intervento);
				allevamento.fetch(AllevamentoImpegnatoModel_.domandaUnica);
			}
			Predicate filterPredicate =  
					Specification.where(getFilterIdAllev(filtri.getIdAllevamento()))
					.and(getFilterCodiceCapo(filtri.getCodiceCapo()))
					.and(getFilterRichiesto(filtri.getRichiesto()))
					.and(getFilterCampagna(filtri.getCampagna()))
					.toPredicate(root, query, cb);
			return filterPredicate;
		};
	}
	
	private static Specification<EsitoCalcoloCapoModel> getFilterCampagna(Integer campagna) {
		return (root, query, cb) -> {
			if (campagna == null) {
				return null;
			}
			return cb.equal(
					root.get(EsitoCalcoloCapoModel_.allevamentoImpegnato).get(AllevamentoImpegnatoModel_.domandaUnica).get(DomandaUnicaModel_.campagna),
					campagna);
		};
	}

	private static Specification<EsitoCalcoloCapoModel> getFilterIdAllev(Long idAllevamento) {
		return (root, query, cb) -> {
			if (idAllevamento == null) {
				return null;
			}
			return cb.equal(
					root.get(EsitoCalcoloCapoModel_.allevamentoImpegnato).get(AllevamentoImpegnatoModel_.id),
					idAllevamento);
		};
	}
	
	public static Specification<EsitoCalcoloCapoModel> getFilterCodiceCapo(String codice) {
		return (root, query, cb) -> {
			if (codice == null) {
				return null;
			}
			return cb.like(cb.upper(root.get(EsitoCalcoloCapoModel_.codiceCapo)), StringSupport.upperlike(codice));
		};
	}
	
	private static Specification<EsitoCalcoloCapoModel> getFilterRichiesto(Boolean richiesto) {
		return (root, query, cb) -> {
			if (richiesto == null) {
				return null;
			}
			return cb.equal(
					root.get(EsitoCalcoloCapoModel_.richiesto), richiesto);
		};
	}
	
}
