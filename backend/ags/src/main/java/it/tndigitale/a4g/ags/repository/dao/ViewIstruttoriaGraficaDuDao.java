package it.tndigitale.a4g.ags.repository.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.ags.entity.ViewIstruttoriaGraficaDuEntity;

@Repository
public interface ViewIstruttoriaGraficaDuDao extends JpaRepository<ViewIstruttoriaGraficaDuEntity, Long>, JpaSpecificationExecutor<ViewIstruttoriaGraficaDuEntity> {

	public Optional<ViewIstruttoriaGraficaDuEntity> findByCuaaAndIdDomanda(final String cuaa, final Long idDomanda);
	
}
