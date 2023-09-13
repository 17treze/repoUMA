--Richiesta
INSERT INTO TAB_AGRI_UMAL_RICHIESTA_CARBURANTE
(ID, VERSIONE, CUAA, CAMPAGNA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, GASOLIO, BENZINA, GASOLIO_SERRE, GASOLIO_TERZI, NOTE, DOCUMENTO, DENOMINAZIONE)
VALUES(7732, 2, 'ZMBRCR96D11L174G', 2021, TIMESTAMP '2021-02-22 12:22:04.000000', 'FRLGPP67A01H330V', 'AUTORIZZATA', 'PAT_TEST/RFS151-22/02/2021-0000336', TIMESTAMP '2021-02-22 12:29:51.000000', 0, 0, 0, 5, NULL, NULL, 'Denominazione');
INSERT INTO TAB_AGRI_UMAL_RICHIESTA_CARBURANTE
(ID, VERSIONE, CUAA, CAMPAGNA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, GASOLIO, BENZINA, GASOLIO_SERRE, GASOLIO_TERZI, NOTE, DOCUMENTO, DENOMINAZIONE)
VALUES(7733, 2, 'ZMBRCR96D11L173A', 2021, TIMESTAMP '2021-02-22 12:22:04.000000', 'FRLGPP67A01H330V', 'AUTORIZZATA', 'PAT_TEST/RFS151-22/02/2021-0000336', TIMESTAMP '2021-02-22 12:29:51.000000', 0, 0, 0, 3, NULL, NULL, 'Denominazione');
INSERT INTO TAB_AGRI_UMAL_RICHIESTA_CARBURANTE
(ID, VERSIONE, CUAA, CAMPAGNA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, GASOLIO, BENZINA, GASOLIO_SERRE, GASOLIO_TERZI, NOTE, DOCUMENTO, DENOMINAZIONE)
VALUES(7734, 2, 'ZMBRCR96D11L173B', 2021, TIMESTAMP '2021-02-22 12:22:04.000000', 'FRLGPP67A01H330V', 'AUTORIZZATA', 'PAT_TEST/RFS151-22/02/2021-0000336', TIMESTAMP '2021-02-22 12:29:51.000000', 0, 0, 0, 0, NULL, NULL, 'Denominazione');


--Prelievi 7732
INSERT INTO TAB_AGRI_UMAL_PRELIEVI
(ID, VERSIONE, ID_DISTRIBUTORE, ID_RICHIESTA, GASOLIO, BENZINA, GASOLIO_SERRE, "DATA", CONSEGNATO)
VALUES(10, 0, 8162, 7732, 400, 500, 600, TIMESTAMP '2021-04-07 12:45:56.000000', 0);
INSERT INTO TAB_AGRI_UMAL_PRELIEVI
(ID, VERSIONE, ID_DISTRIBUTORE, ID_RICHIESTA, GASOLIO, BENZINA, GASOLIO_SERRE, "DATA", CONSEGNATO)
VALUES(11, 0, 8162, 7734, 400, 500, 600, TIMESTAMP '2021-04-07 12:45:56.000000', 0);
INSERT INTO TAB_AGRI_UMAL_PRELIEVI
(ID, VERSIONE, ID_DISTRIBUTORE, ID_RICHIESTA, GASOLIO, BENZINA, GASOLIO_SERRE, "DATA", CONSEGNATO)
VALUES(12, 0, 8162, 7733, 400, 500, 600, TIMESTAMP '2021-04-07 12:45:56.000000', 0);

--Consumi
INSERT INTO TAB_AGRI_UMAL_DICHIARAZIONE_CONSUMI
(ID, VERSIONE, ID_RICHIESTA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, DATA_CONDUZIONE)
VALUES(7761, 0, 7732, TIMESTAMP '2021-03-02 17:13:29.000000', 'CNTBRN95T13F839K', 'IN_COMPILAZIONE', NULL, NULL, TIMESTAMP '2021-11-01 23:59:59.000000');
INSERT INTO TAB_AGRI_UMAL_DICHIARAZIONE_CONSUMI
(ID, VERSIONE, ID_RICHIESTA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, DATA_CONDUZIONE)
VALUES(7762, 0, 7733, TIMESTAMP '2021-03-02 17:13:29.000000', 'CNTBRN95T13F839K', 'IN_COMPILAZIONE', NULL, NULL, TIMESTAMP '2021-11-01 23:59:59.000000');
INSERT INTO TAB_AGRI_UMAL_DICHIARAZIONE_CONSUMI
(ID, VERSIONE, ID_RICHIESTA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, DATA_CONDUZIONE)
VALUES(7763, 0, 7734, TIMESTAMP '2021-03-02 17:13:29.000000', 'CNTBRN95T13F839K', 'IN_COMPILAZIONE', NULL, NULL, TIMESTAMP '2021-11-01 23:59:59.000000');

--CONSUNTIVI
INSERT INTO TAB_AGRI_UMAL_CONSUNTIVI_CONSUMI
(ID, VERSIONE, ID_CONSUMI, TIPO, CARBURANTE, QUANTITA, MOTIVAZIONE)
VALUES(16222, 1, 7761, 'RIMANENZA', 'BENZINA', 7, NULL);
INSERT INTO TAB_AGRI_UMAL_CONSUNTIVI_CONSUMI
(ID, VERSIONE, ID_CONSUMI, TIPO, CARBURANTE, QUANTITA, MOTIVAZIONE)
VALUES(16706, 3, 7761, 'CONSUMATO', 'GASOLIO', 1, NULL);
INSERT INTO TAB_AGRI_UMAL_CONSUNTIVI_CONSUMI
(ID, VERSIONE, ID_CONSUMI, TIPO, CARBURANTE, QUANTITA, MOTIVAZIONE)
VALUES(16707, 3, 7762, 'CONSUMATO', 'GASOLIO', 1, NULL);
INSERT INTO TAB_AGRI_UMAL_CONSUNTIVI_CONSUMI (ID, VERSIONE, ID_CONSUMI, TIPO, CARBURANTE, QUANTITA, MOTIVAZIONE)
VALUES(325, 0 , 7763, 'AMMISSIBILE', 'GASOLIO_TERZI', 5, 'ASSEGNAZIONE_SVINCOLATA');
INSERT INTO TAB_AGRI_UMAL_CONSUNTIVI_CONSUMI
(ID, VERSIONE, ID_CONSUMI, TIPO, CARBURANTE, QUANTITA, MOTIVAZIONE)
VALUES(333, 3, 7763, 'CONSUMATO', 'GASOLIO', 1, NULL);

--ALLEGATI CONSUNTIVI
INSERT INTO TAB_AGRI_UMAL_ALLEGATI_CONSUNTIVI (ID, VERSIONE, ID_CONSUNTIVO, NOME_FILE, DESCRIZIONE_FILE, TIPO_DOCUMENTO, DOCUMENTO)
VALUES(167, 0 , 325, 'pippo2.pdf', 'descrizione file', 'AUTORIZZAZIONE_UMA', 'AAAC');
COMMIT;