--Richieste
INSERT INTO A4GT_RICHIESTA_CARBURANTE (ID, VERSIONE, CUAA, CAMPAGNA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, DOCUMENTO, DENOMINAZIONE, GASOLIO_TERZI, GASOLIO, BENZINA, GASOLIO_SERRE)
VALUES (1, 0, '00123890220' , 2022, to_date('01/09/2022 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), '00123890220', 'IN_COMPILAZIONE', NULL , 'NO_DENOM', 0,0,0,0);

INSERT INTO A4GT_RICHIESTA_CARBURANTE (ID, VERSIONE, CUAA, CAMPAGNA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, DOCUMENTO, DENOMINAZIONE, DATA_PROTOCOLLAZIONE, GASOLIO_TERZI, GASOLIO, BENZINA, GASOLIO_SERRE)
VALUES (2, 0, 'BBBDNL95R14L378T' , 2022, to_date('01/09/2022 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), 'BBBDNL95R14L378T', 'AUTORIZZATA', 'AABBAA', 'NO_DENOM', to_date('01/12/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), 0,0,0,0);

INSERT INTO A4GT_RICHIESTA_CARBURANTE (ID, VERSIONE, CUAA, CAMPAGNA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, DOCUMENTO, DENOMINAZIONE, DATA_PROTOCOLLAZIONE, GASOLIO_TERZI, GASOLIO, BENZINA, GASOLIO_SERRE)
VALUES (3, 0, 'STDKRD52P15B160V' , 2022, to_date('01/09/2022 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), 'STDKRD52P15B160V', 'AUTORIZZATA', 'AABBAA', 'NO_DENOM', to_date('01/12/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), 0,0,0,0);


-- inadempiente
INSERT INTO A4GT_RICHIESTA_CARBURANTE (ID, VERSIONE, CUAA, CAMPAGNA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, DOCUMENTO, DENOMINAZIONE, DATA_PROTOCOLLAZIONE, GASOLIO_TERZI, GASOLIO, BENZINA, GASOLIO_SERRE)
VALUES (4, 0, 'TRRCST78B08C794X' , 2022, to_date('01/09/2022 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), 'TRRCST78B08C794X', 'AUTORIZZATA', 'AABBAA', 'NO_DENOM', to_date('01/12/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), 0,0,0,0);


-- cliente con richieste a superficie
INSERT INTO A4GT_RICHIESTA_CARBURANTE (ID, VERSIONE, CUAA, CAMPAGNA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, DOCUMENTO, DENOMINAZIONE, DATA_PROTOCOLLAZIONE, GASOLIO_TERZI, GASOLIO, BENZINA, GASOLIO_SERRE)
VALUES (5, 0, '01720090222' , 2022, to_date('01/09/2022 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), '01720090222', 'AUTORIZZATA', 'AABBAA', 'NO_DENOM', to_date('01/12/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), 0,0,0,0);

INSERT INTO A4GT_FABBISOGNO (ID, VERSIONE, ID_RICHIESTA, QUANTITA, CARBURANTE, ID_LAVORAZIONE) 
VALUES (5, 0 ,5, 100, 'GASOLIO', 1368);

INSERT INTO A4GT_DICHIARAZIONE_CONSUMI
(ID, VERSIONE, ID_RICHIESTA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, DATA_CONDUZIONE)
VALUES(5, 0, 5, to_date('02/03/2022 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), '01720090222', 'PROTOCOLLATA', 'AAA', to_date('04/05/2022 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), to_date('02/03/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'));

INSERT INTO A4GT_CLIENTE (ID, VERSIONE, ID_CONSUMI, CUAA, DENOMINAZIONE, ID_FASCICOLO) 
VALUES (5, 0 , 5, 'TRRCST78B08C794X', 'TRRCST78B08C794X', 10);

INSERT INTO A4GT_CONSUMI_CLIENTI (ID, VERSIONE, ID_CLIENTE, QUANTITA, CARBURANTE, ID_LAVORAZIONE) 
VALUES (5, 0 , 5, 150, 'GASOLIO', 1368);


--Dichiarazioni Consumi
INSERT INTO A4GT_DICHIARAZIONE_CONSUMI
(ID, VERSIONE, ID_RICHIESTA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, DATA_CONDUZIONE)
VALUES(1, 0, 1, to_date('02/03/2022 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), '00123890220', 'IN_COMPILAZIONE', NULL, NULL,to_date('02/03/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'));

INSERT INTO A4GT_DICHIARAZIONE_CONSUMI
(ID, VERSIONE, ID_RICHIESTA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, DATA_CONDUZIONE, MOTIVAZIONE_ACCISA)
VALUES(2, 0, 2, to_date('02/03/2022 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), 'BBBDNL95R14L378T', 'PROTOCOLLATA', 'AAA', to_date('04/05/2022 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), to_date('02/03/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), 'MOTIVAZIONE');

INSERT INTO A4GT_DICHIARAZIONE_CONSUMI
(ID, VERSIONE, ID_RICHIESTA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, PROTOCOLLO, DATA_PROTOCOLLAZIONE, DATA_CONDUZIONE)
VALUES(3, 0, 3, to_date('02/03/2022 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), 'STDKRD52P15B160V', 'IN_COMPILAZIONE', NULL, NULL,to_date('02/03/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'));


commit;