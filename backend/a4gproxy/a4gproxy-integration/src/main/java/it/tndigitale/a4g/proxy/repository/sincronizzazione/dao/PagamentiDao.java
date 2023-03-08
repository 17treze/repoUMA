package it.tndigitale.a4g.proxy.repository.sincronizzazione.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.Pagamenti;

@Repository
public interface PagamentiDao extends JpaRepository<Pagamenti, Long> {

	@Transactional
	@Modifying
	@Query("UPDATE Pagamenti t SET t.dataFineVali = current_date(), t.decoStat = 93 WHERE t.numeCampRife = :numeCampRife AND t.fonte = 'A4G'")
	void deleteByNumeCamp(Long numeCampRife);
	
	// Verifica se ci sia stata una modifica manuale. Se true non si può procedere con l'inserimento. Se false si può procedere.
	@Transactional
	@Query("select case when count(t)> 0 then true else false end from Pagamenti t WHERE t.codiFisc = :cuaa AND t.numeCampRife = :numeCampRife AND t.fonte <> 'A4G'")
	boolean verificaPreliminareInserimentoPagamento(String cuaa, Long numeCampRife);
}