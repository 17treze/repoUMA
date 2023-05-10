package it.tndigitale.a4g.uma.business.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.uma.business.persistence.entity.ColturaGruppiModel;

@Repository
public interface ColturaGruppiDao extends JpaRepository<ColturaGruppiModel, Long> {
	
	@Query(value = "SELECT * FROM A4GR_COLTURA_GRUPPI " + "WHERE CODICE_SUOLO = :codiceSuolo"
			+ " AND CODICE_DEST_USO = :codiceDestUso" + " AND CODICE_USO = :codiceUso"
			+ " AND CODICE_QUALITA = :codiceQualita" + " AND CODICE_VARIETA = :codiceVarieta"
			+ " AND :anno >= ANNO_INIZIO" + " AND (ANNO_FINE IS NULL OR :anno <= ANNO_FINE)", nativeQuery = true)
	public ColturaGruppiModel findByCodificaAndAnno(String codiceSuolo, String codiceDestUso, String codiceUso,
			String codiceQualita, String codiceVarieta, Integer anno);
	
	@Query(value = "SELECT * FROM A4GR_COLTURA_GRUPPI " + "WHERE EXTRACT(YEAR FROM SYSDATE) >= ANNO_INIZIO"
			+ " AND (ANNO_FINE IS NULL OR EXTRACT(YEAR FROM SYSDATE) <= ANNO_FINE)", nativeQuery = true)
	public List<ColturaGruppiModel> findAllValid();
	
}
