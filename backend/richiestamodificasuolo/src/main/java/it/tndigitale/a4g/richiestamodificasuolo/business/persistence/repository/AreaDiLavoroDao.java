package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.AreaDiLavoroModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;

@Repository
public interface AreaDiLavoroDao extends JpaRepository<AreaDiLavoroModel, Long>, JpaSpecificationExecutor<AreaDiLavoroModel> {
	List<AreaDiLavoroModel> findByLavorazioneSuolo(LavorazioneSuoloModel lavorazione);

	@Modifying
	@Query("DELETE FROM AreaDiLavoroModel a WHERE a.lavorazioneSuolo.id = :idLavorazione ")
	public Integer deleteByIdLavorazione(@Param("idLavorazione") Long idLavorazione);

	@Query(value = "select SDO_GEOM.VALIDATE_GEOMETRY(s.shape, :tolleranza) from A4ST_AREA_DI_LAVORO s where id =:id", nativeQuery = true)
	public String validateGeometry(@Param("id") Long id, @Param("tolleranza") Double tolleranza);

	@Query(value = "select SDO_GEOM.SDO_AREA(s.shape, :tolleranza) from A4ST_AREA_DI_LAVORO s where id =:id", nativeQuery = true)
	public Double getArea(@Param("id") Long id, @Param("tolleranza") Double tolleranza);

	@Query(value = "select SDO_GEOM.SDO_AREA(SDO_CUSTOM.SDO_FIX(s.shape, :tolleranza), :tolleranza) from A4ST_AREA_DI_LAVORO s where id =:id", nativeQuery = true)
	Double getAreaFixed(@Param("id") Long id, @Param("tolleranza") Double tolleranza);

	@Transactional
	@Modifying
	@Query(value = "UPDATE A4ST_AREA_DI_LAVORO s set shape = SDO_CUSTOM.SDO_FIX(s.shape, :tolleranza) where id =:id", nativeQuery = true)
	public void fixAdl(@Param("id") Long id, @Param("tolleranza") Double tolleranza);

	@Query(value = "select SDO_GEOM.VALIDATE_GEOMETRY(SDO_CUSTOM.SDO_FIX(s.shape, :tolleranza), :tolleranza) from A4ST_AREA_DI_LAVORO s where id =:id", nativeQuery = true)
	public String validateFixedGeometry(@Param("id") Long id, @Param("tolleranza") Double tolleranza);

	@Query(value = "select SDO_UTIL.GETNUMELEM(SDO_CUSTOM.SDO_FIX(s.shape, :tolleranza)) from A4ST_AREA_DI_LAVORO s where id =:id", nativeQuery = true)
	public Long getNumElemFixed(@Param("id") Long id, @Param("tolleranza") Double tolleranza);

}
