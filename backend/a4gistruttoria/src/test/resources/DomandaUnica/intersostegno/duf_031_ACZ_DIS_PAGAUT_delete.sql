-- cuaa 02413290228X

delete from A4GT_CONF_ISTRUTTORIA where anno_campagna = 2019;

delete from A4GT_ANOM_DOMANDA_SOSTEGNO where ID_PASSO_LAVORAZIONE in (
	select id FROM A4GT_PASSO_TRANSIZIONE where ID_TRANSIZ_SOSTEGNO in (
	select id FROM A4GT_TRANSIZIONE_ISTRUTTORIA where id_istruttoria in	(
		select id FROM A4GT_ISTRUTTORIA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = '02413290228X')
	)
)
);

DELETE FROM A4GT_PASSO_TRANSIZIONE where ID_TRANSIZ_SOSTEGNO in (select id FROM A4GT_TRANSIZIONE_ISTRUTTORIA where ID_ISTRUTTORIA in (SELECT ID FROM A4GT_ISTRUTTORIA WHERE ID_DOMANDA = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = '02413290228X')));
delete FROM A4GT_TRANSIZIONE_ISTRUTTORIA where ID_ISTRUTTORIA in (SELECT ID FROM A4GT_ISTRUTTORIA WHERE ID_DOMANDA = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = '02413290228X'));
delete FROM A4GT_ISTRUTTORIA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = '02413290228X');
delete FROM A4GT_DATI_LAVORAZIONE where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = '02413290228X');
delete FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = '02413290228X';
delete from A4GT_DATI_SETTORE WHERE ID = 6;

COMMIT;