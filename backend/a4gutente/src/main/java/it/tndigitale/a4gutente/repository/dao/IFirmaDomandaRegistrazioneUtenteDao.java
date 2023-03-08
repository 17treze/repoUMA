package it.tndigitale.a4gutente.repository.dao;

import it.tndigitale.a4gutente.repository.model.FirmaDomandaRegistrazioneUtente;

public interface IFirmaDomandaRegistrazioneUtenteDao extends IEntitaDominioRepository<FirmaDomandaRegistrazioneUtente> {
	
	public FirmaDomandaRegistrazioneUtente findOneByIdDomanda(Long idDomanda);
}
