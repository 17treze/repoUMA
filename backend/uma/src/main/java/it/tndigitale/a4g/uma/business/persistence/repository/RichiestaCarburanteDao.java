package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.RichiestaCarburanteModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoRichiestaCarburante;
import it.tndigitale.a4g.uma.dto.DomandaUmaDto;

@Repository
public interface RichiestaCarburanteDao extends JpaRepository<RichiestaCarburanteModel, Long>, JpaSpecificationExecutor<RichiestaCarburanteModel>, PagingAndSortingRepository<RichiestaCarburanteModel, Long> {

	Optional<RichiestaCarburanteModel> findByCuaaAndCampagnaAndStato(String cuaa, Long campagna, StatoRichiestaCarburante stato);

	List<RichiestaCarburanteModel> findByCuaaAndCampagna(String cuaa, Long campagna);

	@Query("select new it.tndigitale.a4g.uma.dto.DomandaUmaDto(r.id , r.campagna , r.stato  , r.cuaa , r.denominazione , r.protocollo, r.dataPresentazione, r.dataProtocollazione, r.entePresentatore) from RichiestaCarburanteModel r where r.campagna = :campagna and r.cuaa in :cuaaList ")
	public List<DomandaUmaDto> findByAbilitazioniAndCampagna(@Param("cuaaList") List<String> cuaaList,@Param("campagna") Long campagna);

	@Query("select new it.tndigitale.a4g.uma.dto.DomandaUmaDto(r.id , r.campagna , r.stato  , r.cuaa , r.denominazione , r.protocollo, r.dataPresentazione, r.dataProtocollazione, r.entePresentatore) from RichiestaCarburanteModel r where r.campagna = :campagna ")
	public List<DomandaUmaDto> findDomandaByCampagna(@Param("campagna") Long campagna);

	@Query("select new it.tndigitale.a4g.uma.dto.DomandaUmaDto(r.campagna, r.cuaa, r.dataPresentazione) from RichiestaCarburanteModel r where r.campagna = :campagna ")
	public List<DomandaUmaDto> findStrictByCampagna(@Param("campagna") Long campagna);

	@Query("select new it.tndigitale.a4g.uma.dto.DomandaUmaDto(r.id, r.cuaa , r.denominazione, r.entePresentatore) "
			+ "from RichiestaCarburanteModel r left join r.dichiarazioneConsumi as c "
			+ "where r.campagna = :campagna "
			+ "and r.stato = 'AUTORIZZATA'"
			+ "and (c.stato is null or c.stato = 'IN_COMPILAZIONE')")
	public List<DomandaUmaDto> findRichiesteInadempienti(@Param("campagna") Long campagna);

}
