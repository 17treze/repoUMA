package it.tndigitale.a4gistruttoria.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4gistruttoria.repository.model.A4gtStatisticaDu;
import it.tndigitale.a4gistruttoria.util.TipologiaStatistica;

@Repository
public interface StatisticheDao extends JpaRepository<A4gtStatisticaDu, Long> {
	
	// C110: anno campagna
	@Transactional
	@Modifying
	@Query("DELETE FROM A4gtStatisticaDu t WHERE t.tipoStatistica = :TipoStatistica AND t.c110 = :C110")
	int deleteByTipoStatisticaAndC110(@Param("TipoStatistica") TipologiaStatistica tipoStatistica, @Param("C110") Integer c110);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM A4gtStatisticaDu t WHERE t.tipoStatistica = :tipoStatistica AND t.c110 = :c110 AND t.c109a in :misure")
	int deleteByTipoStatisticaAndC110AndC109a(TipologiaStatistica tipoStatistica, Integer c110, List<String> misure);

}
