-- Caa 1
INSERT INTO A4GT_CAA
(ID, VERSIONE, CODICE_FISCALE, DENOMINAZIONE, PARTITA_IVA, FORMA_GIURIDICA, ATTO_RICONOSCIMENTO, SOCIETA_SERVIZI, CF_SOCIETA_SERVIZI, DESCRIZIONE_ESTESA, CODICE_ISTAT, FRAZIONE, TOPONIMO, VIA, NUMERO_CIVICO, CAP, PROVINCIA, COMUNE, EMAIL)
VALUES(10, 0, '01896970223', 'CAA COOPTRENTO SRL', '01896970223', 'SOCIETA A RESPONSABILITA LIMITATA', 'determinazione del Dirigente del Servizio Vigilanza e Promozione delle Attività Agricole n. 282 del 23 agosto 2011', NULL, NULL, NULL, NULL, NULL, 'VIA BRENNERO 322', 'VIA BRENNERO', '322', '38121', 'TN', 'TRENTO', 'a4g-test@tndigit.it');

insert into A4GT_SPORTELLO (ID,VERSIONE,IDENTIFICATIVO,DENOMINAZIONE,ID_CAA) values (11,0,11,'SPORTELLO - CORRENTE', 10);

--------------------------------XPDNDR77B03L378X - mandato corrente
insert into A4GT_PERSONA (ID, ID_VALIDAZIONE, VERSIONE, CODICE_FISCALE) values (5, 0, 0, 'XPDNDR77B03L378X');
insert into A4GT_PERSONA (ID, ID_VALIDAZIONE, VERSIONE, CODICE_FISCALE) values (6, 0, 0, 'XPDNDR77B03L378W');
insert into A4GT_PERSONA (ID, ID_VALIDAZIONE, VERSIONE, CODICE_FISCALE) values (7, 0, 0, 'XPDNDR77B03L378Z');
insert into A4GT_PERSONA (ID, ID_VALIDAZIONE, VERSIONE, CODICE_FISCALE) values (8, 0, 0, 'XPDNDR77B03L378R');
insert into A4GT_PERSONA (ID, ID_VALIDAZIONE, VERSIONE, CODICE_FISCALE) values (9, 0, 0, 'ZPDNDR77B03L378R');
-- eredi per XPDNDR77B03L378Z, XPDNDR77B03L378R,  ZPDNDR77B03L378R
insert into A4GT_PERSONA (ID, ID_VALIDAZIONE, VERSIONE, CODICE_FISCALE) values (90, 0, 0, 'APDNDR77B03L378Z');
insert into A4GT_PERSONA (ID, ID_VALIDAZIONE, VERSIONE, CODICE_FISCALE) values (91, 0, 0, 'APDNDR77B03L378R');
insert into A4GT_PERSONA (ID, ID_VALIDAZIONE, VERSIONE, CODICE_FISCALE) values (92, 0, 0, 'BPDNDR77B03L378R');
insert into A4GT_PERSONA (ID, ID_VALIDAZIONE, VERSIONE, CODICE_FISCALE) values (93, 0, 0, 'CPDNDR77B03L378R');

