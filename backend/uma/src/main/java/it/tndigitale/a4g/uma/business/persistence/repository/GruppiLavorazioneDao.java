package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.AmbitoLavorazione;
import it.tndigitale.a4g.uma.business.persistence.entity.GruppoLavorazioneModel;

@Repository
public interface GruppiLavorazioneDao extends JpaRepository<GruppoLavorazioneModel, Long>  {
	public List<GruppoLavorazioneModel> findByAmbitoLavorazione(AmbitoLavorazione ambito);
}
