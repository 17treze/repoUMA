package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.DocumentoIdentitaModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

@Repository
public interface DocumentoIdentitaDao extends JpaRepository<DocumentoIdentitaModel, EntitaDominioFascicoloId> {
	
	Optional<DocumentoIdentitaModel> findByIdAndIdValidazione(final Long id, final Integer idValidazione);
	Optional<DocumentoIdentitaModel> findByFascicolo_CuaaAndFascicolo_IdValidazione(final String cuaa, final Integer idValidazione);
	
	@Query("SELECT coalesce(max(fm.idValidazione), 0) + 1 FROM DocumentoIdentitaModel fm where fm.id=:id")
	Integer getNextIdValidazione(Long id);
}
