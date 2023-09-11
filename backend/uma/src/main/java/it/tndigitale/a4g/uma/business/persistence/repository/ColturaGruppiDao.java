package it.tndigitale.a4g.uma.business.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.ColturaGruppiModel;

@Repository
public interface ColturaGruppiDao extends
		JpaRepository<ColturaGruppiModel, Long>,
		JpaSpecificationExecutor<ColturaGruppiModel> {
	
	@Query(value = "SELECT * FROM TAB_AGRI_UMAL_COLTURA_GRUPPI " + "WHERE CODICE_SUOLO = :codiceSuolo"
			+ " AND CODICE_DEST_USO = :codiceDestUso" + " AND CODICE_USO = :codiceUso"
			+ " AND CODICE_QUALITA = :codiceQualita" + " AND CODICE_VARIETA = :codiceVarieta"
			+ " AND :anno >= ANNO_INIZIO" + " AND (ANNO_FINE IS NULL OR :anno <= ANNO_FINE)", nativeQuery = true)
	public ColturaGruppiModel findByCodificaAndAnno(String codiceSuolo, String codiceDestUso, String codiceUso,
			String codiceQualita, String codiceVarieta, Integer anno);
	
	@Query(value = "SELECT * FROM TAB_AGRI_UMAL_COLTURA_GRUPPI " + "WHERE EXTRACT(YEAR FROM SYSDATE) >= ANNO_INIZIO"
			+ " AND (ANNO_FINE IS NULL OR EXTRACT(YEAR FROM SYSDATE) <= ANNO_FINE)", nativeQuery = true)
	public Page<ColturaGruppiModel> findAllValid(Pageable pageable);
	
}
