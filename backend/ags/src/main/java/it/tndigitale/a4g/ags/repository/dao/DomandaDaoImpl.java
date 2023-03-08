package it.tndigitale.a4g.ags.repository.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import it.tndigitale.a4g.ags.dto.CalcoloSostegnoAgs;
import it.tndigitale.a4g.ags.dto.ControlliPresentazione;
import it.tndigitale.a4g.ags.dto.DatiComune;
import it.tndigitale.a4g.ags.dto.DatiDomandaAgs;
import it.tndigitale.a4g.ags.dto.DatiErede;
import it.tndigitale.a4g.ags.dto.DatiMovimentoAgs;
import it.tndigitale.a4g.ags.dto.DomandaPsr;
import it.tndigitale.a4g.ags.dto.DomandaUnica;
import it.tndigitale.a4g.ags.dto.DomandaUnicaFilter;
import it.tndigitale.a4g.ags.dto.InfoEleggibilitaParticella;
import it.tndigitale.a4g.ags.dto.InfoGeneraliDomanda;
import it.tndigitale.a4g.ags.dto.PsrRitiroTotale;
import it.tndigitale.a4g.ags.dto.Richieste;
import it.tndigitale.a4g.ags.dto.SintesiRichieste;
import it.tndigitale.a4g.ags.dto.SostegniSuperficie;
import it.tndigitale.a4g.ags.dto.VariabileSostegnoAgs;
import it.tndigitale.a4g.ags.dto.domandaunica.Coltura;
import it.tndigitale.a4g.ags.dto.domandaunica.DatiDisaccoppiato;
import it.tndigitale.a4g.ags.dto.domandaunica.DatiDomanda;
import it.tndigitale.a4g.ags.dto.domandaunica.DatiPascolo;
import it.tndigitale.a4g.ags.dto.domandaunica.DichiarazioniDomandaUnica;
import it.tndigitale.a4g.ags.dto.domandaunica.DichiarazioniDomandaUnicaEnum;
import it.tndigitale.a4g.ags.dto.domandaunica.Particella;
import it.tndigitale.a4g.ags.dto.domandaunica.SostegniAllevamento;
import it.tndigitale.a4g.ags.dto.domandaunica.SuperficieImpegnata;
import it.tndigitale.a4g.ags.model.CalcoloSostegnoRowMapper;
import it.tndigitale.a4g.ags.model.DatiComuneMapper;
import it.tndigitale.a4g.ags.model.DatiDomandaRowMapper;
import it.tndigitale.a4g.ags.model.DatiPascoloRowMapper;
import it.tndigitale.a4g.ags.model.DichiarazioniDomandaUnicaRowMapper;
import it.tndigitale.a4g.ags.model.DomandaPsrMapper;
import it.tndigitale.a4g.ags.model.DomandaUnicaRowMapper;
import it.tndigitale.a4g.ags.model.DomandeCollegatePsrFilter;
import it.tndigitale.a4g.ags.model.InfoEleggibilitaParticellaRowMapper;
import it.tndigitale.a4g.ags.model.InfoGeneraliRowMapper;
import it.tndigitale.a4g.ags.model.ParticelleRowMapper;
import it.tndigitale.a4g.ags.model.SostegniAllevamentoRowMapper;
import it.tndigitale.a4g.ags.model.SostegniSuperficiRowMapper;
import it.tndigitale.a4g.ags.model.StatoDomanda;
import it.tndigitale.a4g.ags.model.VariabiliCalcoloRowMapper;
import it.tndigitale.a4g.ags.utils.DecodificaPACSettore;
import it.tndigitale.a4g.ags.utils.DecodificaSettorePAC;
import it.tndigitale.a4g.ags.utils.Messaggi;

/**
 * @author A.Siravo
 *
 */
@Repository
public class DomandaDaoImpl extends JdbcDaoSupport implements DomandaDao {

	private static final Logger logger = LoggerFactory.getLogger(DomandaDaoImpl.class);

	private final String sqlGetInfoGeneraliDomanda = "with domande_soggetto as (\r\n"
			+ "    select d.id_domanda as id_dom_attuale, d.id_soggetto || '-' || m.anno || '-' || m.sco_settore as rif_domanda\r\n"
			+ "    from tdom_domanda d, tdom_modulo_deco m \r\n"
			+ "    where d.id_domanda = ? \r\n"
			+ "    and d.id_modulo = m.id_modulo\r\n"
			+ "    and sysdate between d.dt_insert and d.dt_delete\r\n"
			+ "),\r\n"
			+ "dati_domande_soggetto as (\r\n"
			+ "    SELECT d1.id_domanda, d1.dt_insert, d1.sco_stato, m1.cod_modulo \r\n"
			+ "    FROM TDOMANDA D1, tdom_modulo_deco m1, domande_soggetto ds \r\n"
			+ "    WHERE d1.id_soggetto || '-' || d1.anno || '-' || d1.sco_settore = ds.rif_domanda\r\n"
			+ "    and d1.id_modulo = m1.id_modulo\r\n"
			+ ")\r\n"
			+ "SELECT D.DS_DOM_STATO AS STATO_DOMANDA,\r\n"
			+ "        D.DT_INSERT AS DATA_PASSAGGIO_STATO,\r\n"
			+ "        D.SCO_SETTORE AS SETTORE,\r\n"
			+ "        D.ANNO AS CAMPAGNA,\r\n"
			+ "        MOD.COD_MODULO,\r\n"
			+ "        D.DE_MODULO AS MODULO_DOMANDA,\r\n"
			+ "        S.CUAA AS CUAA,\r\n"
			+ "        S.RAGI_SOCI AS RAGIONE_SOCIALE,\r\n"
			+ "        D.ID_DOMANDA,\r\n"
			+ "        M.DT_MOVIMENTO AS DATA_PROTOCOLLAZIONE,\r\n"
			+ "        (\r\n"
			+ "            SELECT max(id_domanda) FROM dati_domande_soggetto dds \r\n"
			+ "            WHERE dds.SCO_STATO = '000010' and dds.cod_modulo = ('BPS_' || MOD.ANNO)\r\n"
			+ "        ) as ID_DOMANDA_RETTIFICATA, -- quella iniziale\r\n"
			+ "        E.COD_ENTE,\r\n"
			+ "        E.DES_ENTE AS ENTE,\r\n"
			+ "        (SELECT MIN(D1.DT_INSERT)FROM TDOMANDA D1 WHERE D1.ID_DOMANDA = D.ID_DOMANDA AND D1.SCO_STATO = '000010') AS DATA_PRESENTAZIONE,\r\n"
			+ "        (\r\n"
			+ "            SELECT max(dt_insert) FROM dati_domande_soggetto dds \r\n"
			+ "            WHERE dds.SCO_STATO = '000010' and dds.cod_modulo = ('BPS_' || MOD.ANNO)\r\n"
			+ "        ) AS DATA_PRESEN_DOM_RETTIFICATA,\r\n"
			+ "        (\r\n"
			+ "            SELECT max(dt_insert) FROM dati_domande_soggetto dds \r\n"
			+ "            WHERE dds.SCO_STATO = '000015' and dds.cod_modulo = ('BPS_' || MOD.ANNO)\r\n"
			+ "        ) AS DATA_PROTOC_DOM_RETTIFICATA,\r\n"
			+ "        (\r\n"
			+ "            SELECT max(dt_insert) FROM dati_domande_soggetto dds \r\n"
			+ "            WHERE dds.SCO_STATO = '000015' and dds.cod_modulo != ('BPS_RITPRZ_' || MOD.ANNO)\r\n"
			+ "        ) AS DT_PROTOCOLLAZ_ULT_MODIFICA,\r\n"
			+ "        (\r\n"
			+ "            SELECT max(id_domanda) FROM dati_domande_soggetto dds \r\n"
			+ "            WHERE dds.SCO_STATO = '000015' and dds.cod_modulo != ('BPS_RITPRZ_' || MOD.ANNO)\r\n"
			+ "        ) AS NUMERO_DOMANDA_ULT_MODIFICA\r\n"
			+ "FROM TDOMANDA D\r\n"
			+ "    JOIN CONS_SOGG_VIW S ON D.ID_SOGGETTO = S.PK_CUAA\r\n"
			+ "    JOIN TQDO_ENTI QE ON D.ID_DOMANDA = QE.ID_DOMANDA\r\n"
			+ "    JOIN SITI.SITIENTE E ON QE.ID_ENTE_COMPILATORE = E.COD_ENTE\r\n"
			+ "    JOIN TDOM_MOVIMENTO M ON D.ID_DOMANDA = M.ID_DOMANDA\r\n"
			+ "    JOIN TDOM_MODULO_DECO MOD ON D.ID_MODULO = MOD.ID_MODULO\r\n"
			+ "WHERE d.id_domanda = (select id_dom_attuale from domande_soggetto)\r\n"
			+ "    AND M.COD_WORKFLOW in('APP_SUBMIT', 'APP_PROTOC')\r\n"
			+ "    AND M.FG_VALIDO_FINE = 'S'\r\n"
			+ "    and rownum = 1";

