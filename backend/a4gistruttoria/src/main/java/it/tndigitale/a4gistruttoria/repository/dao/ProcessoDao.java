package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.A4gtProcesso;
import it.tndigitale.a4gistruttoria.repository.model.StatoProcesso;
import it.tndigitale.a4gistruttoria.repository.model.TipoProcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessoDao extends JpaRepository<A4gtProcesso, Long> {

	@Query("select count(prc.id) from A4gtProcesso prc " +
			"where prc.tipo = :tipo and prc.stato = :stato")
	Long countByProcessoInEsecuzione(
			@Param("tipo") TipoProcesso tipo,
			@Param("stato") StatoProcesso stato);

	@Query("select prc from A4gtProcesso prc " +
			"where prc.stato = :stato")
	List<A4gtProcesso> findProcessiByStato(@Param("stato") StatoProcesso stato);

	@Query("select prc from A4gtProcesso prc " +
			"where prc.datiElaborazione LIKE CONCAT('%idDomandaAntimafia%',:idAntimafia,',%') " +
			"and prc.tipo = :tipo and prc.stato = :stato")
	A4gtProcesso getProcessoByIdAntimafia(
			@Param("idAntimafia") Long idAntimafia,
			@Param("tipo") TipoProcesso tipo,
			@Param("stato") StatoProcesso stato);
}
