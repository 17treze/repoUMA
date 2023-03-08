package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.A4gtDomandaIntegrativa;
import it.tndigitale.a4gistruttoria.util.StatoDomandaIntegrativa;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Fetch;
import java.util.List;

@Repository
public interface DomandaIntegrativaDao extends JpaRepository<A4gtDomandaIntegrativa, Long>,  JpaSpecificationExecutor<A4gtDomandaIntegrativa> {

	@Query("select a from A4gtDomandaIntegrativa a "
			+ "where a.esitoCalcoloCapo.allevamentoImpegnato.domandaUnica.campagna = :campagna "
			+ "and a.esitoCalcoloCapo.allevamentoImpegnato.domandaUnica.cuaaIntestatario = :cuaaIntestatario")
	public List<A4gtDomandaIntegrativa> findByCampagnaAndCuaaIntestatario(@Param("campagna") Integer campagna, @Param("cuaaIntestatario") String cuaaIntestatario);
	
	final class DomandaIntegrativaSpecifications {
		private DomandaIntegrativaSpecifications() {
		}

		public static Specification<A4gtDomandaIntegrativa> getDomandeIntegrative() {
			return (root, query, cb) -> {
				//eager loading data
				query.distinct(true);
				Fetch<Object, Object> fetchRichAllevamEsito = root.fetch("esitoCalcoloCapo");
				fetchRichAllevamEsito.fetch("a4gtCapoTrackings");
				Fetch<Object, Object> fetchRichiestaAllevamDu = fetchRichAllevamEsito.fetch("allevamentoImpegnato");
				fetchRichiestaAllevamDu.fetch("domandaUnica");
				fetchRichiestaAllevamDu.fetch("intervento");
				return query.where(cb.equal(root.get("stato"), StatoDomandaIntegrativa.PRESENTATA.toString())).getRestriction();
			};
		}
	}
}
