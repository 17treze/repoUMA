package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.CentroAssistenzaAgricolaModel;

@Repository
public interface CentroAssistenzaAgricolaDao extends JpaRepository<CentroAssistenzaAgricolaModel, Long> {
	
	public Optional<CentroAssistenzaAgricolaModel> findById(@Param("id")Long id);
	
}
