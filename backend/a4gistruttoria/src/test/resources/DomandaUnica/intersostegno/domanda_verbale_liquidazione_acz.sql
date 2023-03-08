-- Istruttoria domanda 300000
INSERT INTO A4GT_ISTRUTTORIA 
	(ID,VERSIONE,
	ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE,TIPOLOGIA)
VALUES 
	(nxtnbr.nextval,0,
	(SELECT ID FROM a4gt_domanda WHERE numero_domanda = 300000),
	'ZOOTECNIA',
	(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'PAGAMENTO_AUTORIZZATO'),
	'SALDO');

INSERT INTO A4GT_TRANSIZIONE_ISTRUTTORIA 
	(ID,versione,
	id_istruttoria,
	id_stato_iniziale,
	id_stato_finale,
	data_esecuzione) 
VALUES (
	nxtnbr.nextval,0,
	nxtnbr.currval-1,
	(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'LIQUIDABILE'),
	(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'CONTROLLI_INTERSOSTEGNO_OK'),
	TO_DATE('06/14/2018 18:56:48', 'MM/DD/YYYY HH24:MI:SS'));
        
INSERT INTO A4GT_PASSO_TRANSIZIONE
	(ID,versione,
	id_transiz_sostegno,
	codice_passo,esito,codice_esito,dati_input,dati_output,dati_sintesi_lavorazione)
VALUES 
	(nxtnbr.nextval,0,
	nxtnbr.currval-1,
	'DISCIPLINA_FINANZIARIA','OK','no esito',NULL,'{"variabiliCalcolo":[{"tipoVariabile":"DFFRPAGACZ","valNumber":1084.38},{"tipoVariabile":"DFIMPRIDACZ","valNumber":0.00},{"tipoVariabile":"DFIMPLIQACZ","valNumber":123.12},{"tipoVariabile":"DFFRPAGACZ_310","valNumber":707.15},{"tipoVariabile":"DFFRPAGACZ_311","valNumber":456},{"tipoVariabile":"DFFRPAGACZ_313","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_322","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_315","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_316","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_318","valNumber":789.58},{"tipoVariabile":"DFFRPAGACZ_320","valNumber":0.00},{"tipoVariabile":"DFFRPAGACZ_321","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_310","valNumber":855.47},{"tipoVariabile":"DFIMPDFDISACZ_311","valNumber":707.15},{"tipoVariabile":"DFIMPDFDISACZ_313","valNumber":377.14},{"tipoVariabile":"DFIMPDFDISACZ_322","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_315","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_316","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_318","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_320","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_321","valNumber":0.00}]}',NULL);


-- Istruttoria domanda 300002
INSERT INTO A4GT_ISTRUTTORIA 
	(ID,VERSIONE,
	ID_DOMANDA,SOSTEGNO,ID_STATO_LAVORAZIONE,TIPOLOGIA)
VALUES 
	(nxtnbr.nextval,0,
	(SELECT ID FROM a4gt_domanda WHERE numero_domanda = 300002),
	'ZOOTECNIA',
	(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'PAGAMENTO_AUTORIZZATO'), 
	'SALDO');

INSERT INTO A4GT_TRANSIZIONE_ISTRUTTORIA 
	(ID,versione,
	id_istruttoria,
	id_stato_iniziale,
	id_stato_finale,
	data_esecuzione) 
VALUES (
	nxtnbr.nextval,0,
	nxtnbr.currval-1,
	(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'LIQUIDABILE'),
	(SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'CONTROLLI_INTERSOSTEGNO_OK'),
	TO_DATE('06/14/2018 18:56:48', 'MM/DD/YYYY HH24:MI:SS'));
        
INSERT INTO A4GT_PASSO_TRANSIZIONE
	(ID,versione,
	id_transiz_sostegno,
	codice_passo,esito,codice_esito,dati_input,dati_output,dati_sintesi_lavorazione)
VALUES 
	(nxtnbr.nextval,0,
	nxtnbr.currval-1,
	'DISCIPLINA_FINANZIARIA','OK','no esito',NULL,'{"variabiliCalcolo":[{"tipoVariabile":"DFFRPAGACZ","valNumber":1526.35},{"tipoVariabile":"DFIMPRIDACZ","valNumber":25},{"tipoVariabile":"DFIMPLIQACZ","valNumber":129.25},{"tipoVariabile":"DFFRPAGACZ_310","valNumber":111.25},{"tipoVariabile":"DFFRPAGACZ_311","valNumber":456},{"tipoVariabile":"DFFRPAGACZ_313","valNumber":569.32},{"tipoVariabile":"DFFRPAGACZ_322","valNumber":123.45},{"tipoVariabile":"DFFRPAGACZ_315","valNumber":456.69},{"tipoVariabile":"DFFRPAGACZ_316","valNumber":789.58},{"tipoVariabile":"DFFRPAGACZ_318","valNumber":852.25},{"tipoVariabile":"DFFRPAGACZ_320","valNumber":36.25},{"tipoVariabile":"DFFRPAGACZ_321","valNumber":0},{"tipoVariabile":"DFIMPDFDISACZ_310","valNumber":958.42},{"tipoVariabile":"DFIMPDFDISACZ_311","valNumber":78.25},{"tipoVariabile":"DFIMPDFDISACZ_313","valNumber":753.35},{"tipoVariabile":"DFIMPDFDISACZ_322","valNumber":1},{"tipoVariabile":"DFIMPDFDISACZ_315","valNumber":9},{"tipoVariabile":"DFIMPDFDISACZ_316","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_318","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_320","valNumber":0.00},{"tipoVariabile":"DFIMPDFDISACZ_321","valNumber":0.00}]}',NULL);

-- Inserirsco i relativi elenchi di liquidazione
INSERT INTO a4gt_elenco_liquidazione
VALUES (
	9999999,
	0,
	'009-801-20190513-9999999',
	TO_DATE('06/14/2018 18:56:48', 'MM/DD/YYYY HH24:MI:SS'),
	'D100000100920180420180618009201800191185AZIENDA AGRICOLA CONTI BOSSI FEDRIGOTTI SOCIETA'' SEMPLICE             NA                                                NA                                                 00000000         00122280225     VIA UNIONE 43                                022161 TN38068010821020801IT90M0020300601500000002852588000261101825101000032770000000030000080102611018261010000327700000000020821802026110183010100003277000000000000008030261101832101000032770000000000000080000811118071010000327700000000000000801008111180810100003277000000000644388000131131821101000000000000000000000080101311318221010000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000      00000000000000000000000000000000                                                                                                                                                                009   0005542920                                                                                                                                                                                       ',
	FILE_READ('src\test\resources\DomandaUnica\intersostegno\stampa\output\verbaleLiquidazioneAcz.pdf'),
	'ELENCO_INVIATO_SOC');

-- INSERT INTO a4gr_domanda_elenco
-- VALUES (nxtnbr.nextval,0,(SELECT ID FROM a4gt_domanda WHERE numero_domanda = 300000),9999999);

-- INSERT INTO a4gr_domanda_elenco
-- VALUES (nxtnbr.nextval,0,(SELECT ID FROM a4gt_domanda WHERE numero_domanda = 300002),9999999);

update A4GT_ISTRUTTORIA set id_elenco_liquidazione = 9999999
where SOSTEGNO = 'ZOOTECNIA'
and id_domanda in (SELECT ID FROM a4gt_domanda WHERE numero_domanda in (300000, 300002))
and id_stato_lavorazione = (SELECT ID FROM a4gd_stato_lav_sostegno WHERE identificativo = 'PAGAMENTO_AUTORIZZATO');

COMMIT;