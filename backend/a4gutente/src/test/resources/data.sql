-- Anagrafica profili
Insert into A4GT_PROFILO (ID,VERSIONE,IDENTIFICATIVO,DESCRIZIONE, RESPONSABILITA) values ('2','0','amministratore','Amministratore Sistema','DIPENDENTE_TNDIGIT');
Insert into A4GT_PROFILO (ID,VERSIONE,IDENTIFICATIVO,DESCRIZIONE, RESPONSABILITA) values ('3','0','appag','Utente amministratore APPAG','DIPENDENTE_CAA');
Insert into A4GT_PROFILO (ID,VERSIONE,IDENTIFICATIVO,DESCRIZIONE, RESPONSABILITA) values ('4','0','caa','Utente CAA','DIPENDENTE_CAA');
Insert into A4GT_PROFILO (ID,VERSIONE,IDENTIFICATIVO,DESCRIZIONE, RESPONSABILITA) values ('5','0','azienda','Utente azienda agricola','TITOLARE_AZIENDA_AGRICOLA');
Insert into A4GT_PROFILO (ID,VERSIONE,IDENTIFICATIVO,DESCRIZIONE, RESPONSABILITA) values ('6','0','backoffice','Profilo per accesso alle funzionalita GIS da parte del backoffice','DIPENDENTE_PAT');
Insert into A4GT_PROFILO (ID,VERSIONE,IDENTIFICATIVO,DESCRIZIONE, RESPONSABILITA) values ('7','0','viticolo','Profilo per accesso alle funzionalita per il personale Viticolo','DIPENDENTE_PAT');

--Anagrafica utenti
insert into a4gt_utente (ID,VERSIONE,IDENTIFICATIVO,CODICE_FISCALE)  values ('8','0','it417@itad.infotn.it', 'FRSLBT76H42E625A');
insert into a4gt_utente (ID,VERSIONE,IDENTIFICATIVO,CODICE_FISCALE)  values ('9','0','FRSLBT76H42E625Z','FRSLBT76H42E625Z');
insert into a4gt_utente (ID, VERSIONE, IDENTIFICATIVO, CODICE_FISCALE) values ('1', '0', 'UTENTECAA', 'UTENTECAA');
insert into a4gt_utente (ID, VERSIONE, IDENTIFICATIVO, CODICE_FISCALE) values ('2', '0', 'UTENTEAPPAG', 'UTENTEAPPAG');
insert into a4gt_utente (ID, VERSIONE, IDENTIFICATIVO, CODICE_FISCALE) values ('3', '0', 'UTENTEAZIENDA', 'UTENTEAZIENDA');
insert into a4gt_utente (ID, VERSIONE, IDENTIFICATIVO, CODICE_FISCALE) values ('4', '0', 'UTENTEADMIN', 'UTENTEADMIN');
insert into a4gt_utente (ID, VERSIONE, IDENTIFICATIVO, CODICE_FISCALE) values ('5', '0', 'TRRCST78B08C794X', 'TRRCST78B08C794X');
insert into a4gt_utente (ID, VERSIONE, IDENTIFICATIVO, CODICE_FISCALE) values ('6', '0', 'TRRRNZ56R23F837A', 'TRRRNZ56R23F837A');
insert into a4gt_utente (ID, VERSIONE, IDENTIFICATIVO, CODICE_FISCALE) values ('10', '0', 'BACKOFFICE', 'BACKOFFICE');
insert into a4gt_utente (ID, VERSIONE, IDENTIFICATIVO, CODICE_FISCALE) values ('11', '0', 'VITICOLO', 'VITICOLO');


--Associazione utente-profilo
insert into a4gr_utente_profilo (ID_UTENTE,ID_PROFILO) values (8, 2);
insert into a4gr_utente_profilo (ID_UTENTE,ID_PROFILO) values (9, 3);
insert into a4gr_utente_profilo (ID_UTENTE,ID_PROFILO) values (1, 4);
insert into a4gr_utente_profilo (ID_UTENTE,ID_PROFILO) values (2, 3);
insert into a4gr_utente_profilo (ID_UTENTE,ID_PROFILO) values (3, 5);
insert into a4gr_utente_profilo (ID_UTENTE,ID_PROFILO) values (4, 2);
insert into a4gr_utente_profilo (ID_UTENTE,ID_PROFILO) values (5, 5);
insert into a4gr_utente_profilo (ID_UTENTE,ID_PROFILO) values (10, 6);
insert into a4gr_utente_profilo (ID_UTENTE,ID_PROFILO) values (11, 7);

