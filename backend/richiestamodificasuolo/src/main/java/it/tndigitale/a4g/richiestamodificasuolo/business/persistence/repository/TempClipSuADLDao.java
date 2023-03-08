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
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TempClipSuADLModel;

@Repository
public interface TempClipSuADLDao extends JpaRepository<TempClipSuADLModel, Long>, JpaSpecificationExecutor<TempClipSuADLModel> {

	List<TempClipSuADLModel> findByLavorazioneSuolo(LavorazioneSuoloModel lavorazioneSuoloModel);

	@Transactional
	@Modifying
	@Query("DELETE FROM TempClipSuADLModel a WHERE a.lavorazioneSuolo.id = :idLavorazione ")
	public Integer deleteByIdLavorazioneTempClipSuADL(@Param("idLavorazione") Long idLavorazione);

	List<TempClipSuADLModel> findByLavorazioneSuoloAndPosizionePoligono(LavorazioneSuoloModel lavorazioneSuoloModel, String posizionePoligono);
}
