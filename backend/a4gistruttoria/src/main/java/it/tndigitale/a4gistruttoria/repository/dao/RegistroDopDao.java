package it.tndigitale.a4gistruttoria.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.RegistroDopModel;

@Repository
public interface RegistroDopDao extends JpaRepository<RegistroDopModel, Long> {

	@Query( value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'TRUE' ELSE 'FALSE' END "
			+ "FROM A4GT_REGISTRO_DOP "
			+ "WHERE :anno BETWEEN CAMPAGNA_INIZIO AND CAMPAGNA_FINE OR :anno >= CAMPAGNA_INIZIO "
			+ "AND CUAA = :cuaa", nativeQuery = true)
	public boolean existsByCuaaAndAnno(String cuaa, Integer anno);

}