-- Popolamento tabella TDOMANDA
INSERT INTO tdomanda (pkid,id_soggetto,id_domanda,id_domanda_rettificata,id_modulo,sco_stato,sco_settore,anno,de_modulo,dt_insert,dt_delete,ds_dom_stato)
VALUES ('9000000','1','112233',NULL,'303','000000','PI2014','2018','PAGAMENTI DIRETTI',to_date('13/07/2019 15:23:32','dd/mm/yyyy HH24:MI:SS'),to_date('13/07/2019 15:23:33','dd/mm/yyyy HH24:MI:SS'), 'CREATA');
INSERT INTO tdomanda (pkid,id_soggetto,id_domanda,id_domanda_rettificata,id_modulo,sco_stato,sco_settore,anno,de_modulo,dt_insert,dt_delete,ds_dom_stato)
VALUES ('9000001','1','112233',NULL,'303','000001','PI2014','2018','PAGAMENTI DIRETTI',to_date('13/07/2019 15:23:34','dd/mm/yyyy HH24:MI:SS'),to_date('13/07/2019 15:25:21','dd/mm/yyyy HH24:MI:SS'), 'IN COMPILAZIONE');
INSERT INTO tdomanda (pkid,id_soggetto,id_domanda,id_domanda_rettificata,id_modulo,sco_stato,sco_settore,anno,de_modulo,dt_insert,dt_delete,ds_dom_stato)
VALUES ('9000002','1','112233',NULL,'303','000003','PI2014','2018','PAGAMENTI DIRETTI',to_date('13/07/2019 15:25:22','dd/mm/yyyy HH24:MI:SS'),to_date('13/07/2019 15:25:37','dd/mm/yyyy HH24:MI:SS'), 'CONTROLLATA');
INSERT INTO tdomanda (pkid,id_soggetto,id_domanda,id_domanda_rettificata,id_modulo,sco_stato,sco_settore,anno,de_modulo,dt_insert,dt_delete,ds_dom_stato)
VALUES ('9000003','1','112233',NULL,'303','000010','PI2014','2018','PAGAMENTI DIRETTI',to_date('13/07/2019 15:25:38','dd/mm/yyyy HH24:MI:SS'),to_date('13/07/2019 15:26:21','dd/mm/yyyy HH24:MI:SS'), 'PRESENTATA');
INSERT INTO tdomanda (pkid,id_soggetto,id_domanda,id_domanda_rettificata,id_modulo,sco_stato,sco_settore,anno,de_modulo,dt_insert,dt_delete,ds_dom_stato)
VALUES ('9000004','1','112233',NULL,'303','000015','PI2014','2018','PAGAMENTI DIRETTI',to_date('13/07/2019 15:26:22','dd/mm/yyyy HH24:MI:SS'),to_date('31/12/9999 00:00:00','dd/mm/yyyy HH24:MI:SS'), 'PROTOCOLLATA');

-- Popolamento tabella TQDO_ENTI
INSERT INTO tqdo_enti (pkid,dt_insert,dt_delete,id_soggetto,id_ute,id_ente_compilatore,id_ente_delegato,cod_fonte_dati,sco_fonte_dati,dt_inizio,dt_fine,fg_valido_inizio,fg_valido_fine,id_atto_inizio,id_atto_fine,dt_ultimo_agg,utente,id_domanda) 
VALUES ('900000',to_date('13/07/2019 15:23:41','dd/mm/yyyy HH24:MI:SS'),to_date('31/12/9999 00:00:00','dd/mm/yyyy HH24:MI:SS'),'1',NULL,'12','23','FONDAT','000001',to_date('01/01/2018 00:00:00','dd/mm/yyyy HH24:MI:SS'),to_date('31/12/2018 00:00:00','dd/mm/yyyy HH24:MI:SS'),'N','S',NULL,NULL,to_date('13/07/2019 15:23:41','dd/mm/yyyy HH24:MI:SS'),'SZUECH','112233');

