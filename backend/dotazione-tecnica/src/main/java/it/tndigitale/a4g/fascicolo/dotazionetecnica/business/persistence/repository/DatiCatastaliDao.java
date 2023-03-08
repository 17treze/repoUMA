package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.DatiCatastaliModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

@Repository
public interface DatiCatastaliDao extends JpaRepository<DatiCatastaliModel, EntitaDominioFascicoloId> {
	public Optional<DatiCatastaliModel> findById(Long id);
	
	public Optional<DatiCatastaliModel> deleteByFabbricatoId(Long id);
	
	public Optional<DatiCatastaliModel> deleteByFabbricatoIdAndFabbricato_IdValidazione(Long id, Integer idValidazione);
}
