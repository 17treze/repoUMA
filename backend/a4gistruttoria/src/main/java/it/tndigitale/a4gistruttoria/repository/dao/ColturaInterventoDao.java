package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.A4gdColturaIntervento;
import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColturaInterventoDao extends JpaRepository<A4gdColturaIntervento, Long> {
	@Query("select a from A4gdColturaIntervento a "
			+ "where (a.annoInizio <= :annoRiferimento "
			+ "and (a.annoFine is null or :annoRiferimento <= a.annoFine)) "
			+ "and a.intervento.identificativoIntervento = :codiceInterventoAgs")
	public List<A4gdColturaIntervento> findColtureByIntervento(
			@Param("codiceInterventoAgs") CodiceInterventoAgs codiceInterventoAgs,
			@Param("annoRiferimento") Integer annoRiferimento);

	@Query("select a from A4gdColturaIntervento a "
			+ "where (a.annoInizio <= :annoRiferimento "
			+ "and (a.annoFine is null or :annoRiferimento <= a.annoFine)) "
			+ "and a.codiceColtura3 = :codiceColtura3 "
			+ "and a.intervento.identificativoIntervento = :codiceInterventoAgs")
	public A4gdColturaIntervento findByCodiceColtura3AndIdInterventoDu(
			@Param("codiceColtura3") String codiceColtura3,
			@Param("codiceInterventoAgs") CodiceInterventoAgs codiceInterventoAgs,
			@Param("annoRiferimento") Integer annoRiferimento);
	
}