	private final String sqlGetDatiProtocollazioneDomanda = "SELECT CHK211_1 AS AGGIORNAMENTO_FASCICOLO, CHK211_2 AS VISIONE_ANOMALIE, CHK211_3 AS FIRMA_DOMANDA, CHK211_4 AS ARCHIVIAZIONE_DOMANDA " +
			"FROM SITI.SITIFILE_DOCUMENTI_CAMPI_EXT " +
			"WHERE FKID_DOC = (SELECT AD.FKID_DOC " +
				"FROM SITI.SITIFILE_ATTI_DOC AD JOIN SITI.SITIFILE_DOCUMENTI D ON AD.FKID_DOC = D.PKID_DOC JOIN SITI.SITIFILE_TIPIDOC TD ON D.FKID_TIPO_DOC = TD.PKID_TIPO_DOC " +
				"WHERE FKID_ATTO = (SELECT ID_ATTO_INIZIO FROM TDOM_MOVIMENTO M WHERE ID_DOMANDA = ? AND COD_WORKFLOW in ('APP_SUBMIT', 'APP_PROTOC') " +
					"AND DT_MOVIMENTO = (SELECT MIN(DT_MOVIMENTO) " +
						"FROM TDOM_MOVIMENTO M1 WHERE M1.COD_WORKFLOW IN ('APP_SUBMIT', 'APP_PROTOC') AND M1.FG_VALIDO_FINE = 'S' AND M1.ID_DOMANDA = M.ID_DOMANDA)AND FG_VALIDO_FINE = 'S'" +
				") " +
				"AND TD.DESCRIZIONE = 'CHECKLIST DOMANDA UNICA' AND SYSDATE BETWEEN AD.DATA_INIZIO_VAL  AND AD.DATA_FINE_VAL" +
			")";

	private final String sqlCountDomandeProtocollate = "SELECT COUNT(*) FROM TDOM_MODULO_DECO MODU, TDOM_DOMANDA DOMA WHERE MODU.ANNO = ? AND MODU.SCO_SETTORE = ? AND MODU.ID_MODULO = DOMA.ID_MODULO AND SYSDATE BETWEEN DOMA.DT_INSERT AND DOMA.DT_DELETE AND DOMA.SCO_STATO = ? AND MODU.COD_MODULO <> ?||MODU.ANNO";

	private final String sqlGetListaDomandePerStato = "SELECT DISTINCT DOMA.ID_DOMANDA " +
			"FROM TDOM_MODULO_DECO MODU, TDOM_DOMANDA DOMA " +
			"WHERE MODU.ANNO = :annoRiferimento AND MODU.SCO_SETTORE = :settore AND MODU.ID_MODULO = DOMA.ID_MODULO " +
			"AND SYSDATE BETWEEN DOMA.DT_INSERT AND DOMA.DT_DELETE AND DOMA.SCO_STATO IN (:stati) " +
			"AND MODU.COD_MODULO <> :moduloRitiroTotale||MODU.ANNO";


	private final String sqlGetListaDomandeProtocollate = "SELECT DISTINCT DOMANDE.ID_DOMANDA " +
			"FROM A4G_DU_PROTOCOLLATE DOMANDE " +
			"WHERE DOMANDE.ANNO = :campagna AND DOMANDE.COD_MODULO <> :moduloRitiroTotale||DOMANDE.ANNO";

