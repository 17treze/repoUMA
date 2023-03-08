package it.tndigitale.a4gistruttoria.repository.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.A4gtComuniLimitrofi;

@Repository
public interface ComuniLimitrofiDao extends JpaRepository<A4gtComuniLimitrofi, Long> {

	public List<A4gtComuniLimitrofi> findByCodNazComuneConfinante(String codNazComuneConfinante);

	public List<A4gtComuniLimitrofi> findByCodNazionaleComune(String codNazionaleComune);

	@Query("select distinct c.codiceComune from A4gtComuniLimitrofi c where c.codNazionaleComune = :codNazionaleComune")
	public BigDecimal getCodComuneByCodNazionale(@Param("codNazionaleComune") String codNazionaleComune);

	@Query("select distinct c.codiceComuneConfinante from A4gtComuniLimitrofi c where c.codiceComune in (:listCodiciComune)")
	public Set<BigDecimal> getListCodComuniLimitrofi(@Param("listCodiciComune") Set<BigDecimal> listCodiciComune);

}