-- Anagrafica enti

insert into a4gt_ente (ID, VERSIONE, IDENTIFICATIVO, DESCRIZIONE, CAA) values (424, 0, 4, 'CAA COLDIRETTI DEL TRENTINO - 003', 'COLDIRETTI');
insert into a4gt_ente (ID, VERSIONE, IDENTIFICATIVO, DESCRIZIONE, CAA) values (495, 0, 5, 'CAA COLDIRETTI DEL TRENTINO - 004', 'COLDIRETTI');
insert into a4gt_ente (ID, VERSIONE, IDENTIFICATIVO, DESCRIZIONE, CAA) values (496, 0, 7, 'CAA COLDIRETTI DEL TRENTINO - 006', 'COLDIRETTI');
insert into a4gt_ente (ID, VERSIONE, IDENTIFICATIVO, DESCRIZIONE, CAA) values (497, 0, 12, 'CAA COLDIRETTI DEL TRENTINO - 011', 'COLDIRETTI');
insert into a4gt_ente (ID, VERSIONE, IDENTIFICATIVO, DESCRIZIONE, CAA) values (422, 0, 18, 'CAA CIA - TRENTO - 001', 'CIA');
insert into a4gt_ente (ID, VERSIONE, IDENTIFICATIVO, DESCRIZIONE, CAA) values (499, 0, 19, 'CAA CIA - CLES - 002', 'CIA');
insert into a4gt_ente (ID, VERSIONE, IDENTIFICATIVO, DESCRIZIONE, CAA) values (423, 0, 20, 'CAA ATS - 001 - TRENTO', 'ATS');

-- Associazione utenti-enti (utente caa)
insert into a4gr_utente_ente (ID_UTENTE, ID_ENTE) values (1, 423);
insert into a4gr_utente_ente (ID_UTENTE, ID_ENTE) values (1, 422);
insert into a4gr_utente_ente (ID_UTENTE, ID_ENTE) values (1, 424);

--Anagrafica aziende agricole di sviluppo
Insert into A4GT_AZIENDA_AGRICOLA (ID,VERSIONE,CUAA) values ('31','0','01833620220');
Insert into A4GT_AZIENDA_AGRICOLA (ID,VERSIONE,CUAA) values ('21','0','02388670222');
Insert into A4GT_AZIENDA_AGRICOLA (ID,VERSIONE,CUAA) values ('24','0','BRDMRN71L08C794F');
Insert into A4GT_AZIENDA_AGRICOLA (ID,VERSIONE,CUAA) values ('29','0','LBRMRA57T19L378D');
Insert into A4GT_AZIENDA_AGRICOLA (ID,VERSIONE,CUAA) values ('16','0','TRRCST78B08C794X');
Insert into A4GT_AZIENDA_AGRICOLA (ID,VERSIONE,CUAA) values ('28','0','BZZPLA64A31A178M');

-- Associazione utenti-aziende
insert into a4gr_utente_azienda (ID, VERSIONE, ID_UTENTE, CUAA, ID_CARICA, DATA_AGGIORNAMENTO) values (1, 0, 5, 'TRRCST78B08C794X', 1, to_date('21-09-2018', 'dd-mm-yyyy'));
insert into a4gr_utente_azienda (ID, VERSIONE, ID_UTENTE, CUAA, ID_CARICA, DATA_AGGIORNAMENTO) values (2, 0, 5, '01833620220', 1, to_date('21-09-2018', 'dd-mm-yyyy'));

