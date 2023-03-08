package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.SuoloRilevatoModel;

@Repository
public interface SuoloRilevatoDao extends JpaRepository<SuoloRilevatoModel, Long>, JpaSpecificationExecutor<SuoloRilevatoModel> {

}
