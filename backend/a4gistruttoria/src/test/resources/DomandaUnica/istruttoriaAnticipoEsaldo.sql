UPDATE A4GT_ISTRUTTORIA 
set TIPOLOGIA = 'ANTICIPO' 
where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 181662) 
and SOSTEGNO = 'DISACCOPPIATO';

Insert into A4GT_ISTRUTTORIA (ID,VERSIONE,ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE, TIPOLOGIA)
values (NXTNBR.nextval,1,
(SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'BRTSRG86A24L174X'),
'DISACCOPPIATO',
(select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK'), 
'SALDO');

Insert into A4GT_TRANSIZIONE_ISTRUTTORIA (ID,VERSIONE,ID_ISTRUTTORIA,ID_STATO_INIZIALE,ID_STATO_FINALE,DATA_ESECUZIONE) values
(NXTNBR.nextval,0,
564564564,
(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'RICHIESTO'),
(select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK'),
to_date('07/02/2019 16:26:20', 'dd/mm/yyyy HH24:MI:SS'));

Insert into A4GT_PASSO_TRANSIZIONE (ID,VERSIONE,ID_TRANSIZ_SOSTEGNO,CODICE_PASSO,ESITO,CODICE_ESITO,DATI_SINTESI_LAVORAZIONE) values
(NXTNBR.nextval,
0,
15645645647,
'LIQUIDABILITA',
'OK',
'DUF_019',
'{"variabiliCalcolo":[],"esitiControlli":[{"tipoControllo":"BRIDUSDL038_titolare","esito":false},{"tipoControllo":"BRIDUSDL037_iban","esito":true},{"tipoControllo":"BRIDUSDL039_agea","esito":false},{"tipoControllo":"BRIDUSDL038_titolare","esito":false},{"tipoControllo":"BRIDUSDL037_iban","esito":true},{"tipoControllo":"BRIDUSDL039_agea","esito":false}],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}');

commit;
