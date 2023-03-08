package it.tndigitale.a4g.proxy.repository.sincronizzazione.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.TitoliPac;

public interface TitoliPacDao extends JpaRepository<TitoliPac, Long> {

	@Query("select t from TitoliPac t where t.cuaa = :cuaa and :anno between t.numeCampIniz and t.numeCampFine")
	List<TitoliPac> findByCuaaAndAnno(@Param("cuaa") String codFisc, @Param("anno") BigDecimal annoCamp);

}
