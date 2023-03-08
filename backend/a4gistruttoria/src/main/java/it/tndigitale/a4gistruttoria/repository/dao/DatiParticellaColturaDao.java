package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.repository.model.A4gtDatiParticellaColtura;
import it.tndigitale.a4gistruttoria.repository.model.IstruttoriaModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatiParticellaColturaDao extends JpaRepository<A4gtDatiParticellaColtura, Long> {

	List<A4gtDatiParticellaColtura> findByIstruttoria(IstruttoriaModel istruttoria);

	@Query(value ="select * from ("
			+" select A.ID ID,"
			+" Codice_Coltura_3 codiceColtura3,"
			+" JSON_VALUE(a.info_Catastali, '$.idParticella') idParticella,"
			+" JSON_VALUE(a.info_Catastali, '$.comune') comune,"
			+" JSON_VALUE(a.info_Catastali, '$.codNazionale') codNazionale,"
			+" JSON_VALUE(a.info_Catastali, '$.foglio') foglio,"
			+" JSON_VALUE(a.info_Catastali, '$.particella') particella,"
			+" JSON_VALUE(a.info_Catastali, '$.sub') sub,"
			+" NVL((CASE WHEN JSON_VALUE(a.dati_particella, '$.superficieImpegnata') LIKE '%E%'"
			+" THEN TO_NUMBER(JSON_VALUE(a.dati_particella, '$.superficieImpegnata'), '9999999.999999999999999999EEEE', 'NLS_NUMERIC_CHARACTERS=''. ''')"
			+" ELSE TO_NUMBER(JSON_VALUE(a.dati_particella, '$.superficieImpegnata'), '9999999.999999999999999999',     'NLS_NUMERIC_CHARACTERS=''. ''')"
			+" END),0) AS superficieImpegnata,"
			+" NVL((CASE WHEN JSON_VALUE(a.dati_particella, '$.superficieEleggibile') LIKE '%E%'"
			+" THEN TO_NUMBER(JSON_VALUE(a.dati_particella, '$.superficieEleggibile'), '9999999.999999999999999999EEEE', 'NLS_NUMERIC_CHARACTERS=''. ''')"
			+" ELSE TO_NUMBER(JSON_VALUE(a.dati_particella, '$.superficieEleggibile'), '9999999.999999999999999999',     'NLS_NUMERIC_CHARACTERS=''. ''')"
			+" END),0) AS superficieEleggibile,"
			+" NVL((CASE WHEN JSON_VALUE(a.dati_particella, '$.superficieSigeco') LIKE '%E%'"
			+" THEN TO_NUMBER(JSON_VALUE(a.dati_particella, '$.superficieSigeco'), '9999999.999999999999999999EEEE', 'NLS_NUMERIC_CHARACTERS=''. ''')"
			+" ELSE TO_NUMBER(JSON_VALUE(a.dati_particella, '$.superficieSigeco'), '9999999.999999999999999999',     'NLS_NUMERIC_CHARACTERS=''. ''')"
			+" END),0) AS superficieSigeco,"
			+" NVL(JSON_VALUE(a.dati_particella, '$.anomalieMantenimento'),'false') anomalieMantenimento,"
			+" NVL(JSON_VALUE(a.dati_particella, '$.anomalieCoordinamento'),'false') anomalieCoordinamento,"
			+" NVL((CASE WHEN JSON_VALUE(a.dati_particella, '$.superficieDeterminata') LIKE '%E%'"
			+" THEN TO_NUMBER(JSON_VALUE(a.dati_particella, '$.superficieDeterminata'), '9999999.999999999999999999EEEE', 'NLS_NUMERIC_CHARACTERS=''. ''')"
			+" ELSE TO_NUMBER(JSON_VALUE(a.dati_particella, '$.superficieDeterminata'), '9999999.999999999999999999',     'NLS_NUMERIC_CHARACTERS=''. ''')"
			+" END),0) AS superficieDeterminata,"
			+" JSON_VALUE(a.dati_particella, '$.tipoColtura') tipoColtura,"
			+" JSON_VALUE(a.dati_particella, '$.tipoSeminativo') tipoSeminativo,"
			+" JSON_VALUE(a.dati_particella, '$.colturaPrincipale') colturaPrincipale,"
			+" JSON_VALUE(a.dati_particella, '$.pascolo') pascolo,"
			+" JSON_VALUE(a.dati_particella, '$.secondaColtura') secondaColtura,"
			+" NVL((CASE WHEN JSON_VALUE(a.dati_particella, '$.superficieAnomalieCoordinamento') LIKE '%E%'"
			+" THEN TO_NUMBER(JSON_VALUE(a.dati_particella, '$.superficieAnomalieCoordinamento'), '9999999.999999999999999999EEEE', 'NLS_NUMERIC_CHARACTERS=''. ''')"
			+" ELSE TO_NUMBER(JSON_VALUE(a.dati_particella, '$.superficieAnomalieCoordinamento'), '9999999.999999999999999999',     'NLS_NUMERIC_CHARACTERS=''. ''')"
			+" END),0) AS supAnCoord,"
			+" NVL((CASE WHEN JSON_VALUE(a.dati_particella, '$.superficieImpegnata') LIKE '%E%'"
			+" THEN TO_NUMBER(JSON_VALUE(a.dati_particella, '$.superficieImpegnata'), '9999999.999999999999999999EEEE', 'NLS_NUMERIC_CHARACTERS=''. ''')"
			+" ELSE TO_NUMBER(JSON_VALUE(a.dati_particella, '$.superficieImpegnata'), '9999999.999999999999999999',     'NLS_NUMERIC_CHARACTERS=''. ''')"
			+" END),0) - NVL((CASE WHEN JSON_VALUE(a.dati_particella, '$.superficieDeterminata') LIKE '%E%'"
			+" THEN TO_NUMBER(JSON_VALUE(a.dati_particella, '$.superficieDeterminata'), '9999999.999999999999999999EEEE', 'NLS_NUMERIC_CHARACTERS=''. ''')"
			+" ELSE TO_NUMBER(JSON_VALUE(a.dati_particella, '$.superficieDeterminata'), '9999999.999999999999999999',     'NLS_NUMERIC_CHARACTERS=''. ''')"
			+" END),0) AS superficieScostamento,"
			+" JSON_VALUE(a.dati_particella, '$.azotoFissatrice') azotoFissatrice"
			+" from A4gt_Dati_Particella_Coltura a"
			+" WHERE a.id_istruttoria = :idIstruttoria"
			+" and (:pascolo IS NULL or  (:pascolo = 'true' AND JSON_VALUE(a.dati_particella, '$.pascolo') IS NOT NULL) or (:pascolo = 'false' AND JSON_VALUE(a.dati_particella, '$.pascolo') IS NULL) )"
			+") a", nativeQuery = true)
	List<Object[]> getDettaglioParticellaByIdIstruttoriaPaginata(@Param("idIstruttoria") Long idIstruttoria, @Param("pascolo") String pascolo, Pageable paginazione);

	@Query(value ="select count(*) from A4gt_Dati_Particella_Coltura a WHERE a.id_istruttoria = :idIstruttoria and (:pascolo IS NULL or (:pascolo = 'true' AND JSON_VALUE(a.dati_particella, '$.pascolo') IS NOT NULL) or (:pascolo = 'false' AND JSON_VALUE(a.dati_particella, '$.pascolo') IS NULL)  )",
			nativeQuery = true)
	Long countFindByIdIstruttoria(@Param("idIstruttoria") Long idIstruttoria, @Param("pascolo") String pascolo);

}
