package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.ModoPagamentoModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

@Repository
public interface ModoPagamentoDao extends JpaRepository<ModoPagamentoModel, EntitaDominioFascicoloId>, JpaSpecificationExecutor<ModoPagamentoModel> {
//	@Query( value = "SELECT count(P.ID) "
//	 		+ "FROM A4GT_MODO_PAGAMENTO P "
//	 		+ "JOIN A4GT_FASCICOLO F ON P.ID_FASCICOLO = F.ID "
//	 		+ "WHERE P.IBAN = :iban AND F.CUAA = :cuaa", nativeQuery = true)
//	Long countByCuaaAndIban(@Param("cuaa") String cuaa, @Param("iban") String iban);
	
	Long countByFascicolo_CuaaAndIbanAndIdValidazione(final String cuaa, final String iban, final Integer idValidazione);
	Optional<ModoPagamentoModel> findByIdAndIdValidazione(final Long id, final Integer idValidazione);
	List<ModoPagamentoModel> findByFascicolo_CuaaAndIdValidazione(final String cuaa, final Integer idValidazione);
	
	@Query("SELECT coalesce(max(fm.idValidazione), 0) + 1 FROM ModoPagamentoModel fm where fm.id=:id")
	Integer getNextIdValidazione(Long id);
}