	private final String sqgGetDatiDomandaAgs = "SELECT DOM.PKID, DOM.ID_DOMANDA, DOM.ID_SOGGETTO, DOM.ID_MODULO, DOM.ID_DOMANDA_RETTIFICATA, DOM.SCO_STATO, DOM.DT_INSERT, DOM.DT_DELETE, DW.GROUP_ID FROM TDOM_DOMANDA DOM JOIN TDOM_DEF_WORKFLOW_GROUP GR ON DOM.ID_MODULO = GR.ID_MODULO JOIN TWOR_DEF_WORKFLOW_GROUP DW ON DW.GROUP_ID = GR.GROUP_ID WHERE DOM.ID_DOMANDA = ? AND DOM.DT_DELETE > SYSDATE AND DW.SCO_DESC_GRP = 'DOMSTA' AND GR.DT_END > SYSDATE";
	private final String sqlStoricizzaStatoAttualeDomanda = "UPDATE TDOM_DOMANDA SET DT_DELETE = SYSDATE WHERE PKID = ? AND ID_DOMANDA = ?";
	private final String sqlGetTransitionId = "select SEQ_WOR_LOG_TR_TR_ID.NEXTVAL from dual";
	private final String sqlGetDatiMovimento = "SELECT WF.WORKFLOW_ID, WF.GROUP_ID, WF.SCO_STATUS_FROM, WF.SCO_TRANSITION_TO, WF.SCO_STATUS_TO, WF.RULE_ID, WF.TASK_ID_TO, DM.ACT_TYPE_ID FROM TWOR_DEF_WORKFLOW WF JOIN TWOR_DEF_METADATA DM ON WF.WORKFLOW_ID = DM.WORKFLOW_ID WHERE GROUP_ID = ? AND SCO_TRANSITION_TO = ? AND SCO_STATUS_FROM = ? AND SYSDATE BETWEEN WF.DT_START AND WF.DT_END";
	private final String sqlInserisciTDomLogTransition = "INSERT INTO TDOM_LOG_TRANSITION(ID_DOMANDA, TRANSITION_ID) VALUES (?, ?)";
	private final String sqlInserisciTWorLogTransition = "INSERT INTO TWOR_LOG_TRANSITION (TRANSITION_ID, WORKFLOW_ID, DT_TRANSITION, TRANS_USER, FG_VALID) VALUES (?, ?, SYSDATE, 'A4G', 1)";
	private final String sqlInserisciNuovoStatoDomanda = "insert into tdom_domanda"
			+ "  (pkid, dt_insert, dt_delete, id_soggetto, id_domanda, id_domanda_rettificata, id_modulo, cod_stato, sco_stato, cod_fonte_dati, sco_fonte_dati,"
			+ "  dt_inizio, dt_fine, fg_valido_inizio, fg_valido_fine, dt_ultimo_agg, utente) values (?, sysdate, to_date('31-12-9999', 'dd-mm-yyyy'), ?, ?, ?, ?, 'STADOM', ?, 'FONDAT', '000001',"
			+ "  sysdate, to_date('31-12-9999', 'dd-mm-yyyy'), 'N', 'N', sysdate, 'A4G')";
	private final String sqlIsDomandaAlfanumerica = "SELECT COUNT(*) FROM TDOM_GRAF_BLACKLIST_CUAA WHERE CUAA = ? AND ANNO = ? AND SCO_SETTORE = ?";
	private final String sqlHasImpegnoDisaccoppiato = "SELECT CASE WHEN COUNT(*) > 0 THEN 'S' ELSE 'N' END AS RICHIESTA_DISACCOPPIATO\r\n"
			+ "FROM TQDO_DES_PC_DESTINAZIONE I JOIN TPIANO_COLTURE C ON I.ID_PIANO_COLTURE = C.ID_PIANO_COLTURE AND I.ID_PARTICELLA = C.ID_PARTICELLA\r\n"
			+ "WHERE ID_DOMANDA = ? AND SYSDATE BETWEEN I.DT_INSERT AND I.DT_DELETE AND ? BETWEEN C.DT_INSERT AND C.DT_DELETE\r\n"
			+ "AND ? BETWEEN C.DT_INIZIO AND C.DT_FINE AND ID_DESTINAZIONE IN (SELECT  ID_DESTINAZIONE FROM TDES_DEF_DESTINAZIONE WHERE COD_DESTINAZIONE IN ('BPS1','GREEN'))";
	private final String sqlHasImpegnoAccoppiatoSuperfici = "SELECT CASE WHEN COUNT(*) > 0 THEN 'S' ELSE 'N' END AS RICHIESTA_SUPERFICI\r\n"
			+ "FROM TQDO_DES_PC_DESTINAZIONE I JOIN TPIANO_COLTURE C ON I.ID_PIANO_COLTURE = C.ID_PIANO_COLTURE AND I.ID_PARTICELLA = C.ID_PARTICELLA\r\n"
			+ "WHERE I.ID_DOMANDA = ? AND SYSDATE BETWEEN I.DT_INSERT AND I.DT_DELETE AND ? BETWEEN C.DT_INSERT AND C.DT_DELETE\r\n"
			+ "AND ? BETWEEN C.DT_INIZIO AND C.DT_FINE AND I.ID_DESTINAZIONE IN (SELECT  ID_DESTINAZIONE FROM TDES_DEF_DESTINAZIONE WHERE ID_GRUPPO = 'PACC' AND COD_DESTINAZIONE NOT IN ('OLIVE_BIO','FORAG'))";
	private final String sqlHasImpegnoAccoppiatoZootecnia = "SELECT CASE WHEN COUNT(*) > 0 THEN 'S' ELSE 'N' END AS RICHIESTA_ZOOTECNIA\r\n" + "FROM TQDO_DU_ALLEVAMENTO_INTERVENTO AI \r\n"
			+ "WHERE AI.ID_DOMANDA = ? AND SYSDATE BETWEEN AI.DT_INSERT AND AI.DT_DELETE\r\n" + "AND AI.ID_INTERVENTO IN (SELECT ID_INTERVENTO FROM TDU_DEF_INTERVENTO WHERE ID_GRUPPO = 'ZOOBPS')";
	private final String sqlGetRichiesteSuperficie = "SELECT DES.ID_DESTINAZIONE, DEF.ID_GRUPPO, DEF.COD_DESTINAZIONE, DEF.DS_DESTINAZIONE, SITI_INTERFACCIA_PCK.GET_LABEL_COMUNE ( PC.COD_NAZIONALE, SYSDATE ) AS COMUNE, PC.ID_PIANO_COLTURE, PC.COD_NAZIONALE, PC.FOGLIO, PC.PARTICELLA, PC.SUB,PC.ID_PARTICELLA, PC.SUP_DICHIARATA, DES.ID_PARC, DES.ID_ISOLA, DOMAGRAF.GETCODIISOLA( DES.ID_ISOLA )  AS COD_ISOLA,\r\n"
			+ " COL.ID_COLTURA, COL.CODUAGEA||'-'||COL.CODPAGEA||'-'||COL.CODVAGEA AS EXT_CODE, COL.DE_COLTURA, COL.COEFF_TARA, COL.CODI_PROD||'-'||COL.CODI_DEST_USO||'-'||COL.CODI_USO||'-'||COL.CODI_QUAL||'-'||COL.CODI_VARI AS COL_COLT5, COL.CODI_LIVE AS COD_LIVELLO, DES.SUP_IMPEGNO, L.DS_DECODIFICA AS DESC_MANTENIMENTO\r\n"
			+ " FROM TQDO_DES_PC_DESTINAZIONE DES JOIN TPIANO_COLTURE PC ON PC.ID_PIANO_COLTURE = DES.ID_PIANO_COLTURE JOIN COLTURE_CODICI_3_5 COL ON COL.ID_COLTURA = PC.ID_COLTURA_1 JOIN TDES_DEF_DESTINAZIONE DEF ON DEF.ID_DESTINAZIONE = DES.ID_DESTINAZIONE LEFT JOIN TDECODIFICA_LANGUAGE L ON L.sotto_codice = PC.SCO_CRIT_MANTENIMENTO AND L.codice = PC.COD_CRIT_MANTENIMENTO AND L.LOCALE = 'it'\r\n"
			+ " WHERE DES.ID_DOMANDA = ? AND DEF.ID_GRUPPO IN ('BPS', 'PACC') AND DES.DT_DELETE > SYSDATE AND DES.DT_INSERT BETWEEN PC.DT_INSERT AND PC.DT_DELETE ORDER BY DES.ID_DESTINAZIONE";
	/**
	 * Betty 18.10.2019: Query riscritta con Gabriele Ninfa (grazie!!!).
	 * La query deve estrarre i dati delle particelle di domanda, accorpati per particella e coltura, dando l'informazione complessiva
	 * della superficie eleggibile e della superficie sigeco
	 */
	private final String sqlGetRichiesteSuperficieEleg = "select id_domanda, id_gruppo, cod_destinazione, comune, cod_nazionale, foglio, particella, sub, id_particella, "
			+ "ext_code, col_colt5, cod_livello, sup_eleggibile, fg_an_coor, sup_sigeco"
			+ " from ("
				+ " select des.id_domanda, def.id_gruppo, def.cod_destinazione, siti_interfaccia_pck.get_label_comune(pc.cod_nazionale, sysdate) AS comune,"
				+ " pc.cod_nazionale, pc.foglio, pc.particella, pc.sub, pc.id_particella,"
				+ " col.coduagea || '-' || col.codpagea || '-' || col.codvagea AS ext_code,"
				+ " col.codi_prod || '-' || col.codi_dest_uso || '-' || col.codi_uso || '-' || col.codi_qual || '-' || col.codi_vari AS col_colt5,"
				+ " col.codi_live AS cod_livello, gis.sup_eleggibile AS sup_eleggibile, gis.fg_an_coor AS fg_an_coor, s.sup_sigeco AS sup_sigeco"
				+ " from tqdo_des_pc_destinazione des"
				+ " JOIN tpiano_colture pc ON pc.id_piano_colture = des.id_piano_colture"
				+ " JOIN colture_codici_3_5 col ON col.id_coltura = pc.id_coltura_1"
				+ " JOIN tcoltura_lang tcol ON col.id_coltura = tcol.id_coltura"
				+ " JOIN tdes_def_destinazione def ON def.id_destinazione = des.id_destinazione"
				+ " LEFT JOIN ("
					+ "SELECT g.id_domanda, g.id_particella, g.cod_coltura, g.codice_intervento, SUM(g.fg_supe_ammi) * 10000 AS sup_eleggibile, SUM(g.fg_an_coor) * 10000 AS fg_an_coor"
					+ " FROM siti.t_du_utilizzo_gis g"
					+ " GROUP BY g.id_domanda, g.id_particella, g.cod_coltura, g.codice_intervento"
				+ ") gis on gis.id_domanda = des.id_domanda and des.id_particella = gis.id_particella"
				+ " AND tcol.ext_code = gis.cod_coltura AND def.cod_destinazione = gis.codice_intervento"
				+ " LEFT JOIN ("
					+ "SELECT id_domanda, id_particella, cod_coltura, cod_intervento, SUM(fg_supe_ammi) AS sup_sigeco"
					+ " FROM tpaga_controlli_sigeco"
					+ " WHERE  sysdate BETWEEN dt_inizio AND dt_fine AND cod_intervento=026"
					+ " GROUP BY id_domanda, id_particella, cod_coltura, cod_intervento"
				+ ") s ON des.id_domanda = s.id_domanda AND des.id_particella = s.id_particella AND tcol.ext_code = s.cod_coltura"
				+ " WHERE des.id_domanda = ? AND def.id_gruppo IN ('BPS','PACC')"
				+ " AND des.dt_delete > sysdate AND des.dt_insert BETWEEN pc.dt_insert AND pc.dt_delete"
			+ ")"
			+ " GROUP BY id_domanda, id_gruppo, cod_destinazione, comune, cod_nazionale, foglio,  particella,  sub, id_particella,"
			+ " ext_code, col_colt5, cod_livello, sup_eleggibile, fg_an_coor, sup_sigeco";

