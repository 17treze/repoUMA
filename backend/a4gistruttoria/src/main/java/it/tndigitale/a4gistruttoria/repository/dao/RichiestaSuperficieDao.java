package it.tndigitale.a4gistruttoria.repository.dao;

import java.math.BigDecimal;
import java.util.List;

import it.tndigitale.a4gistruttoria.repository.model.CodiceInterventoAgs;
import it.tndigitale.a4gistruttoria.repository.model.DomandaUnicaModel;
import it.tndigitale.a4gistruttoria.repository.model.Sostegno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4gistruttoria.dto.IRichiestaSuperficie;
import it.tndigitale.a4gistruttoria.repository.model.A4gtRichiestaSuperficie;

@Repository
public interface RichiestaSuperficieDao extends JpaRepository<A4gtRichiestaSuperficie, Long> {

	List<A4gtRichiestaSuperficie> findByDomandaUnicaModel(DomandaUnicaModel domanda);

	@Query("SELECT a FROM A4gtRichiestaSuperficie a WHERE a.domandaUnicaModel.id = :idDomanda AND a.intervento.identificativoIntervento = :codiceInterventoAgs")
	List<A4gtRichiestaSuperficie> findByDomandaIntervento(@Param("idDomanda") Long domanda, @Param("codiceInterventoAgs") CodiceInterventoAgs codiceInterventoAgs);
	
	@Query("SELECT a FROM A4gtRichiestaSuperficie a WHERE a.domandaUnicaModel.id = :idDomanda")
	List<A4gtRichiestaSuperficie> findByDomanda(@Param("idDomanda") Long domanda);

	@Query("SELECT a FROM A4gtRichiestaSuperficie a WHERE a.domandaUnicaModel.id = :idDomanda AND a.intervento.identificativoIntervento = :codiceInterventoAgs")
	Page<A4gtRichiestaSuperficie> findByDomandaInterventoPaginata(@Param("idDomanda") Long domanda, @Param("codiceInterventoAgs") CodiceInterventoAgs codiceInterventoAgs, Pageable paginazione);

	@Query(value = "select * from (select Id, Sup_Richiesta supRichiesta, Sup_Richiesta_Netta supRichiestaNetta, id_intervento idIntervento, Id_Domanda idDomanda, Codice_Coltura_3 codiceColtura3, Codice_Coltura_5 codiceColtura5, JSON_VALUE(a.info_Catastali, '$.idParticella') idParticella, "
			+ " JSON_VALUE(a.info_Catastali, '$.comune') comune, JSON_VALUE(a.info_Catastali, '$.codNazionale') codNazionale, JSON_VALUE(a.info_Catastali, '$.foglio') foglio, "
			+ " JSON_VALUE(a.info_Catastali, '$.particella') particella, JSON_VALUE(a.info_Catastali, '$.sub') sub, "
			+ " JSON_VALUE(a.info_Coltivazione, '$.idPianoColture') idPianoColture, JSON_VALUE(a.info_Coltivazione, '$.idColtura') IdColtura, JSON_VALUE(a.info_Coltivazione, '$.codColtura3') codColtura3, "
			+ " JSON_VALUE(a.info_Coltivazione, '$.codColtura5') codColtura5, JSON_VALUE(a.info_Coltivazione, '$.codLivello') codLivello,  JSON_VALUE(a.info_Coltivazione, '$.descrizioneColtura') descrizioneColtura,  "
			+ " JSON_VALUE(a.info_Coltivazione, '$.coefficienteTara') coefficienteTara, JSON_VALUE(a.info_Coltivazione, '$.superficieDichiarata') superficieDichiarata, JSON_VALUE(a.info_Coltivazione, '$.descMantenimento') descMantenimento, "
			+ " JSON_VALUE(a.riferimenti_Cartografici, '$.idParcella') idParcella, JSON_VALUE(a.riferimenti_Cartografici, '$.idIsola') idIsola, JSON_VALUE(a.riferimenti_Cartografici, '$.codIsola') codIsola from A4gt_Richiesta_Superficie a "
			+ " WHERE a.id_domanda = :idDomanda AND a.id_intervento = :intervento) a", nativeQuery = true)
	List<IRichiestaSuperficie> findByDomandaInterventoPaginata1(@Param("idDomanda") Long domanda, @Param("intervento") Long intervento, Pageable paginazione);

