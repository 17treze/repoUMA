--Test torna a richiesto zootecnia (reset_istruttoria)
--Insert Domanda
INSERT INTO A4GT_DOMANDA ( ID , VERSIONE , NUMERO_DOMANDA , COD_MODULO_DOMANDA , DESC_MODULO_DOMANDA , CUAA_INTESTATARIO , DT_PROTOCOLLAZIONE , NUM_DOMANDA_RETTIFICATA , COD_ENTE_COMPILATORE , DESC_ENTE_COMPILATORE , RAGIONE_SOCIALE , DT_PRESENTAZIONE , DT_PRESENTAZ_ORIGINARIA , DT_PROTOCOLLAZ_ORIGINARIA , ANNULLO_RIDUZIONE , IBAN , ANNO_CAMPAGNA , STATO )
VALUES ( 8661346 , 2 , 213023 , 'BPS_2019' , 'PAGAMENTI DIRETTI' , 'ZTTMRO76H59E565K' , TIMESTAMP '2019-06-10 15:28:16.000000' , NULL , 778 , 'CAA ACLI - TRENTO - 006' , 'ZOTTELE MOIRA' , TIMESTAMP '2019-06-10 15:27:00.000000' , NULL , NULL , NULL , 'IT93C0810235350000055054581' , 2019 , 'IN_ISTRUTTORIA' );
--Insert Istruttoria
INSERT INTO A4GT_ISTRUTTORIA ( ID , VERSIONE , ID_DOMANDA , ID_STATO_LAVORAZIONE , BLOCCATA_BOOL , ERRORE_CALCOLO , DATA_ULTIMO_CALCOLO , TIPOLOGIA , ID_ELENCO_LIQUIDAZIONE , SOSTEGNO ) VALUES ( 8672251 , 44 , 8661346 , (select id from A4GD_STATO_LAV_SOSTEGNO WHERE identificativo = 'NON_AMMISSIBILE') , 0 , 0 , TIMESTAMP '2020-04-07 16:01:33.000000' , 'SALDO' , NULL , 'ZOOTECNIA');
--Insert Transizione	
INSERT INTO A4GT_TRANSIZIONE_ISTRUTTORIA ( ID , VERSIONE , ID_STATO_INIZIALE , ID_STATO_FINALE , DATA_ESECUZIONE , ID_ISTRUTTORIA ) 
VALUES ( 12020314 , 1 , (select id from A4GD_STATO_LAV_SOSTEGNO WHERE identificativo = 'RICHIESTO') , (select id from A4GD_STATO_LAV_SOSTEGNO WHERE identificativo = 'NON_AMMISSIBILE') , TIMESTAMP '2020-04-07 16:01:29.000000' , 8672251 ) ;
--Insert Allevamento
INSERT INTO A4GT_ALLEVAMENTO_IMPEGNATO ( ID , VERSIONE , ID_DOMANDA , CODICE_SPECIE , DATI_ALLEVAMENTO , DATI_PROPRIETARIO , DATI_DETENTORE , ID_INTERVENTO ) 
VALUES ( 8672161 , 0 , 8661346 , 'BOVINI' , '{"idAllevamento":17343,"codiceAllevamento":"156TN916","codiceAllevamentoBdn":"5246813","descrizioneAllevamento":"ZOTTELE MOIRA","comune":null,"indirizzo":null}' , '{"codFiscaleProprietario":null,"denominazioneProprietario":null}' , '{"codFiscaleDetentore":null,"denominazioneDetentore":null}' , ( SELECT id from A4GD_INTERVENTO WHERE codice_agea = 310 )); 
INSERT INTO A4GT_ALLEVAMENTO_IMPEGNATO ( ID , VERSIONE , ID_DOMANDA , CODICE_SPECIE , DATI_ALLEVAMENTO , DATI_PROPRIETARIO , DATI_DETENTORE , ID_INTERVENTO )
VALUES ( 8672162 , 0 , 8661346 , 'BOVINI' , '{"idAllevamento":17343,"codiceAllevamento":"156TN916","codiceAllevamentoBdn":"5246813","descrizioneAllevamento":"ZOTTELE MOIRA","comune":null,"indirizzo":null}' , '{"codFiscaleProprietario":null,"denominazioneProprietario":null}' , '{"codFiscaleDetentore":null,"denominazioneDetentore":null}' , ( SELECT id from A4GD_INTERVENTO WHERE codice_agea = 313 )); 
INSERT INTO A4GT_ALLEVAMENTO_IMPEGNATO ( ID , VERSIONE , ID_DOMANDA , CODICE_SPECIE , DATI_ALLEVAMENTO , DATI_PROPRIETARIO , DATI_DETENTORE , ID_INTERVENTO ) 
VALUES ( 8672163 , 0 , 8661346 , 'BOVINI' , '{"idAllevamento":17343,"codiceAllevamento":"156TN916","codiceAllevamentoBdn":"5246813","descrizioneAllevamento":"ZOTTELE MOIRA","comune":null,"indirizzo":null}' , '{"codFiscaleProprietario":null,"denominazioneProprietario":null}' , '{"codFiscaleDetentore":null,"denominazioneDetentore":null}' , ( SELECT id from A4GD_INTERVENTO WHERE codice_agea = 311 ));
--Insert Esito
INSERT INTO A4GT_ESITO_CALCOLO_CAPO ( ID , VERSIONE , ID_ALLEVAM_DU , CAPO_ID , CODICE_CAPO , ESITO , MESSAGGIO , ID_TRANSIZIONE , DUPLICATO , CONTROLLO_NON_SUPERATO , RICHIESTO ) 
VALUES ( 12020308 , 1 , 8672161 , 104346819 , 'IT022990169212' , 'NON_AMMISSIBILE' , 'Il capo non è ammesso in quanto l’azienda non ha alcuna consegna o vendita di latte durante l’anno di campagna (2019)' , 12020314 , NULL , NULL , NULL );
INSERT INTO A4GT_ESITO_CALCOLO_CAPO ( ID , VERSIONE , ID_ALLEVAM_DU , CAPO_ID , CODICE_CAPO , ESITO , MESSAGGIO , ID_TRANSIZIONE , DUPLICATO , CONTROLLO_NON_SUPERATO , RICHIESTO ) 
VALUES ( 12020310 , 1 , 8672163 , 104346819 , 'IT022990169212' , 'NON_AMMISSIBILE' , 'Il capo non è ammesso in quanto l’azienda non ha alcuna consegna o vendita di latte durante l’anno di campagna (2019)' , 12020314 , NULL , NULL , NULL ); 
INSERT INTO A4GT_ESITO_CALCOLO_CAPO ( ID , VERSIONE , ID_ALLEVAM_DU , CAPO_ID , CODICE_CAPO , ESITO , MESSAGGIO , ID_TRANSIZIONE , DUPLICATO , CONTROLLO_NON_SUPERATO , RICHIESTO ) 
VALUES ( 12020312 , 1 , 8672162 , 104346819 , 'IT022990169212' , 'AMMISSIBILE' , NULL , 12020314 , NULL , NULL , NULL ); 
commit;
