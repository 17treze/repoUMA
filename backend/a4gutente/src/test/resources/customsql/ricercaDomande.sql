DELETE FROM A4GT_FIRMA_DOMANDA_REG;
DELETE FROM A4GT_ALLEGATO_RESPONSABILITA;
DELETE FROM A4GT_DOMANDA_REGISTRAZIONE;

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS,ID_PROTOCOLLO, DT_PROTOCOLLAZIONE, TIPO_DOMANDA_REGISTRAZIONE)
values (1000000,'0','TRRRNZ56R23F837Z','PROTOCOLLATA','TRRRNZ56R23F837Z','fabio','rossi','fabiorossi@gmail.com','3284455667',
'{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":[{"cuaa":"00237830229"}],"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","elencoCaa":null,"sedi":[{"descrizione":"CAA CIA - BORGO VALSUGANA - 003","identificativo":103}],"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}],"allegato":null}]}',
'1','0','0','PAT-45', to_date('08-01-2019', 'dd-mm-yyyy'), 'COMPLETA');

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS,ID_PROTOCOLLO, DT_PROTOCOLLAZIONE, TIPO_DOMANDA_REGISTRAZIONE)
values (1000001,'1','STRFBA7812345678','PROTOCOLLATA','STRFBA7812345678','giovanni','cometa','giovannicometa@gmail.com','3284455669',
'{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":[{"cuaa":"00237830229"}],"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","elencoCaa":null,"sedi":[{"descrizione":"CAA CIA - BORGO VALSUGANA - 003","identificativo":103}],"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}],"allegato":null}]}',
'1','0','0','PAT-46', to_date('08-01-2019', 'dd-mm-yyyy'), 'COMPLETA');

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS,ID_PROTOCOLLO, DT_PROTOCOLLAZIONE, TIPO_DOMANDA_REGISTRAZIONE)
values (1000002,'1','STRFBA7887654321','PROTOCOLLATA','STRFBA7887654321','roberto','abate','robertoabate@gmail.com','3385656797',
'{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":[{"cuaa":"00237830229"}],"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","elencoCaa":null,"sedi":[{"descrizione":"CAA CIA - BORGO VALSUGANA - 003","identificativo":103}],"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}],"allegato":null}]}',
'1','0','0','PAT-47', to_date('18-01-2019', 'dd-mm-yyyy'), 'COMPLETA');

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS,ID_PROTOCOLLO, DT_PROTOCOLLAZIONE, TIPO_DOMANDA_REGISTRAZIONE)
values (1000003,'4','GDAFBA7887654321','IN_LAVORAZIONE','GDAFBA7887654321','alessandro','strada','alex@gmail.com','3367909123',
'{"responsabilitaTitolare":[{"cuaa":"DLLGNS64A61L378G"}],"responsabilitaLegaleRappresentante":null,"responsabilitaCaa":null}',
'1','0','0','PAT-48', to_date('18-01-2019', 'dd-mm-yyyy'), 'COMPLETA');


Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS,ID_PROTOCOLLO, DT_PROTOCOLLAZIONE, TIPO_DOMANDA_REGISTRAZIONE)
values (1000004,'0','AAABBB11C22D333E','APPROVATA','AAABBB11C22D333E','alessandro','strada','e.f@gmail.com','3367909123',
'{"responsabilitaTitolare":[{"cuaa":"DLLGNS64A61L378G"}],"responsabilitaLegaleRappresentante":null,"responsabilitaCaa":null}',
'1','0','0','PAT-49', NULL, 'COMPLETA');

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS,ID_PROTOCOLLO, DT_PROTOCOLLAZIONE, TIPO_DOMANDA_REGISTRAZIONE)
values (1000005,'0','AAABBB11C22D333E','APPROVATA','AAABBB11C22D333E','alessandro','strada','d.e@gmail.com','3367909123',
'{"responsabilitaTitolare":[{"cuaa":"DLLGNS64A61L378G"}],"responsabilitaLegaleRappresentante":null,"responsabilitaCaa":null}',
'1','0','0','PAT-50', to_date('18-03-2020', 'dd-mm-yyyy'), 'COMPLETA');

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS,ID_PROTOCOLLO, DT_PROTOCOLLAZIONE, TIPO_DOMANDA_REGISTRAZIONE)
values (1000006,'0','AAABBB11C22D333E','PROTOCOLLATA','AAABBB11C22D333E','alessandro','strada','b.c@gmail.com','3367909123',
'{"responsabilitaTitolare":[{"cuaa":"DLLGNS64A61L378G"}],"responsabilitaLegaleRappresentante":null,"responsabilitaCaa":null}',
'1','0','0','PAT-51', to_date('18-03-2018', 'dd-mm-yyyy'), 'COMPLETA');

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS,ID_PROTOCOLLO, DT_PROTOCOLLAZIONE, TIPO_DOMANDA_REGISTRAZIONE)
values (1000007,'0','AAABBB11C22D333E','PROTOCOLLATA','AAABBB11C22D333E','alessandro','strada','a.b@gmail.com','3367909123',
'{"responsabilitaTitolare":[{"cuaa":"DLLGNS64A61L378G"}],"responsabilitaLegaleRappresentante":null,"responsabilitaCaa":null}',
'1','0','0','PAT-52', to_date('18-03-2018', 'dd-mm-yyyy'), 'RIDOTTA_AZIENDA');

Insert into A4GT_ISTRUTTORIA (ID,VERSIONE,VARIAZIONE_RICHIESTA,TESTO_COMUNICAZIONE,MOTIVAZIONE_RIFIUTO,ID_DOMANDA)
values (144432335,0,'variazione richiesta','testo comunicazione','motivazione rifiuto',1000003);

insert into A4GT_ENTE (ID, VERSIONE, IDENTIFICATIVO, DESCRIZIONE, CAA)
values (333444992, 0, 9999, 'DESCRIZIONE XXXXX', 'COLDIRETTI');

Insert into A4GR_ENTE_ISTRUTTORIA (ID_ISTRUTTORIA,ID_ENTE)
values (144432335, 333444992);

Insert into A4GT_PROFILO (ID,VERSIONE,IDENTIFICATIVO,DESCRIZIONE, RESPONSABILITA)
values (144432336,0,'identificativo xxxxxxxx','Descrizione yyyyyyyyy','TITOLARE_AZIENDA_AGRICOLA');

Insert into A4GR_PROFILO_ISTRUTTORIA (ID_ISTRUTTORIA,ID_PROFILO)
values (144432335, 144432336);

