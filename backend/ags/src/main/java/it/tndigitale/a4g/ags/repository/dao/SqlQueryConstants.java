package it.tndigitale.a4g.ags.repository.dao;

public class SqlQueryConstants {
	
	private SqlQueryConstants() {}
	
	public static final String RICERCA_DOMANDA_UNICA= ""+
	"SELECT D.SCO_SETTORE                         AS SETTORE, " + 
	"       D.anno                               AS CAMPAGNA, " + 
	"       MOD.cod_modulo, " + 
	"       D.de_modulo                          AS MODULO_DOMANDA, " + 
	"       S.cuaa                               AS CUAA, " + 
	"       S.ragi_soci                          AS RAGIONE_SOCIALE, " + 
	"       D.id_domanda, " + 
	"       d.sco_stato, " + 
	"       d.ds_dom_stato, " + 
	"       M.dt_movimento                       AS DATA_PROTOCOLLAZIONE, " + 
	"       D.id_domanda_rettificata, " + 
	"       (SELECT Min(D1.dt_insert) " + 
	"        FROM   tdomanda D1 " + 
	"        WHERE  D1.id_domanda = D.id_domanda " + 
	"               AND D1.sco_stato = '000010') AS DATA_PRESENTAZIONE " + 
	"FROM   tdomanda D " + 
	"       JOIN tdom_movimento M " + 
	"         ON D.id_domanda = M.id_domanda " + 
	"       JOIN cons_sogg_viw S " + 
	"         ON D.id_soggetto = S.pk_cuaa " + 
	"       JOIN tdom_modulo_deco MOD " + 
	"         ON D.id_modulo = MOD.id_modulo " + 
	"WHERE  M.cod_workflow IN ( 'APP_SUBMIT', 'APP_PROTOC' ) " + 
	"       AND M.fg_valido_fine = 'S' " + 
	"       AND sysdate BETWEEN D.dt_insert AND D.dt_delete " + 
	"       AND sysdate BETWEEN S.data_inizio_val AND S.data_fine_val " + 
	"       AND M.dt_movimento = (SELECT Min(dt_movimento) " + 
	"                             FROM   tdom_movimento M1 " + 
	"                             WHERE  M1.cod_workflow IN ( " + 
	"                                    'APP_SUBMIT', 'APP_PROTOC' ) " + 
	"                                    AND M1.fg_valido_fine = 'S' " + 
	"                                    AND M1.id_domanda = D.id_domanda) " + 
	"       AND mod.sco_settore = 'PI2014' " + 
	"       AND MOD.cod_modulo NOT LIKE 'BPS_RITTOT%' ";

}