	private final String sqlGetRichiesteZootecnia = "SELECT DISTINCT INT.ID_INTERVENTO, 'ZOO' AS SOSTEGNO, DINT.COD_INTERVENTO, DINT.DS_INTERVENTO, AL.ID_ALLEVAMENTO, AL.COD_ID_AZIENDALE, AL.COD_ID_BDN,\r\n"
			+ "AL.DE_ALLEVAMENTO, LSP.DS_DECODIFICA AS SPECIE, NULL AS COMUNE, NULL AS INDIRIZZO, NULL AS COD_FISC_PROPR, NULL AS DENOM_PROPR, NULL AS COD_FISC_DETENT, NULL AS DENOM_DETENT\r\n"
			+ "FROM TALLEVAMENTO AL JOIN TQDO_DU_ALLEVAMENTO_INTERVENTO INT ON INT.ID_ALLEVAMENTO = AL.ID_ALLEVAMENTO \r\n"
			+ "JOIN TDECODIFICA_LANGUAGE LSP ON LSP.SOTTO_CODICE = AL.SCO_SPECIE AND LSP.CODICE = 'SPEALL' JOIN TDU_DEF_INTERVENTO DINT ON DINT.ID_INTERVENTO = INT.ID_INTERVENTO\r\n"
			+ "WHERE INT.ID_DOMANDA = ? AND INT.DT_DELETE > SYSDATE";
	private final String sqlGetDichiarazioni = "SELECT DR.ID_RIGA, D.COD_DOCUMENTO, R.VAL_CHECK, R.VAL_DATA, R.VAL_NUM, R.VAL_TESTO, TRIM(L.DE_DECODIFICA) as DE_DECODIFICA, DR.ORDINE,\r\n"
			+ "(SELECT P.COGNOME || ' ' || P.NOME || ' - ' || P.CODICE_FISCALE FROM TPERSONA P WHERE P.ID_PERSONA = R.SCO_COMBO AND R.SCO_COMBO IS NOT NULL AND R.DT_INSERT BETWEEN P.DT_INSERT AND P.DT_DELETE) AS LEGALE\r\n"
			+ " FROM TDOC_DOCUMENTO_RIGA R JOIN TDOC_DOCUMENTO D ON D.ID_DOCUMENTO = R.ID_DOCUMENTO JOIN TDOC_DEF_RIGA DR ON DR.COD_DOCUMENTO = D.COD_DOCUMENTO AND R.ID_RIGA = DR.ID_RIGA JOIN TDECODIFICA_LANGUAGE L ON L.SOTTO_CODICE = R.SCO_DESCRIZIONE AND L.CODICE = 'RIGDES' \r\n"
			+ " WHERE D.COD_DOCUMENTO IN (:codiciDocumento) AND L.DE_DECODIFICA <> ' ' AND D.ID_DOMANDA = :numeroDomanda AND D.DT_DELETE > SYSDATE AND R.DT_DELETE > SYSDATE AND L.LOCALE = 'it' ORDER BY D.COD_DOCUMENTO, DR.ORDINE ASC\r\n";
	private final String sqlGetDatiPascolo = "SELECT DF.COD_PASCOLO, DF.DENOMINAZIONE, PS.UBA FROM TQDO_DU_PASCOLO PS JOIN TDU_DEF_PASCOLO DF ON DF.COD_PASCOLO = PS.COD_PASCOLO\r\n"
			+ " WHERE PS.ID_DOMANDA = ? AND PS.DT_DELETE > SYSDATE AND PS.DT_INSERT BETWEEN DF.DT_INSERT AND DF.DT_DELETE";
	private final String sqlGetDatiParticellePascolo = "SELECT DISTINCT PP.COD_PASCOLO, SITI_INTERFACCIA_PCK.GET_LABEL_COMUNE ( PP.COD_NAZIONALE, SYSDATE ) AS COMUNE, PP.COD_NAZIONALE, PP.FOGLIO, PP.PARTICELLA, PP.SUB, PC.ID_PARTICELLA \r\n"
			+ " FROM TQDO_DU_PASCOLO_PARTICELLA PP JOIN TQDO_DES_PC_DESTINAZIONE D ON PP.ID_DOMANDA = D.ID_DOMANDA JOIN TPIANO_COLTURE PC ON PC.ID_PIANO_COLTURE = D.ID_PIANO_COLTURE AND PP.COD_NAZIONALE = PC.COD_NAZIONALE AND PP.FOGLIO = PC.FOGLIO AND PP.PARTICELLA = PC.PARTICELLA AND PP.SUB = PC.SUB\r\n"
			+ " WHERE PP.ID_DOMANDA = ? AND PP.COD_PASCOLO = ? AND PP.DT_DELETE > SYSDATE AND D.DT_DELETE > SYSDATE AND D.DT_INSERT BETWEEN PC.DT_INSERT AND PC.DT_DELETE";

	private final String QUERY_DOMANDE_PSR = "SELECT modulo.cod_modulo, anno\r\n" + "  ,csv.CUAA, csv.ragi_soci, tdd.id_domanda, \r\n" + "  (SELECT MIN (dom.dt_insert)\r\n"
			+ "      FROM tdom_domanda dom\r\n" + "      where dom.SCO_STATO = '000010'\r\n"
			+ "      and dom.id_domanda = tdd.id_domanda) as DATA_DOMANDA, sco_settore, decod.ds_decodifica STATO_DOMANDA,\r\n"
			+ "      ( nvl(psr_calc_premio_richiesto.get_totale_premio_richiesto(tdd.id_domanda,'10','PSR_CONTROLLO'),0) +\r\n"
			+ "        nvl(psr_calc_premio_richiesto.get_totale_premio_richiesto(tdd.id_domanda,'11','PSR_CONTROLLO'),0) +\r\n"
			+ "        nvl(psr_calc_premio_richiesto.get_totale_premio_richiesto(tdd.id_domanda,'13','PSR_CONTROLLO'),0)) as IMPORTO_RICHIESTO\r\n" + "FROM  tdom_domanda tdd\r\n"
			+ "join  tdom_modulo_deco modulo\r\n" + "on modulo.id_modulo = tdd.id_modulo\r\n"
			+ "and cod_modulo IN (select cod_modulo from tdom_modulo where anno IN (:annoRiferimento) and sco_settore = 'P22014' and to_char(dt_fine, 'dd/mm/yyyy') = '31/12/9999')\r\n"
			+ "join  cons_sogg_viw csv\r\n" + "on  csv.pk_cuaa = tdd.id_soggetto\r\n" + "join tdecodifica decod\r\n" + "on decod.codice = cod_stato\r\n" + "AND decod.sotto_codice = sco_stato \r\n"
			+ "WHERE sysdate BETWEEN tdd.dt_insert AND tdd.dt_delete\r\n" + "and sysdate BETWEEN tdd.dt_inizio AND tdd.dt_fine\r\n" + "and sysdate between data_inizio_val and data_fine_val\r\n"
			+ "and MODULO.cod_modulo NOT IN (:psrRitTot)\r\n" + "and sco_stato not in ('000000','000001','000002','000003','000005','000007','000010','000011', '000014','000055', '000200')\r\n"
			+ "and cuaa IN (:ids)";
	private final String sqlGetIdCalcoloSostegno = "select max(vr.id_calcolo) as id_calcolo, cd.id_def_calcolo, cd.cod_calcolo\r\n" + " from vclc_registro_params vr \r\n"
			+ " join tclc_registro tr on tr.id_calcolo = vr.id_calcolo join tclc_def_calcolo cd on cd.id_def_calcolo = tr.id_def_calcolo\r\n"
			+ " where vr.valore = ? and vr.nome= 'ID_DOMANDA' and cd.cod_calcolo = ?\r\n"
			+ " and tr.dt_calcolo < (select max(nvl(mov.dt_movimento, sysdate)) from tdom_movimento mov where mov.id_domanda = ? and mov.cod_workflow = ?)\r\n"
			+ " group by  cd.id_def_calcolo, cd.cod_calcolo";

	private final String sqlGetVariabiliCalcolo = "select variabile, ds_variabile, valore, cod_gruppo, ordine from VCLC_RISULTATO_CALCOLO clc where clc.id_calcolo = ? order by ordine asc";

	// Restituisce se il cuaa ha presentato domanda (valida) per il 2017
	private final String sqlGetDomandaUnica2017 = "SELECT distinct (id_domanda) FROM tdom_domanda d JOIN CONS_SOGG_VIW S ON D.ID_SOGGETTO = S.PK_CUAA\r\n"
			+ "AND d.sco_stato NOT IN ('000000', '000001', '000002', '000003', '000010', '000011', '000055', '000200') \r\n"
			+ "AND d.id_modulo IN (SELECT id_modulo FROM TDOM_MODULO_DECO WHERE sco_settore = 'PI2014' AND anno = 2017) \r\n" + "and d.dt_delete > sysdate and s.cuaa = ?";

	private final String sqlGetDataMorte = "SELECT CONS.D_MORTE FROM CONS_SOGG_VIW CONS JOIN TDOM_DOMANDA D ON CONS.PK_CUAA = D.ID_SOGGETTO WHERE SYSDATE BETWEEN D.DT_INSERT AND D.DT_DELETE AND D.ID_DOMANDA = ?";

	private final String getIbanValido = "SELECT COUNT(*) FROM TQDO_MOD_PAGAMENTO TQDO JOIN TMOD_PAGAMENTO_IBAN IBAN ON TQDO.ID_MOD_PAGAMENTO = IBAN.ID_MOD_PAGAMENTO AND TQDO.ID_SOGGETTO = IBAN.ID_SOGGETTO WHERE ID_DOMANDA = ?  AND SYSDATE BETWEEN TQDO.DT_INSERT AND TQDO.DT_DELETE AND SYSDATE BETWEEN TQDO.DT_INIZIO AND TQDO.DT_FINE AND SYSDATE BETWEEN IBAN.DT_INSERT AND IBAN.DT_DELETE AND SYSDATE BETWEEN IBAN.DT_INIZIO AND IBAN.DT_FINE";

	private final String getDomandaSospesaAgea = "select count(*) from tcontrolli_agg c join tdom_domanda d on c.id_soggetto = d.id_soggetto where sysdate between c.dt_insert and c.dt_delete and sysdate between c.dt_inizio and c.dt_fine and sysdate between d.dt_insert and d.dt_delete AND c.sco_blocco = '000001' and id_domanda = ?";

	private final String getDomandaSospesaAgeaBySoggetto = "select count(*) \r\n" + "from tcontrolli_agg c join tdom_domanda d on c.id_soggetto = d.id_soggetto \r\n"
			+ "where sysdate between c.dt_insert and c.dt_delete and sysdate between c.dt_inizio and c.dt_fine \r\n"
			+ "and sysdate between d.dt_insert and d.dt_delete AND c.sco_blocco = '000001' and c.ID_SOGGETTO= (SELECT PK_CUAA FROM CONS_SOGG_VIW WHERE CUAA = ?)";

