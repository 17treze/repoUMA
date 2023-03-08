-- 1. Inserimento transizione
Insert into A4GT_TRANSIZIONE_ISTRUTTORIA (ID,VERSIONE,ID_ISTRUTTORIA,ID_STATO_INIZIALE,ID_STATO_FINALE,DATA_ESECUZIONE)
values (NXTNBR.nextval,1,
(select i.id from a4gt_istruttoria i 
join a4gt_domanda d on d.id = i.id_domanda
where d.CUAA_INTESTATARIO = 'SGNNLN54T28A372J'
and
i.SOSTEGNO = 'SUPERFICIE'),
(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE'),
(select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_INTERSOSTEGNO_OK'),
to_date('07/02/2019 15:26:20', 'dd/mm/yyyy HH24:MI:SS'));

-- 2. Inserimento passi di lavorazione
INSERT INTO A4GT_PASSO_TRANSIZIONE VALUES (NXTNBR.NEXTVAL, 0,
(SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts
join a4gt_istruttoria i on i.id = ts.id_istruttoria
join A4GT_DOMANDA d on i.id_domanda = d.id 
where d.cuaa_intestatario = 'SGNNLN54T28A372J' 
and SOSTEGNO = 'SUPERFICIE'
AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')), 
'CONTROLLO_IMPORTO_ANTIMAFIA', 'OK', 'DUT_059', 
'{"variabiliCalcolo":[{"tipoVariabile":"IMPMINIMOLIQ","valNumber":300.00},{"tipoVariabile":"HASRICACZ","valBoolean":false},{"tipoVariabile":"ACZIMPCALCTOT","valNumber":0.00},{"tipoVariabile":"HASRICACS","valBoolean":true},{"tipoVariabile":"STATOACS","valString":"CONTROLLI_INTERSOSTEGNO_OK"},{"tipoVariabile":"ACSIMPCALCTOT","valNumber":2422.21},{"tipoVariabile":"HASRICBPS","valBoolean":true},{"tipoVariabile":"STATOBPS","valString":"PAGAMENTO_AUTORIZZATO"},{"tipoVariabile":"DISIMPCALC","valNumber":253.99}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"IMPMINIMOLIQ","valNumber":300.00},{"tipoVariabile":"HASRICACZ","valBoolean":false},{"tipoVariabile":"ACZIMPCALCTOT","valNumber":0.00},{"tipoVariabile":"HASRICACS","valBoolean":true},{"tipoVariabile":"STATOACS","valString":"CONTROLLI_INTERSOSTEGNO_OK"},{"tipoVariabile":"ACSIMPCALCTOT","valNumber":2422.21},{"tipoVariabile":"HASRICBPS","valBoolean":true},{"tipoVariabile":"STATOBPS","valString":"PAGAMENTO_AUTORIZZATO"},{"tipoVariabile":"DISIMPCALC","valNumber":253.99}]}', '{"variabiliCalcolo":[],"variabiliCalcoloDaStampare":[]}', '{"variabiliCalcolo":[],"esitiControlli":[{"tipoControllo":"importoMinimoAntimafia","esito":true,"valString":"NON_RAGGIUNTO","livelloControllo":"INFO"}],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}');

INSERT INTO A4GT_PASSO_TRANSIZIONE VALUES (NXTNBR.NEXTVAL, 0,
(SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts
join a4gt_istruttoria i on i.id = ts.id_istruttoria
join A4GT_DOMANDA d on i.id_domanda = d.id 
where d.cuaa_intestatario = 'SGNNLN54T28A372J' 
and SOSTEGNO = 'SUPERFICIE'
AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')), 
'CONTROLLO_IMPORTO_MINIMO', 'OK', 'DUT_042', 
'{"variabiliCalcolo":[{"tipoVariabile":"IMPMINIMOLIQ","valNumber":300.00},{"tipoVariabile":"HASRICACZ","valBoolean":false},{"tipoVariabile":"ACZIMPCALCTOT","valNumber":0.00},{"tipoVariabile":"HASRICACS","valBoolean":true},{"tipoVariabile":"STATOACS","valString":"CONTROLLI_INTERSOSTEGNO_OK"},{"tipoVariabile":"ACSIMPCALCTOT","valNumber":2422.21},{"tipoVariabile":"HASRICBPS","valBoolean":true},{"tipoVariabile":"STATOBPS","valString":"PAGAMENTO_AUTORIZZATO"},{"tipoVariabile":"DISIMPCALC","valNumber":253.99}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"IMPMINIMOLIQ","valNumber":300.00},{"tipoVariabile":"HASRICACZ","valBoolean":false},{"tipoVariabile":"ACZIMPCALCTOT","valNumber":0.00},{"tipoVariabile":"HASRICACS","valBoolean":true},{"tipoVariabile":"STATOACS","valString":"CONTROLLI_INTERSOSTEGNO_OK"},{"tipoVariabile":"ACSIMPCALCTOT","valNumber":2422.21},{"tipoVariabile":"HASRICBPS","valBoolean":true},{"tipoVariabile":"STATOBPS","valString":"PAGAMENTO_AUTORIZZATO"},{"tipoVariabile":"DISIMPCALC","valNumber":253.99}]}', '{"variabiliCalcolo":[],"variabiliCalcoloDaStampare":[]}', '{"variabiliCalcolo":[],"esitiControlli":[{"tipoControllo":"BRIDUSDS040_importoMinimo","esito":true,"livelloControllo":"NULL"}],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}');

INSERT INTO A4GT_PASSO_TRANSIZIONE VALUES (NXTNBR.NEXTVAL, 0,
(SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts
join a4gt_istruttoria i on i.id = ts.id_istruttoria
join A4GT_DOMANDA d on i.id_domanda = d.id 
where d.cuaa_intestatario = 'SGNNLN54T28A372J' 
and SOSTEGNO = 'SUPERFICIE'
AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')),
'CONTROLLO_INTERSOSTEGNO', 'OK', 'DUF_033', NULL, NULL, 
'{"variabiliCalcolo":[],"esitiControlli":[],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}');

INSERT INTO A4GT_PASSO_TRANSIZIONE VALUES (NXTNBR.NEXTVAL, 0,
(SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts
join a4gt_istruttoria i on i.id = ts.id_istruttoria
join A4GT_DOMANDA d on i.id_domanda = d.id 
where d.cuaa_intestatario = 'SGNNLN54T28A372J' 
and SOSTEGNO = 'SUPERFICIE'
AND ID_STATO_INIZIALE = (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE')),'DISCIPLINA_FINANZIARIA', 'OK', 'no esito', 
'{"variabiliCalcolo":[{"tipoVariabile":"IMPMINIMOLIQ","valNumber":300.00},{"tipoVariabile":"HASRICACZ","valBoolean":false},{"tipoVariabile":"ACZIMPCALCTOT","valNumber":0.00},{"tipoVariabile":"HASRICACS","valBoolean":true},{"tipoVariabile":"STATOACS","valString":"CONTROLLI_INTERSOSTEGNO_OK"},{"tipoVariabile":"ACSIMPCALCTOT","valNumber":2422.21},{"tipoVariabile":"HASRICBPS","valBoolean":true},{"tipoVariabile":"STATOBPS","valString":"PAGAMENTO_AUTORIZZATO"},{"tipoVariabile":"DISIMPCALC","valNumber":253.99}]}', '{"variabiliCalcolo":[{"tipoVariabile":"ACSIMPCALC_M10","valNumber":0.00},{"tipoVariabile":"DFFRPAGACS_M17","valNumber":1746.01},{"tipoVariabile":"ACSIMPCALC_M8","valNumber":0.00},{"tipoVariabile":"ACSIMPCALC_M14","valNumber":0.00},{"tipoVariabile":"ACSIMPCALC","valNumber":2422.21},{"tipoVariabile":"DFFR","valNumber":2000.00},{"tipoVariabile":"DFIMPDFDISACS_M16","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACS_M10","valNumber":0.00},{"tipoVariabile":"ACSIMPCALC_M16","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACS_M15","valNumber":0.00},{"tipoVariabile":"DFIMPLIQACS","valNumber":2084.11},{"tipoVariabile":"DFFRAPPDIS","valNumber":253.99},{"tipoVariabile":"DFFRPAGACS_M14","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACS_M17","valNumber":338.10},{"tipoVariabile":"DFFRPAGACS_M16","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACS_M11","valNumber":0.00},{"tipoVariabile":"ACSIMPCALC_M9","valNumber":0.00},{"tipoVariabile":"DFFRPAGACS_M11","valNumber":0.00},{"tipoVariabile":"DFPERC","valNumber":0.50000000},{"tipoVariabile":"ACSIMPCALC_M11","valNumber":0.00},{"tipoVariabile":"ACSIMPCALC_M15","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ","valNumber":0.00},{"tipoVariabile":"DFFRPAGACS_M10","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACS_M8","valNumber":0.00},{"tipoVariabile":"ACSIMPCALC_M17","valNumber":2422.21},{"tipoVariabile":"DFFRPAGACS","valNumber":1746.01},{"tipoVariabile":"DFIMPRIDACS","valNumber":338.10},{"tipoVariabile":"DFFRPAGACS_M8","valNumber":0.00},{"tipoVariabile":"DFFRAPPACS","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACS_M9","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACS_M14","valNumber":0.00},{"tipoVariabile":"DFFRPAGACS_M9","valNumber":0.00},{"tipoVariabile":"DFFRPAGACS_M15","valNumber":0.00}]}', '{"variabiliCalcolo":[],"esitiControlli":[],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}');

--3. Aggiornamento stato domanda
UPDATE A4GT_ISTRUTTORIA set ID_STATO_LAVORAZIONE = (select id from a4gd_stato_lav_sostegno where identificativo = 'PAGAMENTO_AUTORIZZATO') where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 189674) and SOSTEGNO = 'SUPERFICIE';

COMMIT;