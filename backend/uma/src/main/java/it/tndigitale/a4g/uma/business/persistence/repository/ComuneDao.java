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
			+ "where c.codi_prov = :codiProv and c.codi_comu = (\n"
			+ "select d.codi_comu_capo from tab_agri_umal_comuni d\n"
			+ "where d.codi_prov = :codiProv and d.codi_comu = :codiComu)", nativeQuery = true)
	public ComuneModel findCapofilaComune(@Param("codiProv") String codiProv, @Param("codiComu") String codiComu);
}
