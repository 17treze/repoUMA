package it.tndigitale.a4g.psr.business.persistence.repository;

class NativeQueryString {

    private static final String RECUPARA_DOMANDA_QUERY_BASE =
            "SELECT modulo.cod_modulo as modulo" +
            "      ,anno as campagna"+
            "      ,sco_stato " +
            "      ,csv.cuaa as cuaa" +
            "      ,csv.ragi_soci " +
            "      ,tdd.id_domanda as numeroDomanda" +
            "      ,REPLACE(decod.de_decodifica,' ', '_') as stato" +
            "      ,(SELECT MIN(dt_insert) FROM tdom_domanda WHERE id_domanda = tdd.id_domanda AND sco_stato = '000010') AS dataPresentazione " +
            "FROM  tdom_domanda tdd " +
            "JOIN  tdom_modulo modulo " +
            "  ON modulo.id_modulo = tdd.id_modulo " +
            "  AND modulo.sco_settore = 'P22014' " +
            "  AND SYSDATE BETWEEN modulo.dt_inizio AND modulo.dt_fine " +
            "  AND modulo.cod_modulo IN (:moduli) " +
            "JOIN  cons_sogg_viw csv " +
            "  ON  csv.pk_cuaa = tdd.id_soggetto " +
            "JOIN tdecodifica decod " +
            "  ON decod.codice = cod_stato AND decod.sotto_codice = sco_stato " +
            "WHERE SYSDATE BETWEEN tdd.dt_insert AND tdd.dt_delete " +
            "  AND SYSDATE BETWEEN tdd.dt_inizio AND tdd.dt_fine " +
            "  AND SYSDATE BETWEEN data_inizio_val AND data_fine_val " +
            "  AND sco_stato IN (:listaStati)";

	static final String RECUPERA_DOMANDE_PSR =
            RECUPARA_DOMANDA_QUERY_BASE +
			"  AND UPPER(csv.cuaa) = :cuaa " +
			"  AND anno >= 2018";

    static final String RECUPERA_DOMANDA_PSR_SUPERFICIE_BY_ID_DOMANDA =
            RECUPARA_DOMANDA_QUERY_BASE +
                    "  AND id_domanda = :idDomanda ";

	static final String RECUPERA_MISURE_INTERVENTO = "SELECT DISTINCT tqdo.id_domanda" +
			"      ,tdes.cod_operazione " +
			"FROM tqdo_psr_annualita tqdo " +
			"JOIN tdes_def_operazione tdes" +
			"  ON tqdo.id_destinazione = tdes.id_operazione " +
			"WHERE tqdo.id_domanda = :numeroDomanda" +
			"  AND SYSDATE BETWEEN tqdo.dt_insert AND tqdo.dt_delete " +
			"ORDER BY 2";

