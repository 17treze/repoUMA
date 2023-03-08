package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TempPoligoniInOutAdlModel;

@Repository
public interface TempPoligoniInOutAdlDao extends JpaRepository<TempPoligoniInOutAdlModel, Long>, JpaSpecificationExecutor<TempPoligoniInOutAdlModel> {

	List<TempPoligoniInOutAdlModel> findByLavorazioneSuolo(LavorazioneSuoloModel lavorazioneSuoloModel);

	@Query(value = "select SDO_GEOM.VALIDATE_GEOMETRY(s.shape, :tolleranza) from A4ST_TEMP_POLIGONI_IN_OUT_ADL s where id =:id", nativeQuery = true)
	String validateGeometry(@Param("id") Long id, @Param("tolleranza") Double tolleranza);

	@Query(value = "select SDO_GEOM.SDO_AREA(SDO_CUSTOM.SDO_FIX_LIGHT(s.shape, :tolleranza), :tolleranza) from A4ST_TEMP_POLIGONI_IN_OUT_ADL s where id =:id", nativeQuery = true)
	Double getAreaFixed(@Param("id") Long id, @Param("tolleranza") Double tolleranza);

	@Transactional
	@Modifying
	@Query(value = "UPDATE A4ST_TEMP_POLIGONI_IN_OUT_ADL s set area =  SDO_GEOM.SDO_AREA(s.shape, :tolleranza) where id =:id", nativeQuery = true)
	public void fixArea(@Param("id") Long id, @Param("tolleranza") Double tolleranza);

	@Transactional
	@Modifying
	@Query(value = "UPDATE A4ST_TEMP_POLIGONI_IN_OUT_ADL s set shape = SDO_CUSTOM.SDO_FIX_LIGHT(s.shape, :tolleranza) where id =:id", nativeQuery = true)
	public void fixData(@Param("id") Long id, @Param("tolleranza") Double tolleranza);

	@Query(value = "select SDO_GEOM.VALIDATE_GEOMETRY(SDO_CUSTOM.SDO_FIX_LIGHT(s.shape, :tolleranza), :tolleranza) from A4ST_TEMP_POLIGONI_IN_OUT_ADL s where id =:id", nativeQuery = true)
	String validateFixedGeometry(@Param("id") Long id, @Param("tolleranza") Double tolleranza);

	@Query(value = "select SDO_UTIL.GETNUMELEM(SDO_CUSTOM.SDO_FIX_LIGHT(s.shape, :tolleranza)) from A4ST_TEMP_POLIGONI_IN_OUT_ADL s where id =:id", nativeQuery = true)
	Long getNumElemFixed(@Param("id") Long id, @Param("tolleranza") Double tolleranza);

}
