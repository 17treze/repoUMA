package it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.fascicolo.dotazionetecnica.business.persistence.entity.FascicoloModel;
import it.tndigitale.a4g.fascicolo.dotazionetecnica.dto.FabbricatiConParticelleInvalide;
import it.tndigitale.a4g.framework.ext.validazione.fascicolo.EntitaDominioFascicoloId;

@Repository
public interface FascicoloDao extends JpaRepository<FascicoloModel, EntitaDominioFascicoloId> {

	public Optional<FascicoloModel> findByCuaaAndIdValidazione(String cuaa, Integer idValidazione);

	@Query(value = "SELECT DISTINCT t.descrizione AS tipologia, f.comune, f.superficie\n"
			+ "FROM a4gt_fabbricato f\n"
			+ "INNER JOIN a4gd_sottotipo s\n"
			+ "ON f.id_sottotipo = s.ID\n"
			+ "INNER JOIN a4gd_tipologia t\n"
			+ "ON s.id_tipologia = t.ID\n"
			+ "INNER JOIN a4gt_dati_catastali dc\n"
			+ "ON f.ID = dc.fabbricato_id\n"
			+ "INNER JOIN a4gt_fascicolo fas\n"
			+ "ON fas.ID = f.id_fascicolo\n"
			+ "WHERE dc.esito = 'INVALIDA'\n"
			+ "AND fas.cuaa = :cuaa\n"
			+ "AND f.id_validazione = 0\n"
			+ "AND fas.id_validazione = 0", nativeQuery = true)
	List<FabbricatiConParticelleInvalide> getFabbricatiConParticelleInvalidePerCuaaSql(@Param("cuaa")String cuaa);

}