	static final String IMPEGNI_RICHIEST_PSR_SUPERFICIE_PER_DOMANDA = "SELECT\n" +
			"    impegni.ID_DOMANDA, impegni.ID_MODULO, impegni.DATAPRES, impegni.GRAFICA, impegni.COD_DESTINAZIONE, impegni.DS_DESTINAZIONE, sum(impegni.SUP_IMPEGNO) AS SUP_IMPEGNO, sum(impegni.SUP_IMPEGNO_NETTA) AS SUP_IMPEGNO_NETTA\n" +
			"FROM (\n" +
			"         SELECT\n" +
			"             imp.ID_DOMANDA, dom.ID_MODULO, dtpres.DATAPRES, des.COD_DESTINAZIONE, des.DS_DESTINAZIONE, piano.ID_PARTICELLA, CONCAT(CONCAT(CONCAT (piano.COD_NAZIONALE, piano.FOGLIO), piano.PARTICELLA), piano.SUB) as ID_PAR_CON,\n" +
			"             col.ID_COLTURA, col.COD_COLTURA, col.DE_COLTURA, /*col.CODUAGEA, col.CODPAGEA, col.CODVAGEA, col.COEFF_TARA,*/ imp.SUP_IMPEGNO, imp.SUP_IMPEGNO * col.COEFF_TARA AS SUP_IMPEGNO_NETTA, gra.GRAFICA\n" +
			"         FROM fascicolo.TQDO_DES_PC_DESTINAZIONE imp                                                 -- parto dagli impegni\n" +
			"                  INNER JOIN FASCICOLO.TPIANO_COLTURE piano                                                 -- collego gli impegni ai record del piano colturale\n" +
			"                             ON  piano.ID_PIANO_COLTURE = imp.ID_PIANO_COLTURE\n" +
			"                  RIGHT OUTER JOIN FASCICOLO.TDOM_DOMANDA dom                                                   -- collego le domande agli impegni\n" +
			"                                   ON imp.ID_DOMANDA = dom.ID_DOMANDA\n" +
			"                  LEFT OUTER JOIN (                                                                     -- aggiungo la data di presentazione della domanda\n" +
			"             SELECT dp.ID_DOMANDA, max(dp.DT_INSERT) AS DATAPRES\n" +
			"             FROM FASCICOLO.TDOM_DOMANDA dp\n" +
			"             WHERE dp.SCO_STATO = '000010'\n" +
			"             GROUP BY dp.ID_DOMANDA\n" +
			"         ) dtpres\n" +
			"                                  ON dom.ID_DOMANDA = dtpres.ID_DOMANDA\n" +
			"                  LEFT OUTER JOIN (                                                                    -- aggiungo la data di presentazione della domanda iniziale\n" +
			"             SELECT dpi.ID_DOMANDA, max(dpi.DT_INSERT) AS DATAPRESINI\n" +
			"             FROM FASCICOLO.TDOM_DOMANDA dpi\n" +
			"             WHERE dpi.SCO_STATO = '000010'\n" +
			"             GROUP BY dpi.ID_DOMANDA\n" +
			"         ) dtpresini\n" +
			"                                  ON dom.ID_DOMANDA_RETTIFICATA = dtpresini.ID_DOMANDA\n" +
			"                  INNER JOIN FASCICOLO.TCOLTURA_LANG col                                          -- aggiungo la descrizione delle colture\n" +
			"                             ON piano.ID_COLTURA_1 = col.ID_COLTURA\n" +
			"                  inner join FASCICOLO.TDES_DEF_DESTINAZIONE des                                -- aggiungo le descrizioni delle destinazioni\n" +
			"                             ON imp.ID_DESTINAZIONE = des.ID_DESTINAZIONE\n" +
			"                  LEFT JOIN (                                                     -- aggiungo l'informazione se sia o meno una domanda grafica\n" +
			"             SELECT DISTINCT g.ID_DOMANDA,\n" +
			"                             CASE WHEN i.ID_DOMANDA is null\n" +
			"                                      THEN 1\n" +
			"                                  ELSE 0\n" +
			"                                 END AS GRAFICA\n" +
			"             FROM    fascicolo.TQDO_DES_PC_DESTINAZIONE g\n" +
			"                         left JOIN (\n" +
			"                 SELECT  DISTINCT ID_DOMANDA\n" +
			"                 FROM    fascicolo.TQDO_DES_PC_DESTINAZIONE h\n" +
			"                 WHERE   h.ID_ISOLA is null\n" +
			"             ) i\n" +
			"                                   ON g. ID_DOMANDA = i.ID_DOMANDA\n" +
			"         ) gra\n" +
			"                            ON gra.ID_DOMANDA = dom.ID_DOMANDA\n" +
			"         WHERE SYSDATE BETWEEN dom.DT_INSERT AND dom.DT_DELETE                                         -- seleziono le domande nello stato attuale\n" +
			"           AND SYSDATE BETWEEN imp.DT_INSERT AND imp.DT_DELETE\n" +
			"           AND CASE WHEN (\n" +
			"                     dom.ID_MODULO in (\n" +
			"                     SELECT id_modulo\n" +
			"                     FROM fascicolo.TDOM_MODULO_DECO\n" +
			"                     WHERE COD_MODULO like '%PRZ%'\n" +
			"                       AND ANNO > 2016\n" +
			"                 )\n" +
			"                 AND gra.GRAFICA = 0)                                                             -- se è un modulo speciale di nuova concezione prendo la data di presentazione delle domanda precedente\n" +
			"                        THEN dtpresini.DATAPRESINI                                                              -- seleziono il piano colturale alla data di presentazione della domanda\n" +
			"                    ELSE dtpres.DATAPRES\n" +
			"             END between piano.DT_INSERT AND piano.DT_DELETE\n" +
			"          AND imp.ID_DOMANDA = :idDomanda\n" +
			"     ) impegni\n" +
			"GROUP BY impegni.ID_DOMANDA, impegni.ID_MODULO, impegni.DATAPRES, impegni.COD_DESTINAZIONE, impegni.DS_DESTINAZIONE, impegni.GRAFICA";

