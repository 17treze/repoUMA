--configurazione campagna per la percentuale della disciplina
INSERT INTO A4GT_CONF_ISTRUTTORIA (ID, VERSIONE, ANNO_CAMPAGNA, DT_SCADENZA_DOMANDE_INIZIALI, PERC_PAGAMENTO, PERC_DISCIPLINA_FINANZIARIA)
VALUES (99999, 0 , 2020, '2020-07-10', 1, 0.02906192);


-- Popolo la tabella A4GT_DOMANDA
Insert into a4gt_domanda(ID,VERSIONE,NUMERO_DOMANDA,COD_MODULO_DOMANDA,DESC_MODULO_DOMANDA,CUAA_INTESTATARIO,DT_PROTOCOLLAZIONE,NUM_DOMANDA_RETTIFICATA,COD_ENTE_COMPILATORE,DESC_ENTE_COMPILATORE,RAGIONE_SOCIALE,DT_PRESENTAZIONE,DT_PRESENTAZ_ORIGINARIA,DT_PROTOCOLLAZ_ORIGINARIA,ANNULLO_RIDUZIONE,IBAN,ANNO_CAMPAGNA,STATO) 
values (NXTNBR.NEXTVAL,'2',225145,'BPS_2020','PAGAMENTI DIRETTI','02416580229',to_date('11/06/2020', 'dd/mm/yyyy'),null,'3','CAA COLDIRETTI DEL TRENTINO - 001','SOCIETA'' AGRICOLA SEMPLICE PIFFER ANDREA E CECCON SARA',to_date('11/06/2020', 'dd/mm/yyyy'),null,null,null,'IT41J0200834930000104219000','2020','IN_ISTRUTTORIA');


--saldo tabella A4GT_ISTRUTTORIA
Insert into A4GT_ISTRUTTORIA (
    ID,
    VERSIONE,
    ID_DOMANDA,
    ID_STATO_LAVORAZIONE,
    BLOCCATA_BOOL,
    ERRORE_CALCOLO,
    DATA_ULTIMO_CALCOLO,
    TIPOLOGIA,
    ID_ELENCO_LIQUIDAZIONE,
    SOSTEGNO) 
