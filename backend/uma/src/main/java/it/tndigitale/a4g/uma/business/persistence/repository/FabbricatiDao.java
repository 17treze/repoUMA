package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.FabbricatoModel;
import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;

@Repository
public interface FabbricatiDao extends JpaRepository<FabbricatoModel, Long> {

	List<FabbricatoModel> findByRichiestaCarburante_id(long idRichiesta);
	public void deleteByRichiestaCarburante(RichiestaCarburanteModel richiesta);
}
