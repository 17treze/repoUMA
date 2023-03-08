INSERT INTO A4GT_ISTRUTTORIA(ID,VERSIONE,ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE, TIPOLOGIA)
values(7777111,1,
(SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'URRRED82B02F839U'),
'ZOOTECNIA',
(select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK'), 'SALDO');

INSERT INTO A4GT_ISTRUTTORIA(ID,VERSIONE,ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE, TIPOLOGIA)
values(7777112,0,
(SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'URRRED82B02F839U'),
'ZOOTECNIA',
(select id from a4gd_stato_lav_sostegno where identificativo = 'CONTROLLI_CALCOLO_OK'), 'SALDO');



Insert into A4GT_DATI_PARTICELLA_COLTURA
(ID, VERSIONE, ID_DOMANDA, INFO_CATASTALI, CODICE_COLTURA_3, DATI_PARTICELLA, ID_ISTRUTTORIA)
values
(333000111, 0, NULL,
'{"idParticella":1971119,"comune":"VALFLORIANA (TN)","codNazionale":"L575","foglio":9999,"particella":"05488","sub":" "}',
'720-650-009',
'{"superficieImpegnata":0.001,"superficieEleggibile":0.001,"superficieSigeco":0.0,"anomalieMantenimento":true,"anomalieCoordinamento":false,"superficieDeterminata":0.0,"tipoColtura":"Prato Permanente","tipoSeminativo":null,"colturaPrincipale":false,"pascolo":"AZIENDALE - L575","secondaColtura":false,"azotoFissatrice":false,"superficieAnomalieCoordinamento":0}',
7777111);

Insert into A4GT_DATI_PARTICELLA_COLTURA
(ID, VERSIONE, ID_DOMANDA, INFO_CATASTALI, CODICE_COLTURA_3, DATI_PARTICELLA, ID_ISTRUTTORIA)
values
(333000112, 0, NULL,
'{"idParticella":2619499,"comune":"TON - VIGO (TN)","codNazionale":"P616","foglio":9999,"particella":"01192","sub":" "}',
'560-065-009',
'{"superficieImpegnata":2.1584,"superficieEleggibile":2.1584,"superficieSigeco":0.0,"anomalieMantenimento":true,"anomalieCoordinamento":false,"superficieDeterminata":0.0,"tipoColtura":"Prato Permanente","tipoSeminativo":null,"colturaPrincipale":false,"secondaColtura":false,"azotoFissatrice":false,"superficieAnomalieCoordinamento":0}',
7777111
);


Insert into A4GT_DATI_PARTICELLA_COLTURA
(ID, VERSIONE, ID_DOMANDA, INFO_CATASTALI, CODICE_COLTURA_3, DATI_PARTICELLA, ID_ISTRUTTORIA)
values
(333000113, 0, NULL,
'{"idParticella":2619573,"comune":"TON - VIGO (TN)","codNazionale":"P616","foglio":9999,"particella":"0.197","sub":"2"}',
'560-065-009',
'{"superficieImpegnata":0.0106,"superficieEleggibile":0.0106,"superficieSigeco":0.0,"anomalieMantenimento":true,"anomalieCoordinamento":false,"superficieDeterminata":0.0,"tipoColtura":"Prato Permanente","tipoSeminativo":null,"colturaPrincipale":false,"secondaColtura":false,"azotoFissatrice":false,"superficieAnomalieCoordinamento":0}',
7777112
);
