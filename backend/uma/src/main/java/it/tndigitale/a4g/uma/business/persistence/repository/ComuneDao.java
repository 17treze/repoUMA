package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.ComuneModel;

@Repository
public interface ComuneDao extends JpaRepository<ComuneModel, String> {
	
	@Query(value = "SELECT * FROM TAB_AGRI_UMAL_COMUNI WHERE CODI_COMU = CODI_COMU_CAPO ORDER BY DESC_COMU", nativeQuery = true)
	public List<ComuneModel> findAllCapofila();

	@Query(value = "select c.* from tab_agri_umal_comuni c\n"
			+ "where c.codi_prov || c.codi_comu IN (\n"
			+ "    select distinct d.codi_prov || d.codi_comu_capo from tab_agri_umal_comuni d\n"
			+ "    where d.codi_prov || d.codi_comu IN (:comuniTerreni)\n"
			+ ")", nativeQuery = true)
	public List<ComuneModel> findCapofilaComuni(@Param("comuniTerreni") List<String> comuniTerreni);
}
