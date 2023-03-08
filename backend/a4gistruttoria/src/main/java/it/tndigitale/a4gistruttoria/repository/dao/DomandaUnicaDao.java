package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.dto.CuaaDenominazione;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import it.tndigitale.a4gistruttoria.repository.model.StatoDomanda;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DomandaUnicaDao extends JpaRepository<DomandaUnicaModel, Long>, PagingAndSortingRepository<DomandaUnicaModel, Long>, JpaSpecificationExecutor<DomandaUnicaModel> {

	DomandaUnicaModel findByNumeroDomanda(BigDecimal numeroDomanda);

	List<DomandaUnicaModel> findByStato(StatoDomanda statoDomanda);

	@Query("select d from DomandaUnicaModel d where d.campagna = :campagna and d.stato = :statoDomanda")
	List<DomandaUnicaModel> findByCampagnaAndStato(
			@Param("campagna") Integer campagna, @Param("statoDomanda") String statoDomanda);

	@Query("select d from DomandaUnicaModel d "
			+ "where d.campagna = :campagna and d.stato = :statoDomanda and "
			+ "d.cuaaIntestatario not in "
			+ "( select pa.cuaa from A4gtPiccoloAgricoltore pa "
			+ "where pa.annoInizio <= d.campagna "
			+ "and (pa.annoFine is null or d.campagna <= pa.annoFine)"
			+ ")")
	List<DomandaUnicaModel> findByCampagnaAndStatoNotPiccoliAgricoltori(
			@Param("campagna") Integer campagna,
			@Param("statoDomanda") StatoDomanda statoDomanda);

	@Query(value = "select d from DomandaUnicaModel d where d.campagna = :campagna and d.cuaaIntestatario = :cuaaIntestatario")
	DomandaUnicaModel findByCuaaIntestatarioAndCampagna(@Param("cuaaIntestatario") String cuaaIntestatario, @Param("campagna") Integer campagna);

	@Query(value = "select new it.tndigitale.a4gistruttoria.dto.CuaaDenominazione(d.cuaaIntestatario, d.ragioneSociale) from DomandaUnicaModel d where d.campagna = :campagna  ")
	List<CuaaDenominazione> findCuaaDomandaUnicaByCampagna(@Param("campagna") Integer campagna);

	@EntityGraph(value = "istruttorie.eager")
	List<DomandaUnicaModel> findByCampagna(Integer annoCampagna);

	@Query(value = "select distinct d.stato from DomandaUnicaModel d")
	List<StatoDomanda> getListaStatiDomande();

	@Query(value = "select distinct d.campagna from DomandaUnicaModel d")
	List<Integer> getListaAnniCampagne();
}
