--Fascicolo
INSERT INTO A4GT_FASCICOLO (ID, VERSIONE, ID_VALIDAZIONE, CUAA, DT_AGGIORNAMENTO_FONTI_ESTERNE) VALUES(155, 0, 0, '01720090222', NULL);

INSERT INTO A4GT_MACCHINA (ID, VERSIONE, ID_VALIDAZIONE, ID_FASCICOLO, ID_VALIDAZIONE_FASCICOLO, ID_SOTTOTIPO, MARCA, MODELLO, NUMERO_MATRICOLA, NUMERO_TELAIO, ANNO_DI_COSTRUZIONE, TIPO_POSSESSO, DOCUMENTO_POSSESSO, TARGA, DATA_IMMATRICOLAZIONE) VALUES
(990, 0, 0, 155, 0, (SELECT id FROM a4gd_sottotipo WHERE descrizione = 'GRUPPO ELETTROGENO'), 'marca', 'modello', 'matricola', 'telaio', 2021, 'COMODATO', '00', 'targa', null);

INSERT INTO A4GT_MACCHINA_MOTORIZZATA (ID, ID_VALIDAZIONE, VERSIONE, MARCA_MOTORE, TIPO_MOTORE, ALIMENTAZIONE, POTENZA, MATRICOLA) VALUES
	(990, 0, 0, 'marca', 'tipo', 'BENZINA', '1', 'matricola');

-- altro fascicolo	
INSERT INTO A4GT_FASCICOLO (ID, VERSIONE, ID_VALIDAZIONE, CUAA, DT_AGGIORNAMENTO_FONTI_ESTERNE) VALUES(154, 0, 0, '01720090111', NULL);

INSERT INTO A4GT_MACCHINA (ID, VERSIONE, ID_VALIDAZIONE, ID_FASCICOLO, ID_VALIDAZIONE_FASCICOLO, ID_SOTTOTIPO, MARCA, MODELLO, NUMERO_MATRICOLA, NUMERO_TELAIO, ANNO_DI_COSTRUZIONE, TIPO_POSSESSO, DOCUMENTO_POSSESSO, TARGA, DATA_IMMATRICOLAZIONE) VALUES
(980, 0, 0, 154, 0, (SELECT id FROM a4gd_sottotipo WHERE descrizione = 'GRUPPO ELETTROGENO'), NULL, 'modello', 'matricola', 'telaio', 2021, 'COMODATO', NULL, 'targa', null);

INSERT INTO A4GT_MACCHINA_MOTORIZZATA (ID, ID_VALIDAZIONE, VERSIONE, MARCA_MOTORE, TIPO_MOTORE, ALIMENTAZIONE, POTENZA, MATRICOLA) VALUES
	(980, 0, 0, 'marca', 'tipo', 'BENZINA', '1', 'matricola');


INSERT INTO A4GT_FABBRICATO (ID, VERSIONE, ID_VALIDAZIONE, ID_FASCICOLO, ID_VALIDAZIONE_FASCICOLO, ID_SOTTOTIPO, DENOMINAZIONE, INDIRIZZO, COMUNE, VOLUME, SUPERFICIE, DESCRIZIONE) VALUES(22, 0, 0, 154, 0, (SELECT id FROM a4gd_sottotipo WHERE descrizione = 'FIENILE'), 'asd', 'qw21', 'qw', 2, 4, NULL);


COMMIT;