INSERT INTO A4GT_FASCICOLO
	(ID, ID_VALIDAZIONE, VERSIONE, CUAA) 
VALUES 
	(1, 0, 0, 'PDRTTR69M30C794R');
	
INSERT INTO A4GD_TIPOLOGIA
	(ID, VERSIONE, DESCRIZIONE, DATA_INIZIO, DATA_FINE, AMBITO)
VALUES
	(894, 0, 'ALTRE_ATTREZZATURE', TIMESTAMP '2021-12-13 00:00:00.000000', NULL, 'MACCHINE');
	
INSERT INTO a4gd_classe_funzionale 
VALUES 

(9999, 0, 'FORZA MOTRICE', 894 , (SELECT CURRENT_DATE FROM DUAL), NULL);
	

INSERT INTO A4GD_SOTTOTIPO
	(ID, VERSIONE, DESCRIZIONE, ID_TIPOLOGIA, DATA_INIZIO, DATA_FINE, ID_CLASSE_FUNZIONALE)
VALUES
	(874, 0, 'ESSICCATORE', 894 , TIMESTAMP '2021-12-13 00:00:00.000000', NULL, 9999);
	
INSERT INTO A4GD_MAPPING_AGS
    (ID, ID_SOTTOTIPO, SCO_CLASSE, SCO_SOTTO_CLASSE)
VALUES
    (780, 874, '000008', '000056');

INSERT INTO A4GT_MACCHINA
	(ID, VERSIONE, ID_VALIDAZIONE, ID_FASCICOLO, ID_VALIDAZIONE_FASCICOLO, ID_SOTTOTIPO, MARCA, MODELLO, NUMERO_MATRICOLA, NUMERO_TELAIO, ANNO_DI_COSTRUZIONE, TIPO_POSSESSO, DOCUMENTO_POSSESSO, TARGA, DATA_IMMATRICOLAZIONE)
VALUES
	(990, 0, 0, 1, 0, 874, 'marca', 'modello', 'matricola', 'telaio', 2021, 'COMODATO', '00', 'targa', null);

INSERT INTO A4GT_MACCHINA_MOTORIZZATA
	(ID, ID_VALIDAZIONE, VERSIONE, MARCA_MOTORE, TIPO_MOTORE, ALIMENTAZIONE, POTENZA, MATRICOLA)
VALUES
	(990, 0, 0, 'marca', 'tipo', 'BENZINA', '1', 'matricola');
	
INSERT INTO A4GT_MACCHINA
	(ID, VERSIONE, ID_VALIDAZIONE, ID_FASCICOLO, ID_VALIDAZIONE_FASCICOLO, ID_SOTTOTIPO, MARCA, MODELLO, NUMERO_MATRICOLA, NUMERO_TELAIO, ANNO_DI_COSTRUZIONE, TIPO_POSSESSO, DOCUMENTO_POSSESSO, TARGA, DATA_IMMATRICOLAZIONE)
VALUES
	(991, 0, 0, 1, 0, 874, 'marca', 'modello', 'matricola', 'telaio', 2021, 'COMODATO', '00', 'targa', null);
	
INSERT INTO A4GT_MACCHINA_MOTORIZZATA
	(ID, ID_VALIDAZIONE, VERSIONE, MARCA_MOTORE, TIPO_MOTORE, ALIMENTAZIONE, POTENZA, MATRICOLA)
VALUES
	(991, 0, 0, 'marca', 'tipo', 'BENZINA', '1', 'matricola');

INSERT INTO A4GT_MACCHINA
	(ID, VERSIONE, ID_VALIDAZIONE, ID_FASCICOLO, ID_VALIDAZIONE_FASCICOLO, ID_SOTTOTIPO, MARCA, MODELLO, NUMERO_MATRICOLA, NUMERO_TELAIO, ANNO_DI_COSTRUZIONE, TIPO_POSSESSO, DOCUMENTO_POSSESSO, TARGA, DATA_IMMATRICOLAZIONE)
VALUES
	(992, 0, 0, 1, 0, 874, 'marca', 'modello', 'matricola', 'telaio', 2021, 'COMODATO', '00', 'targa', null);

INSERT INTO A4GT_MACCHINA_MOTORIZZATA
	(ID, ID_VALIDAZIONE, VERSIONE, MARCA_MOTORE, TIPO_MOTORE, ALIMENTAZIONE, POTENZA, MATRICOLA)
VALUES
	(992, 0, 0, 'marca', 'tipo', 'BENZINA', '1', 'matricola');
	
INSERT INTO A4GT_MACCHINA
	(ID, VERSIONE, ID_VALIDAZIONE, ID_FASCICOLO, ID_VALIDAZIONE_FASCICOLO, ID_SOTTOTIPO, MARCA, MODELLO, NUMERO_MATRICOLA, NUMERO_TELAIO, ANNO_DI_COSTRUZIONE, TIPO_POSSESSO, DOCUMENTO_POSSESSO, TARGA, DATA_IMMATRICOLAZIONE)
VALUES
	(999, 0, 0, 1, 0, 874, 'marca', 'modello', 'matricola new', 'telaio new', 2021, 'COMODATO', '00', 'targa new', null);

INSERT INTO A4GT_MACCHINA_MOTORIZZATA
	(ID, ID_VALIDAZIONE, VERSIONE, MARCA_MOTORE, TIPO_MOTORE, ALIMENTAZIONE, POTENZA, MATRICOLA)
VALUES
	(999, 0, 0, 'marca', 'tipo', 'BENZINA', '1', 'matricola');
	
