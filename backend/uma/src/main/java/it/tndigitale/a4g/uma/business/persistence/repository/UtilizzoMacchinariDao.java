package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.UtilizzoMacchinariModel;

@Repository
public interface UtilizzoMacchinariDao extends JpaRepository<UtilizzoMacchinariModel, Long> {

	public List<UtilizzoMacchinariModel> findByRichiestaCarburante(RichiestaCarburanteModel richiestaCarburante);
	public void deleteByRichiestaCarburante(RichiestaCarburanteModel richiesta);
}
