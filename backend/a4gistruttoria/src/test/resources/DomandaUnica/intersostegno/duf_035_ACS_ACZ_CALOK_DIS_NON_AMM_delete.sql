-- cuaa MSTFBA79L10H612L

delete from A4GT_ANOM_DOMANDA_SOSTEGNO where ID_PASSO_LAVORAZIONE in (
	select id FROM A4GT_PASSO_TRANSIZIONE where ID_TRANSIZ_SOSTEGNO in (
	select id FROM A4GT_TRANSIZIONE_ISTRUTTORIA where id_istruttoria in	(
		select id FROM A4GT_ISTRUTTORIA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'MSTFBA79L10H612L')
	)
)
);

DELETE FROM A4GT_PASSO_TRANSIZIONE where ID_TRANSIZ_SOSTEGNO in (
	select id FROM A4GT_TRANSIZIONE_ISTRUTTORIA where id_istruttoria in (
		select id from A4GT_ISTRUTTORIA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'MSTFBA79L10H612L')
	)
);

delete FROM A4GT_TRANSIZIONE_ISTRUTTORIA where id_istruttoria in (
	select id from A4GT_ISTRUTTORIA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'MSTFBA79L10H612L')
);

delete FROM A4GT_ISTRUTTORIA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'MSTFBA79L10H612L');

delete FROM A4GT_DATI_LAVORAZIONE where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'MSTFBA79L10H612L');

delete FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'MSTFBA79L10H612L';

COMMIT;