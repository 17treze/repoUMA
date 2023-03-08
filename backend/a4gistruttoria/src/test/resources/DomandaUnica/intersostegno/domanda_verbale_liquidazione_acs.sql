INSERT INTO A4GT_DOMANDA (ID,VERSIONE,NUMERO_DOMANDA,COD_MODULO_DOMANDA,DESC_MODULO_DOMANDA,ANNO_CAMPAGNA,STATO,CUAA_INTESTATARIO,DT_PRESENTAZIONE,NUM_DOMANDA_RETTIFICATA,COD_ENTE_COMPILATORE,DESC_ENTE_COMPILATORE,RAGIONE_SOCIALE,DT_PROTOCOLLAZIONE,DT_PRESENTAZ_ORIGINARIA,DT_PROTOCOLLAZ_ORIGINARIA) 
values (NXTNBR.nextval,0,1200000,'BPS_2018','PAGAMENTI DIRETTI',2018,'IN_ISTRUTTORIA','RRRRED82B02F83XX',to_date('13/04/2018 02:00:00','dd/mm/yyyy hh24:mi:ss'),null,'4','CAA COLDIRETTI DEL TRENTINO - 003','SALVATORE DE LUCA',to_date('13/04/2018 02:00:00','dd/mm/yyyy hh24:mi:ss','DD-MON-RR'),null,null);
INSERT INTO A4GT_DOMANDA (ID,VERSIONE,NUMERO_DOMANDA,COD_MODULO_DOMANDA,DESC_MODULO_DOMANDA,ANNO_CAMPAGNA,STATO,CUAA_INTESTATARIO,DT_PRESENTAZIONE,NUM_DOMANDA_RETTIFICATA,COD_ENTE_COMPILATORE,DESC_ENTE_COMPILATORE,RAGIONE_SOCIALE,DT_PROTOCOLLAZIONE,DT_PRESENTAZ_ORIGINARIA,DT_PROTOCOLLAZ_ORIGINARIA) 
values (NXTNBR.nextval,0,1200002,'BPS_2018','PAGAMENTI DIRETTI',2018,'IN_ISTRUTTORIA','RRRRED82B02F83YY',to_date('13/04/2018 02:00:00','dd/mm/yyyy hh24:mi:ss'),null,'4','CAA COLDIRETTI DEL TRENTINO - 003','SALVATORE DE LUCA',to_date('13/04/2018 02:00:00','dd/mm/yyyy hh24:mi:ss','DD-MON-RR'),null,null);

INSERT INTO a4gt_elenco_liquidazione
VALUES (9999999,0,
	'009-801-20190513-9999999',TO_DATE('06/14/2018 18:56:48', 'MM/DD/YYYY HH24:MI:SS'),
	'D100000100920180420180618009201800191185AZIENDA AGRICOLA CONTI BOSSI FEDRIGOTTI SOCIETA'' SEMPLICE             NA                                                NA                                                 00000000         00122280225     VIA UNIONE 43                                022161 TN38068010821020801IT90M0020300601500000002852588000261101825101000032770000000020000080102611018261010000327700000000020821802026110183010100003277000000000000008030261101832101000032770000000000000080000811118071010000327700000000000000801008111180810100003277000000000644388000131131821101000000000000000000000080101311318221010000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000                                                                                                                                                                009   0005542920                                                                                                                                                                                       '
	,FILE_READ('src\test\resources\DomandaUnica\intersostegno\stampa\output\verbaleLiquidazioneAcs.pdf'),'ELENCO_INVIATO_SOC');

-- INSERT INTO a4gr_domanda_elenco
-- VALUES (nxtnbr.nextval,0,(SELECT ID FROM a4gt_domanda WHERE numero_domanda = 1200000),9999999);

