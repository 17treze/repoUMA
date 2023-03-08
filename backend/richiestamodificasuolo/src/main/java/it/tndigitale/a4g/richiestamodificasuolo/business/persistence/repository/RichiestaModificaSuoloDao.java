package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.RichiestaModificaSuoloModel;
@Repository
public interface RichiestaModificaSuoloDao extends JpaRepository<RichiestaModificaSuoloModel, Long>,JpaSpecificationExecutor<RichiestaModificaSuoloModel> {

}
