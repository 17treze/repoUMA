-- cuaa 02507510226

DELETE FROM A4GT_PASSO_TRANSIZIONE where ID_TRANSIZ_SOSTEGNO in (select id FROM A4GT_TRANSIZIONE_ISTRUTTORIA where ID_ISTRUTTORIA in (SELECT ID FROM A4GT_ISTRUTTORIA WHERE ID_DOMANDA in (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = '02507510226')));
delete FROM A4GT_TRANSIZIONE_ISTRUTTORIA where ID_ISTRUTTORIA in (SELECT ID FROM A4GT_ISTRUTTORIA WHERE ID_DOMANDA in (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = '02507510226'));

delete FROM A4GT_ISTRUTTORIA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = '02507510226');
delete FROM A4GT_DATI_LAVORAZIONE where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = '02507510226');
delete FROM A4GT_ELENCO_LIQUIDAZIONE where COD_ELENCO = '009-801-20190806-8517929';
delete FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = '02507510226';

COMMIT;