INSERT INTO TAB_AGRI_UMAL_RICHIESTA_CARBURANTE (ID, VERSIONE, CUAA, CAMPAGNA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, DOCUMENTO, DENOMINAZIONE)
VALUES (99, 0, 'TRRCST78B08C794Y' , 2020, to_date('01/09/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), 'TRRCST78B08C794Y', 'IN_COMPILAZIONE',NULL, 'NO_DENOM');

INSERT INTO TAB_AGRI_UMAL_RICHIESTA_CARBURANTE (ID, VERSIONE, CUAA, CAMPAGNA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, DOCUMENTO, DENOMINAZIONE)
VALUES (100, 0, 'BBBDNL95R14L378T' , 2020, to_date('01/09/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), 'BBBDNL95R14L378T', 'IN_COMPILAZIONE',NULL, 'NO_DENOM');

INSERT INTO TAB_AGRI_UMAL_RICHIESTA_CARBURANTE (ID, VERSIONE, CUAA, CAMPAGNA, DATA_PRESENTAZIONE, CF_RICHIEDENTE, STATO, DOCUMENTO, DENOMINAZIONE)
VALUES (101, 0, 'TRRCST78B08C794X' , 2019, to_date('01/09/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), 'TRRCST78B08C794X', 'IN_COMPILAZIONE',NULL, 'NO_DENOM');


INSERT INTO TAB_AGRI_UMAL_UTILIZZO_MACCHINARI
(ID, VERSIONE, ID_RICHIESTA, DESCRIZIONE, CLASSE, MARCA, ALIMENTAZIONE, TARGA, POSSESSO, FLAG_UTILIZZO, IDENTIFICATIVO_AGS)
VALUES(1, 0 , 100, 'Macchina X', 'TRATTRICE', 'MERCEDES', 'BENZINA', null , 'PROPRIETA', 0 , 123321);

INSERT INTO TAB_AGRI_UMAL_UTILIZZO_MACCHINARI
(ID, VERSIONE, ID_RICHIESTA, DESCRIZIONE, CLASSE, MARCA, ALIMENTAZIONE, TARGA, POSSESSO, FLAG_UTILIZZO, IDENTIFICATIVO_AGS)
VALUES(2, 0 , 100, 'Macchina Y', 'TRATTRICE', 'MERCEDES', 'GASOLIO', 'XXXAAABBB' , 'PROPRIETA', 1, 123321);

INSERT INTO TAB_AGRI_UMAL_UTILIZZO_MACCHINARI
(ID, VERSIONE, ID_RICHIESTA, DESCRIZIONE, CLASSE, MARCA, ALIMENTAZIONE, TARGA, POSSESSO, FLAG_UTILIZZO, IDENTIFICATIVO_AGS)
VALUES(3, 0 , 101, 'Macchina Z', 'TRATTRICE', 'MERCEDES', 'BENZINA', null , 'PROPRIETA', 0 , 123321);