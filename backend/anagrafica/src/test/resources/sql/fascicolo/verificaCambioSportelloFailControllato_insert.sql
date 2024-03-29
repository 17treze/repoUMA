
INSERT INTO A4GT_CAA
(ID, VERSIONE, CODICE_FISCALE, DENOMINAZIONE, PARTITA_IVA, FORMA_GIURIDICA, ATTO_RICONOSCIMENTO, SOCIETA_SERVIZI, CF_SOCIETA_SERVIZI, DESCRIZIONE_ESTESA, CODICE_ISTAT, FRAZIONE, TOPONIMO, VIA, NUMERO_CIVICO, CAP, PROVINCIA, COMUNE, EMAIL)
VALUES(10, 0, '01896970223', 'CAA COOPTRENTO SRL', '01896970223', 'SOCIETA A RESPONSABILITA LIMITATA', 'determinazione del Dirigente del Servizio Vigilanza e Promozione delle Attività Agricole n. 282 del 23 agosto 2011', NULL, NULL, NULL, NULL, NULL, 'VIA BRENNERO 322', 'VIA BRENNERO', '322', '38121', 'TN', 'TRENTO', 'a4g-test@tndigit.it');

insert into A4GT_SPORTELLO (ID,VERSIONE,IDENTIFICATIVO,DENOMINAZIONE,ID_CAA) values (11,0,11,'SPORTELLO - CORRENTE', 10);
insert into A4GT_SPORTELLO (ID,VERSIONE,IDENTIFICATIVO,DENOMINAZIONE,ID_CAA) values (13,0,13,'SPORTELLO - PROGRAMMATO', 10);
insert into A4GT_SPORTELLO (ID,VERSIONE,IDENTIFICATIVO,DENOMINAZIONE,ID_CAA) values (15,0,15,'SPORTELLO - NEW', 10);

-- caa a cui fare le revoche ordinarie
INSERT INTO A4GT_CAA (ID, VERSIONE, CODICE_FISCALE, DENOMINAZIONE, PARTITA_IVA, FORMA_GIURIDICA, ATTO_RICONOSCIMENTO, SOCIETA_SERVIZI, CF_SOCIETA_SERVIZI, DESCRIZIONE_ESTESA, CODICE_ISTAT, FRAZIONE, TOPONIMO, VIA, NUMERO_CIVICO, CAP, PROVINCIA, COMUNE, EMAIL)
VALUES(696, 0, '05804771003', 'CAA - CIA S.R.L.', '05804771003', 'SOCIETA A RESPONSABILITA LIMITATA', 'determinazione della Regione Lazio n. A02140 del 20 marzo 2012 con oggetto “Approvazione del verbale relativo alla valutazione della richiesta di adeguamento ai requisiti del Decreto del Ministro delle politiche agricole alimentari e forestali del 27 marzo 2008', 'AGRIVERDE - CIA S.R.L.', '01393750227', NULL, NULL, NULL, 'LUNGOTEVERE MICHELANGELO 9', 'LUNGOTEVERE MICHELANGELO', '9', '00192', 'RM', 'ROMA', 'a4g-test@tndigit.it');

INSERT INTO A4GT_SPORTELLO (ID, VERSIONE, IDENTIFICATIVO, DENOMINAZIONE, COMUNE, ID_CAA)
VALUES(712, 0, 18, 'CAA CIA - TRENTO - 001', 'TRENTO', 696);
INSERT INTO A4GT_SPORTELLO
(ID, VERSIONE, IDENTIFICATIVO, DENOMINAZIONE, COMUNE, ID_CAA)
VALUES(713, 0, 19, 'CAA CIA - CLES - 002', 'CLES', 696);

--------------------------------MSTFBA79L10H612L - cambio sportello con revoca ordinaria < 31/12

insert into A4GT_PERSONA (ID, ID_VALIDAZIONE,VERSIONE,CODICE_FISCALE) values (50,0,0,'MSTFBA79L10H612L');
insert into A4GT_PERSONA_FISICA (ID, ID_VALIDAZIONE,VERSIONE,NOME,COGNOME,SESSO,DATA_NASCITA,COMUNE_NASCITA,PROVINCIA_NASCITA,DECEDUTO) values (50,0,0,'MST','FBA','MASCHIO',SYSDATE,'TRENTO','TRENTINO',0);

INSERT INTO A4GT_FASCICOLO (ID, ID_VALIDAZIONE, VERSIONE, CUAA, STATO, DENOMINAZIONE, ID_PERSONA, PERSONA_ID_VALIDAZIONE, ORGANISMO_PAGATORE, UTENTE_MODIFICA, DATA_MODIFICA)
VALUES (2, 0, 0, 'MSTFBA79L10H612L', 'ALLA_FIRMA_AZIENDA', 'MSTFBA79L10H612L', 50, 0, 'APPAG', 'Utente Modifica', to_date('01-01-2020', 'dd-mm-yyyy'));

-- sportello corrente
INSERT INTO A4GT_DETENZIONE (ID, ID_VALIDAZIONE, VERSIONE, DATA_INIZIO, DATA_FINE,  ID_FASCICOLO) 
VALUES(32, 0, 0, to_date('01/09/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), to_date('15/10/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), 2);
INSERT INTO A4GT_MANDATO (ID, VERSIONE, IDENTIFICATIVO_SPORTELLO, CONTRATTO , DATA_SOTTOSCRIZIONE) 
VALUES(32, 0 , 11, 'AAAA', to_date('01/06/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'));

-- sportello programmato -- (indifferente se qui data fine null oppure 31 dicembre)
INSERT INTO A4GT_DETENZIONE (ID, ID_VALIDAZIONE, VERSIONE, DATA_INIZIO, DATA_FINE,  ID_FASCICOLO) 
VALUES(33, 0, 0, to_date('16/10/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), NULL, 2);
INSERT INTO A4GT_MANDATO (ID, VERSIONE, IDENTIFICATIVO_SPORTELLO, CONTRATTO , DATA_SOTTOSCRIZIONE) 
VALUES(33, 0 , 13, 'AAAA', to_date('01/06/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'));