INSERT INTO A4GT_ISTRUTTORIA (ID,VERSIONE,ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE, TIPOLOGIA, id_elenco_liquidazione)
VALUES (nxtnbr.nextval,0,(SELECT ID FROM a4gt_domanda WHERE numero_domanda = 1200000),'SUPERFICIE',(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'PAGAMENTO_AUTORIZZATO'), 'SALDO', 9999999);
INSERT INTO A4GT_TRANSIZIONE_ISTRUTTORIA 
(ID,versione,ID_ISTRUTTORIA,id_stato_iniziale,id_stato_finale,data_esecuzione) 
VALUES 
(nxtnbr.nextval,0,
NXTNBR.currval -1,
(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'LIQUIDABILE'),
(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'CONTROLLI_INTERSOSTEGNO_OK'),
TO_DATE('06/14/2018 18:56:48', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO A4GT_PASSO_TRANSIZIONE (ID,versione,id_transiz_sostegno,codice_passo,esito,codice_esito,dati_input,dati_output,dati_sintesi_lavorazione)
VALUES (nxtnbr.nextval,0,
NXTNBR.currval -1, 
'DISCIPLINA_FINANZIARIA','OK','no esito',NULL,'{"variabiliCalcolo":[{"tipoVariabile":"DFFRPAGACS","valNumber":1084.38},{"tipoVariabile":"DFIMPRIDACS","valNumber":0.00},{"tipoVariabile":"DFIMPLIQACS","valNumber":123.12},{"tipoVariabile":"DFFRPAGACS_M8","valNumber":707.15},{"tipoVariabile":"DFFRPAGACS_M9","valNumber":456},{"tipoVariabile":"DFFRPAGACS_M10","valNumber":0.00},{"tipoVariabile":"DFFRPAGACS_M11","valNumber":0.00},{"tipoVariabile":"DFFRPAGACS_M14","valNumber":0.00},{"tipoVariabile":"DFFRPAGACS_M15","valNumber":0.00},{"tipoVariabile":"DFFRPAGACS_M16","valNumber":789.58},{"tipoVariabile":"DFFRPAGACS_M17","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACS_M8","valNumber":855.47},{"tipoVariabile":"DFIMPDFDISACS_M9","valNumber":707.15},{"tipoVariabile":"DFIMPDFDISACS_M10","valNumber":377.14},{"tipoVariabile":"DFIMPDFDISACS_M11","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACS_M14","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACS_M15","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACS_M16","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACS_M17","valNumber":0.00}]}',NULL);

-- INSERT INTO a4gr_domanda_elenco
-- VALUES (nxtnbr.nextval,0,(SELECT ID FROM a4gt_domanda WHERE numero_domanda = 1200002),9999999);

INSERT INTO A4GT_ISTRUTTORIA (ID,VERSIONE,ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE, TIPOLOGIA, id_elenco_liquidazione)
VALUES (nxtnbr.nextval,0,(SELECT ID FROM a4gt_domanda WHERE numero_domanda = 1200002),'SUPERFICIE',(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'PAGAMENTO_AUTORIZZATO'), 'SALDO', 9999999);
INSERT INTO A4GT_TRANSIZIONE_ISTRUTTORIA 
(ID,versione,ID_ISTRUTTORIA,id_stato_iniziale,id_stato_finale,data_esecuzione) 
VALUES 
(nxtnbr.nextval,0,
NXTNBR.currval -1,
(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'LIQUIDABILE'),
(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'CONTROLLI_INTERSOSTEGNO_OK'),
TO_DATE('06/14/2018 18:56:48', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO A4GT_PASSO_TRANSIZIONE (ID,versione,id_transiz_sostegno,codice_passo,esito,codice_esito,dati_input,dati_output,dati_sintesi_lavorazione)
VALUES (nxtnbr.nextval,0,
NXTNBR.currval -1, 
'DISCIPLINA_FINANZIARIA','OK','no esito',NULL,'{"variabiliCalcolo":[{"tipoVariabile":"DFFRPAGACS","valNumber":1599.38},{"tipoVariabile":"DFIMPRIDACS","valNumber":45.00},{"tipoVariabile":"DFIMPLIQACS","valNumber":123.12},{"tipoVariabile":"DFFRPAGACS_M8","valNumber":707.15},{"tipoVariabile":"DFFRPAGACS_M9","valNumber":456},{"tipoVariabile":"DFFRPAGACS_M10","valNumber":1111.11},{"tipoVariabile":"DFFRPAGACS_M11","valNumber":0.00},{"tipoVariabile":"DFFRPAGACS_M14","valNumber":0.86},{"tipoVariabile":"DFFRPAGACS_M15","valNumber":22.36},{"tipoVariabile":"DFFRPAGACS_M16","valNumber":789.58},{"tipoVariabile":"DFFRPAGACS_M17","valNumber":68.69},{"tipoVariabile":"DFIMPDFDISACS_M8","valNumber":855.47},{"tipoVariabile":"DFIMPDFDISACS_M9","valNumber":526.15},{"tipoVariabile":"DFIMPDFDISACS_M10","valNumber":377.14},{"tipoVariabile":"DFIMPDFDISACS_M11","valNumber":77.00},{"tipoVariabile":"DFIMPDFDISACS_M14","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACS_M15","valNumber":9513.75},{"tipoVariabile":"DFIMPDFDISACS_M16","valNumber":5896.12},{"tipoVariabile":"DFIMPDFDISACS_M17","valNumber":1234.56}]}',NULL);

COMMIT;