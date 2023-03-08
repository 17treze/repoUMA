package it.tndigitale.a4g.proxy.repository.sincronizzazione.dao;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AabagareTab;

@Repository
public interface AabagareDao extends JpaRepository<AabagareTab, Long> {

	@Query("select a.flagGiovAgri from AabagareTab a where a.codFisc = :codFisc and a.annoCamp = :annoCamp and CURRENT_DATE between a.dataInizAsse and a.dataFineAsse")
	BigDecimal findFlagGiovaneByCodFiscAndAnnoCamp(@Param("codFisc") String codFisc, @Param("annoCamp") BigDecimal annoCamp);

}
