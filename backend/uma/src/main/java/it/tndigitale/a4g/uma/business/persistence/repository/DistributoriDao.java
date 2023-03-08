package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.DistributoreModel;

@Repository
public interface DistributoriDao extends JpaRepository<DistributoreModel, Long> {

	public Optional<DistributoreModel> findByIdentificativo(Long identificativo);
	public List<DistributoreModel> findByIdentificativoIn(List<Long> identificativi);
}
