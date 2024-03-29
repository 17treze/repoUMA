delete from A4GT_RICHIESTA_SUPERFICIE where id_domanda in (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 181666);
delete from A4GT_DATI_PARTICELLA_COLTURA where id_domanda in (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 181666);
delete from A4GT_DATI_FILTRO_DOMANDA where id_domanda in (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 181666);
delete from A4GT_ANOM_DOMANDA_SOSTEGNO where ID_PASSO_LAVORAZIONE in (SELECT ID FROM A4GT_PASSO_TRANSIZIONE
                                            where id_transiz_sostegno in (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts where ts.id_istruttoria = 56456456478));

delete from A4GT_PASSO_TRANSIZIONE where id_transiz_sostegno in (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts where ts.id_istruttoria = 56456456478);
delete from A4GT_TRANSIZIONE_ISTRUTTORIA where id in (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts where ts.id_istruttoria = 56456456478);
delete from A4GT_ISTRUTTORIA where id = 56456456478;
delete from A4GT_DICHIARAZIONE_DU where id_domanda in (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 181666);
delete from A4GT_DATI_LAVORAZIONE where id_domanda in (SELECT ID FROM A4GT_DOMANDA WHERE NUMERO_DOMANDA = 181666);
delete from a4gt_domanda where numero_domanda = 181666;

COMMIT;
