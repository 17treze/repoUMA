package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.StatoFascicoloEnum;

@Repository
public interface FascicoloDao extends JpaRepository<FascicoloModel, EntitaDominioFascicoloId>, JpaSpecificationExecutor<FascicoloModel> {

	Optional<FascicoloModel> findByCuaaAndIdValidazione(String cuaa, int idValidazione);

	@Query("SELECT max(obj.dataValidazione) FROM FascicoloModel obj where obj.cuaa=:cuaa")
	Optional<LocalDate> getDataUltimaValidazione(String cuaa);
	
	boolean existsByCuaaAndIdValidazione(String cuaa, int idValidazione);
	
	@Query("SELECT CASE WHEN MAX(id) IS NULL THEN 'NO' ELSE 'YES' END\n"
			+ " FROM FascicoloModel obj\n"
			+ " where obj.cuaa=:cuaa and obj.idValidazione=:idValidazione and obj.stato!='CHIUSO'")
	boolean existsByCuaaAndIdValidazioneAndStatoChiuso(String cuaa, int idValidazione);
	
	Optional<FascicoloModel> findByDetenzioni(EntitaDominioFascicoloId idMandato);
	
	@Query("SELECT coalesce(max(obj.idValidazione), 0) + 1 FROM FascicoloModel obj where obj.cuaa=:cuaa")
	Integer getNextIdValidazione(@Param("cuaa") String cuaa);
	
	@Query(value = "SELECT NXTPDFID.nextval from dual", nativeQuery = true)
    Long getNextPdfId();

    @Query("select a from FascicoloModel a where a.idValidazione = 0 "
    		+ "and (select MAX(b.dataValidazione) from FascicoloModel b where a.cuaa = b.cuaa and b.idValidazione > 0) < :minDataValidazione "
    		+ "and a.stato != 'SOSPESO' and a.stato != 'CHIUSO' and a.stato != 'DORMIENTE' "
    		+ "and a.dataApertura < :minDataValidazione")
    List<FascicoloModel> findTheSleepers(
      @Param("minDataValidazione") LocalDate minDataValidazione);
    
    List<FascicoloModel> findByStato(StatoFascicoloEnum statoFascicoloEnum);
}
