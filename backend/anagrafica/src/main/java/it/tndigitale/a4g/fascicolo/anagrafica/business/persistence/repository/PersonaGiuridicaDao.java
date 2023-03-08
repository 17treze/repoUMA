package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

@Repository
public interface PersonaGiuridicaDao extends JpaRepository<PersonaGiuridicaModel, EntitaDominioFascicoloId> {

	Optional<PersonaGiuridicaModel> findByCodiceFiscaleAndIdValidazione(String codiceFiscale, Integer idValidazione);
}