	private final String getIban = "SELECT iban.cod_iban FROM tqdo_mod_pagamento tqdo JOIN tmod_pagamento_iban iban ON tqdo.id_mod_pagamento = iban.id_mod_pagamento AND tqdo.id_soggetto = iban.id_soggetto WHERE id_domanda = ? AND SYSDATE BETWEEN tqdo.dt_insert AND tqdo.dt_delete AND SYSDATE BETWEEN tqdo.dt_inizio AND tqdo.dt_fine AND SYSDATE BETWEEN iban.dt_insert AND iban.dt_delete";

	private final String getIbanErede = "SELECT iban.COD_IBAN\r\n" + "FROM tmod_pagamento_iban iban\r\n" + "JOIN CONS_SOGG_VIW cons\r\n" + " on cons.PK_CUAA = iban.id_soggetto\r\n"
			+ "WHERE cons.CUAA = ? \r\n" + "AND SYSDATE BETWEEN iban.dt_insert AND iban.dt_delete\r\n" + "AND fg_default = 'S'";

	private final String existDomandaPerSettore = "select count(*) from tdom_domanda d join cons_sogg_viw s on d.id_soggetto = s.pk_cuaa join tdom_modulo_deco m on d.id_modulo = m.id_modulo where sysdate between s.data_inizio_val and s.data_fine_val and sysdate between d.dt_insert and d.dt_delete and d.sco_stato not in ('000001', '000002', '000003', '000011', '000200') and m.sco_settore = ? and m.anno = ? and s.cuaa = ?";

	private final String DATI_COMUNE = "select distinct DENO_REGI,SIGLA_PROV,DENO_PROV,ISTATP||ISTATC as CODICE_ISTAT from siti.siticomu WHERE CODI_FISC_LUNA = :codIstat";

	private final String SQL_DATI_EREDE = "SELECT DISTINCT P.* FROM TPERSONA P\r\n" + "    JOIN CONS_SOGG_VIW CONS ON P.ID_SOGGETTO = CONS.PK_CUAA\r\n"
			+ "    JOIN TDOM_DOMANDA D ON CONS.PK_CUAA = D.ID_SOGGETTO\r\n" + "    WHERE SYSDATE BETWEEN D.DT_INSERT AND D.DT_DELETE AND CONS.D_MORTE is not null\r\n"
			+ "    AND P.cod_ruolo = 'RUOPER'\r\n" + "        AND P.sco_ruolo = '001000'\r\n" + "        AND P.ID_SOGGETTO = (select distinct ID_SOGGETTO from TDOMANDA where ID_DOMANDA = ? )\r\n"
			+ "        AND SYSDATE BETWEEN P.dt_insert AND P.dt_delete\r\n" + "        AND SYSDATE BETWEEN P.dt_inizio AND P.dt_fine   ";
	
	private final String getIbanFascicolo = "SELECT iban.cod_iban FROM tmod_pagamento_iban iban " + 
											"INNER JOIN tdom_domanda dom ON iban.id_soggetto = dom.id_soggetto " + 
											"WHERE SYSDATE BETWEEN iban.dt_insert AND iban.dt_delete " + 
											"AND SYSDATE BETWEEN iban.dt_inizio AND iban.dt_fine " + 
											"AND iban.fg_default = 'S' " + 
											"AND SYSDATE BETWEEN dom.dt_insert AND dom.dt_delete " + 
											"AND dom.id_domanda = ?";

	@Autowired
	private DataSource dataSource;

	@Autowired
	private DecodificaSettorePAC decodificaSettorePAC;

	@Autowired
	private DecodificaPACSettore decodificaPACSettore;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	/**
	 * Metodo per il recupero da sistema AGS dei dati identificativi della domanda. Non recupera i dati di protocollazione.
	 * 
	 * @param numeroDomanda
	 *            identificativo della domanda per la quale procedere al recupero delle informazioni
	 * @return DTO valorizzato con le informazioni recuperate dal sistema AGS
	 */
	@Override
	public InfoGeneraliDomanda getInfoGeneraliDomanda(Long numeroDomanda) {
		List<Object> params = new ArrayList<Object>();
		params.add(numeroDomanda);
		try {
			return (InfoGeneraliDomanda) getJdbcTemplate().queryForObject(this.sqlGetInfoGeneraliDomanda, params.toArray(), new InfoGeneraliRowMapper(decodificaSettorePAC));
		} catch (NoResultException e) {
			logger.warn("getInfoGeneraliDomanda: NoResultException per {}", numeroDomanda);
			return null;
		} catch (EmptyResultDataAccessException e) {
			logger.warn("getInfoGeneraliDomanda: EmptyResultDataAccessException per {}", numeroDomanda);
			return null;
		}
	}

	/**
	 * Metodo per il recupero da sistema AGS dei dati identificativi della domanda. Non recupera i dati di protocollazione.
	 * 
	 * @param numeroDomanda
	 *            identificativo della domanda per la quale procedere al recupero delle informazioni
	 * @return DTO valorizzato con le informazioni recuperate dal sistema AGS
	 */
	//@Override
	public DatiDomanda getDatiDomanda(Long numeroDomanda) {
		List<Object> params = new ArrayList<Object>();
		params.add(numeroDomanda);
		try {
			DatiDomanda domanda = getJdbcTemplate().queryForObject(this.sqlGetInfoGeneraliDomanda, params.toArray(), new DatiDomandaRowMapper());
			List<SostegniSuperficie> superficie = getSostegniSuperficie(numeroDomanda);
			List<SuperficieImpegnata> superficiBPS = superficie.stream()
				.filter(s -> "BPS".equals(s.getCodIntervento()))
				.map(s -> convertiInSuperficieImpegnataBPS(s))
				.collect(Collectors.toList());
			if (superficiBPS != null && !superficiBPS.isEmpty()) {
				DatiDisaccoppiato disaccoppiato = new DatiDisaccoppiato();
				disaccoppiato.setSuperficiImpegnate(superficiBPS);
				domanda.setDisaccoppiato(disaccoppiato);
			}
			
			return domanda;
		} catch (NoResultException e) {
			logger.warn("getDatiDomanda: NoResultException per {}", numeroDomanda);
			return null;
		} catch (EmptyResultDataAccessException e) {
			logger.warn("getDatiDomanda: EmptyResultDataAccessException per {}", numeroDomanda);
			return null;
		}
	}

	private SuperficieImpegnata convertiInSuperficieImpegnataBPS(SostegniSuperficie superficie) {
		SuperficieImpegnata result = new SuperficieImpegnata();
		result.setColtura(convertiInColtura(superficie));
		result.setParticella(superficie.getParticella());
		result.setIdParcella(superficie.getIdParcella());
		result.setIdIsola(superficie.getIdIsola());
		result.setCodIsola(superficie.getCodIsola());
		result.setSupDichiarata(superficie.getSupDichiarata());
		result.setSupImpegnata(superficie.getSupImpegnata());
		return result;
		
	}

	private Coltura convertiInColtura(SostegniSuperficie superficie) {
		Coltura result = new Coltura();
		result.setIdPianoColture(superficie.getIdPianoColture());
		result.setIdColtura(superficie.getIdColtura());
		result.setCodColtura3(superficie.getCodColtura3());
		result.setCodColtura5(superficie.getCodColtura5());
		result.setCodLivello(superficie.getCodLivello());
		result.setDescrizioneColtura(superficie.getDescColtura());
		result.setCoefficienteTara(superficie.getCoeffTara());
		result.setSuperficieDichiarata(superficie.getSupDichiarata());
		result.setDescMantenimento(superficie.getDescMantenimento());
		return result;
	}

	/**
	 * Metodo per il recupero da sistema AGS dei dati di protocollazione della domanda. Non recupera i dati identificativi.
	 * 
	 * @param numeroDomanda
	 *            identificativo della domanda per la quale procedere al recupero dei dati di protocollazione
	 * @return DTO valorizzato con i dati recuperati dal sistema AGS
	 */
	@Override
	public ControlliPresentazione getDatiProtocolloDomanda(Long numeroDomanda) {
		List<Object> params = new ArrayList<Object>();
		params.add(numeroDomanda);
		try {
			ControlliPresentazione ret = (ControlliPresentazione) getJdbcTemplate().queryForObject(this.sqlGetDatiProtocollazioneDomanda, params.toArray(),
					new BeanPropertyRowMapper(ControlliPresentazione.class));
			return ret;
		} catch (NoResultException e) {
			logger.warn("getDatiProtocolloDomanda: NoResultException per {}", numeroDomanda);
			return null;
		} catch (EmptyResultDataAccessException e) {
			logger.warn("getDatiProtocolloDomanda: EmptyResultDataAccessException per {}", numeroDomanda);
			return null;
		}
	}

