package it.tndigitale.a4g.proxy.repository.sincronizzazione.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.proxy.repository.sincronizzazione.model.InviateCcoo2016;

@Repository
public interface ControlloSigecoDao extends JpaRepository<InviateCcoo2016, Long> {

	@Query(value = "select distinct i.codi_esit from inviate_ccoo_2016 i join esito_conv_2016 e on i.id_atto_ammi = e.id_atto_ammi where i.data_fine_vali > sysdate and i.deco_tipo_docu in ('1950') and e.nume_camp = :anno and e.codi_atto_oopr=:numeroDomanda and i.data_iniz_vali = (select max(data_iniz_vali) from INVIATE_CCOO_2016 i1 where i1.ID_DOM_CCOO = i.ID_DOM_CCOO)", nativeQuery = true)
	public Long findByNumeroDomandaAndAnno(@Param("anno") Long anno, @Param("numeroDomanda") String numeroDomanda);
	
	@Query(value = "SELECT FLAG_CONV FROM ESITO_CONV_2016 E WHERE E.CODI_ATTO_OOPR = :numeroDomanda AND E.CODI_FISC = :cuaa AND E.NUME_CAMP = :annoCampagna AND SYSDATE BETWEEN E.DATA_INIZ_VALI AND E.DATA_FINE_VALI ORDER BY E.DATA_INIZ_VALI DESC", nativeQuery = true)
	public List<String> findFlagConvByNumeroDomandaAndAnnoCampagnaAndCuaa(@Param("numeroDomanda") String numeroDomanda, @Param("annoCampagna") Long anno, @Param("cuaa") String cuaa);

}
