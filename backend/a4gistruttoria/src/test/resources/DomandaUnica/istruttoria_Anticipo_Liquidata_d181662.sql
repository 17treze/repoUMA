UPDATE A4GT_ISTRUTTORIA 
set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'PAGAMENTO_AUTORIZZATO') 
where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 181662) 
and SOSTEGNO = 'DISACCOPPIATO'
and TIPOLOGIA = 'ANTICIPO';

Insert into A4GT_TRANSIZIONE_ISTRUTTORIA (ID,VERSIONE,ID_ISTRUTTORIA,ID_STATO_INIZIALE,ID_STATO_FINALE,DATA_ESECUZIONE) values
(15645645547,0,564564554,
(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'CONTROLLI_CALCOLO_OK'),
(select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE'),
to_date('07/03/2019 15:26:20', 'dd/mm/yyyy HH24:MI:SS'));

Insert into A4GT_PASSO_TRANSIZIONE (ID,VERSIONE,ID_TRANSIZ_SOSTEGNO,CODICE_PASSO,ESITO,CODICE_ESITO,DATI_SINTESI_LAVORAZIONE) values
(NXTNBR.nextval,
0,
15645645547,
'LIQUIDABILITA',
'OK',
'DUF_019',
'{"variabiliCalcolo":[],"esitiControlli":[{"tipoControllo":"BRIDUSDL038_titolare","esito":false},{"tipoControllo":"BRIDUSDL037_iban","esito":true},{"tipoControllo":"BRIDUSDL039_agea","esito":false},{"tipoControllo":"BRIDUSDL038_titolare","esito":false},{"tipoControllo":"BRIDUSDL037_iban","esito":true},{"tipoControllo":"BRIDUSDL039_agea","esito":false}],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}');

Insert into A4GT_TRANSIZIONE_ISTRUTTORIA (ID,VERSIONE,ID_ISTRUTTORIA,ID_STATO_INIZIALE,ID_STATO_FINALE,DATA_ESECUZIONE) values
(15645645549,0,564564554,
(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE'),
(select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_INTERSOSTEGNO_OK'),
to_date('08/03/2019 15:26:20', 'dd/mm/yyyy HH24:MI:SS'));

Insert into A4GT_PASSO_TRANSIZIONE (ID,VERSIONE,ID_TRANSIZ_SOSTEGNO,CODICE_PASSO,ESITO,CODICE_ESITO,DATI_INPUT,DATI_OUTPUT,DATI_SINTESI_LAVORAZIONE) values
(NXTNBR.nextval,
0,
15645645549,
'DISCIPLINA_FINANZIARIA','OK','no esito',
'{"variabiliCalcolo":[{"tipoVariabile":"DFPERC","valNumber":0.01411917},{"tipoVariabile":"DFFR","valNumber":2000.00},{"tipoVariabile":"BPSIMPCALCFIN","valNumber":1368.79},{"tipoVariabile":"GREIMPCALCFIN","valNumber":197.43},{"tipoVariabile":"GIOIMPCALCFIN","valNumber":0.00},{"tipoVariabile":"DISIMPCALC","valNumber":1566.22},{"tipoVariabile":"ACZIMPCALC","valNumber":0.00},{"tipoVariabile":"ACSIMPCALC","valNumber":0.00},{"tipoVariabile":"DFFRAPPDIS","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ","valNumber":0.00},{"tipoVariabile":"DFFRAPPACS","valNumber":0.00}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"DFPERC","valNumber":0.01411917},{"tipoVariabile":"DFFR","valNumber":2000.00},{"tipoVariabile":"BPSIMPCALCFIN","valNumber":474.83},{"tipoVariabile":"GREIMPCALCFIN","valNumber":251.18},{"tipoVariabile":"GIOIMPCALCFIN","valNumber":0.00},{"tipoVariabile":"DISIMPCALC","valNumber":726.01},{"tipoVariabile":"ACZIMPCALC","valNumber":0.00},{"tipoVariabile":"ACSIMPCALC","valNumber":0.00},{"tipoVariabile":"DFFRAPPDIS","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ","valNumber":0.00},{"tipoVariabile":"DFFRAPPACS","valNumber":0.00}]}',
'{"variabiliCalcolo":[{"tipoVariabile":"DFFRPAGDIS","valNumber":1566.22},{"tipoVariabile":"DFIMPLIQDIS","valNumber":1566.22},{"tipoVariabile":"DFIMPRIDDIS","valNumber":0.00},{"tipoVariabile":"DFFRPAGDISBPS","valNumber":474.83},{"tipoVariabile":"DFFRPAGDISGRE","valNumber":251.18},{"tipoVariabile":"DFFRPAGDISGIO","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISBPS","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISGRE","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISGIO","valNumber":0.00}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"DFFRPAGDIS","valNumber":726.01},{"tipoVariabile":"DFIMPLIQDIS","valNumber":726.01},{"tipoVariabile":"DFIMPRIDDIS","valNumber":0.00},{"tipoVariabile":"DFFRPAGDISBPS","valNumber":474.83},{"tipoVariabile":"DFFRPAGDISGRE","valNumber":251.18},{"tipoVariabile":"DFFRPAGDISGIO","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISBPS","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISGRE","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISGIO","valNumber":0.00}]}',
'{"variabiliCalcolo":[],"esitiControlli":[],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}'
);

commit;
