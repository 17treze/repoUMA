--- Domanda acs FLMWFG75M12C794M - FLAIM WOLFGANG - DOMANDA 180955

Insert into A4GT_DOMANDA
   (ID, VERSIONE, NUMERO_DOMANDA, COD_MODULO_DOMANDA, DESC_MODULO_DOMANDA, 
    ANNO_CAMPAGNA, STATO, CUAA_INTESTATARIO, DT_PROTOCOLLAZIONE, COD_ENTE_COMPILATORE, 
    DESC_ENTE_COMPILATORE, RAGIONE_SOCIALE, DT_PRESENTAZIONE)
 Values
   (NXTNBR.NEXTVAL, 3, 180955, 'BPS_2018', 'PAGAMENTI DIRETTI', 
    2018, 'IN_ISTRUTTORIA', 
    'FLMWFG75M12C794M', TO_DATE('04/16/2018 16:50:22', 'MM/DD/YYYY HH24:MI:SS'), 4, 
    'CAA COLDIRETTI DEL TRENTINO - 003', 'FLAIM WOLFGANG', TO_DATE('04/16/2018 16:49:51', 'MM/DD/YYYY HH24:MI:SS'));


Insert into A4GT_ALLEVAMENTO_IMPEGNATO
   (ID, VERSIONE, ID_DOMANDA, CODICE_SPECIE, DATI_ALLEVAMENTO, 
    DATI_PROPRIETARIO, DATI_DETENTORE, ID_INTERVENTO)
 Values
   (50544, 9, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'FLMWFG75M12C794M'), 'BOVINI', '{"idAllevamento":9270,"codiceAllevamento":"152TN059","codiceAllevamentoBdn":"1940060","descrizioneAllevamento":"FLAIM WOLFGANG","comune":"REVO'' (TN)","toponimo":"MIAUNERI 9"}',
    '{"codFiscaleProprietario":"FLMWFG75M12C794M","denominazioneProprietario":"FLAIM WOLFGANG"}', '{"codFiscaleDetentore":"FLMWFG75M12C794M","denominazioneDetentore":"FLAIM WOLFGANG"}', (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'BOVINI_VAC'));
Insert into A4GT_ALLEVAMENTO_IMPEGNATO
   (ID, VERSIONE, ID_DOMANDA, CODICE_SPECIE, DATI_ALLEVAMENTO, 
    DATI_PROPRIETARIO, DATI_DETENTORE, ID_INTERVENTO)
 Values
   (50545, 9, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'FLMWFG75M12C794M'), 'BOVINI', '{"idAllevamento":9270,"codiceAllevamento":"152TN059","codiceAllevamentoBdn":"1940060","descrizioneAllevamento":"FLAIM WOLFGANG","comune":"REVO'' (TN)","toponimo":"MIAUNERI 9"}',
    '{"codFiscaleProprietario":"FLMWFG75M12C794M","denominazioneProprietario":"FLAIM WOLFGANG"}', '{"codFiscaleDetentore":"FLMWFG75M12C794M","denominazioneDetentore":"FLAIM WOLFGANG"}', (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'LATTE'));
Insert into A4GT_ALLEVAMENTO_IMPEGNATO
   (ID, VERSIONE, ID_DOMANDA, CODICE_SPECIE, DATI_ALLEVAMENTO, 
    DATI_PROPRIETARIO, DATI_DETENTORE, ID_INTERVENTO)
 Values
   (50546, 8, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'FLMWFG75M12C794M'), 'BOVINI', '{"idAllevamento":9270,"codiceAllevamento":"152TN059","codiceAllevamentoBdn":"1940060","descrizioneAllevamento":"FLAIM WOLFGANG","comune":"REVO'' (TN)","toponimo":"MIAUNERI 9"}',
    '{"codFiscaleProprietario":"FLMWFG75M12C794M","denominazioneProprietario":"FLAIM WOLFGANG"}', '{"codFiscaleDetentore":"FLMWFG75M12C794M","denominazioneDetentore":"FLAIM WOLFGANG"}', (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'LATTE_BMONT'));

Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO)
 Values
   (1604374, 0, 50545, 111319491, 'IT022990238804', 
    'AMMISSIBILE');
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO)
 Values
   (1604381, 0, 50546, 111319491, 'IT022990238804', 
    'AMMISSIBILE');

Insert into A4GT_DOMANDA_INTEGRATIVA
   (ID, VERSIONE, ID_RICH_ALLEVAM_ESITO, STATO, DT_ULTIMO_AGGIORNAMENTO, DUPLICATO,
    CONTROLLO_SUPERATO, IDENTIFICATIVO)
 Values
   (1910703, 0, 1604374, 'PRESENTATA', TO_DATE('04/08/2019 11:47:58', 'MM/DD/YYYY HH24:MI:SS'), 1,
    1, 'DI_205_DUF023');
Insert into A4GT_DOMANDA_INTEGRATIVA
   (ID, VERSIONE, ID_RICH_ALLEVAM_ESITO, STATO, DT_ULTIMO_AGGIORNAMENTO, DUPLICATO,
    CONTROLLO_SUPERATO, IDENTIFICATIVO)
 Values
   (1910704, 0, 1604381, 'PRESENTATA', TO_DATE('04/08/2019 11:47:58', 'MM/DD/YYYY HH24:MI:SS'), 1,
    1, 'DI_205_DUF023');

Insert into A4GT_ISTRUTTORIA(ID,VERSIONE,ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE, TIPOLOGIA)
values(4449871234,0,(SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'FLMWFG75M12C794M'),'ZOOTECNIA',(select id from a4gd_stato_lav_sostegno where identificativo = 'INTEGRATO'), 'SALDO');

INSERT INTO A4GT_CAMPIONE (ID, VERSIONE, ANNO_CAMPAGNA, CUAA, TIPO_CAMPIONE, TIPO, BOVINI, OVINI) VALUES (NXTNBR.nextval, 0, 2018, 'FLMWFG75M12C794M', 'ZOOTECNIA', 'RISCHIO', 1, 0);

INSERT INTO A4GT_ISTRUTTORE_ZOOTECNIA (ID, CONTROLLO_SIGECO_LOCO, CONTROLLO_ANTIMAFIA, ID_ISTRUTTORIA)
VALUES (4449871233, 1, null , 4449871234);

COMMIT;

