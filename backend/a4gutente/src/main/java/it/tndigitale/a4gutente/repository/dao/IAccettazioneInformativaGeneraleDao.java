package it.tndigitale.a4gutente.repository.dao;

import it.tndigitale.a4gutente.repository.model.AccettazioneInformativaGeneraleEntita;
import it.tndigitale.a4gutente.repository.model.PersonaEntita;

public interface IAccettazioneInformativaGeneraleDao extends IEntitaDominioRepository<AccettazioneInformativaGeneraleEntita> {

	public AccettazioneInformativaGeneraleEntita findByPersona(PersonaEntita persona);
}
