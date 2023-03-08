package it.tndigitale.a4g.ags.repository.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import it.tndigitale.a4g.ags.dto.AnagraficaAziendaBaseData;
import it.tndigitale.a4g.ags.dto.AnagraficaAziendaDocumentData;
import it.tndigitale.a4g.ags.dto.AnagraficaAziendaEnteData;
import it.tndigitale.a4g.ags.dto.AnagraficaAziendaFascicoloData;
import it.tndigitale.a4g.ags.model.AnagraficaAziendaBaseDataRowMapper;
import it.tndigitale.a4g.ags.model.AnagraficaAziendaDocumentDataRowMapper;
import it.tndigitale.a4g.ags.model.AnagraficaAziendaEnteDataRowMapper;
import it.tndigitale.a4g.ags.model.AnagraficaAziendaFascicoloDataRowMapper;

@Repository
public class FascicoloAziendaleDaoImpl implements FascicoloAziendaleDao {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public FascicoloAziendaleDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private static final String GET_PK_CUAA_BY_CUAA =
            "SELECT PK_CUAA " +
            "FROM SITI.CONS_AZIE_TAB " +
            "WHERE CUAA = :cuaa";

    @Override
    public long findPkCuaaByCuaa(String cuaa) {
        final MapSqlParameterSource params = new MapSqlParameterSource("cuaa", cuaa);
        return this.jdbcTemplate.queryForObject(GET_PK_CUAA_BY_CUAA, params, Long.class);
    }

    private static final String FIND_DOCUMENT_DATA_BY_PK_CUAA =
            "SELECT sdoc.fkid_tipo_doc AS tipodocumento, " +
            "       NVL (sarc.cod_protocollo, " +
            "            sdoc.cod_archiviazione) AS numerodocumento, " +
            "       sdext.ave_dt_documento AS datadocumento, " +
            "       sdoc.data_scadenza AS datascaddocumento " +
            "      FROM sitifile_documenti sdoc, " +
            "           sitifile_documenti_campi_ext sdext, " +
            "           sitifile_archiviazione sarc " +
            "      WHERE sdoc.pk_cuaa = :pkCuaa " +
            "        AND SYSDATE BETWEEN sdoc.data_inizio_val " +
            "        AND sdoc.data_fine_val " +
            "        AND EXISTS ( " +
            "          (SELECT 1 " +
            "           FROM sitifile_atti_doc sado " +
            "           WHERE sado.fkid_doc = sdoc.pkid_doc " +
            "             AND SYSDATE BETWEEN sado.data_inizio_val " +
            "             AND sado.data_fine_val)) " +
            "        AND sdext.fkid_doc = sdoc.pkid_doc " +
            "        AND sarc.pkcod_archiviazione(+) = sdoc.cod_archiviazione " +
            "        AND NVL (sdoc.data_scadenza, " +
            "                 TO_DATE ('99991231', 'YYYYMMDD') " +
            "              ) > SYSDATE " +
            "        AND sdoc.valido IN ('Y', 'S') " +
            "      ORDER BY sdoc.pkid_doc DESC, " +
            "               sdoc.fkid_tipo_doc DESC, " +
            "               TO_CHAR (sdoc.data_scadenza, 'YYYYMMDD') DESC, " +
            "               sdoc.pkid_doc DESC " +
            "FETCH NEXT 1 ROWS ONLY";

    @Override
    public AnagraficaAziendaDocumentData loadDocumentData(long pkCuaa) {

        final MapSqlParameterSource params = new MapSqlParameterSource("pkCuaa", pkCuaa);

        return jdbcTemplate.queryForObject(FIND_DOCUMENT_DATA_BY_PK_CUAA, params, new AnagraficaAziendaDocumentDataRowMapper());

        // TODO: What if mapping isn't complete?
        //final Map<Integer, Integer> documentTypeMapping = loadDocumentTypeMapping();
        //final SqlRowSet rs = queryForSingleRowSet(FIND_DOCUMENT_DATA_BY_PK_CUAA, params);
        //int siapDocType = rs.getInt("tipodocumento");
        //Integer dtoDocType = documentTypeMapping.getOrDefault(siapDocType, -1);
    }

