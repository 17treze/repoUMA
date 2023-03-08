-- Popolo la tabella A4GT_DOMANDA
insert into a4gt_domanda(ID, VERSIONE, NUMERO_DOMANDA, COD_MODULO_DOMANDA, DESC_MODULO_DOMANDA, ANNO_CAMPAGNA, STATO, CUAA_INTESTATARIO, DT_PRESENTAZIONE,DT_PROTOCOLLAZIONE,DT_PRESENTAZ_ORIGINARIA,DT_PROTOCOLLAZ_ORIGINARIA, NUM_DOMANDA_RETTIFICATA, COD_ENTE_COMPILATORE, DESC_ENTE_COMPILATORE, RAGIONE_SOCIALE, IBAN)
values (56456456477, 0, 181666, 'BPS_2018', 'PAGAMENTI DIRETTI', 2018,'IN_ISTRUTTORIA','BRTSRG86A24L174X',to_date('18/04/2018 15:26:20', 'dd/mm/yyyy HH24:MI:SS'), to_date('18/04/2018 15:29:20', 'dd/mm/yyyy HH24:MI:SS'), null, null, null, 3, 'CAA COLDIRETTI DEL TRENTINO - 004','BERTI SERGIO', 'IT10G0585634270081571373413');


Insert into A4GT_ISTRUTTORIA (ID, VERSIONE, ID_DOMANDA, SOSTEGNO, ID_STATO_LAVORAZIONE, TIPOLOGIA)
values
(56456456478,
0,
56456456477,
'SUPERFICIE',
(select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK'),
'SALDO');


Insert into A4GT_TRANSIZIONE_ISTRUTTORIA (ID,VERSIONE,ID_ISTRUTTORIA,ID_STATO_INIZIALE,ID_STATO_FINALE,DATA_ESECUZIONE) values (NXTNBR.nextval,1,56456456478,(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'RICHIESTO'),(select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK'),to_date('07/02/2019 15:26:20', 'dd/mm/yyyy HH24:MI:SS'));
insert into A4GT_TRANSIZIONE_ISTRUTTORIA (ID,VERSIONE,ID_ISTRUTTORIA,ID_STATO_INIZIALE,ID_STATO_FINALE,DATA_ESECUZIONE) values (NXTNBR.nextval,0,56456456478,(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'CONTROLLI_CALCOLO_OK'),(select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE'),to_date('07/02/2019 15:26:20', 'dd/mm/yyyy HH24:MI:SS'));
insert into A4GT_TRANSIZIONE_ISTRUTTORIA (ID,VERSIONE,ID_ISTRUTTORIA,ID_STATO_INIZIALE,ID_STATO_FINALE,DATA_ESECUZIONE) values (NXTNBR.nextval,0,56456456478,(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE'),(select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_INTERSOSTEGNO_OK'),to_date('07/02/2019 15:26:20', 'dd/mm/yyyy HH24:MI:SS'));



INSERT INTO A4GT_PASSO_TRANSIZIONE
VALUES
(nxtnbr.nextval, 0,
(SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts where ts.ID_STATO_FINALE=(select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_INTERSOSTEGNO_OK') and ts.id_istruttoria = 56456456478),
'DISCIPLINA_FINANZIARIA', 'OK', 'DUT_001', '{}',
'{"variabiliCalcolo":[{"tipoVariabile":"DFIMPLIQACS","valNumber":3.6100},{"tipoVariabile":"DFFRPAGACS_M8","valNumber":352.55},{"tipoVariabile":"DFIMPDFDISACS_M8","valNumber":3.6100},{"tipoVariabile":"GREIMPRIC","valNumber":176.28},{"tipoVariabile":"TITVAL","valNumber":99.65},{"tipoVariabile":"TITVALRID","valNumber":97.66}]}', '{}'
);


INSERT INTO A4GT_PASSO_TRANSIZIONE VALUES (nxtnbr.nextval, 0, (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts where ts.ID_STATO_FINALE=(select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK') and ts.id_istruttoria = 56456456478), 'CALCOLO_ACS', 'OK', 'DUT_001', '{}', '{"variabiliCalcolo":[{"tipoVariabile":"DFIMPLIQACZ","valNumber":3.6100},{"tipoVariabile":"ACSSUPAMM_M8","valNumber":352.55},{"tipoVariabile":"ACZCAPIACC_310","valNumber":3.6100},{"tipoVariabile":"GREIMPRIC","valNumber":176.28},{"tipoVariabile":"TITVAL","valNumber":99.65},{"tipoVariabile":"ACZCAPIACC_310","valNumber":97.66}]}', '{}');
INSERT INTO A4GT_PASSO_TRANSIZIONE VALUES (nxtnbr.nextval, 0, (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts where ts.ID_STATO_FINALE=(select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE') and ts.id_istruttoria = 56456456478), 'LIQUIDABILITA', 'OK', 'DUT_001', '{}', '{"variabiliCalcolo":[{"tipoVariabile":"DFIMPLIQACZ","valNumber":3.6100},{"tipoVariabile":"ACSSUPAMM_M8","valNumber":352.55},{"tipoVariabile":"ACZCAPIACC_310","valNumber":3.6100},{"tipoVariabile":"GREIMPRIC","valNumber":176.28},{"tipoVariabile":"TITVAL","valNumber":99.65},{"tipoVariabile":"ACZCAPIACC_310","valNumber":97.66}]}', '{"variabiliCalcolo":[],"esitiControlli":[{"tipoControllo":"BRIDUNVL129_erede","esito":false,"livelloControllo":"INFO"}],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}');


COMMIT;
