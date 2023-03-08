INSERT INTO A4GT_DOMANDA (
	ID,VERSIONE, NUMERO_DOMANDA, COD_MODULO_DOMANDA, 
	DESC_MODULO_DOMANDA, ANNO_CAMPAGNA, STATO, CUAA_INTESTATARIO, 
	DT_PRESENTAZIONE, NUM_DOMANDA_RETTIFICATA, COD_ENTE_COMPILATORE, 
	DESC_ENTE_COMPILATORE, RAGIONE_SOCIALE, DT_PROTOCOLLAZIONE, 
	DT_PRESENTAZ_ORIGINARIA, DT_PROTOCOLLAZ_ORIGINARIA
)
values (999111, 0, 180999, 'BPS_2018',
		'PAGAMENTI DIRETTI',
		2018,
		'IN_ISTRUTTORIA',
		'BNDCLD69S12C999Z',
		to_date('13/04/2018 02:00:00','dd/mm/yyyy hh24:mi:ss'),
		null,
		'4',
		'CAA COLDIRETTI DEL TRENTINO - 003',
		'BONADIMAN CLAUDIO',
		to_date('13/04/2018 02:00:00','dd/mm/yyyy hh24:mi:ss','DD-MON-RR'),
		null,
		null
);

INSERT INTO A4GT_ISTRUTTORIA(
	ID,VERSIONE,ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE, TIPOLOGIA
)
values (999111, 0, 999111,
		'DISACCOPPIATO',
		(select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK'),
		'SALDO'
);

INSERT INTO A4GT_TRANSIZIONE_ISTRUTTORIA (
	ID, VERSIONE, ID_ISTRUTTORIA, ID_STATO_INIZIALE, ID_STATO_FINALE, DATA_ESECUZIONE
)
values (999111, 0, 999111,
		(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'RICHIESTO'),
		(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'CONTROLLI_CALCOLO_OK'),
		to_date('07-FEB-19','DD-MON-RR')
);

INSERT INTO A4GT_TRANSIZIONE_ISTRUTTORIA (
	ID, VERSIONE, ID_ISTRUTTORIA, ID_STATO_INIZIALE, ID_STATO_FINALE, DATA_ESECUZIONE
)
values (999112, 0, 999111,
		(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE'),
		(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE'),
		to_date('07-FEB-19','DD-MON-RR')
);

INSERT INTO A4GT_PASSO_TRANSIZIONE (
	ID, VERSIONE, ID_TRANSIZ_SOSTEGNO, CODICE_PASSO, ESITO, 
    CODICE_ESITO, DATI_INPUT, DATI_OUTPUT, DATI_SINTESI_LAVORAZIONE
)
values (999111, 0, 999111,
		'CONTROLLI_FINALI',
		'OK', 
		'DUF_037',
		'{"variabiliCalcolo":[{"tipoVariabile":"BPSIMPAMM","valNumber":1185.20},{"tipoVariabile":"GREIMPAMM","valNumber":615.36},{"tipoVariabile":"BPSIMPCALC","valNumber":1133.64},{"tipoVariabile":"GREIMPCALC","valNumber":615.36},{"tipoVariabile":"IMPSALARI","valNumber":0.00},{"tipoVariabile":"PERCRIT","valNumber":0.0000},{"tipoVariabile":"PERCRIDLIN1","valNumber":0.0185},{"tipoVariabile":"PERCRIDLIN2","valNumber":0.2690},{"tipoVariabile":"PERCRIDLIN3","valNumber":0.0000},{"tipoVariabile":"BPSIMPEROGATO","valNumber":0.00},{"tipoVariabile":"GREIMPEROGATO","valNumber":0.00}, {"tipoVariabile":"GIOIMPEROGATO","valNumber":0.00},{"tipoVariabile":"PERCPAGAMENTO","valNumber":1.0000}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"BPSIMPAMM","valNumber":1185.20},{"tipoVariabile":"GREIMPAMM","valNumber":615.36},{"tipoVariabile":"BPSIMPCALC","valNumber":1133.64},{"tipoVariabile":"GREIMPCALC","valNumber":615.36},{"tipoVariabile":"IMPSALARI","valNumber":0.00},{"tipoVariabile":"PERCRIT","valNumber":0.0000},{"tipoVariabile":"PERCRIDLIN1","valNumber":0.0185},{"tipoVariabile":"PERCRIDLIN2","valNumber":0.2690},{"tipoVariabile":"PERCRIDLIN3","valNumber":0.0000},{"tipoVariabile":"BPSIMPEROGATO","valNumber":0.00},{"tipoVariabile":"GREIMPEROGATO","valNumber":0.00}, {"tipoVariabile":"GIOIMPEROGATO","valNumber":0.00},{"tipoVariabile":"PERCPAGAMENTO","valNumber":1.0000}]}',
		'{"variabiliCalcolo":[{"tipoVariabile":"BPSIMPRIDRIT","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDLIN1","valNumber":20.97},{"tipoVariabile":"BPSIMPBCCAP","valNumber":1112.67},{"tipoVariabile":"BPSIMPRIDCAP50","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDCAP100","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDLIN3","valNumber":0.00},{"tipoVariabile":"BPSIMPCALCFINLORDO", "valNumber":1112.67}, {"tipoVariabile":"BPSIMPCALCFIN","valNumber":1112.67},{"tipoVariabile":"GREIMPRIDRIT","valNumber":0.00},{"tipoVariabile":"GREIMPRIDLIN3","valNumber":0.00},{"tipoVariabile":"GREIMPCALCFINLORDO", "valNumber":615.36}, {"tipoVariabile":"GREIMPCALCFIN","valNumber":615.36},{"tipoVariabile":"IMPCALC","valNumber":1749.00},{"tipoVariabile":"IMPCALCFIN","valNumber":1728.03},{"tipoVariabile":"IMPRIDRIT","valNumber":0.00}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"BPSIMPRIDRIT","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDLIN1","valNumber":20.97},{"tipoVariabile":"BPSIMPBCCAP","valNumber":1112.67},{"tipoVariabile":"BPSIMPRIDCAP50","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDCAP100","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDLIN3","valNumber":0.00},{"tipoVariabile":"BPSIMPCALCFINLORDO", "valNumber":1112.67}, {"tipoVariabile":"BPSIMPCALCFIN","valNumber":1112.67},{"tipoVariabile":"GREIMPRIDRIT","valNumber":0.00},{"tipoVariabile":"GREIMPRIDLIN3","valNumber":0.00},{"tipoVariabile":"GREIMPCALCFINLORDO", "valNumber":615.36}, {"tipoVariabile":"GREIMPCALCFIN","valNumber":615.36},{"tipoVariabile":"IMPCALC","valNumber":1749.00},{"tipoVariabile":"IMPCALCFIN","valNumber":1728.03}]}',
		'{"variabiliCalcolo":[],"esitiControlli":[{"tipoControllo":"BRIDUSDC036_verificaRitardo","esito":false,"livelloControllo":"NULL"},{"tipoControllo":"BRIDUSDC043_riduzioneCapping","esito":false,"livelloControllo":"NULL"}],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}'
);


INSERT INTO A4GT_PASSO_TRANSIZIONE (
	ID, VERSIONE, ID_TRANSIZ_SOSTEGNO, CODICE_PASSO, ESITO, 
    CODICE_ESITO, DATI_INPUT, DATI_OUTPUT, DATI_SINTESI_LAVORAZIONE
)
values (999112, 0, 999112,
		'LIQUIDABILITA',
		'KO', 
		'DUF_023',
		NULL,
		NULL,
		'{
			"variabiliCalcolo": [],
			"esitiControlli": [{
					"tipoControllo": "BRIDUSDL039_agea",
					"esito": false,
					"livelloControllo": "INFO"
				}, {
					"tipoControllo": "BRIDUSDL038_titolare",
					"esito": false,
					"livelloControllo": "INFO"
				}, {
					"tipoControllo": "BRIDUSDL037_iban",
					"esito": false,
					"livelloControllo": "ERROR"
				}, {
					"tipoControllo": "BRIDUSDL133_importoErogabilePositivo",
					"esito": true,
					"livelloControllo": "NULL"
				}
			],
			"variabiliParticellaColtura": null,
			"variabiliCalcoloDaStampare": []
		}'
);

COMMIT;