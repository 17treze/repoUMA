package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaConCaricaModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

@Repository
public interface PersonaFisicaConCaricaDao extends JpaRepository<PersonaFisicaConCaricaModel, EntitaDominioFascicoloId> {

	Optional<PersonaFisicaConCaricaModel> findByCodiceFiscaleAndIdValidazione(String codiceFiscale, Integer idValidazione);
	
	Optional<PersonaFisicaConCaricaModel> findByIdAndIdValidazione(Long id, Integer idValidazione);
	
}
