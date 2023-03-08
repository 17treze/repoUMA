--- Domanda acs LNGNTN64B03H7641 (era LNGNTN64B03H764H) - 1206747 (era 206747) - LONGO ANTONIO

delete from A4GT_ANOM_DOMANDA_SOSTEGNO where ID_PASSO_LAVORAZIONE in (
	select id FROM A4GT_PASSO_TRANSIZIONE where ID_TRANSIZ_SOSTEGNO in (
	select id FROM A4GT_TRANSIZIONE_ISTRUTTORIA where id_istruttoria in	(
		select id FROM A4GT_ISTRUTTORIA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'LNGNTN64B03H7641')
	)
)
);

DELETE FROM A4GT_PASSO_TRANSIZIONE where ID_TRANSIZ_SOSTEGNO in (
	select id FROM A4GT_TRANSIZIONE_ISTRUTTORIA where id_istruttoria in	(
		select id FROM A4GT_ISTRUTTORIA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'LNGNTN64B03H7641')
	)
);

delete FROM A4GT_TRANSIZIONE_ISTRUTTORIA where id_istruttoria in (
	select id FROM A4GT_ISTRUTTORIA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'LNGNTN64B03H7641')
);

delete from A4GT_ISTRUTTORE_SUPERFICIE where id_istruttoria in (
	select id FROM A4GT_ISTRUTTORIA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'LNGNTN64B03H7641')
);

delete FROM A4GT_ISTRUTTORIA where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'LNGNTN64B03H7641');

delete FROM A4GT_DATI_LAVORAZIONE where id_domanda = (SELECT ID FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'LNGNTN64B03H7641');

delete FROM A4GT_DOMANDA WHERE CUAA_INTESTATARIO = 'LNGNTN64B03H7641';

COMMIT;