INSERT INTO A4GT_FASCICOLO 
	(ID, ID_VALIDAZIONE, VERSIONE, CUAA) 
VALUES 
	(1, 0, 0, 'PDRTTR69M30C794R');
	
INSERT INTO A4GT_FASCICOLO 
    (ID, ID_VALIDAZIONE, VERSIONE, CUAA, DT_AGGIORNAMENTO_FONTI_ESTERNE) 
VALUES 
    (2, 0, 0, 'PDRTTR69M30C794X', to_date('01/01/1996 01:00:00','dd/mm/yyyy hh24:mi:ss'));
    	

INSERT INTO A4GT_STRUTTURA 
	(ID, ID_VALIDAZIONE, VERSIONE, IDENTIFICATIVO, INDIRIZZO, CAP, LOCALITA, COMUNE, LATITUDINE, LONGITUDINE, FOGLIO, SEZIONE, PARTICELLA, SUBALTERNO, CUAA) 
VALUES 
	(9293, 0, 0, '033TN004', 'SAN GIACOMO, 44', '38022', null, 'CALDES', '46.382120', '10.969250', null, null, null, null, 'PDRTTR69M30C794R');

INSERT INTO a4gt_allevamento (
    id,
    id_validazione,
    versione,
    identificativo,
    identificativo_fiscale,
    tipologia_allevamento,
    cf_proprietario,
    denominazione_proprietario,
    cf_detentore,
    denominazione_detentore,
    dt_inizio_detenzione,
    dt_fine_detenzione,
    soccida,
    specie,
    tipologia_produzione,
    orientamento_produttivo,
    dt_apertura_allevamento,
    dt_chiusura_allevamento,
    autorizzazione_sanitaria_latte,
    id_struttura,
    struttura_id_validazione,
    fascicolo_id,
    fascicolo_id_validazione
) VALUES (
    9299,
    0,
    0,
    729337,
    'PDRTTR69M30C794R',
    NULL,
    'PDRTTR69M30C794R',
    'PEDERGNANA ETTORE',
    'PDRTTR69M30C794R',
    'PEDERGNANA ETTORE',
    to_date('01-01-1996', 'dd-mm-yyyy'),
    NULL,
    'N',
    '0121',
    'AL',
    'L',
    to_date('01-01-1996', 'dd-mm-yyyy'),
    NULL,
    NULL,
    9293,
    0,
    1,
    0
);

INSERT INTO a4gt_allevamento (
    id,
    id_validazione,
    versione,
    identificativo,
    identificativo_fiscale,
    tipologia_allevamento,
    cf_proprietario,
    denominazione_proprietario,
    cf_detentore,
    denominazione_detentore,
    dt_inizio_detenzione,
    dt_fine_detenzione,
    soccida,
    specie,
    tipologia_produzione,
    orientamento_produttivo,
    dt_apertura_allevamento,
    dt_chiusura_allevamento,
    autorizzazione_sanitaria_latte,
    id_struttura,
    struttura_id_validazione,
    fascicolo_id,
    fascicolo_id_validazione
) VALUES (
    9300,
    0,
    0,
    1596429,
    'PDRTTR69M30C794R',
    'ST',
    'PDRTTR69M30C794R',
    'PEDERGNANA ETTORE',
    'PDRTTR69M30C794R',
    'PEDERGNANA ETTORE',
    to_date('01-01-2003', 'dd-mm-yyyy'),
    NULL,
    'N',
    '0122',
    'AL',
    'I',
    to_date('01-01-2003', 'dd-mm-yyyy'),
    NULL,
    NULL,
    9293,
    0,
    1,
    0
);

INSERT INTO a4gt_allevamento (
    id,
    id_validazione,
    versione,
    identificativo,
    identificativo_fiscale,
    tipologia_allevamento,
    cf_proprietario,
    denominazione_proprietario,
    cf_detentore,
    denominazione_detentore,
    dt_inizio_detenzione,
    dt_fine_detenzione,
    soccida,
    specie,
    tipologia_produzione,
    orientamento_produttivo,
    dt_apertura_allevamento,
    dt_chiusura_allevamento,
    autorizzazione_sanitaria_latte,
    id_struttura,
    struttura_id_validazione,
    fascicolo_id,
    fascicolo_id_validazione
) VALUES (
    9301,
    0,
    0,
    4382114,
    'PDRTTR69M30C794R',
    NULL,
    'PDRTTR69M30C794R',
    'PEDERGNANA ETTORE',
    'PDRTTR69M30C794R',
    'PEDERGNANA ETTORE',
    to_date('01-01-2010', 'dd-mm-yyyy'),
    NULL,
    'N',
    '0126',
    'AL',
    'G',
    to_date('01-01-2010', 'dd-mm-yyyy'),
    NULL,
    NULL,
    9293,
    0,
    1,
    0
);

INSERT INTO A4GT_CONTROLLO_COMPLETEZZA (ID,VERSIONE,CUAA,TIPO_CONTROLLO,ESITO,ID_CONTROLLO,DATA_ESECUZIONE,UTENTE)
VALUES ('1','0','PDRTTR69M30C794X','IS_AGGIORNAMENTO_FONTI_ESTERNE_ZOOTECNIA','0',null,to_date('23-NOV-21','DD-MON-RR'),'DPDNDR77B03L378L');

INSERT INTO A4GT_CONTROLLO_COMPLETEZZA (ID,VERSIONE,CUAA,TIPO_CONTROLLO,ESITO,ID_CONTROLLO,DATA_ESECUZIONE,UTENTE)
VALUES ('2','0','PDRTTR69M30C794R','IS_AGGIORNAMENTO_FONTI_ESTERNE_ZOOTECNIA','-3',null,to_date('23-NOV-21','DD-MON-RR'),'DPDNDR77B03L378L');