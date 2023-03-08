package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.DocumentazioneRichiestaModificaSuoloModel;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface DocumentazioneRichiestaDao extends JpaRepository<DocumentazioneRichiestaModificaSuoloModel, Long>, JpaSpecificationExecutor<DocumentazioneRichiestaModificaSuoloModel> {
}

