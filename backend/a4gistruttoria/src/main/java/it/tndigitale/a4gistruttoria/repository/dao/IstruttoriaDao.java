package it.tndigitale.a4gistruttoria.repository.dao;

import it.tndigitale.a4gistruttoria.dto.CuaaDenominazione;
import it.tndigitale.a4gistruttoria.repository.model.TipoIstruttoria;
import it.tndigitale.a4gistruttoria.repository.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IstruttoriaDao extends JpaRepository<IstruttoriaModel, Long>, JpaSpecificationExecutor<IstruttoriaModel> {

	@Deprecated //Betty: questo metodo non e' corretto
	public IstruttoriaModel findByDomandaUnicaModelAndSostegno(DomandaUnicaModel domanda, Sostegno sostegno);
	
	public IstruttoriaModel findByDomandaUnicaModelAndSostegnoAndTipologia(DomandaUnicaModel domanda, Sostegno sostegno, TipoIstruttoria tipologia);

	public List<IstruttoriaModel> findByDomandaUnicaModel_idAndSostegno(Long idDomanda, Sostegno sostegno);

	@Query(value = "select new it.tndigitale.a4gistruttoria.dto.CuaaDenominazione(istruttoria.domandaUnicaModel.cuaaIntestatario, " +
			"istruttoria.domandaUnicaModel.ragioneSociale) " +
			"from IstruttoriaModel istruttoria where istruttoria.a4gdStatoLavSostegno.identificativo = :statoSostegno " +
			"and istruttoria.sostegno = :sostegno")
	List<CuaaDenominazione> findCuaaByA4gdStatoLavSostegnoAndSostegno(
			@Param("statoSostegno") String statoSostegno,
			@Param("sostegno") Sostegno sostegno);
	
	@Query(value = "SELECT istruttoria.domandaUnicaModel.cuaaIntestatario "
			+ "FROM IstruttoriaModel istruttoria "
			+ "WHERE istruttoria.a4gdStatoLavSostegno.identificativo = :statoSostegno "
			+ "AND istruttoria.sostegno = :sostegno "
			+ "AND istruttoria.domandaUnicaModel.campagna = :annoCampagna "
			+ "AND istruttoria.tipologia = :tipo "
			+ "AND UPPER(istruttoria.domandaUnicaModel.cuaaIntestatario) LIKE CONCAT('%', UPPER(:cuaa), '%')")
	Page<String> findCuaaByA4gdStatoLavSostegnoAndA4gdSostegnoDuAndByCuaaLike(
			@Param("statoSostegno") String statoSostegno,
			@Param("sostegno") Sostegno sostegno,
			@Param("annoCampagna") Integer annoCampagna,
			@Param("cuaa") String cuaa,
			@Param("tipo") TipoIstruttoria tipo,
			Pageable paginazione);
	
	@Query(value = "SELECT istruttoria.domandaUnicaModel.ragioneSociale "
			+ "FROM IstruttoriaModel istruttoria "
			+ "WHERE istruttoria.a4gdStatoLavSostegno.identificativo = :statoSostegno "
			+ "AND istruttoria.sostegno = :sostegno "
			+ "AND istruttoria.domandaUnicaModel.campagna = :annoCampagna "
			+ "AND istruttoria.tipologia = :tipo "
			+ "AND UPPER(istruttoria.domandaUnicaModel.ragioneSociale) LIKE CONCAT('%', UPPER(:ragioneSociale), '%')")
	Page<String> findRagioneSocialeByA4gdStatoLavSostegnoAndA4gdSostegnoDuAndByRagioneSocialeLike(
			@Param("statoSostegno") String statoSostegno,
			@Param("sostegno") Sostegno sostegno,
			@Param("annoCampagna") Integer annoCampagna,
			@Param("ragioneSociale") String ragioneSociale,
			@Param("tipo") TipoIstruttoria tipo,
			Pageable paginazione);
	
	List<IstruttoriaModel> findByDomandaUnicaModelId(Long idDomanda);

	List<IstruttoriaModel> findByElencoLiquidazione(ElencoLiquidazioneModel elenco);
	
	List<IstruttoriaModel> findByElencoLiquidazioneId(Long idElenco);

	@Query(value = "SELECT statoIstruttoria.IDENTIFICATIVO, TIPOVARIABILE, SUM(TO_NUMBER(VALORE, '99999999.99')) " + 
			"FROM A4GT_PASSO_TRANSIZIONE passo " + 
			"JOIN A4GT_TRANSIZIONE_ISTRUTTORIA tran ON tran.id = passo.ID_TRANSIZ_SOSTEGNO " + 
			"JOIN A4GT_ISTRUTTORIA istruttoria ON istruttoria.id = tran.id_istruttoria " + 
			"JOIN A4GT_DOMANDA domanda ON istruttoria.id_domanda = domanda.id " + 
			"JOIN A4GD_STATO_LAV_SOSTEGNO statoIstruttoria ON istruttoria.ID_STATO_LAVORAZIONE = statoIstruttoria.id, " + 
			"JSON_TABLE(passo.DATI_OUTPUT, '$.variabiliCalcolo[*]' " + 
			"    COLUMNS ( " + 
			"       \"TIPOVARIABILE\" PATH '$.tipoVariabile', " + 
			"       \"VALORE\" PATH '$.valNumber' " + 
			"    )) \"OUTPUT\" " + 
			"WHERE " + 
			"    statoIstruttoria.IDENTIFICATIVO IN ( " + 
			"        'RICHIESTO', 'CONTROLLI_CALCOLO_KO', 'CONTROLLI_CALCOLO_OK', 'NON_AMMISSIBILE', 'LIQUIDABILE', 'NON_LIQUIDABILE', 'CONTROLLI_LIQUIDABILE_KO', 'DEBITI', " + 
			"        'CONTROLLI_INTERSOSTEGNO_OK', 'PAGAMENTO_AUTORIZZATO', 'PAGAMENTO_NON_AUTORIZZATO') " + 
			"    AND istruttoria.SOSTEGNO = 'DISACCOPPIATO' " + 
			"    AND passo.CODICE_PASSO IN ('CONTROLLI_FINALI','AMMISSIBILITA')" + 
			"    AND domanda.ANNO_CAMPAGNA = :annoCampagna " + 
			"    AND TIPOVARIABILE IN ('IMPCALCFINLORDO', 'IMPCALCFIN', 'BPSIMPRIC', 'GREIMPRIC', 'GIOIMPRIC') " + 
			"    AND istruttoria.tipologia = :tipoIstruttoria " + 
			"    AND tran.id = (SELECT max(tran2.id) " + 
			"        FROM A4GT_PASSO_TRANSIZIONE passo2 " + 
			"        JOIN A4GT_TRANSIZIONE_ISTRUTTORIA tran2 ON tran2.id = passo2.ID_TRANSIZ_SOSTEGNO " + 
			"        JOIN A4GT_ISTRUTTORIA istruttoria2 ON istruttoria2.id = tran2.id_istruttoria  " + 
			"        WHERE istruttoria2.id = istruttoria.id " + 
			"            AND passo2.CODICE_PASSO IN ('CONTROLLI_FINALI','AMMISSIBILITA') " + 
			"        GROUP BY istruttoria2.id " + 
			"    ) " + 
			"GROUP BY statoIstruttoria.IDENTIFICATIVO, TIPOVARIABILE", nativeQuery = true)
	List<Object[]> getValoriPremioLordoNettoPerStatoDisaccoppiato(
			@Param("annoCampagna") Integer annoCampagna,
			@Param("tipoIstruttoria") String tipoIstruttoria);

	@Query(value = "SELECT statoIstruttoria.IDENTIFICATIVO, TIPOVARIABILE, SUM(TO_NUMBER(VALORE, '99999999.99')) " + 
			"FROM A4GT_PASSO_TRANSIZIONE passo " + 
			"JOIN A4GT_TRANSIZIONE_ISTRUTTORIA tran ON tran.id = passo.ID_TRANSIZ_SOSTEGNO " + 
			"JOIN A4GT_ISTRUTTORIA istruttoria ON istruttoria.id = tran.id_istruttoria " + 
			"JOIN A4GT_DOMANDA domanda ON istruttoria.id_domanda = domanda.id " + 
			"JOIN A4GD_STATO_LAV_SOSTEGNO statoIstruttoria ON istruttoria.ID_STATO_LAVORAZIONE = statoIstruttoria.id, " + 
			"JSON_TABLE(passo.DATI_OUTPUT, '$.variabiliCalcolo[*]' " + 
			"    COLUMNS ( " + 
			"       \"TIPOVARIABILE\" PATH '$.tipoVariabile', " + 
			"       \"VALORE\" PATH '$.valNumber' " + 
			"    )) \"OUTPUT\" " + 
			"WHERE " + 
			"    statoIstruttoria.IDENTIFICATIVO IN ( " + 
			"        'RICHIESTO', 'INTEGRATO', 'CONTROLLI_CALCOLO_KO', 'CONTROLLI_CALCOLO_OK', 'NON_AMMISSIBILE', 'LIQUIDABILE', 'NON_LIQUIDABILE', 'CONTROLLI_LIQUIDABILE_KO', 'DEBITI', " + 
			"        'CONTROLLI_INTERSOSTEGNO_OK', 'PAGAMENTO_AUTORIZZATO', 'PAGAMENTO_NON_AUTORIZZATO') " + 
			"    AND istruttoria.sostegno = 'ZOOTECNIA'" + 
			"    AND passo.CODICE_PASSO = 'CALCOLO_ACZ' " + 
			"    AND domanda.ANNO_CAMPAGNA = :annoCampagna " + 
			"    AND TIPOVARIABILE IN ('ACZIMPCALCLORDOTOT', 'ACZIMPCALCTOT') " + 
			"    AND istruttoria.tipologia = :tipoIstruttoria " + 
			"    AND tran.id = (SELECT max(tran2.id) " + 
			"        FROM A4GT_PASSO_TRANSIZIONE passo2 " + 
			"        JOIN A4GT_TRANSIZIONE_ISTRUTTORIA tran2 ON tran2.id = passo2.ID_TRANSIZ_SOSTEGNO " + 
			"        JOIN A4GT_ISTRUTTORIA istruttoria2 ON istruttoria2.id = tran2.id_istruttoria  " + 
			"        WHERE istruttoria2.id = istruttoria.id " + 
			"            AND passo2.CODICE_PASSO = 'CALCOLO_ACZ'  " + 
			"        GROUP BY istruttoria2.id " + 
			"    ) " + 
			"GROUP BY statoIstruttoria.IDENTIFICATIVO, TIPOVARIABILE", nativeQuery = true)
	List<Object[]> getValoriPremioLordoNettoPerStatoAccoppiatoZootecnia(
			@Param("annoCampagna") Integer annoCampagna,
			@Param("tipoIstruttoria") String tipoIstruttoria);
	
	@Query(value = "SELECT statoIstruttoria.IDENTIFICATIVO, TIPOVARIABILE, SUM(TO_NUMBER(VALORE, '99999999.99')) " + 
			"FROM A4GT_PASSO_TRANSIZIONE passo " + 
			"JOIN A4GT_TRANSIZIONE_ISTRUTTORIA tran ON tran.id = passo.ID_TRANSIZ_SOSTEGNO " + 
			"JOIN A4GT_ISTRUTTORIA istruttoria ON istruttoria.id = tran.id_istruttoria " + 
			"JOIN A4GT_DOMANDA domanda ON istruttoria.id_domanda = domanda.id " + 
			"JOIN A4GD_STATO_LAV_SOSTEGNO statoIstruttoria ON istruttoria.ID_STATO_LAVORAZIONE = statoIstruttoria.id, " + 
			"JSON_TABLE(passo.DATI_OUTPUT, '$.variabiliCalcolo[*]' " + 
			"    COLUMNS ( " + 
			"       \"TIPOVARIABILE\" PATH '$.tipoVariabile', " + 
			"       \"VALORE\" PATH '$.valNumber' " + 
			"    )) \"OUTPUT\" " + 
			"WHERE " + 
			"    statoIstruttoria.IDENTIFICATIVO IN ( " + 
			"        'RICHIESTO', 'CONTROLLI_CALCOLO_KO', 'CONTROLLI_CALCOLO_OK', 'NON_AMMISSIBILE', 'LIQUIDABILE', 'NON_LIQUIDABILE', 'CONTROLLI_LIQUIDABILE_KO', 'DEBITI', " + 
			"        'CONTROLLI_INTERSOSTEGNO_OK', 'PAGAMENTO_AUTORIZZATO', 'PAGAMENTO_NON_AUTORIZZATO') " + 
			"    AND istruttoria.sostegno = 'SUPERFICIE'" + 
			"    AND passo.CODICE_PASSO = 'CALCOLO_ACS' " + 
			"    AND domanda.ANNO_CAMPAGNA = :annoCampagna " + 
			"    AND TIPOVARIABILE IN ('ACSIMPCALCLORDOTOT', 'ACSIMPCALCTOT', 'ACSIMPRICTOT') " + 
			"    AND istruttoria.tipologia = :tipoIstruttoria " + 
			"    AND tran.id = (SELECT max(tran2.id) " + 
			"        FROM A4GT_PASSO_TRANSIZIONE passo2 " + 
			"        JOIN A4GT_TRANSIZIONE_ISTRUTTORIA tran2 ON tran2.id = passo2.ID_TRANSIZ_SOSTEGNO " + 
			"        JOIN A4GT_ISTRUTTORIA istruttoria2 ON istruttoria2.id = tran2.id_istruttoria  " + 
			"        WHERE istruttoria2.id = istruttoria.id " + 
			"            AND passo2.CODICE_PASSO = 'CALCOLO_ACS'  " + 
			"        GROUP BY istruttoria2.id " + 
			"    ) " + 
			"GROUP BY statoIstruttoria.IDENTIFICATIVO, TIPOVARIABILE", nativeQuery = true)
	List<Object[]> getValoriPremioLordoNettoPerStatoSuperfici(
			@Param("annoCampagna") Integer annoCampagna,
			@Param("tipoIstruttoria") String tipoIstruttoria);
}
