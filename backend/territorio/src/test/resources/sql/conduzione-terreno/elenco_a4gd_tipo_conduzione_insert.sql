INSERT INTO A4GD_TIPO_CONDUZIONE VALUES (nxtnbr.nextval, 0, 'PRO', 'PROPRIETÀ');
INSERT INTO A4GD_TIPO_CONDUZIONE VALUES (nxtnbr.nextval, 0, 'AFF', 'AFFITTO');
INSERT INTO A4GD_TIPO_CONDUZIONE VALUES (nxtnbr.nextval, 0, 'MEZ', 'MEZZADRIA');
INSERT INTO A4GD_TIPO_CONDUZIONE VALUES (nxtnbr.nextval, 0, 'ALT', 'ALTRO');


INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'PRO'), 0, 'IND', 'PROPRIETÀ INDIVISA');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'PRO'), 0, 'NUD', 'NUDA PROPRIETÀ');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'PRO'), 0, 'IRR', 'IRREPERIBILITÀ');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'PRO'), 0, 'COM', 'COMPROPRIETÀ');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'PRO'), 0, 'CBC', 'COMUNIONE DEI BENI FRA CONIUGI');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'PRO'), 0, 'USC', 'USUCAPIONE');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'PRO'), 0, 'CON', 'CONFERIMENTO DI SUPERFICIDA PARTE DI UN SOCIO');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'AFF'), 0, 'GIO', 'AFFITTO A "GIOVANI AGRICOLTORI"');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'AFF'), 0, 'SCR', 'AFFITTO SCRITTO');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'AFF'), 0, 'VER', 'AFFITTO VERBALE');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'MEZ'), 0, 'MEZ', 'MEZZADRIA');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'MEZ'), 0, 'COL', 'COLONIA PARZIARIA');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'ALT'), 0, 'ENF', 'ENFITEUSI');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'ALT'), 0, 'AFE', 'AFFRANCAZIONE DELL''ENFITEUSI');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'ALT'), 0, 'CIV', 'USO CIVICO');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'ALT'), 0, 'USU', 'USUFRUTTO');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'ALT'), 0, 'CLB', 'CONCESSIONE E LOCAZIONE DI BENI IMMOBILI DEMANIALI');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'ALT'), 0, 'CSR', 'COMODATO SCRITTO');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'ALT'), 0, 'CVR', 'COMODATO VERBALE');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'ALT'), 0, 'OGG', 'USO OGGETTIVO');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (nxtnbr.nextval, (SELECT id FROM A4GD_TIPO_CONDUZIONE WHERE codice = 'ALT'), 0, 'MON', 'ZONE MONTANE');



INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (1, 0, 'VIS', 'VISURA CATASTALE');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (2, 0, 'CLM', 'COPIA DEL LIBRO MAESTRO');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (3, 0, 'ATT', 'ATTO DI PROPRIETÀ IN ORIGINALE O COPIA AUTENTICATA');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (4, 0, 'CPV', 'CONTRATTO PRELIMINARE DI VENDITA AD EFFETTI ANTICIPATI');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (5, 0, 'CSD', 'DICHIARAZIONE DELL''USUFRUTTUARIO CONCEDENTE ATTESTANTE IL CONSENSO A CHE IL NUDO PROPRIETARIO CONDUCA LA SUPERFICIE');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (6, 0, 'CSA', 'COPIA DELLA SENTENZA CHE ACCERTA IL DIRITTO');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (7, 0, 'ACC', 'ATTO COSTITUTIVO DELLA SOCIETÀ oppure ATTO DI CONFERIMENTO  DAL QUALE SI EVINCE IL CONFERIMENTO DELLE SUPERFICI DEL SOCIO ALLA SOCIETÀ');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (8, 0, 'CNR', 'DICHIARAZIONE ATTESTANTE IL CONSENSO DEGLI ALTRI COMPROPRIETARI A CONDURRE IL TERRENO');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (9, 0, 'CAS', 'CONTRATTO DI AFFITTO (ATTO PUBBLICO O SCRITTURA PRIVATA AUTENTICATA)');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (10, 0, 'ACA', 'DICHIARAZIONE ATTESTANTE IL CONSENSO DELL''ALTRO CONIUGE A CONDURRE IL TERRENO');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (11, 0, 'ARA', 'CONTRATTO DI AFFITTO REGISTRATO (ATTO PUBBLICO O SCRITTURA PRIVATA AUTENTICATA) ');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (12, 0, 'CAR', 'CONTRATTO DI AFFITTO NON REGISTRATO (ATTO PUBBLICO O SCRITTURA PRIVATA AUTENTICATA) ');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (13, 0, 'CAP', 'CONTRATTO DI AFFITTO (ATTO PUBBLICO O SCRITTURA PRIVATA AUTENTICATA)');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (14, 0, 'DRG', 'DICHIARAZIONE DEL CONCEDENTE ATTESTANTE L''ESISTENZA DEL RAPPORTO E LE GENERALITÀ DELL''AFFITTUARIO E DICHIARAZIONE DI REGISTRAZIONE');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (15, 0, 'DEG', 'DICHIARAZIONE DEL CONCEDENTE ATTESTANTE L''ESISTENZA DEL RAPPORTO E LE GENERALITÀ DELL''AFFITTUARIO');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (16, 0, 'DUR', 'COPIA DELLA DICHIARAZIONE UNILATERALE DI REGISTRAZIONE');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (17, 0, 'CMZ', 'CONTRATTO DI MEZZADRIA REGISTRATO');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (18, 0, 'CCR', 'CONTRATTO DI COLONIA PARZIARIA REGISTRATO');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (19, 0, 'ACE', 'ATTO COSTITUTIVO DI ENFITEUSI REGISTRATO');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (20, 0, 'PGC', 'PROVVEDIMENTO DEL GIUDICE COMPETENTE CHE ACCOGLIE LA RICHIESTA DELL''ENFITEUTA');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (21, 0, 'DEP', 'DICH. DI AMMINISTRAZ./ENTE PUBBLICO/PRIVATO COMP. A LIVELLO TERRITORIALE CON RIF. CATASTALI E QUOTA PARTE DELLE SUP. ATTRIB. AL PRODUTTORE');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (22, 0, 'CCS', 'CERTIFICAZ. DEL COMUNE CON SUP. TOT. CONCESSA IN FIDA PASCOLO A TUTTI GLI AGRICOLTORI, NUM. CAPI AUTORIZZATI, SUP. E/O CAPI ASSEGNATI ALL''AGRICOLTORE');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (23, 0, 'CCU', 'CONTRATTO DI COSTITUZIONE DELL''USUFRUTTO');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (25, 0, 'ACL', 'ATTO DI CONCESSIONE O DI LOCAZIONE CON INDICAZIONE DI SOPRALLUOGO DELL''UFFICIO TECNICO ERARIALE, ASSENSO DELLA COMMISSIONE, INDICAZIONE DEL CANONE');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (26, 0, 'DCA', 'DICHIARAZIONE SOSTITUTIVA ATTESTANTE L''ESISTENZA DI UNA CONCESSIONE SCRITTA CON INDICAZIONE DEGLI ESTREMI DEL VERBALE DI AGGIUDICAZIONE');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (27, 0, 'CRA', 'CONTRATTO DI COMODATO (ATTO PUBBLICO O SCRITTURA PRIVATA REGISTRATA)');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (28, 0, 'DCE', 'DICHIARAZIONE DEL CONCEDENTE ATTESTANTE L''ESISTENZA DEL RAPPORTO E LE GENERALITÀ DEL COMODATARIO');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (29, 0, 'DPS', 'DICHIARAZIONE DEL PRODUTTORE CHE LE SUPERFICI SONO ESCLUSIVAMENTE ED EFFETTIVAMENTE CONDOTTE DAL MEDESIMO');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (30, 0, 'DCT', 'DICHIARAZIONE ATTESTANTE IL CONSENSO DEGLI ALTRI TITOLARI DEL DIRITTO A CONDURRE IL TERRENO');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (31, 0, 'DCC', 'DICHIARAZIONE DEL CONCEDENTE ATTESTANTE IL CONSENSO DEGLI ALTRI TITOLARI COMPROPRIETARI ALLA STIPULA DEL CONTRATTO');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (32, 0, 'CPE', 'CONDUZIONE PARTICELLA DI ESTENSIONE INF. A 5.000 MQ RICADENTE IN COMUNE MONTANO (L. 11/08/2014 N.116): D.S.A.N. DEL CONDUTTORE CON INDICAZIONE DEL/DEI PROPRIETARIO/I');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (33, 0, 'CPM', 'CONDUZIONE PARTICELLA DI ESTENSIONE INF. A 5.000 MQ RICADENTE IN COMUNE MONTANO (L. 11/08/2014 N.116): D.S.A.N DEL CONDUTTORE RELATIVA ALLA CONDUZIONE DEL FONDO');


INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'IND'), 1, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'IND') , 2, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'IND') , 3, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'IND') , 4, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'NUD') , 1, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'NUD') , 2, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'NUD') , 3, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'NUD') , 4, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'NUD') , 5, 'S', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'IRR') , 1, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'IRR') , 2, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'IRR') , 3, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'IRR') , 4, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'IRR') , 8, 'S', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'COM') , 1, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'COM') , 2, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'COM') , 3, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'COM') , 4, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'COM') , 9, 'S', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CBC') , 1, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CBC') , 2, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CBC') , 3, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CBC') , 4, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CBC') , 10, 'S', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'USC') , 6, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CON') , 7, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'GIO') , 11, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'GIO') , 12, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'GIO') , 30, 'S', 0);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'GIO') , 31, 'S', 0);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'SCR') , 13, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'SCR') , 30, 'S', 0);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'SCR') , 31, 'S', 0);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'VER') , 14, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'VER') , 15, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'VER') , 30, 'S', 0);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'VER') , 31, 'S', 0);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'VER') , 16, 'S', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'MEZ') , 17, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'COL') , 18, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'ENF') , 19, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'AFE') , 20, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CIV') , 21, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CIV') , 22, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'USU') , 23, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'USU') , 30, 'S', 0);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CLB') , 25, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CLB') , 26, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CSR') , 27, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CSR') , 30, 'S', 0);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CSR') , 31, 'S', 0);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CVR') , 28, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CVR') , 30, 'S', 0);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'CVR') , 31, 'S', 0);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'OGG') , 29, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'MON') , 32, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (nxtnbr.nextval, 0, (SELECT id FROM A4GD_SOTTOTIPO_CONDUZIONE WHERE codice = 'MON') , 33, 'P', 1);

 