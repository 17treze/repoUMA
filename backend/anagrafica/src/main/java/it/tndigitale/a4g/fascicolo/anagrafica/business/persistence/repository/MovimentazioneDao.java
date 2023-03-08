package it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.MovimentazioneModel;
import it.tndigitale.a4g.fascicolo.anagrafica.business.persistence.entity.TipoMovimentazioneFascicolo;
import it.tndigitale.a4g.framework.repository.model.EntitaDominio;

@Repository
public interface MovimentazioneDao extends JpaRepository<MovimentazioneModel, EntitaDominio> {

	@Query("select a from MovimentazioneModel a where a.fascicolo.cuaa = :cuaa and a.dataFine IS NULL and a.tipo = :tipo")
	Optional<MovimentazioneModel> findByFascicoloAndDataFineIsNullAndTipo(@Param("cuaa") String cuaa, @Param("tipo") TipoMovimentazioneFascicolo tipo);
	
	@Query("select a from MovimentazioneModel a where a.fascicolo.cuaa = :cuaa and a.tipo = :tipo")
	Optional<List<MovimentazioneModel>> findByFascicoloAndTipo(@Param("cuaa") String cuaa, @Param("tipo") TipoMovimentazioneFascicolo tipo);
}
