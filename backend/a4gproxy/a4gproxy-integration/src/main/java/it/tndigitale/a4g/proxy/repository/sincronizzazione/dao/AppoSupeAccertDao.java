package it.tndigitale.a4g.proxy.repository.sincronizzazione.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AppoSupeAccert;

@Repository
public interface AppoSupeAccertDao extends JpaRepository<AppoSupeAccert, Long> {

	@Transactional
	@Modifying
	@Query("UPDATE AppoSupeAccert t SET t.dataFineVal = current_date() WHERE t.numeCamp = :numeCamp AND t.fonte = 'A4G'")
	void deleteByNumeCamp(Long numeCamp);
	
	// Verifica se ci sia stata una modifica manuale. Se true non si può procedere con l'inserimento. Se false si può procedere.
	@Transactional
	@Query("select case when count(t)> 0 then true else false end from AppoSupeAccert t WHERE t.cuaa = :cuaa AND t.numeCamp = :numeCamp AND t.fonte != 'A4G'")
	boolean verificaPreliminareInserimentoSupAccertate(String cuaa, Long numeCamp);
}