    static final String RECUPERA_CUAA_DA_ID_DOMANDA = "" +
            "    SELECT csv.cuaa as cuaa\n" +
            "    FROM  tdom_domanda tdd\n" +
            "    JOIN  tdom_modulo modulo\n" +
            "    ON modulo.id_modulo = tdd.id_modulo\n" +
            "    JOIN  cons_sogg_viw csv\n" +
            "    ON  csv.pk_cuaa = tdd.id_soggetto\n" +
            "    WHERE id_domanda = :idDomanda\n" +
            "    OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";

    static final String UBA_ALPEGGIATE_PSR_SUPERFICIE_PER_DOMANDA = "SELECT * FROM VUBA_DOMANDA_PSR_SUPERFICIE WHERE ID_DOMANDA=:idDomanda";

    static final String RECUPERA_QUADRO_PASCOLI_DA_ID_DOMANDA = "SELECT * FROM vQUADRO_PASCOLI_PSR_SUPERFICIE WHERE ID_DOMANDA=:idDomanda";

    static final String RECUPERA_IMPEGNIZOO_PASCOLI_PASCOLI_DA_ID_DOMANDA = "SELECT   impegnizoo.ID_DOMANDA, impegnizoo.ID_MODULO, impegnizoo.DATAPRES, impegnizoo.COD_AZIONE, impegnizoo.DE_AZIONE, COUNT(impegnizoo.FG_SESSO) AS QUANTITA_IMP \n" +
				"FROM (\n" +
				"    SELECT   impzoo.ID_DOMANDA, dom.ID_MODULO,dtpres.DATAPRES, azione.COD_AZIONE, azione.DE_AZIONE, bdn.CODICE, bdn.FG_SESSO, bdn.DT_NASCITA\n" +
				"    FROM  FASCICOLO.TDOM_DOMANDA dom                                                            -- parto dalle domande\n" +
				"    LEFT OUTER JOIN (                                                                         -- aggiungo la data di presentazione della domanda\n" +
				"            SELECT dp.ID_DOMANDA, max(dp.DT_INSERT) AS DATAPRES\n" +
				"            FROM FASCICOLO.TDOM_DOMANDA dp\n" +
				"            WHERE dp.SCO_STATO = '000010'\n" +
				"            GROUP BY dp.ID_DOMANDA\n" +
				"            ) dtpres\n" +
				"        ON dom.ID_DOMANDA = dtpres.ID_DOMANDA\n" +
				"    LEFT OUTER JOIN (                                                               -- aggiungo la data di presentazione della domanda iniziale\n" +
				"            SELECT dpi.ID_DOMANDA, max(dpi.DT_INSERT) AS DATAPRESINI\n" +
				"            FROM FASCICOLO.TDOM_DOMANDA dpi\n" +
				"            WHERE dpi.SCO_STATO = '000010'\n" +
				"            GROUP BY dpi.ID_DOMANDA\n" +
				"            ) dtpresini\n" +
				"        ON dom.ID_DOMANDA_RETTIFICATA = dtpresini.ID_DOMANDA  \n" +
				"    LEFT OUTER JOIN fascicolo.TQDO_PSR_IMPEGNO_CAPO impzoo                                -- collego gli impegni zootecnici\n" +
				"        ON impzoo.ID_DOMANDA = dom.ID_DOMANDA\n" +
				"    INNER JOIN FASCICOLO.TCAPO_ANIMALE bdn                                              -- aggiungo le info dei capi del quadro capi del fascicolo\n" +
				"        ON bdn.ID_CAPO_ANIMALE = impzoo.ID_CAPO_ANIMALE\n" +
				"    inner join FASCICOLO.TPSR_DEF_AZIONE azione                                     -- aggiungo le descrizioni delle destinazioni\n" +
				"        ON impzoo.ID_AZIONE = azione.ID_AZIONE\n" +
				"    WHERE SYSDATE BETWEEN impzoo.DT_INSERT AND impzoo.DT_DELETE                         -- seleziono gli impegni validi alla data di presentazione della domanda\n" +
				"        AND CASE WHEN dom.ID_MODULO in (                                                      -- escludo le domande di ritiro totale\n" +
				"                SELECT id_modulo\n" +
				"                FROM fascicolo.TDOM_MODULO_DECO\n" +
				"                WHERE COD_MODULO like '%PRZ%'\n" +
				"                AND ANNO > 2016\n" +
				"                )  \n" +
				"            THEN dtpresini.DATAPRESINI\n" +
				"            ELSE dtpres.DATAPRES\n" +
				"            END  between bdn.DT_INSERT AND bdn.DT_DELETE                                 -- seleziono il dettaglio capi alla data di presentazione della domanda\n" +
				"        AND SYSDATE BETWEEN dom.DT_INSERT AND dom.DT_DELETE                                         -- seleziono le domande nello stato attuale\n" +
				"        AND impzoo.ID_DOMANDA = :idDomanda\n" +
				"    ) impegnizoo\n" +
				"GROUP BY impegnizoo.ID_DOMANDA, impegnizoo.ID_MODULO, impegnizoo.DATAPRES, impegnizoo.COD_AZIONE, impegnizoo.DE_AZIONE";