values (18764444,0,
         (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = '02416580229' and COD_MODULO_DOMANDA = 'BPS_2020'),
         (select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE'),
         '0','0',to_date('02/03/2021', 'dd/mm/yyyy'),
         'SALDO',
         null,
         'DISACCOPPIATO');
         
         
--per calcolare la disciplina fianziaria si deve partire dalal transizione 
--18802146	1	13117	13119	02-MAR-21	18764444	CONTROLLI_CALCOLO_OK	LIQUIDABILE 


Insert into A4GT_TRANSIZIONE_ISTRUTTORIA (ID,VERSIONE,ID_ISTRUTTORIA,ID_STATO_INIZIALE,ID_STATO_FINALE,DATA_ESECUZIONE)
values (NXTNBR.nextval,0,
18764444,
(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'RICHIESTO'),
(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'CONTROLLI_CALCOLO_OK'),
to_date('07/02/2019 15:26:20', 'dd/mm/yyyy HH24:MI:SS'));   
    

		
INSERT INTO A4GT_PASSO_TRANSIZIONE VALUES (
	nxtnbr.nextval, 
	0, (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts where ts.ID_ISTRUTTORIA = 18764444),
	'CONTROLLI_FINALI', 'OK', 'DUF_014',
	'{"variabiliCalcolo":[{"tipoVariabile":"BPSIMPAMM","valNumber":1603.90},{"tipoVariabile":"GREIMPAMM","valNumber":962.34},{"tipoVariabile":"GIOIMPAMM","valNumber":962.34},{"tipoVariabile":"IMPSALARI","valNumber":0.00},{"tipoVariabile":"PERCRIT","valNumber":0.0000},{"tipoVariabile":"PERCRIDLIN1","valNumber":0.0180},{"tipoVariabile":"PERCRIDLIN2","valNumber":0.1000},{"tipoVariabile":"PERCRIDLIN3","valNumber":0.0000},{"tipoVariabile":"PERCPAGAMENTO","valNumber":1.0000},{"tipoVariabile":"BPSIMPEROGATO","valNumber":1157.46},{"tipoVariabile":"GREIMPEROGATO","valNumber":598.98},{"tipoVariabile":"GIOIMPEROGATO","valNumber":0.00}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"BPSIMPAMM","valNumber":1603.90},{"tipoVariabile":"GREIMPAMM","valNumber":962.34},{"tipoVariabile":"GIOIMPAMM","valNumber":962.34},{"tipoVariabile":"IMPSALARI","valNumber":0.00},{"tipoVariabile":"PERCRIT","valNumber":0.0000},{"tipoVariabile":"PERCRIDLIN1","valNumber":0.0180},{"tipoVariabile":"PERCRIDLIN2","valNumber":0.1000},{"tipoVariabile":"PERCRIDLIN3","valNumber":0.0000},{"tipoVariabile":"PERCPAGAMENTO","valNumber":1.0000},{"tipoVariabile":"BPSIMPEROGATO","valNumber":1157.46},{"tipoVariabile":"GREIMPEROGATO","valNumber":598.98},{"tipoVariabile":"GIOIMPEROGATO","valNumber":0.00}]}',
	'{"variabiliCalcolo":[{"tipoVariabile":"BPSIMPRIDRIT","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDLIN1","valNumber":28.87},{"tipoVariabile":"BPSIMPBCCAP","valNumber":1575.03},{"tipoVariabile":"BPSIMPRIDCAP50","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDCAP100","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDLIN3","valNumber":0.00},{"tipoVariabile":"BPSIMPCALCFIN","valNumber":417.57},{"tipoVariabile":"BPSIMPCALCFINLORDO","valNumber":1575.03},{"tipoVariabile":"GREIMPRIDRIT","valNumber":0.00},{"tipoVariabile":"GREIMPRIDLIN3","valNumber":0.00},{"tipoVariabile":"GREIMPCALCFIN","valNumber":363.36},{"tipoVariabile":"GREIMPCALCFINLORDO","valNumber":962.34},{"tipoVariabile":"GIOIMPRIDRIT","valNumber":0.00},{"tipoVariabile":"GIOIMPRIDLIN2","valNumber":96.23},{"tipoVariabile":"GIOIMPRIDLIN3","valNumber":0.00},{"tipoVariabile":"GIOIMPCALCFIN","valNumber":866.11},{"tipoVariabile":"GIOIMPCALCFINLORDO","valNumber":866.11},{"tipoVariabile":"IMPCALC","valNumber":3528.58},{"tipoVariabile":"IMPCALCFIN","valNumber":1647.04},{"tipoVariabile":"IMPRIDRIT","valNumber":0.00},{"tipoVariabile":"IMPCALCFINLORDO","valNumber":3403.48}],"variabiliCalcoloDaStampare":[{"tipoVariabile":"BPSIMPRIDRIT","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDLIN1","valNumber":28.87},{"tipoVariabile":"BPSIMPBCCAP","valNumber":1575.03},{"tipoVariabile":"BPSIMPRIDCAP50","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDCAP100","valNumber":0.00},{"tipoVariabile":"BPSIMPRIDLIN3","valNumber":0.00},{"tipoVariabile":"BPSIMPCALCFIN","valNumber":417.57},{"tipoVariabile":"BPSIMPCALCFINLORDO","valNumber":1575.03},{"tipoVariabile":"GREIMPRIDRIT","valNumber":0.00},{"tipoVariabile":"GREIMPRIDLIN3","valNumber":0.00},{"tipoVariabile":"GREIMPCALCFIN","valNumber":363.36},{"tipoVariabile":"GREIMPCALCFINLORDO","valNumber":962.34},{"tipoVariabile":"GIOIMPRIDRIT","valNumber":0.00},{"tipoVariabile":"GIOIMPRIDLIN2","valNumber":96.23},{"tipoVariabile":"GIOIMPRIDLIN3","valNumber":0.00},{"tipoVariabile":"GIOIMPCALCFIN","valNumber":866.11},{"tipoVariabile":"GIOIMPCALCFINLORDO","valNumber":866.11},{"tipoVariabile":"IMPCALC","valNumber":3528.58},{"tipoVariabile":"IMPCALCFIN","valNumber":1647.04},{"tipoVariabile":"IMPCALCFINLORDO","valNumber":3403.48}]}',
	'{"variabiliCalcolo":[],"esitiControlli":[{"tipoControllo":"BRIDUSDC036_verificaRitardo","esito":false,"livelloControllo":"NULL"},{"tipoControllo":"BRIDUSDC043_riduzioneCapping","esito":false,"livelloControllo":"NULL"},{"tipoControllo":"BRIDUSDC134_importoErogabilePositivo","esito":true,"livelloControllo":"NULL"}],"variabiliParticellaColtura":null,"variabiliCalcoloDaStampare":[]}'
	);
		
		
COMMIT;