-- Funzioni
insert into a4gt_funzione (ID, VERSIONE, IDENTIFICATIVO, DESCRIZIONE) values (261, 0, 'RICERCAFASCICOLOFILTROENTE', 'Ricerca fascicoli con filtro sull''ente');
insert into a4gt_funzione (ID, VERSIONE, IDENTIFICATIVO, DESCRIZIONE) values (262, 0, 'RICERCAFASCICOLONOFILTRO', 'Ricerca fascicoli senza filtri');
insert into a4gt_funzione (ID, VERSIONE, IDENTIFICATIVO, DESCRIZIONE) values (399, 0, 'RICERCAFASCICOLOFILTROUTENTE', 'Ricerca fascicoli con filtro sull''utente');

-- FunzioniProfili

insert into a4gr_profilo_funzione (ID, VERSIONE, ID_PROFILO, ID_FUNZIONE)values (265, 0, 4, 261);
insert into a4gr_profilo_funzione (ID, VERSIONE, ID_PROFILO, ID_FUNZIONE)values (266, 0, 3, 262);
insert into a4gr_profilo_funzione (ID, VERSIONE, ID_PROFILO, ID_FUNZIONE)values (400, 0, 5, 399);
insert into a4gr_profilo_funzione (ID, VERSIONE, ID_PROFILO, ID_FUNZIONE)values (500, 0, 2, 262);

------------ Domanda registrazione nuovo utente
insert into a4gt_domanda_registrazione (id, versione, a4g, ags, codice_fiscale, cognome, email, identificativo_utente, nome, responsabilita, srt, stato, telefono)
values (100, 0, 1, 0, 'TRRCST78B08C794X', 'Torresani', 'tc@gmail.com', 'TRRCST78B08C794X', 'Cristian', '{"test" : "Betty"}', 0, 'PROTOCOLLATA', '00');

insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,A4G,SRT,AGS, RESPONSABILITA)
values ('294','5','BZZLBN21M45A178J','IN_COMPILAZIONE','BZZLBN21M45A178J','Albina','Beozzo','beozzo.aaa@gmail.com','9999','1','1','1', '{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":null,"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","sedi":[{"descrizione":"AGRICOLTURA TRENTINA SERVIZI SRL","identificativo":53},{"descrizione":"APPAG - PROVINCIA AUTONOMA DI TRENTO","identificativo":1},{"descrizione":"CENTRO ASSISTENZA COLDIRETTI DEL TRENTINO SRL","identificativo":50},{"descrizione":"UFFICIO DI TEST","identificativo":21}],"allegato":null}]}');

insert into A4GT_ALLEGATO_RESPONSABILITA (ID,VERSIONE,ID_DOMANDA_REGISTRAZIONE,ID_RESPONSABILITA,COD_RESPONSABILITA,DT_INSERIMENTO)
values ('295','0','294','1','CAA',to_date('05-11-2018','DD-MM-YYYY'));

-- Domanda modifica profilo
Insert into A4GT_ENTE (ID,VERSIONE,IDENTIFICATIVO,DESCRIZIONE,CAA) values ('1033','0','13','CAA ACLI - TRENTO - 001','ACLI');
Insert into A4GT_ENTE (ID,VERSIONE,IDENTIFICATIVO,DESCRIZIONE,CAA) values ('1034','0','103','CAA CIA - BORGO VALSUGANA - 003', 'CIA');

Insert into A4GT_UTENTE (ID,VERSIONE,IDENTIFICATIVO,CODICE_FISCALE,EMAIL,TELEFONO) values ('522','0','TRRRNZ56R23F837Z','TRRRNZ56R23F837Z','a.b@gmail.com','00');
Insert into A4GT_PERSONA (ID,VERSIONE,CODICE_FISCALE,NOME,COGNOME) values ('803','0','TRRRNZ56R23F837Z','Renzo','Torr');
Insert into A4GR_UTENTE_PROFILO (ID_UTENTE,ID_PROFILO) values ('522','3');
Insert into A4GR_UTENTE_PROFILO (ID_UTENTE,ID_PROFILO) values ('522','4');
Insert into A4GR_UTENTE_PROFILO (ID_UTENTE,ID_PROFILO) values ('522','5');
Insert into A4GR_UTENTE_ENTE (ID_UTENTE,ID_ENTE) values ('522','1033');
Insert into A4GR_UTENTE_AZIENDA (ID,VERSIONE,ID_UTENTE,CUAA,ID_CARICA,DATA_AGGIORNAMENTO) values ('842','0','522','TRRCLD69P03C794A','1',to_date('10-12-2018','DD-MM-YYYY')); 

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS) values ('799','18','TRRRNZ56R23F837Z','PROTOCOLLATA','TRRRNZ56R23F837Z','retwer','sdfwe','a.b@gmail.com','00','{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":[{"cuaa":"00237830229"}],"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","elencoCaa":null,"sedi":[{"descrizione":"CAA CIA - BORGO VALSUGANA - 003","identificativo":103}],"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}],"allegato":null}]}','1','0','0');

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS) values ('801','0','prova','PROTOCOLLATA','prova','retwer','sdfwes','a.b@gmail.com','00','{"responsabilitaTitolare":[{"cuaa":"TRRRNZ56R23F837Z"}],"responsabilitaLegaleRappresentante":[{"cuaa":"00237830229"}],"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","elencoCaa":null,"sedi":[{"descrizione":"CAA CIA - BORGO VALSUGANA - 003","identificativo":103}],"allegato":null}],"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}]}','1','0','0');