	@Override
	public Long countDomande(BigDecimal annoRiferimento, String codicePac, String tipoDomanda, String moduloRitiroTotale) {
		String codiceStatoProtocollata = "000015";
		List<Object> params = new ArrayList<Object>();
		params.add(annoRiferimento);
		DecodificaPACSettore.PACKey pacKey = new DecodificaPACSettore.PACKey(codicePac, tipoDomanda);
		params.add(decodificaPACSettore.getValori().get(pacKey));
		params.add(codiceStatoProtocollata);
		params.add(moduloRitiroTotale);

		try {
			Long ret = getJdbcTemplate().queryForObject(this.sqlCountDomandeProtocollate, params.toArray(), Long.class);
			return ret;
		} catch (NoResultException e) {
			logger.warn("countDomande: NoResultException {}, {} {}, {}", annoRiferimento, codicePac, tipoDomanda, moduloRitiroTotale);
			return null;
		}
	}

	/**
	 * Metodo che esegue la movimentazione di una domanda nel sistema AGS, storicizza lo stato attuale della domanda inserisce un nuovo record con lo stato passato come parametro successivamente
	 * scrive nelle tabelle di log di movimento per tenere traccia del cambio di stato
	 * 
	 * @param numeroDomanda
	 * @param tipoMovimento
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String eseguiMovimentazioneDomanda(Long numeroDomanda, String tipoMovimento) throws NoResultException {
		logger.debug("chiamata eseguiMovimentazioneDomanda {}, {}", numeroDomanda, tipoMovimento);
		String result = Messaggi.OK_MOVIMENTO.getMessaggi();
		try {
			DatiDomandaAgs domanda = getDatiDomandaAgs(numeroDomanda);
			if (domanda != null) {
				DatiMovimentoAgs movimento = getDatiMovimento(domanda.getGroupId(), tipoMovimento, domanda.getScoStato());
				if (movimento != null && domanda.getScoStato().equalsIgnoreCase(movimento.getScoStatusFrom())) {
					if (storicizzaStatoDomanda(domanda.getPkid(), domanda.getIdDomanda()) > 0) {
						if (inserisciStatoDomanda(domanda, movimento.getScoStatusTo()) == 1) {
							domanda = getDatiDomandaAgs(numeroDomanda);
							if (inserisciLogMovimentoDomanda(domanda, tipoMovimento, movimento.getWorkflowId()) != 1) {
								result = Messaggi.E_SALVATAGGIO_MOVIMENTO.getMessaggi();
							}
						}
					}
				} else {
					result = Messaggi.E_MOVIMENTO_DOMANDA.getMessaggi();
				}
			} else {
				result = Messaggi.E_CARICAMENTO_DATI_DOMANDA.getMessaggi();
			}
		} catch (Exception e) {
			logger.error("eseguiMovimentazioneDomanda error: {}, {}", numeroDomanda, tipoMovimento, e);
			throw new NoResultException(e.getLocalizedMessage());
		}
		return result;
	}

	/**
	 * storicizza lo stato di una domanda
	 * 
	 * @param pkid
	 * @param numeroDomanda
	 * @return
	 */
	private int storicizzaStatoDomanda(BigDecimal pkid, BigDecimal numeroDomanda) {
		logger.debug("storicizzaStatoDomanda {}", numeroDomanda);
		List<Object> params = new ArrayList<Object>();
		params.add(pkid);
		params.add(numeroDomanda);
		try {
			int result = getJdbcTemplate().update(sqlStoricizzaStatoAttualeDomanda, params.toArray());
			return result;
		} catch (EmptyResultDataAccessException e) {
			logger.warn("storicizzaStatoDomanda: EmptyResultDataAccessException {}", numeroDomanda);
			return 0;
		}
	}

	/**
	 * inserisce un nuovo stato per una domanda
	 * 
	 * @param domanda
	 * @param statoDomanda
	 * @return
	 */
	private int inserisciStatoDomanda(DatiDomandaAgs domanda, String statoDomanda) {
		logger.debug("inserisciStatoDomanda {}, {}", domanda.getIdDomanda(), statoDomanda);
		List<Object> params = new ArrayList<Object>();
		try {
			Long pkid = getJdbcTemplate().queryForObject("SELECT SDOM_DOMANDA_PKID.NEXTVAL FROM DUAL", null, Long.class);
			params.add(pkid);
			params.add(domanda.getIdSoggetto());
			params.add(domanda.getIdDomanda());
			params.add(domanda.getIdDomandaRettificata());
			params.add(domanda.getIdModulo());
			params.add(statoDomanda);
			int result = getJdbcTemplate().update(sqlInserisciNuovoStatoDomanda, params.toArray());
			return result;
		} catch (EmptyResultDataAccessException e) {
			logger.warn("inserisciStatoDomanda: {}, {}", domanda.getIdDomanda(), statoDomanda);
			return 0;
		}
	}

	/**
	 * carica i dettagli dello stato attuale di una domanda AGS
	 * 
	 * @param numeroDomanda
	 * @return
	 */
	private DatiDomandaAgs getDatiDomandaAgs(Long numeroDomanda) {
		logger.debug("getDatiDomandaAgs {}", numeroDomanda);
		List<Object> params = new ArrayList<Object>();
		params.add(numeroDomanda);
		try {
			DatiDomandaAgs ret = (DatiDomandaAgs) getJdbcTemplate().queryForObject(this.sqgGetDatiDomandaAgs, params.toArray(), new BeanPropertyRowMapper(DatiDomandaAgs.class));
			return ret;
		} catch (EmptyResultDataAccessException e) {
			logger.error("getDatiDomandaAgs error: ", e);
			return null;
		}
	}

	/**
	 * carica le informazioni su un movimento che deve essere eseguito
	 * 
	 * @param groupId
	 * @param statoDomanda
	 * @param movimento
	 * @return
	 */
	private DatiMovimentoAgs getDatiMovimento(BigDecimal groupId, String movimento, String statoDomanda) {
		logger.debug("getDatiMovimento {}, {}, {}", groupId, movimento, statoDomanda);
		List<Object> params = new ArrayList<Object>();
		params.add(groupId);
		params.add(movimento);
		params.add(statoDomanda);
		try {
			DatiMovimentoAgs ret = (DatiMovimentoAgs) getJdbcTemplate().queryForObject(this.sqlGetDatiMovimento, params.toArray(), new BeanPropertyRowMapper(DatiMovimentoAgs.class));
			return ret;
		} catch (EmptyResultDataAccessException e) {
			logger.warn("Il movimento " + movimento + " non è disponibile dallo stato " + statoDomanda + " (groupId = " + groupId + ")");
			logger.debug("getDatiMovimento error: ", e);
			return null;
		}
	}

	/**
	 * scrivi i riferimenti di un movimento per passaggio di stato che è stato eseguito
	 * 
	 * @param domanda
	 * @param movimento
	 * @return
	 */
	private int inserisciLogMovimentoDomanda(DatiDomandaAgs domanda, String movimento, BigDecimal workflowId) {
		logger.debug("inserisciLogMovimentoDomanda {}, {}", domanda.getIdDomanda(), movimento);
		List<Object> params = new ArrayList<Object>();
		try {
			Long transitionId = getJdbcTemplate().queryForObject(this.sqlGetTransitionId, null, Long.class);
			params.add(transitionId);
			params.add(workflowId);
			int result = getJdbcTemplate().update(sqlInserisciTWorLogTransition, params.toArray());

			params.clear();
			params.add(domanda.getIdDomanda());
			params.add(transitionId);
			result = getJdbcTemplate().update(sqlInserisciTDomLogTransition, params.toArray());
			return result;
		} catch (EmptyResultDataAccessException e) {
			logger.warn("inserisciLogMovimentoDomanda EmptyResultDataAccessException: {}, {}, {}",
					domanda.getIdDomanda(), movimento, workflowId);
			return -1;
		}

	}

	/**
	 * Metodo per il recupero delle informazioni di sintesi degli impegni presenti nella domanda passata come parametro
	 *
	 * @param numeroDomanda
	 *            numero della domanda per la quale recuperare la sinesi degli imepgni
	 * @return DTO che modella la sintesi degli impegni presenti in domanda
	 */
	@Override
	public SintesiRichieste getSintesiRichiesteDomandaUnica(Long numeroDomanda, InfoGeneraliDomanda infoGeneraliDomanda) {
		SintesiRichieste sintesi = new SintesiRichieste();

		try {
			InfoGeneraliDomanda infoDomanda = infoGeneraliDomanda;
			if (infoDomanda == null) {
				infoDomanda = getInfoGeneraliDomanda(numeroDomanda);
			}
			List<Object> params = new ArrayList<Object>();
			params.add(infoDomanda.getCuaaIntestatario());
			params.add(infoDomanda.getCampagna());
			params.add("PI2014");
			// 2. Determino se la domanda è grafica o alfanumerica
			boolean isDomandaAlfanumerica = (getJdbcTemplate().queryForObject(this.sqlIsDomandaAlfanumerica, params.toArray(), Long.class) > 0);
			params.clear();
			params.add(numeroDomanda);
			// 3. Determino la data di riferimento opportuna
			if (isDomandaAlfanumerica && infoDomanda.getCodModulo().equalsIgnoreCase("BPS_RITPRZ_".concat(infoDomanda.getCampagna().toString()))) {
				params.add(infoDomanda.getDataPresentazOriginaria());
				params.add(infoDomanda.getDataPresentazOriginaria());
			} else {
				params.add(infoDomanda.getDataPresentazione());
				params.add(infoDomanda.getDataPresentazione());
			}
			// 4 recupero le info di sintesi degli impegni
			sintesi.setRichiestaDisaccoppiato(getJdbcTemplate().queryForObject(this.sqlHasImpegnoDisaccoppiato, params.toArray(), String.class).equalsIgnoreCase("S"));
			sintesi.setRichiestaSuperfici(getJdbcTemplate().queryForObject(this.sqlHasImpegnoAccoppiatoSuperfici, params.toArray(), String.class).equalsIgnoreCase("S"));

			params.clear();
			params.add(numeroDomanda);
			sintesi.setRichiestaZootecnia(getJdbcTemplate().queryForObject(this.sqlHasImpegnoAccoppiatoZootecnia, params.toArray(), String.class).equalsIgnoreCase("S"));
		} catch (EmptyResultDataAccessException e) {
			logger.warn("recuperaSintesiImpegniDomanda EmptyResultDataAccessException: {}", numeroDomanda);
		}

		return sintesi;
	}


