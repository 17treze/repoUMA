package it.tndigitale.a4g.uma.business.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.FabbisognoFabbricatoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.FabbricatoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.LavorazioneModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;

@Repository
public interface FabbisognoFabbricatoDao extends JpaRepository<FabbisognoFabbricatoModel, Long> {
	
	void deleteByLavorazioneModelAndRichiestaCarburanteAndFabbricatoModel(LavorazioneModel lavorazioneModel, RichiestaCarburanteModel richiestaCarburanteModel, FabbricatoModel fabbricatoModel);

}
