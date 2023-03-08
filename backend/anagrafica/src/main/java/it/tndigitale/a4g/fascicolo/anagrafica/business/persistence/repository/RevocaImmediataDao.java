package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MandatoModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.RevocaImmediataModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.StatoRevocaImmediata;

@Repository
public interface RevocaImmediataDao extends JpaRepository<RevocaImmediataModel, Long>, JpaSpecificationExecutor<RevocaImmediataModel> {
	
	List<RevocaImmediataModel> findByCodiceFiscale(String codiceFiscale);
	
	Optional<RevocaImmediataModel> findByIdProtocollo(String idProtocollo);
	
	List<RevocaImmediataModel> findByMandato(MandatoModel mandatoModel);
	
	List<RevocaImmediataModel> findByStatoAndMandatoSportelloIdentificativoIn(StatoRevocaImmediata stato, List<Long> identificativiSportello, Pageable paginazione);
	
	List<RevocaImmediataModel> findByStatoNotAndMandatoSportelloIdentificativoIn(StatoRevocaImmediata stato, List<Long> identificativiSportello, Pageable paginazione);
	
	long countByStatoAndMandatoSportelloIdentificativoIn(StatoRevocaImmediata stato, List<Long> identificativiSportello);
	
	long countByStatoNotAndMandatoSportelloIdentificativoIn(StatoRevocaImmediata stato, List<Long> identificativiSportello);
	
	List<RevocaImmediataModel> findByStato(StatoRevocaImmediata stato, Pageable paginazione);
	
	List<RevocaImmediataModel> findByStatoNot(StatoRevocaImmediata stato, Pageable paginazione);
	
	long countByStato(StatoRevocaImmediata stato);
	
	long countByStatoNot(StatoRevocaImmediata stato);
}
