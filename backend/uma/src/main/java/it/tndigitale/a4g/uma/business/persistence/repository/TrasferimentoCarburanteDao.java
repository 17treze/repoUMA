package it.tndigitale.a4g.uma.business.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.TrasferimentoCarburanteModel;

@Repository
public interface TrasferimentoCarburanteDao extends JpaRepository<TrasferimentoCarburanteModel, Long> ,  JpaSpecificationExecutor<TrasferimentoCarburanteModel> {

	public boolean existsByRichiestaCarburante(RichiestaCarburanteModel richiesta);
}