	/**
	 * Metodo per il recupero delle informazioni di sintesi degli impegni presenti nella domanda passata come parametro
	 * 
	 * @param numeroDomanda
	 *            numero della domanda per la quale recuperare la sinesi degli imepgni
	 * @return DTO che modella la sintesi degli impegni presenti in domanda
	 */
	@Override
	public SintesiRichieste getSintesiRichiesteDomandaUnica(Long numeroDomanda) {
		return getSintesiRichiesteDomandaUnica(numeroDomanda, null);
	}

	@Override
	public List<SostegniSuperficie> getSostegniSuperficie(Long numeroDomanda) throws NoResultException {
		logger.debug("chiamata getSostegniSuperficie {}", numeroDomanda);
		List<Object> params = new ArrayList<Object>();
		params.add(numeroDomanda);
		try {
			List<SostegniSuperficie> resultList = getJdbcTemplate().query(this.sqlGetRichiesteSuperficie, params.toArray(), new SostegniSuperficiRowMapper());
			return resultList;
		} catch (NoResultException e) {
			logger.warn("getSostegniSuperficie: NoResultException {}", numeroDomanda);
			throw e;
		}
	}

	@Override
	public List<InfoEleggibilitaParticella> getInfoEleggibilitaPartRichieste(Long numeroDomanda) throws NoResultException {
		logger.info("chiamata getInfoEleggibilitaPartRichieste {}", numeroDomanda);
		List<Object> params = new ArrayList<Object>();
		params.add(numeroDomanda);
		try {
			List<InfoEleggibilitaParticella> resultList = getJdbcTemplate().query(this.sqlGetRichiesteSuperficieEleg, params.toArray(), new InfoEleggibilitaParticellaRowMapper());
			return resultList;
		} catch (NoResultException e) {
			logger.warn("getInfoEleggibilitaPartRichieste: NoResultException {}", numeroDomanda);
			throw e;
		}
	}

	/**
	 * Estrazione dei dati per impegni su allevamenti che si trovano nella scheda Accoppiato Zootecnia
	 * 
	 * @param numeroDomanda
	 * @return
	 */
	@Override
	public List<SostegniAllevamento> getSostegniAllevamento(Long numeroDomanda) {
		logger.debug("chiamata getSostegniAllevamento {}", numeroDomanda);
		List<Object> params = new ArrayList<Object>();

		params.add(numeroDomanda);

		try {
			return getJdbcTemplate().query(this.sqlGetRichiesteZootecnia, params.toArray(), new SostegniAllevamentoRowMapper());

		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getSostegniAllevamento: NoResultException | EmptyResultDataAccessException {}", numeroDomanda);
			throw e;
		}
	}

	@Override
	public List<DatiPascolo> getDatiPascolo(Long numeroDomanda) {
		List<Object> params = new ArrayList<>();
		params.add(numeroDomanda);
		try {
			List<DatiPascolo> listaPascoli = getJdbcTemplate().query(this.sqlGetDatiPascolo, params.toArray(), new DatiPascoloRowMapper());

			listaPascoli.forEach(pascolo -> {
				params.clear();
				params.add(numeroDomanda);
				params.add(pascolo.getCodPascolo());
				List<Particella> listaParticelle = getJdbcTemplate().query(this.sqlGetDatiParticellePascolo, params.toArray(), new ParticelleRowMapper());
				pascolo.setParticelle(listaParticelle);
			});
			return listaPascoli;
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.error("getDatiPascolo: NoResultException | EmptyResultDataAccessException {}", numeroDomanda);
			throw e;
		}
	}

	@Override
	public List<DomandaPsr> getDomandePsr(DomandeCollegatePsrFilter domandeCollegatePsrFilter) {
		/*
		try {
			//query che impiega molto tempo, anche mezz'ora
			//messo setAutoCommit(false) per evitare problema java.sql.SQLException: ORA-01002: fetch out of sequence
			//e altri problemi di transazionalità
			getConnection().setAutoCommit(false);
		} catch (CannotGetJdbcConnectionException | SQLException e) {
			logger.warn("Tentativo di setAutoCommit fallito",e);
		}
		*/
		List<String> psrRitiroTotale = new ArrayList<>();
		List<Integer> anni = new ArrayList<>();
		//Inseriti anni campagna per richiesta business: AM-03-02-02-01REV
		domandeCollegatePsrFilter.getAnniCampagna().forEach(anno -> {
			psrRitiroTotale.add(PsrRitiroTotale.PSR_RIT_TOT_.toString().concat(anno.toString()));
			anni.add(anno);
		});
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", domandeCollegatePsrFilter.getCuaa());
		parameters.addValue("psrRitTot", psrRitiroTotale);
		parameters.addValue("annoRiferimento", anni);
		List queryResult = namedParameterJdbcTemplate.query(QUERY_DOMANDE_PSR, parameters, new DomandaPsrMapper());
		return queryResult;
	}
	