-- Popolamento tabella TDOM_MOVIMENTO
INSERT INTO tdom_movimento (id_movimento,id_domanda,id_workflow,cod_workflow,dt_movimento,id_atto_inizio,id_atto_fine,fg_valido_inizio,fg_valido_fine,id_registro,rule_log_id,utente,note)
VALUES ('900000','112233','14523','APP_CREATE',to_date('13/07/2019 15:23:33','dd/mm/yyyy HH24:MI:SS'),NULL,NULL,'S','S',NULL,'178141503','SZUECH',NULL);
INSERT INTO tdom_movimento (id_movimento,id_domanda,id_workflow,cod_workflow,dt_movimento,id_atto_inizio,id_atto_fine,fg_valido_inizio,fg_valido_fine,id_registro,rule_log_id,utente,note) 
VALUES ('900001','112233','14525','APP_CHECK',to_date('13/07/2019 15:25:20','dd/mm/yyyy HH24:MI:SS'),NULL,NULL,'S','S',NULL,'178148104','SZUECH',NULL);
INSERT INTO tdom_movimento (id_movimento,id_domanda,id_workflow,cod_workflow,dt_movimento,id_atto_inizio,id_atto_fine,fg_valido_inizio,fg_valido_fine,id_registro,rule_log_id,utente,note) 
VALUES ('900002','112233','14527','APP_REQ_SIGNATURE',to_date('13/07/2019 15:25:36','dd/mm/yyyy HH24:MI:SS'),NULL,NULL,'S','S',NULL,'178148234','SZUECH',NULL);
INSERT INTO tdom_movimento (id_movimento,id_domanda,id_workflow,cod_workflow,dt_movimento,id_atto_inizio,id_atto_fine,fg_valido_inizio,fg_valido_fine,id_registro,rule_log_id,utente,note) 
VALUES ('900003','112233','14528','APP_SUBMIT',to_date('13/07/2019 15:26:21','dd/mm/yyyy HH24:MI:SS'),'1122334',NULL,'S','S',NULL,'178148374','SZUECH',NULL);
INSERT INTO tdom_movimento (id_movimento,id_domanda,id_workflow,cod_workflow,dt_movimento,id_atto_inizio,id_atto_fine,fg_valido_inizio,fg_valido_fine,id_registro,rule_log_id,utente,note) 
VALUES ('900004','112233','15519','APP_RICEV_DU_2017',to_date('05/06/2018 18:05:36','dd/mm/yyyy HH24:MI:SS'),'1696973',NULL,'S','N',NULL,'204271905','GNINFA',NULL);