insert into A4GT_PERSONA_FISICA (ID, ID_VALIDAZIONE,VERSIONE,NOME,COGNOME,SESSO,DATA_NASCITA,COMUNE_NASCITA,PROVINCIA_NASCITA,DECEDUTO) values (5,0,0,'ANDREA','DEPEDRI','MASCHIO',SYSDATE,'TRENTO','TRENTINO',0);
insert into A4GT_PERSONA_FISICA (ID, ID_VALIDAZIONE,VERSIONE,NOME,COGNOME,SESSO,DATA_NASCITA,COMUNE_NASCITA,PROVINCIA_NASCITA,DECEDUTO) values (6,0,0,'ANDREA','DEPEDRI','MASCHIO',SYSDATE,'TRENTO','TRENTINO',0);
insert into A4GT_PERSONA_FISICA (ID, ID_VALIDAZIONE,VERSIONE,NOME,COGNOME,SESSO,DATA_NASCITA,COMUNE_NASCITA,PROVINCIA_NASCITA,DECEDUTO) values (7,0,0,'ANDREA2','DEPEDRI2','MASCHIO',SYSDATE,'TRENTO','TRENTINO',0);
insert into A4GT_PERSONA_FISICA (ID, ID_VALIDAZIONE,VERSIONE,NOME,COGNOME,SESSO,DATA_NASCITA,COMUNE_NASCITA,PROVINCIA_NASCITA,DECEDUTO,DATA_MORTE) values (8,0,0,'PERSONA','GENERICA','MASCHIO',SYSDATE,'TRENTO','TRENTINO',1,to_date('01/01/2022 22:00:00', 'dd/mm/yyyy HH24:MI:SS'));
insert into A4GT_PERSONA_FISICA (ID, ID_VALIDAZIONE,VERSIONE,NOME,COGNOME,SESSO,DATA_NASCITA,COMUNE_NASCITA,PROVINCIA_NASCITA,DECEDUTO,DATA_MORTE) values (9,0,0,'PERSONA2','GENERICA2','MASCHIO',SYSDATE,'TRENTO','TRENTINO',1,to_date('01/01/2022 22:00:00', 'dd/mm/yyyy HH24:MI:SS'));
-- eredi per XPDNDR77B03L378Z e XPDNDR77B03L378R
insert into A4GT_PERSONA_FISICA (ID, ID_VALIDAZIONE,VERSIONE,NOME,COGNOME,SESSO,DATA_NASCITA,COMUNE_NASCITA,PROVINCIA_NASCITA,DECEDUTO) values (90,0,0,'ANDREA3','DEPEDRI3','MASCHIO',SYSDATE,'TRENTO','TRENTINO',0);
insert into A4GT_PERSONA_FISICA (ID, ID_VALIDAZIONE,VERSIONE,NOME,COGNOME,SESSO,DATA_NASCITA,COMUNE_NASCITA,PROVINCIA_NASCITA,DECEDUTO) values (91,0,0,'PERSONA3','GENERICA3','MASCHIO',SYSDATE,'TRENTO','TRENTINO',0);
insert into A4GT_PERSONA_FISICA (ID, ID_VALIDAZIONE,VERSIONE,NOME,COGNOME,SESSO,DATA_NASCITA,COMUNE_NASCITA,PROVINCIA_NASCITA,DECEDUTO) values (92,0,0,'ANDREA4','DEPEDRI4','MASCHIO',SYSDATE,'TRENTO','TRENTINO',0);
insert into A4GT_PERSONA_FISICA (ID, ID_VALIDAZIONE,VERSIONE,NOME,COGNOME,SESSO,DATA_NASCITA,COMUNE_NASCITA,PROVINCIA_NASCITA,DECEDUTO) values (93,0,0,'PERSONA4','GENERICA4','MASCHIO',SYSDATE,'TRENTO','TRENTINO',0);

---- BEGIN persona giuridica
INSERT INTO A4GT_PERSONA (ID, ID_VALIDAZIONE, VERSIONE, CODICE_FISCALE)
VALUES(50, 0, 0, '00123890220');

INSERT INTO A4GT_PERSONA_GIURIDICA
(ID, ID_VALIDAZIONE, VERSIONE, PARTITA_IVA, DENOMINAZIONE, FORMA_GIURIDICA, OGGETTO_SOCIALE, DATA_COSTITUZIONE, DATA_TERMINE, CAPITALE_SOCIALE_DELIBERATO, CF_RAPPRESENTANTE_LEGALE, NOME_RAPPRESENTANTE_LEGALE, DATA_ISCRIZIONE, PROVINCIA_CCIAA, NUMERO_REPERTORIO, CESSATA, SEDE_DESCRIZIONE_ESTESA, SEDE_TOPONIMO, SEDE_VIA, SEDE_NUMERO_CIVICO, SEDE_CAP, SEDE_CODICE_ISTAT, SEDE_FRAZIONE, SEDE_PROVINCIA, SEDE_COMUNE)
VALUES(50, 0, 0, '00123890220', 'denominazione', '', '', NULL , NULL , 0, 'MRTLNZ72M30L378I', 'Lorenzo Martinelli', NULL , '', 0, 0, '', '', '', '', '', '', '', '', '');

