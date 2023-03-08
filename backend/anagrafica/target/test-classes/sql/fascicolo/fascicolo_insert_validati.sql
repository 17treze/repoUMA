-- Caa 1
INSERT INTO A4GT_CAA (
	ID, VERSIONE, CODICE_FISCALE, DENOMINAZIONE, PARTITA_IVA, FORMA_GIURIDICA, ATTO_RICONOSCIMENTO, SOCIETA_SERVIZI, CF_SOCIETA_SERVIZI, DESCRIZIONE_ESTESA, CODICE_ISTAT, FRAZIONE, TOPONIMO, VIA, NUMERO_CIVICO, CAP, PROVINCIA, COMUNE, EMAIL
) VALUES (
	10, 0, '01896970223', 'CAA COOPTRENTO SRL', '01896970223', 'SOCIETA A RESPONSABILITA LIMITATA', 'determinazione del Dirigente del Servizio Vigilanza e Promozione delle Attività Agricole n. 282 del 23 agosto 2011', NULL, NULL, NULL, NULL, NULL, 'VIA BRENNERO 322', 'VIA BRENNERO', '322', '38121', 'TN', 'TRENTO', 'a4g-test@tndigit.it'
);

INSERT INTO A4GT_SPORTELLO (ID,VERSIONE,IDENTIFICATIVO,DENOMINAZIONE,ID_CAA) values (11, 0, 11, 'SPORTELLO - CORRENTE', 10);

--------

--------------------------------XPDNDR77B03L378X - mandato corrente
INSERT INTO A4GT_PERSONA (ID, ID_VALIDAZIONE, VERSIONE, CODICE_FISCALE) VALUES (5, 0, 0, 'XPDNDR77B03L378X');
INSERT INTO A4GT_PERSONA (ID, ID_VALIDAZIONE, VERSIONE, CODICE_FISCALE) VALUES (5, 1, 0, 'XPDNDR77B03L378X');

INSERT INTO A4GT_PERSONA_FISICA (
	ID, ID_VALIDAZIONE, VERSIONE, NOME, COGNOME, SESSO, DATA_NASCITA, COMUNE_NASCITA, PROVINCIA_NASCITA, DECEDUTO
) values (
	5, 0, 0, 'ANDREA','DEPEDRI', 'MASCHIO', SYSDATE, 'TRENTO', 'TRENTINO', 0
);

INSERT INTO A4GT_PERSONA_FISICA (
	ID, ID_VALIDAZIONE, VERSIONE, NOME, COGNOME, SESSO, DATA_NASCITA, COMUNE_NASCITA, PROVINCIA_NASCITA, DECEDUTO
) VALUES (
	5, 1, 0, 'ANDREA','DEPEDRI','MASCHIO', SYSDATE, 'TRENTO', 'TRENTINO', 0
);

INSERT INTO A4GT_FASCICOLO (
	ID, ID_VALIDAZIONE, VERSIONE, CUAA, STATO, DENOMINAZIONE, ID_PERSONA, ORGANISMO_PAGATORE, UTENTE_MODIFICA
) VALUES (
	1, 0, 0, 'XPDNDR77B03L378X', 'IN_AGGIORNAMENTO', 'DEPEDRI ANDREA', 5, 'APPAG', 'UTENTE MODIFICA'
);
INSERT INTO A4GT_FASCICOLO (
	ID, ID_VALIDAZIONE, VERSIONE, CUAA, STATO, DENOMINAZIONE, ID_PERSONA, ORGANISMO_PAGATORE, UTENTE_MODIFICA
) VALUES (
	1, 1, 0, 'XPDNDR77B03L378X', 'VALIDATO', 'DEPEDRI ANDREA', 5, 'APPAG', 'UTENTE MODIFICA'
);

-- detenzione 1
INSERT INTO A4GT_DETENZIONE (
	ID, ID_VALIDAZIONE, VERSIONE, DATA_INIZIO, DATA_FINE,  ID_FASCICOLO
) VALUES (
	30, 0, 0 , to_date('01/09/2019 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), null, 1
);
INSERT INTO A4GT_DETENZIONE (
	ID, ID_VALIDAZIONE, VERSIONE, DATA_INIZIO, DATA_FINE,  ID_FASCICOLO
) VALUES (
	30, 1, 0 , to_date('01/09/2019 22:00:00', 'dd/mm/yyyy HH24:MI:SS'), null, 1
);

INSERT INTO A4GT_MANDATO (ID, VERSIONE, IDENTIFICATIVO_SPORTELLO, CONTRATTO , DATA_SOTTOSCRIZIONE) 
VALUES(30, 1 , 11, 'AAAA', to_date('01/06/2020 22:00:00', 'dd/mm/yyyy HH24:MI:SS'));



------- modo pagamento
INSERT INTO A4GT_MODO_PAGAMENTO (ID, VERSIONE, IBAN, BIC, DENOMINAZIONE, ID_FASCICOLO, FASCICOLO_ID_VALIDAZIONE)
VALUES (8375, 0, 'GB82WEST12345698765432', null, 'asdasdsa', 1, 0);

INSERT INTO A4GT_MODO_PAGAMENTO (ID, VERSIONE, IBAN, BIC, DENOMINAZIONE, ID_FASCICOLO, FASCICOLO_ID_VALIDAZIONE)
VALUES (8376, 0, 'GB82WEST12345698765432', null, 'asdasdsa', 1, 1);