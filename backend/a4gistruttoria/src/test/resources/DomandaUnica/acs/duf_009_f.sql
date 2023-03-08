--- Domanda acs MASO PRAEL DI SIGNORETTI NATALINO DOMANDA 189675
Insert into A4GT_DOMANDA
   (ID, VERSIONE, NUMERO_DOMANDA, COD_MODULO_DOMANDA, DESC_MODULO_DOMANDA, 
    ANNO_CAMPAGNA, STATO, CUAA_INTESTATARIO, DT_PROTOCOLLAZIONE, COD_ENTE_COMPILATORE, 
    DESC_ENTE_COMPILATORE, RAGIONE_SOCIALE, DT_PRESENTAZIONE)
 Values
   (NXTNBR.NEXTVAL, 3, 189675, 'BPS_2018', 'PAGAMENTI DIRETTI', 
    2018, 'IN_ISTRUTTORIA', 'SGNNLN54T28A372X', TO_DATE('06/08/2018 15:55:19', 'MM/DD/YYYY HH24:MI:SS'), 7, 
    'CAA COLDIRETTI DEL TRENTINO - 006', 'MASO PRAEL DI SIGNORETTI NATALINO', TO_DATE('06/08/2018 15:54:50', 'MM/DD/YYYY HH24:MI:SS'));

Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 571, 571, 
    '160-111-011', '420-006-000-000-011', '{"idParticella":2086448,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"00240","sub":"8"}', '{"idPianoColture":6626818,"idColtura":4713,"codColtura3":"160-111-011","codColtura5":"420-006-000-000-011","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - CASALIVA","coefficienteTara":1,"superficieDichiarata":571,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614504,"idIsola":220515,"codIsola":"IT01/SGNNLN54T28A372X/AAA07"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 2036, 2036, 
    '160-111-000', '420-006-000-000-000', '{"idParticella":2086271,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"00104","sub":"1"}', '{"idPianoColture":6626812,"idColtura":107,"codColtura3":"160-111-000","codColtura5":"420-006-000-000-000","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - OLIVE DA OLIO","coefficienteTara":1,"superficieDichiarata":2036,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614517,"idIsola":1651046,"codIsola":"IT25/SGNNLN54T28A372X/AAA36"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 26, 26, 
    '160-111-000', '420-006-000-000-000', '{"idParticella":2086448,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"00240","sub":"8"}', '{"idPianoColture":6626821,"idColtura":107,"codColtura3":"160-111-000","codColtura5":"420-006-000-000-000","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - OLIVE DA OLIO","coefficienteTara":1,"superficieDichiarata":26,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614504,"idIsola":220515,"codIsola":"IT01/SGNNLN54T28A372X/AAA07"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 1, 1, 
    '160-111-000', '420-006-000-000-000', '{"idParticella":2086448,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"00240","sub":"8"}', '{"idPianoColture":6626822,"idColtura":107,"codColtura3":"160-111-000","codColtura5":"420-006-000-000-000","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - OLIVE DA OLIO","coefficienteTara":1,"superficieDichiarata":1,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614504,"idIsola":220515,"codIsola":"IT01/SGNNLN54T28A372X/AAA07"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 289, 289, 
    '160-111-011', '420-006-000-000-011', '{"idParticella":2086419,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"00132","sub":" "}', '{"idPianoColture":6626813,"idColtura":4713,"codColtura3":"160-111-011","codColtura5":"420-006-000-000-011","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - CASALIVA","coefficienteTara":1,"superficieDichiarata":289,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614506,"idIsola":220516,"codIsola":"IT01/SGNNLN54T28A372X/AAA08"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 55, 55, 
    '160-111-011', '420-006-000-000-011', '{"idParticella":2088357,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"01823","sub":" "}', '{"idPianoColture":6626854,"idColtura":4713,"codColtura3":"160-111-011","codColtura5":"420-006-000-000-011","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - CASALIVA","coefficienteTara":1,"superficieDichiarata":55,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614507,"idIsola":220527,"codIsola":"IT01/SGNNLN54T28A372X/AAA06"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 399, 399, 
    '160-111-011', '420-006-000-000-011', '{"idParticella":2088357,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"01823","sub":" "}', '{"idPianoColture":6626856,"idColtura":4713,"codColtura3":"160-111-011","codColtura5":"420-006-000-000-011","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - CASALIVA","coefficienteTara":1,"superficieDichiarata":399,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614508,"idIsola":220527,"codIsola":"IT01/SGNNLN54T28A372X/AAA06"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 103, 103, 
    '160-111-011', '420-006-000-000-011', '{"idParticella":2088357,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"01823","sub":" "}', '{"idPianoColture":6626855,"idColtura":4713,"codColtura3":"160-111-011","codColtura5":"420-006-000-000-011","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - CASALIVA","coefficienteTara":1,"superficieDichiarata":103,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614509,"idIsola":220527,"codIsola":"IT01/SGNNLN54T28A372X/AAA06"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 326, 326, 
    '160-111-023', '420-006-000-000-023', '{"idParticella":3592620,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"01470","sub":"2H"}', '{"idPianoColture":6626994,"idColtura":122,"codColtura3":"160-111-023","codColtura5":"420-006-000-000-023","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - FRANTOIO","coefficienteTara":1,"superficieDichiarata":326,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614510,"idIsola":1606429,"codIsola":"IT25/SGNNLN54T28A372X/AAA27"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 1234, 1234, 
    '160-111-023', '420-006-000-000-023', '{"idParticella":3592620,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"01470","sub":"2H"}', '{"idPianoColture":6626995,"idColtura":122,"codColtura3":"160-111-023","codColtura5":"420-006-000-000-023","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - FRANTOIO","coefficienteTara":1,"superficieDichiarata":1234,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614511,"idIsola":1606429,"codIsola":"IT25/SGNNLN54T28A372X/AAA27"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 879, 879, 
    '160-111-011', '420-006-000-000-011', '{"idParticella":2087164,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"03141","sub":" "}', '{"idPianoColture":6626826,"idColtura":4713,"codColtura3":"160-111-011","codColtura5":"420-006-000-000-011","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - CASALIVA","coefficienteTara":1,"superficieDichiarata":879,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614512,"idIsola":1606431,"codIsola":"IT25/SGNNLN54T28A372X/AAA28"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 5050, 5050, 
    '160-111-023', '420-006-000-000-023', '{"idParticella":3321868,"comune":"RIVA DEL GARDA - RIVA DEL GARDA (TN)","codNazionale":"P552","foglio":9999,"particella":"03690","sub":"1"}', '{"idPianoColture":6626992,"idColtura":122,"codColtura3":"160-111-023","codColtura5":"420-006-000-000-023","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - FRANTOIO","coefficienteTara":1,"superficieDichiarata":5050,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614513,"idIsola":1606480,"codIsola":"IT25/SGNNLN54T28A372X/AAA32"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 150, 150, 
    '160-111-011', '420-006-000-000-011', '{"idParticella":2086269,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"00103","sub":"1"}', '{"idPianoColture":6626811,"idColtura":4713,"codColtura3":"160-111-011","codColtura5":"420-006-000-000-011","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - CASALIVA","coefficienteTara":1,"superficieDichiarata":150,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614514,"idIsola":1607362,"codIsola":"IT25/SGNNLN54T28A372X/AAA30"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 719, 719, 
    '160-111-011', '420-006-000-000-011', '{"idParticella":2481669,"comune":"RIVA DEL GARDA - RIVA DEL GARDA (TN)","codNazionale":"P552","foglio":9999,"particella":"03880","sub":" "}', '{"idPianoColture":6626982,"idColtura":4713,"codColtura3":"160-111-011","codColtura5":"420-006-000-000-011","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - CASALIVA","coefficienteTara":1,"superficieDichiarata":719,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614515,"idIsola":1607369,"codIsola":"IT25/SGNNLN54T28A372X/AAA33"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 1513, 1513, 
    '160-111-011', '420-006-000-000-011', '{"idParticella":2481669,"comune":"RIVA DEL GARDA - RIVA DEL GARDA (TN)","codNazionale":"P552","foglio":9999,"particella":"03880","sub":" "}', '{"idPianoColture":6626981,"idColtura":4713,"codColtura3":"160-111-011","codColtura5":"420-006-000-000-011","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - CASALIVA","coefficienteTara":1,"superficieDichiarata":1513,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614515,"idIsola":1607369,"codIsola":"IT25/SGNNLN54T28A372X/AAA33"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 1611, 1611, 
    '160-111-011', '420-006-000-000-011', '{"idParticella":2481776,"comune":"RIVA DEL GARDA - RIVA DEL GARDA (TN)","codNazionale":"P552","foglio":9999,"particella":"03874","sub":"1"}', '{"idPianoColture":6626989,"idColtura":4713,"codColtura3":"160-111-011","codColtura5":"420-006-000-000-011","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - CASALIVA","coefficienteTara":1,"superficieDichiarata":1611,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614516,"idIsola":1644091,"codIsola":"IT25/SGNNLN54T28A372X/AAA34"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 3500, 3500, 
    '160-111-011', '420-006-000-000-011', '{"idParticella":2481723,"comune":"RIVA DEL GARDA - RIVA DEL GARDA (TN)","codNazionale":"P552","foglio":9999,"particella":"03691","sub":" "}', '{"idPianoColture":6626986,"idColtura":4713,"codColtura3":"160-111-011","codColtura5":"420-006-000-000-011","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - CASALIVA","coefficienteTara":1,"superficieDichiarata":3500,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614516,"idIsola":1644091,"codIsola":"IT25/SGNNLN54T28A372X/AAA34"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
