package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaFisicaModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

@Repository
public interface PersonaFisicaDao extends JpaRepository<PersonaFisicaModel, EntitaDominioFascicoloId> {

	Optional<PersonaFisicaModel> findByCodiceFiscaleAndIdValidazione(String codiceFiscale, Integer idValidazione) throws NoSuchElementException;
}
