--- Domanda acs MFFFBA75B28L174A (era MFFFBA75B28L174H) - 'MAFFEI FABIO - DOMANDA 1191062 (era 191061)
--modifco CUAA
-- MFFFBA75B28L174A -> MFFFBA75B28L174A


Insert into A4GT_DOMANDA
   (ID, VERSIONE, NUMERO_DOMANDA, COD_MODULO_DOMANDA, DESC_MODULO_DOMANDA, 
    ANNO_CAMPAGNA, STATO, CUAA_INTESTATARIO, DT_PROTOCOLLAZIONE, COD_ENTE_COMPILATORE, 
    DESC_ENTE_COMPILATORE, RAGIONE_SOCIALE, DT_PRESENTAZIONE, IBAN)
 Values
   (NXTNBR.NEXTVAL, 2, 1191062, 'BPS_2018', 'PAGAMENTI DIRETTI', 
    2018, 'IN_ISTRUTTORIA', 
    'MFFFBA75B28L174A', TO_DATE('06/15/2018 17:15:11', 'MM/DD/YYYY HH24:MI:SS'), 18, 
    'CAA CIA - TRENTO - 001', 'MAFFEI FABIO', TO_DATE('06/15/2018 17:14:05', 'MM/DD/YYYY HH24:MI:SS'), 'IT45P0306935264100000000439');




Insert into A4GT_DATI_LAVORAZIONE
   (ID, VERSIONE, ID_DOMANDA, JSON_DATI_RICEVIBILITA, JSON_DATI_SINTESI_IMPEGNI)
 Values
   (NXTNBR.nextval, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'MFFFBA75B28L174A'), 
   '{"aggiornamentoFascicolo":"S","visioneAnomalie":"S","firmaDomanda":"S","archiviazioneDomanda":"S"}', '{"richiestaDisaccoppiato":true,"richiestaSuperfici":false,"richiestaZootecnia":true}');




       
update A4GT_DOMANDA set ANNO_CAMPAGNA = 2020 where NUMERO_DOMANDA = 1191062; 
INSERT INTO A4GT_CONF_ISTRUTTORIA (ID, VERSIONE, ANNO_CAMPAGNA, DT_SCADENZA_DOMANDE_INIZIALI, PERC_PAGAMENTO, PERC_DISCIPLINA_FINANZIARIA)
VALUES (10, 0 , 2020, '2019-07-10', 0.7, 0.01252145);

