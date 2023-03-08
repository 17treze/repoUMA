package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.JobFmeLavorazioneModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoJobFME;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobFmeLavorazioneDao extends JpaRepository<JobFmeLavorazioneModel, Long>, JpaSpecificationExecutor<JobFmeLavorazioneModel> {

    @Query("select s from JobFmeLavorazioneModel s WHERE s.relLavorazioneSuolo.id = :lavorazione and s.tipoJobFme = :tipoJobFme order by s.idJobFme desc")
    List<JobFmeLavorazioneModel> findByIdLavorazioneAndTipoJobFme(@Param("lavorazione") Long lavorazione, @Param("tipoJobFme") TipoJobFME tipoJobFme);

}
