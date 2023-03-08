package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.TipologiaModel;

@Repository
public interface TipologiaDao extends JpaRepository<TipologiaModel, Long> {
	public List<TipologiaModel> findByAmbito(AmbitoTipologia ambito);
	public Optional<TipologiaModel> findById(Long id);
}