	@Query("SELECT count(a.id) FROM A4gtRichiestaSuperficie a WHERE a.domandaUnicaModel.id = :idDomanda AND a.intervento.identificativoIntervento = :codiceInterventoAgs")
	Long getCountfindByDomandaInterventoPaginata1(@Param("idDomanda") Long idDomanda,  @Param("codiceInterventoAgs") CodiceInterventoAgs codiceInterventoAgs);

	@Query(value = "select nvl(sum(sup_richiesta_netta) / 10000, 0) " +
			"from a4gt_richiesta_superficie s join a4gd_coltura_intervento m on s.codice_coltura_3 = m.codice_coltura_3 " +
			"and s.id_intervento = m.id_intervento join a4gd_intervento i on s.id_intervento = i.id " +
			"where id_domanda = :idDomanda and i.identificativo_intervento  = :#{#codiceInterventoAgs.name()}", nativeQuery = true)
	BigDecimal sumSuperficieRichiestaNettaCompatibile(@Param("idDomanda") Long idDomanda, @Param("codiceInterventoAgs") CodiceInterventoAgs codiceInterventoAgs);

	@Query(value = "select nvl(sum(sup_richiesta) / 10000, 0) " +
			"from a4gt_richiesta_superficie s join a4gd_coltura_intervento m on s.codice_coltura_3 = m.codice_coltura_3 " +
			"and s.id_intervento = m.id_intervento join a4gd_intervento i on s.id_intervento = i.id " +
			"where id_domanda = :idDomanda and i.identificativo_intervento  = :#{#codiceInterventoAgs.name()}", nativeQuery = true)
	BigDecimal sumSuperficieRichiestaLordaCompatibile(@Param("idDomanda") Long idDomanda, @Param("codiceInterventoAgs") CodiceInterventoAgs codiceInterventoAgs);

	@Query(value = "SELECT a.* FROM A4gt_Richiesta_Superficie a JOIN a4gd_intervento i on a.id_intervento = i.id " +
			"where a.id_domanda = :idDomanda and JSON_VALUE(a.info_catastali, '$.idParticella') = :idParticella and i.identificativo_intervento = :#{#codiceInterventoAgs.name()}", nativeQuery = true)
	List<A4gtRichiestaSuperficie> findByDomandaIdParticellaAndIntervento(@Param("idDomanda") Long idDomanda, @Param("idParticella") Long idParticella,
																		 @Param("codiceInterventoAgs") CodiceInterventoAgs codiceInterventoAgs);

	@Query("SELECT a " +
			"FROM A4gtRichiestaSuperficie a " +
			"WHERE a.domandaUnicaModel.id = :idDomanda AND a.intervento.identificativoIntervento = :codiceInterventoAgs AND a.codiceColtura3 IN :coltList")
//	@Query("SELECT a " +
//			"FROM A4gtRichiestaSuperficie a " +
//			"WHERE a.domandaUnicaModel.id = :idDomanda AND a.intervento.identificativoIntervento = :#{#codiceInterventoAgs.name()} AND a.codiceColtura3 IN :coltList")
	List<A4gtRichiestaSuperficie> findByDomandaInterventoCodColtura3(@Param("idDomanda") Long domanda, @Param("codiceInterventoAgs") CodiceInterventoAgs codiceInterventoAgs, @Param("coltList") List<String> coltList);

	@Query(value = "SELECT a.* FROM A4gt_Richiesta_Superficie a where a.codice_coltura_3 like ('080%') and JSON_VALUE(a.info_coltivazione, '$.descMantenimento') in ('PASCOLAMENTO CON ANIMALI PROPRI', 'PASCOLAMENTO CON ANIMALI DI TERZI') and JSON_VALUE(a.info_catastali, '$.idParticella') not in (select JSON_VALUE(p.particelle_catastali, '$.idParticella') from a4gt_dati_pascolo p ) and a.id_domanda = :idDomanda", nativeQuery = true)
	List<A4gtRichiestaSuperficie> findByDomandaAndNotImpegnatePascolo(@Param("idDomanda") Long domanda);

	Long countByDomandaUnicaModel(DomandaUnicaModel domanda);

	@Query(value = "SELECT infoColtivazione FROM A4gtRichiestaSuperficie a WHERE a.domandaUnicaModel.id = :idDomanda AND a.intervento.sostegno = :sostegno AND a.codiceColtura3 = :codiceColtura3 AND JSON_VALUE(a.infoCatastali, '$.idParticella') = :idParticella")
	List<String> getinfoColtivazione(@Param("idDomanda") Long idDomanda, @Param("idParticella") Long idParticella, @Param("sostegno") Sostegno sostegno,
			@Param("codiceColtura3") String codiceColtura3);