-------------------------------------------------------------------------------------
Insert into A4GT_ISTRUTTORIA(ID,VERSIONE,ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE, TIPOLOGIA)
values(NXTNBR.nextval,0,
(SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'MFFFBA75B28L174A'),
'ZOOTECNIA',
(select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_INTERSOSTEGNO_OK'), 
'SALDO');


Insert into A4GT_TRANSIZIONE_ISTRUTTORIA
   (ID, VERSIONE, ID_ISTRUTTORIA, ID_STATO_INIZIALE, 
    ID_STATO_FINALE, DATA_ESECUZIONE)
 Values
   (NXTNBR.nextval, 0, 
   NXTNBR.currval-1,
	--(select i.id from a4gt_istruttoria i join a4gt_domanda d on d.id = i.id_domanda WHERE D.CUAA_INTESTATARIO = 'MFFFBA75B28L174A' and i.sostegno = 'ZOOTECNIA'),
   (select id from a4gd_stato_lav_sostegno where identificativo = 'RICHIESTO'), 
    (select id from a4gd_stato_lav_sostegno where identificativo = 'INTEGRATO'), TO_DATE('04/30/2019 14:35:49', 'MM/DD/YYYY HH24:MI:SS'));
Insert into A4GT_TRANSIZIONE_ISTRUTTORIA
   (ID, VERSIONE, ID_ISTRUTTORIA, ID_STATO_INIZIALE, 
    ID_STATO_FINALE, DATA_ESECUZIONE)
 Values
   (NXTNBR.nextval, 1, 
	NXTNBR.currval-2,--(select i.id from a4gt_istruttoria i join a4gt_domanda d on d.id = i.id_domanda WHERE D.CUAA_INTESTATARIO = 'MFFFBA75B28L174A' and i.sostegno = 'ZOOTECNIA'),
   (select id from a4gd_stato_lav_sostegno where identificativo = 'INTEGRATO'), 
    (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK'), TO_DATE('05/21/2019 18:40:30', 'MM/DD/YYYY HH24:MI:SS'));
Insert into A4GT_TRANSIZIONE_ISTRUTTORIA
   (ID, VERSIONE, ID_ISTRUTTORIA, ID_STATO_INIZIALE, 
    ID_STATO_FINALE, DATA_ESECUZIONE)
 Values
   (NXTNBR.nextval, 0,
   NXTNBR.currval - 3,
	--(select i.id from a4gt_istruttoria i join a4gt_domanda d on d.id = i.id_domanda WHERE D.CUAA_INTESTATARIO = 'MFFFBA75B28L174A' and i.sostegno = 'ZOOTECNIA'),
   (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK'), 
    (select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE'), TO_DATE('05/21/2019 17:50:01', 'MM/DD/YYYY HH24:MI:SS'));
Insert into A4GT_TRANSIZIONE_ISTRUTTORIA
   (ID, VERSIONE, ID_ISTRUTTORIA, ID_STATO_INIZIALE, 
    ID_STATO_FINALE, DATA_ESECUZIONE)
 Values
   (NXTNBR.nextval, 0, 
   NXTNBR.currval-4,
	--(select i.id from a4gt_istruttoria i join a4gt_domanda d on d.id = i.id_domanda WHERE D.CUAA_INTESTATARIO = 'MFFFBA75B28L174A' and i.sostegno = 'ZOOTECNIA'),
   (select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE'), 
    (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_INTERSOSTEGNO_OK'), TO_DATE('05/21/2019 18:50:01', 'MM/DD/YYYY HH24:MI:SS'));

      
  --ACZIMPCALCLORDOTOT permette di determinare il calcolo del premio pe ril calcolo dell'importo mnimo  
Insert into A4GT_PASSO_TRANSIZIONE
   (ID, VERSIONE, ID_TRANSIZ_SOSTEGNO, CODICE_PASSO, ESITO, 
    CODICE_ESITO, DATI_INPUT, DATI_OUTPUT, DATI_SINTESI_LAVORAZIONE)
 Values
   (NXTNBR.nextval, 0,
   --NXTNBR.currval-5,
   (select id from A4GT_TRANSIZIONE_ISTRUTTORIA where id_istruttoria = (select i.id from a4gt_istruttoria i join a4gt_domanda d on d.id = i.id_domanda WHERE D.CUAA_INTESTATARIO = 'MFFFBA75B28L174A' and i.sostegno = 'ZOOTECNIA') and id_stato_finale = (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK')), 
   'CALCOLO_ACZ', 'OK', 
    'DUF_010',
    '{"variabiliCalcolo":[{"tipoVariabile":"ACZVAL_320","valNumber":25.00},{"tipoVariabile":"ACZVAL_311","valNumber":100.00},{"tipoVariabile":"ACZUBA_OVI","valNumber":23.55},{"tipoVariabile":"ACZVAL_316","valNumber":100.00},{"tipoVariabile":"ACZCAPIDEBTOT","valNumber":0},{"tipoVariabile":"ACZCONTROLLILOCO","valBoolean":false},{"tipoVariabile":"ACZCAPIRICNETTOT","valNumber":157},{"tipoVariabile":"AZCMPBOV","valBoolean":false},{"tipoVariabile":"ACZCAPIRICTOT","valNumber":157},{"tipoVariabile":"ACZCAPIDUPTOT","valNumber":0},{"tipoVariabile":"ACZUBA_MAC","valNumber":0.00},{"tipoVariabile":"ACZCAPISANZTOT","valNumber":0},{"tipoVariabile":"ACZCAPIRICNET_320","valNumber":157},{"tipoVariabile":"AZCMPOVI","valBoolean":false},{"tipoVariabile":"ACZCAPIACC_320","valNumber":157},{"tipoVariabile":"ACZVAL_315","valNumber":100.00},{"tipoVariabile":"ACZCAPIACCTOT","valNumber":0},{"tipoVariabile":"AGRATT","valBoolean":true},{"tipoVariabile":"ACZVAL_318","valNumber":100.00},{"tipoVariabile":"ACZCAPIRIC_320","valNumber":157},{"tipoVariabile":"ACZIMPACCTOT","valNumber":2943.75},{"tipoVariabile":"PERCSANZ_320","valNumber":0.0000},{"tipoVariabile":"ACZUBATOT","valNumber":23.55},{"tipoVariabile":"ACZVAL_313","valNumber":100.00},{"tipoVariabile":"ACZIMPRICNETTOT","valNumber":2943.75},{"tipoVariabile":"ACZUBA_LAT","valNumber":0.00},{"tipoVariabile":"ACZIMPEROTOT","valNumber":0.00},{"tipoVariabile":"ACZVAL_322","valNumber":100.00},{"tipoVariabile":"ACZVAL_321","valNumber":25.00},{"tipoVariabile":"PERCSANZDET_320","valNumber":0.0000},{"tipoVariabile":"ACZVAL_310","valNumber":100.00},{"tipoVariabile":"PERCRIT","valNumber":0.0000},{"tipoVariabile":"INFOAGRATT","valBoolean":true}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"ACZVAL_320","valNumber":25.00},{"tipoVariabile":"ACZVAL_311","valNumber":100.00},{"tipoVariabile":"ACZUBA_OVI","valNumber":23.55},{"tipoVariabile":"ACZVAL_316","valNumber":100.00},{"tipoVariabile":"ACZCAPIDEBTOT","valNumber":0},{"tipoVariabile":"ACZCONTROLLILOCO","valBoolean":false},{"tipoVariabile":"ACZCAPIRICNETTOT","valNumber":157},{"tipoVariabile":"AZCMPBOV","valBoolean":false},{"tipoVariabile":"ACZCAPIRICTOT","valNumber":157},{"tipoVariabile":"ACZCAPIDUPTOT","valNumber":0},{"tipoVariabile":"ACZUBA_MAC","valNumber":0.00},{"tipoVariabile":"ACZCAPISANZTOT","valNumber":0},{"tipoVariabile":"ACZCAPIRICNET_320","valNumber":157},{"tipoVariabile":"AZCMPOVI","valBoolean":false},{"tipoVariabile":"ACZCAPIACC_320","valNumber":157},{"tipoVariabile":"ACZVAL_315","valNumber":100.00},{"tipoVariabile":"ACZCAPIACCTOT","valNumber":0},{"tipoVariabile":"AGRATT","valBoolean":true},{"tipoVariabile":"ACZVAL_318","valNumber":100.00},{"tipoVariabile":"ACZCAPIRIC_320","valNumber":157},{"tipoVariabile":"ACZIMPACCTOT","valNumber":2943.75},{"tipoVariabile":"PERCSANZ_320","valNumber":0.0000},{"tipoVariabile":"ACZUBATOT","valNumber":23.55},{"tipoVariabile":"ACZVAL_313","valNumber":100.00},{"tipoVariabile":"ACZIMPRICNETTOT","valNumber":2943.75},{"tipoVariabile":"ACZUBA_LAT","valNumber":0.00},{"tipoVariabile":"ACZIMPEROTOT","valNumber":0.00},{"tipoVariabile":"ACZVAL_322","valNumber":100.00},{"tipoVariabile":"ACZVAL_321","valNumber":25.00},{"tipoVariabile":"PERCSANZDET_320","valNumber":0.0000},{"tipoVariabile":"ACZVAL_310","valNumber":100.00},{"tipoVariabile":"PERCRIT","valNumber":0.0000},{"tipoVariabile":"INFOAGRATT","valBoolean":true}]}',
    '{"variabiliCalcolo":[{"tipoVariabile":"ACZIMPRID_313","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_320","valNumber":0.00},{"tipoVariabile":"ACZIMPDEBCTOT","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_321","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_315","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_311","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_318","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_321","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_313","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_320","valNumber":2943.75},{"tipoVariabile":"ACZIMPCALC_318","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDTOT","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_318","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_311","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_322","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_321","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRITTOT","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_322","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_313","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_322","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_316","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_310","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_316","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_310","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_315","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_315","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_316","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_318","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_315","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_310","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_316","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_315","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_311","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_320","valNumber":2943.75},{"tipoVariabile":"ACZIMPRIDSANZ_313","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZTOT","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_320","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_313","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDOTOT","valNumber":2943.75},{"tipoVariabile":"ACZIMPCALC_310","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_310","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_322","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_310","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_313","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_316","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_322","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_321","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_321","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_318","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_320","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_311","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_321","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_318","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCTOT","valNumber":2943.75},{"tipoVariabile":"ACZIMPACC_322","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_313","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_315","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_316","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_315","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_320","valNumber":2943.75},{"tipoVariabile":"ACZIMPCALC_320","valNumber":2943.75},{"tipoVariabile":"ACZIMPCALCLORDO_311","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_322","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_311","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_318","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_311","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_316","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_310","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_321","valNumber":0.00}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"ACZIMPRID_313","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_320","valNumber":0.00},{"tipoVariabile":"ACZIMPDEBCTOT","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_321","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_315","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_311","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_318","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_321","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_313","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_320","valNumber":2943.75},{"tipoVariabile":"ACZIMPCALC_318","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDTOT","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_318","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_311","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_322","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_321","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRITTOT","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_322","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_313","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_322","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_316","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_310","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_316","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_310","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_315","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_315","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_316","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_318","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_315","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_310","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_316","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_315","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_311","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_320","valNumber":2943.75},{"tipoVariabile":"ACZIMPRIDSANZ_313","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZTOT","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_320","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_313","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDOTOT","valNumber":2943.75},{"tipoVariabile":"ACZIMPCALC_310","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_310","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_322","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_310","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_313","valNumber":0.00},{"tipoVariabile":"ACZIMPACC_316","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_322","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_321","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_321","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_318","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_320","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_311","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_321","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDSANZ_318","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCTOT","valNumber":2943.75},{"tipoVariabile":"ACZIMPACC_322","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_313","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_315","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_316","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_315","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_320","valNumber":2943.75},{"tipoVariabile":"ACZIMPCALC_320","valNumber":2943.75},{"tipoVariabile":"ACZIMPCALCLORDO_311","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_322","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_311","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_318","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_311","valNumber":0.00},{"tipoVariabile":"ACZIMPRIDRIT_316","valNumber":0.00},{"tipoVariabile":"ACZIMPRICNET_310","valNumber":0.00},{"tipoVariabile":"ACZIMPRID_321","valNumber":0.00}]}',
    '{"variabiliCalcolo":[],"esitiControlli":[{"tipoControllo":"BRIDUSDC009_infoAgricoltoreAttivo","esito":true,"livelloControllo":"SUCCESS"},{"tipoControllo":"BRIDUSDC010_agricoltoreAttivo","esito":true,"livelloControllo":"SUCCESS"},{"tipoControllo":"BRIDUSDC022_idDomandaCampione","esito":false,"livelloControllo":"NULL"},{"tipoControllo":"BRIDUACZ107_UbaAmmessi","esito":true,"livelloControllo":"INFO"},{"tipoControllo":"BRIDUACZ123_VerificaControlliInLoco"},{"tipoControllo":"BRIDUACZ126_VerificaSanzioni","esito":true,"valString":"NESSUNA_SANZIONE","livelloControllo":"INFO"},{"tipoControllo":"BRIDUACZ127_Riduzioni","esito":false,"livelloControllo":"WARNING"}],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}');
    
    
    
    
    
    
Insert into A4GT_PASSO_TRANSIZIONE
   (ID, VERSIONE, ID_TRANSIZ_SOSTEGNO, CODICE_PASSO, ESITO, 
    CODICE_ESITO, DATI_INPUT, DATI_OUTPUT, DATI_SINTESI_LAVORAZIONE)
 Values
   (NXTNBR.nextval, 0, 
   (select id from A4GT_TRANSIZIONE_ISTRUTTORIA where id_istruttoria = (select i.id from a4gt_istruttoria i join a4gt_domanda d on d.id = i.id_domanda WHERE D.CUAA_INTESTATARIO = 'MFFFBA75B28L174A' and i.sostegno = 'ZOOTECNIA') and id_stato_finale = (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_INTERSOSTEGNO_OK')),
   'CONTROLLO_IMPORTO_MINIMO', 'OK', 
    'DUT_042', '{"variabiliCalcolo":[{"tipoVariabile":"IMPMINIMOLIQ","valNumber":300.00},{"tipoVariabile":"HASRICACZ","valBoolean":true},{"tipoVariabile":"STATOACZ","valString":"LIQUIDABILE"},{"tipoVariabile":"ACZIMPCALCTOT","valNumber":17050.84},{"tipoVariabile":"HASRICACS","valBoolean":false},{"tipoVariabile":"ACSIMPCALCTOT","valNumber":0.00},{"tipoVariabile":"HASRICBPS","valBoolean":true},{"tipoVariabile":"STATOBPS","valString":"CONTROLLI_CALCOLO_OK"},{"tipoVariabile":"DISIMPCALC","valNumber":34666.37}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"IMPMINIMOLIQ","valNumber":300.00},{"tipoVariabile":"HASRICACZ","valBoolean":true},{"tipoVariabile":"STATOACZ","valString":"LIQUIDABILE"},{"tipoVariabile":"ACZIMPCALCTOT","valNumber":17050.84},{"tipoVariabile":"HASRICACS","valBoolean":false},{"tipoVariabile":"ACSIMPCALCTOT","valNumber":0.00},{"tipoVariabile":"HASRICBPS","valBoolean":true},{"tipoVariabile":"STATOBPS","valString":"CONTROLLI_CALCOLO_OK"},{"tipoVariabile":"DISIMPCALC","valNumber":34666.37}]}', '{"variabiliCalcolo":[],"variabiliCalcoloDaStampare":[]}', '{"variabiliCalcolo":[],"esitiControlli":[{"tipoControllo":"BRIDUSDS040_importoMinimo","esito":true,"valString":"RAGGIUNTO","livelloControllo":"INFO"}],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}');

Insert into A4GT_PASSO_TRANSIZIONE
   (ID, VERSIONE, ID_TRANSIZ_SOSTEGNO, CODICE_PASSO, ESITO, 
    CODICE_ESITO, DATI_SINTESI_LAVORAZIONE)
 Values
   (NXTNBR.nextval, 0, 
   (select id from A4GT_TRANSIZIONE_ISTRUTTORIA where id_istruttoria = (select i.id from a4gt_istruttoria i join a4gt_domanda d on d.id = i.id_domanda WHERE D.CUAA_INTESTATARIO = 'MFFFBA75B28L174A' and i.sostegno = 'ZOOTECNIA') and id_stato_finale = (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_INTERSOSTEGNO_OK')),
   'CONTROLLO_INTERSOSTEGNO', 'OK', 
    'DUF_031', '{"variabiliCalcolo":[],"esitiControlli":[],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}');

Insert into A4GT_PASSO_TRANSIZIONE
   (ID, VERSIONE, ID_TRANSIZ_SOSTEGNO, CODICE_PASSO, ESITO, 
    CODICE_ESITO, DATI_SINTESI_LAVORAZIONE)
 Values
   (NXTNBR.nextval, 0, 
   (select id from A4GT_TRANSIZIONE_ISTRUTTORIA where id_istruttoria = (select i.id from a4gt_istruttoria i join a4gt_domanda d on d.id = i.id_domanda WHERE D.CUAA_INTESTATARIO = 'MFFFBA75B28L174A' and i.sostegno = 'ZOOTECNIA') and id_stato_finale = (select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE')),
   'LIQUIDABILITA', 'OK', 
    'DUF_031', '{"variabiliCalcolo":[],"esitiControlli":[{"tipoControllo":"BRIDUSDL038_titolare","esito":false,"livelloControllo":"INFO"},{"tipoControllo":"BRIDUNVL129_erede","esito":false,"livelloControllo":"NULL"},{"tipoControllo":"BRIDUSDL039_agea","esito":false,"livelloControllo":"INFO"},{"tipoControllo":"BRIDUSDL037_iban","esito":true,"livelloControllo":"INFO"}],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}');

Insert into A4GT_PASSO_TRANSIZIONE
   (ID, VERSIONE, ID_TRANSIZ_SOSTEGNO, CODICE_PASSO, ESITO, 
    CODICE_ESITO, DATI_INPUT, DATI_OUTPUT, DATI_SINTESI_LAVORAZIONE)
 Values
   (NXTNBR.nextval, 0, 
   (select id from A4GT_TRANSIZIONE_ISTRUTTORIA where id_istruttoria = (select i.id from a4gt_istruttoria i join a4gt_domanda d on d.id = i.id_domanda WHERE D.CUAA_INTESTATARIO = 'MFFFBA75B28L174A' and i.sostegno = 'ZOOTECNIA') and id_stato_finale = (select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE')),
   'DISCIPLINA_FINANZIARIA', 'OK', 
    'no esito',
    '{"variabiliCalcolo":[{"tipoVariabile":"DFFRAPPACZ_321","valNumber":5.00},{"tipoVariabile":"DFIMPLIPAGACZ_311","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_322","valNumber":0.00},{"tipoVariabile":"DFIMPLIPAGACZ_320","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_310","valNumber":10.00},{"tipoVariabile":"DFIMPLIPAGACZ_321","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_313","valNumber":3000.00},{"tipoVariabile":"ACZIMPCALC_320","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO","valNumber":3000.00},{"tipoVariabile":"ACZIMPCALC_313","valNumber":3000.00},{"tipoVariabile":"DFIMPLIPAGACZ_315","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_310","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_318","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_320","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_316","valNumber":15.00},{"tipoVariabile":"ACZIMPCALC_321","valNumber":0.00},{"tipoVariabile":"DFIMPLIPAGACZ_322","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_322","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_320","valNumber":20.00},{"tipoVariabile":"DFIMPLIPAGACZ_316","valNumber":0.00},{"tipoVariabile":"DFIMPLIPAGACZ_310","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_315","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_322","valNumber":25.00},{"tipoVariabile":"ACZIMPCALC_318","valNumber":0.00},{"tipoVariabile":"DFFRAPPACS","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_310","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_315","valNumber":0.00},{"tipoVariabile":"DFFRAPPDIS","valNumber":2000.00},{"tipoVariabile":"ACZIMPCALC_311","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_316","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_318","valNumber":30.00},{"tipoVariabile":"ACZIMPCALCLORDO_311","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_311","valNumber":35.00},{"tipoVariabile":"DFFRAPPACZ_315","valNumber":40.00},{"tipoVariabile":"DFPERC","valNumber":0.02906192},{"tipoVariabile":"DFIMPLIPAGACZ_318","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_313","valNumber":45.00},{"tipoVariabile":"DFIMPLIPAGACZ_313","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC","valNumber":3000.00},{"tipoVariabile":"DFFR","valNumber":2000.00},{"tipoVariabile":"ACZIMPCALCLORDO_321","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_316","valNumber":0.00}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"DFFRAPPACZ_321","valNumber":50.00},{"tipoVariabile":"DFIMPLIPAGACZ_311","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_322","valNumber":0.00},{"tipoVariabile":"DFIMPLIPAGACZ_320","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_310","valNumber":55.00},{"tipoVariabile":"DFIMPLIPAGACZ_321","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_313","valNumber":3000.00},{"tipoVariabile":"ACZIMPCALC_320","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO","valNumber":3000.00},{"tipoVariabile":"ACZIMPCALC_313","valNumber":3000.00},{"tipoVariabile":"DFIMPLIPAGACZ_315","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_310","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_318","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_320","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_316","valNumber":60.00},{"tipoVariabile":"ACZIMPCALC_321","valNumber":0.00},{"tipoVariabile":"DFIMPLIPAGACZ_322","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_322","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_320","valNumber":0.00},{"tipoVariabile":"DFIMPLIPAGACZ_316","valNumber":0.00},{"tipoVariabile":"DFIMPLIPAGACZ_310","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_315","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_322","valNumber":70.00},{"tipoVariabile":"ACZIMPCALC_318","valNumber":0.00},{"tipoVariabile":"DFFRAPPACS","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_310","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_315","valNumber":0.00},{"tipoVariabile":"DFFRAPPDIS","valNumber":2000.00},{"tipoVariabile":"ACZIMPCALC_311","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_316","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_318","valNumber":75.00},{"tipoVariabile":"ACZIMPCALCLORDO_311","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_311","valNumber":80.00},{"tipoVariabile":"DFFRAPPACZ_315","valNumber":85.00},{"tipoVariabile":"DFPERC","valNumber":0.02906192},{"tipoVariabile":"DFIMPLIPAGACZ_318","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_313","valNumber":90.00},{"tipoVariabile":"DFIMPLIPAGACZ_313","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC","valNumber":3000.00},{"tipoVariabile":"DFFR","valNumber":2000.00},{"tipoVariabile":"ACZIMPCALCLORDO_321","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_316","valNumber":0.00}]}',
    '{"variabiliCalcolo":[{"tipoVariabile":"DFFRPAGLORACZ_322","valNumber":0.00},{"tipoVariabile":"DFIMPLIQACZ","valNumber":2912.81},{"tipoVariabile":"DFFRPAGLORACZ_315","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_310","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_320","valNumber":10.00},{"tipoVariabile":"DFFRPAGLORACZ_316","valNumber":0.00},{"tipoVariabile":"DFIMPRIDACZ","valNumber":87.19},{"tipoVariabile":"DFIMPLIQACZLORDO","valNumber":2912.81},{"tipoVariabile":"DFFRPAGLORACZ_313","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_322","valNumber":20.00},{"tipoVariabile":"DFIMPDFDISACZ_313","valNumber":2912.81},{"tipoVariabile":"DFIMPDFDISACZ_322","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_311","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_320","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_318","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_311","valNumber":30.00},{"tipoVariabile":"DFFRPAGLORACZ_321","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_316","valNumber":40.00},{"tipoVariabile":"DFIMPDFDISACZ_316","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_310","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_310","valNumber":50.00},{"tipoVariabile":"DFFRPAGLORACZ_311","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_321","valNumber":60.00},{"tipoVariabile":"DFIMPDFDISACZ_321","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_313","valNumber":70.00},{"tipoVariabile":"DFIMPDFDISACZ_318","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_315","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_318","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_315","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_320","valNumber":0.00}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"DFFRPAGLORACZ_322","valNumber":0.00},{"tipoVariabile":"DFIMPLIQACZ","valNumber":2912.81},{"tipoVariabile":"DFFRPAGLORACZ_315","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_310","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_320","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_316","valNumber":0.00},{"tipoVariabile":"DFIMPRIDACZ","valNumber":87.19},{"tipoVariabile":"DFIMPLIQACZLORDO","valNumber":2912.81},{"tipoVariabile":"DFFRPAGLORACZ_313","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_322","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_313","valNumber":2912.81},{"tipoVariabile":"DFIMPDFDISACZ_322","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_311","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_320","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_318","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_311","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_321","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_316","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_316","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_310","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_310","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_311","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_321","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_321","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_313","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_318","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_315","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_318","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_315","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_320","valNumber":0.00}]}',
    ''
   );
   
   
   
--inserire l'istruttoria a pagamento autorizzato   

/*Insert into A4GT_ELENCO_LIQUIDAZIONE
   (ID, VERSIONE, COD_ELENCO,
    DT_CREAZIONE)
 Values
   (8517929, 2, 
   '009-801-20190806-8517929',
    TO_DATE('08/06/2019 17:52:25', 'MM/DD/YYYY HH24:MI:SS'));**/

----------------------------------------------------------------------------------------------------------------------
--Inserire una istruttoria in pagamento autorizzato con lo stesso tipo di sostegno ma in anticipo
--inserita in coda ma con un id inferior ein modo da non essere presa in considerazione per il calcolo del premio iniziale

Insert into A4GT_ISTRUTTORIA(ID,VERSIONE,ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE, TIPOLOGIA)
values(9999,0,
(SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'MFFFBA75B28L174A' and numero_domanda = 1191062),
'ZOOTECNIA',
(select id from a4gd_stato_lav_sostegno where identificativo = 'PAGAMENTO_AUTORIZZATO'), 
'ANTICIPO');

--inserire solo la transizione legata al controllo intersostegno ok
Insert into A4GT_TRANSIZIONE_ISTRUTTORIA
   (ID, VERSIONE, ID_ISTRUTTORIA, ID_STATO_INIZIALE, 
    ID_STATO_FINALE, DATA_ESECUZIONE)
 Values
   (NXTNBR.nextval, 0, 
   9999,
   --NXTNBR.currval-1,
	--(select i.id from a4gt_istruttoria i join a4gt_domanda d on d.id = i.id_domanda WHERE D.CUAA_INTESTATARIO = 'MFFFBA75B28L174A' and i.sostegno = 'ZOOTECNIA'),
   (select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE'), 
    (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_INTERSOSTEGNO_OK'), TO_DATE('05/21/2019 18:50:01', 'MM/DD/YYYY HH24:MI:SS'));
    
    
    
Insert into A4GT_PASSO_TRANSIZIONE
   (ID, VERSIONE, ID_TRANSIZ_SOSTEGNO, CODICE_PASSO, ESITO, 
    CODICE_ESITO, DATI_INPUT, DATI_OUTPUT, DATI_SINTESI_LAVORAZIONE)
 Values
   (NXTNBR.nextval, 0, 
   (select id from A4GT_TRANSIZIONE_ISTRUTTORIA where id_istruttoria = (select i.id from a4gt_istruttoria i join a4gt_domanda d on d.id = i.id_domanda WHERE D.CUAA_INTESTATARIO = 'MFFFBA75B28L174A' and i.sostegno = 'ZOOTECNIA' and i.tipologia = 'ANTICIPO') and id_stato_finale = (select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_INTERSOSTEGNO_OK')),
   'DISCIPLINA_FINANZIARIA', 'OK', 
    'no esito',
    '{"variabiliCalcolo":[{"tipoVariabile":"DFFRAPPACZ_321","valNumber":5.00},{"tipoVariabile":"DFIMPLIPAGACZ_311","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_322","valNumber":0.00},{"tipoVariabile":"DFIMPLIPAGACZ_320","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_310","valNumber":10.00},{"tipoVariabile":"DFIMPLIPAGACZ_321","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_313","valNumber":3000.00},{"tipoVariabile":"ACZIMPCALC_320","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO","valNumber":3000.00},{"tipoVariabile":"ACZIMPCALC_313","valNumber":3000.00},{"tipoVariabile":"DFIMPLIPAGACZ_315","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_310","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_318","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_320","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_316","valNumber":15.00},{"tipoVariabile":"ACZIMPCALC_321","valNumber":0.00},{"tipoVariabile":"DFIMPLIPAGACZ_322","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_322","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_320","valNumber":20.00},{"tipoVariabile":"DFIMPLIPAGACZ_316","valNumber":0.00},{"tipoVariabile":"DFIMPLIPAGACZ_310","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_315","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_322","valNumber":25.00},{"tipoVariabile":"ACZIMPCALC_318","valNumber":0.00},{"tipoVariabile":"DFFRAPPACS","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_310","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_315","valNumber":0.00},{"tipoVariabile":"DFFRAPPDIS","valNumber":2000.00},{"tipoVariabile":"ACZIMPCALC_311","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_316","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_318","valNumber":30.00},{"tipoVariabile":"ACZIMPCALCLORDO_311","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_311","valNumber":35.00},{"tipoVariabile":"DFFRAPPACZ_315","valNumber":40.00},{"tipoVariabile":"DFPERC","valNumber":0.02906192},{"tipoVariabile":"DFIMPLIPAGACZ_318","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_313","valNumber":45.00},{"tipoVariabile":"DFIMPLIPAGACZ_313","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC","valNumber":3000.00},{"tipoVariabile":"DFFR","valNumber":2000.00},{"tipoVariabile":"ACZIMPCALCLORDO_321","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_316","valNumber":0.00}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"DFFRAPPACZ_321","valNumber":50.00},{"tipoVariabile":"DFIMPLIPAGACZ_311","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_322","valNumber":0.00},{"tipoVariabile":"DFIMPLIPAGACZ_320","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_310","valNumber":55.00},{"tipoVariabile":"DFIMPLIPAGACZ_321","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_313","valNumber":3000.00},{"tipoVariabile":"ACZIMPCALC_320","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO","valNumber":3000.00},{"tipoVariabile":"ACZIMPCALC_313","valNumber":3000.00},{"tipoVariabile":"DFIMPLIPAGACZ_315","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_310","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_318","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_320","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_316","valNumber":60.00},{"tipoVariabile":"ACZIMPCALC_321","valNumber":0.00},{"tipoVariabile":"DFIMPLIPAGACZ_322","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_322","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_320","valNumber":0.00},{"tipoVariabile":"DFIMPLIPAGACZ_316","valNumber":0.00},{"tipoVariabile":"DFIMPLIPAGACZ_310","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_315","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_322","valNumber":70.00},{"tipoVariabile":"ACZIMPCALC_318","valNumber":0.00},{"tipoVariabile":"DFFRAPPACS","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_310","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_315","valNumber":0.00},{"tipoVariabile":"DFFRAPPDIS","valNumber":2000.00},{"tipoVariabile":"ACZIMPCALC_311","valNumber":0.00},{"tipoVariabile":"ACZIMPCALCLORDO_316","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_318","valNumber":75.00},{"tipoVariabile":"ACZIMPCALCLORDO_311","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_311","valNumber":80.00},{"tipoVariabile":"DFFRAPPACZ_315","valNumber":85.00},{"tipoVariabile":"DFPERC","valNumber":0.02906192},{"tipoVariabile":"DFIMPLIPAGACZ_318","valNumber":0.00},{"tipoVariabile":"DFFRAPPACZ_313","valNumber":90.00},{"tipoVariabile":"DFIMPLIPAGACZ_313","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC","valNumber":3000.00},{"tipoVariabile":"DFFR","valNumber":2000.00},{"tipoVariabile":"ACZIMPCALCLORDO_321","valNumber":0.00},{"tipoVariabile":"ACZIMPCALC_316","valNumber":0.00}]}',
    '{"variabiliCalcolo":[{"tipoVariabile":"DFFRPAGLORACZ_322","valNumber":0.00},{"tipoVariabile":"DFIMPLIQACZ","valNumber":2912.81},{"tipoVariabile":"DFFRPAGLORACZ_315","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_310","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_320","valNumber":10.00},{"tipoVariabile":"DFFRPAGLORACZ_316","valNumber":0.00},{"tipoVariabile":"DFIMPRIDACZ","valNumber":87.19},{"tipoVariabile":"DFIMPLIQACZLORDO","valNumber":2912.81},{"tipoVariabile":"DFFRPAGLORACZ_313","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_322","valNumber":20.00},{"tipoVariabile":"DFIMPDFDISACZ_313","valNumber":2912.81},{"tipoVariabile":"DFIMPDFDISACZ_322","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_311","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_320","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_318","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_311","valNumber":30.00},{"tipoVariabile":"DFFRPAGLORACZ_321","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_316","valNumber":40.00},{"tipoVariabile":"DFIMPDFDISACZ_316","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_310","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_310","valNumber":50.00},{"tipoVariabile":"DFFRPAGLORACZ_311","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_321","valNumber":60.00},{"tipoVariabile":"DFIMPDFDISACZ_321","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_313","valNumber":70.00},{"tipoVariabile":"DFIMPDFDISACZ_318","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_315","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_318","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_315","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_320","valNumber":0.00}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"DFFRPAGLORACZ_322","valNumber":0.00},{"tipoVariabile":"DFIMPLIQACZ","valNumber":2912.81},{"tipoVariabile":"DFFRPAGLORACZ_315","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_310","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_320","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_316","valNumber":0.00},{"tipoVariabile":"DFIMPRIDACZ","valNumber":87.19},{"tipoVariabile":"DFIMPLIQACZLORDO","valNumber":2912.81},{"tipoVariabile":"DFFRPAGLORACZ_313","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_322","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_313","valNumber":2912.81},{"tipoVariabile":"DFIMPDFDISACZ_322","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_311","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_320","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_318","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_311","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_321","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_316","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_316","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_310","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_310","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_311","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_321","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_321","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_313","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_318","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_315","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_318","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_315","valNumber":0.00},{"tipoVariabile":"DFFRPAGLORACZ_320","valNumber":0.00}]}',
    ''
   );
    
    
    
    
   




 COMMIT;


