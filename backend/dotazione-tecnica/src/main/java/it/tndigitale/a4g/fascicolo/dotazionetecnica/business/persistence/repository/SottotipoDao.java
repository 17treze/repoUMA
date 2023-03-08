package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.SottotipoModel;

@Repository
public interface SottotipoDao extends JpaRepository<SottotipoModel, Long> {
	public List<SottotipoModel> findByDescrizione(String descrizione);
	public Optional<SottotipoModel> findById(Long id);
}
