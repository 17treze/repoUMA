INSERT INTO A4GD_TIPO_CONDUZIONE VALUES (0, 0, 'TEST', 'TEST');

INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (0, 0, 0, 'STTEST', 'TEST SOTTOTIPO');
INSERT INTO A4GD_SOTTOTIPO_CONDUZIONE VALUES (1, 0, 0, 'STTEST2', 'TEST SOTTOTIPO 2');

INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (0, 0, 'DOC1', 'TEST DOCUMENTO 1');
INSERT INTO A4GD_DOCUMENTO_CONDUZIONE VALUES (1, 0, 'DOC2', 'TEST DOCUMENTO 2');

INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (0, 0, 0, 0, 'P', 1);
INSERT INTO A4GR_SOTTOTIPO_DOCUMENTO VALUES (1, 0, 0, 1, 'S', 1);

INSERT INTO A4GR_DOCUMENTO_DIPENDENZA VALUES (0, 0, 0, 1);
COMMIT;
