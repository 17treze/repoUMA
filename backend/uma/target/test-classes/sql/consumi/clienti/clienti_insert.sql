--Richieste
INSERT INTO A4GT_RICHIESTA_CARBURANTE
(ID, VERSIONE, CUAA, CAMPAGNA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, GASOLIO, BENZINA, GASOLIO_SERRE, GASOLIO_TERZI, NOTE, DOCUMENTO, DENOMINAZIONE)
VALUES(7732, 2, 'ZMBRCR96D11L174G', 2021, TIMESTAMP '2021-02-22 12:22:04.000000', 'FRLGPP67A01H330V', 'AUTORIZZATA', 'PAT_TEST/RFS151-22/02/2021-0000336', TIMESTAMP '2021-02-22 12:29:51.000000', NULL, NULL, NULL, NULL, NULL, NULL, 'NO_DENOM');

INSERT INTO A4GT_RICHIESTA_CARBURANTE
(ID, VERSIONE, CUAA, CAMPAGNA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, GASOLIO, BENZINA, GASOLIO_SERRE, GASOLIO_TERZI, NOTE, DOCUMENTO, DENOMINAZIONE)
VALUES(7733, 2, 'ZMBRCR96D11L174X', 2021, TIMESTAMP '2021-02-22 12:22:04.000000', 'FRLGPP67A01H330V', 'AUTORIZZATA', 'PAT_TEST/RFS151-22/02/2021-0000336', TIMESTAMP '2021-02-22 12:29:51.000000', NULL, NULL, NULL, NULL, NULL, NULL, 'NO_DENOM');


INSERT INTO A4GT_RICHIESTA_CARBURANTE
(ID, VERSIONE, CUAA, CAMPAGNA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, GASOLIO, BENZINA, GASOLIO_SERRE, GASOLIO_TERZI, NOTE, DOCUMENTO, DENOMINAZIONE)
VALUES(7734, 2, 'ZMBRCR96D11L174Y', 2021, TIMESTAMP '2021-02-22 12:22:04.000000', 'FRLGPP67A01H330X', 'AUTORIZZATA', 'PAT_TEST/RFS151-22/02/2021-0000336', TIMESTAMP '2021-02-22 12:29:51.000000', NULL, NULL, NULL, NULL, NULL, NULL, 'NO_DENOM');

-- Macchine
INSERT INTO A4GT_UTILIZZO_MACCHINARI
(ID, VERSIONE, ID_RICHIESTA, DESCRIZIONE, CLASSE, MARCA, ALIMENTAZIONE, TARGA, POSSESSO, FLAG_UTILIZZO, IDENTIFICATIVO_AGS)
VALUES(10731, 0, 7732, 'JAGUAR 100 V DT', 'TRATTRICI', 'SAME', 'GASOLIO', 'TN22989', 'PROPRIETA', 1, 84574);

--Consumi
INSERT INTO A4GT_DICHIARAZIONE_CONSUMI
(ID, VERSIONE, ID_RICHIESTA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, DATA_CONDUZIONE, MOTIVAZIONE_ACCISA, DOCUMENTO)
VALUES(7761, 0, 7732, TIMESTAMP '2021-03-02 17:13:29.000000', 'CNTBRN95T13F839K', 'IN_COMPILAZIONE', NULL, NULL, TIMESTAMP '2021-11-01 23:59:59.000000', NULL, NULL);

INSERT INTO A4GT_DICHIARAZIONE_CONSUMI
(ID, VERSIONE, ID_RICHIESTA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, DATA_CONDUZIONE, MOTIVAZIONE_ACCISA, DOCUMENTO)
VALUES(7763, 0, 7733, TIMESTAMP '2021-03-02 17:13:29.000000', 'CNTBRN95T13F839P', 'IN_COMPILAZIONE', NULL, NULL, TIMESTAMP '2021-11-01 23:59:59.000000', NULL, NULL);

INSERT INTO A4GT_DICHIARAZIONE_CONSUMI
(ID, VERSIONE, ID_RICHIESTA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, DATA_CONDUZIONE, MOTIVAZIONE_ACCISA, DOCUMENTO)
VALUES(7764, 0, 7734, TIMESTAMP '2021-03-02 17:13:29.000000', 'FRLGPP67A01H330X', 'PROTOCOLLATA', NULL, NULL, TIMESTAMP '2021-11-01 23:59:59.000000', NULL, NULL);

--Clienti
INSERT INTO A4GT_CLIENTE
(ID, VERSIONE, ID_CONSUMI, CUAA, DENOMINAZIONE, ID_FASCICOLO)
VALUES(1, 0, 7761, 'BBBFBA66E31F187R', 'BEBBER FABIO', 1);

INSERT INTO A4GT_CLIENTE
(ID, VERSIONE, ID_CONSUMI, CUAA, DENOMINAZIONE, ID_FASCICOLO)
VALUES(2, 0, 7761, 'MSTFBA79L10H612L', 'MAESTRANZI FABIO', 2);

INSERT INTO A4GT_CLIENTE
(ID, VERSIONE, ID_CONSUMI, CUAA, DENOMINAZIONE, ID_FASCICOLO)
VALUES(5, 0, 7764, 'MSTFBA79L10H612G', 'MAESTRANZI FABIO', 5);

-- Consumi clienti
INSERT INTO A4GT_CONSUMI_CLIENTI
(ID, VERSIONE, ID_CLIENTE, QUANTITA, CARBURANTE, ID_LAVORAZIONE)
VALUES(8159, 0, 2, 99, 'BENZINA', 1262);
INSERT INTO A4GT_CONSUMI_CLIENTI
(ID, VERSIONE, ID_CLIENTE, QUANTITA, CARBURANTE, ID_LAVORAZIONE)
VALUES(8164, 0, 2, 1.4, 'GASOLIO', 1369);
INSERT INTO A4GT_CONSUMI_CLIENTI
(ID, VERSIONE, ID_CLIENTE, QUANTITA, CARBURANTE, ID_LAVORAZIONE)
VALUES(8135, 0, 2, 99, 'BENZINA', 1395);
INSERT INTO A4GT_CONSUMI_CLIENTI
(ID, VERSIONE, ID_CLIENTE, QUANTITA, CARBURANTE, ID_LAVORAZIONE)
VALUES(8374, 0, 2, 99, 'BENZINA', 1414);
INSERT INTO A4GT_CONSUMI_CLIENTI
(ID, VERSIONE, ID_CLIENTE, QUANTITA, CARBURANTE, ID_LAVORAZIONE)
VALUES(8974, 0, 2, 1.4, 'GASOLIO', 1240);
INSERT INTO A4GT_CONSUMI_CLIENTI
(ID, VERSIONE, ID_CLIENTE, QUANTITA, CARBURANTE, ID_LAVORAZIONE)
VALUES(1253, 0, 2, 99, 'BENZINA', 1247);
INSERT INTO A4GT_CONSUMI_CLIENTI
(ID, VERSIONE, ID_CLIENTE, QUANTITA, CARBURANTE, ID_LAVORAZIONE)
VALUES(1625, 0, 2, 1.4, 'GASOLIO', 1265);

-- Fatture clienti
INSERT INTO A4GT_FATTURE_CLIENTI
(ID, VERSIONE, ID_CLIENTE, DOCUMENTO, NOME_FILE)
VALUES(1, 0, 2, 'CEBF3C2F72A3214FE6D6BDF83271E7E567', 'allegato_1.pdf');


commit;