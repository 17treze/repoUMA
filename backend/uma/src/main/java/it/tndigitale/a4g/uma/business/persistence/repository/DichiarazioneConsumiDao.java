package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.DichiarazioneConsumiModel;
import it.tndigitale.a4g.uma.business.persistence.entity.StatoDichiarazioneConsumi;
import it.tndigitale.a4g.uma.dto.DomandaUmaDto;

@Repository
public interface DichiarazioneConsumiDao extends JpaRepository<DichiarazioneConsumiModel, Long>, JpaSpecificationExecutor<DichiarazioneConsumiModel> {

	public Optional<DichiarazioneConsumiModel> findByRichiestaCarburante_cuaaAndRichiestaCarburante_campagna(String cuaa, long campagna);

	@Query("select new it.tndigitale.a4g.uma.dto.DomandaUmaDto(c.id , r.campagna , c.stato  , r.cuaa , r.denominazione , c.protocollo, c.dataPresentazione, c.dataProtocollazione, c.entePresentatore) from DichiarazioneConsumiModel c join c.richiestaCarburante as r where c.richiestaCarburante = r.id and r.campagna = :campagna and r.cuaa in :cuaaList ")
	public List<DomandaUmaDto> findByAbilitazioniAndCampagna(@Param("cuaaList") List<String> cuaaList,@Param("campagna") Long campagna);

	@Query("select new it.tndigitale.a4g.uma.dto.DomandaUmaDto(c.id , r.campagna , c.stato  , r.cuaa , r.denominazione , c.protocollo, c.dataPresentazione, c.dataProtocollazione, c.entePresentatore) from DichiarazioneConsumiModel c join c.richiestaCarburante as r where c.richiestaCarburante = r.id and r.campagna = :campagna ")
	public List<DomandaUmaDto> findDomandaByCampagna(@Param("campagna") Long campagna);
	
	public List<DichiarazioneConsumiModel> findByRichiestaCarburante_campagnaAndStato(Long campagna, StatoDichiarazioneConsumi stato);
}
