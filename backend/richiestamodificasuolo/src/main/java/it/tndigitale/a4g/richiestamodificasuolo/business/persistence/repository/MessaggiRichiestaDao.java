package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessaggiRichiestaDao extends JpaRepository<MessaggioRichiestaModel, Long>,JpaSpecificationExecutor<MessaggioRichiestaModel> {

    @Query("select s from MessaggioRichiestaModel s WHERE s.relSuoloDichiarato.id = :idDichiarato")
    List<MessaggioRichiestaModel> findByIdDichiarato(@Param("idDichiarato") Long idDichiarato);

}