Insert into A4GT_UTENTE (ID,VERSIONE,IDENTIFICATIVO,CODICE_FISCALE,EMAIL,TELEFONO) values ('523','0','TRRRNZ56R23F837E','TRRRNZ56R23F837A','a.b@gmail.com','00');
Insert into A4GT_PERSONA (ID,VERSIONE,CODICE_FISCALE,NOME,COGNOME) values ('804','0','TRRRNZ56R23F837E','Renzo','Torr');
Insert into A4GR_UTENTE_PROFILO (ID_UTENTE,ID_PROFILO) values ('523','3');
Insert into A4GR_UTENTE_PROFILO (ID_UTENTE,ID_PROFILO) values ('523','4');
Insert into A4GR_UTENTE_PROFILO (ID_UTENTE,ID_PROFILO) values ('523','5');
Insert into A4GR_UTENTE_ENTE (ID_UTENTE,ID_ENTE) values ('523','1033');
Insert into A4GR_UTENTE_AZIENDA (ID,VERSIONE,ID_UTENTE,CUAA,ID_CARICA,DATA_AGGIORNAMENTO) values ('843','0','523','TRRRNZ56R23F837E','1',to_date('10-12-2018','DD-MM-YYYY')); 

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS) values ('802','18','TRRRNZ56R23F837E','PROTOCOLLATA','TRRRNZ56R23F837E','retwer','sdfwe','a.b@gmail.com','00',
'{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":[{"cuaa":"00237830229"}],"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","elencoCaa":null,"sedi":[{"descrizione":"CAA CIA - BORGO VALSUGANA - 003","identificativo":103}],"allegato":null}],"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}]}','0','0','0');


Insert into A4GT_UTENTE (ID,VERSIONE,IDENTIFICATIVO,CODICE_FISCALE,EMAIL,TELEFONO) values ('524','0','TRRRNZ56R23F8372','TRRRNZ56R23F8372','a.b@gmail.com','00');
Insert into A4GT_PERSONA (ID,VERSIONE,CODICE_FISCALE,NOME,COGNOME) values ('805','0','TRRRNZ56R23F8372','Renzo','Torr');
Insert into A4GR_UTENTE_PROFILO (ID_UTENTE,ID_PROFILO) values ('524','3');
Insert into A4GR_UTENTE_PROFILO (ID_UTENTE,ID_PROFILO) values ('524','4');
Insert into A4GR_UTENTE_PROFILO (ID_UTENTE,ID_PROFILO) values ('524','5');
Insert into A4GR_UTENTE_ENTE (ID_UTENTE,ID_ENTE) values ('524','1033');
Insert into A4GR_UTENTE_AZIENDA (ID,VERSIONE,ID_UTENTE,CUAA,ID_CARICA,DATA_AGGIORNAMENTO) values ('844','0','524','TRRRNZ56R23F8372','1',to_date('10-12-2018','DD-MM-YYYY')); 

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS) 
values ('803','18','TRRRNZ56R23F8372','PROTOCOLLATA','TRRRNZ56R23F8372','retwer','sdfwe','a.b@gmail.com','00','{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":[{"cuaa":"00237830229"}],"responsabilitaCaa":null,"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}]}','1','0','0');

