package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.RichiestaModificaSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloDichiaratoModel;

@Repository
public interface SuoloDichiaratoDao extends JpaRepository<SuoloDichiaratoModel, Long>, JpaSpecificationExecutor<SuoloDichiaratoModel> {

	List<SuoloDichiaratoModel> findByRichiestaModificaSuolo(RichiestaModificaSuoloModel richiesta);

	List<SuoloDichiaratoModel> findByLavorazioneSuolo(LavorazioneSuoloModel lavorazione);

	@Query("SELECT c FROM SuoloDichiaratoModel c WHERE (c.richiestaModificaSuolo.id = :idRichiesta and (c.visibileInOrtofoto = null or c.visibileInOrtofoto = false) and (c.interventoInizio = null or c.interventoFine = null) )")
	List<SuoloDichiaratoModel> findNullMandatoryAttributes(Long idRichiesta);

	@Query("select s from SuoloDichiaratoModel s where contains(s.shape, :poligono) = 'TRUE' AND s.richiestaModificaSuolo.campagna=:campagna")
	List<SuoloDichiaratoModel> findByContains(@Param("poligono") Geometry poligono, @Param("campagna") Integer campagna);
}