-- Popolamento tabella TDOM_DOMANDA
INSERT INTO tdom_domanda (pkid, dt_insert, dt_delete, id_soggetto, id_ute, id_domanda, cod_domanda, id_domanda_rettificata, id_modulo, id_ente, cod_stato, sco_stato, cod_fonte_dati, sco_fonte_dati, dt_inizio, dt_fine, fg_valido_inizio, fg_valido_fine, id_atto_inizio, id_atto_fine, dt_ultimo_agg, utente)
VALUES (900, to_date('21/04/2018 15:23:32', 'dd/mm/yyyy hh24:mi:ss'), to_date('22/04/2018 15:23:33', 'dd/mm/yyyy hh24:mi:ss'), 1, NULL, 112233, NULL, NULL, 303, NULL, 'STADOM', '000000', 'FONDAT', '000001', to_date('13/07/2019 15:23:19', 'dd/mm/yyyy hh24:mi:ss'), to_date('31/12/9999', 'dd/mm/yyyy'), 'N', 'N', NULL, NULL, to_date('26-04-2018 15:23:34', 'dd/mm/yyyy hh24:mi:ss'), 'SZUECH');
INSERT INTO tdom_domanda (pkid, dt_insert, dt_delete, id_soggetto, id_ute, id_domanda, cod_domanda, id_domanda_rettificata, id_modulo, id_ente, cod_stato, sco_stato, cod_fonte_dati, sco_fonte_dati, dt_inizio, dt_fine, fg_valido_inizio, fg_valido_fine, id_atto_inizio, id_atto_fine, dt_ultimo_agg, utente)
VALUES (901, to_date('22/04/2018 15:23:34', 'dd/mm/yyyy hh24:mi:ss'), to_date('23/04/2018 15:25:21', 'dd/mm/yyyy hh24:mi:ss'), 1, NULL, 112233, NULL, NULL, 303, NULL, 'STADOM', '000001', 'FONDAT', '000001', to_date('13/07/2019 15:23:19', 'dd/mm/yyyy hh24:mi:ss'), to_date('31/12/9999', 'dd/mm/yyyy'), 'N', 'N', NULL, NULL, to_date('26-04-2018 15:25:22', 'dd/mm/yyyy hh24:mi:ss'), 'SZUECH');
INSERT INTO tdom_domanda (pkid, dt_insert, dt_delete, id_soggetto, id_ute, id_domanda, cod_domanda, id_domanda_rettificata, id_modulo, id_ente, cod_stato, sco_stato, cod_fonte_dati, sco_fonte_dati, dt_inizio, dt_fine, fg_valido_inizio, fg_valido_fine, id_atto_inizio, id_atto_fine, dt_ultimo_agg, utente)
VALUES (902, to_date('23/04/2018 15:25:22', 'dd/mm/yyyy hh24:mi:ss'), to_date('24/04/2018 15:25:37', 'dd/mm/yyyy hh24:mi:ss'), 1, NULL, 112233, NULL, NULL, 303, NULL, 'STADOM', '000003', 'FONDAT', '000001', to_date('13/07/2019 15:23:19', 'dd/mm/yyyy hh24:mi:ss'), to_date('31/12/9999', 'dd/mm/yyyy'), 'N', 'N', NULL, NULL, to_date('26-04-2018 15:25:38', 'dd/mm/yyyy hh24:mi:ss'), 'SZUECH');
INSERT INTO tdom_domanda (pkid, dt_insert, dt_delete, id_soggetto, id_ute, id_domanda, cod_domanda, id_domanda_rettificata, id_modulo, id_ente, cod_stato, sco_stato, cod_fonte_dati, sco_fonte_dati, dt_inizio, dt_fine, fg_valido_inizio, fg_valido_fine, id_atto_inizio, id_atto_fine, dt_ultimo_agg, utente)
VALUES (903, to_date('24/04/2018 15:25:38', 'dd/mm/yyyy hh24:mi:ss'), to_date('13/07/2019 15:26:21', 'dd/mm/yyyy hh24:mi:ss'), 1, NULL, 112233, NULL, NULL, 303, NULL, 'STADOM', '000010', 'FONDAT', '000001', to_date('13/07/2019 15:23:19', 'dd/mm/yyyy hh24:mi:ss'), to_date('31/12/9999', 'dd/mm/yyyy'), 'N', 'N', NULL, NULL, to_date('26-04-2018 15:26:22', 'dd/mm/yyyy hh24:mi:ss'), 'SZUECH');
INSERT INTO tdom_domanda (pkid, dt_insert, dt_delete, id_soggetto, id_ute, id_domanda, cod_domanda, id_domanda_rettificata, id_modulo, id_ente, cod_stato, sco_stato, cod_fonte_dati, sco_fonte_dati, dt_inizio, dt_fine, fg_valido_inizio, fg_valido_fine, id_atto_inizio, id_atto_fine, dt_ultimo_agg, utente)
VALUES (904, to_date('13/07/2019 15:26:22', 'dd/mm/yyyy hh24:mi:ss'), to_date('31/12/9999', 'dd/mm/yyyy'), 1, NULL, 112233, NULL, NULL, 303, NULL, 'STADOM', '000015', 'FONDAT', '000001', to_date('13/07/2019 15:23:19', 'dd/mm/yyyy hh24:mi:ss'), to_date('31/12/9999', 'dd/mm/yyyy'), 'N', 'N', NULL, NULL, to_date('26-04-2018 15:26:22', 'dd/mm/yyyy hh24:mi:ss'), 'SZUECH');

COMMIT;