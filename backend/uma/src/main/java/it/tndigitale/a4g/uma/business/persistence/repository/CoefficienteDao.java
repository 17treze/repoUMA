package it.tndigitale.a4g.uma.business.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.CoefficienteModel;

@Repository
public interface CoefficienteDao extends
		JpaRepository<CoefficienteModel, Long>,
		JpaSpecificationExecutor<CoefficienteModel> {
	
	@Query(value = "SELECT * FROM A4GD_COEFFICIENTI WHERE EXTRACT(YEAR FROM SYSDATE) >= ANNO_INIZIO"
			+ " AND (ANNO_FINE IS NULL OR EXTRACT(YEAR FROM SYSDATE) <= ANNO_FINE)", nativeQuery = true)
	public Page<CoefficienteModel> findAllValid(Pageable pageable);
	
}
