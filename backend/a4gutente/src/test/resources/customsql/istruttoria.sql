Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS)
values (333444987,0,'TRRRNZ56R23F999Z','IN_LAVORAZIONE','TRRRNZ56R23F999Z','alex','fedex','alexfedez@gmail.com','3284455968',
'{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":[{"cuaa":"00237830229"}],"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","elencoCaa":null,"sedi":[{"descrizione":"CAA CIA - BORGO VALSUGANA - 003","identificativo":103}],"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}],"allegato":null}]}',
'1','0','0');

Insert into A4GT_ISTRUTTORIA (ID,VERSIONE,VARIAZIONE_RICHIESTA,TESTO_COMUNICAZIONE,MOTIVAZIONE_RIFIUTO,ID_DOMANDA)
values (333444988,0,'variazione richiesta','testo comunicazione','motivazione rifiuto',333444987);




Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS)
values (333444989,0,'KKKRNZ56R23F999Z','IN_LAVORAZIONE','KKKRNZ56R23F999Z','gianluca','fedex','gianlucafedez@gmail.com','3284412968',
'{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":[{"cuaa":"00237830229"}],"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","elencoCaa":null,"sedi":[{"descrizione":"CAA CIA - BORGO VALSUGANA - 003","identificativo":103}],"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}],"allegato":null}]}',
'1','0','0');

Insert into A4GT_ISTRUTTORIA (ID,VERSIONE,VARIAZIONE_RICHIESTA,TESTO_COMUNICAZIONE,MOTIVAZIONE_RIFIUTO,ID_DOMANDA)
values (333444990,0,'variazione richiesta','testo comunicazione','motivazione rifiuto',333444989);

Insert into A4GT_PROFILO (ID,VERSIONE,IDENTIFICATIVO,DESCRIZIONE, RESPONSABILITA)
values (333444991,0,'identificativo xxxxxxxx','Descrizione yyyyyyyyy','DIPENDENTE_TNDIGIT');

Insert into A4GR_PROFILO_ISTRUTTORIA (ID_ISTRUTTORIA,ID_PROFILO)
values (333444990,333444991);

insert into A4GT_ENTE (ID, VERSIONE, IDENTIFICATIVO, DESCRIZIONE, CAA)
values (333444992, 0, 9999, 'DESCRIZIONE XXXXX', 'COLDIRETTI');

Insert into A4GR_ENTE_ISTRUTTORIA (ID_ISTRUTTORIA,ID_ENTE)
values (333444990, 333444992);




Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS)
values (333444993,0,'TTTRNZ56R23F999Z','IN_LAVORAZIONE','TTTRNZ56R23F999Z','daniele','rossi','danielerossi@gmail.com','3184412968',
'{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":[{"cuaa":"00237830229"}],"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","elencoCaa":null,"sedi":[{"descrizione":"CAA CIA - BORGO VALSUGANA - 003","identificativo":103}],"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}],"allegato":null}]}',
'1','0','0');

insert into A4GT_ENTE (ID, VERSIONE, IDENTIFICATIVO, DESCRIZIONE, CAA)
values (333444993, 0, 9999, 'DESCRIZIONE KKKKKK', 'COLDIRETTI');

Insert into A4GT_PROFILO (ID,VERSIONE,IDENTIFICATIVO,DESCRIZIONE, RESPONSABILITA)
values (333444994,0,'identificativo rrrrrrr','Descrizione kkkkkkkkkk','DIPENDENTE_TNDIGIT');





Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS)
values (333444995,0,'TRRRNZ56R23F959K','PROTOCOLLATA','TRRRNZ56R23F959K','giovanni','ziller','gioziller@gmail.com','3389090150',
'{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":[{"cuaa":"00237830229"}],"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","elencoCaa":null,"sedi":[{"descrizione":"CAA CIA - BORGO VALSUGANA - 003","identificativo":103}],"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}],"allegato":null}]}',
'1','0','0');






Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS)
values (333444996,0,'ABCRNZ56R23F959K','PROTOCOLLATA','ABCRNZ56R23F959K','alessandro','ziller','alessandroziller@gmail.com','3381190150',
'{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":[{"cuaa":"00237830229"}],"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","elencoCaa":null,"sedi":[{"descrizione":"CAA CIA - BORGO VALSUGANA - 003","identificativo":103}],"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}],"allegato":null}]}',
'1','0','0');

Insert into A4GT_ISTRUTTORIA (ID,VERSIONE,VARIAZIONE_RICHIESTA,TESTO_COMUNICAZIONE,MOTIVAZIONE_RIFIUTO,ID_DOMANDA)
values (333444997,0,'variazione richiesta kkk','testo comunicazione oooo','motivazione rifiuto ssss',333444996);
