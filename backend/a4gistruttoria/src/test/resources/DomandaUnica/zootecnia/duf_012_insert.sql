--- Domanda acs FLMWFG75M12C794f - FLAIM WOLFGANG - DOMANDA 180955

Insert into A4GT_DOMANDA
   (ID, VERSIONE, NUMERO_DOMANDA, COD_MODULO_DOMANDA, DESC_MODULO_DOMANDA, 
    ANNO_CAMPAGNA, STATO, CUAA_INTESTATARIO, DT_PROTOCOLLAZIONE, COD_ENTE_COMPILATORE, 
    DESC_ENTE_COMPILATORE, RAGIONE_SOCIALE, DT_PRESENTAZIONE)
 Values
   (NXTNBR.NEXTVAL, 3, 180955, 'BPS_2018', 'PAGAMENTI DIRETTI', 
    2018, 'IN_ISTRUTTORIA', 
    'FLMWFG75M12C794f', TO_DATE('04/16/2018 16:50:22', 'MM/DD/YYYY HH24:MI:SS'), 4, 
    'CAA COLDIRETTI DEL TRENTINO - 003', 'FLAIM WOLFGANG', TO_DATE('04/16/2018 16:49:51', 'MM/DD/YYYY HH24:MI:SS'));


Insert into A4GT_ALLEVAMENTO_IMPEGNATO
   (ID, VERSIONE, ID_DOMANDA, CODICE_SPECIE, DATI_ALLEVAMENTO, 
    DATI_PROPRIETARIO, DATI_DETENTORE, ID_INTERVENTO)
 Values
   (50544, 9, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'FLMWFG75M12C794f'), 'BOVINI', '{"idAllevamento":9270,"codiceAllevamento":"152TN059","codiceAllevamentoBdn":"1940060","descrizioneAllevamento":"FLAIM WOLFGANG","comune":"REVO'' (TN)","toponimo":"MIAUNERI 9"}',
    '{"codFiscaleProprietario":"FLMWFG75M12C794f","denominazioneProprietario":"FLAIM WOLFGANG"}', '{"codFiscaleDetentore":"FLMWFG75M12C794f","denominazioneDetentore":"FLAIM WOLFGANG"}', (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'BOVINI_VAC'));
Insert into A4GT_ALLEVAMENTO_IMPEGNATO
   (ID, VERSIONE, ID_DOMANDA, CODICE_SPECIE, DATI_ALLEVAMENTO, 
    DATI_PROPRIETARIO, DATI_DETENTORE, ID_INTERVENTO)
 Values
   (50545, 9, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'FLMWFG75M12C794f'), 'BOVINI', '{"idAllevamento":9270,"codiceAllevamento":"152TN059","codiceAllevamentoBdn":"1940060","descrizioneAllevamento":"FLAIM WOLFGANG","comune":"REVO'' (TN)","toponimo":"MIAUNERI 9"}',
    '{"codFiscaleProprietario":"FLMWFG75M12C794f","denominazioneProprietario":"FLAIM WOLFGANG"}', '{"codFiscaleDetentore":"FLMWFG75M12C794f","denominazioneDetentore":"FLAIM WOLFGANG"}', (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'LATTE'));
Insert into A4GT_ALLEVAMENTO_IMPEGNATO
   (ID, VERSIONE, ID_DOMANDA, CODICE_SPECIE, DATI_ALLEVAMENTO, 
    DATI_PROPRIETARIO, DATI_DETENTORE, ID_INTERVENTO)
 Values
   (50546, 8, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'FLMWFG75M12C794f'), 'BOVINI', '{"idAllevamento":9270,"codiceAllevamento":"152TN059","codiceAllevamentoBdn":"1940060","descrizioneAllevamento":"FLAIM WOLFGANG","comune":"REVO'' (TN)","toponimo":"MIAUNERI 9"}',
    '{"codFiscaleProprietario":"FLMWFG75M12C794f","denominazioneProprietario":"FLAIM WOLFGANG"}', '{"codFiscaleDetentore":"FLMWFG75M12C794f","denominazioneDetentore":"FLAIM WOLFGANG"}', (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'LATTE_BMONT'));

Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (1604374, 0, 50545, 111319491, 'IT022990238804', 
    'AMMISSIBILE_CON_SANZIONE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (1604381, 0, 50546, 111319491, 'IT022990238804', 
    'AMMISSIBILE_CON_SANZIONE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (1604375, 0, 50545, 101736746, 'IT022990171188', 
    'AMMISSIBILE_CON_SANZIONE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (1604382, 0, 50546, 101736746, 'IT022990171188', 
    'AMMISSIBILE_CON_SANZIONE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (1604376, 0, 50545, 98321871, 'IT022990169797', 
    'AMMISSIBILE_CON_SANZIONE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (1604383, 0, 50546, 98321871, 'IT022990169797', 
    'AMMISSIBILE_CON_SANZIONE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (1604377, 0, 50545, 97216570, 'IT022990148621', 
    'AMMISSIBILE_CON_SANZIONE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (1604384, 0, 50546, 97216570, 'IT022990148621', 
    'AMMISSIBILE_CON_SANZIONE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (1604392, 0, 50544, 108538863, 'IT021002092599', 
    'AMMISSIBILE_CON_SANZIONE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (1604379, 0, 50545, 108200143, 'IT022990238800', 
    'AMMISSIBILE_CON_SANZIONE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (1604386, 0, 50546, 108200143, 'IT022990238800', 
    'AMMISSIBILE_CON_SANZIONE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (1604380, 0, 50545, 101737197, 'IT022990191209', 
    'AMMISSIBILE_CON_SANZIONE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (1604387, 0, 50546, 101737197, 'IT022990191209', 
    'AMMISSIBILE_CON_SANZIONE',1);
    



Insert into A4GT_ISTRUTTORIA(ID,VERSIONE,ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE, TIPOLOGIA)
values(4449871234,0,(SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'FLMWFG75M12C794f'),'ZOOTECNIA',(select id from a4gd_stato_lav_sostegno where identificativo = 'INTEGRATO'), 'SALDO');

COMMIT;

