package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.dto.DomandaFilter;
import it.tndigitale.a4gistruttoria.repository.model.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllevamentoImpegnatoDao
		extends JpaRepository<AllevamentoImpegnatoModel, Long>, JpaSpecificationExecutor<AllevamentoImpegnatoModel> {

	Long countByDomandaUnica(DomandaUnicaModel domanda);
	
	List<AllevamentoImpegnatoModel> findByDomandaUnica_idAndIntervento_codiceAgea(Long id, String codiceAgea);

	final class RichiestaAllevamDuSpecifications {

		private RichiestaAllevamDuSpecifications() {
		}

		public static Specification<AllevamentoImpegnatoModel> withState(List<StatoDomanda> stati) {
			if (stati == null) {
				return null;
			}
			return (root, query, cb) -> root.join(AllevamentoImpegnatoModel_.domandaUnica)
					.get(DomandaUnicaModel_.stato).in(stati);
		}

		public static Specification<AllevamentoImpegnatoModel> withCodiciAgea(List<String> codiciAgea) {
			return (root, query, cb) -> codiciAgea == null ? null
					: root.join(AllevamentoImpegnatoModel_.intervento).get(InterventoModel_.codiceAgea)
							.in(codiciAgea);
		}

		public static Specification<AllevamentoImpegnatoModel> withDomandaIds(List<Long> idsDomande) {
			return (root, query, cb) -> idsDomande == null ? null
					: root.join(AllevamentoImpegnatoModel_.domandaUnica).get(DomandaUnicaModel_.id).in(idsDomande);
		}

		public static Specification<AllevamentoImpegnatoModel> withStateFilter(Object valore) {
			return (root, query, cb) -> valore == null ? null : cb.equal(root.get("stato"), valore);
		}

		public static Specification<AllevamentoImpegnatoModel> build(DomandaFilter domandaFilter) {
			Specification<AllevamentoImpegnatoModel> specs = Specification.where(null);
			return specs.and(withState(domandaFilter.getStati())).and(withCodiciAgea(domandaFilter.getCodiciAgea()));
		}

		// Query usata per filtrare tutte le domande di zootecnia filtrate per stato,
		// intervento ed esito
		// e per recuperare le informazioni relative la domanda e l'intervento così da
		// evitare il LazyInitializationException
		public static Specification<AllevamentoImpegnatoModel> buildQuery4AvvioZootecnia(DomandaFilter domandaFilter) {
			Specification<AllevamentoImpegnatoModel> specs = Specification.where(null);
			return specs.and(withState(domandaFilter.getStati())).and(withCodiciAgea(domandaFilter.getCodiciAgea()))
					.and(withDomandaIds(domandaFilter.getIdsDomande()));
		}
	}
}