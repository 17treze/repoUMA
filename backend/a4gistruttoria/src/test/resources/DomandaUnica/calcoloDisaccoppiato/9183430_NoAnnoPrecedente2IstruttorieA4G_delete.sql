DELETE FROM A4GT_PASSO_TRANSIZIONE where ID_TRANSIZ_SOSTEGNO in (select id FROM A4GT_TRANSIZIONE_ISTRUTTORIA where ID_ISTRUTTORIA in (SELECT ID FROM A4GT_ISTRUTTORIA WHERE ID_DOMANDA in (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'BRGNGL64C30C7941')));
delete FROM A4GT_TRANSIZIONE_ISTRUTTORIA where ID_ISTRUTTORIA in (SELECT ID FROM A4GT_ISTRUTTORIA WHERE ID_DOMANDA in (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'BRGNGL64C30C7941'));
delete FROM A4GT_ISTRUTTORIA where id_domanda in (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'BRGNGL64C30C7941');
delete FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'BRGNGL64C30C7941';

COMMIT;