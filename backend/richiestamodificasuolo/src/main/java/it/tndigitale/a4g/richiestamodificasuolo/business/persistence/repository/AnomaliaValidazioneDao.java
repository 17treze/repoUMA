package it.tndigitale.a4g.richiestamodificasuolo.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.AnomaliaValidazioneModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.LavorazioneSuoloModel;
import it.tndigitale.a4g.richiestamodificasuolo.business.persistence.entity.TipoAnomaliaValidazione;

@Repository
public interface AnomaliaValidazioneDao extends JpaRepository<AnomaliaValidazioneModel, Long> {

	List<AnomaliaValidazioneModel> findByLavorazioneSuoloInAnomaliaValidazioneAndTipoAnomalia(LavorazioneSuoloModel lavorazione, TipoAnomaliaValidazione tipoAnomalia);

	@Procedure("A4SP_VALIDITA_ORACLE.insertFixedWorkspaceAnomalies")
	void insertFixedWorkspaceAnomalies(@Param("pid") Long id, @Param("tipoAnomalia") String tipoAnomalia, @Param("dettaglioAnomalia") String dettaglioAnomalia,
			@Param("ptolleranza") Double tolleranza);

	@Procedure("A4SP_VALIDITA_ORACLE.insertNotFixedWorkspaceErrors")
	void insertNotFixedWorkspaceErrors(@Param("pid") Long id, @Param("tipoAnomalia") String tipoAnomalia, @Param("dettaglioAnomalia") String dettaglioAnomalia,
			@Param("ptolleranza") Double tolleranza);

	@Transactional
	@Modifying
	@Query("delete AnomaliaValidazioneModel WHERE lavorazioneSuoloInAnomaliaValidazione = :lavorazione")
	void deleteAnomalieLavorazione(@Param("lavorazione") LavorazioneSuoloModel lavorazione);

}