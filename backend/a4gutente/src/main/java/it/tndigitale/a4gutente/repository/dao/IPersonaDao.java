package it.tndigitale.a4gutente.repository.dao;

import java.util.List;

import it.tndigitale.a4gutente.repository.model.PersonaEntita;

public interface IPersonaDao extends IEntitaDominioRepository<PersonaEntita> {

	List<PersonaEntita> findByCodiceFiscale(String codiceFiscale);

	PersonaEntita findOneByCodiceFiscale(String codiceFiscale);

}
