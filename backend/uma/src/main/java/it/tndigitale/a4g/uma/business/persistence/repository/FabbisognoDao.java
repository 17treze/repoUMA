package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.FabbisognoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.LavorazioneModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.TipoCarburante;

@Repository
public interface FabbisognoDao extends JpaRepository<FabbisognoModel, Long> {

	public List<FabbisognoModel> findByRichiestaCarburante_id(long idRichiesta);
	public void deleteByLavorazioneModelAndRichiestaCarburante(LavorazioneModel lavorazioneModel, RichiestaCarburanteModel richiestaCarburante);
	public void deleteByRichiestaCarburante(RichiestaCarburanteModel richiesta);
	public void deleteByRichiestaCarburante_idAndCarburanteIn(long idRichiestaCarburante, Set<TipoCarburante> carburanti);
}