Insert into A4GT_UTENTE (ID,VERSIONE,IDENTIFICATIVO,CODICE_FISCALE,EMAIL,TELEFONO) values ('525','0','TRRRNZ56R23F8373','TRRRNZ56R23F8373','a.b@gmail.com','00');
Insert into A4GT_PERSONA (ID,VERSIONE,CODICE_FISCALE,NOME,COGNOME) values ('806','0','TRRRNZ56R23F8373','Renzo','Torr');
Insert into A4GR_UTENTE_PROFILO (ID_UTENTE,ID_PROFILO) values ('525','3');
Insert into A4GR_UTENTE_PROFILO (ID_UTENTE,ID_PROFILO) values ('525','4');
Insert into A4GR_UTENTE_PROFILO (ID_UTENTE,ID_PROFILO) values ('525','5');
Insert into A4GR_UTENTE_ENTE (ID_UTENTE,ID_ENTE) values ('525','1033');
Insert into A4GR_UTENTE_AZIENDA (ID,VERSIONE,ID_UTENTE,CUAA,ID_CARICA,DATA_AGGIORNAMENTO) values ('845','0','525','TRRRNZ56R23F8373','1',to_date('10-12-2018','DD-MM-YYYY')); 
Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS) 
values ('804','18','TRRRNZ56R23F8373','PROTOCOLLATA','TRRRNZ56R23F8373','retwer','sdfwe','a.b@gmail.com','00','{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":null,"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","elencoCaa":null,"sedi":[{"descrizione":"CAA CIA - BORGO VALSUGANA - 003","identificativo":103}],"allegato":null}],"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}]}','1','0','0');

-- Accettazione informativa generale
insert into A4GT_PERSONA (ID,VERSIONE,CODICE_FISCALE, NOME, COGNOME) 
values (249,'0','BRDMRN71L08C794F', 'MRN', 'BRD');

insert into A4GT_PRIVACY_PERSONA (ID,VERSIONE,ID_PERSONA, TIPO_INFORMATIVA, DT_ACCETTAZIONE, DT_PROTOCOLLAZIONE, NR_PROTOCOLLO) 
values (200,'0',249, 'GENERALE', sysdate - 1, sysdate -1, '001');

insert into A4GT_PERSONA (ID,VERSIONE,CODICE_FISCALE, NOME, COGNOME) 
values (250,'0','TRRCST78B08C794X', 'Cristian', 'Torresani');

insert into A4GT_PERSONA (ID,VERSIONE,CODICE_FISCALE, NOME, COGNOME) 
values (251,'0','VNNTTR80A01L378B', 'Ettore', 'Avannotti');

insert into A4GT_PERSONA (ID,VERSIONE,CODICE_FISCALE, NOME, COGNOME) 
values (252,'0','d', 'MRN', 'BRD');

insert into A4GT_PRIVACY_PERSONA (ID,VERSIONE,ID_PERSONA, TIPO_INFORMATIVA, DT_ACCETTAZIONE, DT_PROTOCOLLAZIONE, NR_PROTOCOLLO) 
values (253,'0',252, 'GENERALE', sysdate - 1, sysdate -1, '001');

-- Distributori
Insert into A4GT_DISTRIBUTORE (ID,VERSIONE,DENOMINAZIONE_AZIENDA,COMUNE_DISTRIBUTORE,PROVINCIA,INDIRIZZO,DATA_INIZIO,DATA_FINE)
values ('1','0','Agip','Trento 1','Trento', 'Via Roma 5',to_date('10-12-2018','DD-MM-YYYY'),null);
Insert into A4GT_DISTRIBUTORE (ID,VERSIONE,DENOMINAZIONE_AZIENDA,COMUNE_DISTRIBUTORE,PROVINCIA,INDIRIZZO,DATA_INIZIO,DATA_FINE)
values ('2','0','Agip','Trento 2','Lavis', 'Via Milano 8',to_date('10-12-2018','DD-MM-YYYY'),null);


