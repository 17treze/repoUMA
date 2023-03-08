package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.AnomaliaValidazioneRelModel;

@Repository
public interface AnomaliaWorkspaceRelDao extends JpaRepository<AnomaliaValidazioneRelModel, Long> {

}
