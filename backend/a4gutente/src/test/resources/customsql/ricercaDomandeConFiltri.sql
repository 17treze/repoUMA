DELETE FROM A4GT_FIRMA_DOMANDA_REG;
DELETE FROM A4GT_ALLEGATO_RESPONSABILITA;
DELETE FROM A4GT_DOMANDA_REGISTRAZIONE;


Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS,ID_PROTOCOLLO, DT_PROTOCOLLAZIONE)
values (1000000,'0','TRRRNZ56R23F837Z','RIFIUTATA','TRRRNZ56R23F837Z','fabio','rossi','fabiorossi@gmail.com','3284455667',
'{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":[{"cuaa":"00237830229"}],"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","elencoCaa":null,"sedi":[{"descrizione":"CAA CIA - BORGO VALSUGANA - 003","identificativo":103}],"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}],"allegato":null}]}',
'1','0','0','PAT-45', to_date('08-01-2019', 'dd-mm-yyyy'));

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS,ID_PROTOCOLLO, DT_PROTOCOLLAZIONE)
values (1000001,'1','STRFBA7812345678','APPROVATA','STRFBA7812345678','giovanni','cometa','giovannicometa@gmail.com','3284455669',
'{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":[{"cuaa":"00237830229"}],"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","elencoCaa":null,"sedi":[{"descrizione":"CAA CIA - BORGO VALSUGANA - 003","identificativo":103}],"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}],"allegato":null}]}',
'1','0','0','PAT-46', to_date('08-01-2019', 'dd-mm-yyyy'));

Insert into A4GT_DOMANDA_REGISTRAZIONE (ID,VERSIONE,IDENTIFICATIVO_UTENTE,STATO,CODICE_FISCALE,NOME,COGNOME,EMAIL,TELEFONO,RESPONSABILITA,A4G,SRT,AGS,ID_PROTOCOLLO, DT_PROTOCOLLAZIONE)
values (10000023,'1','STRFBA7812345678','APPROVATA','STRFBA7812345678','giovanni','cometa','giovannicometa@gmail.com','3284455669',
'{"responsabilitaTitolare":null,"responsabilitaLegaleRappresentante":[{"cuaa":"00237830229"}],"responsabilitaCaa":[{"idResponsabilita":1,"responsabile":"sdcf","codResponsabilita":"CAA","elencoCaa":null,"sedi":[{"descrizione":"CAA CIA - BORGO VALSUGANA - 003","identificativo":103}],"responsabilitaPat":[{"idResponsabilita":1,"codResponsabilita":"PAT","matricola":"TORRE","dirigente":"aaa","dipartimento":"s"}],"allegato":null}]}',
'1','0','0','PAT-46', to_date('08-01-2019', 'dd-mm-yyyy'));



Insert into A4GT_ISTRUTTORIA (ID,VERSIONE,VARIAZIONE_RICHIESTA,TESTO_COMUNICAZIONE,MOTIVAZIONE_RIFIUTO,ID_DOMANDA, DATA_TERMINE_ISTRUTTORIA, ID_ISTRUTTORE)
values (1000002,0,'variazione richiesta 2','testo comunicazione','motivazione rifiuto',1000000, to_date('07-01-2019', 'dd-mm-yyyy'), 9);

Insert into A4GT_ISTRUTTORIA (ID,VERSIONE,VARIAZIONE_RICHIESTA,TESTO_COMUNICAZIONE,MOTIVAZIONE_RIFIUTO,ID_DOMANDA, DATA_TERMINE_ISTRUTTORIA, ID_ISTRUTTORE)
values (1000003,0,'variazione richiesta 2','testo comunicazione',null,1000001, to_date('08-01-2019', 'dd-mm-yyyy'), 5);

Insert into A4GT_ISTRUTTORIA (ID,VERSIONE,VARIAZIONE_RICHIESTA,TESTO_COMUNICAZIONE,MOTIVAZIONE_RIFIUTO,ID_DOMANDA, DATA_TERMINE_ISTRUTTORIA, ID_ISTRUTTORE)
values (1000004,0,'variazione richiesta 2','testo comunicazione',null,10000023, to_date('09-01-2019', 'dd-mm-yyyy'), 5);


