package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.WorkspaceTmpModel;

@Repository
public interface WorkspaceTmpDao extends JpaRepository<WorkspaceTmpModel, Long> {
	List<WorkspaceTmpModel> findByIdLavorazioneWorkspaceTmp(LavorazioneSuoloModel lavorazione);
}
