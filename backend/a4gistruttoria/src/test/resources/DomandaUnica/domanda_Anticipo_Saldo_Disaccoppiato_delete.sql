delete from A4GT_ANOM_DOMANDA_SOSTEGNO where ID_PASSO_LAVORAZIONE in (SELECT ID FROM A4GT_PASSO_TRANSIZIONE
where id_transiz_sostegno in (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts where ts.id_istruttoria in (564564564, 564564554))
);
delete from A4GT_PASSO_TRANSIZIONE where id_transiz_sostegno in (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts where ts.id_istruttoria  in (564564564, 564564554));
delete from A4GT_TRANSIZIONE_ISTRUTTORIA where id in (SELECT ts.id FROM A4GT_TRANSIZIONE_ISTRUTTORIA ts where ts.id_istruttoria  in (564564564, 564564554));
delete from A4GT_ISTRUTTORIA where id in (564564564, 564564554);
delete from a4gt_domanda where numero_domanda = 181662;
delete from A4GT_CONF_ISTRUTTORIA where id = 10;

COMMIT;
