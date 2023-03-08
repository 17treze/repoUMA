/** Domanda originale con anticipo erogato ZTTTZN91P04L378H - ZOTTELE TIZIANO - 8659221 -   203502 **/

INSERT INTO A4GT_DOMANDA (
	ID, VERSIONE, NUMERO_DOMANDA, COD_MODULO_DOMANDA, DESC_MODULO_DOMANDA,
	ANNO_CAMPAGNA,
	STATO,
	CUAA_INTESTATARIO,
	DT_PROTOCOLLAZIONE,
	NUM_DOMANDA_RETTIFICATA, COD_ENTE_COMPILATORE, DESC_ENTE_COMPILATORE, RAGIONE_SOCIALE,
	DT_PRESENTAZIONE,
	DT_PRESENTAZ_ORIGINARIA, DT_PROTOCOLLAZ_ORIGINARIA
) VALUES (
	NXTNBR.NEXTVAL, 4, 1203502, 'BPS_2019', 'PAGAMENTI DIRETTI',
	2019,
	'IN_ISTRUTTORIA',
	'ZTTTZN91P04L3781',
	to_date('26/05/2019 12:00:00','dd/mm/yyyy hh24:mi:ss'),
	null, '19', 'CAA CIA - CLES - 002', 'ZOTTELE TIZIANO',
	to_date('26/05/2019 12:00:00','dd/mm/yyyy hh24:mi:ss'),
	null, null
);

INSERT INTO A4GT_ISTRUTTORIA(
	ID, VERSIONE,
	ID_DOMANDA,
	SOSTEGNO,
	ID_STATO_LAVORAZIONE,
	TIPOLOGIA
) VALUES (
	8670227, 0,
	(SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'ZTTTZN91P04L3781'),
	'DISACCOPPIATO',
	(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'PAGAMENTO_AUTORIZZATO'),
	'ANTICIPO'
);

INSERT INTO A4GT_ISTRUTTORIA(
	ID, VERSIONE,
	ID_DOMANDA,
	SOSTEGNO,
	ID_STATO_LAVORAZIONE,
	TIPOLOGIA
) VALUES (
	8670228, 0,
	(SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'ZTTTZN91P04L3781'),
	'DISACCOPPIATO',
	(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'RICHIESTO'),
	'SALDO'
);

INSERT INTO A4GT_TRANSIZIONE_ISTRUTTORIA (
	ID, VERSIONE, ID_ISTRUTTORIA,
	ID_STATO_INIZIALE,
    ID_STATO_FINALE,
    DATA_ESECUZIONE
) VALUES (
	10, 1, 8670227,
	(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'RICHIESTO'), 
    (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'CONTROLLI_CALCOLO_OK'),
    TO_DATE('05/21/2019 18:40:30', 'MM/DD/YYYY HH24:MI:SS')
);

