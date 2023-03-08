package it.tndigitale.a4g.proxy.repository.sincronizzazione.dao;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabaraiaTab;

@Repository
public interface AabaraiaDao extends JpaRepository<AabaraiaTab, Long> {

	@Query("select a from AabaraiaTab a where a.codFisc = :codFisc and a.annoCamp = :annoCamp and CURRENT_DATE between a.dataInizAsse and a.dataFineAsse")
	AabaraiaTab findByCodFiscAndAnnoCamp(@Param("codFisc") String codFisc, @Param("annoCamp") BigDecimal annoCamp);

}
