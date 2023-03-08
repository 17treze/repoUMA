package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloModel;

@Repository
public interface SuoloDao extends JpaRepository<SuoloModel, Long>, JpaSpecificationExecutor<SuoloModel> {

	List<SuoloModel> findByIdLavorazioneInCorso(LavorazioneSuoloModel lavorazioneSuoloModel);

	@Query("select s from SuoloModel s where intersects(s.shape, :poligono) = 'TRUE' AND s.campagna=:campagna")
	List<SuoloModel> findByintersects(@Param("poligono") Geometry poligono, @Param("campagna") Integer campagna);

	@Query("select s from SuoloModel s where intersects(s.shape, (select sd.shape from SuoloDichiaratoModel sd where sd.id = :idSuoloDichiarato)) = 'TRUE' AND s.campagna=:campagna")
	List<SuoloModel> findByintersectsSuoloDichiarato(@Param("idSuoloDichiarato") Long idSuoloDichiarato, @Param("campagna") Integer campagna);

	@Query("select s from SuoloModel s where s.campagna = :campagna AND contains(s.shape, :poligono) = 'TRUE' AND sysdate BETWEEN s.dataInizioValidita AND s.dataFineValidita")
	List<SuoloModel> findByContains(@Param("campagna") Integer campagna, @Param("poligono") Geometry poligono);

	@Transactional
	@Modifying
	@Query("update SuoloModel s set s.idLavorazioneInCorso = null where s.idLavorazioneInCorso.id = :idLavorazione")
	public Integer rimuoviSuoloDaLavorazioneInCorso(@Param("idLavorazione") Long idLavorazione);

}
