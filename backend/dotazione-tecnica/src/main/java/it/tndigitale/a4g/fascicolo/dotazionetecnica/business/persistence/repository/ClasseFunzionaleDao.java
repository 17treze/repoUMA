package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.ClasseFunzionaleModel;

@Repository
public interface ClasseFunzionaleDao extends JpaRepository<ClasseFunzionaleModel, Long> {
	public List<ClasseFunzionaleModel> findByDescrizione(String descrizione);

	public Optional<ClasseFunzionaleModel> findById(Long id);
}
