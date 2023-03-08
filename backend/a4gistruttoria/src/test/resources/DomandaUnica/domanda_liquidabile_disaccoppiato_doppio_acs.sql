UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'DISACCOPPIATO';
UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE';

Insert into A4GT_TRANSIZIONE_ISTRUTTORIA 
(ID,VERSIONE,ID_ISTRUTTORIA,ID_STATO_INIZIALE,ID_STATO_FINALE,DATA_ESECUZIONE) 
values (NXTNBR.nextval,0,
   (select id from A4GT_ISTRUTTORIA where 
   		id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and 
   		SOSTEGNO = 'DISACCOPPIATO'
   ),
(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'CONTROLLI_CALCOLO_OK'),
(select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE'),
to_date('07/02/2019 15:26:20', 'dd/mm/yyyy HH24:MI:SS'));

(NXTNBR.nextval,0,
(select ts.id from A4GT_TRANSIZIONE_ISTRUTTORIA ts
join a4gt_istruttoria i on i.id = ts.id_istruttoria
join a4gt_domanda d on d.id = i.id_domanda
where d.NUMERO_DOMANDA = 189674
and i.SOSTEGNO = 'DISACCOPPIATO'
and ts.ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'CONTROLLI_CALCOLO_OK')),
'LIQUIDABILITA','OK','DUF_019','{"variabiliCalcolo":[],"esitiControlli":[{"tipoControllo":"BRIDUSDL038_titolare","esito":false},{"tipoControllo":"BRIDUSDL037_iban","esito":true},{"tipoControllo":"BRIDUSDL039_agea","esito":false},{"tipoControllo":"BRIDUSDL038_titolare","esito":false},{"tipoControllo":"BRIDUSDL037_iban","esito":true},{"tipoControllo":"BRIDUSDL039_agea","esito":false}],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}',null);

Insert into A4GT_TRANSIZIONE_ISTRUTTORIA (ID,VERSIONE,ID_ISTRUTTORIA,ID_STATO_INIZIALE,ID_STATO_FINALE,DATA_ESECUZIONE) 
values (NXTNBR.nextval,0,
   (select id from A4GT_ISTRUTTORIA where 
   		id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and 
   		SOSTEGNO = 'SUPERFICIE'
   ),
(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'CONTROLLI_CALCOLO_OK'),
(select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE'),
to_date('07/02/2019 15:26:20', 'dd/mm/yyyy HH24:MI:SS'));

Insert into A4GT_PASSO_TRANSIZIONE (ID,VERSIONE,ID_TRANSIZ_SOSTEGNO,CODICE_PASSO,ESITO,CODICE_ESITO,DATI_SINTESI_LAVORAZIONE) values
(NXTNBR.nextval,0,
(select ts.id from A4GT_TRANSIZIONE_ISTRUTTORIA ts
join a4gt_istruttoria i on i.id = ts.id_istruttoria
join a4gt_domanda d on d.id = i.id_domanda
where d.NUMERO_DOMANDA = 189674
and i.SOSTEGNO = 'SUPERFICIE'
and ts.data_esecuzione = '2019-02-07' 
and ts.ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'CONTROLLI_CALCOLO_OK')),
'LIQUIDABILITA','OK','DUF_019',
'{"variabiliCalcolo":[],"esitiControlli":[{"tipoControllo":"BRIDUSDL038_titolare","esito":false},{"tipoControllo":"BRIDUSDL037_iban","esito":true},{"tipoControllo":"BRIDUSDL039_agea","esito":false},{"tipoControllo":"BRIDUSDL038_titolare","esito":false},{"tipoControllo":"BRIDUSDL037_iban","esito":true},{"tipoControllo":"BRIDUSDL039_agea","esito":false}],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}');

Insert into A4GT_TRANSIZIONE_ISTRUTTORIA (ID,VERSIONE,ID_ISTRUTTORIA,ID_STATO_INIZIALE,ID_STATO_FINALE,DATA_ESECUZIONE) 
values (NXTNBR.nextval,0,
   (select id from A4GT_ISTRUTTORIA where 
   		id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and 
   		SOSTEGNO = 'SUPERFICIE'
   ),
(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'CONTROLLI_CALCOLO_OK'),
(select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE'),
to_date('11/02/2019 15:26:20', 'dd/mm/yyyy HH24:MI:SS'));

Insert into A4GT_PASSO_TRANSIZIONE (ID,VERSIONE,ID_TRANSIZ_SOSTEGNO,CODICE_PASSO,ESITO,CODICE_ESITO,DATI_SINTESI_LAVORAZIONE)
values 
(NXTNBR.nextval,0,
(select ts.id from A4GT_TRANSIZIONE_ISTRUTTORIA ts
join a4gt_istruttoria i on i.id = ts.id_istruttoria
join a4gt_domanda d on d.id = i.id_domanda
where d.NUMERO_DOMANDA = 189674
and i.SOSTEGNO = 'SUPERFICIE'
and ts.data_esecuzione = '2019-02-11' 
and ts.ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'CONTROLLI_CALCOLO_OK')),
'LIQUIDABILITA','OK','DUF_019',
'{"variabiliCalcolo":[],"esitiControlli":[{"tipoControllo":"BRIDUSDL038_titolare","esito":false},{"tipoControllo":"BRIDUSDL037_iban","esito":true},{"tipoControllo":"BRIDUSDL039_agea","esito":false},{"tipoControllo":"BRIDUSDL038_titolare","esito":false},{"tipoControllo":"BRIDUSDL037_iban","esito":true},{"tipoControllo":"BRIDUSDL039_agea","esito":false}],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}');

commit;