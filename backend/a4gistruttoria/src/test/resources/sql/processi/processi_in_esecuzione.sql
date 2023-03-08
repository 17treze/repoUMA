INSERT INTO A4GT_PROCESSO (ID,VERSIONE,ID_DATI_SETTORE,TIPO,STATO,DT_INIZIO,DT_FINE,PERCENTUALE_AVANZAMENTO,DATI_ELABORAZIONE)
VALUES (
	6254783,3,NULL,
	'RICEVIBILITA_AGS',
	'RUN',
	to_date('07-FEB-19','DD-MON-RR'),to_date('08-FEB-19','DD-MON-RR'),80,'{"totaleIstruttorieGestite":"2","domandeGestite":["4474032","4466809"],"domandeConProblemi":[],"parzialeIstruttorieGestite":"1"}');

commit;