    static final String RECUPERA_STATO_OPERAZIONE_DA_ID_DOMANDA = "SELECT * FROM TABLE(get_stato_domanda(:idDomanda)) WHERE LOWER(STATO) <> 'nessun impegno in domanda' \n" +
            "AND DATA_ULTIMO_MOVIMENTO IS NOT NULL AND TIPO_PAGAMENTO IS NOT NULL\n" +
            "ORDER BY DATA_ULTIMO_MOVIMENTO DESC\n";

    static final String RECUPERA_STATO_OPERAZIONE_DA_ID_DOMANDA_AND_COD_OPERAZIONE = "SELECT * FROM TABLE(get_stato_domanda(:idDomanda)) WHERE LOWER(STATO) <> 'nessun impegno in domanda'\n" +
            "    AND DATA_ULTIMO_MOVIMENTO IS NOT NULL AND TIPO_PAGAMENTO IS NOT NULL\n" +
            "    AND COD_OPERAZIONE = :codOperazione\n" +
            "    ORDER BY DATA_ULTIMO_MOVIMENTO DESC";

    static final String IMPORTO_TOTALE_RICHIESTO_DA_ANNO_MODULO_E_CUAA= "SELECT \n" +
            "  (SELECT MIN (dom.dt_insert)\n" +
            "      FROM tdom_domanda dom\n" +
            "      where dom.SCO_STATO = '000010'\n" +
            "      and dom.id_domanda = tdd.id_domanda) as DATA_DOMANDA, sco_settore, decod.ds_decodifica STATO_DOMANDA,\n" +
            "      ( nvl(psr_calc_premio_richiesto.get_totale_premio_richiesto(tdd.id_domanda,'10','PSR_CONTROLLO'),0) +\n" +
            "        nvl(psr_calc_premio_richiesto.get_totale_premio_richiesto(tdd.id_domanda,'11','PSR_CONTROLLO'),0) +\n" +
            "        nvl(psr_calc_premio_richiesto.get_totale_premio_richiesto(tdd.id_domanda,'13','PSR_CONTROLLO'),0)) as IMPORTO_RICHIESTO\n" +
            "FROM  tdom_domanda tdd\n" +
            "join  tdom_modulo_deco modulo\n" +
            "on modulo.id_modulo = tdd.id_modulo\n" +
            "and cod_modulo IN (select cod_modulo from tdom_modulo\n" +
            "where anno = :anno\n" +
            "and sco_settore = 'P22014' and to_char(dt_fine, 'dd/mm/yyyy') = '31/12/9999')\n" +
            "join  cons_sogg_viw csv\n" +
            "on  csv.pk_cuaa = tdd.id_soggetto\n" +
            "join tdecodifica decod\n" +
            "on decod.codice = cod_stato\n" +
            "AND decod.sotto_codice = sco_stato\n" +
            "WHERE sysdate BETWEEN tdd.dt_insert AND tdd.dt_delete\n" +
            "and sysdate BETWEEN tdd.dt_inizio AND tdd.dt_fine\n" +
            "and sysdate between data_inizio_val and data_fine_val\n" +
            "and MODULO.cod_modulo <> ':codModulo'\n" +
            "and sco_stato not in ('000000','000001','000002','000003','000005','000007','000010','000011', '000014','000055', '000200')\n" +
            "and cuaa in (:cuaa)";