    private static String FIND_ANAGRAFICA_BY_PK_CUAA =
            "SELECT s.COD_FISCALE, s.NATURA_GIURIDICA, s.NOME, s.COGNOME, s.DENOMINAZIONE, s.SESSO, s.D_NASCITA,\n" +
            "       s.FKCOD_COMU_NASC AS COMUNE_NASCITA, nascita.DENO_PROV AS PROVINCIA_NASCITA,\n" +
            "       recapito.ISTATC AS COMUNE_RECAPITO, recapito.ISTATP AS PROVINCIA_RECAPITO, CAP_RECAP AS CAP_RECAPITO,\n" +
            "       INDIRIZZO_EST_RECAP AS INDIRIZZO_RECAPITO,\n" +
            "       residenza.ISTATC AS COMUNE_RESIDENZA, residenza.ISTATP AS PROVINCIA_RESIDENZA,\n" +
            "       s.CAP as CAP_RESIDENZA, s.INDIRIZZO_EST AS INDIRIZZO_RESIDENZA,\n" +
            "       PART_IVA, rea.NUMERO_REA_CCIAA, fasc.SCO_OP, fasc.DT_INIZIO AS FASC_DT_INIZIO, fasc.DT_FINE AS FASC_DT_FINE,\n" +
            "       rea.EMAIL_PEC, rea.sco_attivita_1\n" +
            "FROM SITI.ANAG_SOGGETTI s\n" +
            "                        left join SITI.SITICOMU_ISTAT nascita\n" +
            "                                   on s.FKCOD_COMU_NASC = nascita.CODI_FISC_LUNA\n" +
            "                        left join SITI.SITICOMU_ISTAT recapito\n" +
            "                                   on s.FKCOD_COMU_RECAPITO = recapito.CODI_FISC_LUNA\n" +
            "                        left join SITI.SITICOMU_ISTAT residenza\n" +
            "                                   on s.FKCOD_COMU_RESI = residenza.CODI_FISC_LUNA\n" +
            "                        left join (SELECT ID_SOGGETTO, NUMERO_REA_CCIAA, EMAIL_PEC, sco_attivita_1\n" +
            "                                   FROM FASCICOLO.TINFO_PARIX\n" +
            "                                   WHERE SYSDATE BETWEEN DT_INSERT AND DT_DELETE) rea ON rea.id_soggetto = s.COD_SOGGETTO\n" +
            "                        left join FASCICOLO.TFASCICOLO fasc ON fasc.ID_SOGGETTO = s.COD_SOGGETTO\n" +
            "WHERE NVL(s.DATA_FINE_VAL, TO_DATE ('99991231', 'YYYYMMDD')) > SYSDATE AND s.COD_SOGGETTO = :pkCuaa";

    public AnagraficaAziendaBaseData loadAnagraficaBaseData(long pkCuaa) {
        final MapSqlParameterSource params = new MapSqlParameterSource("pkCuaa", pkCuaa);

        return jdbcTemplate.queryForObject(FIND_ANAGRAFICA_BY_PK_CUAA, params, new AnagraficaAziendaBaseDataRowMapper());
    }


    private static final String LOAD_FASCICOLO_DATA =
            "SELECT mov.DT_MOVIMENTO\n" +
            "FROM FASCICOLO.TMOVIMENTO mov JOIN FASCICOLO.TFASCICOLO fasc ON mov.ID_FASCICOLO = fasc.ID_FASCICOLO\n" +
            "WHERE mov.SCO_MOVIMENTO = 'VALIDA' AND fasc.ID_SOGGETTO = :pkCuaa\n" +
            "ORDER BY mov.DT_MOVIMENTO DESC\n" +
            "FETCH NEXT 1 ROWS ONLY";

    @Override
    public AnagraficaAziendaFascicoloData loadFascicoloData(long pkCuaa) {
        final MapSqlParameterSource params = new MapSqlParameterSource("pkCuaa", pkCuaa);

        return jdbcTemplate.queryForObject(LOAD_FASCICOLO_DATA, params, new AnagraficaAziendaFascicoloDataRowMapper());
    }

    private static final String LOAD_ENTE_DATA =
            "SELECT cce.DATA_RICHIESTA, se.DES_ENTE, se.CODI_TRAM\n" +
            "FROM SITI.CONS_CUAA_ENTE cce JOIN FASCICOLO.SITIENTE se ON cce.COD_ENTE = se.COD_ENTE\n" +
            "WHERE cce.PK_CUAA = :pkCuaa AND cce.FLAG_GESTIONE = 'S'\n" +
            "ORDER BY cce.DATA_FINE DESC\n" +
            "FETCH NEXT 1 ROWS ONLY";

    public AnagraficaAziendaEnteData loadEnteData(long pkCuaa) {
        final MapSqlParameterSource params = new MapSqlParameterSource("pkCuaa", pkCuaa);

        return jdbcTemplate.queryForObject(LOAD_ENTE_DATA, params, new AnagraficaAziendaEnteDataRowMapper());
    }

    private static final String LIST_DOCUMENT_TYPE_MAPPING =
            "SELECT TO_NUMBER (TRIM (sotto_codice)) AS tipo_doc_int, TRIM (codice_esterno) AS tipo_doc_est " +
            "FROM ttranscodifica " +
            "WHERE sco_transcodifica = 'WRIFAS' AND codice = 'TIPDOC' " +
            "ORDER BY 1";


    // TODO: Cache?
    @Override
    public Map<Integer, Integer> loadDocumentTypeMapping() {
        return this.jdbcTemplate.query(LIST_DOCUMENT_TYPE_MAPPING,
                                       new MapSqlParameterSource(),
                                       (ResultSetExtractor<Map<Integer, Integer>>) rs -> {
                                              HashMap<Integer, Integer> mapRet = new HashMap<>();
                                              while (rs.next()) {
                                                  mapRet.put(rs.getInt("tipo_doc_int"),
                                                             rs.getInt("tipo_doc_est"));
                                              }
                                              return mapRet;
                                          });
    }

}
