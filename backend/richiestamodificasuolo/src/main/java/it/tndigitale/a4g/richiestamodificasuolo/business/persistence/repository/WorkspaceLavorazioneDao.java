package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoAnomaliaValidazione;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.WorkspaceLavSuoloModel;

@Repository
public interface WorkspaceLavorazioneDao extends JpaRepository<WorkspaceLavSuoloModel, Long>, JpaSpecificationExecutor<WorkspaceLavSuoloModel> {
	List<WorkspaceLavSuoloModel> findByIdLavorazioneWorkspaceLavSuolo(LavorazioneSuoloModel lavorazione);

	List<WorkspaceLavSuoloModel> findWorkspaceLavSuoloModelByIdLavorazioneWorkspaceLavSuoloAndAnomaliaValidazioneTipoAnomalia(LavorazioneSuoloModel lavorazione, TipoAnomaliaValidazione tipo);

	@Query(value = "select SDO_GEOM.VALIDATE_GEOMETRY(s.shape, :tolleranza) from A4ST_WORKSPACE_LAV_SUOLO s where id =:id", nativeQuery = true)
	String validateGeometry(@Param("id") Long id, @Param("tolleranza") Double tolleranza);

	@Query(value = "select SDO_GEOM.SDO_AREA(SDO_CUSTOM.SDO_FIX_LIGHT(s.shape, :tolleranza), :tolleranza) from A4ST_WORKSPACE_LAV_SUOLO s where id =:id", nativeQuery = true)
	Double getAreaFixed(@Param("id") Long id, @Param("tolleranza") Double tolleranza);

	@Procedure("A4SP_VALIDITA_ORACLE.fixWorkspace")
	void fixWorkspace(@Param("pid") Long id, @Param("ptolleranza") Double tolleranza);

	@Query(value = "select SDO_GEOM.VALIDATE_GEOMETRY(SDO_CUSTOM.SDO_FIX_LIGHT(s.shape, :tolleranza), :tolleranza) from A4ST_WORKSPACE_LAV_SUOLO s where id =:id", nativeQuery = true)
	String validateFixedGeometry(@Param("id") Long id, @Param("tolleranza") Double tolleranza);

	@Query(value = "select SDO_UTIL.GETNUMELEM(SDO_CUSTOM.SDO_FIX_LIGHT(s.shape, :tolleranza)) from A4ST_WORKSPACE_LAV_SUOLO s where id =:id", nativeQuery = true)
	Long getNumElemFixed(@Param("id") Long id, @Param("tolleranza") Double tolleranza);
}