--------------- Ruoli
INSERT INTO A4GD_RUOLO (IDENTIFICATIVO, DESCRIZIONE) values ('a4gfascicolo.fascicolo.ricerca.tutti','Possibilita di visionare tutti i fascicoli ed eseguire la ricerca senza filtri');
INSERT INTO A4GD_RUOLO (IDENTIFICATIVO, DESCRIZIONE) values ('a4gfascicolo.fascicolo.ricerca.ente','Possibilita di visionare i fascicoli dell''ente');
INSERT INTO A4GD_RUOLO (IDENTIFICATIVO, DESCRIZIONE) values ('a4gfascicolo.fascicolo.ricerca.utente','Possibilita di visionare solo i fascicoli associati all''utente');
INSERT INTO A4GD_RUOLO (IDENTIFICATIVO, DESCRIZIONE) values ('a4gutente.utenti.profili.visualizza','Possibilita di visionare in ricerca i profili degli utenti');


INSERT INTO A4GR_PROFILO_RUOLO(ID, ID_PROFILO, ID_RUOLO)
	VALUES (1, (SELECT ID FROM A4GT_PROFILO WHERE IDENTIFICATIVO = 'caa'), 'a4gfascicolo.fascicolo.ricerca.ente');

INSERT INTO A4GR_PROFILO_RUOLO(ID, ID_PROFILO, ID_RUOLO)
	VALUES (2, (SELECT ID FROM A4GT_PROFILO WHERE IDENTIFICATIVO = 'appag'), 'a4gfascicolo.fascicolo.ricerca.tutti');

INSERT INTO A4GR_PROFILO_RUOLO(ID, ID_PROFILO, ID_RUOLO)
	VALUES (3, (SELECT ID FROM A4GT_PROFILO WHERE IDENTIFICATIVO = 'amministratore'), 'a4gfascicolo.fascicolo.ricerca.tutti');

INSERT INTO A4GR_PROFILO_RUOLO(ID, ID_PROFILO, ID_RUOLO)
	VALUES (4, (SELECT ID FROM A4GT_PROFILO WHERE IDENTIFICATIVO = 'azienda'), 'a4gfascicolo.fascicolo.ricerca.utente');

INSERT INTO A4GR_PROFILO_RUOLO(ID, ID_PROFILO, ID_RUOLO)
	VALUES (5, (SELECT ID FROM A4GT_PROFILO WHERE IDENTIFICATIVO = 'backoffice'), 'a4gutente.utenti.profili.visualizza');

INSERT INTO A4GR_PROFILO_RUOLO(ID, ID_PROFILO, ID_RUOLO)
	VALUES (6, (SELECT ID FROM A4GT_PROFILO WHERE IDENTIFICATIVO = 'viticolo'), 'a4gutente.utenti.profili.visualizza');


Insert into A4GT_LOAD_UTENTE_CAA (COGNOME,NOME,CODICE_FISCALE,EMAIL,TELEFONO,CAA,UFFICIO,RESPONSABILE,NR_FASCICOLO_PRIVACY,NR_PROTOCOLLO_PRIVACY,CARICATO) values ('a','a','a','a','a','COLDIRETTI','CAA COLDIRETTI DEL TRENTINO - 003','aa','1','1',null);
Insert into A4GT_LOAD_UTENTE_CAA (COGNOME,NOME,CODICE_FISCALE,EMAIL,TELEFONO,CAA,UFFICIO,RESPONSABILE,NR_FASCICOLO_PRIVACY,NR_PROTOCOLLO_PRIVACY,CARICATO) values ('a','a','a','a','a','COLDIRETTI','CAA COLDIRETTI DEL TRENTINO - 004','aa','1','1',null);
Insert into A4GT_LOAD_UTENTE_CAA (COGNOME,NOME,CODICE_FISCALE,EMAIL,TELEFONO,CAA,UFFICIO,RESPONSABILE,NR_FASCICOLO_PRIVACY,NR_PROTOCOLLO_PRIVACY,CARICATO) values ('a','a','a','a','a','ACLI','CAA CIA - TRENTO - 001','bb','1','1',null);
Insert into A4GT_LOAD_UTENTE_CAA (COGNOME,NOME,CODICE_FISCALE,EMAIL,TELEFONO,CAA,UFFICIO,RESPONSABILE,NR_FASCICOLO_PRIVACY,NR_PROTOCOLLO_PRIVACY,CARICATO) values ('b','b','b','b','b','ACLI','CAA CIA - TRENTO - 001','aa','1','1',null);
Insert into A4GT_LOAD_UTENTE_CAA (COGNOME,NOME,CODICE_FISCALE,EMAIL,TELEFONO,CAA,UFFICIO,RESPONSABILE,NR_FASCICOLO_PRIVACY,NR_PROTOCOLLO_PRIVACY,CARICATO) values ('b','b','b','b','b','COLDIRETTI','CAA COLDIRETTI DEL TRENTINO - 004','aa','1','1',null);
Insert into A4GT_LOAD_UTENTE_CAA (COGNOME,NOME,CODICE_FISCALE,EMAIL,TELEFONO,CAA,UFFICIO,RESPONSABILE,NR_FASCICOLO_PRIVACY,NR_PROTOCOLLO_PRIVACY,CARICATO) values ('b','b','b','b','b','COLDIRETTI','CAA COLDIRETTI DEL TRENTINO - 003','aa','1','1',null);
Insert into A4GT_LOAD_UTENTE_CAA (COGNOME,NOME,CODICE_FISCALE,EMAIL,TELEFONO,CAA,UFFICIO,RESPONSABILE,NR_FASCICOLO_PRIVACY,NR_PROTOCOLLO_PRIVACY,CARICATO) values ('c','c','c','c','c','COLDIRETTI','CAA COLDIRETTI DEL TRENTINO - 003','aa','1','1','1');

