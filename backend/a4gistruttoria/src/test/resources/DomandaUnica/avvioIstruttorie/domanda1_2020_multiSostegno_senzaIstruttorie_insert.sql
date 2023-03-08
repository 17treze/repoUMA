-- DOMANDE 2020
INSERT INTO A4GT_DOMANDA(ID, VERSIONE, NUMERO_DOMANDA, COD_MODULO_DOMANDA, DESC_MODULO_DOMANDA, ANNO_CAMPAGNA, STATO, CUAA_INTESTATARIO, DT_PRESENTAZIONE,DT_PROTOCOLLAZIONE,
COD_ENTE_COMPILATORE, DESC_ENTE_COMPILATORE, RAGIONE_SOCIALE)
VALUES (1, 0, 1, 'BPS_2020', 'PAGAMENTI DIRETTI', 2020, 'ACQUISITA', 'FRSLBT76H42E6251',
TO_DATE('06/04/2018 22:00:00', 'DD/MM/YYYY HH24:MI:SS'),TO_DATE('26/04/2018 15:26:20', 'DD/MM/YYYY HH24:MI:SS'), 6, 'CAA COLDIRETTI DEL TRENTINO - 005', 'AZIENDA TEST 1');

insert into A4GT_SOSTEGNO (id, versione, id_domanda, sostegno)
values (
1, 0,
1, 'DISACCOPPIATO');

insert into A4GT_SOSTEGNO (id, versione, id_domanda, sostegno)
values (
2, 0,
1, 'SUPERFICIE');

commit;