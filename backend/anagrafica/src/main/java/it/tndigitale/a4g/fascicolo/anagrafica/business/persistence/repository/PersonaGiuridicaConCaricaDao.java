package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaGiuridicaConCaricaModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

@Repository
public interface PersonaGiuridicaConCaricaDao extends JpaRepository<PersonaGiuridicaConCaricaModel, EntitaDominioFascicoloId> {

	List<PersonaGiuridicaConCaricaModel> findByCodiceFiscaleAndIdValidazione(String codiceFiscale, Integer idValidazione);
	
}