Insert into A4GT_LOAD_UTENTE_CAA (COGNOME,NOME,CODICE_FISCALE,EMAIL,TELEFONO,CAA,UFFICIO,RESPONSABILE,NR_FASCICOLO_PRIVACY,NR_PROTOCOLLO_PRIVACY,CARICATO) values ('d','d','d','d','b','COLDIRETTI','CAA COLDIRETTI DEL TRENTINO - 004','aa','1','1',null);
Insert into A4GT_LOAD_UTENTE_CAA (COGNOME,NOME,CODICE_FISCALE,EMAIL,TELEFONO,CAA,UFFICIO,RESPONSABILE,NR_FASCICOLO_PRIVACY,NR_PROTOCOLLO_PRIVACY,CARICATO) values ('TRRRNZ56R23FAAA','TRRRNZ56R23FAAA','TRRRNZ56R23FAAA','TRRRNZ56R23FAAA','c','COLDIRETTI','CAA COLDIRETTI DEL TRENTINO - 003','aa','1','1',null);

------------ Domande registrazione nuovo utente - test protocollazione
insert into a4gt_domanda_registrazione (id, versione, a4g, ags, codice_fiscale, cognome, email, identificativo_utente, nome, responsabilita, srt, stato, telefono, documento, tipo_domanda_registrazione) 
values (200, 0, 1, 0, 'TRRCST78B08C794X', 'Torresani', 'tc@gmail.com', 'TRRCST78B08C794X', 'Cristian', '{"responsabilitaTitolare":[{"cuaa":"TRRCST78B08C794X","denominazione":"TORRESANI CRISTIAN"}],"responsabilitaLegaleRappresentante":null,"responsabilitaCaa":null}', 0, 'IN_COMPILAZIONE', '00', FILE_READ('src/test/resources/domandaRegistrazioneUtente/uploadFileTest/TRRCST78B08C794X.pdf'), 'COMPLETA');


Insert into A4GT_FIRMA_DOMANDA_REG
   (ID, VERSIONE, ID_DOMANDA, XML, PDF)
 Values
   (1, 0, 200, FILE_READ('src/test/resources/domandaRegistrazioneUtente/uploadFileTest/firmaMRTLNZ.xml'), FILE_READ('src/test/resources/domandaRegistrazioneUtente/uploadFileTest/firmaMRTLNZ.pdf'));


