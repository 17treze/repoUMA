package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.PersonaModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

public interface PersonaDao extends JpaRepository<PersonaModel, EntitaDominioFascicoloId> {
	@Query("SELECT coalesce(max(obj.idValidazione), 0) + 1 FROM PersonaModel obj where obj.codiceFiscale=:cuaa")
	Integer getNextIdValidazione(String cuaa);
}