INSERT INTO A4GT_FASCICOLO(ID, VERSIONE, CUAA, STATO, DENOMINAZIONE, ID_PERSONA, PERSONA_ID_VALIDAZIONE, ORGANISMO_PAGATORE, DATA_MODIFICA, UTENTE_MODIFICA)
VALUES(11, 0 , '00123890220', 'IN_CHIUSURA', 'denominazione', 50, 0, 'APPAG', to_date('01-01-2020', 'dd-mm-yyyy'), 'Utente Test');

INSERT INTO A4GT_DETENZIONE (ID, ID_VALIDAZIONE, VERSIONE, DATA_INIZIO, DATA_FINE,  ID_FASCICOLO)
VALUES(11, 0, 0 , to_date('01/09/2019 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), null, 11);
INSERT INTO A4GT_MANDATO (ID, VERSIONE, IDENTIFICATIVO_SPORTELLO, CONTRATTO , DATA_SOTTOSCRIZIONE)
VALUES(11, 0 , 11, 'AAAA', to_date('01/06/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'));
---- END persona giuridica


INSERT INTO A4GT_FASCICOLO (ID, ID_VALIDAZIONE, VERSIONE, CUAA, STATO, DENOMINAZIONE, ID_PERSONA, ORGANISMO_PAGATORE, UTENTE_MODIFICA)
VALUES (1, 0, 0, 'XPDNDR77B03L378X', 'IN_AGGIORNAMENTO', 'DEPEDRI ANDREA', 5, 'APPAG', 'UTENTE MODIFICA');

INSERT INTO A4GT_FASCICOLO (ID, ID_VALIDAZIONE, VERSIONE, CUAA, STATO, DENOMINAZIONE, ID_PERSONA, ORGANISMO_PAGATORE, UTENTE_MODIFICA)
VALUES (2, 0, 0, 'XPDNDR77B03L378W', 'IN_AGGIORNAMENTO', 'DEPEDRI ANDREA', 6, 'APPAG', 'UTENTE MODIFICA');

INSERT INTO A4GT_FASCICOLO (ID, ID_VALIDAZIONE, VERSIONE, CUAA, STATO, DENOMINAZIONE, ID_PERSONA, ORGANISMO_PAGATORE, UTENTE_MODIFICA)
VALUES (3, 0, 0, 'XPDNDR77B03L378Z', 'IN_CHIUSURA', 'DEPEDRI ANDREA', 7, 'APPAG', 'UTENTE MODIFICA');

INSERT INTO A4GT_FASCICOLO (ID, ID_VALIDAZIONE, VERSIONE, CUAA, STATO, DENOMINAZIONE, ID_PERSONA, ORGANISMO_PAGATORE, UTENTE_MODIFICA)
VALUES (4, 0, 0, 'XPDNDR77B03L378R', 'IN_CHIUSURA', 'DEPEDRI ANDREA', 8, 'APPAG', 'UTENTE MODIFICA');

INSERT INTO A4GT_FASCICOLO (ID, ID_VALIDAZIONE, VERSIONE, CUAA, STATO, DENOMINAZIONE, ID_PERSONA, ORGANISMO_PAGATORE, UTENTE_MODIFICA)
VALUES (5, 0, 0, 'ZPDNDR77B03L378R', 'IN_CHIUSURA', 'persona 2 generica 2', 9, 'APPAG', 'UTENTE MODIFICA');

-- detenzione 1
INSERT INTO A4GT_DETENZIONE (ID, ID_VALIDAZIONE, VERSIONE, DATA_INIZIO, DATA_FINE,  ID_FASCICOLO) 
VALUES(30, 0, 0 , to_date('01/09/2019 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), null, 1);
INSERT INTO A4GT_MANDATO (ID, VERSIONE, IDENTIFICATIVO_SPORTELLO, CONTRATTO , DATA_SOTTOSCRIZIONE) 
VALUES(30, 0 , 11, 'AAAA', to_date('01/06/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'));

INSERT INTO A4GT_DETENZIONE (ID, ID_VALIDAZIONE, VERSIONE, DATA_INIZIO, DATA_FINE,  ID_FASCICOLO) 
VALUES(32, 0, 0 , to_date('01/09/2019 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), null, 2);
INSERT INTO A4GT_MANDATO (ID, VERSIONE, IDENTIFICATIVO_SPORTELLO, CONTRATTO , DATA_SOTTOSCRIZIONE) 
VALUES(32, 0 , 11, 'AAAA', to_date('01/06/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'));

INSERT INTO A4GT_DETENZIONE (ID, ID_VALIDAZIONE, VERSIONE, DATA_INIZIO, DATA_FINE,  ID_FASCICOLO)
VALUES(33, 0, 0 , to_date('01/09/2019 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), null, 3);
INSERT INTO A4GT_MANDATO (ID, VERSIONE, IDENTIFICATIVO_SPORTELLO, CONTRATTO , DATA_SOTTOSCRIZIONE)
VALUES(33, 0 , 11, 'AAAA', to_date('01/06/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'));

INSERT INTO A4GT_DETENZIONE (ID, ID_VALIDAZIONE, VERSIONE, DATA_INIZIO, DATA_FINE,  ID_FASCICOLO)
VALUES(34, 0, 0 , to_date('01/09/2019 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), null, 4);
INSERT INTO A4GT_MANDATO (ID, VERSIONE, IDENTIFICATIVO_SPORTELLO, CONTRATTO , DATA_SOTTOSCRIZIONE)
VALUES(34, 0 , 11, 'AAAA', to_date('01/06/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'));

INSERT INTO A4GT_DETENZIONE (ID, ID_VALIDAZIONE, VERSIONE, DATA_INIZIO, DATA_FINE,  ID_FASCICOLO)
VALUES(35, 0, 0 , to_date('01/09/2019 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), null, 5);
INSERT INTO A4GT_MANDATO (ID, VERSIONE, IDENTIFICATIVO_SPORTELLO, CONTRATTO , DATA_SOTTOSCRIZIONE)
VALUES(35, 0 , 11, 'AAAA', to_date('01/06/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'));

INSERT INTO A4GT_EREDE (ID, VERSIONE, PERSONA_FISICA_ID, PERSONA_FISICA_ID_VALIDAZIONE, FASCICOLO_ID, FASCICOLO_ID_VALIDAZIONE, FIRMATARIO)
VALUES (31, 0, 5, 0, 1, 0, 0);

INSERT INTO A4GT_EREDE (ID, VERSIONE, PERSONA_FISICA_ID, PERSONA_FISICA_ID_VALIDAZIONE, FASCICOLO_ID, FASCICOLO_ID_VALIDAZIONE, FIRMATARIO)
VALUES (33, 0, 6, 0, 2, 0, 0);

INSERT INTO A4GT_EREDE (ID, VERSIONE, PERSONA_FISICA_ID, PERSONA_FISICA_ID_VALIDAZIONE, FASCICOLO_ID, FASCICOLO_ID_VALIDAZIONE, FIRMATARIO)
VALUES (34, 0, 90, 0, 3, 0, 1);

INSERT INTO A4GT_EREDE (ID, VERSIONE, PERSONA_FISICA_ID, PERSONA_FISICA_ID_VALIDAZIONE, FASCICOLO_ID, FASCICOLO_ID_VALIDAZIONE, FIRMATARIO)
VALUES (35, 0, 91, 0, 4, 0, 1);

INSERT INTO A4GT_EREDE (ID, VERSIONE, PERSONA_FISICA_ID, PERSONA_FISICA_ID_VALIDAZIONE, FASCICOLO_ID, FASCICOLO_ID_VALIDAZIONE, FIRMATARIO)
VALUES (36, 0, 92, 0, 5, 0, 1);

INSERT INTO A4GT_EREDE (ID, VERSIONE, PERSONA_FISICA_ID, PERSONA_FISICA_ID_VALIDAZIONE, FASCICOLO_ID, FASCICOLO_ID_VALIDAZIONE, FIRMATARIO)
VALUES (37, 0, 93, 0, 5, 0, 1);
