package it.tndigitale.a4gistruttoria.repository.dao;

import java.math.BigDecimal;
import java.util.List;

import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.repository.model.A4gtDatiPascolo;

@Repository
public interface DatiPascoloDao extends JpaRepository<A4gtDatiPascolo, Long> {

	@Query("SELECT a FROM A4gtDatiPascolo a WHERE a.domandaUnicaModel.id = :idDomanda AND a.codicePascolo = :codicePascolo")
	A4gtDatiPascolo findByDomandaAndCodicePascolo(@Param("idDomanda") Long idDomanda, @Param("codicePascolo") String codicePascolo);

	Long countByDomandaUnicaModel(DomandaUnicaModel domanda);

	@Query(value = "select a.codicePascolo from A4gtDatiPascolo a where  (:annoCampagna is null or a.domandaUnicaModel.campagna = :annoCampagna)  and  ( :cuaa is null or a.domandaUnicaModel.cuaaIntestatario = :cuaa)")
	List<String> findDatiPascoloByAnnoAndCUAA(@Param("annoCampagna") Integer annoCampagna, @Param("cuaa") String cuaa);

	List<A4gtDatiPascolo> findByDomandaUnicaModel(DomandaUnicaModel domanda);

}
