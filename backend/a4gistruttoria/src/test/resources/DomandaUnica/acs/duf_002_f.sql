--- Domanda acs MARCABRUNI ANTONIO DOMANDA 189692
Insert into A4GT_DOMANDA
   (ID, VERSIONE, NUMERO_DOMANDA, COD_MODULO_DOMANDA, DESC_MODULO_DOMANDA, 
    ANNO_CAMPAGNA, STATO, CUAA_INTESTATARIO, DT_PROTOCOLLAZIONE, COD_ENTE_COMPILATORE, 
    DESC_ENTE_COMPILATORE, RAGIONE_SOCIALE, DT_PRESENTAZIONE)
 Values
   (NXTNBR.NEXTVAL, 3, 189692, 'BPS_2018', 'PAGAMENTI DIRETTI', 
    2018, 'IN_ISTRUTTORIA', 'MRCNTN53A16A372C', TO_DATE('06/08/2018 15:55:19', 'MM/DD/YYYY HH24:MI:SS'), 7, 
    'CAA COLDIRETTI DEL TRENTINO - 006', 'MARCABRUNI ANTONIO', TO_DATE('06/08/2018 15:54:50', 'MM/DD/YYYY HH24:MI:SS'));

Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'MRCNTN53A16A372C'), 1069, 1069, 
    '160-111-011', '420-006-000-000-011', '{"idParticella":2086309,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"00336","sub":"1"}', '{"idPianoColture":6346448,"idColtura":4713,"codColtura3":"160-111-011","codColtura5":"420-006-000-000-011","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - CASALIVA","coefficienteTara":1,"superficieDichiarata":1069,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":615677,"idIsola":939522,"codIsola":"IT25/MRCNTN53A16A372C/AAA07"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'MRCNTN53A16A372C'), 4123, 4123, 
    '160-111-011', '420-006-000-000-011', '{"idParticella":2086176,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"00383","sub":" "}', '{"idPianoColture":6346445,"idColtura":4713,"codColtura3":"160-111-011","codColtura5":"420-006-000-000-011","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - CASALIVA","coefficienteTara":1,"superficieDichiarata":123,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":615676,"idIsola":939521,"codIsola":"IT25/MRCNTN53A16A372C/AAA06"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'MRCNTN53A16A372C'), 275, 275, 
    '160-111-011', '420-006-000-000-011', '{"idParticella":2086248,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"00275","sub":"12"}', '{"idPianoColture":6346446,"idColtura":4713,"codColtura3":"160-111-011","codColtura5":"420-006-000-000-011","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - CASALIVA","coefficienteTara":1,"superficieDichiarata":275,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":615675,"idIsola":939518,"codIsola":"IT25/MRCNTN53A16A372C/AAA03"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'MRCNTN53A16A372C'), 431, 431, 
    '160-111-011', '420-006-000-000-011', '{"idParticella":2086259,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"00275","sub":"13"}', '{"idPianoColture":6346447,"idColtura":4713,"codColtura3":"160-111-011","codColtura5":"420-006-000-000-011","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - CASALIVA","coefficienteTara":1,"superficieDichiarata":431,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":615675,"idIsola":939518,"codIsola":"IT25/MRCNTN53A16A372C/AAA03"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));


Insert into A4GT_ISTRUTTORIA(ID,VERSIONE,ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE, TIPOLOGIA)
values(NXTNBR.nextval,0,(SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'MRCNTN53A16A372C'),'SUPERFICIE',(select id from a4gd_stato_lav_sostegno where identificativo = 'RICHIESTO'), 'SALDO');
INSERT INTO A4GT_ISTRUTTORE_SUPERFICIE (ID, CONTROLLO_SIGECO_LOCO, VERSIONE, ID_ISTRUTTORIA) VALUES (NXTNBR.nextval, 1, 0, NXTNBR.CURRVAL-1);

INSERT INTO A4GD_REG_OLIO_NAZIONALE (ID, VERSIONE, CUAA_INTESTATARIO, INIZIO_CAMPAGNA, FINE_CAMPAGNA) VALUES (NXTNBR.NEXTVAL, 0, 'MRCNTN53A16A372C', 2016, 2020);
INSERT INTO A4GD_REG_OLIO_QUALITA (ID, VERSIONE, CUAA_INTESTATARIO, INIZIO_CAMPAGNA, FINE_CAMPAGNA) VALUES (NXTNBR.NEXTVAL, 0, 'MRCNTN53A16A372C', 2016, 2020);

INSERT INTO A4GT_CAMPIONE (ID, VERSIONE, ANNO_CAMPAGNA, CUAA, TIPO_CAMPIONE, TIPO) VALUES (NXTNBR.nextval, 0, 2018, 'MRCNTN53A16A372C', 'SUPERFICIE', 'CASUALE');

commit;