	@Override
	public DatiComune getDatiComune(String codIstat) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("codIstat", codIstat);
		List<DatiComune> datiComune = namedParameterJdbcTemplate.query(DATI_COMUNE, parameters, new DatiComuneMapper());
		return datiComune.isEmpty() ? null : datiComune.get(0);
	}

	private Long getNumeroDomandaDU2017(String cuaa) {
		List<Object> params = new ArrayList<Object>();
		params.add(cuaa);
		try {
			Long result = getJdbcTemplate().queryForObject(this.sqlGetDomandaUnica2017, params.toArray(), Long.class);
			return result;
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getNumeroDomandaDU2017: NoResultException | EmptyResultDataAccessException {}", cuaa);
			return null;
		}

	}

	@Override
	public CalcoloSostegnoAgs getCalcoloSostegnoDomandaUnica(String cuaa, String codCalcolo, String movLiquidazione) {
		List<Object> params = new ArrayList<Object>();
		params.add(cuaa);
		try {
			Long domandaPrecedente = getNumeroDomandaDU2017(cuaa);
			if (domandaPrecedente != null) {
				params.clear();
				params.add(domandaPrecedente.toString());
				params.add(codCalcolo);
				params.add(domandaPrecedente);
				params.add(movLiquidazione);
				CalcoloSostegnoAgs calcoloSostegno = (CalcoloSostegnoAgs) getJdbcTemplate().queryForObject(
						this.sqlGetIdCalcoloSostegno, params.toArray(), new CalcoloSostegnoRowMapper());

				if (calcoloSostegno != null) {
					params.clear();
					params.add(calcoloSostegno.getIdCalcolo());
					List<VariabileSostegnoAgs> listaVariabili = getJdbcTemplate().query(
							this.sqlGetVariabiliCalcolo, params.toArray(), new VariabiliCalcoloRowMapper());
					calcoloSostegno.setVariabiliCalcolo(listaVariabili);
				}
				return calcoloSostegno;
			}

		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn(" nessun calcolo trovato NoResultException | EmptyResultDataAccessException {} {} {}",
					cuaa, codCalcolo, movLiquidazione);
		}
		return null;
	}

	@Override
	public Date getDataMorte(Long idDomanda) {
		List<Object> params = new ArrayList<Object>();
		params.add(idDomanda);
		try {
			Date result = getJdbcTemplate().queryForObject(this.sqlGetDataMorte, params.toArray(), Date.class);
			return result;
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getDataMorte: ", e);
			return null;
		}
	}

	@Override
	public Long getIbanValido(Long idDomanda) {
		List<Object> params = new ArrayList<Object>();
		params.add(idDomanda);
		try {
			Long result = getJdbcTemplate().queryForObject(this.getIbanValido, params.toArray(), Long.class);
			return result;
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getIbanValido: NoResultException | EmptyResultDataAccessException {}", idDomanda);
			return Long.valueOf(0);
		}
	}

	@Override
	public Long getDomandaSospesaAgea(Long idDomanda) {
		List<Object> params = new ArrayList<Object>();
		params.add(idDomanda);
		try {
			Long result = getJdbcTemplate().queryForObject(this.getDomandaSospesaAgea, params.toArray(), Long.class);
			return result;
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getDomandaSospesaAgea: NoResultException | EmptyResultDataAccessException {}", idDomanda);
			return null;
		}
	}

	@Override
	public Long getDomandaSospesaAgeaBySoggetto(String cuaa) {
		List<Object> params = new ArrayList<Object>();
		params.add(cuaa);
		try {
			Long result = getJdbcTemplate().queryForObject(this.getDomandaSospesaAgeaBySoggetto, params.toArray(), Long.class);
			return result;
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getDomandaSospesaAgeaBySoggetto: NoResultException | EmptyResultDataAccessException {}", cuaa);
			return null;
		}
	}

	@Override
	public String getIban(Long idDomanda) {
		List<Object> params = new ArrayList<Object>();
		params.add(idDomanda);
		try {
			String result = getJdbcTemplate().queryForObject(this.getIban, params.toArray(), String.class);
			return result;
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getIban: NoResultException | EmptyResultDataAccessException {}", idDomanda);
			return null;
		}
	}

	@Override
	public String getIbanErede(String cuaa) {
		List<Object> params = new ArrayList<Object>();
		params.add(cuaa);
		try {
			String result = getJdbcTemplate().queryForObject(this.getIbanErede, params.toArray(), String.class);
			return result;
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getDomandaSospesaAgea: NoResultException | EmptyResultDataAccessException {}", cuaa);
			return null;
		}
	}

	@Override
	public Boolean checkExistDomandaPerSettore(String codicePac, String tipoDomanda, Long anno, String cuaa) {

		List<Object> params = new ArrayList<Object>();
		DecodificaPACSettore.PACKey pacKey = new DecodificaPACSettore.PACKey(codicePac, tipoDomanda);
		params.add(decodificaPACSettore.getValori().get(pacKey));
		params.add(anno);
		params.add(cuaa);

		try {
			Long result = getJdbcTemplate().queryForObject(this.existDomandaPerSettore, params.toArray(), Long.class);
			return result > 0;
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("checkExistDomandaPerSettore: NoResultException | EmptyResultDataAccessException {} {} {} {}", codicePac, tipoDomanda, anno, cuaa);
			return null;
		}

	}

	@Override
	public DatiErede getDatiErede(Long numeroDomanda) {
		List<Object> params = new ArrayList<Object>();
		params.add(numeroDomanda);
		try {
			DatiErede ret = (DatiErede) getJdbcTemplate().queryForObject(SQL_DATI_EREDE, params.toArray(), new BeanPropertyRowMapper(DatiErede.class));
			return ret;
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getDatiErede: NoResultException | EmptyResultDataAccessException per {}", numeroDomanda);
			return null;	
		}
	}

	@Override
	@Transactional
	public String forzaMovimentazioneDomanda(Long numeroDomanda, String tipoMovimento) throws Exception {
		logger.debug("chiamata forzaMovimentazioneDomanda {}, {}", numeroDomanda, tipoMovimento);
		String result = Messaggi.OK_MOVIMENTO.getMessaggi();
		getJdbcTemplate().update("CALL APPLICATION_ROUTINE_PCK.MOVIMENTA_DOMANDA(?, ?)", numeroDomanda, tipoMovimento);
		return result;
	}

	@Override
	public List<Long> getListaDomandePerStato(Integer annoRiferimento, String settore, List<String> stati, String moduloRitiroTotale) {
		logger.debug("chiamata getListaDomandePerStato {}, {}, {}, {}", annoRiferimento, settore, stati.toArray().toString(), moduloRitiroTotale);

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("stati", stati);
		parameters.addValue("annoRiferimento", annoRiferimento);
		parameters.addValue("settore", settore);
		parameters.addValue("moduloRitiroTotale", moduloRitiroTotale);
		try {
			return namedParameterJdbcTemplate.queryForList(this.sqlGetListaDomandePerStato, parameters, Long.class);
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getListaDomandePerStato NoResultException | EmptyResultDataAccessException per {}, {}, {}, {}",
					annoRiferimento, settore, stati.toArray().toString(), moduloRitiroTotale);
			return null;
		}
	}

	@Override
	public List<Long> getListaDomandeProtocollate(Integer campagna, String moduloRitiroTotale) {
		logger.debug("chiamata getListaDomandeProtocollate {}, {}", campagna, moduloRitiroTotale);

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("campagna", campagna);
		parameters.addValue("moduloRitiroTotale", moduloRitiroTotale);
		try {
			return namedParameterJdbcTemplate.queryForList(this.sqlGetListaDomandeProtocollate, parameters, Long.class);
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getListaDomandeProtocollate: NoResultException | EmptyResultDataAccessException per  per modulo {} campagna {}",
					moduloRitiroTotale, campagna);
			return null;
		}
	}


	@Override
	public List<DichiarazioniDomandaUnica> getDichiarazioniDomanda(Long numeroDomanda, Integer campagna) throws NoResultException {
		logger.debug("chiamata getDichiarazioniDomanda {} {}", numeroDomanda, campagna);

		List<String> codiciDocumento = Arrays.asList(DichiarazioniDomandaUnicaEnum.DU_DICH_.toString(), DichiarazioniDomandaUnicaEnum.DU_DATI_AGG_.toString(),
				DichiarazioniDomandaUnicaEnum.DU_GIOVANE_AGR_.toString(), DichiarazioniDomandaUnicaEnum.DU_RISERVA_.toString()).stream().map(x -> x.concat(String.valueOf(campagna)))
				.collect(Collectors.toList());
		codiciDocumento.add(DichiarazioniDomandaUnicaEnum.DU_SMALL_FARM.toString());

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("codiciDocumento", codiciDocumento);
		params.addValue("numeroDomanda", numeroDomanda);

		try {
			return namedParameterJdbcTemplate.query(this.sqlGetDichiarazioni, params, new DichiarazioniDomandaUnicaRowMapper(campagna));

		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getDichiarazioniDomanda: NoResultException | EmptyResultDataAccessException per domanda {} campagna {}",
					numeroDomanda, campagna);
			throw e;
		}
	}
	
	@Override
	public String getIbanFascicolo(Long idDomanda) {
		List<Object> params = new ArrayList<Object>();
		params.add(idDomanda);
		try {
			String result = getJdbcTemplate().queryForObject(this.getIbanFascicolo, params.toArray(), String.class);
			return result;
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getIbanFascicolo: NoResultException | EmptyResultDataAccessException per idDomanda {}", idDomanda);
			return null;
		}
	}
	
	@Override
	public List<DomandaUnica> getListaDomande(DomandaUnicaFilter domandaUnicaFilter) {
		String sqlQuery=SqlQueryConstants.RICERCA_DOMANDA_UNICA;
		MapSqlParameterSource parameters = new MapSqlParameterSource();


		if (domandaUnicaFilter.getStati() != null && !domandaUnicaFilter.getStati().isEmpty()) {
			List<String> stati = domandaUnicaFilter.getStati().stream().map(StatoDomanda::getCodStato).collect(Collectors.toList());
			sqlQuery=sqlQuery.concat(" AND d.sco_stato IN ( :stati ) ");
			parameters.addValue("stati", stati);
		}
		if (domandaUnicaFilter.getCuaa() != null) {
			sqlQuery=sqlQuery.concat(" AND s.cuaa = :cuaa ");
			parameters.addValue("cuaa", domandaUnicaFilter.getCuaa());
		} 
		if (domandaUnicaFilter.getCampagna() != null) { 
			sqlQuery=sqlQuery.concat(" AND MOD.ANNO = :annoRiferimento ");
			parameters.addValue("annoRiferimento", domandaUnicaFilter.getCampagna());
		}

		try {
			List<DomandaUnica> domandeResult = namedParameterJdbcTemplate.query(sqlQuery, parameters, new DomandaUnicaRowMapper());
			if(!CollectionUtils.isEmpty(domandaUnicaFilter.getExpand())) {
				domandaUnicaFilter.getExpand().forEach(expand -> {
					if (DomandaUnicaFilter.Expand.RICHIESTE.equals(expand)) {
						domandeResult.forEach(domanda -> {
							Richieste richieste = new Richieste();
							richieste.setSintesiRichieste(getSintesiRichiesteDomandaUnica(domanda.getInfoGeneraliDomanda().getNumeroDomanda()));
							domanda.setRichieste(richieste);
						});
					}
				});
			}	
			return domandeResult;
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.warn("getListaDomande: NoResultException | EmptyResultDataAccessException per " + domandaUnicaFilter);
			return new ArrayList<>();
		}
	}	
}