Insert into A4GT_RICHIESTA_SUPERFICIE
   (ID, VERSIONE, ID_DOMANDA, SUP_RICHIESTA, SUP_RICHIESTA_NETTA, 
    CODICE_COLTURA_3, CODICE_COLTURA_5, INFO_CATASTALI, INFO_COLTIVAZIONE, RIFERIMENTI_CARTOGRAFICI, 
    ID_INTERVENTO)
 Values
   (NXTNBR.NEXTVAL, 0, (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'), 2, 2, 
    '160-111-000', '420-006-000-000-000', '{"idParticella":2086448,"comune":"ARCO - OLTRESARCA (TN)","codNazionale":"P370","foglio":9999,"particella":"00240","sub":"8"}', '{"idPianoColture":6626820,"idColtura":107,"codColtura3":"160-111-000","codColtura5":"420-006-000-000-000","codLivello":"121","descrizioneColtura":"OLIVO - OLIVE DA OLIO - OLIVE DA OLIO","coefficienteTara":1,"superficieDichiarata":2,"descMantenimento":"PRATICA ORDINARIA"}', '{"idParcella":614504,"idIsola":220515,"codIsola":"IT01/SGNNLN54T28A372X/AAA07"}', 
    (SELECT ID FROM A4GD_INTERVENTO WHERE IDENTIFICATIVO_INTERVENTO = 'OLIVE_DISC'));
COMMIT;


Insert into A4GT_ISTRUTTORIA(ID,VERSIONE,ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE, TIPOLOGIA)
values(NXTNBR.nextval,0,(SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'SGNNLN54T28A372X'),'SUPERFICIE',(select id from a4gd_stato_lav_sostegno where identificativo = 'RICHIESTO'), 'SALDO');
INSERT INTO A4GT_ISTRUTTORE_SUPERFICIE (ID, CONTROLLO_SIGECO_LOCO, VERSIONE, ID_ISTRUTTORIA) VALUES (NXTNBR.nextval, 1, 0, NXTNBR.CURRVAL-1);

INSERT INTO A4GD_REG_OLIO_NAZIONALE (ID, VERSIONE, CUAA_INTESTATARIO, INIZIO_CAMPAGNA, FINE_CAMPAGNA) VALUES (NXTNBR.NEXTVAL, 0, 'SGNNLN54T28A372X', 2016, 2020);
INSERT INTO A4GD_REG_OLIO_QUALITA (ID, VERSIONE, CUAA_INTESTATARIO, INIZIO_CAMPAGNA, FINE_CAMPAGNA) VALUES (NXTNBR.NEXTVAL, 0, 'SGNNLN54T28A372X', 2016, 2020);

INSERT INTO A4GT_CAMPIONE (ID, VERSIONE, ANNO_CAMPAGNA, CUAA, TIPO_CAMPIONE, TIPO) VALUES (NXTNBR.nextval, 0, 2018, 'SGNNLN54T28A372X', 'SUPERFICIE', 'CASUALE');

commit;
