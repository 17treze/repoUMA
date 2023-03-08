INSERT INTO A4GT_ISTRUTTORIA (
	ID, VERSIONE, 
	ID_DOMANDA, 
	SOSTEGNO,
	ID_STATO_LAVORAZIONE, 
	TIPOLOGIA
)
values (
	999103433,1,
	(SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'URRRED82B02F839U'),
	'ZOOTECNIA',
	(select id from a4gd_stato_lav_sostegno where identificativo = 'LIQUIDABILE'),
	'SALDO'
);

Insert into A4GT_TRANSIZIONE_ISTRUTTORIA
	(ID, 
	VERSIONE, 
	ID_STATO_INIZIALE, 
	ID_STATO_FINALE, 
	DATA_ESECUZIONE, 
	ID_ISTRUTTORIA)
values (
	1999103433,1,
	(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'CONTROLLI_CALCOLO_OK'),
	(SELECT ID FROM A4GD_STATO_LAV_SOSTEGNO WHERE IDENTIFICATIVO = 'LIQUIDABILE'),
	to_date('07-FEB-19','DD-MON-RR'),
	999103433
);

commit;