	@Query(value = "select nvl(sum(sup_richiesta_netta), 0) " +
			"from a4gt_richiesta_superficie s join a4gd_intervento i on s.id_intervento = i.id " +
			"where id_domanda = :idDomanda and i.identificativo_intervento  = :#{#codiceInterventoAgs.name()}", nativeQuery = true)
	BigDecimal sumSuperficieRichiestaNetta(@Param("idDomanda") Long idDomanda, @Param("codiceInterventoAgs") CodiceInterventoAgs codiceInterventoAgs);
	
	@Query(value = "SELECT INTERVENTO, SUM(TO_NUMBER(SUPERFICIE_AMMISSIBILE, '99999.99999'))" + 
			"    FROM A4GT_PASSO_TRANSIZIONE passo" + 
			"    JOIN A4GT_TRANSIZIONE_ISTRUTTORIA tran ON tran.id = passo.ID_TRANSIZ_SOSTEGNO" + 
			"    JOIN A4GT_ISTRUTTORIA istruttoria ON istruttoria.id = tran.id_istruttoria" + 
			"    JOIN A4GT_DOMANDA domanda ON istruttoria.id_domanda = domanda.id" + 
			"    JOIN A4GD_STATO_LAV_SOSTEGNO statoIstruttoria ON istruttoria.ID_STATO_LAVORAZIONE = statoIstruttoria.id," + 
			"    JSON_TABLE(passo.DATI_OUTPUT, '$.variabiliCalcolo[*]'" + 
			"        COLUMNS (" + 
			"           \"INTERVENTO\" PATH '$.tipoVariabile'," + 
			"           \"SUPERFICIE_AMMISSIBILE\" PATH '$.valNumber'" + 
			"        )) \"OUTPUT\"" + 
			"    WHERE (statoIstruttoria.IDENTIFICATIVO != 'NON_AMMISSIBILE' AND statoIstruttoria.IDENTIFICATIVO != 'NON_LIQUIDABILE')" + 
			"        AND domanda.ANNO_CAMPAGNA = :annoCampagna" + 
			"        AND INTERVENTO IN ('ACSSUPRIC_M8','ACSSUPRIC_M9','ACSSUPRIC_M10','ACSSUPRIC_M11','ACSSUPRIC_M14','ACSSUPRIC_M15','ACSSUPRIC_M16','ACSSUPRIC_M17')" + 
			"        AND istruttoria.tipologia = 'SALDO'" + 
			"        AND passo.CODICE_PASSO = 'CALCOLO_ACS'" + 
			"        AND tran.id = (SELECT max(tran2.id)" + 
			"            FROM A4GT_PASSO_TRANSIZIONE passo2" + 
			"            JOIN A4GT_TRANSIZIONE_ISTRUTTORIA tran2 ON tran2.id = passo2.ID_TRANSIZ_SOSTEGNO" + 
			"            JOIN A4GT_ISTRUTTORIA istruttoria2 ON istruttoria2.id = tran2.id_istruttoria" +  
			"            WHERE istruttoria2.id = istruttoria.id" + 
			"                AND passo2.CODICE_PASSO = 'CALCOLO_ACS'" +  
			"            GROUP BY istruttoria2.id" + 
			"        )" + 
			"    GROUP BY INTERVENTO" + 
			"    ORDER BY INTERVENTO", nativeQuery = true)
	List<Object[]> getTotaleSuperficiePerIntervento(@Param("annoCampagna") Integer annoCampagna);
	
	@Query(value = "SELECT a.riferimentiCartografici as riferimentiCartografici"
			+ " FROM A4gtRichiestaSuperficie a"
			+ " WHERE a.domandaUnicaModel.id = :idDomanda"
			+ " AND a.intervento.identificativoIntervento = :codiceInterventoAgs"
			+ " AND a.codiceColtura3 = :codiceColtura"
			+ " AND JSON_VALUE(a.infoCatastali, '$.idParticella') = :idParticella")
	List<String> findIdParcelleByDomandaParticellaIntervento(
			@Param("idDomanda") Long idDomanda,
			@Param("idParticella") Long idParticella,
			@Param("codiceInterventoAgs") CodiceInterventoAgs codiceInterventoAgs,
			@Param("codiceColtura") String codiceColtura);
}