------------ Domande registrazione nuovo utente - test registra nuova domanda
insert into a4gt_domanda_registrazione (id, versione, a4g, ags, codice_fiscale, cognome, email, identificativo_utente, nome, responsabilita, srt, stato, telefono, documento) 
values (201, 0, 1, 0, 'TRRCST78B08C7941', 'Torresani', 'tc@gmail.com', 'TRRCST78B08C7941', 'Cristian', '{"responsabilitaTitolare":[{"cuaa":"TRRCST78B08C7941","denominazione":"TORRESANI CRISTIAN"}],"responsabilitaLegaleRappresentante":null,"responsabilitaCaa":null}', 0, 'IN_COMPILAZIONE', '00', FILE_READ('src/test/resources/domandaRegistrazioneUtente/uploadFileTest/TRRCST78B08C794X.pdf'));


Insert into A4GT_FIRMA_DOMANDA_REG
   (ID, VERSIONE, ID_DOMANDA, XML, PDF)
 Values
   (2, 0, 201, FILE_READ('src/test/resources/domandaRegistrazioneUtente/uploadFileTest/firmaMRTLNZ.xml'), FILE_READ('src/test/resources/domandaRegistrazioneUtente/uploadFileTest/firmaMRTLNZ.pdf'));

Insert into A4GT_UTENTE (ID,VERSIONE,IDENTIFICATIVO,CODICE_FISCALE,EMAIL,TELEFONO) values ('900','0','BNMDNL78B66L378Z','BNMDNL78B66L378Z','a.b@gmail.com','00');
Insert into A4GR_UTENTE_PROFILO (ID_UTENTE,ID_PROFILO) values ('900','4');
Insert into A4GR_UTENTE_ENTE (ID_UTENTE,ID_ENTE) values ('900','1033');

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS) 
values ('900','1','BNMDNL78B66L378Z','PROTOCOLLATA','BNMDNL78B66L378Z','retwer','sdfwe','a.b@gmail.com','00','{"responsabilitaTitolare":[{"cuaa":"BNMDNL78B66L378Z","denominazione":"BONOMI DANIELA"}],"responsabilitaLegaleRappresentante":null,"responsabilitaCaa":null,"responsabilitaPat":null}','1','1','1');



Insert into A4GT_UTENTE (ID,VERSIONE,IDENTIFICATIVO,CODICE_FISCALE,EMAIL,TELEFONO) values ('901','0','UTENTE_AZIENDA','UTENTE_AZIENDA','a.b@gmail.com','00');
Insert into A4GR_UTENTE_PROFILO (ID_UTENTE,ID_PROFILO) values ('901','5');
Insert into A4GR_UTENTE_AZIENDA (ID,VERSIONE,ID_UTENTE,CUAA,ID_CARICA,DATA_AGGIORNAMENTO) values ('901','0','901','UTENTE_AZIENDA','1',to_date('10-12-2018','DD-MM-YYYY')); 

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS) 
values ('901','1','UTENTE_AZIENDA','PROTOCOLLATA','UTENTE_AZIENDA','retwer','sdfwe','a.b@gmail.com','00','{"responsabilitaTitolare":[{"cuaa":"UTENTE_AZIENDA","denominazione":"TEST"}],"responsabilitaLegaleRappresentante":null,"responsabilitaCaa":null,"responsabilitaPat":null}','1','1','1');

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS, TIPO_DOMANDA_REGISTRAZIONE) 
values ('999','1','UTENTE_AZIENDA','PROTOCOLLATA','UTENTE_AZIENDA','retwer','sdfwe','a.b@gmail.com','00','{"responsabilitaTitolare":[{"cuaa":"UTENTE_AZIENDA","denominazione":"TEST"}],"responsabilitaLegaleRappresentante":null,"responsabilitaCaa":null,"responsabilitaPat":null}','1','1','1', 'COMPLETA');
Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS) 
values ('1000','1','UTENTE_AZIENDA','IN_LAVORAZIONE','UTENTE_AZIENDA','retwer','sdfwe','a.b@gmail.com','00','{"responsabilitaTitolare":[{"cuaa":"UTENTE_AZIENDA","denominazione":"TEST"}],"responsabilitaLegaleRappresentante":null,"responsabilitaCaa":null,"responsabilitaPat":null}','1','1','1');

Insert into A4GT_PERSONA (ID,VERSIONE,CODICE_FISCALE,NOME,COGNOME) values ('703','0','NSCMSM74A22H612G','M','N');
