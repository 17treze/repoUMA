package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;
import it.tndigitale.a4g.uma.business.persistence.entity.GruppoLavorazioneModel;

@Repository
public interface GruppiLavorazioneDao extends JpaRepository<GruppoLavorazioneModel, Long> {
	public List<GruppoLavorazioneModel> findByAmbitoLavorazione(AmbitoLavorazione ambito);
	
	@Query(value = "SELECT * FROM TAB_AGRI_UMAL_GRUPPI_LAVORAZIONE WHERE EXTRACT(YEAR FROM SYSDATE) >= ANNO_INIZIO"
			+ " AND (ANNO_FINE IS NULL OR EXTRACT(YEAR FROM SYSDATE) <= ANNO_FINE)", nativeQuery = true)
	public Page<GruppoLavorazioneModel> findAllValid(Pageable pageable);
}
