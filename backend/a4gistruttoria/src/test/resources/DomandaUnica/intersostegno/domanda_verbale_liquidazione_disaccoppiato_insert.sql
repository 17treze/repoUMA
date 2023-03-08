INSERT INTO a4gt_elenco_liquidazione
VALUES (9999999,0,'009-801-20190513-9999999',TO_DATE('06/14/2018 18:56:48', 'MM/DD/YYYY HH24:MI:SS'),
'D100000100920180420180424009201800182984TINGLERHOF SOCIETA'' AGRICOLA SEMPLICE                                 NA                                                NA                                                 00000000         02227750227     FR ROVEDA MASO TINGHERLA 180                 022090 TN38050010817835220IT80Z0000000894070000002778138000261101825101000009000000000012657580102611018261010000090000000000000000802026110183010100000900000000000000008030261101832101000009000000000000000080000811118071010000090000000000036027801008111180810100000900000000000587838000131131821101000009000000000000000080101311318221010000090000000000056428      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000                                                                                                                                                                009   0010580271                                                                                                                                                                                       
D100000200920180420180503009201800184314MARCOLLA DIEGO                                                        MARCOLLA                                          DIEGO                                             M19660226022062TN MRCDGI66B26C794LMASI VIA SANTA MARGHERITA 2                  022200 TN38010010828235670IT67V0000193007170000000316898000261101825101000004276800000000000080102611018261010000042768000000000000802026110183010100000427680000000000008030261101832101000004276800000000000080000811118071010000042768000000031689801008111180810100000427680000000000008000131131821101000000000000000000000080101311318221010000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000                                                                                                                                                                009   0010580271                                                                                                                                                                                       
',FILE_READ('src\test\resources\DomandaUnica\intersostegno\stampa\output\verbaleLiquidazioneDisaccoppiato.pdf'),'ELENCO_INVIATO_SOC');


-- Istruttoria domanda 400000
INSERT INTO A4GT_DOMANDA (ID,VERSIONE,NUMERO_DOMANDA,COD_MODULO_DOMANDA,DESC_MODULO_DOMANDA,ANNO_CAMPAGNA,STATO,CUAA_INTESTATARIO,DT_PRESENTAZIONE,NUM_DOMANDA_RETTIFICATA,COD_ENTE_COMPILATORE,DESC_ENTE_COMPILATORE,RAGIONE_SOCIALE,DT_PROTOCOLLAZIONE,DT_PRESENTAZ_ORIGINARIA,DT_PROTOCOLLAZ_ORIGINARIA) 
values (999123,0,400000,'BPS_2018','PAGAMENTI DIRETTI',2018,'IN_ISTRUTTORIA','MRCDGI66B26C794L',to_date('13/04/2018 02:00:00','dd/mm/yyyy hh24:mi:ss'),null,'4','CAA COLDIRETTI DEL TRENTINO - 003','MARCOLLA DIEGO',to_date('13/04/2018 02:00:00','dd/mm/yyyy hh24:mi:ss','DD-MON-RR'),null,null);
	
INSERT INTO A4GT_ISTRUTTORIA 
	(ID,VERSIONE,
	ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE,TIPOLOGIA,ID_ELENCO_LIQUIDAZIONE)
VALUES 
	(nxtnbr.nextval,0,
	999123, --(SELECT ID FROM a4gt_domanda WHERE numero_domanda = 400000),
	'DISACCOPPIATO',
	(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'PAGAMENTO_AUTORIZZATO'),
	'SALDO',9999999);

INSERT INTO A4GT_TRANSIZIONE_ISTRUTTORIA 
	(ID,versione,
	id_istruttoria,
	id_stato_iniziale,
	id_stato_finale,
	data_esecuzione) 
VALUES (
	nxtnbr.nextval,0,
	nxtnbr.currval-1,
	(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'LIQUIDABILE'),
	(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'CONTROLLI_INTERSOSTEGNO_OK'),
	TO_DATE('06/14/2018 18:56:48', 'MM/DD/YYYY HH24:MI:SS'));
        
INSERT INTO A4GT_PASSO_TRANSIZIONE
	(ID,versione,
	id_transiz_sostegno,
	codice_passo,esito,codice_esito,dati_input,dati_output,dati_sintesi_lavorazione)
VALUES 
	(nxtnbr.nextval
	,0
	,nxtnbr.currval-1	
	,'DISCIPLINA_FINANZIARIA'
	,'OK'
	,'no esito'
	,NULL
	,'{"variabiliCalcolo":[{"tipoVariabile":"DFFRPAGDIS","valNumber":316.89},{"tipoVariabile":"DFIMPLIQDIS","valNumber":316.89},{"tipoVariabile":"DFIMPRIDDIS","valNumber":0.00},{"tipoVariabile":"DFFRPAGDISBPS","valNumber":0.00},{"tipoVariabile":"DFFRPAGDISGRE","valNumber":316.89},{"tipoVariabile":"DFFRPAGDISGIO","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISBPS","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISGRE","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISGIO","valNumber":0.00}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"DFFRPAGDIS","valNumber":316.89},{"tipoVariabile":"DFIMPLIQDIS","valNumber":316.89},{"tipoVariabile":"DFIMPRIDDIS","valNumber":0.00},{"tipoVariabile":"DFFRPAGDISBPS","valNumber":0.00},{"tipoVariabile":"DFFRPAGDISGRE","valNumber":316.89},{"tipoVariabile":"DFFRPAGDISGIO","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISBPS","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISGRE","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISGIO","valNumber":0.00}]}'
	,NULL);

-- Istruttoria domanda 400002
INSERT INTO A4GT_DOMANDA (ID,VERSIONE,NUMERO_DOMANDA,COD_MODULO_DOMANDA,DESC_MODULO_DOMANDA,ANNO_CAMPAGNA,STATO,CUAA_INTESTATARIO,DT_PRESENTAZIONE,NUM_DOMANDA_RETTIFICATA,COD_ENTE_COMPILATORE,DESC_ENTE_COMPILATORE,RAGIONE_SOCIALE,DT_PROTOCOLLAZIONE,DT_PRESENTAZ_ORIGINARIA,DT_PROTOCOLLAZ_ORIGINARIA) 
values (999456,0,400002,'BPS_2018','PAGAMENTI DIRETTI',2018,'IN_ISTRUTTORIA','02227750227',to_date('13/04/2018 02:00:00','dd/mm/yyyy hh24:mi:ss'),null,'4','CAA COLDIRETTI DEL TRENTINO - 003','TINGLERHOF SOCIETA'' AGRICOLA SEMPLICE',to_date('13/04/2018 02:00:00','dd/mm/yyyy hh24:mi:ss','DD-MON-RR'),null,null);
	
INSERT INTO A4GT_ISTRUTTORIA 
	(ID,VERSIONE,
	ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE,TIPOLOGIA,ID_ELENCO_LIQUIDAZIONE)
VALUES 
	(nxtnbr.nextval,0,
	999456, --(SELECT ID FROM a4gt_domanda WHERE numero_domanda = 400002),
	'DISACCOPPIATO',
	(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'PAGAMENTO_AUTORIZZATO'), 
	'SALDO',9999999);

INSERT INTO A4GT_TRANSIZIONE_ISTRUTTORIA 
	(ID,versione,
	id_istruttoria,
	id_stato_iniziale,
	id_stato_finale,
	data_esecuzione) 
VALUES (
	nxtnbr.nextval,0,
	nxtnbr.currval-1,
	(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'LIQUIDABILE'),
	(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'CONTROLLI_INTERSOSTEGNO_OK'),
	TO_DATE('06/14/2018 18:56:48', 'MM/DD/YYYY HH24:MI:SS'));

INSERT INTO A4GT_PASSO_TRANSIZIONE
	(ID,versione,
	id_transiz_sostegno,
	codice_passo,esito,codice_esito,dati_input,dati_output,dati_sintesi_lavorazione)
VALUES 
	(nxtnbr.nextval
	,0
	,nxtnbr.currval-1
	,'DISCIPLINA_FINANZIARIA'
	,'OK'
	,'no esito'
	,NULL
	,'{"variabiliCalcolo":[{"tipoVariabile":"DFFRPAGDIS","valNumber":1626.02},{"tipoVariabile":"DFIMPLIQDIS","valNumber":2778.13},{"tipoVariabile":"DFIMPRIDDIS","valNumber":16.50},{"tipoVariabile":"DFFRPAGDISBPS","valNumber":1265.75},{"tipoVariabile":"DFFRPAGDISGRE","valNumber":360.27},{"tipoVariabile":"DFFRPAGDISGIO","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISBPS","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISGRE","valNumber":587.83},{"tipoVariabile":"DFIMPDFDISGIO","valNumber":564.28}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"DFFRPAGDIS","valNumber":1626.02},{"tipoVariabile":"DFIMPLIQDIS","valNumber":2778.13},{"tipoVariabile":"DFIMPRIDDIS","valNumber":16.50},{"tipoVariabile":"DFFRPAGDISBPS","valNumber":1265.75},{"tipoVariabile":"DFFRPAGDISGRE","valNumber":360.27},{"tipoVariabile":"DFFRPAGDISGIO","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISBPS","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISGRE","valNumber":587.83},{"tipoVariabile":"DFIMPDFDISGIO","valNumber":564.28}]}'
	,NULL);

-- Inserirsco i relativi elenchi di liquidazione
-- INSERT INTO a4gr_domanda_elenco
-- VALUES (nxtnbr.nextval,0,999123,9999999);

-- INSERT INTO a4gr_domanda_elenco
-- VALUES (nxtnbr.nextval,0,999456,9999999);


COMMIT;