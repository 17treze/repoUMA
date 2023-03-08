package it.tndigitale.a4gutente.repository.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gutente.codici.StatoDomandaRegistrazioneUtente;
import it.tndigitale.a4gutente.dto.TipoDomandaRegistrazione;
import it.tndigitale.a4gutente.repository.model.DomandaRegistrazioneUtente;

@Repository
public interface IDomandaRegistrazioneUtenteDao extends 
		IEntitaDominioRepository<DomandaRegistrazioneUtente>, 
		JpaSpecificationExecutor<DomandaRegistrazioneUtente>,PersistenzaEntitaDominioRepository<DomandaRegistrazioneUtente> {
	
	List<DomandaRegistrazioneUtente> findByIdentificativoUtente(String identificativoUtente);

	List<DomandaRegistrazioneUtente> findByIdentificativoUtenteAndStato(String identificativoUtente, StatoDomandaRegistrazioneUtente stato);
	
	List<DomandaRegistrazioneUtente> findByIdentificativoUtenteAndTipoDomandaRegistrazione(String identificativoUtente, TipoDomandaRegistrazione tipoDomandaRegistrazione);
	
	List<DomandaRegistrazioneUtente> findByIdentificativoUtenteAndStatoAndTipoDomandaRegistrazione(String identificativoUtente, StatoDomandaRegistrazioneUtente stato, TipoDomandaRegistrazione tipoDomandaRegistrazione);

	List<DomandaRegistrazioneUtente> findByStato(StatoDomandaRegistrazioneUtente stato, Pageable pageable);

}