    static final String RECUPERA_DETTAGLIO_PAGAMENTO_DA_ID_DOMANDA_E_CODICE =
            "SELECT v.*,\n" +
                    "       tcdv.variabile AS variabile,\n" +
                    "       tcdv.ds_variabile AS ds_variabile,\n" +
                    "       trde.valore AS valore,\n" +
                    "       CASE\n" +
                    "        WHEN trde.valore = '-1' THEN\n" +
                    "          'NA'\n" +
                    "        ELSE\n" +
                    "         Decode (Substr (trde.valore, 1, 1), '.',\n" +
                    "          Replace ('0' || trde.valore, '.', ','),\n" +
                    "          Replace (trde.valore, '.', ','))\n" +
                    "       END AS valore_char,\n" +
                    "       tcdv.ordine AS ordine,\n" +
                    "       tcdv.cod_gruppo AS cod_gruppo,\n" +
                    "       tcdv.fg_totale AS fg_totale\n" +
                    "FROM   (SELECT max(m.ID_CALCOLO) as ID_CALCOLO, max(m.id_def_calcolo) as id_def_calcolo\n" +
                    "                           FROM   fascicolo.vclc_risultato_calcolo_%s m\n" +
                    "                           WHERE  to_number(m.id_domanda) = :ID_DOMANDA\n" +
                    "                            and id_def_calcolo=\n" +
                    "                                (SELECT t.id_def_calcolo\n" +
                    "                               FROM   fascicolo.tclc_def_calcolo t\n" +
                    "                               WHERE  t.cod_calcolo = :COD_CALCOLO)\n" +
                    "                               ) v,\n" +
                    "       fascicolo.tclc_def_variabile tcdv,\n" +
                    "       fascicolo.tclc_risultato_dettaglio trde\n" +
                    "WHERE  v.id_def_calcolo = tcdv.id_def_calcolo\n" +
                    "       AND trde.id_calcolo = v.id_calcolo\n" +
                    "       AND trde.variabile = tcdv.variabile\n" +
                    "ORDER  BY tcdv.ordine\n";


    static final String RECUPERA_IMPORTO_CALCOLATO_DA_ID_DOMANDA_CODICE_E_TIPO_PAGAMENTO = "select (GET_PAGAMENTO_DOMANDA (:ID_DOMANDA, :COD_MISURA, :TIPO_PAGAMENTO)) from dual";
}
