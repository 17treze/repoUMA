package it.tndigitale.a4g.proxy.repository.sincronizzazione.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.AnomDuGrafSettRicPre;

@Repository
public interface ControlloAnomalieCoordinamentoDao extends JpaRepository<AnomDuGrafSettRicPre, Long> {

	@Query(value = "select count(*) from anom_du_graf_sett_ric_pre where nume_anno_camp = :annoCampagna and id_parc_op = :idParcella and (sysdate between data_iniz_vali and data_fine_vali or data_fine_vali is null) and codi_tipo_anom = 'N21' and valo_anom = '01'", nativeQuery = true)
	public Long findByAnnoCampagnaAndIdParcella(@Param("annoCampagna") Long annoCampagna, @Param("idParcella") Long idParcella);
}
