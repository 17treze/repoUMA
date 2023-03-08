--- Domanda acs BLLGPR66B27C372Y - BELLANTE GIAMPIERO - DOMANDA 178729

Insert into A4GT_DOMANDA
   (ID, VERSIONE, NUMERO_DOMANDA, COD_MODULO_DOMANDA, DESC_MODULO_DOMANDA, 
    ANNO_CAMPAGNA, STATO, CUAA_INTESTATARIO, DT_PROTOCOLLAZIONE, COD_ENTE_COMPILATORE, 
    DESC_ENTE_COMPILATORE, RAGIONE_SOCIALE, DT_PRESENTAZIONE)
 Values
   (NXTNBR.NEXTVAL, 3, 178729, 'BPS_2018', 'PAGAMENTI DIRETTI', 
    2018, 'IN_ISTRUTTORIA', 
    'BLLGPR66B27C372Y', TO_DATE('04/05/2018 02:00:00', 'MM/DD/YYYY HH24:MI:SS'), 9, 
    'CAA COLDIRETTI DEL TRENTINO - 008', 'BELLANTE GIAMPIERO', TO_DATE('04/05/2018 02:00:00', 'MM/DD/YYYY HH24:MI:SS'));

Insert into A4GT_ISTRUTTORIA(ID,VERSIONE,ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE, TIPOLOGIA)
values(19999999991L,0,
(SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'BLLGPR66B27C372Y'),
'ZOOTECNIA',
(select id from a4gd_stato_lav_sostegno where identificativo = 'RICHIESTO'),
'SALDO');



Insert into A4GT_ALLEVAMENTO_IMPEGNATO
   (ID, VERSIONE, ID_DOMANDA, CODICE_SPECIE, DATI_ALLEVAMENTO, 
    DATI_PROPRIETARIO, DATI_DETENTORE, ID_INTERVENTO)
 Values
   (1, 1, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'BLLGPR66B27C372Y'), 'BOVINI', '{"idAllevamento":694,"codiceAllevamento":"050TN025","codiceAllevamentoBdn":"729214","descrizioneAllevamento":"BELLANTE GIAMPIERO","comune":"CAVALESE (TN)","toponimo":"MASO PALUA 1/A"}',
    '{"codFiscaleProprietario":"BLLGPR66B27C372Y","denominazioneProprietario":"BELLANTE GIAMPIERO"}', '{"codFiscaleDetentore":"BLLGPR66B27C372Y","denominazioneDetentore":"BELLANTE GIAMPIERO"}', (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'BOVINI_MAC_12M'));
Insert into A4GT_ALLEVAMENTO_IMPEGNATO
   (ID, VERSIONE, ID_DOMANDA, CODICE_SPECIE, DATI_ALLEVAMENTO, 
    DATI_PROPRIETARIO, DATI_DETENTORE, ID_INTERVENTO)
 Values
   (2, 1, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'BLLGPR66B27C372Y'), 'BOVINI', '{"idAllevamento":694,"codiceAllevamento":"050TN025","codiceAllevamentoBdn":"729214","descrizioneAllevamento":"BELLANTE GIAMPIERO","comune":"CAVALESE (TN)","toponimo":"MASO PALUA 1/A"}',
    '{"codFiscaleProprietario":"BLLGPR66B27C372Y","denominazioneProprietario":"BELLANTE GIAMPIERO"}', '{"codFiscaleDetentore":"BLLGPR66B27C372Y","denominazioneDetentore":"BELLANTE GIAMPIERO"}', (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'BOVINI_MAC'));
Insert into A4GT_ALLEVAMENTO_IMPEGNATO
   (ID, VERSIONE, ID_DOMANDA, CODICE_SPECIE, DATI_ALLEVAMENTO, 
    DATI_PROPRIETARIO, DATI_DETENTORE, ID_INTERVENTO)
 Values
   (3, 9, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'BLLGPR66B27C372Y'), 'BOVINI', '{"idAllevamento":694,"codiceAllevamento":"050TN025","codiceAllevamentoBdn":"729214","descrizioneAllevamento":"BELLANTE GIAMPIERO","comune":"CAVALESE (TN)","toponimo":"MASO PALUA 1/A"}',
    '{"codFiscaleProprietario":"BLLGPR66B27C372Y","denominazioneProprietario":"BELLANTE GIAMPIERO"}', '{"codFiscaleDetentore":"BLLGPR66B27C372Y","denominazioneDetentore":"BELLANTE GIAMPIERO"}', (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'BOVINI_VAC'));

--- esiti allevamento 1
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (1, 0, 1, 106536039, 'IT022990212518', 
    'AMMISSIBILE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (2, 0, 1, 103142006, 'IT022990191297', 
    'AMMISSIBILE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (3, 0, 1, 112929822, 'IT022990256652', 
    'AMMISSIBILE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO, MESSAGGIO,RICHIESTO)
 Values
   (4, 0, 1, 110334307, 'IT022990191256', 
    'NON_AMMISSIBILE', 'Il capo non � ammissibile perch� non sono stati rispettati i tempi di identificazione e registrazione in quanto � stato superato il limite 34 tra la data di registrazione della nascita 18/01/2019 e la data di nascita 06/12/2018 che � pari a 43 ( delegata la registrazione nascita; allevamento non autorizzato a proroga marcatura)',1);

--- esiti allevamento 2
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (5, 0, 2, 106536039, 'IT022990212518', 
    'AMMISSIBILE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO)
 Values
   (6, 0, 2, 103142006, 'IT022990191297', 
    'AMMISSIBILE');
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO,RICHIESTO)
 Values
   (7, 0, 2, 112929822, 'IT022990256652', 
    'AMMISSIBILE',1);
Insert into A4GT_ESITO_CALCOLO_CAPO
   (ID, VERSIONE, ID_ALLEVAM_DU, CAPO_ID, CODICE_CAPO, 
    ESITO, MESSAGGIO)
 Values
   (8, 0, 2, 110334307, 'IT022990191256', 
    'NON_AMMISSIBILE', 'Il capo non � ammissibile perch� non sono stati rispettati i tempi di identificazione e registrazione in quanto � stato superato il limite 34 tra la data di registrazione della nascita 18/01/2019 e la data di nascita 06/12/2018 che � pari a 43 ( delegata la registrazione nascita; allevamento non autorizzato a proroga marcatura)');


COMMIT;
