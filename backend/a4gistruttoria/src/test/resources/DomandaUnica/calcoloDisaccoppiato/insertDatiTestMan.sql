
-- 1. PASCOLO FUORI PROVINCIA - Domanda: 182864 - Pascolo: 162BZ050
-- 1.2 inserimento pascolo di malga fuori Provincia
Insert into A4GT_PASCOLO_PARTICELLA values (nxtnbr.nextval,1,(SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 182864),null,'162BZ050','162BZ050','[{"particella":{"idParticella":2288822,"comune":"SELLA GIUDICARIE - LARDARO I (TN)","codNazionale":"P464","foglio":9999,"particella":"00188","sub":"2"},"coltura":"560-461-009","descColtura":null,"livello":"1311","valNum":3500.0,"valBool":null,"valString":"P464","descMantenimento":"PASCOLAMENTO CON ANIMALI PROPRI"}]',3500,null, null, null);


-- 2. PASCOLO AZIENDALE - Domanda: 180523 - Pascolo: AZIENDALE - P464
-- 2.1 Pascolo aziendale
Insert into A4GT_PASCOLO_PARTICELLA values (nxtnbr.nextval,1,(SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 180523),null,'999TN999','AZIENDALE - P464','[{"particella":{"idParticella":2288822,"comune":"SELLA GIUDICARIE - LARDARO I (TN)","codNazionale":"P464","foglio":9999,"particella":"00188","sub":"2"},"coltura":"560-461-009","descColtura":null,"livello":"1311","valNum":3500.0,"valBool":null,"valString":"P464","descMantenimento":"PASCOLAMENTO CON ANIMALI PROPRI"}]',3500,null, null, null);


-- 3. PASCOLO DI MALGA - Domanda: 180513 - Pascolo: 158TN050
-- 3.1 Pascolo di malga
Insert into A4GT_PASCOLO_PARTICELLA values (nxtnbr.nextval,1,(SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 180513),null,'158TN050','158TN050','[{"particella":{"idParticella":2288822,"comune":"SELLA GIUDICARIE - LARDARO I (TN)","codNazionale":"P464","foglio":9999,"particella":"00188","sub":"2"},"coltura":"560-461-009","descColtura":null,"livello":"1311","valNum":3500.0,"valBool":null,"valString":"P464","descMantenimento":"PASCOLAMENTO CON ANIMALI PROPRI"}]',3500,null, null, null);


commit;