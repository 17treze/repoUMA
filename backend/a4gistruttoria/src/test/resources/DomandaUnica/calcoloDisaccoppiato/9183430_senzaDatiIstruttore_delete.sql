DELETE FROM A4GT_TRANSIZIONE_ISTRUTTORIA WHERE ID = 86789;
DELETE FROM A4GT_ISTRUTTORIA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'BRGNGL64C30C7941');
DELETE FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'BRGNGL64C30C7941';

COMMIT;