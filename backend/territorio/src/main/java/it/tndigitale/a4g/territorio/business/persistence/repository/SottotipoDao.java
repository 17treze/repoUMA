package it.tndigitale.a4g.territorio.business.persistence.repository;

import it.tndigitale.a4g.framework.repository.model.EntitaDominio;
import it.tndigitale.a4g.territorio.business.persistence.entity.SottotipoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SottotipoDao extends JpaRepository<SottotipoModel, EntitaDominio> {
	Optional<SottotipoModel> findById(Long id);
	List<SottotipoModel> findByAmbito(String ambito);
}