INSERT INTO A4GT_TRANSIZIONE_ISTRUTTORIA (
	ID, VERSIONE, ID_ISTRUTTORIA,
	ID_STATO_INIZIALE,
    ID_STATO_FINALE,
    DATA_ESECUZIONE
) VALUES (
	11, 1, 8670227,
	(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'CONTROLLI_CALCOLO_OK'), 
    (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE'),
    TO_DATE('05/21/2019 18:40:30', 'MM/DD/YYYY HH24:MI:SS')
);

INSERT INTO A4GT_TRANSIZIONE_ISTRUTTORIA (
	ID, VERSIONE, ID_ISTRUTTORIA,
	ID_STATO_INIZIALE,
    ID_STATO_FINALE,
    DATA_ESECUZIONE
) VALUES (
	12, 1, 8670227,
	(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE'), 
    (SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'CONTROLLO_INTERSOSTEGNO_OK'),
    TO_DATE('05/21/2019 18:40:30', 'MM/DD/YYYY HH24:MI:SS')
);

INSERT INTO A4GT_PASSO_TRANSIZIONE (
	ID, VERSIONE, ID_TRANSIZ_SOSTEGNO,
	CODICE_PASSO, ESITO, CODICE_ESITO,
    DATI_INPUT,
    DATI_OUTPUT,
    DATI_SINTESI_LAVORAZIONE
) VALUES (
 	20, 0, 10,
	'AMMISSIBILITA', 'OK', 'DUT_001',
	'{"variabiliCalcolo":[{"tipoVariabile":"GREPERC","valNumber":0.5192},{"tipoVariabile":"PERCRIDLINTIT","valNumber":0.0000},{"tipoVariabile":"BPSSUPIMP200","valNumber":0.0000},{"tipoVariabile":"BPSSUPIMP","valNumber":5.5447},{"tipoVariabile":"GRERIC","valBoolean":true},{"tipoVariabile":"GIORIC","valBoolean":false},{"tipoVariabile":"TITONUM","valNumber":6},{"tipoVariabile":"TITSUP","valNumber":4.9500},{"tipoVariabile":"TITVAL","valNumber":187.62},{"tipoVariabile":"AGRATT","valBoolean":true}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"GREPERC","valNumber":0.5192},{"tipoVariabile":"PERCRIDLINTIT","valNumber":0.0000},{"tipoVariabile":"BPSSUPIMP200","valNumber":0.0000},{"tipoVariabile":"BPSSUPIMP","valNumber":5.5447},{"tipoVariabile":"GRERIC","valBoolean":true},{"tipoVariabile":"GIORIC","valBoolean":false},{"tipoVariabile":"TITONUM","valNumber":6},{"tipoVariabile":"TITSUP","valNumber":4.9500},{"tipoVariabile":"TITVAL","valNumber":187.62},{"tipoVariabile":"AGRATT","valBoolean":true}]}',
	'{"variabiliCalcolo":[{"tipoVariabile":"BPSSUPRIC","valNumber":4.9500},{"tipoVariabile":"BPSIMPRIC","valNumber":928.72},{"tipoVariabile":"GRESUPRIC","valNumber":4.9500},{"tipoVariabile":"GREIMPRIC","valNumber":482.19},{"tipoVariabile":"TITVALRID","valNumber":187.62}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"BPSSUPRIC","valNumber":4.9500},{"tipoVariabile":"BPSIMPRIC","valNumber":928.72},{"tipoVariabile":"GRESUPRIC","valNumber":4.9500},{"tipoVariabile":"GREIMPRIC","valNumber":482.19},{"tipoVariabile":"TITVALRID","valNumber":187.62}]}',
	'{"variabiliCalcolo":[],"esitiControlli":[{"tipoControllo":"BRIDUSDC011_impegnoTitoli","esito":true,"livelloControllo":"SUCCESS"},{"tipoControllo":"BRIDUSDC010_agricoltoreAttivo","esito":true,"livelloControllo":"SUCCESS"},{"tipoControllo":"BRIDUSDC012_superficieMinima","esito":true,"livelloControllo":"SUCCESS"},{"tipoControllo":"BRIDUSDC009_infoAgricoltoreAttivo","esito":true,"livelloControllo":"SUCCESS"}],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}'
);

INSERT INTO A4GT_PASSO_TRANSIZIONE (
	ID, VERSIONE, ID_TRANSIZ_SOSTEGNO,
	CODICE_PASSO, ESITO, CODICE_ESITO,
    DATI_INPUT,
    DATI_OUTPUT,
    DATI_SINTESI_LAVORAZIONE
) VALUES (
 	21, 0, 10,
	'CONTROLLI_FINALI', 'OK', 'DUF_014',
	'{"variabiliCalcolo":[{"tipoVariabile":"BPSIMPAMM","valNumber":928.72},{"tipoVariabile":"GREIMPAMM","valNumber":482.19},{"tipoVariabile":"IMPSALARI","valNumber":0.00},{"tipoVariabile":"PERCRIT","valNumber":0.0000},{"tipoVariabile":"PERCRIDLIN1","valNumber":0.0000},{"tipoVariabile":"PERCRIDLIN2","valNumber":0.0000},{"tipoVariabile":"PERCRIDLIN3","valNumber":0.0000},{"tipoVariabile":"BPSIMPEROGATO","valNumber":0.00},{"tipoVariabile":"GREIMPEROGATO","valNumber":0.00}, {"tipoVariabile":"GIOIMPEROGATO","valNumber":0.00},{"tipoVariabile":"PERCPAGAMENTO","valNumber":0.7000}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"BPSIMPAMM","valNumber":928.72},{"tipoVariabile":"GREIMPAMM","valNumber":482.19},{"tipoVariabile":"IMPSALARI","valNumber":0.00},{"tipoVariabile":"PERCRIT","valNumber":0.0000},{"tipoVariabile":"PERCRIDLIN1","valNumber":0.0000},{"tipoVariabile":"PERCRIDLIN2","valNumber":0.0000},{"tipoVariabile":"PERCRIDLIN3","valNumber":0.0000},{"tipoVariabile":"BPSIMPEROGATO","valNumber":0.00},{"tipoVariabile":"GREIMPEROGATO","valNumber":0.00}, {"tipoVariabile":"GIOIMPEROGATO","valNumber":0.00},{"tipoVariabile":"PERCPAGAMENTO","valNumber":0.7000}]}',
	'{"variabiliCalcolo":[{"tipoVariabile":"BPSIMPRIDRIT","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDLIN1","valNumber":0.00},{"tipoVariabile":"BPSIMPBCCAP","valNumber":928.72},{"tipoVariabile":"BPSIMPRIDCAP50","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDCAP100","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDLIN3","valNumber":0.00},{"tipoVariabile":"BPSIMPCALCFINLORDO", "valNumber":650.10}, {"tipoVariabile":"BPSIMPCALCFIN","valNumber":650.10},{"tipoVariabile":"GREIMPRIDRIT","valNumber":0.00},{"tipoVariabile":"GREIMPRIDLIN3","valNumber":0.00},{"tipoVariabile":"GREIMPCALCFINLORDO", "valNumber":337.53}, {"tipoVariabile":"GREIMPCALCFIN","valNumber":337.53},{"tipoVariabile":"IMPCALC","valNumber":1410.91},{"tipoVariabile":"IMPCALCFIN","valNumber":987.63},{"tipoVariabile":"IMPRIDRIT","valNumber":0.00}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"BPSIMPRIDRIT","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDLIN1","valNumber":0.00},{"tipoVariabile":"BPSIMPBCCAP","valNumber":928.72},{"tipoVariabile":"BPSIMPRIDCAP50","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDCAP100","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDLIN3","valNumber":0.00},{"tipoVariabile":"BPSIMPCALCFINLORDO", "valNumber":650.10}, {"tipoVariabile":"BPSIMPCALCFIN","valNumber":650.10},{"tipoVariabile":"GREIMPRIDRIT","valNumber":0.00},{"tipoVariabile":"GREIMPRIDLIN3","valNumber":0.00},{"tipoVariabile":"GREIMPCALCFINLORDO", "valNumber":337.53}, {"tipoVariabile":"GREIMPCALCFIN","valNumber":337.53},{"tipoVariabile":"IMPCALC","valNumber":1410.91},{"tipoVariabile":"IMPCALCFIN","valNumber":987.63}]}',
	'{"variabiliCalcolo":[],"esitiControlli":[{"tipoControllo":"BRIDUSDC043_riduzioneCapping","esito":false,"livelloControllo":"NULL"},{"tipoControllo":"BRIDUSDC036_verificaRitardo","esito":false,"livelloControllo":"NULL"}],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}'
);

commit;